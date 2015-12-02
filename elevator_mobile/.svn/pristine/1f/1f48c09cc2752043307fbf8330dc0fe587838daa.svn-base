package com.ztkj.service;

import java.util.HashMap;
import java.util.Map;



import com.chinacnit.elevatorguard.mobile.ui.activity.LoginActivity;
import com.ztkj.tool.SharedPreferenceTool;
import com.ztkj.tool.Tool;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import asmack.bean.BaseResultInfoVo;
import asmack.utils.Common_SMACK;
import asmack.utils.ConnectionUtil;

public class YTZTService extends Service {
	private AlarmManager mAlarmManager = null;
	private PendingIntent mPendingIntent = null;
	private boolean isComplete=true;
	public static Boolean isConn=false;
	public static boolean isLoginOut=false;
	private String uid;
	private String pwd;
	private static final Map<String, LoginListener> listObservers = new HashMap<String, LoginListener>();
	public static void addLoginObserver(String key, LoginListener observer) {
		listObservers.put(key, observer);
	}

	public static void removeAttentionObserver(String key) {
		listObservers.remove(key);
	}
	/**
	 *state=0表示全新登录,state=1表示原来已登录 
	 */
	public static void notifyAllObserverSuc(int state) {
		for (Map.Entry<String, LoginListener> entry : listObservers.entrySet()) {
			entry.getValue().onLoginSuccess(state);
		}
	}

	public static void notifyAllObserverFailed(String result) {
		for (Map.Entry<String, LoginListener> entry : listObservers.entrySet()) {
			entry.getValue().onLoginFailed(result);
		}
	}

	@Override
	public void onCreate() {
		Intent intent = new Intent(getApplicationContext(), YTZTService.class);
		mAlarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
		mPendingIntent = PendingIntent.getService(this, 0, intent,
				Intent.FLAG_ACTIVITY_NEW_TASK);
		long now = System.currentTimeMillis();
		mAlarmManager.setInexactRepeating(AlarmManager.RTC, now, 60000,
				mPendingIntent);

		super.onCreate();
	}
	
	private void login(String username,String password){
		BaseResultInfoVo vologin = ConnectionUtil.getInstance(
		YTZTService.this).login(username, password/*"13360546408","123456"*/);
		if (vologin.getResultCode() == Common_SMACK.Result.FAILURE) {
			if("用户名或密码错误".equals(vologin.getMsg())){
				Log.e("=========", "用户名或密码错误");
//				notifyAllObserverFailed(vologin.getMsg());
//				Intent intent =new Intent(this,LoginActivity.class);
//				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//				startActivity(intent);
				isConn=false;
			}else{
				Log.e("=========", "登录失败");
				notifyAllObserverFailed(vologin.getMsg());
				isConn=false;
			}
		} else {
			Log.e("=========", "登录成功");
			notifyAllObserverSuc(0);
			isConn=true;
		}
	}
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.e("=========", "onStartCommand");
		if(SharedPreferenceTool.getLoginState()==0){
			notifyAllObserverFailed("请重新登录");
			return START_STICKY;
		}
//		uid=60220;
//		pwd="dfsefe";
		
		uid=SharedPreferenceTool.getUid()+"_50001";
		pwd=Tool.md5(SharedPreferenceTool.getPwd()).toUpperCase()+"_50001";
		
		Log.e("========>", uid+"*****"+pwd);
		
		if(Tool.isNullString(SharedPreferenceTool.getPwd())||SharedPreferenceTool.getUid()==-1){
			Tool.startActivity(this.getApplicationContext(), LoginActivity.class);
			isConn=false;
			notifyAllObserverFailed(null);
			return START_STICKY;
		}
		if(PushMessageReceiver.isNetSuc&&isComplete){
			new Thread() {
				public void run() {
					synchronized (isConn) {
						isComplete=false;
						if(isConn){
							Log.e("=========", "已登录");
							notifyAllObserverSuc(1);
						}else{
						Log.e("=========", "准备登录openfire");
							BaseResultInfoVo voregist = ConnectionUtil.getInstance(
									YTZTService.this).regist(uid, pwd/*"13360546408","123456"*/);
							if(voregist.getResultCode()==Common_SMACK.Result.FAILURE){
								if("isexist".equals(voregist.getMsg())){
									Log.e("=========", "注册时候已经有帐号");
									login(uid,pwd);
								}else{
									Log.e("=========", "注册失败");
									notifyAllObserverFailed("添加服务失败");
									isConn=false;
								}
							}else{
								login(uid,pwd);
							}
						}
						isComplete=true;
					}
				};

			}.start();
			return START_STICKY;
		}
		
		if(!isComplete){
			Log.e("=========", "未完成");
			return START_STICKY;
		}
		if(PushMessageReceiver.isNetSuc){
			isConn=false;
			notifyAllObserverFailed("暂无网络");
			return START_STICKY;
		}
		return START_STICKY;
	}
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void onDestroy() {
		Log.e("=========", "onDestroy");
		listObservers.clear();
		super.onDestroy();
	}
}
