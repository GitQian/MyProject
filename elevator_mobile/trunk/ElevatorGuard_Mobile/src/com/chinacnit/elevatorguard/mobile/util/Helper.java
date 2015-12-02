package com.chinacnit.elevatorguard.mobile.util;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.drawable.Drawable;
import android.graphics.Matrix;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.provider.Settings.Secure;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.chinacnit.elevatorguard.mobile.R;
import com.chinacnit.elevatorguard.mobile.application.ElevatorGuardApplication;
import com.chinacnit.elevatorguard.mobile.config.ConfigSettings;
import com.chinacnit.elevatorguard.mobile.ui.activity.MainActivity;
import com.chinacnit.elevatorguard.mobile.util.LogUtils.LogTag;

/**
 * 辅助类
 * 
 * @author ssu
 * @date 2015-5-12 下午5:33:40
 */
public class Helper {
	private static final LogTag TAG = LogUtils.getLogTag(
			Helper.class.getSimpleName(), true);

	private static final long DAY_MILLIS = 24 * 60 * 60 * 1000;
	private static String[] UNITE = new String[] { "B/S", "KB/S", "MB/S",
			"GB/S" };

	public static long getMillisFromStringDate(String date, String time) {
		String splitter = null;
		Calendar cal = Calendar.getInstance();

		try {
			if (date.contains("-")) {
				splitter = "-";
			} else if (date.contains("/")) {
				splitter = "/";
			}
			String[] dateArray = date.split(splitter);// YYYY/MM/DD
			int year = Integer.parseInt(dateArray[0]);
			int month = Integer.parseInt(dateArray[1]) - 1;// zero based
			int day = Integer.parseInt(dateArray[2]);
			String[] timeArray = time.split(":"); // HH:MM
			int hour = Integer.parseInt(timeArray[0]);
			int minute = Integer.parseInt(timeArray[1]);
			int seconds = 0;
			cal.set(year, month, day, hour, minute, seconds);
		} catch (Exception e) {
			LogUtils.e(TAG, "getMillisFromStringDate",
					"ERROR data and time string: " + date + " " + time);
		}

		return cal.getTimeInMillis();
	}

	public static long getMillisFromStringDate(String dateAndtime) {
		Calendar cal = Calendar.getInstance();

		try {
			String[] ss = dateAndtime.split(" ");
			String date = ss[0];
			String time = ss[1];

			String splitter = null;
			if (date.contains("-")) {
				splitter = "-";
			} else if (date.contains("/")) {
				splitter = "/";
			}
			String[] dateArray = date.split(splitter);// YYYY/MM/DD
			int year = Integer.parseInt(dateArray[0]);
			int month = Integer.parseInt(dateArray[1]) - 1;// zero based
			int day = Integer.parseInt(dateArray[2]);
			String[] timeArray = time.split(":"); // HH:MM
			int hour = Integer.parseInt(timeArray[0]);
			int minute = Integer.parseInt(timeArray[1]);
			int seconds = 0;
			cal.set(year, month, day, hour, minute, seconds);
		} catch (Exception e) {
			LogUtils.e(TAG, "getMillisFromStringDate",
					"ERROR data and time string: " + dateAndtime);
		}

		return cal.getTimeInMillis();
	}

