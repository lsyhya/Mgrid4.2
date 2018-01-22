package com.sg.uis;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import com.mgrid.data.DataGetter;
import com.mgrid.data.EquipmentDataModel.Signal;
import com.mgrid.main.MGridActivity;
import com.mgrid.main.MainWindow;
import com.mgrid.main.R;
import com.sg.common.IObject;
import com.sg.common.MyAdapter;
import com.sg.common.UtExpressionParser;
import com.sg.common.UtTable;
import com.sg.common.UtTableAdapter;
import com.sg.common.UtExpressionParser.stBindingExpression;
import com.sg.common.UtExpressionParser.stExpression;

import data_model.ipc_history_signal;
import data_model.local_his_signal;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Paint.Style;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;

/** 历史信号 */
//信号历史数据 SaveEquipt
//author :fjw0312
//time:2015 11 02
public class SaveEquipt extends UtTable implements IObject {

	//方便中英文切换
	private String DeviceName;
	private String SignalName;
	private String Value;
	private String Unit;
	private String ValueType;
	private String AlarmSeverity;
	private String AcquisitionTime;
	
	private String DeviceList;
	private String SetTime;
	private String PreveDay;
	private String NextDay;
	private String Receive;
	
	
	private String textColor="#FF000000";
	private String btnColor="#FFC0C0C0";
	private String titleColor="#FF242424";
	
	private MyAdapter myAdapter=null;
	
	private PopupWindow popupWindow;
	
	public SaveEquipt(Context context) {
		super(context);
		m_rBBox = new Rect();
		

		if(MGridActivity.whatLanguage)
		{
		    DeviceName="设备名称";
		    SignalName="信号名称";
		    Value="数值";
		    Unit="单位";
		    ValueType="数值类型";
		    AlarmSeverity="告警等级";
		    AcquisitionTime="采集时间";
			
			DeviceList="  设备↓   ";
			SetTime="设置日期";
			PreveDay="前一天";
			NextDay="后一天";
			Receive="  获取   ";
		}
		else
		{
			DeviceName="DeviceName";
			SignalName="SignalName";
			Value="Value";
			Unit="Numericalsignal";
			ValueType="ValueType";
			AlarmSeverity="AlarmSeverity";
			AcquisitionTime="AcquisitionTime";
			
			DeviceList="  Device↓   ";
			SetTime="SetTime";
			PreveDay="PreveDay";
			NextDay="NextDay";
			Receive="  Receive   ";
		}
		
		//标头标题
		lstTitles = new ArrayList<String>();
		lstTitles.add(DeviceName);
		lstTitles.add(SignalName);
		lstTitles.add(Value);
		lstTitles.add(Unit);		
		lstTitles.add(ValueType);
		lstTitles.add(AlarmSeverity);
		lstTitles.add(AcquisitionTime);
		//信号名显示text	
		view_text = new TextView(context);
		view_text.setTextColor(Color.BLACK);
		view_text.setText(DeviceList); //  Equiptment↓
		view_text.setTextSize(15);
		view_text.setGravity(Gravity.CENTER);
		view_text.setBackgroundColor(Color.argb(100, 100, 100, 100));
//		view_text.setBackgroundColor(Color.parseColor(btnColor));
//		view_text.setTextColor(Color.parseColor(textColor));
		
		//日期选择button
		view_timeButton = new Button(context);
		view_timeButton.setText(SetTime);   // Set Time
		view_timeButton.setTextColor(Color.BLACK);
		view_timeButton.setTextSize(15);
		view_timeButton.setPadding(2, 2, 2, 2);
		view_timeButton.setOnClickListener(l);//设置该控件的监听	
//		view_timeButton.setBackgroundResource(android.R.drawable.btn_default);
//		view_timeButton.setBackgroundColor(Color.parseColor(btnColor));
//		view_timeButton.setTextColor(Color.parseColor(textColor));
		//前一天button
		view_PerveDay = new Button(context);	
		view_PerveDay.setText(PreveDay);  // PreveDay
		view_PerveDay.setTextColor(Color.BLACK);
		view_PerveDay.setTextSize(15);
		view_PerveDay.setPadding(2, 2, 2, 2);		
		view_PerveDay.setOnClickListener(l);//设置该控件的监听	
//		view_PerveDay.setBackgroundResource(android.R.drawable.btn_default);
//		view_PerveDay.setBackgroundColor(Color.parseColor(btnColor));
//		view_PerveDay.setTextColor(Color.parseColor(textColor));
		//后一天button
		view_NextDay = new Button(context);	
		view_NextDay.setText(NextDay);  // NextDay
		view_NextDay.setTextColor(Color.BLACK);
		view_NextDay.setTextSize(15);	
		view_NextDay.setPadding(2, 2, 2, 2);
		view_NextDay.setOnClickListener(l);//设置该控件的监听	
//		view_NextDay.setBackgroundResource(android.R.drawable.btn_default);
//		view_NextDay.setBackgroundColor(Color.parseColor(btnColor));
//		view_NextDay.setTextColor(Color.parseColor(textColor));
		//接收receive
		view_Receive = new Button(context);		
		view_Receive.setText(Receive); // Receive
		view_Receive.setTextColor(Color.BLACK);
		view_Receive.setTextSize(15);
		view_Receive.setPadding(2, 2, 2, 2);		
		view_Receive.setOnClickListener(l);	//设置该控件的监听	
//		view_Receive.setBackgroundResource(android.R.drawable.btn_default);
//		view_Receive.setBackgroundColor(Color.parseColor(btnColor));
//		view_Receive.setTextColor(Color.parseColor(textColor));
	
		calendar = Calendar.getInstance();
		year = calendar.get(Calendar.YEAR);
		month = calendar.get(Calendar.MONTH);
		day = calendar.get(Calendar.DAY_OF_MONTH);
		dialog = new DatePickerDialog(context, new OnDateSetListener() {			
			@Override
			public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
				// TODO Auto-generated method stub		
				num = 0;  

			}
		}, year, month, day);
		
		
		
		
		myAdapter=new MyAdapter(getContext(), nameList);
		view_text.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (isFirst) {
					parse_expression();
					isFirst=false;
				}

