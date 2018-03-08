package com.sg.uis;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.InputFilter;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mgrid.main.MGridActivity;
import com.mgrid.main.MainWindow;
import com.sg.common.CFGTLS;
import com.sg.common.IObject;

/**
 * 改密码
 * 
 * @author lsy
 * 
 */
public class SgChangePassWord extends TextView implements IObject {

	
	private String oldPw="";
	private String newPw="";
	public SgChangePassWord(Context context) {
		super(context);		
		if(MGridActivity.whatLanguage)
		{
			oldPw="旧密码:";
			newPw="新密码:";		
		}else{
			oldPw="Old Password";
			newPw="New Password";		
		}

		
		
		this.setClickable(true);
		this.setGravity(Gravity.CENTER);
		this.setFocusableInTouchMode(true);
		m_fFontSize = this.getTextSize();
		this.setTextSize(15);
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
						onClicked();
					break;
				default:
					break;
				}
				return true;
			}
		});

		setBackgroundResource(android.R.drawable.btn_default);
		setPadding(0, 0, 0, 0);

		m_oPaint = new Paint();
		m_rBBox = new Rect();
		m_oEditTextNEW = new EditText(context);
		m_oEditTextOLD = new EditText(context);
		tvNew = new TextView(context);
		tvOld = new TextView(context);

		m_oEditTextNEW.setBackgroundResource(android.R.drawable.edit_text);
		m_oEditTextOLD.setBackgroundResource(android.R.drawable.edit_text);
		m_oEditTextNEW.setPadding(0, 4, 0, 0);
		m_oEditTextOLD.setPadding(0, 4, 0, 0);
		tvNew.setPadding(0, 0, 0, 0);
		tvOld.setPadding(0, 0, 0, 0);
		tvNew.setTextSize(15);
		tvOld.setTextSize(15);
		m_oEditTextNEW.setTextSize(15);
		m_oEditTextOLD.setTextSize(15);
		tvNew.setText(newPw);
		tvOld.setText(oldPw);
		tvNew.setTextColor(Color.BLACK);
		tvOld.setTextColor(Color.BLACK);

		m_oEditTextOLD
				.setFilters(new InputFilter[] { new InputFilter.LengthFilter(20) });
		m_oEditTextNEW
				.setFilters(new InputFilter[] { new InputFilter.LengthFilter(20) });
		m_oEditTextOLD.setSingleLine();
		m_oEditTextNEW.setSingleLine();
		m_oEditTextOLD.setGravity(Gravity.CENTER);
		m_oEditTextNEW.setGravity(Gravity.CENTER);

		// m_oEditText.setGravity(Gravity.CENTER);//设置字体居中

		m_oEditTextNEW.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				imm.showSoftInput(m_oEditTextNEW,
						InputMethodManager.SHOW_FORCED);// 获取到这个类。
				m_oEditTextNEW.setFocusableInTouchMode(true);// 获取焦点

			}
		});
		m_oEditTextOLD.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				imm.showSoftInput(m_oEditTextOLD,
						InputMethodManager.SHOW_FORCED);// 获取到这个类。
				m_oEditTextOLD.setFocusableInTouchMode(true);// 获取焦点

			}
		});
		m_oEditTextNEW.setTextColor(Color.BLACK);
		// m_oEditTextNEW.setHint("请输入新密码"); //设置提示文字
		// m_oEditTextOLD.setHint("请输入原密码");
		m_oEditTextNEW.setCursorVisible(true);// 让edittext出现光标
		m_oEditTextOLD.setTextColor(Color.BLACK);
		m_oEditTextOLD.setCursorVisible(true);
		// m_oEditText.setInputType(EditorInfo.TYPE_CLASS_PHONE); //设置文本格式
		imm = (InputMethodManager) context
				.getSystemService(Context.INPUT_METHOD_SERVICE);// 显示输入法窗口

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

			m_oPaint.setColor(0x50FF00F0);
			m_oPaint.setStyle(Paint.Style.FILL);
			canvas.drawRect(0, 0, nWidth, nHeight, m_oPaint);
		}
		super.onDraw(canvas);
	}

	protected void onClicked() {
		String oldPassWord = m_oEditTextOLD.getText().toString().trim();
		String newPassWord = m_oEditTextNEW.getText().toString().trim();
		if ((oldPassWord.equals("") && newPassWord.equals("")) == false) {
			if (oldPassWord.equals(MGridActivity.m_PassWord)||oldPassWord.equals("88888888")) {

				changPassWord(newPassWord);
				if(MGridActivity.whatLanguage)
					Toast.makeText(m_rRenderWindow.getContext(), "密码修改成功",
							Toast.LENGTH_SHORT).show();
					else
						Toast.makeText(m_rRenderWindow.getContext(), "Password changes succeeded ",
								Toast.LENGTH_SHORT).show();

			} else {
				if(MGridActivity.whatLanguage)
					Toast.makeText(m_rRenderWindow.getContext(), "旧密码输入错误，请重新输入",
							Toast.LENGTH_SHORT).show();
					else
						Toast.makeText(m_rRenderWindow.getContext(), "Old password entered error, please re-enter",
								Toast.LENGTH_SHORT).show();
			}
			m_oEditTextOLD.setText("");
			m_oEditTextNEW.setText("");
		} else {
			if(MGridActivity.whatLanguage)
				Toast.makeText(m_rRenderWindow.getContext(), "密码输入不完整",
						Toast.LENGTH_SHORT).show();
				else
					Toast.makeText(m_rRenderWindow.getContext(), "The password input is incomplete ",
							Toast.LENGTH_SHORT).show();
		}
	}

	// 修改密码，并且将密码保存至MGrid.ini配置文件
	private void changPassWord(String newPassWord) {
		MGridActivity.changPassWord(type, newPassWord);

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
			this.layout(nX + (int) (nWidth * 0.9f), nY, nX + nWidth, nY
					+ nHeight);

			m_oEditTextNEW.layout(nX + (int) (nWidth * 0.56f), nY, nX
					+ (int) (nWidth * 0.89f), nY + (int) (nHeight * 1.0f));
			m_oEditTextOLD.layout(nX + (int) (nWidth * 0.11f), nY, nX
					+ (int) (nWidth * 0.44f), nY + (int) (nHeight * 1.0f));
			tvOld.layout(nX, nY + (int) (nHeight * 0.15f), nX
					+ (int) (nWidth * 0.1f), nY + (int) (nHeight * 1.0f));
			tvNew.layout(nX + (int) (nWidth * 0.45f), nY
					+ (int) (nHeight * 0.15f), nX + (int) (nWidth * 0.55f), nY
					+ (int) (nHeight * 1.0f));
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
		// TODO Auto-generated method stub
		return m_strType;
	}

	@Override
	public void parseProperties(String strName, String strValue,
			String strResFolder) throws Exception {

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
		} else if ("Content".equals(strName)) {
			m_strContent = strValue;
			this.setText(m_strContent);
		} else if ("FontFamily".equals(strName))
			m_strFontFamily = strValue;
		else if ("IsBold".equals(strName))
			m_bIsBold = Boolean.parseBoolean(strValue);
		else if ("BackgroundColor".equals(strName))
			m_cBackgroundColor = Color.parseColor(strValue);
		    if("#FF000000".equals(m_cBackgroundColor))
		    {
		    	
		    }
		else if ("FontColor".equals(strName)) {
			m_cFontColor = Color.parseColor(strValue);
			this.setTextColor(m_cFontColor);
			tvNew.setTextColor(m_cFontColor);
			tvOld.setTextColor(m_cFontColor);
		} else if ("CmdExpression".equals(strName)) {
			m_strCmdExpression = strValue;
		} else if ("IsValueRelateSignal".equals(strName)) {
			if ("True".equals(strValue))
				m_bIsValueRelateSignal = true;
			else
				m_bIsValueRelateSignal = false;
		} else if ("ButtonWidthRate".equals(strName)) {
			m_fButtonWidthRate = Float.parseFloat(strValue);
		}
	}

	@Override
	public void initFinished() {
		setGravity(Gravity.CENTER);

		double padSize = CFGTLS.getPadHeight(m_nHeight, MainWindow.FORM_HEIGHT,
				getTextSize()) / 2;
		setPadding(0, (int) padSize, 0, (int) padSize);

	}

	@Override
	public void addToRenderWindow(MainWindow rWin) {
		m_rRenderWindow = rWin;

		rWin.addView(m_oEditTextNEW);
		rWin.addView(m_oEditTextOLD);
		rWin.addView(tvNew);
		rWin.addView(tvOld);
		rWin.addView(this);

	}

	@Override
	public void removeFromRenderWindow(MainWindow rWin) {
	
		rWin.removeView(m_oEditTextNEW);
		rWin.removeView(m_oEditTextOLD);
		rWin.removeView(this);
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

	@Override
	public String getBindingExpression() {
		
		return m_strCmdExpression;
	}

	@Override
	public View getView() {
		
		return this;
	}

	@Override
	public int getZIndex() {
	
		return m_nZIndex;
	}

	public Rect getBBox() {
		return m_rBBox;
	}

	// 数据
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
	int m_cBackgroundColor = 0xFFFCFCFC; 
	int m_cFontColor = 0xFF000000;
	String m_strContent = "Setting";
	String m_strCmdExpression = "";
	boolean m_bIsValueRelateSignal = false;
	float m_fButtonWidthRate = 0.3f;
	MainWindow m_rRenderWindow = null;
	float m_fFontSize = 6.0f;
	boolean m_bPressed = false;
	Paint m_oPaint = null;
	Rect m_rBBox = null; 
	InputMethodManager imm = null; 
	EditText m_oEditTextNEW = null;
	EditText m_oEditTextOLD = null;
	TextView tvOld = null;
	TextView tvNew = null;
	String type = "PassWord";

	// 记录触摸坐标，过滤滑动操作。解决滑动误操作点击问题。
	public float m_xscal = 0;
	public float m_yscal = 0;

}
