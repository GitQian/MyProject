package asmack.utils;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.SSLContext;

import org.jivesoftware.smack.AccountManager;
import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.ConnectionConfiguration.SecurityMode;
import org.jivesoftware.smack.PacketCollector;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.Roster.SubscriptionMode;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.RosterGroup;
import org.jivesoftware.smack.SASLAuthentication;
import org.jivesoftware.smack.SmackAndroid;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.SmackException.NoResponseException;
import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.XMPPException.XMPPErrorException;
import org.jivesoftware.smack.filter.AndFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.filter.PacketIDFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Registration;
import org.jivesoftware.smack.packet.RosterPacket;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smackx.disco.ServiceDiscoveryManager;
import org.jivesoftware.smackx.disco.packet.DiscoverInfo;
import org.jivesoftware.smackx.disco.packet.DiscoverItems;
import org.jivesoftware.smackx.muc.DiscussionHistory;
import org.jivesoftware.smackx.muc.HostedRoom;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.muc.RoomInfo;
import org.jivesoftware.smackx.offline.OfflineMessageManager;
import org.jivesoftware.smackx.search.ReportedData;
import org.jivesoftware.smackx.search.ReportedData.Row;
import org.jivesoftware.smackx.search.UserSearchManager;
import org.jivesoftware.smackx.vcardtemp.packet.VCard;
import org.jivesoftware.smackx.xdata.Form;
import org.jivesoftware.smackx.xdata.FormField;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;
import asmack.bean.BaseResultInfoVo;
import asmack.bean.ChatRoomGroup;
import asmack.bean.ChatRoomVo;
import asmack.bean.UserVo;
import asmack.listener.MultiPacketListener;
import asmack.listener.MultiPacketListener.OnMucMsgReceiveListener;
import asmack.listener.MyConnectionPacketFilter;
import asmack.listener.MyConnectionPacketListener;
import asmack.listener.MyParticipantStatusListener;
import asmack.listener.MyRosterListener;
import asmack.listener.MyRosterListener.OnRosterChangeListener;
import asmack.listener.MyStatuPacketListener;
import asmack.listener.MyXMMPConnectionListener;
import asmack.listener.P2PChatManagerListener;
import asmack.listener.P2PChatManagerListener.OnMessageReceiveListener;

public class ConnectionUtil {
	private String TAG = this.getClass().getSimpleName();
	private LinkedHashMap<String,OnMessageReceiveListener> onMessageSendListeners;
	private LinkedHashMap<String,OnRosterChangeListener>  onRosterChangeListeners;
	/**群聊消息监听回调*/
	private LinkedHashMap<String,OnMucMsgReceiveListener>  onMucMsgReceiveListeners;
	
	private String host_cache = Common_SMACK.HOST_DEFAULT;
	private int port_cache = Common_SMACK.PORT_DEFAULT;
	private String servername_cache = Common_SMACK.SERVERNAME_DEFAULT;
	
	private static ConnectionUtil mConnectionUtil ;
	private Context mContext;
	private XMPPConnection mConnection;
	private UserVo mUserVo ;
	/**连接状态监听*/
	private MyXMMPConnectionListener mXMMPConnectionListener;
	/**连接包过滤*/
	private MyConnectionPacketFilter mConnectionPacketFilter;
	/**链接的数据包监听*/
	private MyConnectionPacketListener mConnectionPacketListener;
	private RecordUtil mRecordUtil;
	/**单聊监听器*/
	private P2PChatManagerListener mP2pChatManagerListener;
	/**花名册变化的监听器*/
	private MyRosterListener mRosterListener;
	
	/**会议室。缓存*/
	private Map<String,MultiUserChat> mMultiUserChatMap;
	/**群聊信息监听*/
	private MultiPacketListener mMucMessageListener;
	/**群聊时，自身状态监听*/
	private MyStatuPacketListener mMucParticipantListener;
	/**群组成员状态监听，比如 某某 ，被踢了，被禁言了，被授权为管理员了。。。being kicked, banned, or granted admin permissions.*/
	private MyParticipantStatusListener mMucParticipantStatusListener;
	
