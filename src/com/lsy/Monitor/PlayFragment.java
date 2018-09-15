package com.lsy.Monitor;

import org.MediaPlayer.PlayM4.Player;

import com.hikvision.netsdk.ExceptionCallBack;
import com.hikvision.netsdk.HCNetSDK;
import com.hikvision.netsdk.NET_DVR_DEVICEINFO_V30;
import com.hikvision.netsdk.NET_DVR_PREVIEWINFO;
import com.hikvision.netsdk.RealPlayCallBack;
import com.mgrid.main.MGridActivity;
import com.mgrid.main.MonitorActivity;
import com.mgrid.main.R;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

public class PlayFragment extends Fragment implements OnClickListener, Callback {

	private Button btn_back;
	private MonitorActivity act;
	private String TAG = "PlayFragment";
	private SurfaceView surface;

	private int m_iLogID = -1; // return by NET_DVR_Login_v30
	private int m_iPlayID = -1; // return by NET_DVR_RealPlay_V30
	private int m_iPlaybackID = -1; // return by NET_DVR_PlayBackByTime
	private NET_DVR_DEVICEINFO_V30 m_oNetDvrDeviceInfoV30 = null;

	private int m_iPort = -1; // play port
	private int m_iStartChan = 0; // start channel no
	private int m_iChanNum = 0; // channel number

