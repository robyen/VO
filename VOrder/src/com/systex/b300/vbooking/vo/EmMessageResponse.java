package com.systex.b300.vbooking.vo;

import com.google.gson.annotations.SerializedName;

public class EmMessageResponse<T> extends BaseVo{
	T update;
	@SerializedName("status_code")	
	String statusCode;
	
	@SerializedName("status_msg")		
	String statusMsg;

	public T getUpdate() {
		return update;
	}

	public void setUpdate(T update) {
		this.update = update;
	}

	public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	public String getStatusMsg() {
		return statusMsg;
	}

	public void setStatusMsg(String statusMsg) {
		this.statusMsg = statusMsg;
	}
	
	
}
