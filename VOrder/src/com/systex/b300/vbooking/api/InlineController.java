package com.systex.b300.vbooking.api;


import java.io.InputStream;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.systex.b300.vbooking.service.BookingService;
import com.systex.b300.vbooking.service.InlineService;
import com.systex.b300.vbooking.service.RestaurantService;
import com.systex.b300.vbooking.sys.InLineThread;
import com.systex.b300.vbooking.sys.SystemManager;
import com.systex.b300.vbooking.sys.WriteLogFileThread;
import com.systex.b300.vbooking.vo.BookingVo;
import com.systex.b300.vbooking.vo.EmFlowReqVo;
import com.systex.b300.vbooking.vo.EmFlowRespVo;
import com.systex.b300.vbooking.vo.EmMessageResponse;
import com.systex.b300.vbooking.vo.RestaurantVo;
import com.systex.b300.vbooking.vo.inline.BookingInlineVo;
import com.systex.b300.vbooking.vo.inline.DoBookingRespVo;
import com.systex.b300.vbooking.vo.inline.ReservationRespVo;
import com.systex.b300.vbooking.vo.inline.RestaurantInlineVo;
import com.systex.b300.vbooking.vo.inline.RestaurantInlineVo.OpeningTime;
import com.systex.b300.vbooking.vo.inline.StartBookingRespVo;



@Path("/inline")
public class InlineController extends BaseController{
	
	private static Logger log = LogManager.getLogger();
	
	@POST
	@Path("startBooking")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String startBooking(InputStream is) throws Exception{
		String reqStr = super.receiveStream(is);

		log.info("startBooking==>req:"+reqStr);
		Gson gson = new Gson();
		EmFlowReqVo<BookingInlineVo> vo = gson.fromJson(reqStr, new TypeToken<EmFlowReqVo<BookingInlineVo>>(){}.getType());
		
		
		RestaurantService restService = new  RestaurantService();
				
		RestaurantVo rest  = restService.qryRestaurantbBySIP(vo.getTask_info().getFromSip());

//		InlineService service = new InlineService();
//		RestaurantInlineVo restVo = service.qryInLineRestaurant(rest);

		InLineThread t = new InLineThread(vo.getUser_id(),rest);
		t.start();
		
		EmFlowRespVo<StartBookingRespVo> resp = new EmFlowRespVo<StartBookingRespVo>();
		resp.setStatus_code("0");
		EmMessageResponse<StartBookingRespVo> msgResponse = new EmMessageResponse<StartBookingRespVo>();
		
		StartBookingRespVo booking = new StartBookingRespVo();

		booking.setRestaurantId(rest.getRestaurantId());
		booking.setRestaurantName(rest.getName());
		msgResponse.setUpdate(booking);
		
		resp.setMsgResponse(msgResponse);				
		String respStr = gson.toJson(resp);
		
		System.out.println("startBooking==>resp:"+respStr);
		log.info("startBooking==>resp:"+respStr);
		
		return respStr;		
	}
		
	final static String NO_SEAT_FOR_TIME = "抱歉,您要訂的時段訂位已滿,請問您要不要改訂%s點%s分";
	final static String NO_SEAT_FOR_DAY = "抱歉,您要訂位日期已滿,請問您要不要改訂其他日期";
	final static String NO_BOOKING_DATE = "抱歉,你要訂位的日期尚未開放訂位,請問您要不要改訂其他日期";
	final static String OVER_MAX_NUM = "訂位人數最多為%d位，將為您通知餐廳人員，回撥電話與您確認訂位";
	final static String BOOKING_FAIL = "訂位失敗,請重新播打電話";
	final static boolean ONLINE_QRY = false;
	
