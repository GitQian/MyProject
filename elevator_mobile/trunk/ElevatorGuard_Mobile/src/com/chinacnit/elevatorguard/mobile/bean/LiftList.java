package com.chinacnit.elevatorguard.mobile.bean;

import java.util.List;

/**
 * 电梯列表
 * 
 * @author: pyyang
 * @date 创建时间：2015年6月5日 上午11:09:36
 */
public class LiftList {
	/**
	 * 记录总数
	 */
	private int total;
	
	private List<LiftListDetail> rows;
	
	private String footer;
	private String groups;
	
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	
	public List<LiftListDetail> getRows() {
		return rows;
	}
	
	public void setRows(List<LiftListDetail> rows) {
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
