package com.ztkj.tool;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期和时间的工具类
 * 
 * @author liucheng liucheng187@qq.com
 */
public class DateTimeUtil {
	public static SimpleDateFormat sdf = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");
	/**
	 * @param datetime
	 *            yyyy-MM-dd HH:mm:ss 格式的日期时间格式， 转化成 “上午 11:30”或“下午1:20"这种格式。
	 * @return
	 */
	public static String getTimeDes(String datetime) {
		String result = "未知时间";
		try {
			Date date = sdf.parse(datetime);
			Calendar cd = Calendar.getInstance();
			cd.setTime(date);

			int hour = cd.get(Calendar.HOUR_OF_DAY);
			if (hour >= 12) {
				result = "下午 " + cd.get(Calendar.HOUR) + ":"
						+ cd.get(Calendar.MINUTE) + ":"
						+ cd.get(Calendar.SECOND);
			} else if (hour < 12) {
				result = "上午 " + cd.get(Calendar.HOUR) + ":"
						+ cd.get(Calendar.MINUTE) + ":"
						+ cd.get(Calendar.SECOND);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return result;
	}
	public static int getCurrentYear() {
		Calendar c = Calendar.getInstance();

		int year = c.get(Calendar.YEAR);

		return year;
	}
	public static int getCurrentMonth() {
		Calendar c = Calendar.getInstance();

		int month = c.get(Calendar.MONTH);
		
		return month+1;
	}
	public static int getCurrentDay() {
		Calendar c = Calendar.getInstance();
		
		int month = c.get(Calendar.DAY_OF_MONTH);
		
		return month+1;
	}
}
