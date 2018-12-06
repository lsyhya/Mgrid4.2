package com.mgrid.main.face.facelistview;

import android.graphics.Bitmap;

public class FaceBean {
	
	Bitmap bitmap;
	String name;
	
	public FaceBean(Bitmap bitmap,String name)
	{
		this.bitmap=bitmap;
		this.name=name;
	}
	
	
	public Bitmap getBitmap() {
		return bitmap;
	}
	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	

}
