package com.systex.b300.vbooking.vo.inline;

import java.sql.Timestamp;

import com.google.gson.annotations.SerializedName;
import com.systex.b300.vbooking.vo.BaseVo;

public class DoBookingRespVo extends BaseVo{
	

	@SerializedName("restaurant_id")
	String restaurantId;
	
	@SerializedName("booking_status")
	String bookingStatus;
	
	String time;
	
	@SerializedName("resp_text")
	String respText;

	public String getRestaurantId() {
		return restaurantId;
	}

	public void setRestaurantId(String restaurantId) {
		this.restaurantId = restaurantId;
	}

	public String getBookingStatus() {
		return bookingStatus;
	}

	public void setBookingStatus(String bookingStatus) {
		this.bookingStatus = bookingStatus;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getRespText() {
		return respText;
	}

	public void setRespText(String respText) {
		this.respText = respText;
	}
	

}
