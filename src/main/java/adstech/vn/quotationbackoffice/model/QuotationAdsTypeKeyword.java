package adstech.vn.quotationbackoffice.model;

public class QuotationAdsTypeKeyword {
	private Double averageCpc;
    private Long cost;
    private Double ctr;
    private Double dailyClicks;
    private Double dailyImpressions;
    private Long adsTypeId;
    private Long quotationKeywordId;
    private String keyword;
	public Double getAverageCpc() {
		return averageCpc;
	}
	public void setAverageCpc(Double averageCpc) {
		this.averageCpc = averageCpc;
	}
	public Long getCost() {
		return cost;
	}
	public void setCost(Long cost) {
		this.cost = cost;
	}
	public Double getCtr() {
		return ctr;
	}
	public void setCtr(Double ctr) {
		this.ctr = ctr;
	}
	public Double getDailyClicks() {
		return dailyClicks;
	}
	public void setDailyClicks(Double dailyClicks) {
		this.dailyClicks = dailyClicks;
	}
	public Double getDailyImpressions() {
		return dailyImpressions;
	}
	public void setDailyImpressions(Double dailyImpressions) {
		this.dailyImpressions = dailyImpressions;
	}
	public Long getAdsTypeId() {
		return adsTypeId;
	}
	public void setAdsTypeId(Long adsTypeId) {
		this.adsTypeId = adsTypeId;
	}
	public Long getQuotationKeywordId() {
		return quotationKeywordId;
	}
	public void setQuotationKeywordId(Long quotationKeywordId) {
		this.quotationKeywordId = quotationKeywordId;
	}
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
}
