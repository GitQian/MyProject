package com.chinacnit.elevatorguard.mobile.http.task.impl;

import java.lang.reflect.Type;

import android.content.Context;

import com.chinacnit.elevatorguard.core.http.exception.HttpApiException;
import com.chinacnit.elevatorguard.core.http.exception.NetworkUnavailableException;
import com.chinacnit.elevatorguard.core.task.CommonAsyncTask;
import com.chinacnit.elevatorguard.mobile.api.JsonData;
import com.chinacnit.elevatorguard.mobile.bean.WeiBaoDetail;
import com.chinacnit.elevatorguard.mobile.http.task.IResultListener;
import com.google.gson.reflect.TypeToken;

/**
 * 维保详情Task
 * 
 * @author ssu
 * @date 2015-6-15 上午11:02:43
 */
public class WeiBaoDetailTask extends CommonAsyncTask<WeiBaoDetail> {

	private long planId;
	private IResultListener<WeiBaoDetail> listener;

	public WeiBaoDetailTask(Context context, int loadingResid, long planId,
			IResultListener<WeiBaoDetail> listener) {
		super(context, loadingResid);
		this.planId = planId;
		this.listener = listener;
	}

	@Override
	public String onDoInBackgroup() throws NetworkUnavailableException,
			HttpApiException {
		return mApplicaiton.getApi().getWeiBaoDetail(planId);
	}

	@Override
	public Type getParseType() {
		return new TypeToken<JsonData<WeiBaoDetail>>() {
		}.getType();
	}

	@Override
	public void onAfterDoInBackgroup(WeiBaoDetail data) {
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
