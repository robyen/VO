package com.systex.b300.vbooking.api;


import java.io.InputStream;
import java.util.List;

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
import com.systex.b300.vbooking.service.RestaurantService;
import com.systex.b300.vbooking.sys.WriteLogThread;
import com.systex.b300.vbooking.vo.BookingVo;
import com.systex.b300.vbooking.vo.EmFlowReqVo;
import com.systex.b300.vbooking.vo.EmFlowRespVo;
import com.systex.b300.vbooking.vo.EmMessageResponse;
import com.systex.b300.vbooking.vo.RestaurantVo;
import com.systex.b300.vbooking.vo.inline.BookingInlineVo;
import com.systex.b300.vbooking.vo.inline.ReservationRespVo;



@Path("/booking")
public class BookingController extends BaseController{
	
	private static Logger log = LogManager.getLogger();
	
	@POST
	@Path("searchBooking")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String searchBooking(InputStream is) throws Exception{
		String reqStr = super.receiveStream(is);
//		System.out.println("searchBooking==>req:"+reqStr);
		log.info("searchBooking==>req:"+reqStr);
//		JSONObject req = new JSONObject(reqStr);
		Gson gson = new Gson();
		EmFlowReqVo<BookingVo> vo = gson.fromJson(reqStr, new TypeToken<EmFlowReqVo<BookingVo>>(){}.getType());
		
		BookingService service = new BookingService();
		
		BookingVo booking = service.searchBooking(vo.getTask_info());
		
		EmFlowRespVo<BookingVo> resp = new EmFlowRespVo<BookingVo>();
		resp.setStatus_code("0");
		EmMessageResponse<BookingVo> msgResponse = new EmMessageResponse<BookingVo>();
		if(booking == null){
			msgResponse.setStatusCode("9");
			msgResponse.setStatusMsg("查無資料");
		}else{
			msgResponse.setUpdate(booking);
		}
		
		resp.setMsgResponse(msgResponse);				
		String respStr = gson.toJson(resp);
		System.out.println("searchBooking==>resp:"+respStr);
		log.info("searchBooking==>resp:"+respStr);

		return respStr;		
	}
		
	@POST
	@Path("doBookingx")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String doBooking(InputStream is) throws Exception{
		String reqStr = super.receiveStream(is);
		log.info("doBooking==>req:"+reqStr);

//		JSONObject req = new JSONObject(reqStr);
		Gson gson = new Gson();
		
		EmFlowReqVo<BookingVo> vo = gson.fromJson(reqStr, new TypeToken<EmFlowReqVo<BookingVo>>(){}.getType());
		
		BookingService service = new BookingService();
		
		BookingVo booking = service.doBooking(vo.getTask_info());
		
		EmFlowRespVo<BookingVo> resp = new EmFlowRespVo<BookingVo>();
		resp.setStatus_code("0");
		EmMessageResponse<BookingVo> msgResponse = new EmMessageResponse<BookingVo>();
		if(booking == null){
			msgResponse.setStatusCode("9");
			msgResponse.setStatusMsg("查無資料");
		}else{
			msgResponse.setUpdate(booking);
		}
		resp.setMsgResponse(msgResponse);				
		String respStr = gson.toJson(resp);
		log.info("doBooking==>resp:"+respStr);

		return respStr;
		
	}
	
	@POST
	@Path("doBooking")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String doBooking2(InputStream is) throws Exception{
		String reqStr = super.receiveStream(is);
		log.info("doBooking==>req:"+reqStr);

//		JSONObject req = new JSONObject(reqStr);
		Gson gson = new Gson();
		
		EmFlowReqVo<BookingVo> vo = gson.fromJson(reqStr, new TypeToken<EmFlowReqVo<BookingVo>>(){}.getType());
		
		BookingService service = new BookingService();
		
		BookingVo booking = vo.getTask_info();
		booking.setPhone(convertPhone(booking.getPhone()));
		
		ReservationRespVo respVo = service.doBookinginLine(booking);
		log.info("inline resp:"+respVo);
		
		
		EmFlowRespVo<BookingVo> resp = new EmFlowRespVo<BookingVo>();
		resp.setStatus_code("0");
		EmMessageResponse<BookingVo> msgResponse = new EmMessageResponse<BookingVo>();
		if(booking == null){
			msgResponse.setStatusCode("9");
			msgResponse.setStatusMsg("查無資料");
		}else{
			int code = respVo.getCode();
			if(code != 0 ){
				booking.setBookingStatus("9");
			}else{
				booking.setBookingStatus("0");
			}
			
			msgResponse.setUpdate(booking);
		}
		resp.setMsgResponse(msgResponse);				
		String respStr = gson.toJson(resp);
		log.info("doBooking==>resp:"+respStr);

		return respStr;
		
	}
	
	
	
