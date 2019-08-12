package com.systex.b300.vbooking.service;

import java.util.List;

import com.systex.b300.vbooking.dao.RestaurantDao;
import com.systex.b300.vbooking.vo.BookingVo;
import com.systex.b300.vbooking.vo.RestaurantVo;

public class RestaurantService {
	
	RestaurantDao dao;
	
	
	private RestaurantDao getDao() throws Exception{
		if (dao == null){
			dao = new RestaurantDao();
		}
		return dao;
	}
	
			
	public RestaurantVo searchRestaurantByName(String name) throws Exception{
		String id = getDao().searchRestaurantByName(name);
		if(id == null) return null;
		return getDao().qryRestaurantDetail(id);
	}
	
	
	public RestaurantVo qryRestaurantOpts(String id) throws Exception{
		System.out.println("qryRestaurantOpts:"+id);
//		String id = getDao().searchRestaurantByName(name);
		if(id == null) return null;
		RestaurantVo vo = getDao().qryRestaurantOpts(id);
		return vo;
	}
	public List<RestaurantVo> qryRestaurantSeats(String id,String date) throws Exception{
		System.out.println("qryRestaurantSeats:"+id);
		if(id == null) return null;
		return getDao().qryRestaurantSeats(id,date);

	}
	
	public RestaurantVo qryRestaurantDetail(String id)throws Exception{
		return getDao().qryRestaurantDetail(id);

	}
	
	public RestaurantVo qryRestaurantbBySIP(String sipNo)throws Exception{
		RestaurantVo vo =  getDao().qryRestaurantbBySIP(sipNo);
		return vo;

	}
	
}
