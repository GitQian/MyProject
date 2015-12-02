package com.chinacnit.elevatorguard.mobile.util;

import com.chinacnit.elevatorguard.mobile.application.ElevatorGuardApplication;
import com.chinacnit.elevatorguard.mobile.config.Constants;
import com.lidroid.xutils.DbUtils;


public class DBUtil {
	private static DBUtil sInstance;
	private DbUtils mDbUtils;
	private static final String DB_NAME = "EG.dat";
	private static String DB_PATH = Constants.DEFAULT_STORAGE_PATH + "/" + Constants.DEFAULT_PROJECT_PATH;
	
	public DBUtil() {
		if (null == mDbUtils) {
			mDbUtils = DbUtils.create(ElevatorGuardApplication.getInstance(), DB_PATH, DB_NAME);
			mDbUtils.configAllowTransaction(true);
			mDbUtils.configDebug(true);
		}
	}
	
	public static DBUtil getInstance() {
		if (null == sInstance) {
			sInstance = new DBUtil();
		}
		return sInstance;
	}
	
	public DbUtils getDbUtils() {
		return mDbUtils;
	}
}
