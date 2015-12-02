package com.ztkj.tool;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * 通用的一些工具方法
 * @author liucheng  liucheng187@qq.com
 */
public class CommUtil {
	private static CommUtil mCommUtil;
	private Context mContext;
	public CommUtil(Context context) {
		this.mContext = context;
	}
	public static synchronized CommUtil getInstance(Context context){
		if(mCommUtil == null){
			mCommUtil = new CommUtil(context);
		}
		return mCommUtil;
	}
	
	/**
	 * 获取shreprefrence对象。
	 * @param spname  文件名
	 * @param mode	      模式
	 * @return   sharprefrence对应的xml文件只有在调用 Edit.commit()时才会生成！
	 */
	public SharedPreferences getSharePre(String spname,int mode){
		return mContext.getSharedPreferences(spname, mode);
	}
	
	//在进程中去寻找当前APP的信息，判断是否在前台运行
	public boolean isAppOnForeground() {
		ActivityManager activityManager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
		String packageName = mContext.getPackageName();
		List<RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
		if (appProcesses == null)
			return false;
		for (RunningAppProcessInfo appProcess : appProcesses) {
			if (appProcess.processName.equals(packageName)
					&& appProcess.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
				return true;
			}
		}
		return false;
	}
	
	
}
