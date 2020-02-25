package adstech.vn.quotationbackoffice.service;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import adstech.vn.quotationbackoffice.contract.CategoryContract;
import adstech.vn.quotationbackoffice.contract.ImportContract;
import adstech.vn.quotationbackoffice.contract.KeywordContract;
import adstech.vn.quotationbackoffice.contract.TreeNodeContract;
import adstech.vn.quotationbackoffice.model.Category;
import adstech.vn.quotationbackoffice.model.DateImport;
import adstech.vn.quotationbackoffice.model.Keyword;
import adstech.vn.quotationbackoffice.repository.CategoryRepository;
import adstech.vn.quotationbackoffice.repository.DateImportRepository;
import adstech.vn.quotationbackoffice.repository.KeywordRepository;
import adstech.vn.quotationbackoffice.util.StringUtil;

@Service
public class KeywordService implements IKeywordService {

	@Autowired
	@Qualifier("dataWarehouseDatasource")
	DataSource dataSource;

	
	
	@Autowired
	CategoryRepository categoryRepository;

	@Autowired
	KeywordRepository keywordRepository;
	
	@Autowired 
	DateImportRepository dateImportRepository;
	
	@Override
	public List<CategoryContract> getListCategory() {
		List<CategoryContract> categories = categoryRepository.getCategories();
		int maxDepth = categoryRepository.maxDepth();
		Map<String, CategoryContract> mapCategories = new HashMap<>();

		for (CategoryContract categoryContract : categories) {
			mapCategories.put(categoryContract.getCategoryId(), categoryContract);
		}

		while (maxDepth > 0) {
			arrange(mapCategories, maxDepth);
			maxDepth--;
		}
		List<CategoryContract> result = new ArrayList<>();
		mapCategories.forEach((k, v) -> {
			if (v.getDepthLevel() == 0) {
				result.add(v);
			}
		});
		return result;
	}

	private void arrange(Map<String, CategoryContract> mapCategories, int levelDepth) {
		mapCategories.forEach((id, category) -> {
			if (levelDepth == category.getDepthLevel()) {
				CategoryContract temp = mapCategories.get(category.getParentId());
				List<CategoryContract> tempChild = temp.getChild();
				if (tempChild == null) {
					tempChild = new ArrayList<>();
				}
				tempChild.add(category);
				temp.setChild(tempChild);
				mapCategories.put(temp.getCategoryId(), temp);
			}
		});
	}

	@Override
	public List<TreeNodeContract> getListCategory(String parentId) {
		List<CategoryContract> categories = categoryRepository.getCategories(parentId);
		List<TreeNodeContract> nodes = new ArrayList<>();
		for (CategoryContract category : categories) {
			TreeNodeContract node = new TreeNodeContract();
			node.setChildren(true);
			node.setIcon("fa fa-folder m--font-warning");
			node.setId(category.getCategoryId().toString());
			node.setText(category.getCategoryName());
			nodes.add(node);
		}
		return nodes;
	}

	@Override
	public List<Keyword> getKeywords(String categoryIds) {
		List<Keyword> keywords = keywordRepository.getKeywords(categoryIds);
		return keywords;
	}

	@Override
	public int createCategory(Category category) {
		category.setCategoryId(UUID.randomUUID().toString());
		if (category.getParentId() != null) {
			Category parent = categoryRepository.getCategory(category.getParentId());
			if (parent != null) {
				category.setDepthLevel(parent.getDepthLevel() + 1);
			}
		}
		return categoryRepository.createCategory(category);
	}

	@Override
	public int deleteCategory(String categoryId, String updateUser) {
		List<String> deleteIds = categoryRepository.getByParentId(categoryId);
		PlatformTransactionManager txManager = new DataSourceTransactionManager(dataSource);
		TransactionStatus status = txManager.getTransaction(new DefaultTransactionDefinition());
		try {
			categoryRepository.deleteCategory(deleteIds, updateUser);
			keywordRepository.deleteKeywordsByCategories(deleteIds, updateUser);
			txManager.commit(status);
			return 1;
		} catch (Exception e) {
			txManager.rollback(status);
			return 0;
		}
	}

	@Override
	public int updateCategory(Category category) {
		return categoryRepository.updateCategory(category);
	}

	@Override
	public Category getCategory(String categoryId) {
		return categoryRepository.getCategory(categoryId);
	}

