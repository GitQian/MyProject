package com.chinacnit.elevatorguard.mobile.bean;

import com.chinacnit.elevatorguard.mobile.R;
import com.chinacnit.elevatorguard.mobile.bean.TagListDetail.Cycle;
import com.chinacnit.elevatorguard.mobile.jsontoenum.IEnum;

/**
 * 维保任务列表详情
 * 
 * @author ssu
 * @date 2015-6-2 下午8:47:17
 */
public class MaintenanceTaskListDetail {
	
//	private boolean isVisible = true; //配合界面是否显示。 该item。
//	public boolean isVisible() {
//		return isVisible;
//	}
//	public void setVisible(boolean isVisible) {
//		this.isVisible = isVisible;
//	}

	/**
	 * 电梯维保状态
	 * 
	 * @author ssu
	 * @date 2015-6-3 下午3:02:54
	 */
	public static enum MaintenanceStatus implements IEnum {
		/** 尚未维保 */
		NOMAINTENANCE(0),
		/** 维保完成 */
		MAINTENANCEED(1),
		/** 维保延期 */
		MAINTENANCEEXTENSION(2);

		private int value;

		private MaintenanceStatus(int value) {
			this.value = value;
		}

		@Override
		public int getValue() {
			// TODO Auto-generated method stub
			return value;
		}

		@Override
		public void setValue(int value) {
			// TODO Auto-generated method stub
			this.value = value;
		}
		public static int getValueByKey(MaintenanceStatus ms){
			if(ms == NOMAINTENANCE){
				return R.string.not_maintenance;
			} else if (ms == MAINTENANCEED){
				return R.string.maintenance_finish;
			} else if (ms == MAINTENANCEEXTENSION){
				return R.string.maintenance_delay;
			}
			return -1;
		}
	}

	/**
	 * 截止日期
	 */
	private String planBeginDate;

	/**
	 * 电梯id
	 */
	private long liftId;
	
	/**电梯民*/
	private String liftName;

	/**
	 * 电梯地址
	 */
	private String locationAddress;

	/**
	 * 0 尚未维保;1 维保完成; 2 维保延期，   但这个是表示签到对象时，这个为0表示已经签到的，否则需要签到。
	 */
	private MaintenanceStatus statusCode;
	
	/**
	 * 维保周期标识(1 半月;2 季度; 4 半年)
	 */
	private Cycle maintainCycle;

	/**
	 * 维保计划id
	 */
	private long planId;

	public String getLiftName() {
		return liftName;
	}

	public void setLiftName(String liftName) {
		this.liftName = liftName;
	}

	public long getLiftId() {
		return liftId;
	}

	public void setLiftId(long liftId) {
		this.liftId = liftId;
	}

	public String getLocationAddress() {
		return locationAddress;
	}

	public void setLocationAddress(String locationAddress) {
		this.locationAddress = locationAddress;
	}

	public MaintenanceStatus getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(MaintenanceStatus statusCode) {
		this.statusCode = statusCode;
	}

	public long getPlanId() {
		return planId;
	}

	public void setPlanId(long planId) {
		this.planId = planId;
	}

	public Cycle getMaintainCycle() {
		return maintainCycle;
	}

	public void setMaintainCycle(Cycle maintainCycle) {
		this.maintainCycle = maintainCycle;
	}

	public String getPlanBeginDate() {
		return planBeginDate;
	}

	public void setPlanBeginDate(String planBeginDate) {
		this.planBeginDate = planBeginDate;
	}

	@Override
	public String toString() {
		return "MaintenanceTaskListDetail [planBeginDate=" + planBeginDate
				+ ", liftId=" + liftId + ", liftName=" + liftName
				+ ", locationAddress=" + locationAddress + ", statusCode="
				+ statusCode + ", maintainCycle=" + maintainCycle + ", planId="
				+ planId + "]";
	}

	
}
