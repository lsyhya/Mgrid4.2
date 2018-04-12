//package com.mgrid.main;
//
//import com.lsy.Monitor.LoginFragment;
//import com.lsy.Monitor.PlayFragment;
//
//import android.app.Activity;
//import android.app.FragmentManager;
//import android.app.FragmentTransaction;
//import android.content.Context;
//import android.os.Bundle;
//import android.view.View;
//import android.view.inputmethod.InputMethodManager;
//
//public class MonitorActivity extends Activity{
//	
//	
//	private  String etIP, etPort, etUser, etPsd;
//	
//	
//	
//	
//	
//
//	public String getEtIP() {
//		return etIP;
//	}
//
//	public void setEtIP(String etIP) {
//		this.etIP = etIP;
//	}
//
//	public String getEtPort() {
//		return etPort;
//	}
//
//	public void setEtPort(String etPort) {
//		this.etPort = etPort;
//	}
//
//	public String getEtUser() {
//		return etUser;
//	}
//
//	public void setEtUser(String etUser) {
//		this.etUser = etUser;
//	}
//
//	public String getEtPsd() {
//		return etPsd;
//	}
//
//	public void setEtPsd(String etPsd) {
//		this.etPsd = etPsd;
//	}
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		
//		System.out.println("oncreate");
//		
//		setContentView(R.layout.monitor);
//		FragmentManager manager=getFragmentManager();
//		FragmentTransaction ft=manager.beginTransaction();
//		LoginFragment fragment=new LoginFragment();
//		ft.add(R.id.monitor, fragment);
//		ft.commit();					
//	}
//	
//	public void startMonitorAct()
//	{
//		FragmentManager manager1=getFragmentManager();
//		FragmentTransaction ft1=manager1.beginTransaction();
//		PlayFragment fragment1=new PlayFragment();
//		ft1.replace(R.id.monitor, fragment1);
//		ft1.commit();
//	}
//	
//	public void hindSoft(View view)
//	{
//		InputMethodManager im=(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//		im.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
//
//	}
//	
//
//
//}
