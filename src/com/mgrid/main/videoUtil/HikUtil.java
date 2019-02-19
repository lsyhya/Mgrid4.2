package com.mgrid.main.videoUtil;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.hikvision.netsdk.ExceptionCallBack;
import com.hikvision.netsdk.HCNetSDK;
import com.hikvision.netsdk.NET_DVR_DEVICEINFO_V30;
import com.hikvision.netsdk.NET_DVR_PREVIEWINFO;
import com.hikvision.netsdk.RealPlayCallBack;

import org.MediaPlayer.PlayM4.Player;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;

/**
 * Created date: 2017/4/11
 * Author:  Leslie
 * 浣跨敤娴峰悍SDK鎾斁瑙嗛娴佺殑宸ュ叿绫�.
 * 鍓嶆彁锛�1->/libs/涓嬫斁鍏ワ細AudioEngineSDK.jar,HCNetSDK.jar,PlayerSDK.jar
 * 2->/src/main/jniLibs/涓嬫斁鍏ワ細寰堝 .so 鏂囦欢.
 * 3->娣诲姞缃戠粶鏉冮檺
 * 鐩墠鍙鐞嗘捣搴锋憚鍍忓ご(瀹ゅ唴鏋瀷缃戠粶鎽勫儚鏈�-銆愬瀷鍙凤細DS-2CD5026EFWD銆�-銆愯蒋浠剁増鏈細V5.4.5_170222銆�)
 * 浣嗘槸璇ヤ緥瀛愪笉浠呴檺浜庤繖绉嶅瀷鍙风殑銆�
 * 浣跨敤鏂规硶[鐢变簬瑕侀瑙� 2 璺紝鎵�浠ュ緢澶氶潤鎬佹柟娉曪紝闈欐�佸彉閲忓幓鎺変簡锛岃皟鐢ㄦ祦绋嬩篃鍙樺寲浜哴锛�
 * 1.HikUtil.initSDK();
 * 2.HikUtil hikUtil = new HikUtil();
 * 2.hikUtil.initView(surfaceView);
 * 3.hikUtil.setDeviceData("192.168.1.22",8000,"admin","eyecool2016");
 * 4.hikUtil.loginDevice(mHandler,LOGIN_SUCCESS_CODE);
 * 5.hikUtil.playOrStopStream();
 */

public class HikUtil {
    private static final String TAG = "HikUtil";
    private static final int HIK_MAIN_STREAM_CODE = 0;      //涓荤爜娴�
    private static final int HIK_SUB_STREAM_CODE = 1;      //瀛愮爜娴�
    private NET_DVR_DEVICEINFO_V30 m_oNetDvrDeviceInfoV30 = null;
    private  int m_iStartChan = 0;
    private  int m_iPort = -1;
    private  int m_iPlaybackID = -1;
    private  int logId = -1;
    private  int playId = -1;
    private SurfaceView mSurfaceView;
    public String mIpAddress;
    private  int mPort;
    private String mUserName;
    private String mPassWord;
    public  onPicCapturedListener mPicCapturedListener;
    private SimpleDateFormat sDateFormat;
    private  Player.MPInteger stWidth;
    private  Player.MPInteger stHeight;
    private  Player.MPInteger stSize;

    /**
     * 瀹氫箟鎺ュ彛锛岀敤浜庣洃鍚浘鐗囨埅鍥炬垚鍔�
     */
    public interface onPicCapturedListener {
        void onPicCaptured(Bitmap bitmap, String bitmapFileAbsolutePath);

        void onPicDataSaved(byte[] picData);
    }

    public HikUtil() {
    }

    /**
     * 鍒濆鍖朒CNet SDK
     *
     * @return
     */
    public static boolean initSDK() {

        if (!HCNetSDK.getInstance().NET_DVR_Init()) {
            Log.e(TAG, "HCNetSDK ---------鍒濆鍖栧け璐�!");
            return false;
        }
        //鎵撳嵃鏃ュ織鍒版湰鍦帮紝鏆傛椂涓嶇敤鎵撳嵃
//        HCNetSDK.getInstance().NET_DVR_SetLogToFile(3, "/mnt/sdcard/sdklog/", true);
        return true;
    }

