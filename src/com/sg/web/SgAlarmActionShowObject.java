package com.sg.web;

import com.sg.web.base.ViewObjectBase;

public class SgAlarmActionShowObject extends ViewObjectBase{
	
	
	String textColor;
	float textSize;
	String text;
	
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
