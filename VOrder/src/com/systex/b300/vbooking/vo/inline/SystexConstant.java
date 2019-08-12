package com.systex.b300.vbooking.vo.inline;

public class SystexConstant {
	public static final String COMPNAY_ID ="-LT5StKQwodH0qFcdSlk:inline-live-2a466";
	public static final String BRANCH_ID ="-LT5StN9tjJyrzSUyaCl";
	public static final String GROUP_ID ="systex";
	public static final String API_KEY ="Waskq4ERPzRFDbpKMptg3vwtdPsbXRvX";
	public static final String INLINE_BASE_URL = "https://api.inline.app/";
	public static final String INLINE_RESSERVATION_URL = INLINE_BASE_URL+"reservations/"+COMPNAY_ID+"/"+BRANCH_ID+"/";
	public static final String TEST_SIP = "07010160369";
	public static final boolean ONLINE_QRY = true;
	public static final boolean SEND_SMS = false;
	public static final boolean WRITE_DB_LOG = true;
	
	public static final String SMS_USER = "API90315001";
	public static final String SMS_PWD = "5td83rf";
	public final static String SMS_URL = "http://sms.systex.com.tw:17760/HTTP_API/input.cgi?";
	public final static String SMS_DATA = "UserID=%s&UserPWD=%s&MSISDN=%s&SMSBODY=%s&Encoding=BIG5&RevTime=%s";
	public final static String SMS_ASCII_DATA = "UserID=%s&UserPWD=%s&MSISDN=%s&SMSBODY=%s&Encoding=ASCII&RevTime=%s";
	
	public static final String DEMO_SIP = "07010108069";
	
	
	public static final String INLINE_ERROR_NO_SEAT = "No capacity available";
	public static final String INLINE_ERROR_OVER_LIMIT = "Customer not allowed to make more reservations";
}
