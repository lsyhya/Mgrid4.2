package com.lsy.Service.TilmePlush;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;

public class TimePlushService extends Service
{

	TimeService service=null;
	@Override
	public IBinder onBind(Intent intent) {
		
		return null;
	}
	
	@Override
	public void onCreate() {
		service=new TimeService(getApplication());
		super.onCreate();
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		myHandler.postDelayed(runnable, 10);
		return super.onStartCommand(intent, flags, startId);
	}
	
	private Handler myHandler=new Handler();
	private Runnable runnable=new Runnable() {
		
		@Override
		public void run() {
			
			if(service!=null)
			{
				service.startCalibrateTime();
			}
			myHandler.postDelayed(runnable, 60*60*1000);
			
		}
	};
}
 