package com.sg.uis.LsyNewView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.mgrid.data.DataGetter;
import com.mgrid.data.EquipmentDataModel.Event;
import com.mgrid.main.MGridActivity;
import com.mgrid.main.MainWindow;
import com.mgrid.main.R;
import com.mgrid.main.SoundService;
import com.sg.common.CFGTLS;
import com.sg.common.IObject;
import com.sg.common.MySimpleAdapter;
import com.sg.common.TotalVariable;
import com.sg.common.UtTable;

/** 告警等级分类 */
@SuppressLint({ "ShowToast", "InflateParams", "RtlHardcoded",
		"ClickableViewAccessibility" })
public class AlarmLevel extends TextView implements IObject {

	// TabHost tablehost;

	public AlarmLevel(Context context) {
		super(context);

		m_oPaint = new Paint();
		m_rBBox = new Rect();

		listView = new ListView(context);
		adapter = new MySimpleAdapter(context, list, R.layout.vlist,
				new String[] { "time", "value", "img" }, new int[] {
						R.id.title, R.id.info, R.id.img });
		listView.setAdapter(adapter);

	}

	private void getData() {
		for (int i = 0; i < 20; i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("time", "G" + i);
			map.put("value", "google " + i);
			map.put("img", R.drawable.ic_jiqiren);
			list.add(map);
		}
	}

