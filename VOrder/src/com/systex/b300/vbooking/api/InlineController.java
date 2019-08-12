package com.systex.b300.vbooking.api;


import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.catalina.valves.CometConnectionManagerValve;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.systex.b300.vbooking.service.InlineService;
import com.systex.b300.vbooking.service.RestaurantService;
import com.systex.b300.vbooking.sys.QRCodeUtil;
import com.systex.b300.vbooking.sys.SystemManager;
import com.systex.b300.vbooking.sys.WriteLogThread;
import com.systex.b300.vbooking.vo.EmFlowReqVo;
import com.systex.b300.vbooking.vo.EmFlowRespVo;
import com.systex.b300.vbooking.vo.EmMessageResponse;
import com.systex.b300.vbooking.vo.LinePayVO;
import com.systex.b300.vbooking.vo.LogVo;
import com.systex.b300.vbooking.vo.QRCodeVo;
import com.systex.b300.vbooking.vo.RestaurantVo;
import com.systex.b300.vbooking.vo.inline.BookingInlineVo;
import com.systex.b300.vbooking.vo.inline.DoBookingRespVo;
import com.systex.b300.vbooking.vo.inline.ReservationRespVo;
import com.systex.b300.vbooking.vo.inline.RestaurantInlineVo;
import com.systex.b300.vbooking.vo.inline.StartBookingRespVo;
import com.systex.b300.vbooking.vo.inline.SystexConstant;



@Path("/inline")
public class InlineController extends BaseController{
	
	private static Logger log = LogManager.getLogger();
	
	@POST
	@Path("startBooking")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String startBooking(InputStream is) throws Exception{
		String reqStr = super.receiveStream(is);
		String uuid = "";

		log.info("startBooking==>req:"+reqStr);
		Gson gson = new Gson();
		EmFlowReqVo<BookingInlineVo> vo = gson.fromJson(reqStr, new TypeToken<EmFlowReqVo<BookingInlineVo>>(){}.getType());
		
		BookingInlineVo booking = vo.getTask_info();
//		booking.setUuid(vo.getUser_id());
		
		if(booking.getUuid() == null && booking.getMeta_uuid() != null){
			booking.setUuid(booking.getMeta_uuid());
		}
		uuid = booking.getUuid();
		
		LogVo logVo = new LogVo();
		logVo.setReq(reqStr);
		logVo.setStep("startBooking");
		logVo.setUuid(booking.getUuid());
		
		RestaurantService restService = new  RestaurantService();
				
		RestaurantVo rest  = restService.qryRestaurantbBySIP(booking.getFromSip());

//		InLineThread t = new InLineThread(vo.getUser_id(),rest);
//		t.start();
		
		EmFlowRespVo<StartBookingRespVo> resp = new EmFlowRespVo<StartBookingRespVo>();
		resp.setStatus_code("0");
		EmMessageResponse<StartBookingRespVo> msgResponse = new EmMessageResponse<StartBookingRespVo>();
		
		StartBookingRespVo startBooking = new StartBookingRespVo();

		booking.setRestaurantId(rest.getRestaurantId());
		booking.setRestaurantName(rest.getName());
		
		startBooking.setRestaurantId(rest.getRestaurantId());
		startBooking.setRestaurantName(rest.getName());
		msgResponse.setUpdate(startBooking);
		try{
			InlineService inlineServie = new InlineService();
			int rc = inlineServie.addBooking(booking);
			if(rc != 1) log.error("add DB Fail:"+rc);
		}catch(Exception e){
			e.printStackTrace();
		}
		resp.setMsgResponse(msgResponse);
		String respStr = gson.toJson(resp);
		
		log.info("startBooking==>resp:"+respStr+","+uuid);
		logVo.setResp(respStr);
		WriteLogThread.addLog(logVo);

		return respStr;		
	}
		
