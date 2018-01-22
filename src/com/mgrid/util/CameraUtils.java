package com.mgrid.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Locale;

import com.mgrid.main.MGridActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.PictureCallback;
import android.text.format.DateFormat;
import android.widget.Toast;


public class CameraUtils {

	
	public Camera camera;
	public boolean isPreviewing;
	public Context context;

	public CameraUtils(Context context) {
		this.context=context;
	
	}
	
	@SuppressLint("NewApi")
	public void openCamera(){

		Camera.CameraInfo cameraInfo = new CameraInfo() ;
		//获得设备上的硬件camera数量
		int count = Camera.getNumberOfCameras() ; 

		for (int i = 0; i < count; i++) {
			Camera.getCameraInfo(i, cameraInfo); 
			if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
				try {
					camera = Camera.open(i) ;	//尝试打开前置摄像头
				} catch (Exception e) {
					System.out.println("打开qian摄像头异常"+e.toString());
					e.printStackTrace();
					releaseCamera();
				}
			}
		}

		if (camera == null) {
			for (int i = 0; i < count; i++) {
				Camera.getCameraInfo(i, cameraInfo); 
				if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
					try {
						camera = Camera.open(i) ;	//尝试打开后置摄像头
					} catch (Exception e) {
						System.out.println("打开后置摄像头异常"+e.toString());
						e.printStackTrace();
						releaseCamera();
					}
				}
			}
		}

		try{
			//			camera.setPreviewDisplay(surfaceHolder);
			if(camera != null){
				camera.startPreview(); // 打开预览画面
				camera.autoFocus(autoFocusCallback);
				isPreviewing = true;
			}	else {
			//	MGridActivity.handler.sendEmptyMessage(3);
				System.out.println("没有前置摄像头");
				
			}	
		} catch (Exception e){ 
			e.printStackTrace();
		}
	}
	
	/**
	 * 自动对焦后拍照
	 **/ 
	AutoFocusCallback autoFocusCallback = new AutoFocusCallback() {

		@Override
		public void onAutoFocus(boolean success, Camera camera) {
			// TODO Auto-generated method stub

			System.out.println("自动对焦完成");

			if(success && camera!=null){

				//设置回调，参数（快门，源数据，JPEG数据）
				camera.takePicture(null,null,new PictureCallback() {

					@SuppressLint("SdCardPath")
					@Override
					public void onPictureTaken(byte[] data, Camera camera) {
						// TODO Auto-generated method stub

						//使用当前的时间拼凑图片的名称
						String name = DateFormat.format("yyyy_MM_dd_hhmmss",Calendar.getInstance(Locale.CHINA))+ ".jpg";

						File file = new File("/mgrid/log/vtu_camera/");
						file.mkdirs(); //创建文件夹保存照片
						String filename=file.getPath()+File.separator+name;

						Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length) ;
						try {

							FileOutputStream fileOutputStream = new FileOutputStream(filename);
							boolean b = bitmap.compress(CompressFormat.JPEG, 100, fileOutputStream);
							fileOutputStream.flush();
							fileOutputStream.close();

							if (b) {
								MGridActivity.handler.sendEmptyMessage(4);
							}else {
								MGridActivity.handler.sendEmptyMessage(5);
							}

						} catch (FileNotFoundException e) {
							e.printStackTrace(); 
						} catch (IOException e) {
							e.printStackTrace();
						}finally{
							releaseCamera();//释放Camera
							
						}
					}
				});
			}
		}
	};

	/**
	 * 释放摄像头资源
	 */
	private void releaseCamera() {
		if (camera != null) {
			try {
				camera.setPreviewDisplay(null);
				camera.stopPreview();
				camera.release();
				camera = null;
				isPreviewing = false;
				System.out.println("释放了");
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	
	
	
}
