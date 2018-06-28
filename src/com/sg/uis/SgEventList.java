package com.sg.uis;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.mgrid.data.DataGetter;
import com.mgrid.data.EquipmentDataModel.Event;
import com.mgrid.main.MGridActivity;
import com.mgrid.main.MainWindow;
import com.mgrid.main.SoundService;
import com.mgrid.util.ExpressionUtils;
import com.sg.common.IObject;
import com.sg.common.LanguageStr;
import com.sg.common.TotalVariable;
import com.sg.common.UtExpressionParser;
import com.sg.common.UtExpressionParser.stBindingExpression;
import com.sg.common.UtExpressionParser.stExpression;
import com.sg.common.UtTable;
import comm_service.service;

import data_model.ipc_control;

/** 事件列表 */
public class SgEventList extends UtTable implements IObject {

	private String DeviceName=LanguageStr.DeviceName;
	private String AlarmName=LanguageStr.AlarmName;
	private String AlarmMeaning=LanguageStr.AlarmMeaning;
	private String AlarmSeverity=LanguageStr.AlarmSeverity;
	private String StartTime=LanguageStr.StartTime;
    private Hashtable<String, Hashtable<String, Event>> listEvents = null;
	private List<String> cmd_list = new ArrayList<String>();

	public SgEventList(Context context) {
		super(context);
		m_rBBox = new Rect();	
		m_listTempEvents = new Hashtable<String, Hashtable<String, Event>>();



		// 表头
		lstTitles = new ArrayList<String>();
		// 名称，含义，开始时间
		lstTitles.add(DeviceName);
		lstTitles.add(AlarmName);
		lstTitles.add(AlarmMeaning);
		// lstTitles.add("信号数值");
		lstTitles.add(AlarmSeverity);
		lstTitles.add(StartTime);
		//lstTitles.add("结束时间  ");

		lstContends = new ArrayList<List<String>>();
		mythread.start();
	}

	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				
				updateContends(lstTitles, lstContends); // 表格行显示
				update();
				break;
			case 2:
				startSound();
				break;
			case 3:
				if (0 != service.send_control_cmd(service.IP, service.PORT,
						MGridActivity.lstCtrlDo1)) {
					System.out.println("发送失败");
				} else {
					System.out.println("发送成功");
				}
				break;
			case 4:
				if (0 != service.send_control_cmd(service.IP, service.PORT,
						MGridActivity.lstCtrlDo2)) {
					System.out.println("发送失败");
				} else {
					System.out.println("发送成功");
				}
				break;
			case 5:
				stopSound();
				break;
			default:
				break;
			}
		};
	};

	private void startSound() {
		Intent intent = new Intent(m_rRenderWindow.m_oMgridActivity,
				SoundService.class);
		intent.putExtra("playing", true);
		m_rRenderWindow.m_oMgridActivity.startService(intent);
	}
	private void stopSound() {
		Intent intent = new Intent(m_rRenderWindow.m_oMgridActivity,
				SoundService.class);
		intent.putExtra("playing", false);
		m_rRenderWindow.m_oMgridActivity.startService(intent);
	}

	Thread mythread = new Thread(new Runnable() {
		@Override
		public void run() {

			while (true) {
				try {
					
					
					
					Thread.sleep(500);
					handler.sendEmptyMessage(1);
				} catch (Exception e) {

				}
			}
		}
	});

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
			notifyTableLayoutChange(nX, nY, nX + nWidth, nY + nHeight);

			for (int i = 0; i < m_title.length; ++i)
				m_title[i].layout(nX + i * nWidth / m_title.length, nY - 18,
						nX + i * nWidth / m_title.length + nWidth
								/ m_title.length, nY);
		}
	}

	@Override
	public void addToRenderWindow(MainWindow rWin) {

		this.setClickable(true);
		this.setBackgroundColor(m_cBackgroundColor);

		m_bUseTitle = false;
		m_title = new TextView[lstTitles.size()];
		for (int i = 0; i < m_title.length; i++) {
			m_title[i] = new TextView(getContext());
			// m_title[i].setTextColor(Color.BLACK);
			// m_title[i].setTextSize(25);
			// m_title[i].setBackgroundColor(Color.GRAY);
			m_title[i].setGravity(Gravity.CENTER);
			m_title[i].setText(lstTitles.get(i));
			rWin.addView(m_title[i]);
		}

		m_rRenderWindow = rWin;
		rWin.addView(this);
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

			// 设定列表坐标参数
			m_nLeft = m_nPosX;
			m_nTop = m_nPosY;
			m_nRight = m_nLeft + m_nTableWidth;
			m_nBottom = m_nTop + m_nTableHeight;
		} else if ("Size".equals(strName)) {
			String[] arrSize = strValue.split(",");
			m_nWidth = Integer.parseInt(arrSize[0]);
			m_nHeight = Integer.parseInt(arrSize[1]);

			// 设定列表坐标参数
			m_nTableWidth = m_nWidth;
			m_nTableHeight = m_nHeight;
			m_nRight = m_nLeft + m_nTableWidth;
			m_nBottom = m_nTop + m_nTableHeight;
		} else if ("Alpha".equals(strName)) {
			m_fAlpha = Float.parseFloat(strValue);
		} else if ("Expression".equals(strName)) {
			m_strExpression = strValue;
			parse_cmd();
			// String str = strValue;
			//
			// if( !"".equals(str) ){
			// String a[] = str.split(":");
			// String b[] = a[1].split("\\]");
			// equiptID = b[0];
			//
			// }else{
			// equiptID = "0";
			// }
		} else if ("ForeColor".equals(strName)) {
			m_cForeColor = Color.parseColor(strValue);
			this.setFontColor(m_cForeColor);
		} else if ("BackgroundColor".equals(strName)) {
			m_cBackgroundColor = Color.parseColor(strValue);
			Log.d("背景颜色", strValue);
			this.setBackgroundColor(m_cBackgroundColor);
		} else if ("BorderColor".equals(strName)) {
			m_cBorderColor = Color.parseColor(strValue);
		} else if ("OddRowBackground".equals(strName)) {
			m_cOddRowBackground = Color.parseColor(strValue);
			//ji=strValue;
		} else if ("EvenRowBackground".equals(strName)) {
			m_cEvenRowBackground = Color.parseColor(strValue);
			//ou=strValue;
		} else if ("CmdExpression".equals(strName)) {
			m_strCmdExpression = strValue;
			parseCmdExpression(m_strCmdExpression);
		}
	}

	public boolean parse_cmd() {

		if (m_strExpression.equals("") || m_strExpression == null)
			return false;
		cmd_list = ExpressionUtils.getExpressionUtils().parseOnlyEq(
				m_strExpression);
		return true;
	}

	// 解析控制命令表达式m_strCmdExpression
	public boolean parseCmdExpression(String strExpression) {
		if ("".equals(strExpression))
			return false;
		if (strExpression == null)
			return false;
		// Log.e("eventList cmd send->m_strCmdExpression:",strExpression);
		// 解析出控件表达式，返回控件表达式类
		stExpression oMathExpress = UtExpressionParser.getInstance()
				.parseExpression(m_strCmdExpression);
		if (oMathExpress != null) {
			// 遍历控件表达式各个数据单元表达式类
			Iterator<HashMap.Entry<String, stBindingExpression>> it = oMathExpress.mapObjectExpress
					.entrySet().iterator();
			while (it.hasNext()) {
				stBindingExpression oBindingExpression = it.next().getValue();
				if (oBindingExpression == null)
					break;
				ipc_control ipcC = new ipc_control();
				ipcC.equipid = oBindingExpression.nEquipId;
				ipcC.ctrlid = oBindingExpression.nCommandId;
				ipcC.valuetype = oBindingExpression.nValueType;
				ipcC.value = oBindingExpression.strValue;// bindingExpression.strValue;

				lstCtrl.add(ipcC);

				oMathExpress = null;
				ipcC = null;
			}
		}
		return true;
	}

	// 发送控制命令
	public boolean send_cmd() {
		if (0 != service.send_control_cmd(service.IP, service.PORT, lstCtrl)) {

		} else {

		}

		return true;
	}

	@Override
	public void initFinished() {
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

	public String getBindingExpression() {
		return m_strExpression;
	}

	public void onDraw(Canvas canvas) {
		if (m_rRenderWindow == null)
			return;
		if (m_rRenderWindow.isLayoutVisible(getBBox()) == false)
			return;

		super.onDraw(canvas);
	}

	@Override
	public void updateWidget() {
		
	
		update();
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean updateValue() {

		
		
		m_bneedupdate = false;
		if (m_rRenderWindow == null)
			return false;
		
		if (m_rRenderWindow.m_bHasRandomData == false) { // 是否用随机数据
			listEvents = m_rRenderWindow.m_oShareObject.m_mapEventListDatas
					.get(this.getUniqueID());

		} else {
			listEvents = m_listTempEvents; // 要用随机数据
		}
		

		if (listEvents == null) { //
			lstContends.clear();
			oldEvenLists=null;
			handler.sendEmptyMessage(5);
			
			return true; //
			
		}

		
		
		if (MGridActivity.alarmWay != null
				&& !MGridActivity.alarmWay.equals("")) {
			MGridActivity.xianChengChi.execute(new Runnable() {

				@Override
				public void run() {
					if (needAlarm(listEvents)) {
						if (MGridActivity.alarmWay.equals("wav")) {
							handler.sendEmptyMessage(2);
						} else if (MGridActivity.alarmWay.equals("DO1")) {
							if (MGridActivity.lstCtrlDo1 != null)
								handler.sendEmptyMessage(3);
						} else {
							if (MGridActivity.lstCtrlDo2 != null)
								handler.sendEmptyMessage(4);
						}

					}
				}
			});
		}

		// 表数据
		Iterator<Hashtable.Entry<String, Hashtable<String, Event>>> equip_it = listEvents
				.entrySet().iterator();
		lstContends.clear();
		while (equip_it.hasNext()) {
			Hashtable.Entry<String, Hashtable<String, Event>> entry = equip_it
					.next();
			String id = entry.getKey();

			String equipname = DataGetter.getEquipmentName(id);
			Iterator<Hashtable.Entry<String, Event>> it = entry.getValue()
					.entrySet().iterator();
			for (int i = 0; i < cmd_list.size(); i++) {
				equiptID = cmd_list.get(i);
				if ("0".equals(equiptID) || id.equals(equiptID)) {
					while (it.hasNext()) {
						Hashtable.Entry<String, Event> event_entry = it.next();
						Event event = event_entry.getValue();

						List<String> lstRow = new ArrayList<String>();

						lstRow.add(equipname);
						lstRow.add(event.name);
						lstRow.add(event.meaning + "");
						
						// lstRow.add(event.value);
						try {
							
							switch (event.grade) {
							case 1:
								lstRow.add(one);
								break;
							case 2:
								lstRow.add(two);
								break;
							case 3:
								lstRow.add(three);
								break;
							case 4:
								lstRow.add(four);
								break;

							}
						} catch (Exception e) {

						}
						//lstRow.add(String.valueOf(event.grade));

						if (m_needsort)
							lstRow.add(String.valueOf(event.starttime * 1000));
						else
							lstRow.add(getDate(event.starttime * 1000,
									"yyyy.MM.dd HH:mm:ss"));

						lstContends.add(lstRow);
					}
				}
			}
		}

		// 按时间排序处理
		if (m_needsort) {
			class SortByEventTime implements Comparator<Object> {
				public int compare(Object o1, Object o2) {

					List<String> l1 = (List<String>) o1;

					List<String> l2 = (List<String>) o2;

					Long s1 = Long.valueOf(l1.get(4));
					Long s2 = Long.valueOf(l2.get(4));

					return s2.compareTo(s1);
				}
			}

			Collections.sort(lstContends, new SortByEventTime());

			// 处理时间
			Iterator<List<String>> it = lstContends.iterator();
			while (it.hasNext()) {
				List<String> next = it.next();
				String startime = getDate(Long.valueOf(next.get(4)),
						"yyyy.MM.dd HH:mm:ss");
				next.set(4, startime);
			}
		}

		// updateContends(lstTitles, lstContends_two); //表格行显示
		
		
     
		return true;
	}

	@Override
	public boolean needupdate() {

		return m_bneedupdate;
	}

	private boolean needAlarm(Hashtable<String, Hashtable<String, Event>> event) {
		Hashtable<String, Hashtable<String, Event>> newEvenLists = event;
		
		if (newEvenLists == null || newEvenLists.size() == 0)
			return false;
		if (oldEvenLists == null) { // 第一次判断时 old_eventss为空 如果new_eventss包含这个告警
			// 就报警.
			System.out.println("old_eventss为空");
			oldEvenLists = newEvenLists;
			return true;
		} else {
			if (newEvenLists.size() > oldEvenLists.size()) {
				oldEvenLists = newEvenLists;
				return true;
			} else {
				Iterator<String> equip_it = newEvenLists.keySet().iterator();
				while (equip_it.hasNext()) {
					String e_equipId = equip_it.next();

					if (oldEvenLists.containsKey(e_equipId)) {
						Hashtable<String, Event> new_events = newEvenLists
								.get(e_equipId);
						Hashtable<String, Event> old_events = oldEvenLists
								.get(e_equipId);

						Iterator<String> event_itt = new_events.keySet()
								.iterator();
						while (event_itt.hasNext()) {
							String event_id = event_itt.next();

							if (old_events.containsKey(event_id) == false) {

								oldEvenLists = newEvenLists;
								return true;
							}
						}
					} else {

						oldEvenLists = newEvenLists;
						return true;
					}
				}
			}
		}
		oldEvenLists = newEvenLists;
		return false;
	}

	@Override
	public void needupdate(boolean bNeedUpdate) {

		
		m_bneedupdate = bNeedUpdate;
		if(TotalVariable.ALARMPIEMAP.size()>0)
		{
			Iterator<Entry<String, IObject>> it=TotalVariable.ALARMPIEMAP.entrySet().iterator();
			while(it.hasNext())
			{
				Entry<String, IObject> entry=it.next();
				entry.getValue().needupdate(m_bneedupdate);
				
			}
		}
		
		if ("".equals(m_strCmdExpression))
			return;
		if (m_strCmdExpression == null)
			return;

		// 判断有新的告警
		// 获取新的告警信息
		Hashtable<String, Hashtable<String, Event>> new_eventss = DataGetter
				.getRTEventList();
		if (new_eventss == null)
			return;
		if (new_eventss.isEmpty())
			return;
		if (old_eventss.isEmpty()) {
			send_cmd(); // 发送控制命令
			old_eventss = new_eventss;
			return;
		}
		// 遍历设备id
		Iterator<String> equip_it = new_eventss.keySet().iterator();
		while (equip_it.hasNext()) {
			String e_equipId = equip_it.next();

			if (old_eventss.containsKey(e_equipId)) {
				Hashtable<String, Event> new_events = new_eventss
						.get(e_equipId);
				Hashtable<String, Event> old_events = old_eventss
						.get(e_equipId);

				Iterator<String> event_itt = new_events.keySet().iterator();
				while (event_itt.hasNext()) {
					String event_id = event_itt.next();

					if (old_events.containsKey(event_id) == false) {
						send_cmd(); // 发送控制命令
						old_eventss = new_eventss;
					}
				}
			} else {
				send_cmd(); // 发送控制命令
				old_eventss = new_eventss;
			}
		}
		

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
	int m_nZIndex = 16;
	int m_nPosX = 2;
	int m_nPosY = 103;
	int m_nWidth = 321;
	int m_nHeight = 175;
	float m_fAlpha = 0.8f;
	String m_strExpression = "Binding{[Equip[Equip:115]]}";
	String m_strCmdExpression = "";
	int m_cForeColor = 0xFF00FF00;
	int m_cBackgroundColor = 0x00000000;
	int m_cBorderColor = 0xFFFFFFFF;
	MainWindow m_rRenderWindow = null;
	Rect m_rBBox = null;
	Hashtable<String, Hashtable<String, Event>> m_listTempEvents = null;

	public boolean m_bneedupdate = true;

	List<ipc_control> lstCtrl = new ArrayList<ipc_control>(); // 控制命令类链表
	public static Hashtable<String, Hashtable<String, Event>> old_eventss = new Hashtable<String, Hashtable<String, Event>>();

	private Hashtable<String, Hashtable<String, Event>> oldEvenLists = null;

	String equiptID = "0";

	// 固定标题栏
	TextView[] m_title;

	// TODO: 临时代替方案
	boolean m_needsort = true;
	List<String> lstTitles = null;
	ArrayList<List<String>> lstContends = null;
	ArrayList<List<String>> lstContends_two = null;
	String one=LanguageStr.one,two=LanguageStr.two,three=LanguageStr.three,four=LanguageStr.four;

}

