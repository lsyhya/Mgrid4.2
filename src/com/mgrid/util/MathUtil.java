package com.mgrid.util;

public class MathUtil {

	public static MathUtil mathUtil=new MathUtil();
	public static MathUtil getMathUtil()
	{
		return  mathUtil;
	}
		
	public  boolean isNumber(String arg)
	{		
		return arg.matches("-*\\d+\\.?\\d*");
	} 
	
}