	@POST
	@Path("doBooking")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String doBooking(InputStream is) {
		/* status
		0:訂位完成
		1:時間不對,給建議時間
		2:訂位人數超過上限
		3:沒有時間可建議 換日期
		4:訂位日期尚未開放
		9:無法訂位
		*/
		String status = "0";

		EmFlowRespVo<DoBookingRespVo> resp = new EmFlowRespVo<DoBookingRespVo>();
		Gson gson = new Gson();
		
		try{
			String reqStr = super.receiveStream(is);
			log.info("doBooking==>req:"+reqStr);
			String errMsg = "";
	//		JSONObject req = new JSONObject(reqStr);
			
			EmFlowReqVo<BookingInlineVo> vo = gson.fromJson(reqStr, new TypeToken<EmFlowReqVo<BookingInlineVo>>(){}.getType());
			
			InlineService service = new InlineService();
			
			RestaurantInlineVo restVo = SystemManager.getRestaurant(vo.getUser_id());
			
			if(ONLINE_QRY){
				long st = System.currentTimeMillis();
				RestaurantService restService = new  RestaurantService();
				RestaurantVo rest  = restService.qryRestaurantbBySIP(vo.getTask_info().getFromSip());				
				restVo = service.qryInLineRestaurant(rest);
				long et = System.currentTimeMillis();
				log.debug("QRY RESTAURANT COST:"+String.valueOf(et -st));
			}
			
			BookingInlineVo booking = vo.getTask_info();
	
			
			DoBookingRespVo respBooking = new DoBookingRespVo();
			EmMessageResponse<DoBookingRespVo> msgResponse = new EmMessageResponse<DoBookingRespVo>();				
			
			respBooking.setRestaurantId(booking.getRestaurantId());				
			resp.setStatus_code("0");
			respBooking.setBookingStatus("0");
			
			
			
			Map<String,List<Integer>>  dayTimes = restVo.getOpenTimes();
			
			int sugTime = 0;
			int kidNum = booking.getKidNum()-booking.getChairNum();
			if (kidNum < 0) kidNum = 0;
			booking.setKidNum(kidNum);
			int total = booking.getGroupNum()+booking.getKidNum()+booking.getChairNum();
			
			if(!dayTimes.containsKey(booking.getDate())){
				status = "4"; //日期不可訂位
			}else if (total > restVo.getMaxBookingGroupSize()){
				status = "2"; //超過上限
			}else if(dayTimes == null || dayTimes.isEmpty()){
				status = "3"; //換日期
			}else{
				List<Integer> times = dayTimes.get(booking.getDate());
				log.debug("times:"+times);
				int realTime = Integer.parseInt(booking.getTime().replaceAll(":", ""));
				if(times.contains(realTime)){

					ReservationRespVo bookingResp = service.doBookinginLine(booking);
					if(bookingResp.getCode() != 0){
						if(bookingResp.getCode() == 300002){  //沒位子?
							status = "3"; //訂位失敗
						}else{
							status = "9"; //訂位失敗
							errMsg = bookingResp.getMessage();
						}
						
					}
				}else{
					sugTime = getSugTime(realTime,times);
					status = sugTime > 0 ? "1" : "3";
//					if(sugTime > 0){
//						status = "1"; //給建議時間
//					}else{
//						status = "3"; //沒有時間可建議 換日期
//					}
				}
			}
			respBooking.setRestaurantId(booking.getRestaurantId());

			switch(status){
			case "1":									 	//給建議時間
				respBooking.setBookingStatus("1");
				String timeStr = String.valueOf(sugTime);
				String hrStr = timeStr.substring(0,2);
				String minStr = timeStr.substring(2,4);
				String text = String.format(NO_SEAT_FOR_TIME, hrStr,minStr);
				respBooking.setRespText(text);
				respBooking.setTime(hrStr+":"+minStr);
				break;
			case "2":										//超過上限
				respBooking.setBookingStatus("2");
				text = String.format(OVER_MAX_NUM, restVo.getMaxBookingGroupSize());
				respBooking.setRespText(text);
				break;
			case "3":										//沒有時間可建議 換日期
				respBooking.setBookingStatus("3");
				respBooking.setRespText(NO_SEAT_FOR_DAY);
				break;
			case "4":										//訂位日期尚未開放
				respBooking.setBookingStatus("4");
				respBooking.setRespText(NO_BOOKING_DATE);
				break;				
			case "9":										//訂位失敗 換日期
				respBooking.setBookingStatus("9");
				respBooking.setRespText("".equals(errMsg) ? BOOKING_FAIL : errMsg );
//				respBooking.setRespText(BOOKING_FAIL);
				break;
			default:
				respBooking.setBookingStatus("0");
				respBooking.setRespText("");
			}
			
			msgResponse.setUpdate(respBooking);
			resp.setMsgResponse(msgResponse);				

		}catch(Exception e){
			log.info(e.getMessage(),e);
			resp.setStatus_code("9");
		}
		

		
		String respStr = gson.toJson(resp);
		log.info("doBooking==>resp:"+respStr);

		return respStr;
		
	}
	
