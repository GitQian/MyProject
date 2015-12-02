package com.ztkj.data.request;

public class RequestStart extends RequestBaseBody{
	private String planId;

	public RequestStart(String planId) {
		super();
		this.planId = planId;
	}

	public String getPlanId() {
		return planId;
	}

	public void setPlanId(String planId) {
		this.planId = planId;
	}
	
}
