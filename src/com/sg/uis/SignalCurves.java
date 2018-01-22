
package com.sg.uis;

import java.io.IOException;

import com.sg.common.SgRealTimeData;

import java.io.InputStream;
import java.text.DecimalFormat;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Paint.Style;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.mgrid.main.MainWindow;
import com.sg.common.IObject;
import com.sg.common.MutiThreadShareObject;
import com.sg.common.UtExpressionParser.stExpression;
import data_model.save_curve_signal;
import data_model.save_multipoint_signal;

/**多采集点模式可选的动态信号曲线**/
//author：fjw0312
//made time：2015.8.18
public class SignalCurves extends View implements IObject {
	
	public SignalCurves(Context context) {
		super(context);
		
		m_rBBox = new Rect(); //新建一个矩形类给予空间		
		m_oPaint = new Paint();   //赋予画笔类空间
		m_oPaint.setTextSize(m_fFontSize); //设置画笔线条大小
		m_oPaint.setColor(m_nFontColor);   //设置画笔颜色
		m_oPaint.setAntiAlias(true); // 设置画笔的锯齿效果
		m_oPaint.setStyle(Paint.Style.STROKE); //设置画笔风格
		
		//定义选择按纽组 按钮数3个
		ridobuttons = new RadioButton[3];
		ridobuttons[0] = new RadioButton(context);
		ridobuttons[0].setText("1 h");
		ridobuttons[0].setChecked(true);
		ridobuttons[1] = new RadioButton(context);
		ridobuttons[1].setText("24 h");
		ridobuttons[2] = new RadioButton(context);
		ridobuttons[2].setText("1 mon");
		for(int i=0;i<3;i++){
			ridobuttons[i].setTextColor(Color.BLACK);
			ridobuttons[i].setOnClickListener(l);	
		}

	}
	
