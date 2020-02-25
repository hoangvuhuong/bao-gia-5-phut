package adstech.vn.quotationbackoffice.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import adstech.vn.quotationbackoffice.model.QuotationEmailSuggest;
import adstech.vn.quotationbackoffice.repository.QuotationEmailSuggestRepository;
@Service
public class QuotationEmailSuggestService implements IQuotationEmailSuggestService{
	@Autowired
	QuotationEmailSuggestRepository quotationEmailSuggestRepository;

	@Override
	public List<QuotationEmailSuggest> getEmailSuggest(String type) {
		
		return quotationEmailSuggestRepository.getEmailSuggest(type);
	}

}
