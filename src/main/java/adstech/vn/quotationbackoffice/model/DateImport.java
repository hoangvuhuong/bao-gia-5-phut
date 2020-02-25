package adstech.vn.quotationbackoffice.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

public class DateImport implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private LocalDate timeImport;
	public LocalDate getTimeImport() {
		return timeImport;
	}
	public void setTimeImport(LocalDate timeImport) {
		this.timeImport = timeImport;
	}
	private int id;
	private int count;
	
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
}
