package com.systex.b300.vbooking.dao;


import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.systex.b300.vbooking.sys.ConnectionManager;


public class BaseDao {
	private static Logger log = LogManager.getLogger("sysLog");
	ResultSet rs;
	PreparedStatement pStmt;
	Connection conn;
	String currSqlCmd;


	public void setConnection(Connection conn){
		this.conn = conn;
	}
	
	private Connection getConnection(){
		if(conn == null) conn = ConnectionManager.getDBConnection();
		return conn;		
	}
	
	protected PreparedStatement getPreparedStatement(String sql) throws SQLException{
//		log.info(sql);
		currSqlCmd = sql;
		pStmt = getConnection().prepareStatement(sql);
		return pStmt;
	}

	protected ResultSet executeQry(String sql,Object ...params) throws SQLException{
		getPreparedStatement(sql);
		setParam(params);
		rs = pStmt.executeQuery();
		return rs;
	}
	
	protected int executeUpdate(String sql,Object ... params) throws SQLException{		
		getPreparedStatement(sql);
		setParam(params);
		int rc = pStmt.executeUpdate();
		closePstmt();
		return rc;
	}

	protected void setParam(Object[] params) throws SQLException{
//		log.info("REAL SQL COMMAND==>"+getRealCommand(currSqlCmd, params));
//		System.out.println("REAL SQL COMMAND==>"+getRealCommand(currSqlCmd, params));
		if(params != null && params.length != 0){
			int idx = 1;
			for(Object obj : params){
				pStmt.setObject(idx++,obj);
			}
		}
	}
	
	protected ResultSet executeQry(String sql,List paramList) throws SQLException{
		
		return executeQry(sql,(paramList == null ? null:paramList.toArray()));
	}

	protected Object executeQrySingleData(String sql,String columnName,List paramList) throws SQLException{
		return executeQrySingleData(sql, columnName, (paramList == null ? null:paramList.toArray()));
	}
	
	protected Object executeQrySingleData(String sql,String columnName,Object ...params) throws SQLException{
		rs = executeQry(sql,params);
		if ( rs.next() ){
			Object retObj = rs.getObject(columnName);
			closePstmt();
			return retObj;
		}
		closePstmt();
		return null;
	}

	protected <A> A executeQry(Class<A> clazz, String sql, List paramList) throws Exception{
		return executeQry(clazz, sql,  (paramList == null ? null:paramList.toArray()));
	}

	protected <AA> AA executeQry(Class<AA> clazz,String sql,Object ...params) throws Exception{
		rs = executeQry(sql,params);
		if(rs == null || !rs.next()) return null;
		AA obj = clazz.newInstance();
		getBean(obj,rs);
		//if(rs != null ) rs.close();
		closePstmt();
		return obj;
	}
	
	protected <T> List<T> executeQryList(Class<T> clazz, String sql, List paramList) throws Exception{
		return executeQryList(clazz,sql,1, paramList.toArray());
	}

	protected int executeUpdate(String sql,List paramList) throws Exception{
		return executeUpdate(sql,paramList.toArray());
	}
	
	
//	protected <T> List<T>  executeQryListByPage(Class<T> clazz,String sql,PagePanelBean page,List<Object> paramList) throws Exception{
//		return this.executeQryListByPage(clazz, sql, page,paramList.toArray());
//	}
//	
//	protected <T> List<T> executeQryListByPage(Class<T> clazz,String sql,PagePanelBean page,Object ...params) throws Exception{
//		int rowFrom = ((page.getRequestPage()-1) * page.getPageSize()) + 1;
//		int rowTo = page.getRequestPage() * page.getPageSize();
//		
//		int totalRecords = getRowCount(sql, params);
//		page.setPageData(totalRecords);
//
//		if(totalRecords == 0)return null;
//		
//		String newSql = "SELECT ROWNUM AS ROW_NO,t.* FROM (SELECT ROWNUM AS ROWNUM1, d.* from ("+sql+") d) t where t.ROWNUM1 between "+rowFrom+" and "+rowTo;
//		return executeQryList(clazz,newSql,rowFrom,params);
//	}
//	
	protected int getRowCount(String sql,Object ...params) throws Exception{
		sql = "SELECT COUNT(*) as rowCount FROM ("+sql+") t";
		ResultSet rs = executeQry(sql,params);
		if(rs == null) return 0;
		if(rs.next()){
			return rs.getInt("rowCount");
		}else{
			return 0;
		}
	}
	
