package com.chinacnit.elevatorguard.mobile.util;

import java.io.Serializable;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

/**
 * 控制页面跳转
 * 
 * @author ssu
 * @date 2015-5-19 上午10:52:28
 */
public class PageTurnUtil {

	private static Context context;

	public static void regAppContext(Context ctx) {
		PageTurnUtil.context = ctx;
	}

	public static Context appCtx() {
		return context;
	}

	/**
	 * 跳转到一个新页面
	 * 
	 * @param oldAct
	 * @param newActClass
	 * @param closeMe
	 */
	public static void gotoActivity(Activity oldAct,
			Class<? extends Activity> newActClass, boolean closeMe,
			Map<String, Serializable> parameters) {
		Intent intent = new Intent(oldAct, newActClass);
		if (parameters != null) {
			for (String s : parameters.keySet()) {
				intent.putExtra(s, parameters.get(s));
			}
		}
		oldAct.startActivity(intent);
		if (closeMe)
			oldAct.finish();
		return;
	}

	public static void gotoActivity(Activity oldAct, Class<? extends Activity> newActClass, boolean closeMe) {
		gotoActivity(oldAct, newActClass, closeMe, null);
	}

	/**
	 * 跳转到一个新页面
	 * 
	 * @param oldAct
	 * @param newActClass
	 * @param closeMe
	 * @param parameters
	 * @param requestCode
	 *            请求代号
	 */
	public static void gotoActivityForResult(Activity oldAct, 
			Class<? extends Activity> newActClass, boolean closeMe,
			Map<String, Serializable> parameters, int requestCode) {
		Intent intent = new Intent(oldAct, newActClass);
		if (parameters != null) {
			for (String s : parameters.keySet()) {
				intent.putExtra(s, parameters.get(s));
			}
		}
		oldAct.startActivityForResult(intent, requestCode);
		if (closeMe)
			oldAct.finish();
		return;
	}
	
	public static void gotoActivityToResult(Activity oldAct, 
			Class<? extends Activity> newActClass, boolean closeMe,
			Map<String, Serializable> parameters, int resultCode) {
		Intent intent = new Intent(oldAct, newActClass);
		if (parameters != null) {
			for (String s : parameters.keySet()) {
				intent.putExtra(s, parameters.get(s));
			}
		}
		oldAct.setResult(resultCode, intent);
		if (closeMe) {
			oldAct.finish();
		}
	}

	public static void goBack(Activity act) {
		// act.onKeyDown(KeyEvent.KEYCODE_BACK, null);
		act.finish();
	}

	public static void closeMe(Activity act) {
		act.finish();
	}

}
