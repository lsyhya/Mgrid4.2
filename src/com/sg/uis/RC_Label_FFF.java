package com.sg.uis;

import com.fjw.view.Axis;
import com.mgrid.main.MainWindow;
import com.sg.common.CFGTLS;
import com.sg.common.IObject;
import com.sg.common.UtExpressionParser;
import com.sg.common.UtExpressionParser.stIfElseExpression;
import com.sg.common.UtExpressionParser.stIntervalExpression;

import android.R;
import android.animation.ArgbEvaluator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.sg.common.SgRealTimeData;
import comm_service.local_rc;
/** 数据累加量柱状图 */  //fjw made 2016 4 9 应用年/月用电量  月/日用电量
/** made by fjw */
public class RC_Label_FFF extends TextView implements IObject {

	public RC_Label_FFF(Context context) {
		super(context); 
		this.setClickable(false);
		this.setBackgroundColor(0x00000000);
		m_rBBox = new Rect();
		my_Axis = new Axis(context);
		
		paint = new Paint();
		
	}
	
	@Override
	public void doLayout(boolean bool, int l, int t, int r, int b) {
		if (m_rRenderWindow == null)
			return;
		int nX = l + (int) (((float)m_nPosX / (float)MainWindow.FORM_WIDTH) * (r-l));
		int nY = t + (int) (((float)m_nPosY / (float)MainWindow.FORM_HEIGHT) * (b-t));
		int nWidth = (int) (((float)m_nWidth / (float)MainWindow.FORM_WIDTH) * (r-l));
		int nHeight = (int) (((float)m_nHeight / (float)MainWindow.FORM_HEIGHT) * (b-t));

		m_rBBox.left = nX;
		m_rBBox.top = nY;
		m_rBBox.right = nX+nWidth;
		m_rBBox.bottom = nY+nHeight;
		if (m_rRenderWindow.isLayoutVisible(m_rBBox)) {
			layout(nX, nY, nX+nWidth, nY+nHeight);
			
		}
		
		my_Axis.doLayout(true, nX, nY, nX+nWidth, nY+nHeight);
		my_Axis.upDataValue(nWidth, nHeight, 10, 10, 100, data*20);
		my_Axis.invalidate();
		
	}
	
	public void onDraw(Canvas canvas) {
		if (m_rRenderWindow == null)
			return;
		if (m_rRenderWindow.isLayoutVisible(getBBox()) == false)
			return;
	
		paint.setColor(Color.RED);
		paint.setTextSize(30);
		paint.setStyle(Paint.Style.FILL);
	
		//绘制text
		canvas.drawText(m_strContent, 100, 60, paint);
		//绘制柱状
		for(int i=1; i<10;i++){
			float rc_x = my_Axis.x_start+my_Axis.x_unit*i;
			float rc_y = my_Axis.y_start-my_Axis.y_per_unit*data*i;
			float rc_x_end = rc_x+my_Axis.x_unit*(float)0.8;
			float rc_y_end = my_Axis.y_start;
			canvas.drawRect(rc_x, rc_y, rc_x_end, rc_y_end, paint);  //画矩形 
		}


		super.onDraw(canvas);
	}
	
	public View getView() {
		return this;
	}
	
	public int getZIndex()
	{
		return m_nZIndex;
	}
	
	@Override
	public void addToRenderWindow(MainWindow rWin) {
		m_rRenderWindow = rWin;
		rWin.addView(my_Axis); //添加坐标轴控件
		rWin.addView(this);
		
	
	}

