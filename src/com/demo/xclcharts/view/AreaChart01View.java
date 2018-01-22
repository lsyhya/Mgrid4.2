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

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.xclcharts.chart.AreaChart;
import org.xclcharts.chart.AreaData;
import org.xclcharts.common.IFormatterDoubleCallBack;
import org.xclcharts.common.IFormatterTextCallBack;
import org.xclcharts.event.click.PointPosition;
import org.xclcharts.renderer.XChart;
import org.xclcharts.renderer.XEnum;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Toast;


/**
 * @ClassName AreaChart01View
 * @Description  闈㈢Н鍥句緥瀛�
 * @author XiongChuanLiang<br/>(xcl_168@aliyun.com)
 */

public class AreaChart01View extends TouchView {
	
	private String TAG = "AreaChart01View";
	
	private AreaChart chart = new AreaChart();	
	//鏍囩闆嗗悎
	private LinkedList<String> mLabels = new LinkedList<String>();
	//鏁版嵁闆嗗悎
	private LinkedList<AreaData> mDataset = new LinkedList<AreaData>();
	

	public AreaChart01View(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		initView();
	}	 
	
	public AreaChart01View(Context context, AttributeSet attrs){   
        super(context, attrs);   
        initView();
	 }
	 
	 public AreaChart01View(Context context, AttributeSet attrs, int defStyle) {
			super(context, attrs, defStyle);
			initView();
	 }
	 
	 private void initView()
	 {
		chartLabels();
		chartDataSet();		
		chartRender();
	 }
	
	@Override  
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {  
        super.onSizeChanged(w, h, oldw, oldh);  
       //鍥炬墍鍗犺寖鍥村ぇ灏�
        chart.setChartRange(w,h);
    }  
			 
	private void chartRender()
	{
		try{												 
				//璁剧疆缁樺浘鍖洪粯璁ょ缉杩沺x鍊�,鐣欑疆绌洪棿鏄剧ずAxis,Axistitle....		
				int [] ltrb = getBarLnDefaultSpadding();
				chart.setPadding(ltrb[0], ltrb[1], ltrb[2], ltrb[3]);
											
				//杞存暟鎹簮						
				//鏍囩杞�
				chart.setCategories(mLabels);
				//鏁版嵁杞�
				chart.setDataSource(mDataset);
							
				//鏁版嵁杞存渶澶у��
				chart.getDataAxis().setAxisMax(100);
				//鏁版嵁杞村埢搴﹂棿闅�
				chart.getDataAxis().setAxisSteps(10);
				
				//缃戞牸
				chart.getPlotGrid().showHorizontalLines();
				chart.getPlotGrid().showVerticalLines();	
				//鎶婇《杞村拰鍙宠酱闅愯棌
				chart.setTopAxisVisible(false);
				chart.setRightAxisVisible(false);
				//鎶婅酱绾垮拰鍒诲害绾跨粰闅愯棌璧锋潵
				chart.getDataAxis().setAxisLineVisible(false);
				chart.getDataAxis().setTickMarksVisible(false);			
				chart.getCategoryAxis().setAxisLineVisible(false);
				chart.getCategoryAxis().setTickMarksVisible(false);				
				
				//鏍囬
				chart.setTitle("鍖哄煙鍥�(Area Chart)");
				chart.addSubtitle("(XCL-Charts Demo)");	
				//鍥句緥
				chart.getAxisTitle().setLowerAxisTitle("(骞翠唤)");
				
				//閫忔槑搴�
				chart.setAreaAlpha(100);
				//鏄剧ず閿��
				chart.getPlotLegend().showLegend();
				
				//婵�娲荤偣鍑荤洃鍚�
				chart.ActiveListenItemClick();
				//涓轰簡璁╄Е鍙戞洿鐏垫晱锛屽彲浠ユ墿澶�5px鐨勭偣鍑荤洃鍚寖鍥�
				chart.extPointClickRange(5);
				
				//瀹氫箟鏁版嵁杞存爣绛炬樉绀烘牸寮�
				chart.getDataAxis().setLabelFormatter(new IFormatterTextCallBack(){
		
					@Override
					public String textFormatter(String value) {
						// TODO Auto-generated method stub		
						Double tmp = Double.parseDouble(value);
						DecimalFormat df=new DecimalFormat("#0");
						String label = df.format(tmp).toString();				
						return (label);
					}
					
				});
				
				//璁惧畾浜ゅ弶鐐规爣绛炬樉绀烘牸寮�
				chart.setItemLabelFormatter(new IFormatterDoubleCallBack() {
					@Override
					public String doubleFormatter(Double value) {
						// TODO Auto-generated method stub
						DecimalFormat df=new DecimalFormat("#0");					 
						String label = df.format(value).toString();
						return label;
					}});
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.e(TAG, e.toString());
		}
	}
	
	private void chartDataSet()
	{
		//灏嗘爣绛句笌瀵瑰簲鐨勬暟鎹泦鍒嗗埆缁戝畾
		//鏍囩瀵瑰簲鐨勬暟鎹泦
		List<Double> dataSeries1= new LinkedList<Double>();	
		dataSeries1.add((double)55); 
		dataSeries1.add((double)60); 
		dataSeries1.add((double)71); 
		dataSeries1.add((double)40);
		dataSeries1.add((double)35);
		
		List<Double> dataSeries2 = new LinkedList<Double>();			
		dataSeries2.add((double)10); 
		dataSeries2.add((double)22); 
		dataSeries2.add((double)30); 	
		dataSeries2.add((double)30); 
		dataSeries2.add((double)15); 
		
		//璁剧疆姣忔潯绾垮悇鑷殑鏄剧ず灞炴��
		//key,鏁版嵁闆�,绾块鑹�,鍖哄煙棰滆壊
		AreaData line1 = new AreaData("灏忕唺",dataSeries1,Color.BLUE,Color.YELLOW);
		//涓嶆樉绀虹偣
		line1.setDotStyle(XEnum.DotStyle.HIDE);
		
		AreaData line2 = new AreaData("灏忓皬鐔�",dataSeries2,
											(int)Color.rgb(79, 200, 100),Color.GREEN);
		//璁剧疆绾夸笂姣忕偣瀵瑰簲鏍囩鐨勯鑹�
		line2.getDotLabelPaint().setColor(Color.RED);
		//璁剧疆鐐规爣绛�
		line2.setLabelVisible(true);
		line2.getDotLabelPaint().setTextAlign(Align.LEFT);	
		
		mDataset.add(line1);
		mDataset.add(line2);	
	}
	
	private void chartLabels()
	{		
		mLabels.add("2010");
		mLabels.add("2011");
		mLabels.add("2012");
		mLabels.add("2013");
		mLabels.add("2014");
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
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub		
		super.onTouchEvent(event);		
		if(event.getAction() == MotionEvent.ACTION_UP) 
		{			
			triggerClick(event.getX(),event.getY());
		}
		return true;
	}
	
	
	//瑙﹀彂鐩戝惉
	private void triggerClick(float x,float y)
	{		
		PointPosition record = chart.getPositionRecord(x,y);			
		if( null == record) return;

		AreaData lData = mDataset.get(record.getDataID());
		Double lValue = lData.getLinePoint().get(record.getDataChildID());	
		
		Toast.makeText(this.getContext(), 
				record.getPointInfo() +
				" Key:"+lData.getLineKey() +
				" Label:"+lData.getLabel() +								
				" Current Value:"+Double.toString(lValue), 
				Toast.LENGTH_SHORT).show();			
	}
	

	
}
