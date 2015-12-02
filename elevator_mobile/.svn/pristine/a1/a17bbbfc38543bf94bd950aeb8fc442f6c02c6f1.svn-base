package asmack.listener;

import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.packet.Packet;

import android.util.Log;

// 链接的数据包过滤器
public class MyConnectionPacketFilter implements PacketFilter {

	@Override
	public boolean accept(Packet arg0) {
		String from = arg0.getFrom();
		if (from != null && !from.equals("null") && !"".equals(from)) {
			return true;
		} else {
			//Log.e("MyConnectionPacketFilter", "收到空空数据。。。。=====");
			return false;
		}
	}
};