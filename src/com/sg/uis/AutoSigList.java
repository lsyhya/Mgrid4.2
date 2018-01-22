package com.sg.uis;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.view.View;
import android.widget.RadioButton;

import com.mgrid.main.MainWindow;
import com.sg.common.IObject;
import com.sg.common.SgRealTimeData;

/** 动态后台实时信号曲线（可绑定一个式子结果） **/
// author：fjw0312
// made time：2015.12.4
// 该控件采集
@SuppressLint({ "UseSparseArrays", "DrawAllocation", "SimpleDateFormat" })
public class AutoSigList extends View implements IObject {

	public AutoSigList(Context context) {
		super(context);

		m_rBBox = new Rect();
		m_oPaint = new Paint();
		m_2Paint = new Paint();
		m_oPaint.setTextSize(m_fFontSize);
		m_oPaint.setColor(m_nFontColor);
		m_oPaint.setAntiAlias(true);
		m_oPaint.setStyle(Paint.Style.STROKE);

		map_Htime_vlaue = new HashMap<Integer, String>();
		map_Dtime_vlaue = new HashMap<Integer, String>();
		map_Mtime_vlaue = new HashMap<Integer, String>();
		map_Ytime_vlaue = new HashMap<Integer, String>();
		Htimelist = new ArrayList<Integer>();
		Dtimelist = new ArrayList<Integer>();
		Mtimelist = new ArrayList<Integer>();
		Ytimelist = new ArrayList<Integer>();

		ridobuttons = new RadioButton[4];
		ridobuttons[0] = new RadioButton(context);
		ridobuttons[0].setText("1 h");
		ridobuttons[0].setChecked(true);
		ridobuttons[1] = new RadioButton(context);
		ridobuttons[1].setText("24 h");
		ridobuttons[2] = new RadioButton(context);
		ridobuttons[2].setText("1 mon");
		ridobuttons[3] = new RadioButton(context);
		ridobuttons[3].setText("1 year");
		for (int i = 0; i < 4; i++) {
			ridobuttons[i].setTextColor(m_nFontColor);
			ridobuttons[i].setOnClickListener(l);
		}
	}

	private OnClickListener l = new OnClickListener() {
		@Override
		public void onClick(View arg0) {

			String strText = (String) ((RadioButton) arg0).getText();
			for (int i = 0; i < 4; i++)
				ridobuttons[i].setChecked(false);
			if ("1 h".equals(strText)) {
				mode = 0;
			} else if ("24 h".equals(strText)) {
				mode = 1;
			} else if ("1 mon".equals(strText)) {
				mode = 2;
			} else if ("1 year".equals(strText)) {
				mode = 3;
			}
			ridobuttons[mode].setChecked(true);
			updateWidget();
		}
	};

