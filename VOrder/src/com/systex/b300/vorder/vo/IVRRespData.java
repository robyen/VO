package com.systex.b300.vorder.vo;

public class IVRRespData extends BaseVo {
	String callerId;
	String uid;
	String status;
	String respText;
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
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getRespText() {
		return respText;
	}
	public void setRespText(String respText) {
		this.respText = respText;
	}
	
	
}
