package com.sg.uis;

import com.mgrid.main.MainWindow;
import com.sg.common.IObject;
import com.sg.common.SgRealTimeData;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

/** 自定义表盘 made fjw 2016 04 19 */
public class Dial extends View implements IObject {
	public Dial(Context context) {
		super(context);
		this.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				return true;
			}
		});
		m_oPaint = new Paint();
		m_rRectF1 = new RectF();
		m_rRectF2 = new RectF();
		m_rRectF3 = new RectF();
		m_rBBox = new Rect();

	}

	@SuppressLint("DrawAllocation")
	protected void onDraw(Canvas canvas) {
		if (m_rRenderWindow == null)
			return;
		if (m_rRenderWindow.isLayoutVisible(getBBox()) == false)
			return;

		// this.setBackgroundColor(Color.WHITE);

		int nWidth = (int) (((float) (m_nWidth) / (float) MainWindow.FORM_WIDTH) * (m_rRenderWindow.VIEW_RIGHT - m_rRenderWindow.VIEW_LEFT));
		int nHeight = (int) (((float) (m_nHeight) / (float) MainWindow.FORM_HEIGHT) * (m_rRenderWindow.VIEW_BOTTOM - m_rRenderWindow.VIEW_TOP));

		int pad = 4; // 外圆边距
		m_rRectF1.left = pad + m_nfillWidth / 2;
		m_rRectF1.top = pad + m_nfillWidth / 2;
		if (nWidth < nHeight) { // 用小的一边长度 保证为圆不变形
			m_rRectF1.right = nWidth - pad - m_nfillWidth / 2;
			;
			m_rRectF1.bottom = nWidth - pad - m_nfillWidth / 2;
		} else {
			m_rRectF1.right = nHeight - pad - m_nfillWidth / 2;
			m_rRectF1.bottom = nHeight - pad - m_nfillWidth / 2;
		}

		if (mode == 8) { // 显示标签
			m_oPaint.setColor(m_cLineColor);
			m_oPaint.setTextSize(40);
			str_value = String.valueOf(data_value * 100 / maxValue) + "%";
			canvas.drawText(str_value,
					(m_rRectF1.right + m_rRectF1.left) / 2 - 45,
					(m_rRectF1.bottom + m_rRectF1.top) / 2, m_oPaint);
		}

		// 画出填充环
		
			if ((data_value * 100 / maxValue) < WarmPer)
				m_oPaint.setColor(m_cSingleFillColor);
			else
				m_oPaint.setColor(WarmPerColor);
		

		m_oPaint.setAntiAlias(true); // 设置画笔的锯齿效果
		m_oPaint.setStrokeWidth(m_nfillWidth);
		m_oPaint.setStyle(Paint.Style.STROKE);
		RectF rect = new RectF(m_rRectF1.left, m_rRectF1.top, m_rRectF1.right,
				m_rRectF1.bottom);
		float angle = 360 / maxValue * data_value;
		canvas.drawArc(rect, // 弧线所使用的矩形区域大小
				90, // 开始角度
				angle, // 扫过的角度
				false, // 是否使用中心
				m_oPaint);
		m_oPaint.setColor(m_cBackgroundColor);
		canvas.drawArc(rect, // 弧线所使用的矩形区域大小
				90+angle-2, // 开始角度
				360-angle+3, // 扫过的角度
				false, // 是否使用中心
				m_oPaint);

		// 画出外圆线
		m_rRectF2.left = m_rRectF1.left - m_nfillWidth / 2;
		m_rRectF2.top = m_rRectF1.top - m_nfillWidth / 2;
		m_rRectF2.right = m_rRectF1.right + m_nfillWidth / 2;
		m_rRectF2.bottom = m_rRectF1.bottom + m_nfillWidth / 2;
		m_oPaint.setColor(m_cLineColor);
		m_oPaint.setAntiAlias(true); // 设置画笔的锯齿效果
		m_oPaint.setStrokeWidth(m_nBorderWidth);
		m_oPaint.setStyle(Paint.Style.STROKE);
		canvas.drawOval(m_rRectF2, m_oPaint);
		// 画出第二圆线
		m_rRectF3.left = m_rRectF2.left + m_nfillWidth;
		m_rRectF3.top = m_rRectF2.top + m_nfillWidth;
		m_rRectF3.right = m_rRectF2.right - m_nfillWidth;
		m_rRectF3.bottom = m_rRectF2.bottom - m_nfillWidth;
		m_oPaint.setColor(m_cLineColor); // 仅填充单色
		m_oPaint.setStrokeWidth(m_nBorderWidth);
		m_oPaint.setStyle(Paint.Style.STROKE);
		canvas.drawOval(m_rRectF3, m_oPaint);

		// 画出中间 覆盖 圆
		// m_rRectF3.left = m_rRectF2.left+m_nBorderWidth/2;
		// m_rRectF3.top = m_rRectF2.top+m_nBorderWidth/2;
		// m_rRectF3.right = m_rRectF2.right-m_nBorderWidth/2;
		// m_rRectF3.bottom = m_rRectF2.bottom-m_nBorderWidth/2;
		// m_oPaint.setColor(m_cBackgroundColor); // 仅填充背景色
		// m_oPaint.setStrokeWidth(m_nBorderWidth);
		// m_oPaint.setStyle(Paint.Style.FILL);
		// canvas.drawOval(m_rRectF3, m_oPaint);
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
			layout(nX, nY, nX + nWidth, nY + nHeight);
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
			m_oPaint.setAlpha((int) (m_fAlpha * 255));
		} else if ("BackgroundColor".equals(strName)) { // 背景色
			m_cBackgroundColor = Color.parseColor(strValue);
		} else if ("BorderColor".equals(strName)) { // 前景色 小圆颜色
			m_cBorderColor = Color.parseColor(strValue);
		} else if ("FillColor".equals(strName)) {
			m_cSingleFillColor = Color.parseColor(strValue);
			m_cStartFillColor = m_cSingleFillColor;
		} else if ("LineColor".equals(strName)) {
			m_cLineColor = Color.parseColor(strValue);
		} else if ("FillWidth".equals(strName)) {
			if ("".equals(strValue)) {
			} else {
				m_nfillWidth = Integer.parseInt(strValue);
			}
		} else if ("WarmPer".equals(strName)) {
			System.out.println(".." + strValue);
			WarmPer = Float.parseFloat(strValue);
		} else if ("WarmPerColor".equals(strName)) {
			WarmPerColor = Color.parseColor(strValue);
		} else if ("Expression".equals(strName))
			m_strExpression = strValue;
		else if ("ColorExpression".equals(strName))
			m_strColorExpression = strValue;
		else if ("MaxValue".equals(strName))
			maxValue = Integer.parseInt(strValue);
		else if ("scale".equals(strName))
			scale = Integer.parseInt(strValue);
		else if ("mode".equals(strName))
			if ("".equals(strValue)) {

			} else {
				mode = Integer.parseInt(strValue);
			}
	}

	@Override
	public void initFinished() {
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
	public boolean updateValue() {
		m_bneedupdate = false;

		SgRealTimeData oRealTimeData = m_rRenderWindow.m_oShareObject.m_mapRealTimeDatas
				.get(this.getUniqueID());
		if (oRealTimeData == null)
			return false;
		String strValue = oRealTimeData.strValue;
		if (strValue == null || "".equals(strValue) == true)
			return false;

		int nValue = 0;
		try {
			nValue = Integer.parseInt(strValue);
		} catch (Exception e) {

		}

		if (strValue.equals(oldSignalValue)) {
			return false;
		} else {
			oldSignalValue = strValue;
			parseFontcolor(oRealTimeData.strData); // 解析数值颜色表达式 fjw add
			if ("".equals(oRealTimeData.strData))
				return false;
			data_value = Float.parseFloat(oRealTimeData.strData);
			return true;
		}

	}

	// 颜色解析函数 传入参数：显示值 fjw add
	public int parseFontcolor(String strValue) {
		m_cSingleFillColor = m_cStartFillColor;
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
				m_cSingleFillColor = Color.parseColor(a[1]);
			}
		}
		return m_cSingleFillColor;
	}

	@Override
	public boolean needupdate() {
		return m_bneedupdate;
	}

	@Override
	public void needupdate(boolean bNeedUpdate) {
		m_bneedupdate = bNeedUpdate;
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
	int m_nZIndex = 4;
	int m_nPosX = 349;
	int m_nPosY = 78;
	int m_nWidth = 200;
	int m_nHeight = 150;
	float m_fAlpha = 1.0f;
	float m_fRotateAngle = 0.0f;
	int m_cBackgroundColor = 0xFFFFFFFF;
	int m_cBorderColor = 0xFF000000;

	int m_cFillColor = 0xFFF2C0FF;
	int m_cLineColor = 0xFF5EC5EE;
	boolean m_bIsDashed = false;
	String m_strExpression = "Binding{[Value[Equip:114-Temp:173-Signal:1]]}";
	String m_strColorExpression = ">20[#FF009090]>30[#FF0000FF]>50[#FFFF0000]>60[#FFFFFF00]";

	int m_cSingleFillColor = 0xFF0000FF;
	int m_cStartFillColor = 0x00000000;
	float[] m_arrGradientColorPos = null;
	int[] m_arrGradientFillColor = null;
	boolean m_bIsHGradient = false; // 水平渐变

	float maxValue = 100; // 表盘的最大值
	int scale = 10; // 表盘的刻度
	int mode = 1; // 表盘样式
	float data_value = 10; // 目前的表盘数值
	int m_nBorderWidth = 2; // 线条宽度
	int m_nfillWidth = 30; // 填充的圆环宽度
	String str_value = "";

	MainWindow m_rRenderWindow = null;
	Paint m_oPaint = null;
	RectF m_rRectF1 = null;
	RectF m_rRectF2 = null;
	RectF m_rRectF3 = null;
	Rect m_rBBox = null;
	public boolean m_bneedupdate = true;
	String oldSignalValue = "";
	private int WarmPerColor = Color.RED;
	private float WarmPer = 50;
}
