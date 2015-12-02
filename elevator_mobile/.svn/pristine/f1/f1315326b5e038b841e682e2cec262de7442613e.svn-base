package com.chinacnit.elevatorguard.mobile.http.task.impl;

import java.lang.reflect.Type;

import android.content.Context;

import com.chinacnit.elevatorguard.core.http.exception.HttpApiException;
import com.chinacnit.elevatorguard.core.http.exception.NetworkUnavailableException;
import com.chinacnit.elevatorguard.core.task.CommonAsyncTask;
import com.chinacnit.elevatorguard.mobile.api.JsonData;
import com.chinacnit.elevatorguard.mobile.bean.LoginDetail;
import com.chinacnit.elevatorguard.mobile.http.task.IResultListener;
import com.google.gson.reflect.TypeToken;

public class UserLoginTask extends CommonAsyncTask<LoginDetail> {

	private String userName;

	private String userPwd;
	
	private String mac;

	private IResultListener<LoginDetail> listener;

	public UserLoginTask(Context context, int loadingResid, String userName,
			String userPwd, String mac, IResultListener<LoginDetail> listener) {
		super(context, loadingResid);
		this.userName = userName;
		this.userPwd = userPwd;
		this.mac = mac;
		this.listener = listener;
	}

	@Override
	public String onDoInBackgroup() throws NetworkUnavailableException, HttpApiException {
		return mApplicaiton.getApi().userLogin(userName, userPwd, mac);
	}

	@Override
	public Type getParseType() {
		return new TypeToken<JsonData<LoginDetail>>() {}.getType();
	}

	@Override
	public void onAfterDoInBackgroup(LoginDetail data) {
		if (listener != null) {
			listener.onSuccess(data);
		}
	}
	
	@Override
	protected void onError(String errorMsg) {
		if (listener != null) {
			listener.onError(errorMsg);
		}
	}
}
