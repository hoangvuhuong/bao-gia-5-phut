package adstech.vn.quotationbackoffice.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import adstech.vn.quotationbackoffice.model.Quotation;
import adstech.vn.quotationbackoffice.model.QuotationAdsType;
import adstech.vn.quotationbackoffice.model.QuotationAdsTypeKeyword;
import adstech.vn.quotationbackoffice.model.QuotationKeyword;
import adstech.vn.quotationbackoffice.model.QuotationPlan;
import adstech.vn.quotationbackoffice.model.QuotationUpdate;
import adstech.vn.quotationbackoffice.repository.QuotationAdsTypeKeywordRepository;
import adstech.vn.quotationbackoffice.repository.QuotationAdsTypeRepository;
import adstech.vn.quotationbackoffice.repository.QuotationHistoryRepository;
import adstech.vn.quotationbackoffice.repository.QuotationKeywordRepository;
import adstech.vn.quotationbackoffice.repository.QuotationPlanRepository;
import adstech.vn.quotationbackoffice.repository.QuotationRepository;
import adstech.vn.quotationbackoffice.util.EmailSender;
import adstech.vn.quotationbackoffice.util.SlackUtil;
import adstech.vn.quotationbackoffice.util.StringUtil;
@Service
public class QuotationService implements IQuotationService {
	
	private DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
	
	@Autowired
	@Qualifier("dataWarehouseDatasource")
	DataSource dataSource;

	@Autowired
	QuotationRepository quotationRepository;

	@Autowired
	QuotationHistoryRepository quotationHistoryRepository;
	
	@Autowired
	QuotationPlanRepository quotationPlanRepository;
	
	@Autowired
	QuotationAdsTypeRepository quotationAdsTypeRepository;
	
	@Autowired
	QuotationKeywordRepository quotationKeywordRepository;
	
	@Autowired
	QuotationAdsTypeKeywordRepository quotationAdsTypeKeywordRepository;
	
	@Override
	public int createQuotation(QuotationUpdate quotation) {
		Map<String, Long> keywordId = new HashMap<String, Long>();
		PlatformTransactionManager txManager = new DataSourceTransactionManager(dataSource);
		TransactionStatus status = txManager.getTransaction(new DefaultTransactionDefinition());
		try {			
			if(quotation.getQuotationId()!=null) {
				//Neu la update
				if(!quotation.getKeyWordID().isEmpty()) {
				quotationKeywordRepository.delete(quotation.getKeyWordID());
				}
				quotationPlanRepository.delete(quotation.getPlanID());
				quotationAdsTypeRepository.delete(quotation.getTypeAds());
				quotationAdsTypeKeywordRepository.delete(quotation.getTypeAds());
			}//end neu update
			
			Long quotationId = quotationRepository.create(quotation);
			quotationHistoryRepository.addHistory(quotationId, "SAVE", null, quotation.getCreatedBy());
			//Create Quotation Keyword
			List<QuotationKeyword>quotationKeyword=quotation.getKeywords();
			List<QuotationKeyword>quotationKeywordRemoveAccent=new ArrayList<QuotationKeyword>();
			for(QuotationKeyword keyWord:quotationKeyword) {
				QuotationKeyword temp = new QuotationKeyword();
				temp.setKeyword(StringUtil.removeAccent(keyWord.getKeyword()));
				quotationKeywordRemoveAccent.add(temp);
			}
			//copy keyword vao mang trung gian
			for(int i=0;i<quotationKeywordRemoveAccent.size();i++) {
				quotationKeyword.get(i).setQuotationId(quotationId);
				quotationKeyword.get(i).setIsIncurredKeyword("NO");
				for( int j=0; j<quotationKeywordRemoveAccent.size();j++) {
					if((i!=j) &&quotationKeywordRemoveAccent.get(i).getKeyword().equals(quotationKeywordRemoveAccent.get(j).getKeyword())&& quotationKeywordRemoveAccent.get(i).getKeyword().equals(quotationKeyword.get(i).getKeyword())) {
						quotationKeyword.get(i).setIsIncurredKeyword("YES");
						break;
					} 
				}
				Long kwId=quotationKeywordRepository.create(quotationKeyword.get(i));
				keywordId.put(quotationKeyword.get(i).getKeyword(), kwId);
			}
			
			//end create Keyword
			//tao ke hoach
			List<QuotationPlan> plans=quotation.getPlan();
//			List<QuotationKeyword> quotationKeyWord=quotation.getKeywords();
			Long quotationPlanId;
			List<QuotationAdsType> adsTypes;
			for(QuotationPlan plan:plans) {
				plan.setQuotation_id(quotationId);
				
				quotationPlanId=quotationPlanRepository.create(plan);
				adsTypes=plan.getType();
				for(QuotationAdsType adsType:adsTypes) {
					adsType.setQuotationPlanId(quotationPlanId);
					Long adsTypeId=quotationAdsTypeRepository.create(adsType);
					if(adsType.getType().equals("Google Search")) {
						//neu la search nen co tu khoa
						for(QuotationAdsTypeKeyword quotationAdstypeKeyword : adsType.getKeywords()) {
							quotationAdstypeKeyword.setAdsTypeId(adsTypeId);
							quotationAdstypeKeyword.setQuotationKeywordId(keywordId.get(quotationAdstypeKeyword.getKeyword()));//lay id keyword duoc tao
							quotationAdsTypeKeywordRepository.create(quotationAdstypeKeyword);
						}//end loop keyword noi voi adstype
					}
				}//end loop adstype
			}//end loop plan
			
			txManager.commit(status);
			return 1;
		} catch (Exception e) {
			e.printStackTrace();
			txManager.rollback(status);
			return 0;
		}
	}

