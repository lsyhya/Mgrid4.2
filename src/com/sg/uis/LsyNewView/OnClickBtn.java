package com.sg.uis.LsyNewView;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.mgrid.main.MainWindow;
import com.sg.common.CFGTLS;
import com.sg.common.IObject;

/** 按钮 */
public class OnClickBtn extends TextView implements IObject {

	
	private Button button ;
	private WindowManager wm;
	private int H ,W;
	private List<MyRect> list=new ArrayList<MyRect>();
	
	public OnClickBtn(Context context) {
		super(context);
		this.setClickable(true);
		this.setGravity(Gravity.CENTER);

//		button=new Button(context);
//		button.setBackgroundResource(android.R.drawable.btn_default);
//		button.setPadding(0, 0, 0, 0);
		
		
		m_oPaint = new Paint();
		m_rBBox = new Rect();
		
		setPadding(0, 0, 0, 0);
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j <6; j++) {
				list.add(new MyRect(new Rect(i*(1024/5), j*(768/6), i*(1024/5)+(1024/5), j*(768/6)+(768/6))));
			}
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		
		int index=event.getPointerCount();
		if(index>=2)
		{
			m_rRenderWindow.m_oMgridActivity.showTaskUI(true);
			return false;
		}
		
		int X=(int)event.getX();
		int Y=(int)event.getY();
		
		
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:

			break;
		case MotionEvent.ACTION_MOVE:

			break;
		case MotionEvent.ACTION_UP:

			for (MyRect rect : list) {
				if(rect.rect.contains(X, Y))
				{
					rect.isTure=true;
				}
			}
			invalidate();
				
