package com.systex.b300.vbooking.dao;

import java.util.ArrayList;
import java.util.List;

import com.systex.b300.vbooking.vo.BookingVo;
import com.systex.b300.vbooking.vo.inline.BookingInlineVo;

public class BookingDao extends BaseDao{

	private static final String[] BOOKING_COLS = {"id","date","time","restaurant_id","name","last_name","seat_num","time_real","booking_status","waiting_order","phone","opt1_yn","opt1_num","opt2_yn","opt2_num","opt3_yn","opt3_num","restaurant_name","create_date","update_date"};
	private static final String[] BOOKING_KEYS = {"id"};
	public boolean searchBooking(String id,String date,int time,int num) throws Exception{
		
		StringBuffer sqls = new StringBuffer();
		sqls.append("SELECT * FROM RESTAURANT_TIME ")
			.append("WHERE RESTAURANT_ID = ? ")
			.append("AND TIME_DATE = ? ")
			.append("AND TIME_START = ? ")
			.append("AND (TIME_NUM - TIME_NUM_USED - ?) >= 0 ");
		
		List<Object> paramList = new ArrayList<Object>();
		
		paramList.add(id);
		paramList.add(date);
		paramList.add(time);
		paramList.add(num);
		
		BookingVo ret = super.executeQry(BookingVo.class, sqls.toString(), paramList);
		return !(ret == null);
		
	}
	
	
	public BookingVo qryBooking(String id) throws Exception{
		return super.executeQry(BookingVo.class, "SELECT * FROM BOOKING WHERE ID = ? ", id);
	}
	
	
	public int insertBooking(BookingVo vo) throws Exception{
		
		return super.executeInsert("booking", BOOKING_COLS, vo);
	}
	
	
	public int updateBookingOpts(BookingVo vo) throws Exception{
		String[] cols ={"opt1_yn","opt1_num","opt2_yn","opt2_num","opt3_yn","opt3_num","update_date"};
		return super.executeUpdate("booking",BOOKING_KEYS, cols, vo);
	}
	
	public List<BookingVo> qryBookingList(BookingVo vo) throws Exception{
		StringBuffer sqls = new StringBuffer();
		sqls.append("SELECT * FROM BOOKING ")
			.append("WHERE 1=1 ");
		
		List<Object> paramList = new ArrayList<Object>();
		super.addCondition(sqls,vo.getDate(), " AND create_date=? ", paramList);
		super.addCondition(sqls,vo.getRestaurantId(), " AND restaurant_id=? ", paramList);
		sqls.append("ORDER BY DATE,TIME ");
		return super.executeQryList(BookingVo.class, sqls.toString(), paramList);
		
	}
	
	public List<BookingInlineVo> qryBookingInlineList(BookingVo vo) throws Exception{
		StringBuffer sqls = new StringBuffer();
		sqls.append("SELECT * FROM BOOKING_INLINE ")
			.append("WHERE 1=1 ");
		
		List<Object> paramList = new ArrayList<Object>();
		super.addCondition(sqls,vo.getDate(), " AND create_date=? ", paramList);
		super.addCondition(sqls,vo.getRestaurantId(), " AND restaurant_id=? ", paramList);
		sqls.append("ORDER BY DATE,TIME ");
		return super.executeQryList(BookingInlineVo.class, sqls.toString(), paramList);
		
	}
	
	public int updBookingUsed(BookingVo vo) throws Exception{
		StringBuffer sqls = new StringBuffer();
		sqls.append("UPDATE RESTAURANT_TIME SET TIME_NUM_USED = TIME_NUM_USED + ? ")
			.append("WHERE RESTAURANT_ID = ? ")
			.append("  AND TIME_DATE = ? ")
			.append("  AND TIME_START = ? ");
		
		List<Object> params = new ArrayList<Object>();
		params.add(vo.getSeatNum());
		params.add(vo.getRestaurantId());
		params.add(vo.getDate());
		params.add(vo.getTimeReal());
		return super.executeUpdate(sqls.toString(), params);

	}
	
	public int updRemStatus(String bookingId,String status) throws Exception{
		StringBuffer sqls = new StringBuffer();
		sqls.append("UPDATE BOOKING SET REMINDER_STATUS = ? ")
			.append("WHERE ID = ? ");
		
		List<Object> params = new ArrayList<Object>();
		
		params.add(status);
		params.add(bookingId);
		
		return super.executeUpdate(sqls.toString(), params);
		
	}
	
	
}
