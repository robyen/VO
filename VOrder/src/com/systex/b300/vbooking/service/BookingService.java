package com.systex.b300.vbooking.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.systex.b300.vbooking.dao.BookingDao;
import com.systex.b300.vbooking.dao.RestaurantDao;
import com.systex.b300.vbooking.vo.BookingVo;
import com.systex.b300.vbooking.vo.RestaurantVo;
import com.systex.b300.vbooking.vo.inline.BookingInlineVo;
import com.systex.b300.vbooking.vo.inline.ReservationRespVo;
import com.systex.b300.vbooking.vo.inline.ReservationVo;
import com.systex.b300.vbooking.vo.inline.RestaurantInlineVo;
import com.systex.b300.vbooking.vo.inline.SystexConstant;

public class BookingService extends BaseService{

	BookingDao booingDao;
	RestaurantDao restaurantDao;
	static SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
	static SimpleDateFormat sdFormat2 = new SimpleDateFormat("yyyyMMdd HH:mm:ss");	
	private static Logger log = LogManager.getLogger();
	static final String QRY_URL_FMT = "https://api.inline.app/v2/groups/%s/companies/%s/branches/%s?type=booking&size=2&date=%s";

	
	public BookingDao getBooingDao() {
		if (booingDao == null){
			booingDao = new BookingDao();
		}
		return booingDao;
	}

	public RestaurantDao getRestaurantDao() {
		if(restaurantDao == null) {
			restaurantDao = new RestaurantDao();
		}
		return restaurantDao;
	}

	public BookingVo searchBooking(BookingVo vo) throws Exception{
		if(super.isBlank(vo.getRestaurantId())){
			String rId = getRestaurantDao().searchRestaurantByName(vo.getRestaurantName());
			if(super.isBlank(rId)){
				vo.setBookingStatus("9");
				return vo;
			}
			vo.setRestaurantId(rId);
		}
				
		int intTime = super.getIntTime(vo.getTime());
		int time = getRestaurantDao().searchRestaurantTime(vo.getRestaurantId(),vo.getDate(),intTime);
		vo.setTimeReal(time);

		if(time == 0){
			vo.setBookingStatus("9");
			return vo;
		}
		
		
		boolean ret = getBooingDao().searchBooking(vo.getRestaurantId(), vo.getDate(), time, vo.getSeatNum());
		if(ret){
			vo.setBookingStatus("0");
		}else{
			RestaurantVo rest = getRestaurantDao().qryRestaurantDetail(vo.getRestaurantId());
			if("Y".equals(rest.getIsCanWaiting())){
				vo.setBookingStatus("1");
			}else{
				vo.setBookingStatus("9");
			}
		}
		
		
		return vo;
	}
	
	public BookingVo doBooking(BookingVo vo) throws Exception{
		searchBooking(vo);
		if("9".equals(vo.getBookingStatus())){
			return vo;
		}
		
		vo.setId(String.valueOf(System.currentTimeMillis()));
		vo.setCreateDate(new Timestamp(System.currentTimeMillis()));
		
		int ret = getBooingDao().insertBooking(vo);
		if(ret != 0)
			ret = getBooingDao().updBookingUsed(vo);

		
		return (ret > 0 ? vo : null);
	}
	

	
	
	public BookingVo updateBookingOpts(BookingVo vo) throws Exception{
		vo.setUpdateDate(new Timestamp(System.currentTimeMillis()));
		int ret = getBooingDao().updateBookingOpts(vo);
		if (ret == 0) return null;
		return getBooingDao().qryBooking(vo.getId());
	}
	
	public List<BookingVo> qryBookingList(BookingVo vo) throws Exception{
		return getBooingDao().qryBookingList(vo);
		
		
	}
	
	public int updRemStatus(String bookingId,String result)throws Exception{
		return getBooingDao().updRemStatus(bookingId,result);
	}
	
	
	public RestaurantInlineVo qryRestaurant(String sipNo) throws Exception{
		return null;
	}
	
	public ReservationRespVo doBookinginLine(BookingVo vo) throws Exception{
		searchBooking(vo);
		log.info("doBookinginLine vo:"+vo);
		
		String jsonString = "";
		
		ReservationVo bookingVo = new ReservationVo();
		bookingVo.setCustomerName(vo.getLastName());
		bookingVo.setPhone(vo.getPhone());
		bookingVo.setGender(2);
		bookingVo.setGroupSize(vo.getSeatNum());
		bookingVo.setNumberOfKidChairs(0);	
		bookingVo.setNumberOfKidSets(0);
		bookingVo.setShouldNotifyCustomer(true);
		String dd = vo.getDate()+" "+vo.getTime()+":00";
		sdFormat2.setTimeZone(TimeZone.getTimeZone("Asia/Taipei"));
		Date d = sdFormat2.parse(dd);
		sdFormat.setTimeZone(TimeZone.getTimeZone("GTM"));
		String datetime = sdFormat.format(d);
		bookingVo.setDatetime(datetime);
		jsonString = (new Gson()).toJson(bookingVo);
		log.info("Send inline String:"+jsonString);		
		String resp = sendinLine(SystexConstant.INLINE_RESSERVATION_URL, jsonString,"POST","UTF8",SystexConstant.API_KEY);
		
		Gson gson = new Gson();
		ReservationRespVo respVo = gson.fromJson(resp, ReservationRespVo.class);
		return respVo;
		
	}
	

	public static String sendinLine(String sendUrl,String data,String method,String encoding,String apiKey)  throws IOException {
		String res = "";
		if(sendUrl == null || sendUrl.isEmpty()){
			return null;
		}
		HttpURLConnection conn = null;
		try {
			String xml = data;
			
			byte [] xmlByte = xml == null ? null : xml.getBytes(encoding);
			URL url = new URL(sendUrl);
			conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod(method);
			conn.setRequestProperty("Content-Type", "application/json");
			if(apiKey != null) {
				conn.setRequestProperty("x-api-key", apiKey);
				
			}

			conn.connect();
			
			if(xmlByte != null){
				OutputStream os = conn.getOutputStream();
				os.write(xmlByte);
				os.flush();
			}
			
			res = getStream(conn.getInputStream(),encoding);
		} catch (IOException e) {
			InputStream is = conn.getErrorStream();
			if(conn == null || is == null){
				e.printStackTrace();
				throw e;
			}else{
				res = getStream(is,encoding);
			}
			
		}
		
		return res;
	}
	private static String getStream(InputStream is,String encoding) throws IOException{
//		InputStream is = conn.getInputStream();
		StringBuffer sb = new StringBuffer();
		BufferedReader bf = new BufferedReader(new InputStreamReader(is,encoding));  
	    String s = null;
	    while((s = bf.readLine()) != null){
	    	sb.append(s);
	    	System.out.println(s.toString());
	    }
		return sb.toString();		
	}
	
	private static String getToday(){
		SimpleDateFormat sdFormat3 = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(System.currentTimeMillis());
		return sdFormat3.format(cal.getTime());

	}
	
}
