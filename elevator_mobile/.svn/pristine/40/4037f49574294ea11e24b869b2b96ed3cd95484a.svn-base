package com.chinacnit.elevatorguard.mobile.api;

import java.io.Serializable;

public class JsonData<T> implements Serializable {
	private static final long serialVersionUID = 8509728630747219577L;

	private boolean success;
	private String msg;
	private T object;

	public JsonData() {
		super();
	}

	public JsonData(boolean success, String msg, T object) {
		super();
		this.success = success;
		this.msg = msg;
		this.object = object;
	}

	public boolean getSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getMessage() {
		return msg;
	}

	public void setMessage(String msg) {
		this.msg = msg;
	}

	public T getResult() {
		return object;
	}

	public void setResult(T object) {
		this.object = object;
	}

}
