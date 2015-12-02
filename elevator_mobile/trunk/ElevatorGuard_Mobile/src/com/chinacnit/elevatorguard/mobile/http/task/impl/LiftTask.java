package com.chinacnit.elevatorguard.mobile.http.task.impl;

import java.lang.reflect.Type;

import android.content.Context;

import com.chinacnit.elevatorguard.core.http.exception.HttpApiException;
import com.chinacnit.elevatorguard.core.http.exception.NetworkUnavailableException;
import com.chinacnit.elevatorguard.core.task.CommonAsyncTask;
import com.chinacnit.elevatorguard.mobile.api.JsonData;
import com.chinacnit.elevatorguard.mobile.bean.Lift;
import com.chinacnit.elevatorguard.mobile.bean.LiftList;
import com.chinacnit.elevatorguard.mobile.http.task.IResultListener;
import com.google.gson.reflect.TypeToken;

/**
 * 获取电梯详情Task
 * 
 * @author: pyyang
 * @date 创建时间：2015年6月11日 下午6:25:59
 */
public class LiftTask extends CommonAsyncTask<Lift> {
	
	private long liftId;
	
	private IResultListener<Lift> listener;
	
	public LiftTask(Context context, long liftId,
			int loadingResid,IResultListener<Lift> listener) {
		super(context,loadingResid);
		this.liftId = liftId;
		this.listener = listener;
		// TODO Auto-generated constructor stub
	}

	@Override
	public String onDoInBackgroup() throws NetworkUnavailableException,
			HttpApiException {
		return mApplicaiton.getApi().getLiftDetails(liftId);
	}

	@Override
	public Type getParseType() {
		return new TypeToken<JsonData<Lift>>() {}.getType();
	}

	@Override
	public void onAfterDoInBackgroup(Lift data) {
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
