package asmack.utils;

import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import asmack.bean.ChatMsgPo;

public class RecordUtil {
	private String TAG = this.getClass().getSimpleName();
	// 聊天记录数据库名
	private static final String DB_NAME = "chatrecord.db";

	// ===========================消息表开始================================//
	private static final String TABLE_CHATMSG = "chatmsg";
	// id字段
	private static final String KEY_ID = "_id";
	//消息发送方 jid的 username那部分
	private static final String KEY_FROM = "_from";
	//消息接收方 
	private static final String KEY_TO = "_to";
	//消息内容
	private static final String KEY_BODY = "_body";
	//消息接收时的时间(这个暂时是本地时间，其实最好是服务器上的时间)
	private static final String KEY_TIME = "_time";
	//该条消息是否已读，0表示未读(false),1表示已读(true)
	private static final String KEY_STATE = "_state";
	//type类型决定是 line-by-line单聊还是 group chats群聊, 0表示单聊，1表示群聊
	private static final String KEY_TYPE = "_type";
	//消息类型  1：inbox 2：sent 3：draft
	private static final String KEY_MSGTYPE = "_msgType";
	

	private static RecordUtil util = null;
	private Context mContext;
	private DbHelper mDbHelper;
	private SQLiteDatabase db;

	private RecordUtil(Context context) {
		mContext = context;
		mDbHelper = new DbHelper(context);
		openDatabase();
	}

	public static synchronized RecordUtil shareRecordUtil(Context context) {
		if (util == null) {
			util = new RecordUtil(context);
		}
		return util;
	}

	private class DbHelper extends SQLiteOpenHelper {

		public DbHelper(Context context) {
			super(context, DB_NAME, null, 1);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			String sql_create_chatmsg = "create table "+TABLE_CHATMSG+"("+KEY_ID+" integer primary key autoincrement,"
										+KEY_FROM+" text,"+KEY_TO+" text,"+KEY_BODY+" text,"
										+KEY_TIME+" integer,"+KEY_STATE+" integer default (0),"
										+KEY_TYPE+" integer,"+KEY_MSGTYPE+" integer)";
			db.execSQL(sql_create_chatmsg);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			
		}
		
	}

	public SQLiteDatabase getDb() {
		return db;
	}

	/**
	 * 打开数据库
	 */
	private void openDatabase() {
		db = mDbHelper.getWritableDatabase();
	}

	/**
	 * 关闭数据库
	 */
	public void closeDatabase() {
		db.close();
		mDbHelper.close();
		db = null;
		util = null; //把自己也置为null
	}

	public boolean addChatMsg(List<ChatMsgPo> chatMsgPos){
		boolean isAdd = false;
		if(chatMsgPos == null || chatMsgPos.size()==0)
			return isAdd;
		db.beginTransaction();
		try {
			for (ChatMsgPo chatMsgPo : chatMsgPos) {
				long _id = addChatMsg(chatMsgPo);
				if(-1 == _id) 
					return isAdd;
			}
			isAdd = true;
			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
		}
		return isAdd;
	}
	
	/**
	 * @param chatMsgPo 
	 * @return the row ID of the newly inserted row, or -1 if an error occurred
	 */
	public long addChatMsg(ChatMsgPo chatMsgPo){
		ContentValues values = generateChatMsgValues(chatMsgPo);
		return db.insert(TABLE_CHATMSG, null, values);
	}

	private ContentValues generateChatMsgValues(ChatMsgPo chatMsgPo) {
		ContentValues values = new ContentValues();
		values.put(KEY_FROM, chatMsgPo.get_from());
		values.put(KEY_TO, chatMsgPo.get_to());
		values.put(KEY_BODY, chatMsgPo.get_body());
		values.put(KEY_TIME, chatMsgPo.get_time());
		values.put(KEY_STATE, chatMsgPo.get_state());
		values.put(KEY_TYPE, chatMsgPo.get_type());
		values.put(KEY_MSGTYPE, chatMsgPo.get_msgType());
		return values;
	}
	
	/**
	 * 根据_id得到一条对应的消息记录
	 * @param _id
	 * @return
	 */
	public ChatMsgPo findChatMsgPoById(long _id){
		
		ChatMsgPo po = null;
		String sql = "select * from "+TABLE_CHATMSG+" where "+KEY_ID+"="+_id;
		Cursor cursor = db.rawQuery(sql, null);
		if(cursor.moveToNext()){
			String _from = cursor.getString(cursor.getColumnIndex(KEY_FROM));
			String _to = cursor.getString(cursor.getColumnIndex(KEY_TO));
			String _body = cursor.getString(cursor.getColumnIndex(KEY_BODY));
			long _time = cursor.getLong(cursor.getColumnIndex(KEY_TIME));
			int _state = cursor.getInt(cursor.getColumnIndex(KEY_STATE));
			int _type = cursor.getInt(cursor.getColumnIndex(KEY_TYPE));
			int _msgType = cursor.getInt(cursor.getColumnIndex(KEY_MSGTYPE));
			po = new ChatMsgPo(_id, _from, _to, _body, _time, _state, _type, _msgType);
		}
		clostCursor(cursor);
		return po;
	}
	
	/**
	 * 根据id删除一条聊天记录
	 * @param _id
	 * @return
	 */
	public boolean delChatMsgPoById(long _id){
		return 0 < db.delete(TABLE_CHATMSG, KEY_ID+"=?", new String[]{_id+""});
	}
	
	/**
	 * 更新一条消息记录,一定要完整的po对象。
	 * @param po
	 * @return
	 */
	public boolean updateChatMsgPo(ChatMsgPo chatMsgPo){
		ContentValues values = generateChatMsgValues(chatMsgPo);
		return 0 < db.update(TABLE_CHATMSG, values, KEY_ID+"=?", new String[]{chatMsgPo.get_id()+""});
	}

	public static void clostCursor(Cursor cursor) {
		if(cursor!=null)
			cursor.close();
	}
	

}
