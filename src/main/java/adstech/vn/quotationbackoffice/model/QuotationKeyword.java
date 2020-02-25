package adstech.vn.quotationbackoffice.model;

public class QuotationKeyword  implements Cloneable{
    
    private Long id;
    private String keyword;
    private Long quotationId;
    private Long campaignId;
    private Float maxCpc;
    private String finalUrl;
    private String finalMobileUrl;
    private String isIncurredKeyword;
    
	public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getKeyword() {
        return keyword;
    }
    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
    public Long getQuotationId() {
        return quotationId;
    }
    public void setQuotationId(Long quotationId) {
        this.quotationId = quotationId;
    }
    public Long getCampaignId() {
        return campaignId;
    }
    public void setCampaignId(Long campaignId) {
        this.campaignId = campaignId;
    }
    public Float getMaxCpc() {
        return maxCpc;
    }
    public void setMaxCpc(Float maxCpc) {
        this.maxCpc = maxCpc;
    }
    public String getFinalUrl() {
        return finalUrl;
    }
    public void setFinalUrl(String finalUrl) {
        this.finalUrl = finalUrl;
    }
    public String getFinalMobileUrl() {
        return finalMobileUrl;
    }
    public void setFinalMobileUrl(String finalMobileUrl) {
        this.finalMobileUrl = finalMobileUrl;
    }
    public String getIsIncurredKeyword() {
        return isIncurredKeyword;
    }
    public void setIsIncurredKeyword(String isIncurredKeyword) {
        this.isIncurredKeyword = isIncurredKeyword;
    }
    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
	
}