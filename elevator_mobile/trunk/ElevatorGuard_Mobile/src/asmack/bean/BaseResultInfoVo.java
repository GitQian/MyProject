package asmack.bean;

/**
 * 操作的返回信息
 * 
 * @author liucheng liucheng187@qq.com
 */
public class BaseResultInfoVo {
	/**
	 * 返回信息，如“登录成功”，“注册成功”，“发送成功”，“连接断开”等。。。这类提示语信息
	 */
	private String msg;
	/**
	 * 操作类型码
	 */
	private int requestCode;
	/**
	 * 操作结果码， 一般是 Common_SMACK.SUCCESS或 Common_SMACK.FAILURE
	 */
	private int resultCode;
	

	public BaseResultInfoVo() {
	}

	public BaseResultInfoVo(String msg, int resultCode) {
		super();
		this.msg = msg;
		this.resultCode = resultCode;
	}

	public BaseResultInfoVo(String msg, int requestCode, int resultCode) {
		super();
		this.msg = msg;
		this.requestCode = requestCode;
		this.resultCode = resultCode;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public int getRequestCode() {
		return requestCode;
	}

	public void setRequestCode(int requestCode) {
		this.requestCode = requestCode;
	}

	public int getResultCode() {
		return resultCode;
	}

	public void setResultCode(int resultCode) {
		this.resultCode = resultCode;
	}

	@Override
	public String toString() {
		return "BaseResultInfoVo [msg=" + msg + ", requestCode=" + requestCode
				+ ", resultCode=" + resultCode + "]";
	}
	
	

}
