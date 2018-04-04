package com.mgrid.uncaughtexceptionhandler;

import android.app.Application;
import android.graphics.Typeface;

public class MyApplication extends Application{

	public static 	Typeface typeface;
	
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		
		typeface=Typeface.createFromAsset(getAssets(), "fonts/kt.ttf");
		CrashHandler crashHandler=CrashHandler.getInstance();
	    crashHandler.init(getApplicationContext());
	}
}
