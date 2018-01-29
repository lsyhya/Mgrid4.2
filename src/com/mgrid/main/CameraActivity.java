package com.mgrid.main;

/**
 * 用来拍照的Activity
 * @author Chenww
 */
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.PictureCallback;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.Window;
import android.view.WindowManager;

@TargetApi(Build.VERSION_CODES.GINGERBREAD)
@SuppressLint("NewApi")
public class CameraActivity extends Activity {

	private SurfaceView mySurfaceView;
	private SurfaceHolder myHolder;
	private double C_sizeM = 100;
	public Camera camera;
	public Context context;
	public static double count = 0;
	public static List<String> nameData = new ArrayList<String>();
	private static final String TAG = "CameraActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		// 无title
		requestWindowFeature(Window.FEATURE_NO_TITLE);
//		// 全屏
//		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

		// 设置布局
		setContentView(R.layout.activity_camera);
		Log.i(TAG, "oncreate");

		// 初始化surface
		initSurface();

		// 这里得开线程进行拍照，因为Activity还未完全显示的时候，是无法进行拍照的，SurfaceView必须先显示
		new Thread(new Runnable() {
			@Override
			public void run() {
				// 初始化camera并对焦拍照
				initCamera();
			}
		}).start();

	}

	@Override
	protected void onDestroy() {
		
		super.onDestroy();
		MGridActivity.isNOChangPage=true;
		System.out.println("我被销毁了");
	}
	
	// 初始化surface
	@SuppressWarnings("deprecation")
	private void initSurface() {
		// 初始化surfaceview
		mySurfaceView = (SurfaceView) findViewById(R.id.camera_surfaceview);

		// 初始化surfaceholder
		myHolder = mySurfaceView.getHolder();

	}

	// 初始化摄像头
	private void initCamera() {

		// 如果存在摄像头
		if (checkCameraHardware(getApplicationContext())) {
			// 获取摄像头（首选前置，无前置选后置）
			if (openFacingFrontCamera()) {
				Log.i(TAG, "openCameraSuccess");
				// 进行对焦
				autoFocus();
			} else {
				Log.i(TAG, "openCameraFailed");
			}

		}
	}

	// 对焦并拍照
	private void autoFocus() {

		try {
			// 因为开启摄像头需要时间，这里让线程睡两秒
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		// 自动对焦
		camera.autoFocus(myAutoFocus);

		// 对焦后拍照
		camera.takePicture(null, null, myPicCallback);
	}

	// 判断是否存在摄像头
	private boolean checkCameraHardware(Context context) {

		if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
			// 设备存在摄像头
			return true;
		} else {
			// 设备不存在摄像头
			return false;
		}

	}

	// 得到后置摄像头
	private boolean openFacingFrontCamera() {

		// 尝试开启前置摄像头
		Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
		for (int camIdx = 0, cameraCount = Camera.getNumberOfCameras(); camIdx < cameraCount; camIdx++) {
			Camera.getCameraInfo(camIdx, cameraInfo);
			if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
				try {
					Log.i(TAG, "tryToOpenCamera");
					camera = Camera.open(camIdx);
				} catch (RuntimeException e) {
					e.printStackTrace();
					return false;
				}
			}
		}

		// 如果开启前置失败（无前置）则开启后置
		if (camera == null) {
			for (int camIdx = 0, cameraCount = Camera.getNumberOfCameras(); camIdx < cameraCount; camIdx++) {
				Camera.getCameraInfo(camIdx, cameraInfo);
				if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
					try {
						camera = Camera.open(camIdx);
					} catch (RuntimeException e) {
						return false;
					}
				}
			}
		}

		try {
			// 这里的myCamera为已经初始化的Camera对象
			camera.setPreviewDisplay(myHolder);
		} catch (IOException e) {
			e.printStackTrace();
			releaseCamera();
		}

		camera.startPreview();

		return true;
	}

	// 自动对焦回调函数(空实现)
	private AutoFocusCallback myAutoFocus = new AutoFocusCallback() {
		@Override
		public void onAutoFocus(boolean success, Camera camera) {
		}
	};

	// 拍照成功回调函数
	private PictureCallback myPicCallback = new PictureCallback() {

		@Override
		public void onPictureTaken(final byte[] data, Camera camera) {
			
			
			// 完成拍照后关闭Activity
			MGridActivity.xianChengChi.execute(new Runnable() {

				@Override
				public void run() {

					
					long time = System.currentTimeMillis();

					SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

					Date date = new Date(time);

					String nowTime = formatter.format(date);

					String f_time = nowTime.substring(0,10);

					String h = nowTime.substring(11,13);

					String m = nowTime.substring(14,16);

					String s = nowTime.substring(17,19);

					nowTime = f_time+"D"+h+"_"+m+"_"+s;

					
					// 使用当前的时间拼凑图片的名称
					//String name = DateFormat.format("yyyy_MM_dd_hhmmss", Calendar.getInstance(Locale.CHINA)) + ".jpg";
					String name=nowTime;
					File file = new File("/mgrid/log/vtu_camera/");
//					double sizeM = getDirSize(file);
//					while (sizeM > C_sizeM) {
//
//						double d = Math.ceil(count / 10);						
//						deleteFile(file, d);
//						sizeM = getDirSize(file);
//					}

					file.mkdirs(); // 创建文件夹保存照片
					String filename = file.getPath() + File.separator + name;

					Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
					try {

						FileOutputStream fileOutputStream = new FileOutputStream(filename);
						boolean b = bitmap.compress(CompressFormat.JPEG, 100, fileOutputStream);
						fileOutputStream.flush();
						fileOutputStream.close();

						if (b) {
							MGridActivity.handler.sendEmptyMessage(4);
						} else {
							MGridActivity.handler.sendEmptyMessage(5);
						}

					} catch (FileNotFoundException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					} finally {
						releaseCamera();// 释放Camera

					}

				}
			});

		}
	};

	public static double getDirSize(File file) {
		// 判断文件是否存在
		if (file.exists()) {
			// 如果是目录则递归计算其内容的总大小
			if (file.isDirectory()) {
				File[] children = file.listFiles();
				double size = 0;
				count = children.length;
				for (File f : children)
					size += getDirSize(f);
				return size;
			} else {// 如果是文件则直接返回其大小,以“兆”为单位
				double size = (double) file.length() / 1024 / 1024;
				return size;
			}
		} else {
			System.out.println("文件或者文件夹不存在，请检查路径是否正确！");
			return 0.0;
		}
	}

	public static void deleteFile(File file, double count) {
		if (file.exists() && file.isDirectory()) {
			nameData.clear();
			File[] children = file.listFiles();
			for (File f : children) {

				String name = f.getName();
				nameData.add(name);

			}
			System.out.println("文件总个数：" + nameData.size());
			Collections.sort(nameData);

			for (String name : nameData) {
				for (File f2 : children) {
					if (count > 0) {
						if (name.equals(f2.getName())) {
							System.out.println(name + "被删");
							f2.delete();
							count--;
							System.out.println("还要删除" + count + "个");
							break;
						}
					}
				}
			}

		}
	}

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
				Intent intent = new Intent(CameraActivity.this, MGridActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				startActivity(intent);
				MGridActivity.isNOChangPage=true;
				CameraActivity.this.finish();
				System.out.println("释放了");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	// 获取文件夹
	private File getDir() {
		// 得到SD卡根目录
		File dir = Environment.getExternalStorageDirectory();

		if (dir.exists()) {
			return dir;
		} else {
			dir.mkdirs();
			return dir;
		}
	}
}