	protected <T> List<T> executeQryList(Class<T> clazz,String sql,Object ...params) throws Exception{
//		System.out.println("params is null:"+(params == null));
//		System.out.println("params Length:"+params.length);
		
		return this.executeQryList(clazz, sql, 1, params);
	}
	
	protected <T> List<T> executeQryList(Class<T> clazz,String sql,int startIdx,Object ...params) throws Exception{
		rs = executeQry(sql,params);
		if(rs == null) return null;
		List<T> retList = new ArrayList<T>();
		int idx = startIdx;
		while ( rs.next() ) {
			T obj = clazz.newInstance();
			getBean(obj,rs);
			try{
				BeanUtils.setProperty(obj, "seqNo", idx++);
			}catch(Exception e){
//				log.debug("seqNo set value fail exception:"+e.getMessage());
			}

			retList.add(obj);
		}
		//if(rs != null ) rs.close();
		closePstmt();
		return retList;
	}
	
	
	protected Object[] executeProcedure(String sql,List<Object> inParam,List<Integer> outParam) throws Exception{
		System.out.println("REAL SQL COMMAND:\r\n"+getRealCommand(sql, inParam.toArray()));

		CallableStatement cstamt = getConnection().prepareCall(sql);
		int idx = 1;
		if(inParam != null && !inParam.isEmpty() ){
			for(Object obj : inParam){
				cstamt.setObject(idx++,obj);
			}
		}
		int outIdx=idx;
		if(outParam != null && !outParam.isEmpty() ){
			for(Integer outType : outParam){
				cstamt.registerOutParameter(idx++, outType);
			}
		}
//		log.info("==============================Start Call Stored Procedure......");
		boolean rc = cstamt.execute();
//		log.info("==============================End Call Stored Procedure......rc:"+rc);
		
		if(outParam != null && !outParam.isEmpty() ){
			Object[] retArr = new Object[outParam.size()];
			for(int cnt=0;cnt<outParam.size();cnt++){
				retArr[cnt] = cstamt.getObject(outIdx+cnt);
			}
			cstamt.close();
			return retArr;
		}
		cstamt.close();
		return null;
	}
	
	protected void setBatch(String sql) throws Exception{
		getPreparedStatement(sql);
	}
	
	protected void addBatch(List parmList) throws Exception{
		setParam(parmList.toArray());
		pStmt.addBatch();
	}
	
	protected int[] executeBatch() throws Exception {
		if(pStmt == null) return new int[0];
		int[] retArr = pStmt.executeBatch();
//		pStmt.executeBatch();
		closeResult();
		return retArr;
	}
	
	protected StringBuffer addCondition(StringBuffer sb,Object val,String addSql,List<Object> paramList){
		if(val == null || "".equals(val)) return sb;
		sb.append(addSql);
		paramList.add(val);
		return sb;
	}
	
	protected StringBuffer addLikeCondition(StringBuffer sb,Object val,String addSql,List<Object> paramList){
		if(val == null || "".equals(val)) return sb;
		sb.append(addSql);
		paramList.add("%"+val+"%");
		return sb;
	}
	
