package com.sg.web;

import java.util.List;

import com.sg.web.base.ViewObjectBase;

public class HisEventObject extends ViewObjectBase{

	String titleColr,textColor,btnColor;
	List<String> nameList;
	String foreColor;
	String backgroundColor;
	String oddRowBackground;
	String evenRowBackground;
	List<String> lstTitles;
	
	public List<String> getLstTitles() {
		return lstTitles;
	}
	public void setLstTitles(List<String> lstTitles) {
		this.lstTitles = lstTitles;
	}
	int alpha=1;
	public String getTitleColr() {
		return titleColr;
	}
	public void setTitleColr(String titleColr) {
		this.titleColr = titleColr;
	}
	public String getTextColor() {
		return textColor;
	}
	public void setTextColor(String textColor) {
		this.textColor = textColor;
	}
	public String getBtnColor() {
		return btnColor;
	}
	public void setBtnColor(String btnColor) {
		this.btnColor = btnColor;
	}
	public List<String> getNameList() {
		return nameList;
	}
	public void setNameList(List<String> nameList) {
		this.nameList = nameList;
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
	public int getAlpha() {
		return alpha;
	}
	public void setAlpha(int alpha) {
		this.alpha = alpha;
	}

	
}
