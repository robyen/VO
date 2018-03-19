package com.systex.b300.vorder.vo;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;


public class BaseVo implements java.io.Serializable{
	
	private static final long serialVersionUID = 1L;
	
	public boolean isBlank(String str){
		return (str == null || str.isEmpty() || str.length() == 0);
	}
	
	public String toString() {
        StringBuffer sb = new StringBuffer();

        try{
	        BeanInfo info = Introspector.getBeanInfo(this.getClass());
		    for (PropertyDescriptor pd : info.getPropertyDescriptors()) {
		    	Object obj = null;
		    	try{
			        Method reader = pd.getReadMethod();
			        if (reader != null) obj = reader.invoke(this);
		    	}catch(Exception e){
		    	}
		    	sb.append(pd.getName()).append("=[").append(String.valueOf(obj)).append("],");
		    }
        }catch(Exception e){
        	
        }
        return sb.toString();
    }
	
	public Map<String,Object> toMap(){
        Map<String,Object> map = new HashMap<String,Object>();
        try{
	        BeanInfo info = Introspector.getBeanInfo(this.getClass());
		    for (PropertyDescriptor pd : info.getPropertyDescriptors()) {
		    	Object obj = null;
		    	if("class".equals(pd.getName())) continue;
		    	try{
			        Method reader = pd.getReadMethod();
			        if (reader != null) obj = reader.invoke(this);
		    	}catch(Exception e){
		    	}
		    	Object data = obj == null ? "" : obj;
		    	map.put(pd.getName(),data);
		    }
        }catch(Exception e){
        	
        }
        return map;
	}
	
}
