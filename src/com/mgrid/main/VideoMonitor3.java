package com.mgrid.main;

import com.mgrid.main.videoUtil.HikUtil;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

public class VideoMonitor3 extends Activity implements OnClickListener{

	private static final String TAG = "MainActivity";

	private SurfaceView surfaceView;
	private Button btn;

	private static final int PLAY_HIK_STREAM_CODE = 1001;

	private static final String IP_ADDRESS = "192.168.1.12";

	private static final int PORT = 8000;
	private static final String USER_NAME = "admin";

	private static final String PASSWORD = "12345";

	private HikUtil hikUtil;

	private Handler mHandler = new Handler(new Handler.Callback() {
		@Override
		public boolean handleMessage(Message msg) {
			switch (msg.what) {
			case PLAY_HIK_STREAM_CODE:
				hikUtil.playOrStopStream();
				break;

			default:
				break;
			}
			return false;
		}
	});

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		setContentView(R.layout.videomonitor3);

		surfaceView = (SurfaceView) findViewById(R.id.surfaceView_video3);
		btn=(Button) findViewById(R.id.btn_video3);
		btn.setOnClickListener(this);
		
		HikUtil.initSDK();
		hikUtil = new HikUtil();
		hikUtil.initView(surfaceView);
		hikUtil.setDeviceData(IP_ADDRESS, PORT, USER_NAME, PASSWORD);
		hikUtil.loginDevice(mHandler, PLAY_HIK_STREAM_CODE);

	}

	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
		case R.id.btn_video3:
			
			
			finish();
			
			break;

		default:
			break;
		}
		
	}
	
	@Override
	protected void onDestroy() {		
		super.onDestroy();
		
		hikUtil.playOrStopStream();
		
	}

}
