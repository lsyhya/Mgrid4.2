package com.sg.uis;

import com.mgrid.main.MainWindow;
import com.sg.common.CFGTLS;
import com.sg.common.IObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Environment;
import android.view.MotionEvent;
import android.view.View;

/** ´°¿Ú */
public class SgForm extends View implements IObject {
	public SgForm(Context context) {  
        super(context); 
        this.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				return true;
			}
        });
        m_oPaint = new Paint();
    	m_rSrcRect = new Rect();
		m_rDestRect = new Rect();
		m_rBBox = new Rect();
	}

	@SuppressLint("DrawAllocation")
	protected void onDraw(Canvas canvas) {
		if (m_rRenderWindow == null)
			return;
		if (m_rRenderWindow.isLayoutVisible(getBBox()) == false)
			return;
		
		if (m_bitBackImage == null) {  
			m_rRenderWindow.setBackgroundColor(m_cBackColor);
	       	return;
		}
		
		int nWidth = m_rRenderWindow.VIEW_RIGHT - m_rRenderWindow.VIEW_LEFT;
		int nHeight = m_rRenderWindow.VIEW_BOTTOM - m_rRenderWindow.VIEW_TOP;
		m_rSrcRect.left = 0;
		m_rSrcRect.top = 0;
		m_rSrcRect.right = m_bitBackImage.getWidth();
		m_rSrcRect.bottom = m_bitBackImage.getHeight();
		
		m_rDestRect.left = 0;
		m_rDestRect.top = 0;
		m_rDestRect.right = nWidth;
		m_rDestRect.bottom = nHeight;
		canvas.drawBitmap(m_bitBackImage, m_rSrcRect, m_rDestRect, m_oPaint);	
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
	
	@Override
	public void removeFromRenderWindow(MainWindow rWin) {
		if (m_bitBackImage != null && m_bitBackImage.isRecycled() == false)
			m_bitBackImage.recycle();
		rWin.removeView(this);
	} 

	public void parseProperties(String strName, String strValue, String strResFolder) throws Exception {
		if ("Params".equals(strName))
       	 	m_strParams = strValue;
        else if ("Size".equals(strName)) {
       	 	String[] arrSize = strValue.split(",");
       	 	m_nWidth = Integer.parseInt(arrSize[0]);
       	 	m_nHeight = Integer.parseInt(arrSize[1]);
       	 	MainWindow.FORM_WIDTH = m_nWidth;
       	 	MainWindow.FORM_HEIGHT = m_nHeight;
        }
        else if ("BackColor".equals(strName)) {
       	 	m_cBackColor = Color.parseColor(strValue);
        }
        else if ("BackImage".equals(strName)) {
       	 	if ("".equals(strValue) == false) {
       	 		m_strBackImage = Environment.getExternalStorageDirectory().getPath() + strResFolder + strValue;
       	 		//m_bitBackImage = BitmapFactory.decodeFile(m_strBackImage);
       	 		m_bitBackImage = CFGTLS.getBitmapByPath(m_strBackImage);
       	 		/*
        		 try {
        			 InputStream is = new BufferedInputStream(new FileInputStream(m_strBackImage));
        			 m_bitBackImage = BitmapFactory.decodeStream(is);
        		 } catch (Exception e) {
        			 e.printStackTrace();
        		 }*/
       	 	}
        }
        else if ("Alpha".equals(strName)) {
       	 	m_fAlpha = Float.parseFloat(strValue);
        }
        else if ("RotateAngle".equals(strName)) {
       	 	m_fRotateAngle = Float.parseFloat(strValue);
        }
        else if ("ZIndex".equals(strName)) {
       	 	m_nZIndex = Integer.parseInt(strValue);
       	    if (MainWindow.MAXZINDEX < m_nZIndex) MainWindow.MAXZINDEX = m_nZIndex;
        }
	}

	@Override
	public void initFinished()
	{
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
	
	public String getBindingExpression() {
		return "";
	}
	
	@Override
	public void updateWidget() {
		
	}

	@Override
	public boolean updateValue()
	{
		m_bneedupdate = false;
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
	String m_strType = "Form";
	String m_strParams = "";
	int m_nWidth = 1000;
	int m_nHeight = 800;
	int m_cBackColor = 0xFF000000;
	String m_strBackImage = "";
	float m_fAlpha = 1.0f;
	float m_fRotateAngle = 0.0f;
	int m_nZIndex = 0;
	Bitmap m_bitBackImage = null;
	MainWindow m_rRenderWindow = null;
	
	Paint m_oPaint = null;
	Rect m_rSrcRect = null;
	Rect m_rDestRect = null;
	Rect m_rBBox = null;
	
	public boolean m_bneedupdate = true;
}
