package com.sg.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.sg.web.base.ViewObjectBase;

public class AlarmLevelObject extends ViewObjectBase{

	List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
	
	String titleColor,lineColor,infoColor;



	public String getTitleColor() {
		return titleColor;
	}

	public void setTitleColor(String titleColor) {
		this.titleColor = titleColor;
	}

	public String getLineColor() {
		return lineColor;
	}

	public void setLineColor(String lineColor) {
		this.lineColor = lineColor;
	}

	public String getInfoColor() {
		return infoColor;
	}

	public void setInfoColor(String infoColor) {
		this.infoColor = infoColor;
	}

	public List<Map<String, Object>> getList() {
		return list;
	}

	public void setList(List<Map<String, Object>> list) {
		this.list = list;
	}
	
	
	
	
}
