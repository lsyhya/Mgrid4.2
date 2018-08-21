package com.mgrid.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimeUtils {

	public static String getNowTime()
	{
		Date curr=new Date();
		SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateString=dateFormat.format(curr);		
		return dateString;
	}
	
	
	public static String getYM()
	{
		return  getNowTime().substring(0,7);
	}
	
	public static String getYMD()
	{
		return  getNowTime().substring(0,10);
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
	
	public static int getWeekOfDate(Date dt) {
	       // String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
	        Calendar cal = Calendar.getInstance();
	        cal.setTime(dt);
	        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
	        if (w ==0)
	            w = 7;
	        return w;
	    }
}
