package adstech.vn.quotationbackoffice.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import adstech.vn.quotationbackoffice.model.QuotationKeyword;

@Repository
public class QuotationKeywordRepository {
	
	@Autowired
	@Qualifier("dataWarehouseJdbcTemplate")
	private NamedParameterJdbcTemplate jdbcTemplate;

	public List<QuotationKeyword> getAdGroups(Long quotationId) {
		String sql = "SELECT * FROM tbl_quotation_keywords WHERE quotation_id = :quotationId AND is_incurred_keyword = 'NO' ORDER BY keyword";
		Map<String, Object> argMap = new HashMap<String, Object>();
		argMap.put("quotationId", quotationId);

		return jdbcTemplate.query(sql, argMap, new BeanPropertyRowMapper<QuotationKeyword>(QuotationKeyword.class));
	}
	
	public int updateAdGroups(List<QuotationKeyword> adsGroups, Long campaignId) {
		if(adsGroups == null || adsGroups.isEmpty()) {
			return 0;
		}
		String sql = "UPDATE tbl_quotation_keywords SET campaign_id = :campaignId, final_url = :finalUrl, final_mobile_url = :finalMobileUrl WHERE id = :id";
		
		SqlParameterSource[] params = new BeanPropertySqlParameterSource[adsGroups.size()];
		for (int i = 0; i < adsGroups.size(); i++) {
			QuotationKeyword adGroup = adsGroups.get(i);
			adGroup.setCampaignId(campaignId);
			params[i] = new BeanPropertySqlParameterSource(adGroup);
		}
		
		jdbcTemplate.batchUpdate(sql, params);
		
		return adsGroups.size();
	}
	
	public int resetAdGroups(List<QuotationKeyword> adsGroups) {
		if(adsGroups == null || adsGroups.isEmpty()) {
			return 0;
		}
		List<Long> ids = adsGroups.stream().map(a -> a.getId()).collect(Collectors.toList());
		String sql = "UPDATE tbl_quotation_keywords SET campaign_id = null, final_url = null, final_mobile_url = null WHERE id IN (:ids)";
		
		Map<String, Object> argMap = new HashMap<String, Object>();
		argMap.put("ids", ids);
		
		return jdbcTemplate.update(sql, argMap);
	}
	public Long create(QuotationKeyword quotationKeyword) {
		String sql="INSERT INTO `tbl_quotation_keywords` (`keyword`, `quotation_id`, `is_incurred_keyword`) "
				+ "VALUES (:keyword,:quotationId,:isIncurredKeyword);"; 
		SqlParameterSource paramMap = new BeanPropertySqlParameterSource(quotationKeyword);
		KeyHolder keyHolder = new GeneratedKeyHolder();

		jdbcTemplate.update(sql, paramMap, keyHolder);
		return keyHolder.getKey().longValue();
	}
	public List<QuotationKeyword> getQuotationKeyword(Long quotation_id){
		String sql="SELECT * FROM tbl_quotation_keywords where quotation_id= :quotation_id ;";
		Map<String, Object> argMap = new HashMap<String, Object>();
		argMap.put("quotation_id", quotation_id);
		
		return jdbcTemplate.query(sql, argMap, new BeanPropertyRowMapper<QuotationKeyword>(QuotationKeyword.class));
	}
	public int delete(List<Long> keyWordID) {
		String sql="DELETE FROM tbl_quotation_keywords WHERE id IN (:ids)";
		Map<String, Object> argMap = new HashMap<String, Object>();
		argMap.put("ids", keyWordID);
		return jdbcTemplate.update(sql, argMap);
	}
}
