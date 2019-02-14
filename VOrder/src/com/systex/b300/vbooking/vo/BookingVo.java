package com.systex.b300.vbooking.vo;

import java.sql.Timestamp;

import com.google.gson.annotations.SerializedName;

public class BookingVo extends BaseVo{
	String id;
	
	String date;
	String time;

	@SerializedName("restaurant_id")
	String restaurantId;
	String name;
	
	@SerializedName("seat_num")
	int seatNum;

	@SerializedName("lastname")	
	String lastName;
	
	@SerializedName("time_real")
	int timeReal;
	
	@SerializedName("booking_status")
	String bookingStatus;
	
	@SerializedName("waiting_order")
	int waitingOrder;
	
	String phone;
	
	@SerializedName("opt1_yn")
	String opt1Yn;
	
	@SerializedName("opt1_num")
	String opt1Num;

	@SerializedName("opt2_yn")
	String opt2Yn;
	
	@SerializedName("opt2_num")
	String opt2Num;
	
	@SerializedName("opt3_yn")
	String opt3Yn;
	
	@SerializedName("opt3_num")
	String opt3Num;

	@SerializedName("restaurant_name")
	String restaurantName;
	
	@SerializedName("create_date")
	Timestamp createDate;

	@SerializedName("update_date")
	Timestamp updateDate;
	
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getRestaurantId() {
		return restaurantId;
	}

	public void setRestaurantId(String restaurantId) {
		this.restaurantId = restaurantId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getSeatNum() {
		return seatNum;
	}

	public void setSeatNum(int seatNum) {
		this.seatNum = seatNum;
	}

	public String getBookingStatus() {
		return bookingStatus;
	}

	public void setBookingStatus(String bookingStatus) {
		this.bookingStatus = bookingStatus;
	}

	public int getWaitingOrder() {
		return waitingOrder;
	}

	public void setWaitingOrder(int waitingOrder) {
		this.waitingOrder = waitingOrder;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getOpt1Yn() {
		return opt1Yn;
	}

	public void setOpt1Yn(String opt1Yn) {
		this.opt1Yn = opt1Yn;
	}

	public String getOpt1Num() {
		return opt1Num;
	}

	public void setOpt1Num(String opt1Num) {
		this.opt1Num = opt1Num;
	}

	public String getOpt2Yn() {
		return opt2Yn;
	}

	public void setOpt2Yn(String opt2Yn) {
		this.opt2Yn = opt2Yn;
	}

	public String getOpt2Num() {
		return opt2Num;
	}

	public void setOpt2Num(String opt2Num) {
		this.opt2Num = opt2Num;
	}

	public String getOpt3Yn() {
		return opt3Yn;
	}

	public void setOpt3Yn(String opt3Yn) {
		this.opt3Yn = opt3Yn;
	}

	public String getOpt3Num() {
		return opt3Num;
	}

	public void setOpt3Num(String opt3Num) {
		this.opt3Num = opt3Num;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public int getTimeReal() {
		return timeReal;
	}

	public void setTimeReal(int timeReal) {
		this.timeReal = timeReal;
	}

	public String getRestaurantName() {
		return restaurantName;
	}

	public void setRestaurantName(String restaurantName) {
		this.restaurantName = restaurantName;
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

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}




}
