package com.mgrid.main;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.sg.common.lsyBase.ClientManager;
import com.sg.uis.LsyNewView.DoorButtManager;
import com.sg.uis.LsyNewView.NBerDoorView;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

public class NiuberDoorService extends Service {

	private SokectBind SokectBind = new SokectBind();

	private Handler handler = new Handler(Looper.getMainLooper());
	private static final int PORT = 6100;
	private ServerSocket server = null;
	private NBerDoorView nDoorView=null;
	private ExecutorService EService;

	@Override
	public void onCreate() {
		super.onCreate();

		

	}

	private void initService() {
		try {
			server = new ServerSocket(PORT);
			EService=Executors.newCachedThreadPool();
			
			Log.e("TAG", "服务器已启动，等待客户端连接...");
			Socket client = null;
			/*
			 * 用死循环等待多个客户端的连接，连接一个就启动一个线程进行管理
			 */
			while (true) {
				client = server.accept();	
				EService.execute(new ClientManager(client, nDoorView)); // 启动一个线程，用以守候从客户端发来的消息
				//toastMsg("连接一台客户端，IP："+client.getInetAddress().toString());
				Log.e("TAG","连接一台客户端，IP："+client.getInetAddress().toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void set(NBerDoorView nDoorView) {

		this.nDoorView = nDoorView;
		MGridActivity.xianChengChi.execute(new Runnable() {
			
			@Override
			public void run() {
				
				initService();
				
			}
		});
		
	}

	@Override
	public IBinder onBind(Intent intent) {

		return SokectBind;
	}

	public class SokectBind extends Binder {
		public NiuberDoorService getService() {
			return NiuberDoorService.this;
		}

	}

	/* 因为Toast是要运行在主线程的 所以需要到主线程哪里去显示toast */
	public void toastMsg(final String msg) {

		handler.post(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
			}
		});
	}

	/* 释放资源 */
	private void releaseSocket() {

		try {
			if (server != null) {
				server.close();
			}
			
			if(EService!=null)
			{
				EService.shutdownNow();
			}
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		releaseSocket();

	}

}
