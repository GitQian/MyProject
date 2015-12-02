package com.chinacnit.elevatorguard.core.net;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

import org.apache.commons.io.FileUtils;

import com.chinacnit.elevatorguard.mobile.util.LogUtils;
import com.chinacnit.elevatorguard.mobile.util.LogUtils.LogTag;


/**
 * 下载器
 * @author ssu 
 * @date 2015-6-25 下午8:28:38
 */
public class DownLoaderThread extends Thread {
	
	private static final LogTag LOG_TAG = LogUtils.getLogTag(DownLoaderThread.class.getSimpleName(), true);

    public static final int DOWNLOAD_STARTED = 1000; // start
    public static final int DOWNLOADING = 1001; // downloading
    public static final int ERROR = 1002; // error
    public static final int COMPLETE = 1003; // complete
    public static final int CANCELED = 1004; // cancel

    private static final int DOWNLOAD_BUFFER_SIZE = 4096;

    private int buffSize = DOWNLOAD_BUFFER_SIZE;

    private String[] remoteUriList; // 下载路径

    // 本地存储地址
    private String localDir;

    // 下载后保存到本地的文件名
    private String[] localFileNameList;

    // 下载过程中的过程监听器
    private FetcherListener listener;

    public int getBuffSize() {
        return buffSize;
    }

    public DownLoaderThread setBuffSize(int buffSize) {
        this.buffSize = buffSize;
        return this;
    }

    public String[] remoteUriList() {
        return remoteUriList;
    }

    public DownLoaderThread setRemoteUriList(String[] remoteUriList) {
        this.remoteUriList = remoteUriList;
        return this;
    }

    public String getLocalDir() {
        return localDir;
    }

    public DownLoaderThread setLocalDir(String localDir) {
        this.localDir = localDir;
        return this;
    }

    public String[] getLocalFileNameList() {
        return localFileNameList;
    }

    public DownLoaderThread setLocalFileNameList(String[] localFileNameList) {
        this.localFileNameList = localFileNameList;
        return this;
    }

    public FetcherListener getListener() {
        return listener;
    }

    public DownLoaderThread setListener(FetcherListener listener) {
        this.listener = listener;
        return this;
    }


    /**
     * Connects to the URL of the file, begins the download, and notifies the
     * AndroidFileDownloader activity of changes in state. Writes the file to
     * the root of the SD card.
     */
    @Override
    public void run() {
        try {
            ensureLocalDir();
        } catch (Exception e) {
            if (listener != null) {
                listener.onError();
            }
            e.printStackTrace();
            return;
        }
        downloadFiles();
    }

    public void downloadFiles() {
        if (isInterrupted())
            return;
        try {
            File tmpDir = new File(localDir);
            for (int i = 0; i < remoteUriList.length; i++) {
                URL url = new URL(remoteUriList[i]);
                URLConnection conn = url.openConnection();
                conn.setUseCaches(false);
                conn.setConnectTimeout(5000);
                int fileSize = conn.getContentLength();
                int fileSizeInKB = fileSize / 1024;
                if (this.listener != null) {
                    this.listener.onStart(localFileNameList[i], fileSizeInKB);
                }
                BufferedInputStream inStream = new BufferedInputStream(conn.getInputStream());
                File outFile = new File(tmpDir, localFileNameList[i]);
                FileOutputStream fileStream = new FileOutputStream(outFile);
                BufferedOutputStream outStream = new BufferedOutputStream(fileStream, DOWNLOAD_BUFFER_SIZE);
                byte[] data = new byte[DOWNLOAD_BUFFER_SIZE];
                int bytesRead = 0, totalRead = 0;
                while (!isInterrupted() && (bytesRead = inStream.read(data, 0, data.length)) >= 0) {
                    outStream.write(data, 0, bytesRead);
                    // update progress bar
                    totalRead += bytesRead;
                    int totalReadInKB = totalRead / 1024;
                    if (this.listener != null) {
                        this.listener.onReceive(i + 1, remoteUriList.length, fileSizeInKB, totalReadInKB);
                    }
                }
                outStream.close();
                fileStream.close();
                inStream.close();
                if (isInterrupted()) {
                    // the download was canceled, so let's delete the partially downloaded file
                    outFile.delete();
                    return;
                } else {
                    if (this.listener != null) {
                        this.listener.onOneFinish(outFile, i + 1, remoteUriList.length);
                    }
                }
            }
            if (isInterrupted()) {
                // clear all download file
                try {
                    FileUtils.cleanDirectory(tmpDir);
                } catch (IOException e) {
                	LogUtils.e(LOG_TAG, "downloadFiles", e);
                }
            } else {
                if (this.listener != null) {
                    this.listener.onAllDone();
                }
            }
        } catch (Exception e) {
            LogUtils.e(LOG_TAG, "downloadFiles", e);
            if (this.listener != null) {
                this.listener.onError();
            }
        }
    }

    public void ensureLocalDir() throws IOException {
        if (localDir == null || remoteUriList == null || localFileNameList == null || localFileNameList.length == 0) {
            throw new IllegalArgumentException();
        } else {
            FileUtils.forceMkdir(new File(localDir));
        }
    }

}
