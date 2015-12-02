package com.chinacnit.elevatorguard.mobile.http.request.params;

import com.chinacnit.elevatorguard.mobile.http.request.HttpBaseRequestParams;

/**
 * 查询维保任务是否开始请求参数
 * @author ssu 
 * @date 2015-6-17 下午7:38:04
 */
public class QueryMaintenanceTaskIsStartParams extends HttpBaseRequestParams {
	/** 计划编号 */
	private long planId;

	public QueryMaintenanceTaskIsStartParams() {
		super();
	}

	public QueryMaintenanceTaskIsStartParams(long planId) {
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
