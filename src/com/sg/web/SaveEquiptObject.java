package com.sg.web;

import java.util.List;

import com.sg.web.base.ViewObjectBase;

public class SaveEquiptObject extends ViewObjectBase{
	
	String titleColr,textColor,btnColor;
	List<String> nameList;
	String foreColor;
	String backgroundColor;
	String evenRowBackground,oddRowBackground;

	
	


	public String getEvenRowBackground() {
		return evenRowBackground;
	}



	public void setEvenRowBackground(String evenRowBackground) {
		this.evenRowBackground = evenRowBackground;
	}



	public String getOddRowBackground() {
		return oddRowBackground;
	}



	public void setOddRowBackground(String oddRowBackground) {
		this.oddRowBackground = oddRowBackground;
	}

	int oddalpha;
	int evenalpha;
	

	public List<String> getNameList() {
		return nameList;
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

	
	public int getOddalpha() {
		return oddalpha;
	}



	public void setOddalpha(int oddalpha) {
		this.oddalpha = oddalpha;
	}



	

	public int getEvenalpha() {
		return evenalpha;
	}



	public void setEvenalpha(int evenalpha) {
		this.evenalpha = evenalpha;
	}




	public void setNameList(List<String> nameList) {
		this.nameList = nameList;
	}

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

}