	private int getSugTime(int realTime,List<Integer> times){
		int sugTime = 0;
		for(int rtime : times){
			if(realTime < rtime){
				sugTime = rtime;
				break;
			}
		}
		return sugTime;
		
	}
	
		
	@POST
	@Path("endBooking")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String endBooking(InputStream is) throws Exception{
		String reqStr = super.receiveStream(is);
		log.info("endBooking==>req:"+reqStr);

//		JSONObject req = new JSONObject(reqStr);
		Gson gson = new Gson();
		EmFlowReqVo<BookingInlineVo> vo = gson.fromJson(reqStr, new TypeToken<EmFlowReqVo<BookingInlineVo>>(){}.getType());
		
		BookingInlineVo req = vo.getTask_info();
		
		
		log.info("endBooking==>end Status:"+req.getEndStatus());

		
		
		EmFlowRespVo<StartBookingRespVo> resp = new EmFlowRespVo<StartBookingRespVo>();

		EmMessageResponse<StartBookingRespVo> msgResponse = new EmMessageResponse<StartBookingRespVo>();
		

		
		msgResponse.setUpdate(null);
		resp.setMsgResponse(msgResponse);
		resp.setStatus_code("0");
		String respStr = gson.toJson(resp);

		log.info("endBooking==>resp:"+respStr);
		
		SystemManager.removeRestaurant(vo.getUser_id());
		
		return respStr;
		
	}
	
	@POST
	@Path("qyrBusinessHours")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String qyrBusinessHours(InputStream is) throws Exception{
		String reqStr = super.receiveStream(is);
		log.info("qyrBusinessHours==>req:"+reqStr);

//		JSONObject req = new JSONObject(reqStr);
		Gson gson = new Gson();
		EmFlowReqVo<BookingInlineVo> vo = gson.fromJson(reqStr, new TypeToken<EmFlowReqVo<BookingInlineVo>>(){}.getType());
		
		BookingInlineVo req = vo.getTask_info();
		
		RestaurantInlineVo restVo = SystemManager.getRestaurant(vo.getUser_id());
		
		List<RestaurantInlineVo.OpeningTime> openTimes = restVo.getOpeningTimes();
		
		EmFlowRespVo<List<RestaurantInlineVo.OpeningTime>> resp = new EmFlowRespVo<List<RestaurantInlineVo.OpeningTime>>();

		EmMessageResponse<List<RestaurantInlineVo.OpeningTime>> msgResponse = new EmMessageResponse<List<RestaurantInlineVo.OpeningTime>>();
		
		msgResponse.setUpdate(openTimes);
		resp.setMsgResponse(msgResponse);
		resp.setStatus_code("0");
		String respStr = gson.toJson(resp);

		log.info("qyrBusinessHours==>resp:"+respStr);
		
		SystemManager.removeRestaurant(vo.getUser_id());
		
		return respStr;
		
	}
	
}
