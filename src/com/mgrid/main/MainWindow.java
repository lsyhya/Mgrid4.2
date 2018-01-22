package com.mgrid.main;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.FloatMath;
import android.util.Xml;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.ViewGroup;
import android.widget.Toast;

import com.mgrid.data.DataGetter;
import com.mgrid.data.EquipmentDataModel.Event;
import com.mgrid.data.EquipmentDataModel.Signal;
import com.mgrid.util.ExpressionUtils;
import com.sg.common.Calculator;
import com.sg.common.IObject;
import com.sg.common.MutiThreadShareObject;
import com.sg.common.SgRealTimeData;
import com.sg.common.TotalVariable;
import com.sg.common.UtExpressionParser;
import com.sg.common.UtExpressionParser.stBindingExpression;
import com.sg.common.UtExpressionParser.stExpression;
import com.sg.uis.AutoSig;
import com.sg.uis.AutoSigList;
import com.sg.uis.Breaker;
import com.sg.uis.Dial;
import com.sg.uis.Dial_A;
import com.sg.uis.Dial_B;
import com.sg.uis.Dial_C;
import com.sg.uis.EventLabel;
import com.sg.uis.HisEvent;
import com.sg.uis.HistoryEventList;
import com.sg.uis.HistorySignalList;
import com.sg.uis.LocalList;
import com.sg.uis.LsyChangExpression;
import com.sg.uis.Pilar;
import com.sg.uis.Pilar_A;
import com.sg.uis.RC_Label;
import com.sg.uis.RC_Label_FFF;
import com.sg.uis.RC_RealTime;
import com.sg.uis.SaveEquipt;
import com.sg.uis.SaveSignal;
import com.sg.uis.SeeImage;
import com.sg.uis.SgAlarmAction;
import com.sg.uis.SgAlarmActionShow;
import com.sg.uis.SgAlarmChangTime;
import com.sg.uis.SgAlarmLight;
import com.sg.uis.SgAmmeter;
import com.sg.uis.SgButton;
import com.sg.uis.SgButton_new;
import com.sg.uis.SgCableTerminal;
import com.sg.uis.SgChangIP;
import com.sg.uis.SgChangNamePhoneTypeState;
import com.sg.uis.SgChangXmlPW;
import com.sg.uis.SgChangePassWord;
import com.sg.uis.SgChart;
import com.sg.uis.SgCircleChart;
import com.sg.uis.SgCommandButton;
import com.sg.uis.SgControlAlarmWay;
import com.sg.uis.SgCurveLineChart;
import com.sg.uis.SgEllipse;
import com.sg.uis.SgEventList;
import com.sg.uis.SgForm;
import com.sg.uis.SgGND;
import com.sg.uis.SgImage;
import com.sg.uis.SgIsolationEventSetter;
import com.sg.uis.SgIsolationSwitch;
import com.sg.uis.SgLabel;
import com.sg.uis.SgPolyline;
import com.sg.uis.SgRectangle;
import com.sg.uis.SgSignalList;
import com.sg.uis.SgSignalNameSetter;
import com.sg.uis.SgStatePanel;
import com.sg.uis.SgStraightLine;
import com.sg.uis.SgTable;
import com.sg.uis.SgTextBox;
import com.sg.uis.SgTextClock;
import com.sg.uis.SgThermometer;
import com.sg.uis.SgTriggerSetter;
import com.sg.uis.SgYKParameter;
import com.sg.uis.SgYTParameter;
import com.sg.uis.SignalCurve;
import com.sg.uis.SignalCurves;
import com.sg.uis.fjw_history_SignalList;
import com.sg.uis.multi_Event_data;
import com.sg.uis.multi_data;
import com.sg.uis.test_quxian;
import com.sg.uis.tigerLabel;
import com.sg.uis.LsyNewView.AlarmCount;
import com.sg.uis.LsyNewView.AlarmLevel;
import com.sg.uis.LsyNewView.AlarmRectangle;
import com.sg.uis.LsyNewView.AlarmShieldTime;
import com.sg.uis.LsyNewView.ChangeLabel;
import com.sg.uis.LsyNewView.ChangeLabelBtn;
import com.sg.uis.LsyNewView.CoolButton;
import com.sg.uis.LsyNewView.EquipHistoryAlarm;
import com.sg.uis.LsyNewView.EventLevelAlter;
import com.sg.uis.LsyNewView.HistoryCurveChart;
import com.sg.uis.LsyNewView.OnClickBtn;
import com.sg.uis.LsyNewView.SgBARS;
import com.sg.uis.LsyNewView.SgBarChartView;
import com.sg.uis.LsyNewView.SgBrokenLine;
import com.sg.uis.LsyNewView.SgClickPieChart;
import com.sg.uis.LsyNewView.SgDial;
import com.sg.uis.LsyNewView.SgHalfCircleChart;
import com.sg.uis.LsyNewView.SgPieChart3D;
import com.sg.uis.LsyNewView.SgSplineChart;
import com.sg.uis.LsyNewView.SgStackBarChart;
import com.sg.uis.LsyNewView.SgVideoView;

import comm_service.local_file;
import comm_service.service;

import data_model.ipc_history_signal;
import data_model.local_his_event;
import data_model.local_his_signal;
import data_model.save_curve_signal;
import data_model.save_multipoint_signal;

@SuppressLint({ "HandlerLeak", "NewApi", "ClickableViewAccessibility" })
@SuppressWarnings("unused")
/** 主窗口 */
public class MainWindow extends ViewGroup {

		
	public MainWindow(final MGridActivity context) {
		super(context);
		setFocusableInTouchMode(true);
		m_mapUIs = new HashMap<String, IObject>();
		m_oShareObject = new MutiThreadShareObject();
		m_YKobj = new ArrayList<ViewGroup>();
		m_oMgridActivity = context;

		m_oInvalidateHandler = new Handler() {

			public void handleMessage(Message msg) {
				super.handleMessage(msg);

				switch (msg.what) {

				case 0:
					updateWidgets();
					break;

				case 1:
					if(MGridActivity.whatLanguage)
					Toast.makeText(context, "设置成功",
							Toast.LENGTH_SHORT).show();
					else
					Toast.makeText(context, "Success",
								Toast.LENGTH_SHORT).show();
					String uid=((String) msg.obj).replace("设置成功.", "");
					if(uid==null) return;
					
					final IObject obj_Y = m_mapUIs.get(uid);
					
					if(obj_Y==null) return;
					
					MGridActivity.xianChengChi.execute(new Runnable() {
					
					@Override
					public void run() {
						Iterator<String> iter =m_mapUIs.keySet().iterator();	
						while (iter.hasNext()) {
							String strKey = iter.next();
							IObject obj_Z = m_mapUIs.get(strKey);
				            if(obj_Z.getType().equals("tigerLabel"))
				            {

				            
				            	if(obj_Y.getBindingExpression().contains(ExpressionUtils.getExpressionUtils().removeBindingString(obj_Z.getBindingExpression())))
				            	{

				            		obj_Z.needupdate(true);
				            		
				            	}
				            }						
						}						
					}
			       });
					break;

				case 2:
					if(MGridActivity.whatLanguage)
					new AlertDialog.Builder(context).setTitle("错误")
							.setMessage((String) msg.obj).show();
					else
					new AlertDialog.Builder(context).setTitle("Title")
						    .setMessage("Fail").show();
					break;

				default:
					break;
				}
			}
		};

		m_oShareObject.clearFromTcpValue(); // instance one share object here
		m_oShareObject.m_oInvalidateHandler = m_oInvalidateHandler; // 赋予该主线程handler
																	// 接受线程池的msg（子线程）

		// timer.schedule(timertask, 0, 3000);

	}


	/** 更新变化的界面数据 */
	void updateWidgets() {
		// TODO: 这个设置是否为了确定放大后的尺寸？ 为更新放大后的UI提供定位依据。 -- CharlesChen

		MainWindow.SCREEN_WIDTH = this.getResources().getDisplayMetrics().widthPixels;
		MainWindow.SCREEN_HEIGHT = this.getResources().getDisplayMetrics().heightPixels;

		// TODO: 此等逻辑处理应该放到子线程中，主线程仅从共享队列取出对象调用更新 -- CharlesChen
		for (int i = 0; i < m_oShareObject.m_listUpdateFromTcpValues.size(); ++i) {
			IObject obj = m_oShareObject.m_listUpdateFromTcpValues.get(i);
			if (obj != null) {
				obj.updateWidget();
			}
		}

		m_oShareObject.clearFromTcpValue();

	}

	/** 替换字符串 */
	public String replaceString(String strSource, String strFrom, String strTo) {
		if (strSource == null) {
			return null;
		}
		int i = 0;
		if ((i = strSource.indexOf(strFrom, i)) >= 0) {
			char[] cSrc = strSource.toCharArray();
			char[] cTo = strTo.toCharArray();
			int len = strFrom.length();
			StringBuffer buf = new StringBuffer(cSrc.length);
			buf.append(cSrc, 0, i).append(cTo);
			i += len;
			int j = i;
			while ((i = strSource.indexOf(strFrom, i)) > 0) {
				buf.append(cSrc, j, i - j).append(cTo);
				i += len;
				j = i;
			}
			buf.append(cSrc, j, cSrc.length - j);
			return buf.toString();
		}
		return strSource;
	}

