package com.sg.web.utils;

import android.util.Log;

public class ViewObjectColorUtil {

	public static String getColor(String color) {

		try {

			if (color == null || !color.startsWith("#") || color.length() != 9 || color.substring(1, 3).equals("00")) {

				if (color == null) {
					return "transparent";
				}

				switch (color) {
				case "Red":

					return "#FF0000";

				case "Greed":

					return "#00FF00";
					
					
				case "Green":

					return "#00FF00";

				default:

					return "transparent";

				}	

			}

			return "#" + color.substring(3, color.length());

		} catch (Exception e) {

			e.printStackTrace();
			Log.e("TAG", color);
			return "transparent";

		}

	}

	public static int getArgb(int color) {

		int alpha = (color & 0xff000000) >>> 24;

		return alpha;
	}

	public static int getRed(int color) {

		int red = (color & 0x00ff0000) >> 16;

		return red;
	}

	public static int getGreen(int color) {

		int green = (color & 0x0000ff00) >> 8;

		return green;
	}

	public static int getBlue(int color) {

		int blue = (color & 0x000000ff);

		return blue;
	}

	public static String getStringColor(int color)
	{
		return  String.format("#%06X", (0xFFFFFFFF & color));

	}

}
