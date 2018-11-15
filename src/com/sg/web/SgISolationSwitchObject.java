package com.sg.web;

import com.sg.web.base.ViewObjectBase;

public class SgISolationSwitchObject extends ViewObjectBase{

	
	float rotateAngle;
	boolean close;
	String color;
	public float getRotateAngle() {
		return rotateAngle;
	}
	public void setRotateAngle(float rotateAngle) {
		this.rotateAngle = rotateAngle;
	}
	
	public boolean isClose() {
		return close;
	}
	public void setClose(boolean close) {
		this.close = close;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	
	
	
}
