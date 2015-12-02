package com.chinacnit.elevatorguard.mobile.http.request.params;

import com.chinacnit.elevatorguard.mobile.http.request.HttpBaseRequestParams;

/**
 * 检查版本是否需要更新请求参数
 * 
 * @author ssu
 * @date 2015-6-30 下午2:19:57
 */
public class CheckVersionIsUpdateParams extends HttpBaseRequestParams {
	/** 计划编号 */
	private long type;

	public CheckVersionIsUpdateParams() {
		super();
	}

	public CheckVersionIsUpdateParams(long type) {
		super();
		this.type = type;
	}

	public long getType() {
		return type;
	}

	public void setType(long type) {
		this.type = type;
	}

}
