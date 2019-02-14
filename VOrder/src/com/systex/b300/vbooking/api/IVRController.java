package com.systex.b300.vbooking.api;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.util.Base64;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jettison.json.JSONObject;

import com.google.gson.Gson;
import com.systex.b300.vbooking.service.BookingService;
import com.systex.b300.vbooking.sys.BroadcasterServer;
import com.systex.b300.vbooking.sys.WriteLogFileThread;
import com.systex.b300.vbooking.sys.WriteWaveFileThread;

//import org.json.JSONObject;

import com.systex.b300.vbooking.vo.IVRRespData;

@Path("/ivr")
public class IVRController extends BaseController {
	private static Logger log = LogManager.getLogger();

	static final String WAVE_FILE_PATH = "/tmp/";
	private static final String TASK_URL = "http://61.216.75.236:8181/v1/V2Task";

	@POST
	@Path("useSTTX")
//	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public String sendASR(@FormParam("file") String file) throws Exception{
		IVRRespData resp = new IVRRespData();

		try{
//			String reqStr = receiveStream(is);
			log.info("file:"+file);

			resp.setRespCode("0");
			resp.setRespText("OK");
			resp.setStatus("0");
			resp.setMessage("");
			byte[] bs = Base64.getDecoder().decode(file);
			String fileName = WAVE_FILE_PATH+String.valueOf(System.currentTimeMillis())+".wav";
			log.info("FileName:"+fileName);
			FileOutputStream os = new FileOutputStream(fileName);
			os.write(bs);
			os.flush();
			os.close();
		}catch(Exception e){
			resp.setStatus("99");
			resp.setMessage(e.getMessage());
			resp.setRespText("系統發生異常,請重撥");
			e.printStackTrace();
			log.debug(e.getMessage(), e);
		}
		
		
//		return (new JSONObject(resp)).toString();
		return "";
	
	}
	
	
	
	@POST
	@Path("sendASR")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String sendASRX(InputStream is) throws Exception{

		IVRRespData resp = new IVRRespData();
		String STTString = "";
		try{
			String reqStr = receiveStream(is);
//			System.out.println("sendASR==>req:"+reqStr);
			log.info("sendASR req==>....");
			
			JSONObject req = new JSONObject(reqStr);
			String userId = getJSONString(req, "uid");
			String wData = "";
			if(req.has("file")){
				wData = getJSONString(req,"file");
			}else{
				wData = getJSONString(req,"waveData");
			}
			byte[] bs = Base64.getDecoder().decode(wData);
			try{
				String fileName = WAVE_FILE_PATH+String.valueOf(System.currentTimeMillis())+".wav";
				
//				FileOutputStream os = new FileOutputStream(fileName);
//				os.write(bs);
//				os.flush();
//				os.close();
				WriteWaveFileThread t = new WriteWaveFileThread(fileName,bs);
				t.start();
				log.info("sendASR==>FileName:"+fileName);
			}catch(Exception e){
				e.printStackTrace();
			}
			
			
			STTString = sendEmSTT(TASK_URL,userId,bs,"POST");
			log.info("sendASR==>sendEmSTT:"+STTString);

			JSONObject stt = new JSONObject(STTString);
			String respText = getJSONString(stt,"TTSText");//  stt.getString("TTSText");
			int finalFalg = stt.getInt("FinalFlag");
			String asrText = getJSONString(stt,"asr_text");//  stt.getString("TTSText");

			WriteLogFileThread logt = new WriteLogFileThread(asrText,respText);
			logt.start();
			
			//沒進入意圖,等待客戶再說一遍
			if(respText == null && finalFalg == -1){
				respText = "對不起,我們目前不提供這項服務,請問你需要什麼服務";
				resp.setRespCode("0");
			//完成訂位 或 結束通話
			}else if(respText.contains("您的訂位服務已完成") || respText.contains("謝謝您的來電")){
				resp.setRespCode("2");
			//轉專人
			}else if(respText.contains("轉接專人中")){
				resp.setRespCode("2");
//				resp.setRespCode("1");
			}else{
				resp.setRespCode("0");
			}
			
			resp.setCallerId(getJSONString(req,"callerId"));
			resp.setUid(userId);
			resp.setRespText(respText);
//			resp.setStatus("0");
			resp.setMessage("");
		}catch(IOException je){
			resp.setStatus("99");
			resp.setRespText("不好意思,請再說一次");
			resp.setMessage(je.getMessage());
			resp.setRespCode("0");
			log.debug(je.getMessage(), je);
		}catch(Exception e){
			resp.setStatus("99");
			resp.setMessage(e.getMessage());
			resp.setRespText("系統發生異常,請重撥");
			e.printStackTrace();
			resp.setRespCode("2");			
			log.debug(e.getMessage(), e);
		}
		
		String respString = (new Gson()).toJson(resp);
		log.info("sendASR==>resp:"+respString);
		
		return respString;
	
	}
	
	private String getJSONString(JSONObject json,String key) throws Exception{
		if(json.has(key)) return json.getString(key);
		return "";
	}
	
	private String sendEmSTT(String sendUrl,String userId, byte[] bs, String method) throws IOException{
		String res = "";
		if (sendUrl == null || sendUrl.isEmpty()) {
			return null;
		}
		HttpURLConnection conn = null;
		byte[] xmlByte = bs;
		URL url = new URL(sendUrl);
		conn = (HttpURLConnection) url.openConnection();
		conn.setDoInput(true);
		conn.setDoOutput(true);
		conn.setRequestMethod(method);
		conn.setRequestProperty("Content-Type", "audio/x-wav");
		conn.setRequestProperty("x-userid", userId);
		conn.connect();
		if (xmlByte != null) {
			OutputStream os = conn.getOutputStream();
			os.write(xmlByte);
			os.flush();
		}

		InputStream is = conn.getInputStream();
		StringBuffer sb = new StringBuffer();
		BufferedReader bf = new BufferedReader(new InputStreamReader(is, "UTF8"));
		String s = null;
		while ((s = bf.readLine()) != null) {
			sb.append(s);
		}
		res = sb.toString();
		return res;
	}	
	
