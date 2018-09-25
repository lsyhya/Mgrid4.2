package com.sg.web.utils;

public class ViewObjectColorUtil {
	
	public static String getColor(String color)
	{
		if(color==null||!color.startsWith("#")||color.length()!=9||color.substring(1, 3).equals("00"))
		{
			return "transparent";
		}
		
		return "#"+color.substring(3, color.length());
	}

}
