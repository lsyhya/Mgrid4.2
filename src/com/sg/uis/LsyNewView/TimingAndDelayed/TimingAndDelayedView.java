package com.sg.uis.LsyNewView.TimingAndDelayed;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.mgrid.main.MGridActivity;
import com.mgrid.main.MainWindow;
import com.mgrid.main.R;
import com.mgrid.util.ExpressionUtils;
import com.mgrid.util.TimeUtils;
import com.sg.common.CFGTLS;
import com.sg.common.IObject;
import com.sg.common.LanguageStr;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Environment;
import android.os.Handler;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import comm_service.service;
import data_model.ipc_control;

public class TimingAndDelayedView extends Button implements IObject, OnClickListener, TimingAndDelayedHandle {
	private Rect m_rBBox;

	private int m_nPosX = 152;
	private int m_nPosY = 287;
	private int m_nWidth = 75;
	private int m_nHeight = 23;
	private int m_nZIndex = 1;
	private String m_cFontColor;
	private float m_fFontSize = 16;

	private int nowHour = -1;

	public boolean m_bneedupdate = false;

	private String m_strID = "";
	private String m_strType = "";
	private String m_strOpenExpression = "";
	private String m_strCloseExpression = "";
	private String m_strHorizontalContentAlignment = "";
	private String m_strVerticalContentAlignment = "";

	private MainWindow m_rRenderWindow = null;

	private List<String> cmdYKPList1 = new ArrayList<>();
	private List<String> cmdYKPList2 = new ArrayList<>();
	private List<ipc_control> c_control = new ArrayList<ipc_control>();
	private List<ipc_control> o_control = new ArrayList<ipc_control>();
	private List<EditText> etList = new ArrayList<>();
	private List<TextView> tvList = new ArrayList<>();
	private List<TimingAndDelayed> tadList = new ArrayList<>();

	private EditText et1, et2, et3, et4, et5, et6;
	private TextView tv1, tv2, tv3, tv4, tv5, tv6;
	private Button btn1;

	private Timer timer;
	private int delayed = 10;
	private String index = "";

	private String filePath = "/mgrid/log/timing/";

	private String openTime = LanguageStr.openTime;
	private String delayedTime = LanguageStr.delayedTime;
	private String show = LanguageStr.show;
	private String ok = LanguageStr.OK;
	private String text9 = LanguageStr.text9;
	private String text21 = LanguageStr.text21;
	private String text22 = LanguageStr.text22;

	public TimingAndDelayedView(Context context) {

		super(context);
		init(context);

	}

	@SuppressLint("ClickableViewAccessibility")
	private void init(Context context) {

		m_rBBox = new Rect();

		et1 = new EditText(context);
		et2 = new EditText(context);
		et3 = new EditText(context);
		et4 = new EditText(context);
		et5 = new EditText(context);
		et6 = new EditText(context);
		etList.add(et1);
		etList.add(et2);
		etList.add(et3);
		etList.add(et4);
		etList.add(et5);
		etList.add(et6);
		setETTextColorAndSize("#000000", 15);

		tv1 = new TextView(context);
		tv2 = new TextView(context);
		tv3 = new TextView(context);
		tv4 = new TextView(context);
		tv5 = new TextView(context);
		tv6 = new TextView(context);
		tvList.add(tv1);
		tvList.add(tv2);
		tvList.add(tv3);
		tvList.add(tv4);
		tvList.add(tv5);
		tvList.add(tv6);
		setTVTextColorAndSize("#000000", 15);

		btn1 = new Button(context);
		setBtnColorAndSize("#000000", 15);

		AlarmReceiver.setTimingAndDelayedHandle(this);

	}

	private void setBtnColorAndSize(String color, float size) {

		btn1.setTextColor(Color.parseColor(color));
		btn1.setTextSize(size);
		btn1.setBackgroundResource(android.R.drawable.btn_default);
		btn1.setText(ok);
		btn1.setGravity(Gravity.CENTER);
		btn1.setOnClickListener(this);
	}

