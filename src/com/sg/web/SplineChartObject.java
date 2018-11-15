package com.sg.web;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.sg.web.base.ViewObjectBase;

public class SplineChartObject extends ViewObjectBase{
	
	
	String fontSize;
	String fontColor;
	String xColor;
	String scaleColor;
	List<String> lableList;
	
	//Map<Integer,List<Map<Double, Double>>> mapData = new HashMap<>();
	Map<Integer, List<LinkedHashMap<Double, Double>>> ldata = new HashMap<>();
	
	public Map<Integer, List<LinkedHashMap<Double, Double>>> getLdata() {
		return ldata;
	}
	public void setLdata(Map<Integer, List<LinkedHashMap<Double, Double>>> ldata) {
		this.ldata = ldata;
	}
//	public Map<Integer, List<Map<Double, Double>>> getMapData() {
//		return mapData;
//	}
//	public void setMapData(Map<Integer, List<Map<Double, Double>>> mapData) {
//		this.mapData = mapData;
//	}
	public String getFontSize() {
		return fontSize;
	}
	public void setFontSize(String fontSize) {
		this.fontSize = fontSize;
	}
	public String getFontColor() {
		return fontColor;
	}
	public void setFontColor(String fontColor) {
		this.fontColor = fontColor;
	}
	public String getxColor() {
		return xColor;
	}
	public void setxColor(String xColor) {
		this.xColor = xColor;
	}
	public String getScaleColor() {
		return scaleColor;
	}
	public void setScaleColor(String scaleColor) {
		this.scaleColor = scaleColor;
	}
	public List<String> getLableList() {
		return lableList;
	}
	public void setLableList(List<String> lableList) {
		this.lableList = lableList;
	}
	public List<String> getColorData() {
		return colorData;
	}
	public void setColorData(List<String> colorData) {
		this.colorData = colorData;
	}
	List<String> colorData;

}
