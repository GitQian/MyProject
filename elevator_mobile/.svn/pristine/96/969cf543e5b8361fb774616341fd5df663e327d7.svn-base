package com.chinacnit.elevatorguard.mobile.config;

import java.io.File;
import java.util.Locale;

import android.content.Context;
import android.media.AudioManager;

import com.chinacnit.elevatorguard.mobile.R;
import com.chinacnit.elevatorguard.mobile.application.ElevatorGuardApplication;
import com.chinacnit.elevatorguard.mobile.bean.LoginDetail;
import com.chinacnit.elevatorguard.mobile.bean.UserInfo;
import com.chinacnit.elevatorguard.mobile.util.Helper;
import com.chinacnit.elevatorguard.mobile.util.LayoutUtil;
import com.chinacnit.elevatorguard.mobile.util.LogUtils;
import com.chinacnit.elevatorguard.mobile.util.LogUtils.LogTag;
import com.chinacnit.elevatorguard.mobile.util.UserCacheUtil;

/**
 * 系统运行参数以及相应设置参数方法
 * 
 * @author ss
 * 
 */
public final class ConfigSettings {

	private static final LogTag TAG = LogUtils.getLogTag(ConfigSettings.class.getSimpleName(), true);

	/** 程序内部版本号 */
	public static final String INNER_APK_VERSION = ElevatorGuardApplication.getInstance().getString(R.string.app_version);
	/** 是否开启输出log到命令行 */
	public static final boolean LOG_TAG = true;

	public static int DENSITY_DPI = LayoutUtil.getDensityDpi();
	public static float DENSITY = LayoutUtil.getDensity();
	public static float SCALED_DENSITY = LayoutUtil.getScaledDensity();
	public static int SCREEN_WIDTH = LayoutUtil.getRealDisplayWidth_1();
	public static int SCREEN_HEIGHT = LayoutUtil.getRealDisplayHeight_1();
	public static int SCREEN_ORIENTATION = LayoutUtil.getDisplayOrientation();

	public static String MAC_ADDRESS = Helper.getMacAddress().toUpperCase();
	/** 启动画面持续时间 */
	public static int SPLASH_DURATION = 1 * 1000;

	/** 语言 */
	public static byte LOCALE = 1;

	public static Locale SYSTEM_LOCALE = Locale.CHINA;
	
	/**
	 * 当前登录详情
	 */
	private static LoginDetail sLoginDetail;
	
	/** 当前登录用户信息 */
	private static UserInfo sUserInfo;

	/**
	 * 应用启动加载配置信息
	 * @author: ssu
	 * @date: 2015-5-20 下午3:29:34
	 */
	public static void loadConfigSettings() {
		DENSITY_DPI = LayoutUtil.getDensityDpi();
		DENSITY = LayoutUtil.getDensity();
		SCALED_DENSITY = LayoutUtil.getScaledDensity();
		SCREEN_WIDTH = LayoutUtil.getRealDisplayWidth_1();
		SCREEN_HEIGHT = LayoutUtil.getRealDisplayHeight_1();
		SCREEN_ORIENTATION = LayoutUtil.getDisplayOrientation();
		LOCALE = (byte) (Locale.getDefault().getCountry().contains("CN") ? 1 : 0);
		SYSTEM_LOCALE = ElevatorGuardApplication.getInstance().getResources().getConfiguration().locale;
		LogUtils.d(TAG, "loadConfigSettings", "DENSITY_DPI:" + DENSITY_DPI 
				+ "DENSITY:" + DENSITY 
				+ "SCALED_DENSITY:" + SCALED_DENSITY
				+ "SCREEN_WIDTH:" + SCREEN_WIDTH
				+ "SCREEN_HEIGHT:" + SCREEN_HEIGHT
				+ ",SCREEN_ORIENTATION: " + SCREEN_ORIENTATION);
		initUser();
		initSDCardDir();
	}
	
	/**
	 * 初始化用户信息
	 * @author: ssu
	 * @date: 2015-5-20 下午3:29:25
	 */
	public static void initUser() {
        if (PreferencesStore.getInstance().getBooleanToKey(PreferencesStore.KEY_IS_CACHED_USER)) {
            // 获取缓存用户
            LoginDetail cacheUser = UserCacheUtil.loadLoingDetail();
            UserInfo userInfo = UserCacheUtil.loadUserInfo();
            if (cacheUser != null && userInfo != null) {
            	sLoginDetail = cacheUser;
            	sUserInfo = userInfo;
            } else {
            	sLoginDetail = null;
            	sUserInfo = null;
            }
        }
    }
	
	/**
	 * 初始化SD卡项目路径
	 * @author: ssu
	 * @date: 2015-5-20 下午3:29:06
	 */
	private static void initSDCardDir() {
        File sdCardDirFile = new File(Constants.APPLICATION_SDCARD_PATH);
        if (!sdCardDirFile.exists()) {
        	sdCardDirFile.mkdirs();
        }
    }

	/**
	 * 获取系统音量大小
	 * 
	 * @param volume
	 *            音量大小
	 */
	public static int getSystemVolume() {
		AudioManager am = (AudioManager) ElevatorGuardApplication.getInstance()
				.getSystemService(Context.AUDIO_SERVICE);
		int currentVolume = am.getStreamVolume(AudioManager.STREAM_MUSIC);
		return currentVolume;
	}

	/**
	 * 设置系统音量大小
	 * 
	 * @param volume
	 *            音量大小
	 */
	public static void setSystemVolume(int volume) {
		if (volume >= 0) {
			AudioManager am = (AudioManager) ElevatorGuardApplication.getInstance()
					.getSystemService(Context.AUDIO_SERVICE);
			if (volume != am.getStreamVolume(AudioManager.STREAM_MUSIC)) {
				am.setStreamVolume(AudioManager.STREAM_MUSIC, volume,
						AudioManager.FLAG_PLAY_SOUND);
			}
		}
	}

	/**
	 * 获取终端日志策略保存天数
	 * 
	 * 
	 */
	public static String getMaxDay() {
		return PreferencesStore.getInstance().getMaxDay();
	}

	/**
	 * 获取终端日志策略保存大小
	 * 
	 * @return 默认2，单位M
	 */
	public static int getMaxSize() {
		int size = 2;
		try {
			size = Integer
					.parseInt(PreferencesStore.getInstance().getMaxSize());
		} catch (Exception e) {
		}
		return size;
	}

	/**
	 * 获取终端日志策略保存数量
	 */
	public static int getMaxNum() {
		int num = 3;
		try {
			num = Integer.parseInt(PreferencesStore.getInstance().getMaxNum());
		} catch (Exception e) {
		}
		return num;
	}
	
	public static LoginDetail getLoginDetail() {
		return sLoginDetail;
	}
	
	public static void saveLoginDetail(LoginDetail loginDetail) {
		sLoginDetail = loginDetail;
	}

	public static UserInfo getUserInfo() {
		return sUserInfo;
	}

	public static void saveUserInfo(UserInfo userInfo) {
		sUserInfo = userInfo;
	}
}
