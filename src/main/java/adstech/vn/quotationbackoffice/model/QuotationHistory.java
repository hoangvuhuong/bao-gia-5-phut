package adstech.vn.quotationbackoffice.model;

import java.io.Serializable;
import java.util.Date;

public class QuotationHistory implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private Long quotationId;
	private String note;
	private String action;
	private String createdBy;
	private Date createdAt;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getQuotationId() {
		return quotationId;
	}
	public void setQuotationId(Long quotationId) {
		this.quotationId = quotationId;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public Date getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}
}
