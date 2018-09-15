package com.sg.web;

import com.sg.web.base.ViewObjectBase;

public class RectangleObject extends ViewObjectBase{

	    String bgColor;//±³¾°ÑÕÉ«
	    int  borderWidth;
	    String borderColor;
	    
	    public String getBgColor() {
			return bgColor;
		}
		public void setBgColor(String bgColor) {
			this.bgColor = bgColor;
		}
		public int getBorderWidth() {
			return borderWidth;
		}
		public void setBorderWidth(int borderWidth) {
			this.borderWidth = borderWidth;
		}
		public String getBorderColor() {
			return borderColor;
		}
		public void setBorderColor(String borderColor) {
			this.borderColor = borderColor;
		}
		
}
