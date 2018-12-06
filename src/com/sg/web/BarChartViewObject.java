package com.sg.web;

import java.util.LinkedList;
import java.util.List;

import com.sg.web.base.ViewObjectBase;

public class BarChartViewObject extends ViewObjectBase{
	
	int startYear;
	boolean math;
	String scaleColor,fontColor;

	List<Double> dList= new LinkedList<Double>();
	List<Double> mList= new LinkedList<Double>();	
	List<Double> yList= new LinkedList<Double>();
	
	
	
	public String getScaleColor() {
		return scaleColor;
	}

	public void setScaleColor(String scaleColor) {
		this.scaleColor = scaleColor;
	}



	public String getFontColor() {
		return fontColor;
	}

	public void setFontColor(String fontColor) {
		this.fontColor = fontColor;
	}

	public List<Double> getdList() {
		return dList;
	}

	public void setdList(List<Double> dList) {
		this.dList = dList;
	}

	public List<Double> getmList() {
		return mList;
	}

	public void setmList(List<Double> mList) {
		this.mList = mList;
	}

	public List<Double> getyList() {
		return yList;
	}

	public void setyList(List<Double> yList) {
		this.yList = yList;
	}

	public boolean isMath() {
		return math;
	}

	public void setMath(boolean math) {
		this.math = math;
	}

	public int getStartYear() {
		return startYear;
	}

	public void setStartYear(int startYear) {
		this.startYear = startYear;
	}
	

}
