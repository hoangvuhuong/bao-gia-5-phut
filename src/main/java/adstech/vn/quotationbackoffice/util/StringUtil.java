package adstech.vn.quotationbackoffice.util;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;



public class StringUtil {
	public static String removeAccent(String s) {
		  
		  String temp = Normalizer.normalize(s, Normalizer.Form.NFD);
		  Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
		  return pattern.matcher(temp).replaceAll("").replace('đ','d').replace('Đ','D');
		 }
	public static List<String> removeArrayAccent(List<String> s){
		List<String> arr = new ArrayList<String>();
		for(String x : s) {
			arr.add(removeAccent(x));
		}
		return arr;
	}
}
