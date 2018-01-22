package com.sg.uis;

import java.util.HashMap;
import java.util.Iterator;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mgrid.main.MGridActivity;
import com.mgrid.main.MainWindow;
import com.mgrid.util.XmlUtils;
import com.sg.common.CFGTLS;
import com.sg.common.IObject;
import com.sg.common.UtExpressionParser;
import com.sg.common.UtExpressionParser.stBindingExpression;
import com.sg.common.UtExpressionParser.stExpression;

@SuppressLint({ "RtlHardcoded", "ShowToast" })
@SuppressWarnings("unused")
/** 标签 */
public class SgAlarmChangTime extends TextView implements IObject {

	private EditText Et_ChangeValue = null;

	public SgAlarmChangTime(final Context context) {
		super(context);
		this.setClickable(true);
		this.setGravity(Gravity.CENTER);
		setBackgroundResource(android.R.drawable.btn_default);
		setPadding(0, 0, 0, 0);
		this.setTextSize(20);
		this.setText("修改");
		setTextColor(Color.BLACK);
		m_rBBox = new Rect();
		Et_ChangeValue = new EditText(context);
		Et_ChangeValue.setBackgroundResource(android.R.drawable.edit_text);
		Et_ChangeValue.setPadding(0, 0, 0, 0);
		Et_ChangeValue.setTextSize(20);
		Et_ChangeValue.setTextColor(Color.BLACK);
		Et_ChangeValue.setSingleLine();
		Et_ChangeValue.setInputType(EditorInfo.TYPE_CLASS_NUMBER);
		Et_ChangeValue.setGravity(Gravity.CENTER);
		imm = (InputMethodManager) context
				.getSystemService(Context.INPUT_METHOD_SERVICE);

		Et_ChangeValue.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				imm.showSoftInput(Et_ChangeValue,
						InputMethodManager.SHOW_FORCED);// 获取到这个类。
				Et_ChangeValue.setFocusableInTouchMode(true);// 获取焦点

			}
		});
		Et_ChangeValue.setCursorVisible(true);

		this.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String time=Et_ChangeValue.getText().toString();
				if(time.equals(""))
				{
					Toast.makeText(getContext(), "请输入时间", 1000).show();
					
				}else
				{
					
						SgAlarmAction sg=(SgAlarmAction) MGridActivity.AlarmAll.get(label);
						if(sg==null)
						{
							Toast.makeText(getContext(), "内容没有配置", 1000).show();
						}
						else
						{
							sg.Et_timeLapse.setText(time);
							sg.ClickSure();
							Toast.makeText(getContext(), "修改s成功", 1000).show();
						}
					
				}
				
				
			}
		});
	}

	@Override
	public void doLayout(boolean bool, int l, int t, int r, int b) {
		if (m_rRenderWindow == null)
			return;
		int nX = l
				+ (int) (((float) m_nPosX / (float) MainWindow.FORM_WIDTH) * (r - l));
		int nY = t
				+ (int) (((float) m_nPosY / (float) MainWindow.FORM_HEIGHT) * (b - t));
		int nWidth = (int) (((float) m_nWidth / (float) MainWindow.FORM_WIDTH) * (r - l));
		int nHeight = (int) (((float) m_nHeight / (float) MainWindow.FORM_HEIGHT) * (b - t));

		m_rBBox.left = nX;
		m_rBBox.top = nY;
		m_rBBox.right = nX + nWidth;
		m_rBBox.bottom = nY + nHeight;
		if (m_rRenderWindow.isLayoutVisible(m_rBBox)) {

			Et_ChangeValue.layout(nX, nY, nX + (int) (0.69 * nWidth), nY
					+ nHeight);
			layout(nX + (int) (0.7 * nWidth), nY, nX + nWidth, nY + nHeight);
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

	public int getZIndex() {
		return m_nZIndex;
	}

	@Override
	public void addToRenderWindow(MainWindow rWin) {
		m_rRenderWindow = rWin;
		rWin.addView(this);
		rWin.addView(Et_ChangeValue);
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
		} else if ("RotateAngle".equals(strName)) {
			m_fRotateAngle = Float.parseFloat(strValue);
		} else if ("Content".equals(strName)) {
			m_strContent = strValue;
			this.setText(m_strContent);
		} else if ("FontFamily".equals(strName))
			m_strFontFamily = strValue;
		else if ("FontSize".equals(strName)) {
			float fWinScale = (float) MainWindow.SCREEN_WIDTH
					/ (float) MainWindow.FORM_WIDTH;
			m_fFontSize = Float.parseFloat(strValue) * fWinScale;
			this.setTextSize(m_fFontSize);
		} else if ("IsBold".equals(strName))
			m_bIsBold = Boolean.parseBoolean(strValue);
		else if ("FontColor".equals(strName)) {
			m_cFontColor = Color.parseColor(strValue);
			m_cStartFillColor = m_cFontColor;
			this.setTextColor(m_cFontColor);
		} else if ("HorizontalContentAlignment".equals(strName))
			m_strHorizontalContentAlignment = strValue;
		else if ("VerticalContentAlignment".equals(strName))
			m_strVerticalContentAlignment = strValue;
		else if ("Expression".equals(strName))
			m_strExpression = strValue;
		else if ("Label".equals(strName)) {
			label = strValue; 
	        
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
			double padSize = CFGTLS.getPadHeight(m_nHeight,
					MainWindow.FORM_HEIGHT, getTextSize());
			setPadding(0, (int) padSize, 0, 0);
		} else if ("Center".equals(m_strVerticalContentAlignment)) {
			nFlag |= Gravity.CENTER_VERTICAL;
			double padSize = CFGTLS.getPadHeight(m_nHeight,
					MainWindow.FORM_HEIGHT, getTextSize()) / 2;
			setPadding(0, (int) padSize, 0, (int) padSize);
		}

		setGravity(nFlag);
	}

	public String getBindingExpression() {
		return m_strExpression;
	}

	public void updateWidget() {
		this.setTextColor(m_cFontColor);
		this.setText(m_strContent);

		this.invalidate();

	}
	
	public void updateText(String s)
	{
		Et_ChangeValue.setText(s);
	}

	@Override
	public boolean updateValue() {
		// 注意在updateValue里面不应该对view的属性经行操作，因为属性操作本来就会调用updateValue.
		// eg:setTextColor()
		m_bneedupdate = false;
		return false;
	}

	@Override
	public boolean needupdate() {
		return m_bneedupdate;
	}

	@Override
	public void needupdate(boolean bNeedUpdate) {
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
	int m_cStartFillColor = 0x00000000;
	String m_strHorizontalContentAlignment = "Center";
	String m_strVerticalContentAlignment = "Center";
	String m_strExpression = "Binding{[Value[Equip:114-Temp:173-Signal:1]]}";

	MainWindow m_rRenderWindow = null;
	String m_strSignalValue = "";

	Rect m_rBBox = null;
	InputMethodManager imm = null;
	


	public String label="";
	public boolean m_bneedupdate = true;

}
