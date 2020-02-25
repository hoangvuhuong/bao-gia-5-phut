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
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import adstech.vn.quotationbackoffice.model.Quotation;

@Repository
public class QuotationRepository {

	@Autowired
	@Qualifier("dataWarehouseJdbcTemplate")
	private NamedParameterJdbcTemplate jdbcTemplate;
	
	@Autowired
	QuotationPlanRepository quotationPlanRepository;
	
	@Autowired
	QuotationKeywordRepository quotationKeywordRepository;
	
	public List<Quotation> getByCreateUser(String user, Integer page, Integer pageSize) {
		String sql = "SELECT * FROM tbl_quotations WHERE created_by = :user order by created_at DESC";
		Map<String, Object> argMap = new HashMap<String, Object>();
		argMap.put("user", user);
		if (page != null && pageSize != null) {
			sql += " LIMIT :from, :pageSize";
			argMap.put("from", page * pageSize);
			argMap.put("pageSize", pageSize);
		}

		return jdbcTemplate.query(sql, argMap, new BeanPropertyRowMapper<Quotation>(Quotation.class));
	}
	
	public Quotation getQuotation(Long quotationId) {
		String sql = "SELECT * FROM tbl_quotations WHERE quotation_id = :quotationId";
		Map<String, Object> argMap = new HashMap<String, Object>();
		argMap.put("quotationId", quotationId);
		Quotation quotation=new Quotation();
		quotation= jdbcTemplate.queryForObject(sql, argMap, new BeanPropertyRowMapper<Quotation>(Quotation.class));
		//set QuotationPlan
		Long quotation_id=quotation.getQuotationId();
		quotation.setPlan(quotationPlanRepository.getQuotationPlan(quotation_id));
		//set KeyWord
		quotation.setKeywords(quotationKeywordRepository.getQuotationKeyword(quotation_id));
		return quotation;
	}

	public List<Quotation> getByApproveUser(String user, Integer page, Integer pageSize,List<String> emailApprove) {
		String sql="";
		Map<String, Object> argMap = new HashMap<String, Object>();
		if(emailApprove.contains(user)) {
			 sql = "SELECT * FROM tbl_quotations WHERE (status = 'SUBMITTED' OR status = 'RE-SUBMITTED')";
		} else {
			 sql = "SELECT * FROM tbl_quotations WHERE approver = :user AND (status = 'SUBMITTED' OR status = 'RE-SUBMITTED')";
			argMap.put("user", user);
		}
		
		if (page != null && pageSize != null) {
			sql += " LIMIT :from, :pageSize";
			argMap.put("from", page * pageSize);
			argMap.put("pageSize", pageSize);
		}

		return jdbcTemplate.query(sql, argMap, new BeanPropertyRowMapper<Quotation>(Quotation.class));
	}

	public int approve(Long quotationId) {
		String sql = "UPDATE tbl_quotations SET status = 'APPROVED', approved_at = CURRENT_TIMESTAMP WHERE quotation_id = :quotationId";
		Map<String, Object> argMap = new HashMap<String, Object>();
		argMap.put("quotationId", quotationId);

		return jdbcTemplate.update(sql, argMap);
	}

	public int reject(Long quotationId, String note) {
		String sql = "UPDATE tbl_quotations SET status = 'REJECTED', rejected_at = CURRENT_TIMESTAMP, note = :note WHERE quotation_id = :quotationId";
		Map<String, Object> argMap = new HashMap<String, Object>();
		argMap.put("quotationId", quotationId);
		argMap.put("note", note);

		return jdbcTemplate.update(sql, argMap);
	}

