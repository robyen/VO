package com.systex.b300.vbooking.sys;


import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ConnectionManager {
	private static Logger log = LogManager.getLogger("sysLog");
	private static ThreadLocal<Connection> dbConn = new ThreadLocal<Connection>();
	
	public static final String JDBC_DRIVER ="Jdbc_Driver";
	public static final String JDBC_URL ="Jdbc_URL";
	public static final String JDBC_USER_NAME ="Jdbc_User_Name";
	public static final String JDBC_PASSWORD ="Jdbc_Password";
	
	private static final String Jdbc_Driver = "com.mysql.cj.jdbc.Driver";
	private static final String Jdbc_URL = "jdbc:mysql://localhost:3306/vbooking?verifyServerCertificate=false&useSSL=false&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
	private static final String Jdbc_User_Name = "root";
	private static final String Jdbc_Password = "Cl2493k5p172";
//	private static final String Jdbc_URL = "jdbc:mysql://10.109.1.50:3306/ec?verifyServerCertificate=false&useSSL=false&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
//	private static final String Jdbc_User_Name = "mysql";
//	private static final String Jdbc_Password = "password";
	
	
	public static Connection getDBConnection(){

		return getDBConnection(true);
	}
	public static Connection getDBConnection(boolean isCreateNew){
		try{
			Connection conn = dbConn.get();
			if(!isCreateNew) return conn;
			if(conn != null && !conn.isClosed()) return conn;
			File f = new File("/root/jdbc.pro");
			if(f.exists()){
				Properties p = new Properties();
				p.load(new FileInputStream(f));
				conn = getConnection(p);
			}else{
				conn = getConnection();
			}
			dbConn.set(conn);
			return conn;
		}catch(Exception e){
			e.printStackTrace();
//			log.debug(e);
			return null;
		}
	}
	
	private static Connection getConnection(Properties property) throws Exception{
		try{
			Class.forName(property.getProperty(JDBC_DRIVER));
			String url = property.getProperty(JDBC_URL);					
			String usr = property.getProperty(JDBC_USER_NAME);
			String pwd = property.getProperty(JDBC_PASSWORD);
			Connection conn = DriverManager.getConnection(url, usr, pwd);
			log.info("Thread{"+Thread.currentThread().getId()+"} Get DB Connection :"+conn);
			
			return conn;
		}catch(Exception e){
			e.printStackTrace();
//			log.debug(e);
			return null;
		}
	}
	
	public static Connection getConnection(String driver,String url,String user,String passwd) throws Exception{
		try{
			Properties pro = new Properties();
			pro.put(JDBC_DRIVER, driver);
			pro.put(JDBC_URL, url);
			pro.put(JDBC_USER_NAME, user);
			pro.put(JDBC_PASSWORD, passwd);
			return getConnection(pro);
		}catch(Exception e){
			e.printStackTrace();
//			log.debug(e);
			return null;
		}
	}
	
	public static Connection getConnection() throws Exception{
		try{
			return getConnection(Jdbc_Driver,Jdbc_URL,Jdbc_User_Name,Jdbc_Password);
		}catch(Exception e){
//			log.debug(e);
			e.printStackTrace();
			return null;
		}
	}	

	public static void closeConnection(){
		try{
			Connection conn = dbConn.get();
			if(conn == null || conn.isClosed()) return;
			log.info("Thread{"+Thread.currentThread().getId()+"} Close DB Connection :"+conn);
			conn.close();
			dbConn.remove();
		}catch(Exception e){
			log.debug(e.getMessage());
		}
		
	}
	
}
