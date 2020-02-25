package adstech.vn.quotationbackoffice.util;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.ValueRange;

import adstech.vn.quotationbackoffice.contract.CustomUserDetail;
import adstech.vn.quotationbackoffice.contract.ImportContract;
import adstech.vn.quotationbackoffice.model.Category;
import adstech.vn.quotationbackoffice.model.Keyword;

public class SheetsServiceUtil {

	private static final String APPLICATION_NAME = "Google Sheets Example";

	private static final String GG_SHEET_URL = "https://docs.google.com/spreadsheets/d/";

	private static Sheets sheets = null;

	public static Sheets getSheetsService() throws IOException, GeneralSecurityException {
		if (sheets == null) {
//			Credential credential = new Credential(BearerToken.AuthorizationHeaderAccessMethod);
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			CustomUserDetail userDetail = (CustomUserDetail) auth.getPrincipal();
			if (StringUtils.isEmpty(userDetail.getGgAccessToken())) {
				Credential credential = GoogleAuthorizeUtil.authorize();
				return new Sheets.Builder(GoogleNetHttpTransport.newTrustedTransport(),
						JacksonFactory.getDefaultInstance(), credential).setApplicationName(APPLICATION_NAME).build();
			}
			GoogleCredential credential = new GoogleCredential().setAccessToken(userDetail.getGgAccessToken());
			return new Sheets.Builder(GoogleNetHttpTransport.newTrustedTransport(), JacksonFactory.getDefaultInstance(),
					credential).setApplicationName(APPLICATION_NAME).build();
		} else {
			return sheets;
		}
	}

	public static ImportContract importFromSpeadSheet(String sheetUrl) throws IOException {
		String sheetId = sheetUrl.replace(GG_SHEET_URL, "").split("/")[0];

		try {
			ValueRange response = getSheetsService().spreadsheets().values().get(sheetId, "Phân tích Keywords")
					.execute();
			List<List<Object>> values = response.getValues();

			List<HashMap<String, String>> listCategories = new ArrayList<HashMap<String, String>>();
			List<HashMap<String, List<String>>> listMaps = new ArrayList<HashMap<String, List<String>>>();

			List<Category> catgResults = new ArrayList<Category>();
			List<Keyword> keyResults = new ArrayList<Keyword>();
			int depth = 0;
			for (int i = 0; true; i++) {
				depth++;
				List<Object> row = values.get(15);
				if (row.get(i).equals("Loại từ khóa")) {
					continue;
				}
				listMaps.add(new HashMap<>());
				if (row.get(i).equals("Từ khóa")) {
					break;
				}

				HashMap<String, String> node = new HashMap<>();
				listCategories.add(node);
			}
			for (int r = 17; true; r++) {
				List<Object> row = values.get(r);

				if (row == null || row.isEmpty() || (row.get(0) == null || row.get(0).toString().isEmpty())
						|| (row.get(1) == null || row.get(1).toString().isEmpty())
						|| (row.get(3) == null || row.get(3).toString().isEmpty())) {
					break;
				}

				String key = "";
				for (int i = 0; i < depth; i++) {
					if (i == depth - 2) {
						continue;
					}
					Object c = row.get(i);
					String parentKey = key;
					key += c.toString();
					if (i < depth - 1) {

						if (listMaps.get(i).get(c.toString()) == null
								|| !listMaps.get(i).get(c.toString()).contains(key)) {
							Category catg = new Category();
							catg.setCategoryId(UUID.randomUUID().toString());
							catg.setCategoryName(c.toString().trim());
							catg.setDepthLevel(i);
							listCategories.get(i).put(key, catg.getCategoryId());
							if (i > 0) {
								catg.setParentId(listCategories.get(i - 1).get(parentKey));
							}
							List<String> temp = listMaps.get(i).get(c.toString());
							if (temp == null) {
								temp = new ArrayList<String>();
							}
							temp.add(key);
							listMaps.get(i).put(c.toString(), temp);
							catgResults.add(catg);
						}
					} else {
						if (listMaps.get(i - 1).get(c.toString()) == null
								|| !listMaps.get(i - 1).get(c.toString()).contains(key)) {
							Keyword keyword = new Keyword();
							keyword.setKeywordId(UUID.randomUUID().toString());
							keyword.setKeyword(c.toString().trim());
							keyword.setCategoryId(listCategories.get(i - 2).get(parentKey));

							List<String> temp = listMaps.get(i - 1).get(c.toString());
							if (temp == null) {
								temp = new ArrayList<String>();
							}
							temp.add(key);
							listMaps.get(i - 1).put(c.toString(), temp);

							keyResults.add(keyword);
						}
					}
				}
			}

			ImportContract importContract = new ImportContract();
			importContract.setCategories(catgResults);
			importContract.setKeywords(keyResults);

			return importContract;
		} catch (GeneralSecurityException e) {
			e.printStackTrace();
			return new ImportContract();
		}
	}

//	public static void main(String[] args) {
//		try {
//			importFromSpeadSheet(
//					"https://docs.google.com/spreadsheets/d/1TrEuDpwx0wiZAZOsdXWBReyLpavSjwWOb3tUa6ylSew/edit#gid=634347005");
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}

}