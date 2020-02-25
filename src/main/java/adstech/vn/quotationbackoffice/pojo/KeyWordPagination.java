/**
 * 
 */
package adstech.vn.quotationbackoffice.pojo;

/**
 * @author toduc
 *
 */
public class KeyWordPagination {
private int page;
private int size;
public int getPage() {
	return page;
}
public void setPage(int page) {
	this.page = page;
}
public int getSize() {
	return size;
}
public void setSize(int size) {
	this.size = size;
}
public KeyWordPagination(int page, int size) {
	super();
	this.page = page;
	this.size = size;
}

}
