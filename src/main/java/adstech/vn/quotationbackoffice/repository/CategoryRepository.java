package adstech.vn.quotationbackoffice.repository;

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

import adstech.vn.quotationbackoffice.contract.CategoryContract;
import adstech.vn.quotationbackoffice.model.Category;
import adstech.vn.quotationbackoffice.util.CommonConstant;

@Repository
public class CategoryRepository {
	
	@Autowired
	@Qualifier("dataWarehouseJdbcTemplate")
	private NamedParameterJdbcTemplate jdbcTemplate;
	
	public List<CategoryContract> getCategories() {
		String sql = "SELECT * FROM tbl_categories WHERE status = :activeStatus ORDER BY category_name";
		Map<String, Object> argMap = new HashMap<String, Object>();
		argMap.put("activeStatus", CommonConstant.SQL_STATUS_ACTIVE);
		
		return jdbcTemplate.query(sql, argMap, new BeanPropertyRowMapper<CategoryContract>(CategoryContract.class));
	}
	public int getFullCatg() {
		String sql = "SELECT COUNT(*) FROM tbl_categories WHERE status = 'ACTIVE'";
		return jdbcTemplate.queryForObject(sql, new HashMap<>(), Integer.class);
	}
	public Category getCategory(String categoryId) {
		String sql = "SELECT * FROM tbl_categories WHERE status = :activeStatus AND category_id = :categoryId";
		Map<String, Object> argMap = new HashMap<String, Object>();
		argMap.put("activeStatus", CommonConstant.SQL_STATUS_ACTIVE);
		argMap.put("categoryId", categoryId);
		try {
			return jdbcTemplate.queryForObject(sql, argMap, new BeanPropertyRowMapper<CategoryContract>(CategoryContract.class));
		} catch (Exception e) {
			return null;
		}
		
	}
	
	public List<CategoryContract> getCategories(String parentId) {
		String sql = "SELECT * FROM tbl_categories WHERE status = :activeStatus";
		Map<String, Object> argMap = new HashMap<String, Object>();
		argMap.put("activeStatus", CommonConstant.SQL_STATUS_ACTIVE);
		if(parentId != null) {
			sql += " AND parent_id = :parentId";
			argMap.put("parentId", parentId);
		} else {
			sql += " AND parent_id IS NULL";
		}
		sql += " ORDER BY category_name";
		return jdbcTemplate.query(sql, argMap, new BeanPropertyRowMapper<CategoryContract>(CategoryContract.class));
	}
	
	public List<String> searchCategories(String matchStr) {
		matchStr = "" + matchStr + "";
		//String sql = "SELECT category_id FROM tbl_categories WHERE status = :activeStatus category_name LIKE '%:matchStr%'";
		String sql = "SELECT category_id FROM tbl_categories WHERE status = :activeStatus AND MATCH(category_name) AGAINST(:matchStr IN boolean mode)";
		Map<String, Object> argMap = new HashMap<String, Object>();
		argMap.put("matchStr", matchStr);
		argMap.put("activeStatus", CommonConstant.SQL_STATUS_ACTIVE);
		
		return jdbcTemplate.queryForList(sql, argMap, String.class);
	}
	
	public Integer maxDepth() {
		String sql = "SELECT MAX(depth_level) FROM tbl_categories WHERE status = :activeStatus";
		Map<String, Object> argMap = new HashMap<String, Object>();
		argMap.put("activeStatus", CommonConstant.SQL_STATUS_ACTIVE);
		
		return jdbcTemplate.queryForObject(sql, argMap, Integer.class);
	}
	
	public int createCategory(Category category) {
		String sql = "INSERT INTO `tbl_categories`(category_id, `category_name`, `parent_id`, `depth_level`, `description`, `created_at`, `created_by`)" + 
				"VALUES(:categoryId, :categoryName, :parentId, :depthLevel, :description, :createdAt, :createdBy)";
		
		SqlParameterSource params = new BeanPropertySqlParameterSource(category);
		return jdbcTemplate.update(sql, params);
	}
	
	public List<String> getByParentId(String categoryId) {
		String sql = 
				"WITH RECURSIVE tree AS ( " + 
				"   SELECT category_id, " + 
				"          parent_id," + 
				"          1 as level" + 
				"   FROM tbl_categories" + 
				"   WHERE category_id = :categoryId" + 
				"   UNION ALL" + 
				"   SELECT p.category_id," + 
				"          p.parent_id, " + 
				"          t.level + 1" + 
				"   FROM tbl_categories p" + 
				"     JOIN tree t ON t.category_id = p.parent_id" + 
				")" + 
				"SELECT category_id " + 
				"FROM tree;";
		
		Map<String, Object> argMap = new HashMap<String, Object>();
		argMap.put("categoryId", categoryId);
		return jdbcTemplate.queryForList(sql, argMap, String.class);
	}
	