				View view = m_rRenderWindow.m_oMgridActivity
						.getLayoutInflater().inflate(R.layout.pop, null);
				popupWindow = new PopupWindow(view, view_text.getWidth(), 200,
						true);
				// 设置一个透明的背景，不然无法实现点击弹框外，弹框消失
				popupWindow.setBackgroundDrawable(new BitmapDrawable());

				// 设置点击弹框外部，弹框消失
				popupWindow.setOutsideTouchable(true);
				popupWindow.setFocusable(true);
				popupWindow.showAsDropDown(view_text);

				ListView lv = (ListView) view.findViewById(R.id.lv_list);
				
				myAdapter.setTextColor(textColor);
				myAdapter.setBtnColor(btnColor);
				lv.setAdapter(myAdapter);
				lv.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						view_text.setText(nameList.get(position));
						popupWindow.dismiss();
					}
				});

			}
		});
		
		
		
		
		lstContends = new ArrayList<List<String>>();

		map_EquiptNameList = new HashMap<String,String>();  //<信号名字,设备id-信号id>

	}
	
	//监听器 view_Receive
		private OnClickListener l = new OnClickListener() {			
					@Override
					public void onClick(View arg0) {
//					
						// TODO Auto-generated method stub
						
						
						//获取设置的日期
						int set_year = dialog.getDatePicker().getYear();
						int set_month = dialog.getDatePicker().getMonth()+1;
						int set_day = dialog.getDatePicker().getDayOfMonth();

						
						//判断哪一个监听器的	
						if(arg0 == view_timeButton){
							dialog.show();  //弹出日期对话框
							flag = 1;	
							num = 0;
							return;
						}
						else if(arg0 == view_Receive){
							num = 0;
//						
						}
						else if(arg0 == view_NextDay){
//							
							num++;	
//						
							set_day = set_day + num; //天数加一天； num有正负之分
							//判断不超过今天日期
							long now_time = java.lang.System.currentTimeMillis();
							SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//时间格式转换
							Date date = new Date(now_time);
							String sampletime = formatter.format(date);
							 String now_year = sampletime.substring(0, 4);
							 String now_month = sampletime.substring(5, 7);
							 String now_day = sampletime.substring(8, 10);
							 int int_now_year = Integer.valueOf(now_year);
							 int int_now_month = Integer.valueOf(now_month);
							 int int_now_day = Integer.valueOf(now_day);							 
							//月末判断
							if(set_day > 31){
								set_day = set_day-31;
								set_month++;
								if(set_month>12){
									set_month = 1;
									set_year++;
								}
							}
							if(set_day < 1){
								set_day = set_day+31;
								set_month--;
								if(set_month<1){
									set_month = 12;
									set_year--;
								}
							}
							if((set_year==int_now_year)&&(set_month==int_now_month)&&(set_day > int_now_day)){								 
								 set_day = int_now_day;
								 num--;
							 }
//						
						
						}
						else if(arg0 == view_PerveDay){
//							
					
							num--;  //num有正负之分
							set_day = set_day + num; //天数加1天
							if(set_day < 1){
								set_day = 31+set_day;
								set_month--;
								if(set_month<1){
									set_month = 12;
									set_year--;
								}
							}if(set_day > 31){
								set_day = set_day-31;
								set_month++;
								if(set_month>12){
									set_month = 1;
									set_year++;
								}
							}
							
						}
						//处理月份日期字符格式
						String str_day;
						String str_nomth;
						if(set_day<10)
						{
							str_day = "0"+String.valueOf(set_day);
						}else{
							str_day = String.valueOf(set_day);
						}
						if(set_month<10)
						{
							str_nomth = "0"+String.valueOf(set_month);
						}else{
							str_nomth = String.valueOf(set_month);
						}
						get_day = String.valueOf(set_year)+"-"+str_nomth+"-"+str_day;
						
			//			get_day = String.valueOf(set_year)+String.valueOf(set_month)+String.valueOf(set_day);
//					
						if("".equals(get_day)) return;
						
					
						String equipt_name = view_text.getText().toString();
	
						if(DeviceList.equals(equipt_name))  return;
						str_EquiptId = map_EquiptNameList.get(equipt_name);
//					
											
						//设置控件需要更新标识变量
						m_bneedupdate = true;   	
						view_Receive.setEnabled(false);
						handler.postDelayed(runable,2000);
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
			int pv = nWidth/5;
			view_text.layout(nX, nY-40, nX+pv, nY-15);
			view_timeButton.layout(nX+pv+20, nY-40, nX+2*pv, nY-16);
			view_NextDay.layout(nX+2*pv+20, nY-40, nX+3*pv, nY-16);
			view_PerveDay.layout(nX+3*pv+20, nY-40, nX+4*pv, nY-16);
			view_Receive.layout(nX+4*pv+20, nY-40, nX+5*pv, nY-16);			
		}
	}
	
	public void onDraw(Canvas canvas) {
		if (m_rRenderWindow == null)
			return;
		if (m_rRenderWindow.isLayoutVisible(getBBox()) == false)
			return;
//		Log.e("SaveEquipt-onDraw", "into onDraw!");
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
			//m_title[i].setTextColor(Color.BLACK);
			//m_title[i].setTextSize(25);
			//m_title[i].setBackgroundColor(Color.GRAY);
			m_title[i].setGravity(Gravity.CENTER);
			m_title[i].setText(lstTitles.get(i));
			m_title[i].setTextColor(Color.parseColor(titleColor));
			rWin.addView(m_title[i]);
		}		
		m_rRenderWindow = rWin;
		rWin.addView(this);
		//view_button画布添加到窗口
		rWin.addView(view_Receive);		
		rWin.addView(view_NextDay);	
		rWin.addView(view_PerveDay);
		rWin.addView(view_text);		
	
		rWin.addView(view_timeButton);	
	
	}

	@Override
	public void removeFromRenderWindow(MainWindow rWin) {
		
		rWin.removeView(this);
		//view_button画布从到窗口去除
		rWin.removeView(view_Receive);
		rWin.removeView(view_NextDay);
		rWin.removeView(view_PerveDay);
		rWin.removeView(view_text);
		
		rWin.removeView(view_timeButton);
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
        else if ("Expression".equals(strName)){
        	m_strExpression = strValue;
  //      	parse_expression();
        }
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
        else if ("SaveTime".equals(strName)) {
        //	save_time = Integer.parseInt(strValue);
        //	save_time = save_time*60*60; //对输入参数的单位为h
        	
        }else if ("BtnColor".equals(strName)) {
			if(!strValue.isEmpty())
			{
			
			if ("#FF000000".equals(strValue)) {
				view_text.setBackgroundResource(R.drawable.bg_shadow);
				view_timeButton.setBackgroundResource(R.drawable.bg_shadow);
				view_PerveDay.setBackgroundResource(R.drawable.bg_shadow);
				view_NextDay.setBackgroundResource(R.drawable.bg_shadow);
				view_Receive.setBackgroundResource(R.drawable.bg_shadow);
				//myAdapter.setBtnColor("#FFFFFFFF");
				btnColor = "#FF4D4D4D";
			} else {
				btnColor = strValue;
				view_text.setBackgroundColor(Color.parseColor(btnColor));
				view_timeButton.setBackgroundColor(Color
						.parseColor(btnColor));
				view_PerveDay
						.setBackgroundColor(Color.parseColor(btnColor));
				view_NextDay.setBackgroundColor(Color.parseColor(btnColor));
				view_Receive.setBackgroundColor(Color.parseColor(btnColor));
				myAdapter.setBtnColor(btnColor);
			}
			}
		}else if ("TextColor".equals(strName)) {
			if(!strValue.isEmpty())
			{
			textColor = strValue;
			myAdapter.setTextColor(textColor);
			view_text.setTextColor(Color.parseColor(textColor));
			view_timeButton.setTextColor(Color
					.parseColor(textColor));
			view_PerveDay
					.setTextColor(Color.parseColor(textColor));
			view_NextDay.setTextColor(Color.parseColor(textColor));
			view_Receive.setTextColor(Color.parseColor(textColor));
			}
		}else if ("TitleColor".equals(strName)) {
			if(!strValue.isEmpty())
			{
			titleColor = strValue;
			myAdapter.notifyDataSetChanged();	
			}
		}
	}

	Runnable runable=new Runnable() {
		public void run() {
			
			handler.sendEmptyMessage(2);
		}
	};
	
	Handler handler=new Handler()
	{
		public void handleMessage(android.os.Message msg) {
			
			switch (msg.what) {
			case 2:
				
				view_Receive.setEnabled(true);
				
				break;
			}
			
		};
	};
	
	
	
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
//	
		//获取历史数据设备列表      多个设备的历史数据列表 列表<设备表<历史信号结构体>>
		List<local_his_signal> his_sig_list = new ArrayList<local_his_signal>();
		if(m_rRenderWindow.m_oShareObject.m_mapSaveEquipt == null) return false;
		his_sig_list = m_rRenderWindow.m_oShareObject.m_mapSaveEquipt.get(this.getUniqueID());
		//遍历第一个列表 获得第一个设备历史信号	
		if (his_sig_list == null)
		{
			
				return false;
		}
	
		//遍历第一个设备列表 将每个历史信号的结构体
		lstContends.clear(); //清楚页面的以前数据 行信号
		Iterator<local_his_signal> iterator_his = his_sig_list.iterator();
		while(iterator_his.hasNext()){
			local_his_signal his_sig = iterator_his.next();
		    	List<String> lstRow_his = new ArrayList<String>();
		    	
		    	lstRow_his.add(his_sig.equip_name);
		    	lstRow_his.add(his_sig.name);
		    	float f=Float.parseFloat(his_sig.value);
		    	int ii=(int) (f*100);
		    	f=(float) (ii/100.0);
		    	lstRow_his.add(f+"");   //这一坨操作就是为了保留小数点后两位
		    
		    	lstRow_his.add(his_sig.unit);
		    	lstRow_his.add(his_sig.value_type);
		    	lstRow_his.add(his_sig.severity);
		    	lstRow_his.add(his_sig.freshtime);
//		    	fjw_signal.addAll(lstRow_his);
		    	lstContends.add(lstRow_his);
		    	
		 }
		updateContends(lstTitles, lstContends);
		updateContends(lstTitles, lstContends);
