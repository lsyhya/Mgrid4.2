package com.sg.uis.LsyNewView;

import java.util.LinkedHashMap;
import java.util.LinkedList;

import org.xclcharts.chart.SplineChart;
import org.xclcharts.chart.SplineData;
import org.xclcharts.renderer.XEnum;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.demo.xclcharts.view.SplineChart03View;
import com.mgrid.data.DataGetter;
import com.mgrid.main.MainWindow;
import com.sg.common.CFGTLS;
import com.sg.common.IObject;

/**历史曲线图 */
@SuppressLint({ "ShowToast", "InflateParams", "RtlHardcoded", "ClickableViewAccessibility" })
public class HistoryCurveChart extends TextView implements IObject {

	private SplineChart schart;
	private LinkedList<SplineData> chartData = new LinkedList<SplineData>();
	//按下时手指的x坐标，按下时控件的x坐标
	private int lastX,viewX;
	public HistoryCurveChart(Context context) {
		super(context);
		
		
		this.setClickable(true);
		this.setPadding(0, 0, 0, 0);
	
		m_oPaint = new Paint();
		m_rBBox = new Rect();
		chart=new SplineChart03View(context);
		chart.setTouch(false); 
        this.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				
				
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					lastX=(int) event.getRawX();
					viewX=v.getLeft();
					break;

                case MotionEvent.ACTION_MOVE:
                	int dx=(int) (event.getRawX()-lastX);
                    int left=viewX+dx;
                    if(left<=m_rBBox.left)
                    {
                    	left=m_rBBox.left;
                    }
                    if(left>=m_rBBox.right-v.getWidth())
                    {
                    	left=m_rBBox.right-v.getWidth();
                    }
                    layout(left,m_rBBox.top, left+v.getWidth(), m_rBBox.bottom);
					break;
                case MotionEvent.ACTION_UP:
	
	                break;
				}
				return false;
			}
		});		
		


		initView();
	
	}
	

	@SuppressLint("DrawAllocation")
	protected void onDraw(Canvas canvas) {
		if (m_rRenderWindow == null)
			return;
		if (m_rRenderWindow.isLayoutVisible(getBBox()) == false)
			return;
        Paint p=new Paint();
        p.setStrokeWidth(2);
        p.setColor(Color.YELLOW);
        p.setStyle(Style.FILL);
        canvas.drawLine(getWidth()/2,0,getWidth()/2, getHeight(), p);     
        
	}
	
	
	private void  initView()
	{
		schart=chart.getChart();
        schart.setPadding(30, 30, 20, 20);
		schart.getCategoryAxis().getTickLabelPaint().setColor(Color.rgb(95, 119, 156));
		schart.getDataAxis().getTickLabelPaint().setColor(Color.rgb(95, 119, 156));
		//schart.getPlotGrid().getHorizontalLinePaint().setColor(Color.rgb(95, 119, 156));
		//schart.getPlotGrid().getHorizontalLinePaint().setStrokeWidth(1);
		setData();
		schart.setDataSource(chartData);	
	}

	private void setData() {
		
		//线1的数据集
		LinkedHashMap<Double,Double> linePoint1 = new LinkedHashMap<Double,Double>();
		linePoint1.put(0d, 0.5d);
		linePoint1.put(3d, 1.5d);
		linePoint1.put(6d, 3d);
		linePoint1.put(9d, 2d);
		linePoint1.put(12d, 1d);
		linePoint1.put(16d, 2d);
		linePoint1.put(18d, 2.5d);		
		linePoint1.put(21d, 0.5d);
		linePoint1.put(24d, 1.5d);	
		

		
		SplineData dataSeries1 = new SplineData("温度",linePoint1,
				(int)Color.rgb(151, 180, 198) );	
		//把线弄细点
		dataSeries1.getLinePaint().setStrokeWidth(3);
		dataSeries1.setDotStyle(XEnum.DotStyle.HIDE);	
//		dataSeries1.setLabelVisible(true);	
//		dataSeries1.getDotLabelPaint().setTextAlign(Align.LEFT);
		
		LinkedHashMap<Double,Double> linePoint2 = new LinkedHashMap<Double,Double>();
		linePoint2.put(0d, 3d);
		linePoint2.put(6d, 1d);

		linePoint2.put(12d, 2.5d);
		linePoint2.put(18d, 2d);		
		
		linePoint2.put(24d, 0.5d);	
		

		
		SplineData dataSeries2 = new SplineData("湿度",linePoint2,
				(int)Color.rgb(115, 174, 136) );
		
		
		
			
		dataSeries2.setDotStyle(XEnum.DotStyle.HIDE);				
		dataSeries2.getDotLabelPaint().setColor(Color.RED);
		dataSeries2.getLinePaint().setStrokeWidth(3);	
			
		//设定数据源		
		chartData.add(dataSeries1);				
		chartData.add(dataSeries2);	
		
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
			
			chart.layout(nX, nY, nX + nWidth, nY + nHeight);
			this.layout(nX+nWidth/2-10, nY, nX + nWidth/2+10, nY + nHeight);
		}
	}

	@Override
	public void addToRenderWindow(MainWindow rWin) {
		m_rRenderWindow = rWin;
		rWin.addView(chart);
		rWin.addView(this);
		
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
	SplineChart03View chart=null;
	//PieChart01View pieChart01View=null;
	private String newValue="";
    private String oldValue="";
    public boolean m_bneedupdate = true;
    private String mExpression = "";

}
