package com.sg.uis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.fjw.view.Axis_RealTime;
import com.mgrid.data.DataGetter;
import com.mgrid.main.MainWindow;
import com.sg.common.CFGTLS;
import com.sg.common.IObject;
import com.sg.common.UtExpressionParser;
import com.sg.common.UtExpressionParser.stBindingExpression;
import com.sg.common.UtExpressionParser.stExpression;

/** 数据累加量标签 */
/** made by fjw */
@SuppressWarnings("rawtypes")
@SuppressLint({ "SimpleDateFormat", "RtlHardcoded", "HandlerLeak",
		"UseSparseArrays", "DrawAllocation" })
public class RC_RealTime extends TextView implements IObject {

	public RC_RealTime(Context context) {
		super(context);
		this.setClickable(false);
		this.setBackgroundColor(0x00000000);
		m_rBBox = new Rect();
		my_Axis = new Axis_RealTime(context);
		paint = new Paint();
		// EqdSalId = new ArrayList<String>();
		stEList = new ArrayList<stBindingExpression>();
		lst = new ArrayList<Float>();
		nameValueList = new HashMap<String, Float>();
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
		layout_w = nWidth;
		layout_h = nHeight;

		m_rBBox.left = nX;
		m_rBBox.top = nY;
		m_rBBox.right = nX + nWidth;
		m_rBBox.bottom = nY + nHeight;
		if (m_rRenderWindow.isLayoutVisible(m_rBBox)) {
			layout(nX, nY, nX + nWidth, nY + nHeight);
		}

		my_Axis.upDataValueFlag(layout_w, layout_h, nameValueList.size(), 10,
				nameValueList.size(), m_max_value / 10 * 14, flag,
				nameValueList);
		my_Axis.doLayout(true, nX, nY, nX + nWidth, nY + nHeight);

	}

	public void onDraw(Canvas canvas) {

		if (m_rRenderWindow == null)
			return;
		if (m_rRenderWindow.isLayoutVisible(getBBox()) == false)
			return;

		my_Axis.LineColor = m_cLineColor;
		my_Axis.BackgroundColor = m_cBackgroundColor;
		my_Axis.upDataValueFlag(layout_w, layout_h, nameValueList.size(), 10,
				nameValueList.size(), m_max_value / 10 * 14, flag,
				nameValueList);
		my_Axis.invalidate();

		paint.setColor(ColorTybe[0]);
		paint.setTextSize(10);
		paint.setStyle(Paint.Style.FILL);
		pText = new Paint();
		pText.setColor(Color.RED);
		pText.setTextSize(10);
		pText.setStyle(Paint.Style.FILL);
		Iterator<Entry<String, Float>> it = nameValueList.entrySet().iterator();
		int i = 0;
		while (it.hasNext()) {

			Map.Entry<String, Float> entry = it.next();
			float value = entry.getValue();
			float rc_x = (float) (my_Axis.x_start + my_Axis.x_unit * (i + 0.6));
			float rc_y = my_Axis.y_start - my_Axis.y_per_unit * value;
			float rc_x_end = (float) (rc_x + my_Axis.x_unit * 0.8);
			float rc_y_end = my_Axis.y_start;
			canvas.drawRect(rc_x, rc_y, rc_x_end, rc_y_end, paint);
			canvas.drawText(
					value + "",
					(float) (rc_x + (rc_x_end - rc_x) / 2 - my_Axis.x_unit * 0.1),
					rc_y - 10, pText);
			i++;
		}

	

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
		rWin.addView(my_Axis);
		rWin.addView(this);

	}

	@Override
	public void removeFromRenderWindow(MainWindow rWin) {
		rWin.removeView(this);
		rWin.removeView(my_Axis);

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
			// this.setText(m_strContent);
		} else if ("FontFamily".equals(strName))
			m_strFontFamily = strValue;
		else if ("FontSize".equals(strName)) {
			float fWinScale = (float) MainWindow.SCREEN_WIDTH
					/ (float) MainWindow.FORM_WIDTH;
			m_fFontSize = Float.parseFloat(strValue) * fWinScale;
		} else if ("IsBold".equals(strName))
			m_bIsBold = Boolean.parseBoolean(strValue);
		else if ("FontColor".equals(strName)) {
			m_cFontColor = Color.parseColor(strValue);
		} else if ("LineColor".equals(strName)) {
			m_cLineColor = Color.parseColor(strValue);
		} else if ("BackgroundColor".equals(strName)) {
			m_cBackgroundColor = Color.parseColor(strValue);
			// this.setBackgroundColor(m_cBackgroundColor);
		} else if ("HorizontalContentAlignment".equals(strName))
			m_strHorizontalContentAlignment = strValue;
		else if ("VerticalContentAlignment".equals(strName))
			m_strVerticalContentAlignment = strValue;
		else if ("Expression".equals(strName)) {
			m_strExpression = strValue;
			parse_expression();
		} else if ("ColorExpression".equals(strName))
			m_strColorExpression = strValue;
		else if ("D_mon".equals(strName))
			mode = Integer.parseInt(strValue);
		else if ("NameList".equals(strName)){
			if(!strValue.equals(""))
			{
				NameList=strValue.split("\\|");
			}
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

		this.invalidate();
	}

