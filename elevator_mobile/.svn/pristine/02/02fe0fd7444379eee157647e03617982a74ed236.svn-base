package com.chinacnit.elevatorguard.mobile.http.request;

import com.chinacnit.elevatorguard.mobile.adapter.MyEnumAdapterFactory;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public abstract class HttpBaseRequestParams {

	public String toJson() {
		GsonBuilder gb = new GsonBuilder(); 
		gb.registerTypeAdapterFactory(new MyEnumAdapterFactory());
		Gson gson = gb.create();
		return gson.toJson(this);
	}
}
