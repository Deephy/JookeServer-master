package org.dartmouth.common;

import org.dartmouth.service.EventCacheService;
import org.dartmouth.service.impl.EventCacheEhCacheImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Yaozhong Kang
 * @date May 20, 2014
 */
public interface GlobalVariables {
	
	/* The maximum events returned to the client when the client search for the nearby events*/
	public static final int MAX_NEAR_EVENTS = 10;
	
	/* Checking interval to avoid busy checking */
	public static final int CHECKING_INTERVAL = 1 * 1000;  // 1s
	
	/* The heart beat tolerance : N times of the heart beat interval*/
	public static final int HEART_BEAT_TOL =  20 * 1000;     //
	
	/* Time limit of a event that can be run */
	public static final int EVENT_TIME_LIMIT = 30 * 60 * 1000;     // 1 min
			
    //3* 60* 60 * 1000;       // 3 hours
	

	
	interface RESPONSE_KEYS {
		public static final String SUCCESS = "success";
		public static final String MSG = "msg";
		public static final String USERID = "userid";
		public static final String EVENTID = "event_id";
		public static final String FULLNAME = "fullname";
		public static final String PROFILE_IMG = "profile_img";
	}

	interface RESPONSE_MESSAGES {
		public static final String INVALID_PARAMETERS = "Invalid Params";
		public static final String DB_ERROR = "Dabase Failure";
		public static final String LOGIN_FAIL = "Wrong Username/Password";
		public static final String SIGN_UP_DUPLICATE = "User Already Existed";
		public static final String EVENT_USER_MISMATCH = "Permission Denied";
		public static final String USER_NOT_EXIST = "User Doesn't Exist";
		public static final String EVENT_EXIST = "Event Already Existed";   // based on the host id
		
		// For forget password
		public static final String KEY_NOT_EXIST = "Key Not Exist";
		public static final String KEY_EXPIRED = "Key Expired";
		public static final String KEY_MATCH = "Key Match";	
		
		public static final String SEND_EMAIL = "Email Already Sent";
		public static final String SYSTEM_ERROR = "System Error";
		public static final String RESET_PWD_SUCCESS = "Reset Password Success";
		public static final String EVENT_NOT_EXIST = "Event Not Exist";
	}
	
	interface REQUEST_KEYS {
		
		// Event Controller
		public static final String KEY_ID = "id";
		public static final String KEY_EVENT_ID = "event_id";
		public static final String KEY_HOST_ID = "host_id";
		public static final String KEY_HOST_IP = "host_ip";
		public static final String KEY_USER_IP = "user_ip";
		public static final String KEY_HOST_PROFILE_IMG = "host_profile_img";
		public static final String KEY_HOST_NAME = "host_name";
		public static final String KEY_LAT = "current_loc_lat";
		public static final String KEY_LON = "current_loc_lon";
		public static final String KEY_ZIP_CODE = "current_zip";
		
		
		// User Controller
		public static final String KEY_LOGIN_TYPE = "login_type";
		public static final String KEY_THIRD_PARTY_ID = "thirdparty_id";
		public static final String KEY_EMAIL = "email";
		public static final String KEY_PASSWORD = "password";
		public static final String KEY_USER_ID = "userid";
		
		public static final String KEY_FULLNAME = "fullname";
		public static final String KEY_INSTAGRAM_LINK = "instagram_link";
		public static final String KEY_FACEBOOK_LINK = "facebook_link";
		public static final String KEY_TWITTER_LINK = "twitter_link";
		public static final String KEY_PROFILE_IMG = "profile_img";
		
		public static final String KEY_FORGET_PWD_CASE = "case";
		public static final String KEY_RE_PASSWORD = "re_password";
		
		public static final String KEY_CASEKEY = "caseKey";
	}
	
	interface USER_DATABASE_COLUMN {
		public static final String USER_COLUMN_ID = "id";
		public static final String USER_COLUMN_EMAIL = "email";
		public static final String USER_COLUMN_PASSWORD = "password";
		public static final String USER_COLUMN_SIGNUP_TYPE = "signup_type";
		public static final String USER_COLUMN_THIRD_PARTY_ID = "third_party_id";
		public static final String USER_COLUMN_NAME = "name";
		public static final String USER_COLUMN_GENDER = "gender";
		public static final String USER_COLUMN_PROFILE_IMG = "profile_img";
		public static final String USER_COLUMN_FACEBOOK_LINK = "facebooklink";
		public static final String USER_COLUMN_TWITTER_LINK = "twitterlink";
		public static final String USER_COLUMN_INSTAGRAM_LINK = "instagramlink";
		public static final String USER_COLUMN_CREATED_AT = "created_at";
		public static final String USER_COLUMN_LAST_MODIFIED = "last_modified";
	}
	
	interface EVENT_DATABASE_COLUMN {
		public static final String EVENT_COLUMN_ID = "id";
		public static final String EVENT_COLUMN_EVENT_NAME = "event_name";
		public static final String EVENT_COLUMN_EVENT_MODE = "event_mode";
		public static final String EVENT_COLUMN_EVENT_ZIP_CODE = "event_zip_code";
		public static final String EVENT_COLUMN_EVENT_TIME = "event_time";
		public static final String EVENT_COLUMN_ALLOW_ADD = "allow_add";
		public static final String EVENT_COLUMN_LATITUDE = "latitude";
		public static final String EVENT_COLUMN_LONGITUDE = "longitude";
		public static final String EVENT_COLUMN_PC_IP = "pc_ip";
		public static final String EVENT_COLUMN_HOST_ID = "host_id";
		public static final String EVENT_COLUMN_HOST_IP = "host_ip";
		public static final String EVENT_COLUMN_HOST_PROFILE_IMG = "host_profile_img";
		public static final String EVENT_COLUMN_HOST_NAME = "host_name";
	}
	
	interface CACHE_NAME{
		public static final String EVENT_CACHE = "event_cache";
		public static final String USER_CACHE = "user_cache";
	}
	
	
	interface EMAIL_SERVICE {
		
		public static final String EMAIL_FROM = "no-reply@jooke.it";
		public static final String EMAIL_SUBJECT = "JOOKE Reset Password";
		public static final String EMAIL_MESSAGE = "Hello:<br>Please click the following link to reset the password<br>";
		public static final String EMAIL_TEAM = "<br>Jooke";
		public static final String EMAIL_RESETPWD_PAGE = "http://jookee-env.elasticbeanstalk.com/reset_password?case=";
		public static final int EMAIL_EXPIRED_TIME_TOL = 20 * 1000;  // 30 mins
		
	}
}
