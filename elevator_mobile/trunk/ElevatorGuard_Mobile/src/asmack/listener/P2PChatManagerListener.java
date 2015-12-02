package asmack.listener;

import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManagerListener;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.util.StringUtils;
import org.json.JSONObject;

import com.chinacnit.elevatorguard.mobile.ui.activity.MessageActivity;
import com.chinacnit.elevatorguard.mobile.ui.activity.MessageNetActivity;
import com.google.gson.Gson;
import com.ztkj.db.DbOperater;
import com.ztkj.db.bean.MessageBean;
import com.ztkj.service.PushInfo;
import com.ztkj.tool.CommUtil;
import com.ztkj.tool.Config;
import com.ztkj.tool.NotifyUtil;
import com.ztkj.tool.NotifyUtil.NotifyConfig;
import com.ztkj.tool.Tool;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import asmack.utils.ConnectionUtil;

/**
 * 单人聊天信息监听类， 无论是离线消息还是什么，都会被调用的。 也就是说可以不去刻意的获取离线消息？
 * @author Administrator
 */
public class P2PChatManagerListener implements ChatManagerListener {
	private String TAG = this.getClass().getSimpleName();
//	private static P2PChatManagerListener mP2PChatListener;
	private XMPPConnection xmppConnection;
	private Context context;
	/**单聊的对象映射，key为 loginuser+withuser，*/
	private Map<String, Chat> mChatMap;
	private List<MyMessageListener> mP2PMsgListener;
	
	
	public P2PChatManagerListener(XMPPConnection xmppConnection, Context context) {
		this.context = context;
		this.xmppConnection = xmppConnection;
		this.mChatMap = new LinkedHashMap<String, Chat>();
		this.mP2PMsgListener = new LinkedList<P2PChatManagerListener.MyMessageListener>();
	}
	
	/**
	 * 回调函数，不要调用。。。
	 */
	@Override
	public void chatCreated(Chat chat, boolean isCreateByLocal) {
////Log.e(TAG+"-chatCreated", "创建的chat："+chat+"--xmppConnection.getUser():"+xmppConnection.getUser());		//创建的chat：Chat [(participant=joychine1@joychinepc), (thread=80efdb2a-0d63-4b54-a5c9-447f4900c937)]--xmppConnection.getUser():joychine1@joychinepc/Smack
		String key = StringUtils.parseBareAddress(xmppConnection.getUser())+"-"+StringUtils.parseName(chat.getParticipant());
		
		mChatMap.put(key, chat);
		MyMessageListener listener = new MyMessageListener(isCreateByLocal);
		chat.addMessageListener(listener);
		mP2PMsgListener.add(listener);
		
		//Log.e(TAG, "mChatMap.size():"+mChatMap.size());
		if(isCreateByLocal==false){
			//Log.e(TAG, "chat被创建了~~~，而且还是远程的，，，，key:"+key);
		}else{
			//Log.e(TAG, "-------------------chat被创建了~~~，本地创建！！！！----key："+key);
		}
	}
	
	public Chat getChat(String key){
		return mChatMap.get(key);
	}
	
