package adstech.vn.quotationbackoffice.service;

import java.util.List;

import adstech.vn.quotationbackoffice.model.Quotation;
import adstech.vn.quotationbackoffice.model.QuotationUpdate;

public interface IQuotationService {
	
	public int createQuotation(Quotation quotation);
	
	public int createQuotation(QuotationUpdate quotation);
	
	public int submitQuotation(Quotation quotation, String url);
	
	public int submitQuotation(QuotationUpdate quotation, String url);
	
	public int approveQuotation(Long quotationId, String user);
	
	public int rejectQuotation(Quotation quotation, String url);
	
	public int submitQuotation(Long quotationId, String user, String url);
	
	public Quotation getQuotation(Long quotationId);
	
	public List<Quotation> getQuotationByCreater(String creater, Integer page, Integer pageSize);
	
	public List<Quotation> getQuotationByApprover(String approver, Integer page, Integer pageSize,List<String> emailApprove);

	public int updateQuotationLinkReport(Long quotationId, String reportLink );

	
}
