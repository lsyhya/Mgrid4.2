package com.sg.uis.newView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.xclcharts.chart.BarChart;
import org.xclcharts.chart.BarData;

import com.demo.xclcharts.view.BarChart01View;
import com.mgrid.data.DataGetter;
import com.mgrid.main.MainWindow;
import com.mgrid.util.ExpressionUtils;
import com.mgrid.util.TimeUtils;
import com.sg.common.CFGTLS;
import com.sg.common.IObject;
import com.sg.common.LanguageStr;
import com.sg.web.BarChartViewObject;
import com.sg.web.base.ViewObjectBase;
import com.sg.web.base.ViewObjectSetCallBack;

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
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

/** 柱状图 */
@SuppressLint({ "ShowToast", "InflateParams", "RtlHardcoded",
		"ClickableViewAccessibility" })
public class SgBarChartView extends TextView implements IObject ,ViewObjectSetCallBack{

	private BarChart Bchart = null;
	private List<String> chartLabels = new LinkedList<String>();
	private List<BarData> chartData = new LinkedList<BarData>();
	private Map<Integer, List<Map<Double, Double>>> linePointListData = new HashMap<Integer, List<Map<Double, Double>>>();
	private List<RadioButton> rButton = new ArrayList<RadioButton>();
	private int mode = 1;
	
	
	private String d=LanguageStr.d;
	private String m=LanguageStr.m;
	private String y=LanguageStr.y;

	public SgBarChartView(Context context) {
		super(context);
		m_oPaint = new Paint();
		m_rBBox = new Rect();
		chart = new BarChart01View(context);
		chart.setTouch(false);
		Bchart = chart.getBarChart();
		addRadio();
	}

	private void addRadio() {

		RadioButton ridobuttons1 = new RadioButton(getContext());
		ridobuttons1.setText(d);
		rButton.add(ridobuttons1);
		ridobuttons1.setChecked(true);
		

		RadioButton ridobuttons2 = new RadioButton(getContext());
		ridobuttons2.setText(m);
		rButton.add(ridobuttons2);
		ridobuttons2.setChecked(false);

		RadioButton ridobuttons3 = new RadioButton(getContext());
		ridobuttons3.setText(y);
		rButton.add(ridobuttons3);
		ridobuttons3.setChecked(false);

		for (int i = 0; i < rButton.size(); i++) {
			rButton.get(i).setTextSize(13);
			rButton.get(i).setTag(i + 1);
			rButton.get(i).setOnClickListener(linClickListener);
		}
	}

