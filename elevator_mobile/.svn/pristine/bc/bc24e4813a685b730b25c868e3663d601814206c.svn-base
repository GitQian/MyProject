package com.chinacnit.elevatorguard.mobile.http.task.impl;

import java.lang.reflect.Type;

import android.content.Context;

import com.chinacnit.elevatorguard.core.http.exception.HttpApiException;
import com.chinacnit.elevatorguard.core.http.exception.NetworkUnavailableException;
import com.chinacnit.elevatorguard.core.task.CommonAsyncTask;
import com.chinacnit.elevatorguard.mobile.api.JsonData;
import com.chinacnit.elevatorguard.mobile.bean.TagList;
import com.chinacnit.elevatorguard.mobile.http.task.IResultListener;
import com.google.gson.reflect.TypeToken;

/**
 * 设置标签列表Task
 * @author ssu 
 * @date 2015-6-9 上午9:56:48
 */
public class SettingsTagListTask extends CommonAsyncTask<TagList>{
	
	private long liftId;// Long 电梯编号
	private int page;
	private int rows;
	
	private IResultListener<TagList> listener;
	
	public SettingsTagListTask(Context context, long liftId, int page, int rows, int loadingResid,
			IResultListener<TagList> listener) {
		super(context, loadingResid);
		this.liftId = liftId;
		this.page = page;
		this.rows = rows;
		this.listener = listener;
	}
	
	@Override
	public String onDoInBackgroup() throws NetworkUnavailableException, HttpApiException {
		return mApplicaiton.getApi().getSettingsTagList(liftId, page, rows);
	}

	@Override
	public Type getParseType() {
		return new TypeToken<JsonData<TagList>>() {}.getType();
	}

	@Override
	public void onAfterDoInBackgroup(TagList data) {
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
