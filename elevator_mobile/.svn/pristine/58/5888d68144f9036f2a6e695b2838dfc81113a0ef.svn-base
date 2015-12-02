package com.ztkj.tool;

import com.chinacnit.elevatorguard.mobile.api.HttpApi;


public class Config {
	/**
	 * 测试为true,正式为false
	 */
	public static boolean TEST=true;
	
	
	public static final String APP_ID="1104707293";//QQ的appId
	
	public static String memberId = null;
	
	public static final String IMAGEURL = "http://car.cnitcloud.com/imageUrl";
	
	public static final String PACKAGE="com.ztkj.carapp";
	// activity动画
	public static final int EXIT = 1;
	public static final int ENTER = 2;
	public static final int NONE = 3;
	public static final int BOTTOM_IN = 4;
	/**
	 * 网络超时设置，不需要重写HttpTransportSE方法了
	 */
	public static final int TIME_OUT = 20000;
	// 配置网络连接超时的处理
	public static final int ACCESSS = 1;
	public static final int ACCESSF = 2;
	public static final int RESULTF = 3;
	public static final int RESULTS = 4;

	public static final int MESSAGE_PAGE = 20;

	// 数据库的版本
	public static final int DB_VERSION = 1;
	//用来配置是否为测试版本1=测试,2=正式
	public static final String TESTFLAG="1";
	
	public static final String URL_COMMON_TEST =HttpApi.BASE_URL_LIFT;
	public static final String URL_COMMON_NORMAL =HttpApi.BASE_URL_LIFT;
	public static final String URL_COMMON =TEST?URL_COMMON_TEST:URL_COMMON_NORMAL;

	// 消息保留的时间
	public static final int SAVE_DAY = 7;
	
	
	//消息通知的一些参数	
	public static final String MSG_NOTIFY_CONFIG = "notify_config";  //消息配置文件，sharepreference
	public static final String IS_MSG_SOUND = "isSound";	//是否来消息有提示音
	public static final String is_MSG_VIBRATE = "isVibrate";	//是否来消息震动
	public static final String is_MSG_LIGHTS = "isLights";	//是否来消息闪烁
	public static final String IS_MSG_TROUBLE = "isTrouble";	//是否23：00-8：00不响铃不振动，不闪烁
	public static final String IS_MSG_SHOWCONTENT = "is_showcontent"; //来消息是否显示内容
	public static final String IS_MSG_SHOWDIALOG = "is_showdialog"; //来消息是否弹框
}
