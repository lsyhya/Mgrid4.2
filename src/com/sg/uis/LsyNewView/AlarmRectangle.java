package com.sg.uis.LsyNewView;

import java.util.Hashtable;
import java.util.Iterator;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;

import com.mgrid.data.DataGetter;
import com.mgrid.data.EquipmentDataModel.Event;
import com.mgrid.main.MainWindow;
import com.sg.common.IObject;
import com.sg.common.SgRealTimeData;

/** 告警判断的方块 */
public class AlarmRectangle extends View implements IObject {
	public AlarmRectangle(Context context) {  
        super(context);
        this.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				return true;
			}
        });
        m_oPaint = new Paint(); 
        m_rBBox = new Rect();
    }
	
	@SuppressLint("DrawAllocation")
	protected void onDraw(Canvas canvas) {
		if (m_rRenderWindow == null)
			return;
		if (m_rRenderWindow.isLayoutVisible(getBBox()) == false)
			return;
		
		m_oPaint.setColor(currentColor);
		m_oPaint.setAntiAlias(false); // 设置画笔的锯齿效果
		m_oPaint.setStrokeWidth(m_nBorderWidth);
		m_oPaint.setStyle(Paint.Style.STROKE);
        
        int nWidth = (int) (((float)(m_nWidth) / (float)MainWindow.FORM_WIDTH) * (m_rRenderWindow.VIEW_RIGHT - m_rRenderWindow.VIEW_LEFT));
		int nHeight = (int) (((float)(m_nHeight) / (float)MainWindow.FORM_HEIGHT) * (m_rRenderWindow.VIEW_BOTTOM - m_rRenderWindow.VIEW_TOP));

        canvas.drawRect(m_nBorderWidth, m_nBorderWidth, nWidth-m_nBorderWidth, nHeight-m_nBorderWidth, m_oPaint);   
        
        // 0,0.4,#FFC0C0C0,0,#FF585858,0.5,#FFC0C0C0,1
        // 渐变颜色和渐变点
//        if (m_arrGradientColorPos != null) {
//		    LinearGradient lg = null;
//		    if (m_bIsHGradient) {
//		        lg = new LinearGradient(0, nHeight/2, nWidth, nHeight/2, m_arrGradientFillColor, 
//		        		m_arrGradientColorPos, TileMode.MIRROR);
//		    }
//		    else {
//		        lg = new LinearGradient(nWidth/2, 0, nWidth/2, nHeight, m_arrGradientFillColor, 
//		        		m_arrGradientColorPos, TileMode.MIRROR);      	
//		    }
//		    m_oPaint.setShader(lg);
//        }
//        else
        m_oPaint.setColor(currentColor); // 仅填充单色
        m_oPaint.setStyle(Paint.Style.FILL);  
        canvas.drawRect(0, 0, nWidth, nHeight, m_oPaint);
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
        else if ("RotateAngle".equals(strName)) {
       	 	m_fRotateAngle = Float.parseFloat(strValue);
        }
        else if ("BorderWidth".equals(strName))
        	m_nBorderWidth = Integer.parseInt(strValue);
        else if ("normalColor".equals(strName)) {
        	normalColor=Color.parseColor(strValue);
        	currentColor=normalColor;
        }
        else if ("alarmColor".equals(strName)) {
        	alarmColor=Color.parseColor(strValue);
        }
        else if ("Expression".equals(strName)) 
        {
       	 	m_strExpression = strValue;
		    parse(m_strExpression);
        }
      
	}

	private void parse(String mExpression) {
		if (mExpression == null || mExpression.equals(""))
			return;

		mExpression = mExpression.replace("Binding{[Value[", "");
		mExpression = mExpression.replace("]]}", "");
		String[] s = mExpression.split("-");
		String[] s0 = s[0].split(":");
		equitId = s0[1];
		String[] s1 = s[1].split(":");
		tempId = s1[1];
		String[] s2 = s[2].split(":");
		eventId = s2[1];
		
	}

	@Override
	public void initFinished()
	{
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
		//m_bneedupdate = false;
		Hashtable<String, Hashtable<String, Event>> listEvents = DataGetter
				.getRTEventList();
		if (listEvents == null || listEvents.size() == 0
				||equitId==null||equitId.equals("")
				||eventId==null||eventId.equals(""))
			return false;
		Iterator<Hashtable.Entry<String, Hashtable<String, Event>>> equaip_it = listEvents
				.entrySet().iterator();
		while (equaip_it.hasNext()) {
			Hashtable.Entry<String, Hashtable<String, Event>> entry = equaip_it
					.next();
			String id = entry.getKey();
            if(!id.equals(equitId)) continue;
			Iterator<Hashtable.Entry<String, Event>> it = entry.getValue()
					.entrySet().iterator();
			while (it.hasNext()) {
				Hashtable.Entry<String, Event> event_entry = it.next();
				String eid=event_entry.getKey();
				if(eid.equals(eventId)){ 
				
					currentColor=alarmColor;
					return true;
				}
			}
		}
		currentColor=normalColor;
		return true;
     
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
    int m_nZIndex = 3;
	int m_nPosX = 94;
	int m_nPosY = 9;
	int m_nWidth = 200;
	int m_nHeight = 150;
	float m_fAlpha = 1.0f;
	float m_fRotateAngle = 0.0f;
	
	int m_nBorderWidth = 3;
	
	float[] m_arrGradientColorPos = null;
	int[] m_arrGradientFillColor = null;
	
	
	

	String m_strExpression = "Binding{[Value[Equip:114-Temp:173-Signal:1]]}";
	boolean m_bIsHGradient = true; // 水平渐变
	MainWindow m_rRenderWindow = null;

	private String equitId,tempId,eventId;
	
	int normalColor = 0xFF000000;//正常颜色
	int alarmColor = 0xFF000000;//告警颜色
	int currentColor= 0xFF000000;//当前颜色
	
	Paint m_oPaint = null;  
	Rect m_rBBox = null;
	
	public boolean m_bneedupdate = true;
}
