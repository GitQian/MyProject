package com.ztkj.tool;

import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.StringTokenizer;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.util.Log;
import android.util.SparseIntArray;

/**
 * [字符处理工具类]
 */
public class StringUtils {

	private final static String TAG = "StringUtils";
	public static final String STAT_FORMAT = "yyyy-MM-dd HH";
	public static final String TIME_FORMAT = "H:mm:ss";
	public static final String DATE_FORMAT_CH = "yyyy年M月d日";
	public static final String DATE_FORMAT = "yyyy-MM-dd";
	public static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";
	public static final String AM_PM_TIME_FORMAT = "HH:mm";
	public static final String HTTP = "http://";
	public static final String HTTPS = "https://";
	public static final String WWW_LOW = "www.";
	public static final String WWW_UPPER = "WWW.";
	public static final String FAVICON_URL_SUFFIX = "/favicon.ico";
	
	static SparseIntArray CHAR_MAP = new SparseIntArray(9);
	static {
		CHAR_MAP.put('一', 1);
		CHAR_MAP.put('二', 2);
		CHAR_MAP.put('三', 3);
		CHAR_MAP.put('四', 4);
		CHAR_MAP.put('五', 5);
		CHAR_MAP.put('六', 6);
		CHAR_MAP.put('七', 7);
		CHAR_MAP.put('八', 8);
		CHAR_MAP.put('九', 9);
	}
	
	private static int getIntFromMap(char ch){
		Integer result = CHAR_MAP.get(ch);
		return result != null ? result : 0; 
	}

	/**
	 * 将0~9999以内的小写汉字转化为数字
	 * 例如：三千四百五十六转化为3456
	 * @param str
	 * @return
	 */
	public static int parseChineseNumber(String str) {
		if (str == null || str.length() <= 0) {
			return -1;
		}
		int result = 0;
		int index = -1;
		index = str.indexOf('千');
		if (index > 0) {
			result += getIntFromMap(str.charAt(index - 1)) * 1000;
		}
		index = str.indexOf('百');
		if (index > 0) {
			result +=  getIntFromMap(str.charAt(index - 1)) * 100;
		}
		index = str.indexOf('十');
		if(index > 0){
			result += getIntFromMap(str.charAt(index - 1)) * 10;
		} else if (index == 0) {
			result += 10;
		} 
		index = str.length();
		if (index > 0) {
			result +=  getIntFromMap(str.charAt(index - 1));
		}
		return result;
	}
	
	public static final boolean isHttpUrl(String url) {
		String regEx = "^(https|http://){0,1}([a-zA-Z0-9]{1,}[a-zA-Z0-9\\-]{0,}\\.){0,4}([a-zA-Z0-9]{1,}[a-zA-Z0-9\\-]{0,}\\.[a-zA-Z0-9]{1,})/{0,1}$";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(url);
		return m.find();
	}

	public static String formatStatDate() {
		SimpleDateFormat format = new SimpleDateFormat(STAT_FORMAT);
		return format.format(new Date());
	}
	
	public static String formatStatDate(long time) {
		SimpleDateFormat format = new SimpleDateFormat(STAT_FORMAT);
		return format.format(new Date(time));
	}

	public static String formatString(Date date, String formatString) {
		String nowdate = "";
		if (date != null) {
			SimpleDateFormat format = new SimpleDateFormat(formatString);
			nowdate = format.format(date);
		}

		return nowdate;
	}

	public static String formatString(Date date) {
		return formatString(date, DATE_FORMAT_CH);
	}
	
	public static String formartAmout(double num) {
		String pattern = "#0.00";
		DecimalFormat format = new DecimalFormat(pattern);
		String value = format.format(num);
		
		return value;
	}

