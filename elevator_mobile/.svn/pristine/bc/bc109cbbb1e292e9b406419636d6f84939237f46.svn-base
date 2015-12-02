package com.chinacnit.elevatorguard.core.util;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;

public class PackageUtil {

    public static ActivityInfo getActivityInfo(Activity activity) {
        ActivityInfo activityinfo = null;
        try {
            activityinfo = activity.getPackageManager().getActivityInfo(activity.getComponentName(), 0);
        } catch (NameNotFoundException e) {

        }
        return activityinfo;
    }

    public static PackageInfo getApkInfo(Context context, String archiveFilePath) {
        return context.getPackageManager().getPackageArchiveInfo(archiveFilePath, PackageManager.GET_META_DATA);
    }

    public static Bundle getAppMetaData(Context context) {
        Bundle bundle;
        try {
            bundle = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA).metaData;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
            bundle = null;
        }
        return bundle;
    }

    /**
     * 判断应用程序是否安装
     * @param context
     * @param packageName
     * @return
     */
    public static boolean isPackageExisted(Context context, String packageName) {
        boolean result = false;
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(packageName, 0);
            if (info != null)
                result = true;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return result;

    }

    /** 
     * 返回当前程序版本
     */
    public static String getAppVersionName(Context context) {
        String versionName = "";
        try {
            versionName = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (NameNotFoundException e) {
        }
        return versionName;
    }

}
