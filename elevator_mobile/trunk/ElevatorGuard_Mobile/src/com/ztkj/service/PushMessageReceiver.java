package com.ztkj.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.ztkj.service.YTZTService;
import com.ztkj.tool.SharedPreferenceTool;
import com.ztkj.tool.Tool;

/**
 * Push消息处理receiver
 */
public class PushMessageReceiver extends BroadcastReceiver {
	public static boolean isNetSuc=true;
	/** TAG to Log */

	// AlertDiaLog.Builder builder;
	/**
	 * @param context
	 *            Context
	 * @param intent
	 *            接收的intent
	 */
	@Override
	public void onReceive(final Context context, Intent intent) {
		long sid=SharedPreferenceTool.getUid();
		if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())){
			if(sid!=-1){
				Tool.startMyService(context);
			}
		}else if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())){
			//网络状态已经改变
			ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo info = connectivityManager.getActiveNetworkInfo();  
            if(info != null && info.isAvailable()) {
//                String name = info.getTypeName();
//                Tool.toastShow(context, "有可用网络"+name);
                isNetSuc=true;
                if(sid!=-1){
    				Tool.startMyService(context);
    			}
            } else {
            	YTZTService.isConn=false;
            	isNetSuc=false;
//            	Tool.toastShow(context, "没有有可用网络");
            }
		}
		
	}
}
