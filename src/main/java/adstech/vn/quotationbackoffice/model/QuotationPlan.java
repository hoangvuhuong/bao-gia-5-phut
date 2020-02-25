package adstech.vn.quotationbackoffice.model;

import java.util.List;

public class QuotationPlan {
	private Long id;
	private Long quotation_id;
	private String name;
	private List<QuotationAdsType> type;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getQuotation_id() {
		return quotation_id;
	}

	public void setQuotation_id(Long quotation_id) {
		this.quotation_id = quotation_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<QuotationAdsType> getType() {
		return type;
	}

	public void setType(List<QuotationAdsType> type) {
		this.type = type;
	}

	public float getS() {
		float S=0;
		for(QuotationAdsType typei :type) {
			S+=typei.getS1();
		}
		return S;
	}
	public int getImplCostPercent() {
		float sum = getS();
		if(sum < 50000000f) {
			return 20;
		} else if (sum < 100000000){
			return 17;
		} else if (sum < 300000000f) {
			return 15;
		} else {
			return 12;
		}
	}
	public float getImplCost() {
		return getImplCostPercent() * getS() / 100;
	}
	
//	public float getFCT() {
//		return getS() / (1 - 0.05f) * 0.05f;
//	}
	
	public float getTotalWithoutVAT() {
		return  getImplCost() + getS();
	}
	
	public float getVAT() {
		return getTotalWithoutVAT() / 10;
	}
	
	public float getTotal() {
		return getTotalWithoutVAT() + getVAT();
	}
	public float getTotalWithoutVatFct() {
		return getImplCost() + getS();
	}
}
