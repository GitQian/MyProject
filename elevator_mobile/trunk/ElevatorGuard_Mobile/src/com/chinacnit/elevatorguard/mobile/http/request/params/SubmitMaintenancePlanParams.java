package com.chinacnit.elevatorguard.mobile.http.request.params;

import com.chinacnit.elevatorguard.mobile.http.request.HttpBaseRequestParams;

/**
 * 提交维保计划请求参数
 * 
 * @author ssu
 * @date 2015-6-19 下午2:04:40
 */
public class SubmitMaintenancePlanParams extends HttpBaseRequestParams {
	/** 计划ID */
	private long planId;

	public SubmitMaintenancePlanParams() {
		super();
	}

	public SubmitMaintenancePlanParams(long planId) {
		super();
		this.planId = planId;
	}

	public long getPlanId() {
		return planId;
	}

	public void setPlanId(long planId) {
		this.planId = planId;
	}

}