	@Override
	public void removeFromRenderWindow(MainWindow rWin) {
		rWin.removeView(this);
		rWin.removeView(my_Axis);  //去除坐标轴控件
	}

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
        else if ("RotateAngle".equals(strName)) {
        	m_fRotateAngle = Float.parseFloat(strValue);
        }
        else if ("Content".equals(strName)) {
        	m_strContent = strValue;
//        	this.setText(m_strContent);
        }
        else if ("FontFamily".equals(strName))
        	m_strFontFamily = strValue;
        else if ("FontSize".equals(strName)) {
        	float fWinScale = (float)MainWindow.SCREEN_WIDTH / (float)MainWindow.FORM_WIDTH;
        	m_fFontSize = Float.parseFloat(strValue)*fWinScale;
    		this.setTextSize(m_fFontSize);
        }
        else if ("IsBold".equals(strName))
       	 	m_bIsBold = Boolean.parseBoolean(strValue);
        else if ("FontColor".equals(strName)) {
       	 	m_cFontColor = Color.parseColor(strValue);
       	 	this.setTextColor(m_cFontColor);     	 	
        }
        else if ("HorizontalContentAlignment".equals(strName))
       	 	m_strHorizontalContentAlignment = strValue;
        else if ("VerticalContentAlignment".equals(strName))
       	 	m_strVerticalContentAlignment = strValue;
        else if ("Expression".equals(strName)) 
       	 	m_strExpression = strValue;
        else if("ColorExpression".equals(strName))
        	m_strColorExpression = strValue;  //字体颜色变化表达式
        else if("D_mon".equals(strName))
        	D_mon = Integer.parseInt(strValue);  //累积量显示模式
	}

	@Override
	public void initFinished()
	{
		int nFlag = Gravity.NO_GRAVITY;
		if ("Left".equals(m_strHorizontalContentAlignment))
			nFlag |= Gravity.LEFT;
		else if ("Right".equals(m_strHorizontalContentAlignment))
			nFlag |= Gravity.RIGHT;
		else if ("Center".equals(m_strHorizontalContentAlignment))
			nFlag |= Gravity.CENTER_HORIZONTAL;
		
		if ("Top".equals(m_strVerticalContentAlignment))
			nFlag |= Gravity.TOP;
		else if ("Bottom".equals(m_strVerticalContentAlignment))
		{
			nFlag |= Gravity.BOTTOM;
			double padSize = CFGTLS.getPadHeight(m_nHeight, MainWindow.FORM_HEIGHT, getTextSize());
			setPadding(0, (int) padSize, 0, 0);
		}
		else if ("Center".equals(m_strVerticalContentAlignment))
		{
			nFlag |= Gravity.CENTER_VERTICAL;
			double padSize = CFGTLS.getPadHeight(m_nHeight, MainWindow.FORM_HEIGHT, getTextSize())/2;
			setPadding(0, (int) padSize, 0, (int) padSize);
		}
		
		setGravity(nFlag);
	}

	public String getBindingExpression() {
		return m_strExpression;
	}
	
	public void updateWidget() {	

		this.invalidate();
	}
	
	@Override
	public boolean updateValue()
	{
		//注意在updateValue里面不应该对view的属性经行操作，因为属性操作本来就会调用updateValue. eg:setTextColor()
		m_bneedupdate = false;
		  
		SgRealTimeData oRealTimeData = m_rRenderWindow.m_oShareObject.m_mapRealTimeDatas.get(this.getUniqueID());
		if (oRealTimeData == null)
			return false;
	
		String strValue = oRealTimeData.strValue;
		if (strValue == null || "".equals(strValue) == true)
			return false;
		
  
        if (m_strSignalValue.equals(strValue) == false) {
        	m_strSignalValue = strValue;     //保存数值留作下次比较  	        	
        	m_strContent = strValue;
            
        	if("".equals(oRealTimeData.strData)) return false;
        	if("-999999".equals(oRealTimeData.strData)) return false;
        	if(oRealTimeData.strData==null)return false;
        	data = Float.parseFloat(oRealTimeData.strData);   //获取数值
			return true;
        }
		
		return true;
	}

	@Override
    public boolean needupdate()
    {
	    return m_bneedupdate;
    }
	
	@Override
    public void needupdate(boolean bNeedUpdate)
    {
	    m_bneedupdate = bNeedUpdate;
    }
	
	public void setUniqueID(String strID) {
		m_strID = strID;
	}
	public void setType(String strType) {
		m_strType = strType;
	}
	
	public String getUniqueID() {
		return m_strID;
	}
	
	public String getType() {
		return m_strType;
	}
	
	public Rect getBBox() {
		return m_rBBox;
	}
	
// params:
	String m_strID = "";
	String m_strType = "Label";
    int m_nZIndex = 1;
	int m_nPosX = 49;
	int m_nPosY = 306;
	int m_nWidth = 60;
	int m_nHeight = 30;
	float m_fAlpha = 1.0f;
	float m_fRotateAngle = 0.0f;
	String m_strContent = "";
	String m_strFontFamily = "微软雅黑";
	float m_fFontSize = 12.0f;
	boolean m_bIsBold = false;
	int m_cFontColor = 0xFF008000; 
	String m_strHorizontalContentAlignment = "Center";
	String m_strVerticalContentAlignment = "Center";
	String m_strExpression = "Binding{[Value[Equip:114-Temp:173-Signal:1]]}";
	String m_strColorExpression = ">20[#FF009090]>30[#FF0000FF]>50[#FFFF0000]>60[#FFFFFF00]";
	int D_mon = 0;  //累积量显示模式 默认0日累积量 1：月累积量  2：年累积量
	
	MainWindow m_rRenderWindow = null;	
	String m_strSignalValue = "";
	
	Rect m_rBBox = null;
	Axis my_Axis;   //定义坐标轴控件元素view
	Paint paint;
	
	
	float data=0;
	public boolean m_bneedupdate = true;
}
