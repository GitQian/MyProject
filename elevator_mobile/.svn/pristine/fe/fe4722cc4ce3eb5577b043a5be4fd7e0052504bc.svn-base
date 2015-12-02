package asmack.listener;
import java.util.List;

import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence;

import android.content.Context;
import android.util.Log;
import asmack.bean.UserVo;
import asmack.utils.StringUtils_SMACK;
import asmack.utils.UserManagerBz;
import asmack.utils.UserManagerBz.OnFriendChangeListener;

/**
 * 链接的数据包监听
 */
public class MyConnectionPacketListener implements PacketListener {
	private String TAG = this.getClass().getSimpleName();
	private Context mContext;
	private XMPPConnection mConnection;
	
	
	public MyConnectionPacketListener(XMPPConnection mConnection,Context context) {
		this.mConnection = mConnection;
		this.mContext = context;
	}

	@Override
	public void processPacket(Packet packet) {
		if (packet instanceof Presence) {
			Presence p = (Presence) packet;
			String from = p.getFrom();
			String status = p.getStatus();
			Presence.Type type = p.getType();
			//Log.e("MyConnectionPacketListener","Presence,from:" + p.getFrom() + " status:"	+ p.getStatus() + " isAvailable:" + p.isAvailable()	+ " type:" + p.getType());
			
			UserVo fromUserVo = new UserVo();
			fromUserVo.setUserId(StringUtils_SMACK.parseBareAddress(from));
			fromUserVo.setType(type.toString());
			fromUserVo.setStatus(status);
			
			RosterEntry entry = mConnection.getRoster().getEntry(fromUserVo.getUserId());
			//Log.e(TAG+"--processPacket", "entry:"+entry); //得到null， 有时候是null。。。
			
			if(entry!=null){
				fromUserVo.setItemType(entry.getType()==null?null:entry.getType().name());
				fromUserVo.setItemStatus(entry.getStatus()==null?null:entry.getStatus().name());
				fromUserVo.setNickName(entry.getName());
			}
			
			// 添加好友邀请
			if (!from.contains("@conference") && !from.contains("@private")) {
//						String with_user =StringUtils_SMACK.parseName(from);
//						UserVo user = mConnectionUtil.getUser(with_user);
				if (p.isAvailable()) {
//				    UserManager.shareFriendManager().userOnline(user);
					Log.v("MyConnectionPacketListener", "用户上线了user："+fromUserVo);  //自己上线也会说。。。
					List<OnFriendChangeListener> listeners = UserManagerBz.getInstance().getOnFriendChangeListeners();
					for (OnFriendChangeListener onFriendChangeListener : listeners) {
						onFriendChangeListener.onPresenceChange(fromUserVo, p);
					}
				} else if(type == Presence.Type.unavailable){ //离线。
					List<OnFriendChangeListener> listeners = UserManagerBz.getInstance().getOnFriendChangeListeners();
					for (OnFriendChangeListener onFriendChangeListener : listeners) {
						onFriendChangeListener.onPresenceChange(fromUserVo, p);
					}
//							UserManager.shareFriendManager().userOffline(user);
					Log.v("MyConnectionPacketListener", "用户离线了user："+fromUserVo);
				} else if (type == Presence.Type.subscribe) {//请求添加好友的数据类型
					Log.v(TAG, fromUserVo+"请求添加我为好友,from:"+packet.getFrom()+"--to:"+packet.getTo());
					List<OnFriendChangeListener> listeners = UserManagerBz.getInstance().getOnFriendChangeListeners();
					for (OnFriendChangeListener onFriendChangeListener : listeners) {
						onFriendChangeListener.onRequest(fromUserVo, p.getType());
					}
					//允许添加好友
//					mConnectionUtil.allowAddFriend(from);
//					mConnectionUtil.addUserToGroup(from, "group001");
				} else if (type == Presence.Type.subscribed) { //同意我添加对方为好友的数据类型。
					Log.v(TAG, fromUserVo+"同意被我添加为好友的操作,from:"+packet.getFrom()+"--to:"+packet.getTo());
					List<OnFriendChangeListener> listeners = UserManagerBz.getInstance().getOnFriendChangeListeners();
					for (OnFriendChangeListener onFriendChangeListener : listeners) {
						onFriendChangeListener.onRequest(fromUserVo, p.getType());
					}
					
					
				} else if (type == Presence.Type.unsubscribe) { //请求删除好友关系，  本地不会被删除只是 RosterEntry的type字段变成了额 none，意味着双方不在关心对方的Presence状态。这个接口将不会收到上线消息
					Log.v(TAG, fromUserVo+"请求unsubscribe我这个好友,from:"+packet.getFrom()+"--to:"+packet.getTo()); //会自动回应对方 同意被删除的请求的。
					//更新 请求itemstatue为null才行。表达被拒绝意义。
/*					long begin  =System.currentTimeMillis();
					ConnectionUtil.getInstance(mContext).syncFriendItemState2Refuse(entry);
					System.out.println("更新好友状态为被拒绝耗时："+(System.currentTimeMillis() - begin));
					
					List<OnFriendChangeListener> listeners = UserManagerBz.getInstance().getOnFriendChangeListeners();
					for (OnFriendChangeListener onFriendChangeListener : listeners) {
						onFriendChangeListener.onRequest(fromUserVo, p.getType());
					}*/
					
//					BaseResultInfoVo vo = ConnectionUtil.getInstance(mContext).removeUser(from); //如果对方把你删了，你可以也把对方删除。
					
				} else if (type == Presence.Type.unsubscribed) { //同意删除好友关系。
					Log.v(TAG, fromUserVo+"已经同意我要删除他这个好友的请求,from:"+packet.getFrom()+"--to:"+packet.getTo()); 
					List<OnFriendChangeListener> listeners = UserManagerBz.getInstance().getOnFriendChangeListeners();
					for (OnFriendChangeListener onFriendChangeListener : listeners) {
						onFriendChangeListener.onRequest(fromUserVo, p.getType());
					}
					
					
				} else if (type == Presence.Type.error) { //有错误的一条状态数据。
					Log.v(TAG, fromUserVo+"有错误的一条状态数据,from:"+packet.getFrom()+"--to:"+packet.getTo()+"错误信息："+p.getError());
					List<OnFriendChangeListener> listeners = UserManagerBz.getInstance().getOnFriendChangeListeners();
					for (OnFriendChangeListener onFriendChangeListener : listeners) {
						onFriendChangeListener.onRequest(fromUserVo, p.getType());
					}
					
				}
				
				
			}else{//处理群好友的状态变化。 这个接口 MyStatuPacketListener已经处理了
				
			}
		} else if (packet instanceof Message) {
			Message m = (Message) packet;
			Log.i("MyConnectionPacketListener", "arg0 instanceof Message！！===Message==from:" + m.getFrom() + " body:" + m.getBody());
		}
		
	}
};