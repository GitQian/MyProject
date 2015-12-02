package com.ztkj.db;


import java.util.ArrayList;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.chinacnit.elevatorguard.mobile.application.ElevatorGuardApplication;
import com.ztkj.db.DBInfo.TB_MESSAGE;
import com.ztkj.db.bean.MessageBean;
import com.ztkj.tool.Config;
import com.ztkj.tool.SharedPreferenceTool;

public class DbOperater {
	private static DbOperater mInstance;
	private SQLiteDatabase db;
	
	/**
	 * 数据库名， 
	 * @param context
	 * @param name 数据库db的名字 就是  uid
	 * @return
	 */
	public static final DbOperater getInstance(){
		synchronized (DbOperater.class) {
			if(mInstance == null){
				mInstance = new DbOperater();
			}
		}
		return mInstance;
	}
	
	private DbOperater() {
		db = new DBOpenHelper(ElevatorGuardApplication.getInstance(), SharedPreferenceTool.getUid()+".db", null, Config.DB_VERSION).getReadableDatabase();
	}
	public static void reset(){
		mInstance=null;
	}
	/**
	 * 插入联系人 , 先查询然后再插入，避免重复插入同一个联系人。
	 * @param list
	 * @return
	 */
	public synchronized void addMessage(MessageBean msg){
		if(msg==null){
			return ;
		}
		synchronized (db) {
			ContentValues values =new ContentValues();
			values.put(TB_MESSAGE._TITLE, msg.getTitle());
			values.put(TB_MESSAGE._CONTENT, msg.getContent());
			values.put(TB_MESSAGE._READSTATE, 1);
			values.put(TB_MESSAGE._SAVETIME, msg.getSavetime());
			db.insert(TB_MESSAGE.TB_NAME, null, values);
		}
	}
	public ArrayList<MessageBean> queryMessage(){
		Cursor cursor=db.query(TB_MESSAGE.TB_NAME, null, null, null, null, null, TB_MESSAGE._SAVETIME+" desc");
		ArrayList<MessageBean> listBean=null;
		if(cursor.moveToFirst()){
			listBean =new ArrayList<MessageBean>();
			do {
				MessageBean bean =new MessageBean();
				bean.set_id(cursor.getString(cursor.getColumnIndex(TB_MESSAGE._ID)));
				bean.setContent(cursor.getString(cursor.getColumnIndex(TB_MESSAGE._CONTENT)));
				bean.setReadState(cursor.getString(cursor.getColumnIndex(TB_MESSAGE._READSTATE)));
				bean.setSavetime(cursor.getString(cursor.getColumnIndex(TB_MESSAGE._SAVETIME)));
				bean.setTitle(cursor.getString(cursor.getColumnIndex(TB_MESSAGE._TITLE)));
				listBean.add(bean);
			} while (cursor.moveToNext());
		}
		cursor.close();
		return listBean;
	}
	public synchronized void updateMessageReaderState(){
		synchronized (db) {
			ContentValues values =new ContentValues();
			values.put(TB_MESSAGE._READSTATE, 0);
			db.update(TB_MESSAGE.TB_NAME, values, TB_MESSAGE._READSTATE+" = ? ", new String[]{"1"});
		}
	}
	
	
}
