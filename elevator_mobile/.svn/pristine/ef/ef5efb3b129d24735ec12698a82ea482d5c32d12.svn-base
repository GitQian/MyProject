package com.chinacnit.elevatorguard.core.http;

import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.entity.mime.MultipartEntity;

import android.content.Context;

import com.chinacnit.elevatorguard.core.http.exception.HttpApiException;
import com.chinacnit.elevatorguard.core.http.exception.NetworkUnavailableException;
import com.chinacnit.elevatorguard.core.util.DeviceUtil;
import com.chinacnit.elevatorguard.mobile.util.Helper;

public class HttpApiAdatper extends BaseHttpApi {

	private Context context;

	public HttpApiAdatper(Context context) {
		super();
		this.context = context;
	}

	/**
	 * 
	 * @Title: getWithString
	 * @Description: 获取json字符串
	 * @param s
	 * @param list
	 * @return
	 * @throws HtppApiException
	 * @throws NetworkUnavailableException
	 * @return String
	 * @throws
	 */
	public String getWithString(String url, List<NameValuePair> parmList)
			throws HttpApiException, NetworkUnavailableException {
		if (!DeviceUtil.isNetAvailable(context))
			throw new NetworkUnavailableException("Network Unavailable.");
		String ret;
		try {
			ret = get(url, parmList);
		} catch (Exception e) {
			throw new HttpApiException("getWithObject occurs exception", e);
		}
		return ret;
	}

	public String getWithString(String url, List<NameValuePair> parmList,
			int timeout) throws HttpApiException, NetworkUnavailableException {
		if (!DeviceUtil.isNetAvailable(context))
			throw new NetworkUnavailableException("Network Unavailable.");
		String ret;
		try {
			ret = get(url, parmList, timeout);
		} catch (Exception exception) {
			throw new HttpApiException("getWithObject occurs exception",
					exception);
		}
		return ret;
	}

	/**
	 * 
	 * @Title: getWithString
	 * @Description: 获取json字符串
	 * @param url
	 * @param parmList
	 * @return
	 * @throws HtppApiException
	 * @throws NetworkUnavailableException
	 * @return String
	 * @throws
	 */
	public String postWithString(String url, List<NameValuePair> parmList)
			throws HttpApiException, NetworkUnavailableException {
		if (!DeviceUtil.isNetAvailable(context)) {
			throw new NetworkUnavailableException("Network Unavailable.");
		}
		String ret;
		try {
			ret = post(url, parmList);
		} catch (Exception e) {
			throw new HttpApiException("getWithObject occurs exception", e);
		}
		return ret;
	}

	public String postWithString(String url, MultipartEntity entity)
			throws HttpApiException, NetworkUnavailableException {
		if (!DeviceUtil.isNetAvailable(context) || !Helper.hasInternetConnection()) {
			throw new NetworkUnavailableException("Network Unavailable.");
		}
		String ret;
		try {
			ret = post(url, entity);
		} catch (Exception e) {
			throw new HttpApiException("getWithObject occurs exception", e);
		}
		return ret;
	}
}
