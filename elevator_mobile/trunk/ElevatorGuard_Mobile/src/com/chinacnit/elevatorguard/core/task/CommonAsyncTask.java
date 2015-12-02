package com.chinacnit.elevatorguard.core.task;

import java.lang.reflect.Type;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.widget.Toast;

import com.chinacnit.elevatorguard.core.http.exception.HttpApiException;
import com.chinacnit.elevatorguard.core.http.exception.HttpParseException;
import com.chinacnit.elevatorguard.core.http.exception.NetworkUnavailableException;
import com.chinacnit.elevatorguard.core.http.exception.ServerDataExcepton;
import com.chinacnit.elevatorguard.core.view.LoadingDialog;
import com.chinacnit.elevatorguard.mobile.R;
import com.chinacnit.elevatorguard.mobile.adapter.MyEnumAdapterFactory;
import com.chinacnit.elevatorguard.mobile.api.JsonData;
import com.chinacnit.elevatorguard.mobile.application.ElevatorGuardApplication;
import com.chinacnit.elevatorguard.mobile.http.task.IResult;
import com.chinacnit.elevatorguard.mobile.http.task.Result;
import com.chinacnit.elevatorguard.mobile.util.CommonToast;
import com.chinacnit.elevatorguard.mobile.util.LogUtils;
import com.chinacnit.elevatorguard.mobile.util.LogUtils.LogTag;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public abstract class CommonAsyncTask<T> extends AsyncTask<Void, Void, IResult<T>> {
	private static final LogTag LOG_TAG = LogUtils.getLogTag(CommonAsyncTask.class.getSimpleName(), true);
	protected Context context;
	protected LoadingDialog loadingDialog;
	protected int loadingResid;
	protected ElevatorGuardApplication mApplicaiton;

	protected boolean okSuccess = true;

	public CommonAsyncTask(Context context) {
		this(context, 0);
	}

	public CommonAsyncTask(Context context, int loadingResid) {
		this(context, loadingResid, true);
	}

	public CommonAsyncTask(Context context, int loadingResid, boolean okSuccess) {
		this.context = context;
		this.loadingResid = loadingResid;
		mApplicaiton = (ElevatorGuardApplication) ((Activity) context).getApplication();
		this.okSuccess = okSuccess;
	}

	public int getLoadingResid() {
		return loadingResid;
	}

	public void setLoadingResid(int loadingResid) {
		this.loadingResid = loadingResid;
	}

	public LoadingDialog getLoadingDialog() {
		if (this.loadingDialog == null) {
			this.loadingDialog = new LoadingDialog(this.context, R.style.elevator_progess_dialog_1, this.loadingResid);
			this.loadingDialog.setCancelable(true);
			this.loadingDialog
					.setOnCancelListener(new DialogInterface.OnCancelListener() {
						public void onCancel(
								DialogInterface paramDialogInterface) {
							onDialogCancel();
						}
					});
		}
		return this.loadingDialog;
	}

	public void setLoadingDialog(LoadingDialog paramLoadingDialog) {
		this.loadingDialog = paramLoadingDialog;
	}

	protected void onDialogCancel() {

	}

	protected void onPreExecute() {
		if ((this.loadingResid > 0) && (getLoadingDialog() != null)) {
			getLoadingDialog().show();
		}
	}

	@Override
	protected Result<T> doInBackground(Void... params) {
		Result<T> result = new Result<T>();
		String jsonRet = null;
		T data = null;
		try {
			jsonRet = onDoInBackgroup();
			data = onParse(jsonRet);
		} catch (NetworkUnavailableException e) {
			LogUtils.e(LOG_TAG, "doInBackground", e);
			result.setError(context.getResources().getString(R.string.network_unavailable));
		} catch (HttpApiException e) {
			LogUtils.e(LOG_TAG, "doInBackground", e);
			result.setError(context.getResources().getString(R.string.http_request_error));
		} catch (ServerDataExcepton e) {
			LogUtils.e(LOG_TAG, "doInBackground", e);
			result.setError(e.getMessage());
		} catch (HttpParseException e) {
			LogUtils.e(LOG_TAG, "doInBackground", e);
			result.setError(context.getResources().getString(R.string.http_parse_error));
		}
		result.setRet(data);
		return result;
	}

	@Override
	protected void onPostExecute(IResult<T> result) {
		super.onPostExecute(result);
		if (this.loadingResid > 0)
			getLoadingDialog().dismiss();

		if (!(context instanceof Activity) || !((Activity) context).isFinishing()) {
			if (result.isOK()) {
				onAfterDoInBackgroup(result.getReturn());
			} else {
				onError(result.getErrorMsg());
			}
		}
	}
 
	protected void onError(String errorMsg) {
		CommonToast.showToast(context, errorMsg);
	}

	protected T onParse(String jsonData) throws ServerDataExcepton, HttpParseException {
		if (jsonData == null)
			return null;
		GsonBuilder gb = new GsonBuilder(); 
		gb.registerTypeAdapterFactory(new MyEnumAdapterFactory());
		Gson gson = gb.create();
		JsonData<T> data = null;
		try {
			data = gson.fromJson(jsonData, getParseType());
			LogUtils.e(LOG_TAG, "onParse", "okSuccess:" + data.getSuccess());
			if (data.getSuccess() != okSuccess) { // 返回结果错误
				throw new ServerDataExcepton(data.getMessage());
			} else {
				return data.getResult();
			}
		} catch (Exception e) {
			if (e instanceof ServerDataExcepton) { 
				throw (ServerDataExcepton) e;
			} else {
				throw new HttpParseException("parse data error", e);
			}
		}
	}

	public abstract String onDoInBackgroup() throws NetworkUnavailableException, HttpApiException;

	public abstract Type getParseType();

	public abstract void onAfterDoInBackgroup(T data);
}
