package com.lsy.Service.TilmePlush;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimePlushUtils {

	public static String getNowTime(long time)
	{
		//Date curr=new Date();
		SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateString=dateFormat.format(time);		
		return dateString;
	}
	
	
	
	public static String getYear(long time)
	{
			
		return  getNowTime(time).substring(0,4);
	}
	
	public static String getMonth(long time)
	{
		return  getNowTime( time).substring(5,7);
	} 
	
	public static String getDay(long time)
	{
		return  getNowTime( time).substring(8,10);
	}
	public static String getHour(long time)
	{
		return  getNowTime( time).substring(11,13);
	}
	public static String getMintus(long time)
	{
		return  getNowTime(time).substring(14,16);
	}
	public static String getScroce(long time)
	{
		return  getNowTime(time).substring(17,19);
	}
}