	private boolean m_bMultiPlay = false;
	private boolean m_bStopPlayback = false;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		act = (MonitorActivity) activity;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.play_monitor, container, false);
		init(view);
		return view;
	}

	private void init(View view) {

		btn_back = (Button) view.findViewById(R.id.btn_play_back);
		surface = (SurfaceView) view.findViewById(R.id.surface);
		surface.getHolder().addCallback(this);
		btn_back.setOnClickListener(this);

		if (!initeSdk()) {
			Toast.makeText(act, "fail", Toast.LENGTH_LONG).show();
		}

	}

	@Override
	public void onClick(View v) {

		if (v == btn_back) {

			stopSinglePreview();
			act.finish();

		}
	}

	@Override
	public void onStart() {

		super.onStart();

	}

	@Override
	public void onResume() {
		super.onResume();
		MGridActivity.xianChengChi.execute(new Runnable() {

			@Override
			public void run() {
				loginPlay();
				playView();
			}
		});

	}

	private boolean initeSdk() {
		// init net sdk
		if (!HCNetSDK.getInstance().NET_DVR_Init()) {
			Log.e(TAG, "HCNetSDK init is failed!");
			return false;
		}
		HCNetSDK.getInstance().NET_DVR_SetLogToFile(3, "/mnt/sdcard/sdklog/", true);
		return true;
	}

	private void loginPlay() {
		try {
			if (m_iLogID < 0) {

				m_iLogID = loginDevice();
				if (m_iLogID < 0) {
					Log.e(TAG, "This device logins failed!");
					return;
				} else {
					System.out.println("m_iLogID=" + m_iLogID);
				}
				// get instance of exception callback and set
				ExceptionCallBack oexceptionCbf = getExceptiongCbf();
				if (oexceptionCbf == null) {
					Log.e(TAG, "ExceptionCallBack object is failed!");
					return;
				}

				if (!HCNetSDK.getInstance().NET_DVR_SetExceptionCallBack(oexceptionCbf)) {
					Log.e(TAG, "NET_DVR_SetExceptionCallBack is failed!");
					return;
				}

			} else {
				// whether we have logout
				if (!HCNetSDK.getInstance().NET_DVR_Logout_V30(m_iLogID)) {
					Log.e(TAG, " NET_DVR_Logout is failed!");
					return;
				}
				m_iLogID = -1;
			}
		} catch (Exception err) {
			Log.e(TAG, "login: " + err.toString());
		}
	}

	private void playView() {
		try {
			// ((InputMethodManager)
			// act.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
			// act.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
			if (m_iLogID < 0) {
				Log.e(TAG, "please login on device first");
				return;
			}

			if (m_iChanNum > 1)// preview more than a channel
			{

			} else // preivew a channel
			{
				if (m_iPlayID < 0) {
					System.out.println("我进来了");
					startSinglePreview();
				} else {
					stopSinglePreview();
				}
			}

		} catch (Exception err) {
			Log.e(TAG, "play: " + err.toString());
		}
	}

	private void startSinglePreview() {
		if (m_iPlaybackID >= 0) {
			Log.i(TAG, "Please stop palyback first");
			return;
		}
		RealPlayCallBack fRealDataCallBack = getRealPlayerCbf();
		if (fRealDataCallBack == null) {
			Log.e(TAG, "fRealDataCallBack object is failed!");
			return;
		}
		Log.i(TAG, "m_iStartChan:" + m_iStartChan);

		NET_DVR_PREVIEWINFO previewInfo = new NET_DVR_PREVIEWINFO();
		previewInfo.lChannel = m_iStartChan;
		previewInfo.dwStreamType = 0; // substream
		previewInfo.bBlocked = 1;
		previewInfo.byPreviewMode = 0;
		// NET_DVR_CLIENTINFO struClienInfo = new NET_DVR_CLIENTINFO();
		// struClienInfo.lChannel = m_iStartChan;
		// struClienInfo.lLinkMode = 0;
		// HCNetSDK start preview
		m_iPlayID = HCNetSDK.getInstance().NET_DVR_RealPlay_V40(m_iLogID, previewInfo, fRealDataCallBack);
		// m_iPlayID = HCNetSDK.getInstance().NET_DVR_RealPlay_V30(m_iLogID,
		// struClienInfo, fRealDataCallBack, false);
		if (m_iPlayID < 0) {
			Log.e(TAG, "NET_DVR_RealPlay is failed!Err:" + HCNetSDK.getInstance().NET_DVR_GetLastError());
			return;
		}
	}

	private RealPlayCallBack getRealPlayerCbf() {
		RealPlayCallBack cbf = new RealPlayCallBack() {
			public void fRealDataCallBack(int iRealHandle, int iDataType, byte[] pDataBuffer, int iDataSize) {
				// player channel 1
				processRealData(1, iDataType, pDataBuffer, iDataSize, Player.STREAM_REALTIME);
			}
		};
		return cbf;
	}

	public void processRealData(int iPlayViewNo, int iDataType, byte[] pDataBuffer, int iDataSize, int iStreamMode) {

		if (HCNetSDK.NET_DVR_SYSHEAD == iDataType) {
			if (m_iPort >= 0) {
				return;
			}
			m_iPort = Player.getInstance().getPort();
			if (m_iPort == -1) {
				Log.e(TAG, "getPort is failed with: " + Player.getInstance().getLastError(m_iPort));
				return;
			}
			Log.i(TAG, "getPort succ with: " + m_iPort);
			if (iDataSize > 0) {
				if (!Player.getInstance().setStreamOpenMode(m_iPort, iStreamMode)) // set stream mode
				{
					Log.e(TAG, "setStreamOpenMode failed");
					return;
				}
				if (!Player.getInstance().openStream(m_iPort, pDataBuffer, iDataSize, 200 * 1024)) // open stream
				{
					Log.e(TAG, "openStream failed");
					return;
				}
				if (!Player.getInstance().setDisplayBuf(m_iPort, 1)) {
					Log.e(TAG, "play failed");
					return;
				}
				
				if(!Player.getInstance().setHardDecode(m_iPort, 1))
				{
					Log.e(TAG, "setHardDecode failed");
					return;
				}

				if (!Player.getInstance().play(m_iPort, surface.getHolder())) {
					Log.e(TAG, "play failed");
					return;
				}
				if (!Player.getInstance().playSound(m_iPort)) {
					Log.e(TAG, "playSound failed with error code:" + Player.getInstance().getLastError(m_iPort));
					return;
				}
			}
		} else {
			if (!Player.getInstance().inputData(m_iPort, pDataBuffer, iDataSize)) {
				// Log.e(TAG, "inputData failed with: " +
				// Player.getInstance().getLastError(m_iPort));
				for (int i = 0; i < 4000 && m_iPlaybackID >= 0 && !m_bStopPlayback; i++) {
					if (Player.getInstance().inputData(m_iPort, pDataBuffer, iDataSize)) {
						break;

					}

					if (i % 100 == 0) {
						Log.e(TAG, "inputData failed with: " + Player.getInstance().getLastError(m_iPort) + ", i:" + i);
					}

					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();

					}
				}
			}

		}

	}

	private void stopSinglePreview() {
		if (m_iPlayID < 0) {
			Log.e(TAG, "m_iPlayID < 0");
			return;
		}

		// net sdk stop preview
		if (!HCNetSDK.getInstance().NET_DVR_StopRealPlay(m_iPlayID)) {
			Log.e(TAG, "StopRealPlay is failed!Err:" + HCNetSDK.getInstance().NET_DVR_GetLastError());
			return;
		}

		m_iPlayID = -1;
		stopSinglePlayer();
	}

	private void stopSinglePlayer() {
		Player.getInstance().stopSound();
		// player stop play
		if (!Player.getInstance().stop(m_iPort)) {
			Log.e(TAG, "stop is failed!");
			return;
		}
		
		if(!Player.getInstance().setHardDecode(m_iPort,0))
		{
			Log.e(TAG, "stop setHardDecode failed!");
			return;
		}

		if (!Player.getInstance().closeStream(m_iPort)) {
			Log.e(TAG, "closeStream is failed!");
			return;
		}
		if (!Player.getInstance().freePort(m_iPort)) {
			Log.e(TAG, "freePort is failed!" + m_iPort);
			return;
		}
		m_iPort = -1;
	}

	private ExceptionCallBack getExceptiongCbf() {
		ExceptionCallBack oExceptionCbf = new ExceptionCallBack() {
			public void fExceptionCallBack(int iType, int iUserID, int iHandle) {
				System.out.println("recv exception, type:" + iType);
			}
		};
		return oExceptionCbf;
	}

	private int loginDevice() {
		int iLogID = -1;

		iLogID = loginNormalDevice();

		return iLogID;
	}

	private int loginNormalDevice() {
		// get instance
		m_oNetDvrDeviceInfoV30 = new NET_DVR_DEVICEINFO_V30();
		if (null == m_oNetDvrDeviceInfoV30) {
			Log.e(TAG, "HKNetDvrDeviceInfoV30 new is failed!");
			return -1;
		}
		String strIP = act.getEtIP();
		int nPort = Integer.parseInt(act.getEtPort());
		String strUser = act.getEtUser();
		String strPsd = act.getEtPsd();

		// call NET_DVR_Login_v30 to login on, port 8000 as default
		int iLogID = HCNetSDK.getInstance().NET_DVR_Login_V30(strIP, nPort, strUser, strPsd, m_oNetDvrDeviceInfoV30);
		if (iLogID < 0) {
			Log.e(TAG, "NET_DVR_Login is failed!Err:" + HCNetSDK.getInstance().NET_DVR_GetLastError());
			return -1;
		}
		if (m_oNetDvrDeviceInfoV30.byChanNum > 0) {
			m_iStartChan = m_oNetDvrDeviceInfoV30.byStartChan;
			m_iChanNum = m_oNetDvrDeviceInfoV30.byChanNum;
		} else if (m_oNetDvrDeviceInfoV30.byIPChanNum > 0) {
			m_iStartChan = m_oNetDvrDeviceInfoV30.byStartDChan;
			m_iChanNum = m_oNetDvrDeviceInfoV30.byIPChanNum + m_oNetDvrDeviceInfoV30.byHighDChanNum * 256;
		}
		Log.i(TAG, "NET_DVR_Login is Successful!");

		return iLogID;
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		surface.getHolder().setFormat(PixelFormat.TRANSLUCENT);
		Log.i(TAG, "surface is created" + m_iPort);
		if (-1 == m_iPort) {
			return;
		}
		Surface surface = holder.getSurface();
		if (true == surface.isValid()) {
			if (false == Player.getInstance().setVideoWindow(m_iPort, 0, holder)) {
				Log.e(TAG, "Player setVideoWindow failed!");
			}
		}

	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		Log.i(TAG, "Player setVideoWindow release!" + m_iPort);
		if (-1 == m_iPort) {
			return;
		}
		if (true == holder.getSurface().isValid()) {
			if (false == Player.getInstance().setVideoWindow(m_iPort, 0, null)) {
				Log.e(TAG, "Player setVideoWindow failed!");
			}
		}

	}

}