	private OnClickListener l = new OnClickListener() {				
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			String strText = (String) ((RadioButton) arg0).getText();
			for(int i=0;i<3;i++)
				ridobuttons[i].setChecked(false);
			if("1 h".equals(strText)){
				mode = 1;				
			}else if("24 h".equals(strText)){
				mode = 2;
			}else if("1 mon".equals(strText)){
				mode = 3;
			}
			ridobuttons[mode-1].setChecked(true);
			setMode(mode);
			updateValue();  //更新数据
			updateWidget(); //该view 请求重新draw()，只会draw该view
		}
	};

	@Override //画布绘制
	protected void onDraw(Canvas canvas) {
		if (m_rRenderWindow == null)
			return;
		if (m_rRenderWindow.isLayoutVisible(getBBox()) == false)
			return;
		
//		Log.e("SignalCurves","into onDraw!"+String.valueOf(mode));
		

		axis_pad = 40;
		axis_x_start = axis_pad;
		axis_x_end = m_nWidth-axis_pad;
		axis_y_start = m_nHeight-axis_pad;
		axis_y_end = axis_pad+20;
		axis_x_lenth = axis_x_end - axis_x_start;
		axis_y_lenth= axis_y_start - axis_y_end;
		axis_x_unit = axis_x_lenth/num; 
	    axis_y_unit = axis_y_lenth/10;
	    m_oPaint.setColor(Color.RED);   //设置画笔线条颜色
//	    canvas.drawCircle(axis_x_start, axis_y_start,5, m_oPaint); //画出4个基准点
//	    canvas.drawCircle(axis_x_start, axis_y_end,5, m_oPaint); //画出4个基准点
//	    canvas.drawCircle(axis_x_end, axis_y_start,5, m_oPaint); //画出4个基准点
	    
		m_oPaint.setColor(m_nLineColor);   //设置画笔线条颜色
//		m_oPaint.setColor(Color.BLACK);   //设置画笔线条颜色
		//画出坐标轴
		canvas.drawLine(axis_x_start, axis_y_start, axis_x_end+20, axis_y_start, m_oPaint); // 画线x轴
		canvas.drawLine(axis_x_start, axis_y_start, axis_x_start, axis_y_end-20, m_oPaint); // 画线y轴
		//画出刻度点
		for(int i=0;i<11;i++){
			canvas.drawLine(axis_x_start, axis_y_start-axis_y_unit*i, axis_x_start+5, axis_y_start-axis_y_unit*i, m_oPaint); // 画y轴刻度线
		}
		for(int i=0;i<num+1;i++){
			canvas.drawLine(axis_x_start+axis_x_unit*i, axis_y_start, axis_x_start+axis_x_unit*i, axis_y_start-5, m_oPaint); // 画x轴刻度线
		}
		
//		Log.e("SignalCurves->onDraw>>axis_x_lenth:", Float.toString(axis_x_lenth));
//		Log.e("SignalCurves->onDraw>>axis_y_lenth:", Float.toString(axis_y_lenth));
		//画刻度标签
		m_oPaint.setTextSize(12); // 设置字体大小
		for(int i=0;i<11;i++){                      //y轴标签
			if(axis_y_lenth<300){  //处理区域过小 标签间隔
				if(i%2==1)  continue; 
			}
			if( ("".equals(y_markLine[i]))||(y_markLine[i]==null) ) y_markLine[i] = "0";
			canvas.drawText(y_markLine[i],18, axis_y_start-axis_y_unit*i, m_oPaint); // 画y轴刻度
		}
		for(int i=0;i<num;i++){					
			if(axis_x_lenth<200){ 
				if(i%5!=0)  continue; 
			}
			if(axis_x_lenth<400){
				if(i%4!=0)  continue; 
			}
			if(axis_x_lenth<600){
				if(i%2!=0)  continue;  
			}
			if("".equals(x_markLineTOw[i][0])||(x_markLineTOw[i][0]==null) ) x_markLineTOw[i][0] = " ";
			if("".equals(x_markLineTOw[i][1])||(x_markLineTOw[i][1]==null) ) x_markLineTOw[i][1] = "0";
			canvas.drawText(x_markLineTOw[i][0], axis_x_start+axis_x_unit*i, axis_y_start+10, m_oPaint); // 画x轴刻度
			canvas.drawText(x_markLineTOw[i][1], axis_x_start+axis_x_unit*i, axis_y_start+22, m_oPaint); // 画x轴刻度
		}
		//画出数值点
		m_oPaint.setColor(m_nFontColor);   //设置画笔颜色
		m_oPaint.setStyle(Style.FILL); 	//设置画笔为实心
		float pre_x = 0;
		float pre_y = 0;
		for(int i=0;i<num;i++){
			float value = sig_value[i];
			float node_x = axis_x_start+axis_x_unit*i;
			float node_y = axis_y_start-value;
			canvas.drawCircle(node_x, node_y,3, m_oPaint); // 画出数值点
			//连线
			if(i!=0){
				canvas.drawLine(pre_x,pre_y,node_x,node_y,m_oPaint);
			}
			pre_x = node_x;
			pre_y = node_y;
		}
		
		super.onDraw(canvas);
	}

	
	@Override //绘制底板
	public void doLayout(boolean bool, int l, int t, int r, int b) {
		if (m_rRenderWindow == null)
			return;
		
		int nX = l + (int) (((float)m_nPosX / (float)MainWindow.FORM_WIDTH) * (r-l));
		int nY = t + (int) (((float)m_nPosY / (float)MainWindow.FORM_HEIGHT) * (b-t));
		int nWidth = (int) (((float)(m_nWidth) / (float)MainWindow.FORM_WIDTH) * (r-l));
		int nHeight = (int) (((float)(m_nHeight) / (float)MainWindow.FORM_HEIGHT) * (b-t));

		m_rBBox.left = nX;
		m_rBBox.top = nY;
		m_rBBox.right = nX+nWidth;
		m_rBBox.bottom = nY+nHeight;
		if (m_rRenderWindow.isLayoutVisible(m_rBBox)) {
			layout(nX, nY, nX+nWidth, nY+nHeight);
			//绘制RadioButton的底板空间
//			for(int i = 0; i < ridobuttons.length; i++)
//				ridobuttons[i].layout(nX + (i+1) * nWidth / 4, nY, nX + (i+1) * nWidth / 4 + nWidth / 4, nY+20);
		}

	}
	 
	@Override//将该控件添加进入主页面
	public void addToRenderWindow(MainWindow rWin) {
		m_rRenderWindow = rWin;
		rWin.addView(this);
//		for(int i = 0; i < ridobuttons.length; i++)
//			rWin.addView(ridobuttons[i]);
	}
	
	@Override//将该控件从主页面移除
	public void removeFromRenderWindow(MainWindow rWin) {  
		m_rRenderWindow = null;
		rWin.removeView(this);
//		for(int i = 0; i < ridobuttons.length; i++)
//			rWin.removeView(ridobuttons[i]);
	} 
	
	//解析控件参数
	public void parseProperties(String strName, String strValue, String strResFolder) {
		if ("ZIndex".equals(strName)) {
       	 	m_nZIndex = Integer.parseInt(strValue);
       	    if (MainWindow.MAXZINDEX < m_nZIndex) MainWindow.MAXZINDEX = m_nZIndex;
        }
        else if ("Location".equals(strName)) {
       	 	String[] arrStr = strValue.split(",");
       	 	m_nPosX = Integer.parseInt(arrStr[0]);
       	 	m_nPosY = Integer.parseInt(arrStr[1]);
       	 	
        }
        else if ("Size".equals(strName)) {
       	 	String[] arrSize = strValue.split(",");
       	 	m_nWidth = Integer.parseInt(arrSize[0]);
       	 	m_nHeight = Integer.parseInt(arrSize[1]);

        }
        else if ("Alpha".equals(strName)) {
       	 	m_fAlpha = Float.parseFloat(strValue);
        }
        else if ("Expression".equals(strName)) {
        	m_strExpression = strValue;
        }
        else if ("FontSize".equals(strName)) {
        	m_fFontSize= Float.parseFloat(strValue);
        	this.setFontSize(m_fFontSize);
        }
        else if ("FontColor".equals(strName)) {
        	m_nFontColor= Color.parseColor(strValue);
        	this.setFontColor(m_nFontColor);
        }
        else if ("LineColor".equals(strName)) {
        	m_nLineColor= Color.parseColor(strValue);
        	this.setLineColor(m_nLineColor);
        }
        else if ("BackgroundColor".equals(strName)) {
        	BackgroundColor = Color.parseColor(strValue);
        	this.setBackgroundColor(BackgroundColor);
        }
        else if ("mode".equals(strName)) {
        	mode = Integer.parseInt(strValue);
        	this.setMode(mode);
        }
	}

	//获得该控件
	public View getView() {
		return this;
	}
	//获得底板方框
	public Rect getBBox() {
		return m_rBBox;
	}	
	@Override //完成初始化后处理函数
	public void initFinished()
	{
	}
	
	//设置ui 控件id
	public void setUniqueID(String strID) {
		m_strID = strID;
	}
	//获取ui 控件id
	public String getUniqueID() {
		return m_strID;
	}
	//设置ui 控件类型
	public void setType(String strType) {
		m_strType = strType;
	}
	//获取ui 控件类型
	public String getType() {
		return m_strType;
	}
	//获取控件所在图层
	public int getZIndex(){
		return m_nZIndex;
	}
	//获取控件表达式
	public String getBindingExpression() {
		return m_strExpression;
	}	
	//更新ui控件属性（一些参数）变化
	public void updateWidget() {
		this.invalidate(); //该view 请求重新draw()，只会draw该view
	}
	@Override //是否需要界面刷新标志函数
    public boolean needupdate()
    {
	    return m_bneedupdate;
    }	
	@Override //是否需要界面刷新标志函数
    public void needupdate(boolean bNeedUpdate)
    {
	    m_bneedupdate = bNeedUpdate;
    }	
	@Override   //数据参数更新函数 ui属性变化函数 
	public boolean updateValue()
	{
		m_bneedupdate = false;
		//获取共享线程池 历史曲线数据  数据获取正常
		save_multipoint_signal sig_class = new save_multipoint_signal();
		if(m_rRenderWindow.m_oShareObject.m_mapMultiPoint == null) return false;
		sig_class =	m_rRenderWindow.m_oShareObject.m_mapMultiPoint.get(this.getUniqueID());
		if(sig_class==null) return false;
		String x_buf[] = new String[num]; //定义x轴刻度
		float y_buf[] = new float[10];    //定义y轴刻度   float型
		float sig_buf[] = new float[num];  //定义曲线点数值

		//将x轴刻度  y轴刻度  曲线点数值 指针放入函数获取数值 并返回刻度最大值
		float max_value = sig_class.get_curve(mode, x_buf, y_buf, sig_buf);
		float maxValue_f = ((int)max_value/10+1)*10;   //将最大容限值 设为10的整数倍
		axis_y_per = axis_y_lenth/maxValue_f;
	//数据赋值 ok	
	
	//	for(int i=0;i<num;i++){
	//		Log.e("into SignalCurves-updateValue数值：", Float.toString(y_buf[i]));
	//	}
		//处理转换刻度 y轴10个刻度点
		for(int i=0;i<11;i++){
			DecimalFormat decimalFloat = new DecimalFormat("0"); //float小数点精度处理
			y_markLine[i] = decimalFloat.format(maxValue_f/10*i);
		}
		
		for(int i=0;i<30;i++){
			sig_value[i] = 0;
		}
		//处理转换刻度 x轴刻度点  应对1h的采集时间格式进行处理太长了
		for(int i=0;i<num;i++){
			if(x_buf[i]==null) x_buf[i]=" ";
			x_markLine[i] = x_buf[i];
			String a = x_markLine[i];
			if( (a.length()<8)&&(a.length()>1) ){
				x_markLineTOw[i][0] = " ";
				x_markLineTOw[i][1]= x_markLine[i];//截取字符串
			}else{
				x_markLineTOw[i][0] = " ";//截取字符串
				x_markLineTOw[i][1] = "0";
			}
			//处理信号值的对应y轴坐标点
			sig_value[i] = axis_y_per * sig_buf[i];
	
		}
		//获取成功
	//	for(int f=0;f<10;f++){
	//		Log.e("in SignalCurve打印 recive save_curve_value",sig_class.curve_time_buf[f]);
	//	}
		
		Log.w("SignalCurves","into updateValue!");
        
        return true;  //有控件参数变化并要变化界面view，不管是text函数图形 都要返回true;
	}

