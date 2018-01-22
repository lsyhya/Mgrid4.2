package com.sg.uis.LsyNewView;

import java.util.ArrayList;
import java.util.List;

import org.xclcharts.chart.GaugeChart;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.demo.xclcharts.view.GaugeChart01View;
import com.mgrid.data.DataGetter;
import com.mgrid.main.MainWindow;
import com.sg.common.CFGTLS;
import com.sg.common.IObject;
import com.sg.common.SgRealTimeData;

/** 半圆仪盘表 */
@SuppressLint({ "ShowToast", "InflateParams", "RtlHardcoded",
		"ClickableViewAccessibility" })
public class SgHalfCircleChart extends TextView implements IObject {

	private GaugeChart Gchart;
	private List<String> label_list = new ArrayList<String>();
	private List<String> color_list = new ArrayList<String>();
	private List<String> andel_list = new ArrayList<String>();
	public SgHalfCircleChart(Context context) {
		super(context);
		m_oPaint = new Paint();
		m_rBBox = new Rect();
		Gauge01View = new GaugeChart01View(context);
		Gchart = Gauge01View.getChart();
	}

	@SuppressLint("DrawAllocation")
	protected void onDraw(Canvas canvas) {
		if (m_rRenderWindow == null)
			return;
		if (m_rRenderWindow.isLayoutVisible(getBBox()) == false)
			return;
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
			Gauge01View.layout(nX, nY, nX + nWidth, nY + nHeight); 
		}
	}

	@Override
	public void addToRenderWindow(MainWindow rWin) {
		m_rRenderWindow = rWin;
		rWin.addView(this);
		rWin.addView(Gauge01View);

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
		} else if ("FontFamily".equals(strName))
			m_strFontFamily = strValue;
		else if ("FontSize".equals(strName)) {
			if (!strValue.isEmpty())
			{
				FontSize = Integer.parseInt(strValue);
				Gauge01View.Tick=FontSize;
			} 
		} else if ("IsBold".equals(strName))
			m_bIsBold = Boolean.parseBoolean(strValue);
		else if ("FontColor".equals(strName)) { 
			if (!strValue.isEmpty()) {
				FontColor = strValue;
				Gauge01View.lColor=FontColor;
			}
		} else if ("ScaleColor".equals(strName)) {
			if (!strValue.isEmpty()) {
				ScaleColor = strValue;
				Gauge01View.tColor=ScaleColor;
			}
		} else if ("PointColor".equals(strName)) {
			if (!strValue.isEmpty()) {
				PointColor = strValue;
				Gauge01View.pColor=PointColor;
			}
		} else if ("ChassisColor".equals(strName)) {
			colorData = strValue;
			
			if(!strValue.isEmpty())
			{
				parse_color();	
			}
			 
		} else if ("HorizontalContentAlignment".equals(strName))
			m_strHorizontalContentAlignment = strValue;
		else if ("VerticalContentAlignment".equals(strName))
			m_strVerticalContentAlignment = strValue; 
		else if ("Expression".equals(strName)) { 
			mExpression = strValue;
			parse_cmd();
		//	setRandom(); 
		} else if ("Content".equals(strName)) { 
			labelData = strValue;
			parse_label();
		}else if ("AngleData".equals(strName)) { 
			angelData=strValue;
			parse_angel();
		}
	}
  
	private void parse_angel() {
		if (angelData == null || angelData.equals("")
				|| angelData.equals("角度")) 
		{
			
			return;
		}
		String[] s = angelData.split("\\|");
		for (int i = 0; i < s.length; i++) {
			andel_list.add(s[i]);  
		}
		Gauge01View.mAngels=andel_list; 
	} 
	
	
	private void parse_label() {
		if (labelData == null || labelData.equals("")
				|| labelData.equals("设置内容")) 
		{
			Gauge01View.initViews();
			return;
		}
		String[] s = labelData.split("\\|");
		for (int i = 0; i < s.length; i++) {
			label_list.add(s[i]);  
		}
		max_data=Float.parseFloat(label_list.get(label_list.size()-1));
		Gauge01View.mLabels.clear();
		Gauge01View.mLabels=label_list;
		Gauge01View.initViews();
	} 

	private void parse_color() {  
		if (colorData == null || colorData.equals(""))
			
			return;
		String[] s = colorData.split("\\|"); 
	
		for (int i = 0; i < s.length; i++) {
			color_list.add(s[i]);
			
		}
		Gauge01View.colorData.clear();
		Gauge01View.mPartitionSet.clear();
		Gauge01View.colorData=color_list;		
	}

	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				Gauge01View.invalidate();
				break;
			}
		};
	};

