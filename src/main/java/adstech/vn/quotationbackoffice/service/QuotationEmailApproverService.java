package adstech.vn.quotationbackoffice.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import adstech.vn.quotationbackoffice.model.QuotationEmailApprover;
import adstech.vn.quotationbackoffice.repository.QuotationEmailApproverRepository;
@Service
public class QuotationEmailApproverService implements IQuotationEmailApproverService{
	@Autowired
	QuotationEmailApproverRepository quotationEmailApproverRepository;

	@Override
	public List<QuotationEmailApprover> getEmailApprover(String type) {
		
		return quotationEmailApproverRepository.getEmailApprover(type);
	}
	public List<String> getListEmailApprover(String type){
		List<QuotationEmailApprover> EmailApprover=getEmailApprover(type);
		List<String> list = new ArrayList<String>();
		for (QuotationEmailApprover quotationEmailApprover : EmailApprover) {
			list.add(quotationEmailApprover.getEmail());
		}
		return list;
	}
}
