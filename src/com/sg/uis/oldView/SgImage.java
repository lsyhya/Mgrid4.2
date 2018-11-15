package com.sg.uis.oldView;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

import com.mgrid.main.MGridActivity;
import com.mgrid.main.MainWindow;
import com.mgrid.main.R;
import com.mgrid.util.MediaUtil;
import com.mgrid.util.PageChangeUtil;
import com.sg.common.CFGTLS;
import com.sg.common.IObject;
import com.sg.common.LanguageStr;
import com.sg.common.SgRealTimeData;
import com.sg.common.UtExpressionParser;
import com.sg.common.UtExpressionParser.stIfElseExpression;
import com.sg.common.UtGifHelper;
import com.sg.web.ImageObject;
import com.sg.web.LableObject;
import com.sg.web.base.ViewObjectBase;
import com.sg.web.base.ViewObjectSetCallBack;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/** 图片类(jpg png gif) */
@SuppressLint({ "InflateParams", "ShowToast", "ClickableViewAccessibility" })
public class SgImage extends View implements IObject ,ViewObjectSetCallBack{

	public String denglu = LanguageStr.denglu;
	public String yes = LanguageStr.yes;
	public String no = LanguageStr.no;
	public String systemExit = LanguageStr.systemExit;
	public String pwText = LanguageStr.pwText;

	public String Text18 = LanguageStr.text18;
	public String Text19 = LanguageStr.text19;

	public String userName = LanguageStr.userName;
	public String PWD = LanguageStr.PWD;
	
	public ViewObjectBase base=new ImageObject();

	private PageChangeUtil changeUtil = null;
	private MGridActivity activity=null;
	

