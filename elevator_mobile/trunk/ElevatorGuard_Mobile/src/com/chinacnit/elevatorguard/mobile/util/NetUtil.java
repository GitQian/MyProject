package com.chinacnit.elevatorguard.mobile.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;

/**
 * 连接管理器(提供判断网络是否连接、当前网络状态是否为WIFI公共函数)
 * 
 * @author ssu
 * @date 2015-5-12 下午7:01:43
 */
public class NetUtil {
	private static final String TAG = "NetUtil";

	public static final int NO_NETWORK = 0;
	public static final int WIFI_NETWORK = 1;
	public static final int EDGE_NETWORK = 2;// edge means gprs
	public static final int G3_NETWORK = 3;

	public static int currentNetState = NO_NETWORK;// 记录当前连接的网络状态

	/**
	 * 判断当前网络状态是否为已连接状态.
	 * 
	 * @param context
	 * @return
	 * @author: ss
	 * @version: 2011-12-1 下午06:48:53
	 */
	public static boolean checkNet(Context mContext) {
		boolean connected = false;
		try {
			ConnectivityManager connectivityManager = (ConnectivityManager) mContext
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			if (connectivityManager != null) {
				NetworkInfo info = connectivityManager.getActiveNetworkInfo();
				if (info != null && info.isConnected()
						&& info.getState() == NetworkInfo.State.CONNECTED) {
					connected = true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			connected = false;
		}
		// CustomLog.v(TAG,
		// "Check whether your device is connected to a network:"
		// + connected);
		return connected;
	}

	/**
	 * 获取网络类型
	 * 
	 * @param mcontext
	 * @return
	 * @author: 龙小龙
	 * @version: 2012-3-5 下午05:51:33
	 */
	public static boolean isWifi(Context mcontext) {
		// 获取当前可用网络信息
		ConnectivityManager connMng = (ConnectivityManager) mcontext.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInf = connMng.getActiveNetworkInfo();
		// CustomLog.v(TAG, "Check whether your device is connected to wifi:"
		// + (netInf != null && "WIFI".equals(netInf.getTypeName())));
		// 如果当前是WIFI连接
		return netInf != null && "WIFI".equals(netInf.getTypeName());
	}

	/**
	 * 
	 * @param context
	 * @return 0(无网络)，1(wifi),2(gprs),3(3g)
	 */
	public static final int getSelfNetworkType(Context mContext) {
		ConnectivityManager connectivityManager = (ConnectivityManager) mContext
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
		NetworkInfo netInfo = connectivityManager
				.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		int netSubtype = -1;
		if (activeNetInfo != null) {
			netSubtype = activeNetInfo.getSubtype();
		}
		if (activeNetInfo != null && activeNetInfo.isConnected()) {
			if ("WIFI".equalsIgnoreCase(activeNetInfo.getTypeName())) {// wifi
				return WIFI_NETWORK;
			} else if (activeNetInfo.getTypeName() != null
					&& activeNetInfo.getTypeName().toLowerCase()
							.contains("mobile")) {// 3g,双卡手机有时为mobile2
				if (netSubtype == TelephonyManager.NETWORK_TYPE_UMTS
						|| netSubtype == TelephonyManager.NETWORK_TYPE_EVDO_0
						|| netSubtype == TelephonyManager.NETWORK_TYPE_EVDO_A
						|| netSubtype == TelephonyManager.NETWORK_TYPE_EVDO_B
						|| netSubtype == TelephonyManager.NETWORK_TYPE_HSDPA
						|| netSubtype == TelephonyManager.NETWORK_TYPE_HSUPA
						|| netSubtype == TelephonyManager.NETWORK_TYPE_HSPA
						// 4.0系统 H+网络为15 TelephonyManager.NETWORK_TYPE_HSPAP
						|| netSubtype == 15) {
					return G3_NETWORK;
				} else {
					return EDGE_NETWORK;
				}
			}

		}

		if (netInfo != null) {
			// CustomLog.v("NetworkMonitor", netInfo.getTypeName());
		}

		return NO_NETWORK;// 网络未连接时当无网络
	}

	/**
	 * 有信网络是否可用 3g或者wifi
	 * 
	 * @param context
	 * @return 3g或者wifi已连接:true
	 */
	public static boolean youxin_net_available(Context mContext) {
		final int netType = getSelfNetworkType(mContext);
		switch (netType) {
		case WIFI_NETWORK:
		case G3_NETWORK:
			return true;
		default:
			return false;
		}
	}

	/**
	 * 网络是否可用 3g wifi gprs
	 * 
	 * @param context
	 * @return 可用 true，不可用false
	 */
	public static final boolean net_available(Context context) {
		final int netType = getSelfNetworkType(context);
		switch (netType) {
		case WIFI_NETWORK:
		case G3_NETWORK:
		case EDGE_NETWORK:
			return true;
		default:
			return false;
		}
	}

	private static WifiManager.WifiLock mWifiLock = null;

	public static void lockWifi(Context context) {
		if (mWifiLock == null) {
			WifiManager wifiManager = (WifiManager) context
					.getSystemService(Context.WIFI_SERVICE);
			mWifiLock = wifiManager.createWifiLock("youxin");
			mWifiLock.setReferenceCounted(true);
		}
		if (!mWifiLock.isHeld()) {
			mWifiLock.acquire();
		}
	}

	public static void unLockWifi() {
		if (mWifiLock != null && mWifiLock.isHeld()) {
			mWifiLock.release();
		}
	}
}