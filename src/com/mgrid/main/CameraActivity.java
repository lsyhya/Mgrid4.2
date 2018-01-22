package com.mgrid.main;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

import com.mgrid.fuction.CameraView;


// fjw add 2016 4 1 
//made author:fjw0312
//date:2016
//notice:
public class CameraActivity extends Activity {

	//Fields 
	public static CameraView cameraview;
	public static int flag = 0;
	//private ProgressDialog dialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		setContentView(R.layout.camera_view);
	
		cameraview = new CameraView(this);
		setContentView(cameraview);		
		flag++;
		//dialog=ProgressDialog.show(this, "提示", "请稍候点击");
		myThread.start();

		
	}

    Thread myThread = new Thread(new Runnable() {
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			long time = System.currentTimeMillis();
			try{			
			cameraview.takePhoto(true);
			}catch(Exception e){
			 
			}
			while((flag!=0)&&(cameraview.af==false)){ 
				long d_time = System.currentTimeMillis() - time;
				if(d_time > 1800000){
					
					break;
				}					
			}
			
			CameraActivity.this.finish(); 
			
		}
	});

    
//    Handler handler = new Handler() {
//        public void handleMessage(Message msg) {
//                // handler接收到消息后就会执行此方法
//                switch (msg.what) {
//                case 1:
//                        dialog.dismiss();
//                        // 关闭ProgressDialog
//                        break;
//                default:
//                        break;
//                }
//                super.handleMessage(msg);
//        }
//};
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
