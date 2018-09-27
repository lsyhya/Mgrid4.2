package com.sg.web.utils;

public class ViewObjectColorUtil {

	public static String getColor(String color) {
		if (color == null || !color.startsWith("#") || color.length() != 9 || color.substring(1, 3).equals("00")) {
			return "transparent";
		}

		return "#" + color.substring(3, color.length());
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

}
