package adstech.vn.quotationbackoffice.model;

import java.util.List;

public class QuotationAdsType {
	private Long id;
	private Long quotationPlanId;
	private String type;
	private String target;
	private String charged;
	private Long price;
	private Long display;
	private Float ctr;
	private Long asdTypeId;
	private Float maxCpc;
	private List<QuotationAdsTypeKeyword> keywords;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getQuotationPlanId() {
		return quotationPlanId;
	}

	public void setQuotationPlanId(Long quotationPlanId) {
		this.quotationPlanId = quotationPlanId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public String getCharged() {
		return charged;
	}

	public void setCharged(String charged) {
		this.charged = charged;
	}

	public Long getPrice() {
		return price;
	}

	public void setPrice(Long price) {
		this.price = price;
	}

	public Long getDisplay() {
		return display;
	}

	public void setDisplay(Long display) {
		this.display = display;
	}

	public Float getCtr() {
		return ctr;
	}

	public void setCtr(Float ctr) {
		this.ctr = ctr;
	}
	public Long getAsdTypeId() {
		return asdTypeId;
	}

	public void setAsdTypeId(Long asdTypeId) {
		this.asdTypeId = asdTypeId;
	}
	public Float getKpi() {
		return display * ctr / 100;
	}
	public float getS1() {
		return getKpi() * price;
	}

	public List<QuotationAdsTypeKeyword> getKeywords() {
		return keywords;
	}

	public void setKeywords(List<QuotationAdsTypeKeyword> keywords) {
		this.keywords = keywords;
	}

	public Float getMaxCpc() {
		return maxCpc;
	}

	public void setMaxCpc(Float maxCpc) {
		this.maxCpc = maxCpc;
	}

	
}
