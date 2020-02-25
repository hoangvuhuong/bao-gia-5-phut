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

import adstech.vn.quotationbackoffice.model.User;

@Repository
public class UserRepository {
	@Autowired
	@Qualifier("erpJdbcTemplate")
	private NamedParameterJdbcTemplate jdbcTemplate;

	public User get(String username) {
		String sql = "SELECT a.*, b.role_name role FROM "
				+ "`user` a LEFT JOIN `role` b ON a.role_id = b.role_id WHERE user_login = :username";
		Map<String, Object> argMap = new HashMap<>();
		argMap.put("username", username);

		try {
			return jdbcTemplate.queryForObject(sql, argMap, new BeanPropertyRowMapper<User>(User.class));
		} catch (Exception e) {
			return null;
		}
	}

	public User getByEmail(String email) {
		String sql = "SELECT a.*, b.role_name role FROM "
				+ "`user` a LEFT JOIN `role` b ON a.role_id = b.role_id WHERE user_email = :email";
		Map<String, Object> argMap = new HashMap<>();
		argMap.put("email", email);

		try {
			return jdbcTemplate.queryForObject(sql, argMap, new BeanPropertyRowMapper<User>(User.class));
		} catch (Exception e) {
			return null;
		}
	}

	public List<User> getAllUser() {
		String sql = "SELECT * FROM `user`";

		try {
			return jdbcTemplate.query(sql, new BeanPropertyRowMapper<User>(User.class));
		} catch (Exception e) {
			return null;
		}
	}

	public List<User> getAllTechnician() {
		String sql = "SELECT * FROM `user` WHERE role_id <> 1";
		return jdbcTemplate.query(sql, new BeanPropertyRowMapper<User>(User.class));
	}

	public int addUser(User user) {
		String sql = "INSERT INTO `user`(`user_login`, `display_name`, `user_email`, "
				+ "`user_time_create`, `user_avatar`) "
				+ "VALUES(:userLogin, :displayName, :userEmail, CURRENT_TIMESTAMP, :userAvatar)";
		SqlParameterSource params = new BeanPropertySqlParameterSource(user);
		return jdbcTemplate.update(sql, params);
	}

	public int assignRole(Long userId, Long roleId) {
		String sql = "UPDATE `user` SET role_id = :roleId WHERE user_id = :userId";
		Map<String, Object> argMap = new HashMap<>();
		argMap.put("userId", userId);
		argMap.put("roleId", roleId);

		return jdbcTemplate.update(sql, argMap);
	}

	public int updateProfile(User user) {
		String sql = "UPDATE `user` SET display_name = :displayName, user_phone = :userPhone WHERE user_login = :userLogin";

		Map<String, Object> argMap = new HashMap<>();
		argMap.put("displayName", user.getDisplayName());
		argMap.put("userLogin", user.getUserLogin());
		argMap.put("userPhone", user.getUserPhone());

		return jdbcTemplate.update(sql, argMap);
	}

}