	public Long create(Quotation quotation) {
		if(quotation.getQuotationId() == null) {
			String sql = "INSERT INTO tbl_quotations(partner_id, partner_name, partner_website, in_charge_partner, "
					+ "approver, main_product_service, suggestion_keywords, geo_area, expected_budget, used_to_advertise, previous_partner, "
					+ "quit_reason, previous_budget, due_date, created_at, created_by, vat, average_cpc, clicks, costs, ctr, impressions) "
					+ "VALUES(:partnerId, :partnerName, :partnerWebsite, :inChargePartner, :approver, :mainProductService,"
					+ ":suggestionKeywords, :geoArea, :expectedBudget, :usedToAdvertise, :previousPartner,"
					+ " :quitReason, :previousBudget, "
					+ ":dueDate, CURRENT_TIMESTAMP, :createdBy, :vat, :averageCpc, :clicks, :costs, :ctr, :impressions)";

			SqlParameterSource paramMap = new BeanPropertySqlParameterSource(quotation);
			KeyHolder keyHolder = new GeneratedKeyHolder();

			jdbcTemplate.update(sql, paramMap, keyHolder);

			return keyHolder.getKey().longValue();
		} else {
			String sql = "UPDATE tbl_quotations SET partner_id = :partnerId, partner_name=:partnerName, partner_website=:partnerWebsite, in_charge_partner=:inChargePartner, "
					+ "approver=:approver, main_product_service=:mainProductService, suggestion_keywords=:suggestionKeywords, vat=:vat, "
					+ "average_cpc=:averageCpc, clicks=:clicks, costs=:costs, ctr=:ctr, impressions=:impressions, "
					+ "geo_area=:geoArea, expected_budget=:expectedBudget, used_to_advertise=:usedToAdvertise, previous_partner=:previousPartner, quit_reason=:quitReason,"
					+ "previous_budget=:previousBudget, due_date=:dueDate, updated_at=CURRENT_TIMESTAMP, updated_by=:updatedBy, report_link=\"\" WHERE quotation_id=:quotationId";
			
			SqlParameterSource paramMap = new BeanPropertySqlParameterSource(quotation);
			KeyHolder keyHolder = new GeneratedKeyHolder();
			jdbcTemplate.update(sql, paramMap, keyHolder);
			return quotation.getQuotationId();
		}
		
	}
	public int updateQuotationLinkReport(Long quotationId, String reportLink) {
		String sql = "UPDATE tbl_quotations SET report_link = :reportLink WHERE quotation_id=:quotationId";
		Map<String, Object> argMap = new HashMap<String, Object>();
		argMap.put("quotationId", quotationId);
		argMap.put("reportLink", reportLink);

		return jdbcTemplate.update(sql, argMap);
	}
	public Long submit(Quotation quotation) {
		if(quotation.getQuotationId() == null) {
			String sql = "INSERT INTO tbl_quotations(partner_id, partner_name, partner_website, in_charge_partner, "
					+ "approver, main_product_service, suggestion_keywords, geo_area, expected_budget, used_to_advertise, previous_partner, "
					+ "quit_reason, previous_budget, due_date, status, created_at, created_by, vat, average_cpc, clicks, costs, ctr, impressions) "
					+ "VALUES(:partnerId, :partnerName, :partnerWebsite, :inChargePartner, :approver, :mainProductService,"
					+ ":suggestionKeywords, :geoArea, :expectedBudget, :usedToAdvertise, :previousPartner,"
					+ " :quitReason, :previousBudget, "
					+ ":dueDate, 'SUBMITTED' ,CURRENT_TIMESTAMP, :createdBy, :vat, :averageCpc, :clicks, :costs, :ctr, :impressions)";

			SqlParameterSource paramMap = new BeanPropertySqlParameterSource(quotation);
			KeyHolder keyHolder = new GeneratedKeyHolder();

			jdbcTemplate.update(sql, paramMap, keyHolder);

			return keyHolder.getKey().longValue();
		} else {
			String sql = "UPDATE tbl_quotations SET partner_id = :partnerId, partner_name=:partnerName, partner_website=:partnerWebsite, in_charge_partner=:inChargePartner, "
					+ "approver=:approver, main_product_service=:mainProductService, suggestion_keywords=:suggestionKeywords, vat=:vat,"
					+ "average_cpc=:averageCpc, clicks=:clicks, costs=:costs, ctr=:ctr, impressions=:impressions, "
					+ "geo_area=:geoArea, expected_budget=:expectedBudget, used_to_advertise=:usedToAdvertise, previous_partner=:previousPartner, quit_reason=:quitReason,"
					+ "previous_budget=:previousBudget, due_date=:dueDate, status=IF(status='REJECTED', 'RE-SUBMITTED', 'SUBMITTED'), updated_at=CURRENT_TIMESTAMP, updated_by=:updatedBy WHERE quotation_id=:quotationId";
			
			SqlParameterSource paramMap = new BeanPropertySqlParameterSource(quotation);
			KeyHolder keyHolder = new GeneratedKeyHolder();

			jdbcTemplate.update(sql, paramMap, keyHolder);
			return quotation.getQuotationId();
		}
		
	}

	public int submit(Long quotationId, String user) {
		String sql = "UPDATE tbl_quotations set status = IF(status='REJECTED', 'RE-SUBMITTED', 'SUBMITTED'), updated_at = CURRENT_TIMESTAMP, updated_by = :user WHERE quotation_id = :quotationId";
		Map<String, Object> argMap = new HashMap<String, Object>();
		argMap.put("quotationId", quotationId);
		argMap.put("user", user);

		return jdbcTemplate.update(sql, argMap);
	}
}
