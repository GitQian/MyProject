package com.chinacnit.elevatorguard.mobile.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.chinacnit.elevatorguard.mobile.application.ElevatorGuardApplication;
import com.chinacnit.elevatorguard.mobile.bean.LoginDetail;
import com.chinacnit.elevatorguard.mobile.bean.UserInfo;
import com.chinacnit.elevatorguard.mobile.config.PreferencesStore;
import com.chinacnit.elevatorguard.mobile.util.LogUtils.LogTag;

/**
 * 用户信息缓存工具类
 * @author ssu 
 * @date 2015-5-20 下午3:09:47
 */
public class UserCacheUtil {
	private static final LogTag LOG_TAG = LogUtils.getLogTag(UserCacheUtil.class.getSimpleName(), true);

	/** 登录详情 */
	private static File cachedLoginDetail = new File(ElevatorGuardApplication.getInstance().getFilesDir() + "/loginDetail");
	/** 用户信息 */
	private static File cachedUserInfo = new File(ElevatorGuardApplication.getInstance().getFilesDir() + "/userInfo");

	/**
	 * 获取登录详情
	 * 
	 * @param
	 * @author: ssu
	 * @date: 2015-5-20 下午3:03:34
	 */
	public static LoginDetail loadLoingDetail() {
		if (!PreferencesStore.getInstance().getBooleanToKey(PreferencesStore.KEY_IS_CACHED_USER)) {
			return null;
		}
		LoginDetail user = null;
		FileInputStream fileInputStream = null;
		try {
			fileInputStream = new FileInputStream(cachedLoginDetail);
			ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
			user = (LoginDetail) objectInputStream.readObject();
		} catch (Exception e) {
			LogUtils.e(LOG_TAG, "loadCachedUser", "load user cache file error!!!" + e);
		} finally {
			try{
				if (fileInputStream != null) {
					fileInputStream.close();
				}
			} catch (Exception e) {
				LogUtils.e(LOG_TAG, "loadCachedUser", e);
			}
		}
		return user;
	}
	
	/**
	 * 获取用户登录信息
	 * 
	 * @param
	 * @author: ssu
	 * @date: 2015-5-20 下午3:03:34
	 */
	public static UserInfo loadUserInfo() {
		if (!PreferencesStore.getInstance().getBooleanToKey(PreferencesStore.KEY_IS_CACHED_USER)) {
			return null;
		}
		UserInfo user = null;
		FileInputStream fileInputStream = null;
		try {
			fileInputStream = new FileInputStream(cachedUserInfo);
			ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
			user = (UserInfo) objectInputStream.readObject();
		} catch (Exception e) {
			LogUtils.e(LOG_TAG, "loadCachedUser", "load user cache file error!!!" + e);
		} finally {
			try{
				if (fileInputStream != null) {
					fileInputStream.close();
				}
			} catch (Exception e) {
				LogUtils.e(LOG_TAG, "loadCachedUser", e);
			}
		}
		return user;
	}

	/**
	 * 保存登录详情
	 * 
	 * @param
	 * @author: ssu
	 * @date: 2015-5-20 下午3:03:49
	 */
	public static void saveLoginDetail(LoginDetail loginDetail) throws IOException {
		clearLoginDetail();
		FileOutputStream fileOutputStream = new FileOutputStream(cachedLoginDetail);
		ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
		objectOutputStream.writeObject(loginDetail);
		fileOutputStream.close();
	}
	
	/**
	 * 保存用户信息
	 * @param
	 * @author: ssu
	 * @date: 2015-6-8 下午3:21:24
	 */
	public static void saveUserInfo(UserInfo userInfo) throws IOException {
		clearUserInfo();
		FileOutputStream fileOutputStream = new FileOutputStream(cachedUserInfo);
		ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
		objectOutputStream.writeObject(userInfo);
		fileOutputStream.close();
	}

	/**
	 * 清除缓存的登录详情
	 * @param
	 * @author: ssu
	 * @date: 2015-5-20 下午3:03:57
	 */
	public static void clearLoginDetail() {
		if (cachedLoginDetail.exists()) {
			cachedLoginDetail.delete();
		}
	}
	
	/**
	 * 清除缓存的用户信息
	 * @param
	 * @author: ssu
	 * @date: 2015-6-8 下午3:20:12
	 */
	public static void clearUserInfo() {
		if (cachedUserInfo.exists()) {
			cachedUserInfo.delete();
		}
	}
}
