package adstech.vn.quotationbackoffice.model;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class Quotation implements Serializable,Cloneable {
	
	private static final long serialVersionUID = 1L;

	private Long quotationId;
	private String partnerId;
	private String partnerName;
	private String partnerWebsite;
	private String inChargePartner;
	
	private String mainProductService;
	private String suggestionKeywords;
	private String geoArea;
	private String expectedBudget;
	private String usedToAdvertise;
	private String previousPartner;
	private String quitReason;
	private String previousBudget;
	private String status;
	private String approver;
	private Date dueDate;
	private String note;
	private Date createdAt;
	private String createdBy;
	private Date approvedAt;
	private Date rejectedAt;
	private Date updatedAt;
	private String updatedBy;
	private List<QuotationPlan> plan;
	private List<QuotationKeyword> keywords;
	private String vat;
	private Long averageCpc;
	private Double clicks;
	private Double costs;
	private Double ctr;
	private Double impressions;
	private String reportLink;
	public Long getAverageCpc() {
		return averageCpc;
	}
	public void setAverageCpc(Long averageCpc) {
		this.averageCpc = averageCpc;
	}
	public Double getClicks() {
		return clicks;
	}
	public void setClicks(Double clicks) {
		this.clicks = clicks;
	}
	public Double getCosts() {
		return costs;
	}
	public void setCosts(Double costs) {
		this.costs = costs;
	}
	public Double getCtr() {
		return ctr;
	}
	public void setCtr(Double ctr) {
		this.ctr = ctr;
	}
	public Double getImpressions() {
		return impressions;
	}
	public void setImpressions(Double impressions) {
		this.impressions = impressions;
	}
	public static final String[] EmailDefautl= {"minhduc98kl@gmail.com","admin","email3@gmail.com"};
	public static final String[] EmailChoose= {"ducnm@gmail.com","datadsplus@gmail.com"};
	public String getMainProductService() {
		return mainProductService;
	}
	public void setMainProductService(String mainProductService) {
		this.mainProductService = mainProductService;
	}
	public String getSuggestionKeywords() {
		return suggestionKeywords;
	}
	public void setSuggestionKeywords(String suggestionKeywords) {
		this.suggestionKeywords = suggestionKeywords;
	}
	public String getGeoArea() {
		return geoArea;
	}
	public void setGeoArea(String geoArea) {
		this.geoArea = geoArea;
	}
	public String getExpectedBudget() {
		return expectedBudget;
	}
	public void setExpectedBudget(String expectedBudget) {
		this.expectedBudget = expectedBudget;
	}
	public String getUsedToAdvertise() {
		return usedToAdvertise;
	}
	public void setUsedToAdvertise(String usedToAdvertise) {
		this.usedToAdvertise = usedToAdvertise;
	}
	public String getPreviousPartner() {
		return previousPartner;
	}
	public void setPreviousPartner(String previousPartner) {
		this.previousPartner = previousPartner;
	}
	public String getQuitReason() {
		return quitReason;
	}
	public void setQuitReason(String quitReason) {
		this.quitReason = quitReason;
	}
	public String getPreviousBudget() {
		return previousBudget;
	}
	public void setPreviousBudget(String previousBudget) {
		this.previousBudget = previousBudget;
	}
	public Long getQuotationId() {
		return quotationId;
	}
	public void setQuotationId(Long quotationId) {
		this.quotationId = quotationId;
	}
	public String getPartnerId() {
		return partnerId;
	}
	public void setPartnerId(String partnerId) {
		this.partnerId = partnerId;
	}
	public String getPartnerName() {
		return partnerName;
	}
	public void setPartnerName(String partnerName) {
		this.partnerName = partnerName;
	}
	public String getPartnerWebsite() {
		return partnerWebsite;
	}
	public void setPartnerWebsite(String partnerWebsite) {
		this.partnerWebsite = partnerWebsite;
	}
	public String getInChargePartner() {
		return inChargePartner;
	}
	public void setInChargePartner(String inChargePartner) {
		this.inChargePartner = inChargePartner;
	}
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Date getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public Date getUpdatedAt() {
		return updatedAt;
	}
	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}
	public String getUpdatedBy() {
		return updatedBy;
	}
	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}
	public String getApprover() {
		return approver;
	}
	public void setApprover(String approver) {
		this.approver = approver;
	}
	public Date getApprovedAt() {
		return approvedAt;
	}
	public void setApprovedAt(Date approvedAt) {
		this.approvedAt = approvedAt;
	}
	public Date getRejectedAt() {
		return rejectedAt;
	}
	public void setRejectedAt(Date rejectedAt) {
		this.rejectedAt = rejectedAt;
	}
	public Date getDueDate() {
		return dueDate;
	}
	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public List<QuotationPlan> getPlan() {
		return plan;
	}
	public void setPlan(List<QuotationPlan> plan) {
		this.plan = plan;
	}
	public List<QuotationKeyword> getKeywords() {
		return keywords;
	}
	public void setKeywords(List<QuotationKeyword> keywords) {
		this.keywords = keywords;
	}
	public String getVat() {
		return vat;
	}
	public void setVat(String vat) {
		this.vat = vat;
	}
	public List<String> getListEmail(){
		List<String> list = Arrays.asList(EmailDefautl);
		return list;
	}
	public String getReportLink() {
		return reportLink;
	}
	public void setReportLink(String reportLink) {
		this.reportLink = reportLink;
	}
	
}
