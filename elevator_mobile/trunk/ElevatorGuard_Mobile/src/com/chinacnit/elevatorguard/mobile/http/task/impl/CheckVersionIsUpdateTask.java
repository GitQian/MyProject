package com.chinacnit.elevatorguard.mobile.http.task.impl;

import java.lang.reflect.Type;

import android.content.Context;

import com.chinacnit.elevatorguard.core.http.exception.HttpApiException;
import com.chinacnit.elevatorguard.core.http.exception.NetworkUnavailableException;
import com.chinacnit.elevatorguard.core.task.CommonAsyncTask;
import com.chinacnit.elevatorguard.mobile.api.JsonData;
import com.chinacnit.elevatorguard.mobile.bean.CheckVersionIsUpdate;
import com.chinacnit.elevatorguard.mobile.http.task.IResultListener;
import com.google.gson.reflect.TypeToken;

/**
 * 检查版本是否有更新
 * @author ssu 
 * @date 2015-6-30 下午2:14:03
 */
public class CheckVersionIsUpdateTask extends CommonAsyncTask<CheckVersionIsUpdate> {

	private int type;

	private IResultListener<CheckVersionIsUpdate> listener;

	public CheckVersionIsUpdateTask(Context context, int loadingResid,
			int type, IResultListener<CheckVersionIsUpdate> listener) {
		super(context, loadingResid);
		this.type = type;
		this.listener = listener;
	}

	@Override
	public String onDoInBackgroup() throws NetworkUnavailableException,
			HttpApiException {
		return mApplicaiton.getApi().checkVersionIsUpdate(type);
	}

	@Override
	public Type getParseType() {
		return new TypeToken<JsonData<CheckVersionIsUpdate>>() {}.getType();
	}

	@Override
	public void onAfterDoInBackgroup(CheckVersionIsUpdate data) {
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
