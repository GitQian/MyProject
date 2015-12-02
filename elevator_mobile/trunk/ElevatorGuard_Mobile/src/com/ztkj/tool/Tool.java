package com.ztkj.tool;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Build;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.chinacnit.elevatorguard.mobile.R;
import com.ztkj.service.YTZTService;


public class Tool {
	private static Toast toast;

	/**
	 * 启动界面并且附加动画
	 * 
	 * @param nowActivity
	 * @param desClass
	 * @param category
	 */
	public static void startActivity(Context nowActivity, Class<?> desClass) {
		Intent intent = new Intent(nowActivity, desClass);
		nowActivity.startActivity(intent);
	}

	public static void startActivity(Activity nowActivity, Class<?> desClass,
			Bundle bundle) {
		if (bundle == null) {
			startActivity(nowActivity, desClass);
		} else {
			Intent intent = new Intent(nowActivity, desClass);
			intent.putExtra(Bundle.class.getName(), bundle);
			nowActivity.startActivity(intent);
		}
	}

	/**
	 * 启动Activity并消除上面的，kill myself also
	 * 
	 * @param nowActivity
	 * @param desClass
	 */
	public static void startActivityClearTop(Context nowActivity,
			Class<?> desClass) {
		Intent intent = new Intent(nowActivity, desClass);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		nowActivity.startActivity(intent);
	}

