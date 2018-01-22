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
 * @license http://www.apache.org/licenses/  Apache v2 License
 * @version 1.0
 */
package com.demo.xclcharts.view;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.xclcharts.chart.PieChart3D;
import org.xclcharts.chart.PieData;
import org.xclcharts.event.click.ArcPosition;
import org.xclcharts.renderer.XChart;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Toast;

/**
 * @ClassName Pie3DChart01View
 * @Description  3D楗煎浘鐨勪緥瀛�
 * 闂姩鐢绘晥鏋滅殑浜哄お澶氫簡锛屽叾瀹炲浘琛ㄥ簱灏卞簲褰撳彧绠＄粯鍥撅紝鍔ㄧ敾鏁堟灉灏变氦缁橵iew鎴朣urfaceView鍚�,
 * 	鐪嬬湅鎴戝紕鐨勬晥鏋滄湁澶氶潛. ~_~ 
 *  渚濊繖涓緥瀛愬彂鎸ュ彂鎸ワ紝鍙互璁╁浘浠庡睆骞曞悇涓柟鍚戝嚭鐜�.
 * @author XiongChuanLiang<br/>(xcl_168@aliyun.com)
 */
public class PieChart3D01View extends TouchView implements Runnable{

	private String TAG = "Pie3DChart01View";
	private PieChart3D chart = new PieChart3D();
	
	private LinkedList<PieData> chartData = new LinkedList<PieData>();	
	
	public PieChart3D01View(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		initView();
	}
	
	public PieChart3D01View(Context context, AttributeSet attrs){   
        super(context, attrs);   
        initView();
	 }
	 
	 public PieChart3D01View(Context context, AttributeSet attrs, int defStyle) {
			super(context, attrs, defStyle);
			initView();
	 }
	 
	 private void initView()
	 {
		 	chartDataSet();		
			chartRender();
			new Thread(this).start();
	 }
	 
	
	@Override  
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {  
        super.onSizeChanged(w, h, oldw, oldh);  
       //鍥炬墍鍗犺寖鍥村ぇ灏�
        chart.setChartRange(w,h);
    }  		
	
	
	private void chartRender()
	{
		try {						
			//璁剧疆缁樺浘鍖洪粯璁ょ缉杩沺x鍊�
			int [] ltrb = getPieDefaultSpadding();
			chart.setPadding(ltrb[0], ltrb[1], ltrb[2], ltrb[3]);	
			
			//璁惧畾鏁版嵁婧�
			//chart.setDataSource(chartData);		
			
			//鏍囬
			//chart.setTitle("涓汉涓撲笟鎶�鑳藉垎甯�");
			//chart.addSubtitle("(XCL-Charts Demo)");
			//chart.getPlotTitle().setTitlePosition(XEnum.Position.LOWER);
			
			//涓嶆樉绀簁ey
			chart.getPlotLegend().hideLegend();
			//鏍囩鏂囨湰鏄剧ず涓虹櫧鑹�
			chart.getLabelPaint().setColor(Color.WHITE);
			
			
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log.e(TAG, e.toString());
		}
	}
	private void chartDataSet()
	{
		//璁剧疆鍥捐〃鏁版嵁婧�			
		//PieData(鏍囩锛岀櫨鍒嗘瘮锛屽湪楗煎浘涓搴旂殑棰滆壊)
		
		chartData.add(new PieData("PHP(15%)",15,
								(int)Color.rgb(1, 170, 255)));
		chartData.add(new PieData("Other",10,
								(int)Color.rgb(148, 42, 133),false));	
		chartData.add(new PieData("Oracle",40,(int)Color.rgb(241, 62, 1)));
		chartData.add(new PieData("Java",15,(int)Color.rgb(242, 167, 69)));	
		
		//灏嗘姣斾緥鍧楃獊鍑烘樉绀�
		chartData.add(new PieData("C++(20%)",20,
								(int)Color.rgb(164, 233, 0),false));
	
		
	}
	
	@Override
    public void render(Canvas canvas) {
        try{
            chart.render(canvas);
        } catch (Exception e){
        	Log.e(TAG, e.toString());
        }
    }

	@Override
	public List<XChart> bindChart() {
		// TODO Auto-generated method stub		
		List<XChart> lst = new ArrayList<XChart>();
		lst.add(chart);		
		return lst;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {          
         	chartAnimation();         	
         }
         catch(Exception e) {
             Thread.currentThread().interrupt();
         }  
	}
	
	
	public void setData(List<PieData> data)
	{
		 chart.setDataSource(data);
		 postInvalidate(); 
	}
	
	
	private void chartAnimation()
	{
		  try {         
			 //璁剧疆鏁版嵁婧�
			  chart.setDataSource(chartData);
			  
			  for(int i=10;i>0;i--)
			  {
				  Thread.sleep(100);
				 // chart.setChartRange(0.0f, 0.0f,getScreenWidth()/i,getScreenHeight()/i);				  
				  chart.setChartRange(0.0f, 0.0f,this.getWidth()/i,this.getHeight()/i);				  
				  
				  if(1 == i)
				  {
					    //鏈�鏈樉绀烘爣棰�
					//	chart.setTitle("涓汉涓撲笟鎶�鑳藉垎甯�");
					//	chart.addSubtitle("(XCL-Charts Demo)");
					//	chart.setTitleVerticalAlign(XEnum.VerticalAlign.BOTTOM);
						chart.setChartRange(0.0f, 0.0f,getWidth(),getHeight());
						
						//婵�娲荤偣鍑荤洃鍚�
					//	chart.ActiveListenItemClick();	
						chart.DeactiveListenItemClick();
				  }
				  postInvalidate(); 
			  }
          }
          catch(Exception e) {
              Thread.currentThread().interrupt();
          }            
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub		
		//super.onTouchEvent(event);		
		if(event.getAction() == MotionEvent.ACTION_UP) 
		{						
			triggerClick(event.getX(),event.getY());
		}
		return true;
	}
	

	//瑙﹀彂鐩戝惉
	private void triggerClick(float x,float y)
	{		
		
		ArcPosition record = chart.getPositionRecord(x,y);			
		if( null == record) return;
		
		PieData pData = chartData.get(record.getDataID());											
		Toast.makeText(this.getContext(),								
				" key:" +  pData.getKey() +
				" Label:" + pData.getLabel() ,
				Toast.LENGTH_SHORT).show();	
		
		/*
		 int k	= chart.getDataSource().size();
		 
		 Toast.makeText(this.getContext(),								
					" k:" +  Integer.toString(k) ,
					Toast.LENGTH_SHORT).show();	
		 */
	}
	
}
