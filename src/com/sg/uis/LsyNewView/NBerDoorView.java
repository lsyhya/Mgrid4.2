package com.sg.uis.LsyNewView;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mgrid.main.MGridActivity;
import com.mgrid.main.MainWindow;
import com.mgrid.main.R;
import com.mgrid.mysqlbase.SqliteUtil;
import com.mgrid.util.ByteUtil;
import com.sg.common.CFGTLS;
import com.sg.common.IObject;
import com.sg.common.MyAdapter;
import com.sg.common.lsyBase.DoorConfig;
import com.sg.common.lsyBase.MyDoorAdapter;
import com.sg.common.lsyBase.MyDoorEvent;
import com.sg.common.lsyBase.MyDoorUser;
import com.sg.common.lsyBase.NiuberManager;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog.Builder;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import android_serialport_api.SerialPort;
import android_serialport_api.SerialPortFinder;

public class NBerDoorView extends TextView implements IObject, OnClickListener, OnItemLongClickListener,
		OnItemClickListener, OnItemSelectedListener, Runnable {

	private MGridActivity mActivity;
	private Button btn, btn2, btn3, btnAdd, btnOpen, btnopenDoor, btnVip, btngetUser, btndeleteUser;

	private TextView tvname, tvCardID, tvUserID, tvPW, tvTime, tvPort, tvBt, tvSp1, tvSp2;
	private EditText etname, etCardID, etUserID, etPW, etTime;

	private PopupWindow popupWindow1, popupWindow2;
	// private Spinner spinner1,spinner2;

	private List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
	private List<Map<String, Object>> list2 = new ArrayList<Map<String, Object>>();
	private MyDoorAdapter adapter, adapter2;

	private String[] devices;
	private String[] ports;

	private String devicesPath = "";
	private String portsPath = "";

	private ArrayList<String> deList = new ArrayList<>();
	private ArrayList<String> pList = new ArrayList<>();

	private ListView listview, listview2;
	private MyAdapter myAdapter1, myAdapter2;

	private Rect rextR = new Rect();
	private Rect rextR2 = new Rect();

	private BufferedInputStream mInputStream;
	private OutputStream mOutputStream;
	private SerialPort mSerialPort;

	private List<MyDoorUser> mydoorU = new ArrayList<>();

	private boolean isAlive = true;
	private boolean isAdd = true;
	private boolean isAllDelete = true;
	private boolean isDelete = true;

	private int m_nLayoutBottomOffset2 = 1;

	private boolean isRun = false;
	private String data4 = NiuberManager.setVip();

	// 数据库
	private SqliteUtil sql;
	private String name, ci, ui, ps, time;
	private String currCID;

	public NBerDoorView(Context context) {
		super(context);

		mActivity = (MGridActivity) context;

		init(context);
	}

	private void getSqlite(Context context) {
		sql = new SqliteUtil(context);
		sql.openorgetSql();
	}

	private void init(Context context) {

		m_rBBox = new Rect();
		this.setClickable(true);
		this.setGravity(Gravity.CENTER);
		setPadding(0, 0, 0, 0);

		getData();

		btn = new Button(context);
		btn2 = new Button(context);
		btn3 = new Button(context);
		btnAdd = new Button(context);
		btnOpen = new Button(context);
		btnopenDoor = new Button(context);
		btnVip = new Button(context);
		btngetUser = new Button(context);
		btndeleteUser = new Button(context);

		tvname = new TextView(context);
		tvCardID = new TextView(context);
		tvUserID = new TextView(context);
		tvPW = new TextView(context);
		tvTime = new TextView(context);
		tvPort = new TextView(context);
		tvBt = new TextView(context);
		tvSp1 = new TextView(context);
		tvSp2 = new TextView(context);

		etname = new EditText(context);
		etCardID = new EditText(context);
		etUserID = new EditText(context);
		etPW = new EditText(context);
		etTime = new EditText(context);

		listview = new ListView(context);
		listview2 = new ListView(context);

		tvSp1.setTag(100);
		tvSp2.setTag(101);
		tvSp1.setOnClickListener(this);
		tvSp2.setOnClickListener(this);

		// spinner1=new Spinner(context);
		// spinner2=new Spinner(context);

		initListview(listview);
		initListview2(listview2);

		// initSp(spinner1,devices);
		// initSp(spinner2,ports);

		initText(tvname, "用户名", "#8B8B8B", 16);
		initText(tvCardID, "卡号", "#8B8B8B", 16);
		initText(tvUserID, "ID", "#8B8B8B", 16);
		initText(tvPW, "密码", "#8B8B8B", 16);
		initText(tvTime, "时间", "#8B8B8B", 16);
		initText(tvPort, "串口", "#8B8B8B", 16);
		initText(tvBt, "波特率", "#8B8B8B", 16);

		initText(tvSp1, "串口", "#303030", 16);
		initText(tvSp2, "波特率", "#303030", 16);

		tvSp1.setBackgroundColor(Color.parseColor("#558B8B8B"));
		tvSp2.setBackgroundColor(Color.parseColor("#558B8B8B"));
		tvSp1.setGravity(Gravity.CENTER);
		tvSp2.setGravity(Gravity.CENTER);

		initEText(etname, "#8B8B8B", 16);
		initEText(etCardID, "#8B8B8B", 16);
		initEText(etUserID, "#8B8B8B", 16);
		initEText(etPW, "#8B8B8B", 16);
		initEText(etTime, "#8B8B8B", 16);

		initBtn(btn, "用户管理", "#159FBB", "#FDFDFF", 20, 1);
		initBtn(btn2, "记录", "#00A7C8", "#FDFDFF", 20, 2);
		initBtn(btn3, "设置", "#00A7C8", "#FDFDFF", 20, 3);
		initBtn(btnAdd, "添加", "#8B8B8B", "#8B8B8B", 16, 4);
		initBtn(btnOpen, "打开", "#8B8B8B", "#8B8B8B", 16, 5);
		initBtn(btnVip, "授权", "#8B8B8B", "#8B8B8B", 16, 6);
		initBtn(btnopenDoor, "开门", "#8B8B8B", "#8B8B8B", 16, 7);
		initBtn(btngetUser, "获取用户信息", "#8B8B8B", "#8B8B8B", 16, 8);
		initBtn(btndeleteUser, "删除所有用户", "#8B8B8B", "#8B8B8B", 16, 9);

		hindoneView(false);
		hindtheView(true);
		hindtwoView(true);

		setNoOnclickBtn(false);

	}

	private void getData() {

		SerialPortFinder spf = new SerialPortFinder();
		devices = spf.getAllDevicesPath();
		ports = getResources().getStringArray(R.array.baudrates);

		for (String str : devices) {

			deList.add(str);
		}

		for (String str : ports) {

			pList.add(str);
		}

	}

	private void initBtn(Button btns, String str, String BackgroundColor, String TextColor, int TextSize, int tag) {
		btns.setText(str);
		if (tag >= 4) {
			btns.setBackgroundResource(android.R.drawable.btn_default_small);
		} else {
			btns.setBackgroundColor(Color.parseColor(BackgroundColor));
		}
		btns.setTextColor(Color.parseColor(TextColor));
		btns.setTextSize(TextSize);
		btns.setTag(tag);
		btns.setOnClickListener(this);
	}

	private void initText(TextView tv, String str, String TextColor, int TextSize) {

		tv.setText(str);
		tv.setTextColor(Color.parseColor(TextColor));
		tv.setTextSize(TextSize);

	}

	private void initEText(EditText et, String TextColor, int TextSize) {

		et.setBackgroundResource(R.drawable.et_select);
		et.setTextColor(Color.parseColor(TextColor));
		et.setTextSize(TextSize);

	}

	private void initListview(ListView lv) {

		adapter = new MyDoorAdapter(mActivity, list, R.layout.door_list, new String[] { "text" },
				new int[] { R.id.tv_doorID });
		lv.setAdapter(adapter);
		lv.setBackgroundColor(Color.parseColor("#C7C8CA"));
		lv.setOnItemLongClickListener(this);
		lv.setOnItemClickListener(this);
	}

	private void initListview2(ListView lv) {

		adapter2 = new MyDoorAdapter(mActivity, list2, R.layout.door_list, new String[] { "text" },
				new int[] { R.id.tv_doorID });
		lv.setAdapter(adapter2);
		lv.setBackgroundColor(Color.parseColor("#C7C8CA"));
		// lv.setOnItemLongClickListener(this);
		// lv.setOnItemClickListener(this);
	}

	// private void initSp(Spinner sp, String[] data) {
	//
	// ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(mActivity,
	// R.layout.spring_item, data);
	//
	// sp.setAdapter(adapter1);
	//
	// sp.setOnItemSelectedListener(this);
	// sp.setGravity(Gravity.CENTER);
	//
	// }

	private void setPup1() {

		myAdapter1 = new MyAdapter(getContext(), deList);
		View view = mActivity.getLayoutInflater().inflate(R.layout.pop, null);
		popupWindow1 = new PopupWindow(view, tvSp1.getWidth(), 200, true);
		// 设置一个透明的背景，不然无法实现点击弹框外，弹框消失
		popupWindow1.setBackgroundDrawable(new BitmapDrawable());

		// 设置点击弹框外部，弹框消失
		popupWindow1.setOutsideTouchable(true);
		popupWindow1.setFocusable(true);
		popupWindow1.showAsDropDown(tvSp1);

		ListView lv = (ListView) view.findViewById(R.id.lv_list);

		// myAdapter.setTextColor(textColor);
		// myAdapter.setBtnColor(btnColor);

		lv.setAdapter(myAdapter1);
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

				devicesPath = deList.get(position);
				tvSp1.setText(deList.get(position));
				popupWindow1.dismiss();

			}
		});

	}

	private void setPup2() {

		myAdapter2 = new MyAdapter(getContext(), pList);
		View view = mActivity.getLayoutInflater().inflate(R.layout.pop, null);
		popupWindow2 = new PopupWindow(view, tvSp1.getWidth(), 200, true);
		// 设置一个透明的背景，不然无法实现点击弹框外，弹框消失
		popupWindow2.setBackgroundDrawable(new BitmapDrawable());

		// 设置点击弹框外部，弹框消失
		popupWindow2.setOutsideTouchable(true);
		popupWindow2.setFocusable(true);
		popupWindow2.showAsDropDown(tvSp2);

		ListView lv = (ListView) view.findViewById(R.id.lv_list);

		// myAdapter.setTextColor(textColor);
		// myAdapter.setBtnColor(btnColor);

		lv.setAdapter(myAdapter2);
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

				portsPath = pList.get(position);
				tvSp2.setText(pList.get(position));
				popupWindow2.dismiss();

			}
		});

	}

	@Override
	public void doLayout(boolean bool, int l, int t, int r, int b) {

		if (m_rRenderWindow == null)
			return;
		int nX = l + (int) (((float) m_nPosX / (float) MainWindow.FORM_WIDTH) * (r - l));
		int nY = t + (int) (((float) m_nPosY / (float) MainWindow.FORM_HEIGHT) * (b - t));
		int nWidth = (int) (((float) m_nWidth / (float) MainWindow.FORM_WIDTH) * (r - l));
		int nHeight = (int) (((float) m_nHeight / (float) MainWindow.FORM_HEIGHT) * (b - t));

		m_rBBox.left = nX;
		m_rBBox.top = nY;
		m_rBBox.right = nX + nWidth;
		m_rBBox.bottom = nY + nHeight;
		if (m_rRenderWindow.isLayoutVisible(m_rBBox)) {

			this.layout(nX, nY, nX + nWidth, nY + nHeight);

			float f = (float) (nHeight * 4 / 5 * 0.077);
			float x = (float) (nWidth / 2 * 0.1);

			btn.layout(nX, nY, nX + nWidth / 3, nY + nHeight / 6);
			btn2.layout(nX + nWidth / 3, nY, nX + nWidth * 2 / 3, nY + nHeight / 6);
			btn3.layout(nX + nWidth * 2 / 3, nY, nX + nWidth, nY + nHeight / 6);
			btnAdd.layout((int) (nX + 5 * x), (int) (nY + nHeight / 5 + f * 11), (int) (nX + x * 7),
					(int) (nY + nHeight / 5 + f * 12));
			btnOpen.layout((int) (nX + nWidth * 4 / 10), (int) (nY + nHeight * 2 / 5 + 3 * f),
					(int) (nX + nWidth * 7 / 10), (int) (nY + nHeight * 2 / 5 + f * 4));
			btnVip.layout((int) (nX + nWidth * 4 / 10), (int) (nY + nHeight * 2 / 5 + 5 * f),
					(int) (nX + nWidth * 7 / 10), (int) (nY + nHeight * 2 / 5 + f * 6));
			btnopenDoor.layout((int) (nX + nWidth * 4 / 10), (int) (nY + nHeight * 2 / 5 + 7 * f),
					(int) (nX + nWidth * 7 / 10), (int) (nY + nHeight * 2 / 5 + f * 8));

			btngetUser.layout((int) (nX + x * 11), (int) (nY + nHeight / 5 + f * 11), (int) (nX + x * 14),
					(int) (nY + nHeight / 5 + f * 12));
			btndeleteUser.layout((int) (nX + 16 * x), (int) (nY + nHeight / 5 + f * 11), (int) (nX + x * 19),
					(int) (nY + nHeight / 5 + f * 12));

			btn.setPadding(0, (nHeight / 5 - getTextHeight("用户管理", true)) / 2, 0, 0);
			btn2.setPadding(0, (nHeight / 5 - getTextHeight("控制", true)) / 2, 0, 0);
			btn3.setPadding(0, (nHeight / 5 - getTextHeight("设置", true)) / 2, 0, 0);

			tvname.layout((int) (nX + x), (int) (nY + nHeight / 5 + f), (int) (nX + x * 3),
					(int) (nY + nHeight / 5 + f * 2));
			tvCardID.layout((int) (nX + x), (int) (nY + nHeight / 5 + f * 3), (int) (nX + x * 3),
					(int) (nY + nHeight / 5 + f * 4));
			tvUserID.layout((int) (nX + x), (int) (nY + nHeight / 5 + f * 5), (int) (nX + x * 3),
					(int) (nY + nHeight / 5 + f * 6));
			tvPW.layout((int) (nX + x), (int) (nY + nHeight / 5 + f * 7), (int) (nX + x * 3),
					(int) (nY + nHeight / 5 + f * 9));
			tvTime.layout((int) (nX + x), (int) (nY + nHeight / 5 + f * 9), (int) (nX + x * 3),
					(int) (nY + nHeight / 5 + f * 10));

			tvPort.layout((int) (nX + nWidth * 3 / 10), (int) (nY + nHeight * 2 / 5 - f), (int) (nX + nWidth * 4 / 10),
					(int) (nY + nHeight * 2 / 5));
			tvBt.layout((int) (nX + nWidth * 3 / 10), (int) (nY + nHeight * 2 / 5 + f), (int) (nX + nWidth * 4 / 10),
					(int) (nY + nHeight * 2 / 5 + f * 2));

			tvSp1.layout((int) (nX + nWidth * 4 / 10), (int) (nY + nHeight * 2 / 5 - f), (int) (nX + nWidth * 7 / 10),
					(int) (nY + nHeight * 2 / 5));
			tvSp2.layout((int) (nX + nWidth * 4 / 10), (int) (nY + nHeight * 2 / 5 + f), (int) (nX + nWidth * 7 / 10),
					(int) (nY + nHeight * 2 / 5 + f * 2));

			tvname.setPadding(0, (int) (f - getTextHeight("用户名", true)) / 2, 0, 0);
			tvCardID.setPadding(0, (int) (f - getTextHeight("用户名", true)) / 2, 0, 0);
			tvUserID.setPadding(0, (int) (f - getTextHeight("用户名", true)) / 2, 0, 0);
			tvPW.setPadding(0, (int) (f - getTextHeight("用户名", true)) / 2, 0, 0);
			tvTime.setPadding(0, (int) (f - getTextHeight("用户名", true)) / 2, 0, 0);

			tvPort.setPadding(0, (int) (f - getTextHeight("串口", true)) / 2, 0, 0);
			tvBt.setPadding(0, (int) (f - getTextHeight("波特率", true)) / 2, 0, 0);
			tvSp1.setPadding(0, (int) (f - getTextHeight("串口", true)) / 2, 0, 0);
			tvSp2.setPadding(0, (int) (f - getTextHeight("波特率", true)) / 2, 0, 0);

			etname.layout((int) (nX + x * 3), (int) (nY + nHeight / 5 + f), (int) (nX + x * 9),
					(int) (nY + nHeight / 5 + f * 2));
			etCardID.layout((int) (nX + x * 3), (int) (nY + nHeight / 5 + f * 3), (int) (nX + x * 9),
					(int) (nY + nHeight / 5 + f * 4));
			etUserID.layout((int) (nX + x * 3), (int) (nY + nHeight / 5 + f * 5), (int) (nX + x * 9),
					(int) (nY + nHeight / 5 + f * 6));
			etPW.layout((int) (nX + x * 3), (int) (nY + nHeight / 5 + f * 7), (int) (nX + x * 9),
					(int) (nY + nHeight / 5 + f * 8));
			etTime.layout((int) (nX + x * 3), (int) (nY + nHeight / 5 + f * 9), (int) (nX + x * 9),
					(int) (nY + nHeight / 5 + f * 10));

			listview.layout((int) (nX + x * 11), (int) (nY + nHeight / 5 + f), (int) (nX + x * 19),
					(int) (nY + nHeight / 5 + f * 10));

			listview2.layout((int) (nX), (int) (nY + nHeight / 6), (int) (nX + nWidth), (int) (nY + nHeight));

			rextR.left = (int) (nX + x * 11);
			rextR.top = (int) (nY + nHeight / 5 + f);
			rextR.right = (int) (nX + x * 19);
			rextR.bottom = (int) (nY + nHeight / 5 + f * 10);

			rextR2.left = (int) (nX);
			rextR2.top = (int) (nY + nHeight / 5);
			rextR2.right = (int) (nX + nWidth);
			rextR2.bottom = (int) (nY + nHeight / 5 + nHeight);

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
	protected void onDraw(Canvas canvas) {

		if (m_rRenderWindow == null)
			return;
		if (m_rRenderWindow.isLayoutVisible(getBBox()) == false)
			return;

		int nWidth = (int) (((float) (m_nWidth) / (float) MainWindow.FORM_WIDTH)
				* (m_rRenderWindow.VIEW_RIGHT - m_rRenderWindow.VIEW_LEFT));
		int nHeight = (int) (((float) (m_nHeight) / (float) MainWindow.FORM_HEIGHT)
				* (m_rRenderWindow.VIEW_BOTTOM - m_rRenderWindow.VIEW_TOP));

		Paint m_oPaint = new Paint();
		m_oPaint.setColor(Color.parseColor("#8B8B8B"));
		m_oPaint.setStyle(Paint.Style.STROKE);
		canvas.drawRect(0, 0, nWidth, nHeight, m_oPaint);

		super.onDraw(canvas);

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
		} else if ("Alpha".equals(strName)) {
			m_fAlpha = Float.parseFloat(strValue);
		} else if ("RotateAngle".equals(strName)) {
			m_fRotateAngle = Float.parseFloat(strValue);
		} else if ("Content".equals(strName)) {
			m_strContent = strValue;
			c_Content = strValue;

		} else if ("FontFamily".equals(strName)) {
			m_strFontFamily = strValue;

		} else if ("FontSize".equals(strName)) {
			float fWinScale = (float) MainWindow.SCREEN_WIDTH / (float) MainWindow.FORM_WIDTH;
			m_fFontSize = Float.parseFloat(strValue) * fWinScale;

		} else if ("IsBold".equals(strName))
			m_bIsBold = Boolean.parseBoolean(strValue);
		else if ("FontColor".equals(strName)) {
			m_cFontColor = Color.parseColor(strValue);
			m_cStartFillColor = m_cFontColor;

		} else if ("HorizontalContentAlignment".equals(strName))
			m_strHorizontalContentAlignment = strValue;
		else if ("VerticalContentAlignment".equals(strName)) {
			m_strVerticalContentAlignment = strValue;
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
			double padSize = CFGTLS.getPadHeight(m_nHeight, MainWindow.FORM_HEIGHT, getTextSize()) / 2;
			setPadding(0, (int) padSize, 0, (int) padSize);
		}

		setGravity(nFlag);

	}

	@Override
	public void addToRenderWindow(MainWindow rWin) {
		m_rRenderWindow = rWin;

		getSqlite(m_rRenderWindow.m_oMgridActivity.getApplicationContext());
		sql.setListValues(list,mydoorU);

		rWin.addView(this);

		rWin.addView(btn);
		rWin.addView(btn2);
		rWin.addView(btn3);
		rWin.addView(btnAdd);
		rWin.addView(btnOpen);
		rWin.addView(btnVip);
		rWin.addView(btnopenDoor);
		rWin.addView(btngetUser);
		rWin.addView(btndeleteUser);

		rWin.addView(tvname);
		rWin.addView(tvCardID);
		rWin.addView(tvPW);
		rWin.addView(tvUserID);
		rWin.addView(tvTime);
		rWin.addView(tvPort);
		rWin.addView(tvBt);

		rWin.addView(etname);
		rWin.addView(etCardID);
		rWin.addView(etPW);
		rWin.addView(etUserID);
		rWin.addView(etTime);

		rWin.addView(listview);

		rWin.addView(tvSp1);
		rWin.addView(tvSp2);

		rWin.addView(listview2);

	}

	@Override
	public void removeFromRenderWindow(MainWindow rWin) {
		rWin.removeView(this);

	}

	@Override
	public void updateWidget() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean updateValue() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean needupdate() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void needupdate(boolean bNeedUpdate) {
		// TODO Auto-generated method stub

	}

	@Override
	public String getBindingExpression() {
		// TODO Auto-generated method stub
		return m_strCmdExpression;
	}

	@Override
	public View getView() {
		// TODO Auto-generated method stub
		return this;
	}

	@Override
	public int getZIndex() {
		// TODO Auto-generated method stub
		return m_nZIndex;
	}

	public Rect getBBox() {
		return m_rBBox;
	}

	String m_strID = "";
	String m_strType = "Label";
	int m_nZIndex = 1;
	int m_nPosX = 49;
	int m_nPosY = 306;
	int m_nWidth = 60;
	int m_nHeight = 30;
	float m_fAlpha = 1.0f;
	float m_fRotateAngle = 0.0f;
	String m_strContent = "设置内容";
	String m_strFontFamily = "微软雅黑";
	String c_Content = "";
	float m_fFontSize = 12.0f;
	boolean m_bIsBold = false;
	int m_cFontColor = 0xFF008000;
	int m_cStartFillColor = 0x00000000;
	String m_strHorizontalContentAlignment = "Center";
	String m_strVerticalContentAlignment = "Center";
	String m_strCmdExpression = "Binding{[Cmd[Equip:1-Temp:173-Command:1-Parameter:1-Value:1]]}";

	MainWindow m_rRenderWindow = null;

	Rect m_rBBox = null;

	public boolean m_bneedupdate = true;
	private int m_nLayoutBottomOffset = 1;

	@Override
	public void onClick(View v) {

		switch ((int) v.getTag()) {

		case 1:

			setView(btn, btn2, btn3);
			hindoneView(false);
			hindtheView(true);
			hindtwoView(true);
			break;

		case 2:

			setView(btn2, btn, btn3);
			hindoneView(true);
			hindtheView(true);
			hindtwoView(false);
			break;

		case 3:

			setView(btn3, btn, btn2);
			hindoneView(true);
			hindtheView(false);
			hindtwoView(true);
			break;

		case 4:

			add();

			break;
		case 100:

			setPup1();
			break;

		case 101:

			setPup2();
			break;

		case 5:

			open();
			break;

		case 6:

			getVip();

			break;
		case 7:

			openDoor();

			break;
		case 8:

			getUserData();

			break;

		case 9:

			deleteAllUser();

			break;

		}

	}

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {

			Object b = msg.obj;

			switch (msg.what) {
			case 0:

				if (b != null) {
					if ((boolean) b) {

						MyDoorUser my = new MyDoorUser(name, ci, ui, ps, time);
						//mydoorU.add(my);

						sql.addUserValue(my);
						sql.setListValues(list,mydoorU);

						Toast.makeText(getContext(), "添加成功", Toast.LENGTH_SHORT).show();

						updateList(true);

					} else {
						Toast.makeText(getContext(), "添加失败", Toast.LENGTH_SHORT).show();
					}
				} else {
					Toast.makeText(getContext(), "添加失败", Toast.LENGTH_SHORT).show();

				}

				isAdd = true;

				break;
			case 1:

				updateList(false);

				isAlive = true;
				break;

			case 2:

				if (b != null) {
					if ((boolean) b) {

						sql.cleanUserTable();
						mydoorU.clear();
						list.clear();

						Toast.makeText(getContext(), "删除成功", Toast.LENGTH_SHORT).show();

						updateList(true);

					} else {
						Toast.makeText(getContext(), "删除失败", Toast.LENGTH_SHORT).show();
					}

				} else {
					Toast.makeText(getContext(), "删除失败", Toast.LENGTH_SHORT).show();
				}

				isAllDelete = true;

				break;

			case 3:

				if (b != null) {
					if ((boolean) b) {

						sql.deleteValue(currCID);
						sql.setListValues(list,mydoorU);

					
						updateList(true);

						Toast.makeText(getContext(), "删除成功", Toast.LENGTH_SHORT).show();

					} else {
						Toast.makeText(getContext(), "删除失败", Toast.LENGTH_SHORT).show();
					}

				} else {

					Toast.makeText(getContext(), "删除失败", Toast.LENGTH_SHORT).show();

				}

				isDelete = true;

				break;

			case 4:

				updateList(true);

				break;

			}

		};
	};

	private void updateList(boolean b) {
		if (b) {
			adapter.notifyDataSetChanged();
			listview.layout(rextR.left, rextR.top, rextR.right, rextR.bottom - m_nLayoutBottomOffset);
			m_nLayoutBottomOffset = -m_nLayoutBottomOffset;
		} else {
			adapter2.notifyDataSetChanged();
			listview2.layout(rextR2.left, rextR2.top, rextR2.right, rextR2.bottom - m_nLayoutBottomOffset);
			m_nLayoutBottomOffset2 = -m_nLayoutBottomOffset;
		}
	}

	private void add() {
		name = etname.getText().toString().trim();
		ci = etCardID.getText().toString().trim();
		ui = etUserID.getText().toString().trim();
		ps = etPW.getText().toString().trim();
		time = etTime.getText().toString().trim();

		if (name.equals("") || ci.equals("") || ui.equals("") || ps.equals("") || time.equals("")) {

			Toast.makeText(mActivity, "输入完整", Toast.LENGTH_LONG).show();

		} else {

			if (isAdd) {
				isAdd = false;
				String data8 = NiuberManager.addUser(ci, ui, ps, time);
				sendData(data8);

			}
		}
	}

	private void getVip() {
		sendData(data4);
		setNoOnclickBtn(true);

		if (!isRun) {
			isRun = true;
			MGridActivity.xianChengChi.execute(new Runnable() {

				@Override
				public void run() {

					while (true) {

						if (isAlive) {

							isAlive = false;
							String data7 = NiuberManager.getHisInfo();
							sendData(data7);

						}

						try {
							Thread.sleep(3000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}

					}

				}
			});
		}
	}

	private void openDoor() {
		String data3 = NiuberManager.openDoor("");
		sendData(data3);
	}

	private void getUserData() {
		
		sql.setListValues(list,mydoorU);
		handler.sendEmptyMessage(4);
	}

	private void deleteAllUser() {

		if (isAllDelete) {
			isAllDelete = false;
			String data7 = NiuberManager.deleteAllUser();
			sendData(data7);
		}
	}

	private void open() {

		if (devicesPath.equals("") || portsPath.equals("")) {
			Toast.makeText(getContext(), "选择出错", Toast.LENGTH_SHORT).show();
			return;
		}

		File device = new File(devicesPath);
		int baurate = Integer.parseInt(portsPath);

		try {
			mSerialPort = new SerialPort(device, baurate, 0);
			if (mSerialPort != null) {
				mOutputStream = mSerialPort.getOutputStream();

				btnVip.setEnabled(true);
				Toast.makeText(mActivity, "成功", Toast.LENGTH_LONG).show();
			} else {
				Toast.makeText(mActivity, "失败", Toast.LENGTH_LONG).show();
			}

		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private synchronized void sendData(String data) {

		Log.e("线程", Thread.currentThread().getName());

		if (mOutputStream != null) {

			byte[] bytes = ByteUtil.hexStr2bytes(data);
			try {
				mOutputStream.write(bytes);
			} catch (IOException e) {

				e.printStackTrace();
			}
		}

		try {

			Thread.sleep(500);

			mInputStream = new BufferedInputStream(mSerialPort.getInputStream());
			byte[] received = new byte[1024];
			int size;

			int available = mInputStream.available();
			if (available > 0) {
				size = mInputStream.read(received);
				if (size > 0) {
					onDataReceive(received, size);
				}
			}
		} catch (IOException e) {
			Log.e("读取数据失败", e.toString());
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void setView(Button btn_1, Button btn_2, Button btn_3) {
		initBtn(btn_1, btn_1.getText().toString(), "#159FBB", "#FDFDFF", 20, (int) btn_1.getTag());
		initBtn(btn_2, btn_2.getText().toString(), "#00A7C8", "#FDFDFF", 20, (int) btn_2.getTag());
		initBtn(btn_3, btn_3.getText().toString(), "#00A7C8", "#FDFDFF", 20, (int) btn_3.getTag());
	}

	private void hindoneView(boolean boo) {
		if (boo) {
			btnAdd.setVisibility(View.GONE);
			tvname.setVisibility(View.GONE);
			tvCardID.setVisibility(View.GONE);
			tvUserID.setVisibility(View.GONE);
			tvPW.setVisibility(View.GONE);
			tvTime.setVisibility(View.GONE);
			etname.setVisibility(View.GONE);
			etCardID.setVisibility(View.GONE);
			etUserID.setVisibility(View.GONE);
			etPW.setVisibility(View.GONE);
			etTime.setVisibility(View.GONE);
			listview.setVisibility(View.GONE);
			btngetUser.setVisibility(View.GONE);
			btndeleteUser.setVisibility(View.GONE);

		} else {

			btnAdd.setVisibility(View.VISIBLE);
			tvname.setVisibility(View.VISIBLE);
			tvCardID.setVisibility(View.VISIBLE);
			tvUserID.setVisibility(View.VISIBLE);
			tvPW.setVisibility(View.VISIBLE);
			tvTime.setVisibility(View.VISIBLE);
			etname.setVisibility(View.VISIBLE);
			etCardID.setVisibility(View.VISIBLE);
			etUserID.setVisibility(View.VISIBLE);
			etPW.setVisibility(View.VISIBLE);
			etTime.setVisibility(View.VISIBLE);
			listview.setVisibility(View.VISIBLE);
			btngetUser.setVisibility(View.VISIBLE);
			btndeleteUser.setVisibility(View.VISIBLE);
		}
	}

	private void hindtheView(boolean boo) {
		if (boo) {
			tvPort.setVisibility(View.GONE);
			tvBt.setVisibility(View.GONE);
			tvSp1.setVisibility(View.GONE);
			tvSp2.setVisibility(View.GONE);
			btnOpen.setVisibility(View.GONE);
			btnVip.setVisibility(View.GONE);
			btnopenDoor.setVisibility(View.GONE);
		} else {

			tvPort.setVisibility(View.VISIBLE);
			tvBt.setVisibility(View.VISIBLE);
			tvSp1.setVisibility(View.VISIBLE);
			tvSp2.setVisibility(View.VISIBLE);
			btnOpen.setVisibility(View.VISIBLE);
			btnVip.setVisibility(View.VISIBLE);
			btnopenDoor.setVisibility(View.VISIBLE);
		}
	}

	private void setNoOnclickBtn(boolean click) {

		btnAdd.setEnabled(click);
		btnopenDoor.setEnabled(click);
		btnVip.setEnabled(click);
		btngetUser.setEnabled(click);
		btndeleteUser.setEnabled(click);

	}

	private void hindtwoView(boolean boo) {
		if (boo) {

			listview2.setVisibility(View.GONE);
		} else {

			listview2.setVisibility(View.VISIBLE);
		}
	}

	private void deleteUser(Map<String, Object> map) {
		if (isDelete) {

			isDelete = false;

			String str = (String) map.get("text");
			String[] s = str.split(":");
			currCID = s[s.length - 1];

			String data = NiuberManager.deleteUserCardId(currCID);
			sendData(data);
		}
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

		Builder builder = new Builder(mActivity);
		builder.setTitle("提示");
		builder.setMessage("是否删除该用户");
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

				// list.remove(position);
				deleteUser(list.get(position));

			}
		});

		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub

			}

		});

		builder.create().show();

		return true;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// TODO Auto-generated method stub

	}

	@Override
	public void run() {

	}

	/**
	 * 处理获取到的数据
	 *
	 * @param received
	 * @param size
	 */
	private void onDataReceive(byte[] received, int size) {
		// TODO: 2018/3/22 解决粘包、分包等

		String hexStr = ByteUtil.bytestoChatr(received, 0, size);

		Log.e("text", hexStr);

		if (isAdd == false) {
			if (hexStr.length() != 20) {

				handler.sendEmptyMessage(0);

				return;
			}

			parseUser(hexStr);
		} 
		if (isAllDelete == false) {

			if (hexStr.length() != 20) {

				handler.sendEmptyMessage(2);

				return;
			}

			parseDeleteUser(hexStr);
		} 
		if (isDelete == false) {

			if (hexStr.length() != 20) {

				handler.sendEmptyMessage(3);

				return;
			}

			parseDelete(hexStr);
		} 
		if (isAlive == false) {
			hexStr = hexStr.substring(14, hexStr.length() - 6);
			if (hexStr.length() != 32) {

				isAlive = true;
				return;
			}

			parse(hexStr);
		}

		// Log.e("text", hexStr);

	}

	private void parseDelete(String str) {
		String RTN = str.substring(8, 10);
		long l = ByteUtil.hexStr2decimal(RTN);
		Message msg = new Message();
		msg.what = 3;
		if (l == 0) {
			msg.obj = true;
		} else {
			msg.obj = false;
		}

		handler.sendMessage(msg);
	}

	private void parseDeleteUser(String str) {
		String RTN = str.substring(8, 10);
		long l = ByteUtil.hexStr2decimal(RTN);
		Message msg = new Message();
		msg.what = 2;
		if (l == 0) {
			msg.obj = true;
		} else {
			msg.obj = false;
		}

		handler.sendMessage(msg);
	}

	private void parseUser(String str) {
		String RTN = str.substring(8, 10);
		long l = ByteUtil.hexStr2decimal(RTN);
		Message msg = new Message();
		msg.what = 0;
		if (l == 0) {
			msg.obj = true;
		} else {
			msg.obj = false;
		}

		handler.sendMessage(msg);
	}

	private void parse(String str) {
		String CargID = str.substring(4, 14);
		String time = str.substring(14, 28);
		String STATUS = str.substring(28, 30);
		String REMARK = str.substring(30, 32);

		long l = ByteUtil.hexStr2decimal(REMARK);
		String event = DoorConfig.get((int) l, 0, CargID, mydoorU);

		String t = time.substring(0, 4) + "-" + time.substring(4, 6) + "-" + time.substring(6, 8) + " "
				+ time.substring(8, 10) + ":" + time.substring(10, 12) + ":" + time.substring(12, 14);

		MyDoorEvent myevent = new MyDoorEvent(CargID, t, event);
		sql.addEventValue(myevent);

		Map<String, Object> hh = new HashMap<String, Object>();
		hh.put("text", "事件ID:" + CargID + " 时间:" + t + " " + event);
		list2.add(hh);

		handler.sendEmptyMessage(1);

		Log.e("hahaha", CargID + "  " + time + "  " + STATUS + " " + REMARK);

	}

}
