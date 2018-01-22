package com.sg.uis;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.mgrid.data.EquipmentDataModel.Signal;
import com.mgrid.main.MGridActivity;
import com.mgrid.main.MainWindow;
import com.sg.common.IObject;
import com.sg.common.UtTable;

/** 信号 */
public class SgSignalList extends UtTable implements IObject {

	
	//方便中英文切换
	private String Name;
	private String RefreshTime;
	private String Value;
	private String Implication;
	private String Unit;


	public SgSignalList(Context context) {
		super(context);
		m_rBBox = new Rect();
		
		if(MGridActivity.whatLanguage)
		{
			Name="名称";
			RefreshTime="刷新时间";
			Value="值";
			Implication="含义";
			Unit="单位";
		}
		else
		{
			Name="Name";
			RefreshTime="RefreshTime";
			Value="Value";
			Implication="Implication";
			Unit="Unit";
		
		}
		
		lstTitles = new ArrayList<String>();
		lstTitles.add(Name);
		lstTitles.add(RefreshTime);
		lstTitles.add(Value);
	//	lstTitles.add("告警等级");
		lstTitles.add(Implication);
		lstTitles.add(Unit);
/*		
		lstTitles.add("Name");
		lstTitles.add("Achieve time");
		lstTitles.add("Value");
//		lstTitles.add("Alarm severity");
		lstTitles.add("Meaning");
		lstTitles.add("Unit");
*/		
		lstContends = new ArrayList<List<String>>();
		m_sortedarray = new ArrayList<String>();
	}

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
			
			/*
			for (int i = 0; i < m_oRadioButtons.length; ++i)
				m_oRadioButtons[i].layout(nX + i * nWidth / 2, nY-50, nX + i * nWidth / 2 + 120, nY-40+40);
			*/
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
		
		/*
		m_oRadioButtons = new RadioButton[2];
		//bt1
		m_oRadioButtons[0] = new RadioButton(this.getContext());
		m_oRadioButtons[0].setText("全部");
		m_oRadioButtons[0].setChecked(true);
		// bt2
		m_oRadioButtons[1] = new RadioButton(this.getContext());
		m_oRadioButtons[1].setText("遥测");
		
		m_oRadioButtons[0].setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (m_oRadioButtons[0].isChecked()) {
					m_oRadioButtons[1].setChecked(false);
				}
			}
		});
		
		m_oRadioButtons[1].setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (m_oRadioButtons[1].isChecked()) {
					m_oRadioButtons[0].setChecked(false);
				}
			}
		});

		for (int i = 0; i < m_oRadioButtons.length; ++i)
			rWin.addView(m_oRadioButtons[i]);
		*/
		
		m_rRenderWindow = rWin;
		rWin.addView(this);
	}

	@Override
	public void removeFromRenderWindow(MainWindow rWin) {
		/*
		for (int i = 0; i < m_oRadioButtons.length; ++i)
			rWin.removeView(m_oRadioButtons[i]);
		*/
		
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
	public boolean updateValue()
	{
		m_bneedupdate = false;
  //      System.out.println("123：我跑进来了");
		Hashtable<String, Signal> listSignals = m_rRenderWindow.m_oShareObject.m_mapSignalListDatas.get(this.getUniqueID());
		
		
		if (listSignals == null) return false;

		lstContends.clear();


		if (m_needsort)
		{
			// 依照信号ID排序
			if (m_sortedarray.size() != listSignals.size())
			{
				// 尚无序列KEY，初始化顺序。
				class SortBySignalID implements Comparator<Object>
				{
					public int compare(Object o1, Object o2)
					{
						Integer s1 = Integer.valueOf((String) o1);
						Integer s2 = Integer.valueOf((String) o2);
						return s1.compareTo(s2);
					}
				}

				m_sortedarray = new ArrayList<String>(listSignals.keySet());
				Collections.sort(m_sortedarray, new SortBySignalID());
			}
			
			Iterator<String> it = m_sortedarray.iterator();
		
			
			while(it.hasNext())
			{
				String signalid = it.next();
				Signal signal = listSignals.get(signalid);
//				if(getUniqueID().equals("SignalList1")){
//				System.out.println("信号ID："+signalid);
//				System.out.println("信号名字B："+signal.name);
//				System.out.println("大小："+listSignals.size());
//				}
				if(signal.name.equals(""))
					continue;
				List<String> lstRow = new ArrayList<String>();				
				lstRow.add(signal.name);
				lstRow.add(getDate(signal.freshtime*1000, "yyyy.MM.dd HH:mm:ss"));
				if(!signal.value.equals("")){
				float f=Float.parseFloat(signal.value);
				int ii=(int) (f*100);
		    	f=(float) (ii/100.0);
				lstRow.add(f+"");
				}else
				{
					
					lstRow.add("");
				}
		    	
			//	lstRow.add(signal.severity+"");
				lstRow.add(signal.meaning+"");
				lstRow.add(signal.unit);
				lstContends.add(lstRow);
			}
		} else
		{
			// 无需排序
			Iterator<Hashtable.Entry<String, Signal>> it = listSignals.entrySet().iterator();
			while (it.hasNext())
			{
				Hashtable.Entry<String, Signal> entry = it.next();
				Signal signal = entry.getValue();

				List<String> lstRow = new ArrayList<String>();

				lstRow.add(signal.name);
				lstRow.add(getDate(signal.freshtime * 1000, "yyyy.MM.dd HH:mm:ss"));
				if(!signal.value.equals("")){
					float f=Float.parseFloat(signal.value);
					int ii=(int) (f*100);
			    	f=(float) (ii/100.0);
					lstRow.add(f+"");
					}else
					{
						lstRow.add("");
					}
				//lstRow.add(signal.severity + "");
				lstRow.add(signal.meaning+"");
				lstRow.add(signal.unit);
				lstContends.add(lstRow);
			}
		}

		updateContends(lstTitles, lstContends);
		//System.out.println("123：我真的跑进来了");
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
		//System.out.println("123signalLit"+bNeedUpdate);
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
	
	MainWindow m_rRenderWindow = null;
	Rect m_rBBox = null;
	
	public boolean m_bNeedINIT = true;
	public boolean m_bneedupdate = true;
	
	// TODO: 临时代替数据
	boolean m_needsort = true;
	ArrayList<String> m_sortedarray = null;
	List<String> lstTitles = null;
	List<List<String>> lstContends = null;
}
