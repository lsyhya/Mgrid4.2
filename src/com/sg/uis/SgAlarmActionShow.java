package com.sg.uis;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.mgrid.main.MGridActivity;
import com.mgrid.main.MainWindow;
import com.sg.common.CFGTLS;
import com.sg.common.IObject;

/** 标签 */
@SuppressLint({ "RtlHardcoded", "HandlerLeak" })
public class SgAlarmActionShow extends TextView implements IObject {

	public SgAlarmActionShow(Context context) {
		super(context);
		this.setClickable(false);
		this.setBackgroundColor(0x00000000);
		m_rBBox = new Rect();
	}

	@Override
	public void doLayout(boolean bool, int l, int t, int r, int b) {
		if (m_rRenderWindow == null)
			return;
		int nX = l
				+ (int) (((float) m_nPosX / (float) MainWindow.FORM_WIDTH) * (r - l));
		int nY = t
				+ (int) (((float) m_nPosY / (float) MainWindow.FORM_HEIGHT) * (b - t));
		int nWidth = (int) (((float) m_nWidth / (float) MainWindow.FORM_WIDTH) * (r - l));
		int nHeight = (int) (((float) m_nHeight / (float) MainWindow.FORM_HEIGHT) * (b - t));

		m_rBBox.left = nX;
		m_rBBox.top = nY;
		m_rBBox.right = nX + nWidth;
		m_rBBox.bottom = nY + nHeight;
		if (m_rRenderWindow.isLayoutVisible(m_rBBox)) {
			layout(nX, nY, nX + nWidth, nY + nHeight);
		}
	}

	public void onDraw(Canvas canvas) {
		if (m_rRenderWindow == null)
			return;
		if (m_rRenderWindow.isLayoutVisible(getBBox()) == false)
			return;

		super.onDraw(canvas);
	}

	public View getView() {
		return this;
	}

	public int getZIndex() {
		return m_nZIndex;
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
		} else if ("RotateAngle".equals(strName)) {
			m_fRotateAngle = Float.parseFloat(strValue);
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
			m_cStartFillColor = m_cFontColor;
			this.setTextColor(m_cFontColor);
		} else if ("HorizontalContentAlignment".equals(strName))
			m_strHorizontalContentAlignment = strValue;
		else if ("VerticalContentAlignment".equals(strName))
			m_strVerticalContentAlignment = strValue;
		else if ("Expression".equals(strName))
			m_strExpression = strValue;
		else if ("Label".equals(strName)) {
			label = strValue; // 字体颜色变化表达式
			new Thread(new Runnable() {

				@Override
				public void run() {
					while (true) {
						try {
							Thread.sleep(2000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						updateValue();
					}

				}
			}).start();
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
			double padSize = CFGTLS.getPadHeight(m_nHeight,
					MainWindow.FORM_HEIGHT, getTextSize());
			setPadding(0, (int) padSize, 0, 0);
		} else if ("Center".equals(m_strVerticalContentAlignment)) {
			nFlag |= Gravity.CENTER_VERTICAL;
			double padSize = CFGTLS.getPadHeight(m_nHeight,
					MainWindow.FORM_HEIGHT, getTextSize()) / 2;
			setPadding(0, (int) padSize, 0, (int) padSize);
		}

		setGravity(nFlag);
	}

	public String getBindingExpression() {
		return m_strExpression;
	}

	public void updateWidget() {
		this.setTextColor(m_cFontColor);
		this.setText(m_strContent);

		this.invalidate();

	}

	
	@SuppressWarnings("unchecked")
	@Override
	public boolean updateValue() {

		
		 File f_data = new File(event_path_data + "/" + label + ".data");
		 if(!f_data.exists())
		 {
				handler.sendEmptyMessage(0);
				return true;
		 }
		 
		
	//	synchronized (MGridActivity.AlarmShow) {
			
			if (MGridActivity.AlarmShow == null)
				return false;
			show=(HashMap<String, ArrayList<String>>) MGridActivity.AlarmShow.clone();
			alist = show.get(label);
			
			if (alist == null) {
	              
				handler.sendEmptyMessage(6);
				
			} else if (alist.size() == 0) {
				
				handler.sendEmptyMessage(1);
				
			} else if(alist.size() > 0){
				text=alist.get(alist.size()-1);
				handler.sendEmptyMessage(7);
			}

			return true;
		}
		
	//}

	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			String s = "";
			switch (msg.what) {
			case 0:

				setText("未配置内容");
				break;
			case 1:
				setText("市电正常");
				break;
			case 2:
				setText("市电正常");
				break;
			case 3:

				if (list.size() >= 1) {
					s = list.get(0).split("&")[1];
					setText(s);
				} else {
					setText("等待时间过后执行下电操作");
				}
				
				break;

			case 4:

				if (list.size() >= 2) {
					s = list.get(1).split("&")[1];
					setText(s);
				} else {
					setText("下电中");
				}

				break;
			case 5:
				if (list.size() >= 3) {
					s = list.get(2).split("&")[1];
					setText(s);
				} else {
					setText("下电完成");
				}
				break;
			case 6:
				
		           setText("加载中");
				
				break;
				
			case 7:
				
			
			    setText(text);	
			    break;


			}

		};
	};

	@Override
	public boolean needupdate() {
		return m_bneedupdate;
	}

	@Override
	public void needupdate(boolean bNeedUpdate) {
		m_bneedupdate = bNeedUpdate;
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

	public Rect getBBox() {
		return m_rBBox;
	}

	// params:
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
	float m_fFontSize = 12.0f;
	boolean m_bIsBold = false;
	int m_cFontColor = 0xFF008000;
	int m_cStartFillColor = 0x00000000;
	String m_strHorizontalContentAlignment = "Center";
	String m_strVerticalContentAlignment = "Center";
	String m_strExpression = "Binding{[Value[Equip:114-Temp:173-Signal:1]]}";
	String m_strColorExpression = ">20[#FF009090]>30[#FF0000FF]>50[#FFFF0000]>60[#FFFFFF00]";

	MainWindow m_rRenderWindow = null;
	String m_strSignalValue = "";

	Rect m_rBBox = null;

	public boolean m_bneedupdate = true;
	public boolean m_bValueupdate = true;
//	 private String event_path_log = "/mgrid/data/Command";
//	private String event_path_log = Environment.getExternalStorageDirectory()
//			.getPath() + "/Command";

	private String event_path_data = "/data/mgrid/sampler/SO";

	private String label = "";

	private ArrayList<String> list = new ArrayList<String>();

	private HashMap<String, ArrayList<String>> show = new HashMap<String, ArrayList<String>>();
	ArrayList<String> alist=null;
	String text="";
}
