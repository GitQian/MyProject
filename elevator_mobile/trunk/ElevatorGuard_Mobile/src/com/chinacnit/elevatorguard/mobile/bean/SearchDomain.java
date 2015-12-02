package com.chinacnit.elevatorguard.mobile.bean;
/**
 * 搜索bean
 * 
 * @author: pyyang
 * @date 创建时间：2015年7月2日 上午11:23:14
 */
public class SearchDomain {
	private String value;
	private String name;
	
	public SearchDomain() {
	}

	public SearchDomain(String value, String name) {
		this.value = value;
		this.name = name;
	}

	
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return name;
	}
	
	
	
}
