package com.systex.b300.vbooking.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.systex.b300.vbooking.vo.RestaurantVo;
import com.systex.b300.vbooking.vo.inline.BookingInlineVo;
import com.systex.b300.vbooking.vo.inline.ReservationRespVo;
import com.systex.b300.vbooking.vo.inline.ReservationVo;
import com.systex.b300.vbooking.vo.inline.RestaurantInlineVo;
import com.systex.b300.vbooking.vo.inline.SystexConstant;

public class InlineService extends BaseService{
	
	static SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
	static SimpleDateFormat sdFormat2 = new SimpleDateFormat("yyyyMMdd HH:mm:ss");	
	private static Logger log = LogManager.getLogger();
	static final String QRY_URL_FMT = "https://api.inline.app/v2/groups/%s/companies/%s/branches/%s?type=booking&size=%d&date=%s";
	private static final boolean SEND_SMS = false;
	
	
	public ReservationRespVo doBookinginLine(BookingInlineVo vo) throws Exception{
		String jsonString = "";
		
		ReservationVo bookingVo = new ReservationVo();
		bookingVo.setCustomerName(vo.getLastName());
		bookingVo.setPhone(vo.getPhone());
		bookingVo.setGender(2);
		bookingVo.setGroupSize(vo.getGroupNum());
		bookingVo.setNumberOfKidChairs(vo.getKidNum());	
		bookingVo.setNumberOfKidSets(vo.getChairNum());
		String dd = vo.getDate()+" "+vo.getTime()+":00";
		Date d = sdFormat2.parse(dd);
		sdFormat.setTimeZone(TimeZone.getTimeZone("GTM"));
		String datetime = sdFormat.format(d);
		bookingVo.setDatetime(datetime);
		bookingVo.setNote(vo.getNote());
		bookingVo.setShouldNotifyCustomer(SEND_SMS);
		jsonString = (new Gson()).toJson(bookingVo);
		log.info("doBookinginLine send inline:"+jsonString);
		String resp = sendinLine(SystexConstant.INLINE_RESSERVATION_URL, jsonString,"POST","UTF8",SystexConstant.API_KEY);
		log.info("doBookinginLine recv inline:"+resp);		

		Gson gson = new Gson();
		ReservationRespVo respVo = gson.fromJson(resp, ReservationRespVo.class);
		return respVo;
		
	}
	public RestaurantInlineVo qryInLineRestaurant(RestaurantVo restaurant) throws Exception{
		return qryInLineRestaurant(restaurant,2);
	}
	
	public RestaurantInlineVo qryInLineRestaurant(RestaurantVo restaurant,int num) throws Exception{
		String url = String.format(QRY_URL_FMT, restaurant.getGroupId(),restaurant.getCompanyId(),restaurant.getBranchId(),num,getToday());
		String resp = sendinLine(url, null,"GET","UTF8",SystexConstant.API_KEY);
		Gson gson = new Gson();
		RestaurantInlineVo vo = gson.fromJson(resp, RestaurantInlineVo.class);
	
//		
		Map<String,Map<String,String>> bookings = vo.getBookingInfo().get("default");
		Map<String,List<Integer>> openTimes = new HashMap<String,List<Integer>>();
		Set<String> dateKeys = bookings.keySet();
		for(String dateKey : dateKeys){
			Map<String,String> dateMap = bookings.get(dateKey);
			Set<String> timeKeys = dateMap.keySet();
			List<Integer> times = new ArrayList<Integer>();
			for(String timeKey : timeKeys){
				String status = dateMap.get(timeKey);
				if("open".equals(status)){
					String timeStr = timeKey.replace(":", "");
					
					int time = Integer.valueOf(timeStr);
					times.add(time);
				}
			}
			
			String dateStr = dateKey.replaceAll("-", "");
			
			times.sort(null);
			openTimes.put(dateStr, times);
		}
		vo.setOpenTimes(openTimes);
		vo.setApiKey(restaurant.getApiKey());
		vo.setGroupId(restaurant.getGroupId());
//		vo.setCompanyId(restaurant.getCompanyId());
		vo.setBranchId(restaurant.getBranchId());
		return vo;
	}
	
	public String sendinLine(String sendUrl,String data,String method,String encoding,String apiKey)  throws IOException {
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
	private String getStream(InputStream is,String encoding) throws IOException{
//		InputStream is = conn.getInputStream();
		StringBuffer sb = new StringBuffer();
		BufferedReader bf = new BufferedReader(new InputStreamReader(is,encoding));  
	    String s = null;
	    while((s = bf.readLine()) != null){
	    	sb.append(s);
//	    	System.out.println(s.toString());
	    }
		return sb.toString();
	}
	
	private String getToday(){
		SimpleDateFormat sdFormat3 = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(System.currentTimeMillis());
		return sdFormat3.format(cal.getTime());

	}
}
