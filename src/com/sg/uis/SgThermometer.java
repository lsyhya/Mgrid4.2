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
import android.view.View;

import com.sg.common.SgRealTimeData;

/** 温度计 */
public class SgThermometer extends View implements IObject {
	public SgThermometer(Context context) {  
        super(context); 

		// load images
		try {
			AssetManager assetManager = this.getContext().getResources().getAssets();
			InputStream is = null;

			if (null == s_bitThermometerImage)
			{
				is = assetManager.open("ui/Thermometer.png");
				s_bitThermometerImage = BitmapFactory.decodeStream(is);
				is.close();
			}

			if (null == s_bitThermometerWaterImage)
			{
				is = assetManager.open("ui/ThermometerWater.png");
				s_bitThermometerWaterImage = BitmapFactory.decodeStream(is);
				is.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		m_oPaint = new Paint();
		m_rSrcRect = new Rect();
		m_rDestRect = new Rect();
		m_rBBox = new Rect();
	}

	@SuppressLint("DrawAllocation")
	protected void onDraw(Canvas canvas) {
		if (s_bitThermometerImage == null || s_bitThermometerWaterImage == null)
			return;
		if (m_rRenderWindow == null)
			return;
		if (m_rRenderWindow.isLayoutVisible(getBBox()) == false)
			return;
		
		int nWidth = (int) (((float)(m_nWidth) / (float)MainWindow.FORM_WIDTH) * (m_rRenderWindow.VIEW_RIGHT - m_rRenderWindow.VIEW_LEFT));
		int nHeight = (int) (((float)(m_nHeight) / (float)MainWindow.FORM_HEIGHT) * (m_rRenderWindow.VIEW_BOTTOM - m_rRenderWindow.VIEW_TOP));
		
		float fScaleX = (float)nWidth / (float)s_bitThermometerImage.getWidth();
		float fScaleY = (float)nHeight / (float)s_bitThermometerImage.getHeight();
		canvas.scale(fScaleX, fScaleY);
		
		// 256*695
		canvas.drawBitmap(s_bitThermometerImage, 0,0, m_oPaint);
		double dTrueTemperature = m_dTemperature + 30.0;
		dTrueTemperature = dTrueTemperature > 80.0f ? 80.0f : dTrueTemperature; // 80.0f--->温度刻度
		// 底部高196->该数据在photoshop中得到
		int nDown = 196;
		int nWaterX = s_bitThermometerImage.getWidth() / 2 - s_bitThermometerWaterImage.getWidth() / 2;
		int nWaterY = 695-196;
		int nWaterW = s_bitThermometerWaterImage.getWidth();
		// 顶部高695-94->该数据在photoshop中得到
		int nUp = 695-94;
		// temperature
		int nValue = (int)((nUp - nDown) * dTrueTemperature / 80.0f);
		m_rSrcRect.left = 0;
		m_rSrcRect.top = 0;
		m_rSrcRect.right = s_bitThermometerWaterImage.getWidth();
		m_rSrcRect.bottom = s_bitThermometerWaterImage.getHeight();
		
		m_rDestRect.left = nWaterX;
		m_rDestRect.top = nWaterY-nValue;
		m_rDestRect.right = nWaterX+nWaterW;
		m_rDestRect.bottom = nWaterY;
		canvas.drawBitmap(s_bitThermometerWaterImage, m_rSrcRect, m_rDestRect, m_oPaint);
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

		if (s_bitThermometerImage != null && s_bitThermometerImage.isRecycled() == false)
			s_bitThermometerImage.recycle();
		if (s_bitThermometerWaterImage != null && s_bitThermometerWaterImage.isRecycled() == false)
			s_bitThermometerWaterImage.recycle();
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
        else if ("Temperature".equals(strName))
    		m_dTemperature = Integer.parseInt(strValue);
        else if ("Expression".equals(strName)) {
    		m_strExpression = strValue;
        }
	}

	@Override
	public void initFinished()
	{
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
		m_bneedupdate = false;
		
		SgRealTimeData oRealTimeData = m_rRenderWindow.m_oShareObject.m_mapRealTimeDatas.get(this.getUniqueID());
		if (oRealTimeData == null)
			return false;
		String strValue = oRealTimeData.strValue;
		if (strValue == null || "".equals(strValue) == true)
			return false;
		
		double dTemperature = 0.0;
		try {
			dTemperature = Double.parseDouble(strValue);
		}catch(Exception e) {
			
		}
		
        // 值变化才刷新页面
        if (m_dTemperature != dTemperature) {
        	m_dTemperature = dTemperature;
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
	
	public void setTemperature(int nTemperature) {
		m_dTemperature = nTemperature;
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
    int m_nZIndex = 11;
	int m_nPosX = 162;
	int m_nPosY = 375;
	int m_nWidth = 57;
	int m_nHeight = 124;
	float m_fAlpha = 1.0f;
	
	double m_dTemperature = 0.0;
	String m_strExpression = "Binding{[Value[Equip:115-Temp:174-Signal:35]]}";
	private static Bitmap s_bitThermometerImage = null; 
	private static Bitmap s_bitThermometerWaterImage = null; 
	MainWindow m_rRenderWindow = null;
	Paint m_oPaint = null;
	Rect m_rSrcRect = null;
	Rect m_rDestRect = null;
	Rect m_rBBox = null;
	
	public boolean m_bneedupdate = true;
}
