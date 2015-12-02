package asmack.utils;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.jivesoftware.smack.packet.Presence;

import asmack.bean.UserVo;

/**
 * 好友关系管理
 * @author liucheng  liucheng187@qq.com
 */
public class UserManagerBz {
	private static UserManagerBz mBz;
	public static final synchronized UserManagerBz getInstance(){
		if(mBz == null){
			mBz = new UserManagerBz();
		}
		return mBz;
	}
	
	private List<OnFriendChangeListener> listeners =  new CopyOnWriteArrayList<OnFriendChangeListener>();;

	public void addOnFriendChangeListener(OnFriendChangeListener listener){
		if(!listeners.contains(listener)){
			listeners.add(listener);
		}
	}
	
	public void removeOnFriendChangeListener(OnFriendChangeListener listener){
		listeners.remove(listener);
	}
	
	public List<OnFriendChangeListener> getOnFriendChangeListeners(){
		return Collections.unmodifiableList(listeners);
	}
	
	/**好友的状态改变监听器*/
	public static interface OnFriendChangeListener{
		/**
		 * 好友状态 改变。
		 * @param userVo 用户对象。
		 * @param p 状态所代表的类。
		 */
		public void onPresenceChange(UserVo userVo,Presence p);
		
		/**当好友请求来到
		 * 
		 * @param userVo  用户对象。
		 * @param type    Presence.Type的枚举值
		 */
		public void onRequest(UserVo userVo,Presence.Type type);
	}
}
