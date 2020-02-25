package adstech.vn.quotationbackoffice.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.security.Principal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import adstech.vn.quotationbackoffice.contract.ImportContract;
import adstech.vn.quotationbackoffice.contract.KeywordContract;
import adstech.vn.quotationbackoffice.contract.MultipartFileData;
import adstech.vn.quotationbackoffice.contract.TreeNodeContract;
import adstech.vn.quotationbackoffice.model.Category;
import adstech.vn.quotationbackoffice.model.DateImport;
import adstech.vn.quotationbackoffice.model.Keyword;
import adstech.vn.quotationbackoffice.service.DateImportService;
import adstech.vn.quotationbackoffice.service.IKeywordService;
import adstech.vn.quotationbackoffice.util.CommonConstant;
import adstech.vn.quotationbackoffice.util.ExcelUtil;
import adstech.vn.quotationbackoffice.util.SheetsServiceUtil;

@RestController
public class KeywordController {

	@Autowired
	IKeywordService keywordService;
	@Autowired
	DateImportService dateImportService;
	@GetMapping("/countImport")
	public ResponseEntity<Integer> getCount(){
		return new ResponseEntity<Integer>(dateImportService.getCount(LocalDate.now()), HttpStatus.OK);
	}
	@GetMapping("/categories")
	public ResponseEntity<?> getCategoryByParent(@RequestParam(required = false) String parent,
			@RequestParam(required = false) String str) {
 		if (str == null) {
			if (parent.equals("#")) {
				parent = null;
			}
			return new ResponseEntity<List<TreeNodeContract>>(keywordService.getListCategory(parent), HttpStatus.OK);
		} else {
			return new ResponseEntity<List<String>>(keywordService.searchCategories(str), HttpStatus.OK);
		}
	}

	@GetMapping("/categories/{id}")
	public ResponseEntity<Category> getCategory(@PathVariable String id) {
		return new ResponseEntity<Category>(keywordService.getCategory(id), HttpStatus.OK);
	}

