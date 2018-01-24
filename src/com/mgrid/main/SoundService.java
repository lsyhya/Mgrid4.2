package com.mgrid.main;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Environment;
import android.os.IBinder;

public class SoundService extends Service {

	private MediaPlayer mp;

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		mp = new MediaPlayer();
		mp.setLooping(true);
		// String song = Environment.getExternalStorageDirectory().getPath() +
		// "/vtu_pagelist/Alarm.wav";
		// System.out.println(song);
		synchronized (MGridActivity.NewWavPath) {
			try {
				mp.setDataSource(MGridActivity.NewWavPath);
				mp.prepare();
				mp.start();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	// �ͷ�
	private void ReleasePlayer() {
		if (mp != null) {
			mp.stop();

			// �ؼ����
			mp.reset();

			mp.release();
			mp = null;
		}

	}

	@Override
	public IBinder onBind(Intent intent) {

		return null;
	}

	@Override
	public void onDestroy() {

		super.onDestroy();
		mp.release();
		stopSelf();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		boolean playing = intent.getBooleanExtra("playing", false);
		if (playing) {
			if (mp != null && !mp.isPlaying()) {
				mp.start();
			} else if (mp == null) {
				mp = new MediaPlayer();
				mp.setLooping(true);
				// String song =
				// Environment.getExternalStorageDirectory().getPath() +
				// "/vtu_pagelist/Alarm.wav";
				synchronized (MGridActivity.NewWavPath) {
					try {
						mp.setDataSource(MGridActivity.NewWavPath);
						mp.prepare();
						mp.start();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		} else {

			if (mp != null && mp.isPlaying()) {
				mp.pause();
				mp.stop();
				mp.reset();
				mp.release();
				mp = null;
			}
		}

		/*
		 * START_STICKY�����service���̱�kill��������service��״̬Ϊ��ʼ״̬�������������͵�intent����
		 * START_NOT_STICKY
		 * ������ճ�Եġ���ʹ���������ֵʱ�������ִ����onStartCommand�󣬷����쳣kill����ϵͳ�����Զ������÷���
		 * START_REDELIVER_INTENT
		 * ���ش�Intent��ʹ���������ֵʱ�������ִ����onStartCommand�󣬷����쳣kill��
		 * ��ϵͳ���Զ������÷��񣬲���Intent��ֵ���롣
		 * START_STICKY_COMPATIBILITY��START_STICKY�ļ��ݰ汾��������֤����kill��һ����������
		 */
		return START_NOT_STICKY;
	}

}