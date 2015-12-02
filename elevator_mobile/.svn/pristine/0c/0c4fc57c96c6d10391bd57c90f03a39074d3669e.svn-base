package com.chinacnit.elevatorguard.mobile.http.task.impl;

import java.lang.reflect.Type;

import android.content.Context;

import com.chinacnit.elevatorguard.core.http.exception.HttpApiException;
import com.chinacnit.elevatorguard.core.http.exception.NetworkUnavailableException;
import com.chinacnit.elevatorguard.core.task.CommonAsyncTask;
import com.chinacnit.elevatorguard.mobile.api.JsonData;
import com.chinacnit.elevatorguard.mobile.http.task.IResultListener;
import com.google.gson.reflect.TypeToken;

/**
 * 提交维保计划Task
 * @author ssu 
 * @date 2015-6-19 下午2:02:38
 */
public class SubmitMaintenancePlanTask extends CommonAsyncTask<Object> {

	/** 计划ID */
	private long planId;

	private IResultListener<Object> listener;

	public SubmitMaintenancePlanTask(Context context, int loadingResid, long planId, IResultListener<Object> listener) {
		super(context, loadingResid);
		this.planId = planId;
		this.listener = listener;
	}

	@Override
	public String onDoInBackgroup() throws NetworkUnavailableException,
			HttpApiException {
		return mApplicaiton.getApi().submitMaintenancePlan(planId);
	}

	@Override
	public Type getParseType() {
		return new TypeToken<JsonData<Object>>() {}.getType();
	}

	@Override
	public void onAfterDoInBackgroup(Object data) {
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
