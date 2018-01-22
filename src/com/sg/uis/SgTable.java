package com.sg.uis;

import com.mgrid.main.MainWindow;
import com.sg.common.CFGTLS;
import com.sg.common.IObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;

/** 布局列表 */
public class SgTable extends View implements IObject {
    public SgTable(Context context) {  
        super(context); 
        
        /*
        //setBackgroundColor(0x00FFFFFF);
        this.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View view, MotionEvent event) {
				return false;
			}
        });
        */
        
        m_oPaint = new Paint();
        m_rBBox = new Rect();
        
    	m_vTableBody = new View(context);
    	m_vTableTitleRow = new View(context);
    	m_vTableTitleClo = new View(context);
    }

    public void setLocation(int x, int y) {
    	m_nPosX = x;
    	m_nPosY = y;
    }
    
    public void setSize(int w, int h) {
    	m_nWidth = w;
    	m_nHeight = h;
    }
    
    public void setRow(int r) {
    	m_nRowNum = r;
    }
    
    public void setColum(int c) {
    	m_nColNum = c;
    }
    
    @SuppressLint("DrawAllocation")
	protected void onDraw(Canvas canvas) {
    	if (m_rRenderWindow == null)
			return;
		if (m_rRenderWindow.isLayoutVisible(getBBox()) == false)
			return;
		
    	m_oPaint.setAntiAlias(true);
    	m_oPaint.setColor(m_cLineColor);
    	m_oPaint.setStyle(Paint.Style.STROKE);
    	m_oPaint.setStrokeWidth(m_nLineThickness);

    	// 由于 doLayout 已经调整过坐标， 无需再次计算起始坐标
		//int nX = m_rRenderWindow.getLeft() + (int) (((float)m_nPosX / (float)MainWindow.FORM_WIDTH) * (m_rRenderWindow.VIEW_RIGHT - m_rRenderWindow.VIEW_LEFT));
		//int nY = m_rRenderWindow.getTop() + (int) (((float)m_nPosY / (float)MainWindow.FORM_HEIGHT) * (m_rRenderWindow.VIEW_BOTTOM - m_rRenderWindow.VIEW_TOP));
    	
    	int nX = m_nLineThickness;
    	int nY = m_nLineThickness;
		int nWidth = (int) (((float)(m_nWidth) / (float)MainWindow.FORM_WIDTH) * (m_rRenderWindow.VIEW_RIGHT - m_rRenderWindow.VIEW_LEFT));
		int nHeight = (int) (((float)(m_nHeight) / (float)MainWindow.FORM_HEIGHT) * (m_rRenderWindow.VIEW_BOTTOM - m_rRenderWindow.VIEW_TOP));

		float fFirstRow = nHeight * m_fFirstRowRatio;
		float fFirstCol = nWidth * m_fFirstColRatio;
        float fGridWidth = (float)(nWidth-fFirstCol) / (float)(m_nColNum-1);
        float fGridHeight = (float)(nHeight-fFirstRow) / (float)(m_nRowNum-1);
        // 左上角矩形
        canvas.drawRect(nX, nY, nX+fFirstCol, nY+fFirstRow, m_oPaint);
        // 第一行
        for (int i = 0; i < m_nColNum-1; ++i)
        	canvas.drawRect(nX+fFirstCol+i*fGridWidth, nY, nX+fFirstCol+(i+1)*fGridWidth, nY+fFirstRow, m_oPaint);
        // 第一列
        for (int i = 0; i < m_nRowNum-1; ++i)
        	canvas.drawRect(nX, nY+fFirstRow+i*fGridHeight, nX+fFirstCol, nY+fFirstRow+(i+1)*fGridHeight, m_oPaint);
        // 右下表格
        for (int i = 0; i < m_nRowNum-1; i++) {
            for (int j = 0; j < m_nColNum-1; j++) {
            	canvas.drawRect(nX+fFirstCol+j*fGridWidth, nY+fFirstRow+i*fGridHeight, nX+fFirstCol+(j+1)*fGridWidth, nY+fFirstRow+(i+1)*fGridHeight, m_oPaint);       
            }
        }
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
		//*
		int nX = l + (int) (((float)m_nPosX / (float)MainWindow.FORM_WIDTH) * (r-l));
		int nY = t + (int) (((float)m_nPosY / (float)MainWindow.FORM_HEIGHT) * (b-t));
		int nWidth = (int) (((float)(m_nWidth) / (float)MainWindow.FORM_WIDTH) * (r-l));
		int nHeight = (int) (((float)(m_nHeight) / (float)MainWindow.FORM_HEIGHT) * (b-t));
		m_rBBox.left = nX;
		m_rBBox.top = nY;
		m_rBBox.right = nX+nWidth;
		m_rBBox.bottom = nY+nHeight;
		
		int fFirstRow = (int) (nHeight * m_fFirstRowRatio);
		int fFirstCol = (int) (nWidth * m_fFirstColRatio);
        int fGridWidth = (int) ((float)(nWidth-fFirstCol) / (float)(m_nColNum-1));
        int fGridHeight = (int) ((float)(nHeight-fFirstRow) / (float)(m_nRowNum-1));
        
		if (m_rRenderWindow.isLayoutVisible(m_rBBox))
		{
			// 表体
			// m_vTableBody.layout(nX+fFirstCol, nY+fFirstRow, nX+nWidth, nY+nHeight);
			m_vTableBody.layout(nX+fFirstCol, nY, nX+nWidth, nY+nHeight);
			
			// 表头 | 首行
			if (m_bIsHasHead)
			m_vTableTitleClo.layout(nX, nY+fFirstRow, nX+fFirstCol, nY+fFirstRow+(m_nRowNum-1)*fGridHeight);
			
			// 首列
			m_vTableTitleRow.layout(nX+fFirstCol, nY, nX+fFirstCol+(m_nColNum-1)*fGridWidth, nY+fFirstRow);
			
			// 表格
			layout(nX-m_nLineThickness, nY-m_nLineThickness, nX+nWidth+m_nLineThickness, nY+nHeight+m_nLineThickness);
		}
		//*/
	}
	
