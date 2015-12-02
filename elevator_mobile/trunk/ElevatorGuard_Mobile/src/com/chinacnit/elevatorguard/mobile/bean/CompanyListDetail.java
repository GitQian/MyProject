package com.chinacnit.elevatorguard.mobile.bean;

import java.io.Serializable;

import com.chinacnit.elevatorguard.mobile.jsontoenum.IEnum;

/**
 * 公司列表详情
 * 
 * @author ssu
 * @date 2015-6-4 下午7:41:49
 */
public class CompanyListDetail implements Serializable {

	/**
	 * @Fields serialVersionUID : TODO 用一句话描述这个变量表示什么
	 */
	private static final long serialVersionUID = 2102036715937890141L;

	/**
	 * 公司类型
	 * 
	 * @author ssu
	 * @date 2015-6-4 下午7:45:27
	 */
	public enum CompanyType implements IEnum {

		/** 质监局 */
		QUALITY(0),
		/** 维保公司 */
		MAINTENANCECOMPANY(1),
		/** 物业公司 */
		PROPERTYCOMPANY(2),
		/** 监察局 */
		INSPECTORATE(3);

		private int value;

		private CompanyType(int value) {
			this.value = value;
		}

		@Override
		public int getValue() {
			return value;
		}

		@Override
		public void setValue(int value) {
			this.value = value;
		}
	}

	/**
	 * 公司编号
	 */
	private long companyId;

	/**
	 * 公司类型 0 质监局;1维保公司;2物业公司;3监察局
	 */
	private CompanyType companyType;

	/**
	 * 公司名称
	 */
	private String companyName;

	public long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(long companyId) {
		this.companyId = companyId;
	}

	public CompanyType getCompanyType() {
		return companyType;
	}

	public void setCompanyType(CompanyType companyType) {
		this.companyType = companyType;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

}
