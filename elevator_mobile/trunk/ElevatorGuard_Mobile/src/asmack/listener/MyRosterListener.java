package asmack.listener;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import org.jivesoftware.smack.RosterListener;
import org.jivesoftware.smack.packet.Presence;

import android.content.Context;
import android.util.Log;
import asmack.utils.ConnectionUtil;
/**
 * 花名册监听！！！ 用来异步更新需要 花名册的那些界面
 * @author liucheng  liucheng187@qq.com
 */
public class MyRosterListener implements RosterListener{
	private String TAG = this.getClass().getSimpleName();
	private Context mContext;
	/**监听花名册监听*/
	public interface OnRosterChangeListener{
		/**
		 * 花名册变化了
		 * @param addresses 花名册 好友的 地址，the collection of address of the added/update/del contacts.
		 */
		public void onRosterChange(Collection<String> addresses);
	}
	
	public MyRosterListener(Context context) {
		this.mContext = context;
	}

	@Override
	public void entriesAdded(Collection<String> addresses) {
		deal(addresses);
	}

	private void deal(Collection<String> addresses) {
		LinkedHashMap<String,OnRosterChangeListener> listeners = ConnectionUtil.getInstance(mContext).getOnRosterChangeListeners();
		for (Map.Entry<String, OnRosterChangeListener> entry : listeners.entrySet()) {
			if(entry.getValue()!=null)
				entry.getValue().onRosterChange(addresses);;
		}
	}

	@Override
	public void entriesUpdated(Collection<String> addresses) {
		deal(addresses);
	}

	@Override
	public void entriesDeleted(Collection<String> addresses) {
		deal(addresses);
	}

	/**这个状态我用不到，我在MyConnectionPacketListener 接口中处理了*/
	@Override
	public void presenceChanged(Presence presence) {
		Log.v(TAG,"状态变化了，，花名册的presence:"+presence);
	}

}
