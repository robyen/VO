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
import com.systex.b300.vbooking.dao.RestaurantDao;
import com.systex.b300.vbooking.service.RestaurantService;
import com.systex.b300.vbooking.vo.BookingVo;
import com.systex.b300.vbooking.vo.EmFlowReqVo;
import com.systex.b300.vbooking.vo.EmFlowRespVo;
import com.systex.b300.vbooking.vo.EmMessageResponse;
import com.systex.b300.vbooking.vo.RestaurantTimeVo;
import com.systex.b300.vbooking.vo.RestaurantVo;

@Path("/restaurant")
public class RestaurantController extends BaseController{
	private static Logger log = LogManager.getLogger();

	@POST
	@Path("qryRestaurantByName")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String qryRestaurantByName(InputStream is) throws Exception{
		String reqStr = super.receiveStream(is);
		log.info("qryRestaurantByName==>req:"+reqStr);

//		JSONObject req = new JSONObject(reqStr);
		Gson gson = new Gson();
		
		EmFlowReqVo<RestaurantVo> req = gson.fromJson(reqStr, new TypeToken<EmFlowReqVo<RestaurantVo>>(){}.getType());
		
		
		RestaurantService service = new RestaurantService();

		RestaurantVo ret = service.searchRestaurantByName(req.getTask_info().getName());
		
		EmFlowRespVo<RestaurantVo> resp = new EmFlowRespVo<RestaurantVo>();
		resp.setStatus_code("0");
		
		EmMessageResponse<RestaurantVo> msgResponse = new EmMessageResponse<RestaurantVo>();
		if(ret == null){
			msgResponse.setStatusCode("9");
			msgResponse.setStatusMsg("查無資料");
		}else{
			msgResponse.setUpdate(ret);
		}
		resp.setMsgResponse(msgResponse);
		
		String respStr = gson.toJson(resp);
		log.info("qryRestaurantByName==>resp:"+respStr);

		return respStr;
		
	}
	
	
	@POST
	@Path("qryRestaurantOpts")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String qryRestaurantOpts(InputStream is) throws Exception{
		
		String reqStr = super.receiveStream(is);
		log.info("qryRestaurantOpts==>req:"+reqStr);
//		JSONObject req = new JSONObject(reqStr);
		Gson gson = new Gson();
		
		EmFlowReqVo<RestaurantVo> req = gson.fromJson(reqStr, new TypeToken<EmFlowReqVo<RestaurantVo>>(){}.getType());
		
		
		RestaurantService service = new RestaurantService();

		RestaurantVo ret = service.qryRestaurantOpts(req.getTask_info().getRestaurantId());
		
		EmFlowRespVo resp = new EmFlowRespVo<RestaurantVo>();

		resp.setStatus_code("0");
		
		EmMessageResponse<RestaurantVo> msgResponse = new EmMessageResponse<RestaurantVo>();
		if(ret == null){
			msgResponse.setStatusCode("9");
			msgResponse.setStatusMsg("查無資料");
		}else{
			msgResponse.setUpdate(ret);
		}
		resp.setMsgResponse(msgResponse);
		
		String respStr = gson.toJson(resp);
		log.info("qryRestaurantOpts==>resp:"+respStr);

		return respStr;
		
	}
	
	
	@POST
	@Path("qryRestaurantSeats")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String qryRestaurantSeats(InputStream is) throws Exception{
		
		String reqStr = super.receiveStream(is);
		log.info("qryRestaurantSeats==>req:"+reqStr);
//		JSONObject req = new JSONObject(reqStr);
		Gson gson = new Gson();
		
		RestaurantTimeVo req = gson.fromJson(reqStr, new TypeToken<RestaurantTimeVo>(){}.getType());
		
		
		RestaurantService service = new RestaurantService();

		List<RestaurantVo> ll = service.qryRestaurantSeats(req.getRestaurantId(),req.getTimeDate());
		
		String respStr = gson.toJson(ll);
		log.info("qryRestaurantSeats==>resp:"+respStr);

		return respStr;
		
	}
	
	
	
	@POST
	@Path("qryRestaurantDetail")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String qryRestaurantDetail(InputStream is) throws Exception{
		
		String reqStr = super.receiveStream(is);
		log.info("qryRestaurantDetail==>req:"+reqStr);
//		JSONObject req = new JSONObject(reqStr);
		Gson gson = new Gson();
		
		RestaurantTimeVo req = gson.fromJson(reqStr, new TypeToken<RestaurantTimeVo>(){}.getType());
		
		
		RestaurantService service = new RestaurantService();

		RestaurantVo ret = service.qryRestaurantDetail(req.getRestaurantId());
		
		String respStr = gson.toJson(ret);
		log.info("qryRestaurantDetail==>resp:"+respStr);

		return respStr;
		
	}
}
