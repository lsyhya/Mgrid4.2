package com.sg.web;

import java.util.HashMap;
import java.util.Map;

import com.sg.web.base.ViewObjectBase;

public class AutoSigListObject extends ViewObjectBase {

	float fontSize;
	String fontColor, lineColor;


	
	Map<Integer, String> mapHvlaue = new HashMap<>();
	Map<Integer, String> mapDvlaue = new HashMap<>();
	Map<Integer, String> mapMvlaue = new HashMap<>();
	Map<Integer, String> mapYvlaue = new HashMap<>();
	
	
	



	

	public Map<Integer, String> getMapHvlaue() {
		return mapHvlaue;
	}

	public void setMapHvlaue(Map<Integer, String> mapHvlaue) {
		this.mapHvlaue = mapHvlaue;
	}

	public Map<Integer, String> getMapDvlaue() {
		return mapDvlaue;
	}

	public void setMapDvlaue(Map<Integer, String> mapDvlaue) {
		this.mapDvlaue = mapDvlaue;
	}

	public Map<Integer, String> getMapMvlaue() {
		return mapMvlaue;
	}

	public void setMapMvlaue(Map<Integer, String> mapMvlaue) {
		this.mapMvlaue = mapMvlaue;
	}

	public Map<Integer, String> getMapYvlaue() {
		return mapYvlaue;
	}

	public void setMapYvlaue(Map<Integer, String> mapYvlaue) {
		this.mapYvlaue = mapYvlaue;
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

	public String getLineColor() {
		return lineColor;
	}

	public void setLineColor(String lineColor) {
		this.lineColor = lineColor;
	}

}
