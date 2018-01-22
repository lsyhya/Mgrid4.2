package com.mgrid.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtils {

	public static String getNowTime()
	{
		Date curr=new Date();
		SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateString=dateFormat.format(curr);		
		return dateString;
	}
	
	public static String getYear()
	{
			
		return  getNowTime().substring(0,4);
	}
	
	public static String getMonth()
	{
		return  getNowTime().substring(5,7);
	} 
	
	public static String getDay()
	{
		return  getNowTime().substring(8,10);
	}
	public static String getHour()
	{
		return  getNowTime().substring(11,13);
	}
	public static String getMintus()
	{
		return  getNowTime().substring(14,16);
	}
	public static String getScroce()
	{
		return  getNowTime().substring(17,19);
	}
}
