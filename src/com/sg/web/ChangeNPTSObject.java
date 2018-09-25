package com.sg.web;

import com.sg.web.base.ViewObjectBase;

public class ChangeNPTSObject extends ViewObjectBase{

	String fontColor;
	String labelOrder;
	float  fontSize;
	String name,number,level;
	public String getFontColor() {
		return fontColor;
	}
	public void setFontColor(String fontColor) {
		this.fontColor = fontColor;
	}
	public String getLabelOrder() {
		return labelOrder;
	}
	public void setLabelOrder(String labelOrder) {
		this.labelOrder = labelOrder;
	}
	public float getFontSize() {
		return fontSize;
	}
	public void setFontSize(float fontSize) {
		this.fontSize = fontSize;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
	
}
