package com.systex.b300.vbooking.sys;

import java.io.FileOutputStream;

import com.systex.b300.vbooking.service.InlineService;
import com.systex.b300.vbooking.vo.RestaurantVo;
import com.systex.b300.vbooking.vo.inline.RestaurantInlineVo;

public class InLineThread extends Thread{
	
	RestaurantVo restVo; 
	String uid;

	public InLineThread(String uid,RestaurantVo restVo){
		this.uid = uid;
		this.restVo = restVo;
		
	}
	
	@Override
	public void run() {
		try{
			InlineService service = new InlineService();

			RestaurantInlineVo vo = service.qryInLineRestaurant(restVo);
			SystemManager.setRestaurant(uid,vo);

		}catch(Exception e){

		}
	}
	
}
