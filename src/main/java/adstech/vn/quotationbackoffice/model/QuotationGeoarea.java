package adstech.vn.quotationbackoffice.model;

public class QuotationGeoarea {
	private Long criteriaId;
	private String name;
	private String canonicalName;
	private Long parentId;
	private String zw;
	private String targetType;
	private String status;
	public Long getCriteriaId() {
		return criteriaId;
	}

	public void setCriteriaId(Long criteriaId) {
		this.criteriaId = criteriaId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCanonicalName() {
		return canonicalName;
	}

	public void setCanonicalName(String canonicalName) {
		this.canonicalName = canonicalName;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public String getZw() {
		return zw;
	}

	public void setZw(String zw) {
		this.zw = zw;
	}

	public String getTargetType() {
		return targetType;
	}

	public void setTargetType(String targetType) {
		this.targetType = targetType;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
