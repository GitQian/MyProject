package com.ztkj.data.request;
/**
 * 请求维保记录
 * @author liucheng  liucheng187@qq.com
 */
public class RequestWeiBaoRecord extends RequestBaseBody {
	/** 维保结束时间 年月日 查询此日期的前三天到后三天的时间段 */
	private String condition;
	private long liftId;
	private int page=1;
	private int rows=Integer.MAX_VALUE;
	/** 标识字段 向上滑动加载数据，当前最底端的截止日期，page还是为1 */
	private String signDate;
	/** 排序字段，与response中的保持一致；服务器端有默认的排序 */
	private String order="maintainEndDtm";
	/** 升序 asc; 降序 desc */
	private String oderType="desc";
	
	public String getCondition() {
		return condition;
	}
	public void setCondition(String condition) {
		this.condition = condition;
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
	public String getOderType() {
		return oderType;
	}
	public void setOderType(String oderType) {
		this.oderType = oderType;
	}
	@Override
	public String toString() {
		return "RequestWeiBaoRecord [condition=" + condition + ", liftId="
				+ liftId + ", page=" + page + ", rows=" + rows + ", signDate="
				+ signDate + ", order=" + order + ", oderType=" + oderType
				+ "]";
	}
	
	
}
