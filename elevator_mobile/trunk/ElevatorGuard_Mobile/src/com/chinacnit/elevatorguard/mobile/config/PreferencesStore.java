package com.chinacnit.elevatorguard.mobile.config;

import android.app.Application;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.chinacnit.elevatorguard.mobile.application.ElevatorGuardApplication;
import com.chinacnit.elevatorguard.mobile.util.LogUtils;
import com.chinacnit.elevatorguard.mobile.util.LogUtils.LogTag;

public final class PreferencesStore {
	private static final LogTag TAG = LogUtils.getLogTag(PreferencesStore.class.getSimpleName(), false);

	private static final String SHARED_PREFERENCES_STORE_KEY = "ss";

	private static final String KEY_MAXDAY = "Maxday";
	private static final String KEY_MAXNUM = "Maxnum";
	private static final String KEY_MAXSIZE = "Maxsize";
	private static final String KEY_ISFIRST_INSTALL = "isFirstInstall";
	private static final String KEY_VERSIONNAME = "versionName";
	
	/**
	 * 是否已经缓存用户
	 */
	public static final String KEY_IS_CACHED_USER = "isCacheUser";

	private static SharedPreferences mSharedPreferences;
	private static PreferencesStore sInstance = new PreferencesStore();

	private PreferencesStore() {
		mSharedPreferences = ElevatorGuardApplication.getInstance().getSharedPreferences(
				SHARED_PREFERENCES_STORE_KEY, Application.MODE_WORLD_READABLE);
	}

	/**
	 * this code does not use synchronization and ensures that the
	 * SharedPreferencesStore object is not created until a call is made to the
	 * static getInstance() method.
	 * 
	 * @return
	 */
	public static PreferencesStore getInstance() {
		return sInstance;
	}

	public SharedPreferences getCustomPreferences() {
		return mSharedPreferences;
	}

	/**
	 * 根据key获取boolean值
	 * 
	 * @param key
	 *            shared里面的key
	 * @return true表示存在，反之不存在
	 */
	public boolean getBooleanToKey(String key) {
		if (!mSharedPreferences.contains(key)) {
			return false;
		}
		return mSharedPreferences.getBoolean(key, false);
	}
	
	public void putBooleanToKey(String key, boolean value) {
		if (TextUtils.isEmpty(key)) {
			return;
		}
		SharedPreferences.Editor editor = mSharedPreferences.edit();
		editor.putBoolean(key, value);
		editor.commit();
	}

	/**
	 * 保存日志策略的保存天数
	 * 
	 * @param maxday
	 *            保存的最大天数
	 */
	public void saveMaxDay(String maxday) {
		SharedPreferences.Editor editor = mSharedPreferences.edit();
		editor.putString(KEY_MAXDAY, maxday);
		editor.commit();
	}

	/**
	 * 获取日志策略的保存天数
	 * 
	 * @return 保存的最大天数
	 */
	public String getMaxDay() {
		return mSharedPreferences.getString(KEY_MAXDAY, "5");
	}

	/**
	 * 保存日志策略的保存数量
	 * 
	 * @param maxday
	 *            保存的最大数量
	 */
	public void saveMaxNum(String maxnum) {
		SharedPreferences.Editor editor = mSharedPreferences.edit();
		editor.putString(KEY_MAXNUM, maxnum);
		editor.commit();
	}

	/**
	 * 获取日志策略的保存的最大数量
	 * 
	 * @return 保存的最大数量
	 */
	public String getMaxNum() {
		return mSharedPreferences.getString(KEY_MAXNUM, "3");
	}

	/**
	 * 保存日志策略的保存大小
	 * 
	 * @param maxday
	 *            保存的最大大小
	 */
	public void saveMaxSize(String maxsize) {
		SharedPreferences.Editor editor = mSharedPreferences.edit();
		editor.putString(KEY_MAXSIZE, maxsize);
		editor.commit();
	}

	/**
	 * 获取日志策略的保存大小
	 * 
	 * @return 保存的最大大小
	 */
	public String getMaxSize() {
		return mSharedPreferences.getString(KEY_MAXSIZE, "2");
	}
	
	/**
	 * 设置是否是第一次安装
	 * @param
	 * @author: ssu
	 * @date: 2015-5-14 下午8:24:43
	 */
	public void saveIsFirstInstall(boolean isFirstInstall) {
		SharedPreferences.Editor editor = mSharedPreferences.edit();
		editor.putBoolean(KEY_ISFIRST_INSTALL, isFirstInstall);
		editor.commit();
	}
	
	/**
	 * 获得是否是第一次安装
	 * @param
	 * @author: ssu
	 * @date: 2015-5-15 下午7:09:23
	 */
	public boolean getIsFirstInstall() {
		return mSharedPreferences.getBoolean(KEY_ISFIRST_INSTALL, true);
	}

	/**
	 * 保存版本名称
	 * @param 
	 * @author: ssu
	 * @date: 2015-5-20 下午3:59:00
	 */
	public void saveVersionName(String versionName) {
		SharedPreferences.Editor editor = mSharedPreferences.edit();
		editor.putString(KEY_VERSIONNAME, versionName);
		editor.commit();
	}
	
	/**
	 * 获得版本名称
	 * @param
	 * @author: ssu
	 * @date: 2015-5-20 下午3:59:45
	 */
	public String getVersionName() {
		return mSharedPreferences.getString(KEY_VERSIONNAME, "");
	}
}
