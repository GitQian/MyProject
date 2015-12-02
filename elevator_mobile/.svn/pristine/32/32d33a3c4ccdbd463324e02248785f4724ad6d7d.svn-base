package com.chinacnit.elevatorguard.mobile.config;

import android.os.Environment;

import com.chinacnit.elevatorguard.mobile.util.Helper;

/**
 * 程序运行中的常量
 * 
 * @author ss
 * 
 */
public class Constants {

	/** UTF-8 编码方式 */
	public static final String ENCODING = "UTF-8";
	/** gbk 编码方式 */
	public static final String GBK_ENCODING = "GBK";

	/** 通信超时间隔 */
	public final static int REQUEST_TIMEOUT = 30 * 1000;

	/** 最近时间，用户判断系统时间是否有误 */
	public static final long RECENT_TIME = 1364782210923l; // 2013-4-1 10:10:10

	/** 最大音量 */
	public static final int MAX_VOLUME_VALUE = Helper.getSystemMaxVolume();
	
	/** 默认存储路径 */
	public static final String DEFAULT_STORAGE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath();
	
	/** 默认项目路径名称 */
	public static final String DEFAULT_PROJECT_PATH = "elevatorguard";
	
	/** 应用程序根目录 */
	public static final String APPLICATION_SDCARD_PATH = DEFAULT_STORAGE_PATH + "/" + DEFAULT_PROJECT_PATH;

	/** 电梯卫士包名 */
	public static final String PUBDS_PACKAGENAME = "com.chinacnit.elevatorguard.mobile";
	
	public static final String APPID_LOGIN = "a53e5823d22a10f3cnit";
	public static final String APPID = "55S15qKv54mp6IGU572R";
	public static final String PRIVATE_KEY_LOGIN = "8e4679_CNIT_TAOPING_API_9c694e6";
	public static final String PRIVATE_KEY = "55S1_CNIT_LIFT_API_5qKv";
	public static final String MAC_NAME = "HmacSHA1";

	/**
	 * 程序版本
	 * 
	 * @author ss
	 * 
	 */
	public static class Version {

		/** 软件类型标志位 */
		private static final short VER_APP = 1;
		/** 操作系统版本号 */
		private static final short OS_VERSION = 1;
		/** 主版本号 */
		private static final short MAIN_VERSION = 1;
		/** 次版本号 */
		private static final short SUB_VERSION = 0;
		/** 软件次版本号序号 */
		private static final short SUB_VERSION_IDX = 0;

		/** 通信所用版本字段 */
		public static final int VERSION = getVersion();

		/**
		 * 返回版本，用于消息通信中version字段
		 * 
		 * @return 4字节 version int
		 */
		private final static int getVersion() {
			return (int) ((VER_APP << 26) | (OS_VERSION << 22)
					| (MAIN_VERSION << 16) | (SUB_VERSION << 8) | SUB_VERSION_IDX);
		}

	}
}
