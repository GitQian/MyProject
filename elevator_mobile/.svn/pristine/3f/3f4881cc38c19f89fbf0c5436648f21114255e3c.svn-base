package com.ztkj.base.business;


import com.ztkj.tool.Tool;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

/**
 * 基类，用于整体风格修正</br> 修正1：内存被清理时是否退出memoryCleanedExit</br> 
 * 在子activity的oncreate方法中 super之后加上<br>
 * if(isMemoryCleanedExit()) {<br>
 * 		finish();<br>
 * 		return; <br>
 * }<br>
 * 最好能自己处理保存变量重新加载
 * 
 * 
 */
public  class BaseActivity extends Activity implements OnClickListener{
	public Activity mActivity;
	public String TAG = this.getClass().getSimpleName();
	
	/**唤起的Activity完成任务ok*/
	public static final int RESULT_OK = -1;  //Activity返回结果ok
	/** Standard activity result: operation canceled. */
	public static final int RESULT_CANCELED    = 0;
	/** Start of user-defined activity results. */
	public static final int RESULT_FIRST_USER   = 1;
	/**唤起的Activity没能完成任务,与服务器的交互失败*/
	public static final int RESULT_FAILED = 2; //失败，操作finish当前Activity。
	
	public BaseActivity() {
		this.mActivity = this;
	}
			
	/**
	 * 内存被清理时是否退出，默认不退出
	 */
	private boolean mMemoryCleanedExit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		if(savedInstanceState!=null){
			if(savedInstanceState.getBoolean("exit")){
				mMemoryCleanedExit=true;
			}
			
		}
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putBoolean("exit", true);
		//缓存信息
		
		
		super.onSaveInstanceState(outState);
	}

	public boolean isMemoryCleanedExit() {
		return mMemoryCleanedExit;
	}

	/**
	 * @param cause
	 *            不为空时弹出toast
	 */
	public void dofinish(String cause) {
		if (cause != null) {
			Tool.toastShow(this, cause);
		}
		finish();
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	public void onClick(View v)
	{
		// TODO Auto-generated method stub
		
	}
}
