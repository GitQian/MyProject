package com.chinacnit.elevatorguard.mobile.http.task.impl;

import java.lang.reflect.Type;

import android.content.Context;

import com.chinacnit.elevatorguard.core.http.exception.HttpApiException;
import com.chinacnit.elevatorguard.core.http.exception.NetworkUnavailableException;
import com.chinacnit.elevatorguard.core.task.CommonAsyncTask;
import com.chinacnit.elevatorguard.mobile.api.JsonData;
import com.chinacnit.elevatorguard.mobile.bean.LoginDetail;
import com.chinacnit.elevatorguard.mobile.bean.UserInfo;
import com.chinacnit.elevatorguard.mobile.http.task.IResultListener;
import com.google.gson.reflect.TypeToken;

public class GetUserCompanyTypeTask extends CommonAsyncTask<UserInfo> {
	
	private LoginDetail mLoginDetail;
	private IResultListener<UserInfo> listener;

	public GetUserCompanyTypeTask(Context context, int loadingResid, LoginDetail loginDetail,
			IResultListener<UserInfo> listener) {
		super(context, loadingResid);
		this.mLoginDetail = loginDetail;
		this.listener = listener;
	}

	@Override
	public String onDoInBackgroup() throws NetworkUnavailableException,
			HttpApiException {
		return mApplicaiton.getApi().getUserCompanyType(mLoginDetail);
	}

	@Override
	public Type getParseType() {
		return new TypeToken<JsonData<UserInfo>>() {}.getType();
	}

	@Override
	public void onAfterDoInBackgroup(UserInfo data) {
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
