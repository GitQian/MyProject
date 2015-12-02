package asmack.listener;

import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.XMPPConnection;

import com.ztkj.service.YTZTService;

import android.util.Log;

public class MyXMMPConnectionListener implements ConnectionListener {
	public  final String TAG = "===========";
	@Override
	public void reconnectionSuccessful() {
		//Log.e(TAG, "重连成功");
	}

	@Override
	public void reconnectionFailed(Exception arg0) {
		arg0.printStackTrace();
		//Log.e(TAG, "重连失败 erromsg:"+arg0.getMessage());

	}

	@Override
	public void reconnectingIn(int arg0) {
		//Log.e(TAG, "重连将会执行 在"+arg0+"秒后");

	}

	@Override
	public void connectionClosedOnError(Exception arg0) {
		arg0.printStackTrace();  //Connection closed with error 
		//Log.e(TAG, "连接出错，关闭connectionClosedOnError，errormsg:"+arg0.getMessage());  //msg:stream:error (conflict)
		if("stream:error (conflict)".equals(arg0.getMessage())){
			//Log.e(TAG, "您在其他设备登录了，当前设备离线");
		}

	}

	@Override
	public void connectionClosed() {
		//Log.e(TAG, "连接关闭");
		YTZTService.isConn=false;
	}

	@Override
	public void connected(XMPPConnection arg0) {
		//Log.e(TAG, "连接成功,user:"+arg0.getUser()+"--serverName:"+arg0.getServiceName());

	}

	@Override
	public void authenticated(XMPPConnection arg0) {
		// TODO Auto-generated method stub
		//Log.e(TAG, "授权成功,user:"+arg0.getUser()+"--serverName:"+arg0.getServiceName());

	}
}
