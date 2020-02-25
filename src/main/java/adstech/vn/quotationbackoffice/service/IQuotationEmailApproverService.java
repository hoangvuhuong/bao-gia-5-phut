package adstech.vn.quotationbackoffice.service;

import java.util.List;

import adstech.vn.quotationbackoffice.model.QuotationEmailApprover;

public interface IQuotationEmailApproverService {
	public List<QuotationEmailApprover> getEmailApprover(String type);
	public List<String> getListEmailApprover(String type);
}
