package com.systex.b300.vbooking.vo;

public class RestaurantTimeVo extends RestaurantVo{
	String timeDate;
	int timeStart;
	int timeEnd;
	int timeNum;
	int timeNumUsed;
	
	public String getTimeDate() {
		return timeDate;
	}
	public void setTimeDate(String timeDate) {
		this.timeDate = timeDate;
	}
	public int getTimeStart() {
		return timeStart;
	}
	public void setTimeStart(int timeStart) {
		this.timeStart = timeStart;
	}
	public int getTimeEnd() {
		return timeEnd;
	}
	public void setTimeEnd(int timeEnd) {
		this.timeEnd = timeEnd;
	}
	public int getTimeNum() {
		return timeNum;
	}
	public void setTimeNum(int timeNum) {
		this.timeNum = timeNum;
	}
	public int getTimeNumUsed() {
		return timeNumUsed;
	}
	public void setTimeNumUsed(int timeNumUsed) {
		this.timeNumUsed = timeNumUsed;
	}
	

	
}