	final static String NO_SEAT_FOR_TIME = "這個時間目前無法訂位,目前最近的可訂位時間是%s,要幫你改訂這個時間嗎?";
	final static String NO_SEAT_FOR_DAY = "這個日期目前無法訂位,請問您要不要改訂其他日期";
	final static String NO_BOOKING_DATE = "抱歉,你要訂位的日期尚未開放訂位,請問您要不要改訂其他日期";
	final static String OVER_MAX_NUM = "目前餐廳只開放%d人以下訂位，將為您通知餐廳人員，回撥電話與您確認訂位";
	final static String OVER_LIMIT_NUM = "親愛的客戶,您的預約數量已達上限,無法再增加預約了";
	final static String INLINE_ERROR_DEFAULT_MSG = "代碼9，無法訂位";
//	final static String OVER_MAX_NUM = "訂位人數最多為%d位，將為您通知餐廳人員，回撥電話與您確認訂位";
	final static String BOOKING_FAIL = "訂位失敗,請重新播打電話";
	final static String ASR_FAIL_SMS = "抱歉無法以電話服務為您完成訂位，請到以下網址完成訂餐";
	
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
		LogVo logVo = new LogVo();
		DoBookingRespVo respBooking = new DoBookingRespVo();
		EmMessageResponse<DoBookingRespVo> msgResponse = new EmMessageResponse<DoBookingRespVo>();				

