package com.chinacnit.elevatorguard.mobile.application;

import java.io.File;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.os.StrictMode;
import android.view.View;
import android.widget.ImageView;

import com.chinacnit.elevatorguard.mobile.api.HttpApi;
import com.chinacnit.elevatorguard.mobile.config.ConfigSettings;
import com.chinacnit.elevatorguard.mobile.util.GeneralTimer;
import com.chinacnit.elevatorguard.mobile.util.LogManager;
import com.chinacnit.elevatorguard.mobile.util.LogUtils;
import com.chinacnit.elevatorguard.mobile.util.LogUtils.LogTag;
import com.chinacnit.elevatorguard.mobile.util.PageTurnUtil;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.utils.StorageUtils;

/**
 * 应用程序入口
 * 
 * @author ssu
 * @date 2015-5-12 下午2:49:07
 */
public class ElevatorGuardApplication extends Application implements Thread.UncaughtExceptionHandler {
	private static final LogTag LOG_TAG = LogUtils.getLogTag(ElevatorGuardApplication.class.getSimpleName(), true);
	private static ElevatorGuardApplication sInstance;
	private HttpApi api;

	/**
	 * 获得MyApplication实例
	 * 
	 * @param
	 * @author: ssu
	 * @date: 2015-5-12 下午4:57:21
	 */
	public static ElevatorGuardApplication getInstance() {
		return sInstance;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		sInstance = this;
		initImageLoader(getApplicationContext());
		ConfigSettings.loadConfigSettings();
		PageTurnUtil.regAppContext(this);
		api = new HttpApi(this);
		LogUtils.d(LOG_TAG, "onCreate", "width: " + ConfigSettings.SCREEN_WIDTH
				+ ", height: " + ConfigSettings.SCREEN_HEIGHT
				+ ", density dpi: " + ConfigSettings.DENSITY_DPI
				+ ", density: " + ConfigSettings.DENSITY + ", scaledDensity: "
				+ ConfigSettings.SCALED_DENSITY + ", orientation: "
				+ ConfigSettings.SCREEN_ORIENTATION);
		GeneralTimer.init();
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
				.detectAll().penaltyLog().build());
		StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
				.detectLeakedSqlLiteObjects().penaltyLog().penaltyDeath()
				.build());
		Thread.setDefaultUncaughtExceptionHandler(this);
	}
	public void initImageLoader(Context context) {
		File cacheDir = StorageUtils.getCacheDirectory(context);
		DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
				.cacheOnDisc(true).bitmapConfig(Config.RGB_565)
				.imageScaleType(ImageScaleType.EXACTLY).build();
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				context).memoryCache(new WeakMemoryCache())
				.threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				.discCacheFileNameGenerator(new Md5FileNameGenerator())
				.tasksProcessingOrder(QueueProcessingType.FIFO)
				.defaultDisplayImageOptions(defaultOptions)
				.discCache(new UnlimitedDiscCache(cacheDir)).build();

		ImageLoader.getInstance().init(config);
	}
	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		String trace = LogUtils.getStackTraceString(ex);
		LogUtils.e(LOG_TAG, "uncaughtException", trace);
		LogManager.getInstance().writeCrashLog(trace);
		try {
			Thread.sleep(1 * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		android.os.Process.killProcess(android.os.Process.myPid());
		System.exit(0);
	}

	@Override
	public void onLowMemory() {
		LogUtils.w(LOG_TAG, "onLowMemory", "system is running on low memory");
		// BitmapCache.get().clearBitmapAll();
		super.onLowMemory();
	}

	@Override
	public void onTerminate() {
		LogUtils.w(LOG_TAG, "onTerminate", "application was terminated");
		super.onTerminate();
	}

	public HttpApi getApi() {
		return api;
	}

	public void setApi(HttpApi api) {
		this.api = api;
	}
	public static class AnimateFirstDisplayListener extends
	SimpleImageLoadingListener {

public static final List<String> displayedImages = Collections
		.synchronizedList(new LinkedList<String>());

public void onLoadingComplete(String imageUri, View view,
		Bitmap loadedImage) {
	if (loadedImage != null) {
		ImageView imageView = (ImageView) view;
		boolean firstDisplay = !displayedImages.contains(imageUri);
		if (firstDisplay) {
			FadeInBitmapDisplayer.animate(imageView, 500);
			displayedImages.add(imageUri);
		}
	}
}
}

}
