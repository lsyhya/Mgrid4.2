package com.sg.uis.LsyNewView.ChangeAbleLabel;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.mgrid.data.DataGetter;
import com.mgrid.main.MGridActivity;
import com.mgrid.main.MainWindow;
import com.mgrid.util.ExpressionUtils;
import com.sg.common.CFGTLS;
import com.sg.common.IObject;
import com.sg.web.LableObject;
import com.sg.web.base.ViewObjectBase;
import com.sg.web.base.ViewObjectSetCallBack;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

public class ChangeableLabel extends TextView implements IObject, ViewObjectSetCallBack {

	public ViewObjectBase base = new LableObject();

	public ChangeableLabel(Context context) {
		super(context);

		init(this);

	}

	private void init(View view) {

		view.setClickable(true);
		view.setBackgroundColor(0x00000000);
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

			layout(nX, nY, nX + nWidth, nY + nHeight);

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
		m_rRenderWindow.viewList.add(base);
		
	}

	@Override
	public void removeFromRenderWindow(MainWindow rWin) {
		rWin.removeView(this);
	}

	public void parseProperties(String strName, String strValue, String strResFolder) {

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
			this.setText(m_strContent);

		} else if ("FontFamily".equals(strName)) {
			m_strFontFamily = strValue;

		} else if ("FontSize".equals(strName)) {
			float fWinScale = (float) MainWindow.SCREEN_WIDTH / (float) MainWindow.FORM_WIDTH;
			m_fFontSize = Float.parseFloat(strValue) * fWinScale;

			this.setTextSize(m_fFontSize);

		} else if ("IsBold".equals(strName))
			m_bIsBold = Boolean.parseBoolean(strValue);
		else if ("FontColor".equals(strName)) {
			currColor = strValue;
			m_cFontColor = Color.parseColor(strValue);
			m_cStartFillColor = m_cFontColor;

			this.setTextColor(m_cFontColor);

		} else if ("HorizontalContentAlignment".equals(strName))
			m_strHorizontalContentAlignment = strValue;
		else if ("VerticalContentAlignment".equals(strName))
			m_strVerticalContentAlignment = strValue;
		else if ("Expression".equals(strName)) {
			m_strExpression = strValue;
			parseStr(m_strExpression);
		} else if ("ColorExpression".equals(strName))
			m_strColorExpression = strValue; // 字体颜色变化表达式
		else if ("CmdExpression".equals(strName))
			m_cmdExpression = strValue;
		else if ("Type".equals(strName)) {
			type = strValue;
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

	public String getBindingExpression() {
		return m_strExpression;
	}

	public void updateWidget() {

		this.setTextColor(m_cFontColor);
		this.setText(m_strContent);
		this.invalidate();

	}

	
	@Override
	public boolean updateValue() {

		m_bneedupdate = false;
		List<Float> valueList = new ArrayList<Float>();

		for (String str : cmdList) {

			String[] sup = str.split("-");
			String equip = sup[0];
			String sig = sup[2];
			if (!isClose(equip, sig)) {
				String value = DataGetter.getSignalValue(equip, sig);
				valueList.add(Float.parseFloat(value));
			}
		}

		
		float value=0;
		
		if (type.equals("max")) {

			value = getMax(valueList);

		} else if (type.equals("min")) {

			value = getMin(valueList);

		} else {

			value = getAvg(valueList);

		}

		// 内容变化才刷新页面
		if (m_strSignalValue.equals(value+"") == false) {

			m_strSignalValue = value+""; // 保存数值留作下次比较

			m_strContent = m_strSignalValue; // 界面数值赋予

			return true;

		}
		return false;
	}

	private float getMax(List<Float> valueList) {

		
		float value=valueList.get(0); 
		
		for (float f : valueList) {
			
			if(f>value)
			{
				value=f;
			}
			
		}
				
		return value;
	}

	private float getMin(List<Float> valueList) {

        float value=valueList.get(0); 
		
		for (float f : valueList) {
			
			if(f<value)
			{
				value=f;
			}
			
		}
				
		return value;
	}

	private float getAvg(List<Float> valueList) {
		
		
		float value=0; 
		
	    for (float f : valueList) {
						
	    	value+=f;
	    	
		}
		

		return (int)(value/valueList.size());
	}

	private boolean isClose(String e, String s) {
		if (MGridActivity.LabelList.size() > 0 && MGridActivity.LabelList.contains(e)) {
			return true;
		}

		if (MGridActivity.EventClose.size() > 0) {

			Iterator<Entry<String, Map<String, String>>> iter = MGridActivity.EventClose.entrySet().iterator();
			while (iter.hasNext()) {
				Map.Entry<String, Map<String, String>> entry = (Map.Entry<String, Map<String, String>>) iter.next();
				Map<String, String> map = (Map<String, String>) entry.getValue();
				Iterator<Entry<String, String>> iter1 = map.entrySet().iterator();
				while (iter1.hasNext()) {
					Map.Entry<String, String> entry1 = (Map.Entry<String, String>) iter1.next();
					String eid = (String) entry1.getKey();
					String sid = (String) entry1.getValue();
					if (eid.equals(e) && sid.equals(s)) {

						return true;
					}
				}
			}

		}

		return false;
	}

	// 颜色解析函数 传入参数：显示值 fjw add
	public int parseFontcolor(String strValue) {
		m_cFontColor = m_cStartFillColor;
		if ((m_strColorExpression == null) || ("".equals(m_strColorExpression)))
			return -1;
		if ((strValue == null) || ("".equals(strValue)))
			return -1;
		if ("-999999".equals(strValue))
			return -1;

		// Log.e("Label-updataValue",
		// "into!"+"--"+m_strColorExpression.substring(0,1));
		if ((">".equals(m_strColorExpression.substring(0, 1))) != true)
			return -1;

		String buf[] = m_strColorExpression.split(">"); // 提取表达式中的条件与颜色单元
		for (int i = 1; i < buf.length; i++) {
			String a[] = buf[i].split("\\[|\\]"); // 处理分隔符[ ]
			// Log.e("Label-updataValue", "比较值"+a[0]+"+颜色数值："+a[1]);
			// 比较数值
			float data = Float.parseFloat(a[0]); // 获得比较值
			float value = Float.parseFloat(strValue); // 输入值
			if (value > data) {
				m_cFontColor = Color.parseColor(a[1]);
			}
		}
		return m_cFontColor;
	}

	private void parseStr(String str) {
		cmdList = ExpressionUtils.getExpressionUtils().parse(str);
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
		// base.setTypeId(m_strID);
	}

	public void setType(String strType) {
		m_strType = strType;
		// base.setType(m_strType);
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
	String c_Content = "";
	float m_fFontSize = 12.0f;
	boolean m_bIsBold = false;
	int m_cFontColor = 0xFF008000;
	int m_cStartFillColor = 0x00000000;
	String currColor;
	String m_strHorizontalContentAlignment = "Center";
	String m_strVerticalContentAlignment = "Center";
	String m_strExpression = "Binding{[Value[Equip:114-Temp:173-Signal:1]]}";
	String m_strColorExpression = ">20[#FF009090]>30[#FF0000FF]>50[#FFFF0000]>60[#FFFFFF00]";
	String m_cmdExpression = "";

	MainWindow m_rRenderWindow = null;
	String m_strSignalValue = "";

	String type;
	Rect m_rBBox = null;

	public boolean m_bneedupdate = true;
	public boolean m_bValueupdate = true;
	public boolean First = true;

	List<String> cmdList = new ArrayList<String>();

	@Override
	public void onCall() {

		base.setZIndex(m_nZIndex);
		base.setFromHeight(MainWindow.FORM_HEIGHT);
		base.setFromWight(MainWindow.FORM_WIDTH);

		base.setWight(m_nWidth);
		base.setHeght(m_nHeight);

		base.setLeft(m_nPosX);
		base.setTop(m_nPosY);

		base.setTypeId(m_strID);
		base.setType(m_strType);

		base.setCmd(m_strExpression);

		((LableObject) base).setText(c_Content);
		((LableObject) base).setTextSize(m_fFontSize);
		((LableObject) base).setTextColor("#" + currColor.substring(3, currColor.length()));

	}

	@Override
	public void onSetData() {

		base.setValue(m_strContent);
		// ((LableObject)base).setTextColor(m_cFontColor);
	}

	@Override
	public void onControl(Object obj) {
		// TODO Auto-generated method stub
		
	}

}