	private OnClickListener linClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			for (int i = 0; i < rButton.size(); i++) {
				rButton.get(i).setChecked(false);
			}
			int tag = (Integer) ((RadioButton) v).getTag();
			mode = tag;
			rButton.get(mode - 1).setChecked(true);
			addLabels(mode);
			isUpdate = true;
			m_bneedupdate = true;
		}
	};

	private void addLabels(int index) {
		chartLabels = null;
		chartLabels = new LinkedList<String>();
		switch (index) {
		case 1:// 日
			for (int i = 1; i <= 31; i++) {
				chartLabels.add(i + "");
			}
			break;
		case 2:// 月
			for (int i = 1; i <= 12; i++) {
				chartLabels.add(i + "");
			}
			break;
		case 3:// 月

			if (startYear < 0)
				startYear = Integer.parseInt(TimeUtils.getYear());

			for (int i = 0; i < 10; i++) {
				chartLabels.add((startYear + i) + "");
			}
			break;
		}
		Bchart.setCategories(chartLabels);
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
			for (int i = 0; i < rButton.size(); i++) {
				rButton.get(i).layout(nX + (i + 1) * nWidth / 4, nY,
						nX + (i + 2) * nWidth / 4, nY + 18);
			}
		}
	}

	@Override
	public void addToRenderWindow(MainWindow rWin) {
		
		m_rRenderWindow = rWin;
		m_rRenderWindow.viewList.add(base);
		rWin.addView(this);
		rWin.addView(chart);
		for (int i = 0; i < rButton.size(); i++) {
			rWin.addView(rButton.get(i));
		}
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
		} else if ("ScaleColor".equals(strName)) {
			if (!strValue.isEmpty()) {
				Bchart.getDataAxis().getAxisPaint()
						.setColor(Color.parseColor(strValue));
				Bchart.getCategoryAxis().getAxisPaint()
						.setColor(Color.parseColor(strValue));
				Bchart.getDataAxis().getTickMarksPaint()
						.setColor(Color.parseColor(strValue));
				Bchart.getCategoryAxis().getTickMarksPaint()
						.setColor(Color.parseColor(strValue));
			}
		} else if ("Content".equals(strName)) {
			m_strContent = strValue;
			parse_content();

		} else if ("FontFamily".equals(strName))
			m_strFontFamily = strValue;
		else if ("FontSize".equals(strName)) {
			float fWinScale = (float) MainWindow.SCREEN_WIDTH
					/ (float) MainWindow.FORM_WIDTH;
			m_fFontSize = Float.parseFloat(strValue) * fWinScale;
		} else if ("IsBold".equals(strName)) {
			m_bIsBold = Boolean.parseBoolean(strValue);
			isNenghao = m_bIsBold;
			if (!isNenghao) {				
				for (int i = 0; i < rButton.size(); i++) {
					rButton.get(i).setVisibility(View.INVISIBLE);
				}
			} else {
				addListData();
				addLabels(mode);
			}
		} else if ("FontColor".equals(strName)) {
			if (!strValue.isEmpty()) {
				// x轴刻度文字画笔
				Bchart.getCategoryAxis().getTickLabelPaint()
						.setColor(Color.parseColor(strValue));
				// y轴刻度文字画笔
				Bchart.getDataAxis().getTickLabelPaint()
						.setColor(Color.parseColor(strValue));
				for (int i = 0; i < rButton.size(); i++) {
					rButton.get(i).setTextColor(Color.parseColor(strValue));
				
				}
				
			}
		} else if ("ClickEvent".equals(strName))
			m_strClickEvent = strValue;
		else if ("Url".equals(strName))
			m_strUrl = strValue;
		else if ("ColorData".equals(strName)) {
			if (!strValue.isEmpty()) {
				color_data = strValue;
				parse_color();
			}
		} else if ("HorizontalContentAlignment".equals(strName))
			m_strHorizontalContentAlignment = strValue;
		else if ("VerticalContentAlignment".equals(strName))
			m_strVerticalContentAlignment = strValue;
		else if ("Expression".equals(strName)) {
			cmd = strValue;
			mExpression = strValue;
			parse_cmd();
		} else if ("Xlabel".equals(strName)) {
			labelData = strValue;
			if (chartLabels.size() == 0)
				parse_label();
		}
	}

	private void parse_label() {
		if (labelData == null || labelData.equals("")
				|| labelData.equals("设置内容")) {
			setlable();
			return;
		}

		String[] s = labelData.split("\\|");
		for (int i = 0; i < s.length; i++) {
			chartLabels.add(s[i]);
		}

		Bchart.setCategories(chartLabels);
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

	private void parse_content() {
		if (m_strContent == null || m_strContent.equals("")
				|| m_strContent.equals("设置内容")) {
			return;
		}
		String[] s = m_strContent.split("\\|");
		for (int i = 0; i < data_cmd.size(); i++) {
			if (i < s.length)
				data_label.add(s[i]);
			else
				data_label.add(s[s.length - 1]);
		}
	}

	private void parse_color() {
		if (data_cmd.size() == 0)
			return;
		String[] str = color_data.split("\\|");
		for (int i = 0; i < data_cmd.size(); i++) {
			if (i < str.length)
				data_color.add(str[i]);
			else
				data_color.add(str[str.length - 1]);
		}
	}

	// fjw add 按钮控制命令功能的控制命令的绑定表达式解析
	// 解析出控件表达式，返回控件表达式类
	public boolean parse_cmd() {
		if (cmd.equals("") || cmd == null)
			return false;
		String[] Expression = cmd.split("/");
		for (int i = 0; i < Expression.length; i++) {
			List<String> list_cmd = ExpressionUtils.getExpressionUtils().parse(
					Expression[i]);
			index = list_cmd.size();
			data_cmd.add(list_cmd);
		}
		return true;
	}

	private void addListData() {
		for (int i = 1; i <= 3; i++) {
			List<Map<Double, Double>> list = new ArrayList<Map<Double, Double>>();
			for (int j = 0; j < data_cmd.size(); j++) {
				Map<Double, Double> map = new TreeMap<Double, Double>();
				list.add(map);
			}
			linePointListData.put(i, list);
		}
	}

	private void setlable() {

		if (chartLabels.size() != 0)
			return;
		for (int i = 1; i <= index; i++) {
			chartLabels.add(i + "");
		}
		Bchart.setCategories(chartLabels);
	}

	@Override
	public void updateWidget() {
		new Thread(new Runnable() {

			@Override
			public void run() {

				handler.sendEmptyMessage(0);
				handler.postDelayed(runnable, 1000 * 3);
			}
		}).start();
	}

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:

				
				Bchart.getDataAxis().setAxisMax((int) max_Value);
				Bchart.getDataAxis().setAxisSteps((int) max_Value / 5 / 2);
				Bchart.setDataSource(chartData);
				chart.invalidate();

				break;
			}

		};
	};

	Runnable runnable = new Runnable() {

		@Override
		public void run() {

			m_bneedupdate = true;
		}
	};

	@Override
	public boolean updateValue() {
		if (isNenghao) {
			return updateNenghao();
		} else {
			return updateData();
		}

	}

	private void cleanData(int index) {
		List<Map<Double, Double>> linePointData = linePointListData.get(index);
		for (int j = 0; j < linePointData.size(); j++) {
			linePointData.get(j).clear();
		}
	}

	private boolean updateNenghao() {
		if (data_cmd.size() <= 0)
			return false;

		double Yeartime = Double.parseDouble(TimeUtils.getYear());
		double Monthtime = Double.parseDouble(TimeUtils.getMonth());
		double Daytime = Double.parseDouble(TimeUtils.getDay());

		if (Daytime != currentDay || Monthtime != currentMonth
				|| Yeartime != currentYear || isUpdate) {
			isUpdate = false;
			if (Monthtime != currentMonth) {
				cleanData(1);
			}
			if (Yeartime != currentYear) {
				cleanData(2);
			}

			double value;
			chartData = new ArrayList<BarData>();
			List<Double> dataSeriesA = null;

			int i = 0;
			for (List<String> list_cmd : data_cmd) {
				dataSeriesA = new LinkedList<Double>();

				for (String s : list_cmd) {

					String[] spl = s.split("-");
					equail = spl[0];
					signal = spl[2];
					newValue = DataGetter.getSignalValue(equail, signal);
					// Random random = new Random();
					// newValue = random.nextInt(100) + "";
					if (newValue == null || newValue.equals(""))
						continue;
					value = Double.parseDouble(newValue);

					if (isFirstIN) {
						for (int j = 1; j <= 3; j++) {

							List<Map<Double, Double>> linePointData = linePointListData
									.get(j);

							readData(linePointData.get(i), equail, signal, j);

						}
					}

					if (Daytime != currentDay) {

						for (double a = 1; a < Daytime; a++) {

							if (linePointListData.get(1).get(i).get(a) == null) {

								linePointListData.get(1).get(i).put(a, value);
							}
						}
						if (linePointListData.get(1).get(i).get(Daytime) == null)
							linePointListData.get(1).get(i).put(Daytime, value);

						saveData(linePointListData.get(1).get(i), equail,
								signal, 1);

					}

					if (Monthtime != currentMonth) {

						for (int a = 1; a < (int) Monthtime; a++) {
							if (!linePointListData.get(2).get(i)
									.containsKey((double) a)) {

								linePointListData.get(2).get(i)
										.put((double) a, value);
							}
						}
						if (!linePointListData.get(2).get(i)
								.containsKey(Monthtime))
							linePointListData.get(2).get(i)
									.put(Monthtime, value);

						saveData(linePointListData.get(2).get(i), equail,
								signal, 2);

					}

					if (Yeartime != currentYear) {

						if (startYear < 0)
							startYear = (int) Yeartime;
						for (int a = startYear; a < (int) Yeartime; a++) {
							if (!linePointListData.get(3).get(i)
									.containsKey((double) a)) {

								linePointListData.get(3).get(i)
										.put((double) a, value);
							}
						}
						if (!linePointListData.get(3).get(i)
								.containsKey(Yeartime))
							linePointListData.get(3).get(i)
									.put(Yeartime, value);

						saveData(linePointListData.get(3).get(i), equail,
								signal, 3);

					}
				}
				Map<Double, Double> map = linePointListData.get(mode).get(i);
				setData(dataSeriesA, map);
				compareMax(dataSeriesA);
				BarData BarDataA = new BarData(data_label.get(i), dataSeriesA,
						Color.parseColor(data_color.get(i)));
				chartData.add(BarDataA);
				i++;
			}
			currentDay = Daytime;
			currentMonth = Monthtime;
			currentYear = Yeartime;
			m_bneedupdate = false;
			isFirstIN = false;
			return true;
		} else {
			return false;
		}
	}

	private void saveData(Map<Double, Double> data, String eqstr, String sistr,
			int index) {
		String Yeartime = TimeUtils.getYear();
		String Monthtime = TimeUtils.getMonth();

		String fileName = "";
		File path = new File(RC_nenghao);
		if (!path.exists()) {
			path.mkdir();
		}

		try {
			switch (index) {

			case 1:// 一天
				fileName = RC_nenghao + "/" + Yeartime + "-" + Monthtime + "-"
						+ eqstr + "-" + sistr;
				break;
			case 2:// 一月
				fileName = RC_nenghao + "/" + Yeartime + "-" + eqstr + "-"
						+ sistr;
				break;
			case 3:// 一年
				fileName = RC_nenghao + "/" + eqstr + "-" + sistr;
				break;
			}

			File f = new File(fileName);
			if (!f.exists()) {
				f.createNewFile();
			}
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(f), "GB2312"));
			Iterator<Entry<Double, Double>> iterator = data.entrySet()
					.iterator();
			while (iterator.hasNext()) {
				Entry<Double, Double> entry = iterator.next();
				double d1 = entry.getKey();
				double d2 = entry.getValue();
				bw.write(d1 + "-" + d2);
				bw.newLine();
			}
			bw.flush();
			bw.close();
		} catch (Exception e) {

		}
	}

	private void setData(List<Double> list, Map<Double, Double> map) {

		if (mode == 3) {

			Iterator<Entry<Double, Double>> it = map.entrySet().iterator();
			// while(it.hasNext())
			// {
			// Entry<Double, Double> en=it.next();
			// System.out.println(en.getKey());
			// }

			for (double i = 1; i < map.size(); i++) {

				Double d1 = map.get(i + (double) startYear);
				Double d2 = map.get((double) startYear + i - 1);
				if (d1 == null || d2 == null) {
					d1 = (double) 0;
					d2 = (double) 0;
				}
				list.add(d1 - d2);
			}

		} else {
			for (double i = 1; i < map.size(); i++) {
				list.add(map.get(i + 1) - map.get(i));
			}
		}
	}

	private void readData(Map<Double, Double> data, String eqstr, String sistr,
			int index) {
		String Yeartime = TimeUtils.getYear();
		String Monthtime = TimeUtils.getMonth();

		String fileName = "";
		try {
			switch (index) {

			case 1:// 一天
				fileName = RC_nenghao + "/" + Yeartime + "-" + Monthtime + "-"
						+ eqstr + "-" + sistr;
				break;
			case 2:// 一月
				fileName = RC_nenghao + "/" + Yeartime + "-" + eqstr + "-"
						+ sistr;
				break;
			case 3:// 一年
				fileName = RC_nenghao + "/" + eqstr + "-" + sistr;

				break;
			}
			File f = new File(fileName);
			if (f.exists()) {

				BufferedReader br = new BufferedReader(new InputStreamReader(
						new FileInputStream(f), "gb2312"));
				String str = "";
				double tem = -1;
				while ((str = br.readLine()) != null) {

					String[] s = str.split("-");
					if (index == 3 && tem < 0) {
						tem = Double.parseDouble(s[0]);
						startYear = (int) tem;
					}
					data.put(Double.parseDouble(s[0]), Double.parseDouble(s[1]));
				}
				br.close();
			}
		} catch (Exception e) {

		}
	}

	private boolean updateData() {
		
		
		if (data_cmd.size() <= 0)
			return false;

		float value;

		chartData = new ArrayList<BarData>();

		List<Double> dataSeriesA = null;

		int i = 0;
		for (List<String> list_cmd : data_cmd) {
			dataSeriesA = new LinkedList<Double>();
			for (String s : list_cmd) {
				String[] spl = s.split("-");
				equail = spl[0];
				signal = spl[2];
				newValue = DataGetter.getSignalValue(equail, signal);
				// Random random = new Random();
				// newValue = random.nextInt(100) + ""; 
				if (newValue == null || newValue.equals(""))
					continue;
				
				value = Float.parseFloat(newValue);
				dataSeriesA.add((double) value);
			}
			compareMax(dataSeriesA);
			BarData BarDataA = new BarData(data_label.get(i), dataSeriesA,
					Color.parseColor(data_color.get(i)));
			chartData.add(BarDataA);
			i++;
		} 

		m_bneedupdate = false;
		return true;
	}

	private void compareMax(List<Double> value) {
		max_Value = 0;
		for (int i = 0; i < value.size(); i++) {
			double v = value.get(i);
			if (v > max_Value)
				max_Value = v;
		}
		// Random random=new Random();
		// max_Value=random.nextDouble()*1000;
		if (max_Value == 0d) {
			max_Value += 10;
		} else {
			max_Value = max_Value * 1.2 + 10;
		}

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
	BarChart01View chart = null;
	// PieChart01View pieChart01View=null;
	private String newValue = "";
	private String cmd = "";
	public boolean m_bneedupdate = true;
	private String mExpression = "";
	private String color_data = null;

	private int index = 0;
	private List<List<String>> data_cmd = new ArrayList<List<String>>();// 表达式分类
	private List<String> data_color = new ArrayList<String>();// 颜色分类
	private List<String> data_label = new ArrayList<String>();// 各个柱状图的含义
	private double max_Value = 0; // 轴刻度最大值

	private String labelData = "";
	private boolean isNenghao = false;

	private String RC_nenghao = "/mgrid/log/RC_nenghao";
	private double currentYear = -1, currentMonth = -1, currentDay = -1;
	private boolean isFirstIN = true;
	private boolean isUpdate = true;
	private int startYear = -1;

	ViewObjectBase base=new BarChartViewObject();
	
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
		
		Log.e(m_strID, m_strType);
		
	}

	@Override
	public void onSetData() {
		
		
	}

}
