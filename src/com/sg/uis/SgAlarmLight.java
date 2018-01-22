package com.sg.uis;

import java.io.IOException;
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
import com.sg.common.SgRealTimeData;

/** 告警灯 */
public class SgAlarmLight extends View implements IObject {
	public SgAlarmLight(Context context) {  
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
			InputStream is = null;
			
			if (null == s_bitAlarmLevel1Image)
			{
				is = assetManager.open("ui/AlarmLevel1.png");
				s_bitAlarmLevel1Image = BitmapFactory.decodeStream(is);
				is.close();
			}

			if (null == s_bitAlarmLevel2Image)
			{
				is = assetManager.open("ui/AlarmLevel2.png");
				s_bitAlarmLevel2Image = BitmapFactory.decodeStream(is);
				is.close();
			}

			if (null == s_bitAlarmLevel3Image)
			{
				is = assetManager.open("ui/AlarmLevel3.png");
				s_bitAlarmLevel3Image = BitmapFactory.decodeStream(is);
				is.close();
			}

			if (null == s_bitAlarmLevel4Image)
			{
				is = assetManager.open("ui/AlarmLevel4.png");
				s_bitAlarmLevel4Image = BitmapFactory.decodeStream(is);
				is.close();
			}

			m_bitCurrentAlarmImage = s_bitAlarmLevel1Image;
		} catch (IOException e) {
			Log.e("UI-AlarmLight","loadImage 异常抛出！");
			e.printStackTrace();
		}
		m_oPaint = new Paint();
		m_rSrcRect = new Rect();
		m_rDestRect = new Rect();
		m_rBBox = new Rect();
	}
	
	@SuppressLint("DrawAllocation")
	protected void onDraw(Canvas canvas) {
		if (m_bitCurrentAlarmImage == null)
			return;
		if (m_rRenderWindow == null)
			return;
		if (m_rRenderWindow.isLayoutVisible(getBBox()) == false)
			return;

		int nWidth = (int) (((float)(m_nWidth) / (float)MainWindow.FORM_WIDTH) * (m_rRenderWindow.VIEW_RIGHT - m_rRenderWindow.VIEW_LEFT));
		int nHeight = (int) (((float)(m_nHeight) / (float)MainWindow.FORM_HEIGHT) * (m_rRenderWindow.VIEW_BOTTOM - m_rRenderWindow.VIEW_TOP));

		m_rSrcRect.left = 0;
		m_rSrcRect.top = 0;
		m_rSrcRect.right = m_bitCurrentAlarmImage.getWidth();
		m_rSrcRect.bottom = m_bitCurrentAlarmImage.getHeight();
		
		m_rDestRect.left = 0;
		m_rDestRect.top = 0;
		m_rDestRect.right = nWidth;
		m_rDestRect.bottom = nHeight;
		canvas.drawBitmap(m_bitCurrentAlarmImage, m_rSrcRect, m_rDestRect, m_oPaint);
		Log.e("ui-AlarmLight","into onDraw!");
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
		if (s_bitAlarmLevel1Image != null && s_bitAlarmLevel1Image.isRecycled() == false)
			s_bitAlarmLevel1Image.recycle();
		if (s_bitAlarmLevel2Image != null && s_bitAlarmLevel2Image.isRecycled() == false)
			s_bitAlarmLevel2Image.recycle();
		if (s_bitAlarmLevel3Image != null && s_bitAlarmLevel3Image.isRecycled() == false)
			s_bitAlarmLevel3Image.recycle();
		if (s_bitAlarmLevel4Image != null && s_bitAlarmLevel4Image.isRecycled() == false)
			s_bitAlarmLevel4Image.recycle();
		rWin.removeView(this);
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
         else if ("EventLevel".equals(strName)) {
     		m_strEventLevel = strValue;
     		if ("Level1".equals(m_strEventLevel))
     			m_bitCurrentAlarmImage = s_bitAlarmLevel1Image;
     		if ("Level2".equals(m_strEventLevel))
     			m_bitCurrentAlarmImage = s_bitAlarmLevel2Image;
     		if ("Level3".equals(m_strEventLevel))
     			m_bitCurrentAlarmImage = s_bitAlarmLevel3Image;
     		if ("Level4".equals(m_strEventLevel))
     			m_bitCurrentAlarmImage = s_bitAlarmLevel4Image;
         }
         else if ("EventLevelExpression".equals(strName)) {
        	 m_strEventLevelExpression = strValue;
         }
	}

	@Override
	public void initFinished()
	{
		
	}

	public String getBindingExpression() {
		return m_strEventLevelExpression;
	}
	
	// 设备更新
	public void updateWidget()
	{
		invalidate();
	}
	
	@Override
	public boolean updateValue() 
	{
		Log.e("ui-AlarmLight","into updateValue!");
		m_bneedupdate = false;
		SgRealTimeData oRealTimeData = m_rRenderWindow.m_oShareObject.m_mapRealTimeDatas.get(this.getUniqueID());
		if (oRealTimeData == null)
			return false;
		String strValue = oRealTimeData.strValue;
		if (strValue == null || "".equals(strValue) == true)
			return false;
		
		int nValue = 0;
		try {
			nValue = Integer.parseInt(strValue);
		}catch(Exception e) {
			
		}
		
        switch (nValue) {
		case 0:
			m_bitCurrentAlarmImage = s_bitAlarmLevel1Image;
			break;
		case 1:
			m_bitCurrentAlarmImage = s_bitAlarmLevel1Image;
			break;
		case 2:
			m_bitCurrentAlarmImage = s_bitAlarmLevel2Image;
			break;
		case 3:
			m_bitCurrentAlarmImage = s_bitAlarmLevel3Image;
			break;
		case 4:
			m_bitCurrentAlarmImage = s_bitAlarmLevel4Image;
			break;
		}
        
        // 数值变化才刷新页面
        if (nValue != m_nLevel) {
        	m_nLevel = nValue;
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
	
	int m_nLevel = 0;
	String m_strEventLevel = "Level1";
	String m_strEventLevelExpression = "Binding{[EventSeverity[Equip:2-Temp:169-Signal:2]]}";
	private static Bitmap s_bitAlarmLevel1Image = null; 
	private static Bitmap s_bitAlarmLevel2Image = null; 
	private static Bitmap s_bitAlarmLevel3Image = null; 
	private static Bitmap s_bitAlarmLevel4Image = null; 
	Bitmap m_bitCurrentAlarmImage = null; 
	
	int m_nLightingIndex = 0;
	MainWindow m_rRenderWindow = null;
	Paint m_oPaint = null;
	Rect m_rSrcRect = null;
	Rect m_rDestRect = null;
	Rect m_rBBox = null;
	
	public boolean m_bneedupdate = true;
}