	public static Date parseDate(String dateStr, String formatString) {
		Date date = null;
		if (dateStr != null && !dateStr.equals("")) {

			try {
				SimpleDateFormat format = new SimpleDateFormat(formatString);
				date = format.parse(dateStr);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}

		return date;
	}

	public static String getToday(String format) {
		Calendar now = Calendar.getInstance();
		if (format != null) {
			SimpleDateFormat dateFormat = new SimpleDateFormat(format);
			return dateFormat.format(now.getTime());
		} else {
			return now.getTime().getTime() + "";
		}
	}

	public static boolean isNumber(String str) {
		if (str == null || str.length() < 1) {
			return false;
		}

		Pattern pattern = Pattern.compile("[0-9]*.[0-9]*");
		return pattern.matcher(str).matches();
	}

	public static boolean isWebUrl(String url) {
		if (url == null || url.length() < 1) {
			return false;
		}

		url = url.toLowerCase();
		return url.startsWith("http://") || url.startsWith("https://") || url.startsWith("ftp://");
	}

	/**
	 * 是否为空
	 * 
	 * @param value
	 * @return
	 */
	public static boolean isEmpty(String value) {
		int strLen;
		if (value == null || (strLen = value.length()) == 0) {
			return true;
		}
		for (int i = 0; i < strLen; i++) {
			if ((Character.isWhitespace(value.charAt(i)) == false)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 是否为邮件地址
	 */
	public static boolean isVaildEmail(String email) {
		if (email == null || email.trim().length() == 0) {
			return false;
		}
		String emailPattern = "[a-zA-Z0-9_-|\\.]+@[a-zA-Z0-9_-]+.[a-zA-Z0-9_.-]+";
		boolean result = Pattern.matches(emailPattern, email);
		return result;
	}

	public static String parseDuration(int second) {
		if (second < 0) {
			return "";
		}
		SimpleDateFormat format = new SimpleDateFormat(TIME_FORMAT);
		format.setTimeZone(TimeZone.getTimeZone("GMT +08:00, GMT +0800"));
		return format.format(new Date(second));
	}

	public static String parseDateFromInt(long date) {
		SimpleDateFormat myFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			return myFormatter.format(date);
		} catch (Exception e) {
			return "";
		}
	}

	public static String parseDateIntoNumStr(long date) {
		SimpleDateFormat myFormatter = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		try {
			return myFormatter.format(date);
		} catch (Exception e) {
			return "";
		}
	}
	
	public static String parseDateFromInt(long date, String formatString) {
		SimpleDateFormat myFormatter = new SimpleDateFormat(formatString);
		try {
			return myFormatter.format(date);
		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * 将从0开始的毫秒数转换为hh:mm:ss格式 如果没有hh那么转换为 mm:ss
	 * 
	 * @param milliseconds
	 * @return
	 */
	public static String parseZeroBaseMilliseconds(int milliseconds) {
		int hh = milliseconds / 1000 / 60 / 60;
		int mm = (milliseconds - (hh * 60 * 60 * 1000)) / 1000 / 60;
		int ss = (milliseconds - (mm * 1000 * 60) - (hh * 60 * 60 * 1000)) / 1000;
		if (hh != 0) {
			return String.format("%02d:%02d:%02d", hh, mm, ss);
		}
		return String.format("%02d:%02d", mm, ss);
	}
	
	/**
	 * 将从0开始的毫秒数转换为hh:mm:ss格式 如果没有hh那么转换为 mm:ss
	 * 
	 * @param milliseconds
	 * @return
	 */
	public static String parseMilliseconds(String format, long milliseconds) {
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		formatter.setTimeZone(TimeZone.getTimeZone("GMT+00:00"));
		String hms = formatter.format(milliseconds);  
		return hms;
	}
	

	/** int ip地址 to String 地址 */
	public static String intToIp(int i) {
		StringBuilder sb = new StringBuilder();
		sb.append(i & 0xFF);
		sb.append(".");
		sb.append((i >> 8) & 0xFF);
		sb.append(".");
		sb.append((i >> 16) & 0xFF);
		sb.append(".");
		sb.append((i >> 24) & 0xFF);
		return sb.toString();
	}

	public static boolean isHex(String key) {
		for (int i = key.length() - 1; i >= 0; i--) {
			final char c = key.charAt(i);
			if (!(c >= '0' && c <= '9' || c >= 'A' && c <= 'F' || c >= 'a' && c <= 'f')) {
				return false;
			}
		}
		return true;
	}

	public static String toHex(byte[] buf) {
		if (buf == null)
			return "";
		StringBuffer result = new StringBuffer(2 * buf.length);
		for (int i = 0; i < buf.length; i++) {
			appendHex(result, buf[i]);
		}
		return result.toString();
	}

	private final static String HEX = "0123456789ABCDEF";

	private static void appendHex(StringBuffer sb, byte b) {
		sb.append(HEX.charAt((b >> 4) & 0x0f)).append(HEX.charAt(b & 0x0f));
	}

	public static byte[] toByte(String hexString) {
		int len = hexString.length() / 2;
		byte[] result = new byte[len];
		for (int i = 0; i < len; i++)
			result[i] = Integer.valueOf(hexString.substring(2 * i, 2 * i + 2), 16).byteValue();
		return result;
	}

	public static String urlEncode(String url) {
		StringBuffer urlB = new StringBuffer();
		StringTokenizer st = new StringTokenizer(url, "/ ", true);
		while (st.hasMoreTokens()) {
			String tok = st.nextToken();
			if (tok.equals("/"))
				urlB.append("/");
			else if (tok.equals(" "))
				urlB.append("%20");
			else {
				try {
					urlB.append(URLEncoder.encode(tok, "UTF-8"));
				} catch (java.io.UnsupportedEncodingException uee) {

				}
			}
		}
		// Log.d(TAG, "urlEncode urlB:" + urlB.toString());
		return urlB.toString();
	}

	public static String htmlEncode(String s) { 
        StringBuilder sb = new StringBuilder(); 
        char c; 
        for (int i = 0; i < s.length(); i++) { 
            c = s.charAt(i); 
            switch (c) { 
            case '<': 
                sb.append("&lt;"); //$NON-NLS-1$ 
                break; 
            case '>': 
                sb.append("&gt;"); //$NON-NLS-1$ 
                break; 
            case '&': 
                sb.append("&amp;"); //$NON-NLS-1$ 
                break; 
            case '\'': 
                sb.append("&apos;"); //$NON-NLS-1$ 
                break; 
            case '"': 
                sb.append("&quot;"); //$NON-NLS-1$ 
                break; 
            default: 
                sb.append(c); 
            } 
        } 
        return sb.toString(); 
    }
	
	public static void printStack(String tag) {
		if (Log.DEBUG==3) {
			return;
		}
		StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
		for(int i = 0;i < stackTraceElements.length;i++) {
			StackTraceElement element = stackTraceElements[i];
			String name = element.getMethodName();
			Log.i(tag, element.getClassName() + "." + name + "() " + element.getLineNumber());
		}
	}
	
	public static boolean equalsString(String str1, String str2){
		if(str1 != null){
			return str1.equals(str2);
		} else {
			return str2 == null;
		}
	}
	
	/**
	 * 比较类似于3.5.0这样的版本号字符串的大小
	 * 
	 * @param str1
	 * @param str2
	 * @return str1 > str2 返回 正数；str1 = str2 返回 0；str1 < str2 返回 负数；
	 */
	public static int compareVerString(String str1, String str2) {
		if (str1 == null || str2 == null) {
			return 0;
		}

		String[] cons1 = str1.split("\\.");
		String[] cons2 = str2.split("\\.");

		int i = 0;
		try {
			while (i < cons1.length && i < cons2.length) {
				int int1 = Integer.parseInt(cons1[i]);
				int int2 = Integer.parseInt(cons2[i]);
				int res = int1 - int2;
				if(res == 0){
					i++;
					continue;
				}else{
					return res;
				}
			}
			
			return cons1.length - cons2.length;
		} catch (Exception e) {
			return 0;
		}
	}
}
