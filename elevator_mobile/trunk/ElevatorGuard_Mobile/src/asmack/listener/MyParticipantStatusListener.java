package asmack.listener;
import org.jivesoftware.smackx.muc.ParticipantStatusListener;

import android.util.Log;

/**
 * 群成员状态监听,会议室状态监听事件，谁来了，谁走了<br/>
 * multiChat.addParticipantStatusListener(new MyParticipantStatusListener());
 * @author Administrator
 */
public class MyParticipantStatusListener implements ParticipantStatusListener {
	private String TAG = this.getClass().getSimpleName();
	@Override
	public void adminGranted(String arg0) {
		Log.i(TAG, "执行了adminGranted方法:" + arg0);
	}

	@Override
	public void adminRevoked(String arg0) {
		Log.i(TAG, "执行了adminRevoked方法:" + arg0);
	}

	@Override
	public void banned(String arg0, String arg1, String arg2) {
		Log.i(TAG, "执行了banned方法:" + arg0);
	}

	@Override
	public void joined(String arg0) {
		Log.i(TAG, "执行了joined方法:" + arg0 + "加入了房间");
		// getAllMember();
		// android.os.Message msg = new android.os.Message();
		// msg.what = MEMBER;
		// handler.sendMessage(msg);
	}

	@Override
	public void kicked(String arg0, String arg1, String arg2) {
		Log.i(TAG, "执行了kicked方法:" + arg0 + "被踢出房间");
	}

	@Override
	public void left(String arg0) {
		String lefter = arg0.substring(arg0.indexOf("/") + 1);
		Log.i(TAG, "执行了left方法:" + lefter + "离开的房间");
		// getAllMember();
		// android.os.Message msg = new android.os.Message();
		// msg.what = MEMBER;
		// handler.sendMessage(msg);
	}

	@Override
	public void membershipGranted(String arg0) {
		Log.i(TAG, "执行了membershipGranted方法:" + arg0);
	}

	@Override
	public void membershipRevoked(String arg0) {
		Log.i(TAG, "执行了membershipRevoked方法:" + arg0);
	}

	@Override
	public void moderatorGranted(String arg0) {
		Log.i(TAG, "执行了moderatorGranted方法:" + arg0);
	}

	@Override
	public void moderatorRevoked(String arg0) {
		Log.i(TAG, "执行了moderatorRevoked方法:" + arg0);
	}

	@Override
	public void nicknameChanged(String arg0, String arg1) {
		Log.i(TAG, "执行了nicknameChanged方法:" + arg0);
	}

	@Override
	public void ownershipGranted(String arg0) {
		Log.i(TAG, "执行了ownershipGranted方法:" + arg0);
	}

	@Override
	public void ownershipRevoked(String arg0) {
		Log.i(TAG, "执行了ownershipRevoked方法:" + arg0);
	}

	@Override
	public void voiceGranted(String arg0) {
		Log.i(TAG, "执行了voiceGranted方法:" + arg0);
	}

	@Override
	public void voiceRevoked(String arg0) {
		Log.i(TAG, "执行了voiceRevoked方法:" + arg0);
	}
};