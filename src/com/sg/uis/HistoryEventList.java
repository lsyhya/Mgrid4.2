package com.sg.uis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.mgrid.data.DataGetter;
import com.mgrid.main.MainWindow;
import com.sg.common.IObject;
import com.sg.common.UtTable;

import data_model.local_his_event;

/** 历史告警 -- 英文版 */
//信号告警数据 HistoryEventList 与HisEvent一样
//author :fjw0312
//time:2015 11 16
public class HistoryEventList extends UtTable implements IObject {

	public HistoryEventList(Context context) {
		super(context);
		m_rBBox = new Rect();
		
		//标头标题
		lstTitles = new ArrayList<String>(); 
		lstTitles.add("Equipment name ");	
//		lstTitles.add("信号名称");			
		lstTitles.add("   Alarm name  ");
		lstTitles.add(" Alarm meaning ");
		lstTitles.add("  Signal data  ");
		lstTitles.add(" Alarm severity");
		lstTitles.add("   Start time  ");
		lstTitles.add("    End time   ");
		//信号名显示text	
		view_text = new TextView(context);
		view_text.setTextColor(Color.BLACK);
		view_text.setText("Equiptment↓");
		view_text.setTextSize(16);
		view_text.setGravity(Gravity.CENTER);
		view_text.setBackgroundColor(Color.argb(100, 100, 100, 100));
		
		//接收receive
		view_Receive = new Button(context);		
		view_Receive.setText("Receive");
		view_Receive.setTextColor(Color.BLACK);
		view_Receive.setTextSize(16);
		view_Receive.setPadding(2, 2, 2, 2);		
		view_Receive.setOnClickListener(l);	//设置该控件的监听	
				
		//信号名选择spinner
		view_EquiptSpinner = new Spinner(context);//信号下拉列表控件
		adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item); 
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		view_EquiptSpinner.setAdapter(adapter);
		adapter.add("Equiptment↓");	
		view_EquiptSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			 public void onItemSelected(
	                    AdapterView<?> parent, View view, int position, long id) {
				 		get_equiptList(); //解析下拉列表成员

	            }

