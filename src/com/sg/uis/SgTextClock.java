package com.sg.uis;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.view.Gravity;
import android.view.View;
import android.widget.TextClock;

import com.mgrid.main.MainWindow;
import com.sg.common.CFGTLS;
import com.sg.common.IObject;
import com.sg.common.UtExpressionParser;
import com.sg.common.UtExpressionParser.stIfElseExpression;

public class SgTextClock extends TextClock implements IObject
{
	public SgTextClock(Context context)
	{
		super(context);
		this.setClickable(false);
		this.setBackgroundColor(0x00000000);
		m_rBBox = new Rect();
		
		setTextColor(Color.BLACK);
		setTextSize(25);
		
		// 设置属性值示例(1970/04/06 3:23am)
		// "MM/dd/yy h:mmaa" -> "04/06/70 3:23am"
		// "MMM dd, yyyy h:mmaa" -> "Apr 6, 1970 3:23am"
		// "MMMM dd, yyyy h:mmaa" -> "April 6, 1970 3:23am"
		// "E, MMMM dd, yyyy h:mmaa" -> "Mon, April 6, 1970 3:23am&
		// "EEEE, MMMM dd, yyyy h:mmaa" -> "Monday, April 6, 1970 3:23am"
		// "'Noteworthy day: 'M/d/yy" -> "Noteworthy day: 4/6/70"
		
        // 设置12时制显示格式
        //setFormat12Hour("EEEE, MMMM dd, yyyy h:mmaa");
		
        // 设置24时制显示格式
        setFormat24Hour("yyyy-MM-dd kk:mm  EEEE");
	}

	@Override
	public void doLayout(boolean bool, int l, int t, int r, int b) {
		if (m_rRenderWindow == null)
			return;
		int nX = l + (int) (((float)m_nPosX / (float)MainWindow.FORM_WIDTH) * (r-l));
		int nY = t + (int) (((float)m_nPosY / (float)MainWindow.FORM_HEIGHT) * (b-t));
		int nWidth = (int) (((float)m_nWidth / (float)MainWindow.FORM_WIDTH) * (r-l));
		int nHeight = (int) (((float)m_nHeight / (float)MainWindow.FORM_HEIGHT) * (b-t));

		m_rBBox.left = nX;
		m_rBBox.top = nY;
		m_rBBox.right = nX+nWidth;
		m_rBBox.bottom = nY+nHeight;
		if (m_rRenderWindow.isLayoutVisible(m_rBBox)) {
			layout(nX, nY, nX+nWidth, nY+nHeight);
		}
	}
	
	public void onDraw(Canvas canvas) {
		if (m_rRenderWindow == null)
			return;
		if (m_rRenderWindow.isLayoutVisible(getBBox()) == false)
			return;
		
		super.onDraw(canvas);
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
        }
        else if ("RotateAngle".equals(strName)) {
        	m_fRotateAngle = Float.parseFloat(strValue);
        }
        else if ("Content".equals(strName)) {
        	m_strContent = strValue;
        	//this.setText(m_strContent);
        }
        else if ("FontFamily".equals(strName))
        	m_strFontFamily = strValue;
        else if ("FontSize".equals(strName)) {
        	float fWinScale = (float)MainWindow.SCREEN_WIDTH / (float)MainWindow.FORM_WIDTH;
        	m_fFontSize = Float.parseFloat(strValue)*fWinScale;
    		this.setTextSize(m_fFontSize);
        }
        else if ("IsBold".equals(strName))
       	 	m_bIsBold = Boolean.parseBoolean(strValue);
        else if ("FontColor".equals(strName)) {
       	 	m_cFontColor = Color.parseColor(strValue);
       	 	this.setTextColor(m_cFontColor);
        }
        else if ("HorizontalContentAlignment".equals(strName))
       	 	m_strHorizontalContentAlignment = strValue;
        else if ("VerticalContentAlignment".equals(strName))
       	 	m_strVerticalContentAlignment = strValue;
        else if ("Expression".equals(strName)) {
       	 	m_strExpression = strValue;
       	 	m_oIfElseExpression = UtExpressionParser.getInstance().parseIfElseExpression(strValue);
        }
        else if ("DateTimeFormat".equals(strName))
        {
        	m_strDateTimeFormat = strValue;
        	setFormat24Hour(m_strDateTimeFormat);
        }
	}

	@Override
	public void initFinished()
	{
		int nFlag = Gravity.NO_GRAVITY;
		if ("Left".equals(m_strHorizontalContentAlignment))
			nFlag |= Gravity.LEFT;
		else if ("Right".equals(m_strHorizontalContentAlignment))
			nFlag |= Gravity.RIGHT;
		else if ("Center".equals(m_strHorizontalContentAlignment))
			nFlag |= Gravity.CENTER_HORIZONTAL;
		
		if ("Top".equals(m_strVerticalContentAlignment))
			nFlag |= Gravity.TOP;
		else if ("Bottom".equals(m_strVerticalContentAlignment))
		{
			nFlag |= Gravity.BOTTOM;
			double padSize = CFGTLS.getPadHeight(m_nHeight, MainWindow.FORM_HEIGHT, getTextSize());
			setPadding(0, (int) padSize, 0, 0);
		}
		else if ("Center".equals(m_strVerticalContentAlignment))
		{
			nFlag |= Gravity.CENTER_VERTICAL;
			double padSize = CFGTLS.getPadHeight(m_nHeight, MainWindow.FORM_HEIGHT, getTextSize())/2;
			setPadding(0, (int) padSize, 0, (int) padSize);
		}
		
		setGravity(nFlag);
	}

	public String getBindingExpression() {
		return m_strExpression;
	}
	
	public void updateWidget() {
		//this.setText(m_strContent);
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
	String m_strType = "Label";
    int m_nZIndex = 1;
	int m_nPosX = 49;
	int m_nPosY = 306;
	int m_nWidth = 60;
	int m_nHeight = 30;
	float m_fAlpha = 1.0f;
	float m_fRotateAngle = 0.0f;
	String m_strContent = "设置内容";
	String m_strFontFamily = "微软雅黑";
	float m_fFontSize = 12.0f;
	boolean m_bIsBold = false;
	int m_cFontColor = 0xFF008000;
	String m_strHorizontalContentAlignment = "Left";
	String m_strVerticalContentAlignment = "Center";
	String m_strExpression = "Binding{[Value[Equip:114-Temp:173-Signal:1]]}";
	private String m_strDateTimeFormat;
	
	MainWindow m_rRenderWindow = null;	
	stIfElseExpression m_oIfElseExpression = null;
	String m_strSignalValue = "";
	
	Rect m_rBBox = null;
	
	public boolean m_bneedupdate = true;
}
