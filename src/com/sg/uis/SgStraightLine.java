package com.sg.uis;

import com.mgrid.main.MainWindow;
import com.sg.common.IObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

/** 直线 */
public class SgStraightLine extends View implements IObject {
	public SgStraightLine(Context context) {  
        super(context); 
        m_oPaint = new Paint();
        m_rBBox = new Rect();
    }
	
	@SuppressLint("DrawAllocation")
	protected void onDraw(Canvas canvas) {
		if (m_rRenderWindow == null)
			return;
		if (m_rRenderWindow.isLayoutVisible(getBBox()) == false)
			return;
         
		m_oPaint.setColor(m_cColor);
		m_oPaint.setAntiAlias(true); // 设置画笔的锯齿效果
		m_oPaint.setStrokeWidth(m_nWidht);

		float fX = m_rRenderWindow.getLeft() + m_fStartPointX / (float)MainWindow.FORM_WIDTH * (m_rRenderWindow.VIEW_RIGHT - m_rRenderWindow.VIEW_LEFT);
		float fY = m_rRenderWindow.getTop() + m_fStartPointY / (float)MainWindow.FORM_HEIGHT * (m_rRenderWindow.VIEW_BOTTOM - m_rRenderWindow.VIEW_TOP);
		float fEX = m_rRenderWindow.getLeft() + m_fEndPointX / (float)MainWindow.FORM_WIDTH * (m_rRenderWindow.VIEW_RIGHT - m_rRenderWindow.VIEW_LEFT);
		float fEY = m_rRenderWindow.getTop() + m_fEndPointY / (float)MainWindow.FORM_HEIGHT * (m_rRenderWindow.VIEW_BOTTOM - m_rRenderWindow.VIEW_TOP);

        canvas.drawLine(fX, fY, fEX, fEY, m_oPaint);
	}
	
	@Override
	public void doLayout(boolean bool, int l, int t, int r, int b) {
		if (m_rRenderWindow == null)
			return;
		m_rBBox.left = l;
		m_rBBox.top = t;
		m_rBBox.right = r;
		m_rBBox.bottom = b;
		if (m_rRenderWindow.isLayoutVisible(m_rBBox)) {
			layout(l, t, r, b);
		}
	}
	
	@Override
	public void addToRenderWindow(MainWindow rWin) {
		m_rRenderWindow = rWin;
		rWin.addView(this);
	}
	public View getView() {
		return this;
	}
	
	public int getZIndex()
	{
		return m_nZIndex;
	}
	
	@Override
	public void removeFromRenderWindow(MainWindow rWin) {
		m_rRenderWindow = null;
		rWin.removeView(this);
	} 
	
	public void parseProperties(String strName, String strValue, String strResFolder) {
		 if ("ZIndex".equals(strName)) {
        	 m_nZIndex = Integer.parseInt(strValue);
       	    if (MainWindow.MAXZINDEX < m_nZIndex) MainWindow.MAXZINDEX = m_nZIndex;
         }
         else if ("StartPoint".equals(strName)) {
        	 String[] arrStr = strValue.split(",");
        	 m_fStartPointX = Float.parseFloat(arrStr[0]);
        	 m_fStartPointY = Float.parseFloat(arrStr[1]);
         }
         else if ("EndPoint".equals(strName)) {
        	 String[] arrStr = strValue.split(",");
        	 m_fEndPointX = Float.parseFloat(arrStr[0]);
        	 m_fEndPointY = Float.parseFloat(arrStr[1]);
         }
         else if ("Alpha".equals(strName)) {
        	 m_fAlpha = Float.parseFloat(strValue);
         }
         else if ("Width".equals(strName)) {
        	 m_nWidht = Integer.parseInt(strValue);
         }
         else if ("Color".equals(strName)) {
        	 m_cColor = Color.parseColor(strValue);
         }
         else if ("IsDashed".equals(strName))
        	 m_bIsDashed = Boolean.parseBoolean(strValue);
         else if ("StateExpression".equals(strName))
        	 m_strStateExpression = strValue;
         else if ("ColorExpression".equals(strName))
        	 m_strColorExpression = strValue;
	}

	@Override
	public void initFinished()
	{
	}

	public String getBindingExpression() {
		return m_strColorExpression;
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
	
	@Override
	public void updateWidget() {
		
	}

	@Override
	public boolean updateValue()
	{
        return false;
	}

	@Override
    public boolean needupdate()
    {
	    return false;
    }
	
	@Override
    public void needupdate(boolean bNeedUpdate)
    {
    }
	
	public Rect getBBox() {
		return m_rBBox;
	}
	
// params:
	String m_strID = "";
	String m_strType = "";
	int m_nZIndex = 2;
	float m_fStartPointX = 0;
	float m_fStartPointY = 0;
	float m_fEndPointX = 400;
	float m_fEndPointY = 400;
	float m_fAlpha = 1.0f;
	int m_nWidht = 3;
	int m_cColor = 0xFFFF0000;
	boolean m_bIsDashed = false;
	String m_strStateExpression = "";
	String m_strColorExpression = "";
	MainWindow m_rRenderWindow = null;
	
	Paint m_oPaint = null; 
	Rect m_rBBox = null;
}
