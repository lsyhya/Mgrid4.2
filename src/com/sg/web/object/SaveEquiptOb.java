package com.sg.web.object;

import java.util.ArrayList;
import java.util.List;

import org.apache.httpcore.HttpResponse;

public class SaveEquiptOb {
	
	 HttpResponse response;	
	 String time;
	 String equipName;
	 List<List<String>> listData=new ArrayList<>();
	 
	public List<List<String>> getListData() {
		return listData;
	}
	public void setListData(List<List<String>> listData) {
		this.listData = listData;
	}
	public HttpResponse getResponse() {
		return response;
	}
	public void setResponse(HttpResponse response) {
		this.response = response;
	}

	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getEquipName() {
		return equipName;
	}
	public void setEquipName(String equipName) {
		this.equipName = equipName;
	}
	 

}
