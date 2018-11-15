package com.sg.web.object;

import java.util.ArrayList;
import java.util.List;

import org.apache.httpcore.HttpResponse;

public class HisEventOb {
	
	 public HttpResponse getResponse() {
		return response;
	}
	public void setResponse(HttpResponse response) {
		this.response = response;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getEquipName() {
		return equipName;
	}
	public void setEquipName(String equipName) {
		this.equipName = equipName;
	}
	public List<List<String>> getListData() {
		return listData;
	}
	public void setListData(List<List<String>> listData) {
		this.listData = listData;
	}
	
	 HttpResponse response;	
	 String startTime,endTime;
	 String equipName;
	 List<List<String>> listData=new ArrayList<>();

}
