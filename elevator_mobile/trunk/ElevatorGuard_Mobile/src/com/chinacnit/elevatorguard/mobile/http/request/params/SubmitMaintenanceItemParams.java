package com.chinacnit.elevatorguard.mobile.http.request.params;

import com.chinacnit.elevatorguard.mobile.bean.WeiBaoItem.WeiBaoItemStatus;
import com.chinacnit.elevatorguard.mobile.http.request.HttpBaseRequestParams;

/**
 * 提交维保项请求参数
 * 
 * @author ssu
 * @date 2015-6-18 下午8:11:50
 */
public class SubmitMaintenanceItemParams extends HttpBaseRequestParams {
	/** 计划ID */
	private long planId;
	/** 标签ID */
	private String tagId;
	/** 手机号 */
	private String mobile;
	/** 备注信息 */
	private String comments;
	/** 状态(0 正常 ; 1 异常) */
	private WeiBaoItemStatus statusCode;

	public SubmitMaintenanceItemParams() {
		super();
	}

	public SubmitMaintenanceItemParams(long planId, String tagId,String mobile,
			String comments, WeiBaoItemStatus statusCode) {
		super();
		this.planId = planId;
		this.tagId = tagId;
		this.mobile = mobile;
		this.comments = comments;
		this.statusCode = statusCode;
	}

	public long getPlanId() {
		return planId;
	}

	public void setPlanId(long planId) {
		this.planId = planId;
	}

	public String getTagId() {
		return tagId;
	}

	public void setTagId(String tagId) {
		this.tagId = tagId;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public WeiBaoItemStatus getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(WeiBaoItemStatus statusCode) {
		this.statusCode = statusCode;
	}

}
