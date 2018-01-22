package com.sg.uis.LsyNewView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.mgrid.data.DataGetter;
import com.mgrid.main.MGridActivity;
import com.mgrid.main.MainWindow;
import com.mgrid.main.R;
import com.sg.common.CFGTLS;
import com.sg.common.IObject;
import comm_service.local_file;

import data_model.local_his_event;

/** 单个设备的所有历史告警 */
@SuppressLint({ "ShowToast", "InflateParams", "RtlHardcoded",
		"ClickableViewAccessibility" })
public class EquipHistoryAlarm extends TextView implements IObject {

	private ListView listView;
	private List list;
	private SimpleAdapter adapter;

	public EquipHistoryAlarm(Context context) {
		super(context);

		m_oPaint = new Paint();
		m_rBBox = new Rect();
		listView = new ListView(context);
		list = new ArrayList<HashMap<String, Object>>();
		getData();
		adapter = new SimpleAdapter(context, list, R.layout.elist,
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
			m_cFontColor = Color.parseColor(strValue);
			// this.setTextColor(m_cFontColor);
		} else if ("ClickEvent".equals(strName))
			m_strClickEvent = strValue;
		else if ("Url".equals(strName))
			m_strUrl = strValue;
		else if ("CmdExpression".equals(strName))
			m_strCmdExpression = strValue;

		else if ("HorizontalContentAlignment".equals(strName))
			m_strHorizontalContentAlignment = strValue;
		else if ("VerticalContentAlignment".equals(strName))
			m_strVerticalContentAlignment = strValue;
		else if ("Expression".equals(strName)) {

			mExpression = strValue;

			parse(mExpression);

			new Thread(new Runnable() {

				@Override
				public void run() {

					updateValue();
					handler.sendEmptyMessage(0);
				}
			}).start();

		}
	}

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:

				adapter.notifyDataSetChanged(); // adapter数据刷新
				// 为了解决界面不自动刷新的问题。
				listView.layout(m_rBBox.left, m_rBBox.top, m_rBBox.right,
						m_rBBox.bottom - m_nLayoutBottomOffset);
				m_nLayoutBottomOffset = -m_nLayoutBottomOffset;

				break;

			}
		};
	};

	private synchronized List<local_his_event> getHisEvent() {
		if (equitId == null || equitId.equals(""))
			return null;
		String filename = "hisevent-" + equitId;

		List<local_his_event> his_event_list = new ArrayList<local_his_event>();
		try {

			local_file l_file = new local_file();

			if (!l_file.has_file(filename, 3)) {

				return null;
			}

			if (!l_file.read_all_line()) {

				return null;
			}
			List<String> list = l_file.buflist1;
			l_file = null;
			his_event_list.clear();

			Iterator<String> iter = list.iterator();
			while (iter.hasNext()) {
				String buf = iter.next();

				local_his_event his_event = new local_his_event();

				his_event.read_string(buf);

				his_event_list.add(his_event);

				if (his_event_list.size() > 500) {
					break;
				}
				his_event = null;
			}
		} catch (Exception e) {

		}

		return his_event_list;
	}

	private void parse(String mExpression) {
		if (mExpression == null || mExpression.equals(""))
			return;

		mExpression = mExpression.replace("Binding{[Value[", "");
		mExpression = mExpression.replace("]]}", "");
		String[] s = mExpression.split("-");
		String[] s0 = s[0].split(":");
		equitId = s0[1];
		String[] s1 = s[1].split(":");
		tempId = s1[1];
		String[] s2 = s[2].split(":");
		singleId = s2[1];
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
	public boolean updateValue() {

		m_bneedupdate = false;
		List<local_his_event> hisEvent = getHisEvent();
		if (hisEvent == null)
			return false;

		Iterator<local_his_event> iter = hisEvent.iterator();
		list.clear();
		while (iter.hasNext()) {

			local_his_event his_event = iter.next();
			String finishTime = his_event.finish_time;
		
			String startTime = his_event.start_time;// 2016-09-29 14:23:35
			if ("1970-01-01".equals(finishTime.substring(0, 10))) {
				isEnd = false;
				continue; // 未结束的告警不显示在历史告警上面？
			} else {
				isEnd = true;
			}
			HashMap<Long, String> hash = MGridActivity.AlarmShieldTimer
					.get(equitId + "_" + his_event.event_id);
			if (hash != null) {
				// 字符串转换成日期
				SimpleDateFormat sdf = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss");
				Iterator<Map.Entry<Long,String>> it=hash.entrySet().iterator();
				long oldTime=0;
				long later=0;
				while(it.hasNext())
				{
					Map.Entry<Long,String> map=it.next();
					oldTime=map.getKey();
					later=Long.parseLong(map.getValue());
				}
				try {
					Date date = sdf.parse(startTime);
					long time = date.getTime();
					System.out.println(oldTime+":::::"+time);
					if(time>oldTime&&time<(oldTime+later))
					{
						continue;
					}
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}

			String eventName = DataGetter.getEventName(equitId,
					his_event.event_id);
			String meaning = his_event.event_mean;
			String grade = his_event.severity;

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("time", startTime);
			map.put("value", eventName + "-" + meaning + "-等级：" + grade);
			if (isEnd)
				map.put("img", R.drawable.hk);
			else
				map.put("img", R.drawable.hj);
			list.add(map);
		}
		return true;
	}

	@Override
	public boolean needupdate() {

		return m_bneedupdate;
	}

	@Override
	public void needupdate(boolean bNeedUpdate) {

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

	MainWindow m_rRenderWindow = null;

	Paint m_oPaint = null;
	Rect m_rBBox = null;

	public boolean m_bneedupdate = true;
	private String mExpression = "";
	private String equitId = "";
	private String tempId = "";
	private String singleId = "";
	private int m_nLayoutBottomOffset = 1;
	private boolean isEnd = false;
}
