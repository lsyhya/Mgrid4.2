package com.sg.uis.LsyNewView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.xclcharts.chart.BarData;
import org.xclcharts.chart.StackBarChart;

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

import com.demo.xclcharts.view.StackBarChart01View;
import com.mgrid.data.DataGetter;
import com.mgrid.main.MainWindow;
import com.sg.common.CFGTLS;
import com.sg.common.IObject;
import com.sg.common.UtExpressionParser;
import com.sg.common.UtExpressionParser.stBindingExpression;
import com.sg.common.UtExpressionParser.stExpression;

/** 叠加柱状图 */
@SuppressLint({ "ShowToast", "InflateParams", "RtlHardcoded",
		"ClickableViewAccessibility" })
public class SgStackBarChart extends TextView implements IObject {

	
//	private StackBarChart SBchart =null;
	List<String> chartLabels = new LinkedList<String>();
	List<BarData> BarDataSet = new LinkedList<BarData>();
	
	public SgStackBarChart(Context context) {
		super(context);

		m_oPaint = new Paint();
		m_rBBox = new Rect();
		chart = new StackBarChart01View(context);
		chart.setTouch(false);
	//	SBchart=chart.getChart();
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

			chart.layout(nX, nY, nX + nWidth, nY + nHeight);
		}
	}

	@Override
	public void addToRenderWindow(MainWindow rWin) {
		m_rRenderWindow = rWin;
		rWin.addView(this);
		rWin.addView(chart);

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
			// this.setBackgroundColor(m_cBackgroundColor);
		} else if ("Content".equals(strName)) {
			m_strContent = strValue;

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
			// this.setTextColor(m_cFontColor);
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
		else if ("Title".equals(strName)) {
			S_title = strValue;
			parse_title();
			setTitle();
//			if (S_title == null || S_title.equals("")) 
//			{
//
//			}
//			else
//			{
//				//SBchart.addSubtitle(S_title);
				
				
//				new Thread(new Runnable() {
//
//					@Override
//					public void run() {
//
//					//	StackBarChart SBC = chart.getChart();
//						List<BarData> BarDataSet = new LinkedList<BarData>();
//						if(S_title.equals("温度通道"))
//						{
//							
//							
//						}else if(S_title.equals("湿度通道")){
//							SBchart.addSubtitle("湿度");
//							Random r = new Random();
//							List<Double> dataSeriesA = new LinkedList<Double>();
//							List<Double> dataSeriesB = new LinkedList<Double>();
//							for (int i = 0; i < 24; i++) {
//								dataSeriesA.add((double) r.nextInt(4)+1);
//								dataSeriesB.add((double) r.nextInt(4)+1);
//							}
//
//							BarDataSet.add(new BarData("冷通道", dataSeriesA, (int) Color.rgb(123, 192, 161)));
//							BarDataSet.add(new BarData("热通道", dataSeriesB, (int) Color.rgb(247, 179, 81)));
//							SBchart.setDataSource(BarDataSet);							
//						}else if(S_title.equals("温度风")){
//							SBchart.addSubtitle("温度");
//							Random r = new Random();
//							List<String> chartLabels = new LinkedList<String>();
//							List<Double> dataSeriesA = new LinkedList<Double>();
//							List<Double> dataSeriesB = new LinkedList<Double>();
//							for (int i = 0; i < 10; i++) {
//								if (i % 2 == 0)
//									chartLabels.add("上侧");
//								else
//									chartLabels.add("下侧");
//							}			
//							for (int i = 0; i < 10; i++) {
//								dataSeriesA.add((double) r.nextInt(4)+1);
//								dataSeriesB.add((double) r.nextInt(4)+1);
//							}
//
//							BarDataSet.add(new BarData("送风", dataSeriesA, (int) Color.rgb(89, 132,
//									206)));
//							BarDataSet.add(new BarData("回风", dataSeriesB, (int) Color.rgb(239, 88,
//									87)));
//							SBchart.setCategories(chartLabels);
//							SBchart.setDataSource(BarDataSet);			
//							
//							
//							
//						}else if(S_title.equals("湿度风")){
//							SBchart.addSubtitle("湿度");
//							Random r = new Random();
//							List<String> chartLabels = new LinkedList<String>();
//							List<Double> dataSeriesA = new LinkedList<Double>();
//							List<Double> dataSeriesB = new LinkedList<Double>();
//							for (int i = 0; i < 10; i++) {
//								if (i % 2 == 0)
//									chartLabels.add("上侧");
//								else
//									chartLabels.add("下侧");
//							}
//							for (int i = 0; i < 10; i++) {
//								dataSeriesA.add((double) r.nextInt(4)+1);
//								dataSeriesB.add((double) r.nextInt(4)+1);
//							}
//
//							BarDataSet.add(new BarData("送风", dataSeriesA, (int) Color.rgb(123, 192, 161)));
//							BarDataSet.add(new BarData("回风", dataSeriesB, (int) Color.rgb(247, 179, 81)));
//							SBchart.setCategories(chartLabels);
//							SBchart.setDataSource(BarDataSet);	
//						}
//					}
//				}).start();
		//	}
			
		} else if ("Expression".equals(strName)) {
			mExpression = strValue;
			parse_cmd();
			
		} else if ("ColorData".equals(strName)) {
			colorCmd = strValue;
			parse_Color();

		}
	}
	
	private void parse_Color() {
		if (colorCmd == null || colorCmd.equals(""))
			return;
		String[] colors = colorCmd.split("\\|");
		for (int i = 0; i < colors.length; i++) {
			colorData.add(colors[i]);
		}

	}
	
	private void parse_title() {
		if (S_title == null || S_title.equals(""))
			return;
		String[] title = S_title.split("\\|");
		for (int i = 0; i < title.length; i++) {
			titleData.add(title[i]);
		}
		
	}

	private void setTitle()
	{
		for (int i = 1; i <=(equAddSignl.size()/titleData.size()); i++) {
			chartLabels.add(i+"");
		}
		//SBchart.setCategories(chartLabels);
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

	// fjw add 按钮控制命令功能的控制命令的绑定表达式解析
	// 解析出控件表达式，返回控件表达式类
	public boolean parse_cmd() {

		if (mExpression.equals("") || mExpression == null)
			return false;
		stExpression oMathExpress = UtExpressionParser.getInstance()
				.parseExpression(mExpression);
		if (oMathExpress != null) {

			Iterator<HashMap.Entry<String, stBindingExpression>> it = oMathExpress.mapObjectExpress
					.entrySet().iterator();
			while (it.hasNext()) {
				stBindingExpression oBindingExpression = it.next().getValue();
				int equipt_id = oBindingExpression.nEquipId;
				int signal_id = oBindingExpression.nSignalId;
				equAddSignl.add(equipt_id + "_" + signal_id);

			}
		}
		return true;
	}

	@Override
	public void updateWidget() {

		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					Thread.sleep(10000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				//handler.sendEmptyMessage(0);
			}
		}).start();
	}
	
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				System.out.println("难受");
			//	SBchart.getDataAxis().setAxisMax((int)maxCount+10);
			//	SBchart.setDataSource(BarDataSet);
				chart.invalidate();
				m_bneedupdate = true;
				break;
			}

		};
	};

	@Override
	public boolean updateValue() {
		m_bneedupdate=false;
		if (equAddSignl.size() <= 0)
			return false;
		float value;
		BarDataSet=new ArrayList<BarData>();
		for (int i = 0; i <titleData.size(); i++) {
			List<Double> dataSeries = new LinkedList<Double>();
			float count = 0;
			for (int j = 0; j <equAddSignl.size(); j++) {
				if(j%titleData.size()==i)
				{
					String[] spl=equAddSignl.get(j).split("_");
					equail=spl[0];
					signal=spl[1];
				//	newValue = DataGetter.getSignalMeaning(equail, signal);
					newValue=Math.random() * 20 + "";
					if (newValue == null || newValue.equals(""))
						return false;
					value = Float.parseFloat(newValue);
					dataSeries.add((double) value);
					count+=value;
				}	
			}
			if(count>maxCount) maxCount=count;
			BarDataSet.add(new BarData(titleData.get(i), dataSeries, Color.parseColor(colorData.get(i))));		
		}
		return true;
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
	StackBarChart01View chart = null;
	// PieChart01View pieChart01View=null;
	private String newValue = "";
	private String oldValue = "";
	public boolean m_bneedupdate = true;
	private String mExpression = "";
	private String S_title = "";
	private ArrayList<String> equAddSignl = new ArrayList<String>();
	private ArrayList<String> titleData = new ArrayList<String>();
	private String colorCmd;
	private ArrayList<String> colorData = new ArrayList<String>();
	private double maxCount;

}
