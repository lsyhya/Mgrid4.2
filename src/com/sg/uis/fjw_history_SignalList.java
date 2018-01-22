package com.sg.uis;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import com.mgrid.data.EquipmentDataModel.Signal;
import com.mgrid.main.MainWindow;
import com.sg.common.IObject;
import com.sg.common.UtTable;
import com.sg.common.UtTableAdapter;
import comm_service.service;

import data_model.ipc_history_signal;

import android.R.drawable;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Paint.Style;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/** 历史信号 */
public class fjw_history_SignalList extends UtTable implements IObject {

	public fjw_history_SignalList(Context context) {
		super(context);
		m_rBBox = new Rect();
		
		lstTitles = new ArrayList<String>();
		lstTitles.add("设备id");
		lstTitles.add("信号id");
		lstTitles.add("数值");
		lstTitles.add("历史时间");
//		lstTitles.add("信号类型");
//		lstTitles.add("数值类型");
//		lstTitles.add("数值");
		lstTitles.add("severity");
//		lstTitles.add("unit");
		
		
		lstContends = new ArrayList<List<String>>();
		m_sortedarray = new ArrayList<String>();
		fjw_signal = new ArrayList<String>();
		

		my_add_view = new TextView(context);		
		my_add_view.setText("fjw_view");
		my_add_view.setTextColor(Color.RED);
		my_add_view.setTextSize(22);
		my_add_view.setGravity(Gravity.CENTER);
		my_add_view.setBackgroundColor(Color.GREEN);

		
		my_button = new Button(context);	
		
		my_button.setText("receive");
		my_button.setTextColor(Color.YELLOW);
		my_button.setTextSize(20);
		
		
		
		my_button.setOnClickListener(l);
		

	}
	

	private OnClickListener l = new OnClickListener() {		
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			n++;
			my_button.setText("receive:"+String.valueOf(n));
			

			long now_time = java.lang.System.currentTimeMillis();
		  my_his_startTime = 10;
		  // my_his_startTime = 3;
			my_his_span = 60;
			my_his_count = 20;
			
		
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
			
			
			my_add_view.layout(nX+nWidth-140, nY-80, nX+nWidth, nY-20);
			my_button.layout(nX+nWidth-300, nY-80, nX+nWidth-160, nY-20);
			
			
	
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
		
		rWin.addView(my_add_view);
		rWin.addView(my_button);
		
	
		
		m_rRenderWindow = rWin;
		rWin.addView(this);

	}

	@Override
	public void removeFromRenderWindow(MainWindow rWin) {

		
		rWin.removeView(this);
		rWin.removeView(my_add_view);
		rWin.removeView(my_button);
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
	public boolean updateValue()
	{
		m_bneedupdate = false;
		
		
		List<ipc_history_signal> listhistorydata = new ArrayList<ipc_history_signal>();
		listhistorydata = m_rRenderWindow.m_oShareObject.m_mapHistorySignal.get(this.getUniqueID());
	
		if (listhistorydata == null)
		{

				return false;
		}

		
		lstContends.clear();
	
	
		Iterator<ipc_history_signal> iterator_his = listhistorydata.iterator();
		while(iterator_his.hasNext()){
		    	ipc_history_signal his_sig = iterator_his.next();
		    	List<String> lstRow_his = new ArrayList<String>();
		    	
		    	lstRow_his.add(Integer.toString(his_sig.equipid));
		    	lstRow_his.add(Integer.toString(his_sig.sigid));
		    	lstRow_his.add(his_sig.name);
		    	lstRow_his.add(his_sig.history_time);
		    	lstRow_his.add(Integer.toString(his_sig.sig_type));
		    	lstRow_his.add(Integer.toString(his_sig.value_type));
		    	lstRow_his.add(his_sig.value);
		    	lstRow_his.add(Integer.toString(his_sig.severity));
		    	lstRow_his.add(his_sig.unit);
		    	fjw_signal.addAll(lstRow_his);
		
		    	lstContends.add(lstRow_his);
		 }

		updateContends(lstTitles, lstContends);
		fjw_signal.clear();

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
	
	//添加view控件
	TextView my_add_view;
	Button  my_button;
	int n = 0;
	public static long  my_his_startTime=0;
	public static long my_his_span=0;
	public static long my_his_count=0;
	
	MainWindow m_rRenderWindow = null;
	Rect m_rBBox = null;
	
	public boolean m_bNeedINIT = true;
	public boolean m_bneedupdate = true;
	
	// TODO: 临时代替数据
	boolean m_needsort = true;
	ArrayList<String> m_sortedarray = null;
	List<String> lstTitles = null;
	List<List<String>> lstContends = null;
	private Paint  mPaint = new Paint();  //注意以后变量的定义一定要赋予空间
	List<String> fjw_signal = null;
}
