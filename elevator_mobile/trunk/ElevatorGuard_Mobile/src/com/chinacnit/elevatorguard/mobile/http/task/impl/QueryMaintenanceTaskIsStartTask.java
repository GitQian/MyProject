package com.chinacnit.elevatorguard.mobile.http.task.impl;

import java.lang.reflect.Type;

import android.content.Context;

import com.chinacnit.elevatorguard.core.http.exception.HttpApiException;
import com.chinacnit.elevatorguard.core.http.exception.NetworkUnavailableException;
import com.chinacnit.elevatorguard.core.task.CommonAsyncTask;
import com.chinacnit.elevatorguard.mobile.api.JsonData;
import com.chinacnit.elevatorguard.mobile.bean.QueryMaintenanceTaskIsStart;
import com.chinacnit.elevatorguard.mobile.http.task.IResultListener;
import com.google.gson.reflect.TypeToken;

/**
 * 查询维保任务是否开始Task
 * 
 * @author ssu
 * @date 2015-6-17 下午7:33:35
 */
public class QueryMaintenanceTaskIsStartTask extends CommonAsyncTask<QueryMaintenanceTaskIsStart> {

	private long planId;

	private IResultListener<QueryMaintenanceTaskIsStart> listener;

	public QueryMaintenanceTaskIsStartTask(Context context, int loadingResid,
			long planId, IResultListener<QueryMaintenanceTaskIsStart> listener) {
		super(context, loadingResid);
		this.planId = planId;
		this.listener = listener;
	}

	@Override
	public String onDoInBackgroup() throws NetworkUnavailableException,
			HttpApiException {
		return mApplicaiton.getApi().queryMaintenanceTaskIsStart(planId);
	}

	@Override
	public Type getParseType() {
		return new TypeToken<JsonData<QueryMaintenanceTaskIsStart>>() {}.getType();
	}

	@Override
	public void onAfterDoInBackgroup(QueryMaintenanceTaskIsStart data) {
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
