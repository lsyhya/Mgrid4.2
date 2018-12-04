package com.mgrid.main;

import com.mgrid.fuction.FaceView;

import android.app.Activity;
import android.content.Intent;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;

@SuppressWarnings("deprecation")
public class FaceActivity extends Activity implements OnClickListener{
	
	LinearLayout lin;
	Button btn;
	Camera camera;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		setContentView(R.layout.faceactivity);
		lin=(LinearLayout) findViewById(R.id.lin);
		btn=(Button) findViewById(R.id.back_btn);
		btn.setOnClickListener(this);
		openCamera();
		FaceView view=new FaceView(this, camera);		
		lin.addView(view);
		
	}

	private void openCamera() {
		
		try {
			
			camera=Camera.open(FindFrontCamera());
			Camera.Parameters para=camera.getParameters();
			para.setPreviewFormat(ImageFormat.NV21);
			camera.setParameters(para);
			
		} catch (Exception e) {
			
		}
	}
	

	
	private int FindFrontCamera(){
        int cameraCount = 0;
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        cameraCount = Camera.getNumberOfCameras(); // get cameras number

        for ( int camIdx = 0; camIdx < cameraCount;camIdx++ ) {
            Camera.getCameraInfo( camIdx, cameraInfo ); // get camerainfo
            if ( cameraInfo.facing ==Camera.CameraInfo.CAMERA_FACING_FRONT ) {
                // 代表摄像头的方位，目前有定义值两个分别为CAMERA_FACING_FRONT前置和CAMERA_FACING_BACK后置
                return camIdx;
            }
        }
        return -1;
    }
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		 if(camera!=null)
	        {
	            camera.stopPreview();
	            camera.release();
	            camera=null;

	        }
	}

	@Override
	public void onClick(View v) {
		
		if(v.getId()==R.id.back_btn)
		{
			Intent i=new Intent();
			i.putExtra("Type", "true");
			setResult(RESULT_OK,  i);						
			finish();
		}
		
	}

	
	
}
