package com.sg.uis.newView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import com.mgrid.data.DataGetter;
import com.mgrid.main.MGridActivity;
import com.mgrid.main.MainWindow;
import com.mgrid.main.R;
import com.sg.common.CFGTLS;
import com.sg.common.IObject;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.method.ScrollingMovementMethod;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/** 自检 */
public class SelfCheck extends TextView implements IObject {

	private TextView textShow;
	private Button clickShow;
	private StringBuffer sb;
	private boolean isSuccess = true;
	private Spannable span;
	private List<Integer> equipNameLeangth = new ArrayList<Integer>();
	private List<Boolean> isSuccessList    = new ArrayList<Boolean>();

	
	
	public SelfCheck(Context context) {
		super(context);

		init(context);

	}

	private void init(Context context) {

		textShow = new TextView(context);
		textShow.setTextSize(15);
		textShow.setTextColor(Color.BLACK);
		textShow.setBackgroundResource(R.drawable.textbroad);
		textShow.setMovementMethod(ScrollingMovementMethod.getInstance());
		textShow.setText("准备就绪");

		clickShow = new Button(context);
		clickShow.setBackgroundResource(android.R.drawable.btn_default);
		clickShow.setPadding(0, 0, 0, 0);
		clickShow.setGravity(Gravity.CENTER);
		clickShow.setText("自检");
		clickShow.setTextSize(15);
		clickShow.setTextColor(Color.BLACK);

		clickShow.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
											
				clickShow.setEnabled(false);

				MGridActivity.xianChengChi.execute(new Runnable() {

					@Override
					public void run() {

						HashSet<String> ht_equiptID = DataGetter.getEquipmentIdList();
						sb = new StringBuffer();

						if (ht_equiptID == null || ht_equiptID.size() == 0) {

							handler.sendEmptyMessage(0);
							handler.sendEmptyMessage(2);
							return;
						}

						Iterator<String> iterator_Id = ht_equiptID.iterator();
						while (iterator_Id.hasNext()) {
							String id = iterator_Id.next();
							String value = DataGetter.getSignalValue(id, "10001");
							String name = DataGetter.getEquipmentName(id);
							equipNameLeangth.add(name.length() + 3);
							if (value == null) {

								sb.append(name + "自检:失败" + "\n");
								isSuccess = false;
								isSuccessList.add(false);

							} else {

								if (Float.parseFloat(value) == 0) {
									sb.append(name + "自检:失败" + "\n");
									isSuccess = false;
									isSuccessList.add(false);
								} else {
									sb.append(name + "自检:成功" + "\n");
									isSuccessList.add(true);
								}

							}
						}

						equipNameLeangth.add(5);
						if (isSuccess) {
							sb.append("自检结果:成功" + "\n");
							isSuccessList.add(true);
						} else {
							sb.append("自检结果:失败" + "\n");
							isSuccessList.add(false);
						}

						handler.sendEmptyMessage(1);
					}
				});

			}
		});

		m_rBBox = new Rect();
	}

	private Handler handler = new Handler() {

		public void handleMessage(android.os.Message msg) {

			switch (msg.what) {
			case 0:

				textShow.setText("自检失败，未找到相关数据");
				break;

			case 1:

				setMyText(sb.toString());

				break;

			case 2:

				isSuccess = true;
				clickShow.setEnabled(true);
				equipNameLeangth.clear();
				isSuccessList.clear();
				

				break;
			}

		};
	};

	// 延迟显示文字
	protected void setMyText(final String string) {

		MGridActivity.xianChengChi.execute(new Runnable() {

			@Override
			public void run() {
		
				span = new SpannableString(string);
				int n = 1;
				while (n <= string.length()) {
					span.setSpan(new ForegroundColorSpan(Color.parseColor("#000000")), n-1, n,
							Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

					int startIndex=0;
					for (int i = 0; i < equipNameLeangth.size(); i++) {

						startIndex += equipNameLeangth.get(i);
						boolean bool = isSuccessList.get(i);		
						
						if(startIndex+i*3>n)
						{
							break;
						}
						
						if(!(startIndex+2+i*3>=n&&startIndex+i*3<=n))
						{
							continue;
						}
						
						
						
						if (bool) {
							span.setSpan(new ForegroundColorSpan(Color.GREEN), startIndex+i*3, startIndex+2+i*3,
									Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
						} else {
							span.setSpan(new ForegroundColorSpan(Color.RED),   startIndex+i*3, startIndex+2+i*3,
									Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
						}

					}
					span.setSpan(new AbsoluteSizeSpan(20),string.length()-8,string.length(),Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
					span.setSpan(new ForegroundColorSpan(Color.parseColor("#00ffffff")), n, string.length(),
							Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
					textShow.post(new Runnable() {

						@Override
						public void run() {
							textShow.setText(span);
						}
					});

					try {
						String s = string.charAt(n - 1) + "";
						if (s.equals(":")) {
							Thread.sleep(1000);
						} else {
							Thread.sleep(100);
						}
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

					n++;
				}

				handler.sendEmptyMessage(2);
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
			textShow.layout(nX + nWidth / 10, nY + nHeight / 15, nX + nWidth * 9 / 10, nY + nHeight * 12 / 15);
			clickShow.layout(nX + nWidth * 2 / 10, nY + nHeight * 13 / 15, nX + nWidth * 8 / 10,
					nY + nHeight * 19 / 20);
		}
	}

	public void onDraw(Canvas canvas) {   

		if (m_rRenderWindow == null)
			return;
		if (m_rRenderWindow.isLayoutVisible(getBBox()) == false)
			return;
		super.onDraw(canvas);

		// 画边框
		Paint p = new Paint();
		p.setColor(Color.parseColor("#AAAAAA"));
		p.setStyle(Paint.Style.STROKE);
		canvas.drawRect(0, 0, m_rBBox.right - m_rBBox.left, m_rBBox.bottom - m_rBBox.top, p);

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
		rWin.addView(textShow);
		rWin.addView(clickShow);
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
		} else if ("RotateAngle".equals(strName)) {
			m_fRotateAngle = Float.parseFloat(strValue);
		} else if ("Content".equals(strName)) {
			m_strContent = strValue;
			c_Content = strValue;
			this.setText(m_strContent);
			// shimmerTv.setText(m_strContent);
		} else if ("FontFamily".equals(strName)) {
			m_strFontFamily = strValue;
			// this.setTypeface(MyApplication.typeface);
		} else if ("FontSize".equals(strName)) {
			float fWinScale = (float) MainWindow.SCREEN_WIDTH / (float) MainWindow.FORM_WIDTH;
			m_fFontSize = Float.parseFloat(strValue) * fWinScale;
			this.setTextSize(m_fFontSize);
			// shimmerTv.setTextSize(m_fFontSize);
		} else if ("IsBold".equals(strName))
			m_bIsBold = Boolean.parseBoolean(strValue);
		else if ("FontColor".equals(strName)) {
			m_cFontColor = Color.parseColor(strValue);
			m_cStartFillColor = m_cFontColor;
			this.setTextColor(m_cFontColor);
			// shimmerTv.setTextColor(m_cFontColor);
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
			double padSize = CFGTLS.getPadHeight(m_nHeight, MainWindow.FORM_HEIGHT, getTextSize()) / 2;
			setPadding(0, (int) padSize, 0, (int) padSize);
		}

		setGravity(nFlag);

	}

	public void updateWidget() {

		this.setTextColor(m_cFontColor);
		this.setText(m_strContent);
		this.invalidate();

	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public boolean updateValue() {

		return false;
	}

	@Override
	public String getBindingExpression() {
		// TODO Auto-generated method stub
		return null;
	}

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
	String c_Content = "";
	float m_fFontSize = 12.0f;
	boolean m_bIsBold = false;
	int m_cFontColor = 0xFF008000;
	int m_cStartFillColor = 0x00000000;
	String m_strHorizontalContentAlignment = "Center";
	String m_strVerticalContentAlignment = "Center";

	MainWindow m_rRenderWindow = null;

	Rect m_rBBox = null;

	public boolean m_bneedupdate = true;

}
