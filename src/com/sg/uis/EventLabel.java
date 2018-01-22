package com.sg.uis;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import com.mgrid.data.EquipmentDataModel;
import com.mgrid.main.MainWindow;
import com.sg.common.CFGTLS;
import com.sg.common.IObject;
import com.sg.common.UtExpressionParser;
import com.sg.common.UtExpressionParser.stIfElseExpression;
import com.sg.common.UtExpressionParser.stIntervalExpression;

import android.R;
import android.animation.ArgbEvaluator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.sg.common.SgRealTimeData;
/** 设备告警统计标签 */ //fjw0312 fixme: 2016 8 2
public class EventLabel extends TextView implements IObject {

	public EventLabel(Context context) { 
		super(context); 

		m_rBBox = new Rect();
		

		Timer timer = new Timer(true);
		timer.schedule(new myTimer(), 1000, 1000);
	}

  private  Handler handler = new Handler(){ 
    	public void handleMessage(Message msg){
    		switch (msg.what){
    		case 1:
    			parseFontcolor(m_strContent);
    			EventLabel.this.setText(m_strContent);
    			EventLabel.this.setTextColor(m_cFontColor);
    			break;
    		default:
    			break;
    		}
    	}
    };
   
	private class myTimer extends TimerTask{
		ArrayList<String> equipListID_1=equipListID;		
		public void run() { 
			
			for (int i = 0; i < equipListID_1.size(); i++) {
				if("".equals(equipListID_1.get(i)))
				{
					equipListID.remove(i);
				}
			}
			if(equipListID.size()==0) return;
			String[] num=new String[equipListID.size()];
			//System.out.println("呵呵呵"+num.length);
			
			synchronized (EquipmentDataModel.equipID_eventNum_lst) {
				if(EquipmentDataModel.equipID_eventNum_lst==null) return;
				for(int i=0;i<num.length;i++)
				{
					num[i]= EquipmentDataModel.equipID_eventNum_lst.get(equipListID.get(i));					
				}
			}
			
			m_strContent="0";
			for(int i=0;i<num.length;i++)
			{
				
				if(num[i]==null)	
				{
					num[i]="0";
				}
				else if(num[i].equals(""))
				{
					num[i]="0";
				}
				m_strContent=Integer.parseInt(m_strContent)+Integer.parseInt(num[i])+"";
				
				//System.out.println("呵呵呵"+num[i]);
			}
			handler.sendEmptyMessage(1);
			
//			只能测试单个，上面是lsy改进的，可以测试多个 。还未验证，不知道能行不
//			if("".equals(equipID)) return;
//			String num="0"; 
//			 
//	
//			synchronized (EquipmentDataModel.equipID_eventNum_lst) {
//				if(EquipmentDataModel.equipID_eventNum_lst==null) return;
//				
//				num = EquipmentDataModel.equipID_eventNum_lst.get(equipID);
//			}
//		//-----
//		
//			if( ("".equals(num)==true)||num==null){
//				m_strContent= "0" ;			
//			}else{
//				m_strContent = num;
//			}
//	//		parseFontcolor(m_strContent);
//			
//		
//			handler.sendEmptyMessage(1);	

		}
	}
	
	@Override
	public void doLayout(boolean bool, int l, int t, int r, int b) {
		if (m_rRenderWindow == null)
			return;
		int nX = l + (int) (((float)m_nPosX / (float)MainWindow.FORM_WIDTH) * (r-l));
		int nY = t + (int) (((float)m_nPosY / (float)MainWindow.FORM_HEIGHT) * (b-t));
		int nWidth = (int) (((float)(m_nWidth) / (float)MainWindow.FORM_WIDTH) * (r-l));
		int nHeight = (int) (((float)(m_nHeight) / (float)MainWindow.FORM_HEIGHT) * (b-t));

		m_rBBox.left = nX;
		m_rBBox.top = nY;
		m_rBBox.right = nX+nWidth;
		m_rBBox.bottom = nY+nHeight;

		jj++;
		if (m_rRenderWindow.isLayoutVisible(m_rBBox)) {
			layout(nX, nY, nX+nWidth, nY+nHeight);
		}

	}
	
