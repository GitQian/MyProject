package com.chinacnit.elevatorguard.mobile.http.request.params;

import com.chinacnit.elevatorguard.mobile.http.request.HttpBaseRequestParams;

/**
 * 设置标签列表参数
 * @author ssu 
 * @date 2015-6-8 下午8:32:24
 */
public class SettingTagListParams extends HttpBaseRequestParams {
	
	/**
	 * 电梯Id
	 */
	private long liftId;
	
	/**
	 * 当前页
	 */
	private int page;
	
	/**
	 * 当前页行数
	 */
	private int rows;
	
	/**
	 * 电梯标识
	 */
	private String keyCode;
	
	public SettingTagListParams(){
		super();
	}
	
	public SettingTagListParams(String keyCode, long liftId, int page, int rows){
		super();
		this.keyCode = keyCode;
		this.liftId = liftId;
		this.page = page;
		this.rows = rows;
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

	public String getKeyCode() {
		return keyCode;
	}

	public void setKeyCode(String keyCode) {
		this.keyCode = keyCode;
	}

	public long getLiftId() {
		return liftId;
	}

	public void setLiftId(long liftId) {
		this.liftId = liftId;
	}
	
}
