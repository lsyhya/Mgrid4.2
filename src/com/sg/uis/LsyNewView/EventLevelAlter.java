package com.sg.uis.LsyNewView;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mgrid.main.MainWindow;
import com.mgrid.util.XmlUtils;
import com.sg.common.CFGTLS;
import com.sg.common.IObject;

/** 告警等级修改 */
@SuppressLint({ "ShowToast", "InflateParams", "RtlHardcoded",
		"ClickableViewAccessibility" })
public class EventLevelAlter extends TextView implements IObject {

	EditText editText = null;
	InputMethodManager imm = null;

	public EventLevelAlter(Context context) {
		super(context);

		setClickable(true);
		setGravity(Gravity.CENTER);
		setBackgroundResource(android.R.drawable.btn_default);
		setTextSize(20);
		setTextColor(Color.BLACK);
		// setText("修改");

		editText = new EditText(context);
		editText.setBackgroundResource(android.R.drawable.edit_text);
		editText.setPadding(0, 5, 0, 0);
		editText.setTextSize(20);
		editText.setTextColor(Color.BLACK);
		editText.setGravity(Gravity.CENTER);
		editText.setInputType(InputType.TYPE_CLASS_NUMBER);

		m_oPaint = new Paint();
		m_rBBox = new Rect();

		imm = (InputMethodManager) context
				.getSystemService(Context.INPUT_METHOD_SERVICE);

		editText.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				imm.showSoftInput(editText, InputMethodManager.SHOW_FORCED);// 获取到这个类。
				editText.setFocusableInTouchMode(true);// 获取焦点

			}
		});
		editText.setCursorVisible(true);
		this.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				String txt = editText.getText().toString();
				if (txt.equals("")) {
					Toast.makeText(getContext(), "不能为空", 500).show();
					return;
				}
				if (EquipEvent == null)
					EquipEvent = xml.getNodeList("EquipEvent", tempId);

				if (EventCondition == null) {
					for (int i = 0; i < EquipEvent.getLength(); i++) {
						Element element = (Element) EquipEvent.item(i);
						String EventId = element.getAttribute("EventId");
						if (EventId.equals(evendId)) {
							NodeList Conditions = element.getChildNodes();
							for (int j = 0; j < Conditions.getLength(); j++) {
								NodeList n = Conditions.item(j).getChildNodes();
								for (int k = 0; k < n.getLength(); k++) {
									if (n.item(k).getNodeName()
											.equals("EventCondition")) {
										EventCondition = (Element) n.item(k);
										EventCondition.setAttribute(
												"EventSeverity", editText
														.getText().toString());
										Toast.makeText(getContext(), "修改成功",
												500).show();
										break;
									}

								}

							}
							break;
						}
					}
				} else {
					EventCondition.setAttribute("EventSeverity", editText
							.getText().toString());
					Toast.makeText(getContext(), "修改成功", 500).show();

				}
				xml.saveXmlData();

			}
		});

	}

	@SuppressLint("DrawAllocation")
	protected void onDraw(Canvas canvas) {
		if (m_rRenderWindow == null)
			return;
		if (m_rRenderWindow.isLayoutVisible(getBBox()) == false)
			return;
		super.onDraw(canvas);

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

			editText.layout(nX, nY, (int) (0.69 * nWidth + nX), nY + nHeight);
			this.layout((int) (0.7 * nWidth + nX), nY, nWidth + nX, nY
					+ nHeight);

		}
	}

	@Override
	public void addToRenderWindow(MainWindow rWin) {
		m_rRenderWindow = rWin;
		rWin.addView(editText);
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
			this.setTextSize(m_fFontSize);
		} else if ("IsBold".equals(strName))
			m_bIsBold = Boolean.parseBoolean(strValue);
		else if ("FontColor".equals(strName)) {
			m_cFontColor = Color.parseColor(strValue);
			this.setTextColor(m_cFontColor);
		} else if ("ClickEvent".equals(strName))
			m_strClickEvent = strValue;
		else if ("Url".equals(strName))
			m_strUrl = strValue;
		else if ("CmdExpression".equals(strName))
			m_strCmdExpression = strValue;

		else if ("HorizontalContentAlignment".equals(strName))
			m_strHorizontalContentAlignment = strValue;
		else if ("VerticalContentAlignment".equals(strName))
			m_strVerticalContentAlignment = strValue;
		else if ("Expression".equals(strName)) {

			mExpression = strValue;
			parse(mExpression);

		}
	}

	private void parse(String mExpression) {
		if (mExpression == null || mExpression.equals(""))
			return;

		mExpression = mExpression.replace("Binding{[Value[", "");
		mExpression = mExpression.replace("]]}", "");
		String[] s = mExpression.split("-");
		String[] s1 = s[1].split(":");
		tempId = s1[1];
		String[] s2 = s[2].split(":");
		evendId = s2[1];
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
		return mExpression;
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

		return true;
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
	private String mExpression = "Binding{[Value[Equip:114-Temp:173-Signal:1]]}";

	MainWindow m_rRenderWindow = null;

	Paint m_oPaint = null;
	Rect m_rBBox = null;

	private String tempId = "175";
	private String evendId = "1";
	private XmlUtils xml = XmlUtils.getXml();
	private NodeList EquipEvent = null;
	private Element EventCondition = null;
	public boolean m_bneedupdate = true;

}