	private Object getBean(Object obj,ResultSet rs) throws Exception{
		int collen = 0;
		try {
			collen = rs.getMetaData().getColumnCount();
		} catch (SQLException e) {
//			log.error("get Columns Fail Exception:"+e.getMessage());
			return null;
		}
//		for(int cnt=0;cnt<collen;cnt++){
//			String columnName = rs.getMetaData().getColumnLabel(cnt+1);
//			System.out.println("columnName:"+columnName);
//
//		}
		
		for(int cnt=0;cnt<collen;cnt++){
			String columnName;
			columnName = rs.getMetaData().getColumnName(cnt+1);
//			columnName = rs.getMetaData().getColumnLabel(cnt+1);
			Object val = rs.getObject(columnName);
			if(val == null) continue;
			String name = getVarName(columnName);
			try {
				BeanUtils.setProperty(obj, name, val);
			} catch (Exception e) {
//				log.debug(name+" set value fail exception:"+e.getMessage());
			}
		}
		return obj;
	}
	
	private String getVarName(String name){
		if(name == null || "".equals(name)) return "";
		String[] strArr = name.toLowerCase().split("_");
		StringBuffer sb = new StringBuffer();
		for(String str : strArr){
			if(str == null || str.isEmpty()) continue;
			if(sb.length() == 0){
				sb.append(str);
			}else{
				sb.append(str.substring(0,1).toUpperCase()).append(str.substring(1));
			}
		}
		return sb.toString();
	}
	
	private Object copyBean(Object src,Class clazz) throws Exception{
		Object target = clazz.newInstance();
		BeanUtils.copyProperties(target, src);
		return target;
	}
	
