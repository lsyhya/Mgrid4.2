package com.sg.web;

import com.sg.web.base.ViewObjectBase;

public class RectangleObject extends ViewObjectBase{

	    String bgColor;//±³¾°ÑÕÉ«
	    int  borderWidth;
	    String borderColor;
	    int alpha,red,green,blue;
	    
	    public int getAlpha() {
			return alpha;
		}
		public void setAlpha(int alpha) {
			this.alpha = alpha;
		}
		public int getRed() {
			return red;
		}
		public void setRed(int red) {
			this.red = red;
		}
		public int getGreen() {
			return green;
		}
		public void setGreen(int green) {
			this.green = green;
		}
		public int getBlue() {
			return blue;
		}
		public void setBlue(int blue) {
			this.blue = blue;
		}
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
