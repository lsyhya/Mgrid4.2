package com.sg.web;

import com.sg.web.base.ViewObjectBase;

public class TriggerSetterObject extends ViewObjectBase{
	
	String content,fontColor;
	float  fontSize;
	boolean click;
	
	public boolean isClick() {
		return click;
	}
	public void setClick(boolean click) {
		this.click = click;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getFontColor() {
		return fontColor;
	}
	public void setFontColor(String fontColor) {
		this.fontColor = fontColor;
	}
	public float getFontSize() {
		return fontSize;
	}
	public void setFontSize(float fontSize) {
		this.fontSize = fontSize;
	}

}
