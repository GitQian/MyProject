package com.chinacnit.elevatorguard.mobile.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 开始维保任务详情
 * 
 * @author ssu
 * @date 2015-6-18 上午10:38:01
 */
public class StartMaintenanceTaskDetail implements Serializable{
	/** 计划开始时间 */
	private String planBeginDate;
	/** 计划结束时间 */
	private String planEndDate;
	/** 维保项列表 */
	private List<StartMaintenanceTaskItem> maintainContent;

	public String getPlanBeginDate() {
		return planBeginDate;
	}

	public void setPlanBeginDate(String planBeginDate) {
		this.planBeginDate = planBeginDate;
	}

	public String getPlanEndDate() {
		return planEndDate;
	}

	public void setPlanEndDate(String planEndDate) {
		this.planEndDate = planEndDate;
	}

	public List<StartMaintenanceTaskItem> getMaintainContent() {
		return maintainContent;
	}

	public void setMaintainContent(
			List<StartMaintenanceTaskItem> maintainContent) {
		this.maintainContent = maintainContent;
	}

}
