package com.sg.uis;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.mgrid.data.DataGetter;
import com.mgrid.data.EquipmentDataModel.Event;
import com.mgrid.main.MGridActivity;
import com.mgrid.main.MainWindow;
import com.mgrid.main.SoundService;
import com.mgrid.util.XmlUtils;
import com.sg.common.CFGTLS;
import com.sg.common.IObject;
import comm_service.service;

import data_model.ipc_control;

/** 控制按钮 控制告警方式 暂时有 喇叭告警 DO1告警 DO2告警 */
@SuppressLint({ "RtlHardcoded", "ClickableViewAccessibility" })
public class SgControlAlarmWay extends TextView implements IObject {

	private String dataPath = "/mgrid/data/control";

	public SgControlAlarmWay(Context context) {
		super(context);
		this.setClickable(true);
		this.setGravity(Gravity.CENTER);
		File f = new File(dataPath);
		if (!f.exists()) {
			try {
				f.mkdir();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		this.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View view, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					m_bPressed = true;
					view.invalidate();

					m_xscal = event.getX();
					m_yscal = event.getY();
					break;

				case MotionEvent.ACTION_UP:
					m_bPressed = false;
					view.invalidate();

					float xslip = Math.abs(event.getX() - m_xscal);
					float yslip = Math.abs(event.getY() - m_yscal);

					if (xslip < 3 && yslip < 3)
						onClicked();
					break;

				default:
					break;
				}
				return true;
			}
		});

		m_oPaint = new Paint();
		m_rBBox = new Rect();

		setBackgroundResource(android.R.drawable.btn_default);
		setPadding(0, 4, 0, 0);
	}

	@SuppressLint("DrawAllocation")
	protected void onDraw(Canvas canvas) {
		if (m_rRenderWindow == null)
			return;
		if (m_rRenderWindow.isLayoutVisible(getBBox()) == false)
			return;

		if (m_bPressed) {
			int nWidth = (int) (((float) (m_nWidth) / (float) MainWindow.FORM_WIDTH) * (m_rRenderWindow.VIEW_RIGHT - m_rRenderWindow.VIEW_LEFT));
			int nHeight = (int) (((float) (m_nHeight) / (float) MainWindow.FORM_HEIGHT) * (m_rRenderWindow.VIEW_BOTTOM - m_rRenderWindow.VIEW_TOP));

			m_oPaint.setColor(0x500000F0);
			m_oPaint.setStyle(Paint.Style.FILL);
			canvas.drawRect(0, 0, nWidth, nHeight, m_oPaint);
		}
		super.onDraw(canvas);
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
			layout(nX, nY, nX + nWidth, nY + nHeight);
		}
	}

	@Override
	public void addToRenderWindow(MainWindow rWin) {
		m_rRenderWindow = rWin;
		rWin.addView(this);
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
			this.setBackgroundColor(m_cBackgroundColor);
		} else if ("Content".equals(strName)) {
			m_strContent = strValue;
			this.setText(selectWay);
		} else if ("FontFamily".equals(strName))
			m_strFontFamily = strValue;
		else if ("FontSize".equals(strName)) {
			float fWinScale = (float) MainWindow.SCREEN_WIDTH
					/ (float) MainWindow.FORM_WIDTH;
			m_fFontSize = Float.parseFloat(strValue) * fWinScale;
			this.setTextSize(Float.parseFloat(strValue));
		} else if ("IsBold".equals(strName))
			m_bIsBold = Boolean.parseBoolean(strValue);
		else if ("FontColor".equals(strName)) {
			m_cFontColor = Color.parseColor(strValue);
			this.setTextColor(m_cFontColor);
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
			parseCmd();
			initView();

		}
	}

	private void initView() {

		MGridActivity.xianChengChi.execute(new Runnable() {

			@Override
			public void run() {
				wayFile = new File(dataPath + "/" + equitId + "-" + eventId
						+ ".way");
				if (!wayFile.exists())
					return;
				try {
					BufferedReader br = new BufferedReader(
							new InputStreamReader(new FileInputStream(wayFile),
									"gb2312"));
					String s = "";
					while ((s = br.readLine()) != null) {
						String[] arg = s.split("=");
						selectCount = Integer.parseInt(arg[0]);
						selectWay = arg[1];
					}
					br.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
				handler.sendEmptyMessage(0);
				System.out.println("::"+selectCount);

			}
		});
	}

	private void parseCmd() {
		if (mExpression == null || mExpression.equals(""))
			return;

        String	mExpression1 = mExpression.replace("Binding{[Value[", "");
		mExpression1 = mExpression1.replace("]]}", "");
		String[] s = mExpression1.split("-");
		if(s.length<=1)
		{
			String[] s0 = s[0].split(":");
			equitId = s0[1];
			tempId="0";
			eventId="0";
		}
		else
		{
			String[] s0 = s[0].split(":");
			equitId = s0[1];
			String[] s1 = s[1].split(":");
			tempId = s1[1];
			String[] s2 = s[2].split(":");
			eventId = s2[1];
			System.out.println(equitId+":::"+eventId);
		}

	}

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				setText(selectWay);
                if(selectCount!=0) isRuning=true;
				break;

			case 1:
				break;
			}
		};
	};

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

	private void onClicked() {
		// 点击事件

		showAlarmWay();

	}

	private void showAlarmWay() {
		AlertDialog.Builder builder = new Builder(getContext());
		builder.setTitle("方式");
		builder.setSingleChoiceItems(wayList, 0,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						String s = wayList[which];
						selectWay = s;
						selectCount = which;
						if (!selectWay.equals("关闭")) {

							isRuning = true;

							if (selectWay.equals("DO1")
									|| selectWay.equals("DO2")) {
								ipc_control ipc = new ipc_control();
								ipc.equipid = Integer.parseInt(equitId);
								XmlUtils xml = XmlUtils.getXml();
								NodeList list = xml.getCommandlist();
								if (list != null) {
									for (int i = 0; i < list.getLength(); i++) {
										Element e = (Element) list.item(i);
										String name = e
												.getAttribute("CommandName");
										if (name.equals(selectWay)) {
											ctrlid = e
													.getAttribute("CommandId");
											ipc.ctrlid = Integer
													.parseInt(ctrlid);
											break;
										}
									}
								}
								ipc.valuetype = 1;
								ipc.value = "1";
								System.out.println(ipc.equipid + " "
										+ ipc.ctrlid + " " + ipc.valuetype
										+ " " + ipc.value);
								lstCtrl.clear();
								lstCtrl.add(ipc);

							}
						} else {
							isRuning = false;
							pauseSound();
						}
						setText(selectWay);
						writeWayFile();

					}
				});
		builder.show();

	}

	private void writeWayFile() {

		MGridActivity.xianChengChi.execute(new Runnable() {

			@Override
			public void run() {

				if (wayFile == null)
					wayFile = new File(dataPath + "/" + equitId + "-" + eventId
							+ ".way");
				if (!wayFile.exists()) {
					try {
						wayFile.createNewFile();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				try {
					BufferedWriter bw = new BufferedWriter(
							new OutputStreamWriter(
									new FileOutputStream(wayFile), "gb2312"));
					bw.write(selectCount + "=" + selectWay);
					bw.flush();
					bw.close();
				} catch (Exception e) {

					e.printStackTrace();
				}
			}
		});
	}

	@Override
	public void updateWidget() {
	}

	@Override
	public boolean updateValue() {

		switch (selectCount) {
		case 0:

			// pauseSound(); //哈哈 跑不进来 自己把逻辑写死了
			break;
		case 1:

			System.out.println("我开始播放了");
			startSound();

			break;
		case 2:

			if (0 != service
					.send_control_cmd(service.IP, service.PORT, lstCtrl)) {
				System.out.println("发送失败");
			} else {
				System.out.println("发送成功");
			}
			break;
		case 3:

			if (0 != service
					.send_control_cmd(service.IP, service.PORT, lstCtrl)) {
				System.out.println("发送失败");
			} else {
				System.out.println("发送成功");
			}
			break;
		}

		return false;
	}

	private void startSound() {
		Intent intent = new Intent(m_rRenderWindow.m_oMgridActivity,
				SoundService.class);
		intent.putExtra("playing", true);
		m_rRenderWindow.m_oMgridActivity.startService(intent);
	}

	private void pauseSound() {
		Intent intent = new Intent(m_rRenderWindow.m_oMgridActivity,
				SoundService.class);
		intent.putExtra("playing", false);
		m_rRenderWindow.m_oMgridActivity.startService(intent);
	}


	@Override
	public boolean needupdate() {

		
		if (!isRuning)
			return false;
	//	HashMap<Long, String> hash=MGridActivity.AlarmShieldTimer.get(equitId+"_"+eventId);    //腾讯版才有这个
	//	if(hash!=null)	return false;

		
		Hashtable<String, Hashtable<String, Event>> new_eventss = DataGetter
				.getRTEventList();
		
		if (new_eventss == null||new_eventss.size()==0)
			return false;

	
		
		if (old_eventss == null) { // 第一次判断时 old_eventss为空 如果new_eventss包含这个告警
									// 就报警.
			System.out.println("old_eventss为空");
			old_eventss = new_eventss;
			if(equitId.equals("0"))
			{
				if(new_eventss!=null)
				{
					return true;
				}
			}
			else
			{
				if (new_eventss.containsKey(equitId)) {
					System.out.println("new_eventss包含设备ID");
					Hashtable<String, Event> new_events = new_eventss.get(equitId);

					if (new_events.containsKey(eventId)) {
						System.out.println("new_eventss包含告警ID");
						return true;
					}
				} 
				System.out.println("new_eventss没有设备ID或者告警ID");			
			}
			return false;
		}
		/**
		 * 之后判断，先判断new_events有没有这个告警 ，如果有(再判断old_eventss里有没有这个告警,如果有，说明不需要告警
		 * ，如果没有 ，就需要告警).如果没有（不告警）；
		 */
		if(equitId.equals("0"))
		{
			if(new_eventss.size()>old_eventss.size())
			{
				old_eventss = new_eventss;
				return true;
			}
			
		}else
		{
			if (new_eventss.containsKey(equitId)) {
				// System.out.println("之后;new_eventss包含设备ID");
				Hashtable<String, Event> new_events = new_eventss.get(equitId);
				if (new_events.containsKey(eventId)) {
					// System.out.println("之后;new_eventss包含告警ID");
					if (old_eventss.containsKey(equitId)) {
						// System.out.println("之后;old_eventss包含设备ID");
						Hashtable<String, Event> old_events = old_eventss
								.get(equitId);
						if (old_events.containsKey(eventId)) {
							// System.out.println("之后;old_eventss包含告警ID");
							old_events = new_events;
							return false;
						}
					}
					// System.out.println("之后;old_eventss不包含设备ID或告警ID");
					old_eventss = new_eventss;
					return true;
				}
			}
			// System.out.println("之后;new_eventss不包含设备ID或告警ID");
		}
		
		old_eventss = new_eventss;
		return false;
	
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
	String m_strClickEvent = "";
	String m_strUrl = "";
	String m_strCmdExpression = "";
	String mExpression = "";
	String m_strHorizontalContentAlignment = "Center";
	String m_strVerticalContentAlignment = "Center";
	boolean m_bPressed = false;
	MainWindow m_rRenderWindow = null;

	Paint m_oPaint = null;
	Rect m_rBBox = null;

	// 记录触摸坐标，过滤滑动操作。解决滑动误操作点击问题。
	public float m_xscal = 0;
	public float m_yscal = 0;
	private boolean isRuning = false;
	private String selectWay = "关闭";
	private String[] wayList = { "关闭", "喇叭", "DO1", "DO2" };

	private List<ipc_control> lstCtrl = new ArrayList<ipc_control>();
	// private List<ipc_control> lstCtrl_redu = new ArrayList<ipc_control>();
	
	private Hashtable<String, Hashtable<String, Event>> old_eventss = null;
	private int selectCount = -1;
	private String ctrlid;
	private String equitId, tempId, eventId;
	private File wayFile;

}