	public void onDraw(Canvas canvas) {
		if (m_rRenderWindow == null)
			return;
		if (m_rRenderWindow.isLayoutVisible(getBBox()) == false)
			return;
		
	//	this.setTextSize(m_fFontSize);
	//	this.setTextColor(m_cFontColor);

	
		super.onDraw(canvas);
	}
	
	public View getView() {
		return this; 
	}
	
	public int getZIndex()
	{ 
		return m_nZIndex;
	}
	
	@Override
	public void addToRenderWindow(MainWindow rWin) {
		m_rRenderWindow = rWin;
		rWin.addView(this);
	
	}

	@Override
	public void removeFromRenderWindow(MainWindow rWin) { 
		rWin.removeView(this);
	}

	public void parseProperties(String strName, String strValue, String strResFolder) {
	
        if ("ZIndex".equals(strName)) {
       	 	m_nZIndex = Integer.parseInt(strValue);
       	    if (MainWindow.MAXZINDEX < m_nZIndex) MainWindow.MAXZINDEX = m_nZIndex;
        }
        else if ("Location".equals(strName)) {
	       	String[] arrStr = strValue.split(",");
	        m_nPosX = Integer.parseInt(arrStr[0]);
	       	m_nPosY = Integer.parseInt(arrStr[1]);
	       
        }
        else if ("Size".equals(strName)) {
	       	String[] arrSize = strValue.split(",");
	        m_nWidth = Integer.parseInt(arrSize[0]);
	        m_nHeight = Integer.parseInt(arrSize[1]);
        }
        else if ("Alpha".equals(strName)) {
       	 	m_fAlpha = Float.parseFloat(strValue);
        }  
        else if ("RotateAngle".equals(strName)) {
        	m_fRotateAngle = Float.parseFloat(strValue);
        }
        else if ("Content".equals(strName)) {
        	m_strContent = strValue;
        	this.setText("0");
        }
        else if ("FontFamily".equals(strName))
        	m_strFontFamily = strValue;
        else if ("FontSize".equals(strName)) {
        	float fWinScale = (float)MainWindow.SCREEN_WIDTH / (float)MainWindow.FORM_WIDTH;
        	m_fFontSize = Float.parseFloat(strValue)*fWinScale;    
        	this.setTextSize(m_fFontSize);
        }
        else if ("IsBold".equals(strName))
       	 	m_bIsBold = Boolean.parseBoolean(strValue);
        else if ("FontColor".equals(strName)) {
       	 	m_cFontColor = Color.parseColor(strValue);
       	 	m_cStartFillColor = m_cFontColor;
       	 	 	
        }
        else if ("HorizontalContentAlignment".equals(strName))
       	 	m_strHorizontalContentAlignment = strValue;
        else if ("VerticalContentAlignment".equals(strName))
       	 	m_strVerticalContentAlignment = strValue;
        else if ("Expression".equals(strName)){
       	 	m_strExpression = strValue;
       	 	parseExpression(strValue);
        }
        else if("ColorExpression".equals(strName))
        	m_strColorExpression = strValue;  //字体颜色变化表达式

	}

