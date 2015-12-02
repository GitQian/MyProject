package com.chinacnit.elevatorguard.mobile.bean;

/**
 * 查询维保任务是否开始Bean
 * 
 * @author ssu
 * @date 2015-6-17 下午7:34:32
 */
public class QueryMaintenanceTaskIsStart {

	/** true:当前日期在计划维保时间段内,false当前日期不在计划维保时间段内，不能进行维保 */
	private boolean flag;
	/** true表示已开启，false表示未开启 */
	private boolean startMode;

	public boolean getStartMode() {
		return startMode;
	}

	public void setStartMode(boolean startMode) {
		this.startMode = startMode;
	}

	public boolean getFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}
}
