package com.chinacnit.elevatorguard.mobile.ui.fragment;

import android.app.Fragment;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;

import com.chinacnit.elevatorguard.mobile.util.LogUtils;
import com.chinacnit.elevatorguard.mobile.util.LogUtils.LogTag;

/**
 * Fragment基类
 * 
 * @author ssu
 * @date 2015-5-18 下午2:44:01
 */
public abstract class BaseFragment extends Fragment {
	private static final LogTag LOG_TAG = LogUtils.getLogTag(BaseFragment.class.getSimpleName(), true);

	private boolean mWasDestroyed;

	protected final Paint paint = new Paint(Paint.FILTER_BITMAP_FLAG
			| Paint.DITHER_FLAG);
	protected Handler mHandler;

	/**
	 * 保证值调用一次notifyPlayComplete
	 */
	protected boolean switchComplete = true;
	/* Initialization block */
	{
		if (Looper.myLooper() == null) {
			Looper.prepare();
			mHandler = new Handler();
			Looper.loop();
		} else {
			mHandler = new Handler();
		}
	}

	public BaseFragment() {
		mWasDestroyed = false;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		mWasDestroyed = false;
	}

	@Override
	public void onResume() {
		super.onResume();
		LogUtils.d(LOG_TAG, "onResume", "onResume");
	}

	@Override
	public void onStart() {
		LogUtils.d(LOG_TAG, "onStart", "onStart");
		super.onStart();
	}

	@Override
	public void onDestroy() {
		LogUtils.d(LOG_TAG, "onDestroy", "onDestroy");
		super.onDestroy();
	}

	@Override
	public void onPause() {
		super.onPause();
		LogUtils.d(LOG_TAG, "onPause", "onPause");
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		LogUtils.d(LOG_TAG, "onDestroyView", "onDestroyView");
		mWasDestroyed = true;
	}
}
