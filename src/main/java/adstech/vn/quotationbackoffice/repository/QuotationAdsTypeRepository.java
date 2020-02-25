package adstech.vn.quotationbackoffice.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import adstech.vn.quotationbackoffice.model.QuotationAdsType;

@Repository
public class QuotationAdsTypeRepository {
	@Autowired
	@Qualifier("dataWarehouseJdbcTemplate")
	private NamedParameterJdbcTemplate jdbcTemplate;
	
	@Autowired
	QuotationAdsTypeKeywordRepository quotationAdsTypeKeywordRepository;
	
	public Long create(QuotationAdsType adsType) {
		String sql ="INSERT INTO `tbl_quotation_ads_types` (`quotation_plan_id`, `type`, `target`, `charged`, `price`, `display`, `ctr`, `max_cpc`) "
				+ "VALUES (:quotationPlanId, :type, :target, :charged, :price, :display, :ctr, :maxCpc);";
		SqlParameterSource paramMap = new BeanPropertySqlParameterSource(adsType);
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(sql, paramMap, keyHolder);
		 return keyHolder.getKey().longValue();
		
	}
	public List<QuotationAdsType> getQuotationAdsType(Long quotation_id,Long quotationPlanId) {
		String sql="SELECT * FROM tbl_quotation_ads_types where quotation_plan_id=:quotationPlanId;";
		Map<String, Object> argMap = new HashMap<String, Object>();
		argMap.put("quotationPlanId", quotationPlanId);
		List<QuotationAdsType> adsTypes=new ArrayList<QuotationAdsType>();
		adsTypes= jdbcTemplate.query(sql, argMap, new BeanPropertyRowMapper<QuotationAdsType>(QuotationAdsType.class));
		Long adsTypeId;
		for(QuotationAdsType adsType:adsTypes) {
			adsTypeId=adsType.getId();
			adsType.setKeywords(quotationAdsTypeKeywordRepository.getAdsTypeKeyword(adsTypeId));;
		}
		return adsTypes;
	}
	public int delete(List<Long> AdsTypeId) {
		String sql="DELETE FROM tbl_quotation_ads_types WHERE id IN (:ids)";
		Map<String, Object> argMap = new HashMap<String, Object>();
		argMap.put("ids", AdsTypeId);
		return jdbcTemplate.update(sql, argMap);
	}
}
