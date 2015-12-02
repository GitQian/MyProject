package com.chinacnit.elevatorguard.mobile.http.task.impl;

import java.lang.reflect.Type;

import android.content.Context;
import android.text.TextUtils;

import com.chinacnit.elevatorguard.core.http.exception.HttpApiException;
import com.chinacnit.elevatorguard.core.http.exception.NetworkUnavailableException;
import com.chinacnit.elevatorguard.core.task.CommonAsyncTask;
import com.chinacnit.elevatorguard.mobile.api.JsonData;
import com.chinacnit.elevatorguard.mobile.bean.WeiBaoRecordList;
import com.chinacnit.elevatorguard.mobile.http.task.IResultListener;
import com.google.gson.reflect.TypeToken;

/**
 * @author: pyyang
 * @date 创建时间：2015年6月8日 下午5:34:27
 */
public class WeiBaoRecordTask extends CommonAsyncTask<WeiBaoRecordList> {

	/** 维保结束时间 年月日 查询此日期的前三天到后三天的时间段 */
	private String condition;
	private long liftId;
	private int page;
	private int rows;
	/** 标识字段 向上滑动加载数据，当前最底端的截止日期，page还是为1 */
	private String signDate;
	/** 排序字段，与response中的保持一致；服务器端有默认的排序 */
	private String order;
	/** 升序 asc; 降序 desc */
	private String oderType;

	private IResultListener<WeiBaoRecordList> listener;

	public WeiBaoRecordTask(Context context, int page, int rows, long liftId, String signDate,
			String order, String oderType, int loadingResid, IResultListener<WeiBaoRecordList> listener) {
		super(context, loadingResid);
		this.page = page;
		this.rows = rows;
		this.liftId = liftId;
		this.signDate = signDate;
		this.order = order;
		this.oderType = oderType;
		this.listener = listener;
	}
	
	public WeiBaoRecordTask(Context context, String condition, int page, int rows, long liftId, String signDate,
			String order, String oderType, int loadingResid, IResultListener<WeiBaoRecordList> listener) {
		super(context, loadingResid);
		this.condition = condition;
		this.page = page;
		this.rows = rows;
		this.liftId = liftId;
		this.signDate = signDate;
		this.order = order;
		this.oderType = oderType;
		this.listener = listener;
	}

	@Override
	public String onDoInBackgroup() throws NetworkUnavailableException, HttpApiException {
		if (!TextUtils.isEmpty(condition)) {
			return mApplicaiton.getApi().getWeiBaoRecord(condition, liftId, page, rows, signDate, order, oderType);
		} 
		return mApplicaiton.getApi().getWeiBaoRecord(liftId, page, rows, signDate, order, oderType);
	}

	@Override
	public Type getParseType() {
		return new TypeToken<JsonData<WeiBaoRecordList>>() {}.getType();
	}

	@Override
	public void onAfterDoInBackgroup(WeiBaoRecordList data) {
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
