package com.sg.uis.LsyNewView;

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
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.xclcharts.chart.SplineChart;
import org.xclcharts.chart.SplineData;
import org.xclcharts.renderer.XEnum;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.demo.xclcharts.view.SplineChart03View;
import com.mgrid.data.DataGetter;
import com.mgrid.main.MainWindow;
import com.mgrid.main.R;
import com.mgrid.util.CustomPopWindow;
import com.mgrid.util.ExpressionUtils;
import com.mgrid.util.TimeUtils;
import com.sg.common.CFGTLS;
import com.sg.common.IObject;
import com.sg.common.MyAdapter;
import com.sg.common.SgRealTimeData;

/** mPUE曲线图 */
@SuppressLint({ "ShowToast", "InflateParams", "RtlHardcoded",
		"ClickableViewAccessibility" })
public class SgSplineChart extends TextView implements IObject {

	private SplineChart Schart;// 关键view
	private LinkedList<String> labels = new LinkedList<String>();// X轴标签对象
	private LinkedList<SplineData> chartData = new LinkedList<SplineData>();;// Y轴标签对象
	private Map<Integer, List<LinkedHashMap<Double, Double>>> linePointMapData = new HashMap<Integer, List<LinkedHashMap<Double, Double>>>();
	private List<LinkedHashMap<Double, Double>> oldYearData = new ArrayList<LinkedHashMap<Double, Double>>();
	private List<RadioButton> rButton = new ArrayList<RadioButton>();
	private CustomPopWindow popupWindow = null;
	private ArrayList<String> nameList = new ArrayList<String>();
	private MyAdapter myAdapter = null;

	public SgSplineChart(Context context) {
		super(context);
		m_oPaint = new Paint();
		m_rBBox = new Rect();
		chart = new SplineChart03View(context);
		chart.setTouch(false);
		Schart = chart.getChart();
		Schart.getDataAxis().setAxisMax(max_value);
		Schart.getDataAxis().setAxisSteps(AxisSteps);
		addRadio();
		setData();
	}

	private void setData() {
		nameList.add((Integer.parseInt(TimeUtils.getYear()) - 1) + " 年");
		nameList.add(TimeUtils.getYear() + " 年");
	}

	private void addRadio() {
		RadioButton ridobuttons1 = new RadioButton(getContext());
		ridobuttons1.setText("时");
		rButton.add(ridobuttons1);
		ridobuttons1.setChecked(true);

		RadioButton ridobuttons2 = new RadioButton(getContext());
		ridobuttons2.setText("日");
		rButton.add(ridobuttons2);
		ridobuttons2.setChecked(false);

		RadioButton ridobuttons3 = new RadioButton(getContext());
		ridobuttons3.setText("月");
		rButton.add(ridobuttons3);
		ridobuttons3.setChecked(false);

		RadioButton ridobuttons4 = new RadioButton(getContext());
		ridobuttons4.setText("年");
		rButton.add(ridobuttons4);
		ridobuttons4.setChecked(false);

		for (int i = 0; i < rButton.size(); i++) {
			rButton.get(i).setTextSize(13);
			rButton.get(i).setTag(i + 1);
			rButton.get(i).setOnClickListener(linClickListener);
		}
		addLabels(mode);
	}

