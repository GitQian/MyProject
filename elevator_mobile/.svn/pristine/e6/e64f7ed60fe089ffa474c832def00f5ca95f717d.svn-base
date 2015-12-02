package com.chinacnit.elevatorguard.mobile.http.task.impl;

import java.lang.reflect.Type;

import android.content.Context;

import com.chinacnit.elevatorguard.core.http.exception.HttpApiException;
import com.chinacnit.elevatorguard.core.http.exception.NetworkUnavailableException;
import com.chinacnit.elevatorguard.core.task.CommonAsyncTask;
import com.chinacnit.elevatorguard.mobile.api.JsonData;
import com.chinacnit.elevatorguard.mobile.http.task.IResultListener;
import com.google.gson.reflect.TypeToken;
import com.ztkj.data.response.MaintenanceBean;

/**
 * 查询 归属与某个 planid 下的 所有维保bean集合。
 * 
 * @author joychine
 * @date 2015年9月2日 16:23:50
 */
public class QueryMaintenanceBeanTask extends CommonAsyncTask<MaintenanceBean> {

	private long planId;

	private IResultListener<MaintenanceBean> listener;

	public QueryMaintenanceBeanTask(Context context, int loadingResid,
			long planId, IResultListener<MaintenanceBean> listener) {
		super(context, loadingResid);
		this.planId = planId;
		this.listener = listener;
	}

	@Override
	public String onDoInBackgroup() throws NetworkUnavailableException,
			HttpApiException {
		return mApplicaiton.getApi().queryMaintenanceBean(planId);
	}

	@Override
	public Type getParseType() {
		return new TypeToken<JsonData<MaintenanceBean>>() {}.getType();
	}

	@Override
	public void onAfterDoInBackgroup(MaintenanceBean data) {
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
