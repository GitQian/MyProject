package com.chinacnit.elevatorguard.mobile.http.task.impl;

import java.lang.reflect.Type;

import android.content.Context;
import android.text.TextUtils;

import com.chinacnit.elevatorguard.core.http.exception.HttpApiException;
import com.chinacnit.elevatorguard.core.http.exception.NetworkUnavailableException;
import com.chinacnit.elevatorguard.core.task.CommonAsyncTask;
import com.chinacnit.elevatorguard.mobile.api.JsonData;
import com.chinacnit.elevatorguard.mobile.bean.MaintenanceTaskList;
import com.chinacnit.elevatorguard.mobile.http.task.IResultListener;
import com.google.gson.reflect.TypeToken;

/**
 * 获得维保工维保任务列表Task
 * @author ssu 
 * @date 2015-6-11 下午8:34:36
 */
public class MaintenanceTaskListTask extends CommonAsyncTask<MaintenanceTaskList> {

	/** 计划开始时间 年月日 */
	private String condition;
	/** 当前页 */
	private int page;
	/** 当前页行数 */
	private int rows;
	/** 标识字段 向上滑动加载数据，当前最底端的截止日期，page还是为1 */
	private String signDate;
	/** 排序字段，与response中的保持一致；服务器端有默认的排序 */
	private String order;
	/** 升序 asc; 降序 desc */
	private String orderType;

	private IResultListener<MaintenanceTaskList> listener;

	public MaintenanceTaskListTask(Context context, int page, int rows, String signDate, 
			String order, String orderType, int loadingResid,
			IResultListener<MaintenanceTaskList> listener) {
		super(context, loadingResid);
		this.page = page;
		this.rows = rows;
		this.signDate = signDate;
		this.order = order;
		this.orderType = orderType;
		this.listener = listener;
	}
	
	public MaintenanceTaskListTask(Context context, String condition, int page, int rows, String signDate, 
			String order, String orderType, int loadingResid,
			IResultListener<MaintenanceTaskList> listener) {
		super(context, loadingResid);
		this.condition = condition;
		this.page = page;
		this.rows = rows;
		this.signDate = signDate;
		this.order = order;
		this.orderType = orderType;
		this.listener = listener;
	}

	@Override
	public String onDoInBackgroup() throws NetworkUnavailableException, HttpApiException {
		if (!TextUtils.isEmpty(condition)) {
			return mApplicaiton.getApi().getMaintenanceTaskList(condition, page, rows, signDate, order, orderType);
		}
		return mApplicaiton.getApi().getMaintenanceTaskList(page, rows, signDate, order, orderType);
	}

	@Override
	public Type getParseType() {
		return new TypeToken<JsonData<MaintenanceTaskList>>() {}.getType();
	}

	@Override
	public void onAfterDoInBackgroup(MaintenanceTaskList data) {
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