	public static long getMillisFromStringTime(String time) {
		Calendar cal = Calendar.getInstance();
		String[] timeArray = time.split(":"); // HH:MM
		int hour = Integer.parseInt(timeArray[0]);
		int minute = Integer.parseInt(timeArray[1]);
		cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
				cal.get(Calendar.DAY_OF_MONTH), hour, minute, 0);
		return (long) (cal.getTimeInMillis() * 0.001) * 1000;
	}

	public static long getMondayMillisFromStringDate(String date) {
		String splitter = null;
		Calendar cal = Calendar.getInstance();
		try {
			if (date.contains("-")) {
				splitter = "-";
			} else if (date.contains("/")) {
				splitter = "/";
			}
			String[] dateArray = date.split(splitter);// YYYY/MM/DD
			int year = Integer.parseInt(dateArray[0]);
			int month = Integer.parseInt(dateArray[1]) - 1;// zero based
			int day = Integer.parseInt(dateArray[2]);
			cal.set(year, month, day, 0, 0, 0);
		} catch (Exception e) {
			LogUtils.e(TAG, "getMondayMillisFromStringDate",
					"ERROR date string: " + date);
		}

		long time0 = cal.getTimeInMillis();
		int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);

		return time0 - (dayOfWeek - 1) * DAY_MILLIS;
	}

	/**
	 * 获取今天是一周第几天
	 * 
	 * @return 今天是一周第几天（周一为一周第一天）
	 */
	public static int getTodayInWeek() {
		Calendar cal = Calendar.getInstance();
		int today = cal.get(Calendar.DAY_OF_WEEK) - 1;
		if (today == 0) {
			today = 7;
		}
		return today;
	}

	/**
	 * 这是哪一年
	 * 
	 * @return
	 */
	public static int getThisYear() {
		Calendar cal = Calendar.getInstance();
		return cal.get(Calendar.YEAR);
	}

	/**
	 * 这是几月
	 * 
	 * @return 月份
	 */
	public static int getThisMonth() {
		Calendar cal = Calendar.getInstance();
		return cal.get(Calendar.MONTH) + 1;
	}

	/**
	 * 获取今天多少号
	 * 
	 * @return 日期
	 */
	public static int getTodayInMonth() {
		Calendar cal = Calendar.getInstance();
		return cal.get(Calendar.DATE);
	}

	/**
	 * 获取本月第几周
	 * 
	 * @return 第几周
	 */
	public static int getWeekInMonth() {
		Calendar cal = Calendar.getInstance();
		return cal.get(Calendar.WEEK_OF_MONTH);
	}

	/**
	 * 获取本月最后一天
	 * 
	 * @return 本月最后一天
	 */
	public static int getLastDayInMonth() {
		Calendar cal = Calendar.getInstance();
		int month0 = cal.get(Calendar.MONTH);
		System.out.println("month0: " + month0);
		cal.set(Calendar.MONTH, month0 + 1);
		cal.set(Calendar.DATE, 0);
		return cal.get(Calendar.DATE);
	}

	/**
	 * 获取本月最后一周
	 * 
	 * @return 本月最后一周
	 */
	public static int getLastWeekInMonth() {
		int lastDayInMonth = getLastDayInMonth();
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.DATE, lastDayInMonth);
		int weekInMonth = cal.get(Calendar.WEEK_OF_MONTH);
		return weekInMonth;
	}

	/**
	 * check the application internet connectivity
	 * 
	 * @param context
	 * @return true if the application has internet connectivity, false
	 *         otherwise
	 */
	public static boolean hasInternetConnection() {
		ConnectivityManager connectivityManager = (ConnectivityManager) ElevatorGuardApplication
				.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
		if (networkInfo != null && networkInfo.isConnected()) {
			return true;
		} else if (networkInfo == null) {
			return false;
		} else if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.DISCONNECTED
				|| connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.DISCONNECTED) {
			return false;
		} else {
			return false;
		}
	}

	/**
	 * Monitor wifi is open
	 * 
	 * @return true if wifi is enable and false otherwise.
	 */
	public static boolean isWifiEnable() {
		boolean isEnable = false;
		WifiManager wm = (WifiManager) ElevatorGuardApplication.getInstance().getSystemService(Context.WIFI_SERVICE);
		isEnable = wm.isWifiEnabled();
		return isEnable;
	}

	/**
	 * Checks if device has Adobe Flash Player installed
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isFlashInstalled(Context context) {
		PackageManager pm = context.getPackageManager();
		ApplicationInfo ai;
		boolean isFlashInstalled = false;
		// Check if Adobe Flash Player is installed
		try {
			ai = pm.getApplicationInfo("com.adobe.flashplayer", 0);
			if (ai != null) {
				isFlashInstalled = true;
			}
		} catch (PackageManager.NameNotFoundException e) {
			isFlashInstalled = false;
		}
		return isFlashInstalled;
	}

	/**
	 * Returns the filename of this part file
	 * 
	 * @param fileName
	 *            the name of the file
	 * @param packNumber
	 *            the number of parts
	 * @param index
	 *            the part number
	 * @return (e.g. file.avi.20-00003.part)
	 */
	public static String getPartFileName(String fileName, int packNumber,
			int index) {
		String path = fileName + "." + packNumber + "-"
				+ String.format("%05d", index) + ".part";
		return path;
	}

	/**
	 * Gets the name of the file excluding the extension
	 * 
	 * @param fileName
	 *            the filename including extension (e.g. the_one_file.avi)
	 * @return the file without file extension (e.g. the_one_file)
	 */
	public static String getNameWithoutExtension(String fileName) {
		return fileName.substring(0, fileName.lastIndexOf('.'));
	}

	public static String getFileNameByPath(String filePath) {
		return filePath.substring(filePath.lastIndexOf("/") + 1);
	}

	/**
	 * Returns an integer value from a byte array. Be careful of int
	 * overflow.(Max 4 bytes. Java int is 32 bits)
	 * 
	 * @param buff
	 *            the byte array
	 * @return the integer value.
	 */
	public static int getIntFromByteArray(byte[] buff) {
		int value = 0;
		for (int i = 0; i < buff.length; i++) {
			value += (buff[i] & 0xff) << (8 * i);
		}
		return value;
	}

	/**
	 * Gets the hexadecimal representation of this byte array
	 * 
	 * @param arr
	 *            the byte array
	 * @return hexadecimal representation
	 */
	public static String getStringFromBytes(byte[] arr) {
		String result = "";
		for (int i = 0; i < arr.length; i++) {
			result += Integer.toString((arr[i] & 0xff) + 0x100, 16)
					.substring(1);
		}
		return result;
	}

	/**
	 * 获取Android Mac地址或设备ID
	 * 
	 * @param context
	 *            Context
	 * @return 12位Mac地址或16位的设备ID
	 */
	public static String getMacAddress() {
		Context context = ElevatorGuardApplication.getInstance();
		String macAddress = Secure.getString(context.getContentResolver(), Secure.ANDROID_ID).toUpperCase();
		WifiManager wifiMgr = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		if (wifiMgr != null) {
			WifiInfo info = wifiMgr.getConnectionInfo();
			String s = info.getMacAddress();
			if (!TextUtils.isEmpty(s)) {
				macAddress = s.replace(":", "");
			} else {
				return macAddress;
			}
			LogUtils.w(TAG, "getMacAddress", macAddress);
		}
		return macAddress;
	}

	/*
	 * Load file content to String
	 */
	public static String loadFileAsString(String filePath)
			throws java.io.IOException {
		StringBuffer fileData = new StringBuffer(1000);
		BufferedReader reader = new BufferedReader(new FileReader(filePath));
		char[] buf = new char[1024];
		int numRead = 0;
		while ((numRead = reader.read(buf)) != -1) {
			String readData = String.valueOf(buf, 0, numRead);
			fileData.append(readData);
		}
		reader.close();
		return fileData.toString();
	}

	/*
	 * Get the STB MacAddress
	 */
	public static String getSTBMacAddress() {
		try {
			return loadFileAsString("/sys/class/net/eth0/address")
					.toUpperCase().substring(0, 17);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 获得本地图片Bitmap
	 * 
	 * @param
	 * @author: ssu
	 * @date: 2015-4-29 上午10:17:41
	 */
	public static Bitmap getBitmapWithLocalPicture(String filePath,
			int viewWidth, int viewHeight) {
		long l1 = Helper.uptimeMillis();
		if (null == filePath) {
			LogUtils.d(TAG, "getBitmapWithScale ", "file path is null");
			return getBitmapFromResourceWithScale(ElevatorGuardApplication.getInstance()
					.getResources(), R.drawable.no_photo, viewWidth, viewHeight);
		}
		Bitmap bitmap = null;
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inJustDecodeBounds = true;
		opt.inPreferredConfig = Config.RGB_565;
		opt.inPurgeable = true;
		opt.inPreferQualityOverSpeed = false;
		opt.inDensity = (int) ConfigSettings.SCALED_DENSITY;
		opt.inTargetDensity = (int) ConfigSettings.SCALED_DENSITY;
		BitmapFactory.decodeFile(filePath, opt);
		int requiredScale = calculateInSampleSize(opt, viewWidth, viewHeight);
		long l3 = Helper.uptimeMillis();
		LogUtils.timeConsume(TAG, "decode bitmap scale: " + requiredScale, l3
				- l1);
		opt.inJustDecodeBounds = false;
		opt.inSampleSize = requiredScale;
		bitmap = BitmapFactory.decodeFile(filePath, opt);
		return bitmap;
	}

	/**
	 * get sub folder name
	 * 
	 * @param filePath
	 *            detailed path
	 * @return sub folder name
	 */
	private static String getFolderNameByPath(String filePath) {
		if (null == filePath) {
			return null;
		}
		String subFilePath = filePath.substring(0, filePath.lastIndexOf("/"));
		String subFolderName = subFilePath.substring(
				subFilePath.lastIndexOf("/") + 1, subFilePath.length());
		return subFolderName;
	}

	public static int calculateInSampleSize(Options opts, int reqWidth,
			int reqHeight) {
		final int height = opts.outHeight;
		final int width = opts.outWidth;
		int inSampleSize = 1;
		if (reqWidth == 0 && reqHeight > 0) {
			inSampleSize = Math.round((float) height / (float) reqHeight);
		} else if (reqWidth > 0 && reqHeight == 0) {
			inSampleSize = Math.round((float) width / (float) reqWidth);
		} else if (reqWidth == 0 && reqHeight == 0) {
			inSampleSize = 1;
		} else if (height > reqHeight || width > reqWidth) {
			if (width > height) {
				inSampleSize = Math.round((float) height / (float) reqHeight);
			} else {
				inSampleSize = Math.round((float) width / (float) reqWidth);
			}

			// This offers some additional logic in case the image has a strange
			// aspect ratio. For example, a panorama may have a much larger
			// width than height. In these cases the total pixels might still
			// end up being too large to fit comfortably in memory, so we should
			// be more aggressive with sample down the image (=larger
			// inSampleSize).

			final float totalPixels = width * height;

			// Anything more than 2x the requested pixels we'll sample down
			// further.
			final float totalReqPixelsCap = reqWidth * reqHeight * 2;

			while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
				inSampleSize++;
			}
		}

		return inSampleSize;
	}

	/**
	 * This method constructs a scaled bitmap if it is the case. We check if the
	 * view in which the picture is set is smaller than the original image ,
	 * than we should resize it. The resize ratio is an integer and is equal to
	 * the sqrt(original_image_size/view_size). The scaled image is saved to be
	 * reused later. (the original version is never deleted or modified)
	 * 
	 * @param fileName
	 *            the original file name
	 * @param viewWidth
	 *            the view width
	 * @param viewHeight
	 *            the view height
	 * @return the original bitmap or a scaled version of it if the view is much
	 *         smaller than the original image.
	 */
	public static Bitmap getBitmapFromResourceWithScale(Resources res,
			int resId, int viewWidth, int viewHeight) {
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inJustDecodeBounds = true;
		BitmapFactory.decodeResource(res, resId);

		int requiredScale = calculateInSampleSize(opt, viewWidth, viewHeight);
		opt.inJustDecodeBounds = false;
		opt.inSampleSize = requiredScale;
		opt.inPreferredConfig = Config.RGB_565;
		opt.inPurgeable = true;
		opt.inPreferQualityOverSpeed = false;
		opt.inDensity = (int) ConfigSettings.SCALED_DENSITY;
		opt.inTargetDensity = (int) ConfigSettings.SCALED_DENSITY;
		return BitmapFactory.decodeResource(res, resId);
	}

	/**
	 * This method calculates the Greatest common divisor for an array of
	 * numbers
	 * 
	 * @param numberList
	 *            the list of numbers
	 * @return the GCD of those numbers
	 */
	public static int getGCD(ArrayList<Integer> numberList) {
		if (numberList.size() == 2) {
			return getGCD(numberList.get(0), numberList.get(1));
		} else {
			return getGCD(numberList.remove(0), numberList.remove(0),
					numberList);
		}
	}

	private static int getGCD(int a, int b, ArrayList<Integer> numberList) {
		int gcd = getGCD(a, b);
		if (numberList.size() == 1) {
			return getGCD(gcd, numberList.get(0));
		} else if (numberList.size() == 0) {
			return gcd;
		} else {
			return getGCD(gcd, numberList.remove(0), numberList);
		}
	}

	public static int getGCD(int a, int b) {
		if (b == 0)
			return a;
		return getGCD(b, a % b);
	}

	/**
	 * Converts dp to px.
	 * 
	 * @param dp
	 *            The dp value to be converted
	 * @return the size in pixels
	 */
	public static float pxFromDp(float dp) {
		final float scale = ElevatorGuardApplication.getInstance().getResources()
				.getDisplayMetrics().density;
		return dp * scale;
	}

	public static void toastLong(Context ctx, CharSequence str) {
		Toast.makeText(ctx, str, Toast.LENGTH_LONG).show();
	}

	public static void toastShort(final CharSequence str) {
		Activity a = MainActivity.getInstance();
		if (a != null) {
			a.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					Toast.makeText(MainActivity.getInstance(), str,
							Toast.LENGTH_SHORT).show();
				}
			});
		}
	}

	/**
	 * Clean target Folder .
	 * 
	 * @param dir
	 *            Target directory that need to be clean .
	 * @return If clean action success , then return success .
	 */
	public static boolean clsDir(File dir) {
		if (dir.exists() && dir.isDirectory()) {
			if (dir.listFiles() != null) {
				for (File file : dir.listFiles()) {
					if (file.isDirectory()) {
						clsDir(file);
					} else if (file.isFile()) {
						file.delete();
					}
				}
				if (dir.listFiles() == null) {
					dir.delete();
				}
				return true;
			} else {
				dir.delete();
				return true;
			}
		} else {
			return false;
		}
	}

	/**
	 * 更新当前时间（毫秒）
	 * 
	 * @return long（毫秒）
	 */
	public static final long uptimeMillis() {
		return android.os.SystemClock.uptimeMillis();
	}

	/**
	 * 获取今天日期字符串
	 * 
	 * @return yyyy_MM_dd
	 */
	public static String getToday() {
		return new SimpleDateFormat("yyyy_MM_dd", ConfigSettings.SYSTEM_LOCALE)
				.format(new Date());
	}

	/**
	 * 获取今年字符串
	 * 
	 * @return yyyy
	 */
	public static String getYear() {
		return new SimpleDateFormat("yyyy", ConfigSettings.SYSTEM_LOCALE)
				.format(new Date());
	}

	public static String getTime(long millis) {
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
				ConfigSettings.SYSTEM_LOCALE).format(new Date(millis));
	}

	/**
	 * 同步前要把当前北京时间东八区转换成标准时间
	 * 
	 * @param time
	 *            "2013-04-10 13:13:50"
	 * @return 少8个小时的时间串 2013-04-10 05:13:50
	 */
	public static String getSyncTime(String time) {

		String s = null;
		try {
			SimpleDateFormat formatter = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss", ConfigSettings.SYSTEM_LOCALE);
			Date d = (Date) formatter.parseObject(time);
			long after = d.getTime() - 8 * 60 * 60 * 1000;
			d = new Date(after);
			s = formatter.format(d);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return s;
	}

	/**
	 * 同步前要把当前服务器时间转换成标准时间
	 * 
	 * @param time
	 * @return
	 */
	public static String getSyncTimeByServerTime(String time) {
		String s = null;
		try {
			SimpleDateFormat formatter1 = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss");
			SimpleDateFormat formatter2 = new SimpleDateFormat(
					"yyyy-MM-dd'T'HH:mm:ss.SSSz");
			Date d = (Date) formatter2.parse(time);
			s = formatter1.format(d);
		} catch (Exception e) {
			LogUtils.d(TAG, "getSyncTimeByServerTime", e);
		}
		return s;
	}

	/**
	 * 获取当前时间字符串
	 * 
	 * @return yyyy-MM-dd HH:mm:ss
	 */
	public static String getTime() {
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
				ConfigSettings.SYSTEM_LOCALE).format(new Date());
	}

	public static long getTime(String time) {
		long millis = -1;
		try {
			millis = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSz").parse(
					time).getTime();
		} catch (ParseException e) {
		}

		return millis;
	}

	/**
	 * 返回当前系统时间字符串
	 * 
	 * @return yyyy_MM_dd_HH_mm_ss
	 */
	public static String getNowString() {
		return new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss",
				ConfigSettings.SYSTEM_LOCALE).format(new Date());
	}

	/**
	 * 获取layout的pid和version字符串数组
	 * 
	 * @param filename
	 *            layout文件名
	 * @return String[] | null（layout文件名不对）
	 */
	public static String[] spliteFilePidAndVersion(String filename) {
		// layout_628_8_.xml
		String[] s1 = filename.split("_");
		if (null == s1 || s1.length != 4) {
			return null;
		}

		String[] s2 = new String[2];
		s2[0] = s1[1]; // pid
		s2[1] = s1[2]; // version

		return s2;
	}

	/**
	 * 根据url获取主机地址
	 * 
	 * @param url
	 *            ftp文件url
	 * @return 主机地址 192.168.3.144
	 */
	public static String getHostByUrl(String url) {
		String s = null;
		URL u = null;
		try {
			u = new URL(url);
			s = u.getHost();
			u = null;
		} catch (Exception e) {
		} finally {
			u = null;
		}

		return s;
	}

	/**
	 * 根据url获取端口号（默认为21）
	 * 
	 * @param url
	 *            ftp url
	 * @return 端口号（默认21）
	 */
	public static int getPortByUrl(String url) {
		int port = 21;
		URL u = null;
		try {
			u = new URL(url);
			port = u.getPort();
			u = null;
			// 默认 ftp 端口21
			if (port == -1)
				port = 21;
		} catch (Exception e) {
		} finally {
			u = null;
		}

		return port;
	}

	/**
	 * 根据url获取文件路径（不包含host和port）
	 * 
	 * @param url
	 *            ftp url
	 * @return 文件路径 /Config/2/394/LayoutFiles.xml
	 */
	public static String getFilePathByUrl(String url) {
		String s = null;
		URL u = null;
		try {
			u = new URL(url);
			s = u.getPath();
			u = null;
		} catch (Exception e) {
		} finally {
			u = null;
		}

		return s;
	}

	/**
	 * 心跳消息里传入的版本信息计算版本号比大小
	 * 
	 * @param version
	 *            2.2.2类似的字符串
	 * @return float数字 2.2
	 */
	public static float getApkVersion(String version) {
		float v = 0f;
		String[] s = version.split("\\.");
		try {
			v = Float.parseFloat(s[0] + "." + s[1]);
		} catch (Exception e) {
		}

		return v;
	}

	public static String getMD5(String filePath) {
		long l1 = uptimeMillis();
		String md5 = null;
		try {

			MessageDigest md = MessageDigest.getInstance("MD5");
			int bufferSize = 256 * 1024;
			File file = new File(filePath);

			if (file.exists()) {

				FileInputStream fis = null;

				fis = new FileInputStream(file);

				byte[] buffer = new byte[bufferSize];
				int length = 0;
				while ((length = fis.read(buffer)) != -1) {
					md.update(buffer, 0, length);
				}
				fis.close();

				BigInteger bi = new BigInteger(1, md.digest());
				md5 = bi.toString(16).toUpperCase();
			}
		} catch (NoSuchAlgorithmException e) {
			LogUtils.e(TAG, "getMD5", e);
		} catch (IOException e) {
			LogUtils.e(TAG, "getMD5", e);
		}
		long l2 = uptimeMillis();
		LogUtils.timeConsume(TAG, "getMD5: " + filePath, l2 - l1);

		return md5;
	}

	/**
	 * get system max volume value
	 * 
	 * @return max volume value
	 */
	public static int getSystemMaxVolume() {
		return ((AudioManager) ElevatorGuardApplication.getInstance().getSystemService(
				Context.AUDIO_SERVICE))
				.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
	}

	/**
	 * Return a localized string from the application's package's default string
	 * table.
	 * 
	 * @param resId
	 *            Resource id for the string
	 */
	public final static String getString(int resId) {
		return ElevatorGuardApplication.getInstance().getResources().getString(resId);
	}

	/**
	 * zoom dial Image
	 * 
	 * @param bitmap
	 *            source bitmap
	 * @param width
	 *            zoom width
	 * @param height
	 *            zoom height
	 * @return zoom bitmap
	 */
	public static Bitmap zoomImage(Bitmap bitmap, float width, float height) {
		float xBig = ((float) width / bitmap.getWidth());
		float yBig = ((float) height / bitmap.getHeight());
		Matrix matrix = new Matrix();
		matrix.postScale(xBig, yBig);
		Bitmap resizeBmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
				bitmap.getHeight(), matrix, true);
		return resizeBmp;
	}

	/**
	 * 获取本机IP地址
	 * 
	 * @return ip地址
	 */
	public static String getLocalIpAddress() {
		String ipAddress = null;
		try {
			List<NetworkInterface> interfaces = Collections
					.list(NetworkInterface.getNetworkInterfaces());
			for (NetworkInterface iface : interfaces) {
				if (iface.getDisplayName().equals("eth0")) {
					List<InetAddress> addresses = Collections.list(iface
							.getInetAddresses());
					for (InetAddress address : addresses) {
						if (address instanceof Inet4Address) {
							ipAddress = address.getHostAddress();
						}
					}
				} else if (iface.getDisplayName().equals("wlan0")) {
					List<InetAddress> addresses = Collections.list(iface
							.getInetAddresses());
					for (InetAddress address : addresses) {
						if (address instanceof Inet4Address) {
							ipAddress = address.getHostAddress();
						}
					}
				}
			}
		} catch (SocketException e) {
			e.printStackTrace();
		}
		return ipAddress;
	}

	/**
	 * 算传输速度
	 * 
	 * @param speed
	 *            B/S
	 * @return 转换成 "B/S", "KB/S", "MB/S", "GB/S"
	 */
	public static String getSpeed(int speed) {
		float i = speed;
		int j = 0;
		boolean flag = true;
		String s;
		while (flag) {
			if (i >= 1000) {
				i = i / 1024;
				j++;
			} else {
				flag = false;
			}
			if (j == UNITE.length - 1) {
				flag = false;
			}
		}
		i = Math.round(i * 10);
		if (i % 10 == 0) {
			i = i / 10;
			s = (int) i + " " + UNITE[j];
		} else {
			i = i / 10;
			s = i + " " + UNITE[j];
		}
		return s;
	}

	/**
	 * 获取当前的时分，并且转换成毫秒
	 * 
	 * @return
	 */
	public static long getCurrentTimeHM() {
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
			return dateFormat.parse(dateFormat.format(new Date())).getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * 根据time获取long
	 * 
	 * @param time
	 *            时间 08:11
	 * @return
	 * 
	 */
	public static long getTimeLongToTime(String time) {
		try {
			Log.d("Helper", "getTimeLongToTime time:" + time);
			SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
			return dateFormat.parse(time).getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * split time string
	 * 
	 * @param time
	 *            format "1,0,0,hour,minute"
	 * @return hour:minute
	 */
	public static String splitString(String time) {
		String times[] = time.split(":");
		int hourLength = times[0].length();
		int minLength = times[1].length();
		String hourTime = times[0];
		String minuteTime = times[1];
		if (1 == hourLength) {
			hourTime = "0" + hourTime;
		}
		if (1 == minLength) {
			minuteTime = "0" + minuteTime;
		}

		return hourTime + ":" + minuteTime;
	}

	/**
	 * Bitmap转换成byte[]
	 * 
	 * @param bitmap
	 *            Bitmap 对象
	 * @return byte[]
	 */
	public static byte[] bitmapToByteArray(Bitmap bitmap) {
		if (null == bitmap) {
			return null;
		}
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		try {
			bitmap.compress(CompressFormat.PNG, 100, stream);
			stream.close();
			if (!bitmap.isRecycled()) {
				bitmap.recycle();
				bitmap = null;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return stream.toByteArray();
	}

	/**
	 * 根据报名判断是否安装了应用
	 * 
	 * @param cxt
	 *            Context对象
	 * @param packageName
	 *            应用程序的包名
	 * @return 返回值true表示有安装，false表示没有安装
	 */
	public static boolean ifInstallToPackage(Context cxt, String packageName) {
		if (null == cxt || null == packageName) {
			return false;
		}
		PackageInfo packageInfo = null;
		try {
			packageInfo = cxt.getPackageManager()
					.getPackageInfo(packageName, 0);
		} catch (NameNotFoundException e) {
			packageInfo = null;
			// e.printStackTrace();
		} finally {
			if (null == packageInfo) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 获取图片文件的信息，并逆时针旋转90度
	 * 
	 * @param bitmap
	 *            需要旋转的图片
	 * @param path
	 *            图片的路径
	 */
	public static Bitmap reviewPicRotate(Bitmap bitmap, String path) {
		// 定义矩阵对象
		Matrix matrix = new Matrix();
		// 缩放原图
		matrix.postScale(1f, 1f);
		// 向左旋转90度，参数为正则向右旋转
		matrix.postRotate(-90);
		// bmp.getWidth(), 500分别表示重绘后的位图宽高
		Bitmap dstbmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
				bitmap.getHeight(), matrix, true);
		return dstbmp;
	}

	public static Bitmap zoomDrawable(Drawable drawable, int w, int h) {
		int width = drawable.getIntrinsicWidth();
		int height = drawable.getIntrinsicHeight();
		Bitmap bm = drawableToBitmap(drawable, width, height);
		float scaleW = (float) ((LayoutUtil.getDisplayWidth() * 1.0) / (w * 1.0));
		float scaleH = (float) ((LayoutUtil.getDisplayHeight() * 1.0) / (h * 1.0));
		Matrix matrix = new Matrix(); // 创建操作图片用的Matrix对象
		float scale;
		if (scaleW >= scaleH) {
			scale = 1 / (scaleW);
		} else {
			scale = 1 / (scaleH);
		}
		matrix.postScale(scale, scale);
		Bitmap newbmp = Bitmap.createBitmap(bm, 0, 0, width, height, matrix,
				true); // 建立新的bitmap，其内容是对原bitmap的缩放后的图
		return newbmp; // 把bitmap转换成drawable并返回

	}

	public static Bitmap drawableToBitmap(Drawable drawable, int width,
			int height) {
		Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
				: Bitmap.Config.RGB_565; // 取drawable的颜色格式
		Bitmap bitmap = Bitmap.createBitmap(width, height, config); // 建立对应bitmap
		Canvas canvas = new Canvas(bitmap); // 建立对应bitmap的画布
		drawable.setBounds(0, 0, width, height);
		drawable.draw(canvas); // 把drawable内容画到画布中
		return bitmap;
	}
}
