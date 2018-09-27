package com.mgrid.util;

import java.text.SimpleDateFormat;

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
	
	public boolean isValidDate(String str) {
		boolean convertSuccess = true;

		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		try {

			format.setLenient(false);
			format.parse(str);
		} catch (Exception e) {
			e.printStackTrace();

			convertSuccess = false;
		}
		return convertSuccess;
	}
	
}
