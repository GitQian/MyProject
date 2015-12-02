package asmack.listener;
//package listener;
//
//import java.util.Timer;
//import java.util.TimerTask;
//
//import org.jivesoftware.smack.ConnectionListener;
//import org.jivesoftware.smack.XMPPConnection;
//
//import com.example.myim.ConnectionUtil;
//
//import android.util.Log;
//
///**
// * 连接监听类
// * 
// * @author Administrator
// * 
// */
//public class MyConnectionListener implements ConnectionListener {
//	private Timer tExit;
//	private String username;
//	private String password;
//	private int logintime = 2000;
//
//	@Override
//	public void connectionClosed() {
//		Log.i("TaxiConnectionListener", "连接开始");
//		// 關閉連接
//		ConnectionUtil.getInstance().closeConnection();
//		// 重连服务器
//		tExit = new Timer();
//		tExit.schedule(new timetask(), logintime);
//	}
//
//	@Override
//	public void connectionClosedOnError(Exception e) {
//		Log.i("TaxiConnectionListener", "连接异常关闭");
//		// 判斷為帳號已被登錄
//		boolean error = e.getMessage().equals("stream:error (conflict)");
//		if (!error) {
//			// 關閉連接
//			ConnectionUtil.getInstance().closeConnection();
//			// 重连服务器
//			tExit = new Timer();
//			tExit.schedule(new timetask(), logintime);
//		}
//	}
//
//	class timetask extends TimerTask {
//		@Override
//		public void run() {
//			username = "joychine1";
//			password = "1231";
//			if (username != null && password != null) {
//				Log.i("TaxiConnectionListener", "嘗試登錄");
//				// 连接服务器
//				if (ConnectionUtil.getInstance().loginIn(username, password)) {
//					Log.i("TaxiConnectionListener", "登錄成功");
//				} else {
//					Log.i("TaxiConnectionListener", "重新登錄");
//					tExit.schedule(new timetask(), logintime);
//				}
//			}
//		}
//	}
//
//	@Override
//	public void reconnectingIn(int arg0) {
//		
//	}
//
//	@Override
//	public void reconnectionFailed(Exception arg0) {
//	}
//
//	@Override
//	public void reconnectionSuccessful() {
//	}
//
//	@Override
//	public void connected(XMPPConnection connection) {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void authenticated(XMPPConnection connection) {
//		// TODO Auto-generated method stub
//		
//	}
//
//}
