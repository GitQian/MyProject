package com.chinacnit.elevatorguard.mobile.bean;

import java.util.List;

/**
 * 维保记录列表（时间轴列表）
 * 
 * @author: pyyang
 * @date 创建时间：2015年6月8日 下午5:46:58
 */
public class WeiBaoRecordList {
	/**
	 * 记录总数
	 */
	private int total;
	
	private List<WeiBaoRecordBean> rows;
	
	private String footer;
	
	private String groups;
	
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	
	public List<WeiBaoRecordBean> getRows() {
		return rows;
	}
	
	public void setRows(List<WeiBaoRecordBean> rows) {
		this.rows = rows;
	}
	
	public String getFooter() {
		return footer;
	}
	
	public void setFooter(String footer) {
		this.footer = footer;
	}
	
	public String getGroups() {
		return groups;
	}
	
	public void setGroups(String groups) {
		this.groups = groups;
	}
	
}
