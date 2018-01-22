package com.mgrid.util;

import com.mgrid.main.SoundService;

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
}
