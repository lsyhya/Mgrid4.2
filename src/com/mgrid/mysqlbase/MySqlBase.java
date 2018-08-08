package com.mgrid.mysqlbase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MySqlBase extends SQLiteOpenHelper{
	
	
	public static  final String dbName="Mgrid.db";
	public static  final String doorTable="Door_T";
	public static  final String doorEventTable="Event_T";
	private static  final int    dbVersion=1;
	

	public MySqlBase(Context context) {
		
		super(context, dbName, null, dbVersion);
		
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		
		String doorManagerTable="create table if not exists "+doorTable+" (UserName text ,CardId text primary key,UserId text,PassWord text,Time text) ";
		String doorEvent="create table if not exists "+doorEventTable+" (id integer primary key autoincrement,CardId text,Time text,Event text) "; 
		db.execSQL(doorManagerTable);
		db.execSQL(doorEvent);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
		
		switch (oldVersion) {
		case 1:
			
			break;

		default:
			break;
		}
		
	}

	
	
}
