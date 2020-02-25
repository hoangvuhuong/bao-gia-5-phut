package adstech.vn.quotationbackoffice.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
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
import adstech.vn.quotationbackoffice.model.DateImport;
import adstech.vn.quotationbackoffice.util.CommonConstant;

@Repository
public class DateImportRepository {

	@Autowired
	@Qualifier("dataWarehouseJdbcTemplate")
	private NamedParameterJdbcTemplate jdbcTemplate;
	
	public List<DateImport> getAllDate(){
		String sql = "SELECT * FROM tbl_date_import_file";
		return jdbcTemplate.query(sql, new BeanPropertyRowMapper<DateImport>(DateImport.class));
	}
	
	public int createDateImport(DateImport dateImport){
		String sql = "INSERT INTO tbl_date_import_file(`time_import`,`count`) VALUES(:timeImport, :count)";
		SqlParameterSource params = new BeanPropertySqlParameterSource(dateImport);
		return jdbcTemplate.update(sql, params);
//		MapSqlParameterSource parameters = new MapSqlParameterSource();
//		parameters.addValue("timeImport", dateImport.getTimeImport());
//		parameters.addValue("count", dateImport.getCount());
//		return jdbcTemplate.update(sql, parameters);
	}
	public int updateCount(DateImport dateImport) {
		String sql ="UPDATE tbl_date_import_file SET time_import = :timeImport , count = :count";
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		parameters.addValue("timeImport", dateImport.getTimeImport());
		parameters.addValue("count", dateImport.getCount());
		return jdbcTemplate.update(sql, parameters);
	}
	public DateImport getDateImport(LocalDate date) {
		String sql = "Select * from tbl_date_import_file where month(time_import) = :month and day(time_import) = :day and year(time_import) = :year";
		Map<String, Object> argMap = new HashMap<String, Object>();
		argMap.put("month", date.getMonthValue());
		argMap.put("day", date.getDayOfMonth());
		argMap.put("year", date.getYear());
		try {
			return jdbcTemplate.queryForObject(sql, argMap, new BeanPropertyRowMapper<DateImport>(DateImport.class));
		} catch (Exception e) {
			return null;
		}
	}
	public List<DateImport> getListDateImport(LocalDate date){
		String sql = "SELECT * FROM tbl_date_import_file where  time_import BETWEEN  date(DATE_ADD(:date, INTERVAL -7 DAY))   AND   date(:date)";
		Map<String, Object> argMap = new HashMap<String, Object>();
		argMap.put("date", date);
		return jdbcTemplate.query(sql, argMap, new BeanPropertyRowMapper<DateImport>(DateImport.class));
		 
	}
}
