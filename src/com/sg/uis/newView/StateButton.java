package com.sg.uis.newView;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.mgrid.main.MGridActivity;
import com.mgrid.main.MainWindow;
import com.mgrid.util.ExpressionUtils;
import com.sg.common.CFGTLS;
import com.sg.common.IObject;
import com.sg.common.SgRealTimeData;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import comm_service.service;
import data_model.ipc_control;

public class StateButton extends Button implements IObject, OnClickListener ,OnTouchListener{

	private Rect m_rBBox;

	private int m_nPosX = 152;
	private int m_nPosY = 287;
	private int m_nWidth = 75;
	private int m_nHeight = 23;
	private int m_nZIndex = 1;
	private int m_cFontColor;
	private int openValue = -1, closeValue = -1;
	private float m_fFontSize = 16;

	private boolean m_bPressed=false;
	private boolean isFrist = true;
	public boolean m_bneedupdate = true;

	private Timer timer=null;
	private Paint m_oPaint=null;
	
	private String m_strID = "";
	private String m_strType = "";
	private String m_strContent = "";
	private String m_strExpression = "";
	private String m_strOpenExpression = "";
	private String m_strCloseExpression = "";
	private String m_strHorizontalContentAlignment = "";
	private String m_strVerticalContentAlignment = "";

	private MainWindow m_rRenderWindow = null;
	private Drawable drawableOpen, drawableClose;

	//private List<String> cmdList = new ArrayList<>();
	private List<String> cmdYKPList1 = new ArrayList<>();
	private List<String> cmdYKPList2 = new ArrayList<>();
	private List<ipc_control> c_control = new ArrayList<ipc_control>();
	private List<ipc_control> o_control = new ArrayList<ipc_control>();
	
	
	private Handler handler=new Handler() {
		
		public void handleMessage(Message msg) {
			
			switch (msg.what) {
			case 0:
				
				setEnabled(true);
				
				break;

			default:
				break;
			}
			
		}
		
	};
	
	

	public StateButton(Context context) {

		super(context);

		init();

	}

	@SuppressLint("ClickableViewAccessibility")
	private void init() {

		m_rBBox = new Rect();
		m_oPaint=new Paint();
		
		setClickable(true);
		setGravity(Gravity.CENTER);
		setOnClickListener(this);
		setOnTouchListener(this);
		setBackgroundResource(android.R.drawable.btn_default);
		setPadding(0, 0, 0, 0);
		
		
		timer=new Timer();
		
	}

	

	
	
	
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
		if (m_rRenderWindow.isLayoutVisible(m_rBBox)) {
			layout(nX, nY, nX + nWidth, nY + nHeight);
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
			m_strContent = strValue;
			this.setText(m_strContent);
		} else if ("FontSize".equals(strName)) {
			float fWinScale = (float) MainWindow.SCREEN_WIDTH / (float) MainWindow.FORM_WIDTH;
			m_fFontSize = Float.parseFloat(strValue) * fWinScale;
			this.setTextSize(m_fFontSize);
		} else if ("FontColor".equals(strName)) {
			m_cFontColor = Color.parseColor(strValue);
			this.setTextColor(m_cFontColor);
		} else if ("CmdExpressionOpen".equals(strName)) {
			m_strOpenExpression = strValue;
			
			cmdYKPList1=parseExCmd(m_strOpenExpression);
			
		} else if ("CmdExpressionClose".equals(strName)) {
			m_strCloseExpression = strValue;
			
			cmdYKPList2=parseExCmd(m_strCloseExpression);
			
		} else if ("HorizontalContentAlignment".equals(strName)) {
			m_strHorizontalContentAlignment = strValue;
		} else if ("VerticalContentAlignment".equals(strName)) {
			m_strVerticalContentAlignment = strValue;
		} else if ("Expression".equals(strName)) {
			m_strExpression = strValue;
			
		} else if ("ImageOpen".equals(strName)) {

			String path = Environment.getExternalStorageDirectory().getPath() + strResFolder + strValue;
			drawableOpen = new BitmapDrawable(getResources(), path);

		} else if ("ImageClose".equals(strName)) {

			String path = Environment.getExternalStorageDirectory().getPath() + strResFolder + strValue;
			drawableClose = new BitmapDrawable(getResources(), path);

		} else if ("OpenValue".equals(strName)) {

			openValue = Integer.parseInt(strValue);

		} else if ("CloseValue".equals(strName)) {

			closeValue = Integer.parseInt(strValue);
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
		rWin.addView(this);

	}

