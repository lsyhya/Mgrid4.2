package com.sg.uis;

import com.mgrid.main.MainWindow;
import com.sg.common.CFGTLS;
import com.sg.common.IObject;

import android.R;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

/** 命令按钮 */
public class SgCommandButton extends TextView implements IObject {
	public SgCommandButton(Context context) {
		super(context); 
		this.setClickable(true);
		this.setGravity(Gravity.CENTER);
		this.setBackgroundColor(Color.GRAY);
		m_fFontSize = this.getTextSize();
        this.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View view, MotionEvent event) {
	            switch (event.getAction())
	            {
		            case MotionEvent.ACTION_DOWN:
		            	m_bPressed = true;	
		            	view.invalidate();
		            	
		            	m_xscal = event.getX();
		            	m_yscal = event.getY();
		            break;
		            
		            case MotionEvent.ACTION_UP:
		            	m_bPressed = false;	
		            	view.invalidate();
		            	
		            	float xslip = Math.abs(event.getX()-m_xscal);
		            	float yslip = Math.abs(event.getY()-m_yscal);
		            	
		            	if ( xslip < 3  && yslip < 3)
		            	onClicked();
		            break;
		            default: break;
	            }
				return true;
			}
        });
        m_oPaint = new Paint();
        m_rBBox = new Rect();
        
        //setBackgroundResource(com.mgrid.main.R.drawable.sg_button_up);
        setBackgroundResource(R.drawable.btn_default);
        setPadding(0, 0, 0, 0);
	}
	
	public void setSliderText(int nValue) {
		this.setText(nValue+"");
	}
	
	@SuppressLint("DrawAllocation")
	protected void onDraw(Canvas canvas) {
		if (m_rRenderWindow == null)
			return;
		if (m_rRenderWindow.isLayoutVisible(getBBox()) == false)
			return;

		if (m_bPressed) {
			int nWidth = (int) (((float)(m_nWidth) / (float)MainWindow.FORM_WIDTH) * (m_rRenderWindow.VIEW_RIGHT - m_rRenderWindow.VIEW_LEFT));
			int nHeight = (int) (((float)(m_nHeight) / (float)MainWindow.FORM_HEIGHT) * (m_rRenderWindow.VIEW_BOTTOM - m_rRenderWindow.VIEW_TOP));

			m_oPaint.setColor(0x50FF00F0);
			m_oPaint.setStyle(Paint.Style.FILL); 
			canvas.drawRect(0,0,nWidth,nHeight, m_oPaint);
		}
		super.onDraw(canvas);
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
			this.layout(nX+(int)(nWidth*0.65f), nY, nX+nWidth, nY+nHeight);
		}
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
	       	 m_nWidth = m_nWidth < 60 ? 60 : m_nWidth;
	       	 m_nHeight = m_nHeight < 40 ? 40 : m_nHeight;
        }
        else if ("Alpha".equals(strName)) {
       	 	m_fAlpha = Float.parseFloat(strValue);
        }
        else if ("Content".equals(strName)) {
	       	 m_strContent = strValue;
	       	 this.setText(m_strContent);
        }
        else if ("FontFamily".equals(strName))
       	 	m_strFontFamily = strValue;
        else if ("IsBold".equals(strName))
       	 	m_bIsBold = Boolean.parseBoolean(strValue);
        else if ("FontColor".equals(strName)) {
	       	 m_cFontColor = Color.parseColor(strValue);
	       	 this.setTextColor(m_cFontColor);
        }
        else if ("CmdExpression".equals(strName))
        	m_strCmdExpression = strValue;
		
        else if ("HorizontalContentAlignment".equals(strName))
       	 	m_strHorizontalContentAlignment = strValue;
        else if ("VerticalContentAlignment".equals(strName))
       	 	m_strVerticalContentAlignment = strValue;
        else if ("Expression".equals(strName)) {
        	m_strCmdExpression = strValue;
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
			setPadding(0, (int) padSize, 0, (int)padSize);
		}
		
		setGravity(nFlag);
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
	
	private void onClicked() {
		// 发送控制命令
		if ("".equals(m_strCmdExpression) == false) {
			synchronized(m_rRenderWindow.m_oShareObject) {
				m_rRenderWindow.m_oShareObject.m_mapCmdCommand.put(getUniqueID(), "");
			}
		}
	}
	
	public String getBindingExpression() {
		return m_strCmdExpression;
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
    int m_nZIndex = 10;
	int m_nPosX = 152;
	int m_nPosY = 287;
	int m_nWidth = 75;
	int m_nHeight = 23;
	float m_fAlpha = 1.0f;
	boolean m_bIsBold = false;
	String m_strFontFamily = "微软雅黑";
	int m_cFontColor = 0xFF000000;
	String m_strContent = "发送控制命令";
	String m_strCmdExpression = "Binding{[Cmd[Equip:113-Temp:168-Command:1]]}";
	String m_strHorizontalContentAlignment = "Center";
	String m_strVerticalContentAlignment = "Center";
	MainWindow m_rRenderWindow = null;
	float m_fFontSize = 6.0f;
	boolean m_bPressed = false;
	Paint m_oPaint = null;
	Rect m_rBBox = null;
	
	// 记录触摸坐标，过滤滑动操作。解决滑动误操作点击问题。
	public float m_xscal = 0;
	public float m_yscal = 0;
}
