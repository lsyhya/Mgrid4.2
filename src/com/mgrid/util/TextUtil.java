package com.mgrid.util;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Paint.FontMetricsInt;

public class TextUtil {

	
	public static Rect rect = new Rect();

	public static int getTextWigth(Paint paint, String text) {
		paint.getTextBounds(text, 0, text.length(), rect);

		return rect.width();
	}

	public static int getTextHeight(Paint paint, String text) {

		paint.getTextBounds(text, 0, text.length(), rect);

		return rect.height();
	}
	
	
	//实现文字居中
	public void draw(Canvas canvas,String testString,Paint paint,float rc_x,float rc_y,float rc_x_end,float rc_y_end) {
		Rect targetRect = new Rect((int)rc_x, (int)rc_y,(int)rc_x_end, (int)rc_y_end);
		FontMetricsInt fontMetrics = paint.getFontMetricsInt();	   
		int baseline = (targetRect.bottom + targetRect.top - fontMetrics.bottom - fontMetrics.top) / 2;
		// 下面这行是实现水平居中，drawText对应改为传入targetRect.centerX()
		paint.setTextAlign(Paint.Align.CENTER);		
		canvas.drawText(testString, targetRect.centerX(), baseline, paint);
	}

}
