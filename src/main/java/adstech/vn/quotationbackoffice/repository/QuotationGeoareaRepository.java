package adstech.vn.quotationbackoffice.repository;

import java.util.Arrays;
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
import org.springframework.stereotype.Repository;

import adstech.vn.quotationbackoffice.model.QuotationGeoarea;

@Repository
public class QuotationGeoareaRepository {
	@Autowired
	@Qualifier("dataWarehouseJdbcTemplate")
	private NamedParameterJdbcTemplate jdbcTemplate;

	public int create(QuotationGeoarea quotationGeoarea) {
		String sql = "INSERT INTO `tbl_quotation_geoareas` (`criteria_id`, `name`, `canonical_name`, `parent_id`, `zw`, `target_type`) "
				+ "VALUES (:criteriaId, :name, :canonicalName, :parentId, :zw, :targetType);";
		SqlParameterSource paramMap = new BeanPropertySqlParameterSource(quotationGeoarea);
		return jdbcTemplate.update(sql, paramMap);

	}
	
	public List<QuotationGeoarea> getAll() {
		System.out.println();
		String sql="SELECT * FROM tbl_quotation_geoareas ";
		return jdbcTemplate.query(sql, new BeanPropertyRowMapper<QuotationGeoarea>(QuotationGeoarea.class));
		
	}
	public List<QuotationGeoarea> getByCriteriaId(String geoArea) {
		List<String> criteriaIds = Arrays.asList(geoArea.split(","));
		List<Long> criteriaIdsInt=criteriaIds.stream().map(c->Long.parseLong(c)).collect(Collectors.toList());
		
		String sql="SELECT * FROM tbl_quotation_geoareas WHERE criteria_id IN (:criteriaIds)";
		Map<String, Object> argMap = new HashMap<String, Object>();
		argMap.put("criteriaIds", criteriaIdsInt);
		
		return jdbcTemplate.query(sql, argMap, new BeanPropertyRowMapper<QuotationGeoarea>(QuotationGeoarea.class));
		
	}
}
