package com.mgrid.fuction;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.hardware.Camera;
import android.hardware.Camera.ShutterCallback;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;


//made author:fjw0312
//date:2016
//notice:
@SuppressWarnings("deprecation")
public class CameraView extends SurfaceView implements SurfaceHolder.Callback,Camera.PictureCallback{
	//Fields
	private SurfaceHolder holder;
	private Camera camera;
	public boolean af;    
	public int hasPreview; 
	private boolean taked;
	Context thisContext;
	long time;
	//private boolean thread_live = false;
	//private MediaPlayer mediaPlayer; 
	String path = "/mgrid/log/vtu_camera/";

	public CameraView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub		
		af = false;
		hasPreview = 0;
		taked = false;
		thisContext = context;
		holder = getHolder();
		holder.addCallback(this);
		holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS); 
		

		
	}

	@Override
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
		

		Camera.Parameters parameters = camera.getParameters(); 

		parameters.setPreviewSize(200, 160); //设置预览图大小
	
		parameters.setPictureSize(400, 320); //设置图片大小
		camera.setParameters(parameters);     //绑定
		camera.startPreview();                //开启
		hasPreview++;
		
	}

	@Override
	public void surfaceCreated(SurfaceHolder arg0) {
		
		// TODO Auto-generated method stub  
		try{
		camera = Camera.open(0);
		if(camera==null){
		
			return;
		}

		camera.setPreviewDisplay(holder);
		
		}catch(Exception e){ 
			
		}

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder arg0) {


		// TODO Auto-generated method stub
		camera.setPreviewCallback(null);
		camera.stopPreview();   
		camera.release();
		camera = null;
		

	}

	@Override  
	public void onPictureTaken(byte[] arg0, Camera arg1) {
		// TODO Auto-generated method stub
		try{
			time = System.currentTimeMillis();
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date date = new Date(time);
			String nowTime = formatter.format(date);
			String f_time = nowTime.substring(0,10);
			String h = nowTime.substring(11,13);
			String m = nowTime.substring(14,16);
			String s = nowTime.substring(17,19);
			nowTime = f_time+"D"+h+"_"+m+"_"+s;

			File jpgFile = new File(path+nowTime+".jpg");
			FileOutputStream out= new FileOutputStream(jpgFile);
			if(arg0==null){
				
			}
			out.write(arg0);
			out.close();
		}catch(Exception e){  
		
		}
	}

	private ShutterCallback shutterCallback = new ShutterCallback(){

		@Override
		public void onShutter() {

			taked = true;
	
		}
		
	};

	public boolean takePhoto(boolean take){  
		if(take){
			mythread.start();
			return true;
		}
		return false;
	}

	public boolean onTouchEvent(MotionEvent event){
		try{
		if(event.getAction() == MotionEvent.ACTION_DOWN){
			camera.autoFocus(null);
		}else if(event.getAction() == MotionEvent.ACTION_UP){
			camera.takePicture(shutterCallback, null, this);  
			af = true; 
		}
		}catch(Exception e){
	
		}
		return true;
		
	}


	private Thread mythread = new Thread(new Runnable(){  

		@Override
		public void run() {
		
			long time = System.currentTimeMillis();	
			 while(hasPreview == 0); 
			 try{
			 Thread.sleep(10); 
			 }catch(Exception e){
		
			 }		
			 camera.autoFocus(null); // 
			 while(CameraView.this.hasWindowFocus()==false){ //
				 long d_time = System.currentTimeMillis()-time;	
				 if(d_time>10000){ 
				
					 break;
				 }
			 }
			 
			 
			// MGridActivity.handler.sendEmptyMessage(1);
			 
			 
			 try{
				 	Thread.sleep(10);
			 }catch(Exception e){
					
			 }
			 camera.takePicture(shutterCallback, null, CameraView.this); 
						

			 while(true){	 		
				if(taked){
					
						try{
						Thread.sleep(200);
						}catch(Exception e){  
							
						}
						af = true;					
						break;
					}
			} 		
		}});
}
