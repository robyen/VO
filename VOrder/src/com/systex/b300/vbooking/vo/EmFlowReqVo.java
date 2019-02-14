package com.systex.b300.vbooking.vo;

public class EmFlowReqVo<T> {
	String user_id;
	String text;
	T task_info;
	
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public T getTask_info() {
		return task_info;
	}
	public void setTask_info(T task_info) {
		this.task_info = task_info;
	}
	
	
}
