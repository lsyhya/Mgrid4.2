package com.mgrid.main.broadcastreceiver;

import com.mgrid.main.CameraActivity;
import com.mgrid.main.MGridActivity;
import com.mgrid.util.LoginUtil;
import com.sg.uis.oldView.SgImage;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.util.Log;

public class ScreenBroadcaseReceiver extends BroadcastReceiver {

	
	static final String Tag="ScreenBroadcaseReceiver";
	
	IntentFilter intentF = new IntentFilter();
	Context context;
	WakeLock mWakeLock;// 锁屏类
    Handler  handler=new Handler();
	
	public ScreenBroadcaseReceiver(Context context) {

		this.context = context;
		intentF.addAction(Intent.ACTION_SCREEN_OFF);
		intentF.addAction(Intent.ACTION_SCREEN_ON);

	}

	@Override
	public void onReceive(Context context, Intent intent) {

		switch (intent.getAction()) {

		case Intent.ACTION_SCREEN_ON:

			
			Log.e(Tag, "ACTION_SCREEN_ON");
			
			if (MGridActivity.m_bTakePhoto) { // 拍照功能是否开启
				// 启动拍照页面
				MGridActivity.isNOChangPage = false;
				Intent intents = new Intent(context, CameraActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				context.startActivity(intents);

			}
			if (!LoginUtil.isPlay && MGridActivity.loginUtil != null && MGridActivity.ISOPENFACE) {
				MGridActivity.loginUtil.showListDialog();
			}

			break;

		case Intent.ACTION_SCREEN_OFF:

			Log.e(Tag, "ACTION_SCREEN_OFF");
			
			if (!MGridActivity.isLoading && MGridActivity.isPlaygif) {// 判断是否加载完成并且开启屏保gif功能
				((MGridActivity) context).onPageChange("gif.xml");
				if (MGridActivity.isChangGif) {
					SgImage.isChangColor = false;
					acquireWakeLock();
				}
			} else if (!MGridActivity.isLoading && MGridActivity.isPlaymv) {// 判断是否加载完成并且开启屏保mv功能
				if (MGridActivity.isChangGif) {
					if (MGridActivity.isSleep) {
						SgImage.isChangColor = true;
						((MGridActivity) context).onPageChange(MGridActivity.m_sMainPage);
					} else {
						((MGridActivity) context).onPageChange("mv.xml");
						SgImage.isChangColor = false;
						acquireWakeLock();
						handler.postDelayed(runTime, MGridActivity.sleepTime * 1000);
						
					}
				}
			} else if (!MGridActivity.isLoading) {
				((MGridActivity) context).onPageChange(MGridActivity.m_sMainPage);
			}

			break;

		}

	}

	/**
	 * 开启广播
	 */
	public void startReceiver() {

		context.registerReceiver(this, intentF);

	}

	/**
	 * 注销广播
	 */
	public void stopReceiver() {
		context.unregisterReceiver(this);
		releaseWakeLock();
	}

	/**
	 * 锁定屏幕
	 */
	public void acquireWakeLock() {
		if (mWakeLock == null) {
			PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
			mWakeLock = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.SCREEN_DIM_WAKE_LOCK
					| PowerManager.ON_AFTER_RELEASE, "SimpleTimer");
			mWakeLock.acquire();
		}
	}

	/**
	 * 释放屏幕锁
	 */
	public void releaseWakeLock() {
		if (mWakeLock != null && mWakeLock.isHeld()) {
			mWakeLock.release();
			mWakeLock = null;
		}
	}

	/**
	 * 定时任务 释放屏幕锁
	 */
	public Runnable runTime = new Runnable() {
		public void run() {
			if (MGridActivity.svv != null) {
				MGridActivity.svv.pauseMv();
				releaseWakeLock();
				MGridActivity.isSleep = true;
			}
		}
	};

}
