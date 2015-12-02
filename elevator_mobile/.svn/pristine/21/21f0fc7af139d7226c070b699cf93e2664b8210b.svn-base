package com.ztkj.data.request;

public class RequestMaintenanceListBody extends RequestBaseBody{
	/** 计划开始时间 年月日 */
	private String condition;
	/** 当前页 */
	private int page = 1;
	/** 当前页行数 */
	private int rows = 20;
	/** 标识字段 向上滑动加载数据，当前最底端的截止日期，page还是为1 */
	private String signDate;
	/** 排序字段，与response中的保持一致；服务器端有默认的排序 */
	private String order = "planEndDate";
	/** 升序 asc; 降序 desc */
	private String orderType ="desc";
	
	public String getCondition() {
		return condition;
	}
	public void setCondition(String condition) {
		this.condition = condition;
	}
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	public int getRows() {
		return rows;
	}
	public void setRows(int rows) {
		this.rows = rows;
	}
	public String getSignDate() {
		return signDate;
	}
	public void setSignDate(String signDate) {
		this.signDate = signDate;
	}
	public String getOrder() {
		return order;
	}
	public void setOrder(String order) {
		this.order = order;
	}
	public String getOrderType() {
		return orderType;
	}
	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}
	
	
	
}
