package com.chinacnit.elevatorguard.mobile.http.task.impl;

import java.lang.reflect.Type;

import android.content.Context;

import com.chinacnit.elevatorguard.core.http.exception.HttpApiException;
import com.chinacnit.elevatorguard.core.http.exception.NetworkUnavailableException;
import com.chinacnit.elevatorguard.core.task.CommonAsyncTask;
import com.chinacnit.elevatorguard.mobile.api.JsonData;
import com.chinacnit.elevatorguard.mobile.bean.StartMaintenanceTaskDetail;
import com.chinacnit.elevatorguard.mobile.http.task.IResultListener;
import com.google.gson.reflect.TypeToken;

/**
 * 维保任务开始
 * @author ssu 
 * @date 2015-6-18 上午10:45:28
 */
public class StartMaintenanceTask extends CommonAsyncTask<StartMaintenanceTaskDetail> {

	private long planId;

	private IResultListener<StartMaintenanceTaskDetail> listener;

	public StartMaintenanceTask(Context context, int loadingResid,
			long planId, IResultListener<StartMaintenanceTaskDetail> listener) {
		super(context, loadingResid);
		this.planId = planId;
		this.listener = listener;
	}

	@Override
	public String onDoInBackgroup() throws NetworkUnavailableException,
			HttpApiException {
		return mApplicaiton.getApi().startMaintenanceTask(planId);
	}

	@Override
	public Type getParseType() {
		return new TypeToken<JsonData<StartMaintenanceTaskDetail>>() {}.getType();
	}

	@Override
	public void onAfterDoInBackgroup(StartMaintenanceTaskDetail data) {
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
