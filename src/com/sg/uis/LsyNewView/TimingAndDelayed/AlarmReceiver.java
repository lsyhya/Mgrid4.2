package com.sg.uis.LsyNewView.TimingAndDelayed;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class AlarmReceiver extends BroadcastReceiver {

	public static final String BC_ACTION = "com.ex.action.BC_ACTION";
	public static int   delayed = -1;
	public static List<TimingAndDelayedHandle> timeHandleList=new ArrayList<>();
	
	
	private Timer timer;

	@Override
	public void onReceive(Context context, Intent intent) {

		Log.e("¹ã²¥", "½øÀ´");
		
		if (intent.getAction().equals(BC_ACTION)) {
			

			if (delayed == -1) {
				delayed = Integer.parseInt(intent.getStringExtra("delayed"));
			}
			
			timingHandle();

		}

	}
	
	
	private void setTimeTask()
	{
		
		timer=new Timer();
		
		timer.schedule(new TimerTask() {
				
				@Override
				public void run() {
					
					delayedHandle();
					
				}
			}, delayed*1000*60);
		
	}
	

	
	private void timingHandle() {
		
         for (TimingAndDelayedHandle timingAndDelayedHandle : timeHandleList) {
			
        	 timingAndDelayedHandle.timingHandle();
        	 
        	 setTimeTask();
        	 
		 }
		
	}
	
	
	
	
	private void delayedHandle()
	{
		
		 for (TimingAndDelayedHandle timingAndDelayedHandle : timeHandleList) {
				
        	 timingAndDelayedHandle.delayedHandle();
        	 
		 }
	}
	
	

	public static void setTimingAndDelayedHandle(TimingAndDelayedHandle timeHandle)
	{
		  AlarmReceiver.timeHandleList.add(timeHandle);
	}
	
	
	

}
