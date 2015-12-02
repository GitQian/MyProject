package com.ztkj.data.request;

public class RequestMsgNet extends RequestBaseBody{
	private String uid;

	public RequestMsgNet(String uid) {
		super();
		this.uid = uid;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}
	
}
