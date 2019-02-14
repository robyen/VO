package com.systex.b300.vbooking.vo;

public class IVRRespData extends BaseVo {
	String callerId;
	String uid;
	String respCode;
	String respText;
	String status;
	String message;
	
	public String getCallerId() {
		return callerId;
	}
	public void setCallerId(String callerId) {
		this.callerId = callerId;
	}
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public String getRespText() {
		return respText;
	}
	public void setRespText(String respText) {
		this.respText = respText;
	}
	public String getRespCode() {
		return respCode;
	}
	public void setRespCode(String respCode) {
		this.respCode = respCode;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	
}
