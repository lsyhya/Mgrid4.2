
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

import com.mgrid.main.MainWindow;
import com.sg.common.IObject;
import com.sg.common.MutiThreadShareObject;
import com.sg.common.UtExpressionParser.stExpression;
import data_model.save_curve_signal;

/**动态信号曲线**/
//author：fjw0312
//made time：2015.8.14
//该控件采集SignalCurve数据模型的数据 采集点为10个点
public class SignalCurve extends View implements IObject {
	
	public SignalCurve(Context context) {
		super(context);
		
		m_rBBox = new Rect(); //新建一个矩形类给予空间		
		m_oPaint = new Paint();   //赋予画笔类空间
		m_oPaint.setTextSize(m_fFontSize); //设置画笔线条大小
		m_oPaint.setColor(m_nFontColor);   //设置画笔颜色
		m_oPaint.setAntiAlias(true); // 设置画笔的锯齿效果
		m_oPaint.setStyle(Paint.Style.STROKE); //设置画笔风格
	}

	@Override //画布绘制
	protected void onDraw(Canvas canvas) {
		if (m_rRenderWindow == null)
			return;
		if (m_rRenderWindow.isLayoutVisible(getBBox()) == false)
			return;
		
		Log.w("SignalCurve","into onDraw!");
		//画出标题
		m_oPaint.setColor(Color.WHITE);   //设置画笔颜色
		m_oPaint.setTextSize(25); //设置画笔线条大小
		canvas.drawText("动态历史曲线",m_nHeight/2, 25, m_oPaint); // 画y轴刻度
		
		m_oPaint.setColor(m_nLineColor);   //设置画笔颜色
		m_oPaint.setTextSize(20); //设置画笔线条大小
		//画出坐标轴
		canvas.drawLine(40, m_nHeight-40, m_nWidth-40, m_nHeight-40, m_oPaint); // 画线x轴
		canvas.drawLine(40, m_nHeight-40, 40, 40, m_oPaint);    // 画线y轴
		//画出刻度点
		int x_unit = (m_nWidth-80)/num;  //距边沿40pin
		int y_unit = (m_nHeight-90)/10; //轴尾部空10
		for(int i=0;i<10;i++){
			canvas.drawLine(40, y_unit*i+50, 45, y_unit*i+50, m_oPaint); // 画y轴刻度线
		}
		for(int i=0;i<num;i++){
			canvas.drawLine(x_unit*i+40, m_nHeight-40, x_unit*i+40, m_nHeight-45, m_oPaint); // 画x轴刻度线
		}
		//画刻度标签
		m_oPaint.setTextSize(12); // 设置字体大小
		for(int i=0;i<10;i++){
			if("".equals(y_markLine[i])||(".00".equals(y_markLine[i])) ) y_markLine[i] = "0.00";
			canvas.drawText(y_markLine[9-i],2, y_unit*(i+1)+50, m_oPaint); // 画y轴刻度
		}
		for(int i=0;i<num;i++){
			if("".equals(x_markLineTOw[i][0]) ) x_markLineTOw[i][0] = " ";
			if("".equals(x_markLineTOw[i][1]) ) x_markLineTOw[i][1] = "0";
			canvas.drawText(x_markLineTOw[i][0], x_unit*i+20, m_nHeight-10, m_oPaint); // 画x轴刻度
			canvas.drawText(x_markLineTOw[i][1], x_unit*i+20, m_nHeight-22, m_oPaint); // 画x轴刻度
		}
		//画出数值点
		m_oPaint.setColor(m_nFontColor);   //设置画笔颜色
		m_oPaint.setStyle(Style.FILL); 	//设置画笔为实心
		for(int i=0;i<num;i++){
			float value = sig_value[i];
			canvas.drawCircle(x_unit*i+40, m_nHeight-40-value,4, m_oPaint); // 画出数值点
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
		}
	}
	
	@Override//将该控件添加进入主页面
	public void addToRenderWindow(MainWindow rWin) {
		m_rRenderWindow = rWin;
		rWin.addView(this);
	}
	
	@Override//将该控件从主页面移除
	public void removeFromRenderWindow(MainWindow rWin) {  
		m_rRenderWindow = null;
		rWin.removeView(this);
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
		//获取共享线程池 历史曲线数据
		save_curve_signal sig_class = new save_curve_signal();
		if(m_rRenderWindow.m_oShareObject.m_mapHisPoint == null) return false;
		sig_class =	m_rRenderWindow.m_oShareObject.m_mapHisPoint.get(this.getUniqueID());
		if(sig_class==null) return false;
		String x_buf[] = sig_class.curve_mark_xline; //获取x轴刻度
		float y_buf[] = sig_class.curve_mark_yline;  //获取y轴刻度   float型
		float sig_buf[] = sig_class.curve_value_buf;  //获取曲线点数值
		float max_value = sig_class.max_markLineValue; //获得最大刻度
		
		//处理转换刻度
		for(int i=0;i<10;i++){
			DecimalFormat decimalFloat = new DecimalFormat(".00"); //float小数点精度处理
			y_markLine[i] = decimalFloat.format(y_buf[i]);
			x_markLine[i] = x_buf[i];
			if((x_markLine[i]!="0") && (x_markLine[i]!=null)){
				x_markLineTOw[i][0] = x_markLine[i].substring(0, 10);//截取字符串
				x_markLineTOw[i][1] = x_markLine[i].substring(11);
			}else{
				x_markLineTOw[i][0] = " ";//截取字符串
				x_markLineTOw[i][1]= "   0";
			}
			//处理信号值的对应y轴坐标点
			sig_value[i] = (m_nHeight - 90)/max_value * sig_buf[i];
	
		}
		//获取成功
	//	for(int f=0;f<10;f++){
	//		Log.e("in SignalCurve打印 recive save_curve_value",sig_class.curve_time_buf[f]);
	//	}
		
		Log.w("SignalCurve","into updateValue!");
        
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
	public int getCollectTime(){
		return collectTime;
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
	int m_nFontColor = 0xFFFF0000;  //控件画笔 (曲线点颜色) 字体颜色 0xFFFF0000为红色
	int m_nLineColor = 0xFFFF0000;  //控件画笔  (坐标轴颜色)字体颜色 0xFFFF0000为红色
	int BackgroundColor = 0xFF000000; //控件底板颜色    0xFF000000为黑色
	
//控件必要变量
	Rect m_rBBox = null;   //新建底板方框 类变量
	Paint m_oPaint = null; //新建画笔 类变量
	MainWindow m_rRenderWindow = null;  //新建主页 类变量
	public boolean m_bneedupdate = true; //新建需要控件数据更新标志的变量
//控件特别的必要变量
	public int num = 10; //控件采集点数
	public int collectTime = 20; //曲线点采集周期  20s
	
//特别控件需要参数
	float sig_value[] = {0,0,0,0,0,0,0,0,0,0};  //信号值得屏幕像素长度
	String[][] x_markLineTOw = {{" ","   0"},{" ","   0"},{" ","   0"},{" ","   0"},{" ","   0"},
			{" ","   0"},{" ","   0"},{" ","   0"},{" ","   0"},{" ","   0"}};
	String x_markLine[] = {"0","0","0","0","0","0","0","0","0","0"};
	String y_markLine[] = {"0.00","0.00","0.00","0.00","0.00","0.00","0.00","0.00","0.00","0.00"};
	
}
