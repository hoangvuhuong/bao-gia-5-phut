package adstech.vn.quotationbackoffice.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import adstech.vn.quotationbackoffice.model.DateImport;
import adstech.vn.quotationbackoffice.repository.DateImportRepository;

@Service
public class DateImportService {
	@Autowired
	DateImportRepository dateImportReposiroty;
	
	
	public int getCount(LocalDate date) {
		DateImport datIp = dateImportReposiroty.getDateImport(date);
		return datIp.getCount();
	}
	public List<DateImport> getListDate(){
		return dateImportReposiroty.getListDateImport(LocalDate.now());
	}
}
