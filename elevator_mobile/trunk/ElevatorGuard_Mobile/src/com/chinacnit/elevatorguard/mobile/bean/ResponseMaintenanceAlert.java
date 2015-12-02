package com.chinacnit.elevatorguard.mobile.bean;
/**
 *维修故障 。。。
 * @author liucheng  liucheng187@qq.com
 */
public class ResponseMaintenanceAlert {
	private String liftId; //// 电梯ID
	private String liftName; //// 电梯名称
	private String locationAddress; //// String 电梯地址
	private String taskId; //// 维修任务ID
	private String malfunctionId; //Long // 维修故障信息ID
	private String malfunctionType; //,String // 故障类型
	private String createTime; //2015-09-08 14:50:40维修任务下发时间
	
	public String getLiftId() {
		return liftId;
	}
	public void setLiftId(String liftId) {
		this.liftId = liftId;
	}
	public String getLiftName() {
		return liftName;
	}
	public void setLiftName(String liftName) {
		this.liftName = liftName;
	}
	public String getLocationAddress() {
		return locationAddress;
	}
	public void setLocationAddress(String locationAddress) {
		this.locationAddress = locationAddress;
	}
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public String getMalfunctionId() {
		return malfunctionId;
	}
	public void setMalfunctionId(String malfunctionId) {
		this.malfunctionId = malfunctionId;
	}
	public String getMalfunctionType() {
		return malfunctionType;
	}
	public void setMalfunctionType(String malfunctionType) {
		this.malfunctionType = malfunctionType;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	@Override
	public String toString() {
		return "ResponseMaintenanceAlert [liftId=" + liftId + ", liftName="
				+ liftName + ", locationAddress=" + locationAddress
				+ ", taskId=" + taskId + ", malfunctionId=" + malfunctionId
				+ ", malfunctionType=" + malfunctionType + ", createTime="
				+ createTime + "]";
	}
	
	
}
