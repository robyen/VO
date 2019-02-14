package com.systex.b300.vbooking.vo.inline;

import com.systex.b300.vbooking.vo.BaseVo;

public class ReservationRespVo extends BaseVo{
	String reservationId;
	String reservationLink;
	String customerId;
	int code;
	String message;
	String reason;
	
	public String getReservationId() {
		return reservationId;
	}
	public void setReservationId(String reservationId) {
		this.reservationId = reservationId;
	}
	public String getReservationLink() {
		return reservationLink;
	}
	public void setReservationLink(String reservationLink) {
		this.reservationLink = reservationLink;
	}
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	
	
	
}
