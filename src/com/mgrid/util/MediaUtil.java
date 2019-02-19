package com.mgrid.util;

import java.util.ArrayList;

import com.mgrid.main.SoundService;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.content.Intent;


//喇叭告警工具类
public class MediaUtil {

	private static  MediaUtil mediaUtil=new MediaUtil();
	public  static  MediaUtil getMediaUtil()
	{
		return mediaUtil;
	}
	
	public void startSound(Context  context) {
		Intent intent = new Intent(context,
				SoundService.class);
		intent.putExtra("playing", true);
		context.startService(intent);
	}

	public void stopSound(Context  context) {
		Intent intent = new Intent(context,
				SoundService.class);
		intent.putExtra("playing", false);
		context.startService(intent);
	}
	
	public void stopService(Context  context)
	{
		if(isServiceRunning(context, "com.mgrid.main.SoundService"))
		{
			Intent intent = new Intent(context,
					SoundService.class);
		
			context.stopService(intent);
		}
	
	}
	
	



	public static boolean isServiceRunning(Context context, String ServiceName) {

		if (("").equals(ServiceName) || ServiceName == null)

			return false;

		ActivityManager myManager = (ActivityManager) context

				.getSystemService(Context.ACTIVITY_SERVICE);

		ArrayList<RunningServiceInfo> runningService = (ArrayList<RunningServiceInfo>) myManager

				.getRunningServices(30);

		for (int i = 0; i < runningService.size(); i++) {

			if (runningService.get(i).service.getClassName().toString()

					.equals(ServiceName)) {

				return true;

			}

		}

		return false;

	}

}

	

