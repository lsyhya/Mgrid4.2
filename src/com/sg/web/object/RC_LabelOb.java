package com.sg.web.object;

import java.util.ArrayList;
import java.util.List;

import org.apache.httpcore.HttpResponse;

public class RC_LabelOb {

	 HttpResponse response;
	 String value;
	 List<Float> listValue = new ArrayList<Float>();
	 

	public List<Float> getListValue() {
		return listValue;
	}

	public void setListValue(List<Float> listValue) {
		this.listValue = listValue;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public HttpResponse getResponse() {
		return response;
	}

	public void setResponse(HttpResponse response) {
		this.response = response;
	}	
	 
	
}
