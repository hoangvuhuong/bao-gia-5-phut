package adstech.vn.quotationbackoffice.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import adstech.vn.quotationbackoffice.model.QuotationEmailSuggest;

@Repository
public class QuotationEmailSuggestRepository {
	@Autowired
	@Qualifier("dataWarehouseJdbcTemplate")
	private NamedParameterJdbcTemplate jdbcTemplate;
	public List<QuotationEmailSuggest> getEmailSuggest(String type){
		String sql="SELECT * FROM tbl_quotation_email_suggests where type= :type;";
		Map<String, Object> argMap = new HashMap<String, Object>();
		argMap.put("type", type);
		return jdbcTemplate.query(sql, argMap, new BeanPropertyRowMapper<QuotationEmailSuggest>(QuotationEmailSuggest.class));
	}

}
