package com.sg.uis;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import junit.framework.Test;

import com.mgrid.data.EquipmentDataModel.Signal;
import com.mgrid.main.MainWindow;
import com.sg.common.IObject;
import com.sg.common.UtTable;
import com.sg.common.UtTableAdapter;

import data_model.ipc_history_signal;
import data_model.local_his_signal;

import android.R.integer;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Paint.Style;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;

/** 历史信号 */
//author :fjw0312
//time:2015 8 20
public class LocalList extends UtTable implements IObject {

	public LocalList(Context context) {
		super(context);
		this.setClickable(true);
		m_rBBox = new Rect();
		
		lstTitles = new ArrayList<String>();
		lstTitles.add("设备名称");
		lstTitles.add("信号名称");
		lstTitles.add("数值");
		lstTitles.add("单位");		
		lstTitles.add("数值类型");
		lstTitles.add("告警等级");
		lstTitles.add("采集时间");
		
	
		view_Receive = new Button(context);		
//		view_Receive.setId(20);
		view_Receive.setText("Receive");
		view_Receive.setTextColor(Color.BLACK);
		view_Receive.setTextSize(20);
	
		view_Receive.setOnClickListener(l);
	
		

		view_PerveDay = new Button(context);	
//		view_PerveDay.setId(21);
		view_PerveDay.setText("PerveDay");
		view_PerveDay.setTextColor(Color.BLACK);
		view_PerveDay.setTextSize(20);
	
		view_PerveDay.setOnClickListener(l);
		

		view_NextDay = new Button(context);	
//		view_NextDay.setId(22);
		view_NextDay.setText("NextDay");
		view_NextDay.setTextColor(Color.BLACK);
		view_NextDay.setTextSize(20);
	
		view_NextDay.setOnClickListener(l);
		

		view_timeButton = new Button(context);
		view_timeButton.setText("set time");
		view_timeButton.setTextColor(Color.BLACK);
		view_timeButton.setTextSize(20);
		view_timeButton.setOnClickListener(l);
		

		view_text1 = new TextView(context);
		view_text1.setTextColor(Color.BLACK);
		view_text1.setText("Equipment↓");
		view_text1.setTextSize(20);
		
	
		view_text2 = new TextView(context);
		view_text2.setTextColor(Color.BLACK);
		view_text2.setText("Signal↓");
		view_text2.setTextSize(20);				
		

		view_EquipSpinner = new Spinner(context);  

		adapter1 = new ArrayAdapter<String>(context,android.R.layout.simple_spinner_item);

		adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		view_EquipSpinner.setAdapter(adapter1);		
		adapter1.add("Equipment1");
		adapter1.add("Equipment2");
		adapter1.add("Equipment3");
		adapter1.add("Equipment4");
		adapter1.add("Equipment5");
		
	
		

		view_SignalSpinner = new Spinner(context);//信号下拉列表控件
	

		adapter2 = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item);   

		adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);        
		
		view_SignalSpinner.setAdapter(adapter2);
		adapter2.add("All");
		adapter2.add("Signal1");	
		adapter2.add("Signal2");
		adapter2.add("Signal3");
		adapter2.add("Signal4");		
		
		

		calendar = Calendar.getInstance();
		year = calendar.get(Calendar.YEAR);
		month = calendar.get(Calendar.MONTH)+1;
		day = calendar.get(Calendar.DAY_OF_MONTH);
		dialog = new DatePickerDialog(context, new OnDateSetListener() {			
			@Override
			public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
				// TODO Auto-generated method stub		
				num = 0; 

			}
		}, year, month, day);
		
		lstContends = new ArrayList<List<String>>();
		m_sortedarray = new ArrayList<String>();
		fjw_signal = new ArrayList<String>();
	
	}
	
	//监听器 view_Receive
	private OnClickListener l = new OnClickListener() {			
				@Override
				public void onClick(View arg0) {
			
					// TODO Auto-generated method stub
					
					if(arg0 == view_timeButton){
						dialog.show();  //弹出日期对话框
						flag = 1;				
						return;
					}
					//获取设置的日期
					int set_year = dialog.getDatePicker().getYear();
					int set_month = dialog.getDatePicker().getMonth();
					int set_day = dialog.getDatePicker().getDayOfMonth();
				
					
					//判断哪一个监听器的				
					if(arg0 == view_Receive){
						
	
						
					}
					else if(arg0 == view_NextDay){
					
						num++;
						set_day = set_day + num;
			
						long now_time = java.lang.System.currentTimeMillis();
						SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//时间格式转换
						Date date = new Date(now_time);
						String sampletime = formatter.format(date);
						 String now_day = sampletime.substring(8, 10);
						 int int_now_day = Integer.valueOf(now_day);
						 if(set_day > int_now_day){
							 num--;
							 set_day--;
						 }
			
						if(set_day > 31){
							set_day = 1;
							set_month++;
							if(set_month>13){
								set_month = 1;
								set_year++;
							}
						}
						
					
					}
					else if(arg0 == view_PerveDay){
			
						num--; //天数加1天
						set_day = set_day + num;
						if(set_day < 1){
							set_day = 31;
							set_month--;
							if(set_month<1){
								set_month = 12;
								set_year--;
							}
						}
						
					}
					get_day = String.valueOf(set_year)+"-"
						       +String.valueOf(set_month)+"-0"+String.valueOf(set_day);
				
					
		
					String equip_name = (String) view_EquipSpinner.getSelectedItem();
					String signal_name = (String) view_SignalSpinner.getSelectedItem();
					view_text1.setText(equip_name);  //设备选择显示
					view_text2.setText(signal_name);
					if("Equipment1".equals(equip_name)) equip_id = 2;
					if("Equipment2".equals(equip_name)) equip_id = 9;					
					if("".equals(get_day)) return;
					
				
					m_bneedupdate = true;   
								
		}
	};	
				
	@Override
	public void doLayout(boolean bool, int l, int t, int r, int b) {
		if (m_rRenderWindow == null)
			return;
		int nX = l + (int) (((float)m_nPosX / (float)MainWindow.FORM_WIDTH) * (r-l));
		int nY = t + (int) (((float)m_nPosY / (float)MainWindow.FORM_HEIGHT) * (b-t));
		int nWidth = (int) (((float)(m_nWidth) / (float)MainWindow.FORM_WIDTH) * (r-l));
		int nHeight = (int) (((float)(m_nHeight) / (float)MainWindow.FORM_HEIGHT) * (b-t));

		m_rBBox.left = nX;
		m_rBBox.top = nY;
		m_rBBox.right = nX+nWidth;
		m_rBBox.bottom = nY+nHeight;
		if (m_rRenderWindow.isLayoutVisible(m_rBBox)) {
			notifyTableLayoutChange(nX, nY, nX+nWidth, nY+nHeight);
			
			for (int i = 0; i < m_title.length; ++i)
				m_title[i].layout(nX + i * nWidth / m_title.length, nY-18, nX + i * nWidth / m_title.length + nWidth / m_title.length, nY);
			
			//绘制view_button的底板空间
			view_Receive.layout(nX, nY-80, nX+130, nY-19);
			view_PerveDay.layout(nX+150, nY-80, nX+280, nY-19);
			view_NextDay.layout(nX+300, nY-80, nX+480, nY-19);
			view_text1.layout(nX+500, nY-80, nX+630, nY-19);
			view_text2.layout(nX+650, nY-80, nX+780, nY-19);
			view_EquipSpinner.layout(nX+500, nY-80, nX+630, nY-19);
			view_SignalSpinner.layout(nX+650, nY-80, nX+780, nY-19);
			view_timeButton.layout(nX+800, nY-80, nX+930, nY-19);
			
	
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
	
	public int getZIndex()
	{
		return m_nZIndex;
	}
	
	@Override
	public void addToRenderWindow(MainWindow rWin) {
		this.setClickable(true);
		this.setBackgroundColor(m_cBackgroundColor);
		
		m_bUseTitle = false;
		m_title = new TextView[lstTitles.size()];

		for (int i = 0; i < m_title.length; i++)
		{
			m_title[i] = new TextView(getContext());

			m_title[i].setGravity(Gravity.CENTER);
			m_title[i].setText(lstTitles.get(i));
			rWin.addView(m_title[i]);
		}
		 //view_button画布添加到窗口
		rWin.addView(view_Receive);		
		rWin.addView(view_NextDay);	
		rWin.addView(view_PerveDay);
		rWin.addView(view_text1);
		rWin.addView(view_text2);		
		rWin.addView(view_EquipSpinner);
		rWin.addView(view_SignalSpinner);	
		rWin.addView(view_timeButton);		
		m_rRenderWindow = rWin;
		rWin.addView(this);
		

	}

	@Override
	public void removeFromRenderWindow(MainWindow rWin) {
		
		rWin.removeView(view_Receive);
		rWin.removeView(view_NextDay);
		rWin.removeView(view_PerveDay);
		rWin.removeView(view_text1);
		rWin.removeView(view_text2);
		rWin.removeView(view_EquipSpinner);
		rWin.removeView(view_SignalSpinner);	
		rWin.removeView(view_timeButton);	
		rWin.removeView(this);
	}

	public void parseProperties(String strName, String strValue, String strResFolder) {
		if ("ZIndex".equals(strName)) {
       	 	m_nZIndex = Integer.parseInt(strValue);
       	    if (MainWindow.MAXZINDEX < m_nZIndex) MainWindow.MAXZINDEX = m_nZIndex;
        }
        else if ("Location".equals(strName)) {
       	 	String[] arrStr = strValue.split(",");
       	 	m_nPosX = Integer.parseInt(arrStr[0]);
       	 	m_nPosY = Integer.parseInt(arrStr[1]);
       	 	
       	 	// 设定列表坐标参数
			m_nLeft = m_nPosX;
			m_nTop  = m_nPosY;
			m_nRight  = m_nLeft + m_nTableWidth;
			m_nBottom = m_nTop + m_nTableHeight;
        }
        else if ("Size".equals(strName)) {
       	 	String[] arrSize = strValue.split(",");
       	 	m_nWidth = Integer.parseInt(arrSize[0]);
       	 	m_nHeight = Integer.parseInt(arrSize[1]);

       	 	// 设定列表坐标参数
			m_nTableWidth  = m_nWidth;
			m_nTableHeight = m_nHeight;
			m_nRight  = m_nLeft + m_nTableWidth;
			m_nBottom = m_nTop + m_nTableHeight;
        }
        else if ("Alpha".equals(strName)) {
       	 	m_fAlpha = Float.parseFloat(strValue);
        }
        else if ("Expression".equals(strName))
        	m_strExpression = strValue;
        else if ("RadioButtonColor".equals(strName)) {
        	m_cRadioButtonColor = Color.parseColor(strValue);
        }
        else if ("ForeColor".equals(strName)) {
        	m_cForeColor = Color.parseColor(strValue);
        	this.setFontColor(m_cForeColor);
        }
        else if ("BackgroundColor".equals(strName)) {
        	m_cBackgroundColor = Color.parseColor(strValue);
        	this.setBackgroundColor(m_cBackgroundColor);
        }
        else if ("BorderColor".equals(strName)) {
        	m_cBorderColor = Color.parseColor(strValue);
        }
        else if ("OddRowBackground".equals(strName)) {
        	m_cOddRowBackground = Color.parseColor(strValue);
        }
        else if ("EvenRowBackground".equals(strName)) {
        	m_cEvenRowBackground = Color.parseColor(strValue);
        }
	}

	@Override
	public void initFinished()
	{
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
	public void updateWidget()
	{
		update();
	}

	@Override
	public boolean updateValue()  //由于更新不给力在这里要做更新处理 fjw notice
	{
		m_bneedupdate = false;  //如果为真，表示数据不根据数据更新时时刷界面
	
		List<local_his_signal> localHisSignals = new ArrayList<local_his_signal>();
		localHisSignals = m_rRenderWindow.m_oShareObject.m_mapLocalSignal.get(this.getUniqueID());

	
		if (localHisSignals == null)
		{
			
				return false;
		}
	
		//遍历第一个设备列表 将每个历史信号的结构体
		lstContends.clear(); //清楚页面的以前数据 行信号
		Iterator<local_his_signal> iterator_his = localHisSignals.iterator();
		while(iterator_his.hasNext()){
			local_his_signal his_sig = iterator_his.next();
		    	List<String> lstRow_his = new ArrayList<String>();
		    	
		    	lstRow_his.add(his_sig.equip_name);
		    	lstRow_his.add(his_sig.name);
		    	lstRow_his.add(his_sig.value);
		    	lstRow_his.add(his_sig.unit);
		    	lstRow_his.add(his_sig.value_type);
		    	lstRow_his.add(his_sig.severity);
		    	lstRow_his.add(his_sig.freshtime);
		    	fjw_signal.addAll(lstRow_his);
	
		    	lstContends.add(lstRow_his);
		 }
		updateContends(lstTitles, lstContends);
		fjw_signal.clear();
		
	//	m_bneedupdate = true;
		return true;
	}

	@Override
    public boolean needupdate()
    {
	    return m_bneedupdate;
    }
	
	@Override
    public void needupdate(boolean bNeedUpdate)
    {
	    m_bneedupdate = bNeedUpdate;
    }
	
	public Rect getBBox() {
		return m_rBBox;
	}
	
	
	
// params:
	String m_strID = "";
	String m_strType = "";  
    int m_nZIndex = 15;
	int m_nPosX = 40;
	int m_nPosY = 604;
	int m_nWidth = 277;
	int m_nHeight = 152;
	float m_fAlpha = 0.8f;
	String m_strExpression = "Binding{[Equip[Equip:113]]}";
	int m_cRadioButtonColor = 0xFFFF8000;
	int m_cForeColor = 0xFF00FF00;
	int m_cBackgroundColor = 0xFF000000;
	int m_cBorderColor = 0xFFFFFFFF;
	
	// radio buttons
	//RadioButton[] m_oRadioButtons;
	
	// 固定标题栏
	TextView[] m_title;
	Button  view_Receive;
	Button  view_PerveDay;
	Button  view_NextDay;
	Button  view_timeButton;
	TextView view_text1;
	TextView view_text2;
	public Spinner view_EquipSpinner = null;  //设备下拉列表控件
	public Spinner view_SignalSpinner = null; //信号下拉列表控件
	private  ArrayAdapter<String> adapter1 = null;
	private  ArrayAdapter<String> adapter2 = null;
	private  DatePickerDialog  dialog;  //日期对话框选择应用
	private int year,month,day;   //对话框显示的年月日变量
	private Calendar calendar;
	private int flag = 0;
	private int num = 0; //加减按纽加减数

	int n = 0;
	public static int equip_id = 0;    //要获取的设备的id
	public static long receive_time ;   //获取此时系统时间
	public static String get_day="";   //所要获取数据的日期
	
	MainWindow m_rRenderWindow = null;
	Rect m_rBBox = null;
	
	public boolean m_bNeedINIT = true;
	public boolean m_bneedupdate = false;
	
	// TODO: 临时代替数据
	boolean m_needsort = true;
	ArrayList<String> m_sortedarray = null;
	List<String> lstTitles = null;
	List<List<String>> lstContends = null;
	private Paint  mPaint = new Paint();  //注意以后变量的定义一定要赋予空间
	List<String> fjw_signal = null;
}
