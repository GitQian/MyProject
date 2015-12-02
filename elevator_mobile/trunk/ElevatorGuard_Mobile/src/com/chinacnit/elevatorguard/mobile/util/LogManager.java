package com.chinacnit.elevatorguard.mobile.util;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

import android.net.ParseException;

import com.chinacnit.elevatorguard.mobile.config.ConfigSettings;
import com.chinacnit.elevatorguard.mobile.config.Constants;
import com.chinacnit.elevatorguard.mobile.util.LogUtils.LogTag;

/**
 * 日志Manager
 * 
 * @author ssu
 * @date 2015-5-12 下午5:35:43
 */
public class LogManager {
	private static final LogTag TAG = LogUtils.getLogTag(LogManager.class.getSimpleName(), false);
	private static final String ROOT_FOLDER = Constants.DEFAULT_STORAGE_PATH;
	private static final String DIRECTORY = "/elevatorguard/log";

	private static LogManager sInstance = new LogManager();

	/** Number of milliseconds to wait until a new write is made */
	private static final int MEGA_BYTES = 1024 * 1024;

	/** Number of logs in the queue that triggers a write if exceeded */
	private static final int LOG_QUEUE_TRIGGER_SIZE = 50;
	private static final int TOLARENCE = 100;
	private static final String LOG_PREFIX = "elevatorguard";
	private static final String LOG_SUFFIX = "log";

	private LogInfo operationLogInfo = new LogInfo(LogType.OPERATION_LOG);
	private LogInfo transmissionLogInfo = new LogInfo(LogType.TRANSMISSION_LOG);
	private LogInfo crashLogInfo = new LogInfo(LogType.CRASH_LOG);

	private LogManager() {
	}

	public static LogManager getInstance() {
		if (sInstance == null) {
			sInstance = new LogManager();
		}
		return sInstance;
	}

