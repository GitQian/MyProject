package com.ztkj.net;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;

import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import com.ztkj.data.request.Request;
import com.ztkj.tool.Config;

/**
 * 网络访问类，ondestroy里面一定要setNetListener为null
 * 
 * @author Administrator
 *
 */
public class NetTask extends AsyncTask<Request, Integer, String> {
	private NetListener netListener;
	private Request request;
	private int mRepeat;

	public NetTask(NetListener netListener) {
		// TODO Auto-generated constructor stub
		this.netListener = netListener;
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
	}

	/**
	 * 必须
	 */
	@Override
	protected String doInBackground(Request... requestBean) {
		request = requestBean[0];
		String result;
		switch (request.getParamsType()) {
		case JSON:
			publishProgress(10);
			result = acceptJson();
			publishProgress(80);
			break;
		case FILE:

			result = "1";
			break;
		default:
			result = "-1";
			break;
		}
		return result;

	}

	@Override
	protected void onProgressUpdate(Integer... values) {
		// TODO Auto-generated method stub
		setReqPro(values[0]);
	}

	@Override
	protected void onPostExecute(String result) {
		Log.e("返回=========", result + "");
		setReqPro(100);
		if (request.isCancel() || netListener == null) {
			return;
		}
		if (result == null) {
			netListener.netResultFailed("请求网络失败", request);
			return;
		}

		boolean success = false;
		String strBadMessage = "解析错误";
		try {
			JSONObject jo = new JSONObject(result);
			success = jo.getBoolean("success");
			if(jo.has("msg")){
				strBadMessage = jo.getString("msg");
			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		// 成功
		if (success) {
			netListener.netResultSuccess(result, request);
		} else {
			netListener.netResultFailed(strBadMessage, request);
		}

	}

	private String acceptJson() {
		String strResult = null;
		try {
			strResult = getData();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			if (request.isShouldRepeat()) {
				mRepeat++;
				if (mRepeat >= request.getMaxRepeat()) {
					return strResult;
				} else {
					acceptJson();
				}
			}
		}

		return strResult;
	}

	private String getData() throws Exception {
		String strResult;

		HttpURLConnection conn = openConnection();

		if (request.isCancel()) {
			return null;
		}

		InputStream is = conn.getInputStream();

		if (request.isCancel()) {
			return null;
		}

		int ch;
		StringBuffer b = new StringBuffer();
		while ((ch = is.read()) != -1) {
			b.append((char) ch);
		}
//		is.close();
		conn.disconnect();
		

		Log.e("返回处理前======>", b.toString());

		strResult = new String(b.toString().getBytes("iso-8859-1"), "utf-8");
//		strResult=b.toString();

		return strResult;
	}

	private void acceptFile() {

	}

	private HttpURLConnection openConnection() throws IOException {
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
			if(request.getUrl().contains("https")){
				System.setProperty("https.keepAlive", "false");
			}else{
				System.setProperty("http.keepAlive", "false");
			}
		}
		String httpMethod = "";
		String finalUrl = "";

		switch (request.getMethodType()) {
		case GET:
			httpMethod = "GET";
			finalUrl = request.getUrl() +request.getMethodName()+ "?"+ request.getFormatGet();
			Log.e("提交===>", "提交======>"+finalUrl);
			break;
		case POST:
			httpMethod = "POST";
			// finalUrl=Config.URL_COMMON + bean.getMethodName();
			break;
		default:
			break;
		}

		URL url = new URL(finalUrl);
		HttpURLConnection conn;
		if (request.getUrl().contains("https")) {
			trustAllHosts();
			conn = (HttpsURLConnection) url.openConnection();
			((HttpsURLConnection) conn).setHostnameVerifier(DO_NOT_VERIFY);

		} else {
			conn = (HttpURLConnection) url.openConnection();
		}
		
		conn.setConnectTimeout(Config.TIME_OUT);
		conn.setReadTimeout(Config.TIME_OUT);

		if ("POST".equals(httpMethod)) {
			conn.setDoOutput(true);
		}
		
		conn.setRequestProperty("Content-type", "application/x-www-form-urlencoded");
//		conn.setRequestProperty("Content-type", "text/html");
//		conn.setRequestProperty("Accept-Charset", "utf-8");
//		conn.setRequestProperty("Charset", "utf-8");
//		conn.setRequestProperty("contentType", "utf-8");
		conn.setUseCaches(false);
		conn.setDoInput(true);
		conn.setRequestMethod(httpMethod);
		conn.connect();

		if ("POST".equals(httpMethod)) {
			// OutputStream os = conn.getOutputStream();
			// PrintWriter p = new PrintWriter(os);
			// // p.print("data=" + commitJson);
			// if(Config.isDebug){
			// Log.e("POST提交=====>", bean.getCommitJson().toString());
			// }
			// p.print("json=" +
			// URLEncoder.encode(bean.getCommitJson().toString(), "utf-8"));
			// // os.flush();
			// // os.close();
			// p.flush();
			// p.close();
			// // os.flush();
			// os.close();
		}
		if ("GET".equals(httpMethod)) {

		}

		return conn;
	}

	private void setReqPro(int progress) {
		if (request.getReqProlistener() != null && !request.isCancel()) {
			request.getReqProlistener().onRequestProgress(progress);
		}
	}

	private static TrustManager[] xtmArray = new MytmArray[] { new MytmArray() };

	private static void trustAllHosts() {
		// Create a trust manager that does not validate certificate chains
		// Android 采用X509的证书信息机制
		// Install the all-trusting trust manager
		try {
			SSLContext sc = SSLContext.getInstance("TLS");
			sc.init(null, xtmArray, new java.security.SecureRandom());
			HttpsURLConnection
					.setDefaultSSLSocketFactory(sc.getSocketFactory());
			// HttpsURLConnection.setDefaultHostnameVerifier(DO_NOT_VERIFY);//
			// 不进行主机名确认
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {
		@Override
		public boolean verify(String hostname, SSLSession session) {
			return true;
		}
	};
}
