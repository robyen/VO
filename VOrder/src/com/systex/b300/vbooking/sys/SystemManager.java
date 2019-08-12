package com.systex.b300.vbooking.sys;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.systex.b300.vbooking.vo.inline.RestaurantInlineVo;

public class SystemManager {

//	public static ThreadLocal<RestaurantInlineVo> restaurants = new ThreadLocal<RestaurantInlineVo>();
	public static Map<String,RestaurantInlineVo> restaurants = new HashMap<String,RestaurantInlineVo>();
	private static Logger log = LogManager.getLogger("sysLog");

	
	public static RestaurantInlineVo getRestaurant(String uid) throws Exception{
		if(restaurants.containsKey(uid)){
			return restaurants.get(uid);
		}
		return null;
	}
	
	public static void setRestaurant(String uid,RestaurantInlineVo vo) throws Exception{
		log.info("setRestaurant uid ==>"+uid);
//		log.debug("setRestaurant RestaurantInlineVo ==>"+vo);
		
		restaurants.put(uid, vo);
	}
	
	public static void removeRestaurant(String uid)throws Exception{
		log.info("removeRestaurant uid ==>"+uid);
		restaurants.remove(uid);
	}
	
}