//	private void setRandom() {
//		final Random r = new Random();
// 
//		new Thread(new Runnable() { 
//
//			@Override
//			public void run() {
//				while (true) {
//					DangQianValue = r.nextInt((int)max_data);
//					DangQianValue= (int) (DangQianValue / max_data * 180);
//					if (DangQianValue > DuiBiValue) {
//						for (int m = DuiBiValue; m <= DangQianValue; m++) {					
//							Gchart.setCurrentAngle(m);
//							try {
//								Thread.sleep(10);
//							} catch (InterruptedException e) {
//
//								e.printStackTrace();
//							}
//							handler.sendEmptyMessage(0);
//						}
//					} else {
//						for (int m = DuiBiValue; m >= DangQianValue; m--) {
//
//							Gchart.setCurrentAngle(m);
//							try {
//								Thread.sleep(10);
//							} catch (InterruptedException e) {
//								e.printStackTrace();
//							}
//							handler.sendEmptyMessage(0);
//						}
//					}
//					DuiBiValue = DangQianValue;
//					try {
//						Thread.sleep(1000);
//					} catch (InterruptedException e) {
//
//						e.printStackTrace();
//					}
//				}
//			}
//		}).start();
//	}

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

	// fjw add 按钮控制命令功能的控制命令的绑定表达式解析
	// 解析出控件表达式，返回控件表达式类
	public boolean parse_cmd() {

		if (mExpression == null || mExpression.equals(""))
			return false;
		
		if (containMath(mExpression)) {
			isMath=true;
		} else {
			isMath=false;
			String[] arg1 = mExpression.split("-");
			equail = arg1[0].split(":")[1];
			signal = arg1[2].split(":")[1].split("]")[0];
		}		
		return true;
	}
	
	public boolean containMath(String cmd) {
		if (cmd.contains("(") || cmd.contains(")"))
			return true;
		else
			return false;
	}

	@Override
	public void updateWidget() {

		float wendu = Float.parseFloat(newValue);
		int angle = (int) (wendu / max_data * 180);
		if (angle < 0)
			return;
		Gchart.setCurrentAngle(angle);
		Gauge01View.invalidate();
	}

	@Override
	public boolean updateValue() {

		if (mExpression == null || mExpression.equals(""))
			return false;
		
		if(isMath)
		{
			SgRealTimeData oRealTimeData = m_rRenderWindow.m_oShareObject.m_mapRealTimeDatas
					.get(this.getUniqueID());
			newValue = oRealTimeData.strValue;
			if (newValue == null || newValue.equals("") || newValue.equals("-999999")) {
				return false;
			}
			
		}else
		{
			if (equail.equals("") || signal.equals(""))
				return false;
			newValue = DataGetter.getSignalMeaning(equail, signal);
		}
		
		if (!newValue.equals(oldValue)) {
			oldValue = newValue;
			return true;
		}
		return false;
	}

	@Override
	public boolean needupdate() {
		return m_bneedupdate;
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

	private String signal = "";
	private String equail = "";
	GaugeChart01View Gauge01View = null;
	private String newValue = "";
	private String oldValue = "";
	public boolean m_bneedupdate = true;
	private String mExpression = ""; 
	private int DangQianValue, DuiBiValue;

	private String FontColor, ScaleColor, PointColor, ChassisColor,angelData;
	private int FontSize;
	private String labelData;
	private String colorData;
    private float max_data=30;
    private boolean isMath=false;
}
