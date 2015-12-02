package com.ztkj.tool;


import com.chinacnit.elevatorguard.mobile.R;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;

public class NotifyUtil {
	private static NotifyUtil mNotifyUtil;
	private NotificationManager mNM;
	private Context mContext;
	
	public NotifyUtil(Context context) {
		mContext = context;
		mNM = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
	}

	public static  NotifyUtil getInstance(Context context){
		if(mNotifyUtil == null){
			synchronized (NotifyUtil.class) {
				mNotifyUtil = new NotifyUtil(context);
			}
		}
		return mNotifyUtil;
	}
	
	/**发出一个通知*/
	public void notify(NotifyConfig notifyConfig){
		//新建状态栏通知
		Notification baseNF = new Notification();
		 
		//设置通知在状态栏显示的图标
		baseNF.icon = notifyConfig.icon == 0?R.drawable.logo:notifyConfig.icon;
		
		//通知时在状态栏显示的内容
		baseNF.tickerText = notifyConfig.tickerText;
		
		//通知的默认参数 DEFAULT_SOUND, DEFAULT_VIBRATE, DEFAULT_LIGHTS. 
		//如果要全部采用默认值, 用 DEFAULT_ALL.
		//此处采用默认声音
		if(notifyConfig.isLights){
			baseNF.defaults = Notification.DEFAULT_LIGHTS;
		}
		if(notifyConfig.isSound){
			baseNF.defaults |= Notification.DEFAULT_SOUND;
		}
		if(notifyConfig.isVibrate){
			baseNF.defaults |= Notification.DEFAULT_VIBRATE;
		}
//		baseNF.defaults = Notification.DEFAULT_ALL;
		
		 //让声音、振动无限循环，直到用户响应  
//        baseNF.flags |= Notification.FLAG_INSISTENT;  
        //通知被点击后，自动消失  
        baseNF.flags |= Notification.FLAG_AUTO_CANCEL;  
        //点击'Clear'时，不清楚该通知(QQ的通知无法清除，就是用的这个)  
//        baseNF.flags |= Notification.FLAG_NO_CLEAR;  
		
		//第二个参数 ：下拉状态栏时显示的消息标题 expanded message title
		//第三个参数：下拉状态栏时显示的消息内容 expanded message text
		//第四个参数：点击该通知时执行页面跳转
		baseNF.setLatestEventInfo(mContext, notifyConfig.contentTitle, notifyConfig.contentText, notifyConfig.pd);
		
		//发出状态栏通知
		//The first parameter is the unique ID for the Notification 
		// and the second is the Notification object.
		mNM.notify(notifyConfig.id, baseNF);
	}
	
	/**通知的配置信息*/
	public static class NotifyConfig{
		public int id;
		public int icon;
		public String tickerText;
		public String contentTitle ;
		public String contentText ;
		public PendingIntent contentIntent ;
		public boolean isSound;
		public boolean isVibrate;
		public boolean isLights;
		public PendingIntent pd;
	}
}