	public void closeResult(){
		try {
			if(rs != null){
				rs.close();
				rs = null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}	
	
	public void closePstmt(){
		try {
			closeResult();
			if(pStmt != null){
				pStmt.close();
				pStmt = null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void closeConnection(){
		try {
			if(conn != null) conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}	
	}
	
	public void closeDB(){
		closeResult();
		closePstmt();
		closeConnection();
	}
	
	private String getRealCommand(String pre_command,Object[] paramMap) throws SQLException {
		try{
			StringBuffer buffer = new StringBuffer();
			int i = 0;
			int startIndex = 0;
			int index = 0;
			if (pre_command.indexOf("{") != -1) { // Call Function
				int equalIndex = pre_command.indexOf("=", startIndex);
				if (equalIndex != -1) {
					startIndex = equalIndex;
					buffer.append(pre_command.substring(0, equalIndex));
					i = 2;
				}
			} else { // SQL
				startIndex = 0;
			}
			if(pre_command.indexOf("?") == -1 ||  paramMap == null || paramMap.length == 0 ){
				return pre_command;
			}
			for (; (index = pre_command.indexOf("?", startIndex)) != -1; i++) {
				buffer.append(pre_command.substring(startIndex, index));
				if(paramMap.length <= i) break;
				Object obj = paramMap[i];
				String param = null;
				if (obj == null) {
					param = "NULL";
	//				throw new SQLException("Missing Parameter " + i);
				} else 
				if (obj instanceof Boolean) {
					param = ((Boolean) obj).booleanValue() ? String.valueOf(1)
							: String.valueOf(0);
				} else if (obj instanceof Integer) {
					param = obj.toString();
				} else if (obj instanceof Long) {
					param = obj.toString();
				} else if (obj instanceof Float) {
					param = obj.toString();
				} else if (obj instanceof Double) {
					param = obj.toString();
				} else if (obj instanceof BigDecimal) {
					BigDecimal b = (BigDecimal)obj;
					param = b.toPlainString();
				} else if (obj instanceof Timestamp) {
					param = "TO_TIMESTAMP('" + obj.toString()
							+ "','YYYY-MM-DD HH24:MI:SS.FF')";
					// For Oracle
				} else {
					param = "'" + obj.toString() + "'";
				}
				startIndex = index + 1;
				buffer.append(param);
			}
			buffer.append(pre_command.substring(startIndex));
			return buffer.toString();
		}catch(Exception e){
			e.printStackTrace();
			return e.getMessage();
		}
	}
	
	public int executeInsert(String tableName,String[] cols,Object data) throws Exception{
//		if(data instanceof List){
//			return executeInsertList(tableName,cols,(List)data);
//		}
		StringBuffer sqls = new StringBuffer();
		sqls.append("INSERT INTO ").append(tableName).append("(");
		List<Object> paramList = new ArrayList<Object>();
		for(String col : cols){
			Object obj = null;
			String fldName = getVarName(col);
			try{
				obj = PropertyUtils.getProperty(data,fldName );
			}catch(Exception e){
				System.out.println("["+fldName+"] exception:"+e.getMessage());
			}
			if(!isBlank(obj)){
				sqls.append(col).append(",");
				paramList.add(obj);
			}
		}
		if(paramList.isEmpty()) return 0;
		sqls.setCharAt(sqls.length()-1, ')');
		sqls.append(" VALUES(");
		for(int cnt=0;cnt<paramList.size();cnt++){
			sqls.append("?,");
		}
		sqls.setCharAt(sqls.length()-1, ')');
//		System.out.println("real SQL:"+getRealCommand(sqls.toString(), paramList.toArray()));
		return this.executeUpdate(sqls.toString(), paramList);
	}

	
	private int executeInsertList(String tableName,String[] cols, List<Object> list) throws Exception{
		StringBuffer sqls = new StringBuffer();
		sqls.append("INSERT INTO ").append(tableName).append("(");
		for(String col : cols){
			sqls.append(col).append(",");
		}
		sqls.setCharAt(sqls.length()-1, ')');
		sqls.append(" VALUES(");
		for(int cnt=0;cnt<cols.length;cnt++){
			sqls.append("?,");
		}
		sqls.setCharAt(sqls.length()-1, ')');
		setBatch(sqls.toString());
		System.out.println("Start Prepare Data");
		long st = System.currentTimeMillis();
		for (Object data : list){
			List<Object> paramList = new ArrayList<Object>();			
			for(String col : cols){
				Object obj = PropertyUtils.getProperty(data, getVarName(col));
				sqls.append(col).append(",");
				paramList.add(obj);
			}
			addBatch(paramList);
		}
		long et = System.currentTimeMillis();

		System.out.println("end Prepare Data");
		System.out.println("time:"+String.valueOf((et-st) / 1000));

		int retInt = 0;

		System.out.println("Start Execute DB");
		st = System.currentTimeMillis();
		int[] rcArr = executeBatch();
		et = System.currentTimeMillis();
		System.out.println("End Execute DB");
		System.out.println("time:"+String.valueOf((et-st) / 1000));

		for(int cnt : rcArr) retInt = retInt + cnt;
		return retInt;
	}
	
	public int executeUpdate(String tableName,String[] keys,String[] cols,Object data) throws Exception{
		StringBuffer sqls = new StringBuffer();
		if(keys == null || keys.length == 0 ) throw new Exception("沒有設定Primary Key欄位");
		sqls.append("UPDATE ").append(tableName).append(" SET ");
		List<Object> paramList = new ArrayList<Object>();
		for(String col : cols){
			Object obj = PropertyUtils.getProperty(data, getVarName(col));
			if(!isBlank(obj)){
				sqls.append(col).append(" = ?,");
				paramList.add(obj);
			} 
		}
		if(paramList.isEmpty()) return 0; 
		sqls.setLength(sqls.length() - 1);
		
		sqls.append(" WHERE 1=1 ");
		for(String key : keys){
			Object obj = PropertyUtils.getProperty(data, getVarName(key));
			System.out.println("Key:"+key+",value:"+obj);
			if(!isBlank(obj)){
				sqls.append(" AND ").append(key).append(" = ? ");
				paramList.add(obj);
			}
		}
		return this.executeUpdate(sqls.toString(), paramList);
	}
	
	public boolean isBlank(Object data){
		if(data == null) return true;
		if(data instanceof String){
			String str = (String)data;
			if(str.length() == 0 || str.isEmpty()) return true;
			return false;
		}else{
			return false;
		}
	}
}
