package com.chinacnit.elevatorguard.mobile.http.task.impl;

import java.lang.reflect.Type;

import android.content.Context;

import com.chinacnit.elevatorguard.core.http.exception.HttpApiException;
import com.chinacnit.elevatorguard.core.http.exception.NetworkUnavailableException;
import com.chinacnit.elevatorguard.core.task.CommonAsyncTask;
import com.chinacnit.elevatorguard.mobile.api.JsonData;
import com.chinacnit.elevatorguard.mobile.bean.WeiBaoItem.WeiBaoItemStatus;
import com.chinacnit.elevatorguard.mobile.http.task.IResultListener;
import com.google.gson.reflect.TypeToken;

/**
 * 提交维保项Task
 * @author ssu 
 * @date 2015-6-18 下午8:03:45
 */
public class SubmitMaintenanceItemTask extends CommonAsyncTask<Object> {

	/** 计划ID */
	private long planId;
	/** 标签ID */
	private String tagId;
	/** 手机号 */
	private String mobile;
	/** 备注信息 */
	private String comments;
	/** 状态(0 正常 ; 1 异常) */
	private WeiBaoItemStatus statusCode;

	private IResultListener<Object> listener;

	public SubmitMaintenanceItemTask(Context context, int loadingResid, long planId, String tagId, String mobile,
			String comments, WeiBaoItemStatus statusCode, IResultListener<Object> listener) {
		super(context, loadingResid);
		this.planId = planId;
		this.tagId = tagId;
		this.mobile = mobile;
		this.comments = comments;
		this.statusCode = statusCode;
		this.listener = listener;
	}

	@Override
	public String onDoInBackgroup() throws NetworkUnavailableException,
			HttpApiException {
		return mApplicaiton.getApi().submitMaintenanceItem(planId, tagId, mobile, comments, statusCode);
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
