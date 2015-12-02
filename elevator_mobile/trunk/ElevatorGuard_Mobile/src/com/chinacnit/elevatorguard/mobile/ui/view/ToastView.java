package com.chinacnit.elevatorguard.mobile.ui.view;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.chinacnit.elevatorguard.mobile.R;

/**
 * 自定义ToastView
 * 
 * @author ssu
 * @date 2015-5-18 上午11:16:00
 */
public class ToastView {

	private static Toast toast;

	private static void creatTotast(Context context, CharSequence message,
			int duration) {
		View view = LayoutInflater.from(context).inflate(R.layout.view_toast, null);
		toast = new Toast(context);
//		toast.setGravity(Gravity.CENTER, 0, 10);
		toast.setDuration(Toast.LENGTH_SHORT);
		((TextView) view.findViewById(R.id.message)).setText(message);
		toast.setView(view);
	}

	public static void showShort(Context context, int resId) {
		showShort(context, context.getResources().getString(resId));
	}

	/**
	 * 短时间显示Toast
	 * 
	 * @param context
	 * @param message
	 */
	public static void showShort(Context context, CharSequence message) {
		if (null == toast) {
			creatTotast(context, message, Toast.LENGTH_SHORT);
		} else {
			((TextView) toast.getView().findViewById(R.id.message)).setText(message);
		}
		toast.show();
	}

	/**
	 * 长时间显示Toast
	 * 
	 * @param context
	 * @param resId
	 */
	public static void showLong(Context context, int resId) {
		showLong(context, context.getResources().getString(resId));
	}

	/**
	 * 长时间显示Toast
	 * 
	 * @param context
	 * @param message
	 */
	public static void showLong(Context context, CharSequence message) {
		if (null == toast) {
			creatTotast(context, message, Toast.LENGTH_LONG);
		} else {
			((TextView) toast.getView().findViewById(R.id.message))
					.setText(message);
		}
		toast.show();
	}

	/** Hide the toast, if any. */
	public static void hideToast() {
		if (null != toast) {
			toast.cancel();
		}
	}
}
