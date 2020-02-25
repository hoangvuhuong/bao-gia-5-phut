package adstech.vn.quotationbackoffice.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import adstech.vn.quotationbackoffice.model.QuotationGeoarea;
import adstech.vn.quotationbackoffice.service.IQuotationGeoareaService;

@Controller
public class QuotationGeoareaController {
	@Autowired
	IQuotationGeoareaService quotationGeoareaService;
	@GetMapping(path = "/geoarea")
	public String indexGeoara(Model model) {
		model.addAttribute("geoarea", quotationGeoareaService.getAll());
		return "geoarea/index";
	}
	@PostMapping(path = "/geoarea")
	public ResponseEntity<?> createQuotation(@RequestBody List<QuotationGeoarea> quotationGeoareas) {
		
		return ResponseEntity.ok(quotationGeoareaService.create(quotationGeoareas));
	}
	@PostMapping(path = "/geoarea/getByCriteriaId")
	public ResponseEntity<?> getByCriteriaId(@RequestBody Map<String,Object> geoareas) {
		HashMap<String, Object> result = new HashMap<String, Object>();
		try {
			List<QuotationGeoarea> quotationGeoarea = quotationGeoareaService.getByCriteriaId(geoareas.get("geoArea").toString());
			
			result.put("data", quotationGeoarea);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ResponseEntity.ok(result);
	}
}
