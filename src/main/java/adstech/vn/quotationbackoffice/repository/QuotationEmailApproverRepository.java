package adstech.vn.quotationbackoffice.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import adstech.vn.quotationbackoffice.model.QuotationEmailApprover;

@Repository
public class QuotationEmailApproverRepository {
	@Autowired
	@Qualifier("dataWarehouseJdbcTemplate")
	private NamedParameterJdbcTemplate jdbcTemplate;
	public List<QuotationEmailApprover> getEmailApprover(String type){
		String sql="SELECT * FROM tbl_quotation_email_approvers where type= :type;";
		Map<String, Object> argMap = new HashMap<String, Object>();
		argMap.put("type", type);
		return jdbcTemplate.query(sql, argMap, new BeanPropertyRowMapper<QuotationEmailApprover>(QuotationEmailApprover.class));
	}
}
