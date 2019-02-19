package com.sg.web;

import java.util.ArrayList;
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
	List<Double> cList= new LinkedList<Double>();
	List<String> strList= new LinkedList<String>();
	List<String> colorList= new ArrayList<String>();
	List<String> dataLable= new ArrayList<String>();
	
	
	public List<String> getDataLable() {
		return dataLable;
	}

	public void setDataLable(List<String> dataLable) {
		this.dataLable = dataLable;
	}

	public List<String> getColorList() {
		return colorList;
	}

	public void setColorList(List<String> colorList) {
		this.colorList = colorList;
	}

	public List<String> getStrList() {
		return strList;
	}

	public void setStrList(List<String> strList) {
		this.strList = strList;
	}

	public List<Double> getcList() {
		return cList;
	}

	public void setcList(List<Double> cList) {
		this.cList = cList;
	}

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