	/**
	 * 获取系统三个月之前的时间
	 * 
	 * @return
	 */
	public static String getThreeMonthBeforeTime() {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MONTH, -3);
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH) + 1;
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		return year + "-" + String.format("%02d", month) + "-"
				+ String.format("%02d", day);
	}

	/**
	 * 启动界面并且附加动画
	 * 
	 * @param nowActivity
	 * @param desClass
	 * @param category
	 */
	public static void startActivityWithAnim(Activity nowActivity,
			Class<?> desClass, int category) {
		startActivity(nowActivity, desClass);
		switch (category) {
		case Config.ENTER:
			nowActivity.overridePendingTransition(R.anim.activity_right_in,
					R.anim.activity_alpha_out);
			break;
		case Config.EXIT:
			nowActivity.overridePendingTransition(
					R.anim.activity_translucent_enter,
					R.anim.activity_translucent_exit);
			break;
		case Config.NONE:
			break;
		default:
			break;
		}
	}

	/**
	 * 退出动画
	 */
	public static void exitActivityWithAnim(Activity nowActivity, int category) {
		switch (category) {
		case Config.EXIT:
			nowActivity.overridePendingTransition(R.anim.activity_alpha_in,
					R.anim.activity_right_out);
			break;
		default:
			break;
		}
	}

	/**
	 * 显示与隐藏toast
	 * 
	 * @param context
	 * @param msg
	 */
	public static void toastShow(Context context, String msg) {
		if (toast == null) {
			toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
		} else {
			toast.setText(msg);
		}
		toast.show();
	}

	/**
	 * 获得系统时间
	 * 
	 * @return
	 */
	public static String getNowTime() {
		SimpleDateFormat tempDate = new SimpleDateFormat("yyyy-MM-dd" + " "
				+ "HH:mm:ss", Locale.getDefault());
		String datetime = tempDate.format(new java.util.Date());
		return datetime;
	}

	/**
	 * 隐藏输入法
	 */
	public static void hideInputKeyBroad(Activity activity) {
		if (activity.getCurrentFocus() == null) {
			return;
		}
		if (activity.getCurrentFocus().getWindowToken() != null) {
			InputMethodManager imm = (InputMethodManager) activity
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			if (imm.isActive()) {
				imm.hideSoftInputFromWindow(activity.getCurrentFocus()
						.getWindowToken(), 0);
			}
		}
	}

	private static final char HEX_DIGITS[] = { '0', '1', '2', '3', '4', '5',
			'6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

	public static String toHexString(byte[] b) { // String to byte
		StringBuilder sb = new StringBuilder(b.length * 2);
		for (int i = 0; i < b.length; i++) {
			sb.append(HEX_DIGITS[(b[i] & 0xf0) >>> 4]);
			sb.append(HEX_DIGITS[b[i] & 0x0f]);
		}
		return sb.toString();
	}

	public static String md5(String s) {
		try {
			// Create MD5 Hash
			MessageDigest digest = java.security.MessageDigest
					.getInstance("MD5");
			digest.update(s.getBytes());
			byte messageDigest[] = digest.digest();

			return toHexString(messageDigest);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		return "";
	}

	/**
	 * 获取appkey
	 */
	public static String getMetaValue(Context context, String metaKey) {
		Bundle metaData = null;
		String apiKey = null;
		if (context == null || metaKey == null) {
			return null;
		}
		try {
			ApplicationInfo ai = context.getPackageManager()
					.getApplicationInfo(context.getPackageName(),
							PackageManager.GET_META_DATA);
			if (null != ai) {
				metaData = ai.metaData;
			}
			if (null != metaData) {
				apiKey = metaData.getString(metaKey);
			}
		} catch (NameNotFoundException e) {

		}
		return apiKey;
	}

	/**
	 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
	 */
	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/**
	 * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
	 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	public static int dpToPx(Resources res, int dp) {
		return (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
				dp, res.getDisplayMetrics()) + 0.5f);
	}

	/**
	 * 功能：身份证的有效验证
	 * 
	 * @param IDStr
	 *            身份证号
	 * @return 有效：返回"" 无效：返回String信息
	 * @throws ParseException
	 */
	public static String IDCardValidate(String IDStr) throws ParseException {
		String errorInfo = "";// 记录错误信息
		String[] ValCodeArr = { "1", "0", "X", "9", "8", "7", "6", "5", "4",
				"3", "2" };
		String[] Wi = { "7", "9", "10", "5", "8", "4", "2", "1", "6", "3", "7",
				"9", "10", "5", "8", "4", "2" };
		String Ai = "";
		// ================ 号码的长度 15位或18位 ================
		if (IDStr.length() != 15 && IDStr.length() != 18) {
			errorInfo = "身份证号码长度应该为15位或18位";
			return errorInfo;
		}
		// =======================(end)========================

		// ================ 数字 除最后以为都为数字 ================
		if (IDStr.length() == 18) {
			Ai = IDStr.substring(0, 17);
		} else if (IDStr.length() == 15) {
			Ai = IDStr.substring(0, 6) + "19" + IDStr.substring(6, 15);
		}
		if (isNumeric(Ai) == false) {
			errorInfo = "身份证15位号码都应为数字 ; 18位号码除最后一位外，都应为数字";
			return errorInfo;
		}
		// =======================(end)========================

		// ================ 出生年月是否有效 ================
		String strYear = Ai.substring(6, 10);// 年份
		String strMonth = Ai.substring(10, 12);// 月份
		String strDay = Ai.substring(12, 14);// 月份
		if (isDataFormat(strYear + "-" + strMonth + "-" + strDay) == false) {
			errorInfo = "身份证生日无效";
			return errorInfo;
		}
		GregorianCalendar gc = new GregorianCalendar();
		SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd",
				Locale.getDefault());
		if ((gc.get(Calendar.YEAR) - Integer.parseInt(strYear)) > 150
				|| (gc.getTime().getTime() - s.parse(
						strYear + "-" + strMonth + "-" + strDay).getTime()) < 0) {
			errorInfo = "身份证生日不在有效范围";
			return errorInfo;
		}
		if (Integer.parseInt(strMonth) > 12 || Integer.parseInt(strMonth) == 0) {
			errorInfo = "身份证月份无效";
			return errorInfo;
		}
		if (Integer.parseInt(strDay) > 31 || Integer.parseInt(strDay) == 0) {
			errorInfo = "身份证日期无效";
			return errorInfo;
		}
		// =====================(end)=====================

		// ================ 地区码时候有效 ================
		Hashtable<String, String> h = GetAreaCode();
		if (h.get(Ai.substring(0, 2)) == null) {
			errorInfo = "身份证地区编码错误";
			return errorInfo;
		}
		// ==============================================

		// ================ 判断最后一位的值 ================
		int TotalmulAiWi = 0;
		for (int i = 0; i < 17; i++) {
			TotalmulAiWi = TotalmulAiWi
					+ Integer.parseInt(String.valueOf(Ai.charAt(i)))
					* Integer.parseInt(Wi[i]);
		}
		int modValue = TotalmulAiWi % 11;
		String strVerifyCode = ValCodeArr[modValue];
		Ai = Ai + strVerifyCode;

		if (IDStr.length() == 18) {
			if (Ai.equals(IDStr) == false) {
				errorInfo = "身份证无效，不是合法的身份证号码";
				return errorInfo;
			}
		} else {
			return "";
		}
		// =====================(end)=====================
		return "";
	}

	/**
	 * 功能：判断字符串是否为数字
	 * 
	 * @param str
	 * @return
	 */
	private static boolean isNumeric(String str) {
		Pattern pattern = Pattern.compile("[0-9]*");
		Matcher isNum = pattern.matcher(str);
		if (isNum.matches()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 功能：设置地区编码
	 * 
	 * @return Hashtable 对象
	 */
	private static Hashtable<String, String> GetAreaCode() {
		Hashtable<String, String> hashtable = new Hashtable<String, String>();
		hashtable.put("11", "北京");
		hashtable.put("12", "天津");
		hashtable.put("13", "河北");
		hashtable.put("14", "山西");
		hashtable.put("15", "内蒙古");
		hashtable.put("21", "辽宁");
		hashtable.put("22", "吉林");
		hashtable.put("23", "黑龙江");
		hashtable.put("31", "上海");
		hashtable.put("32", "江苏");
		hashtable.put("33", "浙江");
		hashtable.put("34", "安徽");
		hashtable.put("35", "福建");
		hashtable.put("36", "江西");
		hashtable.put("37", "山东");
		hashtable.put("41", "河南");
		hashtable.put("42", "湖北");
		hashtable.put("43", "湖南");
		hashtable.put("44", "广东");
		hashtable.put("45", "广西");
		hashtable.put("46", "海南");
		hashtable.put("50", "重庆");
		hashtable.put("51", "四川");
		hashtable.put("52", "贵州");
		hashtable.put("53", "云南");
		hashtable.put("54", "西藏");
		hashtable.put("61", "陕西");
		hashtable.put("62", "甘肃");
		hashtable.put("63", "青海");
		hashtable.put("64", "宁夏");
		hashtable.put("65", "新疆");
		hashtable.put("71", "台湾");
		hashtable.put("81", "香港");
		hashtable.put("82", "澳门");
		hashtable.put("91", "国外");
		return hashtable;
	}

	/**
	 * 验证日期字符串是否是YYYY-MM-DD格式
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isDataFormat(String str) {
		boolean flag = false;
		// String
		// regxStr="[1-9][0-9]{3}-[0-1][0-2]-((0[1-9])|([12][0-9])|(3[01]))";
		String regxStr = "^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))(\\s(((0?[0-9])|([1-2][0-3]))\\:([0-5]?[0-9])((\\s)|(\\:([0-5]?[0-9])))))?$";
		Pattern pattern1 = Pattern.compile(regxStr);
		Matcher isNo = pattern1.matcher(str);
		if (isNo.matches()) {
			flag = true;
		}
		return flag;
	}

	public static String StringNull(String src, String nullForResult) {
		if (src == null || src.equals("")) {
			return nullForResult;
		} else {
			return src;
		}
	}

	public static String dealString(String str) {
		if (str == null) {
			return "";
		} else {
			return str;
		}
	}

	public static int getVerCode(Context context) {
		int verCode = -1;
		try {
			verCode = context.getPackageManager().getPackageInfo(
					Config.PACKAGE, 0).versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return verCode;
	}

	public static String getVerName(Context context) {
		String verName = "";
		try {
			verName = context.getPackageManager().getPackageInfo(
					Config.PACKAGE, 0).versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return verName;

	}

	public static long string2Long(String str) {
		try {
			return Long.parseLong(str);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return 0;
	}

	/**
	 * 截取字符串前10位
	 * 
	 * @param strDate
	 * @return
	 */
	public static String dealOnlyDate(String strDate) {
		if (strDate == null) {
			return "暂无日期";
		}
		if (strDate.length() < 10) {
			return strDate;
		}
		return strDate.substring(0, 10);

	}

	public static String dealOnlyMD(String strDate) {
		if (strDate == null) {
			return "";
		}
		if (strDate.length() < 10) {
			return strDate;
		}
		return strDate.substring(5, 10);

	}

	/**
	 * 比较日期大小
	 * 
	 * @return 第一个值大于等于第二个值返回true,异常也返回true
	 */
	public static boolean compareDate(String DATE1, String DATE2) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			Date dt1 = df.parse(DATE1);
			Date dt2 = df.parse(DATE2);
			if (dt1.getTime() >= dt2.getTime()) {
				return true;
			} else if (dt1.getTime() < dt2.getTime()) {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * 判断字符串是否为空
	 */
	public static boolean isNullString(String str) {
		if (str == null || "".equals(str)) {
			return true;
		}
		return false;
	}

	/**
	 * 判断字符串是否为空
	 */
	public static boolean isNullList(List<?> list) {
		if (list == null || list.size() == 0) {
			return true;
		}
		return false;
	}

	/**
	 * 设置一句话的第几个字的颜色
	 */
	public static SpannableString setSelectTextColor(String content, int color,
			int start, int end) {
		if (isNullString(content)) {
			return null;
		}
		SpannableString sp = new SpannableString(content);
		sp.setSpan(new ForegroundColorSpan(color), start, end,
				Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
		return sp;
	}

	/**
	 * 保存一个整数至少有几位
	 */
	public static String keepIntCount(int value, int count) {
		String retrunValue = String.format("%0" + count + "d", value);
		return retrunValue;
	}

	/**
	 * 判断两个字符串是否相等
	 */
	public static boolean isEqualString(String str1, String str2) {
		if (str1 == null) {
			return str1 == str2;
		}
		return str1.equals(str2);
	}

	/**
	 * 比较两个字符串忽略顺序
	 * 
	 * @param str1
	 * @param str2
	 * @return
	 */
	public static boolean compareStringIgnoreOrder(String str1, String str2) {
		// mmmmmmmm.println(str1 + "=======" + str2);
		if (str1 == null || str2 == null) {
			return str1 == str2;
		}
		if (str1.length() == str2.length()) {
			for (int i = 0; i < str1.length(); i++) {
				if (!str2.contains(String.valueOf(str1.charAt(i)))) {
					return false;
				}
			}
			return true;
		}
		return false;
	}

	/**
	 * 获得当前日期（格式：2014年02月02日）
	 * 
	 * @param str1
	 * @param str2
	 * @return
	 */
	public static String getCurentDate() {
		SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy年MM月dd日");
		String date = sDateFormat.format(new java.util.Date());
		return date;
	}

	/**
	 * 获得当前时间（格式：yyyy-MM-dd hh:mm:ss ）
	 * 
	 * @param str1
	 * @param str2
	 * @return
	 */
	public static String getCurentDateForDb() {
		SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String date = sDateFormat.format(new java.util.Date());
		return date;
	}

	/**
	 * 获得当前时间（格式：去掉时分秒后的毫秒数）
	 * 
	 * @return
	 */
	public static long getCurentYMDTimeMillis() {
		Calendar cd = Calendar.getInstance();
		int year = cd.get(Calendar.YEAR);
		int month = cd.get(Calendar.MONTH);
		int day = cd.get(Calendar.DAY_OF_MONTH);
		cd.clear();
		cd.set(Calendar.YEAR, year);
		cd.set(Calendar.MONTH, month);
		cd.set(Calendar.DAY_OF_MONTH, day);
		long nowTime = cd.getTimeInMillis();
		return nowTime;
	}

	/**
	 * yyyy-MM-dd HH:mm:ss  24小时的时间制式。
	 * @return
	 */
	public static String getTime() {
		SimpleDateFormat sDateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		String date = sDateFormat.format(new java.util.Date());
		return date;
	}
	/**
	 * 获得当前时间（格式：yyyy-MM-dd hh:mm:ss ）
	 * 
	 * @param str1
	 * @param str2
	 * @return
	 */
	public static String getCurentTime() {
		SimpleDateFormat sDateFormat = new SimpleDateFormat(
				"yyyy-MM-dd hh:mm:ss");
		String date = sDateFormat.format(new java.util.Date());
		return date;
	}

	/**
	 * 获得当前时间（格式：yyyy-MM-dd hh:mm:ss ） 24小时制
	 * 
	 * @param str1
	 * @param str2
	 * @return
	 */
	public static String getCurentTime24() {
		SimpleDateFormat sDateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		String date = sDateFormat.format(new java.util.Date());
		return date;
	}

	/**
	 * 字符串转换到时间格式
	 * 
	 * @param dateStr
	 *            需要转换的字符串
	 * @return dateFormatStr 需要转换的字符串的时间格式
	 * @param formatStr
	 *            需要格式的目标字符串 举例 yyyyMMdd
	 * @return String 返回转换后的时间字符串
	 * @throws ParseException
	 *             转换异常
	 */
	public static String StringToDate(String dateStr, String dateFormatStr,
			String formatStr) {
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormatStr);
		Date date = null;
		try {
			date = sdf.parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		SimpleDateFormat s = new SimpleDateFormat(formatStr);
		return s.format(date);
	}

	/**
	 * 获得当前时间（格式：hh:mm）
	 * 
	 * @param str1
	 * @param str2
	 * @return
	 */
	public static String getCurentHourMin() {
		SimpleDateFormat sDateFormat = new SimpleDateFormat("hh:mm");
		String date = sDateFormat.format(new java.util.Date());
		return date;
	}

	/**
	 * 把秒转化为时间格式（00:11:12）
	 * 
	 * @param str1
	 * @param str2
	 * @return
	 */
	public static String parsSecondToTime(int time) {
		String timeStr = null;
		int hour = 0;
		int minute = 0;
		int second = 0;
		if (time <= 0)
			return "00:00:00";
		else {
			minute = time / 60;
			if (minute < 60) {
				second = time % 60;
				timeStr = "00:" + unitFormat(minute) + ":" + unitFormat(second);
			} else {
				hour = minute / 60;
				if (hour > 99)
					return "99:59:59";
				minute = minute % 60;
				second = time - hour * 3600 - minute * 60;
				timeStr = unitFormat(hour) + ":" + unitFormat(minute) + ":"
						+ unitFormat(second);
			}
		}
		return timeStr;
	}

	// /**
	// * 把秒转化为多少时，分，秒
	// *
	// * @param str1
	// * @param str2
	// * @return
	// */
	// public static int[] parsSecondToHMS(int time) {
	// int[] hms = new int[3];
	// String timeStr = parsSecondToTime(time);
	// String[] timeArray = timeStr.split(":");
	// return hms;
	// }

	public static String unitFormat(int i) {
		String retStr = null;
		if (i >= 0 && i < 10)
			retStr = "0" + Integer.toString(i);
		else
			retStr = "" + i;
		return retStr;
	}

	/**
	 * 保留四位小数
	 */
	public static float keepFloatCount(float value, int count) {
		BigDecimal b = new BigDecimal(value);
		float f1 = (float) b.setScale(count, BigDecimal.ROUND_HALF_UP)
				.doubleValue();
		return f1;
	}

	public static int getCount(Context context) {
		SharedPreferences preferences = context.getSharedPreferences(
				"countMsg", Context.MODE_PRIVATE);
		return preferences.getInt("count", 0);
	}

	/**
	 * 根据Asset下的文件路径找到图片，返回bitmap
	 */
	public static Bitmap getBitmapFromAsset(Context context, String imagePath) {
		Bitmap bitmap = null;
		if (Tool.isNullString(imagePath)) {
			return bitmap;
		}
		AssetManager assetManager = context.getAssets();
		InputStream is = null;
		try {
			is = assetManager.open(imagePath);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (is != null) {
			bitmap = BitmapFactory.decodeStream(is);
		}
		return bitmap;
	}

	/*
	 * 缩放图片
	 */
	public static Bitmap zoomImg(Bitmap bm, int newWidth, int newHeight) {
		if (bm == null) {
			return null;
		}
		// 获得图片的宽高
		int width = bm.getWidth();
		int height = bm.getHeight();
		// 计算缩放比例
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;
		// 取得想要缩放的matrix参数
		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);
		// 得到新的图片
		Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix,
				true);
		return newbm;
	}

	private static final double EARTH_RADIUS = 6378137.0;

	// 返回单位是米
	public static double getDistance(double longitude1, double latitude1,
			double longitude2, double latitude2) {
		double Lat1 = rad(latitude1);
		double Lat2 = rad(latitude2);
		double a = Lat1 - Lat2;
		double b = rad(longitude1) - rad(longitude2);
		double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
				+ Math.cos(Lat1) * Math.cos(Lat2)
				* Math.pow(Math.sin(b / 2), 2)));
		s = s * EARTH_RADIUS;
		s = Math.round(s * 10000) / 10000;
		BigDecimal bg = new BigDecimal(s / 1000);
		return bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	private static double rad(double d) {
		return d * Math.PI / 180.0;
	}

	public static ArrayList<Activity> activityList = new ArrayList<Activity>();

	public static void clearActivitys() {
		for (Activity activity : activityList) {
			activity.finish();
		}
	}

	public static boolean isInStrs(String str, String[] strs) {
		if (strs == null || str == null) {
			return false;
		}
		for (int i = 0; i < strs.length; i++) {
			if (str.equals(strs[i])) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 动态设置ListView的高度
	 * 
	 * @param listView
	 */
	public static void setListViewHeightBasedOnChildren(ListView listView) {
		if (listView == null)
			return;

		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			// pre-condition
			return;
		}

		int totalHeight = 0;
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);
	}

	public static String dealDateWithoutSecond(String strDate) {
		if (isNullString(strDate)) {
			return "";
		}
		if (strDate.length() < 16) {
			return strDate;
		}
		return strDate.substring(0, 16);
	}

	public static String dealParentheses(String str) {
		if (isNullString(str)) {
			return "";
		}
		return "(" + str + ")";
	}

	public static  byte[] decodeBitmap(String filePath) {
		// 第一次解析将inJustDecodeBounds设置为true，来获取图片大小
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filePath, options);
		options.inSampleSize = (int) ((options.outHeight * options.outWidth * 4) / (Runtime
				.getRuntime().maxMemory() / 20));
		// 使用获取到的inSampleSize值再次解析图片
		options.inJustDecodeBounds = false;
		
		Bitmap bitmap=BitmapFactory.decodeFile(filePath, options);
		if(getBitmapsize(bitmap)>1024*250){
			return dealImg(bitmap);
		}
		 ByteArrayOutputStream baos = new ByteArrayOutputStream();
		 bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		 
		 bitmap.recycle();
		 bitmap=null;
		 
		 return baos.toByteArray();
		
	}

	public static long getBitmapsize(Bitmap bitmap) {

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {
			return getHONEYCOMBBitmapsize(bitmap);
		}
		return bitmap.getRowBytes() * bitmap.getHeight();
	}
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
	public static long getHONEYCOMBBitmapsize(Bitmap bitmap){
		return bitmap.getByteCount();
	}
	public static byte[] dealImg(Bitmap image) {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		if (baos.toByteArray().length / 1024 > 1024) {// 判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出
			baos.reset();// 重置baos即清空baos
			image.compress(Bitmap.CompressFormat.JPEG, 50, baos);// 这里压缩50%，把压缩后的数据存放到baos中
		}

		image.recycle();
		image = null;

		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		// 开始读入图片，此时把options.inJustDecodeBounds 设回true了
		newOpts.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
		newOpts.inJustDecodeBounds = false;
		int w = newOpts.outWidth;
		int h = newOpts.outHeight;
		// 现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
		float hh = 800f;// 这里设置高度为800f
		float ww = 480f;// 这里设置宽度为480f
		// 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
		int be = 1;// be=1表示不缩放
		if (w > h && w > ww) {// 如果宽度大的话根据宽度固定大小缩放
			be = (int) (newOpts.outWidth / ww);
		} else if (w < h && h > hh) {// 如果高度高的话根据宽度固定大小缩放
			be = (int) (newOpts.outHeight / hh);
		}
		if (be <= 0)
			be = 1;
		newOpts.inSampleSize = be;// 设置缩放比例
		// 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
		isBm = new ByteArrayInputStream(baos.toByteArray());
		bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
		return compressImage(bitmap);// 压缩好比例大小后再进行质量压缩
	}

	public static byte[] compressImage(Bitmap image) {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
		int options = 100;
		while (baos.toByteArray().length / 1024 > 100) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
			baos.reset();// 重置baos即清空baos
			image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
			options -= 10;// 每次都减少10
		}
		// ByteArrayInputStream isBm = new
		// ByteArrayInputStream(baos.toByteArray());//
		// 把压缩后的数据baos存放到ByteArrayInputStream中
		// Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//
		// 把ByteArrayInputStream数据生成图片
		// return bitmap;

		image.recycle();
		image = null;

		return baos.toByteArray();
	}
	public static void startMyService(Context context){
		Intent startServiceIntent = new Intent(context, YTZTService.class);
//		startServiceIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startService(startServiceIntent);
	}
}
