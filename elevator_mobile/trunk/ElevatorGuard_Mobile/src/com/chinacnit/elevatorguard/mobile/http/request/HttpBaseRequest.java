package com.chinacnit.elevatorguard.mobile.http.request;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.util.Log;

import com.chinacnit.elevatorguard.mobile.config.Constants;
import com.chinacnit.elevatorguard.mobile.http.request.params.LoginParams;
import com.chinacnit.elevatorguard.mobile.util.EncodingUtils;
import com.chinacnit.elevatorguard.mobile.util.LogUtils;
import com.chinacnit.elevatorguard.mobile.util.LogUtils.LogTag;

public class HttpBaseRequest {
	
	private static final LogTag LOG_TAG = LogUtils.getLogTag(HttpBaseRequest.class.getSimpleName(), true);

	private HttpBaseRequestParams params1;
	private HttpBaseRequestParams params2;
	private String appid_login;// 程序id
	private String appid;// 程序id
	private String privateKeyLogin;// 私钥
	private String privateKey;// 私钥

	public HttpBaseRequest() {
		super();
		appid_login = Constants.APPID_LOGIN;
		appid = Constants.APPID;
		privateKeyLogin = Constants.PRIVATE_KEY_LOGIN;
		privateKey = Constants.PRIVATE_KEY;
	}

	public HttpBaseRequest(HttpBaseRequestParams params1, HttpBaseRequestParams params2) {
		this();
		this.params1 = params1;
		this.params2 = params2;
	}

	public HttpBaseRequestParams getParams1() {
		return params1;
	}

	public void setParams1(HttpBaseRequestParams params1) {
		this.params1 = params1;
	}

	public HttpBaseRequestParams getParams2() {
		return params2;
	}

	public void setParams2(HttpBaseRequestParams params2) {
		this.params2 = params2;
	}

	public String getAppid() {
		return appid;
	}

	public void setAppid(String appid) {
		this.appid = appid;
	}

	public String getPrivateKey() {
		return privateKey;
	}

	public void setPrivateKey(String privateKey) {
		this.privateKey = privateKey;
	}

	public List<NameValuePair> getHttpRequestParams() {
		StringBuilder sb = new StringBuilder();
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		if (params1 != null) {
			LogUtils.d(LOG_TAG, "getHttpRequestParams", "params1:" + params1.toJson());
			String json1 = EncodingUtils.toBase64String(params1.toJson());
			list.add(new BasicNameValuePair("param1", json1));
			sb.append(json1);
		}
		if (params2 != null) {
			LogUtils.d(LOG_TAG, "getHttpRequestParams", "params2:" + params2.toJson());
			String json2 = EncodingUtils.toBase64String(params2.toJson());
			list.add(new BasicNameValuePair("param2", json2));
			sb.append(json2);
		}
		if (params2 instanceof LoginParams) {
			list.add(new BasicNameValuePair("appId", appid_login));
			sb.append(appid_login);
			list.add(new BasicNameValuePair("key", EncodingUtils.hmacSHA1Encrypt(sb.toString(), privateKeyLogin)));
		} else {
			list.add(new BasicNameValuePair("appId", appid));
			sb.append(appid);
			
			Log.e("===================>>>", "提交=========>"+sb.toString());
			
			
			list.add(new BasicNameValuePair("key", EncodingUtils.hmacSHA1Encrypt(sb.toString(), privateKey)));
		}
		return list;
	}
}
