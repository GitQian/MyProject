package com.chinacnit.elevatorguard.mobile.bean;

import java.io.Serializable;

import com.chinacnit.elevatorguard.mobile.jsontoenum.IEnum;

/**
 * 维保项
 * 
 * @author ssu
 * @date 2015-6-12 下午7:02:53
 */
public class WeiBaoItem implements Serializable{
	/**
	 * 状态 -1没有状态;0 正常; 1 异常
	 */
	public enum WeiBaoItemStatus implements IEnum {
		/** 尚未维保 */
		NOSTATUS(-1),
		/** 正常 */
		NORMAL(0),
		/** 异常 */
		ABNORMAL(1);
		
		private int value;

		private WeiBaoItemStatus(int value) {
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

	/** 维保内容 */
	private String maintainItem;
	/** 状态 0 正常; 1 异常 */
	private WeiBaoItemStatus statusCode;
	/** 异常备注 */
	private String comments;
	/**
	 * 这个维保项的维保人。
	 */
	private String maintainUser;
	/**
	 * 最后更新时间。
	 */
	private String LastUpdateDtm;
	
	public String getMaintainUser() {
		return maintainUser;
	}

	public void setMaintainUser(String maintainUser) {
		this.maintainUser = maintainUser;
	}

	public String getLastUpdateDtm() {
		return LastUpdateDtm;
	}

	public void setLastUpdateDtm(String lastUpdateDtm) {
		LastUpdateDtm = lastUpdateDtm;
	}

	public String getMaintainItem() {
		return maintainItem;
	}

	public void setMaintainItem(String maintainItem) {
		this.maintainItem = maintainItem;
	}

	public WeiBaoItemStatus getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(WeiBaoItemStatus statusCode) {
		this.statusCode = statusCode;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

}
