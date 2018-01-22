package com.mgrid.main;

import android.content.Context;
import android.graphics.Canvas;
import android.view.View;
import android.view.ViewGroup;

public class ContainerView extends ViewGroup {

	public ContainerView(Context context) { 
		super(context);
	}

	// 解决 VIEWGROUP 中子VIEW布局混乱问题
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {

	    if(mCurrentView != null)
	    {
	    	mCurrentView.layout(l, t, r, b);
	    }
	}
	
	
	
	protected void dispatchDraw(Canvas canvas)    
	{
	    super.dispatchDraw(canvas);
	    
	    if(mCurrentView != null)
	    {
	        drawChild(canvas, mCurrentView, getDrawingTime());
	    }
	}
	
	

	// 解决多重 ViewGroup 嵌套事件响应问题。 TODO: 有无其他影响尚待观察  -- CharlesChen
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{
		
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		
	    if(mCurrentView != null)
	    {
	    	mCurrentView.measure(widthMeasureSpec, heightMeasureSpec);
	    }
		
		// 改善性能，不处理非显示页面。

	}

	public View mCurrentView;
}
