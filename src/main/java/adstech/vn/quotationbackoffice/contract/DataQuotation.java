package adstech.vn.quotationbackoffice.contract;

import java.io.Serializable;

public class DataQuotation implements Serializable, Cloneable {
	
	private static final long serialVersionUID = 1L;

	private String url;
	private Long price;
	private Long display;
	private Float ctr;
	private Long price_re;
	private Long display_re;
	private Float ctr_re;
	private Long price_gdn;
	private Long display_gdn;
	private Float ctr_gdn;
	private Long price_shop;
	private Long display_shop;
	private Float ctr_shop;
	private String target;
	private String target_re;
	private String target_gdn;
	private String target_shop;
	private String inChargeUser;
	private String inChargeUsername;
	private String inChargeUserPhone;
	private String charged;
	private String charged_re;
	private String charged_gdn;
	private String charged_shop;
	private String inChargePartner;
	private String partner;
	
	public String getCharged() {
		return charged;
	}
	public void setCharged(String charged) {
		this.charged = charged;
	}
	public String getCharged_re() {
		return charged_re;
	}
	public void setCharged_re(String charged_re) {
		this.charged_re = charged_re;
	}
	public String getCharged_gdn() {
		return charged_gdn;
	}
	public void setCharged_gdn(String charged_gdn) {
		this.charged_gdn = charged_gdn;
	}
	public String getCharged_shop() {
		return charged_shop;
	}
	public void setCharged_shop(String charged_shop) {
		this.charged_shop = charged_shop;
	}
	public String getInChargeUser() {
		return inChargeUser;
	}
	public void setInChargeUser(String inChargeUser) {
		this.inChargeUser = inChargeUser;
	}
	public String getInChargeUsername() {
		return inChargeUsername;
	}
	public void setInChargeUsername(String inChargeUsername) {
		this.inChargeUsername = inChargeUsername;
	}
	public String getInChargeUserPhone() {
		return inChargeUserPhone;
	}
	public void setInChargeUserPhone(String inChargeUserPhone) {
		this.inChargeUserPhone = inChargeUserPhone;
	}
	public Long getDisplay_shop() {
		return display_shop;
	}
	public void setDisplay_shop(Long display_shop) {
		this.display_shop = display_shop;
	}
	public Float getKpi() {
		return display * ctr / 100;
	}
	
	public Float getKpi_re() {
		return display_re * ctr_re / 100;
	}
	
	public Float getKpi_gdn() {
		return display_gdn * ctr_gdn / 100;
	}
	public Float getKpi_shop() {
		return display_shop * ctr_shop / 100;
	}
	public float getS1() {
		return getKpi() * price;
	}
	public float getS2() {
		return getKpi_re() * price_re;
	}
	public float getS3() {
		return getKpi_gdn() * price_gdn;
	}
	public float getS4() {
		return getKpi_shop() * price_shop;
	}
	public String getTarget() {
		return target;
	}
	public void setTarget(String target) {
		this.target = target;
	}
	public String getTarget_re() {
		return target_re;
	}
	public void setTarget_re(String target_re) {
		this.target_re = target_re;
	}
	public String getTarget_gdn() {
		return target_gdn;
	}
	public void setTarget_gdn(String target_gdn) {
		this.target_gdn = target_gdn;
	}
	public String getTarget_shop() {
		return target_shop;
	}
	public void setTarget_shop(String target_shop) {
		this.target_shop = target_shop;
	}
	public Long getPrice_gdn() {
		return price_gdn;
	}
	public void setPrice_gdn(Long price_gdn) {
		this.price_gdn = price_gdn;
	}
	public Long getDisplay_gdn() {
		return display_gdn;
	}
	public void setDisplay_gdn(Long display_gdn) {
		this.display_gdn = display_gdn;
	}
	public Float getCtr_gdn() {
		return ctr_gdn;
	}
	public void setCtr_gdn(Float ctr_gdn) {
		this.ctr_gdn = ctr_gdn;
	}
	public Long getPrice_shop() {
		return price_shop;
	}
	public void setPrice_shop(Long price_shop) {
		this.price_shop = price_shop;
	}
	public Float getCtr_shop() {
		return ctr_shop;
	}
	public void setCtr_shop(Float ctr_shop) {
		this.ctr_shop = ctr_shop;
	}
	public Long getPrice() {
		return price;
	}
	public void setPrice(Long price) {
		this.price = price;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
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
	public Long getPrice_re() {
		return price_re;
	}
	public void setPrice_re(Long price_re) {
		this.price_re = price_re;
	}
	public Float getCtr_re() {
		return ctr_re;
	}
	public void setCtr_re(Float ctr_re) {
		this.ctr_re = ctr_re;
	}
	public Long getDisplay_re() {
		return display_re;
	}
	public void setDisplay_re(Long display_re) {
		this.display_re = display_re;
	}
	public float getS() {
		return getS1() + getS2() + getS3() + getS4();
	}
	public int getImplCostPercent() {
		float sum = getS();
		if(sum < 15000000f) {
			return 20;
		} else if (sum < 30000000f){
			return 17;
		} else if (sum < 100000000f) {
			return 15;
		} else {
			return 12;
		}
	}
	
	public float getImplCost() {
		return getImplCostPercent() * getS() / 100;
	}
	
	public float getFCT() {
		return getS() / (1 - 0.05f) * 0.05f;
	}
	
	public float getTotalWithoutVAT() {
		return getFCT() + getImplCost() + getS();
	}
	
	public float getVAT() {
		return getTotalWithoutVAT() / 10;
	}
	
	public float getTotal() {
		return getTotalWithoutVAT() + getVAT();
	}
	public String getInChargePartner() {
		return inChargePartner;
	}
	public void setInChargePartner(String inChargePartner) {
		this.inChargePartner = inChargePartner;
	}
	public String getPartner() {
		return partner;
	}
	public void setPartner(String partner) {
		this.partner = partner;
	}
}
