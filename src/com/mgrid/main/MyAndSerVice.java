package com.mgrid.main;

import java.util.concurrent.TimeUnit;

import com.mgrid.main.service.andservice.IndexHandler;
import com.mgrid.util.NetUtils;
import com.yanzhenjie.andserver.AndServer;
import com.yanzhenjie.andserver.Server;
import com.yanzhenjie.andserver.filter.HttpCacheFilter;
import com.yanzhenjie.andserver.website.AssetsWebsite;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class MyAndSerVice extends Service{

	
	private Server mServer;
	
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		Log.e("MyService", "¿ªÆô");
		
		mServer = AndServer.serverBuilder()
                .inetAddress(NetUtils.getLocalIPAddress()) // Bind IP address.
                .port(8080)
                .timeout(15, TimeUnit.SECONDS)
                .website(new AssetsWebsite(getAssets(), "web"))
                .registerHandler("/index", new IndexHandler())                
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
            String hostAddress = mServer.getInetAddress().getHostAddress();
            ServerManager.serverStart(MyAndSerVice.this, hostAddress);
        }

        @Override
        public void onStopped() {
            ServerManager.serverStop(MyAndSerVice.this);
        }

        @Override
        public void onError(Exception e) {
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
                String hostAddress = mServer.getInetAddress().getHostAddress();
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
            mServer.shutdown();
        }
    }

	
	
	

}
