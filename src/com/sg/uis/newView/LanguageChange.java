package com.sg.uis.newView;

import java.io.File;
import java.util.ArrayList;

import com.mgrid.main.MGridActivity;
import com.mgrid.main.MainWindow;
import com.mgrid.main.R;
import com.mgrid.util.DialogUtils;
import com.mgrid.util.FileUtil;
import com.sg.common.CFGTLS;
import com.sg.common.IObject;
import com.sg.common.LanguageStr;
import com.sg.common.MyAdapter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Environment;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

/** 按钮 */
@SuppressLint({ "ShowToast", "InflateParams", "RtlHardcoded", "ClickableViewAccessibility" })
public class LanguageChange extends TextView implements IObject {

	public static int index = -1;// 0-中文 1-English

	private TextView tvSelectLan = null;
	private Button btnSetUp = null;

	private String language = "语言/Language ↓";

	private PopupWindow popupWindow = null;
	private MyAdapter adapter = null;
	private ArrayList<String> list = new ArrayList<String>();

	private FileUtil util = null;

	private static final String mainPath = Environment.getExternalStorageDirectory().getPath();

	private static final String chinaFile = "vtu_config-Chinese";
	private static final String engFile = "vtu_config-English";

	private static final String soPath = "/data/mgrid/sampler";

	private static final String ini_File = "MGrid.ini";
	private static final String sam_Dir = "sampler";
	private static final String vtu_Dir = "vtu_pagelist";

	private static final String so_Dir = "SO";
	private static final String xml_Dir = "XmlCfg";

	private static final String tempPath = "tmp/reboot.txt";

	private String set = LanguageStr.set;

	private String title = LanguageStr.title;

	private String content = LanguageStr.content;

	private String fail = LanguageStr.Fail;
	
	private String text = LanguageStr.text20;

	public LanguageChange(Context context) {
		super(context);

		m_oPaint = new Paint();
		m_rBBox = new Rect();

		adapter = new MyAdapter(context, list);
		tvSelectLan = new TextView(context);
		btnSetUp = new Button(context);

		util = new FileUtil();

		init();
	}

