package com.sg.uis;

import com.mgrid.main.MainWindow;
import com.sg.common.IObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.Rect;
import android.util.Xml;
import android.view.View;
/** 多点画线 */
public class SgPolyline extends View implements IObject {
	
	public SgPolyline(Context context) {  
        super(context); 
        m_arrfFlowValue = new float[]{5,5};
        m_oPath = new Path();
        m_oPaint = new Paint(); 
        m_oDotEffects = new DashPathEffect(m_arrfFlowValue, 1);
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
		m_oPaint.setStyle(Paint.Style.STROKE); 
        
        if (m_bIsFlow) {
	        m_arrfFlowValue[0] += m_nFlowSpeed / 10.0f;
	        m_arrfFlowValue[1] -= m_nFlowSpeed / 10.0f;
	        if (m_arrfFlowValue[0] >= 10)
	        	m_arrfFlowValue[0] = 5;
	        if (m_arrfFlowValue[1] <= 1)
	        	m_arrfFlowValue[1] = 5;
        }
        
        if (m_bIsDashed) {
	        m_oPaint.setPathEffect(m_oDotEffects);
        }
        
        m_oPath.reset();
        String[] arrStrPoints = m_strPoints.split(";");
        float fStartX = 0.0f, fStartY = 0.0f;
        for (int i = 0; i < arrStrPoints.length; ++i)
        {
        	String[] strOnePoint = arrStrPoints[i].split(",");
        	if (strOnePoint.length != 2)
        		continue;
        	if (i == 0)
        	{
        		fStartX = m_rRenderWindow.getLeft() + ((float)Float.valueOf(strOnePoint[0]) / (float)MainWindow.FORM_WIDTH) * (m_rRenderWindow.VIEW_RIGHT - m_rRenderWindow.VIEW_LEFT);
        		fStartY = m_rRenderWindow.getTop() + ((float)Float.valueOf(strOnePoint[1]) / (float)MainWindow.FORM_HEIGHT) * (m_rRenderWindow.VIEW_BOTTOM - m_rRenderWindow.VIEW_TOP);
        		m_oPath.moveTo(fStartX, fStartY);
        	}
        	else
        	{
        		float fEndX = m_rRenderWindow.getLeft() + ((float)Float.valueOf(strOnePoint[0]) / (float)MainWindow.FORM_WIDTH) * (m_rRenderWindow.VIEW_RIGHT - m_rRenderWindow.VIEW_LEFT);
        		float fEndY = m_rRenderWindow.getTop() + ((float)Float.valueOf(strOnePoint[1]) / (float)MainWindow.FORM_HEIGHT) * (m_rRenderWindow.VIEW_BOTTOM - m_rRenderWindow.VIEW_TOP);
        		m_oPath.lineTo(fEndX, fEndY);
        		fStartX = fEndX;
        		fStartY = fEndY;
        	}
        }
        
        canvas.drawPath(m_oPath, m_oPaint);
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
		rWin.addView(this);
	}
	
	@Override
	public void removeFromRenderWindow(MainWindow rWin) {
		m_rRenderWindow = null;
		rWin.removeView(this);
	} 
	
	public void parseXml(Xml xml){
		
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
        else if ("Points".equals(strName)) {
       	 	m_strPoints = strValue;
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
        else if ("IsFlow".equals(strName))
       	 	m_bIsFlow = Boolean.parseBoolean(strValue);
        else if ("FlowSpeed".equals(strName))
       	 	m_nFlowSpeed = Integer.parseInt(strValue);
        else if ("StateExpression".equals(strName))
        	m_strStateExpression = strValue;
	}

	@Override
	public void initFinished()
	{
	}

	public String getBindingExpression() {
		return m_strStateExpression;
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
		if (m_bIsFlow == true)
			this.invalidate();
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
	int m_nZIndex = 5;
	float m_fStartPointX = 593.5f;
	float m_fStartPointY = 90.5f;
	float m_fEndPointX = 619.5f;
	float m_fEndPointY = 154.5f;
	String m_strPoints = "593.5,90.5;770.5,84.5;734.5,141.5;621.5,154.5;619.5,154.5";
	float m_fAlpha = 1.0f;
	int m_nWidht = 3;
	int m_cColor = 0xFF000000;
	boolean m_bIsDashed = false;
	boolean m_bIsFlow = true;
	int m_nFlowSpeed = 5;
	String m_strStateExpression = "Binding{[State[Equip:114]]}";
	MainWindow m_rRenderWindow = null;
	float[] m_arrfFlowValue = null;
	Path m_oPath = null;
	Paint m_oPaint = null; 
	PathEffect m_oDotEffects = null;
	Rect m_rBBox = null;
}