	@SuppressLint({ "InlinedApi", "FloatMath" })
	@Override
	/** 按键响应 */
	public boolean onInterceptTouchEvent(MotionEvent event) {

		switch (event.getAction() & MotionEvent.ACTION_MASK) {
		case MotionEvent.ACTION_DOWN:
			m_fOldX = event.getX();
			m_fOldY = event.getY();
			m_fDragStartX = event.getX();
			m_fDragStartY = event.getY();
			m_fDragEndX = event.getX();
			m_fDragEndY = event.getY();
			m_bTwoFigerDown = false;
			m_bOneFigerDown = true;
			m_bHadTowFiger = false;

			// CharlesChen
			if (mVelocityTracker == null) {
				mVelocityTracker = VelocityTracker.obtain();
				mVelocityTracker.addMovement(event);
			}

			return false;

		case MotionEvent.ACTION_POINTER_DOWN:
			m_fStartScaleValue = distance(event);
			if (m_fStartScaleValue > 5.0f) {
				if (MGridActivity.m_bCanZoom)
					m_bTwoFigerDown = true;
			}
			m_bOneFigerDown = false;
			m_bHadTowFiger = true;
			break;

		case MotionEvent.ACTION_MOVE:

			if (mVelocityTracker != null) {
				mVelocityTracker.addMovement(event);
			}

			if (m_bTwoFigerDown == true) {
				float fCurDis = distance(event);
				if (fCurDis < 5.0f)
					break;
				float fScale = (fCurDis - m_fStartScaleValue) / 250.0f;
				m_fStartScaleValue = fCurDis;
				m_fScale += fScale;

				m_fScale = m_fScale < 1.0f ? 1.0f : m_fScale;
				m_fScale = m_fScale > 5.0f ? 5.0f : m_fScale;
				this.setScaleX(m_fScale);
				this.setScaleY(m_fScale);
				// reset translate
				if (this.getLeft() - this.getRight() * 0.5f * (m_fScale - 1.0f)
						- m_fOffsetX >= 0) {
					m_fOffsetX += this.getLeft() - this.getRight() * 0.5f
							* (m_fScale - 1.0f) - m_fOffsetX;
				}
				if (this.getRight() + this.getRight() * 0.5f
						* (m_fScale - 1.0f) - m_fOffsetX <= SCREEN_WIDTH) {
					m_fOffsetX += this.getRight() + this.getRight() * 0.5f
							* (m_fScale - 1.0f) - m_fOffsetX - SCREEN_WIDTH;
				}
				if (this.getTop() - this.getBottom() * 0.5f * (m_fScale - 1.0f)
						- m_fOffsetY >= 0) {
					m_fOffsetY += this.getTop() - this.getBottom() * 0.5f
							* (m_fScale - 1.0f) - m_fOffsetY;
				}
				if (this.getBottom() + this.getBottom() * 0.5f
						* (m_fScale - 1.0f) - m_fOffsetY <= SCREEN_HEIGHT) {
					m_fOffsetY += this.getBottom() + this.getBottom() * 0.5f
							* (m_fScale - 1.0f) - m_fOffsetY - SCREEN_HEIGHT;
				}
				if (m_fScale <= 1.0f) {
					m_fOffsetX = 0.0f;
					m_fOffsetY = 0.0f;
				}
				this.setTranslationX(-m_fOffsetX);
				this.setTranslationY(-m_fOffsetY);
			} else if (m_bOneFigerDown == true) {

				if (m_fScale <= 1.0f) {
					// TODO: 滑屏操作，应加入画面跟随手指动画。 -- CharlesChen
					break; 
				}

				float fEllipseX = m_fOldX - event.getX();
				float fEllipseY = m_fOldY - event.getY();
 
				m_fOldX = event.getX();
				m_fOldY = event.getY();

				if (this.getLeft() - this.getRight() * 0.5f * (m_fScale - 1.0f)
						- (m_fOffsetX + fEllipseX) > 0)
					fEllipseX = 0.0f;
				if (this.getRight() + this.getRight() * 0.5f
						* (m_fScale - 1.0f) - (m_fOffsetX + fEllipseX) < SCREEN_WIDTH)
					fEllipseX = 0.0f;
				if (this.getTop() - this.getBottom() * 0.5f * (m_fScale - 1.0f)
						- (m_fOffsetY + fEllipseY) > 0)
					fEllipseY = 0.0f;
				if (this.getBottom() + this.getBottom() * 0.5f
						* (m_fScale - 1.0f) - (m_fOffsetY + fEllipseY) < SCREEN_HEIGHT)
					fEllipseY = 0.0f;

				m_fOffsetX += fEllipseX * 0.75f;
				m_fOffsetY += fEllipseY * 0.75f;

				this.setTranslationX(-m_fOffsetX);
				this.setTranslationY(-m_fOffsetY);
			}
			break;

		case MotionEvent.ACTION_POINTER_UP:
			
			
			m_bTwoFigerDown = false; // 会引起缩放操作时误切页 -- CharlesChen
			break;

		case MotionEvent.ACTION_UP:
			
			
//			
//			if(timer!=null&&timertask!=null)
//			{
//			   isChangePage=true;
//			}
			this.requestFocus();
			m_oMgridActivity.mImm.hideSoftInputFromWindow(
					this.getWindowToken(), 0);

			m_bOneFigerDown = false;
			m_fDragEndX = event.getX();
			m_fDragEndY = event.getY();
			if (m_fScale <= 1.0f && !m_bHadTowFiger) {

				// 通过加速度判断是否切页，不再使用距离计算。 -- CharlesChen
				int velocityX = 0;
				int velocityY = 0;
				if (mVelocityTracker != null) {
					mVelocityTracker.addMovement(event);
					mVelocityTracker.computeCurrentVelocity(1000);

					velocityX = (int) mVelocityTracker.getXVelocity();
					velocityY = (int) mVelocityTracker.getYVelocity();

					mVelocityTracker.recycle();
					mVelocityTracker = null;
				}

				if (m_fDragEndX - m_fDragStartX == 0
						|| Math.abs((m_fDragEndY - m_fDragStartY)
								/ (m_fDragEndX - m_fDragStartX)) > 0.6)
					break;

				if (velocityY > 300 && velocityY != 0
						&& Math.abs(velocityX / velocityY) < 2.8)
					break;

				// 有待改善啊，暂时只支持一个权限页面，有多个权限页面将不需要密码直接滑进页面。
				if (velocityX > SNAP_VELOCITY && null != m_oPrevPage && !m_oPrevPage.m_strCurrentPage.equals("动态图.xml")) {
                      boolean isPage=true;
                      if(MGridActivity.m_MaskPage!=null)
                      {
                          for(String ss[]:MGridActivity.m_MaskPage)
                          {
                        	  for(String s:ss)
                        	  {
                        		  if(s.equals(m_oPrevPage.m_strCurrentPage))
                             	 {
                             		 isPage=false;
                             	 }
                        	  }
                        	
                          }
                      }

                      if(isPage)
                      {
                    	  m_oMgridActivity.onPageChange(m_oPrevPage.m_strCurrentPage);
                      }

				} else if (velocityX < -SNAP_VELOCITY && null != m_oNextPage &&!m_oNextPage.m_strCurrentPage.equals("动态图.xml")) {

					 boolean isPage=true;
					
                    if(MGridActivity.m_MaskPage!=null)
                    {
                    	 for(String ss[]:MGridActivity.m_MaskPage)
                         {
                        	 for(String s:ss)
                        	 {
                        		 if(s.equals(m_oNextPage.m_strCurrentPage))
                               	 {
                               		 isPage=false;
                               	 }
                        	 }
                       	 
                         }
                    }
                     if(isPage)
                     {
                   	  m_oMgridActivity.onPageChange(m_oNextPage.m_strCurrentPage);
                     }

				} else {

				}

			}

			m_bHadTowFiger = false;
			break;

		default:
			break;
		}

		return false;
	}

	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			break;

		case MotionEvent.ACTION_UP:

