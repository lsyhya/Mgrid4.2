package com.mgrid.uncaughtexceptionhandler;

import java.io.File;
import java.io.IOException;

import android.app.Application;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.graphics.Typeface;
import android.os.Environment;

public class MyApplication extends Application {

	public static Typeface typeface;
	private static final String  dbPath="SQL";

	@Override
	public void onCreate() {
		super.onCreate();

		typeface = Typeface.createFromAsset(getAssets(), "fonts/kt.ttf");
		CrashHandler crashHandler = CrashHandler.getInstance();
		crashHandler.init(getApplicationContext());
	}

//	@Override
//	public SQLiteDatabase openOrCreateDatabase(String name, int mode, CursorFactory factory,
//			DatabaseErrorHandler errorHandler) {
//
//		SQLiteDatabase sqLiteDatabase = SQLiteDatabase.openOrCreateDatabase(getDatabasePath(name), null);
//
//		return sqLiteDatabase;
//	}
//
//	@Override
//	public SQLiteDatabase openOrCreateDatabase(String name, int mode, CursorFactory factory) {
//
//		SQLiteDatabase result = SQLiteDatabase.openOrCreateDatabase(getDatabasePath(name), null);
//		return result;
//
//	}
//
//	@Override
//	public File getDatabasePath(String name) {
//		File parentFile = new File(
//				Environment.getExternalStorageDirectory() + File.separator + dbPath + File.separator);
//		if (!parentFile.exists()) {
//			
//			boolean mkParentRes = parentFile.mkdirs();
//		
//		}
//
//		File realDBFile = new File(parentFile, name);
//		if (!realDBFile.exists()) {
//			try {
//				realDBFile.createNewFile();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
//		return realDBFile;
//
//	}

}
