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

import adstech.vn.quotationbackoffice.model.QuotationPlan;

@Repository
public class QuotationPlanRepository {
	@Autowired
	@Qualifier("dataWarehouseJdbcTemplate")
	private NamedParameterJdbcTemplate jdbcTemplate;
	
	@Autowired
	QuotationAdsTypeRepository quotationAdsTypeRepository;
	
	public Long create(QuotationPlan plan) {
		String sql ="INSERT INTO `tbl_quotation_plans` (`quotation_id`, `name`) VALUES (:quotation_id, :name);";
		SqlParameterSource paramMap = new BeanPropertySqlParameterSource(plan);
		KeyHolder keyHolder = new GeneratedKeyHolder();

		jdbcTemplate.update(sql, paramMap, keyHolder);

		return keyHolder.getKey().longValue();
	}
	public List<QuotationPlan> getQuotationPlan(Long quotation_id){
		String sql="SELECT * FROM tbl_quotation_plans where quotation_id= :quotation_id;";
		Map<String, Object> argMap = new HashMap<String, Object>();
		argMap.put("quotation_id", quotation_id);
		List<QuotationPlan> quotationPlans=new ArrayList<QuotationPlan>();

		quotationPlans= jdbcTemplate.query(sql, argMap, new BeanPropertyRowMapper<QuotationPlan>(QuotationPlan.class));
		Long quotationPlanId;
		for(QuotationPlan quotationPlan:quotationPlans) {
			quotationPlanId=quotationPlan.getId();
			quotationPlan.setType(quotationAdsTypeRepository.getQuotationAdsType(quotationPlan.getQuotation_id(),quotationPlanId));
		}
		return quotationPlans;
	}
	public int delete(List<Long> PlanId) {
		String sql="DELETE FROM tbl_quotation_plans WHERE id IN (:ids)";
		Map<String, Object> argMap = new HashMap<String, Object>();
		argMap.put("ids", PlanId);
		return jdbcTemplate.update(sql, argMap);
	}
}
