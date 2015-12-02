package com.chinacnit.elevatorguard.mobile.bean;

import java.util.List;

/**
 * 维保任务列表
 * 
 * @author ssu
 * @date 2015-6-11 下午8:22:39
 */
public class MaintenanceTaskList {
	/**
	 * 记录总数
	 */
	private int total;

	private List<MaintenanceTaskListDetail> rows;

	private String footer;
	private String groups;

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public List<MaintenanceTaskListDetail> getRows() {
		return rows;
	}

	public void setRows(List<MaintenanceTaskListDetail> rows) {
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
