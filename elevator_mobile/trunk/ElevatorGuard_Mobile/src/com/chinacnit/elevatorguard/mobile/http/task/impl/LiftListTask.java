package com.chinacnit.elevatorguard.mobile.http.task.impl;

import java.lang.reflect.Type;

import android.content.Context;
import android.text.TextUtils;

import com.chinacnit.elevatorguard.core.http.exception.HttpApiException;
import com.chinacnit.elevatorguard.core.http.exception.NetworkUnavailableException;
import com.chinacnit.elevatorguard.core.task.CommonAsyncTask;
import com.chinacnit.elevatorguard.mobile.api.JsonData;
import com.chinacnit.elevatorguard.mobile.bean.LiftList;
import com.chinacnit.elevatorguard.mobile.http.task.IResultListener;
import com.google.gson.reflect.TypeToken;

/**
 * 获取电梯列表任务
 * @author: pyyang
 * @date 创建时间：2015年6月5日 上午10:54:22
 */
public class LiftListTask extends CommonAsyncTask<LiftList> {
	/**
	 * 电梯位置或者电梯名称 模糊查询
	 */
	private String liftName;
	private String locationAddress;
	private int floor;
	private String liftTrademark;
	private String liftType;
	private int page;
	private int rows;
	
	private IResultListener<LiftList> listener;

	public LiftListTask(Context context, int page, int rows,
			int loadingResid,IResultListener<LiftList> listener) {
		super(context,loadingResid);
		this.page = page;
		this.rows = rows;
		this.listener = listener;
	}
	public LiftListTask(Context context, String liftName, int page, int rows,
			int loadingResid,IResultListener<LiftList> listener) {
		super(context,loadingResid);
		this.liftName = liftName;
		this.page = page;
		this.rows = rows;
		this.listener = listener;
	}
	
	public LiftListTask(Context context, String liftName, String locationAddress, int floor, 
			String liftTrademark, String liftType, int page, int rows,int loadingResid,IResultListener<LiftList> listener) {
		super(context,loadingResid);
		this.liftName = liftName;
		this.locationAddress = locationAddress;
		this.floor = floor;
		this.liftTrademark = liftTrademark;
		this.liftType = liftType;
		this.page = page;
		this.rows = rows;
		this.listener = listener;
	}

	@Override
	public String onDoInBackgroup() throws NetworkUnavailableException, HttpApiException {
		if (!TextUtils.isEmpty(liftName)) {
			return mApplicaiton.getApi().getLiftList(liftName,locationAddress, floor, liftTrademark, liftType, page, rows);
		}
		return mApplicaiton.getApi().getLiftList(page, rows);
	}

	@Override
	public Type getParseType() {
		return new TypeToken<JsonData<LiftList>>() {}.getType();
	}

	@Override
	public void onAfterDoInBackgroup(LiftList data) {
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
