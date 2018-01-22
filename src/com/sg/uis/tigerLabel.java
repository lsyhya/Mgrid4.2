package com.sg.uis;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

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

import com.mgrid.main.MainWindow;
import com.sg.common.CFGTLS;
import com.sg.common.IObject;
import com.sg.common.SgRealTimeData;
import com.sg.common.UtExpressionParser;
import com.sg.common.UtExpressionParser.stBindingExpression;
import com.sg.common.UtExpressionParser.stExpression;

import comm_service.service;

import data_model.ipc_cfg_trigger_value;  


//fjw made 告警阀值回显标签  author made by：fjw0312
//date:2016 4 18
public class tigerLabel extends TextView implements IObject {

	public tigerLabel(Context context) {
		// TODO Auto-generated constructor stub
		super(context); 
		this.setClickable(false);
		this.setBackgroundColor(0x00000000);
		m_rBBox = new Rect();
	//	mythread.start();
	}


		@Override
		public void doLayout(boolean bool, int l, int t, int r, int b) {
			if (m_rRenderWindow == null)
				return;
			int nX = l + (int) (((float)m_nPosX / (float)MainWindow.FORM_WIDTH) * (r-l));
			int nY = t + (int) (((float)m_nPosY / (float)MainWindow.FORM_HEIGHT) * (b-t));
			int nWidth = (int) (((float)m_nWidth / (float)MainWindow.FORM_WIDTH) * (r-l));
			int nHeight = (int) (((float)m_nHeight / (float)MainWindow.FORM_HEIGHT) * (b-t));

			m_rBBox.left = nX;
			m_rBBox.top = nY;
			m_rBBox.right = nX+nWidth;
			m_rBBox.bottom = nY+nHeight;
			if (m_rRenderWindow.isLayoutVisible(m_rBBox)) {
				layout(nX, nY, nX+nWidth, nY+nHeight);
			}
		}
		
		public void onDraw(Canvas canvas) {
			if (m_rRenderWindow == null)
				return;
			if (m_rRenderWindow.isLayoutVisible(getBBox()) == false)
				return;
			
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
	        	this.setText(m_strContent);
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
	       	 	this.setTextColor(m_cFontColor);     	 	
	        }
	        else if ("HorizontalContentAlignment".equals(strName))
	       	 	m_strHorizontalContentAlignment = strValue;
	        else if ("VerticalContentAlignment".equals(strName))
	       	 	m_strVerticalContentAlignment = strValue;
	        else if ("Expression".equals(strName)){ 
	        	m_strExpression = strValue;
	        	parse_banding();
	        }	       	 	
	        else if("ColorExpression".equals(strName))
	        	m_strColorExpression = strValue;  //字体颜色变化表达式
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
//			return "";
			return m_strExpression;
		}
		
		public void updateWidget() {	
//			Log.e("tigerLabel->updateWidget","into！");  
			this.setTextColor(m_cFontColor); 
			this.setText(m_strContent);
			this.invalidate();
		}
		
		
	    public 	Handler handler=new Handler()
		{
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 0:
					
					tigerLabel.this.setText(m_strContent);
					
					
					break;
				}
			};
		};
		
		

		
		@Override
		public boolean updateValue()
		{
			//注意在updateValue里面不应该对view的属性经行操作，因为属性操作本来就会调用updateValue. eg:setTextColor()
			m_bneedupdate = false;
//			Log.e("tigerLabel->updateValue","into！");  
			String strValue = "";
			float f_value = 0;
			//获取告警阀值
			List<ipc_cfg_trigger_value> triglst = service.get_cfg_trigger_value("127.0.0.1", 9630, bt_EquipID);
			if(triglst==null) return false;
			Iterator<ipc_cfg_trigger_value> iter = triglst.iterator();
			while(iter.hasNext()){
				ipc_cfg_trigger_value clas_cfg = iter.next();
				f_value = clas_cfg.startvalue;
				int c_equipID = clas_cfg.equipid;
				int c_event = clas_cfg.eventid;
				int c_condictoin = clas_cfg.conditionid; 				
				if( (bt_EquipID==c_equipID)&&(bt_EventID==c_event)&&(bt_Condition==c_condictoin) )
				strValue = String.valueOf(f_value);
			}
			
			if (strValue == null || "".equals(strValue) == true)
				return false;
//			Log.e("label-updataValue更新值strValue=：",strValue);	
	        // 内容变化才刷新页面
	        if (m_strSignalValue.equals(strValue) == false) {
	        	m_strSignalValue = strValue;     //保存数值留作下次比较  	        	
	        	m_strContent = strValue;      //界面数值赋予
//	        	this.setText(m_strContent);
	        	
//	        	parseFontcolor(strValue);   //解析数值颜色表达式 fjw add
				return true;
	        }
			
			return false;
		}
		
		//解析出控件表达式，返回控件表达式类
		public boolean parse_banding(){
//			if("".equals(m_strCmdExpression)) return false;
//			if(m_strCmdExpression==null) return false;
			stExpression oMathExpress = UtExpressionParser.getInstance().parseExpression(m_strExpression);
			if (oMathExpress != null) {		
				//遍历控件表达式各个数据单元表达式类
				Iterator<HashMap.Entry<String, stBindingExpression>> it = oMathExpress.mapObjectExpress.entrySet().iterator();
				while(it.hasNext())
				{
					stBindingExpression oBindingExpression = it.next().getValue();
					bt_EquipID = oBindingExpression.nEquipId;
					bt_EventID = oBindingExpression.nEventId;
					bt_Condition = oBindingExpression.nConditionId;
				}
			}
		 return true;
		}
		
		//颜色解析函数  传入参数：显示值   fjw add
		public int parseFontcolor(String strValue)
		{
			m_cFontColor = m_cStartFillColor;
			if( (m_strColorExpression == null)||("".equals(m_strColorExpression)) ) return -1;
			if( (strValue == null)||("".equals(strValue)) ) return -1;
			if("-999999".equals(strValue)) return -1;		

//			Log.e("Label-updataValue", "into!"+"--"+m_strColorExpression.substring(0,1));
			if( (">".equals(m_strColorExpression.substring(0,1)))!=true ) return -1;

		
			String buf[] = m_strColorExpression.split(">"); //提取表达式中的条件与颜色单元
			for(int i=1;i<buf.length;i++){
				String a[] = buf[i].split("\\[|\\]"); //处理分隔符[ ]			
//				Log.e("Label-updataValue", "比较值"+a[0]+"+颜色数值："+a[1]);
				//比较数值	
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
		    return m_bneedupdate;
	    }
		
		@Override
	    public void needupdate(boolean bNeedUpdate)
	    {
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
		public String m_strID = "";
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
		int m_cStartFillColor = 0x00000000;
		String m_strHorizontalContentAlignment = "Center";
		String m_strVerticalContentAlignment = "Center";
		String m_strExpression = "Binding{[Value[Equip:114-Temp:173-Signal:1]]}";
		String m_strColorExpression = ">20[#FF009090]>30[#FF0000FF]>50[#FFFF0000]>60[#FFFFFF00]";
		
		MainWindow m_rRenderWindow = null;	
		String m_strSignalValue = "";
		
		Rect m_rBBox = null;
		
		int bt_EquipID=1;
		int bt_EventID=1;
		int bt_Condition =1;
		
		public boolean m_bneedupdate = true;
}


