package adstech.vn.quotationbackoffice.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import adstech.vn.quotationbackoffice.model.QuotationAdsTypeKeyword;

@Repository
public class QuotationAdsTypeKeywordRepository {
	@Autowired
	@Qualifier("dataWarehouseJdbcTemplate")
	private NamedParameterJdbcTemplate jdbcTemplate;
	
	public List<QuotationAdsTypeKeyword> getAdsTypeKeyword(Long adsTypeId) {
		String sql = "SELECT quotation_keyword_id,ads_type_id,average_cpc,cost,ctr,daily_clicks,daily_impressions,keyword\n " + 
				"FROM tbl_quotation_ads_type_keywords \n" + 
				"INNER JOIN tbl_quotation_keywords On tbl_quotation_ads_type_keywords.quotation_keyword_id=tbl_quotation_keywords.id \n" + 
				"WHERE tbl_quotation_ads_type_keywords.ads_type_id= :adsTypeId "
				+"order by cost desc;";
		Map<String, Object> argMap = new HashMap<String, Object>();
		argMap.put("adsTypeId", adsTypeId);

		return jdbcTemplate.query(sql, argMap, new BeanPropertyRowMapper<QuotationAdsTypeKeyword>(QuotationAdsTypeKeyword.class));
	}
	public int create(QuotationAdsTypeKeyword quotationAdsTypeKeyword) {
		String sql="INSERT INTO `tbl_quotation_ads_type_keywords` "
				+ "(`quotation_keyword_id`, `ads_type_id`, `average_cpc`, `cost`, `ctr`, `daily_clicks`, `daily_impressions`) "
				+ "VALUES (:quotationKeywordId, :adsTypeId, :averageCpc, :cost, :ctr, :dailyClicks, :dailyImpressions);"; 
		SqlParameterSource paramMap = new BeanPropertySqlParameterSource(quotationAdsTypeKeyword);
		return jdbcTemplate.update(sql, paramMap);
	}
	public int delete(List<Long> AdsTypeId) {
		String sql="DELETE FROM tbl_quotation_ads_type_keywords WHERE ads_type_id IN (:ads_type_ids)";
		Map<String, Object> argMap = new HashMap<String, Object>();
		argMap.put("ads_type_ids", AdsTypeId);
		return jdbcTemplate.update(sql, argMap);
	}
}
