package com.sg.uis.newView;

import com.mgrid.main.MainWindow;
import com.sg.common.CFGTLS;
import com.sg.common.IObject;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

public class DoorControlView extends TextView implements IObject {

	public DoorControlView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	private void init() {
		m_rBBox = new Rect();

	}

	

	

	
	@Override
	public void doLayout(boolean bool, int l, int t, int r, int b) {

		if (m_rRenderWindow == null)
			return;
		int nX = l + (int) (((float) m_nPosX / (float) MainWindow.FORM_WIDTH) * (r - l));
		int nY = t + (int) (((float) m_nPosY / (float) MainWindow.FORM_HEIGHT) * (b - t));
		int nWidth = (int) (((float) m_nWidth / (float) MainWindow.FORM_WIDTH) * (r - l));
		int nHeight = (int) (((float) m_nHeight / (float) MainWindow.FORM_HEIGHT) * (b - t));

		m_rBBox.left = nX;
		m_rBBox.top = nY;
		m_rBBox.right = nX + nWidth;
		m_rBBox.bottom = nY + nHeight;
		if (m_rRenderWindow.isLayoutVisible(m_rBBox)) {

		}

	}

	@Override
	protected void onDraw(Canvas canvas) {

		if (m_rRenderWindow == null)
			return;
		if (m_rRenderWindow.isLayoutVisible(getBBox()) == false)
			return;

		super.onDraw(canvas);
		// »­±ß¿ò
		Paint p = new Paint();
		p.setColor(Color.parseColor("#000000"));
		p.setStyle(Paint.Style.STROKE);
		canvas.drawRect(0, 0, m_rBBox.right - m_rBBox.left, m_rBBox.bottom - m_rBBox.top, p);

		
	}

	@Override
	public void setUniqueID(String strID) {
		m_strID = strID;
	}

	@Override
	public String getUniqueID() {

		return m_strID;
	}

	@Override
	public void setType(String strType) {
		m_strType = strType;

	}

	@Override
	public String getType() {

		return m_strType;
	}

	@Override
	public void parseProperties(String strName, String strValue, String strResFolder) throws Exception {
		if ("ZIndex".equals(strName)) {
			m_nZIndex = Integer.parseInt(strValue);
			if (MainWindow.MAXZINDEX < m_nZIndex)
				MainWindow.MAXZINDEX = m_nZIndex;
		} else if ("Location".equals(strName)) {
			String[] arrStr = strValue.split(",");
			m_nPosX = Integer.parseInt(arrStr[0]);
			m_nPosY = Integer.parseInt(arrStr[1]);
		} else if ("Size".equals(strName)) {
			String[] arrSize = strValue.split(",");
			m_nWidth = Integer.parseInt(arrSize[0]);
			m_nHeight = Integer.parseInt(arrSize[1]);
		} else if ("Alpha".equals(strName)) {
			m_fAlpha = Float.parseFloat(strValue);
		} else if ("RotateAngle".equals(strName)) {
			m_fRotateAngle = Float.parseFloat(strValue);
		} else if ("Content".equals(strName)) {
			m_strContent = strValue;
			c_Content = strValue;
			// this.setText(m_strContent);
			// shimmerTv.setText(m_strContent);
		} else if ("FontFamily".equals(strName)) {
			m_strFontFamily = strValue;
			// this.setTypeface(MyApplication.typeface);
		} else if ("FontSize".equals(strName)) {
			float fWinScale = (float) MainWindow.SCREEN_WIDTH / (float) MainWindow.FORM_WIDTH;
			m_fFontSize = Float.parseFloat(strValue) * fWinScale;
			// this.setTextSize(m_fFontSize);
			// shimmerTv.setTextSize(m_fFontSize);
		} else if ("IsBold".equals(strName))
			m_bIsBold = Boolean.parseBoolean(strValue);
		else if ("FontColor".equals(strName)) {
			m_cFontColor = Color.parseColor(strValue);
			m_cStartFillColor = m_cFontColor;
			// this.setTextColor(m_cFontColor);
			// shimmerTv.setTextColor(m_cFontColor);
		} else if ("HorizontalContentAlignment".equals(strName))
			m_strHorizontalContentAlignment = strValue;
		else if ("VerticalContentAlignment".equals(strName)) {
			m_strVerticalContentAlignment = strValue;
		} else if ("CmdExpression".equals(strName)) {
			m_strCmdExpression = strValue;

		}

	}

	@Override
	public void initFinished() {
		int nFlag = Gravity.NO_GRAVITY;
		if ("Left".equals(m_strHorizontalContentAlignment))
			nFlag |= Gravity.LEFT;
		else if ("Right".equals(m_strHorizontalContentAlignment))
			nFlag |= Gravity.RIGHT;
		else if ("Center".equals(m_strHorizontalContentAlignment))
			nFlag |= Gravity.CENTER_HORIZONTAL;

		if ("Top".equals(m_strVerticalContentAlignment))
			nFlag |= Gravity.TOP;
		else if ("Bottom".equals(m_strVerticalContentAlignment)) {
			nFlag |= Gravity.BOTTOM;
			double padSize = CFGTLS.getPadHeight(m_nHeight, MainWindow.FORM_HEIGHT, getTextSize());
			setPadding(0, (int) padSize, 0, 0);
		} else if ("Center".equals(m_strVerticalContentAlignment)) {
			nFlag |= Gravity.CENTER_VERTICAL;
			double padSize = CFGTLS.getPadHeight(m_nHeight, MainWindow.FORM_HEIGHT, getTextSize()) / 2;
			setPadding(0, (int) padSize, 0, (int) padSize);
		}

		setGravity(nFlag);

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

	@Override
	public void updateWidget() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean updateValue() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean needupdate() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void needupdate(boolean bNeedUpdate) {
		// TODO Auto-generated method stub

	}

	@Override
	public String getBindingExpression() {
		// TODO Auto-generated method stub
		return m_strCmdExpression;
	}

	@Override
	public View getView() {
		// TODO Auto-generated method stub
		return this;
	}

	@Override
	public int getZIndex() {
		// TODO Auto-generated method stub
		return m_nZIndex;
	}

	public Rect getBBox() {
		return m_rBBox;
	}  

	String m_strID = "";
	String m_strType = "Label";
	int m_nZIndex = 1;
	int m_nPosX = 49;
	int m_nPosY = 306;
	int m_nWidth = 60;
	int m_nHeight = 30;
	float m_fAlpha = 1.0f;
	float m_fRotateAngle = 0.0f;
	String m_strContent = "ÉèÖÃÄÚÈÝ";
	String m_strFontFamily = "Î¢ÈíÑÅºÚ";
	String c_Content = "";
	float m_fFontSize = 12.0f;
	boolean m_bIsBold = false;
	int m_cFontColor = 0xFF008000;
	int m_cStartFillColor = 0x00000000;
	String m_strHorizontalContentAlignment = "Center";
	String m_strVerticalContentAlignment = "Center";
	String m_strCmdExpression = "Binding{[Cmd[Equip:1-Temp:173-Command:1-Parameter:1-Value:1]]}";

	MainWindow m_rRenderWindow = null;

	Rect m_rBBox = null;

	public boolean m_bneedupdate = true;

}
