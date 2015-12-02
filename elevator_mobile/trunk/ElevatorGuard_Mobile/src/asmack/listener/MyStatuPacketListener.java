package asmack.listener;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence;

import android.util.Log;

/**
 * 群聊时，状态监听。。。
 */
public class MyStatuPacketListener implements PacketListener {
	private String TAG = this.getClass().getSimpleName();

	@Override
	public void processPacket(Packet packet) {
		// 线上--------------chat
		// 忙碌--------------dnd
		// 离开--------------away
		// 隐藏--------------xa
		Presence presence = (Presence) packet;
		// PacketExtension pe = presence.getExtension("x",
		// "http://jabber.org/protocol/muc#user");
		String fromUser = presence.getFrom();
		String kineName = fromUser.substring(fromUser.indexOf("/") + 1);
		String stats = "";
		//Log.e("MyStatuPacketListener", "群聊成员状态变化fromUser："+fromUser+"，presence:" + presence);

		//Log.e(TAG, "presence.mode:"+presence.getMode());
		if(presence.getMode() == null){
			return;
		}
		if ("chat".equals(presence.getMode().toString())) {
			stats = "[线上]";
		}
		if ("dnd".equals(presence.getMode().toString())) {
			stats = "[忙碌]";
		}
		if ("away".equals(presence.getMode().toString())) {
			stats = "[离开]";
		}
		if ("xa".equals(presence.getMode().toString())) {
			stats = "[隐藏]";
		}
		
		//Log.e(TAG,"群聊时，"+fromUser+"的状态："+stats);

		// for (int i = 0; i < affiliates.size(); i++) {
		// String name = affiliates.get(i);
		// if (kineName.equals(name.substring(name.indexOf("]") + 1))) {
		// affiliates.set(i, stats + kineName);
		// System.out.println("状态改变成：" + affiliates.get(i));
		// android.os.Message msg = new android.os.Message();
		// msg.what = MEMBER;
		// handler.sendMessage(msg);
		// break;
		// }
		// }

	}
};