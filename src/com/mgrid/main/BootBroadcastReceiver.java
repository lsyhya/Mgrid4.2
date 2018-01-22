package com.mgrid.main;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;


public class BootBroadcastReceiver extends BroadcastReceiver { 
    static final String action_boot="android.intent.action.BOOT_COMPLETED";  

  
    @Override
    public void onReceive(Context context, Intent intent) { 
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)){  
  
            Intent ootStartIntent=new Intent(context, MGridActivity.class);  
            ootStartIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
            ootStartIntent.setFlags( Intent.FLAG_ACTIVITY_NEW_TASK );
            context.startActivity(ootStartIntent);           
        } 
      
    }  
} 