	@Override
	// 画布绘制
	protected void onDraw(Canvas canvas) {
	//	System.out.println("呵呵");
		if (m_rRenderWindow == null)
			return;
		if (m_rRenderWindow.isLayoutVisible(getBBox()) == false)
			return;

		int num = 12 + 1;
		if (mode == 1) {
			num = 24 + 1;
		} else if (mode == 2) {
			num = 31 + 1;
		} else if (mode == 3) {
			num = 12 + 1;
		}
		float pad = 40;
		float x_start = pad;
		float x_end = m_nWidth - pad;
		float y_start = m_nHeight - pad;
		float y_end = pad;
		float x_lenth = x_end - x_start - pad / 2;
		float y_lenth = y_start - y_end - pad / 2;
		float x_unit1 = x_lenth / (num - 1);
		float y_unit1 = y_lenth / 10;

		m_oPaint.setColor(m_nLineColor);
		m_oPaint.setTextSize(20);
		canvas.drawLine(x_start, y_start, x_end, y_start, m_oPaint);
		canvas.drawLine(x_start, y_start, x_start, y_end, m_oPaint);
		for (int i = 0; i < 11; i++) {
			canvas.drawLine(x_start, y_start - y_unit1 * i, x_start + 5,
					y_start - y_unit1 * i, m_oPaint); // 画y轴刻度线
		}
		for (int i = 0; i < num; i++) {
			canvas.drawLine(x_start + x_unit1 * i, y_start, x_start + x_unit1
					* i, y_start - 5, m_oPaint); // 画x轴刻度线
		}

		float y_max = (((int) max_value[mode] / 10) + 1) * 10; // 整数
		float y_p = y_max / 10;

		m_oPaint.setTextSize(12);
		for (int i = 0; i < 11; i++) {
			if (y_lenth < 300) {
				if (i % 2 != 0)
					continue;
			}
			DecimalFormat decimalFloat = new DecimalFormat("0.0");
			String str_yp = decimalFloat.format(y_p * i);
			canvas.drawText(str_yp, pad / 8, y_start - y_unit1 * i, m_oPaint);
			decimalFloat = null;
		}
		for (int i = 0; i < num; i++) {
			if (mode == 0 && x_lenth < 300) {
				if (i % 2 != 0)
					continue;
			}
			if (mode == 1 && x_lenth < 300) {
				if (i % 4 != 0)
					continue;
			}
			if (mode == 1 && x_lenth < 600) {
				if (i % 2 != 0)
					continue;
			}
			if (mode == 2 && x_lenth < 500) {
				if (i % 2 != 0)
					continue;
			}
			if (mode == 3 && x_lenth < 200) {
				if (i % 2 != 0)
					continue;
			}
			canvas.drawText(x_markLine[mode][i], x_start + x_unit1 * i,
					m_nHeight - pad / 2, m_oPaint); // 画x轴标签
		}

		m_2Paint.setColor(m_nFontColor);
		m_2Paint.setStyle(Style.FILL);
		m_2Paint.setStrokeWidth((float) 2.0);
		HashMap<Integer, String> map = new HashMap<Integer, String>();
		List<Integer> time_list = new ArrayList<Integer>();
		float xv_unit = 0;
		float yv_unit = y_lenth / y_max;
		if (mode == 0) {
			map = map_Htime_vlaue;
			time_list = Htimelist;
		//	System.out.println(map_Htime_vlaue.size()+"::::"+Htimelist.size());
			xv_unit = (float) (x_lenth) / 3600;
		} else if (mode == 1) {
			map = map_Dtime_vlaue;
			time_list = Dtimelist;

			xv_unit = (float) (x_lenth) / 60 / 24;

		} else if (mode == 2) {
			map = map_Mtime_vlaue;
			time_list = Mtimelist;

			xv_unit = (float) (x_lenth) / 31;

		} else if (mode == 3) {
			map = map_Ytime_vlaue;
			time_list = Ytimelist;

			xv_unit = (float) (x_lenth) / 12;

		}
		float pre_x = 0;
		float pre_y = 0;

		Iterator<Integer> keylist = time_list.iterator();
		while (keylist.hasNext()) {
			int ii_time = keylist.next();
			if (mode == 0 && ii_time > old_time) {
				for (int i = 0; i < Htimelist.size(); i++) {
					if (Htimelist.get(i).equals(ii_time)){
						Htimelist.remove(i);
						break;
					}
				}
				map_Htime_vlaue.remove(ii_time);
				return;
			}
			String s_value = map.get(ii_time);
			if(s_value==null)
				continue;
			float i_value = Float.parseFloat(s_value);

			if ("-999999".equals(s_value))
				continue;

			float now_x = x_start + ii_time * xv_unit;
			float now_y = y_start - i_value * yv_unit;

			if (now_y > y_start)
				continue;
			// canvas.drawPoint(now_x, now_y, m_oPaint);
			canvas.drawCircle(now_x, now_y, 1, m_2Paint);

			if ((pre_x != 0) || (pre_y != 0)) {
				if (pre_x <= now_x) {
					canvas.drawLine(pre_x, pre_y, now_x, now_y, m_2Paint);
				}
			}
			pre_x = now_x;
			pre_y = now_y;

		}
		map = null;
		time_list = null;

		super.onDraw(canvas);
	}

	@Override
	// 绘制底板
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

