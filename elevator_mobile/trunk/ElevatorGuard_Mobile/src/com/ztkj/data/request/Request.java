package com.ztkj.data.request;


import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;

import android.util.Log;

import com.chinacnit.elevatorguard.mobile.bean.LoginDetail;
import com.chinacnit.elevatorguard.mobile.config.ConfigSettings;
import com.chinacnit.elevatorguard.mobile.config.Constants;
import com.chinacnit.elevatorguard.mobile.http.request.params.VerificationParams;
import com.chinacnit.elevatorguard.mobile.util.EncodingUtils;
import com.ztkj.tool.Config;
import com.ztkj.tool.Tool;

/**
 * 请求封装类
 *
 */
public final class Request {
	public enum PARAMS_TYPE {
		JSON,FILE
	}		
	public enum METHOD_TYPE{
		POST,GET
	}
	
	private PARAMS_TYPE mParamsType=PARAMS_TYPE.JSON;
	private METHOD_TYPE mMethodType=METHOD_TYPE.GET;
	private RequestProgressListener mReqProlistener;
	private RequestBaseBody mReqData;
	private int mCode;
	private int mMaxRepeat=3;
	private boolean mShouldRepeat;
	private boolean mCancel;
	private String mMethodName;
	private Object mTag;
	private String mUrl=Config.URL_COMMON;
	public Request(){
	}
	public Request(RequestBaseBody reqData,String methodName){
		this.mReqData=reqData;
		this.mMethodName=methodName;
	}
	public Request(RequestBaseBody reqData,String methodName,int code){
		this.mCode=code;
		this.mReqData=reqData;
		this.mMethodName=methodName;
	}
	/**
	 * @param reqData
	 * @param methodName
	 * @param code  用来区分具体是哪个请求
	 * @param shouldRepeat
	 */
	public Request(RequestBaseBody reqData,String methodName,int code,boolean shouldRepeat){
		this.mShouldRepeat=shouldRepeat;
		this.mCode=code;
		this.mReqData=reqData;
		this.mMethodName=methodName;
	}

	public String getFormatGet(){
		List<NameValuePair> listParams = new ArrayList<NameValuePair>();
		StringBuffer sb=new StringBuffer();
		
		//添加验证信息
		LoginDetail loginDetail = ConfigSettings.getLoginDetail();
    	VerificationParams params1 = null;
    	if (null != loginDetail) {
    		params1 = new VerificationParams(loginDetail.getUid(), loginDetail.getSid());
    	}
    	if (params1 != null) {
			String json1 = EncodingUtils.toBase64String(params1.toJson());
			listParams.add(new BasicNameValuePair("param1", json1));
			sb.append(json1);
		}
    	
    	String json2=EncodingUtils.toBase64String(mReqData.toJsonString());
    	
    	Log.e("---------", "json2===》"+mReqData.toJsonString());
    	
    	listParams.add(new BasicNameValuePair("param2", json2));
    	sb.append(json2);
    	
    	//添加请求令牌
    	listParams.add(new BasicNameValuePair("appId", Constants.APPID));
    	sb.append(Constants.APPID);
		
    	listParams.add(new BasicNameValuePair("key", EncodingUtils.hmacSHA1Encrypt(sb.toString(), Constants.PRIVATE_KEY)));
    	
		return URLEncodedUtils.format(listParams, "UTF-8");
		
	}
	
	public PARAMS_TYPE getParamsType() {
		return mParamsType;
	}

	public void setParamsType(PARAMS_TYPE paramsType) {
		this.mParamsType = paramsType;
	}


	public METHOD_TYPE getMethodType() {
		return mMethodType;
	}
	public void setMethodType(METHOD_TYPE methodType) {
		this.mMethodType = methodType;
	}
	public RequestProgressListener getReqProlistener() {
		return mReqProlistener;
	}

	public void setReqProlistener(RequestProgressListener reqProlistener) {
		this.mReqProlistener = reqProlistener;
	}

	public RequestBaseBody getReqData() {
		return mReqData;
	}

	public void setReqData(RequestBaseBody reqData) {
		this.mReqData = reqData;
	}

	public int getCode() {
		return mCode;
	}

	public void setCode(int code) {
		this.mCode = code;
	}

	public boolean isShouldRepeat() {
		return mShouldRepeat;
	}

	public void setShouldRepeat(boolean shouldRepeat) {
		this.mShouldRepeat = shouldRepeat;
	}

	public boolean isCancel() {
		return mCancel;
	}

	public void setCancel(boolean cancel) {
		this.mCancel = cancel;
	}
	public String getMethodName() {
		return mMethodName;
	}
	public void setMethodName(String methodName) {
		this.mMethodName = methodName;
	}
	public Object getTag() {
		return mTag;
	}
	public void setTag(Object tag) {
		this.mTag = tag;
	}
	public String getUrl() {
		return mUrl;
	}
	public void setUrl(String url) {
		this.mUrl = url;
	}
	public int getMaxRepeat() {
		return mMaxRepeat;
	}
	public void setMaxRepeat(int mMaxRepeat) {
		this.mMaxRepeat = mMaxRepeat;
	}
}
