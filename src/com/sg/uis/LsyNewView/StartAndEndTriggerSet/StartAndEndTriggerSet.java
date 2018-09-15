package com.sg.uis.LsyNewView.StartAndEndTriggerSet;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.InputFilter;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import comm_service.service;
import data_model.ipc_cfg_trigger_value;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.mgrid.main.MainWindow;
import com.mgrid.main.R;
import com.sg.common.CFGTLS;
import com.sg.common.IObject;
import com.sg.common.UtExpressionParser;
import com.sg.common.UtExpressionParser.stBindingExpression;
import com.sg.common.UtExpressionParser.stExpression;

public class StartAndEndTriggerSet extends TextView implements IObject {
	public StartAndEndTriggerSet(Context context) {
		super(context);
		this.setClickable(true);
		this.setGravity(Gravity.CENTER);
		this.setTextColor(m_cFontColor);
		this.setBackgroundColor(m_cBackgroundColor);
		this.setFocusableInTouchMode(true);
		m_fFontSize = this.getTextSize();

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
						onClicked();
					break;

				default:
					break;
				}
				return true;
			}
		});

		m_oPaint = new Paint();
		m_rBBox = new Rect();

		m_oEditText = new EditText(context);
		end_oEditText = new EditText(context);
		initEt(end_oEditText);
		initEt(m_oEditText);

		start_Textview = new TextView(context);
		end_Textview = new TextView(context);
		sTextview = new TextView(context);
		eTextview = new TextView(context);

		initTv(start_Textview, "0");
		initTv(end_Textview, "0");
		initTv(sTextview, "开始:");
		initTv(eTextview, "结束:");

		// m_oEditText.setOnClickListener(new View.OnClickListener() {
		//
		// @Override
		// public void onClick(View arg0) {
		// imm.showSoftInput(m_oEditText, InputMethodManager.SHOW_FORCED);
		// m_oEditText.setFocusableInTouchMode(true);
		//
		// }
		// });
		//
		// imm = (InputMethodManager) context
		// .getSystemService(Context.INPUT_METHOD_SERVICE);

		setText("设置");
		setTextColor(Color.WHITE);
		setBackgroundResource(android.R.drawable.btn_default);
		setPadding(0, 0, 0, 0);
	}

	private void initEt(EditText text) {
		text.setSingleLine();
		text.setGravity(Gravity.CENTER);
		text.setFilters(new InputFilter[] { new InputFilter.LengthFilter(7) }); // 写死可输入位数
		text.setTextColor(Color.BLACK);
		text.setBackgroundResource(android.R.drawable.edit_text);
		text.setPadding(0, 2, 0, 0);
		text.setTextSize(14);
		text.setCursorVisible(true);
		text.setInputType(EditorInfo.TYPE_CLASS_PHONE);
	}

	private void initTv(TextView text, String str) {

		text.setGravity(Gravity.CENTER);
		text.setTextColor(Color.BLACK);
		text.setPadding(0, 2, 0, 0);
		text.setTextSize(14);
		text.setCursorVisible(true);
		text.setText(str);
	}

	@SuppressLint("DrawAllocation")
	protected void onDraw(Canvas canvas) {
		if (m_rRenderWindow == null)
			return;
		if (m_rRenderWindow.isLayoutVisible(getBBox()) == false)
			return;

		if (m_bPressed) {
			int nWidth = (int) (((float) (m_nWidth) / (float) MainWindow.FORM_WIDTH)
					* (m_rRenderWindow.VIEW_RIGHT - m_rRenderWindow.VIEW_LEFT));
			int nHeight = (int) (((float) (m_nHeight) / (float) MainWindow.FORM_HEIGHT)
					* (m_rRenderWindow.VIEW_BOTTOM - m_rRenderWindow.VIEW_TOP));

			m_oPaint.setColor(0x50FF00F0);
			m_oPaint.setStyle(Paint.Style.FILL);
			canvas.drawRect(0, 0, nWidth, nHeight, m_oPaint);
		}
		super.onDraw(canvas);
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

			sTextview.layout(nX, nY, (int) (nX + nWidth / 10.0), nY + nHeight);
			start_Textview.layout((int) (nX + nWidth / 10.0), nY, (int) (nX + nWidth * 2 / 10.0), nY + nHeight);
			m_oEditText.layout((int) (nX + nWidth * 2 / 10.0), nY, (int) (nX + nWidth * 4 / 10.0 - 5), nY + nHeight);

			eTextview.layout((int) (nX + nWidth * 4 / 10.0), nY, (int) (nX + nWidth * 5 / 10.0), nY + nHeight);
			end_Textview.layout((int) (nX + nWidth * 5 / 10.0), nY, (int) (nX + nWidth * 6 / 10.0), nY + nHeight);
			end_oEditText.layout((int) (nX + nWidth * 6 / 10.0), nY, (int) (nX + nWidth * 8 / 10.0 - 5), nY + nHeight);

			this.layout((int) (nX + nWidth * 8 / 10.0), nY, nX + nWidth, nY + nHeight);
		}
	}

	@Override
	public void addToRenderWindow(MainWindow rWin) {
		m_rRenderWindow = rWin;

		rWin.addView(sTextview);
		rWin.addView(start_Textview);
		rWin.addView(m_oEditText);

		rWin.addView(eTextview);
		rWin.addView(end_Textview);
		rWin.addView(end_oEditText);

		rWin.addView(this);
	}

	@Override
	public void removeFromRenderWindow(MainWindow rWin) {

		rWin.removeView(m_oEditText);
		rWin.removeView(this);
	}

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
			this.setText(m_strContent);
		} else if ("FontFamily".equals(strName))
			m_strFontFamily = strValue;
		else if ("IsBold".equals(strName))
			m_bIsBold = Boolean.parseBoolean(strValue);
		else if ("BackgroundColor".equals(strName)) {

			if ("#FF000000".equals(strValue)) {
				m_oEditText.setBackgroundResource(R.drawable.et_select);
				// setBackgroundResource(R.drawable.bg_shadow);
				setBackgroundResource(R.drawable.bg_shadow);
			} else {
				m_cBackgroundColor = Color.parseColor(strValue);
			}
		} else if ("FontColor".equals(strName)) {
			m_cFontColor = Color.parseColor(strValue);
			this.setTextColor(m_cFontColor);
			start_Textview.setTextColor(m_cFontColor);
			end_Textview.setTextColor(m_cFontColor);
			sTextview.setTextColor(m_cFontColor);
			eTextview.setTextColor(m_cFontColor);
		} else if ("FontSize".equals(strName)) {
			m_fFontSize = Float.parseFloat(strValue);
			this.setTextSize(m_fFontSize);
			start_Textview.setTextSize(m_fFontSize);
			end_Textview.setTextSize(m_fFontSize);
			sTextview.setTextSize(m_fFontSize);
			eTextview.setTextSize(m_fFontSize);
		} else if ("Expression".equals(strName)) {
			m_strCmdExpression = strValue;
			parse_banding();

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
		double padSize = CFGTLS.getPadHeight(m_nHeight, MainWindow.FORM_HEIGHT, getTextSize()) / 2;
		setPadding(0, (int) padSize, 0, (int) padSize);
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

	private void onClicked() {

		String startvalue = m_oEditText.getText().toString().trim();
		String stopvalue = end_oEditText.getText().toString().trim();

		if(startvalue.isEmpty()&&stopvalue.isEmpty())
		{
			return ;
		}




		// 发送阀值设定控制命令
		if ("".equals(m_strCmdExpression) == false) {
			synchronized (m_rRenderWindow.m_oShareObject) {
				m_rRenderWindow.m_oShareObject.m_mapTriggerCommand.put(getUniqueID(),
						startvalue+","+stopvalue+"");
				
				//Log.e(startvalue, stopvalue);
			}
		}

	}

	// 解析出控件表达式，返回控件表达式类
	public boolean parse_banding() {

		stExpression oMathExpress = UtExpressionParser.getInstance().parseExpression(m_strCmdExpression);
		if (oMathExpress != null) {
			// 遍历控件表达式各个数据单元表达式类
			Iterator<HashMap.Entry<String, stBindingExpression>> it = oMathExpress.mapObjectExpress.entrySet()
					.iterator();
			while (it.hasNext()) {
				stBindingExpression oBindingExpression = it.next().getValue();
				bt_EquipID = oBindingExpression.nEquipId;
				bt_EventID = oBindingExpression.nEventId;
				bt_Condition = oBindingExpression.nConditionId;
				//Log.e("tag", bt_EquipID+":"+bt_EventID+":"+bt_Condition);
			}
		}
		return true;
	}

	public String getBindingExpression() {
		return m_strCmdExpression;
	}

	@Override
	public void updateWidget() {

	
		
		if (m_strContent == null) {
			start_Textview.setText("");
		} else {
			start_Textview.setText(m_strContent);
		}
		
		if(end_Textview==null)
		{
			end_Textview.setText("");
		}else
		{
			end_Textview.setText(stop_strContent);
		}
		

	}

	@Override
	public boolean updateValue() {

		// 注意在updateValue里面不应该对view的属性经行操作，因为属性操作本来就会调用updateValue. eg:setTextColor()
		m_bneedupdate = false;
		// Log.e("tigerLabel->updateValue","into！");
		String strValue = "";
		String stopValue = "";
		float s_value = 0;
		float e_value = 0;
		// 获取告警阀值
		List<ipc_cfg_trigger_value> triglst = service.get_cfg_trigger_value("127.0.0.1", 9630, bt_EquipID);
		
		//Log.e("tag", "一");
		
		if (triglst == null)
			return false;
		
		//Log.e("tag", "二");
		Iterator<ipc_cfg_trigger_value> iter = triglst.iterator();
		while (iter.hasNext()) {
			ipc_cfg_trigger_value clas_cfg = iter.next();
			s_value = clas_cfg.startvalue;
			e_value = clas_cfg.stopvalue;
			int c_equipID = clas_cfg.equipid;
			int c_event = clas_cfg.eventid;
			int c_condictoin = clas_cfg.conditionid;
			if ((bt_EquipID == c_equipID) && (bt_EventID == c_event) && (bt_Condition == c_condictoin)) {
				strValue = String.valueOf(s_value);
				stopValue = String.valueOf(e_value);
			//	Log.e("tag", strValue+"::::"+stopValue);
			}
		}

		m_strContent = strValue; // 界面数值赋予
		stop_strContent = stopValue;

		return true;

	}

	@Override
	public boolean needupdate() {
		return m_bneedupdate;
	}

	@Override
	public void needupdate(boolean bNeedUpdate) {

		m_bneedupdate = bNeedUpdate;
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
	String stop_strContent = "Setting";
	String m_strCmdExpression = "";
	boolean m_bIsValueRelateSignal = false;
	float m_fButtonWidthRate = 0.3f;
	MainWindow m_rRenderWindow = null;
	float m_fFontSize = 6.0f;
	boolean m_bPressed = false;
	Paint m_oPaint = null;
	Rect m_rBBox = null;
	InputMethodManager imm = null;
	EditText m_oEditText = null;
	EditText end_oEditText = null;
	TextView start_Textview = null;
	TextView end_Textview = null;
	TextView sTextview = null;
	TextView eTextview = null;

	String m_strSignalValue = "";

	int bt_EquipID = -1;
	int bt_EventID = -1;
	int bt_Condition = -1;

	public boolean m_bneedupdate = true;
	// 记录触摸坐标，过滤滑动操作。解决滑动误操作点击问题。
	public float m_xscal = 0;
	public float m_yscal = 0;
}
