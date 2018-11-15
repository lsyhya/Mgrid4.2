package com.sg.web;

import java.util.HashMap;
import java.util.Map;

import com.sg.web.base.ViewObjectBase;

public class ChangeUserInfoObject extends ViewObjectBase{

	private int index=0;
	private Map<String,String> mapData=new HashMap<>();

	public Map<String, String> getMapData() {
		return mapData;
	}

	public void setMapData(Map<String, String> mapData) {
		this.mapData = mapData;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}
	
}
