package com.chinacnit.elevatorguard.mobile.bean;

import java.io.Serializable;

import com.chinacnit.elevatorguard.mobile.bean.TagListDetail.Cycle;

/**
 * 维保记录
 * 
 * @author: pyyang
 * @date 创建时间：2015年5月28日 上午10:21:19
 */
public class WeiBaoRecordBean implements Serializable {
	
	/** 维保时间 */
	private String maintainEndDtm;
	
	/** 维保周期 */
	private Cycle maintainCycle;
	
	/** 维保人员名称：对应当前计划中的最后一个刷标签的维保人员 */
	private String userAlias;
	
	/** 计划Id */
	private Long planId;
	
	
	private String planTime;
	private String maintenanceTime;
	private String type;
	private String status; //异常 、正常。
	private String people;

	public Long getPlanId() {
		return planId;
	}

	public void setPlanId(Long planId) {
		this.planId = planId;
	}

	public String getUserAlias() {
		return userAlias;
	}

	public void setUserAlias(String userAlias) {
		this.userAlias = userAlias;
	}

	public String getMaintainEndDtm() {
		return maintainEndDtm;
	}

	public void setMaintainEndDtm(String maintainEndDtm) {
		this.maintainEndDtm = maintainEndDtm;
	}

	public Cycle getMaintainCycle() {
		return maintainCycle;
	}

	public void setMaintainCycle(Cycle maintainCycle) {
		this.maintainCycle = maintainCycle;
	}

	public String getPlanTime() {
		return planTime;
	}

	public void setPlanTime(String planTime) {
		this.planTime = planTime;
	}

	public String getMaintenanceTime() {
		return maintenanceTime;
	}

	public void setMaintenanceTime(String maintenanceTime) {
		this.maintenanceTime = maintenanceTime;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getPeople() {
		return people;
	}

	public void setPeople(String people) {
		this.people = people;
	}

	@Override
	public String toString() {
		return "WeiBaoRecordBean [maintainEndDtm=" + maintainEndDtm
				+ ", maintainCycle=" + maintainCycle + ", userAlias="
				+ userAlias + ", planId=" + planId + ", planTime=" + planTime
				+ ", maintenanceTime=" + maintenanceTime + ", type=" + type
				+ ", status=" + status + ", people=" + people + "]";
	}
	
	

}
