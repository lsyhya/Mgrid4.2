package com.sg.web;

import com.sg.web.base.ViewObjectBase;

public class ELabelObject extends ViewObjectBase{

	
	String text;
	float textSize;
	String textColor;
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public float getTextSize() {
		return textSize;
	}
	public void setTextSize(float textSize) {
		this.textSize = textSize;
	}
	public String getTextColor() {
		return textColor;
	}
	public void setTextColor(String textColor) {
		this.textColor = textColor;
	}
	
	
	
}
