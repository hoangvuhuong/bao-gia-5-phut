package adstech.vn.quotationbackoffice.contract;

import java.util.List;

import adstech.vn.quotationbackoffice.model.Category;

public class CategoryContract extends Category{

	private static final long serialVersionUID = 1L;
	
	private List<CategoryContract> child;

	public List<CategoryContract> getChild() {
		return child;
	}

	public void setChild(List<CategoryContract> child) {
		this.child = child;
	}

}
