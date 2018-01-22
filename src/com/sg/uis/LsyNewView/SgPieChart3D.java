package com.sg.uis.LsyNewView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.xclcharts.chart.PieData;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.demo.xclcharts.view.PieChart3D01View;
import com.mgrid.data.DataGetter;
import com.mgrid.main.MainWindow;
import com.sg.common.CFGTLS;
import com.sg.common.IObject;

/** 3d饼图 */
@SuppressLint({ "ShowToast", "InflateParams", "RtlHardcoded", "ClickableViewAccessibility" })
public class SgPieChart3D extends TextView implements IObject {

	public SgPieChart3D(Context context) {
		super(context);
		
		m_oPaint = new Paint();
		m_rBBox = new Rect();
		pieChart3D01View=new PieChart3D01View(context);
	//	pieChart01View=new PieChart01View(context);
	}
	

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
			pieChart3D01View.layout(nX, nY, nX + nWidth, nY + nHeight);
		}
	}

	@Override
	public void addToRenderWindow(MainWindow rWin) {
		m_rRenderWindow = rWin;
		rWin.addView(this);
		rWin.addView(pieChart3D01View);
		
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
		} else if ("Alpha".equals(strName)) {
			m_fAlpha = Float.parseFloat(strValue);
		} else if ("BackgroundColor".equals(strName)) {
			if (strValue.isEmpty())
				return;
			m_cBackgroundColor = Color.parseColor(strValue);
			// this.setBackgroundColor(m_cBackgroundColor);
		} else if ("Content".equals(strName)) {
			m_strContent = strValue;
		
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
		//	this.setTextColor(m_cFontColor);
		} else if ("ClickEvent".equals(strName))
			m_strClickEvent = strValue;
		else if ("Url".equals(strName))
			m_strUrl = strValue;
		else if ("CmdExpression".equals(strName))
			m_strCmdExpression = strValue;

		else if ("HorizontalContentAlignment".equals(strName))
			m_strHorizontalContentAlignment = strValue;
		else if ("VerticalContentAlignment".equals(strName))
			m_strVerticalContentAlignment = strValue;
		else if ("Expression".equals(strName)) {
			 mExpression = strValue; 
			 parse_cmd();
             new Thread(new Runnable() {
				
				@Override
				public void run() {
					while(true)
					{
					    try {
							Thread.sleep(5000);
						} catch (InterruptedException e) {
							
							e.printStackTrace();
						}
						setData();
						
					}
					
				}
			}).start();
			 
		}
	}

	
	private void setData()
	{
		 List<PieData> data=new ArrayList<PieData>();
		 int i1=random.nextInt(100);
		 int i2=random.nextInt(100);
		// int i3=random.nextInt(100);
		// int i4=random.nextInt(100);
		 int all=i1+i2;//+i3+i4;
		 int j1=(int)((double)i1/all*100);
		 int j2=100-j1;
		// int j3=(int)((double)i3/all*100);
		// int j4=100-(j1+j2+j3);
		 data.add(new PieData("IT耗能",j1,
							(int)Color.rgb(253, 252, 250)) );
		 data.add(new PieData("其他耗能",j2,
					(int)Color.rgb(147, 146, 144)) );
		// data.add(new PieData("他",j3,
		//			(int)Color.rgb(241, 62, 1)) );
		// data.add(new PieData("她",j4,
		//			(int)Color.rgb(164, 233, 0)) );
		 pieChart3D01View.setData(data);
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
					MainWindow.FORM_HEIGHT, getTextSize()) / 2f;
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

	
	

	// fjw add 按钮控制命令功能的控制命令的绑定表达式解析
	// 解析出控件表达式，返回控件表达式类
	public boolean parse_cmd() {
		
		if(mExpression.equals("")||mExpression==null) return false;
		String[] arg1=mExpression.split("-");
		equail=arg1[0].split(":")[1];
		signal=arg1[2].split(":")[1].split("]")[0];
	
		return true;
	}

	@Override
	public void updateWidget() {
		
//		chart02View.setPercentage((int)Float.parseFloat(newValue));
//		chart02View.chartRender();
//		chart02View.invalidate();	  
	}

	@Override
	public boolean updateValue() {
		if(equail.equals("")||signal.equals(""))
			return false;
		newValue=DataGetter.getSignalMeaning(equail, signal);
		System.out.println(newValue);
		if(!newValue.equals(oldValue))
		{
			oldValue=newValue;
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
	String m_strClickEvent = "科士达-IDU系统设定UPS.xml";
	String m_strUrl = "www.baidu.com";
	String m_strCmdExpression = "Binding{[Cmd[Equip:1-Temp:173-Command:1-Parameter:1-Value:1]]}";
	String m_strHorizontalContentAlignment = "Center";
	String m_strVerticalContentAlignment = "Center";
	boolean m_bPressed = false;
	MainWindow m_rRenderWindow = null;
	String cmd_value = "";

	Paint m_oPaint = null;
	Rect m_rBBox = null;
	public static ProgressDialog dialog;

	// 记录触摸坐标，过滤滑动操作。解决滑动误操作点击问题。
	public float m_xscal = 0;
	public float m_yscal = 0;


	Intent m_oHomeIntent = null;
	
	private String signal="";	
	private String equail="";
	PieChart3D01View pieChart3D01View=null;
	//PieChart01View pieChart01View=null;
	private String newValue="";
    private String oldValue="";
    public boolean m_bneedupdate = true;
    private String mExpression = "";
	private Random random=new Random();

	

}
