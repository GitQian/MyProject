package com.chinacnit.elevatorguard.mobile.bean;

import java.util.List;

/**
 * 公司列表
 * 
 * @author ssu
 * @date 2015-6-4 下午7:40:45
 */
public class CompanyList {
	/**
	 * 记录总数
	 */
	private int total;

	private List<CompanyListDetail> rows;

	private String footer;
	private String groups;

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public List<CompanyListDetail> getRows() {
		return rows;
	}

	public void setRows(List<CompanyListDetail> rows) {
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
