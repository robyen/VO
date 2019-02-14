package com.systex.b300.vbooking.dao;

import java.util.ArrayList;
import java.util.List;

import com.systex.b300.vbooking.vo.BookingVo;
import com.systex.b300.vbooking.vo.RestaurantVo;

public class RestaurantDao extends BaseDao{
	
	public String searchRestaurantByName(String name) throws Exception{
		StringBuffer sqls = new StringBuffer();
		sqls.append("SELECT RESTAURANT_ID FROM RESTAURANT_NAMES ")
			.append("WHERE NAME = ? " );
		Object obj = super.executeQrySingleData(sqls.toString(), "RESTAURANT_ID", name);
		
		return obj == null ? null : String.valueOf(obj);
	}
	
	
	public RestaurantVo qryRestaurantDetail(String id) throws Exception{
		StringBuffer sqls = new StringBuffer();
		sqls.append("SELECT * FROM RESTAURANT ")
			.append("WHERE RESTAURANT_ID = ? " );
		RestaurantVo vo = super.executeQry(RestaurantVo.class,sqls.toString(), id);
		
		return vo;
		
	}
	
	public RestaurantVo qryRestaurantOpts(String id) throws Exception{
		StringBuffer sqls = new StringBuffer();
		sqls.append("SELECT RESTAURANT_ID,OPT1_DESC,OPT1_RESP,OPT1_NUM_DESC,OPT2_DESC,OPT2_RESP,OPT2_NUM_DESC,OPT3_DESC,OPT3_RESP,OPT3_NUM_DESC FROM RESTAURANT ")
			.append("WHERE RESTAURANT_ID = ? " );
		RestaurantVo vo = super.executeQry(RestaurantVo.class,sqls.toString(), id);
		
		return vo;
		
	}
	
	public int searchRestaurantTime(String id,String date,int time) throws Exception{
		StringBuffer sqls = new StringBuffer();
		sqls.append("SELECT MAX(TIME_START) as TIME FROM RESTAURANT_TIME ")
			.append("WHERE RESTAURANT_ID = ? ")
			.append("AND TIME_DATE = ? ")
			.append("AND TIME_START <= ? ")
			.append("AND (? - TIME_START) < 100 ");
		
		List<Object> paramList = new ArrayList<Object>();
		
		paramList.add(id);
		paramList.add(date);
		paramList.add(time);
		paramList.add(time);
		
		Object obj = super.executeQrySingleData(sqls.toString(), "TIME", paramList);
		
		return obj == null ? 0 : (int)obj;

	}

	public List<RestaurantVo> qryRestaurantSeats(String id,String date) throws Exception{
		StringBuffer sqls = new StringBuffer();
		sqls.append("select * from restaurant a left join restaurant_time b on a.restaurant_id = b.restaurant_id ")
			.append("WHERE 1=1 " );
		List<Object> paramList = new ArrayList<Object>();
		super.addCondition(sqls, id, " AND TIME_DATE = ? ", paramList);
		super.addCondition(sqls, date, " AND TIME_DATE = ? ", paramList);
		List<RestaurantVo> ll = super.executeQryList(RestaurantVo.class, sqls.toString(), id);	
		return ll;
		
	}
	
	public RestaurantVo qryRestaurantbBySIP(String sip) throws Exception{
		StringBuffer sqls = new StringBuffer();
		sqls.append("SELECT A.* FROM RESTAURANT A LEFT JOIN RESTAURANT_SIP B ")
			.append("ON A.RESTAURANT_ID = B.RESTAURANT_ID ")
			.append("WHERE B.RESTAURANT_SIP = ? " );
		
		return super.executeQry(RestaurantVo.class, sqls.toString(), sip);
	}
}