	@Override
	public int approveQuotation(Long quotationId, String user) {
		PlatformTransactionManager txManager = new DataSourceTransactionManager(dataSource);
		TransactionStatus status = txManager.getTransaction(new DefaultTransactionDefinition());
		try {
			quotationRepository.approve(quotationId);
			quotationHistoryRepository.addHistory(quotationId, "APPROVE", null, user);
			txManager.commit(status);

			Quotation q = quotationRepository.getQuotation(quotationId);
			EmailSender.getInstance().sendEmailNotifyApproval(q.getCreatedBy(), q.getApprover(), q.getPartnerName());
			SlackUtil.sendDirectMsg(q.getCreatedBy(), "Báo giá của bạn đã được duyệt. Bạn có thể gửi cho khách hàng ngay bây giờ!");
			return 1;
		} catch (Exception e) {
			e.printStackTrace();
			txManager.rollback(status);
			return 0;
		}
	}

	@Override
	public int rejectQuotation(Quotation quotation, String url) {
		PlatformTransactionManager txManager = new DataSourceTransactionManager(dataSource);
		TransactionStatus status = txManager.getTransaction(new DefaultTransactionDefinition());
		try {
			quotationRepository.reject(quotation.getQuotationId(), quotation.getNote());
			quotationHistoryRepository.addHistory(quotation.getQuotationId(), "REJECT", null, quotation.getUpdatedBy());
			txManager.commit(status);
			
			Quotation q = quotationRepository.getQuotation(quotation.getQuotationId());
			EmailSender.getInstance().sendEmailNotifyRejection(q.getCreatedBy(), q.getApprover(), q.getPartnerName(), url);
			SlackUtil.sendDirectMsg(q.getCreatedBy(), "Báo giá của bạn bị từ chối. Truy cập " + url + " để chỉnh sửa!");
			return 1;
		} catch (Exception e) {
			txManager.rollback(status);
			e.printStackTrace();
			return 0;
		}
	}

	@Override
	public int submitQuotation(Long quotationId, String user, String url) {
		PlatformTransactionManager txManager = new DataSourceTransactionManager(dataSource);
		TransactionStatus status = txManager.getTransaction(new DefaultTransactionDefinition());
		try {
			quotationRepository.submit(quotationId, user);
			Quotation q = quotationRepository.getQuotation(quotationId);
			String action = "RE-SUBMIT";
			if(q == null || q.getStatus() == "PENDING") {
				action = "SUBMIT";
			}
			quotationHistoryRepository.addHistory(quotationId, action, null, user);
			txManager.commit(status);
			EmailSender.getInstance().sendEmailRequestApproval(q.getApprover(), q.getApprover(), q.getCreatedBy(), q.getPartnerName(), 
					q.getPartnerWebsite(), df.format(q.getDueDate()), url);
			SlackUtil.sendDirectMsg(q.getApprover(), "Bạn có một yêu cầu duyệt báo giá mới. Truy cập " + url + " để duyệt!");
			return 1;
		} catch (Exception e) {
			e.printStackTrace();
			txManager.rollback(status);
			return 0;
		}
	}

