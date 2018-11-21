package com.sg.web;

import com.sg.web.base.ViewObjectBase;

public class LableObject extends ViewObjectBase{

	public String TextColor;//字体颜色
	public String text;//文本内容 
	public float  textSize;
	public String duiqiWay;
	
	public String getDuiqiWay() {
		return duiqiWay;
	}


	public void setDuiqiWay(String duiqiWay) {
		this.duiqiWay = duiqiWay;
	}


	public float getTextSize() {
		return textSize;
	}


	public void setTextSize(float textSize) {
		this.textSize = textSize;
	}


	public String getText() {
		return text;
	}


	public void setText(String text) {
		this.text = text;
	}


	public LableObject(String typeId, float left, float top, float wight, float heght, int ZIndex,String TextColor) {
		super(typeId, left, top, wight, heght, ZIndex);
		this.TextColor=TextColor;
	}

	
	public LableObject()
	{
		
	}

	public String getTextColor() {
		return TextColor;
	}


	public void setTextColor(String textColor) {
		TextColor = textColor;
	}
	
	

}
