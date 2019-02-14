package com.systex.b300.vbooking.vo;

import com.google.gson.annotations.SerializedName;

public class EmFlowRespVo<T> extends BaseVo{
	String status_code;
	
	@SerializedName("msg_response")	
	EmMessageResponse<T> msgResponse;
	
	public String getStatus_code() {
		return status_code;
	}
	public void setStatus_code(String status_code) {
		this.status_code = status_code;
	}
	public EmMessageResponse<T> getMsgResponse() {
		return msgResponse;
	}
	public void setMsgResponse(EmMessageResponse<T> msgResponse) {
		this.msgResponse = msgResponse;
	}


}