			for (int i = 0; i < 4; i++)
				ridobuttons[i].layout(nX + i * nWidth / 4, nY, nX + i * nWidth
						/ 4 + nWidth / 4, nY + 20);
		}
	}

	@Override
	public void addToRenderWindow(MainWindow rWin) {
		m_rRenderWindow = rWin;
		rWin.addView(this);
		for (int i = 0; i < ridobuttons.length; i++)
			rWin.addView(ridobuttons[i]);
	}

	@Override
	public void removeFromRenderWindow(MainWindow rWin) {
		m_rRenderWindow = null;
		for (int i = 0; i < ridobuttons.length; ++i)
			rWin.removeView(ridobuttons[i]);
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
		} else if ("Expression".equals(strName)) {
			m_strExpression = strValue;
		} else if ("FontSize".equals(strName)) {
			m_fFontSize = Float.parseFloat(strValue);
			this.setFontSize(m_fFontSize);
		} else if ("FontColor".equals(strName)) {
			m_nFontColor = Color.parseColor(strValue);
			this.setFontColor(m_nFontColor);
			for (int i = 0; i < 4; i++) {
				ridobuttons[i].setTextColor(m_nFontColor);
			}
			
		} else if ("LineColor".equals(strName)) {
			m_nLineColor = Color.parseColor(strValue);
			this.setLineColor(m_nLineColor);
		} else if ("BackgroundColor".equals(strName)) {
			BackgroundColor = Color.parseColor(strValue);
			this.setBackgroundColor(BackgroundColor);
		} else if ("mode".equals(strName)) {
			if ("".equals(strValue))
				mode = 0;
			else
				mode = Integer.parseInt(strValue);
		}
	}

	public View getView() {
		return this;
	}

	public Rect getBBox() {
		return m_rBBox;
	}

	@Override
	public void initFinished() {
	}

	public void setUniqueID(String strID) {
		m_strID = strID;
	}

	public String getUniqueID() {
		return m_strID;
	}

	public void setType(String strType) {
		m_strType = strType;
	}

	public String getType() {
		return m_strType;
	}

	public int getZIndex() {
		return m_nZIndex;
	}

	public String getBindingExpression() {
		return m_strExpression;
	}

	public void updateWidget() {
		this.invalidate();
	}

	@Override
	public boolean needupdate() {
		return m_bneedupdate;
	}

	@Override
	public void needupdate(boolean bNeedUpdate) {
		m_bneedupdate = bNeedUpdate;
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
		oRealTimeData = null;

		long time = java.lang.System.currentTimeMillis();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date(time);
		String sampletime = formatter.format(date);
	    String year = sampletime.substring(0, 4);
		String mon = sampletime.substring(5, 7);
		String day = sampletime.substring(8, 10);
		String hour = sampletime.substring(11, 13);
		String min = sampletime.substring(14, 16);
		String sec = sampletime.substring(17, 19);
	//	System.out.println(sampletime);
		int i_sec = Integer.parseInt(sec);
		int i_min = Integer.parseInt(min);
		int i_hour = Integer.parseInt(hour);
		int i_day = Integer.parseInt(day);
		int i_mon = Integer.parseInt(mon);
		int dd_time = i_min * 60 + i_sec;
		old_time = dd_time;
		formatter = null;
		date = null;
		newTime = System.currentTimeMillis();
		if (newTime - m_oldTime < 10000) {

			if(m_oldTime-newTime>0)
			{
				m_oldTime = newTime;
			}
			return false;
		}
		
		m_oldTime = newTime;
		if (strValue.equals("0.0")) {
			if (zero_Time == 0 || newTime - zero_Time > 1000 * 30) {
				zero_Time = newTime;
				return false;
			}

		}
		
		map_Htime_vlaue.put(dd_time, strValue);
		max_value[0] = get_max_vlaue(map_Htime_vlaue);
		Htimelist.add(dd_time);
		
		if (old_hour.equals(""))
			old_hour = hour;
		if (old_day.equals(""))
			old_day = day;
		if (old_Mon.equals(""))
			old_Mon = mon;
		if (old_Year.equals(""))
			old_Year = year;
		
		
		if (!old_hour.equals(hour)) {
			old_hour = hour;
			Htimelist.clear();
			Htimelist = null; 
			Htimelist = new ArrayList<Integer>();
			map_Htime_vlaue.clear();
			map_Htime_vlaue = null;
			map_Htime_vlaue = new HashMap<Integer, String>();
		}

		if (newTime - d_oldTime > 1000 * 60 * 10) {

			d_oldTime = newTime;
			map_Dtime_vlaue.put(i_hour * 60 + i_min, strValue);
			max_value[1] = get_max_vlaue(map_Dtime_vlaue);
			Dtimelist.add(i_hour * 60 + i_min);

			if (!old_day.equals(day)&& (map_Dtime_vlaue != null)) {

				String m_value = String.valueOf(get_aver(map_Dtime_vlaue));
				map_Mtime_vlaue.put(i_day, m_value);
				max_value[2] = get_max_vlaue(map_Mtime_vlaue); // 计算出链表的最大值
				Mtimelist.add(i_day);
 
					old_day=day;
					Dtimelist.clear();
					Dtimelist = null;
					Dtimelist = new ArrayList<Integer>();
					map_Dtime_vlaue.clear();
					map_Dtime_vlaue = null;
					map_Dtime_vlaue = new HashMap<Integer, String>();
				
			}
		}
	

		if (!old_Mon.equals(mon)&& (map_Mtime_vlaue != null)) {
			old_Mon=mon;
			String y_value = String.valueOf(get_aver(map_Mtime_vlaue));
			if (i_mon == 1)
				i_mon = 13;
			map_Ytime_vlaue.put(i_mon - 1, y_value);
			max_value[3] = get_max_vlaue(map_Ytime_vlaue);
			Ytimelist.add(i_mon - 1);

			Mtimelist.clear();
			Mtimelist = null;
			Mtimelist = new ArrayList<Integer>();
			map_Mtime_vlaue.clear();
			map_Mtime_vlaue = null;
			map_Mtime_vlaue = new HashMap<Integer, String>();

		}

		if (!old_Year.equals(year)&& (map_Ytime_vlaue != null)) {
			old_Year=year;
			Ytimelist.clear();
			Ytimelist = null;
			Ytimelist = new ArrayList<Integer>();
			map_Ytime_vlaue.clear();
			map_Ytime_vlaue = null;
			map_Ytime_vlaue = new HashMap<Integer, String>();
		}

		if (Htimelist.size() > 2000 || map_Htime_vlaue.size() > 2000) {
			Htimelist.clear();
			map_Htime_vlaue.clear();
		}
		if (Dtimelist.size() > 300 || map_Dtime_vlaue.size() > 300) {
			Dtimelist.clear();
			map_Dtime_vlaue.clear();
		}
		if (Mtimelist.size() > 40 || map_Mtime_vlaue.size() > 40) {
			Mtimelist.clear();
			map_Mtime_vlaue.clear();
		}
		if (Ytimelist.size() > 20 || map_Ytime_vlaue.size() > 20) {
			Ytimelist.clear();
			map_Ytime_vlaue.clear();
		}
       
		return true;
	}

	public float get_max_vlaue(HashMap<Integer, String> map) {
		float max_value = 0;

		Iterator<Integer> keylist = map.keySet().iterator();
		while (keylist.hasNext()) {
			String g_value = map.get(keylist.next());
			float f_value = Float.parseFloat(g_value);
			if (f_value > max_value)
				max_value = f_value;
		}
		return max_value;
	}

	public float get_aver(HashMap<Integer, String> map) {
		float aver_value = 0;
		float num = 0;
		int i = 0;

		Iterator<Integer> keylist = map.keySet().iterator();
		while (keylist.hasNext()) {
			String g_value = map.get(keylist.next());
			float f_value = Float.parseFloat(g_value);
			if (f_value == 0)
				continue;
			num += f_value;
			i++;
		}
		aver_value = num / i;

		return aver_value;
	}

	public void setFontSize(float FontSize) {
		m_fFontSize = FontSize;
	}

	public void setFontColor(int FontColor) {
		m_nFontColor = FontColor;
	}

	public void setLineColor(int LineColor) {
		m_nLineColor = LineColor;
	}

	String m_strID = "";
	String m_strType = "";
	int m_nZIndex = 20;
	int m_nPosX = 300;
	int m_nPosY = 397;
	int m_nWidth = 150;
	int m_nHeight = 137;
	float m_fAlpha = 1.0f;
	String m_strExpression = null;
	float m_fFontSize = 20.0f;
	int m_nFontColor = 0xFFFF0000;
	int m_nLineColor = 0xFFFF0000;
	int BackgroundColor = 0xFF000000;

	Rect m_rBBox = null;
	Paint m_oPaint = null;
	Paint m_2Paint = null;
	MainWindow m_rRenderWindow = null;
	public boolean m_bneedupdate = true;
	RadioButton[] ridobuttons;

	String x_markLine[][] = {
			{ "0", "5", "10", "15", "20", "25", "30", "35", "40", "45", "50",
					"55", "60" },
			{ "0", "1:00", "2:00", "3:00", "4:00", "5:00", "6:00", "7:00",
					"8:00", "9:00", "10:00", "11:00", "12:00", "13:00",
					"14:00", "15:00", "16:00", "17:00", "18:00", "19:00",
					"20:00", "21:00", "22:00", "23:00", "24:00" },
			{ "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11",
					"12", "13", "14", "15", "16", "17", "18", "19", "20", "21",
					"22", "23", "24", "25", "26", "27", "28", "29", "30", "31" },
			{ "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11",
					"12" } };
	int mode = 0;
	HashMap<Integer, String> map_Htime_vlaue = null;
	HashMap<Integer, String> map_Dtime_vlaue = null;
	HashMap<Integer, String> map_Mtime_vlaue = null;
	HashMap<Integer, String> map_Ytime_vlaue = null;
	List<Integer> Htimelist = null;
	List<Integer> Dtimelist = null;
	List<Integer> Mtimelist = null;
	List<Integer> Ytimelist = null;

	float max_value[] = { 0, 0, 0, 0 };
	public static int old_min = 0;
	private long m_oldTime = 0;// 一个小时的时间判断
	private long newTime = 0;// 当前的时间
	private long d_oldTime = 0;// 一天的时间判断
	private long zero_Time = 0;// 数值为零的时间判断
	private String old_hour = "";
	private String old_day = "";
	private String old_Mon = "";
	private String old_Year = "";
	
	private int old_time;
}
