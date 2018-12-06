package com.mgrid.main.face;

import com.arcsoft.facerecognition.AFR_FSDKFace;

import android.graphics.Bitmap;

public class FaceBase {


	AFR_FSDKFace face;
	Bitmap bitmap;

	public FaceBase(AFR_FSDKFace face, Bitmap bitmap) {

		
		this.face = face;
		this.bitmap = bitmap;

	}

	public AFR_FSDKFace getFace() {
		return face;
	}

	public void setFace(AFR_FSDKFace face) {
		this.face = face;
	}

	public Bitmap getBitmap() {
		return bitmap;
	}

	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}

}
