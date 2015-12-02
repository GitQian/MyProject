package com.chinacnit.elevatorguard.mobile.http.task.impl;

import java.lang.reflect.Type;

import android.content.Context;

import com.chinacnit.elevatorguard.core.http.exception.HttpApiException;
import com.chinacnit.elevatorguard.core.http.exception.NetworkUnavailableException;
import com.chinacnit.elevatorguard.core.task.CommonAsyncTask;
import com.chinacnit.elevatorguard.mobile.api.JsonData;
import com.chinacnit.elevatorguard.mobile.bean.BindTagResult;
import com.chinacnit.elevatorguard.mobile.http.task.IResultListener;
import com.google.gson.reflect.TypeToken;

/**
 * 绑定标签Task
 * @author ssu 
 * @date 2015-6-11 上午10:40:35
 */
public class BindTagTask extends CommonAsyncTask<BindTagResult>{
	
	/** 电梯id */
	private long liftId;
	/** 维保标识字段 */
	private String keyValue;
	/** 对应tag卡号*/
	private String tagInfo;
	/** 是否覆盖记录  "true" 表示覆盖 */
	private String cover;
	private IResultListener<BindTagResult> listener;
	
	public BindTagTask(Context context, int loadingResid, long liftId, String keyValue, String tagInfo, String cover,
			IResultListener<BindTagResult> listener) {
		super(context, loadingResid);
		this.liftId = liftId;
		this.keyValue = keyValue;
		this.tagInfo = tagInfo;
		this.cover = cover;
		this.listener = listener;
	}
	
	@Override
	public String onDoInBackgroup() throws NetworkUnavailableException, HttpApiException {
		return mApplicaiton.getApi().bindTag(liftId, keyValue, tagInfo, cover);
	}

	@Override
	public Type getParseType() {
		return new TypeToken<JsonData<BindTagResult>>() {}.getType();
	}

	@Override
	public void onAfterDoInBackgroup(BindTagResult data) {
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
