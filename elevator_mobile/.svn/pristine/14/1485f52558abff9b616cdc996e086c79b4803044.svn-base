package com.chinacnit.elevatorguard.mobile.http.request.params;

import com.chinacnit.elevatorguard.mobile.http.request.HttpBaseRequestParams;

/**
 * 绑定标签请求参数
 * 
 * @author ssu
 * @date 2015-6-11 上午10:48:07
 */
public class BindTagParams extends HttpBaseRequestParams {

	/** 电梯id */
	private long liftId;
	/** 维保标识字段 */
	private String keyValue;
	/** 对应tag卡号 */
	private String tagInfo;
	/** 是否覆盖记录  "true" 表示覆盖 */
	private String cover;

	public BindTagParams() {
		super();
	}

	public BindTagParams(long liftId, String keyValue, String tagInfo, String cover) {
		super();
		this.liftId = liftId;
		this.keyValue = keyValue;
		this.tagInfo = tagInfo;
		this.cover = cover;
	}

	public long getLiftId() {
		return liftId;
	}

	public void setLiftId(long liftId) {
		this.liftId = liftId;
	}

	public String getKeyValue() {
		return keyValue;
	}

	public void setKeyValue(String keyValue) {
		this.keyValue = keyValue;
	}

	public String getTagInfo() {
		return tagInfo;
	}

	public void setTagInfo(String tagInfo) {
		this.tagInfo = tagInfo;
	}

	public String isCover() {
		return cover;
	}

	public void setCover(String cover) {
		this.cover = cover;
	}

}
