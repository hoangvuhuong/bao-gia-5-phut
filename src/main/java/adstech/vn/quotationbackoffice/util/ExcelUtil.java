package adstech.vn.quotationbackoffice.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import adstech.vn.quotationbackoffice.contract.ImportContract;
import adstech.vn.quotationbackoffice.model.Category;
import adstech.vn.quotationbackoffice.model.Keyword;

public class ExcelUtil {
	public static ImportContract importFile(String fileName) throws IOException {
		FileInputStream excelFile = new FileInputStream(new File(fileName));
		ImportContract importContract = new ImportContract();
		@SuppressWarnings("resource")
		Workbook workbook = new XSSFWorkbook(excelFile);
		Sheet datatypeSheet = workbook.getSheetAt(0);
		Iterator<Row> iterator = datatypeSheet.iterator();
		List<HashMap<String, String>> listCategories = new ArrayList<HashMap<String, String>>();
		List<HashMap<String, List<String>>> listMaps = new ArrayList<HashMap<String, List<String>>>();

		List<Category> catgResults = new ArrayList<Category>();
		List<Keyword> keyResults = new ArrayList<Keyword>();

		Row currentRow = iterator.next();
		int depth = 0;
		for (int i = 0; i < currentRow.getLastCellNum(); i++) {
			depth++;
			if (currentRow.getCell(i).getStringCellValue().equals("")) {
				listCategories.remove(0);
				depth--;
				break;
			}
			listMaps.add(new HashMap<>());
			if (i == currentRow.getLastCellNum() - 1) {
				break;
			}
			HashMap<String, String> node = new HashMap<>();
			listCategories.add(node);
		}

		int  check_break=0;
		//bien kiem tra da lap toi hang cuoi cung hay chua
		
		while (iterator.hasNext()) {
			currentRow = iterator.next();
			String key = "";
			

			for (int i = 0; i < depth; i++) {
				Cell c = currentRow.getCell(i);
				if (c == null) {
					if (i > 1) {
						// Neu khong phai cot dau tien rong la file khong format
						importContract.setCode(CommonConstant.FAIL);
						importContract.setMessage("Lỗi file mẫu bị trống cột");
						return importContract;
					}
					check_break++;
					break;
				}
				String parentKey = key;

				key += c.getStringCellValue();
				if (i < depth - 1) {
//					if (!listCategories.get(i).containsKey(c.getStringCellValue())) {
//						Category catg = new Category();
//						catg.setCategoryId(UUID.randomUUID().toString());
//						catg.setCategoryName(c.getStringCellValue());
//						if(i > 0) {
//							catg.setParentId(listCategories.get(i-1).get(currentRow.getCell(i - 1).getStringCellValue()).getCategoryId());
//						}
//						listCategories.get(i).put(c.getStringCellValue(), catg);
//					}

					if (listMaps.get(i).get(c.getStringCellValue()) == null
							|| !listMaps.get(i).get(c.getStringCellValue()).contains(key)) {
						Category catg = new Category();
						catg.setCategoryId(UUID.randomUUID().toString());
						catg.setCategoryName(c.getStringCellValue().trim());
						catg.setDepthLevel(i);

						listCategories.get(i).put(key, catg.getCategoryId());
						if (i > 0) {
							catg.setParentId(listCategories.get(i - 1).get(parentKey));
							//neu khong phai la tu khoa cap 1
						}
						List<String> temp = listMaps.get(i).get(c.getStringCellValue());
						if (temp == null) {
							temp = new ArrayList<String>();
						}
						temp.add(key);
						listMaps.get(i).put(c.getStringCellValue(), temp);
						catgResults.add(catg);
					}
				} else {
					if (listMaps.get(i).get(c.getStringCellValue()) == null
							|| !listMaps.get(i).get(c.getStringCellValue()).contains(key)) {
						Keyword keyword = new Keyword();
						keyword.setKeywordId(UUID.randomUUID().toString());
						keyword.setKeyword(c.getStringCellValue().trim());
						keyword.setCategoryId(listCategories.get(i - 1).get(parentKey));

						List<String> temp = listMaps.get(i).get(c.getStringCellValue());
						if (temp == null) {
							temp = new ArrayList<String>();
						}
						temp.add(key);
						listMaps.get(i).put(c.getStringCellValue(), temp);

						keyResults.add(keyword);
					}
				}
			}
			if(check_break>0) {
				break;
			}
		}
		for (int i = 0; i < keyResults.size() - 1; i++) {
			for (int j = i + 1; j < keyResults.size(); j++) {
				if (keyResults.get(i).getCategoryId().equals(keyResults.get(j).getCategoryId())
						&& keyResults.get(i).getKeyword().equals(keyResults.get(j).getKeyword())) {
						keyResults.remove(j);
						j--;
				}
			}
		}
		importContract.setCategories(catgResults);
		importContract.setKeywords(keyResults);

		return importContract;
	}
}
