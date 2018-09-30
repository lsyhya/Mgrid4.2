package com.sg.web;

import java.util.List;

import com.sg.web.base.ViewObjectBase;

public class ClickPieChartObject extends ViewObjectBase{

	
	float  fontSize;
	String fontColor;
	List<String> colorData;
	List<String> textData;
	List<String> dataList;
	
	public List<String> getDataList() {
		return dataList;
	}
	public void setDataList(List<String> dataList) {
		this.dataList = dataList;
	}
	public float getFontSize() {
		return fontSize;
	}
	public void setFontSize(float fontSize) {
		this.fontSize = fontSize;
	}
	public String getFontColor() {
		return fontColor;
	}
	public void setFontColor(String fontColor) {
		this.fontColor = fontColor;
	}
	public List<String> getColorData() {
		return colorData;
	}
	public void setColorData(List<String> colorData) {
		this.colorData = colorData;
	}
	public List<String> getTextData() {
		return textData;
	}
	public void setTextData(List<String> textData) {
		this.textData = textData;
	} 
	
	
}