			break;
		}

		return false;
	}

	// TODO: 有无其他影响尚待观察 -- CharlesChen
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		if (m_YKobj.isEmpty())
			return;

		Iterator<ViewGroup> ykobj_it = m_YKobj.iterator();
		for (; ykobj_it.hasNext();) {
			ykobj_it.next().measure(widthMeasureSpec, heightMeasureSpec);
		}

	}

	protected void dispatchDraw(Canvas canvas) {
		super.dispatchDraw(canvas);

		if (m_YKobj.isEmpty())
			return;

		Iterator<ViewGroup> ykobj_it = m_YKobj.iterator();
		for (; ykobj_it.hasNext();) {
			drawChild(canvas, ykobj_it.next(), getDrawingTime());
		}
	}

	/** 计算两点之间的距离像素 **/
	@SuppressLint({ "NewApi", "FloatMath" })
	/** 获取后面的点坐标 - 前面点的坐标 */
	private float distance(MotionEvent e) {
		float eX = e.getX(1) - e.getX(0); // 后面的点坐标 - 前面点的坐标
		float eY = e.getY(1) - e.getY(0);
		return FloatMath.sqrt(eX * eX + eY * eY);
	}

	/* @throws FileNotFoundException */
	public void loadPage(String xmlFile) throws FileNotFoundException {
		m_strCurrentPage = xmlFile;
		parseXml(xmlFile);

		m_oCaculateThread = new SgExpressionCacularThread();
		m_oCaculateThread.setHasRandomData(m_bHasRandomData);

		
		Iterator<String> iter = m_mapUIs.keySet().iterator();
		while (iter.hasNext()) {
			String strKey = iter.next();
			IObject obj = m_mapUIs.get(strKey);
			m_oCaculateThread.addExpression(obj.getUniqueID(), obj.getType(),
					obj.getBindingExpression());

			obj.initFinished();
		}

		m_oCaculateThread.start();
	}

	protected void unloadPage() {
		m_oCaculateThread.autoDestroy();
		m_fOffsetX = 0.0f;
		m_fOffsetY = 0.0f;
		m_fScale = 1.0f;
		this.setScaleX(m_fScale);
		this.setScaleY(m_fScale);
		this.setTranslationX(m_fOffsetX);
		this.setTranslationY(m_fOffsetY);
		Iterator<String> iter = m_mapUIs.keySet().iterator();
		while (iter.hasNext()) {
			String strKey = iter.next();
			IObject object = m_mapUIs.get(strKey);
			object.removeFromRenderWindow(this);
			object = null;
		}
		m_mapUIs.clear();
		System.gc();
	}

	public void changePage(String strPage) {
		String[] arrStr = strPage.split(",");
		m_strReplaceX1 = null;
		if (arrStr.length == 2)
			m_strReplaceX1 = arrStr[1];
		String strNewPage = arrStr[0] + ".xml";
		if (strNewPage.equals(m_strCurrentPage) == false) {

			switch (SWITCH_STYLE) {
			case 1:
				m_oMgridActivity.applyRotation(strNewPage, 0, 90);
				break;

			default:
				m_oMgridActivity.onPageChange(strNewPage);
			}
		}
	}

	public void active(boolean isFrontPage) {
		m_bIsActive = isFrontPage;
		if (isFrontPage)
			synchronized (this) {
				this.notifyAll();
			}
	}

	private void showMsgDlg(String title, String message) {
		if (++NUMOFDAILOG < 10 && MGridActivity.m_bErrMsgParser) {
			new AlertDialog.Builder(m_oMgridActivity).setTitle(title)
					.setMessage(message).show();
		} else if (NUMOFDAILOG == 10 && MGridActivity.m_bErrMsgParser) {
			new AlertDialog.Builder(m_oMgridActivity).setTitle("无法显示更多错误信息")
					.setMessage("该组态配置中错误过多！！！").show();
		}
	}

	public void showTaskUI(boolean bShow) {
		m_oMgridActivity.showTaskUI(bShow);
	}

	// CharlesChen
	private VelocityTracker mVelocityTracker;
	private static final int SNAP_VELOCITY = 600;

	public String m_strCurrentPage = "";
	public MainWindow m_oPrevPage = null;
	public MainWindow m_oNextPage = null;
	public List<ViewGroup> m_YKobj = null;

	@Override
	protected void onLayout(boolean bool, int l, int t, int r, int b) {
		VIEW_LEFT = l;
		VIEW_RIGHT = r;
		VIEW_TOP = t;
		VIEW_BOTTOM = b;

		Iterator<String> iter = m_mapUIs.keySet().iterator();
		while (iter.hasNext()) {
			String strKey = iter.next();
			IObject object = m_mapUIs.get(strKey);
			object.doLayout(bool, l, t, r, b);
		}
	}

	/**
	 * @throws FileNotFoundException
	 */
	
	
	
	public void parseXml(String xmlFile) throws FileNotFoundException {
		String[] arrStr = xmlFile.split("\\.");
		m_strResFolder = m_strRootFolder + arrStr[0] + ".files/";
       System.out.println("xmlFile:"+xmlFile);
       System.out.println(Thread.currentThread().getName());
        MGridActivity.XmlFile=xmlFile;
		InputStream is = new BufferedInputStream(new FileInputStream(
				Environment.getExternalStorageDirectory().getPath()
						+ m_strRootFolder + xmlFile));
		parseStream(is);

		for (int i = 0; i <= MAXZINDEX; i++) {
			Iterator<HashMap.Entry<String, IObject>> entry_it = m_mapUIs
					.entrySet().iterator();
			while (entry_it.hasNext()) {
				HashMap.Entry<String, IObject> entry = entry_it.next();
				IObject obj = entry.getValue();
				if (i == obj.getZIndex()) {

					obj.addToRenderWindow(this);

				}
			}
		}

		try {
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void parseStream(InputStream inStream) {
		String strElementType = "";
		IObject iCurrentObj = null;
		XmlPullParser pullParser = Xml.newPullParser();

		try {
			pullParser.setInput(inStream, "utf-8");
			int eventType = pullParser.getEventType();

			while (eventType != XmlPullParser.END_DOCUMENT) {

				if (eventType == XmlPullParser.START_TAG) {
					String name = pullParser.getName();

					if ("Element".equals(name)) {
						String strID = pullParser.getAttributeValue("", "ID");
						String strType = pullParser.getAttributeValue("",
								"Type");
						strElementType = strType;

						boolean bExit = true;
						if ("Form".equals(strType)) {
							SgForm from = new SgForm(this.getContext());
							m_mapUIs.put(strID, from);
						} else if ("Label".equals(strType)) {
							SgLabel sgLabel = new SgLabel(this.getContext());
							m_mapUIs.put(strID, sgLabel);
						} else if ("TextClock".equals(strType)) {
							SgTextClock sgTextClock = new SgTextClock(
									this.getContext());
							m_mapUIs.put(strID, sgTextClock);
						} else if ("StraightLine".equals(strType)) {
							SgStraightLine sgStraightLine = new SgStraightLine(
									this.getContext());
							m_mapUIs.put(strID, sgStraightLine);
						} else if ("Rectangle".equals(strType)) {
							SgRectangle sgRectangle = new SgRectangle(
									this.getContext());
							m_mapUIs.put(strID, sgRectangle);
						} else if ("Ellipse".equals(strType)) {
							SgEllipse sgEllipse = new SgEllipse(
									this.getContext());
							m_mapUIs.put(strID, sgEllipse);
						} else if ("Polyline".equals(strType)) {
							SgPolyline sgPolyline = new SgPolyline(
									this.getContext());
							m_mapUIs.put(strID, sgPolyline);
						} else if ("Image".equals(strType)) {
							SgImage sgImage = new SgImage(this.getContext());
							m_mapUIs.put(strID, sgImage);
						} else if ("Button".equals(strType)) {
							SgButton sgButton = new SgButton(this.getContext());
							//SgButton_new  sgButton = new SgButton_new(this.getContext());
							m_mapUIs.put(strID, sgButton);
						} else if ("TextBox".equals(strType)) {
							SgTextBox sgTextBox = new SgTextBox(
									this.getContext());
							m_mapUIs.put(strID, sgTextBox);
						} else if ("Table".equals(strType)) {
							SgTable sgTable = new SgTable(this.getContext());
							m_mapUIs.put(strID, sgTable);
						} else if ("CommandButton".equals(strType)) {
							SgCommandButton sgCommandButton = new SgCommandButton(
									this.getContext());
							m_mapUIs.put(strID, sgCommandButton);
						} else if ("YTParameter".equals(strType)) {
							SgYTParameter sgYTParameter = new SgYTParameter(
									this.getContext());
							m_mapUIs.put(strID, sgYTParameter);
						} else if ("YKParameter".equals(strType)) {
							SgYKParameter sgYKParameter = new SgYKParameter(
									this.getContext());
							m_mapUIs.put(strID, sgYKParameter);
							m_YKobj.add(sgYKParameter.m_oSpinner);
						} else if ("AlarmLight".equals(strType)) {
							SgAlarmLight sgAlarmLight = new SgAlarmLight(
									this.getContext());
							m_mapUIs.put(strID, sgAlarmLight);
						} else if ("Thermometer".equals(strType)) {
							SgThermometer sgThermometer = new SgThermometer(
									this.getContext());
							m_mapUIs.put(strID, sgThermometer);
						} else if ("Ammeter".equals(strType)) {
							SgAmmeter sgAmmeter = new SgAmmeter(
									this.getContext());
							m_mapUIs.put(strID, sgAmmeter);
						} else if ("SignalList".equals(strType)) {
							SgSignalList sgSignalList = new SgSignalList(
									this.getContext());
							m_mapUIs.put(strID, sgSignalList);
						} else if ("EventList".equals(strType)) {
							SgEventList sgEventList = new SgEventList(
									this.getContext());
							m_mapUIs.put(strID, sgEventList);
						} else if ("StatePanel".equals(strType)) {
							SgStatePanel sgStatePanel = new SgStatePanel(
									this.getContext());
							m_mapUIs.put(strID, sgStatePanel);
						} else if ("ThreeDPieChart".equals(strType)) {
							SgChart sgMultiChart = new SgChart(
									this.getContext());
							sgMultiChart.setChartType("Pie");
							m_mapUIs.put(strID, sgMultiChart);
						} else if ("MultiChart".equals(strType)) {
							SgChart sgMultiChart = new SgChart(
									this.getContext());
							sgMultiChart.setChartType("Bar");
							m_mapUIs.put(strID, sgMultiChart);
						} else if ("EventConditionStartSetter".equals(strType)) {
							SgTriggerSetter triggerSetter = new SgTriggerSetter(
									this.getContext());
							m_mapUIs.put(strID, triggerSetter);
						} else if ("HistorySignalCurve".equals(strType)) {
							SgCurveLineChart sgLineChart = new SgCurveLineChart(
									this.getContext());
							m_mapUIs.put(strID, sgLineChart);
						} else if ("IsolationSwitch".equals(strType)) {
							SgIsolationSwitch sgIsolationSwitch = new SgIsolationSwitch(
									this.getContext());
							m_mapUIs.put(strID, sgIsolationSwitch);
						} else if ("DoubleImageButton".equals(strType)) {
							SgIsolationEventSetter isolationEventSetter = new SgIsolationEventSetter(
									this.getContext());
							m_mapUIs.put(strID, isolationEventSetter);
						} else if ("SignalNameSetter".equals(strType)) {
							SgSignalNameSetter signalNameSetter = new SgSignalNameSetter(
									this.getContext());
							m_mapUIs.put(strID, signalNameSetter);
						} else if ("CableTerminal".equals(strType)) {
							SgCableTerminal cableTerminal = new SgCableTerminal(
									this.getContext());
							m_mapUIs.put(strID, cableTerminal);
						} else if ("GND".equals(strType)) {
							SgGND end = new SgGND(this.getContext());
							m_mapUIs.put(strID, end);
						}
						// fjw add
						else if ("fjw_history_SignalList".equals(strType)) {
							fjw_history_SignalList fjw_his_sig = new fjw_history_SignalList(
									this.getContext());
							m_mapUIs.put(strID, fjw_his_sig);
						} else if ("LocalList".equals(strType)) {
							LocalList locallist = new LocalList(
									this.getContext());
							m_mapUIs.put(strID, locallist);
						} else if ("HisEvent".equals(strType)) {
							HisEvent hiseventlist = new HisEvent(
									this.getContext());
							m_mapUIs.put(strID, hiseventlist);
						} else if ("HistoryEventList".equals(strType)) {
							HistoryEventList hiseventlist = new HistoryEventList(
									this.getContext());
							m_mapUIs.put(strID, hiseventlist);
						} else if ("HistorySignalList".equals(strType)) {
							HistorySignalList saveequipt = new HistorySignalList(
									this.getContext());
							m_mapUIs.put(strID, saveequipt);
						} else if ("SaveSignal".equals(strType)) {
							SaveSignal savesignal = new SaveSignal(
									this.getContext());
							m_mapUIs.put(strID, savesignal);
						} else if ("SaveEquipt".equals(strType)) {
							SaveEquipt saveequipt = new SaveEquipt(
									this.getContext());
							m_mapUIs.put(strID, saveequipt);
						} else if ("SignalCurve".equals(strType)) {
							SignalCurve signalcurve = new SignalCurve(
									this.getContext());
							m_mapUIs.put(strID, signalcurve);
						} else if ("SignalCurves".equals(strType)) {
							SignalCurves signalcurves = new SignalCurves(
									this.getContext());
							m_mapUIs.put(strID, signalcurves);
						} else if ("multi_data".equals(strType)) {
							multi_data multiData = new multi_data(
									this.getContext());
							m_mapUIs.put(strID, multiData);
						} else if ("RC_Label".equals(strType)) {
							RC_Label rc_label = new RC_Label(this.getContext());
							m_mapUIs.put(strID, rc_label);
						} else if ("AutoSig".equals(strType)) {
							AutoSig autosig = new AutoSig(this.getContext());
							m_mapUIs.put(strID, autosig);
						} else if ("AutoSigList".equals(strType)) {
							AutoSigList autosiglist = new AutoSigList(
									this.getContext());
							m_mapUIs.put(strID, autosiglist);
						} else if ("SeeImage".equals(strType)) {
							SeeImage seeimage = new SeeImage(this.getContext());
							m_mapUIs.put(strID, seeimage);
						} else if ("RC_Label_FFF".equals(strType)) {
							RC_Label_FFF RC_Label_fff = new RC_Label_FFF(
									this.getContext());
							m_mapUIs.put(strID, RC_Label_fff);
						} else if ("tigerLabel".equals(strType)) {
							tigerLabel tigerLabel = new tigerLabel(
									this.getContext());
							m_mapUIs.put(strID, tigerLabel);
						} else if ("Dial".equals(strType)) {
							Dial dial = new Dial(this.getContext());
							m_mapUIs.put(strID, dial);
						} else if ("Dial_A".equals(strType)) {
							Dial_A dial_a = new Dial_A(this.getContext());
							m_mapUIs.put(strID, dial_a);
						} else if ("Dial_B".equals(strType)) {
							Dial_B dial_b = new Dial_B(this.getContext());
							m_mapUIs.put(strID, dial_b);
						} else if ("Dial_C".equals(strType)) {
							Dial_C dial_c = new Dial_C(this.getContext());
							m_mapUIs.put(strID, dial_c);
						} else if ("Image_change".equals(strType)) {

						} else if ("ELabel".equals(strType)) {
							EventLabel eventLabel = new EventLabel(
									this.getContext());
							m_mapUIs.put(strID, eventLabel);
						} else if ("Pilar".equals(strType)) {
							Pilar pilar = new Pilar(this.getContext());
							m_mapUIs.put(strID, pilar);
						} else if ("Pilar_A".equals(strType)) {
							Pilar_A pilar_a = new Pilar_A(this.getContext());
							m_mapUIs.put(strID, pilar_a);
						} else if ("test_quxian".equals(strType)) {
							test_quxian autosiglist = new test_quxian(
									this.getContext());
							m_mapUIs.put(strID, autosiglist);
						} else if ("ChangePassWord".equals(strType)) {
							SgChangePassWord cpw = new SgChangePassWord(
									this.getContext());
							m_mapUIs.put(strID, cpw);
						} else if ("ChangeIP".equals(strType)) {
							SgChangIP CIP = new SgChangIP(this.getContext());
							m_mapUIs.put(strID, CIP);

						} else if ("SMSConfig".equals(strType)) {
							SgChangNamePhoneTypeState CNPTS = new SgChangNamePhoneTypeState(
									this.getContext());
							m_mapUIs.put(strID, CNPTS);

						} else if ("ChangXmlPW".equals(strType)) {
							SgChangXmlPW xmlPW = new SgChangXmlPW(
									this.getContext());
							m_mapUIs.put(strID, xmlPW);

						} else if ("multi_Event_data".equals(strType)) {
							multi_Event_data mEd = new multi_Event_data(
			 						this.getContext());
		 					m_mapUIs.put(strID, mEd);

						}else if("RC_RealTime".equals(strType))
						{
							RC_RealTime RCrt=new RC_RealTime(this.getContext());
							m_mapUIs.put(strID, RCrt);
						}
						else if("AlarmAction".equals(strType))
						{
							SgAlarmAction SgAA=new SgAlarmAction(this.getContext());
							m_mapUIs.put(strID, SgAA);
						}
						else if("ChangExpression".equals(strType))
						{
							LsyChangExpression Lve=new LsyChangExpression(this.getContext());
							m_mapUIs.put(strID, Lve);
						}
						else if("SgAlarmActionShow".equals(strType))
						{
							SgAlarmActionShow Sas=new SgAlarmActionShow(this.getContext());
							m_mapUIs.put(strID, Sas);
						}
						else if("SgControlAlarmWay".equals(strType))
						{
							SgControlAlarmWay Sca=new SgControlAlarmWay(this.getContext());
							m_mapUIs.put(strID, Sca);
						}
						else if("SgCircleChart".equals(strType))
						{
							SgCircleChart Scc=new SgCircleChart(this.getContext());
							m_mapUIs.put(strID, Scc);
						}
						else if("Breaker".equals(strType))
						{
							Breaker Bk=new Breaker(this.getContext());
							m_mapUIs.put(strID, Bk);
						}else if("SgAlarmChangTime".equals(strType))
						{
							SgAlarmChangTime sact=new SgAlarmChangTime(this.getContext());
							m_mapUIs.put(strID, sact);
						}else if("SgBrokenLine".equals(strType))
						{
							SgBrokenLine SBL=new SgBrokenLine(this.getContext());
							m_mapUIs.put(strID, SBL);
						}else if("SgBARS".equals(strType))
						{
							SgBARS BARS=new SgBARS(this.getContext());
							m_mapUIs.put(strID, BARS);
						}else if("SgDial".equals(strType))
						{
							SgDial SD=new SgDial(this.getContext());
							m_mapUIs.put(strID, SD);
						}else if("SgVideoView".equals(strType))
						{
							SgVideoView SVV=new SgVideoView(this.getContext());
							m_mapUIs.put(strID, SVV);
						}	else if("AlarmLevel".equals(strType))
						{
							AlarmLevel AL=new AlarmLevel(this.getContext());
							m_mapUIs.put(strID, AL);
						}
						else if("AlarmCount".equals(strType))
						{
							AlarmCount AC=new AlarmCount(this.getContext());
							m_mapUIs.put(strID, AC);
						}
						else if("ChangeLabel".equals(strType))
						{
							ChangeLabel CL=new ChangeLabel(this.getContext());
							m_mapUIs.put(strID, CL);
						}
						else if("ChangeLabelBtn".equals(strType))
						{
							ChangeLabelBtn CLB=new ChangeLabelBtn(this.getContext());
							m_mapUIs.put(strID, CLB);
						}else if("SgHalfCircleChar".equals(strType))
						{
							SgHalfCircleChart SCC=new SgHalfCircleChart(this.getContext());
							m_mapUIs.put(strID, SCC);
						}
						else if("SgClickPieChart".equals(strType))
						{
							SgClickPieChart SCPC=new SgClickPieChart(this.getContext());
							m_mapUIs.put(strID, SCPC);
						}
						else if("SgSplineChart".equals(strType)) 
						{
							SgSplineChart SSC=new SgSplineChart(this.getContext());
							m_mapUIs.put(strID, SSC);
						}else if("AlarmRectangle".equals(strType))
						{
							AlarmRectangle AR=new AlarmRectangle(this.getContext());
							m_mapUIs.put(strID, AR);
						}else if("SgBarChartView".equals(strType))
						{
							SgBarChartView SBCV=new SgBarChartView(this.getContext());
							m_mapUIs.put(strID, SBCV);
						}else if("SgStackBarChart".equals(strType))
						{
							SgStackBarChart SSBC=new SgStackBarChart(this.getContext());
							m_mapUIs.put(strID, SSBC);
						}else if("AlarmShieldTime".equals(strType))
						{
							AlarmShieldTime AST=new AlarmShieldTime(this.getContext());
							m_mapUIs.put(strID, AST);
						}else if("EquipHistoryAlarm".equals(strType))
						{
							EquipHistoryAlarm EHA=new EquipHistoryAlarm(this.getContext());
							m_mapUIs.put(strID, EHA);
						}else if("EventLevelAlter".equals(strType))
						{
							EventLevelAlter ELA=new EventLevelAlter(this.getContext());
							m_mapUIs.put(strID, ELA);
						}else if("HistoryCurveChart".equals(strType))
						{
							HistoryCurveChart HCC=new HistoryCurveChart(this.getContext());
							m_mapUIs.put(strID, HCC);
						}else if("OnClickBtn".equals(strType))
						{
							OnClickBtn OCB=new OnClickBtn(this.getContext());
							m_mapUIs.put(strID, OCB);
						}else if("CoolButton".equals(strType))
						{
							CoolButton CB=new CoolButton(this.getContext());
							m_mapUIs.put(strID, CB);
						}else if("SgPieChart3D".equals(strType))
						{
							SgPieChart3D spc=new SgPieChart3D(this.getContext());
							m_mapUIs.put(strID, spc);
						}
 
 
						else {
							showMsgDlg("警告", "不支持的控件类型： " + strType);
							bExit = false;
						}

						if (bExit == true) {
							iCurrentObj = m_mapUIs.get(strID);
							iCurrentObj.setUniqueID(strID);
							iCurrentObj.setType(strType);
							// iCurrentObj.addToRenderWindow(this);
						}

					} else if ("Property".equals(name)) {

						String strName = pullParser.getAttributeValue("",
								"Name");
						String strOrigValue = pullParser.getAttributeValue("",
								"Value");

						String strValue = strOrigValue;

						if (m_strReplaceX1 != null)
							strValue = replaceString(strOrigValue, "X1",
									m_strReplaceX1); // 模板字符串替换

						if (iCurrentObj == null)
							continue;

						/* 无意义的判断 -- CharlesChen */
						if ("Form".equals(strElementType)
								|| "AlarmLight".equals(strElementType)
								|| "Ammeter".equals(strElementType)
								|| "Button".equals(strElementType)
								|| "CommandButton".equals(strElementType)
								|| "Ellipse".equals(strElementType)
								|| "Image".equals(strElementType)
								|| "Label".equals(strElementType)
								|| "TextClock".equals(strElementType)
								|| "Polyline".equals(strElementType)
								|| "YTParameter".equals(strElementType)
								|| "YKParameter".equals(strElementType)
								|| "Rectangle".equals(strElementType)
								|| "StatePanel".equals(strElementType)
								|| "StraightLine".equals(strElementType)
								|| "Table".equals(strElementType)
								|| "TextBox".equals(strElementType)
								|| "Thermometer".equals(strElementType)
								|| "EventList".equals(strElementType)
								|| "SignalList".equals(strElementType)
								|| "ThreeDPieChart".equals(strElementType)
								|| "MultiChart".equals(strElementType)
								|| "HistorySignalCurve".equals(strElementType)
								|| "IsolationSwitch".equals(strElementType)
								|| "DoubleImageButton".equals(strElementType)
								|| "SignalNameSetter".equals(strElementType)
								|| "EventConditionStartSetter"
										.equals(strElementType)
								|| "CableTerminal".equals(strElementType)
								|| "GND".equals(strElementType)
								|| "LocalList".equals(strElementType)
								|| "HisEvent".equals(strElementType)
								|| "HistoryEventList".equals(strElementType)
								|| "HistorySignalList".equals(strElementType)
								|| "SaveSignal".equals(strElementType)
								|| "SaveEquipt".equals(strElementType)
								|| "SignalCurve".equals(strElementType)
								|| "SignalCurves".equals(strElementType)
								|| "multi_data".equals(strElementType)
								|| "RC_Label".equals(strElementType)
								|| "AutoSig".equals(strElementType)
								|| "AutoSigList".equals(strElementType)
								|| "SeeImage".equals(strElementType)
								|| "test_quxian".equals(strElementType)
								|| "RC_Label_FFF".equals(strElementType)
								|| "tigerLabel".equals(strElementType)
								|| "Dial".equals(strElementType)
								|| "Image_change".equals(strElementType)
								|| "Pilar".equals(strElementType)
								|| "Pilar_A".equals(strElementType)
								|| "Dial_A".equals(strElementType)
								|| "Dial_B".equals(strElementType)
								|| "Dial_C".equals(strElementType)
								|| "ELabel".equals(strElementType)
								|| "fjw_history_SignalList"
										.equals(strElementType)
								|| "ChangePassWord".equals(strElementType)
								|| "ChangeIP".equals(strElementType)
								|| "SMSConfig".equals(strElementType)
								|| "ChangXmlPW".equals(strElementType)
								|| "multi_Event_data".equals(strElementType)
								|| "RC_RealTime".equals(strElementType)
								|| "AlarmAction".equals(strElementType)
								|| "ChangExpression".equals(strElementType)
								|| "SgAlarmActionShow".equals(strElementType)
								|| "SgControlAlarmWay".equals(strElementType)
								|| "SgCircleChart".equals(strElementType)
								|| "Breaker".equals(strElementType)
								|| "SgAlarmChangTime".equals(strElementType)
								|| "SgBrokenLine".equals(strElementType)
								|| "SgBARS".equals(strElementType)
								|| "SgDial".equals(strElementType)
								|| "AlarmLevel".equals(strElementType)
								|| "AlarmCount".equals(strElementType)
								|| "SgVideoView".equals(strElementType)
								|| "ChangeLabel".equals(strElementType)
								|| "ChangeLabelBtn".equals(strElementType)
								|| "SgHalfCircleChar".equals(strElementType)
								|| "SgClickPieChart".equals(strElementType)
								|| "SgSplineChart".equals(strElementType)
								|| "AlarmRectangle".equals(strElementType)
								|| "SgBarChartView".equals(strElementType)
								|| "SgStackBarChart".equals(strElementType)
								|| "AlarmShieldTime".equals(strElementType)
								|| "EquipHistoryAlarm".equals(strElementType)
								|| "EventLevelAlter".equals(strElementType)
								|| "HistoryCurveChart".equals(strElementType)
								|| "OnClickBtn".equals(strElementType)
								|| "CoolButton".equals(strElementType)
								|| "SgPieChart3D".equals(strElementType)) {
							try {
								iCurrentObj.parseProperties(strName, strValue,
										m_strResFolder);
							} catch (Throwable e) {
		 						e.printStackTrace();
								showMsgDlg("错误", "解析参数失败！\n\n参数名： [ " + strName
										+ " ]\n参数值： [ " + strValue
										+ " ]\n\n页面名称： [ " + m_strCurrentPage
										+ " ]\n控件类型： [ " + strElementType
										+ " ]\n资源路径： [ " + m_strResFolder
										+ " ]\n\n错误信息：\n" + e.toString());
 
							} // end of catch
						} 
					} /* else if ("Property".equals(name)) */
				} /* if (eventType == XmlPullParser.START_TAG) */ 

				try {
					eventType = pullParser.next();
				} catch (IOException e) {
					e.printStackTrace();
				}

			} /* end of while */

		} catch (XmlPullParserException e) {
			e.printStackTrace();
		}

	} /* end of parseStream(InputStream inStream) */

	/** 判断一个矩形框是否在屏幕内 */
	public boolean isLayoutVisible(Rect bb) {
		if (bb.left < 0 && bb.right <= 0)
			return false;
		if (bb.top < 0 && bb.bottom <= 0)
			return false;
		if (bb.left >= MainWindow.SCREEN_WIDTH
				&& bb.right > MainWindow.SCREEN_WIDTH)
			return false;
		if (bb.top >= MainWindow.SCREEN_WIDTH
				&& bb.bottom > MainWindow.SCREEN_WIDTH)
			return false;

		return true; 
	}

	// Params:
	static public int MAXZINDEX = 0;
	static public int FORM_WIDTH = 0;
	static public int FORM_HEIGHT = 0;
	static public int SWITCH_STYLE = 0;
	public HashMap<String, IObject> m_mapUIs = null;

	String m_strResFolder = "/";
	String m_strRootFolder = "/"; // 指定文件夹,如 "/ShangeAndroidRes/"

	public int VIEW_LEFT = 0;
	public int VIEW_RIGHT = 0;
	public int VIEW_TOP = 0;
	public int VIEW_BOTTOM = 0;
	public static int SCREEN_WIDTH = 0;
	public static int SCREEN_HEIGHT = 0;
	// scale
	float m_fScale = 1.0f;

	public Handler m_oInvalidateHandler = null;
	boolean m_bFirstSetLayout = true;

	boolean m_bOneFigerDown = false;
	boolean m_bTwoFigerDown = false;
	boolean m_bHadTowFiger = false; // 记录是否有多个指示点，消除缩放时误切页。
	float m_fStartScaleValue = 1.0f;
	float m_fOffsetX = 0.0f;
	float m_fOffsetY = 0.0f;
	float m_fOldX = 0.0f;
	float m_fOldY = 0.0f;
	float m_fDragStartX = 0.0f;
	float m_fDragStartY = 0.0f;
	float m_fDragEndX = 0.0f;
	float m_fDragEndY = 0.0f;

	public boolean m_bHasRandomData = false;
	public boolean m_bIsActive = false;
	public MutiThreadShareObject m_oShareObject = null;
	public MGridActivity m_oMgridActivity = null;

	String m_strReplaceX1 = null;

    public SgExpressionCacularThread m_oCaculateThread = null;
    
    public Map<String, stExpression> Event_data=new HashMap<String, stExpression>();
    public   Map<String, stBindingExpression> Label_data=new HashMap<String, stBindingExpression>();

	static private short NUMOFDAILOG = 0;
	// fjw add
	static int n = 0;
	
	static int waitTime=3;

	long oldTime = 0;

	int index = 0;

	static boolean isChangePage = false;

	/** 子线程->处理数据 */
	public class SgExpressionCacularThread extends Thread {

		public SgExpressionCacularThread() {
			m_mapExpression = new HashMap<String, stExpression>();
			m_mapCaculateValues = new HashMap<String, String>();
			m_oCalculator = new Calculator();
			m_bIsRunning = true;

		}

		public void addExpression(String strUniqueID, String strUiType,
				String strExpression) {

			stExpression oMathExpress = UtExpressionParser.getInstance()
					.parseExpression(strExpression);
			if (oMathExpress != null) {
				oMathExpress.strUiType = strUiType; // ui类型
				m_mapExpression.put(strUniqueID, oMathExpress);// 添加控件id 和 控件表达式类
				if(strUiType.equals("multi_Event_data"))   	//	只用于告警饼状图  multi_Event_data  lsy add
				Event_data.put(strUniqueID, oMathExpress);	
			    

				Iterator<HashMap.Entry<String, stBindingExpression>> it = oMathExpress.mapObjectExpress
						.entrySet().iterator();
				while (it.hasNext()) {
					stBindingExpression oBindingExpression = it.next()
							.getValue();

					if(strUiType.equals("Label"))                //	只用于 SgLable
					{
						Label_data.put(strUniqueID, oBindingExpression);
						//MGridActivity.m_Label=Label_data;
					}
					if (oMathExpress.strBindType.equals("Value")) {

						DataGetter.setSignal(oBindingExpression.nEquipId,
								oBindingExpression.nSignalId, m_strCurrentPage,
								m_mapUIs.get(strUniqueID));
					} else if (oMathExpress.strBindType.equals("EventSeverity")) {

						DataGetter.setAlarmSignal(oBindingExpression.nEquipId,
								oBindingExpression.nSignalId, m_strCurrentPage,
								m_mapUIs.get(strUniqueID));
					} else if (oMathExpress.strBindType.equals("Equip")) {
						if (strUiType.equals("SignalList")) {

							DataGetter
									.setSignalList(oBindingExpression.nEquipId,
											m_strCurrentPage,
											m_mapUIs.get(strUniqueID));
						} else if (strUiType.equals("EventList")||strUiType.equals("AlarmLevel")) {

							System.out.println("我注册了"+strUniqueID);
							DataGetter.setMainAlarmList(m_mapUIs
									.get(strUniqueID));
						} else if (strUiType.equals("LocalList")) {

							DataGetter
									.setLocalSignal(
											oBindingExpression.nEquipId,
											m_strCurrentPage,
											m_mapUIs.get(strUniqueID));
						} else if (strUiType.equals("SaveEquipt")) {

							DataGetter
									.setLocalSignal(
											oBindingExpression.nEquipId,
											m_strCurrentPage,
											m_mapUIs.get(strUniqueID));
						} else if (strUiType.equals("HistorySignalList")) {

							DataGetter
									.setLocalSignal(
											oBindingExpression.nEquipId,
											m_strCurrentPage,
											m_mapUIs.get(strUniqueID));
						}
					} else if (oMathExpress.strBindType.equals("State")) {

						DataGetter.setEquipState(oBindingExpression.nEquipId,
								m_mapUIs.get(strUniqueID));
					} else if (oMathExpress.strBindType.equals("Name")) {

						if (0 == oBindingExpression.nEquipId)
							continue;

						if (0 < oBindingExpression.nSignalId
								&& 1 > oBindingExpression.nEventId)
							DataGetter
									.regSignalName(oBindingExpression.nEquipId,
											oBindingExpression.nSignalId,
											m_strCurrentPage,
											m_mapUIs.get(strUniqueID));
						else if (1 > oBindingExpression.nSignalId
								&& 0 < oBindingExpression.nEventId)
							DataGetter
									.regEventName(oBindingExpression.nEquipId,
											oBindingExpression.nSignalId,
											m_strCurrentPage,
											m_mapUIs.get(strUniqueID));
					} else if(oMathExpress.strBindType.equals("Mask")) 
					{
//						//lsy add 为了优化SgIsolationEventSetter控件 因为控件重启后无法判断是什么状态

						m_oShareObject.m_SgIsolationEventSetter.put(strUniqueID,oBindingExpression);
						MGridActivity.m_DoubleButton=m_oShareObject.m_SgIsolationEventSetter	;						
							
								
					}
					// TODO: ...
				}
			}
		}

		public void pushMutiChartDatas(String strKey, stExpression oExpression) {
			String strMutiChartKey = "";
			Iterator<String> iterMuti = oExpression.mapObjectExpress.keySet()
					.iterator();
			List<String> listCharts = new ArrayList<String>();
			while (iterMuti.hasNext()) {
				strMutiChartKey = iterMuti.next();
				stBindingExpression bindingExpression = oExpression.mapObjectExpress
						.get(strMutiChartKey);
				if ("Value".equals(bindingExpression.strBindType)) {
					listCharts.add(DataGetter.getSignalValue(
							bindingExpression.nEquipId,
							bindingExpression.nSignalId));

				}
			}
			m_oShareObject.m_mapMutiChartDatas.put(strKey, listCharts);
		}

		private void pushSignalName(String strUniqueId,
				stExpression oMathExpression) {
			if (oMathExpression == null)
				return;

			String strRetValue = "";
			String strKey = "";
			Iterator<String> iter = oMathExpression.mapObjectExpress.keySet()
					.iterator();
			if (iter.hasNext())
				strKey = iter.next();
			stBindingExpression oFirstBindingExp = oMathExpression.mapObjectExpress
					.get(strKey);
			if (oFirstBindingExp == null)
				return;

			if (0 < oFirstBindingExp.nSignalId && 1 > oFirstBindingExp.nEventId)
				strRetValue = DataGetter.getSignalName(
						oFirstBindingExp.nEquipId, oFirstBindingExp.nSignalId);
			else if (1 > oFirstBindingExp.nSignalId
					&& 0 < oFirstBindingExp.nEventId)
				strRetValue = DataGetter.getEventName(
						oFirstBindingExp.nEquipId, oFirstBindingExp.nEventId);
			else
				return;

			SgRealTimeData oRealTimeData = new SgRealTimeData();
			oRealTimeData.nDataType = oFirstBindingExp.nValueType;
			oRealTimeData.strValue = strRetValue;
			m_oShareObject.m_mapRealTimeDatas.put(strUniqueId, oRealTimeData);
		}

		private void pushRealTimeValue(String strUniqueId,
				stExpression oMathExpression) {
			if (oMathExpression == null)
				return;

			String strRetValue = "";
			String strKey = "";
			String strData = "0";

			Iterator<String> iter = oMathExpression.mapObjectExpress.keySet()
					.iterator();
			if (iter.hasNext())
			strKey = iter.next();
			stBindingExpression oFirstBindingExp = oMathExpression.mapObjectExpress
					.get(strKey);
			if (oFirstBindingExp == null)
				return;

			if (oFirstBindingExp.nValueType == 0
					|| oFirstBindingExp.nValueType == 2
					|| oFirstBindingExp.nValueType == 3
					|| oFirstBindingExp.nValueType == 4) {
				String strMathExpression = "";

				int nSize = oMathExpression.listMathExpress.size();

				for (int i = 0; i < nSize; ++i) {
					String strStr = oMathExpression.listMathExpress.get(i);

					if (strStr.length() != 1) {
						stBindingExpression oExpress = oMathExpression.mapObjectExpress
								.get(strStr);

						if (nSize > 1) {

							strStr = DataGetter.getSignalValue(
									oExpress.nEquipId, oExpress.nSignalId);

						}

						else if (m_mapUIs.get(strUniqueId) instanceof SgIsolationSwitch) {
							// m_mapUIs.get(strUniqueId) instanceof
							// SgIsolationSwitch
							// SgIsolationSwitch.class.isInstance(m_mapUIs.get(strUniqueId))
							strStr = DataGetter.getSignalValue(
									oExpress.nEquipId, oExpress.nSignalId);

						}
						else if (m_mapUIs.get(strUniqueId) instanceof Breaker) {
							// m_mapUIs.get(strUniqueId) instanceof
							// SgIsolationSwitch
							// SgIsolationSwitch.class.isInstance(m_mapUIs.get(strUniqueId))
							strStr = DataGetter.getSignalValue(
									oExpress.nEquipId, oExpress.nSignalId);

						}

						else {
							strStr = getRealTimeValueFromTcp(oExpress);
							strData = DataGetter.getSignalValue(
									oExpress.nEquipId, oExpress.nSignalId);

							// fjw add mark there is SignalCure into!

						}
					}

					strMathExpression += strStr;
				}

				// fjw mark

				if ("multi_data".equals(oMathExpression.strUiType)) {
					SgRealTimeData oRealTimeData_multi = new SgRealTimeData();
					oRealTimeData_multi.nDataType = oFirstBindingExp.nValueType;
					oRealTimeData_multi.strValue = strMathExpression;
					m_oShareObject.m_mapRealTimeDatas.put(strUniqueId,
							oRealTimeData_multi);
					return;
				}
				if ("AutoSig".equals(oMathExpression.strUiType)) {
					SgRealTimeData oRealTimeData_multi = new SgRealTimeData();
					oRealTimeData_multi.nDataType = oFirstBindingExp.nValueType;
					oRealTimeData_multi.strValue = strMathExpression;
					m_oShareObject.m_mapRealTimeDatas.put(strUniqueId,
							oRealTimeData_multi);
					return;
				}
				if ("SgBARS".equals(oMathExpression.strUiType)) {
					SgRealTimeData oRealTimeData_multi = new SgRealTimeData();
					oRealTimeData_multi.nDataType = oFirstBindingExp.nValueType;
					oRealTimeData_multi.strValue = strMathExpression;
					m_oShareObject.m_mapRealTimeDatas.put(strUniqueId,
							oRealTimeData_multi);
					return;
				}
				// fjw add end
				//lsy add 2016_11_02
//				if("RC_RealTime".equals(oMathExpression.strUiType))
//				{
//					SgRealTimeData oRealTimeData_multi = new SgRealTimeData();
//					oRealTimeData_multi.nDataType = oFirstBindingExp.nValueType;
//					oRealTimeData_multi.sigalName=DataGetter.getSignalName(oFirstBindingExp.nEquipId, oFirstBindingExp.nSignalId);
//					oRealTimeData_multi.strValue =  getRealTimeValueFromTcp(oFirstBindingExp);
//					if(oRealTimeData_multi.strValue.equals("-999999"))
//					{
//						oRealTimeData_multi.strValue="-1";
//					}
//					oRealTimeData_multi.strData=DataGetter.getSignalValue(oFirstBindingExp.nEquipId,
//							oFirstBindingExp.nSignalId);
//					m_oShareObject.m_mapRealTimeDatas.put(strUniqueId,
//							oRealTimeData_multi);
//					
//				}
				//lsy end 2016_11_02

				if ("".equals(strMathExpression) == false && nSize > 1) {
					try {
						strRetValue = m_oCalculator
								.calculate(strMathExpression) + "";
						strData = strRetValue;
					} catch (Exception e) {

					}
				} else {
					strRetValue = strMathExpression;
				}

			} else {
				strRetValue = getRealTimeValueFromTcp(oFirstBindingExp);
				strData = DataGetter.getSignalValue(oFirstBindingExp.nEquipId,
						oFirstBindingExp.nSignalId);

			}

			SgRealTimeData oRealTimeData = new SgRealTimeData();
			oRealTimeData.nDataType = oFirstBindingExp.nValueType;
			oRealTimeData.strValue = strRetValue;
			oRealTimeData.strData = strData;
			m_oShareObject.m_mapRealTimeDatas.put(strUniqueId, oRealTimeData);
		}
		
		

		public boolean getLocalData(stExpression oMathExpression) {
			if (oMathExpression == null)
				return false;
			try {

				int f_eqiupid = 0;
				Iterator<String> iter = oMathExpression.mapObjectExpress
						.keySet().iterator();
				if (iter.hasNext()) {
					String strKey = iter.next();
					f_eqiupid = oMathExpression.mapObjectExpress.get(strKey).nEquipId;

				}

				if (LocalList.equip_id == 0)
					return false;
				String file_name = LocalList.equip_id + "#" + LocalList.get_day;

				if (file_name.length() < 8)
					return false;

				local_file l_file = new local_file();
				if (!l_file.has_file(file_name, 1)) {

					return false;
				}

				if (l_file.read_all_line() == false) {

					return false;
				}
				List<String> buflist = new ArrayList<String>();
				buflist = l_file.buflist1;

				int flag = 0;
				local_data_list.clear();
				Iterator<String> iterator = buflist.iterator();
				while (iterator.hasNext()) {
					String buf = iterator.next();

					if (buf == null) {
						flag++;
						if (flag > 3) {
							break;
						}
						continue;
					}
					local_his_signal signal_data = new local_his_signal();
					if (!signal_data.read_string(buf)) {

					}

					local_data_list.add(signal_data);
					signal_data = null;

				}
				buflist = null;
				l_file = null;

			} catch (Exception e) {
				local_data_list.clear();

				return false;
			}

			return true;
		}

		public boolean getSaveEquiptList(String uiType) {

			String EquiptId = SaveEquipt.str_EquiptId;
			String get_date = SaveEquipt.get_day;
			if ("SaveEquipt".equals(uiType)) {
				EquiptId = SaveEquipt.str_EquiptId;
				get_date = SaveEquipt.get_day;
			}
			if ("HistorySignalList".equals(uiType)) {
				EquiptId = HistorySignalList.str_EquiptId;
				get_date = HistorySignalList.get_day;
			}
			if (("".equals(EquiptId)) || (EquiptId == null)
					|| ("".equals(get_date)))
				return false;
			String file_name = EquiptId + "#" + get_date;

			try {

				local_file l_file = new local_file();
				if (!l_file.has_file(file_name, 1)) {

					return false;
				}

				if (l_file.read_all_line() == false) {

					return false;
				}
				List<String> buflist = new ArrayList<String>();
				buflist = l_file.buflist1;
				l_file = null;

				int flag = 0;
				his_equipt_list.clear();
				Iterator<String> iterator = buflist.iterator();
				while (iterator.hasNext()) {
					String buf = iterator.next();
					if ((buf == null) || ("".equals(buf)) || (buf.length() < 5)) {
						flag++;
						if (flag > 3) {
							break;
						}
						continue;
					}
					local_his_signal signal_data = new local_his_signal();
					if (!signal_data.read_string(buf)) {

					}
					his_equipt_list.add(signal_data);
					signal_data = null;
				}
				buflist = null;
			} catch (Exception e) {

				return false;
			}
			return true;
		}

		public boolean getSaveSignalList() {

			String EquiptSignalId = SaveSignal.str_EquiptSignalId;
			String get_date = SaveSignal.get_day;
			if (("".equals(EquiptSignalId)) || (EquiptSignalId == null)
					|| ("".equals(get_date)))
				return false;
			String file_name = EquiptSignalId + "#" + get_date;

			try {

				local_file l_file = new local_file();
				if (!l_file.has_file(file_name, 2)) {

					return false;
				}

				if (l_file.read_all_line() == false) {

					return false;
				}
				List<String> buflist = new ArrayList<String>();
				buflist = l_file.buflist1;
				l_file = null;

				int flag = 0;
				his_signal_list.clear();
				Iterator<String> iterator = buflist.iterator();
				while (iterator.hasNext()) {
					String buf = iterator.next();
					if ((buf == null) || ("".equals(buf)) || (buf.length() < 5)) {
						flag++;
						if (flag > 3) {
							break;
						}
						continue;
					}
					local_his_signal signal_data = new local_his_signal();
					if (!signal_data.read_string(buf)) {

					}
					his_signal_list.add(signal_data);
					signal_data = null;
				}
				buflist = null;
			} catch (Exception e) {

				return false;
			}
			return true;
		}

		public save_curve_signal getCurveSignal1(stExpression oMathExpression,
				String ui_ID) {
			if (oMathExpression == null)
				return null;

			String strKey = "";
			Iterator<String> iter = oMathExpression.mapObjectExpress.keySet()
					.iterator();
			if (iter.hasNext())
				strKey = iter.next();

			return DataGetter.getCurveSignal(
					oMathExpression.mapObjectExpress.get(strKey).nEquipId,
					oMathExpression.mapObjectExpress.get(strKey).nSignalId,
					ui_ID);
		}

		public save_multipoint_signal getCurvesSignal1(
				stExpression oMathExpression, String ui_ID) {
			if (oMathExpression == null)
				return null;

			String strKey = "";
			Iterator<String> iter = oMathExpression.mapObjectExpress.keySet()
					.iterator();
			if (iter.hasNext())
				strKey = iter.next();

			return DataGetter.getCurvesSignal(
					oMathExpression.mapObjectExpress.get(strKey).nEquipId,
					oMathExpression.mapObjectExpress.get(strKey).nSignalId,
					ui_ID);
		}

		public List<local_his_event> getHisEvent(String uiType) {

			String filename = "hisevent-" + HisEvent.str_EquiptId;
			if ("HisEvent".equals(uiType)) {
				filename = "hisevent-" + HisEvent.str_EquiptId;
			}
			if ("HistoryEventList".equals(uiType)) {
				filename = "hisevent-" + HistoryEventList.str_EquiptId;
			}
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

//					if (his_event_list.size() > 500) {
//						break;
//					}
					his_event = null;
				}
			} catch (Exception e) {

			}

			return his_event_list;
		}

		public List<ipc_history_signal> get_test_his_list() {

			List<ipc_history_signal> his_data = new ArrayList<ipc_history_signal>();
			his_data = service.get_his_sig_list(service.IP, service.PORT, 9, 8,
					fjw_history_SignalList.my_his_startTime,
					fjw_history_SignalList.my_his_span,
					fjw_history_SignalList.my_his_count, false);
			if (his_data == null) {

			}
			return his_data;
		}

		public List<ipc_history_signal> getHistorySinals(
				stExpression oMathExpression) {
			if (oMathExpression == null)
				return null;

			
			String strKey = "";
			Iterator<String> iter = oMathExpression.mapObjectExpress.keySet()
					.iterator();

			// fjw add
			if (iter.hasNext()) {
				strKey = iter.next();

			}

			List<ipc_history_signal> list_his = new ArrayList<ipc_history_signal>();
			List<ipc_history_signal> list_his1 = new ArrayList<ipc_history_signal>();
			list_his1 = service.get_history_signal_list(service.IP,
					service.PORT, 9);

			for (int k = 0; k < list_his.size(); k++) {
				ipc_history_signal his_sig = list_his1.get(k);

			}

			return list_his1;

		}

		/** 获取实时告警列表 */
		private Hashtable<String, Hashtable<String, Event>> getActiveEvents(
				stExpression oMathExpression) {
			if (oMathExpression == null)
				return null;

			// fjw add

			return DataGetter.getRTEventList();
		}

		private Hashtable<String, Signal> getActiveSignals(
				stExpression oMathExpression) {
			if (oMathExpression == null)
				return null;

			String strKey = "";
			Iterator<String> iter = oMathExpression.mapObjectExpress.keySet()
					.iterator();
			if (iter.hasNext())
				strKey = iter.next();
			return DataGetter
					.getEquipSignalList(oMathExpression.mapObjectExpress
							.get(strKey).nEquipId);
		}

		public void autoDestroy() {
			m_bIsRunning = false;
		}

		@Override
		public void run() {
			
			m_oCaculateThread
					.setPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);// 设置线程优先级

			if (m_mapExpression.isEmpty())
				return;

			HashMap<String, stExpression> mapCmds = new HashMap<String, stExpression>();

			HashMap<String, stExpression> mapTriggers = new HashMap<String, stExpression>();

			HashMap<IObject, stExpression> mapSignals = new HashMap<IObject, stExpression>();

			HashMap<String, stExpression> mapNamings = new HashMap<String, stExpression>();

			Iterator<HashMap.Entry<String, stExpression>> exp_it = m_mapExpression
					.entrySet().iterator();
			while (exp_it.hasNext()) {
				HashMap.Entry<String, stExpression> entry = exp_it.next();
				String strKey = entry.getKey();// 获取的是UIid
				stExpression oExpression = entry.getValue();

			    
				if (oExpression == null)
					continue;

				if ("Cmd".equals(oExpression.strBindType)) {
					mapCmds.put(strKey, oExpression);
					continue;
				} else if ("Trigger".equals(oExpression.strBindType)
						|| "Mask".equals(oExpression.strBindType)) {
					
					if(strKey.contains("tigerLabel"))
					{
						mapSignals.put(m_mapUIs.get(strKey), oExpression);
						
					}else
					{
                        mapTriggers.put(strKey, oExpression);
					}
					
					
					continue;
				} else if ("Naming".equals(oExpression.strBindType)
						|| "SignalNameSetter".equals(oExpression.strUiType)) {
					mapNamings.put(strKey, oExpression);
					continue;
				} else {
					mapSignals.put(m_mapUIs.get(strKey), oExpression);

				}
			} // end of while (exp_it.hasNext())

			try {

				int listneedupdate = 10;
				int curveneedupdate = 7;

				while (m_bIsRunning) {
					
					// 对于非活动页面的后台线程，使进入慢速运作状态。 -- CharlesChen
					
					if (!m_bIsActive) {

						synchronized (MainWindow.this) {
							MainWindow.this.wait(5*1000);
//							waitTime++;
//							if(waitTime>=8) waitTime=3;
						}

						//continue;
					}
              
				//	System.out.println("SgExpressionXXX:m_bIsRunning开始运行"+Thread.currentThread().getName());
					if (m_oShareObject.m_listUpdateFromTcpValues.size() > 0) // 主线程是否已经处理完毕
					{
						yield(); // 切出CPU时间片代替死循环等待 -- CharlesChen
						continue;
					}
                   
					boolean hasupdate = false;
					Iterator<HashMap.Entry<IObject, stExpression>> iter = mapSignals
							.entrySet().iterator();
	 				while (iter.hasNext()) {

						HashMap.Entry<IObject, stExpression> entry = iter
								.next();
						IObject io=entry.getKey();
						String s=io.getType();
					
						
						if (!entry.getKey().needupdate())
							continue;
						
						
						String strKey = entry.getKey().getUniqueID();

						stExpression oExpression = entry.getValue();
						if (oExpression == null)
							continue;

						if ("Equip".equals(oExpression.strBindType)) {

							if ("SignalList".equals(oExpression.strUiType)) {

								if (--listneedupdate != 0)
									continue;
				 				else
									listneedupdate = 10;

								m_oShareObject.m_mapSignalListDatas.put(strKey,
										getActiveSignals(oExpression));
							}

							if ("EventList".equals(oExpression.strUiType)) {

								m_oShareObject.m_mapEventListDatas.put(strKey,
										getActiveEvents(oExpression));// 获取到的列表数据放入线程池共享数据
							}

							if ("SaveEquipt".equals(oExpression.strUiType)
									|| ("HistorySignalList"
											.equals(oExpression.strUiType))) {
								if (getSaveEquiptList(oExpression.strUiType))
									m_oShareObject.m_mapSaveEquipt.put(strKey,
											his_equipt_list);
							}

							if (("HisEvent".equals(oExpression.strUiType))
									|| ("HistoryEventList"
											.equals(oExpression.strUiType))) {

								if (getHisEvent(oExpression.strUiType) != null)
									m_oShareObject.m_mapLocalEvent.put(strKey,
											his_event_list);
							}

							if ("fjw_history_SignalList"
									.equals(oExpression.strUiType)) {

							}
							if ("LocalList".equals(oExpression.strUiType)) {

								// if(!getLocalData(oExpression)){
								// continue;
								// }
								// Log.e("has getLocalData",local_data_list.toString());
								// 把<控件名-历史数据列表>放入数据线程池的信号列表链
								// m_oShareObject.m_mapLocalSignal.put(strKey,
								// local_data_list);

							}

						} else if ("Name".equals(oExpression.strBindType)) {
							pushSignalName(strKey, oExpression);
						} else {

							if ("HistorySignalCurve"
									.equals(oExpression.strUiType)
									|| "ThreeDPieChart"
											.equals(oExpression.strUiType)
									|| "MultiChart"
											.equals(oExpression.strUiType)) {

								if (--curveneedupdate != 0)
									continue;
								else
									curveneedupdate = 7;

								pushMutiChartDatas(strKey, oExpression);

								/*
								 * // 获取历史曲线数据 if
								 * ("HistorySignalCurve".equals(oExpression
								 * .strUiType)) { List<ipc_history_signal>
								 * listHistorySignals =
								 * getHistorySinals(oExpression);
								 * List<List<ipc_history_signal>> mutiLines =
								 * new ArrayList<List<ipc_history_signal>>();
								 * for (int i = 0; i <
								 * oExpression.mapObjectExpress.size(); ++i) {
								 * mutiLines.add(new
								 * ArrayList<ipc_history_signal>()); } for (int
								 * i = 0; i < listHistorySignals.size(); ++i) {
								 * ipc_history_signal si =
								 * listHistorySignals.get(i); int nIndex1 = 0;
								 * Iterator<String> iterMuti =
								 * oExpression.mapObjectExpress
								 * .keySet().iterator(); while
								 * (iterMuti.hasNext()) { String
								 * strHistorySignalKey = iterMuti.next();
								 * stBindingExpression bindExp =
								 * oExpression.mapObjectExpress
								 * .get(strHistorySignalKey);
								 * 
								 * if (bindExp.nSignalId == si.sigid) {
								 * mutiLines.get(nIndex1).add(si); break; }
								 * nIndex1++; }
								 * 
								 * }
								 * m_oShareObject.m_mapHistorySignals.put(strKey
								 * , mutiLines); }
								 */
							} else if ("SaveSignal"
									.equals(oExpression.strUiType)) {

								if (getSaveSignalList())
									m_oShareObject.m_mapSaveSignal.put(strKey,
											his_signal_list);
							} else if ("SignalCurves"
									.equals(oExpression.strUiType)) {

								m_oShareObject.m_mapMultiPoint.put(strKey,
										getCurvesSignal1(oExpression, strKey));

							} else if ("SignalCurve"
									.equals(oExpression.strUiType)) {
								// getCurveSignal();
								m_oShareObject.m_mapHisPoint.put(strKey,
										getCurveSignal1(oExpression, strKey));

							} else if ("RC_Label".equals(oExpression.strUiType)) {
								// 待完善
							} else if ("ELabel".equals(oExpression.strUiType)) {
								// 待完善
							} else if ("tigerLabel".equals(oExpression.strUiType)) {
								
							} else {

								pushRealTimeValue(strKey, oExpression);

							}
						} // end of else

						if (entry.getKey().updateValue()) {
							hasupdate = true; //
							m_oShareObject.m_listUpdateFromTcpValues.add(entry
									.getKey()); // 添加到需要更新UI列表 页面列表<更新的控件>
						}
					} /* end of while (iter.hasNext()) */
 
					m_oShareObject.processCmdCommands(mapCmds);

					m_oShareObject.processTriggerCommands(mapTriggers);

					m_oShareObject.processNamingCommands(mapNamings);

					if (hasupdate)
						m_oInvalidateHandler.sendEmptyMessage(0);

					Thread.sleep(200);
				} /* end of while (m_bIsRunning) */
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();

			}

			// will stop
			m_mapExpression.clear();
			m_mapCaculateValues.clear();
			m_oCalculator = null; 
			//System.out.println("SgExpressionXXX:结束");
			// local_data_list.clear();
		} /* end of run() */

		/** 是否使用随机数据 */
		public void setHasRandomData(boolean bHasRandom) {
			m_bHasRandomData = bHasRandom;
		}

		public String getRealTimeValueFromTcp(stBindingExpression bindingExpression) {

			if (m_bHasRandomData == true) {
				Random rand = new Random();
				return "1" + rand.nextInt(99) + 1;
			}

			if ("Value".equals(bindingExpression.strBindType)) {
				String value = DataGetter
						.getSignalMeaning(bindingExpression.nEquipId,
								bindingExpression.nSignalId);

				if (!value.isEmpty())
					return value;
			} else if ("EventSeverity".equals(bindingExpression.strBindType)) {

				return String.valueOf(DataGetter
						.getSignalSeverity(bindingExpression.nEquipId,
								bindingExpression.nSignalId));
			} else if ("State".equals(bindingExpression.strBindType)) {

				String str = DataGetter.getEquipState(
						bindingExpression.nEquipId, 10001);
				if (str == null)
					str = "0";

				return str;
			}

			return "-999999";
		}

		public HashMap<String, stExpression> m_mapExpression = null; // <IObject
																		// UniqueID,
																		// stExpression>
		HashMap<String, String> m_mapCaculateValues = null;
		public Calculator m_oCalculator = null;
		boolean m_bIsRunning = true;
		boolean m_bHasRandomData = false;
		
		public List<local_his_signal> local_data_list = new ArrayList<local_his_signal>();
		public List<local_his_signal> his_equipt_list = new ArrayList<local_his_signal>();
		public List<local_his_signal> his_signal_list = new ArrayList<local_his_signal>();
		public List<local_his_event> his_event_list = new ArrayList<local_his_event>();
	
	}
}