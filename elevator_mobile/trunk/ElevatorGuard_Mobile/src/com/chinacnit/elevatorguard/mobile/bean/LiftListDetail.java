package com.chinacnit.elevatorguard.mobile.bean;

/**
 * 电梯列表详情
 * 
 * @author: pyyang
 * @date 创建时间：2015年6月5日 上午10:19:25
 */
public class LiftListDetail {

	public LiftListDetail() {
		super();
	}

	/**
	 * 电梯编号
	 */
	private long liftId;

	/**
	 * 电梯名称
	 */
	private String liftName;

	/**
	 * 维保时间 如果此电梯未开始进行维保，则此字段显示为"尚未维保"
	 */
	private String maxMaintainEndDtm;

	/**
	 * 本地地址
	 */
	private String locationAddress;

	public String getMaxMaintainEndDtm() {
		return maxMaintainEndDtm;
	}

	public void setMaxMaintainEndDtm(String maxMaintainEndDtm) {
		this.maxMaintainEndDtm = maxMaintainEndDtm;
	}

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

}
