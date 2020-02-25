package adstech.vn.quotationbackoffice.model;

import java.io.Serializable;
import java.util.Date;

public class Category implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String categoryId;
	private String categoryName;
	private String parentId;
	private Integer depthLevel;
	private String description;
	private String status;
	private Date createdAt;
	private String createdBy;
	private Date updatedAt;
	private String updatedBy;
	
	public Category() {}
	public Category(String categoryId, String categoryName, String parentId, Integer depthLevel, String description,
			String status, Date createdAt, String createdBy, Date updatedAt, String updatedBy) {
		this.categoryId = categoryId;
		this.categoryName = categoryName;
		this.parentId = parentId;
		this.depthLevel = depthLevel;
		this.description = description;
		this.status = status;
		this.createdAt = createdAt;
		this.createdBy = createdBy;
		this.updatedAt = updatedAt;
		this.updatedBy = updatedBy;
	}
	public void setCatg(Category category) {
		this.categoryId = category.getCategoryId();
		this.categoryName = category.getCategoryName();
		this.parentId = category.getParentId();
		this.depthLevel = category.getDepthLevel();
		this.description = category.getDescription();
		this.status = category.getStatus();
		this.createdAt = category.getCreatedAt();
		this.createdBy = category.getCreatedBy();
		this.updatedAt = category.getUpdatedAt();
		this.updatedBy = category.getUpdatedBy();
	}
	public String getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	public Integer getDepthLevel() {
		return depthLevel;
	}
	public void setDepthLevel(Integer depthLevel) {
		this.depthLevel = depthLevel;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
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
	
	@Override
	public boolean equals(Object obj) {
		Category catg = (Category) obj;
		return this.categoryName.equals(catg.getCategoryName()) && this.parentId.equals(catg.getParentId());
	}
}
