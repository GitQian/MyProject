package com.ztkj.data.request;

/**
 * 故障报警。 body
 * 
 * @author liucheng liucheng187@qq.com
 */
public class RequestMaintenanceAlert extends RequestBaseBody {
	private String userId; // 维修工ID
	private int page;// // Integer 当前页
	private int rows; // Integer 每页显示的条数
	private String order = "createTime";// String
										// 排序字段，与response中的保持一致；服务器端有默认的排序
	private String oderType = "desc";// // String 升序 asc; 降序 desc
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
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
	public String getOrder() {
		return order;
	}
	public void setOrder(String order) {
		this.order = order;
	}
	public String getOderType() {
		return oderType;
	}
	public void setOderType(String oderType) {
		this.oderType = oderType;
	}

	
}