	private void setETTextColorAndSize(String color, float size) {
		for (EditText et : etList) {

			et.setTextColor(Color.parseColor(color));
			et.setTextSize(size);
			et.setBackgroundResource(R.drawable.et_select);
			et.setInputType(InputType.TYPE_CLASS_NUMBER);
		}
	}

	private void setTVTextColorAndSize(String color, float size) {

		int i = 1;
		for (TextView tv : tvList) {

			if (i == 6) {

				tv.setText(delayedTime);

			} else {
				tv.setText(openTime + i);
			}

			tv.setTextColor(Color.parseColor(color));
			tv.setTextSize(size);
			i++;
		}

	}

	private Handler handler = new Handler() {

		public void handleMessage(android.os.Message msg) {

			switch (msg.what) {
			case 0:

				btn1.setText(show);

				break;
			}

		};

	};

	@Override
	public void doLayout(boolean bool, int l, int t, int r, int b) {

		if (m_rRenderWindow == null)
			return;
		int nX = l + (int) (((float) m_nPosX / (float) MainWindow.FORM_WIDTH) * (r - l));
		int nY = t + (int) (((float) m_nPosY / (float) MainWindow.FORM_HEIGHT) * (b - t));
		int nWidth = (int) (((float) (m_nWidth) / (float) MainWindow.FORM_WIDTH) * (r - l));
		int nHeight = (int) (((float) (m_nHeight) / (float) MainWindow.FORM_HEIGHT) * (b - t));

		m_rBBox.left = nX;
		m_rBBox.top = nY;
		m_rBBox.right = nX + nWidth;
		m_rBBox.bottom = nY + nHeight;

		float f = (float) (nHeight / 10);
		int h = getTextHeight("开启时间", true);

		if (m_rRenderWindow.isLayoutVisible(m_rBBox)) {

			for (int i = 0; i < tvList.size(); i++) {

				tvList.get(i).layout((int) (nX + nWidth / 10.0), (int) (nY + nHeight * (i * 11) / 70.0),
						(int) (nX + nWidth * 4 / 10.0), (int) (nY + nHeight * (7 + i * 11) / 70.0));

				tvList.get(i).setPadding(0, (int) ((f - h) / 2), 0, 0);

			}

			for (int i = 0; i < etList.size(); i++) {

				etList.get(i).layout((int) (nX + nWidth * 4 / 10.0), (int) (nY + nHeight * (i * 11) / 70.0),
						(int) (nX + nWidth * 9 / 10.0), (int) (nY + nHeight * (7 + i * 11) / 70.0));

				etList.get(i).setPadding(0, (int) ((f - h) / 2), 0, 0);
			}

			btn1.layout((int) (nX + nWidth * 3 / 10.0), (int) (nY + nHeight * 63 / 70.0),
					(int) (nX + nWidth * 7 / 10.0), (int) (nY + nHeight));

		}

	}

	private int getTextHeight(String str, boolean boo) {
		Rect rect = new Rect();
		getPaint().getTextBounds(str, 0, str.length(), rect);

		if (boo) {
			return rect.height();
		} else {
			return rect.width();
		}
	}

	@Override
	public void setUniqueID(String strID) {

		m_strID = strID;

	}

	@Override
	public String getUniqueID() {

		return m_strID;
	}

	@Override
	public void setType(String strType) {

		m_strType = strType;

	}

	@Override
	public String getType() {

		return m_strType;
	}

