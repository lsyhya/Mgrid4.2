package com.sg.uis;

import java.io.IOException;

import com.sg.common.SgRealTimeData;

import java.io.InputStream;

import com.mgrid.main.MainWindow;
import com.sg.common.IObject;
import com.sg.common.MutiThreadShareObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
/** 状态面板 */
public class SgStatePanel extends View implements IObject {
	public SgStatePanel(Context context) {  
        super(context); 
        this.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				return true;
			}
        });
		// load image
		try {
			AssetManager assetManager = this.getContext().getResources().getAssets();
			InputStream is = assetManager.open("ui/normal.png");
			s_bitNormalImage = BitmapFactory.decodeStream(is);
			is = assetManager.open("ui/error.png");
			s_bitErrorImage = BitmapFactory.decodeStream(is);
			is = assetManager.open("ui/warning.png");
			s_bitWarrningImage = BitmapFactory.decodeStream(is);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		m_bitCurrentImage = s_bitNormalImage;
		m_oPaint = new Paint();
		m_rSrcRect = new Rect();
		m_rDestRect = new Rect();
		m_rBBox = new Rect();
	}
	
	@SuppressLint("DrawAllocation")
	protected void onDraw(Canvas canvas) {
		if (m_bitCurrentImage == null)
			return;
		if (m_rRenderWindow == null)
			return;
		if (m_rRenderWindow.isLayoutVisible(getBBox()) == false)
			return;
		
		int nWidth = (int) (((float)(m_nWidth) / (float)MainWindow.FORM_WIDTH) * (m_rRenderWindow.VIEW_RIGHT - m_rRenderWindow.VIEW_LEFT));
		int nHeight = (int) (((float)(m_nHeight) / (float)MainWindow.FORM_HEIGHT) * (m_rRenderWindow.VIEW_BOTTOM - m_rRenderWindow.VIEW_TOP));

		m_rSrcRect.left = 0;
		m_rSrcRect.top = 0;
		m_rSrcRect.right = m_bitCurrentImage.getWidth();
		m_rSrcRect.bottom = m_bitCurrentImage.getHeight();
		
		m_rDestRect.left = 0;
		m_rDestRect.top = 0;
		m_rDestRect.right = nWidth;
		m_rDestRect.bottom = nHeight;
		canvas.drawBitmap(m_bitCurrentImage, m_rSrcRect,m_rDestRect,m_oPaint);
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
		}
	}
	
	@Override
	public void addToRenderWindow(MainWindow rWin) {
		m_rRenderWindow = rWin;
		rWin.addView(this);
	}
	
	@Override
	public void removeFromRenderWindow(MainWindow rWin) {
		m_rRenderWindow = null;
		if (s_bitNormalImage != null && s_bitNormalImage.isRecycled() == false)
			s_bitNormalImage.recycle();
		if (s_bitErrorImage != null && s_bitErrorImage.isRecycled() == false)
			s_bitErrorImage.recycle();
		if (s_bitWarrningImage != null && s_bitWarrningImage.isRecycled() == false)
			s_bitWarrningImage.recycle();

		rWin.removeView(this);
	} 
	public View getView() {
		return this;
	}
	
	public int getZIndex()
	{
		return m_nZIndex;
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
       	 	m_oPaint.setAlpha((int)(m_fAlpha*255));
        }
        else if ("State".equals(strName)) {
    		m_strState = strValue;
    		if ("Normal".equals(m_strState))
    			this.m_bitCurrentImage = this.s_bitNormalImage;
    		if ("Interrupt".equals(m_strState))
    			this.m_bitCurrentImage = this.s_bitErrorImage;
    		if ("Alarm".equals(m_strState))
    			this.m_bitCurrentImage = this.s_bitWarrningImage;
        }
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

		//Log.e("Statepanel-updateValue","线程池获取的数据str:"+strValue);
		int nValue = 0;
		try {
			nValue = Integer.parseInt(strValue);
		}catch(Exception e) {
			
		}
		
		Bitmap m_bitImage = m_bitCurrentImage;
		switch (nValue) {
		case 0:
			m_bitImage = s_bitNormalImage;
			break;
		case 1:
			m_bitImage = s_bitErrorImage;
			break;
		case 2:
			m_bitImage = s_bitWarrningImage;
			break;
		}
		if (m_bitImage != m_bitCurrentImage) {
			m_bitCurrentImage = m_bitImage;
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
	
	public Rect getBBox() {
		return m_rBBox;
	}
	
// params:
	String m_strID = "";
	String m_strType = "StatePanel";  
    int m_nZIndex = 11;
	int m_nPosX = 42;
	int m_nPosY = 388;
	int m_nWidth = 54;
	int m_nHeight = 48;
	float m_fAlpha = 1.0f;
	String m_strState = "Normal";
	String m_strStateExpression = "Binding{[State[Equip:114]]}";
	Bitmap s_bitNormalImage = null; 
	Bitmap s_bitErrorImage = null; 
	Bitmap s_bitWarrningImage = null; 
	Bitmap m_bitCurrentImage = null;

	int m_nLightingIndex = 0;
	MainWindow m_rRenderWindow = null;
	Paint m_oPaint = null;
	Rect m_rSrcRect = null;
	Rect m_rDestRect = null;
	Rect m_rBBox = null;
	
	public boolean m_bneedupdate = true;
}
