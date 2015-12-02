package com.chinacnit.elevatorguard.mobile.util;

import android.os.Build;
import android.util.Log;

import com.chinacnit.elevatorguard.mobile.config.ConfigSettings;

/**
 * 日志打印
 * 
 * @author ssu
 * @date 2015-5-12 下午5:35:51
 */
public class LogUtils {

	/**
	 * log状态帮助类
	 * 
	 * @author ss
	 * 
	 */
	public static class LogTag {
		private String tagString;
		private boolean log;

		private LogTag(String tag, boolean log) {
			this.tagString = tag;
			this.log = log;
		}

		public String getTagString() {
			return tagString;
		}
	}

	private static final String TAG = "LogUtils";

	private static final LogTag LOG_FLAG = new LogTag(TAG, true);

	/**
	 * 创建LogTag
	 * 
	 * @param tag
	 *            Log tag字符串
	 * @param log
	 *            是否打印此类log标志位
	 * @return LogTag对象
	 */
	public static final LogTag getLogTag(String tag, boolean log) {
		return new LogTag(tag, log);
	}

	public static final void v(LogTag tag, String method, Object message) {
		if (tag == null) {
			tag = LOG_FLAG;
		}
		if (ConfigSettings.LOG_TAG && tag.log) {
			Log.v(tag.tagString, "Method: " + method + ", " + "Message: "
					+ message);
		}
	}

	public static final void timeConsume(LogTag tag, String method, long millis) {
		if (tag == null) {
			tag = LOG_FLAG;
		}
		if (ConfigSettings.LOG_TAG && tag.log) {
			Log.i(tag.tagString, "Method: " + method + ", " + "time consume: "
					+ millis + " ms");
		}
	}

	public static final void d(LogTag tag, String method, Object message) {
		if (tag == null) {
			tag = LOG_FLAG;
		}
		if (ConfigSettings.LOG_TAG && tag.log) {
			Log.d(tag.tagString, "Method: " + method + ", " + "Message: "
					+ message);
		}
	}

	public static final void i(LogTag tag, String method, Object message) {
		if (tag == null) {
			tag = LOG_FLAG;
		}
		if (ConfigSettings.LOG_TAG && tag.log) {
			Log.i(tag.tagString, "Method: " + method + ", " + "Message: "
					+ message);
		}
	}

	public static final void w(LogTag tag, String method, Object message) {
		if (tag == null) {
			tag = LOG_FLAG;
		}
		if (ConfigSettings.LOG_TAG && tag.log) {
			Log.w(tag.tagString, "Method: " + method + ", " + "Message: "
					+ message);
		}
	}

	public static final void e(LogTag tag, String method, Object message) {
		if (tag == null) {
			tag = LOG_FLAG;
		}
		String trace = "Method: " + method + ", " + "Message: " + message;
		if (ConfigSettings.LOG_TAG && tag.log) {
			Log.e(tag.tagString, trace);
		}
	}

	public static final void e(LogTag tag, String method, Throwable message) {
		if (tag == null) {
			tag = LOG_FLAG;
		}
		String trace = "Method: " + method + ", " + "Message: "
				+ Log.getStackTraceString(message);
		if (ConfigSettings.LOG_TAG && tag.log) {
			Log.e(tag.tagString, trace);
		}
	}

	public static String getStackTraceString(Throwable ex) {
		String appVersionName = ConfigSettings.INNER_APK_VERSION;
		String msg = Helper.getTime() + "\n" + Build.DISPLAY
				+ ", " // 系统版本
				+ appVersionName /* APK版本 */+ ", \n"
				+ Log.getStackTraceString(ex); // 板卡信息;
		return msg;
	}
}
