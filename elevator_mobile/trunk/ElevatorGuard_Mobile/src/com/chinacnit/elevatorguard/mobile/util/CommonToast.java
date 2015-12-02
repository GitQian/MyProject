package com.chinacnit.elevatorguard.mobile.util;

import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

public class CommonToast {
	private static Toast mToast;
	private static Handler mHandler = new Handler();
	private static Runnable r = new Runnable() {
		public void run() {
			mToast.cancel();
		}
	};

	public static void showToast(Context mContext, String text, int duration) {
		mHandler.removeCallbacks(r);
		if (mToast != null) {
			mToast.setText(text);
		} else {
			mToast = Toast.makeText(mContext, text, Toast.LENGTH_SHORT);
		}
		mHandler.postDelayed(r, duration);
		mToast.show();
	}

	// 显示Toast代码：CustomToast.showToast(getBaseContext(), "提示信息", 1000);
	// 因为一般提示信息都是放在strings.xml中，所以为了方便使用，又写了个方法：
	public static void showToast(Context mContext, int resId, int duration) {
		showToast(mContext, mContext.getResources().getString(resId), duration);
	}
	
	public static void showToast(Context mContext, int resId) {
		showToast(mContext, mContext.getResources().getString(resId), (int)2.5 * 1000);
	}
	
	public static void showToast(Context mContext, String text) {
		showToast(mContext, text, (int)2.5 * 1000);
	}
}
