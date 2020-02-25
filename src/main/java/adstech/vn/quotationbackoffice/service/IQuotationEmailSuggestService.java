package adstech.vn.quotationbackoffice.service;

import java.util.List;

import adstech.vn.quotationbackoffice.model.QuotationEmailSuggest;

public interface IQuotationEmailSuggestService {
	public List<QuotationEmailSuggest> getEmailSuggest(String type);
	
}
