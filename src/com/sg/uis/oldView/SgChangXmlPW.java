package com.sg.uis.oldView;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mgrid.main.MGridActivity;
import com.mgrid.main.MainWindow;
import com.mgrid.main.R;
import com.sg.common.CFGTLS;
import com.sg.common.IObject;
import com.sg.common.LanguageStr;
import com.sg.web.SgChangXmlPWObject;
import com.sg.web.base.ViewObjectBase;
import com.sg.web.base.ViewObjectSetCallBack;
import com.sg.web.utils.ViewObjectColorUtil;

/**
 * 改密码
 * 
 * @author lsy
 *
 */
public class SgChangXmlPW extends TextView implements IObject,ViewObjectSetCallBack{

	private String oldPw = LanguageStr.oldPw;
	private String newPw = LanguageStr.newPw;
	private String confirm =LanguageStr.confirm;

	private String text11=LanguageStr.text11, text12=LanguageStr.text12, text13=LanguageStr.text13, text14=LanguageStr.text14, text15=LanguageStr.text15, text16=LanguageStr.text16, text17=LanguageStr.text17;

	public SgChangXmlPW(Context context) {
		super(context);
		this.setClickable(true);
		// this.setGravity(Gravity.CENTER);
		this.setFocusableInTouchMode(true);
		m_fFontSize = this.getTextSize();



		setBackgroundResource(android.R.drawable.btn_default);
		setPadding(0, 0, 0, 0);

		MakeBtn = new Button(context);

		m_oPaint = new Paint();
		m_rBBox = new Rect();
		m_oEditTextNEW = new EditText(context);
		m_oEditTextOLD = new EditText(context);
		E_CPassword = new EditText(context);

		tvNew = new TextView(context);
		tvOld = new TextView(context);
		T_CPassword = new TextView(context);

		m_oEditTextNEW.setBackgroundResource(android.R.drawable.edit_text);
		m_oEditTextOLD.setBackgroundResource(android.R.drawable.edit_text);
		E_CPassword.setBackgroundResource(android.R.drawable.edit_text);

		MakeBtn.setBackgroundResource(android.R.drawable.btn_default_small);

		m_oEditTextNEW.setPadding(0, 2, 0, 0);
		m_oEditTextOLD.setPadding(0, 2, 0, 0);
		E_CPassword.setPadding(0, 2, 0, 0);

		tvNew.setPadding(0, 0, 0, 0);
		T_CPassword.setPadding(0, 0, 0, 0);
		tvOld.setPadding(0, 0, 0, 0);
		MakeBtn.setPadding(0, 0, 0, 0);

		tvNew.setTextSize(15);
		T_CPassword.setTextSize(15);
		tvOld.setTextSize(15);
		m_oEditTextNEW.setTextSize(15);
		m_oEditTextOLD.setTextSize(15);
		E_CPassword.setTextSize(15);

		MakeBtn.setTextSize(15);

		tvNew.setText(newPw);
		T_CPassword.setText(confirm);
		tvOld.setText(oldPw);

		MakeBtn.setTextColor(Color.BLACK);
		tvNew.setTextColor(Color.BLACK);
		T_CPassword.setTextColor(Color.BLACK);
		tvOld.setTextColor(Color.BLACK);

		m_oEditTextOLD.setFilters(new InputFilter[] { new InputFilter.LengthFilter(20) });
		m_oEditTextNEW.setFilters(new InputFilter[] { new InputFilter.LengthFilter(20) });
		E_CPassword.setFilters(new InputFilter[] { new InputFilter.LengthFilter(20) });
		m_oEditTextOLD.setSingleLine();
		E_CPassword.setSingleLine();
		m_oEditTextNEW.setSingleLine();
		m_oEditTextOLD.setGravity(Gravity.CENTER);
		E_CPassword.setGravity(Gravity.CENTER);
		m_oEditTextNEW.setGravity(Gravity.CENTER);
		MakeBtn.setGravity(Gravity.CENTER);

		// m_oEditText.setGravity(Gravity.CENTER);//设置字体居中

		MakeBtn.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View view, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					m_bPressed = true;
					// view.invalidate();
					// view.setBackgroundColor(Color.GREEN);
					m_xscal = event.getX();
					m_yscal = event.getY();
					break;

				case MotionEvent.ACTION_UP:
					m_bPressed = false;

					// MakeBtn.setBackgroundResource(android.R.drawable.btn_default_small);
					// MakeBtn.setText("修改");
					if (m_xscal == event.getX() && m_yscal == event.getY())
					{
						
						String oldPassWord = m_oEditTextOLD.getText().toString().trim();
						String newPassWord = m_oEditTextNEW.getText().toString().trim();
						String newPassWordTwo = E_CPassword.getText().toString().trim();
						onClicked(oldPassWord,newPassWord,newPassWordTwo);
					}
					break;
				default:
					break;
				}
				return true;
			}
		});

		E_CPassword.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				imm.showSoftInput(E_CPassword, InputMethodManager.SHOW_FORCED);// 获取到这个类。
				E_CPassword.setFocusableInTouchMode(true);// 获取焦点

			}
		});

		m_oEditTextNEW.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				imm.showSoftInput(m_oEditTextNEW, InputMethodManager.SHOW_FORCED);// 获取到这个类。
				m_oEditTextNEW.setFocusableInTouchMode(true);// 获取焦点

			}
		});
		m_oEditTextOLD.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				imm.showSoftInput(m_oEditTextOLD, InputMethodManager.SHOW_FORCED);// 获取到这个类。
				m_oEditTextOLD.setFocusableInTouchMode(true);// 获取焦点

			}
		});
		m_oEditTextNEW.setTextColor(Color.BLACK);
		m_oEditTextNEW.setCursorVisible(true);// 让edittext出现光标
		m_oEditTextOLD.setTextColor(Color.BLACK);
		m_oEditTextOLD.setCursorVisible(true);
		E_CPassword.setTextColor(Color.BLACK);
		E_CPassword.setCursorVisible(true);
		// m_oEditText.setInputType(EditorInfo.TYPE_CLASS_PHONE); //设置文本格式
		imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);// 显示输入法窗口

	}

	@SuppressLint("DrawAllocation")
	protected void onDraw(Canvas canvas) {
		if (m_rRenderWindow == null)
			return;
		if (m_rRenderWindow.isLayoutVisible(getBBox()) == false)
			return;

		super.onDraw(canvas);
	}

	protected void onClicked(String oldPassWord,String newPassWord,String newPassWordTwo) {
	
		if ((oldPassWord.equals("") || newPassWord.equals("") || newPassWordTwo.equals("")) == false && label > 0) {
			if (MGridActivity.m_pagePassWord == null) {

				//Toast.makeText(getContext(), text12, 1000).show();
				post(text12);
				return;
			}

			if (!newPassWordTwo.equals(newPassWord)) {

				//Toast.makeText(getContext(), text13, 1000).show();
				post(text13);
				return;
			}
			if (MGridActivity.m_pagePassWord.length < label) {

				//Toast.makeText(getContext(), text14, 1000).show();
				post(text14);
				return;
			}
			if (oldPassWord.equals(MGridActivity.m_pagePassWord[label - 1]) || oldPassWord.equals("88888888")) {

				changPassWord(newPassWord);
				
					//Toast.makeText(m_rRenderWindow.getContext(), text15, Toast.LENGTH_SHORT).show();
					post(text15);

			} else {

				//Toast.makeText(m_rRenderWindow.getContext(), text16, Toast.LENGTH_SHORT).show();
				post(text16);
			}
			m_oEditTextOLD.setText("");
			m_oEditTextNEW.setText("");
			E_CPassword.setText("");
		} else {

			//Toast.makeText(m_rRenderWindow.getContext(), text17, Toast.LENGTH_SHORT).show();
			post(text17);
		}
	}

	// 修改密码，并且将密码保存至MGrid.ini配置文件
	private void changPassWord(String newPassWord) {
		if (label > 0) {
			MGridActivity.changPassWord(Type, newPassWord, label);
		} else {

			Toast.makeText(getContext(), text11, 1000).show();
		}

	}
	
	
	private void post(final String text)
	{
		this.post(new Runnable() {
			
			@Override
			public void run() {
				
				Toast.makeText(m_rRenderWindow.getContext(), text, Toast.LENGTH_SHORT).show();
			}
		});
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

			tvOld.layout(nX + (int) (nWidth * 0.1f), nY + (int) (nHeight * 0.1f), nX + (int) (nWidth * 0.3f),
					nY + (int) (nHeight * 0.24f));
			m_oEditTextOLD.layout(nX + (int) (nWidth * 0.35f), nY + (int) (nHeight * 0.1f), nX + (int) (nWidth * 0.9f),
					nY + (int) (nHeight * 0.24f));
			tvNew.layout(nX + (int) (nWidth * 0.1f), nY + (int) (nHeight * 0.34f), nX + (int) (nWidth * 0.3f),
					nY + (int) (nHeight * 0.48f));
			m_oEditTextNEW.layout(nX + (int) (nWidth * 0.35f), nY + (int) (nHeight * 0.34f), nX + (int) (nWidth * 0.9f),
					nY + (int) (nHeight * 0.48f));
			T_CPassword.layout(nX + (int) (nWidth * 0.1f), nY + (int) (nHeight * 0.58f), nX + (int) (nWidth * 0.3f),
					nY + (int) (nHeight * 0.72f));
			E_CPassword.layout(nX + (int) (nWidth * 0.35f), nY + (int) (nHeight * 0.58f), nX + (int) (nWidth * 0.9f),
					nY + (int) (nHeight * 0.72f));
			MakeBtn.layout(nX + (int) (nWidth * 0.42f), nY + (int) (nHeight * 0.82f), nX + (int) (nWidth * 0.65f),
					nY + (int) (nHeight * 1f));
			MakeBtn.setPadding(0, (int)(nHeight * 0.18f/5), 0, 0);

		}
		Rect rect = new Rect();
		getPaint().getTextBounds("修改", 0, 2, rect);
		int h = rect.height();

		

	}

	@Override
	public void setUniqueID(String strID) {
		// TODO Auto-generated method stub
		m_strID = strID;
	}

	@Override
	public String getUniqueID() {
		// TODO Auto-generated method stub
		return m_strID;
	}

	@Override
	public void setType(String strType) {
		// TODO Auto-generated method stub
		m_strType = strType;
	}

	@Override
	public String getType() {
		// TODO Auto-generated method stub
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
		} else if ("Alpha".equals(strName)) {
			m_fAlpha = Float.parseFloat(strValue);
		} else if ("Content".equals(strName)) {
			m_strContent = strValue;
			MakeBtn.setText(m_strContent);
		} else if ("FontFamily".equals(strName))
			m_strFontFamily = strValue;
		else if ("IsBold".equals(strName))
			m_bIsBold = Boolean.parseBoolean(strValue);
		else if ("BackgroundColor".equals(strName))

			if ("#FF000000".equals(strValue)) {
				m_oEditTextNEW.setBackgroundResource(R.drawable.et_select);
				m_oEditTextOLD.setBackgroundResource(R.drawable.et_select);
				E_CPassword.setBackgroundResource(R.drawable.et_select);
				MakeBtn.setBackgroundResource(R.drawable.bg_shadow);
			} else {
				m_cBackgroundColor = Color.parseColor(strValue);
			}
		else if ("FontColor".equals(strName)) {
			fontColor=strValue;
			m_cFontColor = Color.parseColor(strValue);
			this.setTextColor(m_cFontColor);
			tvNew.setTextColor(m_cFontColor);
			tvOld.setTextColor(m_cFontColor);
			T_CPassword.setTextColor(m_cFontColor);
			MakeBtn.setTextColor(m_cFontColor);
		} else if ("CmdExpression".equals(strName)) {
			m_strCmdExpression = strValue;
		} else if ("IsValueRelateSignal".equals(strName)) {
			if ("True".equals(strValue))
				m_bIsValueRelateSignal = true;
			else
				m_bIsValueRelateSignal = false;
		} else if ("ButtonWidthRate".equals(strName)) {
			m_fButtonWidthRate = Float.parseFloat(strValue);
		} else if ("Label".equals(strName)) {
			label = Integer.parseInt(strValue);
		}else if ("FontSize".equals(strName)) {
		    int size = Integer.parseInt(strValue);
			tvNew.setTextSize(size);
			T_CPassword.setTextSize(size);
			tvOld.setTextSize(size);
			m_oEditTextNEW.setTextSize(size);
			m_oEditTextOLD.setTextSize(size);
			E_CPassword.setTextSize(size);
		}
	}

	@Override
	public void initFinished() {
		setGravity(Gravity.CENTER);

		double padSize = CFGTLS.getPadHeight(m_nHeight, MainWindow.FORM_HEIGHT, getTextSize()) / 2;
		setPadding(0, (int) padSize, 0, (int) padSize);

	}

	@Override
	public void addToRenderWindow(MainWindow rWin) {
		
		m_rRenderWindow = rWin;
		m_rRenderWindow.viewList.add(base);
		rWin.addView(m_oEditTextNEW);
		rWin.addView(m_oEditTextOLD);
		rWin.addView(tvNew);
		rWin.addView(tvOld);
		rWin.addView(this);
		rWin.addView(T_CPassword);
		rWin.addView(E_CPassword);
		rWin.addView(MakeBtn);

	}

	@Override
	public void removeFromRenderWindow(MainWindow rWin) {
		// TODO Auto-generated method stub
		rWin.removeView(m_oEditTextNEW);
		rWin.removeView(m_oEditTextOLD);
		rWin.removeView(this);
	}

	@Override
	public void updateWidget() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean updateValue() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean needupdate() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void needupdate(boolean bNeedUpdate) {
		// TODO Auto-generated method stub

	}

	@Override
	public String getBindingExpression() {
		// TODO Auto-generated method stub
		return m_strCmdExpression;
	}

	@Override
	public View getView() {
		// TODO Auto-generated method stub
		return this;
	}

	@Override
	public int getZIndex() {
		// TODO Auto-generated method stub
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
	Button MakeBtn = null;

	TextView T_CPassword = null;
	EditText E_CPassword = null;
	String Type = "MaskPagePassword";
	int label = -1;

	// 记录触摸坐标，过滤滑动操作。解决滑动误操作点击问题。
	public float m_xscal = 0;
	public float m_yscal = 0;
	
	String fontColor;
	
	
	ViewObjectBase base=new SgChangXmlPWObject();
	

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

		((SgChangXmlPWObject)base).setFontColor(ViewObjectColorUtil.getColor(fontColor));
	}

	@Override
	public void onSetData() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onControl(Object obj) {
		
		String value=(String)obj;
		String[] str=value.split("-");
		
		if(str.length==3)
		{
			onClicked(str[0],str[1],str[2]);
		}
		
		
		
	}

}