	@Override
	public int addKeywords(KeywordContract keywords, String createUser) {
		if (keywords.getKeywords() != null && keywords.getKeywords().size() > 0) {
			List<Keyword> arrKey = keywordRepository.getKeywords(keywords.getCategoryId());
			List<Keyword> lstKeyword = new ArrayList<>();
			for (String keyword : keywords.getKeywords()) {
				if(isKeyDup(keyword, arrKey)) {
					continue;
				}
				else {
				Keyword k = new Keyword();
				k.setKeywordId(UUID.randomUUID().toString());
				k.setCategoryId(keywords.getCategoryId());
				k.setCreatedAt(new Date());
				k.setCreatedBy(createUser);
				k.setKeyword(keyword);
				lstKeyword.add(k);
			}
			}
			return keywordRepository.batchInsertKeyword(lstKeyword);
		}
		return 0;
	}
	
	private boolean isKeyDup(String keyword, List<Keyword> arrKey) {
		for(Keyword key : arrKey) {
			if(key.getKeyword().toLowerCase().trim().equals(keyword.trim().toLowerCase())) {
				return true;
			}
		}
		return false;
	}
	@Override
	public int deleteKeywords(String keywordIds, String updateUser) {
		List<String> ids = Arrays.asList(keywordIds.split(","));
		return keywordRepository.deleteKeywords(ids, updateUser);
	}

