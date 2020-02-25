package adstech.vn.quotationbackoffice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;

import com.google.api.services.sheets.v4.SheetsScopes;

import adstech.vn.quotationbackoffice.contract.CustomUserDetail;
import adstech.vn.quotationbackoffice.service.IUserService;

@Controller
public class Home {

	@Autowired
	private Environment env;

	@Autowired
	IUserService userService;

	@GetMapping(path = "/")
	public String home() {
		return "home";
	}

	@GetMapping(path = "/keywords")
	public String keywords(Model model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		CustomUserDetail userDetail = (CustomUserDetail) auth.getPrincipal();
		if (StringUtils.isEmpty(userDetail.getGgAccessToken())) {
			model.addAttribute("googleLoginUrl",
					"https://accounts.google.com/o/oauth2/auth?access_type=offline&client_id="
							+ env.getProperty("google.app.id")
							+ "&redirect_uri=http://localhost:8282/Callback&response_type=code&scope="
							+ SheetsScopes.SPREADSHEETS_READONLY);
		}
		return "keywords/keywords";
	}

	@GetMapping(path = "/admin")
	public String admin(Model model) {
		model.addAttribute("users", userService.getAllUser());
		model.addAttribute("roles", userService.getAllRole());
		return "admin";
	}
}
