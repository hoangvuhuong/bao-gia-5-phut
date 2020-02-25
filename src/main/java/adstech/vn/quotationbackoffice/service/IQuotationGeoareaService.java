package adstech.vn.quotationbackoffice.service;

import java.util.List;

import adstech.vn.quotationbackoffice.model.QuotationGeoarea;

public interface IQuotationGeoareaService {
	public List<QuotationGeoarea> getAll();
	public int create(List<QuotationGeoarea> quotationGeoarea);
	public List<QuotationGeoarea> getByCriteriaId(String geoArea);
}
