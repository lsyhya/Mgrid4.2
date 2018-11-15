package com.sg.web;

import java.util.List;

import com.sg.web.base.ViewObjectBase;

public class SignalListObject extends ViewObjectBase{
	
	
	String foreColor;
	String backgroundColor;
	String oddRowBackground;
	List<List<String>> data;
	
	public List<List<String>> getData() {
		return data;
	}
	public void setData(List<List<String>> data) {
		this.data = data;
	}
	public String getForeColor() {
		return foreColor;
	}
	public void setForeColor(String foreColor) {
		this.foreColor = foreColor;
	}
	public String getBackgroundColor() {
		return backgroundColor;
	}
	public void setBackgroundColor(String backgroundColor) {
		this.backgroundColor = backgroundColor;
	}
	public String getOddRowBackground() {
		return oddRowBackground;
	}
	public void setOddRowBackground(String oddRowBackground) {
		this.oddRowBackground = oddRowBackground;
	}
	public String getEvenRowBackground() {
		return evenRowBackground;
	}
	public void setEvenRowBackground(String evenRowBackground) {
		this.evenRowBackground = evenRowBackground;
	}
	String evenRowBackground;

}
