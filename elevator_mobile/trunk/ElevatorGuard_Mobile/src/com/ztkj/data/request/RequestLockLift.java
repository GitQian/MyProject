package com.ztkj.data.request;

public class RequestLockLift extends RequestBaseBody{
	private String liftId;
	private String finish;
	public String getLiftId() {
		return liftId;
	}

	public void setLiftId(String liftId) {
		this.liftId = liftId;
	}

	public String getFinish() {
		return finish;
	}

	public void setFinish(String finish) {
		this.finish = finish;
	}
}
