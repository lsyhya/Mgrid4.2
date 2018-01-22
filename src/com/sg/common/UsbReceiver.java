package com.sg.common;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.mgrid.main.MGridActivity;
import com.sg.common.Export_His_Data;

/**
 * 已经舍弃掉  没用
 * @author lsy
 *用来监听 U盘是否插入的广播
 */
public class UsbReceiver{
	   private BroadcastReceiver mReceiver;
	   public UsbReceiver(Context context){
	      mReceiver = new BroadcastReceiver(){
	

		@Override
		public void onReceive(Context arg0, Intent arg1) {
			  //intent.getAction());获取存储设备当前状态        

        System.out.println("UUUBroadcastReceiver:"+arg1.getAction());

         //intent.getData().getPath());获取存储设备路径
        System.out.println("UUUpath:"+arg1.getData().getPath());
     //   Export_His_Data.fileName=arg1.getData().getPath()+"/123.dat";
        MGridActivity.usbName=arg1.getData().getPath();
        
			
		}

	     };
	  
	      IntentFilter filter = new IntentFilter();
	      filter.addAction(Intent.ACTION_MEDIA_SHARED);//如果SDCard未安装,并通过USB大容量存储共享返回
	      filter.addAction(Intent.ACTION_MEDIA_MOUNTED);//表明sd对象是存在并具有读/写权限
	      filter.addAction(Intent.ACTION_MEDIA_UNMOUNTED);//SDCard已卸掉,如果SDCard是存在但没有被安装
	      filter.addAction(Intent.ACTION_MEDIA_CHECKING);  //表明对象正在磁盘检查
	      filter.addAction(Intent.ACTION_MEDIA_EJECT);  //物理的拔出 SDCARD
	      filter.addAction(Intent.ACTION_MEDIA_REMOVED);  //完全拔出
	      filter.addDataScheme("file"); // 必须要有此行，否则无法收到广播   
	      context.registerReceiver(mReceiver, filter);
	}
	}