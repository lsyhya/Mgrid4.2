package com.sg.uis.LsyNewView;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mgrid.main.MGridActivity;
import com.mgrid.main.MainWindow;
import com.sg.common.CFGTLS;
import com.sg.common.IObject;
import com.sg.common.UtIniReader;

/** 告警屏蔽延时时间 */
@SuppressLint({ "ShowToast", "InflateParams", "RtlHardcoded",
		"ClickableViewAccessibility", "UseSparseArrays" })
public class AlarmShieldTime extends TextView implements IObject {

	private EditText shieldTime = null;

	// private InputMethodManager imm = null;
	private File filePath = null;
	private File markPath = null;
	// private String path = Environment.getExternalStorageDirectory().getPath()
	// + "/ShieldTime";
	private String path = "/mgrid/data/ShieldTime";
	public String equitId, eventId;
	private BufferedWriter BWriter;
	private String OldTime, LaterTime;
	private final Timer timer = new Timer();
	// private TimerTask task=new TimerTask() {
	//
	// @Override
	// public void run() {
	// long nowTime=System.currentTimeMillis();
	// long oldTime=Long.parseLong(OldTime);
	// long latertime=Long.parseLong(LaterTime)*1000;
	// System.out.println("时间：：：："+(nowTime-oldTime));
	// if(nowTime-oldTime>=latertime)
	// {
	// Timecancel();
	// }
	// }
	// };
	private Runnable run = new Runnable() {

		@Override
		public void run() {

			long nowTime = System.currentTimeMillis();
			long oldTime = Long.parseLong(OldTime);
			long latertime = Long.parseLong(LaterTime) * 1000;
			System.out.println("时间：：：：" + (nowTime - oldTime));
			if (nowTime - oldTime >= latertime) {
				
				Timecancel(); 
               
			} else {
				
				handler.postDelayed(this, 2000);
				
			}
		}
	};

