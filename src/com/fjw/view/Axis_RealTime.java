package com.fjw.view;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;
import android.view.ViewGroup;


//自定义view 坐标轴  用于控件的应用元素
public class Axis_RealTime extends View{

	public Axis_RealTime(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		//画笔设置
		paint = new Paint();
		paint.setTextSize(10); //设置画笔线条大小
		paint.setColor(LineColor); //设置画笔颜色
		paint.setAntiAlias(false); //设置非锯齿风格
		paint.setStyle(Paint.Style.STROKE); //设置画笔风格
	}
	//Fileds
 public int x_num,y_num;  //坐标的刻度数
 public	float x_start,y_start; //坐标轴原点的像素坐标
 public	float x_end,y_end;     //坐标轴线结束点的像素坐标
 public	float x_lenth,y_lenth;  //坐标轴线的像素长度
 public	float x_unit,y_unit;    //坐标轴线刻度单位的像素坐标
 public	float x_per_unit,y_per_unit;     //轴的像素/数值 比例单位
 public	float x_pad=40,y_pad=30;    //控件的边界
 public ArrayList<String> x_markValue = new ArrayList<String>();
 public ArrayList<String> y_markValue = new ArrayList<String>();
 public int LineColor = 0xFFFFFF00;
 public int BackgroundColor = 0xFFFFFFFF;
 public int flag=0;
 
 private Paint paint;  //定义画笔
	
	//重写 onDraw 方法 绘制canvas画布
	public void onDraw(Canvas canvas){
		super.onDraw(canvas);
		
		//设置底板颜色
		canvas.drawColor(BackgroundColor);
		
		//设置画笔
		paint.setTextSize(10);
		paint.setColor(LineColor);
		
		//画出坐标轴
		canvas.drawLine(x_start, y_start, x_end, y_start, paint); //x轴
		canvas.drawLine(x_start, y_start, x_start, y_end, paint); //y轴
		//画出刻度
		for(int i=0;i<x_num+1;i++){  //x轴刻度
			float x = x_start + x_unit*i;
			canvas.drawLine(x, y_start, x, y_start-6, paint); 
		}
		for(int i=0;i<y_num+1;i++){  //y轴刻度 
			float y = y_start - y_unit*i;
			canvas.drawLine(x_start, y, x_start+6, y, paint); 
		}
		//设置画笔
		paint.setTextSize(10);
		paint.setColor(LineColor);
		if(x_markValue==null) return;
		if(y_markValue==null) return;   
		if(x_markValue.size()<=0) return; 
		//画出标签
		for(int i=0;i<x_num;i++){  //x轴标签
			String s=x_markValue.get(i);
			float m=s.length()/2;	//	得到x轴标签的字符个数 除以2 
			if(m==0) m=0.7f;
			float x = x_start + x_unit*(i+1)-m*6;  //尽量使字符分布早x刻度正下方
			if(flag!=2){
			   if(Integer.parseInt(s)!=0)
			   canvas.drawText(s, x, y_start+10, paint); 
			}
			else
			{
				canvas.drawText(s, x, y_start+10, paint); 
			}
		}
		for(int i=0;i<y_num+1;i++){  //y轴标签
			float y = y_start - y_unit*i;
			canvas.drawText(y_markValue.get(i), x_start-35, y, paint); 
		}
		
		
	}
	//调用layout 方法 绘制自身layout
	public void doLayout(boolean bool,int l,int t, int r,int b){
		this.layout(l, t, r, b);
	}
	//调用invalidate()方法 请求重绘view树->view.onDraw()自动调用
	public void doInvalidate(){
		this.invalidate();
	} 
	//调用addView()方法 将该视图添加到父视图中
	public boolean doAddToView(ViewGroup view){
		view.addView(this);
		return true;
	}

	
	
	public boolean upDataValueFlag(float width, float height,int xNum,int yNum,float x_maxVlaue,float y_maxVlaue,int flag,HashMap<String, Float> nameValueList){
		x_start = x_pad;
		y_start = height-y_pad;
		x_end = width;
		y_end = 0;
		x_num = xNum;
		y_num = yNum;
		x_lenth = x_end - x_start;
		y_lenth = y_start - y_end;
		x_unit = x_lenth/(x_num+1);
		y_unit = y_lenth/(y_num+1);
		x_per_unit = (x_lenth-x_unit)/x_maxVlaue;
		y_per_unit = (y_lenth-y_unit)/y_maxVlaue;
		this.flag=flag;
		dealMarkVlaue(x_maxVlaue,y_maxVlaue,nameValueList);
		return true;
	}
	
	
	
	
	public void dealMarkVlaue(float x_maxValue,float y_maxValue,HashMap<String, Float> nameValueList){
		if(nameValueList.size()<=0)
		return;
		x_markValue.clear();
		Iterator<Map.Entry<String,Float>> it=nameValueList.entrySet().iterator();
		while(it.hasNext())
		{
			Map.Entry<String,Float> entry=it.next();
			String name=entry.getKey();
			if(name.equals(""))
				name="0";
			x_markValue.add(name);
		}
		
		float yUnit = y_maxValue/y_num;
		y_markValue.clear();
		for(int i=0;i<y_num+1;i++){
    		DecimalFormat decimalFloat = new DecimalFormat("0"); //float小数点精度处理
    		String strValue= decimalFloat.format(yUnit*i);
			y_markValue.add(strValue);			
		}
	}
	

}