	private void init() {

		tvSelectLan.setBackgroundColor(Color.argb(100, 100, 100, 100));
		tvSelectLan.setPadding(0, 5, 0, 0);
		tvSelectLan.setText(language);
		tvSelectLan.setTextSize(15);
		tvSelectLan.setTextColor(Color.BLACK);
		tvSelectLan.setGravity(Gravity.CENTER);
		list.add("中文");
		list.add("English");

		btnSetUp.setBackgroundResource(android.R.drawable.btn_default);
		btnSetUp.setText(set);
		btnSetUp.setTextSize(15);
		btnSetUp.setTextColor(Color.BLACK);
		btnSetUp.setGravity(Gravity.CENTER);

		tvSelectLan.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				View view = m_rRenderWindow.m_oMgridActivity.getLayoutInflater().inflate(R.layout.pop, null);
				popupWindow = new PopupWindow(view, tvSelectLan.getWidth(), 100);
				// 设置一个透明的背景，不然无法实现点击弹框外，弹框消失
				popupWindow.setBackgroundDrawable(new BitmapDrawable());

				// 设置点击弹框外部，弹框消失
				popupWindow.setOutsideTouchable(true);
				popupWindow.setFocusable(true);
				popupWindow.showAsDropDown(tvSelectLan);

				ListView lv = (ListView) view.findViewById(R.id.lv_list);

				lv.setAdapter(adapter);
				lv.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
						tvSelectLan.setText(list.get(position));
						index = position;
						popupWindow.dismiss();
					}
				});

			}
		});

		btnSetUp.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (index != -1)
					btnSetUp.setEnabled(false);
				copyFile();

			}
		});
	}

	private void copyFile() {
		MGridActivity.xianChengChi.execute(new Runnable() {

			@Override
			public void run() {

				File file_china = new File(mainPath + "/" + chinaFile);
				File file_english = new File(mainPath + "/" + engFile);

				if (file_china.exists() && file_english.exists()) {

					if (index == 0) {

						System.out.println("替换中文");

						util.deleteDir(new File(mainPath + "/" + ini_File));
						util.deleteDir(new File(mainPath + "/" + vtu_Dir));
						util.deleteDir(new File(soPath + "/" + so_Dir));
						util.deleteDir(new File(soPath + "/" + xml_Dir));

						util.copyFileno(mainPath + "/" + chinaFile + "/" + ini_File, mainPath + "/" + ini_File);
						util.copyFolder(mainPath + "/" + chinaFile + "/" + vtu_Dir, mainPath + "/" + vtu_Dir);
						util.copyFolder(mainPath + "/" + chinaFile + "/" + sam_Dir + "/" + so_Dir,
								soPath + "/" + so_Dir);
						util.copyFolder(mainPath + "/" + chinaFile + "/" + sam_Dir + "/" + xml_Dir,
								soPath + "/" + xml_Dir);

					} else if (index == 1) {
						System.out.println("替换英文");

						util.deleteDir(new File(mainPath + "/" + ini_File));
						util.deleteDir(new File(mainPath + "/" + vtu_Dir));
						util.deleteDir(new File(soPath + "/" + so_Dir));
						util.deleteDir(new File(soPath + "/" + xml_Dir));

						util.copyFileno(mainPath + "/" + engFile + "/" + ini_File, mainPath + "/" + ini_File);
						util.copyFolder(mainPath + "/" + engFile + "/" + vtu_Dir, mainPath + "/" + vtu_Dir);
						util.copyFolder(mainPath + "/" + engFile + "/" + sam_Dir + "/" + so_Dir, soPath + "/" + so_Dir);
						util.copyFolder(mainPath + "/" + engFile + "/" + sam_Dir + "/" + xml_Dir,
								soPath + "/" + xml_Dir);

					} else {
						hand.sendEmptyMessage(1);
						return;
					}
					util.deleteDir(new File(mainPath + "/" + tempPath));
					hand.sendEmptyMessage(0);
				}else
				{
					hand.sendEmptyMessage(2);
					//btnSetUp.setEnabled(true);
				}
			}
		});
	}

	private Handler hand = new Handler() {

		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:

				DialogUtils.getDialog().showDialog(getContext(), title, content);

				break;
			case 1:

				Toast.makeText(getContext(), fail, 1000).show();

				break;
			case 2:

				btnSetUp.setEnabled(true);
				Toast.makeText(getContext(), text, 1000).show();

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

			tvSelectLan.layout(nX, nY, nX + (int) (0.69 * nWidth), (int) (nY + 0.9 * nHeight));
			btnSetUp.layout((int) (nX + 0.7 * nWidth), nY, nX + nWidth, nY + nHeight);

		}
	}

	@Override
	public void addToRenderWindow(MainWindow rWin) {
		m_rRenderWindow = rWin;
		rWin.addView(tvSelectLan);
		rWin.addView(btnSetUp);
	}

	@Override
	public void removeFromRenderWindow(MainWindow rWin) {
		rWin.removeView(this);
	}

	public void parseProperties(String strName, String strValue, String strResFolder) {
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

		} else if ("Content".equals(strName)) {
			m_strContent = strValue;
			// btnSetUp.setText(m_strContent);

		} else if ("FontFamily".equals(strName))
			m_strFontFamily = strValue;
		else if ("FontSize".equals(strName)) {
			float fWinScale = (float) MainWindow.SCREEN_WIDTH / (float) MainWindow.FORM_WIDTH;
			m_fFontSize = Float.parseFloat(strValue) * fWinScale;

		} else if ("IsBold".equals(strName))
			m_bIsBold = Boolean.parseBoolean(strValue);
		else if ("FontColor".equals(strName)) {
			m_cFontColor = Color.parseColor(strValue);
			// this.setTextColor(m_cFontColor);
		} else if ("HorizontalContentAlignment".equals(strName))
			m_strHorizontalContentAlignment = strValue;
		else if ("VerticalContentAlignment".equals(strName))
			m_strVerticalContentAlignment = strValue;

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

	String m_strUrl = "www.baidu.com";
	String m_strHorizontalContentAlignment = "Center";
	String m_strVerticalContentAlignment = "Center";

	MainWindow m_rRenderWindow = null;

	Paint m_oPaint = null;
	Rect m_rBBox = null;

	public boolean m_bneedupdate = true;
	private String mExpression = "";
}
