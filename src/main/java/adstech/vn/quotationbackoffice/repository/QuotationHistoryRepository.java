package adstech.vn.quotationbackoffice.repository;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class QuotationHistoryRepository {
	
	@Autowired
	@Qualifier("dataWarehouseJdbcTemplate")
	private NamedParameterJdbcTemplate jdbcTemplate;
	
	public int addHistory(Long quotationId, String action, String note, String user) {
		String sql = "INSERT INTO tbl_quotation_histories(quotation_id, note, action, created_at, created_by) "
				+ "VALUES(:quotationId, :note, :action, CURRENT_TIMESTAMP, :user)";
		
		Map<String, Object> argMap = new HashMap<String, Object>();
		argMap.put("quotationId", quotationId);
		argMap.put("action", action);
		argMap.put("note", note);
		argMap.put("user", user);
		
		return jdbcTemplate.update(sql, argMap);
	}
}
