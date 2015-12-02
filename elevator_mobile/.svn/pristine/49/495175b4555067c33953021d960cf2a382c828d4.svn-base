package com.chinacnit.elevatorguard.mobile.util;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import android.util.Base64;

import com.chinacnit.elevatorguard.mobile.config.Constants;
import com.chinacnit.elevatorguard.mobile.util.LogUtils.LogTag;

public class EncodingUtils {

	private static final LogTag LOG_TAG = LogUtils.getLogTag(EncodingUtils.class.getSimpleName(), true);
	/**
	 * 加密字符串
	 * 
	 * @param str
	 * @param privatekey
	 * @return [参数说明]
	 * @return String
	 * @exception throws [违例类型] [违例说明]
	 */
	public static String hmacSHA1Encrypt(String data, String key) {
		byte[] keyBytes = key.getBytes();
		SecretKeySpec signingKey = new SecretKeySpec(keyBytes, Constants.MAC_NAME);
		Mac mac;
		StringBuilder sb = new StringBuilder();
		try {
			mac = Mac.getInstance(Constants.MAC_NAME);
			mac.init(signingKey);
			byte[] rawHmac = mac.doFinal(data.getBytes());
			for (byte b : rawHmac) {
				sb.append(byteToHexString(b));
			}
		} catch (NoSuchAlgorithmException e) {
			LogUtils.e(LOG_TAG, "hmacSHA1Encrypt", e);
		} catch (InvalidKeyException e) {
			LogUtils.e(LOG_TAG, "hmacSHA1Encrypt", e);
		}
		return sb.toString();
	}

	/**
	 * 将字节转换成16进制
	 * 
	 * @param ib
	 * @return [参数说明]
	 * @return String
	 * @exception throws [违例类型] [违例说明]
	 */
	private static String byteToHexString(byte ib) {
		char[] Digit = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a',
				'b', 'c', 'd', 'e', 'f' };
		char[] ob = new char[2];
		ob[0] = Digit[(ib >>> 4) & 0X0f];
		ob[1] = Digit[ib & 0X0F];
		String s = new String(ob);
		return s;
	}

	/**
	 * 将字符串通过Base64加密
	 * 
	 * @param s
	 *            需要加密的字符串
	 * @return 加密后的字符串
	 */
	public static String toBase64String(String s) {
		try {
			byte[] bs = s.getBytes(Constants.ENCODING);
			String result = Base64.encodeToString(bs, Base64.DEFAULT);
			return result;
		} catch (UnsupportedEncodingException e) {
			LogUtils.e(LOG_TAG, "toBase64String", e);
		}
		return null;
	}
}
