package com.sg.web;

import com.sg.web.base.ViewObjectBase;

public class ChangeLabelObject extends ViewObjectBase{

	String text;
	String textColor;
	float textSize;
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getTextColor() {
		return textColor;
	}
	public void setTextColor(String textColor) {
		this.textColor = textColor;
	}
	public float getTextSize() {
		return textSize;
	}
	public void setTextSize(float textSize) {
		this.textSize = textSize;
	}
	
	
	
	
}
