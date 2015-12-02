package com.chinacnit.elevatorguard.mobile.http.task.impl;

import java.lang.reflect.Type;

import android.content.Context;

import com.chinacnit.elevatorguard.core.http.exception.HttpApiException;
import com.chinacnit.elevatorguard.core.http.exception.NetworkUnavailableException;
import com.chinacnit.elevatorguard.core.task.CommonAsyncTask;
import com.chinacnit.elevatorguard.mobile.api.JsonData;
import com.chinacnit.elevatorguard.mobile.bean.CompanyList;
import com.chinacnit.elevatorguard.mobile.bean.CompanyListDetail.CompanyType;
import com.chinacnit.elevatorguard.mobile.http.task.IResultListener;
import com.google.gson.reflect.TypeToken;

/**
 * 获取公司列表任务
 * @author ssu 
 * @date 2015-6-2 下午5:21:26
 */
public class CompanyListTask extends CommonAsyncTask<CompanyList> {

	private int page;
	private int rows;
	private CompanyType companyType;

	private IResultListener<CompanyList> listener;

	public CompanyListTask(Context context, CompanyType companyType, int page, int rows, int loadingResid,
			IResultListener<CompanyList> listener) {
		super(context, loadingResid);
		this.companyType = companyType;
		this.page = page;
		this.rows = rows;
		this.listener = listener;
	}

	@Override
	public String onDoInBackgroup() throws NetworkUnavailableException, HttpApiException {
		return mApplicaiton.getApi().getCompanyList(companyType, page, rows);
	}

	@Override
	public Type getParseType() {
		return new TypeToken<JsonData<CompanyList>>() {}.getType();
	}

	@Override
	public void onAfterDoInBackgroup(CompanyList data) {
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