	@SuppressLint("DrawAllocation")
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
			listView.layout(nX, nY, nX + nWidth, nY + nHeight);
		}
	}

	@Override
	public void addToRenderWindow(MainWindow rWin) {
		m_rRenderWindow = rWin;
		rWin.addView(this);
		rWin.addView(listView);

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

		} else if ("Content".equals(strName)) {
			m_strContent = strValue;

		} else if ("FontFamily".equals(strName))
			m_strFontFamily = strValue;
		else if ("FontSize".equals(strName)) {
			float fWinScale = (float) MainWindow.SCREEN_WIDTH
					/ (float) MainWindow.FORM_WIDTH;
			m_fFontSize = Float.parseFloat(strValue) * fWinScale;

		} else if ("IsBold".equals(strName))
			m_bIsBold = Boolean.parseBoolean(strValue);
		else if ("FontColor".equals(strName)) {
			if (!strValue.isEmpty())
				adapter.setInfoColor(strValue);
		} else if ("ColorData".equals(strName)) {
			if (!strValue.isEmpty())
				adapter.setLinColor(strValue);

		} else if ("ScaleColor".equals(strName)) {
			if (!strValue.isEmpty()) {
				adapter.setTitleColor(strValue);
				adapter.notifyDataSetChanged();
			}
		} else if ("CmdExpression".equals(strName))
			m_strCmdExpression = strValue;

		else if ("HorizontalContentAlignment".equals(strName))
			m_strHorizontalContentAlignment = strValue;
		else if ("VerticalContentAlignment".equals(strName))
			m_strVerticalContentAlignment = strValue;
		else if ("Expression".equals(strName)) {

			mExpression = strValue;
			new Thread(new Runnable() {

				@Override
				public void run() {
					while (true) {
						if (updateValue()) {

							handler.sendEmptyMessage(0);
						}
						try {
							Thread.sleep(2000);
						} catch (InterruptedException e) {

							e.printStackTrace();
						}
					}

				}
			}).start();
		} else if ("Grade".equals(strName)) {
			grade = strValue;
		}
	}

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:

				list.clear();
				for (int i = 0; i < old_list.size(); i++) {
					list.add(old_list.get(i));
				}
				adapter.notifyDataSetChanged(); // adapter数据刷新
				// 为了解决界面不自动刷新的问题。
				listView.layout(m_rBBox.left, m_rBBox.top, m_rBBox.right,
						m_rBBox.bottom - m_nLayoutBottomOffset);
				m_nLayoutBottomOffset = -m_nLayoutBottomOffset;
				break;

			case 2:
				System.out.println("我要开始播放了");
				startSound();
				break;

			case 5:
				stopSound();
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

	@Override
	public void updateWidget() {

	}

	@Override
	public boolean needupdate() {

		return false;
	}

	@Override
	public boolean updateValue() {

		m_bneedupdate = false;

		final Hashtable<String, Hashtable<String, Event>> listEvents = DataGetter
				.getRTEventList();

		if (listEvents == null || listEvents.size() == 0) {
			
			OldSize = 0;
			old_list.clear();
			handler.sendEmptyMessage(5);
			return true;
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

		if (OldSize == listEvents.size()) {
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		OldSize = listEvents.size();

		Iterator<Hashtable.Entry<String, Hashtable<String, Event>>> equaip_it = listEvents
				.entrySet().iterator();
		old_list.clear();
		while (equaip_it.hasNext()) {
			Hashtable.Entry<String, Hashtable<String, Event>> entry = equaip_it
					.next();
			String equitid = entry.getKey();
			String equipname = DataGetter.getEquipmentName(equitid);

			Iterator<Hashtable.Entry<String, Event>> it = entry.getValue()
					.entrySet().iterator();
			while (it.hasNext()) {
				Hashtable.Entry<String, Event> event_entry = it.next();

				Event event = event_entry.getValue();
				String gradeLevel = "";
				switch (event.grade) {
				case 1:
					gradeLevel = "通知";
					break;

				case 2:
					gradeLevel = "一般告警";
					break;
				case 3:
					gradeLevel = "严重告警";
					break;
				case 4:
					gradeLevel = "紧急告警";
					break;
				}
				String value = equipname + "——" + event.name + ":"
						+ event.meaning + "    等级：" + gradeLevel;
				Map<String, Object> map_Value = new HashMap<String, Object>();
				map_Value.put("time", event.starttime * 1000);
				map_Value.put("value", value);
				if (grade.equals("0")) {
					if (event.grade == 3) {
						map_Value.put("img", R.drawable.hj);

					}
					if (event.grade == 2) {
						map_Value.put("img", R.drawable.hi);

					}
					if (event.grade == 1) {
						map_Value.put("img", R.drawable.hk);
					}
				} else if (grade.equals("1") && event.grade == 1) {

					map_Value.put("img", R.drawable.hk);

				} else if (grade.equals("2") && event.grade == 2) {

					map_Value.put("img", R.drawable.hi);

				} else if (grade.equals("3") && event.grade == 3) {

					map_Value.put("img", R.drawable.hj);

				} else {
					continue;
				}
				old_list.add(map_Value);
			}
		}
		if (old_list != null && old_list.size() > 0) {
			Collections.sort(old_list, new SortByEventTime());
			for (int i = 0; i < old_list.size(); i++) {
				Map<String, Object> map = old_list.get(i);
				Long time = (Long) map.get("time");
				String stime = UtTable.getDate(time, "yyyy.MM.dd HH:mm:ss");
				map.put("time", stime);
			}
		}
		return true;
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

	class SortByEventTime implements Comparator<Object> {
		public int compare(Object o1, Object o2) {

			Map<String, Object> m1 = (Map<String, Object>) o1;

			Map<String, Object> m2 = (Map<String, Object>) o2;

			Long s1 = (Long) m1.get("time");
			Long s2 = (Long) m2.get("time");

			return s2.compareTo(s1);
		}
	}

	@Override
	public void needupdate(boolean bNeedUpdate) {
            
            if(TotalVariable.ALARMPIEMAP.size()>0)
     		{
     			Iterator<Entry<String, IObject>> it=TotalVariable.ALARMPIEMAP.entrySet().iterator();
     			while(it.hasNext())
     			{
     				Entry<String, IObject> entry=it.next();
     				entry.getValue().needupdate(m_bneedupdate);
     				
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

	private ListView listView = null;

	Intent m_oHomeIntent = null;
	public boolean m_bneedupdate = true;
	private String mExpression = "";
	private List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
	private List<Map<String, Object>> old_list = new ArrayList<Map<String, Object>>();
	private int OldSize = 0;
	private MySimpleAdapter adapter;
	private int m_nLayoutBottomOffset = 1;
	private String grade = "0";
	private Hashtable<String, Hashtable<String, Event>> oldEvenLists = null;
}
