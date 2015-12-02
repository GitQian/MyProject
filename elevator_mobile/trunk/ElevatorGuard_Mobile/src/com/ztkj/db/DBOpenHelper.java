package com.ztkj.db;


import com.ztkj.db.DBInfo.TB_MESSAGE;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DBOpenHelper extends SQLiteOpenHelper{
	/**
	 * 消息表
	 */
	private String sql_message = "CREATE TABLE "
			+ TB_MESSAGE.TB_NAME        +" (" 
			+ TB_MESSAGE._ID            +" INTEGER PRIMARY KEY AUTOINCREMENT,"
			+ TB_MESSAGE._TITLE         +" TEXT,"
			+ TB_MESSAGE._CONTENT       +" TEXT,"
			+ TB_MESSAGE._READSTATE     +" INTEGER,"
			+ TB_MESSAGE._SAVETIME      +" TEXT" +")";
			
	
	public DBOpenHelper(Context context, String name, CursorFactory factory,int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(sql_message);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}
	
}