	@DeleteMapping("/categories/{id}")
	public ResponseEntity<?> deleteCategory(@PathVariable String id, Principal principal) {
		try {
			return new ResponseEntity<Integer>(keywordService.deleteCategory(id, principal.getName()), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<String>(e.getCause() == null ? e.getMessage() : e.getCause().getMessage(),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/categories")
	public ResponseEntity<String> createCategory(@RequestBody Category category, Principal principal) {
		try {
			if(category.getParentId().equals("")) {
				category.setParentId(null);
			}
			category.setCreatedAt(new Date());
			category.setCreatedBy(principal.getName());
			int result = keywordService.createCategory(category);
			if (result == 1) {
				return new ResponseEntity<String>(CommonConstant.SUCCESS, HttpStatus.OK);
			}
			return new ResponseEntity<String>(CommonConstant.FAIL, HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			return new ResponseEntity<String>(CommonConstant.FAIL, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PutMapping("/categories")
	public ResponseEntity<String> updateCategory(@RequestBody Category category, Principal principal) {
		try {
			category.setUpdatedAt(new Date());
			category.setUpdatedBy(principal.getName());
			int result = keywordService.updateCategory(category);
			if (result == 1) {
				return new ResponseEntity<String>(CommonConstant.SUCCESS, HttpStatus.OK);
			}
			return new ResponseEntity<String>(CommonConstant.FAIL, HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			return new ResponseEntity<String>(CommonConstant.FAIL, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/list-keywords")
	public ResponseEntity<List<Keyword>> getKeywords(@RequestParam String categoryIds) {
		return new ResponseEntity<List<Keyword>>(keywordService.getKeywords(categoryIds), HttpStatus.OK);
	}
	
	@GetMapping("/all-keywords")
	public ResponseEntity<List<Keyword>> getAllKeywords(@RequestParam String categoryIds) {
		return new ResponseEntity<List<Keyword>>(keywordService.getAllKeywords(categoryIds), HttpStatus.OK);
	}

	@PostMapping("/add-keywords")
	public ResponseEntity<?> addKeywords(@RequestBody KeywordContract keywordContract, Principal principal) {
		try {
			return new ResponseEntity<Integer>(keywordService.addKeywords(keywordContract, principal.getName()),
					HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<String>(e.getCause() == null ? e.getMessage() : e.getCause().getMessage(),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@DeleteMapping("/delete-keywords")
	public ResponseEntity<?> deleteKeyword(@RequestParam String keywordIds, Principal principal) {
		try {
			return new ResponseEntity<Integer>(keywordService.deleteKeywords(keywordIds, principal.getName()),
					HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<String>(e.getCause() == null ? e.getMessage() : e.getCause().getMessage(),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PutMapping("/edit-keywords")
	public ResponseEntity<String> updateKeywords(@RequestBody List<Keyword> keywords, Principal principal) {
		try {
			int result = keywordService.updateKeywords(keywords, principal.getName());
			if (result >= 1) {
				return new ResponseEntity<String>(CommonConstant.SUCCESS, HttpStatus.OK);
			}
			return new ResponseEntity<String>(CommonConstant.FAIL, HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			return new ResponseEntity<String>(e.getCause() == null ? e.getMessage() : e.getCause().getMessage(),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	@GetMapping("/getNumber")
	public ResponseEntity<List<Integer>> getNumber(){
		return new ResponseEntity<List<Integer>>(keywordService.getNumKeyAndCatg(), HttpStatus.OK);
	}
	@GetMapping("/NumKey")
	public ResponseEntity<Integer> getNumKey(){
		return  new ResponseEntity<Integer>(keywordService.getNumKey(), HttpStatus.OK);
	}
	@GetMapping("/NumCatg")
	public ResponseEntity<Integer> getNumCatg(){
		return  new ResponseEntity<Integer>(keywordService.getNumCatg(), HttpStatus.OK);
	}
	@PostMapping("/import-data")
	public ResponseEntity<String> importData(@ModelAttribute("myUploadForm") MultipartFileData importData,
			Principal principal) {
		File uploadRootDir = new File(CommonConstant.TEMPORATION_FOLDER);
		if (!uploadRootDir.exists()) {
			uploadRootDir.mkdirs();
		}
		String name = uploadRootDir.getAbsolutePath() + File.separator + principal.getName() + "_"
				+ importData.getFileDatas().getOriginalFilename();

		try {
			File serverFile = new File(name);
			BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
			stream.write(importData.getFileDatas().getBytes());
			stream.close();
			ImportContract importContract = ExcelUtil.importFile(name);
			if(importContract.getCode()==CommonConstant.FAIL) {
				serverFile.delete();
				return new ResponseEntity<String>(CommonConstant.FAIL, HttpStatus.INTERNAL_SERVER_ERROR);
			}
			keywordService.importData(importContract, principal.getName());
			
			return new ResponseEntity<String>(CommonConstant.SUCCESS, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(CommonConstant.FAIL, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/import-google-sheet")
	public ResponseEntity<String> importData(@RequestParam String url, Principal principal) {
		try {
			ImportContract importContract = SheetsServiceUtil.importFromSpeadSheet(url);
			keywordService.importData(importContract, principal.getName());
			return new ResponseEntity<String>(CommonConstant.SUCCESS, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(CommonConstant.FAIL, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	@GetMapping("/statistic/keyword")
	public ResponseEntity<List<Map<String,Object>>> statisticKeywordByUser(){
		try {
			return new ResponseEntity<List<Map<String,Object>>>(keywordService.statisticKeyworkByUser(),HttpStatus.OK);
		} catch (Exception e) {
			Map<String,Object> error=new HashMap<String, Object>();
			error.put("status", "error");
			error.put("error", e);
			List<Map<String,Object>> response =new ArrayList<>();
			response.add(error);
			return new ResponseEntity<List<Map<String,Object>>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	@GetMapping("/listDateImport")
	public ResponseEntity<List<DateImport>> getListDateImport(){
		try {
			return new ResponseEntity<List<DateImport>>(dateImportService.getListDate(), HttpStatus.OK);
		}
		catch(Exception e){
			e.printStackTrace();
			return new ResponseEntity<List<DateImport>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}
}
