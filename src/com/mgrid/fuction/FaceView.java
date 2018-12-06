package com.mgrid.fuction;

import java.util.ArrayList;
import java.util.List;

import com.arcsoft.facerecognition.AFR_FSDKEngine;
import com.arcsoft.facerecognition.AFR_FSDKError;
import com.arcsoft.facerecognition.AFR_FSDKFace;
import com.arcsoft.facerecognition.AFR_FSDKMatching;
import com.arcsoft.facetracking.AFT_FSDKEngine;
import com.arcsoft.facetracking.AFT_FSDKError;
import com.arcsoft.facetracking.AFT_FSDKFace;
import com.mgrid.main.FaceActivity;
import com.mgrid.main.MGridActivity;
import com.mgrid.main.face.FaceDB;
import com.mgrid.uncaughtexceptionhandler.MyApplication;
import com.mgrid.util.AllUtilS;

import android.content.Context;
import android.graphics.Rect;
import android.hardware.Camera;
import android.os.AsyncTask;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

@SuppressWarnings("deprecation")
public class FaceView extends SurfaceView implements SurfaceHolder.Callback, Camera.PreviewCallback, Runnable {

	private static final String Tag = "CameraView";

	private Context context;
	private Camera camera;
	private SurfaceHolder holder;

	private int width, height;
	private boolean isPreview = true;

	private AFT_FSDKEngine engine = new AFT_FSDKEngine();
	private AFR_FSDKEngine engineR = new AFR_FSDKEngine();
	private AFR_FSDKMatching score = new AFR_FSDKMatching();
	List<AFT_FSDKFace> result = new ArrayList<>();
	private byte[] datas = null;
	AFT_FSDKFace mAFT_FSDKFace = null;
	
	private String name;
	private Thread th;

	public FaceView(Context context, Camera camera) {

		super(context);
		this.camera = camera;
		this.context = context;

		holder = getHolder();
		holder.addCallback(this);

		if (camera != null) {
			camera.setDisplayOrientation(0);
			width = camera.getParameters().getPictureSize().width;
			height = camera.getParameters().getPictureSize().height;
		}

		AFT_FSDKError err = engine.AFT_FSDK_InitialFaceEngine(MGridActivity.appid, MGridActivity.ft_key,
				AFT_FSDKEngine.AFT_OPF_0_HIGHER_EXT, 16, 5);
		Log.e(Tag, "AFT_FSDKError:" + err.getCode());

		AFR_FSDKError error = engineR.AFR_FSDK_InitialEngine(MGridActivity.appid, MGridActivity.fr_key);
		Log.e(Tag, "AFR_FSDKError:" + error.getCode());

		th=new Thread(this);
		th.start();

	}

	@Override
	public void run() {

		while (!Thread.currentThread().isInterrupted()) {

			synchronized (FaceView.this) {
				
				
				try {

					Thread.sleep(1500);
					
					if (camera != null && isPreview) {
						isPreview = false;
						camera.setOneShotPreviewCallback(FaceView.this);
						Log.e(Tag, "setOneShotPreview...");
					}

					
					wait();

				} catch (Exception e) {

					e.printStackTrace();
					Thread.currentThread().interrupt();

				}
				
			}	

		}

	}

	@Override
	public void onPreviewFrame(byte[] data, Camera camera) {

		new MyAsyncTask().execute(data);

	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

		if (holder.getSurface() == null) {
			return;
		}

	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {

		try {

			camera.setPreviewDisplay(holder);
			camera.startPreview();

		} catch (Exception e) {

		}

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {

		isPreview = false;
		camera.setPreviewCallback(null);
		camera.stopPreview();

		Log.e(Tag, "Destroyed");

	}

	class MyAsyncTask extends AsyncTask<byte[], Void, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(byte[]... bytes) {
				
				
				result.clear();
				AFT_FSDKError err = engine.AFT_FSDK_FaceFeatureDetect(bytes[0], width, height, AFT_FSDKEngine.CP_PAF_NV21,
						result);
				Log.e(Tag, err.getCode() + "::" + width + "::" + height);

				for (AFT_FSDKFace face : result) {
					Log.d(Tag, "Face:" + face.toString());
					Log.e(Tag, "找到人脸");
				}

				if (!result.isEmpty()) {
					datas = bytes[0].clone();
					mAFT_FSDKFace = result.get(0).clone();

					AFR_FSDKFace face = new AFR_FSDKFace();
					AFR_FSDKError errR = engineR.AFR_FSDK_ExtractFRFeature(datas, width, height, AFR_FSDKEngine.CP_PAF_NV21,
							new Rect(mAFT_FSDKFace.getRect()), mAFT_FSDKFace.getDegree(), face);
					Log.e(Tag, "" + errR.getCode());

					
					for (FaceDB.FaceRegist fr : MyApplication.mFaceDB.mRegister) {
						for (AFR_FSDKFace faces : fr.mFaceList.values()) {
							errR = engineR.AFR_FSDK_FacePairMatching(face, faces, score);												
						    if(score.getScore()>0.6)
						    {
						    	name=fr.mName;
						    	return null;
						    }
						}
					}
					
				} else {
					
				}

					
				return null;
				
			
	
		}

		@Override
		protected void onPostExecute(Void results) {
			super.onPostExecute(results);

			
			synchronized (FaceView.this) {
				
				if (!result.isEmpty()) {
					
					if(score.getScore()>0.6)
					{
						//Toast.makeText(context, "人脸识别成功,当前用户:"+name+",相似度:"+score.getScore(), Toast.LENGTH_LONG).show();
						AllUtilS.showToast(context, "人脸识别成功,当前用户:"+name+",相似度:"+score.getScore());
						th.interrupt();
						((FaceActivity)context).finish();
					}else {
						//Toast.makeText(context,  score.getScore()+"：：人脸识别失败", Toast.LENGTH_SHORT).show();
						AllUtilS.showToast(context, score.getScore()+"：：人脸识别失败");
					}
				}else
				{
					//Toast.makeText(context, "未找到人脸", Toast.LENGTH_SHORT).show();
					AllUtilS.showToast(context, "未找到人脸");
				}
				
				isPreview = true;
				FaceView.this.notifyAll();
				
			}
			
	
		}

	}

}