	@Override
	public int updateKeywords(List<Keyword> keywords, String updateUser) {
		return keywordRepository.batchUpdateKeyword(keywords, updateUser);
	}
	@Override
	public List<Integer> getNumKeyAndCatg(){
		List<Integer> array = new ArrayList<Integer>();
		array.add(keywordRepository.getFullKeyword());
		array.add(categoryRepository.getFullCatg());
		return array;
	}
	@Override
	public int getNumKey() {
		return keywordRepository.getFullKeyword();
	}
	@Override
	public int getNumCatg() {
		return categoryRepository.getFullCatg();
	}
	@Override
	public int importData(ImportContract importContract, String createUser) {
		List<Category> getCategoryRoot = categoryRepository.getListCategoryRoot();
		String check = "";
		for (Category tmp : importContract.getCategories()) {
			if (tmp.getDepthLevel() == 0) {
//				check = new Category(tmp.getCategoryId(), tmp.getCategoryName(), 
//						tmp.getParentId(), tmp.getDepthLevel(), tmp.getDescription(),
//						tmp.getStatus(), tmp.getCreatedAt(), tmp.getCreatedBy(), tmp.getUpdatedAt(),
//						tmp.getUpdatedBy());
				check = tmp.getCategoryName();
				break;
			}
		}
		if (isRootDuplicate(check, getCategoryRoot)) {
			List<String> categories = new ArrayList<String>();
			List<String> keywords = new ArrayList<String>();
			Category root = null;
			for (Category category : importContract.getCategories()) {
				categories.add(category.getCategoryName());
				category.setCreatedAt(new Date());
				category.setCreatedBy(createUser);
			}
			for (Keyword keyword : importContract.getKeywords()) {
				keywords.add(keyword.getKeyword());
				keyword.setCreatedBy(createUser);
				keyword.setCreatedAt(new Date());
			}

			List<Category> existCatg = categoryRepository.getCategoryByName(categories);
			for (Category category : existCatg) {
				if (category.getDepthLevel() == 0) {
					root = new Category(category.getCategoryId(), category.getCategoryName(), category.getParentId(),
							category.getDepthLevel(), category.getDescription(), category.getStatus(),
							category.getCreatedAt(), category.getCreatedBy(), category.getUpdatedAt(),
							category.getUpdatedBy());
					break;
				}
			}
			List<Category> containerCategory = getChildRoot(root, existCatg);

			List<Keyword> exitsKeyword = keywordRepository.getKeywordByName(keywords);
			List<Keyword> containerKeyword = getKeyInCatg(exitsKeyword, containerCategory);
			
			
			List<Category> existCatgs = categoryRepository.getCategoryByName(categories);
			HashMap<String, Category> mapExiHashMap = new HashMap<String, Category>();
			for (Category category : existCatgs) {
				mapExiHashMap.put(category.getCategoryName() + "-" + category.getDepthLevel(), category);
			}
			Map<String, String> replaceCatgId = new HashMap<String, String>();
			for (Category category : importContract.getCategories()) {
				Category existCat = mapExiHashMap.get(category.getCategoryName() + "-" + category.getDepthLevel());
				if (existCat != null
						&& (category.getDepthLevel() == 0 || replaceCatgId.containsKey(category.getParentId()))) {
					replaceCatgId.put(category.getCategoryId(), existCat.getCategoryId());
					category.setCategoryId(existCat.getCategoryId());
				}
				if (replaceCatgId.containsKey(category.getParentId())) {
					category.setParentId(replaceCatgId.get(category.getParentId()));
				}
			}

			for (Keyword keyword : importContract.getKeywords()) {
				if (replaceCatgId.containsKey(keyword.getCategoryId())) {
					keyword.setCategoryId(replaceCatgId.get(keyword.getCategoryId()));
				}
			}

			List<Keyword> exitsKeywords = keywordRepository.getKeywordByName(keywords);
			Map<String, Keyword> mapExistKeyword = new HashMap<String, Keyword>();
			for (Keyword keyword : exitsKeywords) {
				mapExistKeyword.put(keyword.getKeyword() + "-" + keyword.getCategoryId(), keyword);
			}

			for (Keyword keyword : importContract.getKeywords()) {
				if (mapExistKeyword.containsKey(keyword.getKeyword() + "-" + keyword.getCategoryId())) {
					keyword.setKeywordId(
							mapExistKeyword.get(keyword.getKeyword() + "-" + keyword.getCategoryId()).getKeywordId());
				}
			}
			
			
			
			PlatformTransactionManager txManager = new DataSourceTransactionManager(dataSource);
			TransactionStatus status = txManager.getTransaction(new DefaultTransactionDefinition());
			try {
				LocalDate local = LocalDate.now();
//				addImport(local);
				
				getDateImport(local);
				keywordRepository.batchInsertKeyword(importContract.getKeywords());
				categoryRepository.batchInsertCategory(importContract.getCategories());
				keywordRepository.batchInsertKeyword(containerKeyword);
				categoryRepository.batchInsertCategory(containerCategory);
				
				txManager.commit(status);
				return 1;
			} catch (Exception e) {
				txManager.rollback(status);
				return 0;
			}

		} else {

			List<String> categories = new ArrayList<String>();
			List<String> keywords = new ArrayList<String>();
			for (Category category : importContract.getCategories()) {
				categories.add(category.getCategoryName());
				category.setCreatedAt(new Date());
				category.setCreatedBy(createUser);
			}
			for (Keyword keyword : importContract.getKeywords()) {
				keywords.add(keyword.getKeyword());
				keyword.setCreatedBy(createUser);
				keyword.setCreatedAt(new Date());
			}

			List<Category> existCatg = categoryRepository.getCategoryByName(categories);
			HashMap<String, Category> mapExiHashMap = new HashMap<String, Category>();
			for (Category category : existCatg) {
				mapExiHashMap.put(category.getCategoryName() + "-" + category.getDepthLevel(), category);
			}
			Map<String, String> replaceCatgId = new HashMap<String, String>();
			for (Category category : importContract.getCategories()) {
				Category existCat = mapExiHashMap.get(category.getCategoryName() + "-" + category.getDepthLevel());
				if (existCat != null
						&& (category.getDepthLevel() == 0 || replaceCatgId.containsKey(category.getParentId()))) {
					replaceCatgId.put(category.getCategoryId(), existCat.getCategoryId());
					category.setCategoryId(existCat.getCategoryId());
				}
				if (replaceCatgId.containsKey(category.getParentId())) {
					category.setParentId(replaceCatgId.get(category.getParentId()));
				}
			}

			for (Keyword keyword : importContract.getKeywords()) {
				if (replaceCatgId.containsKey(keyword.getCategoryId())) {
					keyword.setCategoryId(replaceCatgId.get(keyword.getCategoryId()));
				}
			}

			List<Keyword> exitsKeyword = keywordRepository.getKeywordByName(keywords);
			Map<String, Keyword> mapExistKeyword = new HashMap<String, Keyword>();
			for (Keyword keyword : exitsKeyword) {
				mapExistKeyword.put(keyword.getKeyword() + "-" + keyword.getCategoryId(), keyword);
			}

			for (Keyword keyword : importContract.getKeywords()) {
				if (mapExistKeyword.containsKey(keyword.getKeyword() + "-" + keyword.getCategoryId())) {
					keyword.setKeywordId(
							mapExistKeyword.get(keyword.getKeyword() + "-" + keyword.getCategoryId()).getKeywordId());
				}
			}
			
			PlatformTransactionManager txManager = new DataSourceTransactionManager(dataSource);
			TransactionStatus status = txManager.getTransaction(new DefaultTransactionDefinition());
			try {
				LocalDate local = LocalDate.now();
				getDateImport(local);
				keywordRepository.batchInsertKeyword(importContract.getKeywords());
				categoryRepository.batchInsertCategory(importContract.getCategories());
				txManager.commit(status);
				return 1;
			} catch (Exception e) {
				txManager.rollback(status);
				return 0;
			}
		}

	}

