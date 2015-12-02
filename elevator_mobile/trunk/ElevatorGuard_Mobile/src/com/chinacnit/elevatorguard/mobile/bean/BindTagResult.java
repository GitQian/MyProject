package com.chinacnit.elevatorguard.mobile.bean;

import com.chinacnit.elevatorguard.mobile.jsontoenum.IEnum;

/**
 * 绑定标签结果类
 * 
 * @author ssu
 * @date 2015-6-16 下午2:18:49
 */
public class BindTagResult {
	/**
	 * -1 维保内容不符合规定 0 操作成功;1此维保内容已经绑定了其他的标签;2此标签已绑定了其他的维保内容
	 * 
	 * @author ssu
	 * @date 2015-6-16 下午2:22:45
	 */
	public enum ResultStatus implements IEnum {
		/** 维保内容不符合规定  */
		NONCOMPLIANCE(-1), 
		/** 操作成功 */
		SUCCESS(0), 
		/** 此维保内容已经绑定了其他的标签 */
		ITEMBINDEDTAG(1), 
		/** 此标签已绑定了其他的维保内容 */
		TAGBINDEDITEM(2);
		private int value;

		private ResultStatus(int value) {
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

	};

	/** -1 维保内容不符合规定 0 操作成功;1此维保内容已经绑定了其他的标签;2此标签已绑定了其他的维保内容 */
	private ResultStatus statusCode;

	public ResultStatus getStatus() {
		return statusCode;
	}

	public void setStatus(ResultStatus statusCode) {
		this.statusCode = statusCode;
	}
}
