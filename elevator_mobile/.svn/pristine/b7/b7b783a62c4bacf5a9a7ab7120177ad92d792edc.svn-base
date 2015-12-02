package com.chinacnit.elevatorguard.mobile.http.request.params;

import com.chinacnit.elevatorguard.mobile.http.request.HttpBaseRequestParams;

/**
 * 维保记录(列表)
 * 
 * @author: pyyang
 * @date 创建时间：2015年6月8日 下午4:56:24
 */
public class WeiBaoRecordParams extends HttpBaseRequestParams {
	
	/** 维保结束时间 年月日 查询此日期的前三天到后三天的时间段 */
	private String condition;
	/** 电梯编号 */
	private long liftId;
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

	public WeiBaoRecordParams() {
		super();
	}

	public WeiBaoRecordParams(long liftId, int page, int rows, String signDate,
			String order, String orderType) {
		super();
		this.liftId = liftId;
		this.page = page;
		this.rows = rows;
		this.signDate = signDate;
		this.order = order;
		this.orderType = orderType;
	}
	
	public WeiBaoRecordParams(String condition, long liftId, int page,
			int rows, String signDate, String order, String oderType) {
		super();
		this.condition = condition;
		this.liftId = liftId;
		this.page = page;
		this.rows = rows;
		this.signDate = signDate;
		this.order = order;
		this.orderType = orderType;
	}

	public long getLiftId() {
		return liftId;
	}

	public void setLiftId(long liftId) {
		this.liftId = liftId;
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