	private void parseExpression(String str){ 
		
//		if("".equals(str)) return;  
//		String a[] = str.split("\\[|\\]"); //处理分隔符[ ]	
//		//System.out.println("哈哈哈哈哈哈哈哈哈哈"+str);
//		for (int i = 0; i < a.length; i++) {
//			//System.out.println("哈哈哈哈哈哈哈哈哈"+a[i]);
//		}
//		String b[] = a[2].split(":");
//
//		equipID = b[1];
		String[] arr=str.split("\\{|\\}");
		String[] arrs;
		arr=arr[1].split("\\|");
		for(String sm:arr)
		{
			arrs=sm.split("\\[|\\]|:");
//			for(String sd:arrs)
//			{
//				System.out.println(sd);
//			}
			equipListID.add(arrs[3]);
		}
//		for (int i = 0; i < equipListID.size(); i++) {
//			System.out.println("哈哈哈哈：："+equipListID.get(i));
//		}
		 
	}
	
	
	@Override
	public void initFinished()
	{
		int nFlag = Gravity.NO_GRAVITY;
		if ("Left".equals(m_strHorizontalContentAlignment))
			nFlag |= Gravity.LEFT;
		else if ("Right".equals(m_strHorizontalContentAlignment))
			nFlag |= Gravity.RIGHT;
		else if ("Center".equals(m_strHorizontalContentAlignment))
			nFlag |= Gravity.CENTER_HORIZONTAL;
		
		if ("Top".equals(m_strVerticalContentAlignment))
			nFlag |= Gravity.TOP;
		else if ("Bottom".equals(m_strVerticalContentAlignment))
		{
			nFlag |= Gravity.BOTTOM;
			double padSize = CFGTLS.getPadHeight(m_nHeight, MainWindow.FORM_HEIGHT, getTextSize());
			setPadding(0, (int) padSize, 0, 0);
		}
		else if ("Center".equals(m_strVerticalContentAlignment))
		{
			nFlag |= Gravity.CENTER_VERTICAL;
			double padSize = CFGTLS.getPadHeight(m_nHeight, MainWindow.FORM_HEIGHT, getTextSize())/2;
			setPadding(0, (int) padSize, 0, (int) padSize);
		}
		
		setGravity(nFlag);
	}

	public String getBindingExpression() {
		return "";
	}
	
	public void updateWidget() {	

	}
	
	@Override
	public boolean updateValue()
	{
		return false;

	}
	
	
	public int parseFontcolor(String strValue)
	{
		m_cFontColor = m_cStartFillColor;
		if( (m_strColorExpression == null)||("".equals(m_strColorExpression)) ) return -1;
		if( (strValue == null)||("".equals(strValue)) ) return -1;
		if("-999999".equals(strValue)) return -1;		


		if( (">".equals(m_strColorExpression.substring(0,1)))!=true ) return -1;

	
		String buf[] = m_strColorExpression.split(">"); //提取表达式中的条件与颜色单元
		for(int i=1;i<buf.length;i++){
			String a[] = buf[i].split("\\[|\\]"); //处理分隔符[ ]			

			float data = Float.parseFloat(a[0]); //获得比较值
			float value = Float.parseFloat(strValue); //输入值
			if(value > data){
				m_cFontColor = Color.parseColor(a[1]);
			}
		}		
		return m_cFontColor;
	}

	@Override
    public boolean needupdate()
    {
	    return false;
    }
	
	@Override
    public void needupdate(boolean bNeedUpdate)
    {
	   
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
	String m_strType = "EventLabel";
	
    int m_nZIndex = 1;
	int m_nPosX = 49;
	int m_nPosY = 306;
	int m_nWidth = 60;
	int m_nHeight = 30;
	float m_fAlpha = 1.0f;
	float m_fRotateAngle = 0.0f;
	String m_strContent = "****";
	String m_strFontFamily = "微软雅黑";
	float m_fFontSize = 12.0f;
	boolean m_bIsBold = false;
	int m_cFontColor = 0xFF008000;
	int m_cStartFillColor = 0x00000000;
	String m_strHorizontalContentAlignment = "Center";
	String m_strVerticalContentAlignment = "Center";
	String m_strExpression = "Binding{[Value[Equip:114-Temp:173-Signal:1]]}";
	String m_strColorExpression = ">20[#FF009090]>30[#FF0000FF]>50[#FFFF0000]>60[#FFFFFF00]";
	
	MainWindow m_rRenderWindow = null;	
	String m_strSignalValue = "";
	
	Rect m_rBBox = null;
	
	String equipID = "";
	ArrayList<String> equipListID=new ArrayList<String>();

	
	int jj = 0;
	
	public boolean m_bneedupdate = true;
}
