package com.systex.b300.vbooking.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

public class BaseController {
	
	protected String receiveStream(InputStream is ){
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
	
	protected boolean isBlank(String data){
		if(data == null) return true;
		if(data.isEmpty()) return true;
		if("　".equals(data)) return true;
		return false;
	}
}
