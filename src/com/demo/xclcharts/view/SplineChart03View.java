package com.demo.xclcharts.view;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import org.xclcharts.chart.SplineChart;
import org.xclcharts.chart.SplineData;
import org.xclcharts.common.IFormatterTextCallBack;
import org.xclcharts.event.click.PointPosition;
import org.xclcharts.renderer.XChart;
import org.xclcharts.renderer.XEnum;
import org.xclcharts.renderer.plot.PlotGridRender;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Toast;

public class SplineChart03View  extends TouchView {
	

private String TAG = "SplineChart03View";
private SplineChart chart = new SplineChart();
//鍒嗙被杞存爣绛鹃泦鍚�
private LinkedList<String> labels = new LinkedList<String>();
private LinkedList<SplineData> chartData = new LinkedList<SplineData>();

public SplineChart03View(Context context) {
	super(context);
	// TODO Auto-generated constructor stub
	initView();
}

public SplineChart03View(Context context, AttributeSet attrs){   
    super(context, attrs);   
    initView();
 }
 
 public SplineChart03View(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView();
 }
 
 private void initView()
 {
		chartLabels();
		chartDataSet();	
		chartRender();
 }
 
 public SplineChart getChart()
 {
	 return chart;
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
		//设置绘图区默认缩进px值,留置空间显示Axis,Axistitle....		
		int [] ltrb = getBarLnDefaultSpadding();
		chart.setPadding(40,30,10, 20);	
		
		//显示边框
		//chart.showRoundBorder();
		
		//数据源	
		chart.setCategories(labels);
		chart.setDataSource(chartData);					
		//坐标系
		//数据轴最大值
		chart.getDataAxis().setAxisMax(3);
		//chart.getDataAxis().setAxisMin(0);
		//数据轴刻度间隔
		chart.getDataAxis().setAxisSteps(1);
		
		
		
		//标签轴最大值
		chart.setCategoryAxisMax(24);	
		//标签轴最小值
		chart.setCategoryAxisMin(0);	
		
		
		
		
		//设置图的背景色
	//	chart.setApplyBackgroundColor(true);
	//	chart.setBackgroundColor( (int)Color.rgb(212, 194, 129) );
	//	chart.getBorder().setBorderLineColor((int)Color.rgb(179, 147, 197));
				
		//x轴刻度文字画笔
		chart.getCategoryAxis().getTickLabelPaint().setColor(Color.WHITE);
		chart.getCategoryAxis().getTickLabelPaint().setTextSize(10);
		//y轴刻度文字画笔
		chart.getDataAxis().getTickLabelPaint().setColor(Color.WHITE);
		chart.getDataAxis().getTickLabelPaint().setTextSize(10);
		//调轴线与网络线风格
		chart.getCategoryAxis().setTickMarksVisible(false);
		//chart.getDataAxis().setAxisLineVisible(true);
		chart.getDataAxis().setTickMarksVisible(false);		
		chart.getPlotGrid().showHorizontalLines();
		chart.setTopAxisVisible(false);
		chart.setRightAxisVisible(false);				
		
		chart.getPlotGrid().getHorizontalLinePaint().setColor(Color.parseColor("#62666D"));
		chart.getPlotGrid().getHorizontalLinePaint().setStrokeWidth(1);
		
		
		
		chart.getCategoryAxis().getAxisPaint().setColor(Color.BLUE);
		chart.getCategoryAxis().getAxisPaint().setStrokeWidth(1);
		
		chart.getDataAxis().getAxisPaint().setColor(Color.BLUE);
		chart.getDataAxis().getAxisPaint().setStrokeWidth(1);
		
			
		//定义交叉点标签显示格式,特别备注,因曲线图的特殊性，所以返回格式为:  x值,y值
		//请自行分析定制
		chart.setDotLabelFormatter(new IFormatterTextCallBack(){

			@Override
			public String textFormatter(String value) {
				// TODO Auto-generated method stub						
				String label = "["+value+"]";				
				return (label);
			}
			
		});
		//标题
	//	chart.setTitle("Spline Chart");
	//	chart.addSubtitle("(XCL-Charts Demo)");
		
		//激活点击监听
//		chart.ActiveListenItemClick();
		//为了让触发更灵敏，可以扩大5px的点击监听范围 
//		chart.extPointClickRange(5);
		
		chart.DeactiveListenItemClick();
		
		
		//显示平滑曲线
		chart.setCrurveLineStyle(XEnum.CrurveLineStyle.BEZIERCURVE);
		
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		Log.e(TAG, e.toString());
	}
}
private void chartDataSet()
{
	//线1的数据集
//		LinkedHashMap<Double,Double> linePoint1 = new LinkedHashMap<Double,Double>();
//		linePoint1.put(5d, 8d);
//		
//		linePoint1.put(12d, 12d);
//		linePoint1.put(25d, 15d);
//		linePoint1.put(30d, 30d);
//		linePoint1.put(45d, 25d);
//		
//		linePoint1.put(55d, 33d);
//		linePoint1.put(62d, 45d);
//		
//		
//		linePoint1.put(75d, 43d);
//		linePoint1.put(82d, 55d);
//		linePoint1.put(90d, 60d);
//		linePoint1.put(96d, 68d);
//		
//		SplineData dataSeries1 = new SplineData("线一",linePoint1,
//				(int)Color.rgb(54, 141, 238) );	
//		//把线弄细点
//		dataSeries1.getLinePaint().setStrokeWidth(2);
//		dataSeries1.setLabelVisible(true);	
//		dataSeries1.getDotLabelPaint().setTextAlign(Align.LEFT);
		
		//线2的数据集
		LinkedHashMap<Double,Double> linePoint2 = new LinkedHashMap<Double,Double>();
		linePoint2.put(0d, 3d);
		linePoint2.put(6d, 1d);

		linePoint2.put(12d, 2.5d);
		linePoint2.put(18d, 2d);		
		
		linePoint2.put(24d, 0.5d);	
		

		
		SplineData dataSeries2 = new SplineData("mPUE",linePoint2,
				(int)Color.parseColor("#FF76A1EC") );
		
		
		
			
		dataSeries2.setDotStyle(XEnum.DotStyle.HIDE);				
		dataSeries2.getDotLabelPaint().setColor(Color.RED);
		dataSeries2.getLinePaint().setStrokeWidth(2);	
			
		//设定数据源		
	//	chartData.add(dataSeries1);				
		chartData.add(dataSeries2);	
}

