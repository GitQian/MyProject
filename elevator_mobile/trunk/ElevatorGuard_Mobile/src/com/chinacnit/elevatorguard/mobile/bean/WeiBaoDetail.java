package com.chinacnit.elevatorguard.mobile.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 维保详情
 * 
 * @author ssu
 * @date 2015-6-12 下午7:01:12
 */
public class WeiBaoDetail implements Serializable{
	/** 维保开始时间 */
	private String maintainBeginDtm;
	/** 维保结束时间 */
	private String maintainEndDtm;
	/** 所有维保人员名称：对应当前计划中所有进行过维保的维保人员 */
	private List<String> userAlias = new ArrayList<String>();
	/** 电梯编号 */
	private long liftId;
	/** 维保项列表 */
	private List<WeiBaoItem> maintainItems;

	public String getMaintainEndDtm() {
		return maintainEndDtm;
	}

	public void setMaintainEndDtm(String maintainEndDtm) {
		this.maintainEndDtm = maintainEndDtm;
	}

	public List<String> getUserAlias() {
		return userAlias;
	}

	public void setUserAlias(List<String> userAlias) {
		this.userAlias = userAlias;
	}

	public long getLiftId() {
		return liftId;
	}

	public void setLiftId(long liftId) {
		this.liftId = liftId;
	}

	public List<WeiBaoItem> getMaintainItems() {
		return maintainItems;
	}

	public void setMaintainItems(List<WeiBaoItem> maintainItems) {
		this.maintainItems = maintainItems;
	}

	public String getMaintainBeginDtm() {
		return maintainBeginDtm;
	}

	public void setMaintainBeginDtm(String maintainBeginDtm) {
		this.maintainBeginDtm = maintainBeginDtm;
	}

	@Override
	public String toString() {
		return "WeiBaoDetail [maintainBeginDtm=" + maintainBeginDtm
				+ ", maintainEndDtm=" + maintainEndDtm + ", userAlias="
				+ userAlias + ", liftId=" + liftId + ", maintainItems="
				+ maintainItems + "]";
	}

	
	
}