	@Override
	public List<Quotation> getQuotationByCreater(String creater, Integer page, Integer pageSize) {
		try {
			return quotationRepository.getByCreateUser(creater, page, pageSize);
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public List<Quotation> getQuotationByApprover(String approver, Integer page, Integer pageSize,List<String> emailApprove) {
		try {
			return quotationRepository.getByApproveUser(approver, page, pageSize,emailApprove);
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public Quotation getQuotation(Long quotationId) {
		try {
			return quotationRepository.getQuotation(quotationId);
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public int submitQuotation(QuotationUpdate quotation, String url) {
		PlatformTransactionManager txManager = new DataSourceTransactionManager(dataSource);
		TransactionStatus status = txManager.getTransaction(new DefaultTransactionDefinition());
		Map<String, Long> keywordId = new HashMap<String, Long>();
		try {
			if(quotation.getQuotationId()!=null) {
				if(!quotation.getKeyWordID().isEmpty()) {
				quotationKeywordRepository.delete(quotation.getKeyWordID());
				}
				quotationPlanRepository.delete(quotation.getPlanID());
				quotationAdsTypeRepository.delete(quotation.getTypeAds());
				quotationAdsTypeKeywordRepository.delete(quotation.getTypeAds());
				//update thi xoa het cac phan keyword va tao moi lai
			}
			Long quotationId = quotationRepository.submit(quotation);
			quotationHistoryRepository.addHistory(quotationId, "SUBMIT", null, quotation.getCreatedBy());
			//Create Quotation Keyword
			List<QuotationKeyword>quotationKeyword=quotation.getKeywords();
			List<QuotationKeyword>quotationKeywordRemoveAccent=new ArrayList<QuotationKeyword>();
			for(QuotationKeyword keyWord:quotationKeyword) {
				QuotationKeyword temp = new QuotationKeyword();
				temp.setKeyword(StringUtil.removeAccent(keyWord.getKeyword()));
				quotationKeywordRemoveAccent.add(temp);
			}
			//copy keyword vao mang trung gian
			for(int i=0;i<quotationKeywordRemoveAccent.size();i++) {
				quotationKeyword.get(i).setQuotationId(quotationId);
				quotationKeyword.get(i).setIsIncurredKeyword("NO");
				for( int j=0; j<quotationKeywordRemoveAccent.size();j++) {
					if((i!=j) &&quotationKeywordRemoveAccent.get(i).getKeyword().equals(quotationKeywordRemoveAccent.get(j).getKeyword())&& quotationKeywordRemoveAccent.get(i).getKeyword().equals(quotationKeyword.get(i).getKeyword())) {
						quotationKeyword.get(i).setIsIncurredKeyword("YES");
						break;
					} 
				}
				Long kwId=quotationKeywordRepository.create(quotationKeyword.get(i));
				keywordId.put(quotationKeyword.get(i).getKeyword(), kwId);
			}
			
			//end create Keyword
			List<QuotationPlan> plans=quotation.getPlan();
//			List<QuotationKeyword> quotationKeyWord=quotation.getKeywords();
			Long quotationPlanId;
			List<QuotationAdsType> adsTypes;
			for(QuotationPlan plan:plans) {
				plan.setQuotation_id(quotationId);
				
				quotationPlanId=quotationPlanRepository.create(plan);
				adsTypes=plan.getType();
				for(QuotationAdsType adsType:adsTypes) {
					adsType.setQuotationPlanId(quotationPlanId);
					Long adsTypeId=quotationAdsTypeRepository.create(adsType);
					if(adsType.getType().equals("Google Search")) {
						//neu la search nen co tu khoa
						for(QuotationAdsTypeKeyword quotationAdstypeKeyword : adsType.getKeywords()) {
							quotationAdstypeKeyword.setAdsTypeId(adsTypeId);
							quotationAdstypeKeyword.setQuotationKeywordId(keywordId.get(quotationAdstypeKeyword.getKeyword()));//lay id keyword duoc tao
							quotationAdsTypeKeywordRepository.create(quotationAdstypeKeyword);
						}//end loop keyword noi voi adstype
					}
				}//end loop adstype
			}
			
			txManager.commit(status);

			Quotation q = quotationRepository.getQuotation(quotationId);
			EmailSender.getInstance().sendEmailRequestApproval(q.getApprover(), q.getApprover(), q.getCreatedBy(), q.getPartnerName(), 
					q.getPartnerWebsite(), df.format(q.getDueDate()), url);
			SlackUtil.sendDirectMsg(q.getApprover(), "Bạn có một yêu cầu duyệt báo giá mới. Truy cập " + url + " để duyệt!");
			return 1;
		} catch (Exception e) {
			e.printStackTrace();
			txManager.rollback(status);
			return 0;
		}
	}

	@Override
	public int createQuotation(Quotation quotation) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int submitQuotation(Quotation quotation, String url) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int updateQuotationLinkReport(Long quotationId, String reportLink) {
		
		return quotationRepository.updateQuotationLinkReport(quotationId, reportLink);
	}
	

}
