package com.chinacnit.elevatorguard.mobile.http.request.params;

import com.chinacnit.elevatorguard.mobile.http.request.HttpBaseRequestParams;

/**
 * 维保工任务列表参数
 * @author ssu 
 * @date 2015-6-11 下午8:36:57
 */
public class MaintenanceTaskListParams extends HttpBaseRequestParams {
	
	/** 计划开始时间 年月日 */
	private String condition;

	/** 当前页 */
	private int page;

	/** 当前页行数 */
	private int rows;
	
	/** 标识字段 向上滑动加载数据，当前最底端的截止日期，page还是为1 */
	private String signDate;
	
	/** 排序字段，与response中的保持一致；服务器端有默认的排序 */
	private String order;
	
	/** 升序 asc; 降序 desc */
	private String orderType;

	public MaintenanceTaskListParams() {
		super();
	}

	public MaintenanceTaskListParams(int page, int rows, String signDate,
			String order, String orderType) {
		super();
		this.page = page;
		this.rows = rows;
		this.signDate = signDate;
		this.order = order;
		this.orderType = orderType;
	}

	public MaintenanceTaskListParams(String condition, int page, int rows,
			String signDate, String order, String orderType) {
		super();
		this.condition = condition;
		this.page = page;
		this.rows = rows;
		this.signDate = signDate;
		this.order = order;
		this.orderType = orderType;
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

	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}
}
