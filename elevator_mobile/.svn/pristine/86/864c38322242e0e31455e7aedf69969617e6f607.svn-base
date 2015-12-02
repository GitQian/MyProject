package com.chinacnit.elevatorguard.mobile.ui.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Window;
import android.view.WindowManager;

import com.chinacnit.elevatorguard.mobile.util.LogUtils;
import com.chinacnit.elevatorguard.mobile.util.LogUtils.LogTag;

/**
 * Activity基类，包含通用方法
 * 
 * @author ss
 * 
 */
public class BaseActivity extends FragmentActivity {
	private static final LogTag LOG_TAG = LogUtils.getLogTag(BaseActivity.class.getSimpleName(), true);

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		forceAlwaysOn();
		LogUtils.d(LOG_TAG, "onCreate", "onCreate");
	}

	@Override
	protected void onStop() {
		LogUtils.d(LOG_TAG, "onStop", "onStop");
		super.onStop();
	}

	@Override
	protected void onPause() {
		LogUtils.d(LOG_TAG, "onPause", "onPause");
		super.onPause();
	}

	@Override
	protected void onResume() {
		LogUtils.d(LOG_TAG, "onResume", "onResume");
		super.onResume();
	}

	@Override
	protected void onStart() {
		LogUtils.d(LOG_TAG, "onStart", "onStart");
		super.onStart();
	}

	/**
	 * This method will force the screen to remain on if this window is in
	 * foreground
	 */
	protected void forceAlwaysOn() {
		// 设置无标题
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		LogUtils.d(LOG_TAG, "onDestroy", "onDestroy");
	}
}