	@Override
	public List<Keyword> getAllKeywords(String categoryIds) {
		List<String> allIds = new ArrayList<String>();
		List<String> rootIds = Arrays.asList(categoryIds.split(","));
		for (String string : rootIds) {
			allIds.addAll(categoryRepository.getByParentId(string));
		}
		return keywordRepository.getKeywords(allIds);
	}

	@Override
	public List<String> searchCategories(String searchString) {
		String tmp = StringUtil.removeAccent(searchString).trim();
		List<String> listMatchIds = categoryRepository.getAllCategory();
		//List<String> listMatchIds = categoryRepository.searchCategories(searchString);
		List<Category> matchCategories = categoryRepository.getParent(listMatchIds);
		List<String> nameCategory = new ArrayList<String>();
		for(Category category : matchCategories) {
			nameCategory.add(category.getCategoryName());
		}
		List<String> name = StringUtil.removeArrayAccent(nameCategory);
		
		List<String> nodes = new ArrayList<>();
		Set<String> getRoot = new HashSet<>();
		if (matchCategories != null) {
			for(int i=0; i < name.size(); i ++) {
				if(name.get(i).toLowerCase().contains(tmp.toLowerCase())) {
					Category catmp = new Category();
					
					getRoot.add(matchCategories.get(i).getCategoryId());
					
//					  if(doGetRoot(matchCategories.get(i)) != null)
//					  getRoot.add(doGetRoot(matchCategories.get(i)));
//					  if(matchCategories.get(i).getDepthLevel() == 2)
//					  getRoot.add(categoryRepository.getCategory(matchCategories.get(i).getParentId
//					  ()).getCategoryId());
//					 
					
					catmp.setCatg(matchCategories.get(i));
					for(int k= 0; k < matchCategories.get(i).getDepthLevel(); k++) {
						catmp.setCatg(categoryRepository.getCategory(catmp.getParentId()));
						getRoot.add(catmp.getCategoryId());
					}
				}
			}
			
		}
		for(String cat : getRoot) {
			nodes.add(cat);
		}
		return nodes;
	}

	private Boolean isRootDuplicate(String category, List<Category> listCategory) {
		for (Category tmp : listCategory) {
			if (category.equals(tmp.getCategoryName())) {
				return true;
			}
		}
		return false;
	}

	private List<Category> getChildRoot(Category root, List<Category> listCatg) {
		List<Category> arr = new ArrayList<Category>();
		for (Category tmp : listCatg) {
			if (tmp.getDepthLevel() == 0)
				arr.add(tmp);
			else if (tmp.getDepthLevel() == 1 && tmp.getParentId().equals(root.getCategoryId())) {
				arr.add(tmp);

			} else if (tmp.getDepthLevel() == 2) {
				for (Category tmp2 : listCatg) {
					if (tmp2.getDepthLevel() == 1 && tmp2.getParentId().equals(root.getCategoryId())
							&& tmp.getParentId().equals(tmp2.getCategoryId())) {
						arr.add(tmp);

					}
				}
			}
		}
		return arr;

	}

	private List<Keyword> getKeyInCatg(List<Keyword> listKey, List<Category> listCatg) {
		List<Keyword> arr = new ArrayList<Keyword>();
		for (Keyword key : listKey) {
			for (Category category : listCatg) {
				if (key.getCategoryId() != null && category.getCategoryId() != null) {
					if (key.getCategoryId().equals(category.getCategoryId())) {
						arr.add(key);
						break;
					}
				}
			}
		}
		return arr;
	}
	private String doGetRoot(Category child) {
		if(child.getDepthLevel() == 1)
			return categoryRepository.getCategory(child.getParentId()).getCategoryId();
		else if(child.getDepthLevel() == 2) {
			return categoryRepository.getCategory(categoryRepository.getCategory(child.getParentId()).getParentId()).getCategoryId();
		}
		else return null;
		
	}
	private void getDateImport(LocalDate date) {
		
		DateImport dateIp = dateImportRepository.getDateImport(date);
		if(dateIp == null) {
			dateIp = new DateImport();
			dateIp.setCount(1);
			dateIp.setTimeImport(date);
			dateImportRepository.createDateImport(dateIp);
		}
		else {
			dateIp.setCount(dateIp.getCount() + 1);
			dateImportRepository.updateCount(dateIp);
		}
	
	}
	
	@Override
	public List<Map<String, Object>> statisticKeyworkByUser() {
		
		return keywordRepository.statisticKeyworkByUser();

	}

}
