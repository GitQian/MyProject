package asmack.listener;
import java.util.LinkedHashMap;
import java.util.Map;

import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;

import android.content.Context;
import asmack.utils.ConnectionUtil;

/**
 * 会议室信息监听器。<br/>  会议室消息懒得去保存了。 
 * multiChat.addMessageListener(new MultiPacketListener());<br/>
 * <strong>multiChat.sendMessage(str);//multiChat </strong>为聊天室对象
 * 
 * @author Administrator
 * 
 */
public class MultiPacketListener implements PacketListener {
	private String TAG = this.getClass().getSimpleName();
	private Context mContext;
	
	public MultiPacketListener(Context mContext) {
		this.mContext = mContext;
	}

	public interface OnMucMsgReceiveListener{
		public void onMesageReceive(String from,String to,String msg);
	}
	
	@Override
	public void processPacket(Packet packet) {
		// 接收来自聊天室的聊天信息
		Message message = (Message) packet; // 接收来自聊天室的聊天信息
		
		
		// String from = connectionServer.getUserName(message.getFrom());
		// String login_user = connectionServer.loginUser.getUserName();
		// from:needpsw@conference.aji/user002
		// body:消息
		//Log.e(TAG + "-MultiPacketListener", "会议室：" + message.getFrom() + ":" + message.getBody());
		LinkedHashMap<String,OnMucMsgReceiveListener> listeners = ConnectionUtil.getInstance(mContext).getOnMucMsgReceiveListeners();
		for (Map.Entry<String, OnMucMsgReceiveListener> entry : listeners.entrySet()) {
			entry.getValue().onMesageReceive(packet.getFrom(), packet.getTo(), message.getBody());
		}
		
	}
}