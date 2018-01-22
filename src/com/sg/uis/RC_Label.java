package com.sg.uis;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

import com.fjw.view.Axis;
import com.mgrid.main.MainWindow;
import com.sg.common.CFGTLS;
import com.sg.common.IObject;
import com.sg.common.UtExpressionParser;
import com.sg.common.UtExpressionParser.stBindingExpression;
import com.sg.common.UtExpressionParser.stExpression;
import comm_service.local_rc;

import data_model.rc_value;

/** 数据累加量标签 */
/** made by fjw */
@SuppressWarnings("rawtypes")
@SuppressLint({ "SimpleDateFormat", "RtlHardcoded", "HandlerLeak",
		"UseSparseArrays" })
public class RC_Label extends TextView implements IObject {

	public RC_Label(Context context) {
		super(context);
		this.setClickable(false);
		this.setBackgroundColor(0x00000000);
		m_rBBox = new Rect();
		my_Axis = new Axis(context);
		paint = new Paint();

		EqdSalId = new ArrayList<String>();

		// 定义选择按纽组 按钮数4个
		ridobuttons = new RadioButton[3];
		ridobuttons[0] = new RadioButton(context);
		ridobuttons[0].setText("year");
		ridobuttons[1] = new RadioButton(context);
		ridobuttons[1].setText("mon");
		ridobuttons[1].setChecked(true);
		ridobuttons[2] = new RadioButton(context);
		ridobuttons[2].setText("day");
		// 设置监听
		for (int i = 0; i < 3; i++) {
			ridobuttons[i].setTextColor(Color.BLACK);
			ridobuttons[i].setOnClickListener(l);
		}
	}