	@Override
	public void parseProperties(String strName, String strValue, String strResFolder) throws Exception {

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
		} else if ("Content".equals(strName)) {

		} else if ("FontSize".equals(strName)) {

			if (!strValue.isEmpty()) {
				m_fFontSize = Float.parseFloat(strValue);
			}

		} else if ("FontColor".equals(strName)) {

			if (!strValue.isEmpty()) {

				m_cFontColor = strValue;
				setTVTextColorAndSize(m_cFontColor, m_fFontSize);
				setETTextColorAndSize(m_cFontColor, m_fFontSize);

			}

		} else if ("CmdExpressionOpen".equals(strName)) {

			m_strOpenExpression = strValue;
			cmdYKPList1 = parseExCmd(m_strOpenExpression);

		} else if ("CmdExpressionClose".equals(strName)) {
			m_strCloseExpression = strValue;

			cmdYKPList2 = parseExCmd(m_strCloseExpression);

		} else if ("HorizontalContentAlignment".equals(strName)) {
			m_strHorizontalContentAlignment = strValue;
		} else if ("VerticalContentAlignment".equals(strName)) {
			m_strVerticalContentAlignment = strValue;
		} else if ("Index".equals(strName)) {

			index = strValue;

			MGridActivity.xianChengChi.execute(new Runnable() {

				@Override
				public void run() {

					readData();
					m_bneedupdate = true;

				}
			});

		}

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
			double padSize = CFGTLS.getPadHeight(m_nHeight, MainWindow.FORM_HEIGHT, getTextSize());
			setPadding(0, (int) padSize, 0, 0);
		} else if ("Center".equals(m_strVerticalContentAlignment)) {
			nFlag |= Gravity.CENTER_VERTICAL;
			double padSize = CFGTLS.getPadHeight(m_nHeight, MainWindow.FORM_HEIGHT, getTextSize()) / 2f;
			setPadding(0, (int) padSize, 0, (int) padSize);
		}

		setGravity(nFlag);

	}

	private List<String> parseExCmd(String str) {

		if (str != null && !str.equals("")) {

			return ExpressionUtils.getExpressionUtils().parseYKP(str);
		}

		return null;

	}

	@Override
	public void addToRenderWindow(MainWindow rWin) {

		m_rRenderWindow = rWin;
		for (EditText et : etList) {

			m_rRenderWindow.addView(et);

		}

		for (TextView tv : tvList) {

			m_rRenderWindow.addView(tv);

		}

		m_rRenderWindow.addView(btn1);

	}

	@Override
	public void removeFromRenderWindow(MainWindow rWin) {

		rWin.removeView(this);

	}

	@Override
	public void updateWidget() {

	}

	@Override
	public boolean updateValue() {

		int hour = Integer.parseInt(TimeUtils.getHour());

		if (hour != nowHour) {
			nowHour = hour;

			for (TimingAndDelayed time : tadList) {

				if (time.getTiming() == nowHour) {
					// Log.e("time4", "handle");
					timingHandle();
					setTimeTask();
					break;

				}

			}

		}

		return false;
	}

	private void setTimeTask() {

		timer = new Timer();

		timer.schedule(new TimerTask() {

			@Override
			public void run() {

				delayedHandle();

			}
		}, delayed * 1000 * 60);

	}

	@Override
	public boolean needupdate() {

		return true;
	}

	@Override
	public void needupdate(boolean bNeedUpdate) {

		this.m_bneedupdate = true;

	}

	@Override
	public String getBindingExpression() {

		return "Binding{[Value[Equip:1-Temp:175-Signal:6]]}";
	}

	@Override
	public View getView() {

		return this;
	}

	@Override
	public int getZIndex() {

		return m_nZIndex;
	}

	private void sendOpenCmd() {

		if (!m_strCloseExpression.equals("") && !m_strOpenExpression.equals("") && cmdYKPList1 != null
				&& cmdYKPList2 != null) {

			if (o_control.size() == 0) {
				for (String cmd : cmdYKPList1) {

					ipc_control ipc = new ipc_control();
					String[] cmds = cmd.split("-");
					ipc.equipid = Integer.parseInt(cmds[0]);
					ipc.ctrlid = Integer.parseInt(cmds[2]);
					ipc.valuetype = Integer.parseInt(cmds[3]);
					ipc.value = cmds[4];
					o_control.add(ipc);
				}
			}

			service.send_control_cmd(service.IP, service.PORT, o_control);

		}
	}

	private void sendCloseCmd() {

		if (!m_strCloseExpression.equals("") && !m_strOpenExpression.equals("") && cmdYKPList1 != null
				&& cmdYKPList2 != null) {

			if (c_control.size() == 0) {
				for (String cmd : cmdYKPList2) {

					ipc_control ipc = new ipc_control();
					String[] cmds = cmd.split("-");
					ipc.equipid = Integer.parseInt(cmds[0]);
					ipc.ctrlid = Integer.parseInt(cmds[2]);
					ipc.valuetype = Integer.parseInt(cmds[3]);
					ipc.value = cmds[4];
					c_control.add(ipc);
				}
			}

			// Log.e("close",
			// c_control.get(0).equipid+":"+c_control.get(0).ctrlid+":"+c_control.get(0).valuetype+":"+c_control.get(0).value);
			service.send_control_cmd(service.IP, service.PORT, c_control);

		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

	}

	@Override
	public void onClick(View v) {

		if (v == btn1) {

			if (btn1.getText().toString().equals(show)) {
				showData();
			} else if (btn1.getText().toString().equals(ok)) {
				setData();
			}

		}

	}

	private void showData() {

		btn1.setText(ok);
		for (int i = 0; i < tadList.size(); i++) {

			etList.get(i).setText(tadList.get(i).getTiming() + "");
		}

		et6.setText(delayed + "");

	}

	private void setData() {
		String str1 = et1.getText().toString();
		String str2 = et2.getText().toString();
		String str3 = et3.getText().toString();
		String str4 = et4.getText().toString();
		String str5 = et5.getText().toString();
		String str6 = et6.getText().toString();

		if (!str6.isEmpty()) {

			delayed = Integer.parseInt(str6);
			tadList.clear();

			if (!str1.isEmpty()) {
				addTAD(Integer.parseInt(str1), Integer.parseInt(str6));
			}
			if (!str2.isEmpty()) {
				addTAD(Integer.parseInt(str2), Integer.parseInt(str6));
			}
			if (!str3.isEmpty()) {
				addTAD(Integer.parseInt(str3), Integer.parseInt(str6));
			}
			if (!str4.isEmpty()) {
				addTAD(Integer.parseInt(str4), Integer.parseInt(str6));
			}
			if (!str5.isEmpty()) {
				addTAD(Integer.parseInt(str5), Integer.parseInt(str6));
			}

			if (tadList.size() == 0) {
				Toast.makeText(getContext(), text21, Toast.LENGTH_SHORT).show();
				return;
			}

			m_bneedupdate = true;

			MGridActivity.xianChengChi.execute(new Runnable() {

				@Override
				public void run() {

					saveData();

				}
			});

			Toast.makeText(getContext(), text9, Toast.LENGTH_SHORT).show();

		} else {

			Toast.makeText(getContext(), text22, Toast.LENGTH_SHORT).show();

		}
	}

	private void saveData() {

		try {
			if (!index.isEmpty()) {
				File f = new File(filePath);
				if (!f.exists()) {
					f.mkdir();
				}
				File file = new File(filePath + index + ".timing");
				if (!file.exists()) {

					file.createNewFile();

				}
				BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "gb2312"));
				for (TimingAndDelayed time : tadList) {

					bw.write(time.getTiming() + "");
					bw.newLine();

				}

				bw.write("delayed:" + delayed);
				bw.flush();
				bw.close();

			}
		} catch (IOException e) {

			e.printStackTrace();
		}
	}

	private void readData() {

		try {
			if (!index.isEmpty()) {
				File f = new File(filePath);
				if (!f.exists()) {

					return;
				}
				File file = new File(filePath + index + ".timing");
				if (!file.exists()) {

					return;

				}
				BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "gb2312"));
				String str;
				while ((str = br.readLine()) != null) {
					if (str.startsWith("delayed")) {
						str = str.replace("delayed:", "");
						delayed = Integer.parseInt(str);

					} else {

						TimingAndDelayed tim = new TimingAndDelayed(Integer.parseInt(str), 0);
						tadList.add(tim);
					}
				}

				br.close();

				handler.sendEmptyMessage(0);

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public List<TimingAndDelayed> getTimingAndDelayedList() {
		return tadList;
	}

	/**
	 * 把延时对象添加进list
	 * 
	 * @param timing
	 * @param delayed
	 */
	private void addTAD(int timing, int delayed) {

		TimingAndDelayed tad = new TimingAndDelayed(timing, delayed);
		tadList.add(tad);

	}

	@Override
	public void timingHandle() {

		sendOpenCmd();

	}

	@Override
	public void delayedHandle() {

		sendCloseCmd();

	}

}