	@Override
	public void addToRenderWindow(MainWindow rWin) {
		m_rRenderWindow = rWin;
		
		m_vTableBody.setBackgroundColor(m_cTableBackgroundColor);
		m_vTableTitleClo.setBackgroundColor(m_cHeadBackgroundColor);
		m_vTableTitleRow.setBackgroundColor(m_cFirstColBackgroundColor);
		
		rWin.addView(m_vTableBody);
		rWin.addView(m_vTableTitleClo);
		rWin.addView(m_vTableTitleRow);
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
		m_vTableBody = null;
		m_vTableTitleClo = null;
		m_vTableTitleRow = null;
		m_rRenderWindow = null;
		
		rWin.removeView(m_vTableBody);
		rWin.removeView(m_vTableTitleClo);
		rWin.removeView(m_vTableTitleRow);
		rWin.removeView(this);
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
       	    m_vTableBody.setAlpha(m_fAlpha);
        }
        else if ("RowNum".equals(strName))
        	m_nRowNum = Integer.parseInt(strValue);
        else if ("ColNum".equals(strName))
       	 	m_nColNum = Integer.parseInt(strValue);
        else if ("FirstRowRatio".equals(strName))
        	m_fFirstRowRatio = Float.parseFloat(strValue);
        else if ("FirstColRatio".equals(strName))
        	m_fFirstColRatio = Float.parseFloat(strValue);
        else if ("LineColor".equals(strName)) {
	       	m_cLineColor = CFGTLS.parseColor(strValue);
	    }
        else if ("HeadBackgroundColor".equals(strName)) {
       	 	m_cHeadBackgroundColor = CFGTLS.parseColor(strValue);
       	    m_vTableTitleRow.setBackgroundColor(m_cHeadBackgroundColor);
        }
        else if ("FirstColBackgroundColor".equals(strName)) {
       	 	m_cFirstColBackgroundColor = CFGTLS.parseColor(strValue);
       	    m_vTableTitleClo.setBackgroundColor(m_cFirstColBackgroundColor);
        }
        else if ("TableBackgroundColor".equals(strName)) {
       	 	m_cTableBackgroundColor = CFGTLS.parseColor(strValue);
       	 	m_vTableBody.setBackgroundColor(m_cTableBackgroundColor);
        }
        else if ("LineThickness".equals(strName))
   		 	m_nLineThickness = Integer.parseInt(strValue);
        else if ("IsHasHead".equals(strName))
        	m_bIsHasHead = Boolean.parseBoolean(strValue);
        else if ("Radius".equals(strName))
        	m_nRadius = Integer.parseInt(strValue);
	}

	@Override
	public void initFinished()
	{
		if (m_bIsHasHead) m_nRowNum ++;
	}

	public String getBindingExpression() {
		return "";
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
	int m_nPosX = 150;
	int m_nPosY = 250;
	int m_nWidth = 300;
	int m_nHeight = 350;
	int m_nRowNum = 3;
	int m_nColNum = 3;
	float m_fAlpha = 1.0f;
	int m_nZIndex = 0;
	float m_fFirstRowRatio = 0.3f;
	float m_fFirstColRatio = 0.3f;
	int m_cLineColor = Color.BLACK;
	int m_cHeadBackgroundColor = 0x00FFFFFF;  // 0xFFFF0000
	int m_cFirstColBackgroundColor = 0x00FFFFFF; // 0xFF00FF00
	int m_cTableBackgroundColor = 0x00FFFFFF; // 0xFF0000FF
	int m_nLineThickness = 3;
	boolean m_bIsHasHead = false;
	int m_nRadius = 0;
	MainWindow m_rRenderWindow = null;
	Paint m_oPaint = null;
	Rect m_rBBox = null;
	View m_vTableBody = null;
	View m_vTableTitleRow = null;
	View m_vTableTitleClo = null;
}