//可能会调用的变化控件参数
	public void setFontSize(float FontSize){
		m_fFontSize = FontSize;
	}
	public void setFontColor(int FontColor){
		m_nFontColor = FontColor;
	}
	public void setLineColor(int LineColor){
		m_nLineColor = LineColor;
	}
	public void setMode(int mode){
		switch(mode){
			case 1: num = 30; break;
			case 2: num = 24; break;
			case 3: num = 30; break;
			default:num = 30; break;
		}
	}
	
// params:
//xml 控件的参数
	String m_strID = "";     //控件id
	String m_strType = "";    //控件类型
    int m_nZIndex = 20;     //控件id标号
	int m_nPosX = 300;     //控件位置location x y坐标值
	int m_nPosY = 397;
	int m_nWidth = 150;    //控件大小 长和宽 w h 
	int m_nHeight = 137;
	float m_fAlpha = 1.0f;   //控件色相深度
	String m_strExpression = null;//控件表达式
	float m_fFontSize = 20.0f;	   //控件画笔 字体大小
	int m_nFontColor = 0xFFFF0000;  //控件画笔  字体颜色 0xFFFF0000为红色
	int m_nLineColor = 0xFFFF0000;  //控件画笔  (坐标轴颜色)字体颜色 0xFFFF0000为红色
	int BackgroundColor = 0xFF000000; //控件底板颜色    0xFF000000为黑色
	
	RadioButton[] ridobuttons;
	
//控件必要变量
	Rect m_rBBox = null;   //新建底板方框 类变量
	Paint m_oPaint = null; //新建画笔 类变量
	MainWindow m_rRenderWindow = null;  //新建主页 类变量
	public boolean m_bneedupdate = true; //新建需要控件数据更新标志的变量
	public int mode = 1;
	public int num = 30;
//坐标轴相关参数
	float axis_pad = 40;
	float axis_x_start;
	float axis_x_end;
	float axis_y_start;
	float axis_y_end;
	float axis_x_lenth;
	float axis_y_lenth;
	float axis_x_unit;
	float axis_y_unit;
	float axis_y_per;
	
//特别控件需要参数
	float sig_value[] = new float[30];  //信号值得屏幕像素长度
	String[][] x_markLineTOw = new String[30][2];
	String x_markLine[] = new String[30];
	String y_markLine[] = new String[11];
	
}
