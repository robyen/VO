package com.systex.b300.vorder.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.json.JSONObject;

import com.systex.b300.vorder.vo.IVRReqData;

@Path("/order")
public class IVRController {

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public JSONObject sendASR(InputStream is) throws Exception{
		String reqStr = receiveStream(is);
		JSONObject json = new JSONObject(reqStr);
		
		return json;
	
	}

	private String receiveStream(InputStream is ){
	    StringBuffer buffer = null;

	    if (is != null) {
		    buffer = new StringBuffer();
		    try {
			     Reader in = new BufferedReader(
			                new InputStreamReader(is,"UTF-8"));
			     int ch;
			     while ((ch = in.read()) > -1) {
			         buffer.append((char)ch);
			     }
			     in.close();
			 } catch (IOException e) {
			            return null;
			 }
		} else {
			return null;
		}
	    return buffer.toString();
	}
	

}
