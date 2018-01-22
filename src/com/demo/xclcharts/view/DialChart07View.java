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
 * @Description Android鍥捐〃鍩虹被搴撴紨绀�
 * @author XiongChuanLiang<br/>(xcl_168@aliyun.com)
 * @license http://www.apache.org/licenses/  Apache v2 License
 * @version 1.3
 */
package com.demo.xclcharts.view;

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
 * @Description Android图表基类库演示
 * @author XiongChuanLiang<br/>(xcl_168@aliyun.com)
 * @license http://www.apache.org/licenses/  Apache v2 License
 * @version 1.3
 */

import java.util.ArrayList;
import java.util.List;

import org.xclcharts.chart.DialChart;
import org.xclcharts.common.MathHelper;
import org.xclcharts.renderer.XEnum;
import org.xclcharts.renderer.axis.RoundAxis;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;

public class DialChart07View extends GraphicalView {

	private String TAG = "DialChart07View";	
	
	public String PointColor="#F1F1F1";//指针颜色
	public String BcColor="#FFF0EEF9";//背景颜色
	public String textColor="#FF000000";//字体颜色
	public String leftColor="#898989";//左半圆颜色
	public String rightColor="#4F4F4F";//右半圆颜色
	public List<String> textList=new ArrayList<String>();
	
	
	
	private DialChart chart180 = new DialChart();	
	private float mPercentage = 0.9f;
	
	public DialChart07View(Context context) {
		super(context);

		initView();
	}
	
	public DialChart07View(Context context, AttributeSet attrs){   
        super(context, attrs);   
        initView();
	 }
	 
	 public DialChart07View(Context context, AttributeSet attrs, int defStyle) {
			super(context, attrs, defStyle);
			initView();
	 }
	 
	 
	 private void initView()
	 {

		textList.add("0");
		textList.add("15");
		textList.add("30");
		chartRender180();
	 }
	 
	 
	 public DialChart getChart()
	 {
		 return chart180;
	 }
	 
	 @Override  
	    protected void onSizeChanged(int w, int h, int oldw, int oldh) {  
	        super.onSizeChanged(w, h, oldw, oldh);  
	
	        chart180.setChartRange(w ,h*1.5f );
	  
	    }  		
						

		
				
		public void chartRender180()
		{
			try {	
				
				chart180.setPadding(0, 0, 0, 0);
				
				chart180.setTotalAngle(160f); 
				chart180.setStartAngle(190f);
						
				//设置当前百分比
				chart180.getPointer().setPercentage(mPercentage);
				
				//设置指针长度
				chart180.getPointer().setPointerStyle(XEnum.PointerStyle.TRIANGLE);
				chart180.getPointer().getPointerPaint().setColor(Color.parseColor(PointColor));
				chart180.getPointer().getBaseCirclePaint().setColor(Color.parseColor(PointColor));
				chart180.getPointer().setBaseRadius(10);
				chart180.getPointer().setLength(0.65f,0f);	
				
				List<Float> ringPercentage = new ArrayList<Float>();			
				float rper = MathHelper.getInstance().div(1, 2); //相当于40%	//270, 4
				ringPercentage.add(rper);
				ringPercentage.add(rper);
	
				
				List<Integer> rcolor  = new ArrayList<Integer>();			
				rcolor.add((int)Color.parseColor(leftColor));
				rcolor.add((int)Color.parseColor(rightColor));					
				chart180.addStrokeRingAxis(0.75f,0.5f, ringPercentage, rcolor);
			//	chart180.getPlotAxis().get(0).getFillAxisPaint().setColor((int)Color.parseColor("#FF2B2B2B") );		#00000000	
				chart180.getPlotAxis().get(0).getFillAxisPaint().setColor((int)Color.parseColor(BcColor) );
				List<String> tickLabels = new ArrayList<String>();
			    for (int i = 0; i < textList.size(); i++) {
			    	tickLabels.add(textList.get(i));					
				}			
				chart180.addInnerTicksAxis(1f, tickLabels);
				List<RoundAxis> mRoundAxis=chart180.getPlotAxis();
				for (int i = 0; i < mRoundAxis.size(); i++) {
					if(mRoundAxis.get(i).getAxisType()==XEnum.RoundAxisType.TICKAXIS)
					{
						mRoundAxis.get(i).setAxisLineVisible(false);
						mRoundAxis.get(i).setTickMarksVisible(false);
						mRoundAxis.get(i).getTickLabelPaint().setColor(Color.parseColor(textColor));						
					}				
				}
				
//				Paint paintTB = new Paint();
//				paintTB.setColor(Color.WHITE);
//				paintTB.setTextAlign(Align.CENTER);
//				paintTB.setTextSize(22);	
//				paintTB.setAntiAlias(true);	
//				chart180.addAttributeInfo(XEnum.Location.BOTTOM, "180度仪表盘", 0.5f, paintTB);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				Log.e(TAG, e.toString());
			}
			
		}
		

		

		
		public void refreshAll()
		{											
			chart180.clearAll();		
			chartRender180();
		}
		
		public void setCurrent(float percentage)
		{								
			mPercentage =  percentage;
			chart180.getPointer().setPercentage(mPercentage);
		}


		@Override
		public void render(Canvas canvas) {
			// TODO Auto-generated method stub
			 try{
				// 	chart.render(canvas);
		        //    chart90.render(canvas);
		            chart180.render(canvas);
		            		           
		        } catch (Exception e){
		        	Log.e(TAG, e.toString());
		        }
		}

}

