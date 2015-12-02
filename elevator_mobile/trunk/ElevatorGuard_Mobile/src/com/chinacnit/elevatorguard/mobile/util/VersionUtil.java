package com.chinacnit.elevatorguard.mobile.util;

import android.content.Context;

import com.chinacnit.elevatorguard.mobile.util.LogUtils.LogTag;

/**
 * 版本信息工具类
 * 
 * @author ssu
 * @date 2015-5-20 下午3:51:50
 */
public class VersionUtil {
	private static final LogTag LOG_TAG = LogUtils.getLogTag(
			VersionUtil.class.getSimpleName(), true);

	/**
	 * 获得当前VersionName
	 * 
	 * @param
	 * @author: ssu
	 * @date: 2015-5-20 下午3:53:17
	 */
	public static String getCurrentVersionName(Context mContext) {
		String versionName = "";
		try {
			versionName = mContext.getPackageManager().getPackageInfo(
					mContext.getPackageName(), 0).versionName;
		} catch (Exception e) {
			LogUtils.e(LOG_TAG, "getCurrentVersionName", e);
		}
		return versionName;
	}
}
