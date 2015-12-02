package com.chinacnit.elevatorguard.mobile.util;

import com.lidroid.xutils.HttpUtils;

public class HttpUtil {
	private static HttpUtil sInstance;
	private HttpUtils mHttpUtils;
	
	public HttpUtil() {
		if (null == mHttpUtils) {
			mHttpUtils = new HttpUtils();
		}
	}

	public static HttpUtil getInstance() {
		if (sInstance == null) {
			sInstance = new HttpUtil();
		}
		return sInstance;
	}
	
	public HttpUtils getHttpUtils() {
		return mHttpUtils;
	}
	
}