    public  void initView(SurfaceView surfaceView) {
        mSurfaceView = surfaceView;
        mSurfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                mSurfaceView.getHolder().setFormat(PixelFormat.TRANSLUCENT);
                Log.i(TAG, "surface is created" + m_iPort);
                if (-1 == m_iPort) {
                    return;
                }
                Surface surface = holder.getSurface();
                if (surface.isValid()) {
                    if (!Player.getInstance().setVideoWindow(m_iPort, 0, holder)) {
                        Log.e(TAG, "鎾斁鍣ㄨ缃垨閿�姣佹樉绀哄尯鍩熷け璐�!");
                    }
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                Log.i(TAG, "Player setVideoWindow release!" + m_iPort);
                if (-1 == m_iPort) {
                    return;
                }
                if (holder.getSurface().isValid()) {
                    if (!Player.getInstance().setVideoWindow(m_iPort, 0, null)) {
                        Log.e(TAG, "鎾斁鍣ㄨ缃垨閿�姣佹樉绀哄尯鍩熷け璐�!");
                    }
                }
            }
        });
    }

    /**
     * 閰嶇疆缃戠粶鎽勫儚澶村弬鏁�
     * @param ipAddress IP 鍦板潃
     * @param port 绔彛鍙凤紝榛樿鏄� 8000
     * @param userName 鐢ㄦ埛鍚�
     * @param passWord 瀵嗙爜
     */
    public  void setDeviceData(String ipAddress, int port, String userName, String passWord) {
        mIpAddress = ipAddress;
        mPort = port;
        mUserName = userName;
        mPassWord = passWord;

    }

    public  void loginDevice(final Handler handler, final int resultCode) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean loginState = login(mIpAddress, mPort, mUserName, mPassWord);
                Message message = handler.obtainMessage();
                message.obj = loginState;
                message.what = resultCode;
                handler.sendMessage(message);
            }
        }).start();
    }

    /**
     * 鎾斁鎴栬�呭仠姝㈡挱鏀捐棰戞祦
     */
    public  void playOrStopStream() {

        if (logId < 0) {
            Log.e(TAG, "璇峰厛鐧诲綍璁惧");
            return;
        }
        if (playId < 0) {   //鎾斁

            RealPlayCallBack fRealDataCallBack = getRealPlayerCbf();
            if (fRealDataCallBack == null) {
                Log.e(TAG, "fRealDataCallBack object is failed!");
                return;
            }
            Log.i(TAG, "m_iStartChan:" + m_iStartChan);

            NET_DVR_PREVIEWINFO previewInfo = new NET_DVR_PREVIEWINFO();
            previewInfo.lChannel = m_iStartChan;
            previewInfo.dwStreamType = HIK_SUB_STREAM_CODE;                                                             //瀛愮爜娴�
            previewInfo.bBlocked = 1;
            // HCNetSDK start preview
            playId = HCNetSDK.getInstance().NET_DVR_RealPlay_V40(logId, previewInfo, fRealDataCallBack);
            if (playId < 0) {
                Log.e(TAG, "瀹炴椂棰勮澶辫触!-----------------Err:" + HCNetSDK.getInstance().NET_DVR_GetLastError());
                return;
            }

            Log.i(TAG, "NetSdk 鎾斁鎴愬姛 锛�");
//            mPlayButton.setText("鍋滄");
        } else {    //鍋滄鎾斁
            if (playId < 0) {
                Log.e(TAG, "m_iPlayID < 0");
                return;
            }

            //  net sdk stop preview
            if (!HCNetSDK.getInstance().NET_DVR_StopRealPlay(playId)) {
                Log.e(TAG, "鍋滄棰勮澶辫触!----------------閿欒:" + HCNetSDK.getInstance().NET_DVR_GetLastError());
                return;
            }

            playId = -1;
            Player.getInstance().stopSound();
            // player stop play
            if (!Player.getInstance().stop(m_iPort)) {
                Log.e(TAG, "-------------------鏆傚仠澶辫触!");
                return;
            }

            if (!Player.getInstance().closeStream(m_iPort)) {
                Log.e(TAG, "-------------------鍏虫祦澶辫触!");
                return;
            }
            if (!Player.getInstance().freePort(m_iPort)) {
                Log.e(TAG, "-------------------閲婃斁鎾斁绔彛澶辫触!" + m_iPort);
                return;
            }
            m_iPort = -1;
            logId = -1;
            playId = -1;
//            mPlayButton.setText("鎾斁");
        }

    }

    private RealPlayCallBack getRealPlayerCbf() {
        RealPlayCallBack cbf = new RealPlayCallBack() {
            public void fRealDataCallBack(int iRealHandle, int iDataType, byte[] pDataBuffer, int iDataSize) {
                // 鎾斁閫氶亾1
                processRealData(1, iDataType, pDataBuffer, iDataSize, Player.STREAM_REALTIME);
            }
        };
        return cbf;
    }

    public  void processRealData(int iPlayViewNo, int iDataType, byte[] pDataBuffer, int iDataSize, int iStreamMode) {
        if (HCNetSDK.NET_DVR_SYSHEAD == iDataType) {
            if (m_iPort >= 0) {
                return;
            }
            m_iPort = Player.getInstance().getPort();
            if (m_iPort == -1) {
                Log.e(TAG, "鑾峰彇绔彛澶辫触锛�: " + Player.getInstance().getLastError(m_iPort));
                return;
            }
            Log.i(TAG, "鑾峰彇绔彛鎴愬姛锛�: " + m_iPort);
            if (iDataSize > 0) {
                if (!Player.getInstance().setStreamOpenMode(m_iPort, iStreamMode))  //set stream mode
                {
                    Log.e(TAG, "璁剧疆娴佹挱鏀炬ā寮忓け璐ワ紒");
                    return;
                }
                
                if (!Player.getInstance().openStream(m_iPort, pDataBuffer, iDataSize, 2 * 1024 * 1024)) //open stream
                {
                    Log.e(TAG, "鎵撳紑娴佸け璐ワ紒");
                    return;
                }
                
                if (!Player.getInstance().setDisplayBuf(m_iPort, 6)) {
					Log.e(TAG, "setDisplayBuf failed");
					return;
				}
                
                if (!Player.getInstance().play(m_iPort, mSurfaceView.getHolder())) {
                    Log.e(TAG, "鎾斁澶辫触锛�");
                    return;
                }
                if (!Player.getInstance().playSound(m_iPort)) {
                    Log.e(TAG, "浠ョ嫭鍗犳柟寮忔挱鏀鹃煶棰戝け璐ワ紒澶辫触鐮� :" + Player.getInstance().getLastError(m_iPort));
                    return;
                }
            }
        } else {
            if (!Player.getInstance().inputData(m_iPort, pDataBuffer, iDataSize)) {
//		    		Log.e(TAG, "inputData failed with: " + Player.getInstance().getLastError(m_iPort));
                for (int i = 0; i < 4000 && m_iPlaybackID >= 0; i++) {
                    if (!Player.getInstance().inputData(m_iPort, pDataBuffer, iDataSize))
                        Log.e(TAG, "杈撳叆娴佹暟鎹け璐�: " + Player.getInstance().getLastError(m_iPort));
                    else
                        break;
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


    private  boolean login(String ipAddress, int portNum, String userName, String passWord) {
        try {
            if (logId < 0) {
                // 鐧诲綍璁惧
                logId = loginDevice(ipAddress, portNum, userName, passWord);
                if (logId < 0) {
                    Log.e(TAG, "璁惧鐧诲綍澶辫触锛�");
                    return false;
                }
                // 鑾峰彇寮傚父鍥炶皟鍜屽紓甯歌缃殑鍥炶皟
                ExceptionCallBack oexceptionCbf = getExceptiongCbf();
                if (oexceptionCbf == null) {
                    Log.e(TAG, "寮傚父鍥炶皟瀵硅薄澶辫触锛�");
                    return false;
                }

                if (!HCNetSDK.getInstance().NET_DVR_SetExceptionCallBack(oexceptionCbf)) {
                    Log.e(TAG, "娉ㄥ唽鎺ユ敹寮傚父銆侀噸杩炴秷鎭洖璋冨嚱鏁板け璐� !");
                    return false;
                }

//                loginButton.setText("娉ㄩ攢");
                Log.i(TAG, "鐧诲綍鎴愬姛 锛�");
                return true;
            } else {
                // 鏄惁鐧诲嚭
                if (!HCNetSDK.getInstance().NET_DVR_Logout_V30(logId)) {
                    Log.e(TAG, " 鐢ㄦ埛娉ㄩ攢澶辫触!");
                    return false;
                }
//                loginButton.setText("鐧诲綍");
                logId = -1;
                return true;
            }
        } catch (Exception err) {
            Log.e(TAG, "閿欒: " + err.toString());
            return false;
        }
    }

    private  int loginDevice(String ipAddress, int portNum, String userName, String passWord) {
        //瀹炰緥鍖栬澶囦俊鎭璞�
        m_oNetDvrDeviceInfoV30 = new NET_DVR_DEVICEINFO_V30();
        if (null == m_oNetDvrDeviceInfoV30) {
            Log.e(TAG, "瀹炰緥鍖栬澶囦俊鎭�(NET_DVR_DEVICEINFO_V30)澶辫触!");
            return -1;
        }
        // call NET_DVR_Login_v30 to login on, port 8000 as default
        int iLogID = HCNetSDK.getInstance().NET_DVR_Login_V30(ipAddress, portNum, userName, passWord, m_oNetDvrDeviceInfoV30);
        if (iLogID < 0) {
            Log.e(TAG, "缃戠粶璁惧鐧诲綍澶辫触!-------------Err:" + HCNetSDK.getInstance().NET_DVR_GetLastError());
            return -1;
        }
        if (m_oNetDvrDeviceInfoV30.byChanNum > 0) {
            m_iStartChan = m_oNetDvrDeviceInfoV30.byStartChan;
        } else if (m_oNetDvrDeviceInfoV30.byIPChanNum > 0) {
            m_iStartChan = m_oNetDvrDeviceInfoV30.byStartDChan;
        }
        Log.i(TAG, "缃戠粶璁惧鐧诲綍鎴愬姛!");

        return iLogID;
    }

    private ExceptionCallBack getExceptiongCbf() {
        ExceptionCallBack oExceptionCbf = new ExceptionCallBack() {
            public void fExceptionCallBack(int iType, int iUserID, int iHandle) {
                System.out.println("recv exception------------------------------, type:" + iType);
            }
        };
        return oExceptionCbf;
    }

    /**
     * 鎴彇涓�甯у浘鐗�,鎴愬姛杩斿洖bitmap瀵硅薄锛屽け璐ヨ繑鍥瀗ull
     * 缁忔祴璇曞緱鍑猴細
     * 鑾峰彇鎴浘鏁版嵁鑰楁椂 <10ms
     * 鑾峰彇鎴浘鏁版嵁鍚庝繚瀛樺埌纾佺洏鑰楁椂 鈮�25ms
     * 浠庤幏鍙栨埅鍥炬暟-淇濆瓨鍒扮鐩�-瑙ｇ爜鏂囦欢鍒� bitmap 鑰楁椂 鈮�45ms
     */
    public Bitmap captureFrame(onPicCapturedListener picCapturedListener) {
        try {
            long time1 = System.currentTimeMillis();
            mPicCapturedListener = picCapturedListener;
            Player.MPInteger stWidth = new Player.MPInteger();
            Player.MPInteger stHeight = new Player.MPInteger();
            if (!Player.getInstance().getPictureSize(m_iPort, stWidth, stHeight)) {
                Log.e(TAG, "鑾峰彇鍥剧墖灏哄澶辫触---> error code:" + Player.getInstance().getLastError(m_iPort));
                return null;
            }
            int nSize = 5 * stWidth.value * stHeight.value;
            byte[] picBuf = new byte[nSize];
            Player.MPInteger stSize = new Player.MPInteger();
            if (!Player.getInstance().getBMP(m_iPort, picBuf, nSize, stSize)) {
                Log.e(TAG, "鑾峰彇浣嶅浘澶辫触----> error code:" + Player.getInstance().getLastError(m_iPort));
                return null;
            }
            long time2 = System.currentTimeMillis();
            if (sDateFormat == null) {
                sDateFormat = new SimpleDateFormat("yyyy-MM-dd-hh_mm_ss_Sss");
            }
            String date = sDateFormat.format(new java.util.Date());
            File dir = new File(Environment.getExternalStorageDirectory() + "/capture");
            if (!dir.exists()) {
                dir.mkdir();
            }
            File file = new File(dir, date + ".jpg");
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(picBuf, 0, stSize.value);
            fos.close();
            long time3 = System.currentTimeMillis();
            Bitmap bitmap = BitmapFactory.decodeFile(dir.getAbsolutePath() + "/" + date + ".jpg");
            long time4 = System.currentTimeMillis();
            //鍥剧墖淇濆瓨鎴愬姛浜嗭紝閫氱煡缁欏闈�
            mPicCapturedListener.onPicCaptured(bitmap, file.getAbsolutePath());
            return bitmap;
        } catch (Exception err) {
            Log.e(TAG, "error: " + err.toString());
        } finally {

            return null;
        }
    }

    /**
     * 鎴彇涓�甯у浘鐗�,鎴愬姛杩斿洖bitmap瀵硅薄锛屽け璐ヨ繑鍥瀗ull
     * 鍥剧墖鏁版嵁瀛樻斁鍦ㄥ唴瀛樹腑
     */
    public  byte[] captureFrameOnMemroy(onPicCapturedListener picCapturedListener, Handler handler) {
        try {
            long start = System.currentTimeMillis();
            mPicCapturedListener = picCapturedListener;
            if (stWidth == null) {
                stWidth = new Player.MPInteger();
            }
            if (stHeight == null) {
                stHeight = new Player.MPInteger();
            }
            if (!Player.getInstance().getPictureSize(m_iPort, stWidth, stHeight)) {
                Log.e(TAG, "鑾峰彇鍥剧墖灏哄澶辫触---> error code:" + Player.getInstance().getLastError(m_iPort));
                return null;
            }
            int nSize = 5 * stWidth.value * stHeight.value;
            byte[] picBuf = new byte[nSize];
            if (stSize == null) {

                stSize = new Player.MPInteger();
            }
            if (!Player.getInstance().getBMP(m_iPort, picBuf, nSize, stSize)) {
//                mPicCapturedListener.onPicDataSavedError();
                Log.e(TAG, "鑾峰彇浣嶅浘澶辫触----> error code:" + Player.getInstance().getLastError(m_iPort));
                return null;
            }
            //鍥剧墖淇濆瓨鏁版嵁鑾峰彇鎴愬姛浜嗭紝閫氱煡缁欏闈€�傛垨鑰呯敤handler鍙戦�佸嚭鍘�
           /* mPicCapturedListener.onPicDataSaved(picBuf);
            Message message = handler.obtainMessage();
            message.obj = picBuf;
            message.what = Constant.VIDEO_FRAME_PIC_DATA_SAVED;
            handler.sendMessage(message);
            long end = System.currentTimeMillis();*/
            return picBuf;
        } catch (Exception err) {
            Log.e(TAG, "error: " + err.toString());
        }
        return null;
    }
}
