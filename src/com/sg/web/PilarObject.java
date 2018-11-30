package com.sg.web;

import com.sg.web.base.ViewObjectBase;

public class PilarObject extends ViewObjectBase{

	String warmColor,normColor;
	float  warmPer,maxValue,datas;
	public String getWarmColor() {
		return warmColor;
	}
	public void setWarmColor(String warmColor) {
		this.warmColor = warmColor;
	}
	public String getNormColor() {
		return normColor;
	}
	public void setNormColor(String normColor) {
		this.normColor = normColor;
	}
	public float getWarmPer() {
		return warmPer;
	}
	public void setWarmPer(float warmPer) {
		this.warmPer = warmPer;
	}
	public float getMaxValue() {
		return maxValue;
	}
	public void setMaxValue(float maxValue) {
		this.maxValue = maxValue;
	}
	public float getDatas() {
		return datas;
	}
	public void setDatas(float datas) {
		this.datas = datas;
	}
	
	
	
}
