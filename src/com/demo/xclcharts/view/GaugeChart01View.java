/**
 * Copyright 2014  XCL-Charts
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 	
 * @Project XCL-Charts 
 * @Description Android鍥捐〃鍩虹被搴�
 * @author XiongChuanLiang<br/>(xcl_168@aliyun.com)
 * @Copyright Copyright (c) 2014 XCL-Charts (www.xclcharts.com)
 * @license http://www.apache.org/licenses/  Apache v2 License
 * @version 1.0
 */
package com.demo.xclcharts.view;

import java.util.ArrayList;
import java.util.List;

import org.xclcharts.chart.GaugeChart;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Pair;

/**
 * @ClassName GaugeChart01View
 * @Description  浠〃鐩樹緥瀛�
 * @author XiongChuanLiang<br/>(xcl_168@aliyun.com)
 */
public class GaugeChart01View  extends GraphicalView {

	private String TAG = "GaugeChart01View";
	private GaugeChart chart = new GaugeChart();
	
	public List<String> mLabels = new ArrayList<String>();
	public List<Pair> mPartitionSet = new ArrayList<Pair>();	
	public List<String> mAngels = new ArrayList<String>();
	private float mAngle = 0.0f;

	public String tColor="#B2D4E9";
	public String pColor="#B2D4E9";
	public String lColor="#B2D4E9";	
	public int Tick=5;
	
	public List<String> colorData=new ArrayList<String>();
	
	public GaugeChart01View(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		initView();	
	}
		
	public GaugeChart01View(Context context, AttributeSet attrs){   
        super(context, attrs);   
        initView();
	 }
	 
	 public GaugeChart01View(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView();
	 }
	 
	 private void initView()
	 {
		 chartColors();
		chartLabels();
		chartDataSet();	
		chartRender();
	 }
	 public void initViews()
	 {
		
		chartDataSet();	
		chartRender();
	 }
	 
	 public GaugeChart getChart()
	 {
		 return chart;
	 }
	 
	 @Override  
     protected void onSizeChanged(int w, int h, int oldw, int oldh) {  
        super.onSizeChanged(w, h, oldw, oldh);          
        chart.setChartRange(w ,h);   
        chart.setPadding(w/20,h/10,w/20, 0);
     }  
	 
	
	//浠巗eekbar浼犲叆鐨勫��
	public void setAngle(float currentAngle)
	{
		mAngle = currentAngle;
	}	
		
	public void chartRender()
	{
		try {								 
						
			//璁剧疆鏍囬
			//chart.setTitle("鍒诲害鐩� ");
								
			//鍒诲害姝ラ暱
			chart.setTickSteps(Tick);
			
			chart.getTickPaint().setColor(Color.parseColor(tColor));	//	刻度颜色  
			chart.getPointerLinePaint().setColor(Color.parseColor(pColor));	//	指针颜色  
			chart.getPinterCirclePaint().setColor(Color.parseColor(pColor));//	指针圆心颜色  
			chart.getPinterCirclePaint().setStrokeWidth(5);
			chart.getDountPaint().setColor(Color.parseColor(tColor));	//外围圆颜色

            chart.getLabelPaint().setColor(Color.parseColor(lColor));//标签颜色
            chart.getLabelPaint().setTextSize(15);
			chart.setCategories(mLabels);					
			//鍒嗗尯 
			chart.setPartition(mPartitionSet); 
			 
			//璁剧疆褰撳墠鎸囧悜瑙掑害(0-180).
			//chart.setCurrentAngle(90f); 
			chart.setCurrentAngle(mAngle);
			//缁樺埗杈规 
	//		chart.showRoundBorder();
		
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log.e(TAG, e.toString());
		}
		
	}
	
	//鍒嗗尯[瑙掑害(0-mStartAngle)锛岄鑹瞉		
	private void chartDataSet()
	{
		
		
		if(mAngels.size()>0&&colorData.size()==mAngels.size())
		{			
			for (int i = 0; i < colorData.size(); i++) {
				mPartitionSet.add(new Pair<Float,Integer>((float)Float.parseFloat(mAngels.get(i)), Color.parseColor(colorData.get(i))));
			}
		}
		else
		{
			int Angle = 180/colorData.size();
			for (int i = 0; i < colorData.size(); i++) {
				mPartitionSet.add(new Pair<Float,Integer>((float)Angle, Color.parseColor(colorData.get(i))));
			}
		}
		
	}
	
	
	private void chartColors()
	{
		colorData.add("#66C2A5");
		colorData.add("#FFC656");
		colorData.add("#FF7369");
	}
	
	private void chartLabels()
	{
		//鏍囩		
		mLabels.add("0");
		mLabels.add("10");
		mLabels.add("20");
		mLabels.add("30");
		mLabels.add("40");
		mLabels.add("50");
	}

	
	@Override
    public void render(Canvas canvas) {
        try{
        	
            chart.render(canvas);
        } catch (Exception e){
        	Log.e(TAG, e.toString());
        }
    }
}
