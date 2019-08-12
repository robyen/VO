package com.systex.b300.vbooking.vo.inline;

import java.sql.Timestamp;

import com.google.gson.annotations.SerializedName;
import com.systex.b300.vbooking.vo.BaseVo;

public class BookingInlineVo extends BaseVo{
	
	String note;
	String uuid;
	String meta_uuid;
	
	
	@SerializedName("group_num")
	int groupNum;
	
	@SerializedName("kid_num")
	int kidNum;
	
	@SerializedName("chair_num")
	int chairNum;
	
	String date;
	String time;

	
//	@SerializedName("time_date")
//	String date;
//	
//	@SerializedName("time_time")
//	String time;	
//	
//	@SerializedName("seat_num")
//	int groupNum;
//	
//	@SerializedName("seat_num_children")
//	int kidNum;
//	
//	@SerializedName("chair_num")
//	int chairNum;
//		

	
	
	@SerializedName("restaurant_id")
	String restaurantId;
	
	@SerializedName("restaurant_name")
	String restaurantName;
	
	@SerializedName("lastname")	
	String lastName;
	
//	@SerializedName("time_real")
//	int timeReal;
	
	@SerializedName("booking_status")
	String bookingStatus;
	
//	@SerializedName("waiting_order")
//	int waitingOrder;
	
	String phone;
	
	@SerializedName("create_date")
	Timestamp createDate;

	@SerializedName("update_date")
	Timestamp updateDate;
	
	@SerializedName("from_sip")
	String fromSip;

	@SerializedName("caller_id")
	String callerId;
	
	@SerializedName("end_status")
	String endStatus;

	String reservationId;
	String customerId;
	String reservationLink;
	int inlineCode;
	String inlineReason;
	String inlineMessage;
	
	
	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public int getGroupNum() {
		return groupNum;
	}

	public void setGroupNum(int groupNum) {
		this.groupNum = groupNum;
	}

	public int getKidNum() {
		return kidNum;
	}

	public void setKidNum(int kidNum) {
		this.kidNum = kidNum;
	}

	public int getChairNum() {
		return chairNum;
	}

	public void setChairNum(int chairNum) {
		this.chairNum = chairNum;
	}

	public String getRestaurantId() {
		return restaurantId;
	}

	public void setRestaurantId(String restaurantId) {
		this.restaurantId = restaurantId;
	}
	
	public String getRestaurantName() {
		return restaurantName;
	}

	public void setRestaurantName(String restaurantName) {
		this.restaurantName = restaurantName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getBookingStatus() {
		return bookingStatus;
	}

	public void setBookingStatus(String bookingStatus) {
		this.bookingStatus = bookingStatus;
	}

//	public int getWaitingOrder() {
//		return waitingOrder;
//	}
//
//	public void setWaitingOrder(int waitingOrder) {
//		this.waitingOrder = waitingOrder;
//	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Timestamp getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Timestamp createDate) {
		this.createDate = createDate;
	}

	public Timestamp getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Timestamp updateDate) {
		this.updateDate = updateDate;
	}

	public String getFromSip() {
		return fromSip;
	}

	public void setFromSip(String fromSip) {
		this.fromSip = fromSip;
	}

	public String getCallerId() {
		return callerId;
	}

	public void setCallerId(String callerId) {
		this.callerId = callerId;
	}


	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getEndStatus() {
		return endStatus;
	}

	public void setEndStatus(String endStatus) {
		this.endStatus = endStatus;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getReservationId() {
		return reservationId;
	}

	public void setReservationId(String reservationId) {
		this.reservationId = reservationId;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getReservationLink() {
		return reservationLink;
	}

	public void setReservationLink(String reservationLink) {
		this.reservationLink = reservationLink;
	}

	public int getInlineCode() {
		return inlineCode;
	}

	public void setInlineCode(int inlineCode) {
		this.inlineCode = inlineCode;
	}

	public String getInlineReason() {
		return inlineReason;
	}

	public void setInlineReason(String inlineReason) {
		this.inlineReason = inlineReason;
	}

	public String getInlineMessage() {
		return inlineMessage;
	}

	public void setInlineMessage(String inlineMessage) {
		this.inlineMessage = inlineMessage;
	}

	public String getMeta_uuid() {
		return meta_uuid;
	}

	public void setMeta_uuid(String meta_uuid) {
		this.meta_uuid = meta_uuid;
	}
	
	


}