	@POST
	@Path("updBookingOptsx")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String updBookingOpts(InputStream is) throws Exception{
		String reqStr = super.receiveStream(is);
		log.info("updBookingOpts==>req:"+reqStr);
		

//		JSONObject req = new JSONObject(reqStr);
		Gson gson = new Gson();
	
		EmFlowReqVo<BookingVo> vo = gson.fromJson(reqStr, new TypeToken<EmFlowReqVo<BookingVo>>(){}.getType());
		
		BookingService service = new BookingService();
		
		BookingVo booking = service.updateBookingOpts(vo.getTask_info());
		
		EmFlowRespVo<BookingVo> resp = new EmFlowRespVo<BookingVo>();
		resp.setStatus_code("0");
		
		EmMessageResponse<BookingVo> msgResponse = new EmMessageResponse<BookingVo>();
		if(booking == null){
			msgResponse.setStatusCode("9");
			msgResponse.setStatusMsg("查無資料");
		}else{
			msgResponse.setUpdate(booking);
		}
		resp.setMsgResponse(msgResponse);		
		String respStr = gson.toJson(resp);
		
		log.info("updBookingOpts==>resp:"+respStr);
		
		return respStr;
		
	}
	
	
	@POST
	@Path("updBookingOpts")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String updBookingOpts2(InputStream is) throws Exception{
		String reqStr = super.receiveStream(is);
		log.info("updBookingOpts==>req:"+reqStr);
		

//		JSONObject req = new JSONObject(reqStr);
		Gson gson = new Gson();
	
		EmFlowReqVo<BookingVo> vo = gson.fromJson(reqStr, new TypeToken<EmFlowReqVo<BookingVo>>(){}.getType());
		BookingVo booking = vo.getTask_info();
				
		EmFlowRespVo<BookingVo> resp = new EmFlowRespVo<BookingVo>();
		resp.setStatus_code("0");
		
		EmMessageResponse<BookingVo> msgResponse = new EmMessageResponse<BookingVo>();
		if(booking == null){
			msgResponse.setStatusCode("9");
			msgResponse.setStatusMsg("查無資料");
		}else{
			msgResponse.setUpdate(booking);
		}
		resp.setMsgResponse(msgResponse);		
		String respStr = gson.toJson(resp);
		
		log.info("updBookingOpts==>resp:"+respStr);
		
		return respStr;
		
	}
	
	@POST
	@Path("qryBookings")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String qryBookings(InputStream is) throws Exception{
		String reqStr = super.receiveStream(is);
		log.info("qryBookings==>req:"+reqStr);

//		JSONObject req = new JSONObject(reqStr);
		Gson gson = new Gson();
	
		BookingVo vo = gson.fromJson(reqStr, new TypeToken<BookingVo>(){}.getType());
		
		BookingService service = new BookingService();
		
//		List<BookingVo> ll = service.qryBookingList(vo);
		List<BookingInlineVo> ll = service.qryBookingInlineList(vo);
		
		String respStr = gson.toJson(ll);
 
//		log.info("qryBookings==>resp:"+respStr);
		
		return respStr;
		
	}
	
	private static final String[] NUMBER_BIG = {"零","一","二","三","四","五","六","七","八","九"};
	
	private String convertPhone(String phone){
		for(int cnt=0;cnt<NUMBER_BIG.length;cnt++){
			phone = phone.replace(NUMBER_BIG[cnt], String.valueOf(cnt));
		}
		return phone;
	}
	
}