			break;
		}

		return super.onTouchEvent(event);
	}

	@SuppressLint("DrawAllocation")
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if (m_rRenderWindow == null)
			return;
		if (m_rRenderWindow.isLayoutVisible(getBBox()) == false)
			return;

	
			int nWidth = (int) (((float) (m_nWidth) / (float) MainWindow.FORM_WIDTH) * (m_rRenderWindow.VIEW_RIGHT - m_rRenderWindow.VIEW_LEFT));
			int nHeight = (int) (((float) (m_nHeight) / (float) MainWindow.FORM_HEIGHT) * (m_rRenderWindow.VIEW_BOTTOM - m_rRenderWindow.VIEW_TOP));

			m_oPaint.setColor(Color.BLACK);
			m_oPaint.setStyle(Paint.Style.FILL);
			
			for (int i = 1; i < 5; i++) {
				canvas.drawLine(i*(1024/5), 0, i*(1024/5), 768, m_oPaint);
			}
			
			for (int i = 1; i < 6; i++) {
				canvas.drawLine(0, i*(768/6), 1024, i*(768/6), m_oPaint);
			}
			m_oPaint.setColor(Color.BLUE);
			for (int i = 0; i < list.size(); i++) {
				if(list.get(i).isTure)
				{
					canvas.drawRect(list.get(i).rect,m_oPaint);
				}
			}

	
	}

	@Override
	public void doLayout(boolean bool, int l, int t, int r, int b) {
		if (m_rRenderWindow == null)
			return;
		int nX = l
				+ (int) (((float) m_nPosX / (float) MainWindow.FORM_WIDTH) * (r - l));
		int nY = t
				+ (int) (((float) m_nPosY / (float) MainWindow.FORM_HEIGHT) * (b - t));
		int nWidth = (int) (((float) (m_nWidth) / (float) MainWindow.FORM_WIDTH) * (r - l));
		int nHeight = (int) (((float) (m_nHeight) / (float) MainWindow.FORM_HEIGHT) * (b - t));

		m_rBBox.left = nX;
		m_rBBox.top = nY;
		m_rBBox.right = nX + nWidth;
		m_rBBox.bottom = nY + nHeight;
		if (m_rRenderWindow.isLayoutVisible(m_rBBox)) {
		//	button.layout(nX, nY, nX + nWidth, nY + nHeight);
			layout(0, 0, 1028, 768);
		}
	}

	@Override
	public void addToRenderWindow(MainWindow rWin) {
		m_rRenderWindow = rWin;
		wm=m_rRenderWindow.m_oMgridActivity.getWindowManager();
		H=wm.getDefaultDisplay().getHeight();
		W=wm.getDefaultDisplay().getWidth();
	
		rWin.addView(this);
	}

	@Override
	public void removeFromRenderWindow(MainWindow rWin) {
		rWin.removeView(this);
	}

	public void parseProperties(String strName, String strValue,
			String strResFolder) {
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
		} else if ("BackgroundColor".equals(strName)) {
			if (strValue.isEmpty())
				return;
			m_cBackgroundColor = Color.parseColor(strValue);
		} else if ("Content".equals(strName)) {
			m_strContent = strValue;
			this.setText(m_strContent);
		} else if ("FontFamily".equals(strName))
			m_strFontFamily = strValue;
		else if ("FontSize".equals(strName)) {
			float fWinScale = (float) MainWindow.SCREEN_WIDTH
					/ (float) MainWindow.FORM_WIDTH;
			m_fFontSize = Float.parseFloat(strValue) * fWinScale;
			this.setTextSize(Float.parseFloat(strValue));
		} else if ("IsBold".equals(strName))
			m_bIsBold = Boolean.parseBoolean(strValue);
		else if ("FontColor".equals(strName)) {
			m_cFontColor = Color.parseColor(strValue);
			this.setTextColor(m_cFontColor);
		} else if ("HorizontalContentAlignment".equals(strName))
			m_strHorizontalContentAlignment = strValue;
		else if ("VerticalContentAlignment".equals(strName))
			m_strVerticalContentAlignment = strValue;

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
			double padSize = CFGTLS.getPadHeight(m_nHeight,
					MainWindow.FORM_HEIGHT, getTextSize());
			setPadding(0, (int) padSize, 0, 0);
		} else if ("Center".equals(m_strVerticalContentAlignment)) {
			nFlag |= Gravity.CENTER_VERTICAL;
			double padSize = CFGTLS.getPadHeight(m_nHeight,
					MainWindow.FORM_HEIGHT, getTextSize()) / 2f;
			setPadding(0, (int) padSize, 0, (int) padSize);
		}

		setGravity(nFlag);
	}

	public String getBindingExpression() {
		return m_strCmdExpression;
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
	public boolean updateValue() {
		return false;
	}

	@Override
	public boolean needupdate() {
		return false;
	}

	@Override
	public void needupdate(boolean bNeedUpdate) {
	}

	public View getView() {
		return this;
	}

	public int getZIndex() {
		return m_nZIndex;
	}

	public Rect getBBox() {
		return m_rBBox;
	}
	
	
	public class MyRect
	{
		public Rect rect;
		public boolean isTure=false;
		public MyRect(Rect rect)
		{
			this.rect=rect;
		}
	}

	// params:
	String m_strID = "";
	String m_strType = "";
	int m_nZIndex = 7;
	int m_nPosX = 152;
	int m_nPosY = 287;
	int m_nWidth = 75;
	int m_nHeight = 23;
	float m_fAlpha = 1.0f;
	int m_cBackgroundColor = 0xF00CF00C;
	String m_strContent = "按钮";
	String m_strFontFamily = "微软雅黑";
	float m_fFontSize = 12.0f;
	boolean m_bIsBold = false;
	int m_cFontColor = 0xFF008000;
	String m_strClickEvent = "科士达-IDU系统设定UPS.xml";
	String m_strUrl = "www.baidu.com";
	String m_strCmdExpression = "Binding{[Cmd[Equip:1-Temp:173-Command:1-Parameter:1-Value:1]]}";
	String m_strHorizontalContentAlignment = "Center";
	String m_strVerticalContentAlignment = "Center";
	boolean m_bPressed = false;
	MainWindow m_rRenderWindow = null;
	String cmd_value = "";

	Paint m_oPaint = null;
	Rect m_rBBox = null;
	public static ProgressDialog dialog;

	// 记录触摸坐标，过滤滑动操作。解决滑动误操作点击问题。
	public float m_xscal = 0;
	public float m_yscal = 0;

	Intent m_oHomeIntent = null;

	String isDelete = "失败";


}


