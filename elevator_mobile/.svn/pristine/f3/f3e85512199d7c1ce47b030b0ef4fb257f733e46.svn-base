package com.chinacnit.elevatorguard.mobile.util;


import android.graphics.Bitmap;

import com.chinacnit.elevatorguard.mobile.R;
import com.chinacnit.elevatorguard.mobile.application.ElevatorGuardApplication;
import com.chinacnit.elevatorguard.mobile.config.Constants;
import com.lidroid.xutils.BitmapUtils;

public class BitmapUtil {
	private static BitmapUtil sInstance;
	private BitmapUtils mBitmapUtils;
	private String diskCachePath = Constants.DEFAULT_STORAGE_PATH + "/" + Constants.DEFAULT_PROJECT_PATH + "/cache";
			
	public BitmapUtil() {
		if (null == mBitmapUtils) {
			mBitmapUtils = new BitmapUtils(ElevatorGuardApplication.getInstance(), diskCachePath);
			mBitmapUtils.configDefaultLoadingImage(R.drawable.no_photo); //设置默认背景图片
			mBitmapUtils.configDefaultLoadFailedImage(R.drawable.no_photo); //设置加载失败的图片
			mBitmapUtils.configDefaultBitmapConfig(Bitmap.Config.RGB_565); // 设置图片压缩类型
		}
	}

	public static BitmapUtil getInstance() {
		if (null == sInstance) {
			sInstance = new BitmapUtil();
		}
		return sInstance;
	}
}
