package com.systex.b300.vbooking.service;

public class BaseService {

	protected int getIntTime(String time){
		if(time == null || time.isEmpty() || time.length() == 0) return 0;
		return Integer.parseInt(time.replaceAll(":", ""));
	}
 
	protected boolean isBlank(String data){
		if(data == null) return true;
		if(data.length() == 0) return true;
		if("　".equals(data)) return true; 
		return false;
	}
	 
}
