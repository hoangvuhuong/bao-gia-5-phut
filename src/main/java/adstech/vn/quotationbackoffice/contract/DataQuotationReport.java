package adstech.vn.quotationbackoffice.contract;

import java.util.ArrayList;
import java.util.List;

import adstech.vn.quotationbackoffice.model.Quotation;
import adstech.vn.quotationbackoffice.model.QuotationAdsTypeKeyword;
import adstech.vn.quotationbackoffice.model.QuotationPlan;
import adstech.vn.quotationbackoffice.pojo.KeyWordPagination;
import adstech.vn.quotationbackoffice.util.CommonConstant;

public class DataQuotationReport {

	private Long quotationId;
	private String inChargeUser;
	private String inChargeUsername;
	private String inChargeUserPhone;
	private Quotation quotation;
	private List<KeyWordPagination> pagination;
	private List<List<List<QuotationAdsTypeKeyword>>> listKeywordPagination;
	
	public List<List<List<QuotationAdsTypeKeyword>>> getListKeywordPagination() {
		return listKeywordPagination;
	}
	public void setListKeywordPagination(List<List<List<QuotationAdsTypeKeyword>>> listKeywordPagination) {
		this.listKeywordPagination = listKeywordPagination;
	}
	public List<KeyWordPagination> getPagination() {
		return pagination;
	}
	public void setPagination(List<KeyWordPagination> pagination) {
		this.pagination = pagination;
	}
	public Long getQuotationId() {
		return quotationId;
	}
	public void setQuotationId(Long quotationId) {
		this.quotationId = quotationId;
	}
	public Quotation getQuotation() {
		return quotation;
	}
	public void setQuotation(Quotation quotation) {
		this.quotation = quotation;
	}
	public String getInChargeUser() {
		return inChargeUser;
	}
	public void setInChargeUser(String inChargeUser) {
		this.inChargeUser = inChargeUser;
	}
	public String getInChargeUsername() {
		return inChargeUsername;
	}
	public void setInChargeUsername(String inChargeUsername) {
		this.inChargeUsername = inChargeUsername;
	}
	public String getInChargeUserPhone() {
		return inChargeUserPhone;
	}
	public void setInChargeUserPhone(String inChargeUserPhone) {
		this.inChargeUserPhone = inChargeUserPhone;
	} 
	public void setPaginationKeyWord() {
		List<KeyWordPagination> pagination_ex=new ArrayList<KeyWordPagination>();
		for (QuotationPlan plan : quotation.getPlan()) {
			int sizePage=plan.getType().get(0).getKeywords().size();
			int page=(int) Math.ceil(sizePage / CommonConstant.KEYWORD_PERPAGE_PDF);
			pagination_ex.add(new KeyWordPagination(page, CommonConstant.KEYWORD_PERPAGE_PDF));
		}
		this.pagination=pagination_ex;
	}
	public void paginationKeyword() {
		List<List<List<QuotationAdsTypeKeyword>>> listKeywordPagination= new ArrayList<>();
		for (QuotationPlan plan : quotation.getPlan()) {
			List<QuotationAdsTypeKeyword> listKeyword=plan.getType().get(0).getKeywords();
			List<List<QuotationAdsTypeKeyword>> keywordPlan=new ArrayList<>();
			int page=(int) Math.ceil(listKeyword.size() / CommonConstant.KEYWORD_PERPAGE_PDF);
			for(int p=0;p<page;p++) {
				List<QuotationAdsTypeKeyword> keywordPerPage=new ArrayList<>();
				for(int i=0;i<CommonConstant.KEYWORD_PERPAGE_PDF;i++) {
					keywordPerPage.add(listKeyword.get(p*CommonConstant.KEYWORD_PERPAGE_PDF +i));
					//List1 tu khoa 1 trang
				}
				keywordPlan.add(keywordPerPage);
				//List 2 ke hoach
			}
			listKeywordPagination.add(keywordPlan);
			//List 3 toan bo
		}
		this.listKeywordPagination=listKeywordPagination;
	}
}
