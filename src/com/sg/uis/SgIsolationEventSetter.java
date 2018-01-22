package com.sg.uis;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.mgrid.data.DataGetter;
import com.mgrid.main.MGridActivity;
import com.mgrid.main.MainWindow;
import com.mgrid.util.XmlUtils;
import com.sg.common.CFGTLS;
import com.sg.common.IObject;
import com.sg.common.UtExpressionParser.stBindingExpression;

import data_model.ipc_cfg_trigger_value;

public class SgIsolationEventSetter extends ToggleButton implements IObject {
	public SgIsolationEventSetter(Context context) {
		super(context);

		// 监听 按钮 触控事件
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

					if (m_xscal == event.getX() && m_yscal == event.getY())
						SgIsolationEventSetter.this.performClick();
					break;
				default:
					break;
				}
				return true;
			}
		});

		setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// 发送控制命令
				if ("".equals(m_strCmdExpression) == false) {
					// TODO: 依据当前告警状态区分设定还是切换显示状态
					synchronized (m_rRenderWindow.m_oShareObject) {
						SgIsolationEventSetter.this.setEnabled(false);
						handler.postDelayed(runnable, 5000);
						m_rRenderWindow.m_oShareObject.m_mapTriggerCommand.put(
								getUniqueID(), isChecked() ? "1" : "0");
						if (isChecked()) {
							setTextColor(Color.BLUE);

							//
						} else {
							setTextColor(Color.RED);

						}

					}
				}
			}
		});

		m_oPaint = new Paint();
		m_rBBox = new Rect();

		// setBackgroundResource(com.mgrid.main.R.drawable.sg_button_up);
		// setBackgroundResource(R.drawable.btn_default);
		// setPadding(0, 0, 0, 0);
		setTextOn(m_strTextOn);
		setTextOff(m_strTextOff);
	}

	@SuppressLint("DrawAllocation")
	protected void onDraw(Canvas canvas) {
		/*
		 * if (m_rRenderWindow == null) return; if
		 * (m_rRenderWindow.isLayoutVisible(getBBox()) == false) return;
		 * 
		 * if (m_bPressed) { int nWidth = (int) (((float)(m_nWidth) /
		 * (float)MainWindow.FORM_WIDTH) * (m_rRenderWindow.VIEW_RIGHT -
		 * m_rRenderWindow.VIEW_LEFT)); int nHeight = (int) (((float)(m_nHeight)
		 * / (float)MainWindow.FORM_HEIGHT) * (m_rRenderWindow.VIEW_BOTTOM -
		 * m_rRenderWindow.VIEW_TOP));
		 * 
		 * m_oPaint.setColor(0x50FF00F0); m_oPaint.setStyle(Paint.Style.FILL);
		 * canvas.drawRect(0,0,nWidth,nHeight, m_oPaint); }
		 */
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
			this.layout(nX, nY, nX + nWidth, nY + nHeight);
		}
	}

	@Override
	public void addToRenderWindow(MainWindow rWin) {
		m_rRenderWindow = rWin;
		// int
		// enabled=m_rRenderWindow.m_oShareObject.m_SgIsolationEventSetter.get(m_strID);
		// if(enabled==1){
		setChecked(true);
		setText(m_strTextOn);
		// }else if(enabled==0)
		// {
		// setChecked(false);
		// setText(m_strTextOff);
		// }

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
			m_nWidth = m_nWidth < 60 ? 60 : m_nWidth;
			m_nHeight = m_nHeight < 40 ? 40 : m_nHeight;
		} else if ("Alpha".equals(strName)) {
			m_fAlpha = Float.parseFloat(strValue);
		} else if ("Content".equals(strName)) {
			m_strContent = strValue;
			this.setText(m_strContent);
		} else if ("TooltipFirst".equalsIgnoreCase(strName)) {
			m_strTextOn = strValue;
			this.setTextOn(m_strTextOn);
		} else if ("TooltipSecond".equalsIgnoreCase(strName)) {
			m_strTextOff = strValue;
			this.setTextOff(m_strTextOff);
		} else if ("FontFamily".equals(strName))
			m_strFontFamily = strValue;
		else if ("IsBold".equals(strName))
			m_bIsBold = Boolean.parseBoolean(strValue);
		else if ("FontColor".equals(strName)) {
			m_cFontColor = Color.parseColor(strValue);
			this.setTextColor(m_cFontColor);
		} else if ("MaskExpression".equals(strName))
			m_strCmdExpression = strValue;
		else if ("HorizontalContentAlignment".equals(strName))
			m_strHorizontalContentAlignment = strValue;
		else if ("VerticalContentAlignment".equals(strName))
			m_strVerticalContentAlignment = strValue;
		else if ("Expression".equals(strName)) {
			m_strCmdExpression = strValue;
			new Thread(new Runnable() {

				@Override
				public void run() {
					while (true) {
					//	System.out.println(DataGetter.isLoading+"::"+MGridActivity.isLoading);
						if (!DataGetter.isLoading&&!MGridActivity.isLoading) {
							
							setEnabled();
							break;
						}
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
		return m_strCmdExpression;
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

	// 定时启用
	final Handler handler = new Handler();
	Runnable runnable = new Runnable() {
		public void run() {
			SgIsolationEventSetter.this.setEnabled(true);
		} // end of run
	};

	private Handler hand = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:

				if (MGridActivity.whatLanguage)
					setText("告警屏蔽");
				else
					setText("OFF");
				setChecked(false);
				setTextColor(Color.RED);

				break;

			case 1:

				if (MGridActivity.whatLanguage)
					setText("告警开启");
				else
					setText("ON");
				setChecked(true);
				setTextColor(Color.BLUE);
 
				break;
				
			case 2:

				String s=msg.obj.toString();
				Toast.makeText(getContext(),
						"设备Id" + s + "并没有找到,检查一下吧", 200)
						.show();

				break;
			case 3:

			
				Toast.makeText(getContext(),
						"屏蔽整个设备时发生错误", 200)
						.show();

				break;
			}

		};
	};

	public void setEnabled() {
		stBindingExpression oBindingExpression = m_rRenderWindow.m_oShareObject.m_SgIsolationEventSetter
				.get(m_strID);
		if (oBindingExpression == null)
			return;

		List<ipc_cfg_trigger_value> trigger_value_list = DataGetter
				.getTrigger_list().get(oBindingExpression.nEquipId + "");
		if (trigger_value_list == null) {
			Message mess=new Message();
			mess.obj=oBindingExpression.nEquipId;
			mess.what=2;
			hand.sendMessage(mess);
			return;
		}
		Iterator<ipc_cfg_trigger_value> trigger_value_it = trigger_value_list
				.iterator();
		int enabled = -1;
		while (trigger_value_it.hasNext()) {
			ipc_cfg_trigger_value ipc_trigger = trigger_value_it.next();
			if (oBindingExpression.nEventId == 0) {

				
				try {
					dbf = DocumentBuilderFactory.newInstance();
					db = dbf.newDocumentBuilder();
//					doc = db.parse(new File(
//							"/data/mgrid/sampler/XmlCfg/MonitorUnitVTU.xml"));
					doc = db.parse(new File("/data/mgrid/sampler/XmlCfg/"+XmlUtils.getXml().Mainpath));

				} catch (Exception e) {
					e.printStackTrace();
					hand.sendEmptyMessage(3);
				}

				list1 = doc.getElementsByTagName("CfgEquipment");
				for (int i = 0; i < list1.getLength(); i++) {
					Element element1 = (Element) list1.item(i);
					String EquipId = element1.getAttribute("EquipId");
					if (!EquipId.equals(oBindingExpression.nEquipId + ""))
						continue;
					String EventLocked = element1.getAttribute("EventLocked");
					if (EventLocked.equals("false")) {
						enabled = 1;

					} else {
						enabled = 0;
							MGridActivity.LabelList.add(oBindingExpression.nEquipId
									+ "");								
					}

				}

			} else {

			
				
				if (ipc_trigger.eventid == oBindingExpression.nEventId) {
					enabled = ipc_trigger.enabled;

				}
				
				if (enabled == 0) {
					
					XmlUtils xml = XmlUtils.getXml();
					synchronized(xml){
						String TemplateId = xml
								.getTemplateId(oBindingExpression.nEquipId + "");
						
					
						NodeList list = xml.getNodeList("EquipEvent", TemplateId);
						System.out.println("::::"+TemplateId);
						for (int i = 0; i < list.getLength(); i++) {
							Element element = (Element) list.item(i);
							String EvenId = element.getAttribute("EventId");
							if (EvenId.equals(oBindingExpression.nEventId+"")) {
								
								String StartExpression = element
										.getAttribute("StartExpression");
					
								String SingalId=StartExpression.substring(4,StartExpression.length()-1);
								
								Map<String, String> map = new HashMap<String, String>();
								map.put(oBindingExpression.nEquipId + "", SingalId);
								MGridActivity.EventClose.put(m_strID, map);
							
							}
						}
					}
	
				}
			}
			isIn = false;

		}
		if (enabled == 1) {
			hand.sendEmptyMessage(1);
			
		} else if (enabled == 0) {
			hand.sendEmptyMessage(0);
		}
	}

	// params:
	String m_strID = "";
	String m_strType = "";
	int m_nZIndex = 10;
	int m_nPosX = 152;
	int m_nPosY = 287;
	int m_nWidth = 75;
	int m_nHeight = 23;
	float m_fAlpha = 1.0f;
	boolean m_bIsBold = false;
	String m_strFontFamily = "微软雅黑";
	int m_cFontColor = 0xFF000000;
	String m_strContent = "发送控制命令";
	String m_strTextOn = "告警开启";
	String m_strTextOff = "告警屏蔽";
	String m_strCmdExpression = "Binding{[Cmd[Equip:113-Temp:168-Command:1]]}";
	String m_strHorizontalContentAlignment = "Center";
	String m_strVerticalContentAlignment = "Center";
	public MainWindow m_rRenderWindow = null;
	float m_fFontSize = 6.0f;
	boolean m_bPressed = false;
	Paint m_oPaint = null;
	Rect m_rBBox = null;

	DocumentBuilderFactory dbf = null;
	DocumentBuilder db = null;
	NodeList list1 = null;
	NodeList list2 = null;
	Document doc = null;
	public boolean isIn = true;

	// 记录触摸坐标，过滤滑动操作。解决滑动误操作点击问题。
	public float m_xscal = 0;
	public float m_yscal = 0;

}