	private OnClickListener l = new OnClickListener() {
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			String strText = (String) ((RadioButton) arg0).getText();
			for (int i = 0; i < ridobuttons.length; i++)
				ridobuttons[i].setChecked(false);
			if ("year".equals(strText)) {
				mode = 0;
				flag = 1;
			} else if ("mon".equals(strText)) {
				mode = 1;
				flag = 0;
			} else if ("day".equals(strText)) {
				mode = 2;
				flag = 0;
			}
			ridobuttons[mode].setChecked(true);

			myThread mythread = new myThread();
			mythread.start();
		}
	};

	Handler myHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				updateWidget();
				break;
			default:
				break;
			}
		}
	};

	private class myThread extends Thread {
		public void run() {
			updateValue();
            
			myHandler.sendEmptyMessage(1);
		}
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
		layout_w = nWidth;
		layout_h = nHeight;

		m_rBBox.left = nX;
		m_rBBox.top = nY;
		m_rBBox.right = nX + nWidth;
		m_rBBox.bottom = nY + nHeight;
		if (m_rRenderWindow.isLayoutVisible(m_rBBox)) {
			layout(nX, nY, nX + nWidth, nY + nHeight);
		}

		my_Axis.upDataValueFlag(layout_w, layout_h, m_num, 10, m_num,
				m_max_value / 10 * 14, flag);
		my_Axis.doLayout(true, nX, nY, nX + nWidth, nY + nHeight);

		for (int i = 0; i < ridobuttons.length; i++)
			ridobuttons[i].layout(nX + (i + 1) * nWidth / 4, nY, nX + (i + 1)
					* nWidth / 4 + nWidth / 4, nY + 20);
	}

	public void onDraw(Canvas canvas) {
		if (m_rRenderWindow == null)
			return;
		if (m_rRenderWindow.isLayoutVisible(getBBox()) == false)
			return;
 
		my_Axis.LineColor = m_cLineColor;
		my_Axis.BackgroundColor = m_cBackgroundColor;
		my_Axis.upDataValueFlag(layout_w, layout_h, m_num, 10, m_num,
				m_max_value / 10 * 14, flag);
		my_Axis.invalidate();

		//System.out.println("size："+value_flag.size());
		for(int l=0;l<value_flag.size();l++){
		float moveCount=(float) (0.8/value_flag.size());
			
		paint.setColor(ColorTybe[l%6]);
		paint.setTextSize(10);
		paint.setStyle(Paint.Style.FILL);

		for (int i = 0; i < m_num; i++) {
			if (value_flag.size() == 0)
				break;
			float rc_x = (float) (my_Axis.x_start + my_Axis.x_unit * (i + 0.6+l*moveCount));
			if(value_flag.size()<l+1)
			{
				return;
			}
			
			float rc_y = my_Axis.y_start - my_Axis.y_per_unit * ((Float)value_flag.get(l).get(i));
					//* value_flag.get(i);
			float rc_x_end = rc_x + my_Axis.x_unit * moveCount;
			float rc_y_end = my_Axis.y_start;

			if (mode == 0) {
				canvas.drawRect(rc_x, rc_y, rc_x_end, rc_y_end, paint);
			} else if (mode == 1) {
				canvas.drawRect(rc_x, rc_y, rc_x_end, rc_y_end, paint);
			} else if (mode == 2) {
				canvas.drawRect(rc_x, rc_y, rc_x_end, rc_y_end, paint);

			}
		}
		}
		//清零 便于下次处理数据
//		m_max_value=0;
//		value_Day.clear();
//		value_Mon.clear();
//		value_Year.clear();
		isDelete=true;
	//	isRun=false;
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
		rWin.addView(my_Axis);
		rWin.addView(this);
		for (int i = 0; i < ridobuttons.length; i++)
			rWin.addView(ridobuttons[i]);
	}

	@Override
	public void removeFromRenderWindow(MainWindow rWin) {
		rWin.removeView(this);
		rWin.removeView(my_Axis);
		for (int i = 0; i < ridobuttons.length; i++)
			rWin.removeView(ridobuttons[i]);
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
			// this.setText(m_strContent);
		} else if ("FontFamily".equals(strName))
			m_strFontFamily = strValue;
		else if ("FontSize".equals(strName)) {
			float fWinScale = (float) MainWindow.SCREEN_WIDTH
					/ (float) MainWindow.FORM_WIDTH;
			m_fFontSize = Float.parseFloat(strValue) * fWinScale;
		} else if ("IsBold".equals(strName))
			m_bIsBold = Boolean.parseBoolean(strValue);
		else if ("FontColor".equals(strName)) {
			m_cFontColor = Color.parseColor(strValue);
			for (int i = 0; i < 3; i++) {
				ridobuttons[i].setTextColor(m_cFontColor);
			}
		} else if ("LineColor".equals(strName)) {
			m_cLineColor = Color.parseColor(strValue);
		} else if ("BackgroundColor".equals(strName)) {
			m_cBackgroundColor = Color.parseColor(strValue);
			// this.setBackgroundColor(m_cBackgroundColor);
		} else if ("HorizontalContentAlignment".equals(strName))
			m_strHorizontalContentAlignment = strValue;
		else if ("VerticalContentAlignment".equals(strName))
			m_strVerticalContentAlignment = strValue;
		else if ("Expression".equals(strName)) {
			m_strExpression = strValue;
			parse_expression();
		} else if ("ColorExpression".equals(strName))
			m_strColorExpression = strValue;
		else if ("D_mon".equals(strName))
			mode = Integer.parseInt(strValue);
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

		this.invalidate();
	}

	@Override
	public boolean updateValue() {

		m_bneedupdate = false;
		long pre_time = System.currentTimeMillis();

		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date(pre_time);
		String sampletime = formatter.format(date);
		String dateTime = sampletime.substring(0, 10);
		year = Integer.parseInt(dateTime.substring(0, 4));
		mon = Integer.parseInt(dateTime.substring(5, 7));
		day = Integer.parseInt(dateTime.substring(8, 10));
		long c_time ; 
		if(day==1)
		{
			 c_time = System.currentTimeMillis();
		}else{
			 c_time = System.currentTimeMillis() - 86400 * 1000;
		}
		Date c_data = new Date(c_time);
		String c_sampletime = formatter.format(c_data);
		String c_dateTime = c_sampletime.substring(0, 10);
		int c_year = Integer.parseInt(c_dateTime.substring(0, 4));

		List<rc_value> D_lst = new ArrayList<rc_value>();
		local_rc l_rc = new local_rc();
		//value_flag.clear();

		
       // System.out.println("命令个数:"+EqdSalId.size());
		for (String s:EqdSalId) {
			
			String[] str=s.split("\\|");
			equiptID = Integer.parseInt(str[0]);
			siganlID = Integer.parseInt(str[1]);

			if (mode == 0) {

				if(isDelete)
				{
					m_max_value=0;
					value_Year.clear();
					isDelete=false;
				}
				
				ArrayList<Float>  YList = new ArrayList<Float>(); 
				
				m_num = 10;
				year = 0;
				// year--;
				D_lst = l_rc.getD_Vlaue(equiptID, siganlID, year, mon, day,
						mode);
				if (D_lst == null || D_lst.size() == 0) {
					continue;
				}
				
				//value_Year.clear();
				
				for (int i = 0; i < m_num; i++) {
					YList.add((float) 0);
				}

				
				Iterator<rc_value> iter = D_lst.iterator();

				while (iter.hasNext()) {
					rc_value rc_v = new rc_value();
					rc_v = iter.next();
					if (rc_v == null)
						break;
					String str_year = rc_v.datetime.substring(0, 4);
					int label = c_year - Integer.parseInt(str_year);
					if (label < m_num && label >= 0)
						YList.set(m_num - label - 1,
								Float.parseFloat(rc_v.value));

				}
				// value_Year.set(0, fl);
				get_max(YList);
				value_Year.add(YList);
				value_flag = value_Year;

			} else if (mode == 1) {

				if(isDelete)
				{
					m_max_value=0;
					value_Mon.clear();
					isDelete=false;
				}
				ArrayList<Float>  MList = new ArrayList<Float>();
				mon = 0;
				m_num = 12;
				D_lst = l_rc.getD_Vlaue(equiptID, siganlID, year, mon, day,
						mode);
				if (D_lst == null || D_lst.size() == 0) {
					continue;
				}

			    //value_flag.clear();
			//	value_Mon.clear();
				for (int i = 0; i < m_num; i++) {
					MList.add((float) 0);
				}
                //System.out.println("月:"+D_lst.size());
				Iterator<rc_value> iter = D_lst.iterator();
				while (iter.hasNext()) {
					rc_value rc_v = new rc_value();
					rc_v = iter.next();
					String str_mon = rc_v.datetime.substring(5, 7);
					MList.set(Integer.parseInt(str_mon) - 1,
							Float.parseFloat(rc_v.value));
				}
				get_max(MList);
				value_Mon.add(MList);
				value_flag = value_Mon;

			} else if (mode == 2) {

				if(isDelete)
				{
					m_max_value=0;
					value_Day.clear();
					isDelete=false;
				}
				ArrayList<Float>  DList = new ArrayList<Float>();
				
				if (mon == 2) {
					m_num = 29;
				} else if (mon == 4 || mon == 6 || mon == 9 || mon == 10) {
					m_num = 30;
				} else {
					m_num = 31;
				}
				//value_Day.clear();
				for (int i = 0; i < m_num; i++) {

					DList.add((float) 0);
				}
				
				day=0;
				
				D_lst = l_rc.getD_Vlaue(equiptID, siganlID, year, mon, day,
						mode);
				if (D_lst == null || D_lst.size() == 0) {
					continue;
				}
				
				Iterator<rc_value> iter = D_lst.iterator();
				while (iter.hasNext()) {
					rc_value rc_v = new rc_value();
					rc_v = iter.next();
					String str_day = rc_v.datetime.substring(8, 10);
					DList.set(Integer.parseInt(str_day) - 1,
							Float.parseFloat(rc_v.value));
				}


				get_max(DList);
				value_Day.add(DList);
				value_flag = value_Day;
			}
		}
		
		//isRun=true;
		return true;
	}

	// 计算出最大的数值
	public float get_max(List<Float> lst) {
		float value = 0;
		for (int i = 0; i < m_num; i++) {
			if (lst.get(i) > value) {
				value = lst.get(i);
			}
		}
		switch (mode) {
		case 0:
			
			if(Y_max_value==0)
			{
				Y_max_value=value;
			}
			else{
				if(Y_max_value<value)
				{
					Y_max_value=value;
				}
			}
			m_max_value=Y_max_value;	
			break;

		case 1:
			
			if(M_max_value==0)
			{
				M_max_value=value;
			}
			else{
				if(M_max_value<value)
				{
					M_max_value=value;
				}
			}
			m_max_value=M_max_value;
			
			break;
		case 2:
			
			if(D_max_value==0)
			{
				D_max_value=value;
			}
			else{
				if(D_max_value<value)
				{
					D_max_value=value;
				}
			}
			m_max_value=D_max_value;			
			break;
		}
		return m_max_value;
	}

	public boolean parse_expression() {
		if ("".equals(m_strExpression))
			return false;
		
	
			stExpression oMathExpress = UtExpressionParser.getInstance()
					.parseExpression(m_strExpression);
			if (oMathExpress != null) {

				Iterator<HashMap.Entry<String, stBindingExpression>> it = oMathExpress.mapObjectExpress
						.entrySet().iterator();
				while (it.hasNext()) {
					stBindingExpression oBindingExpression = it.next()
							.getValue();
					int equipt_id = oBindingExpression.nEquipId;
					int signal_id = oBindingExpression.nSignalId;

					// String str_equiptName =
					// DataGetter.getEquipmentName(equipt_id);
					// String str_signalName =
					// DataGetter.getSignalName(equipt_id, signal_id);
					equiptID = equipt_id;
					siganlID = signal_id;
					EqdSalId.add(equiptID+"|"+siganlID);

				}
			}
		
		
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
	int m_cLineColor = 0xFF008000;
	int m_cBackgroundColor = 0xFFFFFFFF;
	String m_strHorizontalContentAlignment = "Center";
	String m_strVerticalContentAlignment = "Center";
	String m_strExpression = "Binding{[Value[Equip:114-Temp:173-Signal:1]]}";
	String m_strColorExpression = ">20[#FF009090]>30[#FF0000FF]>50[#FFFF0000]>60[#FFFFFF00]";
	int D_mon = 0; // 累积量显示模式 默认0日累积量 1：月累积量 2：年累积量

	MainWindow m_rRenderWindow = null;
	String m_strSignalValue = "";

	Rect m_rBBox = null;
	public int equiptID;
	public int siganlID;
	public int year;
	public int mon;
	public int day = 1;
	public int mode = 1; // 0：一年的月度累积量 1：月的日度累积量 2：某天的累积量
	public int m_num = 31; // x 轴的刻度数
	public float m_max_value = 10;
	int x_markFlg[] = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16,
			17, 18, 19, 20, 21, 22, 23, 24, 25, 27, 28, 29, 30, 31 }; // x轴的标签数组
	List<ArrayList> value_flag = new ArrayList<ArrayList>(); // 各个数值 数组

	List<ArrayList> value_Mon = new ArrayList<ArrayList>(); // 当月 每天各个数值 数组
	List<ArrayList> value_Year = new ArrayList<ArrayList>(); // 当月 每天各个数值 数组

	List<ArrayList> value_Day = new ArrayList<ArrayList>(); // 当天各个数值

	Axis my_Axis; // 定义坐标轴控件元素view
	Paint paint;
	RadioButton[] ridobuttons;
	int layout_w, layout_h;

	int flag = 0;// 用于判断x轴标签

	int day_num = 3;
    int [] ColorTybe={Color.BLUE,Color.GREEN,Color.RED,Color.YELLOW,Color.BLUE,Color.GREEN};
	public boolean m_bneedupdate = true;
 
	// lsy Add
	private ArrayList<String> EqdSalId = null;
	private float  Y_max_value=0;
	private float  M_max_value=0;
	private float  D_max_value=0;
	private boolean  isDelete=false;
	//private boolean  isRun=false;
}