private void chartLabels()
{
	labels.add("0"); 
	//labels.add("");
	labels.add("6");
	//labels.add("");
	labels.add("12");
	//labels.add("");
	labels.add("18");
	//labels.add("");
	labels.add("24"); 
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
	//	triggerClick(event.getX(),event.getY());	
	}
	return true;
}


//瑙﹀彂鐩戝惉
private void triggerClick(float x,float y)
{
	PointPosition record = chart.getPositionRecord(x,y);			
	if( null == record) return;

	SplineData lData = chartData.get(record.getDataID());
	LinkedHashMap<Double,Double> linePoint =  lData.getLineDataSet();	
	int pos = record.getDataChildID();
	int i = 0;
	Iterator it = linePoint.entrySet().iterator();
	while(it.hasNext())
	{
		Entry  entry=(Entry)it.next();	
		
		if(pos == i)
		{							 						
		     Double xValue =(Double) entry.getKey();
		     Double yValue =(Double) entry.getValue();	
		     
		     Toast.makeText(this.getContext(), 
						record.getPointInfo() +
						" Key:"+lData.getLineKey() +								
						" Current Value(key,value):"+
						Double.toString(xValue)+","+Double.toString(yValue), 
						Toast.LENGTH_SHORT).show();
		     break;
		}
        i++;
	}//end while			
}

}