	private void showPopUpWin(final RadioButton btn) {

		View view = m_rRenderWindow.m_oMgridActivity.getLayoutInflater()
				.inflate(R.layout.pop, null);
//		popupWindow = new PopupWindow(view, (int) (btn.getWidth() * 0.3), 100,
//				true);
//		// 设置一个透明的背景，不然无法实现点击弹框外，弹框消失
//		popupWindow.setBackgroundDrawable(new BitmapDrawable());
//
//		// 设置点击弹框外部，弹框消失
//		popupWindow.setOutsideTouchable(true);
//		popupWindow.setFocusable(true);
//		popupWindow.showAsDropDown(btn);
		  popupWindow = new CustomPopWindow.PopupWindowBuilder(getContext())
		 .size(100,100)
         .setView(view)
         .setFocusable(true)
         .setOutsideTouchable(true)
         .create();
		  popupWindow.showAsDropDown(btn,5,0);

		ListView lv = (ListView) view.findViewById(R.id.lv_list);
		myAdapter = new MyAdapter(getContext(), nameList);

		myAdapter.setTextColor("#F0F0F0");
		myAdapter.setBtnColor("#FF4D4D4D");
		lv.setAdapter(myAdapter);
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				btn.setText(nameList.get(position));

				popupWindow.onDismiss();
				
				m_bneedupdate = true;
			}
		});

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
			if (mode == 4 && selectYear.equals("")) {
				selectYear = TimeUtils.getYear();
				rButton.get(mode - 1).setText(selectYear + " 年");
			} else if (mode == 4 && !selectYear.equals("")) {

				showPopUpWin(rButton.get(mode - 1));

			} else {
				selectYear = "";
				rButton.get(3).setText("年");
			}
			m_bneedupdate = true;
		}
	};

	@SuppressLint("Dra wAllocation")
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
			chart.layout(nX, nY + 19, nX + nWidth, nY + nHeight);
			for (int i = 0; i < rButton.size(); i++) {
				rButton.get(i).layout(nX + (i + 1) * nWidth / 5, nY,
						nX + (i + 1) * nWidth / 4 + nWidth / 4, nY + 18);
			}
		}
	}

	@Override
	public void addToRenderWindow(MainWindow rWin) {
		m_rRenderWindow = rWin;
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
		} else if ("BackgroundColor".equals(strName)) {
			if (strValue.isEmpty())
				return;
			m_cBackgroundColor = Color.parseColor(strValue);
			// this.setBackgroundColor(m_cBackgroundColor);
		} else if ("Content".equals(strName)) {
			labelData = strValue;
			parse_label();
		} else if ("FontFamily".equals(strName))
			m_strFontFamily = strValue;
		else if ("FontSize".equals(strName)) {
			float fWinScale = (float) MainWindow.SCREEN_WIDTH
					/ (float) MainWindow.FORM_WIDTH;
			m_fFontSize = Float.parseFloat(strValue) * fWinScale;

		} else if ("IsBold".equals(strName))
			try {
				if (!strValue.isEmpty() && strValue != null
						&& !strValue.equals("")) {
					isSave = Boolean.parseBoolean(strValue);

				}
			} catch (Exception e) {
				Toast.makeText(getContext(), "曲线控件Content属性错误", 200).show();
			}
		else if ("FontColor".equals(strName)) {
			if (!strValue.isEmpty()) {
				Schart.getCategoryAxis().getTickLabelPaint()
						.setColor(Color.parseColor(strValue));
				Schart.getDataAxis().getTickLabelPaint()
						.setColor(Color.parseColor(strValue));
				for (int i = 0; i < rButton.size(); i++) {
					rButton.get(i).setTextColor(Color.parseColor(strValue));

				}
			}
		} else if ("ScaleColor".equals(strName)) {
			if (!strValue.isEmpty()) {
				Schart.getPlotGrid().getHorizontalLinePaint()
						.setColor(Color.parseColor(strValue));

			}
		} else if ("ColorData".equals(strName)) {
			if (!strValue.isEmpty()) {
				parse_color(strValue);
			}
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
			parse_cmd();
		} else if ("Max".equals(strName)) {
			if (!strValue.isEmpty() && strValue != null && !strValue.equals("")) {
				try {
					max_value = Integer.parseInt(strValue);
					Schart.getDataAxis().setAxisMax(max_value);
					Schart.getDataAxis().setAxisSteps(max_value / ycount);
				} catch (Exception e) {
					Toast.makeText(getContext(), "曲线图配置出错", 200).show();
				}

			}
		} else if ("Ycount".equals(strName)) {
			if (!strValue.isEmpty() && strValue != null && !strValue.equals("")) {
				ycount = Integer.parseInt(strValue);
				Schart.getDataAxis().setAxisSteps(max_value / ycount);
			}
		} else if ("XColor".equals(strName)) {
			if (!strValue.isEmpty()) {
				Schart.getCategoryAxis().getAxisPaint()
						.setColor(Color.parseColor(strValue));
				Schart.getDataAxis().getAxisPaint()
						.setColor(Color.parseColor(strValue));
			}
		}
	}

	private void addLabels(int index) {
		labels = null;
		labels = new LinkedList<String>();
		switch (index) {
		case 1:// 一小时
			labels.add("0");
			labels.add("20");
			labels.add("40");
			labels.add("60");
			break;
		case 2:// 一天
			labels.add("0");
			labels.add("8");
			labels.add("16");
			labels.add("24");
			break;
		case 3:// 一月
			labels.add("0");
			labels.add("10");
			labels.add("20");
			labels.add("30");
			break;
		case 4:// 一年
			labels.add("0");
			labels.add("4");
			labels.add("8");
			labels.add("12");
			break;
		}
		Schart.setCategories(labels);
		Schart.setCategoryAxisMax(Integer.parseInt(labels.get(labels.size() - 1)));
	}

	private void parse_color(String strValue) {
		if (strValue == null || strValue.equals(""))
			return;
		String[] s = strValue.split("\\|");
		for (int i = 0; i < s.length; i++) {
			colorData.add(s[i]);
		}
	}

	private void parse_label() {
		if (labelData == null || labelData.equals("")
				|| labelData.equals("设置内容")) {

			return;
		}
		String[] s = labelData.split("\\|");
		for (int i = 0; i < s.length; i++) {
			label_data.add(s[i]);
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

	// 解析表达式
	// 添加不同位置不同模式的容器
	public boolean parse_cmd() {

		if (mExpression.equals("") || mExpression == null)
			return false;

		if (containMath(mExpression)) {
			isMath = true;
		} else {
			isMath = false;
			cmdList = ExpressionUtils.getExpressionUtils().parse(mExpression);
		}

		
		sizeMath = ExpressionUtils.getExpressionUtils().getSize(mExpression);
		
		for (int i = 1; i <= 4; i++) {
			List<LinkedHashMap<Double, Double>> linePointData = new ArrayList<LinkedHashMap<Double, Double>>();
			for (int j = 0; j < sizeMath; j++) {
				LinkedHashMap<Double, Double> linePoint = new LinkedHashMap<Double, Double>();
				linePointData.add(linePoint);
			}
			linePointMapData.put(i, linePointData);
		}
		//初始化去年数据的容器
		for (int j = 0; j < sizeMath; j++) {
			LinkedHashMap<Double, Double> linePoint = new LinkedHashMap<Double, Double>();
			oldYearData.add(linePoint);
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

		// 数据源
		Schart.setDataSource(chartData);

		chart.invalidate();
		if (isAuth) {
			handler.postDelayed(runnable, 30 * 1000); // 10秒
			isAuth = false;
			if (isDay) {
				isDay = false;
				handDay.postDelayed(runDay, 10 * 1000 * 60);
				// handler.postDelayed(runDay, 30 * 1000);
			}
			if (isMon) {

				isMon = false;
				handMon.postDelayed(runMon, 2 * 60 * 1000 * 60);
				// handler.postDelayed(runMon, 30 * 1000);

			}
			if (isYear) {

				isYear = false;
				handYear.postDelayed(runYear, 8 * 60 * 60 * 1000);
				// handler.postDelayed(runYear, 30 * 1000);

			}
			if (isSave) {
				isSave = false;
				waitTime();
			}
		}

	}

	Handler handHour = new Handler();
	Runnable runHour = new Runnable() {

		@Override
		public void run() {

			isHour = true;
		}
	};

	Handler handDay = new Handler();
	Runnable runDay = new Runnable() {

		@Override
		public void run() {

			isDay = true;
		}
	};

	Handler handMon = new Handler();
	Runnable runMon = new Runnable() {

		@Override
		public void run() {

			isMon = true;
		}
	};

	Handler handYear = new Handler();
	Runnable runYear = new Runnable() {

		@Override
		public void run() {

			isYear = true;
		}
	};

	Handler handler = new Handler();
	Runnable runnable = new Runnable() {

		@Override
		public void run() {

			isAuth = true;
			m_bneedupdate = true;
		}
	};

	@Override
	public boolean updateValue() {

		// linePoint1.clear();
		// chartData.clear();
		if (isMath) {
			if (sizeMath <= 0 || mode == 0 || linePointMapData.size() <= 0)
				return false;

			return updateMathData();

		} else {
			if (cmdList.size() <= 0 || mode == 0
					|| linePointMapData.size() <= 0)
				return false;
			return updateData();
		}

	}

	private boolean updateMathData() {
		chartData = new LinkedList<SplineData>();
		int i = 0;
		for (; i < sizeMath; i++) {

			SgRealTimeData oRealTimeData = m_rRenderWindow.m_oShareObject.m_mapRealTimeDatas
					.get(this.getUniqueID());
			String value = oRealTimeData.strValue;
			// Random random=new Random();
			// String value = random.nextInt(5)+"";
			if (value == null || value.equals("") || value.equals("-999999")) {
				return false;
			}

			String Yeartime = TimeUtils.getYear();
			String Monthtime = TimeUtils.getMonth();
			String Daytime = TimeUtils.getDay();
			String HourTime = TimeUtils.getHour();
			String Mintime = TimeUtils.getMintus();
			String Sintime = TimeUtils.getScroce();

			if (isFirstIN) {
				for (int j = 1; j <= 4; j++) {
					List<LinkedHashMap<Double, Double>> linePointData = linePointMapData
							.get(j);

					readData(linePointData.get(i), "PUE", "", j);					
				}
				readData(oldYearData.get(i), "PUE", "", -1);				
			}

			double time = 0;
			if (isHour) {
				time = Double.parseDouble(Mintime)
						+ (Double.parseDouble(Sintime) / 60);
				Double H = Double.parseDouble(HourTime);
				linePointMapData.get(1).get(i)
						.put(time, Double.parseDouble(value));
				if (H != currentHour) {
					currentHour = H;
					cleanData(1);
				}
			}
			if (isDay) {
				time = Double.parseDouble(HourTime)
						+ (Double.parseDouble(Mintime) / 60)
						+ (Double.parseDouble(Sintime) / 60 / 60);
				Double D = Double.parseDouble(Daytime);
				linePointMapData.get(2).get(i)
						.put(time, Double.parseDouble(value));
				if (D != currentDay) {
					currentDay = D;
					cleanData(2);
				}
			}
			if (isMon) {
				time = Double.parseDouble(Daytime)
						+ (Double.parseDouble(HourTime) / 24)
						+ (Double.parseDouble(Mintime) / 24 / 60)
						+ (Double.parseDouble(Sintime) / 24 / 60 / 60);
				Double M = Double.parseDouble(Monthtime);
				linePointMapData.get(3).get(i)
						.put(time, Double.parseDouble(value));
				if (M != currentMonth) {
					currentMonth = M;
					cleanData(3);
				}
			}
			if (isYear) {
				time = Double.parseDouble(Monthtime)
						+ (Double.parseDouble(Daytime) / 31)
						+ (Double.parseDouble(HourTime) / 31 / 24)
						+ (Double.parseDouble(Mintime) / 31 / 24 / 60)
						+ (Double.parseDouble(Sintime) / 31 / 24 / 60 / 60);
				Double Y = Double.parseDouble(Yeartime);
				linePointMapData.get(4).get(i)
						.put(time, Double.parseDouble(value));
				if (Y != currentYear) {
					currentYear = Y;
					cleanData(4);
				}
			}

			// String name = DataGetter.getSignalName(equail, signal);
			SplineData dataSeries = null;
//
//			if (label_data.size() <= 0) {
//				
//			} else {
//				if (colorData.size() - 1 >= i)
//					dataSeries = new SplineData(label_data.get(i),
//							linePointMapData.get(mode).get(i),
//							Color.parseColor(colorData.get(i)));
//				else
//					dataSeries = new SplineData(label_data.get(i),
//							linePointMapData.get(mode).get(i),
//							(int) Color.parseColor("#FF76A1EC"));
//			}
			
			String year = rButton.get(mode - 1).getText().toString().replace("年", "").trim();
			String currentYear=Integer.parseInt(Yeartime)-1+"";
			// 绑定数据 （名字 颜色 内容 有多种不同的 情况）
			if (label_data.size() <= 0) {
				
			} else {
				if (colorData.size() - 1 >= i) {
					if (!currentYear.equals(year)) {
						dataSeries = new SplineData(label_data.get(i),
								linePointMapData.get(mode).get(i),
								Color.parseColor(colorData.get(i)));
						
					} else {
						System.out.println("oldYearData："+oldYearData.size());
						dataSeries = new SplineData(label_data.get(i), oldYearData.get(i),
								Color.parseColor(colorData.get(i)));
					}
				} else {
					if (!currentYear.equals(year)) {
						dataSeries = new SplineData(label_data.get(i),
								linePointMapData.get(mode).get(i),
								(int) Color.parseColor("#FF76A1EC"));
					} else {
						System.out.println("oldYearData："+oldYearData.size());
						dataSeries = new SplineData(label_data.get(i), oldYearData.get(i),
								Color.parseColor(colorData.get(i)));
					}
				}
			}

			dataSeries.setDotStyle(XEnum.DotStyle.HIDE);
			dataSeries.getDotLabelPaint().setColor(Color.RED);
			dataSeries.getLinePaint().setStrokeWidth(2);
			chartData.add(dataSeries);

			if (isSave) {
				for (int j = 1; j <= 4; j++) {
					List<LinkedHashMap<Double, Double>> linePointData = linePointMapData
							.get(j);
					saveData(linePointData.get(i), "PUE", "", j);
				}
			}
			i++;
		}

		isFirstIN = false;
		m_bneedupdate = false;
		return true;
	}

	private boolean updateData() {
		chartData = new LinkedList<SplineData>();
		int i = 0;
		for (String list : cmdList) {
			String[] str = list.split("-");
			equail = str[0];
			signal = str[2];

			// Random random=new Random();
			// String value = random.nextInt(100)+"";
			String value = DataGetter.getSignalValue(equail, signal);
			if (value == null || value.equals("") || value.equals("-999999")) {
				return false;
			}

			String Yeartime = TimeUtils.getYear();
			String Monthtime = TimeUtils.getMonth();
			String Daytime = TimeUtils.getDay();
			String HourTime = TimeUtils.getHour();
			String Mintime = TimeUtils.getMintus();
			String Sintime = TimeUtils.getScroce();

			if (isFirstIN) {
				for (int j = 1; j <= 4; j++) {
					List<LinkedHashMap<Double, Double>> linePointData = linePointMapData
							.get(j);
					readData(linePointData.get(i), equail, signal, j);
				}
				readData(oldYearData.get(i), equail, signal, -1);
			}

			double time = 0;
			if (isHour) {
				time = Double.parseDouble(Mintime)
						+ (Double.parseDouble(Sintime) / 60);
				Double H = Double.parseDouble(HourTime);
				linePointMapData.get(1).get(i)
						.put(time, Double.parseDouble(value));
				if (H != currentHour) {
					currentHour = H;
					cleanData(1);
				}
			}
			if (isDay) {
				time = Double.parseDouble(HourTime)
						+ (Double.parseDouble(Mintime) / 60)
						+ (Double.parseDouble(Sintime) / 60 / 60);
				Double D = Double.parseDouble(Daytime);
				linePointMapData.get(2).get(i)
						.put(time, Double.parseDouble(value));
				if (D != currentDay) {
					currentDay = D;
					cleanData(2);
				}
			}
			if (isMon) {
				time = Double.parseDouble(Daytime)
						+ (Double.parseDouble(HourTime) / 24)
						+ (Double.parseDouble(Mintime) / 24 / 60)
						+ (Double.parseDouble(Sintime) / 24 / 60 / 60);
				Double M = Double.parseDouble(Monthtime);
				linePointMapData.get(3).get(i)
						.put(time, Double.parseDouble(value));
				if (M != currentMonth) {
					currentMonth = M;
					cleanData(3);
				}
			}
			if (isYear) {
				time = Double.parseDouble(Monthtime)
						+ (Double.parseDouble(Daytime) / 31)
						+ (Double.parseDouble(HourTime) / 31 / 24)
						+ (Double.parseDouble(Mintime) / 31 / 24 / 60)
						+ (Double.parseDouble(Sintime) / 31 / 24 / 60 / 60);
				Double Y = Double.parseDouble(Yeartime);
				linePointMapData.get(4).get(i)
						.put(time, Double.parseDouble(value));
				if (Y != currentYear) {
					currentYear = Y;
					cleanData(4);
				}
			}

			String name = DataGetter.getSignalName(equail, signal);
			SplineData dataSeries = null;
			String year = rButton.get(mode - 1).getText().toString().replace("年", "").trim();
            String currentYear=Integer.parseInt(Yeartime)-1+"";
			// 绑定数据 （名字 颜色 内容 有多种不同的 情况）
          
			if (label_data.size() <= 0) {
				if (colorData.size() - 1 >= i) {
					
					if (!currentYear.equals(year)) {
						dataSeries = new SplineData(name, linePointMapData.get(
								mode).get(i),
								Color.parseColor(colorData.get(i)));
					} else {
						
						dataSeries = new SplineData(name, oldYearData.get(i),
								Color.parseColor(colorData.get(i)));
					}

				} else {
				
					if (!currentYear.equals(year)) {
						dataSeries = new SplineData(name, linePointMapData.get(
								mode).get(i),
								(int) Color.parseColor("#FF76A1EC"));
					} else {
					
						dataSeries = new SplineData(name, oldYearData.get(i),
								Color.parseColor(colorData.get(i)));
					}
				}
			} else {
			
				if (colorData.size() - 1 >= i) {
					
					if (!currentYear.equals(year)) {
						dataSeries = new SplineData(label_data.get(i),
								linePointMapData.get(mode).get(i),
								Color.parseColor(colorData.get(i)));
					} else {
						System.out.println(i+";;;;;;;"+oldYearData.get(i).size());
						dataSeries = new SplineData(label_data.get(i), oldYearData.get(i),
								Color.parseColor(colorData.get(i)));
					}
				} else {
					
					if (!currentYear.equals(year)) {
						dataSeries = new SplineData(label_data.get(i),
								linePointMapData.get(mode).get(i),
								(int) Color.parseColor("#FF76A1EC"));
					} else {
						dataSeries = new SplineData(label_data.get(i), oldYearData.get(i),
								Color.parseColor(colorData.get(i)));
					}
				}
			}

			dataSeries.setDotStyle(XEnum.DotStyle.HIDE);
			dataSeries.getDotLabelPaint().setColor(Color.RED);
			dataSeries.getLinePaint().setStrokeWidth(2);
			chartData.add(dataSeries);

			if (isSave) {
				for (int j = 1; j <= 4; j++) {
					List<LinkedHashMap<Double, Double>> linePointData = linePointMapData
							.get(j);
					saveData(linePointData.get(i), equail, signal, j);
				}
			}
			i++;
		}

		isFirstIN = false;
		m_bneedupdate = false;
		return true;
	}

	private void waitTime() {
		handler2.postDelayed(runnable2, 1000 * 60 * 60 * 4);
	}

	Handler handler2 = new Handler();
	Runnable runnable2 = new Runnable() {

		@Override
		public void run() {
			isSave = true;
		}
	};

	private void saveData(LinkedHashMap<Double, Double> data, String eqstr,
			String sistr, int index) {
		String Yeartime = TimeUtils.getYear();
		String Monthtime = TimeUtils.getMonth();
		String Daytime = TimeUtils.getDay();
		String Hourtime = TimeUtils.getHour();
		String fileName = "";
		try {
			switch (index) {
			case 1:// 一小时

				fileName = RC_signal + Yeartime + "-" + Monthtime + "-"
						+ Daytime + "-" + Hourtime + "-" + eqstr + "-" + sistr;
				return;
			case 2:// 一天
				fileName = RC_signal + Yeartime + "-" + Monthtime + "-"
						+ Daytime + "-" + eqstr + "-" + sistr;
				break;
			case 3:// 一月
				fileName = RC_signal + Yeartime + "-" + Monthtime + "-" + eqstr
						+ "-" + sistr;
				break;
			case 4:// 一年
				fileName = RC_signal + Yeartime + "-" + eqstr + "-" + sistr;
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

	private void readData(LinkedHashMap<Double, Double> data, String eqstr,
			String sistr, int index) {
		String Yeartime = TimeUtils.getYear();
		String Monthtime = TimeUtils.getMonth();
		String Daytime = TimeUtils.getDay();
		String Hourtime = TimeUtils.getHour();
		String fileName = "";
		try {
			switch (index) {
			// 去年
			case -1:
				Yeartime = Integer.parseInt(Yeartime) - 1 + "";
				fileName = RC_signal + Yeartime + "-" + eqstr + "-" + sistr;
				System.out.println(fileName);
				break;
			case 1:// 一小时

				currentHour = Double.parseDouble(Hourtime);
				fileName = RC_signal + Yeartime + "-" + Monthtime + "-"
						+ Daytime + "-" + Hourtime + "-" + eqstr + "-" + sistr;
				break;
			case 2:// 一天
				currentDay = Double.parseDouble(Daytime);
				fileName = RC_signal + Yeartime + "-" + Monthtime + "-"
						+ Daytime + "-" + eqstr + "-" + sistr;
				break;
			case 3:// 一月
				currentMonth = Double.parseDouble(Monthtime);
				fileName = RC_signal + Yeartime + "-" + Monthtime + "-" + eqstr
						+ "-" + sistr;
				break;
			case 4:// 一年
				currentYear = Double.parseDouble(Yeartime);
				fileName = RC_signal + Yeartime + "-" + eqstr + "-" + sistr;
				break;
			}
			File f = new File(fileName);
			if (f.exists()) {
				BufferedReader br = new BufferedReader(new InputStreamReader(
						new FileInputStream(f), "gb2312"));
				String str = "";
				while ((str = br.readLine()) != null) {
					String[] s = str.split("-");
					data.put(Double.parseDouble(s[0]), Double.parseDouble(s[1]));
				}
				br.close();
			}
		} catch (Exception e) {

		}
	}

	private void compareMax(String value) {
		double i = Double.parseDouble(value);
		if (i > max_value) {
			max_value = (int) i + 20;
		}
		Schart.getDataAxis().setAxisMax(max_value);
		// chart.getDataAxis().setAxisMin(0);
		// 数据轴刻度间隔
		Schart.getDataAxis().setAxisSteps(max_value / 5);
	}

	private void cleanData(int index) {
		List<LinkedHashMap<Double, Double>> linePointData = linePointMapData
				.get(index);
		for (int j = 0; j < linePointData.size(); j++) {
			linePointData.get(j).clear();
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
	SplineChart03View chart = null;
	public boolean m_bneedupdate = true;
	private String mExpression = "";
	private List<String> cmdList = new ArrayList<String>();
	private String labelData = "";

	private List<String> colorData = new ArrayList<String>();

	private int max_value = 100;
	private int AxisSteps = 20;
	private int ycount = 5;
	private int mode = 1;
	private double currentYear = -1, currentMonth = -1, currentDay = -1,
			currentHour = -1;
	private boolean isSave = false;
	private boolean isFirstIN = true;
	private String RC_signal = "/mgrid/log/RC_signal/";
	private boolean isHour = true, isDay = true, isMon = true, isYear = true;
	private boolean isAuth = true;
	private List<String> label_data = new ArrayList<String>();
	private boolean isMath = false;
	private int sizeMath = 0;
	private String selectYear = "";

}
