package com.chinacnit.elevatorguard.mobile.http.request.params;

import com.chinacnit.elevatorguard.mobile.http.request.HttpBaseRequestParams;

/**
 * 查询维保任务 详情item bean  ，归属于某个 planid下的。
 * @author ssu 
 * @date 2015-6-17 下午7:38:04
 */
public class QueryMaintenanceBeanParams extends HttpBaseRequestParams {
	/** 计划编号 */
	private long planId;

	public QueryMaintenanceBeanParams() {
		super();
	}

	public QueryMaintenanceBeanParams(long planId) {
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
