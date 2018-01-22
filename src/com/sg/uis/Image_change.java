package com.sg.uis;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.ref.WeakReference;

import com.mgrid.main.MainWindow;
import com.mgrid.main.R;
import com.sg.common.CFGTLS;
import com.sg.common.IObject;
import com.sg.common.UtExpressionParser;
import com.sg.common.UtGifHelper;
import com.sg.common.UtExpressionParser.stIfElseExpression;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.sg.common.SgRealTimeData;

/** 根据信号可变 图片类(jpg png)  未实现 后期完善！
 * author fjw0312 
 * made date 2016 4 20
 *  */
public class Image_change extends View implements IObject {
	public Image_change(Context context) {  
        super(context); 
        this.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View view, MotionEvent event) {
	            switch (event.getAction())
	            {
		           
	            }
				return true;
			}
        });
        

        flag = System.currentTimeMillis()/1000;
        m_oPaint = new Paint();
    	m_rSrcRect = new Rect();
		m_rDestRect = new Rect();
		m_rBBox = new Rect();
	}
		
	public void threadInvalidate() {
		this.invalidate();
	}
	
	@SuppressLint("DrawAllocation")
	protected void onDraw(Canvas canvas) {
		if (m_bitCurrentBackImage == null)
			return;
		
		if (m_rRenderWindow == null)
			return;
		if (m_rRenderWindow.isLayoutVisible(getBBox()) == false)
			return;
		
		int nWidth = (int) (((float)(m_nWidth) / (float)MainWindow.FORM_WIDTH) * (m_rRenderWindow.VIEW_RIGHT - m_rRenderWindow.VIEW_LEFT));
		int nHeight = (int) (((float)(m_nHeight) / (float)MainWindow.FORM_HEIGHT) * (m_rRenderWindow.VIEW_BOTTOM - m_rRenderWindow.VIEW_TOP));

		m_rSrcRect.left = 0;
		m_rSrcRect.top = 0;
		m_rSrcRect.right = m_bitCurrentBackImage.getWidth();
		m_rSrcRect.bottom = m_bitCurrentBackImage.getHeight();
		
		m_rDestRect.left = 0;
		m_rDestRect.top = 0;
		m_rDestRect.right = nWidth;
		m_rDestRect.bottom = nHeight;
		
		canvas.rotate(m_fRotateAngle, nWidth/2, nHeight/2);
		canvas.drawBitmap(m_bitCurrentBackImage, m_rSrcRect, m_rDestRect, m_oPaint);

	}
	
	@Override
	public void doLayout(boolean bool, int l, int t, int r, int b) {
		if (m_rRenderWindow == null)
			return;
		int nX = l + (int) (((float)m_nPosX / (float)MainWindow.FORM_WIDTH) * (r-l));
		int nY = t + (int) (((float)m_nPosY / (float)MainWindow.FORM_HEIGHT) * (b-t));
		int nWidth = (int) (((float)(m_nWidth) / (float)MainWindow.FORM_WIDTH) * (r-l));
		int nHeight = (int) (((float)(m_nHeight) / (float)MainWindow.FORM_HEIGHT) * (b-t));

		//m_rRenderWindow.m_utPrint.drawText(250, 400, 0xffff0000, "ViewGroup大小="+(m_rRenderWindow.getRight()-m_rRenderWindow.getLeft())+":"+(m_rRenderWindow.getBottom()-m_rRenderWindow.getTop()));
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
		rWin.removeView(this);
	} 
	
	public void parseProperties(String strName, String strValue, String strResFolder) throws Exception {
		
		if ("ZIndex".equals(strName)) {
       	 	m_nZIndex = Integer.parseInt(strValue);
       	    if (MainWindow.MAXZINDEX < m_nZIndex) MainWindow.MAXZINDEX = m_nZIndex;
       	    path =  Environment.getExternalStorageDirectory().getPath() + strResFolder;
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
        else if ("RotateAngle".equals(strName)) {
       	 m_fRotateAngle = Float.parseFloat(strValue);
        }
        else if ("Strtch".equals(strName))
        	m_strStrtch = strValue;
        else if ("ImgSrc".equals(strName)) {
        	path =  Environment.getExternalStorageDirectory().getPath() + strResFolder;
        	m_strImgSrc = Environment.getExternalStorageDirectory().getPath() + strResFolder + strValue;
			String[] arrStr = strValue.split("\\.");
			if ("gif".equals(arrStr[1]))
			{
				Toast.makeText(getContext(), "图片gif格式无效！", Toast.LENGTH_SHORT).show();
			} else
			{
				m_bitBackImage = CFGTLS.getBitmapByPath(m_strImgSrc);
				m_bitCurrentBackImage = m_bitBackImage;
			}

        }
        else if ("ImageExpression".equals(strName)) {
    		m_strImageExpression = strValue;      	 		
        }
        else if ("Expression".equals(strName)) {
        	m_strExpression = strValue;
        }
        else if ("Url".equals(strName)) 
    		m_strUrl = strValue;
        else if ("user".equals(strName))
    		turnUsr = strValue;
        else if ("passWork".equals(strName))
    		turnPass = strValue;
	}

	@Override
	public void initFinished()
	{
	}

	public String getBindingExpression() {
		return m_strExpression;
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

		
		if(strValue.equals(old_strValue)){
			return false;
        }else{
      		old_strValue = strValue;
      		 //解析图片切换表达式 fjw add	        
	    	return true;
        }

	}

	
	public int parseImageExpression(String strValue)
	{


		return 1;
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
	String m_strType = "";  
    int m_nZIndex = 6;
	int m_nPosX = 787;
	int m_nPosY = 39;
	int m_nWidth = 200;
	int m_nHeight = 150;
	float m_fAlpha = 1.0f;
	float m_fRotateAngle = 0.0f;
	long base = 1467272645;
	String m_strStrtch = "Fill";
	String m_strImgSrc = "mobileLOGO.png";
	String m_strImageExpression = "&gt;1[2.jpg]&gt;2[3.jpg]&gt;3[4.jpg]";
	int m_nMaxHeightOrWidth = 0;
	int m_nMaxValue = 0;
	String m_strExpression = "";
	String m_strUrl = "";
    long flag = 0;
	Bitmap m_bitBackImage = null; 
	Bitmap m_bitCurrentBackImage = null;
//	Bitmap[] m_bitBackImages = new Bitmap[10]; //定义变化的图片数组
	UtGifHelper m_oGifHelper = null;
	boolean m_bPressed = false;
	MainWindow m_rRenderWindow = null;
	Handler m_oInvalidateHandler = null;
	Intent m_oHomeIntent = null;
	String turnUsr = "";       //页面跳转用户名
	String turnPass = "fang";  //页面跳转密码
	
	// 记录触摸坐标，过滤滑动操作。解决滑动误操作点击问题。
	public float m_xscal = 0;
	public float m_yscal = 0;
	// 
	//stMathExpression m_oMathExpression = null;
	stIfElseExpression m_oIfElseExpression = null;
	Bitmap m_bitIfTrueImage = null;
	Bitmap m_bitIfFalseImage = null;
	String m_strBindingValue = "";
	Paint m_oPaint = null;
	Rect m_rSrcRect = null;
	Rect m_rDestRect = null;
	Rect m_rBBox = null;
	String user = "admin";
	String pass = "12348765";
	String root = "fang";
	String rootpass = "pass";
	
	String path = "";
	String old_strValue="";
	
	public boolean m_bneedupdate = true;
		
}