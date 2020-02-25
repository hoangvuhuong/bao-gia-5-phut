package adstech.vn.quotationbackoffice.repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import adstech.vn.quotationbackoffice.model.Keyword;
import adstech.vn.quotationbackoffice.util.CommonConstant;

@Repository
public class KeywordRepository {
	
	@Autowired
	@Qualifier("dataWarehouseJdbcTemplate")
	private NamedParameterJdbcTemplate jdbcTemplate;
	
	public List<Keyword> getKeywords(String categoryIds) {
		String sql = "SELECT * FROM tbl_keywords WHERE status = :activeStatus AND category_id IN (:categoryIds)";
		Map<String, Object> argMap = new HashMap<String, Object>();
		argMap.put("activeStatus", CommonConstant.SQL_STATUS_ACTIVE);
		argMap.put("categoryIds", Arrays.asList(categoryIds.split(",")));
		
		return jdbcTemplate.query(sql, argMap, new BeanPropertyRowMapper<Keyword>(Keyword.class));
	}
	
	public List<Keyword> getKeywords(List<String> categoryIds) {
		String sql = "SELECT * FROM tbl_keywords WHERE status = :activeStatus AND category_id IN (:categoryIds) ORDER BY keyword";
		Map<String, Object> argMap = new HashMap<String, Object>();
		argMap.put("activeStatus", CommonConstant.SQL_STATUS_ACTIVE);
		argMap.put("categoryIds", categoryIds);
		
		return jdbcTemplate.query(sql, argMap, new BeanPropertyRowMapper<Keyword>(Keyword.class));
	}
	public int getFullKeyword() {
		String sql = "SELECT COUNT(*) FROM tbl_keywords WHERE status = 'ACTIVE'";
		return jdbcTemplate.queryForObject(sql, new HashMap<>(), Integer.class);
	}
//	public List<Keyword> getKeywordByParentId(String parentId){
//		
//		String sql = "SELECT * FROM tbl_keywords WHERE status = :activeStatus AND category_id = :parentId";
//		Map<String, Object> argMap = new HashMap<String, Object>();
//		argMap.put("activeStatus", CommonConstant.SQL_STATUS_ACTIVE);
//		argMap.put("parentId", parentId);
//		
//		return jdbcTemplate.query(sql, argMap, new BeanPropertyRowMapper<Keyword>(Keyword.class));
//		
//	}
	public int batchInsertKeyword(List<Keyword> keywords) {
		String sql = "INSERT INTO tbl_keywords (keyword_id, keyword, category_id, created_at, created_by) "
				+ "VALUES(:keywordId, :keyword, :categoryId, :createdAt, :createdBy) ON DUPLICATE KEY UPDATE status = 'ACTIVE'";
		SqlParameterSource[] params = new BeanPropertySqlParameterSource[keywords.size()];
		
		for (int i = 0; i < keywords.size(); i++) {
			params[i] = new BeanPropertySqlParameterSource(keywords.get(i));
		}
		jdbcTemplate.batchUpdate(sql, params);
		return keywords.size();
	}
	
	public int deleteKeywords(List<String> keywordIds, String updateUser) {
		String sql = "UPDATE tbl_keywords SET status = :status, updated_at = CURRENT_TIMESTAMP, updated_by = :updateUser "
				+ "WHERE keyword_id IN (:keywordIds)";
		Map<String, Object> argMap = new HashMap<String, Object>();
		argMap.put("status", CommonConstant.SQL_STATUS_INACTIVE);
		argMap.put("keywordIds", keywordIds);
		argMap.put("updateUser", updateUser);
		
		return jdbcTemplate.update(sql, argMap);
	}
	
	public int deleteKeywordsByCategories(List<String> categoryIds, String updateUser) {
		String sql = "UPDATE tbl_keywords SET status = :status, updated_at = CURRENT_TIMESTAMP, updated_by = :updateUser "
				+ "WHERE category_id IN (:categoryIds)";
		Map<String, Object> argMap = new HashMap<String, Object>();
		argMap.put("status", CommonConstant.SQL_STATUS_INACTIVE);
		argMap.put("categoryIds", categoryIds);
		argMap.put("updateUser", updateUser);
		
		return jdbcTemplate.update(sql, argMap);
	}
	
	public int batchUpdateKeyword(List<Keyword> keywords, String updateUser) {
		String sql = "UPDATE tbl_keywords SET keyword = :keyword, updated_at = CURRENT_TIMESTAMP, updated_by = :updatedBy "
				+ "WHERE keyword_id = :keywordId";
		SqlParameterSource[] params = new BeanPropertySqlParameterSource[keywords.size()];
		
		for (int i = 0; i < keywords.size(); i++) {
			Keyword keyword = keywords.get(i);
			keyword.setUpdatedBy(updateUser);
			params[i] = new BeanPropertySqlParameterSource(keyword);
		}
		jdbcTemplate.batchUpdate(sql, params);
		return keywords.size();
	}
	
	public List<Keyword> getKeywordByName(List<String> keywordNames){
		String sql = "SELECT * FROM tbl_keywords WHERE keyword IN (:keywordNames)";
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		parameters.addValue("keywordNames", keywordNames);
		
		return jdbcTemplate.query(sql, parameters, new BeanPropertyRowMapper<Keyword>(Keyword.class));
	}
	public List<Map<String, Object>> statisticKeyworkByUser(){
		String sql="SELECT DATE(created_at) as 'date',created_by,count(*) as 'count' FROM tbl_keywords\n" + 
				"group by DATE(created_at),created_by\n" + 
				"order by \n" + 
				"DATE(created_at) asc,\n" + 
				"created_by asc\n" + 
				";";
		Map<String, Object> argMap = new HashMap<String, Object>();
		List<Map<String, Object>> rows= jdbcTemplate.queryForList(sql, argMap);
		return rows;
	}
}
