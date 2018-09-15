package com.sg.web;

import com.sg.web.base.ViewObjectBase;

public class ImageObject extends ViewObjectBase{

	String imagePath;
	String hrefUrl;

	public String getHrefUrl() {
		return hrefUrl;
	}

	public void setHrefUrl(String hrefUrl) {
		this.hrefUrl = hrefUrl;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}
	
	
	
	
}
