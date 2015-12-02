package com.chinacnit.elevatorguard.core.net;

import java.io.File;

public interface FetcherListener {
	void onStart(String fileName, int fileLength);

	/**
	 * @param total
	 *            包的大小，字节
	 * @param received
	 *            接收到多少，字节
	 */
	void onReceive(int currentIndex, int allIndex, int total, int received);

	void onOneFinish(File apkFile, int currentIndex, int allIndex) throws Exception;

	void onAllDone();

	void onError();

	void onInterrupt();
}