	public List<Category> getParent(List<String> categoryIds) {
		if(categoryIds == null || categoryIds.isEmpty()) {
			return null;
		}
		String sql = 
				"WITH RECURSIVE tree AS ( " + 
				"   SELECT category_id, " + 
				"          category_name," +
				"          parent_id," + 
				"          depth_level" + 
				"   FROM tbl_categories" + 
				"   WHERE category_id IN (:categoryIds)" + 
				"   UNION ALL" + 
				"   SELECT p.category_id," +
				"          p.category_name, " + 
				"          p.parent_id, " + 
				"          t.depth_level - 1" + 
				"   FROM tbl_categories p" + 
				"     JOIN tree t ON t.parent_id = p.category_id" + 
				")" + 
				"SELECT distinct(category_id), category_name, parent_id, depth_level " + 
				"FROM tree;";
		
		Map<String, Object> argMap = new HashMap<String, Object>();
		argMap.put("categoryIds", categoryIds);
		return jdbcTemplate.query(sql, argMap, new BeanPropertyRowMapper<Category>(Category.class));
	}
	
	public int deleteCategory(List<String> deleteIds, String updateUser) {
		String sql = "UPDATE `tbl_categories` SET `status` = :status, `updated_at` = CURRENT_TIMESTAMP, `updated_by` = :updateUser "
				+ "WHERE category_id IN (:deleteIds)";
		
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		parameters.addValue("status", CommonConstant.SQL_STATUS_INACTIVE);
		parameters.addValue("updateUser", updateUser);
		parameters.addValue("deleteIds", deleteIds);
		
		return jdbcTemplate.update(sql, parameters);
	}
	
	public int updateCategory(Category category) {
		String sql = "UPDATE tbl_categories SET ";
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		String query = "";
		if(category.getCategoryName() != null && !category.getCategoryName().equals("")) {
			query += "category_name = :categoryName";
			parameters.addValue("categoryName", category.getCategoryName());
		}
		if(category.getDescription() != null && !category.getDescription().equals("")) {
			query += (query.equals("") ? "" : ", ") + "description = :description";
			parameters.addValue("description", category.getDescription());
		}
		if(!query.equals("")) {
			sql += query + ", updated_at = CURRENT_TIMESTAMP, updated_by = :updatedBy WHERE category_id = :categoryId";
			parameters.addValue("updatedBy", category.getUpdatedBy());
			parameters.addValue("categoryId", category.getCategoryId());
			return jdbcTemplate.update(sql, parameters);
		}
		return 0;
	}
	
	public int batchInsertCategory(List<Category> categories) {
		String sql = "INSERT INTO `tbl_categories`(category_id, `category_name`, `parent_id`, `depth_level`, `description`, `created_at`, `created_by`)" + 
				"VALUES(:categoryId, :categoryName, :parentId, :depthLevel, :description, :createdAt, :createdBy)"
				+ " ON DUPLICATE KEY UPDATE status = 'ACTIVE'";
		SqlParameterSource[] params = new BeanPropertySqlParameterSource[categories.size()];
		
		for (int i = 0; i < categories.size(); i++) {
			params[i] = new BeanPropertySqlParameterSource(categories.get(i));
		}
		jdbcTemplate.batchUpdate(sql, params);
		return categories.size();
	}
	
	public List<Category> getCategoryByName(List<String> categoryNames){
		String sql = "SELECT * FROM tbl_categories WHERE category_name IN (:categoryNames)";
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		parameters.addValue("categoryNames", categoryNames);
		
		return jdbcTemplate.query(sql, parameters, new BeanPropertyRowMapper<Category>(Category.class));
	}
	public List<Category> getListCategoryRoot(){
		String sql = "SELECT * FROM tbl_categories WHERE depth_level = :dethpLv";
		Map<String, Object> argMap = new HashMap<String, Object>();
		argMap.put("dethpLv", "0");
		
		return jdbcTemplate.query(sql, argMap, new BeanPropertyRowMapper<Category>(Category.class));
	}
	public List<String> getAllCategory(){
		String sql = "SELECT category_id FROM tbl_categories WHERE status = :activeStatus and depth_level != 3" ;
		Map<String, Object> argMap = new HashMap<String, Object>();
		
		argMap.put("activeStatus", CommonConstant.SQL_STATUS_ACTIVE);
		
		return jdbcTemplate.queryForList(sql, argMap, String.class);
	}
}
