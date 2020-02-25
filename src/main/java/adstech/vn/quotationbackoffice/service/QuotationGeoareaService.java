package adstech.vn.quotationbackoffice.service;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import adstech.vn.quotationbackoffice.model.QuotationGeoarea;
import adstech.vn.quotationbackoffice.repository.QuotationGeoareaRepository;

@Service
public class QuotationGeoareaService implements IQuotationGeoareaService {
	@Autowired
	@Qualifier("dataWarehouseDatasource")
	DataSource dataSource;
	@Autowired
	QuotationGeoareaRepository quotationGeoareaRepository;

	@Override
	public List<QuotationGeoarea> getAll() {
		return quotationGeoareaRepository.getAll();

	}

	@Override
	public int create(List<QuotationGeoarea> quotationGeoareas) {
		
			for (QuotationGeoarea quotationGeoarea : quotationGeoareas) {
				quotationGeoareaRepository.create(quotationGeoarea);
			}
			return 1;
	}

	@Override
	public List<QuotationGeoarea> getByCriteriaId(String geoArea) {
		
		return quotationGeoareaRepository.getByCriteriaId(geoArea);
	}

}
