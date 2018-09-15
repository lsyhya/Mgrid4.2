package com.sg.web;

import com.sg.web.base.ViewObjectBase;

public class TableObject extends ViewObjectBase{
	
	int rowNUm;
	int colNum;
	public int getRowNUm() {
		return rowNUm;
	}
	public void setRowNUm(int rowNUm) {
		this.rowNUm = rowNUm;
	}
	public int getColNum() {
		return colNum;
	}
	public void setColNum(int colNum) {
		this.colNum = colNum;
	}
	
	
	public float getFirstRow() {
		return firstRow;
	}
	public void setFirstRow(float firstRow) {
		this.firstRow = firstRow;
	}
	public float getFirstCol() {
		return firstCol;
	}
	public void setFirstCol(float firstCol) {
		this.firstCol = firstCol;
	}
	public String getLineColor() {
		return lineColor;
	}
	public void setLineColor(String lineColor) {
		this.lineColor = lineColor;
	}
	float firstRow;
	float firstCol;
	String lineColor;

}
