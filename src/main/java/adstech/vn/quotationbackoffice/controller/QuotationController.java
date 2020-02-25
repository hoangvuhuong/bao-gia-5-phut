package adstech.vn.quotationbackoffice.controller;

import java.net.URL;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import adstech.vn.quotationbackoffice.model.Quotation;
import adstech.vn.quotationbackoffice.model.QuotationUpdate;
import adstech.vn.quotationbackoffice.service.IQuotationEmailApproverService;
import adstech.vn.quotationbackoffice.service.IQuotationService;

@RestController
@RequestMapping("/quotations")
public class QuotationController {

	@Autowired
	IQuotationService quotationService;
	@Autowired
	IQuotationEmailApproverService quotationEmailApproverService;
	
	@GetMapping("/my-quotations")
	public ResponseEntity<?> getMyQuotation(@RequestParam(required = false) Integer p,
			@RequestParam(required = false) Integer s, Principal principal) {
		HashMap<String, Object> result = new HashMap<String, Object>();
		try {
			List<Quotation> quotations = quotationService.getQuotationByCreater(principal.getName(), p, s);
			
			result.put("data", quotations);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return ResponseEntity.ok(result);
	}

	@GetMapping("/my-approval-requests")
	public ResponseEntity<?> getMyApprovalRequest(@RequestParam(required = false) Integer p,
			@RequestParam(required = false) Integer s, Principal principal) {
		HashMap<String, Object> result = new HashMap<String, Object>();
		try {
			
			List<Quotation> quotations = quotationService.getQuotationByApprover(principal.getName(), p, s,quotationEmailApproverService.getListEmailApprover("HANOI"));
			result.put("data", quotations);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ResponseEntity.ok(result);
	}

	@PostMapping
	public ResponseEntity<?> createQuotation(@RequestBody QuotationUpdate quotation, Principal principal) {
		quotation.setCreatedBy(principal.getName());
		quotation.setUpdatedBy(principal.getName());
		return ResponseEntity.ok(quotationService.createQuotation(quotation));
	}

	@PostMapping("/submit")
	public ResponseEntity<?> submitQuotation(@RequestBody QuotationUpdate quotation, Principal principal,
			HttpServletRequest request) {
		quotation.setCreatedBy(principal.getName());
		quotation.setUpdatedBy(principal.getName());

		String url = getURLBase(request) + "/quotation/approval-quotations";
		return ResponseEntity.ok(quotationService.submitQuotation(quotation, url));
	}

	@GetMapping("/{quotationId}/approval")
	public ResponseEntity<?> approveQuotation(@PathVariable Long quotationId, Principal principal) {
		return ResponseEntity.ok(quotationService.approveQuotation(quotationId, principal.getName()));
	}

	@PutMapping("/{quotationId}/rejection")
	public ResponseEntity<?> rejectQuotation(@PathVariable Long quotationId, @RequestBody Quotation quotation,
			Principal principal, HttpServletRequest request) {
		quotation.setQuotationId(quotationId);
		quotation.setUpdatedBy(principal.getName());

		String url = getURLBase(request) + "/quotation/step-1?id=" + quotationId;
		return ResponseEntity.ok(quotationService.rejectQuotation(quotation, url));
	}

	@GetMapping("/{quotationId}/submit")
	public ResponseEntity<?> resubmitQuotation(@PathVariable Long quotationId, Principal principal,
			HttpServletRequest request) {
		String url = getURLBase(request) + "/quotation/approval-quotations";
		return ResponseEntity.ok(quotationService.submitQuotation(quotationId, principal.getName(), url));
	}

	public String getURLBase(HttpServletRequest request) {
		try {
			URL requestURL = new URL(request.getRequestURL().toString());
			String port = requestURL.getPort() == -1 ? "" : ":" + requestURL.getPort();
			return requestURL.getProtocol() + "://" + requestURL.getHost() + port;
		} catch (Exception e) {
			return null;
		}

	}

}
