package com.mgrid.main;

import java.io.File;
import java.net.InetAddress;
import java.util.concurrent.TimeUnit;

import com.mgrid.util.NetUtils;
import com.sg.web.handler.DataCallBackHandler;
import com.sg.web.handler.IndexHandler;
import com.sg.web.handler.LoginHandler;
import com.sg.web.handler.OnClickHandler;
import com.sg.web.handler.SendDataHandler;
import com.yanzhenjie.andserver.AndServer;
import com.yanzhenjie.andserver.Server;
import com.yanzhenjie.andserver.filter.HttpCacheFilter;
import com.yanzhenjie.andserver.website.StorageWebsite;

import android.app.Service;
import android.content.Intent;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;

public class MyAndSerVice extends Service{

	
	private Server mServer;
	private String filePath=Environment.getExternalStorageDirectory()+"/vtu_pagelist";
	private static int index=0;
	private File file = new File(filePath);
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		

		//File file = new File(filePath);
		if(file.exists())
		{
		
//			mServer = AndServer.serverBuilder()
//	                .inetAddress(NetUtils.getLocalIPAddress()) // Bind IP address.
//	                .port(8080)
//	                .timeout(15, TimeUnit.SECONDS)
//	                .website(new StorageWebsite(file.getAbsolutePath()))
//	                .registerHandler("/test", new IndexHandler())   
//	                .registerHandler("/getdata", new SendDataHandler())
//	                .registerHandler("/onClick", new OnClickHandler())
//	                .registerHandler("/callback", new DataCallBackHandler())
//	                .registerHandler("/login", new LoginHandler())
//	                .filter(new HttpCacheFilter())
//	                .listener(mListener)
//	                .build();
			
			
			buildServer(8080);
			
		}else
		{
			Log.e("", "²¿ÊðÊ§°Ü");
		}
		
		
		
	}
	
	
	private void buildServer(int port)
	{
		
		mServer = AndServer.serverBuilder()
                .inetAddress(NetUtils.getLocalIPAddress()) // Bind IP address.
                .port(port)
                .timeout(15, TimeUnit.SECONDS)
                .website(new StorageWebsite(file.getAbsolutePath()))
                .registerHandler("/test", new IndexHandler())   
                .registerHandler("/getdata", new SendDataHandler())
                .registerHandler("/onClick", new OnClickHandler())
                .registerHandler("/callback", new DataCallBackHandler())
                .registerHandler("/login", new LoginHandler())
                .filter(new HttpCacheFilter())
                .listener(mListener)
                .build();
		
	}
	
	
	/**
     * Server listener.
     */
    private Server.ServerListener mListener = new Server.ServerListener() {
        @Override
        public void onStarted() {
          
        	if(mServer==null)
        	{
        		Log.e("webService", " mServer==null");
        		return;
        	}

        	InetAddress inet=mServer.getInetAddress();
        	
        	if(inet==null)
        	{
        		Log.e("webService", " inet==null");
        		return;
        	}
        	
        	String hostAddress = inet.getHostAddress();
        	
        	if(hostAddress==null)
        	{
        		Log.e("webService", "hostAddress=null");
        		return;
        	}
        	
            ServerManager.serverStart(MyAndSerVice.this, hostAddress);
            Log.e("webService succ", "succ");
        }

        @Override
        public void onStopped() {
        	
        	Log.e("webService stop", "stop");
            ServerManager.serverStop(MyAndSerVice.this);
            
        }

        @Override
        public void onError(Exception e) {
        	
              
        	Log.e("webService err", e.getMessage());      	
            ServerManager.serverError(MyAndSerVice.this, e.getMessage());
            
           
                       
        }
    };

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
    	
    	Log.e("ANDService", "¿ªÆô");
    	
        startServer();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopServer(); // Stop server.
    }

    /**
     * Start server.
     */
    private void startServer() {
        if (mServer != null) {
            if (mServer.isRunning()) {
            	
            	InetAddress inet=mServer.getInetAddress();
            	
            	if(inet==null)
            	{
            		Log.e("webService", " inet==null");
            		return;
            	}
            	
            	String hostAddress = inet.getHostAddress();
            	
            	if(hostAddress==null)
            	{
            		Log.e("webService", "hostAddress=null");
            		return;
            	}
                
                ServerManager.serverStart(MyAndSerVice.this, hostAddress);
            } else {
                mServer.startup();
            }
        }
    }

    /**
     * Stop server.
     */
    private void stopServer() {
        if (mServer != null && mServer.isRunning()) {
        	Log.e("mServer", "close");
            mServer.shutdown();
        }
    }

	
	
	

}
