package com.sg.uis.LsyNewView;


import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;

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
import android.widget.TextView;

import com.mgrid.data.DataGetter;
import com.mgrid.data.EquipmentDataModel.Event;
import com.mgrid.main.MGridActivity;
import com.mgrid.main.MainWindow;
import com.sg.common.CFGTLS;
import com.sg.common.IObject;

/** 分类告警数量统计 */
@SuppressLint({ "ShowToast", "InflateParams", "RtlHardcoded",
		"ClickableViewAccessibility" })
public class AlarmCount extends TextView implements IObject {


	public AlarmCount(Context context) {
		super(context);

		m_oPaint = new Paint();
		m_rBBox = new Rect();
      
	}

	
	@SuppressLint("DrawAllocation")
	protected void onDraw(Canvas canvas) {
		if (m_rRenderWindow == null)
			return;
		if (m_rRenderWindow.isLayoutVisible(getBBox()) == false)
			return;
		Paint p=new Paint();
	//	p.setColor(Color.parseColor("#64A9DB"));
		p.setAntiAlias(true);//抗锯齿
		p.setTextSize(10);
		p.setStyle(Paint.Style.FILL);
//		float i=x/2;
//		float m=y/2;
		
		if(grade.equals("1"))
		{
			p.setColor(Color.parseColor("#65AADC"));
		}else if(grade.equals("2")){
			p.setColor(Color.parseColor("#F7CC4A"));
		}else if(grade.equals("3")){
			p.setColor(Color.rgb(246, 90, 72));
		}else{
			p.setColor(Color.RED);
		}   
		//canvas.drawCircle(i, m, m, p);
		canvas.drawRect(x/6, y/6, x*5/6, y*5/6, p);
		p.setTextSize(15);
		p.setColor(Color.WHITE);
		canvas.drawText(Text, x/2-4, y/2+4, p);		
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
		x=nWidth;
		y=nHeight;
		if (m_rRenderWindow.isLayoutVisible(m_rBBox)) {

			this.layout(nX, nY, nX + nWidth, nY + nHeight);
		
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
			// this.setBackgroundColor(m_cBackgroundColor);
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
			
		} else if ("Grade".equals(strName)) {
			grade = strValue;
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					while(true)
					{
						updateValue();
						handler.sendEmptyMessage(0);
						try {
							Thread.sleep(3000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					
				}
			}).start();
			
		}
	}

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:

				invalidate();
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

	@Override
	public void updateWidget() {

	}

	@Override
	public boolean needupdate() {

		return false;
	}

	@Override
	public boolean updateValue() {

		m_bneedupdate=false;
		Hashtable<String, Hashtable<String, Event>> listEvents = DataGetter
				.getRTEventList();

		if (listEvents == null)
			return false;
		
		int count=0;
		Iterator<Hashtable.Entry<String, Hashtable<String, Event>>> equaip_it = listEvents
				.entrySet().iterator();
		while (equaip_it.hasNext()) {
			Hashtable.Entry<String, Hashtable<String, Event>> entry = equaip_it
					.next();
			String equitid=entry.getKey();
			Iterator<Hashtable.Entry<String, Event>> it = entry.getValue()
					.entrySet().iterator();
			while (it.hasNext()) {
				Hashtable.Entry<String, Event> event_entry = it.next();
				String eventid=event_entry.getKey();
//				HashMap<Long, String> hash=MGridActivity.AlarmShieldTimer.get(equitid+"_"+eventid);
//				if(hash!=null) continue;
				Event event = event_entry.getValue();
				if (grade.equals("0")) {
					count++; 
				} else if (grade.equals("1") && event.grade == 1) {

					count++; 

				} else if (grade.equals("2") && event.grade == 2) {

					count++; 

				} else if (grade.equals("3") && event.grade == 3) {

					count++; 
				} else {
					continue;
				}
			}
		}
		
		
		Text=count+"";
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
	boolean m_bPressed = false;
	MainWindow m_rRenderWindow = null;
	String cmd_value = "";

	Paint m_oPaint = null;
	Rect m_rBBox = null;
	public static ProgressDialog dialog;

	// 记录触摸坐标，过滤滑动操作。解决滑动误操作点击问题。
	public float m_xscal = 0;
	public float m_yscal = 0;


	Intent m_oHomeIntent = null;
	public boolean m_bneedupdate = true;
	private String mExpression = "";
	private String grade = "0";
	private String Text="0";
	private int x,y;
}
