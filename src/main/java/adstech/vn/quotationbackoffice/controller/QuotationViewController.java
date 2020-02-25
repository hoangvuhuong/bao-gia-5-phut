package adstech.vn.quotationbackoffice.controller;

import java.io.IOException;
import java.security.Principal;

import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import adstech.vn.quotationbackoffice.model.Quotation;
import adstech.vn.quotationbackoffice.service.IKeywordService;
import adstech.vn.quotationbackoffice.service.IQuotationEmailApproverService;
import adstech.vn.quotationbackoffice.service.IQuotationEmailSuggestService;
import adstech.vn.quotationbackoffice.service.IQuotationGeoareaService;
import adstech.vn.quotationbackoffice.service.IQuotationService;

@Controller
@RequestMapping(value = "/quotation")
public class QuotationViewController {

	@Autowired
	IQuotationService quotationService;
	
	@Autowired
	IQuotationEmailApproverService quotationEmailApproverService;
	@Autowired
	IQuotationEmailSuggestService quotationEmailSuggestService;
	@Autowired
	IQuotationGeoareaService quotationGeoareaService;
	@Autowired
	IKeywordService keywordSerivce;
	
	@GetMapping(path = "/step-1")
	public String step1(Model model, @RequestParam(required = false) Long id) {
		if (id != null) {
			model.addAttribute("quotation", quotationService.getQuotation(id));
		} else {
			Quotation quotation_empty=new Quotation();
			quotation_empty.setGeoArea("");
			model.addAttribute("quotation", quotation_empty);
		}
		model.addAttribute("emailApprover", quotationEmailApproverService.getListEmailApprover("HANOI"));
		model.addAttribute("emailSuggest", quotationEmailSuggestService.getEmailSuggest("HANOI"));
		model.addAttribute("geoareas", quotationGeoareaService.getAll());
		 return "quotation/quotation-step-1";
	}

	@GetMapping(path = "/my-quotations")
	public String myQuotation(Model model) {
		return "quotation/my-quotations";
	}

	@GetMapping(path = "/approval-quotations")
	public String approvalQuotation(Model model) {
		return "quotation/approval-quotations";
	}
	
	@GetMapping(path="/exportReport/view")
	public String viewReport(Model model, @RequestParam(required = false) Long id,Principal principal,HttpServletResponse response) throws IOException {
		if (id != null) {
			Quotation quotation =quotationService.getQuotation(id);
			String user=principal.getName();
			
			if(!user.equals(quotation.getApprover())&&!quotationEmailApproverService.getListEmailApprover("HANOI").contains(user)) {
				model.addAttribute("hasPermission",false);
				
			}
			else {
				model.addAttribute("hasPermission",true);
				model.addAttribute("quotation",quotation);
			}
			
		} else {
			model.addAttribute("quotation", new Quotation());
			
		}
    	return "quotation/viewReport";
    	
	}
	@GetMapping("/statistic-keyword")
	public String statisticKeyword(Model model) {
		model.addAttribute("statistic",keywordSerivce.statisticKeyworkByUser());
		return "keywords/statistic-keyword";
	}
	
	
}