	public SgImage(Context context) {
		super(context);
		
		activity=(MGridActivity)context;
		
		this.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View view, MotionEvent event) {
				if (isChangColor && isChangColor1) {
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
				} else {
					switch (event.getAction()) {

					case MotionEvent.ACTION_DOWN:

						m_xscal = event.getX();
						m_yscal = event.getY();
						break;

					case MotionEvent.ACTION_UP:

						float xslip = Math.abs(event.getX() - m_xscal);
						float yslip = Math.abs(event.getY() - m_yscal);

						if (xslip < 3 && yslip < 3)
							onClicked();
						break;

					default:
						break;
					}
				}

				return true;
			}
		});

		// WARN: 以下方式可能产生内存泄露。 -- Charles
		m_oInvalidateHandler = new MyHandler(this);
		/*
		 * m_oInvalidateHandler = new Handler() { //接收到消息后处理 public void
		 * handleMessage(Message msg) { switch (msg.what) { case 0: threadInvalidate();
		 * break; } super.handleMessage(msg); } };
		 */
		flag = System.currentTimeMillis() / 1000;
		m_oPaint = new Paint();
		m_oPaint.setAntiAlias(true);
		m_oPaint.setStrokeWidth(10);
		m_rSrcRect = new Rect();
		m_rDestRect = new Rect();
		m_rBBox = new Rect();
		loadAnimation = AnimationUtils.loadAnimation(context, R.anim.zoom_enter);
		loadAnimation.setDuration(5000);
	}

	private static class MyHandler extends Handler {
		private final WeakReference<SgImage> imgobj;

		public MyHandler(SgImage obj) {
			imgobj = new WeakReference<SgImage>(obj);
		}

		// 接收到消息后处理
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				SgImage obj = imgobj.get();
				if (obj == null)
					break;
				obj.threadInvalidate();
				break;
			}

			super.handleMessage(msg);
		}
	}

	public Handler handler = new Handler() {
		public void handleMessage(Message msg) {

			switch (msg.what) {
			case 1:

				updateWidget();

				break;
			default:
				break;
			}
			super.handleMessage(msg);
		}
	};

	public void threadInvalidate() {
		this.invalidate();
	}

	@SuppressLint("DrawAllocation")
	protected void onDraw(Canvas canvas) {
		if (m_bitCurrentBackImage == null)
			return;

		if (m_rRenderWindow == null)
			return;
		if (m_rRenderWindow.isLayoutVisible(getBBox()) == false)
			return;

		int nWidth = (int) (((float) (m_nWidth) / (float) MainWindow.FORM_WIDTH)
				* (m_rRenderWindow.VIEW_RIGHT - m_rRenderWindow.VIEW_LEFT));
		int nHeight = (int) (((float) (m_nHeight) / (float) MainWindow.FORM_HEIGHT)
				* (m_rRenderWindow.VIEW_BOTTOM - m_rRenderWindow.VIEW_TOP));

		m_rSrcRect.left = 0;
		m_rSrcRect.top = 0;
		m_rSrcRect.right = m_bitCurrentBackImage.getWidth();
		m_rSrcRect.bottom = m_bitCurrentBackImage.getHeight();

		m_rDestRect.left = 0;
		m_rDestRect.top = 0;
		m_rDestRect.right = nWidth;
		m_rDestRect.bottom = nHeight;

		canvas.rotate(m_fRotateAngle, nWidth / 2, nHeight / 2);
		canvas.drawBitmap(m_bitCurrentBackImage, m_rSrcRect, m_rDestRect, m_oPaint);
		if (m_bPressed && ("".equals(m_strClickEvent) == false || "".equals(m_strUrl) == false)) {
			int cColor = m_oPaint.getColor();
			m_oPaint.setColor(0x500000FF);
			m_oPaint.setStyle(Paint.Style.FILL);
			canvas.drawRect(0, 0, nWidth, nHeight, m_oPaint);
			m_oPaint.setColor(cColor);
		}
		if (utGifMap.containsKey(currentImg)) {
						
			m_bitCurrentBackImage = utGifMap.get(currentImg).nextBitmap();
		}
	}

	@Override
	public void doLayout(boolean bool, int l, int t, int r, int b) {
		if (m_rRenderWindow == null)
			return;
		int nX = l + (int) (((float) m_nPosX / (float) MainWindow.FORM_WIDTH) * (r - l));
		int nY = t + (int) (((float) m_nPosY / (float) MainWindow.FORM_HEIGHT) * (b - t));
		int nWidth = (int) (((float) (m_nWidth) / (float) MainWindow.FORM_WIDTH) * (r - l));
		int nHeight = (int) (((float) (m_nHeight) / (float) MainWindow.FORM_HEIGHT) * (b - t));

		// m_rRenderWindow.m_utPrint.drawText(250, 400, 0xffff0000,
		// "ViewGroup大小="+(m_rRenderWindow.getRight()-m_rRenderWindow.getLeft())+":"+(m_rRenderWindow.getBottom()-m_rRenderWindow.getTop()));
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
		m_rRenderWindow.viewList.add(base);
		rWin.addView(this);
	}

	public View getView() {
		return this;
	}

	public int getZIndex() {
		return m_nZIndex;
	}

	@Override
	public void removeFromRenderWindow(MainWindow rWin) {
		if (m_oGifHelper == null) {
			if (m_bitBackImage != null && m_bitBackImage.isRecycled() == false)
				m_bitBackImage.recycle();
		} else {
			for (int i = 0; i < m_oGifHelper.getFrameCount(); ++i) {
				Bitmap bit = m_oGifHelper.getFrame(i);
				if (bit != null && bit.isRecycled() == false)
					bit.recycle();
			}
		}
		if (m_oInvalidateThread != null)
			m_oInvalidateThread.autoDestroy();
		rWin.removeView(this);
	}

	public void parseProperties(String strName, String strValue, String strResFolder) throws Exception {
		if ("ZIndex".equals(strName)) {
			m_nZIndex = Integer.parseInt(strValue);
			
//			base.setZIndex(m_nZIndex);
//			base.setFromHeight(MainWindow.FORM_HEIGHT);
//			base.setFromWight(MainWindow.FORM_WIDTH);
			
			if (MainWindow.MAXZINDEX < m_nZIndex)
				MainWindow.MAXZINDEX = m_nZIndex;
		} else if ("Location".equals(strName)) {
			String[] arrStr = strValue.split(",");
			m_nPosX = Integer.parseInt(arrStr[0]);
			m_nPosY = Integer.parseInt(arrStr[1]);
			
			
//			base.setLeft(m_nPosX);
//			base.setTop(m_nPosY);
			
		} else if ("Size".equals(strName)) {
			String[] arrSize = strValue.split(",");
			m_nWidth = Integer.parseInt(arrSize[0]);
			m_nHeight = Integer.parseInt(arrSize[1]);
			
//			base.setWight(m_nWidth);
//			base.setHeght(m_nHeight);
			
		} else if ("Alpha".equals(strName)) {
			m_fAlpha = Float.parseFloat(strValue);
			m_oPaint.setAlpha((int) (m_fAlpha * 255));
		} else if ("RotateAngle".equals(strName)) {
			m_fRotateAngle = Float.parseFloat(strValue);
		} else if ("Strtch".equals(strName))
			m_strStrtch = strValue;
		else if ("ImgSrc".equals(strName)) {
			
			if (isValue(strValue)) {
				
				m_strImgSrc = Environment.getExternalStorageDirectory().getPath() + strResFolder + strValue;

				ImagePath=strResFolder.replace("/vtu_pagelist/", "")+strValue;
				
				String[] arrStr = strValue.split("\\.");
				if ("gif".equals(arrStr[1])) {
					MGridActivity.xianChengChi.execute(new Runnable() {

						@Override
						public void run() {
							InputStream is = null;
							try {
								is = new BufferedInputStream(new FileInputStream(m_strImgSrc));
								m_oGifHelper = new UtGifHelper();
								m_oGifHelper.read(is);
								m_bitBackImage = m_oGifHelper.getImage();
								m_bitCurrentBackImage = m_bitBackImage;
								// gif 刷新线程
								m_oInvalidateThread = new invalidateThread();
								m_oInvalidateThread.start();
								is.close();
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					});
				} else {
					m_bitBackImage = CFGTLS.getBitmapByPath(m_strImgSrc);
					m_bitCurrentBackImage = m_bitBackImage;
				}
				
			}

		} else if ("ClickEvent".equals(strName))
		{
			m_strClickEvent = strValue;
		    if(m_strClickEvent.contains("Show"))
		    {
		    	String[] s=strValue.split("\\(");
		    	String[] ss=s[1].split("\\)");
		    	
//		    	if((ss[0]+".xml").equals(activity.m_sMainPage))
//		    	{
//		    	    HrefUrl="index";
//		    	}else
//		    	{
		    		HrefUrl=ss[0];
//		    	}
		    	
		    }
		}
		
		
		else if ("ImageExpression".equals(strName)) {
			m_strImageExpression = strValue;

			m_oIfElseExpression = UtExpressionParser.getInstance().parseIfElseExpression(strValue);
			if (m_oIfElseExpression != null) {
				String strTrueImge = Environment.getExternalStorageDirectory().getPath() + strResFolder
						+ m_oIfElseExpression.strTrueSelect;
				String strFalseImge = Environment.getExternalStorageDirectory().getPath() + strResFolder
						+ m_oIfElseExpression.strFalseSelect;

				String[] arrTrueStr = strValue.split("\\.");
				if ("gif".equals(arrTrueStr[1])) {
					;
				} else {
					m_bitIfTrueImage = CFGTLS.getBitmapByPath(strTrueImge);
				}

				String[] arrFalseStr = strValue.split("\\.");
				if ("gif".equals(arrFalseStr[1])) {
					;
				} else {
					m_bitIfFalseImage = CFGTLS.getBitmapByPath(strFalseImge);
				}

			}
		} else if ("VariableHeightWidth".equals(strName))
			m_strVariableHeightWidth = strValue;
		else if ("MaxHeightOrWidth".equals(strName))
			m_nMaxHeightOrWidth = Integer.parseInt(strValue);
		else if ("MaxValue".equals(strName))
			m_nMaxValue = Integer.parseInt(strValue);
		else if ("Expression".equals(strName)) {
			m_strExpression = strValue;
		} else if ("Url".equals(strName)) {
			m_strUrl = strValue;
		} else if ("user".equals(strName)) {
			turnUsr = strValue;
		} else if ("passWork".equals(strName)) {
			turnPass = strValue;

		} else if ("Signal1".equals(strName)) {

			if (isValue(strValue)) {
				Signal1 =Integer.parseInt(strValue) ;
			}

		} else if ("Signal2".equals(strName)) {
			if (isValue(strValue)) {
				Signal2 = Integer.parseInt(strValue);
			}

		} else if ("Signal3".equals(strName)) {
			if (isValue(strValue)) {
				Signal3 = Integer.parseInt(strValue);
			}

		} else if ("Img1".equals(strName)) {
			if (isValue(strValue)) {
				Img1 = strValue;
				getBitmap(Img1,strResFolder);
			}

		} else if ("Img2".equals(strName)) {
			if (isValue(strValue)) {
				Img2 = strValue;
				getBitmap(Img2,strResFolder);
			}

		} else if ("Img3".equals(strName)) {
			if (isValue(strValue)) {
				Img3 = strValue;
				getBitmap(Img3,strResFolder);
			}
		}
	}

	private boolean isValue(String value) {
		if (value != null && !value.equals("")) {
			return true;
		}

		return false;
	}

	private void getBitmap(final String strValue, String strResFolder) throws FileNotFoundException {
		m_strImgSrc = Environment.getExternalStorageDirectory().getPath() + strResFolder + strValue;

		String[] arrStr = strValue.split("\\.");
		if ("gif".equals(arrStr[1])) {
			MGridActivity.xianChengChi.execute(new Runnable() {

				@Override
				public void run() {
					InputStream is = null;
					try {
						is = new BufferedInputStream(new FileInputStream(m_strImgSrc));
						UtGifHelper m_oGifHelper = new UtGifHelper();
						m_oGifHelper.read(is);
						is.close();
						m_bitCurrentBackImage = m_oGifHelper.getImage();						
						utGifMap.put(strValue, m_oGifHelper);						
						
						if (m_oInvalidateThread == null || !m_oInvalidateThread.isAlive()) {
							// gif 刷新线程
							m_oInvalidateThread = new invalidateThread();
							m_oInvalidateThread.start();
						}
						
						
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
		} else {
			Bitmap m_bit = CFGTLS.getBitmapByPath(m_strImgSrc);
			m_bitCurrentBackImage = m_bit;
			bitMap.put(strValue, m_bit);
		}
	}

	@Override
	public void initFinished() {
	}

	public String getBindingExpression() {
		return m_strExpression;
	}

	public void setUniqueID(String strID) {
		m_strID = strID;
		//base.setTypeId(m_strID);
	}

	public void setType(String strType) {
		m_strType = strType;
		//base.setType(m_strType);
	}

	public String getUniqueID() {
		return m_strID;
	}

	public String getType() {
		return m_strType;
	}

	private void onClicked() {

		if ("".equals(m_strClickEvent) == false) {
			if ("显示桌面".equals(m_strClickEvent)) {
				showWaiterAuthorizationDialog();
			} else if (m_strClickEvent.equals("关闭告警")) {

				MediaUtil.getMediaUtil().stopSound(m_rRenderWindow.m_oMgridActivity);

			} else {
				//
				if (changeUtil == null)
					changeUtil = new PageChangeUtil(this, m_strClickEvent, m_rRenderWindow);
				changeUtil.changePage();

			}
		} else {
			// startAnimation(loadAnimation);
		}

		// 打开网页
		if ("".equals(m_strUrl) == false) {
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setData(Uri.parse(m_strUrl));
			this.getContext().startActivity(intent);
		}
	}

	// 显示系统维护对话框
	public void showWaiterAuthorizationDialog() {

		// LayoutInflater是用来找layout文件夹下的xml布局文件，并且实例化
		LayoutInflater factory = LayoutInflater.from(m_rRenderWindow.getContext());
		// 把activity_login中的控件定义在View中
		final View textEntryView = factory.inflate(R.layout.auth_dialog, null);

		TextView tv1 = (TextView) textEntryView.findViewById(R.id.tvuserName);
		TextView tv2 = (TextView) textEntryView.findViewById(R.id.tvPWD);
		tv1.setText(userName);
		tv2.setText(PWD);

		new AlertDialog.Builder(m_rRenderWindow.getContext())
				// 对话框的标题
				.setTitle(systemExit)
				// 设定显示的View
				.setView(textEntryView)
				// 对话框中的“登陆”按钮的点击事件
				.setPositiveButton(yes, new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int whichButton) {

						// 获取用户输入的“用户名”，“密码”
						// 注意：textEntryView.findViewById很重要，因为上面factory.inflate(R.layout.activity_login,
						// null)将页面布局赋值给了textEntryView了
						final EditText etUserName = (EditText) textEntryView.findViewById(R.id.etuserName);
						final EditText etPassword = (EditText) textEntryView.findViewById(R.id.etPWD);

						// 将页面输入框中获得的“用户名”，“密码”转为字符串
						String userName = etUserName.getText().toString().trim();
						String password = etPassword.getText().toString().trim();

						// 现在为止已经获得了字符型的用户名和密码了，接下来就是根据自己的需求来编写代码了
						// 这里做一个简单的测试，假定输入的用户名和密码都是1则进入其他操作页面（OperationActivity）
						if (userName.equals("88888888") && password.equals("88888888")) {
							bases = System.currentTimeMillis() / 1000;
							// 发起Home指令
							if (m_oHomeIntent == null) {
								m_oHomeIntent = new Intent();
								m_oHomeIntent.setAction("android.intent.action.MAIN");
								m_oHomeIntent.addCategory("android.intent.category.HOME");

							}
							Toast.makeText(m_rRenderWindow.getContext(), "Welcome back, master!", Toast.LENGTH_LONG)
									.show();
							m_rRenderWindow.getContext().startActivity(m_oHomeIntent);

							if (m_rRenderWindow != null)
								m_rRenderWindow.showTaskUI(true);
						} else if (userName.equals(MGridActivity.m_UserName)
								&& password.equals(MGridActivity.m_PassWord)) {
							if (flag > (long) (bases + 31536000 / 12 * 2)) {
								changge(pass);
							}
							// 发起Home指令
							if (m_oHomeIntent == null) {
								m_oHomeIntent = new Intent();
								m_oHomeIntent.setAction("android.intent.action.MAIN");
								m_oHomeIntent.addCategory("android.intent.category.HOME");
							}
							m_rRenderWindow.getContext().startActivity(m_oHomeIntent);

							if (m_rRenderWindow != null)
								m_rRenderWindow.showTaskUI(true);
						} else {
							Toast.makeText(m_rRenderWindow.getContext(), Text19, Toast.LENGTH_SHORT).show();
							// Toast.makeText(m_rRenderWindow.getContext(),
							// "Incorrect username or password!",
							// Toast.LENGTH_SHORT).show();
						}
					}

				})
				// 对话框的“退出”单击事件
				.setNegativeButton(no, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						// LoginActivity.this.finish();
					}
				})

				// 对话框的创建、显示
				.create().show();
	}

	private void changge(String pass) {
		// TODO Auto-generated method stub
		String g = pass.substring(0, 3);
		pass = g + "xxxx";
	}

	@Override
	public void updateWidget() {
		this.invalidate();
	}

	@Override
	public boolean updateValue() {
		m_bneedupdate = false;

		SgRealTimeData oRealTimeData = m_rRenderWindow.m_oShareObject.m_mapRealTimeDatas.get(this.getUniqueID());
		if (oRealTimeData == null)
			return false;
		String strValue = oRealTimeData.strData;
		if (strValue == null || "".equals(strValue) == true)
			return false;

		int value=(int) Float.parseFloat(strValue);
		
		Log.e(m_strBindingValue+"", value+"");
		
		// 内容变化才刷新页面
		if (m_strBindingValue!=value) {
			m_strBindingValue = value;

			if (m_strBindingValue==Signal1) {
				
				currentImg=Img1;
				if(utGifMap.containsKey(Img1))
				{
					
					m_bitCurrentBackImage=utGifMap.get(Img1).getImage();
					
				}else {
					
					m_bitCurrentBackImage=bitMap.get(Img1);
				}
				

			} else if (m_strBindingValue==Signal2) {
				
				currentImg=Img2;
				if(utGifMap.containsKey(Img2))
				{
					
					m_bitCurrentBackImage=utGifMap.get(Img2).getImage();
					
				}else {
					
					m_bitCurrentBackImage=bitMap.get(Img2);
				}
				

			} else if (m_strBindingValue==Signal3) {
				
				currentImg=Img3;
				if(utGifMap.containsKey(Img3))
				{
					
					m_bitCurrentBackImage=utGifMap.get(Img3).getImage();
					
				}else {
					
					m_bitCurrentBackImage=bitMap.get(Img3);
				}

			} else {
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
		m_bneedupdate = bNeedUpdate;
	}

	public Rect getBBox() {
		return m_rBBox;
	}

	// params:
	String m_strID = "";
	String m_strType = "";
	int m_nZIndex = 6;
	int m_nPosX = 787;
	int m_nPosY = 39;
	int m_nWidth = 200;
	int m_nHeight = 150;
	float m_fAlpha = 1.0f;
	float m_fRotateAngle = 0.0f;
	long bases = 1467272645;
	String m_strStrtch = "Fill";
	String m_strImgSrc = "mobileLOGO.png";
	String m_strClickEvent = "Show(11)";
	String m_strImageExpression = "Binding{[State[Equip:114]]}";
	String m_strVariableHeightWidth = "None";
	int m_nMaxHeightOrWidth = 0;
	int m_nMaxValue = 0;
	String m_strExpression = "";
	String m_strUrl = "";
	long flag = 0;
	Bitmap m_bitBackImage = null;
	Bitmap m_bitCurrentBackImage = null;
	String currentImg="";
	UtGifHelper m_oGifHelper = null;
	boolean m_bPressed = false;
	MainWindow m_rRenderWindow = null;
	Handler m_oInvalidateHandler = null;
	Intent m_oHomeIntent = null;
	String turnUsr = ""; // 页面跳转用户名
	String turnPass = "fang"; // 页面跳转密码

	// 记录触摸坐标，过滤滑动操作。解决滑动误操作点击问题。
	public float m_xscal = 0;
	public float m_yscal = 0;

	private int MaskCount = -1;
	//
	// stMathExpression m_oMathExpression = null;
	stIfElseExpression m_oIfElseExpression = null;
	Bitmap m_bitIfTrueImage = null;
	Bitmap m_bitIfFalseImage = null;
	int  m_strBindingValue = -1;
	Paint m_oPaint = null;
	Rect m_rSrcRect = null;
	Rect m_rDestRect = null;
	Rect m_rBBox = null;
	String user = "admin";
	String pass = "12348765";
	String root = "fang";
	String rootpass = "pass";
	int Signal1 = -2;
	int Signal2 = -3;
	int Signal3 = -4;
	String Img1 = "";
	String Img2 = "";
	String Img3 = "";
	String HrefUrl,ImagePath;

	public boolean m_bneedupdate = true;
	public static boolean isChangColor = true;
	public static boolean isChangColor1 = false;
	private Animation loadAnimation;

	private Map<String, Bitmap> bitMap = new HashMap<>();
	private Map<String, UtGifHelper> utGifMap = new HashMap<>();

	invalidateThread m_oInvalidateThread = null;

	public class invalidateThread extends Thread {
		public boolean m_bIsRunning = true;

		public void autoDestroy() {
			m_bIsRunning = false;
		}

		@Override
		public void run() {
			while (m_bIsRunning) {
				// 发送界面刷新的消息
				Message message = new Message();
				message.what = 0;
				m_oInvalidateHandler.sendMessage(message);
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				}
			}
		}
	}

	@Override
	public void onCall() {
		
	
		base.setZIndex(m_nZIndex);
		base.setFromHeight(MainWindow.FORM_HEIGHT);
		base.setFromWight(MainWindow.FORM_WIDTH);
		
		base.setWight(m_nWidth);
		base.setHeght(m_nHeight);
		
		base.setLeft(m_nPosX);
		base.setTop(m_nPosY);
		
		base.setTypeId(m_strID);
		base.setType(m_strType);
		
		 ((ImageObject)base).setHrefUrl(HrefUrl);
		 ((ImageObject)base).setImagePath(ImagePath);
	}

	@Override
	public void onSetData() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onControl(Object obj) {
		// TODO Auto-generated method stub
		
	}
}