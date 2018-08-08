package com.mgrid.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
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

@SuppressWarnings("deprecation")
public class CameraUtils {

	private double C_sizeM = 100;
	public Camera camera;
	public Context context;
	public static double count = 0;
	public static List<String> nameData = new ArrayList<String>();

	public CameraUtils(Context context) {
		this.context = context;
	}

	@SuppressLint("NewApi")
	public void openCamera() {

		Camera.CameraInfo cameraInfo = new CameraInfo();

		// 获得设备上的硬件camera数量

		int count = Camera.getNumberOfCameras();

		for (int i = 0; i < count; i++) {

			Camera.getCameraInfo(i, cameraInfo);

			if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {

				try {

					camera = Camera.open(i); // 尝试打开前置摄像头

				} catch (Exception e) {

					System.out.println("打开qian摄像头异常" + e.toString());

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

						camera = Camera.open(i); // 尝试打开后置摄像头

					} catch (Exception e) {

						System.out.println("打开后置摄像头异常" + e.toString());

						e.printStackTrace();

						releaseCamera();

					}

				}

			}

		}

		try {

			// camera.setPreviewDisplay(surfaceHolder);

			if (camera != null) {

				camera.startPreview(); // 打开预览画面
				camera.autoFocus(autoFocusCallback);
				camera.takePicture(null, null, myPicCallback);

			} else {

				// MGridActivity.handler.sendEmptyMessage(3);
				System.out.println("没有前置摄像头");

			}

		} catch (Exception e) {

			e.printStackTrace();

		}

	}


	private PictureCallback myPicCallback = new PictureCallback() {

		@Override
		public void onPictureTaken(final byte[] data, Camera camera) {
			MGridActivity.xianChengChi.execute(new Runnable() {

				@Override
				public void run() {

					// 使用当前的时间拼凑图片的名称
					String name = DateFormat.format("yyyy_MM_dd_HHmmss", Calendar.getInstance(Locale.CHINA)) + ".jpg";

					File file = new File("/mgrid/log/vtu_camera/");
					double sizeM = getDirSize(file);
					while (sizeM > C_sizeM) {

						double d = Math.ceil(count / 10);
						System.out.println("我要删文件了 " + d);
						deleteFile(file, d);
						sizeM = getDirSize(file);
					}

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

	/**
	 * 自动对焦后拍照
	 **/
	AutoFocusCallback autoFocusCallback = new AutoFocusCallback() {

		@Override
		public void onAutoFocus(boolean success, Camera camera) {
			// TODO Auto-generated method stub

			// System.out.println("自动对焦完成");
			// System.out.println(success);
			// if (camera != null) {
			//
			// // 设置回调，参数（快门，源数据，JPEG数据）
			// camera.takePicture(null, null, new PictureCallback() {
			//
			// @SuppressLint("SdCardPath")
			// @Override
			// public void onPictureTaken(final byte[] data, Camera camera) {
			// // TODO Auto-generated method stub
			//
			//
			// }
			// });
			// } else {
			// releaseCamera();// 释放Camera
			// }
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
				System.out.println("释放了");

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
