package com.chinacnit.elevatorguard.mobile.http.request.params;

import com.chinacnit.elevatorguard.mobile.http.request.HttpBaseRequestParams;

/**
 * 维保任务开始
 * 
 * @author ssu
 * @date 2015-6-18 上午10:47:10
 */
public class StartMaintenanceTaskParams extends HttpBaseRequestParams {
	/** 计划编号 */
	private long planId;

	public StartMaintenanceTaskParams() {
		super();
	}

	public StartMaintenanceTaskParams(long planId) {
		super();
		this.setPlanId(planId);
	}

	public long getPlanId() {
		return planId;
	}

	public void setPlanId(long planId) {
		this.planId = planId;
	}

}
