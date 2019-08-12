package com.systex.b300.vbooking.sys;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import com.systex.b300.vbooking.service.LogService;
import com.systex.b300.vbooking.vo.LogVo;
import com.systex.b300.vbooking.vo.inline.SystexConstant;

public class WriteLogThread extends Thread {
	
	static List<LogVo> logs = new ArrayList<LogVo>();
	static LogService logService;
	static final long SLEEP_TIME = 10 * 1000;
	static WriteLogThread instance = null;
	
	private LogService getService(){
		if(logService == null){
			logService = new LogService();
		}
		return logService;
	}
	
	public WriteLogThread(){
	}
	
	public static void addLog(LogVo log){
		if (!SystexConstant.WRITE_DB_LOG) return;
		logs.add(log);
		if(instance == null){
			instance = new WriteLogThread();
			instance.start();
		}
		if(instance.isInterrupted() || !instance.isAlive()){
			instance.start();
		}
		
	}
	

	@Override
	public void run() {
		while(true){
			try{
				while(!logs.isEmpty()){
					LogVo log = logs.get(0);
					getService().addLog(log);
					logs.remove(0);
				}
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				delay(SLEEP_TIME);
			}
		}
	}
	
	private void delay(long time){
		try{
			Thread.sleep(time);
		}catch(Exception e){
			this.interrupt();
		}
	}
	
}
