package adstech.vn.quotationbackoffice.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.client.ClientProtocolException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.google.api.services.sheets.v4.SheetsScopes;

import adstech.vn.quotationbackoffice.contract.CustomUserDetail;
import adstech.vn.quotationbackoffice.contract.GooglePojo;
import adstech.vn.quotationbackoffice.util.GoogleUtils;

@Controller
public class LoginController {

	@Autowired
	private Environment env;

	@Autowired
	private GoogleUtils googleUtils;

	@GetMapping("/login")
	public String login(Model model, HttpServletRequest request) {
		String googleLoginUrl = env.getProperty("google.link.get.authen") + "?scope=email "
				+ SheetsScopes.SPREADSHEETS_READONLY + "&redirect_uri=" + env.getProperty("google.redirect.uri")
				+ "&response_type=code&client_id=" + env.getProperty("google.app.id") + "&approval_prompt=auto";
		model.addAttribute("googleLoginUrl", googleLoginUrl);
		return "login";
	}

	@GetMapping("/login-google")
	public String loginGoogle(HttpServletRequest request, HttpServletResponse response)
			throws ClientProtocolException, IOException {
		String code = request.getParameter("code");

		if (code == null || code.isEmpty()) {
			return "redirect:/login?google=error";
		}
		String accessToken = googleUtils.getToken(code);

		GooglePojo googlePojo = googleUtils.getUserInfo(accessToken);
		CustomUserDetail userDetail = googleUtils.buildUser(googlePojo);
		userDetail.setGgAccessToken(accessToken);
		UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetail, null,
				userDetail.getAuthorities());
		authentication.setDetails(userDetail);
		authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
		SecurityContextHolder.getContext().setAuthentication(authentication);
		RequestCache cache = new HttpSessionRequestCache();
		SavedRequest savedRequest = cache.getRequest(request, response);
		if(savedRequest != null) {
		String targetUrl = savedRequest.getRedirectUrl();
		return "redirect:" + targetUrl;
		}
		return "redirect:/";
	}

	@GetMapping("/403")
	public String accessDenied() {
		SecurityContextHolder.getContext().setAuthentication(null);
		return "error/403";
	}

}
