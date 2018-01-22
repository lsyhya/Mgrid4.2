package com.sg.uis;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.MotionEvent;
import android.view.View;

import com.mgrid.main.MainWindow;
import com.sg.common.CFGTLS;
import com.sg.common.IObject;
import com.sg.common.SgRealTimeData;
/** 开关 */
public class Breaker extends View implements IObject {
	public Breaker(Context context) {  
        super(context); 
        this.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				return true;
			}
        });

		m_oPaint = new Paint();
		m_rBBox = new Rect();
		m_rRectF = new RectF();
	}
	
	@SuppressLint("DrawAllocation")
	protected void onDraw(Canvas canvas) {
		if (m_rRenderWindow == null)
			return;
		if (m_rRenderWindow.isLayoutVisible(getBBox()) == false)
			return;
		
		m_oPaint.setColor(m_cPaintColor);
		m_oPaint.setAntiAlias(true); // 设置画笔的锯齿效果
		m_oPaint.setStrokeWidth(m_nThickness);
		m_oPaint.setStyle(Paint.Style.STROKE);
        
        float nWidth = ((float)(m_nWidth) / (float)MainWindow.FORM_WIDTH) * (float)(m_rRenderWindow.VIEW_RIGHT - m_rRenderWindow.VIEW_LEFT);
		float nHeight = ((float)(m_nHeight) / (float)MainWindow.FORM_HEIGHT) * (float)(m_rRenderWindow.VIEW_BOTTOM - m_rRenderWindow.VIEW_TOP);

		
		// 调整画布坐标系
		canvas.save();    //保存canvas状态
		canvas.rotate(m_fRotateAngle-90, nWidth/2, nHeight/2); // 通过计算中心点旋转坐标系
		
		
		// 画圆
		//m_rRectF.left = nHeight / 8;
		m_rRectF.left = m_nThickness;
		m_rRectF.top = nHeight / 2 - nHeight / 8;
		m_rRectF.right = nHeight / 4;
		m_rRectF.bottom = nHeight / 2 + nHeight / 8;
		canvas.drawOval(m_rRectF, m_oPaint);
        
        // 画竖线
        canvas.drawLine(6*nWidth/8, 3*nHeight/8 , 8*nWidth/8, 5*nHeight / 8, m_oPaint);
        canvas.drawLine(6*nWidth/8, 5*nHeight / 8 , 8*nWidth/8, 3*nHeight / 8, m_oPaint);

        // 画开关
        if (m_bState == true) {
        	
        	canvas.drawLine(nHeight / 8, nHeight / 2, nWidth, nHeight / 2-1, m_oPaint);
        }
        else{
        	
        	canvas.drawLine(nHeight / 8, nHeight / 2, nWidth, 0, m_oPaint);
        }
        
        // 恢复坐标系
        canvas.restore();
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
		if (m_rRenderWindow.isLayoutVisible(m_rBBox))
			layout(nX, nY, nX+nWidth, nY+nHeight);
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
	
	public void parseProperties(String strName, String strValue, String strResFolder) throws Exception {
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
         else if ("Foreground".equals(strName)) {
        	 m_cForeground = CFGTLS.parseColor(strValue);
         }
         else if ("Background".equals(strName)) {
        	 m_cBackground = CFGTLS.parseColor(strValue);
         }
         else if ("Thickness".equals(strName)) {
         	m_nThickness = Integer.parseInt(strValue);
         }
         else if ("State".equals(strName)) {
         	if ("True".equals(strValue) == true)
         		m_bState = true;
         	else
         		m_bState = false;
         }
         else if ("ClickEvent".equals(strName)) {
          	m_strClickEvent = strValue;
         }
         else if ("Expression".equals(strName)) {
          	m_strExpression = strValue;
         }
	}

	@Override
	public void initFinished()
	{
		m_cPaintColor = m_bState ? m_cForeground : m_cBackground;
	}

	public String getBindingExpression() {
		return m_strExpression;
	}
	
	// 设备更新
	public void updateWidget() {
		this.invalidate();
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
		
		boolean bState = false;
		if (oRealTimeData.nDataType != 1) {
			int nValue = 0;
			try {
				nValue = (int)Double.parseDouble(strValue);
			}catch(Exception e) {
				
			}
			if (nValue == 0) {
				
				bState = false;
				m_cPaintColor = m_cBackground;
			}
			else {
				
				bState = true;
				m_cPaintColor = m_cForeground;
			}
		}
		if (bState != m_bState) {
			m_bState = bState;
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
	
	public View getView() {
		return this;
	}
	
	public int getZIndex()
	{
		return m_nZIndex;
	}
	
	public Rect getBBox() {
		return m_rBBox;
	}

// params:
	String m_strID = "";
	String m_strType = "";  
    int m_nZIndex = 11;
	int m_nPosX = 42;
	int m_nPosY = 388;
	int m_nWidth = 54;
	int m_nHeight = 48;
	float m_fAlpha = 1.0f;
	float m_fRotateAngle = 0.0f;
	int m_cForeground = Color.RED;
	int m_cBackground = Color.GREEN;
	int m_cPaintColor = 0xff000000;
	int m_nThickness = 2;
	boolean m_bState = false;
	String m_strClickEvent = "";
	String m_strExpression = "";
	
	MainWindow m_rRenderWindow = null;
	Paint m_oPaint = null;
	RectF m_rRectF = null;
	Rect m_rBBox = null;
	
	public boolean m_bneedupdate = true;
}