//	@POST
//	@Path("sendFile")
//	@Consumes(MediaType.MULTIPART_FORM_DATA)
//	@Produces(MediaType.APPLICATION_JSON)
//	public String sendFile(               
//			@FormParam("uid") String uid,
//			@FormParam("callerId") String callerId,
//			@FormParam("waveFile") InputStream file,
//            @FormParam("waveFile") FormDataContentDisposition fileDetail) throws Exception{
//
//		IVRRespData resp = new IVRRespData();
//		resp.setCallerId(callerId);
//		resp.setUid(uid);
//		resp.setRespText("OK");
//		resp.setErrCode("0");
//		
//		try{
//			String data = receiveStream(file);
//			String fileName = WAVE_FILE_PATH +String.valueOf(System.currentTimeMillis())+".wav";
//			FileOutputStream os = new FileOutputStream(fileName);
//			os.write(data.getBytes("ISO8859-1"));
//			os.flush();
//			os.close();
//		}catch(Exception e){
//			e.printStackTrace();
//		}
//		
//		
//		return (new JSONObject(resp)).toString();
//	
//	}
//	
	
	@POST
	@Path("endCall")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String endCall(InputStream is) throws Exception{
		JSONObject resp = new JSONObject();
		
		try{
			String reqStr = super.receiveStream(is);
			JSONObject req = new JSONObject(reqStr);
			
			resp.put("callerId",req.getString("callerId"));
			resp.put("uid",req.getString("uid"));
			resp.put("errCode","0");
			resp.put("errMsg","");
		}catch(Exception e){
			e.printStackTrace();
			resp.put("errCode","99");
			resp.put("errMsg",e.getMessage());
			e.printStackTrace();
			log.debug(e.getMessage(), e);
		}
		return resp.toString();
	
	}
	@POST
	@Path("outCallBack")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String outCallBack(InputStream is) throws Exception{
		JSONObject resp = new JSONObject();
		
		try{
			String reqStr = super.receiveStream(is);
			JSONObject req = new JSONObject(reqStr);
			String bookingId = getJSONString(req,"callerId");
			String result = getJSONString(req,"result");
			BookingService service = new BookingService();
			int ret = service.updRemStatus(bookingId, result);
			resp.put("errCode",String.valueOf((ret-1)));				
			resp.put("outBoundSeq",getJSONString(req,"outBoundSeq"));
			resp.put("errMsg","");
		}catch(Exception e){
			e.printStackTrace();
			resp.put("errCode","99");
			resp.put("errMsg",e.getMessage());
			e.printStackTrace();
			log.debug(e.getMessage(), e);
		}
		return resp.toString();
	
	}

}