		EmFlowRespVo<DoBookingRespVo> resp = new EmFlowRespVo<DoBookingRespVo>();
		Gson gson = new Gson();
		String uuid = "";
		try{
			String reqStr = super.receiveStream(is);
			log.info("doBooking==>req:"+reqStr);
			String errMsg = "";
	//		JSONObject req = new JSONObject(reqStr);
			
			EmFlowReqVo<BookingInlineVo> vo = gson.fromJson(reqStr, new TypeToken<EmFlowReqVo<BookingInlineVo>>(){}.getType());

			
			InlineService service = new InlineService();
			
//			RestaurantInlineVo restVo = SystemManager.getRestaurant(vo.getUser_id());
			RestaurantInlineVo restVo = null;
			
			BookingInlineVo booking = vo.getTask_info();
//			booking.setUuid(vo.getUser_id());
			if(booking.getUuid() == null && booking.getMeta_uuid() != null){
				booking.setUuid(booking.getMeta_uuid());
			}
			uuid = booking.getUuid();
			
			System.out.println("DEMO_SIP:"+SystexConstant.DEMO_SIP+",getFromSip:"+booking.getFromSip());
			if(SystexConstant.DEMO_SIP.equals(booking.getFromSip())){
				String retStr = forDemoPhone(booking);
				log.info("doBooking==>resp:"+retStr+","+booking.getUuid());
				return retStr;
			}
						
			logVo.setReq(reqStr);
			logVo.setStep("doBooking");
			logVo.setUuid(booking.getUuid());
			int kidNum = booking.getKidNum() - booking.getChairNum();
			if(kidNum < 0){
				booking.setChairNum(booking.getKidNum());
				kidNum = 0;
			}
			booking.setKidNum(kidNum);

			int total = booking.getGroupNum()+booking.getKidNum()+booking.getChairNum();
			
			if(total == 0) total = 1;
			
			if(SystexConstant.ONLINE_QRY){
				long st = System.currentTimeMillis();
				RestaurantService restService = new  RestaurantService();
				RestaurantVo rest  = restService.qryRestaurantbBySIP(booking.getFromSip());				
				restVo = service.qryInLineRestaurant(rest,total,uuid);
				long et = System.currentTimeMillis();
				log.debug("QRY RESTAURANT COST:"+String.valueOf(et -st)+","+uuid);
			}
			
			
			respBooking.setRestaurantId(booking.getRestaurantId());				
			resp.setStatus_code("0");
			respBooking.setBookingStatus("0");
			
			Map<String,List<Integer>>  dayTimes = restVo.getOpenTimes();
			
			int sugTime = 0;

			
			if(dayTimes == null || dayTimes.isEmpty()){
				status = "3"; //日期不可訂位				
			}else if (total > restVo.getMaxBookingGroupSize()){
				status = "2"; //超過上限
			}else if(!dayTimes.containsKey(booking.getDate())){
				status = "4"; //換日期
				log.debug("dayTimes:"+dayTimes.size()+","+uuid);

			}else{
				List<Integer> times = dayTimes.get(booking.getDate());
				log.debug("times:"+times+","+uuid);
				int realTime = Integer.parseInt(booking.getTime().replaceAll(":", ""));
				if(times.contains(realTime)){

					ReservationRespVo bookingResp = service.doBookinginLine(booking,uuid);
					if(bookingResp.getCode() != 0){
						booking.setInlineCode(bookingResp.getCode());
						booking.setInlineMessage(bookingResp.getMessage());
						booking.setInlineReason(bookingResp.getReason());
						
						if(bookingResp.getCode() == 300002){  //沒位子?
							times.remove(realTime);
							sugTime = getSugTime(realTime,times);
							status = sugTime > 0 ? "1" : "3";
						}else if(bookingResp.getCode() == 300001){
							status = "9"; //訂位失敗
							errMsg = bookingResp.getReason();
							if(SystexConstant.INLINE_ERROR_OVER_LIMIT.equals(errMsg)){
								errMsg = OVER_LIMIT_NUM;
							}else{
								errMsg = INLINE_ERROR_DEFAULT_MSG;
							}
						}else{
							status = "9"; //訂位失敗
							errMsg = INLINE_ERROR_DEFAULT_MSG;
						}
					}else{
						booking.setReservationId(bookingResp.getReservationId());
						booking.setCustomerId(bookingResp.getCustomerId());
						booking.setReservationLink(bookingResp.getReservationLink());
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
				timeStr = hrStr +"點"+ ("00".equals(minStr) ? "": minStr+"分");
				
				String text = String.format(NO_SEAT_FOR_TIME, timeStr);
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
			booking.setBookingStatus(respBooking.getBookingStatus());
			
			try{
				InlineService inlineServie = new InlineService();
				int rc = inlineServie.updateBooking(booking);
				if(rc != 1) log.error("add DB Fail:"+rc);
			}catch(Exception e){
				e.printStackTrace();
			}
		}catch(Exception e){
			log.info(e.getMessage(),e);
			respBooking.setBookingStatus("9");
			respBooking.setRespText(BOOKING_FAIL);
			resp.setStatus_code("9");
		}finally{
			msgResponse.setUpdate(respBooking);
			resp.setMsgResponse(msgResponse);				
		}

		String respStr = gson.toJson(resp);
		log.info("doBooking==>resp:"+respStr+","+uuid);
		logVo.setResp(respStr);
		WriteLogThread.addLog(logVo);
		return respStr;
	}
	
	
	private String forDemoPhone(BookingInlineVo booking) throws Exception{
		QRCodeVo qrcode = new QRCodeVo();
		PropertyUtils.copyProperties(qrcode, booking);
		qrcode.setGroup_num(booking.getGroupNum());
		qrcode.setKid_num(booking.getKidNum());
		qrcode.setChair_num(booking.getChairNum());
		makeQRCode(qrcode, booking.getUuid());
		EmFlowRespVo<DoBookingRespVo> resp = new EmFlowRespVo<DoBookingRespVo>();
		EmMessageResponse<DoBookingRespVo> msgResponse = new EmMessageResponse<DoBookingRespVo>();				
		DoBookingRespVo vo = new DoBookingRespVo();
		vo.setRestaurantId("A99");
		vo.setBookingStatus("0");
		vo.setRespText("");
		msgResponse.setStatusCode("0");
		msgResponse.setUpdate(vo);
		resp.setMsgResponse(msgResponse);
		resp.setStatus_code("0");
		Gson gson = new Gson();
		String retStr= gson.toJson(resp);
		return retStr;
	}
	
	private int getSugTime(int realTime,List<Integer> times){
		int sugTime = 0;
		int nearTime = 0;
		int diffTime = 9999;
		
		//沒有可以訂的時間
		if(times == null || times.isEmpty()) return 0;
		
		int max = times.get(times.size()-1);
		
		//超過最晚的時間
		if(realTime > max ) return max;
		
		for(int rtime : times){
			//往後找最接近的時間
			if(realTime < rtime && sugTime == 0){
				sugTime = rtime;
			}
			//找出一個小時內最接近的	
			int dTime = Math.abs(realTime - rtime);
			if(dTime < diffTime && dTime <= 100){
				nearTime = rtime;
				diffTime = dTime;
			}
		}
		
//		return (sugTime == 0 ? nearTime : sugTime);
		return (nearTime == 0 ? sugTime: nearTime);
		
	}
	
		
	@POST
	@Path("endBooking")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String endBooking(InputStream is) throws Exception{
		String reqStr = super.receiveStream(is);
		log.info("endBooking==>req:"+reqStr);
		String uuid = "";
//		JSONObject req = new JSONObject(reqStr);
		Gson gson = new Gson();
		EmFlowReqVo<BookingInlineVo> vo = gson.fromJson(reqStr, new TypeToken<EmFlowReqVo<BookingInlineVo>>(){}.getType());
		
		BookingInlineVo req = vo.getTask_info();
		if(req.getUuid() == null && req.getMeta_uuid() != null){
			req.setUuid(req.getMeta_uuid());
		}
		
		uuid = req.getUuid();
		
		log.info("endBooking==>end Status:"+uuid+":"+req.getEndStatus());
		
		
		LogVo logVo = new LogVo();
		logVo.setReq(reqStr);
		logVo.setStep("endBooking");
		logVo.setUuid(req.getUuid());
		
		EmFlowRespVo<StartBookingRespVo> resp = new EmFlowRespVo<StartBookingRespVo>();

		EmMessageResponse<StartBookingRespVo> msgResponse = new EmMessageResponse<StartBookingRespVo>();
		
		msgResponse.setUpdate(null);
		resp.setMsgResponse(msgResponse);
		resp.setStatus_code("0");
		String respStr = gson.toJson(resp);

		
		try{
			String phone = req.getPhone();
			InlineService inlineServie = new InlineService();
			if(SystexConstant.DEMO_SIP.equals(req.getFromSip())){
				if(phone != null && phone.startsWith("09") && "0".equals(req.getEndStatus())){
					sendDemoSMS(phone, req.getUuid());
				}
				return respStr;
			}
			int rc = inlineServie.updateEndBooking(req);
			if(rc != 1){
				log.error("updateEndBooking DB Fail:"+rc);
			}else{
				log.info("updateEndBooking Status:"+req.getEndStatus()+",uuid:"+req.getUuid());

			}
			if( SystexConstant.SEND_SMS && "1".equals(req.getEndStatus())){
				if(phone != null && phone.startsWith("09")){
					inlineServie.sendSMS(ASR_FAIL_SMS,phone,"http://www.google.com.tw/");
				}
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		log.info("endBooking==>resp:"+respStr+","+uuid);

		logVo.setResp(respStr);
		WriteLogThread.addLog(logVo);
		
//		SystemManager.removeRestaurant(vo.getUser_id());
		
		return respStr;
		
	}
	
	@POST	
	@Path("testBooking")
//	@Consumes(MediaType.APPLICATION_JSON)
//	@Produces(MediaType.APPLICATION_JSON)
	public String testBooking(InputStream is) throws Exception{
		String reqStr = super.receiveStream(is);
		log.info("testBooking==>req:"+reqStr);

//		JSONObject req = new JSONObject(reqStr);
		Gson gson = new Gson();
		EmFlowReqVo<BookingInlineVo> vo = gson.fromJson(reqStr, new TypeToken<EmFlowReqVo<BookingInlineVo>>(){}.getType());
		

		BookingInlineVo req = vo.getTask_info();
		
		req.setUuid(vo.getUser_id());
		log.info("testBooking==>end Status:"+vo.getUser_id()+":"+req.getEndStatus());

//		LogVo logVo = new LogVo();
//		logVo.setReq(reqStr);
//		logVo.setStep("endBooking");
//		logVo.setUuid(req.getUuid());
		
		EmFlowRespVo<StartBookingRespVo> resp = new EmFlowRespVo<StartBookingRespVo>();

		EmMessageResponse<StartBookingRespVo> msgResponse = new EmMessageResponse<StartBookingRespVo>();
		StartBookingRespVo startBooking = new StartBookingRespVo();
		startBooking.setRestaurantId("A");
		startBooking.setRestaurantName("中文");
		msgResponse.setUpdate(startBooking);
		resp.setMsgResponse(msgResponse);
		resp.setStatus_code("0");
		
		String respStr = gson.toJson(resp);

		log.info("testBooking==>resp:"+respStr);
		
		
		return respStr;
		
	}
	
	private static final String LINE_URL = "https://api-pay.line.me/v2/payments/request";
	private static final String TRANS_KEYWORD = "line://pay/payment/";
	private static final String QRCODE_PREFIX = "http://line.me/R/pay/payment/";
	
	@POST	
	@Path("linePay")
//	@Consumes(MediaType.APPLICATION_JSON)
//	@Produces(MediaType.APPLICATION_JSON)
	public String linePay(InputStream is) throws Exception{
		String respStr = "";
		String reqStr = super.receiveStream(is);
		log.info("linePay==>req:"+reqStr);

//		JSONObject req = new JSONObject(reqStr);
		Gson gson = new Gson();
		LinePayVO vo = gson.fromJson(reqStr,LinePayVO.class );
		if(vo.getOrderId() == null || "".equals(vo.getOrderId())) vo.setOrderId(String.valueOf(System.currentTimeMillis()));		
		String reqLineStr = gson.toJson(vo);
		log.info("linePay==>Line request:"+reqLineStr);
		
		String respLineStr = sendLineHttp(LINE_URL, reqLineStr.toString(),"POST","UTF8");

		log.info("linePay==>Line response:"+respLineStr);
		JsonObject json = gson.fromJson(respLineStr, JsonObject.class);
		String returnCode = getJsonString(json,"returnCode");
		if("0000".equals(returnCode)){
			String txId = getJsonTxId(json);
			
			respStr = "{\"returnCode\":\"0\",\"qrcode\":\""+QRCODE_PREFIX+txId+"\",\"txid\":\""+txId+"\"}";
		}else{
			respStr = "{\"returnCode\":\""+returnCode+"\",\"message\":\"Call LinePay URL Fail\"}";
		}
		
		return respStr;
	}
	
	private String getJsonString(JsonObject json,String name){
		if(json.has(name)){
			return json.get(name).getAsString();
		}
		return "";
	}
	private String getJsonTxId(JsonObject json){
		String txId = "";
		if(json.has("info")){
			JsonObject info = json.get("info").getAsJsonObject();
			if(info.has("paymentUrl")){
				JsonObject paymentUrl = info.get("paymentUrl").getAsJsonObject();
				if(paymentUrl.has("app")){
					String appUrl = paymentUrl.get("app").getAsString();
					txId = appUrl.replace(TRANS_KEYWORD, "");
				}
			}
		}
		return txId;
	}
	
	@POST
	@Path("doBooking2")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String doBooking2(InputStream is) {
		/* status
		0:訂位完成
		1:時間不對,給建議時間
		2:訂位人數超過上限
		3:沒有時間可建議 換日期
		4:訂位日期尚未開放
		9:無法訂位
		*/
		DoBookingRespVo respBooking = new DoBookingRespVo();
		BookingInlineVo booking = new BookingInlineVo();

		Gson gson = new Gson();
		try{
			String reqStr = super.receiveStream(is);
			log.info("doBooking2==>req:"+reqStr);
			
			String errMsg = "";
	//		JSONObject req = new JSONObject(reqStr);
			QRCodeVo qrcode = gson.fromJson(reqStr,  QRCodeVo.class);
			PropertyUtils.copyProperties(booking , qrcode);
			
			
			if(booking.getUuid() == null ){
				booking.setUuid(String.valueOf(System.currentTimeMillis()));
			}
			respBooking.setRestaurantId("A99");				
			respBooking.setBookingStatus("0");
			respBooking.setRespText("");

			booking.setBookingStatus(respBooking.getBookingStatus());
			booking.setReservationId(respBooking.getRestaurantId());
			booking.setGroupNum(qrcode.getGroup_num());
			booking.setKidNum(qrcode.getKid_num());
			booking.setChairNum(qrcode.getChair_num());
			
			
//			InlineService inlineServie = new InlineService();
//			int rc = inlineServie.addBooking(booking);
//			if(rc != 1) log.error("add DB Fail:"+rc);
			makeQRCode(qrcode,booking.getUuid());
			sendDemoSMS(booking.getPhone(),booking.getUuid());
		}catch(Exception e){
			log.info(e.getMessage(),e);
			respBooking.setBookingStatus("9");
			respBooking.setRespText(e.getMessage());
		}

		String respStr = gson.toJson(respBooking);
		log.info("doBooking2==>resp:"+respStr+",uuid:"+booking.getUuid());
		return respStr;
	}
	
	private void sendDemoSMS(String phone,String uuid) throws Exception{
		if("0910274529".equals(phone) || "0937939801".equals(phone) || "0935509235".equals(phone)){
			InlineService inlineService = new InlineService();
			String myUid = getMyUID(uuid);
			String url = "https://tinyurl.com/y6tuxfkl/QR?U="+myUid;
//			inlineServie.sendUrlSMS(msg, phone);
			inlineService.sendSMS("粉紅圈圈甜點屋訂位通知:點開連結取得訂位代號 ", phone, url);
		}
	}
	
	private void makeQRCode(QRCodeVo vo,String uuid) throws Exception{
		Gson gson = new Gson();
		String qrcodeStr = gson.toJson(vo);
		String myUid = getMyUID(uuid);
		FileOutputStream fo = new FileOutputStream("/data/qrcode/"+myUid+".gif");
		BufferedImage logo = null;
		File logoFile = new File("/data/logo.png");
		if(logoFile.exists()) logo = ImageIO.read(new File("/data/logo.png"));
		QRCodeUtil.generate(qrcodeStr, 300, fo, logo);
		
		fo.close();
	}
	
	private String getMyUID(String uuid) throws Exception{
		String newUID = uuid;
		if(uuid.contains("-")){
			newUID = uuid.substring(0,13);
		}
		return newUID;
	}
	
	private String sendLineHttp(String sendUrl,String data,String method,String encoding)  throws IOException {
		String res = "";
		if(sendUrl == null || sendUrl.isEmpty()){
			return null;
		}
//		try {
			String xml = data;
			
			byte [] xmlByte = xml == null ? null : xml.getBytes(encoding);
			URL url = new URL(sendUrl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod(method);
			conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
			conn.setRequestProperty("x-line-channelid", "1570936886");
			conn.setRequestProperty("x-line-channelsecret", "e17070133fbd9322149fb26a0a3af251");

			conn.connect();
			
			if(xmlByte != null){
				OutputStream os = conn.getOutputStream();
				os.write(xmlByte);
				os.flush();
			}
			res = super.receiveStream(conn.getInputStream());
//			InputStream is = conn.getInputStream();
//			StringBuffer sb = new StringBuffer();
//			BufferedReader bf = new BufferedReader(new InputStreamReader(is,encoding));  
//		    String s = null;
//		    while((s = bf.readLine()) != null){
//		    	sb.append(s);
//		    }
//			res = sb.toString();
		
		return res;
	}
}
