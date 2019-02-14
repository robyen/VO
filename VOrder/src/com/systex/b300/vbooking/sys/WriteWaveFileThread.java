package com.systex.b300.vbooking.sys;

import java.io.FileOutputStream;

public class WriteWaveFileThread extends Thread {
	
	String fileName;
	byte[] data;
	
	public WriteWaveFileThread(String fileName,byte[] data){
		this.fileName = fileName;
		this.data = data;
	}

	@Override
	public void run() {
		try{
			FileOutputStream os = new FileOutputStream(fileName);
			os.write(data);
			os.flush();
			os.close();
		}catch(Exception e){
			e.printStackTrace();
			System.out.println("Write File:"+fileName+" fail:"+e.getMessage());
		}
	}
	
}