	            public void onNothingSelected(AdapterView<?> parent) {
	            	
	            }
		});
		
		lstContends = new ArrayList<List<String>>();

		map_EquiptNameList = new HashMap<String,String>();  //<信号名字,设备id-信号id>

	}
	

		private OnClickListener l = new OnClickListener() {			
					@Override
					public void onClick(View arg0) {

						// TODO Auto-generated method stub											
						
						 if(arg0 == view_Receive){
					
					
						closeEquiptName = (String) view_EquiptSpinner.getSelectedItem();

						view_text.setText(closeEquiptName);	
						if("Equiptment↓".equals(closeEquiptName))  return;
						str_EquiptId = map_EquiptNameList.get(closeEquiptName);
						
											
						//设置控件需要更新标识变量
						m_bneedupdate = true;   	
					   }
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
	
	
			int pv = nWidth/5;
			view_text.layout(nX, nY-40, nX+pv, nY-14);
			view_EquiptSpinner.layout(nX, nY-42, nX+pv, nY-12);	
			view_Receive.layout(nX+4*pv+20, nY-42, nX+5*pv, nY-12);			
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
			//m_title[i].setTextColor(Color.BLACK);
			//m_title[i].setTextSize(25);
			//m_title[i].setBackgroundColor(Color.GRAY);
			m_title[i].setGravity(Gravity.CENTER);
			m_title[i].setText(lstTitles.get(i));
			rWin.addView(m_title[i]);
		}		
		m_rRenderWindow = rWin;
		rWin.addView(this);

		rWin.addView(view_Receive);		
		rWin.addView(view_text);		
		rWin.addView(view_EquiptSpinner);	
	
	}

	@Override
	public void removeFromRenderWindow(MainWindow rWin) {
		
		rWin.removeView(this);
		//view_button画布从到窗口去除
		rWin.removeView(view_Receive);
		rWin.removeView(view_text);
		rWin.removeView(view_EquiptSpinner);
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
        	if("".equals(strValue)){
        		m_strExpression = "Binding{[Equip[Equip:0]]}";
        	}
     //   	parse_expression();
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
		m_bneedupdate = false;  

		List<local_his_event> his_event_list = new ArrayList<local_his_event>();
		if(m_rRenderWindow.m_oShareObject.m_mapLocalEvent == null) return false;
		his_event_list = m_rRenderWindow.m_oShareObject.m_mapLocalEvent.get(this.getUniqueID());

		if (his_event_list == null)
		{
		
				return false;
		}

		List<String> key = new ArrayList();
		Hashtable<String,local_his_event> hast_his = new Hashtable<String,local_his_event>();
		Iterator<local_his_event> iter = his_event_list.iterator();
		while(iter.hasNext()){
			local_his_event his_event = iter.next();
			if(his_event==null) return false;	
			boolean flag = true;
	
			if(hast_his.containsKey(his_event.start_time+"#"+his_event.event_id)){
				flag = false;
				
				if("1970-01-01".equals(his_event.finish_time.substring(0, 10)) )
					continue;
			}
			hast_his.put(his_event.start_time+"#"+his_event.event_id, his_event);	

			if(flag){
				key.add(his_event.start_time+"#"+his_event.event_id);
			}		
		}
		
	
		
		
	
		lstContends.clear(); //清楚页面的以前数据 行信号
		if(key == null||hast_his == null) return false;
		Iterator<String> iterator_key = key.iterator();
		while(iterator_key.hasNext()){
			String his_event_key = iterator_key.next();
			if(his_event_key==null||"".equals(his_event_key)) return false;
			local_his_event his_event = hast_his.get(his_event_key);
			if(his_event == null) return false;
		    List<String> lstRow_his = new ArrayList<String>();
		    	//对通信中断告警结束时间做判断
		    	String finishTime = his_event.finish_time;

		    	//该处理有debug待后期完善
		    	if("1970-01-01".equals(finishTime.substring(0, 10))){ 		    		  
		    		  finishTime = "null";	 
//		    		  continue;
		    	}
		    	//获取信号名称
		    //	String name = DataGetter.getSignalName(str_EquiptId, his_event.sigid);
		    	String eventName = DataGetter.getEventName(str_EquiptId, his_event.event_id);
		    	lstRow_his.add(closeEquiptName);  //设备名称
		    //	lstRow_his.add(name);    //信号名称		    	
		    	lstRow_his.add(eventName);//告警名称
		    	lstRow_his.add(his_event.event_mean); //告警含义
		    	lstRow_his.add(his_event.value); //信号数值
		    	lstRow_his.add(his_event.severity);    //告警等级
		    	lstRow_his.add(his_event.start_time); //开始时间
		    	lstRow_his.add(finishTime);//结束时间
		    	
	//	    	fjw_signal.addAll(lstRow_his);
		    	lstContends.add(lstRow_his);
		    
		 }
		updateContends(lstTitles, lstContends);
		lstContends.clear(); //清楚页面的以前数据 行信号
		hast_his.clear();
		his_event_list.clear();
		key.clear();

		return true;
	}

	//解析出控件表达式，返回控件表达式类
	public boolean get_equiptList(){
			//获取采集器中<设备id,设备名称>
		 HashSet<String>  ht_equiptID = DataGetter.getEquipmentIdList();
		 if(ht_equiptID==null) return false;
		 Iterator<String> iter = ht_equiptID.iterator();
		 while(iter.hasNext()){
			 String equiptId = iter.next();
			 String equiptName = DataGetter.getEquipmentName(equiptId);
			 if("".equals(equiptName)){
		
				 continue;  
			 }	
			 adapter.add(equiptName);
			 map_EquiptNameList.put(equiptName, equiptId);
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
	Spinner view_EquiptSpinner = null; 		//设备名选择spinner
	Button  view_Receive;		            //接收receive
	
	private HashMap<String,String> map_EquiptNameList = null;  //<设备名-设备id>
	private  ArrayAdapter<String> adapter = null;
	private String closeEquiptName = "";
	public static String str_EquiptId = ""; //所需要的设备-信号id字符串	
	
	MainWindow m_rRenderWindow = null;
	Rect m_rBBox = null;
	
	public boolean m_bNeedINIT = true;
	public boolean m_bneedupdate = false;
	
	// TODO: 临时代替数据
	boolean m_needsort = true;
//	ArrayList<String> m_sortedarray = null;
	List<String> lstTitles = null;
	List<List<String>> lstContends = null;
	private Paint  mPaint = new Paint();  //注意以后变量的定义一定要赋予空间
//	List<String> fjw_signal = null;
}