	@Override
	public boolean updateValue() {

		// m_bneedupdate = false;
		Iterator<stBindingExpression> it = stEList.iterator();
		int i=0;
		while (it.hasNext()) {
			stBindingExpression stb = it.next();
			String value_S = DataGetter.getSignalMeaning(stb.nEquipId,
					stb.nSignalId);
			String Ename = DataGetter.getEquipmentName(stb.nEquipId);
			String Sname = DataGetter
					.getSignalName(stb.nEquipId, stb.nSignalId);
			if (value_S.equals(""))
				value_S = "0";
			float value_F = Float.parseFloat(value_S);
			lst.add(value_F);
			if (Ename != "" && Sname != "")
				nameValueList.put(NameList[i], value_F);
			i++;
		}
		get_max(lst);
		return true;
	}

	// 计算出最大的数值
	public float get_max(List<Float> lst) {
		float value = 0;
		if (lst.size() <= 0)
			return 0;
		for (int i = 0; i < lst.size(); i++) {
			if (lst.get(i) > value) {
				value = lst.get(i);
			}
		}
		m_max_value = value;
		return m_max_value;
	}

	public boolean parse_expression() {
		if ("".equals(m_strExpression))
			return false;

		stExpression oMathExpress = UtExpressionParser.getInstance()
				.parseExpression(m_strExpression);
		if (oMathExpress != null) {

			Iterator<HashMap.Entry<String, stBindingExpression>> it = oMathExpress.mapObjectExpress
					.entrySet().iterator();
			while (it.hasNext()) {
				stBindingExpression oBindingExpression = it.next().getValue();
				stEList.add(oBindingExpression);
			}
		}

		return true;
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
	int m_cLineColor = 0xFF008000;
	int m_cBackgroundColor = 0xFFFFFFFF;
	String m_strHorizontalContentAlignment = "Center";
	String m_strVerticalContentAlignment = "Center";
	String m_strExpression = "Binding{[Value[Equip:114-Temp:173-Signal:1]]}";
	String m_strColorExpression = ">20[#FF009090]>30[#FF0000FF]>50[#FFFF0000]>60[#FFFFFF00]";
	int D_mon = 0; // 累积量显示模式 默认0日累积量 1：月累积量 2：年累积量

	MainWindow m_rRenderWindow = null;
	String m_strSignalValue = "";

	Rect m_rBBox = null;
	public int equiptID;
	public int siganlID;
	public int year;
	public int mon;
	public int day = 1;
	public int mode = 1; // 0：一年的月度累积量 1：月的日度累积量 2：某天的累积量
	public int m_num = 31; // x 轴的刻度数
	public float m_max_value = 10;
	int x_markFlg[] = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16,
			17, 18, 19, 20, 21, 22, 23, 24, 25, 27, 28, 29, 30, 31 }; // x轴的标签数组
	List<ArrayList> value_flag = new ArrayList<ArrayList>(); // 各个数值 数组

	List<ArrayList> value_Mon = new ArrayList<ArrayList>(); // 当月 每天各个数值 数组
	List<ArrayList> value_Year = new ArrayList<ArrayList>(); // 当月 每天各个数值 数组

	List<ArrayList> value_Day = new ArrayList<ArrayList>(); // 当天各个数值

	Axis_RealTime my_Axis; // 定义坐标轴控件元素view
	Paint paint;

	int layout_w, layout_h;

	int flag = 2;// 用于判断x轴标签

	int day_num = 3;
	int[] ColorTybe = { Color.BLUE, Color.GREEN, Color.RED, Color.YELLOW };
	public boolean m_bneedupdate = true;

	// lsy Add 
	// private ArrayList<String> EqdSalId = null;

	private ArrayList<stBindingExpression> stEList = null;
	private List<Float> lst = null;
	private HashMap<String, Float> nameValueList = null;
	Paint pText = null;
	private String[] NameList=null;
}
