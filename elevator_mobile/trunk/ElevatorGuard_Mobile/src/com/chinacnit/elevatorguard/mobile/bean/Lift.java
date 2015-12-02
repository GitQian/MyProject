package com.chinacnit.elevatorguard.mobile.bean;

/**
 * 电梯详情bean
 * 
 * @author: pyyang
 * @date 创建时间：2015年6月11日 下午6:31:02
 */
public class Lift {
	/**
	 * 维保公司
	 */
	private String maintainCompanyName;
	/**
	 * 物业公司
	 */
	private String propertyCompanyName;
	/**
	 * 品牌
	 */
	private String liftBrandName;
	/**
	 * 生产日期
	 */
	private String factoryDate;
	/**
	 * 地址
	 */
	private String locationAddress;

	public String getMaintainCompanyName() {
		return maintainCompanyName;
	}

	public void setMaintainCompanyName(String maintainCompanyName) {
		this.maintainCompanyName = maintainCompanyName;
	}

	public String getPropertyCompanyName() {
		return propertyCompanyName;
	}

	public void setPropertyCompanyName(String propertyCompanyName) {
		this.propertyCompanyName = propertyCompanyName;
	}

	public String getLiftBrandName() {
		return liftBrandName;
	}

	public void setLiftBrandName(String liftBrandName) {
		this.liftBrandName = liftBrandName;
	}

	public String getFactoryDate() {
		return factoryDate;
	}

	public void setFactoryDate(String factoryDate) {
		this.factoryDate = factoryDate;
	}

	public String getLocationAddress() {
		return locationAddress;
	}

	public void setLocationAddress(String locationAddress) {
		this.locationAddress = locationAddress;
	}

}
