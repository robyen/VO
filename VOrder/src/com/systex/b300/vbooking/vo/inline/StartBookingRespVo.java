package com.systex.b300.vbooking.vo.inline;

import com.google.gson.annotations.SerializedName;

public class StartBookingRespVo {

	@SerializedName("restaurant_id")
	String restaurantId;
	
	@SerializedName("restaurant_name")
	String restaurantName;
	
	
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

}
