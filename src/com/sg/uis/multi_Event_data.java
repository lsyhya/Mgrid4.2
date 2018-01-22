package com.sg.uis;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import com.mgrid.main.MainWindow;
import com.sg.common.IObject;
import com.sg.common.UtExpressionParser.stBindingExpression;
import com.sg.common.UtExpressionParser.stExpression;
import com.sg.common.UtExpressionParser.stIntervalExpression;
import comm_service.local_file;

@SuppressLint("ClickableViewAccessibility")
@SuppressWarnings("unused")
/** 多数据控件 fjw test */
public class multi_Event_data extends TextView implements IObject {
	public multi_Event_data(Context context) {
		super(context);
		// this.setClickable(true);
		// // this.setGravity(Gravity.CENTER);
		// this.setFocusableInTouchMode(true);
		// setBackgroundResource(android.R.drawable.btn_default);
		// setPadding(0, 0, 0, 0);
		// this.setTextColor(Color.RED);

		m_oPaint = new Paint();
		m_rBBox = new Rect();
		strValue_list = new ArrayList<String>();

		startTimeBtn = new Button(context);
		startTimeBtn.setText("开始时间");
		startTimeBtn.setTextColor(Color.BLACK);
		startTimeBtn.setTextSize(13);
		startTimeBtn.setPadding(2, 2, 2, 2);
		startTimeBtn.setOnClickListener(l);
		startTimeBtn
				.setBackgroundResource(android.R.drawable.btn_default_small);
		// startTimeBtn.setVisibility(View.INVISIBLE);

		endTimeBtn = new Button(context);
		endTimeBtn.setText("结束时间");
		endTimeBtn.setTextColor(Color.BLACK);
		endTimeBtn.setTextSize(13);
		endTimeBtn.setPadding(2, 2, 2, 2);
		endTimeBtn.setOnClickListener(l);
		endTimeBtn.setBackgroundResource(android.R.drawable.btn_default_small);
		// endTimeBtn.setVisibility(View.INVISIBLE);

		this.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				return true;
			}
		});

		calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		startDialog = new DatePickerDialog(context, new OnDateSetListener() {
			@Override
			public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
				// TODO Auto-generated method stub

			}
		}, year, month, day);

		endDialog = new DatePickerDialog(context, new OnDateSetListener() {

			@Override
			public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
				// TODO Auto-generated method stub

			}
		}, year, month, day);

	}

	private OnClickListener l = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			if (arg0 == startTimeBtn) {

				startDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "设置",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								int set_year = startDialog.getDatePicker()
										.getYear();
								int set_month = startDialog.getDatePicker()
										.getMonth() + 1;
								int set_day = startDialog.getDatePicker()
										.getDayOfMonth();

								String set_months, set_days;
								if (set_day < 10) {
									set_days = "0" + String.valueOf(set_day);
								} else {
									set_days = String.valueOf(set_day);
								}
								if (set_month < 10) {
									set_months = "0"
											+ String.valueOf(set_month);
								} else {
									set_months = String.valueOf(set_month);
								}
								startTimeBtn.setText(String.valueOf(set_year)
										+ "-" + set_months + "-" + set_days);
								isStart = true;
							}
						});

				startDialog.show();

				return;

			} else if (arg0 == endTimeBtn) {
				endDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "设置",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								int set_year = endDialog.getDatePicker()
										.getYear();
								int set_month = endDialog.getDatePicker()
										.getMonth() + 1;
								int set_day = endDialog.getDatePicker()
										.getDayOfMonth();

								String set_months, set_days;
								if (set_day < 10) {
									set_days = "0" + String.valueOf(set_day);
								} else {
									set_days = String.valueOf(set_day);
								}
								if (set_month < 10) {
									set_months = "0"
											+ String.valueOf(set_month);
								} else {
									set_months = String.valueOf(set_month);
								}
								endTimeBtn.setText(String.valueOf(set_year)
										+ "-" + set_months + "-" + set_days);
								isEnd = true;
							}
						});
				endDialog.show();
				isupdate = true;
				return;
			}

		}
	};

	@SuppressLint("DrawAllocation")
	protected void onDraw(Canvas canvas) {
		// Log.e("multi_data-onDraw","into");
		if (m_rRenderWindow == null)
			return;
		if (m_rRenderWindow.isLayoutVisible(getBBox()) == false)
			return;

		m_oPaint.setColor(m_cSingleFillColor);
		m_oPaint.setAntiAlias(true); // 设置画笔的锯齿效果
		m_oPaint.setStrokeWidth(40);
		m_oPaint.setStyle(Paint.Style.FILL);

		int nWidth = (int) (((float) (m_nWidth) / (float) MainWindow.FORM_WIDTH) * (m_rRenderWindow.VIEW_RIGHT - m_rRenderWindow.VIEW_LEFT));
		int nHeight = (int) (((float) (m_nHeight) / (float) MainWindow.FORM_HEIGHT) * (m_rRenderWindow.VIEW_BOTTOM - m_rRenderWindow.VIEW_TOP));
		// 画出基础圆
		RectF rect = new RectF(0, 0, (float) (nWidth / 10 * 7),
				(float) (nHeight / 10 * 7)); // 80%的面积显示圆饼
		canvas.drawArc(rect, // 弧线所使用的矩形区域大小
				0, // 开始角度
				360, // 扫过的角度
				true, // 是否使用中心
				m_oPaint);
		// Log.e("multi_data-onDraw","控件id:"+m_strID+"--into  3333");

		m_oPaint.setStyle(Style.FILL); // 设置画笔为实心
		m_oPaint.setTextSize(40); // 设置字体大小

		// 遍历出各个数据
		int i = 0;
		float angle = 0;
		Iterator<String> iter = strValue_list.iterator();
		while (iter.hasNext()) {
			String value = iter.next(); // 获得数据
			if ("".equals(value))
				break;
			float data = Float.parseFloat(value);
			float data1 = data * 360;
			m_oPaint.setColor(m_DataColor[i]); // 设置画笔颜色
			canvas.drawArc(rect, // 弧线所使用的矩形区域大小
					angle, // 开始角度
					data1, // 扫过的角度
					true, // 是否使用中心
					m_oPaint);
			angle = angle + data1;
			float r_lenth = (float) nWidth / 10 * 2 / 5;
			float r_x = (float) nWidth / 10 * 8 + 4;
			float r_y = (float) nHeight / 20 + (nHeight / 20 + r_lenth) * i;
			canvas.drawRect(r_x, r_y, r_x + r_lenth, r_y + r_lenth, m_oPaint); // 画方块
			m_oPaint.setTextSize(r_lenth); // 设置字体大小
			DecimalFormat decimalFloat = new DecimalFormat("0.00"); // float小数点精度处理
			String strValue = decimalFloat.format(data * 100);
			canvas.drawText(strValue, r_x + r_lenth * 3 / 2, r_y + r_lenth,
					m_oPaint); // 画标签

			i++;
		}

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
			startTimeBtn.layout(nX, nY, nX + (int) (0.3f * nWidth), nY
					+ (int) (0.13f * nHeight));
			endTimeBtn.layout(nX + (int) (0.4f * nWidth), nY, nX
					+ (int) (0.7f * nWidth), nY + (int) (0.13f * nHeight));
			this.layout(nX, nY + (int) (0.14f * nHeight), nX + nWidth, nY
					+ nHeight);
		}
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
		rWin.addView(startTimeBtn);
		rWin.addView(endTimeBtn);
	}

	@Override
	public void removeFromRenderWindow(MainWindow rWin) {
		m_rRenderWindow = null;
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
		} else if ("RotateAngle".equals(strName)) {
			m_fRotateAngle = Float.parseFloat(strValue);
		} else if ("ForeColor".equals(strName)) {
			m_cSingleFillColor = Color.parseColor(strValue);
		} else if ("DataColor".equals(strName)) {
			String strData[] = strValue.split("\\|");
			for (int i = 0; i < strData.length; i++) {
				m_DataColor[i] = Color.parseColor(strData[i]);
			}
		} else if ("IsDashed".equals(strName)) {
			m_bIsDashed = Boolean.parseBoolean(strValue);
		} else if ("Radius".equals(strName))
			m_fRadius = Float.parseFloat(strValue);
		else if ("Effect".equals(strName))
			m_strEffect = strValue;
		else if ("Expression".equals(strName)) {
			m_strMultiExpression = strValue;

		}
	}

	@Override
	public void initFinished() {
	}

	public String getBindingExpression() {
		return m_strMultiExpression;
	}

	// 设备更新
	public void updateWidget() {
		this.invalidate();
	}

	@Override
	public boolean updateValue()// 这里的实时更新是通过判断手机内存中告警文本中的行数实行判断的
	{
		m_bneedupdate = true;
		if (beforeTime == 0) {
			beforeTime = System.currentTimeMillis();
		} else {
			afterTime = System.currentTimeMillis();
			if (afterTime - beforeTime >= 5*1000) {
				beforeTime = afterTime;
			} else {
				return false;
			}
		}

		if (m_strMultiExpression == null)
			return false;
		// 获得数据
		stExpression stE = m_rRenderWindow.Event_data.get(this.getUniqueID());

		if (stE == null)
			return false;

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date dt = null;
		Date dt1 = null;
		long lstime = 0;
		long letime = 0;

		Iterator<HashMap.Entry<String, stBindingExpression>> it = stE.mapObjectExpress
				.entrySet().iterator();
		list.clear();
		int allCount = 0;
		if (isStart && isEnd) {

			startTime = startTimeBtn.getText().toString();
			endTime = endTimeBtn.getText().toString();
			//System.out.println(startTime + ":" + endTime);
			try {
				dt = sdf.parse(startTime);
				dt1 = sdf.parse(endTime);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			lstime = dt.getTime();
			letime = dt1.getTime();
		}
		while (it.hasNext()) {
			stBindingExpression stB = it.next().getValue();
			int EquipId = stB.nEquipId;
			if (EquipId == -1)
				continue;
			String filename = "hisevent-" + EquipId;
			local_file l_file = new local_file();

			if (!l_file.has_file(filename, 3)) {
				strValue_list.clear();
				return true;
			}
			if (isStart && isEnd) {

				if (!l_file.read_time_line(lstime, letime)) {
					return false;
				}
			} else {

				if (!l_file.read_all_line()) {
					return false;
				}
			}		
			list.add((double) local_file.r_line_num);
			allCount += local_file.r_line_num;
			
		}

		// if (old_list != null) {
		// for (int i = 0; i < old_list.size(); i++) {
		// if (old_list.get(i) != list.get(i)) {
		// isupdate = true;
		// System.out.println("更新");
		// break;
		// }
		// }
		// }

		if (isupdate) {
			strValue_list.clear();
			for (int i = 0; i < list.size(); i++) {
				double d = list.get(i) / allCount;
				strValue_list.add(d + "");
			}
		}

		// old_list = list;
		// isupdate = false;
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
	String m_strType = "";
	int m_nZIndex = 3;
	int m_nPosX = 94;
	int m_nPosY = 9;
	int m_nWidth = 200;
	int m_nHeight = 150;
	float m_fAlpha = 1.0f;
	float m_fRotateAngle = 0.0f;
	int m_cSingleFillColor = 0x00000000;
	int[] m_DataColor = new int[50];
	boolean m_bIsDashed = false;
	float m_fRadius = 0.0f;
	String m_strStateExpression = "";
	String m_strEffect = "";
	String m_strMultiExpression = "";
	boolean m_bIsHGradient = true; // 水平渐变
	MainWindow m_rRenderWindow = null;

	int m_nSignalValue = -1;
	// stMathExpression m_oMathExpression = null;
	stIntervalExpression m_oColorIntervalExpression;
	String old_string = "";
	List<String> strValue_list = null; // 数据链表变量
	int value_nember = 0; // 数据个数

	Paint m_oPaint = null;
	Rect m_rBBox = null;

	private Button startTimeBtn, endTimeBtn;
	private DatePickerDialog startDialog = null;
	private DatePickerDialog endDialog = null;

	private Calendar calendar = null;
	private boolean isStart = false; // 按钮上是否有时间
	private boolean isEnd = false;
	boolean isupdate = true;
	private boolean m_bneedupdate = true;
	private String startTime, endTime; // 按钮上的显示时间

	private long beforeTime = 0;// 之前时间
	private long afterTime = 0;// 当前时间
	// private List<Double> old_list = new ArrayList<Double>();
	private List<Double> list = new ArrayList<Double>();

}
