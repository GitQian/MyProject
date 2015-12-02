package com.chinacnit.elevatorguard.mobile.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import android.os.Environment;

import com.chinacnit.elevatorguard.mobile.config.Constants;
import com.chinacnit.elevatorguard.mobile.util.LogUtils.LogTag;

/**
 * 文件操作类
 * 
 * @author ssu
 * @date 2015-5-12 下午5:33:17
 */
public class FileStore {
	private static final LogTag LOG_TAG = LogUtils.getLogTag(FileStore.class.getSimpleName(), true);
	public static final String PATH_LOG = "log";

	/**
	 * 获取log文件存放路径
	 * 
	 * @return log文件存放路径
	 */
	public static final String getLogFolderPath() {
		return Constants.DEFAULT_STORAGE_PATH + "/" + Constants.DEFAULT_PROJECT_PATH + "/" + PATH_LOG;
	}

	/**
	 * 获取SD卡地址
	 * 
	 * @return
	 */
	public static File getSDCreadFlie() {
		File outputFile = null;
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			outputFile = Environment.getExternalStorageDirectory();
		} else {
			outputFile = Environment.getExternalStorageDirectory();
		}
		return outputFile;
	}

	/**
	 * 加载文本文件（当心内存溢出）
	 * 
	 * @param filePath
	 *            文本路径
	 * @param encoding
	 *            the encoding of the text in the file
	 * @return a byte[] with the contents of the file.
	 */
	public static String loadTextFromFile(String filePath, String encoding) {
		InputStreamReader isr = null;
		BufferedWriter out = null;
		String result = null;
		try {
			File file = new File(filePath);
			if (!file.exists()) {
				return null;
			}
			isr = new InputStreamReader(new FileInputStream(file), encoding);
			StringBuilder builder = new StringBuilder();
			char[] buffer = new char[512];
			int count = 0;
			while ((count = isr.read(buffer)) != -1) {
				// write the read bytes from internet to the card.
				builder.append(buffer, 0, count);
			}
			result = builder.toString().replaceAll("\r\n", "\t\t");
		} catch (IOException e) {
			LogUtils.e(LOG_TAG, "loadTextFromFile", e);
		} finally {
			try {
				if (isr != null) {
					isr.close();
				}
				if (out != null) {
					out.flush();
					out.close();
				}
			} catch (IOException e) {
			}
		}
		return result;
	}

	/**
	 * 判断文件是否合法
	 * 
	 * @param filePath
	 *            文件路径
	 * @param fileSize
	 *            原本文件大小
	 * @return true 合法| false 不合法
	 */
	public static boolean isFileValid(String filePath, long fileSize) {
		File f = new File(filePath);
		if (!f.exists()) {
			return false;
		}
		return f.length() == fileSize;
	}

	/**
	 * 去掉文件名后缀
	 * 
	 * @param filename
	 *            文件名
	 * @return
	 */
	public static String getFileNameWithoutSuffix(String filename) {
		return filename.substring(0, filename.lastIndexOf("."));
	}

	/**
	 * 递归删除文件夹和文件夹下面的所有子文件
	 * 
	 * @Title: delete
	 * @Description: TODO
	 * @param path
	 * @Date:2015-4-17
	 * @return void
	 */
	public static void delete(String path) {
		File f1 = new File(path);
		if (f1.isDirectory()) {
			String[] fileList = f1.list();
			if (null == fileList) {
				return;
			}
			for (String s : fileList) {
				File f2 = new File(f1, s);
				if (f2.isDirectory()) {
					delete(f2.getAbsolutePath());
				} else {
					f2.delete();
					LogUtils.d(LOG_TAG, "delete", s);
				}
			}
			f1.delete();
		} else {
			f1.delete();
		}
	}

	/**
	 * 删除空文件夹
	 * 
	 * @param path
	 *            删除文件的路径
	 */
	public static void deleteDirectory(String path) {
		File f1 = new File(path);
		f1.delete();
	}
}