	@Override
	public void removeFromRenderWindow(MainWindow rWin) {

		rWin.removeView(this);

	}

	@Override
	public void updateWidget() {

		if (!isFrist) {

			this.setBackground(drawableOpen);

		} else {

			this.setBackground(drawableClose);

		}

	}

	@Override
	public boolean updateValue() {
		m_bneedupdate = false;

		if (drawableOpen == null || drawableClose == null || m_strCloseExpression.equals("")
				|| m_strOpenExpression.equals("") || m_strExpression.equals("") || openValue == -1
				|| closeValue == -1) {
			return false;
		}

		SgRealTimeData oRealTimeData = m_rRenderWindow.m_oShareObject.m_mapRealTimeDatas.get(this.getUniqueID());
		if (oRealTimeData != null) {
			String strValue = oRealTimeData.strData;

			try {

				int value = (int) Float.parseFloat(strValue);
				

				if (openValue == value) {

					isFrist = false;

				} else if (closeValue == value) {

					isFrist = true;

				} else {

					Log.e(getUniqueID(), "≥ˆœ÷“Ï≥£");
					return false;
				}

			} catch (Exception e) {

				Log.e(getUniqueID(), e.toString());
				return false;
			}

			return true;

		}

		return false;
	}

	@Override
	public boolean needupdate() {

		return m_bneedupdate;
	}

	@Override
	public void needupdate(boolean bNeedUpdate) {

		this.m_bneedupdate = bNeedUpdate;

	}

	@Override
	public String getBindingExpression() {

		return m_strExpression;
	}

	@Override
	public View getView() {

		return this;
	}

	@Override
	public int getZIndex() {

		return m_nZIndex;
	}

	@Override
	public void onClick(final View v) {

		
		v.setEnabled(false);	
		
		timer.schedule(new TimerTask() {
			
			@Override
			public void run() {
				
				handler.sendEmptyMessage(0);
				
			}
		}, 3000);
		
		MGridActivity.xianChengChi.execute(new Runnable() {

			@Override
			public void run() {

				
				
				
				sendCmd();

			}
		});

	}

	private void sendCmd() {
		if (!m_strCloseExpression.equals("") && !m_strOpenExpression.equals("")&&cmdYKPList1!=null&&cmdYKPList2!=null) {

			if (isFrist) {

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

				for (ipc_control i : o_control) {
					
					Log.e(isFrist+"", i.equipid+""+i.ctrlid+""+i.valuetype+""+i.value);
				}
                
				service.send_control_cmd(service.IP, service.PORT, o_control);

			} else {

				if (c_control.size() == 0) {
					for (String cmd : cmdYKPList2) {

						ipc_control ipc = new ipc_control();
						String[] cmds = cmd.split("-");
						ipc.equipid = Integer.parseInt(cmds[0]);
						ipc.ctrlid = Integer.parseInt(cmds[2]);
						ipc.valuetype = Integer.parseInt(cmds[3]);
						ipc.value =cmds[4];
						c_control.add(ipc);
					}
				}

                for (ipc_control i : c_control) {
					
					Log.e(isFrist+"", i.equipid+""+i.ctrlid+""+i.valuetype+""+i.value);
				}
				

				service.send_control_cmd(service.IP, service.PORT, c_control);

			}
		}
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		
		
		
		switch (event.getAction()) {
		
		case MotionEvent.ACTION_DOWN:
			
			m_bPressed = true;
			invalidate();

			
			break;

		case MotionEvent.ACTION_UP:
			
			m_bPressed = false;
			invalidate();

		

			
			break;

		default:
			break;
		}
		
		return false;
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		if (m_bPressed) {
			int nWidth = (int) (((float) (m_nWidth) / (float) MainWindow.FORM_WIDTH)
					* (m_rRenderWindow.VIEW_RIGHT - m_rRenderWindow.VIEW_LEFT));
			int nHeight = (int) (((float) (m_nHeight) / (float) MainWindow.FORM_HEIGHT)
					* (m_rRenderWindow.VIEW_BOTTOM - m_rRenderWindow.VIEW_TOP));

			m_oPaint.setColor(0x500000F0);
			m_oPaint.setStyle(Paint.Style.FILL);
			canvas.drawRect(0, 0, nWidth, nHeight, m_oPaint);
		}
	}

}
