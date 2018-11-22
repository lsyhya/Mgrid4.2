package com.sg.web;

import com.sg.web.base.ViewObjectBase;

public class ImageObject extends ViewObjectBase{

	String imagePath;
	String hrefUrl;
	String onClickEvent;

	public String getOnClickEvent() {
		return onClickEvent;
	}

	public void setOnClickEvent(String onClickEvent) {
		this.onClickEvent = onClickEvent;
	}

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
