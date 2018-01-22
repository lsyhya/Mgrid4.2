package com.sg.uis;

import com.mgrid.main.MainWindow;
import com.sg.common.CFGTLS;
import com.sg.common.SgRealTimeData;
import com.sg.common.IObject;
import com.sg.common.UtExpressionParser;
import com.sg.common.UtExpressionParser.stIfElseExpression;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.Rect;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
/** TextBox */
public class SgTextBox extends TextView implements IObject {

	public SgTextBox(Context context) {
		super(context, null, android.R.attr.textViewStyle); 
		this.setClickable(true);
        this.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				return true;
			}
        });
        m_rBBox = new Rect();
	}
	
	@Override
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
			//float fWinScale = (float)SgRenderWindow.SCREEN_WIDTH / (float)SgRenderWindow.m_snFormWidth;
			//this.setTextScaleX(m_rRenderWindow.getScale()*fWinScale);
		}
	}

	@Override
	public void onDraw(Canvas canvas) {
		if (m_rRenderWindow == null)
			return;
		if (m_rRenderWindow.isLayoutVisible(getBBox()) == false)
			return;
		
		super.onDraw(canvas);
	}
	
	@Override
	public void addToRenderWindow(MainWindow rWin) {
		m_rRenderWindow = rWin;
		rWin.addView(this);
	}

	@Override
	public void removeFromRenderWindow(MainWindow rWin) {
		rWin.removeView(this);
	}
	public View getView() {
		return this;
	}
	
	public int getZIndex()
	{
		return m_nZIndex;
	}
	
	@SuppressLint("NewApi")
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
       		this.setAlpha(m_fAlpha);
        }
        else if ("RotateAngle".equals(strName))
       		m_fRotateAngle = Float.parseFloat(strValue);
        else if ("Text".equals(strName)) {
     		m_strText = strValue;
     		this.setText(m_strText);
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
        else if ("Foreground".equals(strName)) {
       		m_cForeground = Color.parseColor(strValue);
       		this.setTextColor(m_cForeground);
        }
        else if ("Background".equals(strName)) {
        	m_cBackground = Color.parseColor(strValue);
       		this.setBackgroundColor(m_cBackground);	 
        }
        else if ("HorizontalContentAlignment".equals(strName))
     		m_strHorizontalContentAlignment = strValue;
        else if ("VerticalContentAlignment".equals(strName))
     		m_strVerticalContentAlignment = strValue;
        else if ("BorderThickness".equals(strName))
     		m_nBorderThickness = Integer.parseInt(strValue);
        else if ("BorderColor".equals(strName))
        	m_cBorderColor = Color.parseColor(strValue);
        else if ("Expression".equals(strName)) {
     		m_strExpression = strValue;
     		//m_oMathExpression = UtExpressionParser.getInstance().parseExpression(strValue);
     		m_oIfElseExpression = UtExpressionParser.getInstance().parseIfElseExpression(strValue);
        }
	}

	@Override
	public void initFinished()
	{
		//this.setSingleLine();
		//setPadding(0, (m_nHeight-getLineHeight())/2+1, 0, 0);
		
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
		if (null == m_strExpression || m_strExpression.trim().isEmpty())
			m_bneedupdate = false;
		
		return m_strExpression;
	}
	
	public void updateWidget() {
		this.invalidate();
		this.setText(m_strText);
	}
	
	@Override
	public boolean updateValue()
	{
		m_bneedupdate = false;
		
		SgRealTimeData oRealTimeData = m_rRenderWindow.m_oShareObject.m_mapRealTimeDatas.get(this.getUniqueID());
		if (oRealTimeData == null)
			return false;
		String strValue = oRealTimeData.strValue;
		if (strValue == null || "".equals(strValue) == true)
			return false;
		
        // 内容变化才刷新页面
        if (m_strSignalValue.equals(strValue) == false) {
        	m_strSignalValue = strValue;     	
        	m_strText = strValue;
        	if (m_oIfElseExpression != null) {
        		if (m_oIfElseExpression.isDigist == false) {
        			if (strValue.equals(m_oIfElseExpression.strRet))
        				m_strText = m_oIfElseExpression.strTrueSelect;
        			else
        				m_strText = m_oIfElseExpression.strFalseSelect;
        		}
        		else {
        			try {
        				if (Double.parseDouble(strValue) == Double.parseDouble(m_oIfElseExpression.strRet))
        					m_strText = m_oIfElseExpression.strTrueSelect;
        				else
        					m_strText = m_oIfElseExpression.strFalseSelect;
        			} catch(Exception e) {
        				Log.v("Warnning", "SgTextBox 强转失败 字符串= " + strValue);
        			}
        		}
	        }
        	
			return true;
        }
		
        return false;
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
	String m_strType = "";
    int m_nZIndex = 8;
 	int m_nPosX = 312;
 	int m_nPosY = 289;
 	int m_nWidth = 120;
 	int m_nHeight = 30;
 	float m_fAlpha = 1.0f;
 	float m_fRotateAngle = 0.0f;
 	String m_strText = "设置内容";
 	String m_strFontFamily = "微软雅黑";
 	float m_fFontSize = 12.0f;
 	boolean m_bIsBold = false;
 	int m_cForeground = 0xFF008000;
 	int m_cBackground = 0xFFFFFFFF;
 	String m_strHorizontalContentAlignment = "Center";
 	String m_strVerticalContentAlignment = "Center";
 	int m_nBorderThickness = 1;
 	int m_cBorderColor = 0xFF7F9DB9;
 	String m_strExpression = ""; //"Binding{[Value[Equip:114-Temp:173-Signal:3]]}";
 	
 	String m_strSignalValue = "";
 	MainWindow m_rRenderWindow = null;	
 	//stMathExpression m_oMathExpression = null;
 	stIfElseExpression m_oIfElseExpression = null;
 	Rect m_rBBox = null;
 	
 	public boolean m_bneedupdate = true;
}
