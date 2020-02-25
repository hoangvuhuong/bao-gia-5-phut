package adstech.vn.quotationbackoffice.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import adstech.vn.quotationbackoffice.model.Role;

@Repository
public class RoleRepository {
	@Autowired
	@Qualifier("erpJdbcTemplate")
	private NamedParameterJdbcTemplate jdbcTemplate;
	
	public List<Role> getAllRole() {
		String sql = "SELECT * FROM `role`";
		
		try {
			return jdbcTemplate.query(sql, new BeanPropertyRowMapper<Role>(Role.class));
		} catch (Exception e) {
			return null;
		}
	}

}
