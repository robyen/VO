package com.systex.b300.vbooking.vo;

import com.google.gson.annotations.SerializedName;

public class RestaurantVo extends BaseVo{
	String city;
	String area;
	String name;
	String addr;
	String landmark;
	String landmark2;
	String tel;	
	String apiKey;
	String groupId;
	String companyId;
	String branchId;	
	
	
	@SerializedName("restaurant_id")
	String restaurantId;
	
	@SerializedName("name_eng")
	String nameEng;
	
	@SerializedName("name_nick")
	String nameNick;
	
	@SerializedName("name_abbr")
	String nameAbbr;
	
	@SerializedName("business_time")
	String businessTime;
	
	@SerializedName("is_chain_store")
	String isChainStore;
	
	@SerializedName("is_contract_store")
	String isContractStore;
	
	@SerializedName("branch_name")
	String branchName;
	
	@SerializedName("opt1_desc")
	String opt1Desc;
	
	@SerializedName("opt1_resp")
	String opt1Resp;
	
	@SerializedName("opt1_num_desc")
	String opt1NumDesc;
	
	
	@SerializedName("opt2_desc")
	String opt2Desc;
	
	@SerializedName("opt2_resp")
	String opt2Resp;
	
	@SerializedName("opt2_num_desc")
	String opt2NumDesc;
	
	
	@SerializedName("opt3_desc")
	String opt3Desc;
	
	@SerializedName("opt3_resp")
	String opt3Resp;
	
	@SerializedName("opt3_num_desc")
	String opt3NumDesc;
	
	@SerializedName("is_can_waiting")
	String isCanWaiting;
	
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAddr() {
		return addr;
	}
	public void setAddr(String addr) {
		this.addr = addr;
	}
	public String getLandmark() {
		return landmark;
	}
	public void setLandmark(String landmark) {
		this.landmark = landmark;
	}
	public String getLandmark2() {
		return landmark2;
	}
	public void setLandmark2(String landmark2) {
		this.landmark2 = landmark2;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	
	public String getRestaurantId() {
		return restaurantId;
	}
	public void setRestaurantId(String restaurantId) {
		this.restaurantId = restaurantId;
	}
	public String getNameEng() {
		return nameEng;
	}
	public void setNameEng(String nameEng) {
		this.nameEng = nameEng;
	}
	public String getNameNick() {
		return nameNick;
	}
	public void setNameNick(String nameNick) {
		this.nameNick = nameNick;
	}
	public String getNameAbbr() {
		return nameAbbr;
	}
	public void setNameAbbr(String nameAbbr) {
		this.nameAbbr = nameAbbr;
	}
	public String getBusinessTime() {
		return businessTime;
	}
	public void setBusinessTime(String businessTime) {
		this.businessTime = businessTime;
	}
	public String getIsChainStore() {
		return isChainStore;
	}
	public void setIsChainStore(String isChainStore) {
		this.isChainStore = isChainStore;
	}
	public String getIsContractStore() {
		return isContractStore;
	}
	public void setIsContractStore(String isContractStore) {
		this.isContractStore = isContractStore;
	}
	public String getBranchName() {
		return branchName;
	}
	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}
	public String getOpt1Desc() {
		return opt1Desc;
	}
	public void setOpt1Desc(String opt1Desc) {
		this.opt1Desc = opt1Desc;
	}
	public String getOpt1Resp() {
		return opt1Resp;
	}
	public void setOpt1Resp(String opt1Resp) {
		this.opt1Resp = opt1Resp;
	}
	public String getOpt1NumDesc() {
		return opt1NumDesc;
	}
	public void setOpt1NumDesc(String opt1NumDesc) {
		this.opt1NumDesc = opt1NumDesc;
	}
	public String getOpt2Desc() {
		return opt2Desc;
	}
	public void setOpt2Desc(String opt2Desc) {
		this.opt2Desc = opt2Desc;
	}
	public String getOpt2Resp() {
		return opt2Resp;
	}
	public void setOpt2Resp(String opt2Resp) {
		this.opt2Resp = opt2Resp;
	}
	public String getOpt2NumDesc() {
		return opt2NumDesc;
	}
	public void setOpt2NumDesc(String opt2NumDesc) {
		this.opt2NumDesc = opt2NumDesc;
	}
	public String getOpt3Desc() {
		return opt3Desc;
	}
	public void setOpt3Desc(String opt3Desc) {
		this.opt3Desc = opt3Desc;
	}
	public String getOpt3Resp() {
		return opt3Resp;
	}
	public void setOpt3Resp(String opt3Resp) {
		this.opt3Resp = opt3Resp;
	}
	public String getOpt3NumDesc() {
		return opt3NumDesc;
	}
	public void setOpt3NumDesc(String opt3NumDesc) {
		this.opt3NumDesc = opt3NumDesc;
	}
	public String getIsCanWaiting() {
		return isCanWaiting;
	}
	public void setIsCanWaiting(String isCanWaiting) {
		this.isCanWaiting = isCanWaiting;
	}
	public String getApiKey() {
		return apiKey;
	}
	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}
	public String getGroupId() {
		return groupId;
	}
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	public String getBranchId() {
		return branchId;
	}
	public void setBranchId(String branchId) {
		this.branchId = branchId;
	}
	public String getCompanyId() {
		return companyId;
	}
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	
}