	/**
	 * delete log in the list
	 * 
	 * @param log
	 *            list
	 */
	public void deleteLogs(List<String> list) {
		LogUtils.d(TAG, "deleteLogs", "begin delete logs");
		if (list == null || list.isEmpty()) {
			LogUtils.d(TAG, "deleteLogs", "noting to delete");
			return;
		}
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				File file = new File(ROOT_FOLDER + DIRECTORY + "/"
						+ list.get(i));
				if (file.exists())
					file.delete();
			}
		}
	}

	public void queueOperationMessage(String logMessage) {
		if (operationLogInfo != null) {
			operationLogInfo.queueMessage("\n" + logMessage);
			operationLogInfo.checkForQueueOverflow();
		}
	}

	public void queueTransmissionMessage(String logMessage) {
		if (transmissionLogInfo != null) {
			transmissionLogInfo.queueMessage("\n" + logMessage);
			transmissionLogInfo.checkForQueueOverflow();
		}
	}

	public void writeCrashLog(String logMessage) {
		if (crashLogInfo != null) {
			crashLogInfo.queueMessage("\n" + logMessage);
			crashLogInfo.writeMessagesInTheQueue();
		}
	}

	/**
	 * 删除指定的日志
	 */
	public void deleteLog(String path) {
		File file = new File(ROOT_FOLDER + DIRECTORY + "/" + path);
		if (file.exists())
			file.delete();
	}

	/**
	 * 删除内存下过期的日志
	 */
	public void deleteSDcardExpiredLog() {
		File file = new File(ROOT_FOLDER + DIRECTORY);
		if (file.isDirectory()) {
			File[] allFiles = file.listFiles();
			for (File logFile : allFiles) {
				long ModifiedTime = logFile.lastModified();
				if (canDeleteLogFile(ModifiedTime)) {
					logFile.delete();
				}
			}
		}
	}

	/**
	 * 判断日志文件是否可以删除
	 * 
	 * @param createDateStr
	 * @return
	 */
	public boolean canDeleteLogFile(long ModifiedTime) {
		boolean canDel = false;
		Calendar calendar = Calendar.getInstance();

		int mMaxDay = Integer.valueOf(ConfigSettings.getMaxDay());// 默认保存天数为5天
		calendar.add(Calendar.DAY_OF_MONTH, -1 * mMaxDay);// 删除已过保存期的日志
		Date expiredDate = calendar.getTime();
		try {
			Date createDate = new Date(ModifiedTime);
			canDel = createDate.before(expiredDate);
		} catch (ParseException e) {
			canDel = false;
		}
		return canDel;
	}

	/**
	 * 程序退出，把所有的日志都写到文件中
	 */
	public void flush() {
		if (transmissionLogInfo != null) {
			transmissionLogInfo.writeMessagesInTheQueue();
		}
		if (operationLogInfo != null) {
			operationLogInfo.writeMessagesInTheQueue();
		}
		if (crashLogInfo != null) {
			crashLogInfo.writeMessagesInTheQueue();
		}
	}

	/**
	 * 日志信息类
	 * 
	 * @author ss
	 * 
	 */
	private class LogInfo {

		/** 缓存日志信息是否输出到磁盘flag */
		private AtomicBoolean sCanWriteToDisk = new AtomicBoolean(true);

		private String sFileName;

		/** 内存缓存列表 */
		private List<String> sPendingQueue = new CopyOnWriteArrayList<String>();
		/** */
		private List<String> sWriterQueue = new CopyOnWriteArrayList<String>();

		private LogType logType;

		private int index = 1;

		private LogInfo(LogType type) {
			logType = type;
		}

		private void queueMessage(String message) {
			sPendingQueue.add(message);
			LogUtils.d(TAG, logType + "-queueMessage: " + sPendingQueue.size(),
					message);
		}

		private void checkForQueueOverflow() {
			if (sPendingQueue.size() > LOG_QUEUE_TRIGGER_SIZE) {
				writeMessagesInTheQueue();
			}
		}

		/**
		 * This method will start the writing procedure. Steps: <br>
		 * 1) Copy messages from pending queue into write queue. <br>
		 * 2) Clear pending queue.<br>
		 * 3) Write all messages in write queue to disk. <br>
		 * 4) Clear write queue <br>
		 * <h3>This method is executed on a background thread.</h3>
		 */
		private void writeMessagesInTheQueue() {
			if (canWriteToDisk() && sCanWriteToDisk.compareAndSet(true, false)
					&& sPendingQueue.size() > 0) {
				Thread writerThread = new Thread(sWriterRunnable);
				writerThread.start();
			} else {
				LogUtils.w(TAG, logType + "-writeMessagesInTheQueue",
						"current cannot do write log");
			}
		}

		/**
		 * 缓存pubds目录是否可写
		 * 
		 * @return true if the the media is mounted and can be written into
		 */
		private boolean canWriteToDisk() {
			return new File(Constants.DEFAULT_STORAGE_PATH).canWrite();
		}

		/**
		 * This method copies the log messages from the first buffer to the
		 * writer buffer from where they will be written to disk
		 */
		private void copyMessagesFromQueue() {
			long l1 = Helper.uptimeMillis();
			synchronized (sPendingQueue) {
				sWriterQueue.addAll(sPendingQueue);
				sPendingQueue.clear();// clear the queue on copy finished
			}
			long l2 = Helper.uptimeMillis();
			LogUtils.timeConsume(TAG, logType + "-copyMessagesFromQueue", l2
					- l1);
		}

		/**
		 * A runnable which run the write methods than stops the timer
		 */
		private Runnable sWriterRunnable = new Runnable() {
			@Override
			public void run() {
				executeWriteProcedure();
			}
		};

		/**
		 * This method does the actual writing.
		 */
		private void executeWriteProcedure() {
			long l1 = Helper.uptimeMillis();
			copyMessagesFromQueue();// includes clear of pending queue
			saveMessagesToDisk();// includes clear of writer queue
			long l2 = Helper.uptimeMillis();
			LogUtils.timeConsume(TAG, logType + "-executeWriteProcedure", l2
					- l1);
		}

		/**
		 * This method will write all the messages from the writing buffer.
		 */
		private void saveMessagesToDisk() {
			StringBuilder builder = new StringBuilder();
			for (String s : sWriterQueue) {
				builder.append(s);
			}
			LogUtils.w(TAG, logType + "-saveMessagesToDisk", builder.toString());
			writeMessage(builder.toString());
			sWriterQueue.clear();// clear the buffer on write finished
			sCanWriteToDisk.set(true);
		}

		/**
		 * 写日志到磁盘日志文件
		 * 
		 * @param text
		 *            日志信息
		 */
		private void writeMessage(String text) {
			// set LogTactics 设置日志策略
			int mMaxSize = ConfigSettings.getMaxSize() * MEGA_BYTES;// 默认大小为2M
			Calendar cal = Calendar.getInstance();
			File logDirectory = new File(ROOT_FOLDER + DIRECTORY);
			if (!logDirectory.exists()) {
				logDirectory.mkdirs();
			}
			boolean flag = true;
			while (flag) {
				LogUtils.v(TAG, "writeMessage : " + flag, 1);

				try {
					File logFile = new File(logDirectory, getFileName(cal,
							index));

					if (logFile.length() < mMaxSize) {
						flag = false;
						FileOutputStream fos = new FileOutputStream(logFile,
								true);
						fos.write(text.getBytes());
						LogUtils.v(TAG, "writeMessage", text.getBytes());
						fos.close();
					} else {
						index++;
						if (index > TOLARENCE) {
							flag = false;
						}
						LogUtils.v(TAG, "writeMessage : " + flag, 2 + ","
								+ index);

					}
				} catch (FileNotFoundException e) {
					flag = false;
					LogUtils.e(TAG, "writeMessage", e);
				} catch (IOException e) {
					flag = false;
					LogUtils.e(TAG, "writeMessage", e);
				} catch (Exception e) {
					flag = false;
					LogUtils.e(TAG, "writeMessage", e);
				}
			}
			cleanCacheLogFile();
		}

		/**
		 * Returns the filename of the log file. It can be the same file, or it
		 * can be a separate file for each day
		 * 
		 * @return the filename
		 */
		private String getFileName(Calendar cal, int index) {
			sFileName = LOG_PREFIX + "_" + logType + "_" + Helper.getToday()
					+ "_" + String.format("%03d", index) + "." + LOG_SUFFIX;
			return sFileName;
		}

		private FileFilter filter = new FileFilter() {
			@Override
			public boolean accept(File pathname) {
				String regex = LOG_PREFIX + "_" + logType + "_"
						+ "[0-9]{4}_[0-9]{2}_[0-9]{2}_[0-9]{3}" + "."
						+ LOG_SUFFIX;
				return pathname.getName().matches(regex);
			}
		};

		private Comparator<File> comparator = new Comparator<File>() {

			@Override
			public int compare(File o1, File o2) {
				return -o1.getName().compareTo(o2.getName());
			}
		};

		/**
		 * 清除缓存log
		 */
		private void cleanCacheLogFile() {
			File logDir = new File(ROOT_FOLDER + DIRECTORY);
			File[] fileList = logDir.listFiles(filter);
			int mMaxNum = ConfigSettings.getMaxNum();// 默认数量为3个
			if (fileList != null && fileList.length > mMaxNum) {
				Arrays.sort(fileList, comparator);
				List<File> list = Arrays.asList(fileList);
				list = list.subList(mMaxNum, list.size());
				for (File f : list) {
					LogUtils.d(TAG, "cleanCacheLogFile",
							"delete log: " + f.getName());
					f.delete();
				}
			} else {
				LogUtils.d(TAG, "cleanCacheLogFile", "no cache log file");
			}
		}
	}

	/** 日志枚举类型 */
	public enum LogType {
		/** 系统操作日志 */
		OPERATION_LOG,
		/** 传输日志 */
		TRANSMISSION_LOG,
		/** 系统崩溃日志 */
		CRASH_LOG
	}
}