package com.sg.web;

import com.sg.web.base.ViewObjectBase;

public class DialObject extends ViewObjectBase{
	
	
	String fillColor,warmPerColor,lineColor;
	float warmPer;
	public String getFillColor() {
		return fillColor;
	}
	public void setFillColor(String fillColor) {
		this.fillColor = fillColor;
	}
	public String getWarmPerColor() {
		return warmPerColor;
	}
	public void setWarmPerColor(String warmPerColor) {
		this.warmPerColor = warmPerColor;
	}
	public String getLineColor() {
		return lineColor;
	}
	public void setLineColor(String lineColor) {
		this.lineColor = lineColor;
	}
	public float getWarmPer() {
		return warmPer;
	}
	public void setWarmPer(float warmPer) {
		this.warmPer = warmPer;
	}

}
