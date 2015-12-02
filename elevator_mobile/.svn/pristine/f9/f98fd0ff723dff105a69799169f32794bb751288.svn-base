package com.chinacnit.elevatorguard.mobile.bean;

import java.io.Serializable;

import com.chinacnit.elevatorguard.mobile.bean.WeiBaoItem.WeiBaoItemStatus;

/**
 * 开始维保任务维保项
 * @author ssu
 * @date 2015-6-18 上午10:39:09
 */
public class StartMaintenanceTaskItem implements Serializable{
	/** 用户ID */
	private long uid;
	/** 标签号 */
	private String tagInfo;
	/** 维保项 */
	private String maintainItem;
	/** 状态 -1 未扫描;0 正常; 1 异常 */
	private WeiBaoItemStatus statusCode;
	/** 异常备注 */
	private String comments;
	private String strVal1;

	public long getUid() {
		return uid;
	}

	public void setUid(long uid) {
		this.uid = uid;
	}

	public String getTagInfo() {
		return tagInfo;
	}

	public void setTagInfo(String tagInfo) {
		this.tagInfo = tagInfo;
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

	public String getStrVal1() {
		return strVal1;
	}

	public void setStrVal1(String strVal1) {
		this.strVal1 = strVal1;
	}

	@Override
	public String toString() {
		return "StartMaintenanceTaskItem [uid=" + uid + ", tagInfo=" + tagInfo
				+ ", maintainItem=" + maintainItem + ", statusCode="
				+ statusCode + ", comments=" + comments + ", strVal1="
				+ strVal1 + "]";
	}

}
