package com.sg.uis;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import android.R;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;

import com.mgrid.main.MainWindow;
import com.sg.common.IObject;
import com.sg.common.MutiThreadShareObject;
import com.svenkapudija.fancychart.FancyChart;
import com.svenkapudija.fancychart.FancyChartPointListener;
import com.svenkapudija.fancychart.data.ChartData;
import com.svenkapudija.fancychart.data.Point;

import comm_service.service;
import data_model.ipc_data_signal;
import data_model.ipc_history_signal;

@SuppressLint("SimpleDateFormat")
public class SgCurveLineChart extends View implements IObject {
	public SgCurveLineChart(Context context) {
		super(context);
		
		m_oLineChart = new FancyChart(context, null);

		m_oLineChart.setOnPointClickListener(new FancyChartPointListener() {	
			@Override
			public void onClick(Point point) {
				
			}
		});
		
		setDataLineSize(1);
		m_HistoryDatas = new ArrayList<List<ipc_history_signal>>();
		m_listRealTimeLineDatas = new ArrayList<List<RealTimeLineData>>();
		m_arrRadioButtons = new RadioButton[4];
		//bt1
		m_arrRadioButtons[0] = new RadioButton(this.getContext());
		m_arrRadioButtons[0].setText("实时");
		m_arrRadioButtons[0].setChecked(true);
		// bt2
		m_arrRadioButtons[1] = new RadioButton(this.getContext());
		m_arrRadioButtons[1].setText("24小时");
		//bt3
		m_arrRadioButtons[2] = new RadioButton(this.getContext());
		m_arrRadioButtons[2].setText("7天");
		// bt4
		m_arrRadioButtons[3] = new RadioButton(this.getContext());
		m_arrRadioButtons[3].setText("30天");
		
		for (int i = 0; i < 4; ++i)
		{
			m_arrRadioButtons[i].setTextColor(Color.BLACK);
			m_arrRadioButtons[i].setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					String strText = (String) ((RadioButton) v).getText();
					int nIndex = -1;
					for (int j = 0; j < 4; ++j)
					{
						if ("实时".equals(strText))
							nIndex = 0;
						else if ("24小时".equals(strText))
							nIndex = 1;
						else if ("7天".equals(strText))
							nIndex = 2;
						else if ("30天".equals(strText))
							nIndex = 3;
					}
					m_nType = nIndex;
					m_bneedupdate = true;
					m_arrRadioButtons[nIndex].setChecked(true);
					for (int j = 0; j < 4; ++j)
					{
						if (j != nIndex)
							m_arrRadioButtons[j].setChecked(false);
					}

					// 清空实时曲线数据
					if (nIndex != 0)
					{
						for (int i = 0; i < m_listRealTimeLineDatas.size(); ++i)
						{
							m_listRealTimeLineDatas.get(i).clear();
						}
					}
				}
			});
		}
		m_rBBox = new Rect();
	}

	public void setDataLineSize(int nSize) {
		m_oLineChart.getChartStyle().setDataLineWidth(nSize);
	}
	
	@Override
	public void doLayout(boolean bool, int l, int t, int r, int b) {
		// TODO Auto-generated method stub
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
			m_oLineChart.layout(nX, nY+20, nX+nWidth, nY+nHeight);
			//m_oLineChart.layout(nX, nY+(int)(nHeight*0.15f), nX+nWidth, nY+nHeight);
			for (int i = 0; i < m_arrRadioButtons.length; ++i)
				m_arrRadioButtons[i].layout(nX + i * nWidth / 4, nY, nX + i * nWidth / 4 + nWidth / 4, nY+20);
				//m_arrRadioButtons[i].layout(nX + i * nWidth / 4, nY-(int)(nHeight*0.05f), nX + i * nWidth / 4 + nWidth / 4, nY+(int)(nHeight*0.125f));
		}
	}

	public void onDraw(Canvas canvas) {
		if (m_rRenderWindow == null)
			return;
		if (m_rRenderWindow.isLayoutVisible(getBBox()) == false)
			return;
		super.onDraw(canvas);
	}
	
	public Rect getBBox() {
		return m_rBBox;
	}
	
	@Override
	public void setUniqueID(String strID) {
		// TODO Auto-generated method stub
		m_strID = strID;
	}

	@Override
	public String getUniqueID() {
		// TODO Auto-generated method stub
		return m_strID;
	}

	@Override
	public void setType(String strType) {
		// TODO Auto-generated method stub
		m_strType = strType;
	}

	@Override
	public String getType() {
		// TODO Auto-generated method stub
		return m_strType;
	}

	@Override
	public void parseProperties(String strName, String strValue, String strResFolder) throws Exception {
		// TODO Auto-generated method stub
		if ("ZIndex".equals(strName)) {
       	 	m_nZIndex = Integer.parseInt(strValue);
       	    if (MainWindow.MAXZINDEX < m_nZIndex) MainWindow.MAXZINDEX = m_nZIndex;
        }
        else if ("Location".equals(strName)) {
       	 	String[] arrStr = strValue.split(",");
       	 	m_nPosX = Integer.parseInt(arrStr[0]);
       	 	m_nPosY = Integer.parseInt(arrStr[1]);
        }
        else if ("Size".equals(strName)) {
       	 	String[] arrSize = strValue.split(",");
       	 	m_nWidth = Integer.parseInt(arrSize[0]);
       	 	m_nHeight = Integer.parseInt(arrSize[1]);
        }
        else if ("Alpha".equals(strName)) {
       	 	m_fAlpha = Float.parseFloat(strValue);
        }
        else if ("Expression".equals(strName))
    		m_strExpression = strValue;
	}

	@Override
	public void initFinished()
	{
	}

	@Override
	public void addToRenderWindow(MainWindow rWin) {
		// TODO Auto-generated method stub
		m_rRenderWindow = rWin;
		rWin.addView(m_oLineChart);
		for (int i = 0; i < m_arrRadioButtons.length; ++i)
			rWin.addView(m_arrRadioButtons[i]);
	}

	@Override
	public void removeFromRenderWindow(MainWindow rWin) {
		// TODO Auto-generated method stub
		m_rRenderWindow = null;
		for (int i = 0; i < m_arrRadioButtons.length; ++i)
			rWin.removeView(m_arrRadioButtons[i]);
		rWin.removeView(m_oLineChart);
	}

	@Override
	public void updateWidget() {
		switch (m_nType) {
		case 0:
			if (!processRealTimeLines()) break;
			
			if (m_listRealTimeLineDatas.get(0).size() >= m_nMaxPoints)
			{
				for (int i = 0; i < m_listRealTimeLineDatas.size(); ++i)
				{
					m_listRealTimeLineDatas.get(i).remove(0);// m_listRealTimeLineDatas.get(i).clear();
					m_listRealTimeLineDatas.get(i).remove(1);
				}
			} else
			{
				m_oLineChart.invalidate();
			}
			break;
			
		case 1:
		case 2:
		case 3:
			processHistoryLineDatas();
			break;
		}
	}

	@Override
	public boolean updateValue()
	{
		//m_bneedupdate = false;
		
		switch (m_nType) {
		case 0:
			break;
		case 1:
		case 2:
		case 3:
			m_bneedupdate = false;
			queryHistoryLineDatas();
			break;
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
		// 曲线控件的更新行为由控件本身决定
	    //m_bneedupdate = bNeedUpdate;
    }
	
	@Override
	public String getBindingExpression() {
		// TODO Auto-generated method stub
		return m_strExpression;
	}

	@Override
	public View getView() {
		// TODO Auto-generated method stub
		return null;
	}

	public int getZIndex()
	{
		return m_nZIndex;
	}
	
	class RealTimeLineData {
		String strAxisX = "";
		int nValue = 0;
	}
// Params:
	String m_strID = "";
	String m_strType = "HistorySignalCurve";    
	int m_nZIndex = 14;
	int m_nPosX = 69;
	int m_nPosY = 392;
	int m_nWidth = 372;
	int m_nHeight = 186;
	float m_fAlpha = 1.0f;
	String m_strExpression = "Binding{[Value[Equip:114-Temp:173-Signal:4]]|[Value[Equip:114-Temp:173-Signal:5]]|[Value[Equip:114-Temp:173-Signal:6]]}";
	MainWindow m_rRenderWindow = null;	
	Rect m_rBBox = null;
	FancyChart m_oLineChart = null;
	RadioButton[] m_arrRadioButtons;
	int m_nType = 0;  // 0--实时  1--24小时  2--7天  3--30天
	List<List<RealTimeLineData>> m_listRealTimeLineDatas = null;
	int m_nMaxPoints = 320;	// 最大显示点
	
	public boolean m_bneedupdate = true;
	private List<List<ipc_history_signal>> m_HistoryDatas = null;
	
	/** 处理实时曲线数据 */
	private boolean processRealTimeLines() {
		List<String> listChartData = m_rRenderWindow.m_oShareObject.getMutiChartData(this.getUniqueID());
		
		// 是否使用随机数据
		if (m_rRenderWindow.m_bHasRandomData == true) {
			listChartData.clear();
			Random rand = new Random(); 
			listChartData.add(rand.nextInt(250)+"");
			listChartData.add(rand.nextInt(100)+"");
			listChartData.add(rand.nextInt(100)+"");
		}
		
		if (listChartData == null || listChartData.size() == 0)
			return false;
		
		int nSize = listChartData.size();
		if (m_listRealTimeLineDatas.size() != nSize) {
			for (int i = 0; i < m_listRealTimeLineDatas.size(); ++i)
				m_listRealTimeLineDatas.get(i).clear();
			m_listRealTimeLineDatas.clear();
			
			for (int i = 0; i < nSize; ++i) {
				m_listRealTimeLineDatas.add(new ArrayList<RealTimeLineData>());
			}
		}
		
		//SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		SimpleDateFormat sDateFormat = new SimpleDateFormat("HH:mm:ss");
		String date = sDateFormat.format(new java.util.Date());
		try {
			for (int i = 0; i < nSize; ++i) {
				RealTimeLineData point = new RealTimeLineData();
				point.strAxisX = date;
				point.nValue = (int) (Float.parseFloat(listChartData.get(i))*1000);
				m_listRealTimeLineDatas.get(i).add(point);
			}
		} catch (Exception e) {
			
		}
		
		m_oLineChart.clearChartData();
		for (int i = 0; i < nSize; ++i) {
			List<RealTimeLineData> line = m_listRealTimeLineDatas.get(i);
			String LineColor = ChartData.LINE_COLOR_BLUE;
			if ((i+1) % 2 == 0)
				LineColor = ChartData.LINE_COLOR_RED;
			else if ((i+1)%3 == 0)
			{
				LineColor = ChartData.LINE_COLOR_GREEN;
			}
			
			ChartData data = new ChartData(LineColor);
			for(int j = 0; j < m_nMaxPoints; j++) {
				if (j < line.size()) {
					data.addPoint(line.size()-j-1, line.get(line.size()-j-1).nValue);
					if (j == 0 || j == line.size()-1 || j == line.size() / 2)
						data.addXValue(line.size()-j-1, line.get(line.size()-j-1).strAxisX);
				}
				else {
					data.addXValue(line.size()-j-1, "");
				}
			}
			m_oLineChart.addData(data);
		}


		// 曲线上的节点 大于 m_nMaxPoints则清除头2个点
		return true;
	}

	/** 处理各种历史曲线 */
	private boolean queryHistoryLineDatas() {
		// List<List<ipc_history_signal>> listHistoryDatas = m_rRenderWindow.m_oShareObject.getHistorySignal(this.getUniqueID());
		
		Calendar rightNow = Calendar.getInstance();
		long curr_ms = rightNow.getTimeInMillis()/1000;  
		
		m_HistoryDatas.clear();
		
		List<ipc_history_signal> list = service.get_his_sig_list(service.IP, service.PORT, 1, 10, curr_ms-60*60*24, 60*60*24, 1024, true);
		
		if (null == list) return false;
		
		List<ipc_history_signal> list1 = new ArrayList<ipc_history_signal>();
		List<ipc_history_signal> list2 = new ArrayList<ipc_history_signal>();
		List<ipc_history_signal> list3 = new ArrayList<ipc_history_signal>();
		
		Iterator<ipc_history_signal> it = list.iterator();
		while (it.hasNext())
		{
			ipc_history_signal signal = it.next();
			if (signal.sigid == 8)
			{
				list1.add(signal);
			}
			else if (signal.sigid == 22)
			{
				list2.add(signal);
			}
			else if (signal.sigid == 24)
			{
				list3.add(signal);
			}
		}
		
		m_HistoryDatas.add(list1);
		m_HistoryDatas.add(list2);
		m_HistoryDatas.add(list3);
		
		return true;
	}
	
	private void processHistoryLineDatas() {
		// List<List<ipc_history_signal>> listHistoryDatas = m_rRenderWindow.m_oShareObject.getHistorySignal(this.getUniqueID());
		/*
		Calendar rightNow = Calendar.getInstance();
		long curr_ms = rightNow.getTimeInMillis()/1000;  
		
		List<List<ipc_history_signal>> listHistoryDatas = new ArrayList<List<ipc_history_signal>>();
		
		List<ipc_history_signal> list = service.get_his_sig_list(service.IP, service.PORT, 1, 10, curr_ms, 60*60*24, 0, true);
		
		List<ipc_history_signal> list1 = new ArrayList<ipc_history_signal>();
		List<ipc_history_signal> list2 = new ArrayList<ipc_history_signal>();
		List<ipc_history_signal> list3 = new ArrayList<ipc_history_signal>();
		
		Iterator<ipc_history_signal> it = list.iterator();
		while (it.hasNext())
		{
			ipc_history_signal signal = it.next();
			if (signal.sigid == 10)
			{
				list1.add(signal);
			}
			else if (signal.sigid == 17)
			{
				list2.add(signal);
			}
			else if (signal.sigid == 24)
			{
				list3.add(signal);
			}
		}
		
		listHistoryDatas.add(list1);
		listHistoryDatas.add(list2);
		listHistoryDatas.add(list3);
		*/
		
		List<List<ipc_history_signal>> listHistoryDatas = m_HistoryDatas;
		
		// 是否使用随机数据
		if (m_rRenderWindow.m_bHasRandomData == true) {
			listHistoryDatas = null;
			listHistoryDatas = new ArrayList<List<ipc_history_signal>>();
			for (int i = 0; i < 3; ++i) {
				listHistoryDatas.add(new ArrayList<ipc_history_signal>());
				
				// test 30d
				for (int j = 0; j < 15; j++) {
					Random rand = new Random(); 
					ipc_history_signal sg0 = new ipc_history_signal();
					sg0.value = rand.nextInt(250)+"";
					sg0.history_time = "2014-04-12 10:20:32";
					listHistoryDatas.get(i).add(sg0);
				}
				// test 24h
				for (int j = 0; j < 3; j++) {
					Random rand = new Random(); 
					ipc_history_signal sg0 = new ipc_history_signal();
					sg0.value = rand.nextInt(250)+"";
					sg0.history_time = "2014-05-05 10:20:32";
					listHistoryDatas.get(i).add(sg0);
				}
				// test 7d
				for (int j = 0; j < 2; j++) {
					Random rand = new Random(); 
					ipc_history_signal sg0 = new ipc_history_signal();
					sg0.value = rand.nextInt(250)+"";
					sg0.history_time = "2014-05-01 10:20:32";
					listHistoryDatas.get(i).add(sg0);
				}
			}
		}
		
		if (listHistoryDatas == null || listHistoryDatas.size() == 0)
			return;
		
		int nSize = listHistoryDatas.size();
		m_oLineChart.clearChartData();
    
		Date currentDate = new Date();
		try {
			for (int i = 0; i < nSize; ++i) {
				String LineColor = ChartData.LINE_COLOR_BLUE;
				if (i % 2 == 0)
					LineColor = ChartData.LINE_COLOR_RED;
				ChartData data = new ChartData(LineColor);
				List<ipc_history_signal> singleLine = listHistoryDatas.get(i);
				List<ipc_history_signal> splitLineDatas = new ArrayList<ipc_history_signal>();
				for (int j = 0; j < singleLine.size(); ++j) {
					Date dHistory = getDateFromString(singleLine.get(j).history_time);
					if (dHistory == null)
						continue;
					
					// 24小时
					if (m_nType == 1) {
						if ((currentDate.getTime()-dHistory.getTime())/(24 * 60 * 60 * 1000) <= 1) {
							splitLineDatas.add(singleLine.get(j));
						}
					}
					// 7天
					else if (m_nType == 2) {
						if ((currentDate.getTime()-dHistory.getTime())/(24 * 60 * 60 * 1000) <= 7) {
							splitLineDatas.add(singleLine.get(j));
						}
					}
					// 30天
					else if (m_nType == 3) {
						if ((currentDate.getTime()-dHistory.getTime())/(24 * 60 * 60 * 1000) <= 30) {
							splitLineDatas.add(singleLine.get(j));
						}
					}
				}
				
				int nSkip = splitLineDatas.size() / 24;
				for(int j = 0; j < splitLineDatas.size() - nSkip; j++) {
					data.addPoint(j, Integer.parseInt(splitLineDatas.get(j).value));
					if (j == 0 || j == splitLineDatas.size()-1 || j == splitLineDatas.size() / 2)
						data.addXValue(j, splitLineDatas.get(j).history_time);
				}
				m_oLineChart.addData(data);
			}
		} catch(Exception e) {
			
		}
		m_oLineChart.invalidate();
		listHistoryDatas.clear();
	}
	
	private Date getDateFromString(String strDate) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = null;
		try {
			date = df.parse(strDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}
}
