package com.chinacnit.elevatorguard.mobile.bean;

import com.chinacnit.elevatorguard.mobile.R;
import com.chinacnit.elevatorguard.mobile.jsontoenum.IEnum;

/**
 * 设置标签列表详情
 * 
 * @author ssu
 * @date 2015-6-8 下午8:04:34
 */
public class TagListDetail {

	/** 维保标识字段 */
	private String keyValue;
	/** 维保项对应的维保内容 */
	private String keyCname;
	/** 维保要求 */
	private String strVal1;
	/** 维保周期标识(1 半月;2 季度; 4 半年) */
	private Cycle intVal1;
	/** 排序索引号 */
	private int sortIndex;
	/** ""表示未绑定标签;否则显示卡号信息 */
	private String tagInfo;

	public String getKeyValue() {
		return keyValue;
	}

	public void setKeyValue(String keyValue) {
		this.keyValue = keyValue;
	}

	public String getKeyCname() {
		return keyCname;
	}

	public void setKeyCname(String keyCname) {
		this.keyCname = keyCname;
	}

	public String getStrVal1() {
		return strVal1;
	}

	public void setStrVal1(String strVal1) {
		this.strVal1 = strVal1;
	}

	public Cycle getIntVal1() {
		return intVal1;
	}

	public void setIntVal1(Cycle intVal1) {
		this.intVal1 = intVal1;
	}

	public int getSortIndex() {
		return sortIndex;
	}

	public void setSortIndex(int sortIndex) {
		this.sortIndex = sortIndex;
	}

	public String getTagInfo() {
		return tagInfo;
	}

	public void setTagInfo(String tagInfo) {
		this.tagInfo = tagInfo;
	}
	
	/**
	 * 维保周期标识(1 半月;2 季度; 4 半年)
	 * 
	 * @author ssu
	 * @date 2015-6-8 下午8:08:57
	 */
	public enum Cycle implements IEnum {
		/** 维保签到 */
		SIGN(0),
		/** 半月维保 */
		HALFMONTH(1),
		/** 季度维保 */
		QUARTER(2),
		/** 半年维保 */
		HALFYEAR(4),
		/**年度维保*/
		YEAR(5);
		private int value;

		private Cycle(int value) {
			this.value = value;
		}

		@Override
		public int getValue() {
			return value;
		}

		@Override
		public void setValue(int value) {
			this.value = value;
		}
		
		public static int getValueByKey(Cycle cycle) {
			if (cycle == HALFMONTH) {
				return R.string.halfmonth_maintenance;
			} else if (cycle == QUARTER) {
				return R.string.quarter_maintenance;
			} else if (cycle == HALFYEAR) {
				return R.string.halfyear_maintenance;
			}else if (cycle == YEAR) {
				return R.string.year_maintenance;
			}
			return 0;
		}

	}

	@Override
	public String toString() {
		return "TagListDetail [keyValue=" + keyValue + ", keyCname=" + keyCname
				+ ", strVal1=" + strVal1 + ", intVal1=" + intVal1
				+ ", sortIndex=" + sortIndex + ", tagInfo=" + tagInfo + "]";
	};
	
	

}
