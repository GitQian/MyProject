package com.chinacnit.elevatorguard.mobile.bean;

import java.util.List;

/**
 * 设置标签列表
 * 
 * @author ssu
 * @date 2015-6-8 下午8:03:16
 */
public class TagList {
	/**
	 * 记录总数
	 */
	private int total;

	private List<TagListDetail> rows;

	private String footer;
	private List<TagFinish> groups;

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public List<TagListDetail> getRows() {
		return rows;
	}

	public void setRows(List<TagListDetail> rows) {
		this.rows = rows;
	}

	public String getFooter() {
		return footer;
	}

	public void setFooter(String footer) {
		this.footer = footer;
	}

	public List<TagFinish> getGroups() {
		return groups;
	}

	public void setGroups(List<TagFinish> groups) {
		this.groups = groups;
	}
}
