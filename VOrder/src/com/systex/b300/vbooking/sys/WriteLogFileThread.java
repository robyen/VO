package com.systex.b300.vbooking.sys;

import java.io.FileOutputStream;

public class WriteLogFileThread extends Thread {
	
	String ttsString;
	String asrString;
	static final String LOG_FILE_NAME = "/home/systex/tomcat8/webapps/VBooking/ASR.txt";

	public WriteLogFileThread(String asrString,String ttsString){
		this.ttsString = ttsString;
		this.asrString = "["+asrString+"]";
	}

	@Override
	public void run() {
		try{
			FileOutputStream os = new FileOutputStream(LOG_FILE_NAME,true);
			String data = asrString+"\r\n"+ttsString+"\r\n";
			os.write(data.getBytes());
//			os.write("\r\n".getBytes());
//			os.write(ttsString.getBytes());
//			os.write("\r\n".getBytes());
			os.flush();
			os.close();
			BroadcasterServer.broadcasterMessage(data);
		}catch(Exception e){
			e.printStackTrace();
			System.out.println("Write File:"+LOG_FILE_NAME+" fail:"+e.getMessage());
		}
	}
	
}
