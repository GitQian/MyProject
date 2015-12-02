package com.chinacnit.elevatorguard.mobile.bean;

import java.io.Serializable;

import com.chinacnit.elevatorguard.mobile.jsontoenum.IEnum;

/**
 * 用户信息
 * @author ssu 
 * @date 2015-6-8 下午12:36:00
 */
public class UserInfo implements Serializable{

	/**
	 * @Fields serialVersionUID : TODO 用一句话描述这个变量表示什么
	 */
	private static final long serialVersionUID = 2516151588885353861L;
	
	/**
	 * 用户角色类型
	 * 
	 * @author ssu
	 * @date 2015-5-27 下午2:23:51
	 */
	public enum RoleType implements IEnum {
		/** 没有角色 */
		NOROLE(-1),
		/** 质监局 */
		QUALITY(0),
		/** 维保公司 */
		MAINTENANCECOMPANY(1),
		/** 物业公司 */
		PROPERTYCOMPANY(2),
		/** 监察局 */
		INSPECTORATE(3);

		private int value;

		private RoleType(int value) {
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
	 * 用户类型, 当companyType=1，userType返回 1 管理员 2 维保工 3 管理员/维保工，0 表示非维保公司人员
	 * @author ssu 
	 * @date 2015-6-8 下午1:54:09
	 */
	public enum UserType implements IEnum {
		/** 非维保公司人员 */
		NOMAINTENANCE(0),
		/** 管理员 */
		ADMINISTRATOR(1),
		/** 维保工 */
		MAINTENANCEWORKERS(2),
		/** 管理员和维保工 */
		ADMINANDWORKERS(3);
		
		private int value;
		
		private UserType(int value) {
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
		
	};
	
	/** 用户角色  0 质监局;1维保公司;2物业公司;3监察局 ; -1 不存在的companyId*/
	private RoleType companyType;
	
	/** 生日 */
	private String birthday;
	
	/** M 男 G 女 */
	private String userGender;
	
	/** 手机号 */
	private String userMobile;
	
	/** 真实姓名 */
	private String realName;
	
	/** 别名（登录名） */
	private String userAlias;
	
	/** 用户类型 */
	private UserType userType;

	public RoleType getCompanyType() {
		return companyType;
	}

	public void setCompanyType(RoleType companyType) {
		this.companyType = companyType;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getUserGender() {
		return userGender;
	}

	public void setUserGender(String userGender) {
		this.userGender = userGender;
	}

	public String getUserMobile() {
		return userMobile;
	}

	public void setUserMobile(String userMobile) {
		this.userMobile = userMobile;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getUserAlias() {
		return userAlias;
	}

	public void setUserAlias(String userAlias) {
		this.userAlias = userAlias;
	}

	public UserType getUserType() {
		return userType;
	}

	public void setUserType(UserType userType) {
		this.userType = userType;
	}

	@Override
	public String toString() {
		return "UserInfo [companyType=" + companyType + ", birthday="
				+ birthday + ", userGender=" + userGender + ", userMobile="
				+ userMobile + ", realName=" + realName + ", userAlias="
				+ userAlias + ", userType=" + userType + ", getCompanyType()="
				+ getCompanyType() + ", getBirthday()=" + getBirthday()
				+ ", getUserGender()=" + getUserGender() + ", getUserMobile()="
				+ getUserMobile() + ", getRealName()=" + getRealName()
				+ ", getUserAlias()=" + getUserAlias() + ", getUserType()="
				+ getUserType() + ", getClass()=" + getClass()
				+ ", hashCode()=" + hashCode() + ", toString()="
				+ super.toString() + "]";
	}
	
	
	
}