//		fjw_signal.clear();
		his_sig_list.clear();

		return true;
	}

	//解析出控件表达式，返回控件表达式类
	@SuppressWarnings({ "unchecked", "static-access" })
	public boolean parse_expression(){
		    nameList.add(DeviceList);
			if("".equals(m_strExpression)) return false;
			String Mathstr=UtExpressionParser.getInstance().getMathExpression(m_strExpression);
			ArrayList<Integer> list=new ArrayList<Integer>();
			String[] strCExp=Mathstr.split("\\|");
		 	for(String str:strCExp)
			{
				String[] strResult=str.split("\\]");
				String[] strResult1=strResult[0].split("\\:");
				list.add( Integer.parseInt(strResult1[1]));
			}
			for(int id:list)
			{
				String str_equiptName = DataGetter.getEquipmentName(id);
				map_EquiptNameList.put(str_equiptName, String.valueOf(id));
				nameList.add(str_equiptName);
			}

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
	String m_strExpression = "Binding{[Equip[Equip:2]]}";
	int m_cRadioButtonColor = 0xFFFF8000;
	int m_cForeColor = 0xFF00FF00;
	int m_cBackgroundColor = 0xFF000000;
	int m_cBorderColor = 0xFFFFFFFF;
	
	// radio buttons
	//RadioButton[] m_oRadioButtons;
	
	// 固定标题栏
	TextView[] m_title;
	TextView view_text;		            //信号名显示text		
		//信号名选择spinner
	Button  view_timeButton;		        //日期选择button
	Button  view_PerveDay;		            //前一天button
	Button  view_NextDay;		            //后一天button
	Button  view_Receive;		            //接收receive
	
	private HashMap<String,String> map_EquiptNameList = null;  //<设备名，设备id>
	
	private  DatePickerDialog  dialog;  //日期对话框选择应用
	private int year,month,day;   //对话框显示的年月日变量
	private Calendar calendar;
	private int flag = 0;
	private int num = 0; //加减按纽加减数	
	public static String str_EquiptId = ""; //所需要的设备id字符串	
	public static String get_day="";   //所要获取数据的日期
	public static int save_time = 60*60*2;   //saveEquipt采集时间   2h
	
	MainWindow m_rRenderWindow = null;
	Rect m_rBBox = null;
	
	public boolean m_bNeedINIT = true;
	public boolean m_bneedupdate = false;
	
	// TODO: 临时代替数据
	boolean m_needsort = true;
//	ArrayList<String> m_sortedarray = null;
	List<String> lstTitles = null;
	List<List<String>> lstContends = null;

	private ArrayList<String> nameList = new ArrayList<String>();
	private boolean isFirst=true;
}
