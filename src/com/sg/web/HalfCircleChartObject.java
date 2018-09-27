package com.sg.web;

import java.util.List;

import com.sg.web.base.ViewObjectBase;

public class HalfCircleChartObject extends ViewObjectBase{
	
	private String FontColor, ScaleColor;
	float   maxValue,minValue;
	List<String> listColor;
	List<String>  listValue;
	
	public List<String> getListColor() {
		return listColor;
	}
	public void setListColor(List<String> listColor) {
		this.listColor = listColor;
	}

	public List<String> getListValue() {
		return listValue;
	}
	public void setListValue(List<String> listValue) {
		this.listValue = listValue;
	}
	public String getFontColor() {
		return FontColor;
	}
	public void setFontColor(String fontColor) {
		FontColor = fontColor;
	}
	public String getScaleColor() {
		return ScaleColor;
	}
	public void setScaleColor(String scaleColor) {
		ScaleColor = scaleColor;
	}
	public float getMaxValue() {
		return maxValue;
	}
	public void setMaxValue(float maxValue) {
		this.maxValue = maxValue;
	}
	public float getMinValue() {
		return minValue;
	}
	public void setMinValue(float minValue) {
		this.minValue = minValue;
	}
	

}
