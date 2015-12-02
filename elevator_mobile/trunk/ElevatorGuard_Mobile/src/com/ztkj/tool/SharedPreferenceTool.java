package com.ztkj.tool;



import com.chinacnit.elevatorguard.mobile.application.ElevatorGuardApplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SharedPreferenceTool {
	public static long getUid() {
		SharedPreferences preferences = ElevatorGuardApplication.getInstance()
				.getSharedPreferences("userinfo", Context.MODE_PRIVATE);
		return preferences.getLong("uid", -1);
	}

	public static void putUid(long uid) {
		SharedPreferences preferences = ElevatorGuardApplication.getInstance().getSharedPreferences("userinfo", Context.MODE_PRIVATE);
		Editor editor = preferences.edit();

		editor.putLong("uid", uid);
		editor.commit();
	}
	public static String getPwd() {
		SharedPreferences preferences = ElevatorGuardApplication.getInstance()
				.getSharedPreferences("userinfo", Context.MODE_PRIVATE);
		return preferences.getString("pwd", "");
	}
	
	public static void putPwd(String pwd) {
		SharedPreferences preferences = ElevatorGuardApplication.getInstance()
				.getSharedPreferences("userinfo", Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		
		editor.putString("pwd", pwd);
		editor.commit();
	}
	public static String getMsgNewCount() {
		SharedPreferences preferences = ElevatorGuardApplication.getInstance()
				.getSharedPreferences("countMsg", Context.MODE_PRIVATE);
		return preferences.getString("count", "0");
	}
	
	public static void putMsgNewCount(String count) {
		SharedPreferences preferences = ElevatorGuardApplication.getInstance()
				.getSharedPreferences("countMsg", Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		
		editor.putString("count", count);
		editor.commit();
	}
	/**
	 * 0=未登录，1=登录
	 * @param loginState
	 */
	public static int getLoginState() {
		SharedPreferences preferences = ElevatorGuardApplication.getInstance()
				.getSharedPreferences("userinfo", Context.MODE_PRIVATE);
		return preferences.getInt("loginState", 0);
	}
	/**
	 * 0=未登录，1=登录
	 * @param loginState
	 */
	public static void putLoginsState(int loginState) {
		SharedPreferences preferences = ElevatorGuardApplication.getInstance()
				.getSharedPreferences("userinfo", Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		
		editor.putInt("loginState", loginState);
		editor.commit();
	}
}