	public AlarmShieldTime(Context context) {
		super(context);

		shieldTime = new EditText(context);
		shieldTime.setBackgroundResource(android.R.drawable.edit_text);
		shieldTime.setPadding(0, 5, 0, 0);
		shieldTime.setTextSize(20);
		shieldTime.setTextColor(Color.BLACK);
		shieldTime.setGravity(Gravity.CENTER);
		shieldTime.setInputType(InputType.TYPE_CLASS_NUMBER);
		shieldTime.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				shieldTime.setFocusableInTouchMode(true);// 获取焦点
			}
		});
		shieldTime.setCursorVisible(true);

		setBackgroundResource(android.R.drawable.btn_default);

		setGravity(Gravity.CENTER);

		m_oPaint = new Paint();
		m_rBBox = new Rect();

		this.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				LaterTime = shieldTime.getText().toString();
				final String btntxt = getText().toString();
				if (LaterTime.equals("")) {
					Toast.makeText(getContext(), "不能为空", 500).show();
					return;
				}

				MGridActivity.xianChengChi.execute(new Runnable() {

					@Override
					public void run() {

						if (equitId == null || eventId == null
								|| equitId.equals("") || eventId.equals("")) {
							handler.sendEmptyMessage(1);
							return;
						}

						if (markPath == null)
							markPath = new File(path);
						if (!markPath.exists())
							markPath.mkdir();

						filePath = new File(path + "/" + equitId + "_"
								+ eventId + ".txt");

						if (btntxt.equals("屏蔽")) {

							addShield();
							handler.sendEmptyMessage(0);
						} else if (btntxt.equals("取消")) {
							Timecancel();
						}

					}
				});
			}
		});
	}

	// 取消屏蔽
	private void Timecancel() {
		synchronized (MGridActivity.AlarmShieldTimer) {
			MGridActivity.AlarmShieldTimer.remove(equitId + "_" + eventId);
		}
		filePath.delete();
		handler.removeCallbacks(run);
		handler.sendEmptyMessage(2);
	}

	// 添加屏蔽
	private void addShield() {
		try {
			if (!filePath.exists())
				filePath.createNewFile();
			BWriter = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(filePath), "GB2312"));
			long time = System.currentTimeMillis();
			OldTime = time + "";
			HashMap<Long, String> hashmap = new HashMap<Long, String>();
			hashmap.put(time, LaterTime);
			BWriter.write("[ShieldTime]");
			BWriter.newLine();
			BWriter.write("OldTime=" + time);
			BWriter.newLine();
			BWriter.write("LaterTime=" + LaterTime);
			BWriter.flush();
			BWriter.close();
			synchronized (MGridActivity.AlarmShieldTimer) {
				MGridActivity.AlarmShieldTimer.put(equitId + "_" + eventId,
						hashmap);
			}
			// timer.schedule(task, 1000, 1000);
			handler.postDelayed(run, 2000);
	
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void updateText() {
		shieldTime.setText(LaterTime);
	}

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				Toast.makeText(getContext(), "保存完毕", 1000).show();
				setText("取消");
				break;
			case 1:
				Toast.makeText(getContext(), "配置文件出错，没有获取到设备ID或者告警ID", 1000)
						.show();
				setText("");
				break;
			case 2:
				setText("屏蔽");
				shieldTime.setText("");
				break;
			case 3:
				setText("取消");
				shieldTime.setText(LaterTime);
				break;

			}

		};
	};

	@SuppressLint("DrawAllocation")
	protected void onDraw(Canvas canvas) {
		if (m_rRenderWindow == null)
			return;
		if (m_rRenderWindow.isLayoutVisible(getBBox()) == false)
			return;
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

			this.layout(nX, nY, (int) (nX + 0.49 * nWidth), nY + nHeight);
			shieldTime.layout((int) (nX + 0.51 * nWidth), nY, nX + nWidth, nY
					+ nHeight);
		}
	}

	@Override
	public void addToRenderWindow(MainWindow rWin) {
		m_rRenderWindow = rWin;
		rWin.addView(this);
		rWin.addView(shieldTime);
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
			// this.setBackgroundColor(m_cBackgroundColor);

		} else if ("Content".equals(strName)) {
			m_strContent = strValue;
			this.setText(m_strContent);

		} else if ("FontFamily".equals(strName))
			m_strFontFamily = strValue;
		else if ("FontSize".equals(strName)) {
			float fWinScale = (float) MainWindow.SCREEN_WIDTH
					/ (float) MainWindow.FORM_WIDTH;
			m_fFontSize = Float.parseFloat(strValue) * fWinScale;
			this.setTextSize(m_fFontSize);
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
			parse(mExpression);
			initView();
		}
	}

	private void initView() {
		MGridActivity.xianChengChi.execute(new Runnable() {

			@Override
			public void run() {
				filePath = new File(path + "/" + equitId + "_" + eventId
						+ ".txt");
				if (!filePath.exists())
					return;
				try {
					UtIniReader iniReader = new UtIniReader(filePath
							.getAbsolutePath());
					OldTime = iniReader.getValue("ShieldTime", "OldTime");
					LaterTime = iniReader.getValue("ShieldTime", "LaterTime");
					System.out.println(OldTime + ";;;;" + LaterTime);
					HashMap<Long, String> hashmap = new HashMap<Long, String>();
					hashmap.put(Long.parseLong(OldTime), LaterTime);
					synchronized (MGridActivity.AlarmShieldTimer) {
						MGridActivity.AlarmShieldTimer.put(equitId + "_"
								+ eventId, hashmap);
					}
					handler.postDelayed(run, 2000);
				} catch (IOException e) {

					e.printStackTrace();
				}
				handler.sendEmptyMessage(3);

			}
		});

	}

	private void parse(String mExpression) {
		if (mExpression == null || mExpression.equals(""))
			return;

		mExpression = mExpression.replace("Binding{[Value[", "");
		mExpression = mExpression.replace("]]}", "");
		String[] s = mExpression.split("-");
		String[] s0 = s[0].split(":");
		equitId = s0[1];
		// String[] s1 = s[1].split(":");
		// tempId = s1[1];
		String[] s2 = s[2].split(":");
		eventId = s2[1];
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

		return false;
	}

	@Override
	public boolean needupdate() {

		return true;
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

}
