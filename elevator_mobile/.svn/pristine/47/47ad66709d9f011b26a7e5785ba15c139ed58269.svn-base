package com.chinacnit.elevatorguard.mobile.util;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.hardware.Camera.Area;
import android.os.Build;
import android.text.Layout.Alignment;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chinacnit.elevatorguard.mobile.R;
import com.chinacnit.elevatorguard.mobile.application.ElevatorGuardApplication;
import com.chinacnit.elevatorguard.mobile.config.ConfigSettings;
import com.chinacnit.elevatorguard.mobile.util.LogUtils.LogTag;
import com.lidroid.xutils.db.annotation.Column;

public class LayoutUtil {

	private static final LogTag LOG_TAG = LogUtils.getLogTag(
			LayoutUtil.class.getSimpleName(), true);

	/**
	 * Gets the scaleRatio to which a rawSize constant should be multiplied with
	 * to get a number close to the realSize.
	 * 
	 * @param rawSize
	 *            the realSize
	 * @param realSize
	 *            the desiredSize
	 * @return the scale factor
	 */
	public static float getScaleRatio(int rawSize, int realSize) {
		return realSize / rawSize;
	}

	/**
	 * Gets the display width
	 * 
	 * @param context
	 *            the context
	 * @return the display width in px
	 */
	public static int getDisplayWidth() {

		Display d = ((WindowManager) ElevatorGuardApplication.getInstance()
				.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
		Point p = new Point();
		d.getSize(p);
		int width = p.x;
		return width;
	}

	/**
	 * Gets the display width
	 * 
	 * @param context
	 *            the context
	 * @return the display width in px
	 */
	public static int getDisplayHeight() {
		Display d = ((WindowManager) ElevatorGuardApplication.getInstance()
				.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
		Point p = new Point();
		d.getSize(p);
		int height = p.y;
		return height;
	}

	public static int getRealDisplayHeight_1() {
		int heightPixels;
		WindowManager w = ((WindowManager) ElevatorGuardApplication
				.getInstance().getSystemService(Context.WINDOW_SERVICE));
		Display d = w.getDefaultDisplay();
		DisplayMetrics metrics = new DisplayMetrics();
		d.getMetrics(metrics);
		// since SDK_INT = 1;
		heightPixels = metrics.heightPixels;
		// includes window decorations (statusbar bar/navigation bar)
		if (Build.VERSION.SDK_INT >= 14 && Build.VERSION.SDK_INT < 17) {
			try {
				heightPixels = (Integer) Display.class
						.getMethod("getRawHeight").invoke(d);
			} catch (Exception e) {
				LogUtils.e(LOG_TAG, "getRealDisplayHeight_1", e);
			}
		} else if (Build.VERSION.SDK_INT >= 17) { // includes window decorations
													// (statusbar bar/navigation
													// bar)
			try {
				Point realSize = new Point();
				Display.class.getMethod("getRealSize", Point.class).invoke(d,
						realSize);
				heightPixels = realSize.y;
			} catch (Exception e) {
				LogUtils.e(LOG_TAG, "getRealDisplayHeight_1", e);
			}
		}
		LogUtils.d(LOG_TAG, "getRealDisplayHeight_1", "realHightPixels:"
				+ heightPixels);
		return heightPixels;
	}

	public static int getRealDisplayWidth_1() {
		int widthtPixels;
		WindowManager w = ((WindowManager) ElevatorGuardApplication
				.getInstance().getSystemService(Context.WINDOW_SERVICE));
		Display d = w.getDefaultDisplay();
		DisplayMetrics metrics = new DisplayMetrics();
		d.getMetrics(metrics);
		// since SDK_INT = 1;
		widthtPixels = metrics.widthPixels;
		// includes window decorations (statusbar bar/navigation bar)
		if (Build.VERSION.SDK_INT >= 14 && Build.VERSION.SDK_INT < 17) {
			try {
				widthtPixels = (Integer) Display.class.getMethod("getRawWidth")
						.invoke(d);
			} catch (Exception e) {
				LogUtils.e(LOG_TAG, "getRealDisplayWidth_1", e);
			}
		} else if (Build.VERSION.SDK_INT >= 17) { // includes window decorations
													// (statusbar bar/navigation
													// bar)
			try {
				Point realSize = new Point();
				Display.class.getMethod("getRealSize", Point.class).invoke(d,
						realSize);
				widthtPixels = realSize.x;
			} catch (Exception e) {
				LogUtils.e(LOG_TAG, "getRealDisplayWidth_1", e);
			}
		}
		LogUtils.d(LOG_TAG, "getRealDisplayWidth_1", "realWidthPixels:"
				+ widthtPixels);
		return widthtPixels;
	}

	public static int getRealDisplayWidth() {
		int width = 0;
		Display display = ((WindowManager) ElevatorGuardApplication
				.getInstance().getSystemService(Context.WINDOW_SERVICE))
				.getDefaultDisplay();
		DisplayMetrics dm = new DisplayMetrics();
		@SuppressWarnings("rawtypes")
		Class c;
		try {
			c = Class.forName("android.view.Display");
			@SuppressWarnings("unchecked")
			Method method = c.getMethod("getRealMetrics", DisplayMetrics.class);
			method.invoke(display, dm);
			width = dm.widthPixels;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return width;
	}

	public static int getRealDisplayHeight() {
		int height = 0;
		Display display = ((WindowManager) ElevatorGuardApplication
				.getInstance().getSystemService(Context.WINDOW_SERVICE))
				.getDefaultDisplay();
		DisplayMetrics dm = new DisplayMetrics();
		@SuppressWarnings("rawtypes")
		Class c;
		try {
			c = Class.forName("android.view.Display");
			@SuppressWarnings("unchecked")
			Method method = c.getMethod("getRealMetrics", DisplayMetrics.class);
			method.invoke(display, dm);
			height = dm.heightPixels;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return height;
	}

	/**
	 * Get density dpi
	 * 
	 * @param context
	 * @return
	 */
	public static int getDensityDpi() {
		Display d = ((WindowManager) ElevatorGuardApplication.getInstance()
				.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
		DisplayMetrics dm = new DisplayMetrics();
		d.getMetrics(dm);
		return dm.densityDpi;
	}

	/**
	 * get density
	 * 
	 * @param context
	 * @return
	 */
	public static float getDensity() {
		Display d = ((WindowManager) ElevatorGuardApplication.getInstance()
				.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
		DisplayMetrics dm = new DisplayMetrics();
		d.getMetrics(dm);
		return dm.density;
	}

	/**
	 * get orientation
	 * 
	 * @param context
	 * @return
	 */
	public static int getDisplayOrientation() {
		int orientation = ElevatorGuardApplication.getInstance().getResources()
				.getConfiguration().orientation;
		return orientation;
	}

	/**
	 * get scaled density
	 * 
	 * @param context
	 * @return
	 */
	public static float getScaledDensity() {
		Display d = ((WindowManager) ElevatorGuardApplication.getInstance()
				.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
		DisplayMetrics dm = new DisplayMetrics();
		d.getMetrics(dm);
		return dm.scaledDensity;
	}

	/**
	 * get fullscreen layout params
	 * 
	 * @return
	 */
	public static FrameLayout.LayoutParams getFullScreenLayoutParams() {
		int width, height;
		width = ConfigSettings.SCREEN_WIDTH;
		height = ConfigSettings.SCREEN_HEIGHT;
		FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(width,
				height);
		return params;
	}

	/**
	 * 将px值转换为dip或dp值，保证尺寸大小不变
	 * 
	 * @param pxValue
	 * @param scale
	 *            （DisplayMetrics类中属性density）
	 * @return
	 */
	public static int px2dip(float pxValue, float scale) {
		return (int) (pxValue / scale + 0.5f);
	}

	/**
	 * 将dip或dp值转换为px值，保证尺寸大小不变
	 * 
	 * @param dipValue
	 * @param scale
	 *            （DisplayMetrics类中属性density）
	 * @return
	 */
	public static int dip2px(float dipValue, float scale) {
		return (int) (dipValue * scale + 0.5f);
	}

	/**
	 * 将px值转换为sp值，保证文字大小不变
	 * 
	 * @param pxValue
	 * @param fontScale
	 *            （DisplayMetrics类中属性scaledDensity）
	 * @return
	 */
	public static int px2sp(float pxValue, float fontScale) {
		return (int) (pxValue / fontScale + 0.5f);
	}

	/**
	 * 将sp值转换为px值，保证文字大小不变
	 * 
	 * @param spValue
	 * @param fontScale
	 *            （DisplayMetrics类中属性scaledDensity）
	 * @return
	 */
	public static int sp2px(float spValue, float fontScale) {
		return (int) (spValue * fontScale + 0.5f);
	}

}
