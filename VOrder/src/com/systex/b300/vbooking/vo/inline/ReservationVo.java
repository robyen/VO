package com.systex.b300.vbooking.vo.inline;

import java.util.List;

import com.systex.b300.vbooking.vo.BaseVo;

public class ReservationVo extends BaseVo {
	String customerName;
	int gender = 2;
	String phone;
	String email;
	String language= "zh-TW";
	int groupSize = 0;
	int numberOfKidChairs = 0;
	int numberOfKidSets = 0;
	String datetime;
	String contactName;
	int contactSex;
	String contactPhone;
	String contactEmail;
	String fbPageId;
	String fbUserId;
	String note;
	String customerNote;
	List<String> preferredTableTags;
	String createdFrom="systex";
	boolean shouldNotifyCustomer = false;
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public int getGender() {
		return gender;
	}
	public void setGender(int gender) {
		this.gender = gender;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	public int getGroupSize() {
		return groupSize;
	}
	public void setGroupSize(int groupSize) {
		this.groupSize = groupSize;
	}
	public int getNumberOfKidChairs() {
		return numberOfKidChairs;
	}
	public void setNumberOfKidChairs(int numberOfKidChairs) {
		this.numberOfKidChairs = numberOfKidChairs;
	}
	public int getNumberOfKidSets() {
		return numberOfKidSets;
	}
	public void setNumberOfKidSets(int numberOfKidSets) {
		this.numberOfKidSets = numberOfKidSets;
	}
	public String getDatetime() {
		return datetime;
	}
	public void setDatetime(String datetime) {
		this.datetime = datetime;
	}
	public String getContactName() {
		return contactName;
	}
	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	public int getContactSex() {
		return contactSex;
	}
	public void setContactSex(int contactSex) {
		this.contactSex = contactSex;
	}
	public String getContactPhone() {
		return contactPhone;
	}
	public void setContactPhone(String contactPhone) {
		this.contactPhone = contactPhone;
	}
	public String getContactEmail() {
		return contactEmail;
	}
	public void setContactEmail(String contactEmail) {
		this.contactEmail = contactEmail;
	}
	public String getFbPageId() {
		return fbPageId;
	}
	public void setFbPageId(String fbPageId) {
		this.fbPageId = fbPageId;
	}
	public String getFbUserId() {
		return fbUserId;
	}
	public void setFbUserId(String fbUserId) {
		this.fbUserId = fbUserId;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public String getCustomerNote() {
		return customerNote;
	}
	public void setCustomerNote(String customerNote) {
		this.customerNote = customerNote;
	}
	public List<String> getPreferredTableTags() {
		return preferredTableTags;
	}
	public void setPreferredTableTags(List<String> preferredTableTags) {
		this.preferredTableTags = preferredTableTags;
	}
	public String getCreatedFrom() {
		return createdFrom;
	}
	public void setCreatedFrom(String createdFrom) {
		this.createdFrom = createdFrom;
	}
	public boolean isShouldNotifyCustomer() {
		return shouldNotifyCustomer;
	}
	public void setShouldNotifyCustomer(boolean shouldNotifyCustomer) {
		this.shouldNotifyCustomer = shouldNotifyCustomer;
	}
	
	
	
}
