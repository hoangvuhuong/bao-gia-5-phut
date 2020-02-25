package adstech.vn.quotationbackoffice.contract;

import java.util.List;

import adstech.vn.quotationbackoffice.model.Category;
import adstech.vn.quotationbackoffice.model.Keyword;

public class ImportContract {
	
	private List<Category> categories;
	private List<Keyword> keywords;
	private String code;
	private String message;
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public List<Category> getCategories() {
		return categories;
	}
	public void setCategories(List<Category> categories) {
		this.categories = categories;
	}
	public List<Keyword> getKeywords() {
		return keywords;
	}
	public void setKeywords(List<Keyword> keywords) {
		this.keywords = keywords;
	}
}