	static{
		try {
			// asmack本身封装了断线重连机制，但是有一点需要注意
			// 在设置链接属性钱，要加载ReconnectionManager这个类，否则没有效果。
			Class.forName("org.jivesoftware.smack.ReconnectionManager");
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
	
	public static ConnectionUtil getInstance(Context context){
		if(mConnectionUtil == null){
			synchronized (ConnectionUtil.class) {
				if(mConnectionUtil == null){
					mConnectionUtil = new ConnectionUtil(context);
				}
			}
		}
		return mConnectionUtil;
	}
	
	/**群聊消息监听回调*/
	public LinkedHashMap<String, OnMucMsgReceiveListener> getOnMucMsgReceiveListeners() {
		return onMucMsgReceiveListeners;
	}

	public void addOnMucMsgReceiveListeners(OnMucMsgReceiveListener listener) {
		//Log.e(TAG, "class name:"+listener.getClass().getName());
		onMucMsgReceiveListeners.put(listener.getClass().getName(), listener);
	}

	/**花名册变化监听*/
	public void addOnRosterChangeListener(OnRosterChangeListener listener){
		onRosterChangeListeners.put(listener.getClass().getName(), listener);
	}
	
	public LinkedHashMap<String,OnRosterChangeListener> getOnRosterChangeListeners(){
		return onRosterChangeListeners;
	}
	
	/**单聊消息接口*/
	public void addOnMessageReceiveListener(OnMessageReceiveListener listener){
		onMessageSendListeners.put(listener.getClass().getName(), listener);
	}
	/**取消单聊消息接口*/
	public void removeOnMessageReceiveListener(String listener){
		onMessageSendListeners.remove(listener);
	}
	
	public LinkedHashMap<String,OnMessageReceiveListener> getOnMessageReceiveListeners(){
		return onMessageSendListeners;
	}
	
	
	
	private ConnectionUtil(Context context) {
		mContext = context;
		onMessageSendListeners = new LinkedHashMap<String,P2PChatManagerListener.OnMessageReceiveListener>();
		onRosterChangeListeners = new LinkedHashMap<String,MyRosterListener.OnRosterChangeListener>();
		onMucMsgReceiveListeners = new LinkedHashMap<String, MultiPacketListener.OnMucMsgReceiveListener>();
		//一些静态配置。
		SmackAndroid.init(mContext);
		//手动管理添加好友操作。
		Roster.setDefaultSubscriptionMode(SubscriptionMode.manual);
		
		mRecordUtil = RecordUtil.shareRecordUtil(mContext);
		
		mMultiUserChatMap = new LinkedHashMap<String, MultiUserChat>();
		
		mMucMessageListener = new MultiPacketListener(mContext);
		mMucParticipantListener = new MyStatuPacketListener();
		mMucParticipantStatusListener = new MyParticipantStatusListener();
		
		mXMMPConnectionListener = new MyXMMPConnectionListener();
	}
	
	/**当前的登录用户*/
	public UserVo getLoginUser() {
		return mUserVo;
	}

	public boolean isLoginIn(){
		return mConnection!=null && mConnection.isAuthenticated();
	}
	/**
	 * 获取连接。。。
	 * @return
	 */
	private  synchronized XMPPConnection getConnection(){
		return getConnection(host_cache, port_cache, servername_cache);
	}
	
	/**
	 * 不要连接这个。。。
	 * 1.连接服务器, 如果参数给全给null，端给给0，则使用默认的参数。
	 * @param serverName 服务器地址或网络名 如10.1.1.1或主机名 www.baidu.com或 joychinepc
	 * @return
	 */
	private synchronized XMPPConnection getConnection(String host,int port,String serviceName) {
		if(mConnection !=null && mConnection.isConnected())
			return mConnection;
		if(mConnection!=null){
			//Log.e(TAG, "当前mConnection不为null。。。。。。。。。。。先释放前一个对象");
			freeResouce();
		}
		
//	configureConnection(ProviderManager.get);
		
		ConnectionConfiguration config;
//		config = new ConnectionConfiguration((TextUtils.isEmpty(host)?Common_SMACK.HOST_DEFAULT:host),port==0?Common_SMACK.PORT_DEFAULT:port,TextUtils.isEmpty(serviceName)?Common_SMACK.SERVERNAME_DEFAULT:serviceName);
		config = new ConnectionConfiguration(host, port);
		
		config.setRosterLoadedAtLogin(false); //登录后不去获取通信录
		/** 是否启用安全验证 */
		config.setSecurityMode(SecurityMode.disabled);
		config.setCompressionEnabled(false);
		SASLAuthentication.supportSASLMechanism("PLAIN",0);
		// 允许自动连接
		config.setReconnectionAllowed(true);
		// 允许登陆成功后更新在线状态
		config.setSendPresence(true);  //发现一旦设置了这个，为true，在获取离线消息的那个方法中总是不能再获得消息了(离线消息只能在Presence为离线时获取)。。。 如果不发送Presence，是不能接受聊天和 离线消息的。
		/** 是否启用调试 */
		 config.setDebuggerEnabled(true);
		/** 创建connection链接 */
		try {
			// 收到好友邀请后manual表示需要经过同意,accept_all表示不经同意自动为好友
			mConnection = new XMPPTCPConnection(config);
			
			mConnectionPacketFilter = new MyConnectionPacketFilter();
			mConnectionPacketListener = new MyConnectionPacketListener(mConnection,mContext);
			
			mConnection.addPacketListener(mConnectionPacketListener,mConnectionPacketFilter);
			
			mConnection.addConnectionListener(mXMMPConnectionListener);
			
			/** 建立连接 */
			mConnection.connect();
			mConnection.sendPacket(new Presence(Presence.Type.available));
			
			if(!TextUtils.isEmpty(host)){
				host_cache = host;
				port_cache = port;
				servername_cache = serviceName;
			}
		} catch (XMPPException e) {
			e.printStackTrace();
			mConnection = null;
		} catch (SmackException e) {
			e.printStackTrace();
			mConnection = null;
		} catch (IOException e) {
			e.printStackTrace();
			mConnection = null;
		} 
		return mConnection;
	}
	
	
	/**注销登录*/
	public synchronized BaseResultInfoVo loginOut(){
		if(mConnection==null)
			return new BaseResultInfoVo("当前没登陆，无需注销", Common_SMACK.Result.FAILURE);
		try{
//			setPresence(5); //conn.disconnect()中发送了状态包。
			freeResouce();
			return new BaseResultInfoVo("注销登录成功", Common_SMACK.Result.SUCCESS);
		}catch(Exception e){
			e.printStackTrace();
			return new BaseResultInfoVo("注销失败："+e.getMessage(), Common_SMACK.Result.FAILURE);
		}
		
	}
	/**
	 * 断开连接，关闭监听,释放资源
	 */
	public void freeResouce(){
		if(mConnection==null) 
			return;
		if(mP2pChatManagerListener!=null){
			mP2pChatManagerListener.freeResouce();
			ChatManager.getInstanceFor(mConnection).removeChatListener(mP2pChatManagerListener);  
		}
		mConnection.getRoster().removeRosterListener(mRosterListener);
		
		mConnection.removeConnectionListener(mXMMPConnectionListener);
		mConnection.removePacketListener(mConnectionPacketListener);
		for (Map.Entry<String, MultiUserChat> entry : mMultiUserChatMap.entrySet()) {
			entry.getValue().removeMessageListener(mMucMessageListener);
			entry.getValue().removeParticipantListener(mMucParticipantListener);
			entry.getValue().removeParticipantStatusListener(mMucParticipantStatusListener);
		}
		mMultiUserChatMap.clear();
		try {
			mConnection.disconnect(); //这里涉及到网络卡访问。。。
		} catch (NotConnectedException e) {
			e.printStackTrace();
		}
		mConnection = null;
	}
	
	/**
	 * 
	 * 创建连接,登录,并且把单聊监听器加入。
	 * @param username    登录帐号
	 * @param password    登录密码
	 * @return
	 */
	public BaseResultInfoVo login(String username,String password) {
		//Log.e(TAG+"-login", "username:"+username+"--password:"+password);
		try{
			if(getConnection() == null)
				return new BaseResultInfoVo("服务器不可用，连接失败", Common_SMACK.Result.FAILURE);
			if(mConnection.isConnected() && mConnection.isAuthenticated() ){
				/*if(mConnection.isAnonymous()){
					mConnection.disconnect();
					if(getConnection() == null)
						return new BaseResultInfoVo("服务器不可用，连接失败", Common_SMACK.Result.FAILURE);
				}else{
				}*/
				return new BaseResultInfoVo("已经是登录成功状态,海登陆个p啊！", Common_SMACK.Result.SUCCESS) ;
			}
			//先释放上次的连接的资源。
			mConnection.login(username, password);//,StringUtils_SMACK.randomString(8)
			//加入单聊监听器
			mP2pChatManagerListener = new P2PChatManagerListener(mConnection, mContext);
			ChatManager.getInstanceFor(mConnection).addChatListener(mP2pChatManagerListener);
			
			// 添加連接監聽，用自带的算了咯。。
//			mConnectionListener = new MyConnectionListener();
//			mConnection.addConnectionListener(mConnectionListener);
			mRosterListener = new MyRosterListener(mContext);
			mConnection.getRoster().addRosterListener(mRosterListener);
			Presence presence = mConnection.getRoster().getPresence(username);
			
			mUserVo = new UserVo(StringUtils_SMACK.parseBareAddress(mConnection.getUser()),  presence.getType().name(),presence.getStatus());
			return new BaseResultInfoVo("登录成功，用户名【"+username+"】", Common_SMACK.Result.SUCCESS) ;
		} catch (Exception e) {
			e.printStackTrace();
			freeResouce();  //不释放，发现导致后面输入的正确帐号密码都无法登录
			//Log.e(TAG, "login--Exception错误:"+e.getMessage());  //是个null
			if("SASLError using PLAIN: not-authorized".equals(e.getMessage())){
				return new BaseResultInfoVo("用户名或密码错误", Common_SMACK.Result.FAILURE) ;
			}else{
				return new BaseResultInfoVo("登录失败，错误信息："+e.getMessage(), Common_SMACK.Result.FAILURE) ;
			}
		} 
	}
	

	/**
	 * 获取用户信息
	 * 
	 * @param entry
	 * @return
	 */
	private UserVo getUser(RosterEntry entry) {
		UserVo userVo = getUser(entry.getUser());
		if(entry!=null){
			userVo.setNickName(entry.getName());
			userVo.setItemType(entry.getType()==null?null:entry.getType().name());
			userVo.setItemStatus(entry.getStatus()==null?null:entry.getStatus().name());
		}
		return userVo;
	}

	/**
	 * 根据用户名和用户id获得用户  ，type不准，查看源码可知，这个 获取的是历史的在线状态，不准确的。。。
	 * @param userID  用户全民jid
	 * @return 得到不含 NickName，ItemType，ItemStatus字段的UserVo
	 */
	public UserVo getUser( String userID) {
		userID = StringUtils_SMACK.parseName(userID)+"@"+mConnection.getServiceName();
		
		Presence presence = mConnection.getRoster().getPresence(userID);
		String type = presence.getType().toString();  //查看源码可知，这个 获取的是历史的在线状态，不准确的。。。
		String status = presence.getStatus();
		UserVo user = new UserVo();
		user.setType(type);
		user.setStatus(status);
		user.setUserId(userID);
		return user;
	}
	
    /** 
     * 6.删除当前用户 
     * @param connection 
     * @return 
     */  
    public  boolean deleteAccount() {
    	if(getConnection() == null){
    		return false;
    	}
        try {  
        	AccountManager.getInstance(mConnection).deleteAccount();
            return true;  
        } catch (Exception e) {  
            return false;  
        }  
    }
	
	/**
	 * 2.注册， 需要登录后才能注册。
	 * 
	 * @param username 注册帐号
	 * @param password 注册密码
	 */
	public BaseResultInfoVo regist(String username, String password) { 
		try{
			if(getConnection() == null){
				return new BaseResultInfoVo("服务器不可用，连接失败", Common_SMACK.Result.FAILURE);
			}
			if(mConnection.isAuthenticated() && !mConnection.isAnonymous()){
				if(StringUtils_SMACK.parseName(mConnection.getUser()).equals(username)){
					 return new BaseResultInfoVo(/*"当前用户【"+username+"】已登录无需再注册"*/"isexist", Common_SMACK.Result.FAILURE);
				}
			}
			
			
			
			
//			AccountManager m=AccountManager.getInstance(mConnection);
//			System.out.println("========>>"+m.supportsAccountCreation());
//			m.createAccount("xxx", "123456");
			
			
			
			
			
			
			
			
			
			long begin = System.currentTimeMillis();
			//Log.e(TAG, "开始注册前："+begin);
			//①需要先登陆某个测试帐号，然后再进行注册其他新账户的操作。
			Registration reg = new Registration();
			reg.setType(IQ.Type.SET);
			reg.setTo(/*mConnection.getServiceName()*/Common_SMACK.HOST_NAME);
			Map<String, String> attributes = new HashMap<String, String>();
//			Collection<String> keys = AccountManager.getInstance(mConnection).getAccountAttributes();
//	        for (String attributeName : keys) {
//	            attributes.put(attributeName, "");
//	        }
			
//			 <username>xxx</username>
//		        <password>123456</password>
//		        <email></email>
//		        <registered></registered>
//		        <name></name>
//			attributes.put("instructions", "android_smack");
			
		    attributes.put("username", username);  
		    attributes.put("password", password);  
//		    attributes.put("email", "");  
//		    attributes.put("registered", "");  
//			attributes.put("name", ""); 
	        reg.setAttributes(attributes);  
	         
			PacketFilter filter = new AndFilter(new PacketIDFilter(reg.getPacketID()), new PacketTypeFilter(IQ.class));
			PacketCollector collector = mConnection.createPacketCollector(filter);
			mConnection.sendPacket(reg);
			IQ result = (IQ) collector.nextResult(SmackConfiguration.getDefaultPacketReplyTimeout());
			//Log.e(TAG, "注册完成后："+System.currentTimeMillis()+"---用时："+(System.currentTimeMillis()-begin));
			// Stop queuing results
			collector.cancel();// 停止请求results（是否成功的结果）
			if (result == null) {
				//Log.e(TAG, "No response from server.");
			    return new BaseResultInfoVo("网络连接超时", Common_SMACK.NetworkResult.NETWORK_TIMEOUT);
			} else if (result.getType() == IQ.Type.RESULT) {
				return new BaseResultInfoVo("注册成功", Common_SMACK.Result.SUCCESS);
			} else {// if (result.getType() == IQ.Type.ERROR)
				//Log.e(TAG, "--result.getError().toString():"+result.getError().toString());
				if (result.getError().toString().equalsIgnoreCase("conflict")) {
					//Log.e("RegistActivity", "IQ.Type.ERROR: "+ result.getError().toString());
					return new BaseResultInfoVo(/*"注册失败，账户已存在"*/"isexist",Common_SMACK.Result.FAILURE);
				} else {
					//Log.e("RegistActivity", "IQ.Type.ERROR: "+ result.getError().toString());
					return new BaseResultInfoVo("注册失败，错误信息："+result.getError().toString(), Common_SMACK.Result.FAILURE);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			return new BaseResultInfoVo("未知的错误，错误信息："+e.getMessage(), Common_SMACK.Result.FAILURE);
		}
//			return new BaseResultInfoVo("xxxxxxxxxx", Common_SMACK.Result.SUCCESS);
	}
	
	public BaseResultInfoVo changePwd(String password){
		try {
			AccountManager.getInstance(mConnection).changePassword(password);
		} catch (NoResponseException e) {
			e.printStackTrace();
			return new BaseResultInfoVo("响应超时",Common_SMACK.Result.FAILURE);
		} catch (XMPPErrorException e) {
			e.printStackTrace();
			return new BaseResultInfoVo("发生错误，错误信息："+e.getMessage(),Common_SMACK.Result.FAILURE);
		} catch (NotConnectedException e) {
			e.printStackTrace();
			return new BaseResultInfoVo("未与服务器建立连接",Common_SMACK.Result.FAILURE);
		};
		return new BaseResultInfoVo("修改密码成功!",Common_SMACK.Result.SUCCESS);
	}
	
/*	*//**帐号是否已经存在， 不靠谱， 是查询自己而已。。。我艹
	 * @throws NotConnectedException 
	 * @throws XMPPErrorException 
	 * @throws NoResponseException *//*
	private BaseResultInfoVo isAccountExist(String username) {
		try{
			Registration reg = new Registration();
			reg.setTo(mConnection.getServiceName());
			
//        Registration info = (Registration) mConnection.createPacketCollectorAndSend(reg).nextResultOrThrow();
			
			PacketFilter packetFilter = new IQReplyFilter(reg, mConnection);
			// Create the packet collector before sending the packet
			PacketCollector packetCollector = mConnection.createPacketCollector(packetFilter);
			// Now we can send the packet as the collector has been created
			mConnection.sendPacket(reg);
			
			Registration result = (Registration) packetCollector.nextResult(SmackConfiguration.getDefaultPacketReplyTimeout());
			packetCollector.cancel();
			if (result == null) {
				return new BaseResultInfoVo("响应超时",Common_SMACK.Result.FAILURE);
			}
			XMPPError xmppError = result.getError();
			if (xmppError != null) {
				return new BaseResultInfoVo("发生错误，错误信息："+new XMPPErrorException(xmppError).getMessage(),Common_SMACK.Result.FAILURE);
			}
			Map<String, String> attributes = result.getAttributes();
			
			for (Map.Entry<String, String> entry : attributes.entrySet()) {
				//Log.e(TAG, "key:"+entry.getKey()+"--value:"+entry.getValue());
			}
			if(attributes!=null && !TextUtils.isEmpty(attributes.get("username"))){
				return new BaseResultInfoVo("用户【"+username+"】已经存在!", Common_SMACK.Result.FAILURE);
			}
			return new BaseResultInfoVo("用户【"+username+"】可注册", Common_SMACK.Result.SUCCESS);
		} catch (NotConnectedException e) {
			e.printStackTrace();
			return new BaseResultInfoVo("未与服务器建立连接",Common_SMACK.Result.FAILURE);
		} 
	}*/
	
	
	/**
	 * 
	 * 把一个好友添加到一个组中, 只是add 好友到某个组，如果groupname对应的组不存在会创建一个组。
	 * @param user添加的好友账号    the XMPP address of the user (eg "jsmith@example.com"). The address could be in any valid format (e.g. "domain/resource", "user@domain" or "user@domain/resource").
	 * @param groupName
	 */
	public BaseResultInfoVo addUserToGroup( String user,  String groupName) {
		if(getConnection() == null){
			return new BaseResultInfoVo("服务器不可用，连接失败", Common_SMACK.Result.FAILURE);
		}else if (!mConnection.isAuthenticated()) {
			return new BaseResultInfoVo("当前未登录，添加好友失败", Common_SMACK.Result.FAILURE);
		}
		if ( user == null)
			return new BaseResultInfoVo("user不能为null", Common_SMACK.Result.FAILURE);
		
		user = generateJid(user);
		// 将一个rosterEntry添加到group中是PacketCollector，会阻塞线程
		RosterGroup group = mConnection.getRoster().getGroup(groupName);
		RosterEntry entry = mConnection.getRoster().getEntry(user);
		try {
			if (group != null ) {
				if (entry != null){
					group.addEntry(entry);
				}else{
					return new BaseResultInfoVo("好友不存！！error", Common_SMACK.Result.FAILURE);
				}
			} else {
				// 这个组已经存在就添加到这个组，不存在创建一个组
				if (entry != null){
					RosterGroup newGroup = mConnection.getRoster().createGroup(groupName);
					newGroup.addEntry(entry);
				}else{
					return new BaseResultInfoVo("好友不存在，添加到【"+groupName+"】组失败", Common_SMACK.Result.FAILURE);
				}
			}
			BaseResultInfoVo BaseResultInfoVo = new BaseResultInfoVo("添加好友"+user+"到"+groupName+"分组成功", Common_SMACK.Result.SUCCESS);
			return BaseResultInfoVo;
		} catch (NoResponseException e) {
			e.printStackTrace();
			return new BaseResultInfoVo("服务器响应超时："+e.getMessage(), Common_SMACK.Result.FAILURE);
		} catch (XMPPErrorException e) {
			e.printStackTrace();
			return new BaseResultInfoVo("添加好友"+user+"到"+groupName+"分组失败"+e.getMessage(), Common_SMACK.Result.FAILURE);
		} catch (NotConnectedException e) {
			e.printStackTrace();
			return new BaseResultInfoVo("与服务器的连接不可用:"+e.getMessage(), Common_SMACK.Result.FAILURE);
		}
	}

	/**转化成jid的格式    转化成 name@domain  格式
	 * 如 joychine 转化成joychine@joychinepc.com ，如果已经是jid格式，不受影响的。
	 * @param user
	 * @return
	 */
	private String generateJid(String user) {
		user = StringUtils_SMACK.parseName(user)+"@"+mConnection.getServiceName();
		return user;
	}
	
	/**
	 * 添加好友 无分组,  测试可用。
	 * @param user the user. (e.g. johndoe@jabber.org)， 实际测试 给username也行。。。 因为就是给什么就加什么。。
	 * @param nickname the nickname of the user
	 * @return
	 */
	public BaseResultInfoVo addUser(String user, String nickname) {
		return addUser(user, nickname, null);
	}

	/**
	 * 添加好友 有分组, 如果groupname为空则添加到无分组列表，如果用户好友已存在就不在添加，但会更新昵称nickname和分组。<br/>
	 * <b>注意这个添加好友真的只是添加好友，不会管这个好友是不是真的存在，所以在添加好友前需要搜索好友。。。</b>
	 * @param user  the user. (e.g. johndoe@jabber.org)
	 * @param nickname   好友的昵称
	 * @param groupName  给空字符串或者null 表示不添加到任何组里。如果目前在某个分组里会从分组中移除。
	 * @return
	 */
	public BaseResultInfoVo addUser(String user, String nickname, String groupName) {
		try {
//			Presence subscription = new Presence(Presence.Type.subscribed);
//			subscription.setTo(user);
//			user += "@" + getConnection().getServiceName();
//			getConnection().sendPacket(subscription);
			
			user = generateJid(user);
			getConnection().getRoster().createEntry(user, nickname,new String[] { groupName });
			return new BaseResultInfoVo("添加好友【"+user+"】成功!", Common_SMACK.Result.SUCCESS);
		} catch (Exception e) {
			e.printStackTrace();
			return new BaseResultInfoVo("添加好友失败:"+e.getMessage(), Common_SMACK.Result.FAILURE);
		}
	}
	
	/**
	 * 移动  某些好友 到 某些组里(注意：这里是移动，意味着从A组移动到B组后A组里的user会删除，此外每个好友是可以属于多个分组的)
	 * @param users			要移动的user
	 * @param groupName  	准备把user移动到该分组中。 如果该值给 ""或者是 null，意味着把 users从原来的分组中移除到默认分组。
	 * @return
	 */
	public BaseResultInfoVo moveUserToGroup(List<String> users,String groupName){
		if(users==null ){
			throw new IllegalArgumentException("users不能为null");
		}
		if(getConnection() == null){
			return new BaseResultInfoVo("服务器不可用，连接失败", Common_SMACK.Result.FAILURE);
		}
		if(!mConnection.isAuthenticated())
			return new BaseResultInfoVo("登录状态丢失,请重新登录", Common_SMACK.Result.FAILURE);
		try{
			if (mConnection.isAnonymous()) {
	            throw new IllegalStateException("Anonymous users can't have a roster.");
	        }
			
			for (String user : users) {
				user = generateJid(user);
				RosterEntry entry = mConnection.getRoster().getEntry(user);
				if(entry == null){
					//Log.e(TAG, "实体为nul。。。！！！！！！");
					continue;
				}
				// Create and send roster entry creation packet.
				RosterPacket rosterPacket = new RosterPacket();
				rosterPacket.setType(IQ.Type.SET);
				RosterPacket.Item item = new RosterPacket.Item(user, entry.getName());
				if(!TextUtils.isEmpty(groupName)) {
					item.addGroupName(groupName);
				}
				rosterPacket.addRosterItem(item);
				long begin = System.currentTimeMillis();
				Log.v(TAG, "开始移动好友前："+begin);
				mConnection.createPacketCollectorAndSend(rosterPacket).nextResultOrThrow();
				Log.v(TAG, "移动好友："+user+"--耗时："+(System.currentTimeMillis()-begin));
			}
			
			return new BaseResultInfoVo("移动好友成功",Common_SMACK.Result.SUCCESS);
		}catch(Exception e){
			e.printStackTrace();
			return new BaseResultInfoVo("移动好友失败，错误信息："+e.getMessage(),Common_SMACK.Result.FAILURE);
		}
	}
	
	/**
	 * 移动  某些好友 到 某些组里(注意：这里是移动，意味着从A组移动到B组后A组里的user会删除，此外每个好友是可以属于多个分组的)
	 * @param rosterUsers			要移动的user
	 * @param rosterGroup  	准备把user移动到该分组中。 如果该值给 ""或者是 null，意味着把 users从原来的分组中移除到默认分组。
	 * @return
	 */
	public BaseResultInfoVo moveUserToGroup(Collection<RosterEntry> rosterUsers,String rosterGroup){
		List<String> users = new ArrayList<String>();
		for (RosterEntry rosterEntry : rosterUsers) {
			users.add(rosterEntry.getUser());
		}
		return moveUserToGroup(users, rosterGroup);
	}

	/**
	 * 允许添加好友,响应申请者，回复一条状态消息。
	 * @param from
	 */
	public BaseResultInfoVo allowAddFriend(String from) {
		if(getConnection() == null){
			return new BaseResultInfoVo("服务器不可用，连接失败", Common_SMACK.Result.FAILURE);
		}
		if(!mConnection.isAuthenticated())
			return new BaseResultInfoVo("登录状态丢失,请重新登录", Common_SMACK.Result.FAILURE);
		try {
			Presence presence = new Presence(Presence.Type.subscribed);
			presence.setTo(from);
			mConnection.sendPacket(presence);
			return new BaseResultInfoVo("已发送回复信息", Common_SMACK.Result.SUCCESS);
		} catch (NotConnectedException e) {
			e.printStackTrace();
			return new BaseResultInfoVo("发送回复信息失败，信息："+e.getMessage(), Common_SMACK.Result.FAILURE);
		}
	}
	
	public BaseResultInfoVo refuseAddFriend(String from){
		if(getConnection() == null){
			return new BaseResultInfoVo("服务器不可用，连接失败", Common_SMACK.Result.FAILURE);
		}
		if(!mConnection.isAuthenticated())
			return new BaseResultInfoVo("登录状态丢失,请重新登录", Common_SMACK.Result.FAILURE);
		try {
			Presence presence = new Presence(Presence.Type.unsubscribed);
			presence.setTo(from);
			mConnection.sendPacket(presence);
			return new BaseResultInfoVo("已发送回复信息", Common_SMACK.Result.SUCCESS);
		} catch (NotConnectedException e) {
			e.printStackTrace();
			return new BaseResultInfoVo("发送回复信息失败，信息："+e.getMessage(), Common_SMACK.Result.FAILURE);
		}
	}
	
	/**取消对某个好友的 ”添加请求“改为，”被拒绝“*/
	@Deprecated
	public BaseResultInfoVo syncFriendItemState2Refuse(RosterEntry entry){
		try{
//			RosterPacket packet = new RosterPacket();
//			packet.setType(IQ.Type.SET);
//			
//			RosterPacket.Item item = new RosterPacket.Item(entry.getUser(), entry.getName());
////			item.setItemStatus(ItemStatus.UNSUBSCRIPTION_PENDING);
//			item.setItemStatus(null);
//			// Set the correct group names for the item.
//			for (RosterGroup group : entry.getGroups()) {
//				item.addGroupName(group.getName());
//			}
//			
////			item.setItemType(RosterPacket.ItemType.none);
//			item.setItemType(null);
//			packet.addRosterItem(item);
//			mConnection.createPacketCollectorAndSend(packet).nextResultOrThrow();
			
			Presence p = new Presence(Presence.Type.unsubscribed);
			p.setTo(entry.getUser());
			mConnection.sendPacket(p);
					
			return new BaseResultInfoVo("更新好友状态为被拒绝状态", Common_SMACK.Result.SUCCESS);
		}catch (NotConnectedException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	/**
	 * 3.发消息给好友
	 * @param with_user  对方账号 ， name最简形式， 如：joychine1
	 * @param msg  内容
	 * @return
	 */
	public BaseResultInfoVo sendMessage(String with_user, String msg) {
		if(getConnection() == null){
			return new BaseResultInfoVo("服务器不可用，连接失败", Common_SMACK.Result.FAILURE);
		}
		if(!mConnection.isAuthenticated())
			return new BaseResultInfoVo("登录状态丢失,请重新登录", Common_SMACK.Result.FAILURE);
		
		try {
			String key = StringUtils_SMACK.parseBareAddress(mConnection.getUser()) + "-" + StringUtils_SMACK.parseName(with_user);
			
			Chat chat = mP2pChatManagerListener.getChat(key);
			if (chat == null) {
				//Log.e(TAG, "sendMessage--没有chat。。key:"+key);
				String account = StringUtils_SMACK.parseName(with_user) + "@" + mConnection.getServiceName();
				chat = ChatManager.getInstanceFor(mConnection).createChat(account, null);
//				mP2pChatManagerListener.getChatMap().put(key, chat);    会自动添加的。。。
				if(StringUtils_SMACK.parseName(mConnection.getUser()) .equals(StringUtils_SMACK.parseName(with_user))){//自己发给自己
					//Log.e(TAG, "当前登录用户和接受消息用户相同都是："+StringUtils_SMACK.parseName(with_user));
				}
				//Log.e(TAG, "当前登录用户："+StringUtils_SMACK.parseName(mConnection.getUser())+"--接受消息用户："+StringUtils_SMACK.parseName(with_user));
			}else{
				//Log.e(TAG, "sendMessage--已经缓存了。。chat。。");
			}
		    chat.sendMessage(msg);
		} catch (Exception e) {
		    e.printStackTrace();
		    return new BaseResultInfoVo("发送消息失败："+e.getMessage(), Common_SMACK.Result.FAILURE);
		}  
		return new BaseResultInfoVo("发送消息成功",Common_SMACK.Result.SUCCESS);
	}
	
	/**
	 * 发消息到聊天室
	 * @param roomName  房间名 
	 * @param msg
	 */
	public BaseResultInfoVo sendMessageToChatRoom(String roomName, String msg) {
		if(getConnection() == null){
			return new BaseResultInfoVo("服务器不可用，连接失败", Common_SMACK.Result.FAILURE);
		}
		if(!mConnection.isAuthenticated())
			return new BaseResultInfoVo("登录状态丢失,请重新登录", Common_SMACK.Result.FAILURE);
		
		try {
			MultiUserChat muc = mMultiUserChatMap.get(generateRoomKey(generateRoomId(roomName)));
			if(muc == null){
				return new BaseResultInfoVo("请想加入聊天室先，发送消息失败", Common_SMACK.Result.FAILURE);
			}
			
	        
			muc.sendMessage(msg);
			return new BaseResultInfoVo("群聊消息发送成功", Common_SMACK.Result.SUCCESS);
		} catch (Exception e) {
			e.printStackTrace();
			return new BaseResultInfoVo("群聊发送消息失败："+e.getMessage(), Common_SMACK.Result.FAILURE);
		}
	}
	
	
	/**
	 * 获取离线消息 ， 不需要用它，每次上线都会自动获取离线消息然后自动删除服务器端的。 而且这玩意只能在”离线状态去查询“才有返回。
	 * @return
	 * @throws XMPPException
	 * @throws NotConnectedException 
	 * @throws NoResponseException 
	 */
	public boolean getOfflineMesssage() throws XMPPException, NoResponseException, NotConnectedException {
		OfflineMessageManager offlineManager = new OfflineMessageManager(mConnection);
		 boolean b = offlineManager.supportsFlexibleRetrieval();
		 Log.i(TAG+"getOfflineMesssage()", "=====是否支持离线消息========"+b);
		// //获取支持灵活的检索状态，正常应该是为true，个人理解为服务器的离线消息功能支持开关
		Log.i(TAG+"getOfflineMesssage()", "离线消息数: " + offlineManager.getMessageCount());

		boolean result = true;
		 long begin = System.currentTimeMillis();
		List<Message> msgList = offlineManager.getMessages(); //获取服务器上的历史消息耗时：5034 差不多5秒！！！ 数据多耗时可能更长
		
		//Log.e(TAG, "获取服务器上的历史消息耗时："+(System.currentTimeMillis() - begin));
		
		long begin1 = System.currentTimeMillis();
		SQLiteDatabase db = mRecordUtil.getDb();
		db.beginTransaction();
	    try {
			for (Message message : msgList) {
				Log.i(TAG, "离线信息message："+message);
				//入数据库
			}
	       db.setTransactionSuccessful();
	    } finally {
	       db.endTransaction();
	    }
	    //Log.e(TAG, "把离线信息插入到数据库中耗时："+(System.currentTimeMillis() - begin1));
	   
//		offlineManager.deleteMessages(); // 上报服务器已获取，需删除服务器备份，不然下次登录会重新获取， 需要耗时 62毫秒, 好像又不需要删除了。。。
		// Presence presence = new Presence(Presence.Type.available);//
		// 此时再上报用户状态
		// connection.sendPacket(presence);
		
//		setPresence(3);
		return result;
	}
	
	/**
	 * 更改用户状态
	 * @throws NotConnectedException 
	 */
	public void setPresence(int code) throws NotConnectedException {
		if (getConnection() == null)
			return;
		Presence presence;
		switch (code) {
		case 0:
			presence = new Presence(Presence.Type.available);
			mConnection.sendPacket(presence);
			Log.v("state", "设置在线");
			break;
		case 1:
			presence = new Presence(Presence.Type.available);
			presence.setMode(Presence.Mode.chat);
			long beginDo = System.currentTimeMillis();
			//Log.e(TAG, "发送packet前："+beginDo);
			mConnection.sendPacket(presence); 
			//Log.e(TAG, "发送packet后,毫秒耗时："+(System.currentTimeMillis() -beginDo ));  //才3。。。
			Log.v("state", "设置Q我吧");
			System.out.println(presence.toXML());
			break;
		case 2:
			presence = new Presence(Presence.Type.available);
			presence.setMode(Presence.Mode.dnd);
			mConnection.sendPacket(presence);
			Log.v("state", "设置忙碌");
			System.out.println(presence.toXML());
			break;
		case 3:
			presence = new Presence(Presence.Type.available);
			presence.setMode(Presence.Mode.away);
			mConnection.sendPacket(presence);
			Log.v("state", "设置离开");
			System.out.println(presence.toXML());
			break;
		case 4:
			Roster roster = mConnection.getRoster();
			Collection<RosterEntry> entries = roster.getEntries();
			for (RosterEntry entry : entries) {
				presence = new Presence(Presence.Type.unavailable);
				presence.setPacketID(Packet.ID_NOT_AVAILABLE);
				presence.setFrom(mConnection.getUser());
				presence.setTo(entry.getUser());
				mConnection.sendPacket(presence);
				System.out.println(presence.toXML());
			}
			// 向同一用户的其他客户端发送隐身状态
			presence = new Presence(Presence.Type.unavailable);
			presence.setPacketID(Packet.ID_NOT_AVAILABLE);
			presence.setFrom(mConnection.getUser());
			presence.setTo(StringUtils_SMACK.parseBareAddress(mConnection.getUser()));
			mConnection.sendPacket(presence);
			Log.v("state", "设置隐身");
			break;
		case 5:
			presence = new Presence(Presence.Type.unavailable);
			mConnection.sendPacket(presence);
			Log.v("state", "设置离线");
			break;
		default:
			break;
		}
	}
	
	/**
	 * 修改密码
	 * 
	 * @param newPwd  需要更新的新密码
	 * @return
	 */
	public boolean changePassword(String newPwd) {
		try {
			if(getConnection()!=null && getConnection().isAuthenticated()){
				
				AccountManager.getInstance(mConnection).changePassword(newPwd);
			}
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	
//	/**
//	 * 获得一个 群聊对象。 存在就返回，不存在就创建
//	 * @param roomName 房间名
//	 * @param password 房间密码， 给null为不设置密码
//	 * @return
//	 * @throws SmackException 
//	 * @throws XMPPErrorException 
//	 */
//	public MultiUserChat getMultiUserChat(String roomName,String password) {
//		String roomID = generateRoomId(roomName);
//		String key = generateRoomKey(roomID);
//		MultiUserChat muc = mMultiUserChatMap.get(key);
//		if(muc == null){
//			Log.i(TAG	, "群聊对象 不存在，构建："+muc);
//			muc = createRoomChat(roomName, mConnection.getUser()+"创建的房间，这是描述", password);
//		}else{
//			Log.i(TAG	, "群聊对象已经 缓存存在："+muc);
//		}
//		return muc;
//	}

	/**生成map的key*/
	private String generateRoomKey(String roomID) {
		String key = StringUtils_SMACK.parseBareAddress(mConnection.getUser()) + "-" + roomID;
		return key;
	}
	
	/**生成放假缓存 roomID*/
	private String generateRoomId(String roomName){
		String roomID = roomName + "@conference." + getConnection().getServiceName();
		return roomID;
	}
	
	/**
	 * 删除一个聊天室,前提的事你是这个房间的owner。
	 * 首先加入房间，然后摧毁
	 * @param roomName  房间名
	 * @param password  房间如果有密码销毁前要提供。
	 * @param reason    销毁房间的原因。
	 * @return
	 */
	public BaseResultInfoVo destoryRoom(String roomName,String password,String reason){
		try{
			if(getConnection() == null){
				return new BaseResultInfoVo("服务器不可用，连接失败", Common_SMACK.Result.FAILURE);
			}else if (!mConnection.isAuthenticated()) {
				return new BaseResultInfoVo("当前未登录，删除聊天室失败", Common_SMACK.Result.FAILURE);
			}
			
			String roomID = generateRoomId(roomName);
			String key = generateRoomKey(roomID);
			MultiUserChat muc = mMultiUserChatMap.get(key);
			if(muc!=null){
				muc.destroy(TextUtils.isEmpty(reason)?"群主无心经营，解散该群":reason, null);
				return new BaseResultInfoVo("群主关闭群成功",Common_SMACK.Result.SUCCESS);
			}else{//加入该组先
				MultiUserChat chat = joinMultiUserChat(roomName, StringUtils_SMACK.parseName(mConnection.getUser()), password);
				chat.destroy(TextUtils.isEmpty(reason)?"群主无心经营，解散该群":reason, null);
				
				return new BaseResultInfoVo("群主关闭群成功",Common_SMACK.Result.SUCCESS);
			}
			
		} catch (NoResponseException e) {
			e.printStackTrace();
			return new BaseResultInfoVo("服务器无响应",Common_SMACK.Result.FAILURE);
		} catch (XMPPErrorException e) {
			e.printStackTrace();
			//Forbidden" error (403).
			boolean isOk = "forbidden".equals(e.getMessage());
			String msg = "错误:"+e.getMessage();
			if(isOk)
				msg = "您无权利取消该会议室";
			return new BaseResultInfoVo(msg,Common_SMACK.Result.FAILURE);
		} catch (NotConnectedException e) {
			e.printStackTrace();
			return new BaseResultInfoVo("连接未建立",Common_SMACK.Result.FAILURE);
		}
	}
	
	/**离开某个聊天室*/
	public BaseResultInfoVo leaveRoom(String roomName) {
		try {
			if(getConnection() == null){
				return new BaseResultInfoVo("服务器不可用，连接失败", Common_SMACK.Result.FAILURE);
			}else if (!mConnection.isAuthenticated()) {
				return new BaseResultInfoVo("当前未登录，离开聊天室失败", Common_SMACK.Result.FAILURE);
			}
			
			String roomID = generateRoomId(roomName);
			String key = generateRoomKey(roomID);
			MultiUserChat muc = mMultiUserChatMap.get(key);
			if (muc != null) {
				muc.leave();
				mMultiUserChatMap.remove(key);
				return new BaseResultInfoVo("离开聊天室成功",Common_SMACK.Result.SUCCESS);
			}else{
				return new BaseResultInfoVo("聊天室不存在",Common_SMACK.Result.FAILURE);
			}
		} catch (NotConnectedException e) {
			e.printStackTrace();
			return new BaseResultInfoVo("服务器无响应",Common_SMACK.Result.FAILURE);
		}
	}
	/**
	 * 
	 * @param roomName 房间名称， 必须保证是唯一的。
	 * @param roomdesc 房间的描述信息
	 * @param password
	 * @return
	 * @throws XMPPErrorException 
	 * @throws SmackException 
	 */
	public MultiUserChat createRoomChat(String roomName, String roomdesc,String password)  {
		if (getConnection() == null || !mConnection.isAuthenticated())
			return null;
		
		MultiUserChat muc = null;
		try{
			// 使用XMPPConnection创建一个MultiUserChat
			String roomID = generateRoomId(roomName);
			
			//Log.e(TAG, "z准备创建房间,roomID:"+roomID+"--roomName:"+roomName+"--roomdesc:"+roomdesc);
			muc = new MultiUserChat(mConnection, roomID);
			
			// 群聊信息监听
			muc.addMessageListener(mMucMessageListener);
			// 个人状态监听
			muc.addParticipantListener(mMucParticipantListener);
			// 群组成员状态监听
			muc.addParticipantStatusListener(mMucParticipantStatusListener);
			
			// 创建聊天室
			muc.create(roomName);
			// 获得聊天室的配置表单
			Form form = muc.getConfigurationForm();
			// 根据原始表单创建一个要提交的新表单。
			Form submitForm = form.createAnswerForm();
			// 向要提交的表单添加默认答复
			List<FormField> formFieldList = form.getFields();
			for (FormField formField : formFieldList) {
				if (!FormField.TYPE_HIDDEN.equals(formField.getType()) && formField.getVariable() != null) {
					// 设置默认值作为答复
					submitForm.setDefaultAnswer(formField.getVariable());
				}
			}
			// 设置聊天室的新拥有者
			List<String> owners = new ArrayList<String>();
			owners.add(mConnection.getUser());// 用户JID
			submitForm.setAnswer("muc#roomconfig_roomowners", owners);
			
			// 可以更改主题
			submitForm.setAnswer("muc#roomconfig_changesubject", true);
			// 房间描述
			submitForm.setAnswer("muc#roomconfig_roomdesc", roomdesc);
			// 设置聊天室是持久聊天室，即将要被保存下来
			submitForm.setAnswer("muc#roomconfig_persistentroom", true);
			// 房间仅对成员开放
			submitForm.setAnswer("muc#roomconfig_membersonly", false);
			// 允许占有者邀请其他人
			submitForm.setAnswer("muc#roomconfig_allowinvites", true);
			if (!TextUtils.isEmpty(password)) {
				// 进入是否需要密码
				submitForm.setAnswer("muc#roomconfig_passwordprotectedroom",true);
				// 设置进入密码
				submitForm.setAnswer("muc#roomconfig_roomsecret", password);
			}else{
				submitForm.setAnswer("muc#roomconfig_passwordprotectedroom",false);
			}
			// 能够发现占有者真实 JID 的角色
			// submitForm.setAnswer("muc#roomconfig_whois", "anyone");
			// 登录房间对话
			submitForm.setAnswer("muc#roomconfig_enablelogging", true);
			// 仅允许注册的昵称登录
			submitForm.setAnswer("x-muc#roomconfig_reservednick", true);
			// 允许使用者修改昵称
			submitForm.setAnswer("x-muc#roomconfig_canchangenick", true);
			// 允许用户注册房间
			submitForm.setAnswer("x-muc#roomconfig_registration", true);
			// 发送已完成的表单（有默认值）到服务器来配置聊天室
			muc.sendConfigurationForm(submitForm);
			
			mMultiUserChatMap.put(generateRoomKey(roomID), muc);
//			if (password != null) {
//				muc.join(mConnection.getUser(), password);
//			} else {
//				muc.join(mConnection.getUser());
//			}
		}catch(Exception e){
			e.printStackTrace();
			muc = null;
		}
		
		return muc;
	}
	
	/**
	 * 创建并且加入会议室， 可多次加入，执行的操作是 离开再加入，昵称改变。
	 * @param roomName 会议室名
	 * @param nickname 昵称,群聊使用的。
	 * @param password 会议室密码
	 */
	public MultiUserChat joinMultiUserChat(String roomName,String nickname, String password) {
		try {
			if(getConnection() == null){
				return null;
			}
			boolean isExist = false;
			ArrayList<ChatRoomGroup> roomGroups = getRoomList();
			lable: for (ChatRoomGroup chatRoomGroup : roomGroups) {
				for(ChatRoomVo vo: chatRoomGroup.getRoomList()){
					 isExist = vo.getName().equals(roomName);
					if(isExist){
						break lable;
					}
				}
			}
			
			if(!isExist){ //服务器上不存在该 roomName的聊天室。
				return null;
			}else{
				MultiUserChat muc = mMultiUserChatMap.get( generateRoomKey(generateRoomId(roomName)) ) ;
				if( muc==null ) {
					muc = new MultiUserChat(mConnection, generateRoomId(roomName));  //创建一个聊天室，使用默认的配置。
					// 群聊信息监听
					muc.addMessageListener(mMucMessageListener);
					// 个人状态监听
					muc.addParticipantListener(mMucParticipantListener);
					// 群组成员状态监听
					muc.addParticipantStatusListener(mMucParticipantStatusListener);
					
					mMultiUserChatMap.put(generateRoomKey(generateRoomId(roomName)), muc);
				}else{
					System.out.println("已经在会议室了，还加入干嘛~"); //换昵称？
				}
				// 聊天室服务将会决定要接受的历史记录数量
				DiscussionHistory history = new DiscussionHistory();
				history.setMaxStanzas(0);
				//history.setSince(new Date());
				// 用户加入聊天室
				muc.join(TextUtils.isEmpty(nickname)?StringUtils_SMACK.parseName(mConnection.getUser()):nickname, password, history, mConnection.getPacketReplyTimeout());
				System.out.println("会议室加入成功........");
				return muc;
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("会议室加入失败........");
			return null;
		}
	}
	
	/**
	 * 查询会议室中的所有成员
	 * @param muc
	 */
	public List<String> findMulitUser(MultiUserChat muc){
		List<String> listUser = new ArrayList<String>();
		List<String> it = muc.getOccupants();
		//遍历出聊天室人员名称
		for (String string : it) {
			// 聊天室成员名字
			String name = StringUtils_SMACK.parseName(string);
			listUser.add(name);
		}
		return listUser;
	}
	
	/**
	 * 获取服务器上所有的聊天室以及房间
	 * 
	 * @return
	 * @throws Exception
	 */
	public ArrayList<ChatRoomGroup> getRoomList()  {
		ArrayList<ChatRoomGroup> list = new ArrayList<ChatRoomGroup>();
		// 查找服务
		try{
			List<String> col = getConferenceServices(mConnection.getServiceName(),	mConnection);
			for (String aCol : col) {
				String service = aCol;
				// 查询服务器上的聊天室
				Collection<HostedRoom> rooms = MultiUserChat.getHostedRooms(mConnection, service);
				ArrayList<ChatRoomVo> roomlist = new ArrayList<ChatRoomVo>();
				for (HostedRoom room : rooms) {
					// 查看Room消息
					// System.out.println(room.getName() + "-" + room.getJid());
					// 记录MultiUserChat对象
					// getMultiUserChat(room.getJid());
					// 获取房间信息
					RoomInfo roomInfo = MultiUserChat.getRoomInfo(mConnection,room.getJid());
					ChatRoomVo chatroom = new ChatRoomVo();
					chatroom.setName(room.getName());
					chatroom.setJid(room.getJid());
					if (roomInfo != null) {
						chatroom.setSubject(roomInfo.getSubject());
						chatroom.setDescription(roomInfo.getDescription());
						chatroom.setPasswordProtected(roomInfo.isPasswordProtected());
						chatroom.setOccupantsCount(roomInfo.getOccupantsCount());;
						//Log.e(TAG+"getRoomList()",	"房间描述："+roomInfo.getDescription() + " 房间人数："+ roomInfo.getOccupantsCount());
						
					}
					roomlist.add(chatroom);
				}
				list.add(new ChatRoomGroup(aCol, roomlist));
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * 获取聊天室服务器
	 * 
	 * @param server
	 * @param connection
	 * @return
	 * @throws Exception
	 */
	public List<String> getConferenceServices(String server,XMPPConnection connection) throws Exception {
		List<String> answer = new ArrayList<String>();
		ServiceDiscoveryManager discoManager = ServiceDiscoveryManager.getInstanceFor(connection);
		DiscoverItems items = discoManager.discoverItems(server);
		for (DiscoverItems.Item it : items.getItems()) {
			DiscoverItems.Item item = it;
			if (item.getEntityID().startsWith("conference")	|| item.getEntityID().startsWith("private")) {
				answer.add(item.getEntityID());
			} else {
				try {
					DiscoverInfo info = discoManager.discoverInfo(item.getEntityID());
					if (info.containsFeature("http://jabber.org/protocol/muc")) {
						answer.add(item.getEntityID());
					}
				} catch (XMPPException e) {
					e.printStackTrace();
				}
			}
		}
		return answer;
	}
	
/*	*//**
	 * 获取服务器上所有会议室
	 * @return
	 * @throws XMPPException
	 * @throws NotConnectedException 
	 * @throws NoResponseException 
	 *//*
	public List<ChatRoomVo> getRooms()  {
		List<ChatRoomVo> list = new ArrayList<ChatRoomVo>();
		try{
	//		ServiceDiscoveryManager.getInstanceFor(mConnection);
			if (!MultiUserChat.getHostedRooms(mConnection,	mConnection.getServiceName()).isEmpty()) {
				
				for (HostedRoom k : MultiUserChat.getHostedRooms(mConnection,mConnection.getServiceName())) {
					
					for (HostedRoom j : MultiUserChat.getHostedRooms(mConnection,k.getJid())) {
						RoomInfo info2 = MultiUserChat.getRoomInfo(mConnection,	j.getJid());
						if (j.getJid().indexOf("@") > 0) {
							
							ChatRoomVo friendrooms = new ChatRoomVo();
							friendrooms.setName(j.getName());//聊天室的名称
							friendrooms.setJid(j.getJid());//聊天室JID
							friendrooms.setOccupantsCount(info2.getOccupantsCount());//聊天室中占有者数量
							friendrooms.setDescription(info2.getDescription());//聊天室的描述
							friendrooms.setSubject(info2.getSubject());//聊天室的主题
							list.add(friendrooms);
						}else{
							//Log.e(TAG, "来鬼了，会议室没有@符号。。");
						}
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return list;
	}*/
	
	
	/**
	 * 获取所有好友信息，entry的itemrype必须是 to 或者是 both才能显示在好友列表中，就是只有A加B，B同意了，这种单向好友，或者双方互为好友才显示在好友列表中。
	 * @return
	 */
	public List<UserVo> getAllEntries() {
		List<UserVo> entryList = new ArrayList<UserVo>();
		if (getConnection() !=null ){
			Collection<RosterEntry> rosterEntry = mConnection.getRoster().getEntries();
			for (RosterEntry entry : rosterEntry) {
				Log.v(TAG, "===好友rosterEntry===，entry.name:"+entry.getName()+"--entry.user:"+entry.getUser()+"--ItemType:"+entry.getType()+"--ItemStatus:"+entry.getStatus()+"--groupds:"+entry.getGroups());
//				if(entry.getType() == RosterPacket.ItemType.to || entry.getType() == RosterPacket.ItemType.both)
			    entryList.add(getUser(entry));
			}
			
		}
		return entryList;
	}
	
	
	/** 获取某个组里面的所有好友 , entry的itemrype必须是 to 或者是 both才能显示在好友列表中，就是只有A加B，B同意了，这种单向好友，或者双方互为好友才显示在好友列表中。
    * @param roster 
    * @param groupName       组名 
    * @return 
    */  
   public List<RosterEntry> getEntriesByGroup(Roster roster,   String groupName) {
       List<RosterEntry> Entrieslist = new ArrayList<RosterEntry>();  
       RosterGroup rosterGroup = roster.getGroup(groupName);  
       Collection<RosterEntry> rosterEntry = rosterGroup.getEntries();  
		for (RosterEntry entry : rosterEntry) {
//			if (entry.getType() == RosterPacket.ItemType.to	|| entry.getType() == RosterPacket.ItemType.both) 
				Entrieslist.add(entry);
		}
       return Entrieslist;  
   }  
	
	 /**
	 	获取所有组 的对象(组对象是包含RosterEntry对象集合的)，entry的itemrype必须是 to 或者是 both才能显示在好友列表中，就是只有A加B，B同意了，这种单向好友，或者双方互为好友才显示在好友列表中。否则应该属于垃圾数据。
     * @param roster 
     * @return 所有组集合 
     */  
    public List<RosterGroup>  getGroups() {
    	List<RosterGroup> grouplist = new ArrayList<RosterGroup>();  
    	if (getConnection() == null || !mConnection.isAuthenticated()) {
			return grouplist;
		} 
        Collection<RosterGroup> rosterGroups = mConnection.getRoster().getGroups(); 
//        //Log.e(TAG, "没有过滤前的分组个数："+rosterGroups.size());
        for (RosterGroup rosterGroup : rosterGroups) {
        	 Collection<RosterEntry> rosterEntrys = rosterGroup.getEntries();
//        	 boolean isRosterEntryOk = false;
        	 for (RosterEntry rosterEntry : rosterEntrys) {
        		 //Log.e(TAG+"-getGroups()", "entry:"+rosterEntry);
//        		 
//				if(rosterEntry.getType() == RosterPacket.ItemType.to || rosterEntry.getType() == RosterPacket.ItemType.both){
//					isRosterEntryOk = true;
//					break;
//				}
			}
//        	if(isRosterEntryOk)
    		grouplist.add(rosterGroup);
		}
        //Log.e(TAG, "共有分组个数："+grouplist.size());
        return grouplist;
    }
    
    /** 添加一个分组 , Note: you must add at least one entry to the group for the group to be kept
     * after a logout/login. This is due to the way that XMPP stores group information.
    * @param roster 
    * @param groupName 
    * @return 
    */  
   public BaseResultInfoVo addGroup(String groupName) {
       try {
			if (getConnection() == null) {
				return new BaseResultInfoVo("服务器不可用，连接失败",	Common_SMACK.Result.FAILURE);
			} else if (!mConnection.isAuthenticated()) {
				return new BaseResultInfoVo("当前未登录，新增分组失败",Common_SMACK.Result.FAILURE);
			}
			if (groupName == null) {
				return new BaseResultInfoVo("groupName不能为null",Common_SMACK.Result.FAILURE);
			}
			RosterGroup rosterGroup = mConnection.getRoster().getGroup(groupName);
			if (rosterGroup != null) {
				return new BaseResultInfoVo("【" + groupName + "】组已经存在，无需新增",Common_SMACK.Result.FAILURE);
			}
			rosterGroup = mConnection.getRoster().createGroup(groupName);
			RosterEntry entry = mConnection.getRoster().getEntry(generateJid("默认人"));
			if (entry == null) {
				entry = createEntry(3);
			}
			if(entry == null){
				return new BaseResultInfoVo("添加分组【"+groupName+"】失败，超时", Common_SMACK.Result.FAILURE);
			}
			rosterGroup.addEntry(entry); // 分组下必须存在一个好友才能保证该分组不被删除。
			return new BaseResultInfoVo("添加分组【" + groupName + "】成功",Common_SMACK.Result.SUCCESS);
       } catch (Exception e) { //if logged in anonymously
           e.printStackTrace();  
           return new BaseResultInfoVo("添加分组【"+groupName+"】失败，错误信息："+e.getMessage(), Common_SMACK.Result.FAILURE);
       }  
   }
   
   /**麻痹的服务器数据返回不是同步的*/
   private RosterEntry createEntry(int tryNum) {
	   RosterEntry entry = null;
	   try{
		 if (tryNum > 0) {
			 Log.v(TAG, "创建RosterEntry，尝试次数："+tryNum);
	        // Create and send roster entry creation packet.
	        RosterPacket rosterPacket = new RosterPacket();
	        rosterPacket.setType(IQ.Type.SET);
	        RosterPacket.Item item = new RosterPacket.Item(generateJid("默认人"), "默认人");
	        rosterPacket.addRosterItem(item);
	        mConnection.createPacketCollectorAndSend(rosterPacket).nextResultOrThrow();
	        
//			mConnection.getRoster().createEntry(getJid("默认人"),"默认人", null); 这个会有好友添加请求的操作。 
			entry = mConnection.getRoster().getEntry(generateJid("默认人"));
			if(entry == null){
				tryNum--;
				Thread.sleep(100);
				return createEntry(tryNum);
			}
		 }
	   }catch(Exception e){
		   e.printStackTrace();
	   }
	   return entry;
   }
   
	/**
	 * 删除分组,  ①如果没有好友会自动被删除，根本不用调用任何方法。 ②如果好友存在还要删除该分组，那么所有的好友移动到默认分组去。 
	 * @param groupName
	 * @return
	 */
	public BaseResultInfoVo removeGroup(String groupName) {
		if(getConnection() == null){
			return new BaseResultInfoVo("服务器不可用，连接失败", Common_SMACK.Result.FAILURE);
		}
		if(!mConnection.isAuthenticated())
			return new BaseResultInfoVo("登录状态丢失,请重新登录", Common_SMACK.Result.FAILURE);
		if(groupName == null){
			return new BaseResultInfoVo("当前已经是默认分组，不能删除", Common_SMACK.Result.FAILURE);
		}
		Collection<RosterEntry>  entrys = mConnection.getRoster().getGroup(groupName).getEntries();
		//Log.e(TAG, "准备删除的分组groupname："+groupName+"--它下面有实体个数："+entrys.size());
		
		return moveUserToGroup(entrys, null);
	}
	
   /** 获取用户VCard信息 
   * @param connection 
   * @param user 
   * @return 
   * @throws XMPPException 
   * @throws NotConnectedException 
   * @throws NoResponseException 
   */  
  public static VCard getUserVCard(XMPPConnection connection, String user)   throws XMPPException, NoResponseException, NotConnectedException {  
      VCard vcard = new VCard();  
      vcard.load(connection, user);  
      return vcard;  
  }
	  
/*	  *//** 获取用户头像信息 
	  *  
	  * @param connection 
	  * @param user 
	  * @return 
	  *//*  
	 public static Drawable getUserImage(XMPPConnection connection, String user) {  
	     ByteArrayInputStream bais = null;  
	     try {  
	         VCard vcard = new VCard();  
	         // 加入这句代码，解决No VCard for  
	         ProviderManager.getInstance().addIQProvider("vCard", "vcard-temp",  
	                 new org.jivesoftware.smackx.provider.VCardProvider());  
	
	         vcard.load(connection, user+"@"+connection.getServiceName());  
	
	         if (vcard == null || vcard.getAvatar() == null)  
	             return null;  
	         bais = new ByteArrayInputStream(vcard.getAvatar());  
	
	     } catch (Exception e) {  
	         e.printStackTrace();  
	     }  
	     if (bais == null)  
	         return null;  
	     return FormatTools.getInstance().InputStream2Drawable(bais);  
	 }  
	 
	 *//*** 修改用户头像 
     *  
     * @param connection 
     * @param f 
     * @throws XMPPException 
     * @throws IOException 
     *//*  
    public static void changeImage(XMPPConnection connection, File f)  
            throws XMPPException, IOException {
  
        VCard vcard = new VCard();  
        vcard.load(connection);  
  
        byte[] bytes;  
  
        bytes = getFileBytes(f);  
        String encodedImage = StringUtils_SMACK.encodeBase64(bytes);  
        vcard.setAvatar(bytes, encodedImage);  
        vcard.setEncodedImage(encodedImage);  
        vcard.setField("PHOTO", "<TYPE>image/jpg</TYPE><BINVAL>" + encodedImage  
                + "</BINVAL>", true);  
  
        ByteArrayInputStream bais = new ByteArrayInputStream(vcard.getAvatar());  
        FormatTools.getInstance().InputStream2Bitmap(bais);  
  
        vcard.save(connection);  
    }*/
 
	/** 删除好友 
	 *  
	 * @param roster 
	 * @param user  the XMPP address of the user (eg "jsmith@example.com"). The address could be in any valid format (e.g. "domain/resource", "user@domain" or "user@domain/resource").
user参数可以随意。。与addUser时保持一致格式就可。
	 * @return 
	 */  
	public  BaseResultInfoVo removeUser(String user) {
	    try {
	    	if(getConnection() == null){
	    		return new BaseResultInfoVo("服务器不可用，连接失败", Common_SMACK.Result.FAILURE);
	    	}
	    	if(mConnection.isAuthenticated()==false){
	    		return new BaseResultInfoVo("当前未登录，删除好友失败", Common_SMACK.Result.FAILURE);
	    	}
	        user = generateJid(user);
	        RosterEntry entry = mConnection.getRoster().getEntry(user);  
	        if(entry == null){
	        	return new BaseResultInfoVo("删除好友失败,好友已不存在", Common_SMACK.Result.FAILURE);
	        }
	        
	        //Log.e(TAG, "removeUser删除好友：" + user);  
	        mConnection.getRoster().removeEntry(entry);  
	        return new BaseResultInfoVo("删除好友【"+user+"】成功!", Common_SMACK.Result.SUCCESS);
	    } catch (Exception e) {  
	        e.printStackTrace();  
	        return new BaseResultInfoVo("删除好友失败，信息："+e.getMessage(), Common_SMACK.Result.FAILURE);
	    }  
	}  
	
	
	
	/**
	 * 查询用户
	 * 
	 * @param userName 
	 * @return
	 * @throws XMPPException
	 */
	public List<HashMap<String, String>> searchUsers(String userName) {
		if (getConnection() == null)
			return null;
		HashMap<String, String> user = null;
		List<HashMap<String, String>> results = new ArrayList<HashMap<String, String>>();
		try {
//			ServiceDiscoveryManager discoveryManger = ServiceDiscoveryManager.getInstanceFor(mConnection);

			UserSearchManager usm = new UserSearchManager(mConnection);
			
			Log.v(TAG, "---mConnection	.getServiceName():"+mConnection	.getServiceName());

			Form searchForm = usm.getSearchForm(mConnection.getServiceName());//一定要用域，不能用ip ，如"10.10.20.55" 不行，joychinepc可以 ， 这行报错：feature-not-implemented
			Form answerForm = searchForm.createAnswerForm();
			answerForm.setAnswer("Username", true);
			answerForm.setAnswer("search", userName);
			
			answerForm.setAnswer("userAccount", true);
			answerForm.setAnswer("userPhote", userName);
			ReportedData data = usm.getSearchResults(answerForm, "search."	+ mConnection.getServiceName());

			 List<Row> list = data.getRows();
			 for (Row row : list) {
				 List<String> values = row.getValues("userAccount");
				 for (String string : values) {
					//Log.e(TAG, "userAccount:"+string);
				}
				user = new HashMap<String, String>();
				user.put("userAccount", row.getValues("userAccount").get(0).toString());
				user.put("userPhote", row.getValues("userPhote").get(0)	.toString());
				results.add(user);
				// 若存在，则有返回,UserName一定非空，其他两个若是有设，一定非空
			 }
			 
		} catch (XMPPException e) {
			e.printStackTrace();
		} catch (NoResponseException e) {
			e.printStackTrace();
		} catch (NotConnectedException e) {
			e.printStackTrace();
		}
		return results;
	}

	/**
	 * 修改心情
	 * @param connection
	 * @param status
	 * @throws NotConnectedException 
	 */
	public void changeStateMessage(String status) throws NotConnectedException {
		if (getConnection() == null)
			return;
		Presence presence = new Presence(Presence.Type.available);
		presence.setStatus(status);
		getConnection().sendPacket(presence);
	}
 
  
  
}
