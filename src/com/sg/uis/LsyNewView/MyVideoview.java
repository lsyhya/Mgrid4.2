package com.sg.uis.LsyNewView;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.VideoView;

public class MyVideoview extends VideoView{

	public MyVideoview(Context context) {
		super(context);
		
	}

	public MyVideoview(Context context, AttributeSet attrs) {
		super(context, attrs);
	
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
 
		int ws=MeasureSpec.getSize(widthMeasureSpec);
		int wm=MeasureSpec.getMode(widthMeasureSpec);
		int hs=MeasureSpec.getSize(heightMeasureSpec);
		int hm=MeasureSpec.getMode(heightMeasureSpec);
		if(wm==MeasureSpec.EXACTLY&&hm==MeasureSpec.EXACTLY)
		{
			setMeasuredDimension(ws,hs);
		}else
		{
			super.onMeasure(widthMeasureSpec,heightMeasureSpec);
		}
		
	}

}
