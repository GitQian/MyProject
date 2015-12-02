package com.ztkj.base.business;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.ztkj.tool.Tool;
public class BaseFragmentActivity extends FragmentActivity{
	public Activity mActivity;
	public String TAG = this.getClass().getSimpleName();
	/**
	 * 内存被清理时是否退出，默认不退出
	 */
	private boolean memoryCleanedExit;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		mActivity = this;
		super.onCreate(savedInstanceState);
		if(savedInstanceState!=null){
			if(savedInstanceState.getBoolean("exit")){
				memoryCleanedExit=true;
			}		
		}
	}
	
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		outState.putBoolean("exit", true);
		super.onSaveInstanceState(outState);
	}

	public boolean isMemoryCleanedExit() {
		return memoryCleanedExit;
	}
	/**
	 * @param cause 不为空时弹出toast
	 */
	public void dofinish(String cause){
		if(cause!=null){
			Tool.toastShow(this, cause);
		}
		finish();
	}
}
