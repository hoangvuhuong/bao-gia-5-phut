package adstech.vn.quotationbackoffice.contract;

import java.util.List;

public class KeywordContract {
	private String categoryId;
	private List<String> keywords;
	
	public String getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}
	public List<String> getKeywords() {
		return keywords;
	}
	public void setKeywords(List<String> keywords) {
		this.keywords = keywords;
	}
}
