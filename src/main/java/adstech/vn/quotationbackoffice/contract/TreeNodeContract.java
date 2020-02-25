package adstech.vn.quotationbackoffice.contract;

public class TreeNodeContract {
	private String id;
	private String icon;
	private String text;
	private Boolean children;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public Boolean getChildren() {
		return children;
	}
	public void setChildren(Boolean children) {
		this.children = children;
	}
}
