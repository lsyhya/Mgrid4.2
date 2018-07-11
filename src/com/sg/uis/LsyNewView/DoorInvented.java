package com.sg.uis.LsyNewView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.mgrid.main.MGridActivity;
import com.mgrid.main.MainWindow;
import com.mgrid.main.R;
import com.mgrid.main.user.User;
import com.mgrid.util.FileUtil;
import com.mgrid.util.TimeUtils;
import com.sg.common.CFGTLS;
import com.sg.common.IObject;
import com.sg.common.LanguageStr;
import com.sg.common.UtExpressionParser;
import com.sg.common.UtExpressionParser.stBindingExpression;
import com.sg.common.UtExpressionParser.stExpression;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class DoorInvented extends TextView implements IObject {

	private TextView tv;
	private String sb = "";
	private String cmd_value = "";
	private String nowUser = "";
	private String text19 = LanguageStr.text19;
	private FileUtil util;
	private String savePath = "/mgrid/log/DoorEvent/";
	private String passWord="";//当前输入的密码

	private List<Button> btnData = new ArrayList<Button>();

	public DoorInvented(Context context) {
		super(context);

		init();

	}

	private void init() {
		m_rBBox = new Rect();
		util = new FileUtil();

		tv = new TextView(getContext());
		tv.setTextSize(25);
		tv.setTextColor(Color.parseColor("#000000"));
		tv.setBackgroundResource(R.drawable.textbroad);
		tv.setText("");

		for (int i = 0; i <= 11; i++) {
			Button btn = new Button(getContext());
			btn.setBackgroundResource(android.R.drawable.btn_default);
			btn.setPadding(0, 0, 0, 0);
			btn.setGravity(Gravity.CENTER);
			btn.setTextSize(15);
			btn.setTextColor(Color.BLACK);

			switch (i) {
			case 10:

				btn.setText("清空");

				break;
			case 11:

				btn.setText("确认");

				break;

			default:

				btn.setText(i + "");

				break;
			}

			btnData.add(btn);
			btn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					Button btn = (Button) v;
					if (btn.getText().toString().equals("清空")) {

						Clear();

					} else if (btn.getText().toString().equals("确认")) {

						Sure();

					} else {

						setTV(btn.getText().toString());

					}
				}
			});

		}
	}

	// 发送开门命令
	private void sendCmd() {

		if (!m_strCmdExpression.equals("")) {

			synchronized (m_rRenderWindow.m_oShareObject) {

				if ("".equals(cmd_value)) {
					deal_cmd();
				}

				if (!"".equals(cmd_value)) {

					m_rRenderWindow.m_oShareObject.m_mapCmdCommand.put(getUniqueID(), cmd_value);

				}
			}

		}
	}

	// 解析Cmd
	public boolean deal_cmd() {
		// if("".equals(m_strCmdExpression)) return false;
		// if(m_strCmdExpression==null) return false;
		stExpression oMathExpress = UtExpressionParser.getInstance().parseExpression(m_strCmdExpression);
		if (oMathExpress != null) {
			// 遍历控件表达式各个数据单元表达式类
			Iterator<HashMap.Entry<String, stBindingExpression>> it = oMathExpress.mapObjectExpress.entrySet()
					.iterator();
			while (it.hasNext()) {
				stBindingExpression oBindingExpression = it.next().getValue();
				cmd_value = oBindingExpression.strValue;
			}
		}
		return true;
	}

	/*
	 * 清空
	 */
	private void Clear() {
		tv.setText("");
		sb = "";
	}

	// 确定
	private void Sure() {

		if (isSure()) {
			sendCmd();
		} else {
			saveResult(0);

			Toast.makeText(getContext(), text19, Toast.LENGTH_SHORT).show();
		}
		Clear();
	}

	// 密码是否正确
	private boolean isSure() {

		passWord = tv.getText().toString();

		if (MGridActivity.m_ControlAway == 1) {

			if (MGridActivity.userManager.getNowUser() != null) {

				if (MGridActivity.userManager.getNowUser().getPassWord().equals(passWord)) {

					saveResult(1);
					return true;

				}
			}

		} else if (MGridActivity.m_ControlAway == 0) {

			if (MGridActivity.userManager.getPassWordList().contains(passWord)) {

				saveResult(1);
				return true;

			}

		}

		return false;
	}

	// 保存结果
	private void saveResult(final int i) {

		MGridActivity.xianChengChi.execute(new Runnable() {

			@Override
			public void run() {

				String nowTime = TimeUtils.getNowTime();
				String event = "开门";
				getNowUser();

				switch (i) {
				case 0:

					util.saveDoorEvent(savePath, "DoorEven.dat", nowUser + "," + nowTime + "," + event + "," + "失败");

					break;

				case 1:

					util.saveDoorEvent(savePath, "DoorEven.dat", nowUser + "," + nowTime + "," + event + "," + "成功");

					break;
				}

			}
		});

	}

	/**
	 * 得到当前操作用户
	 * 
	 * @param string
	 */
	private void getNowUser() {

		if (MGridActivity.m_ControlAway == 1) {
			if (MGridActivity.userManager.getNowUser() != null) {
				nowUser = MGridActivity.userManager.getNowUser().getUserID();
			}
		} else if (MGridActivity.m_ControlAway == 0) {

			
			Map<Integer, User> userManaget = MGridActivity.userManager.getUserManaget();
			Iterator<Map.Entry<Integer, User>> it = userManaget.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry<Integer, User> entry = it.next();
				if (entry.getValue().getPassWord().equals(passWord)) {
					nowUser = entry.getValue().getUserID();
					return;
				}

				nowUser = passWord;
			} 

		}
	}

	// 设置文字
	private void setTV(String str) {
		sb += str;
		tv.setText(sb);
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

			Rect rect = new Rect();
			getPaint().getTextBounds("确认", 0, "确认".length(), rect);
			int height = rect.height();
			this.layout(nX, nY, nX + nWidth, nY + nHeight);
			tv.layout((int) (nX + 0.1 * nWidth), (int) (nY + 0.05 * nHeight), (int) (nX + 0.9 * nWidth),
					(int) (nY + 0.19 * nHeight));
			tv.setPadding(0, (int) (0.14 * nWidth - height) / 2, 0, 0);

			for (int i = 0; i < btnData.size(); i++) {

				btnData.get(i).setPadding(0, (int) (0.14 * nWidth - height) / 2, 0, 0);

				if (0 < i && i < 4) {
					btnData.get(i).layout((int) (nX + (0.1 + (i - 1) * 0.3) * nWidth), (int) (nY + 0.24 * nHeight),
							(int) (nX + 0.3 * nWidth * i), (int) (nY + 0.38 * nHeight));
				}
				if (3 < i && i < 7) {
					btnData.get(i).layout((int) (nX + (0.1 + (i - 1 - 3) * 0.3) * nWidth), (int) (nY + 0.43 * nHeight),
							(int) (nX + 0.3 * nWidth * (i - 3)), (int) (nY + 0.57 * nHeight));
				}
				if (6 < i && i < 10) {
					btnData.get(i).layout((int) (nX + (0.1 + (i - 1 - 3 - 3) * 0.3) * nWidth),
							(int) (nY + 0.62 * nHeight), (int) (nX + 0.3 * nWidth * (i - 3 - 3)),
							(int) (nY + 0.76 * nHeight));
				}
				if (i == 0) {
					btnData.get(i).layout((int) (nX + (0.4) * nWidth), (int) (nY + 0.81 * nHeight),
							(int) (nX + 0.6 * nWidth), (int) (nY + 0.95 * nHeight));
				}
				if (i == 10) {
					btnData.get(i).layout((int) (nX + (0.1) * nWidth), (int) (nY + 0.81 * nHeight),
							(int) (nX + 0.3 * nWidth), (int) (nY + 0.95 * nHeight));
				}
				if (i == 11) {
					btnData.get(i).layout((int) (nX + (0.7) * nWidth), (int) (nY + 0.81 * nHeight),
							(int) (nX + 0.9 * nWidth), (int) (nY + 0.95 * nHeight));
				}

			}

		}

	}

	@Override
	protected void onDraw(Canvas canvas) {

		if (m_rRenderWindow == null)
			return;
		if (m_rRenderWindow.isLayoutVisible(getBBox()) == false)
			return;

		super.onDraw(canvas);
		// 画边框
		Paint p = new Paint();
		p.setColor(Color.parseColor("#000000"));
		p.setStyle(Paint.Style.STROKE);
		canvas.drawRect(0, 0, m_rBBox.right - m_rBBox.left, m_rBBox.bottom - m_rBBox.top, p);

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
		} else if ("RotateAngle".equals(strName)) {
			m_fRotateAngle = Float.parseFloat(strValue);
		} else if ("Content".equals(strName)) {
			m_strContent = strValue;
			c_Content = strValue;
			// this.setText(m_strContent);
			// shimmerTv.setText(m_strContent);
		} else if ("FontFamily".equals(strName)) {
			m_strFontFamily = strValue;
			// this.setTypeface(MyApplication.typeface);
		} else if ("FontSize".equals(strName)) {
			float fWinScale = (float) MainWindow.SCREEN_WIDTH / (float) MainWindow.FORM_WIDTH;
			m_fFontSize = Float.parseFloat(strValue) * fWinScale;
			// this.setTextSize(m_fFontSize);
			// shimmerTv.setTextSize(m_fFontSize);
		} else if ("IsBold".equals(strName))
			m_bIsBold = Boolean.parseBoolean(strValue);
		else if ("FontColor".equals(strName)) {
			m_cFontColor = Color.parseColor(strValue);
			m_cStartFillColor = m_cFontColor;
			// this.setTextColor(m_cFontColor);
			// shimmerTv.setTextColor(m_cFontColor);
		} else if ("HorizontalContentAlignment".equals(strName))
			m_strHorizontalContentAlignment = strValue;
		else if ("VerticalContentAlignment".equals(strName)) {
			m_strVerticalContentAlignment = strValue;
		} else if ("CmdExpression".equals(strName)) {
			m_strCmdExpression = strValue;

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
			double padSize = CFGTLS.getPadHeight(m_nHeight, MainWindow.FORM_HEIGHT, getTextSize());
			setPadding(0, (int) padSize, 0, 0);
		} else if ("Center".equals(m_strVerticalContentAlignment)) {
			nFlag |= Gravity.CENTER_VERTICAL;
			double padSize = CFGTLS.getPadHeight(m_nHeight, MainWindow.FORM_HEIGHT, getTextSize()) / 2;
			setPadding(0, (int) padSize, 0, (int) padSize);
		}

		setGravity(nFlag);

	}

	@Override
	public void addToRenderWindow(MainWindow rWin) {
		m_rRenderWindow = rWin;
		rWin.addView(this);
		rWin.addView(tv);
		for (Button btn : btnData) {
			rWin.addView(btn);
		}

	}

	@Override
	public void removeFromRenderWindow(MainWindow rWin) {
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
	String m_strCmdExpression = "Binding{[Cmd[Equip:1-Temp:173-Command:1-Parameter:1-Value:1]]}";

	MainWindow m_rRenderWindow = null;

	Rect m_rBBox = null;

	public boolean m_bneedupdate = true;

}
