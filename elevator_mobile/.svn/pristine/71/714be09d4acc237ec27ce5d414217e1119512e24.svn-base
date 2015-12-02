package com.chinacnit.elevatorguard.mobile.bean;

import java.io.Serializable;

import android.text.TextUtils;

/**
 * 用户信息
 * 
 * @author ssu
 * @date 2015-5-20 下午2:00:00
 */
public class LoginDetail implements Serializable {

	private static final long serialVersionUID = -4899745454354612880L;

	/** 用户名 */
	private String userName;

	/** 登录UID */
	private long uid;

	/** 登录SID */
	private long sid;

	/** 公司ID */
	private long companyID;

	public LoginDetail() {
	}

	public LoginDetail(String userName) {
		super();
		this.userName = userName;
	}

	/**
	 * 判断用户是否登录
	 * 
	 * @param
	 * @author: ssu
	 * @date: 2015-5-20 下午4:06:31
	 */
	public boolean isLogin() {
		if (!TextUtils.isEmpty(userName) && uid != 0 && sid != 0 && companyID != 0) {
			return true;
		}
		return false;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public long getCompanyID() {
		return companyID;
	}

	public void setCompanyID(long companyID) {
		this.companyID = companyID;
	}

	public long getUid() {
		return uid;
	}

	public void setUid(long uid) {
		this.uid = uid;
	}

	public long getSid() {
		return sid;
	}

	public void setSid(long sid) {
		this.sid = sid;
	}

}