	/**消息处理接口， 这里自己发出去的居然不被调用……*/
	private class MyMessageListener implements MessageListener{
		private boolean isCreateByLocal;
		public MyMessageListener(boolean isCreateByLocal) {
			this.isCreateByLocal = isCreateByLocal;
		}
		@Override
		public void processMessage(Chat chat, Message message) {
			//登录用户
			String userName = StringUtils.parseName(xmppConnection.getUser());
			//发送消息用户
			String msgFrom = message.getFrom();
			//消息内容
			String body = message.getBody();
			if(body == null){//用户在输入时，也会传过来状态，但是body是null
				Log.v(TAG,msgFrom+"正在输入...");
				return;
			}
			
			//Log.e(TAG+"processMessage","--isCreateByLocal:"+isCreateByLocal+"--userName:"+userName+"-msgFrom:"+msgFrom+"--body:"+body);//isCreateByLocal:false--userName:joychine1-msgFrom:joychine@joychinepc/Spark 2.6.3--body:22334455
//			//Log.e(TAG, "消息对象message："+message);//消息对象message：<message id='2385O-51' to='joychine1@joychinepc' from='joychine@joychinepc/Spark 2.6.3' type='chat'><body>22334455</body><thread>7boDBA</thread><x xmlns="jabber:x:event"><offline/><composing/></x><delay xmlns="urn:xmpp:delay" stamp="2015-04-22T06:17:24.553+00:00" from="joychinepc"></delay><x xmlns="jabber:x:delay" stamp="20150422T06:17:24" from="joychinepc"></x></message>
			LinkedHashMap<String,OnMessageReceiveListener> listeners =  ConnectionUtil.getInstance(context).getOnMessageReceiveListeners();
			for (Map.Entry<String, OnMessageReceiveListener> entry : listeners.entrySet()) {
				entry.getValue().onMessageReceive(message);
			}
			
			Log.e("======================>>>", "======================>>>收到消息******"+message.getBody());
			MessageBean msgBean =new Gson().fromJson(body, MessageBean.class);
			DbOperater.getInstance().addMessage(msgBean);
			
			SharedPreferences sp = CommUtil.getInstance(context).getSharePre(Config.MSG_NOTIFY_CONFIG, Context.MODE_PRIVATE);
			//通知栏通知
			if(!CommUtil.getInstance(context).isAppOnForeground()){
//				//Log.e(TAG, "耗时选寻应用："+(System.currentTimeMillis() - begin)); //仅仅4毫秒，
				NotifyConfig config = new NotifyConfig();
				if(!sp.getBoolean(Config.IS_MSG_TROUBLE, true) && !isInNormalTime()){
					return;
				}else{
					config.tickerText =Tool.StringNull(msgBean.getTitle(), "");
					config.contentText = Tool.StringNull(msgBean.getContent(), "");
					if(!sp.getBoolean(Config.IS_MSG_SHOWCONTENT, true)){
						config.tickerText = "收到一条新消息";
						config.contentText = "收到一条新消息";
					}
					config.contentTitle =Tool.StringNull(msgBean.getTitle(), "");
					config.isSound = sp.getBoolean(Config.IS_MSG_SOUND, true);
					config.isVibrate = sp.getBoolean(Config.is_MSG_VIBRATE, true);
					config.isLights = sp.getBoolean(Config.is_MSG_LIGHTS, true);
					
					Intent i = new Intent(context, MessageNetActivity.class);
//					Intent i = new Intent(context, MessageActivity.class);
//					i.putExtra(Config.TAG, bean);
//				i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP );
					i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK );//先摧毁，再创建，ok
					config.pd = PendingIntent.getActivity(context, 0x1103, i, PendingIntent.FLAG_ONE_SHOT);
					NotifyUtil.getInstance(context).notify(config);
				}
			}else{
				if(sp.getBoolean(Config.IS_MSG_SHOWDIALOG, true)){
					Intent intent =new Intent(context,PushInfo.class);
					intent.putExtra("title", msgBean.getTitle());
					intent.putExtra("content", msgBean.getContent());
					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					context.startActivity(intent);
				}
			}
			
		}
		
		
		private boolean isInNormalTime() { //23:00~8:00 不提示notify
			Calendar cd = Calendar.getInstance();
			long now = cd.getTimeInMillis();
			int year = cd.get(Calendar.YEAR);
			int month = cd.get(Calendar.MONTH);
			int day = cd .get(Calendar.DAY_OF_MONTH);
			cd.clear();
			cd.set(year, month, day, 23, 0);
			long endTime = cd.getTimeInMillis();
			
			cd.clear();
			cd.set(year, month, day, 8, 0);
			long beginTime = cd.getTimeInMillis();
			
			return beginTime<= now && now<= endTime;
		}
	}

	/**
	 * 释放资源，清空字段缓存。 这个connection一般需要重新new了
	 */
	public void freeResouce() {
		mChatMap.clear();
		mP2PMsgListener.clear();
	}
	
	/**
	 * 收到消息时的接口。 还是 单聊的，群里没测试过
	 * @author liucheng  liucheng187@qq.com
	 */
	public static interface OnMessageReceiveListener{
		/**
		 * 当收到聊天消息时。 单聊
		 * @param mesage
		 */
		public void onMessageReceive(Message message);
	}
}
