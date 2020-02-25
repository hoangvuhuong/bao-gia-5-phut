package adstech.vn.quotationbackoffice.model;

import java.util.List;

public class QuotationUpdate extends Quotation{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private List<Long> planID;
	private List<Long> typeAds;
	private List<Long> keyWordID;
	public List<Long> getPlanID() {
		return planID;
	}

	public void setPlanID(List<Long> planID) {
		this.planID = planID;
	}

	public List<Long> getTypeAds() {
		return typeAds;
	}

	public void setTypeAds(List<Long> typeAds) {
		this.typeAds = typeAds;
	}

	public List<Long> getKeyWordID() {
		return keyWordID;
	}

	public void setKeyWordID(List<Long> keyWordID) {
		this.keyWordID = keyWordID;
	}
	
}
