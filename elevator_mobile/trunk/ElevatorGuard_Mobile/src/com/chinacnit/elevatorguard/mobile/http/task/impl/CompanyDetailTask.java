package com.chinacnit.elevatorguard.mobile.http.task.impl;

import java.lang.reflect.Type;

import android.content.Context;

import com.chinacnit.elevatorguard.core.http.exception.HttpApiException;
import com.chinacnit.elevatorguard.core.http.exception.NetworkUnavailableException;
import com.chinacnit.elevatorguard.core.task.CommonAsyncTask;
import com.chinacnit.elevatorguard.mobile.api.JsonData;
import com.chinacnit.elevatorguard.mobile.bean.CompanyDetail;
import com.chinacnit.elevatorguard.mobile.http.task.IResultListener;
import com.google.gson.reflect.TypeToken;

public class CompanyDetailTask extends CommonAsyncTask<CompanyDetail> {
	private long companyId;
	private int companyType;
	private IResultListener<CompanyDetail> listener;

	public CompanyDetailTask(Context context, long companyId, int companyType, int loadingResid,
			IResultListener<CompanyDetail> listener) {
		super(context, loadingResid);
		this.companyId = companyId;
		this.companyType = companyType;
		this.listener = listener;
	}

	@Override
	public String onDoInBackgroup() throws NetworkUnavailableException,
			HttpApiException {
		return mApplicaiton.getApi().getCompanyDetail(companyId, companyType);
	}

	@Override
	public Type getParseType() {
		return new TypeToken<JsonData<CompanyDetail>>() {}.getType();
	}

	@Override
	public void onAfterDoInBackgroup(CompanyDetail data) {
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
