package com.chinacnit.elevatorguard.core.net;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;

import com.chinacnit.elevatorguard.mobile.util.LogUtils;
import com.chinacnit.elevatorguard.mobile.util.LogUtils.LogTag;

/**
 * 上传器
 * 
 * @author ssu
 * @date 2015-7-7 下午3:10:03
 */
public class UploaderThread extends Thread {
	private static final LogTag LOG_TAG = LogUtils.getLogTag(UploaderThread.class.getSimpleName(), true);
	
	public static final int UPLOAD_STARTED = 1000; // start
    public static final int UPLOADING = 1001; // uploading
    public static final int ERROR = 1002; // error
    public static final int COMPLETE = 1003; // complete
    public static final int CANCELED = 1004; // cancel
    
    private static final int UPLOAD_BUFFER_SIZE = 4096;

    private int buffSize = UPLOAD_BUFFER_SIZE;

    private String[] remoteUriList; // 上传路径
    
    private File[] uploadFiles; // 要上传的文件

    // 下载过程中的过程监听器
    private FetcherListener listener;
    
    private static final int TIME_OUT = 10 * 1000;   //超时时间
    private static final String CHARSET = "utf-8"; //设置编码
    private String PREFIX = "--" , LINE_END = "\r\n"; 
    private String CONTENT_TYPE = "multipart/form-data";   //内容类型
    
    public int getBuffSize() {
        return buffSize;
    }

    public UploaderThread setBuffSize(int buffSize) {
        this.buffSize = buffSize;
        return this;
    }

    public String[] remoteUriList() {
        return remoteUriList;
    }

    public UploaderThread setRemoteUriList(String[] remoteUriList) {
        this.remoteUriList = remoteUriList;
        return this;
    }

	public File[] getUploadFiles() {
		return uploadFiles;
	}

	public void setUploadFiles(File[] uploadFiles) {
		this.uploadFiles = uploadFiles;
	}
	
	public FetcherListener getListener() {
        return listener;
    }

    public UploaderThread setListener(FetcherListener listener) {
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
        	checkFiles();
        } catch (Exception e) {
            if (listener != null) {
                listener.onError();
            }
            e.printStackTrace();
            return;
        }
        uploadFiles();
    }
    
    public void uploadFiles() {
    	 if (isInterrupted())
             return;
    	 try {
    		 for (int i = 0; i < remoteUriList.length; i++) {
    			 String BOUNDARY =  UUID.randomUUID().toString();  //边界标识   随机生成
    			 URL url = new URL(remoteUriList[i]);
    			 HttpURLConnection conn = (HttpURLConnection) url.openConnection();
    			 conn.setReadTimeout(TIME_OUT);
	             conn.setConnectTimeout(TIME_OUT);
	             conn.setDoInput(true);  //允许输入流
	             conn.setDoOutput(true); //允许输出流
	             conn.setUseCaches(false);  //不允许使用缓存
	             conn.setRequestMethod("POST");  //请求方式
	             conn.setRequestProperty("Charset", CHARSET);  //设置编码
	             conn.setRequestProperty("connection", "keep-alive");   
	             conn.setRequestProperty("Content-Type", CONTENT_TYPE + ";boundary=" + BOUNDARY); 
	             File file = uploadFiles[i];
	             int fileSize = (int) file.length();
	             int fileSizeInKB = fileSize / 1024;
	             if (this.listener != null) {
                     this.listener.onStart(file.getName(), fileSizeInKB);
                 }
	             /**
	              * 当文件不为空，把文件包装并且上传
	              */
                 DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
                 StringBuffer sb = new StringBuffer();
                 sb.append(PREFIX);
                 sb.append(BOUNDARY);
                 sb.append(LINE_END);
                 /**
                  * 这里重点注意：
                  * name里面的值为服务器端需要key,只有这个key才可以得到对应的文件
                  * filename是文件的名字，包含后缀名的   比如:abc.png  
                  */
                 sb.append("Content-Disposition: form-data; name=\"img\"; filename=\"" + file.getName() + "\"" + LINE_END); 
                 sb.append("Content-Type: application/octet-stream; charset=" + CHARSET + LINE_END);
                 sb.append(LINE_END);
                 dos.write(sb.toString().getBytes());
                 InputStream is = new FileInputStream(file);
                 byte[] bytes = new byte[buffSize];
                 int bytesRead = 0, totalRead = 0;
                 while (!isInterrupted() && (totalRead = is.read(bytes, 0, bytes.length)) != -1) {
                     dos.write(bytes, 0, bytesRead);
                     // update progress bar
                     totalRead += bytesRead;
                     int totalReadInKB = totalRead / 1024;
                     if (this.listener != null) {
                         this.listener.onReceive(i + 1, remoteUriList.length, fileSizeInKB, totalReadInKB);
                     }
                 }
                 is.close();
                 dos.write(LINE_END.getBytes());
                 byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINE_END).getBytes();
                 dos.write(end_data);
                 dos.flush();
                 
                 /**
                  * 获取响应码  200=成功
                  * 当响应成功，获取响应的流  
                  */
                 int res = conn.getResponseCode();  
                 LogUtils.d(LOG_TAG, "uploadFiles", "response code:" + res);
                 if (res == 200) {
                	 LogUtils.d(LOG_TAG, "uploadFiles", "request success");
                	 InputStream input = conn.getInputStream();
                     StringBuffer sb1 = new StringBuffer();
                     int ss;
                     while ((ss=input.read())!=-1) {
                         sb1.append((char)ss);
                     }
                     String result = sb1.toString();
                     LogUtils.d(LOG_TAG, "uploadFiles", "result:" + result);
                 }
                 
                 dos.close();
                 is.close();
                 if (isInterrupted()) {
                	 return;
                 } else {
                	 if (this.listener != null) {
                         this.listener.onOneFinish(file, i + 1, remoteUriList.length);
                     }
                 }
    		 }
    		 if (!isInterrupted()) {
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
    
    public void checkFiles() throws IOException {
        if (remoteUriList == null || uploadFiles == null || uploadFiles.length == 0) {
            throw new IllegalArgumentException();
        }
    }
}
