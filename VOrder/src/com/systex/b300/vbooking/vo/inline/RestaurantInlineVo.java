package com.systex.b300.vbooking.vo.inline;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.annotations.SerializedName;
import com.systex.b300.vbooking.vo.BaseVo;

public class RestaurantInlineVo extends BaseVo{
	String apiKey;
	String groupId;
	String branchId;
	
	String id;
	String name;
	String phoneNumber;
	String companyId;
	List<String> images;
	String price;
	boolean webBookingEnabled;
	int maxBookingGroupSize;
	int minBookingGroupSize;
	int minReservationOffset;
	int maxReservationOffset;
	boolean webKidsSelectorHidden;
	boolean webKidChairsSelectorHidden;
	CompanyVo company;
	LocationVo location;
	String minimumCharge;
	List<OpeningTime> openingTimes;
	WaitingInfo waitingInfo;
	Map<String,Map<String,Map<String,String>>> bookingInfo;
	Object menus;
	Object groupMeta;
	Object alternateWaitingBranches;
	Map<String,List<Integer>> openTimes = new HashMap<String,List<Integer>>();
	
	public Map<String,List<Integer>> getOpenTimes() {
		return openTimes;
	}

	public void setOpenTimes(Map<String,List<Integer>> openTimes) {
		this.openTimes = openTimes;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public List<String> getImages() {
		return images;
	}

	public void setImages(List<String> images) {
		this.images = images;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public boolean isWebBookingEnabled() {
		return webBookingEnabled;
	}

	public void setWebBookingEnabled(boolean webBookingEnabled) {
		this.webBookingEnabled = webBookingEnabled;
	}

	public int getMaxBookingGroupSize() {
		return maxBookingGroupSize;
	}

	public void setMaxBookingGroupSize(int maxBookingGroupSize) {
		this.maxBookingGroupSize = maxBookingGroupSize;
	}

	public int getMinBookingGroupSize() {
		return minBookingGroupSize;
	}

	public void setMinBookingGroupSize(int minBookingGroupSize) {
		this.minBookingGroupSize = minBookingGroupSize;
	}

	public int getMinReservationOffset() {
		return minReservationOffset;
	}

	public void setMinReservationOffset(int minReservationOffset) {
		this.minReservationOffset = minReservationOffset;
	}

	public int getMaxReservationOffset() {
		return maxReservationOffset;
	}

	public void setMaxReservationOffset(int maxReservationOffset) {
		this.maxReservationOffset = maxReservationOffset;
	}

	public boolean isWebKidsSelectorHidden() {
		return webKidsSelectorHidden;
	}

	public void setWebKidsSelectorHidden(boolean webKidsSelectorHidden) {
		this.webKidsSelectorHidden = webKidsSelectorHidden;
	}

	public boolean isWebKidChairsSelectorHidden() {
		return webKidChairsSelectorHidden;
	}

	public void setWebKidChairsSelectorHidden(boolean webKidChairsSelectorHidden) {
		this.webKidChairsSelectorHidden = webKidChairsSelectorHidden;
	}

	public CompanyVo getCompany() {
		return company;
	}

	public void setCompany(CompanyVo company) {
		this.company = company;
	}

	public LocationVo getLocation() {
		return location;
	}

	public void setLocation(LocationVo location) {
		this.location = location;
	}

	public String getMinimumCharge() {
		return minimumCharge;
	}

	public void setMinimumCharge(String minimumCharge) {
		this.minimumCharge = minimumCharge;
	}

	public List<OpeningTime> getOpeningTimes() {
		return openingTimes;
	}

	public void setOpeningTimes(List<OpeningTime> openingTimes) {
		this.openingTimes = openingTimes;
	}

	public WaitingInfo getWaitingInfo() {
		return waitingInfo;
	}

	public void setWaitingInfo(WaitingInfo waitingInfo) {
		this.waitingInfo = waitingInfo;
	}


	public Map<String, Map<String, Map<String, String>>> getBookingInfo() {
		return bookingInfo;
	}

	public void setBookingInfo(Map<String, Map<String, Map<String, String>>> bookingInfo) {
		this.bookingInfo = bookingInfo;
	}

	public Object getMenus() {
		return menus;
	}

	public void setMenus(Object menus) {
		this.menus = menus;
	}

	public Object getGroupMeta() {
		return groupMeta;
	}

	public void setGroupMeta(Object groupMeta) {
		this.groupMeta = groupMeta;
	}

	public Object getAlternateWaitingBranches() {
		return alternateWaitingBranches;
	}

	public void setAlternateWaitingBranches(Object alternateWaitingBranches) {
		this.alternateWaitingBranches = alternateWaitingBranches;
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

	class LocationVo extends BaseVo{
		float lat;
		float lng;
		public float getLat() {
			return lat;
		}
		public void setLat(float lat) {
			this.lat = lat;
		}
		public float getLng() {
			return lng;
		}
		public void setLng(float lng) {
			this.lng = lng;
		}
		
		
	}

	class CompanyVo {
		String id;
		String name;
		List<TagVo> tags;
		String webBannerBackgroundColor;
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public List<TagVo> getTags() {
			return tags;
		}
		public void setTags(List<TagVo> tags) {
			this.tags = tags;
		}
		public String getWebBannerBackgroundColor() {
			return webBannerBackgroundColor;
		}
		public void setWebBannerBackgroundColor(String webBannerBackgroundColor) {
			this.webBannerBackgroundColor = webBannerBackgroundColor;
		}
		
	}
	
	 class TagVo extends BaseVo{
			String id;
			String name;
			public String getId() {
				return id;
			}
			public void setId(String id) {
				this.id = id;
			}
			public String getName() {
				return name;
			}
			public void setName(String name) {
				this.name = name;
			}
	 }
	 
	 public class OpeningTime extends BaseVo{
		 String start;
		 String end;
		public String getStart() {
			return start;
		}
		public void setStart(String start) {
			this.start = start;
		}
		public String getEnd() {
			return end;
		}
		public void setEnd(String end) {
			this.end = end;
		}
		 
	 }
	
	 class WaitingInfo extends BaseVo{
		int estimatedWaitingMinutes;
		int numberOfWaitings;
		String status;
		
		public int getEstimatedWaitingMinutes() {
			return estimatedWaitingMinutes;
		}
		public void setEstimatedWaitingMinutes(int estimatedWaitingMinutes) {
			this.estimatedWaitingMinutes = estimatedWaitingMinutes;
		}
		public int getNumberOfWaitings() {
			return numberOfWaitings;
		}
		public void setNumberOfWaitings(int numberOfWaitings) {
			this.numberOfWaitings = numberOfWaitings;
		}
		public String getStatus() {
			return status;
		}
		public void setStatus(String status) {
			this.status = status;
		}
		 
	 }
	 
	 class BookingInfo extends BaseVo{
		 Map<String,String> booking;
	 }
	 
}
