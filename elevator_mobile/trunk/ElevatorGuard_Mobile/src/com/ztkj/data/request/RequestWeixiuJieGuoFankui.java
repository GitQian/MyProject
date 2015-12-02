package com.ztkj.data.request;

/**
 * 维修结果反馈。
 * 
 * @author liucheng liucheng187@qq.com
 */
public class RequestWeixiuJieGuoFankui extends RequestBaseBody {
	private String userId; // 维修工ID
	private String taskId; // //维修任务ID
	private String malfunctionId; // ,Long // 维修故障信息ID
	private String processingDtm; // 2000-12-11 12:00:05,Date // 处理时间
	private String remark; // 零件坏了，没有备用，需要公司购买新零件", //维修任务结果说明
	private String userName; // "test03", // 维修工人姓名
	private int statusCode;// //4 故障已解决、5 故障未解决

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
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

	public String getProcessingDtm() {
		return processingDtm;
	}

	public void setProcessingDtm(String processingDtm) {
		this.processingDtm = processingDtm;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

}
