package adstech.vn.quotationbackoffice.service;

import java.util.List;
import java.util.Map;

import adstech.vn.quotationbackoffice.contract.CategoryContract;
import adstech.vn.quotationbackoffice.contract.ImportContract;
import adstech.vn.quotationbackoffice.contract.KeywordContract;
import adstech.vn.quotationbackoffice.contract.TreeNodeContract;
import adstech.vn.quotationbackoffice.model.Category;
import adstech.vn.quotationbackoffice.model.Keyword;

public interface IKeywordService {
	
	public int getNumKey();
	
	public int getNumCatg();
	
	public  List<Integer> getNumKeyAndCatg();
	
	public List<CategoryContract> getListCategory();
	
	public List<TreeNodeContract> getListCategory(String parentId);
	
	public List<String> searchCategories(String searchString);
	
	public List<Keyword> getKeywords(String categoryIds);
	
	public List<Keyword> getAllKeywords(String categoryId);
	
	public Category getCategory(String categoryId);
	
	public int createCategory(Category category);
	
	public int importData(ImportContract importContract, String createUser);
	
	public int updateCategory(Category category);
	
	public int deleteCategory(String categoryId, String updateUser);
	
	public int addKeywords(KeywordContract keywords, String createUser);
	
	public int deleteKeywords(String keywordIds, String updateUser);
	
	public int updateKeywords(List<Keyword> keywords, String updateUser);
	
	public List<Map<String, Object>> statisticKeyworkByUser();
}
