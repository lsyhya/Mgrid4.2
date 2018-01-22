package com.sg.uis;

import java.text.DecimalFormat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.MotionEvent;
import android.view.View;

import com.mgrid.main.MainWindow;
import com.sg.common.IObject;
import com.sg.common.SgRealTimeData;
/**自定义表盘 made fjw 2016 04 25 */
public class Dial_C extends View implements IObject {
	public Dial_C(Context context) {  
        super(context); 
        this.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				return true;
			}
        });
        m_oPaint = new Paint();
        m_rRectF1 = new RectF();
        m_rRectF2 = new RectF();
        m_rRectF3 = new RectF(); 
        m_rBBox = new Rect();
       
    }
	
	@SuppressLint("DrawAllocation")
	protected void onDraw(Canvas canvas) {
		if (m_rRenderWindow == null)
			return;
		if (m_rRenderWindow.isLayoutVisible(getBBox()) == false)
			return;
		

        
        int nWidth = (int) (((float)(m_nWidth) / (float)MainWindow.FORM_WIDTH) * (m_rRenderWindow.VIEW_RIGHT - m_rRenderWindow.VIEW_LEFT));
		int nHeight = (int) (((float)(m_nHeight) / (float)MainWindow.FORM_HEIGHT) * (m_rRenderWindow.VIEW_BOTTOM - m_rRenderWindow.VIEW_TOP));

		float angle=0;
		if(((data_value-minValue)-(maxValue-minValue))>=1)
		{
			angle =270;
		}else
		{
			angle = 270/(maxValue-minValue) * (data_value-minValue);
		}
		int pad = m_nBorderWidth/2+4;  //外圆边距
		m_rRectF1.left = pad+m_nfillWidth/2;
		m_rRectF1.top = pad+m_nfillWidth/2;
		if(nWidth<nHeight){    //用小的一边长度   保证为圆不变形
			m_rRectF1.right = nWidth-pad-m_nfillWidth/2;;
			m_rRectF1.bottom = nWidth-pad-m_nfillWidth/2;
		}else{
			m_rRectF1.right = nHeight-pad-m_nfillWidth/2;
			m_rRectF1.bottom = nHeight-pad-m_nfillWidth/2;
		}
	
 
		if(scale>maxValue) scale=(int) maxValue;
		
			//画出外圆线
			m_rRectF2.left = m_rRectF1.left-m_nfillWidth/2;
			m_rRectF2.top = m_rRectF1.top-m_nfillWidth/2;
			m_rRectF2.right = m_rRectF1.right+m_nfillWidth/2;
			m_rRectF2.bottom = m_rRectF1.bottom+m_nfillWidth/2;
			m_oPaint.setColor(m_cBorderColor);
			m_oPaint.setAntiAlias(true); // 设置画笔的锯齿效果
			m_oPaint.setStrokeWidth(m_nBorderWidth);
			m_oPaint.setStyle(Paint.Style.STROKE);
	        canvas.drawOval(m_rRectF2, m_oPaint);  
	        
			//画出中间 覆盖 圆
			m_rRectF3.left = m_rRectF2.left+m_nBorderWidth/2;
			m_rRectF3.top = m_rRectF2.top+m_nBorderWidth/2;
			m_rRectF3.right = m_rRectF2.right-m_nBorderWidth/2;
			m_rRectF3.bottom = m_rRectF2.bottom-m_nBorderWidth/2;
			m_oPaint.setColor(m_cBackgroundColor);
	//		m_oPaint.setColor(m_cBorderColor); // 仅填充背景色
			m_oPaint.setStyle(Paint.Style.FILL);   
			canvas.drawOval(m_rRectF3, m_oPaint);	
	      
			if(warnPer!=0){
				 //画出告警圆弧
				int hh = 6;  //圆弧线条宽度 
				m_rRectF3.left = m_rRectF3.left+hh/2; 
				m_rRectF3.top = m_rRectF3.top+hh/2; 
				m_rRectF3.right = m_rRectF3.right-hh/2;
				m_rRectF3.bottom = m_rRectF3.bottom-hh/2;
				m_oPaint.setColor(warnPerColor); // 仅填充单色
				m_oPaint.setStrokeWidth(hh);
				m_oPaint.setStyle(Paint.Style.STROKE);   
//				canvas.drawOval(m_rRectF3, m_oPaint); 
				
				float angle_x = 270*warnPer;
				 canvas.drawArc(m_rRectF3, //弧线所使用的矩形区域大小   
						 	angle_x+135,  //开始角度   
						 	270-angle_x, //扫过的角度   
				            false, //是否使用中心     
				            m_oPaint); 				
			}


			float x_origin = (m_rRectF3.left+m_rRectF3.right)/(float)2.0;
			float y_origin = (m_rRectF3.top+m_rRectF3.bottom)/(float)2.0;
			float x_p = m_rRectF2.right - x_origin;
			float y_p = m_rRectF2.bottom - y_origin;
			
		//画出外圆刻度  粗刻度
			m_oPaint.setColor(m_cLineColor);		
			m_oPaint.setStrokeWidth(4);				
			canvas.save();
			canvas.translate(x_origin, y_origin);	
			canvas.rotate(45);
//			canvas.rotate(-270/scale);
			for(int i=0;i<=scale;i++){						 
				 
				 canvas.drawLine(0, y_p, 0, y_p-16, m_oPaint);	
				 canvas.rotate((float)270/(float)scale);
			}
			canvas.restore();
			//画出外圆刻度  细刻度
//			m_oPaint.setColor(Color.RED);			
			m_oPaint.setStrokeWidth(2);				 
			canvas.save();
			canvas.translate(x_origin, y_origin);
			canvas.rotate(45);
			float count2 = scale*10;
			for(int i=0;i<=count2;i++){						 				 
				 canvas.drawLine(0, y_p, 0, y_p-10, m_oPaint);	
				 canvas.rotate((float)270/(float)count2);
			}
			canvas.restore();
		
		//再覆盖一遍外圆	
			m_oPaint.setColor(m_cBorderColor); // 仅填充单色	
			m_oPaint.setStrokeWidth(m_nBorderWidth);
			m_oPaint.setStyle(Paint.Style.STROKE);
			m_oPaint.setAntiAlias(true); // 设置画笔的锯齿效果   
			if(m_rRectF2.bottom-m_rRectF2.top!=m_rRectF2.right-m_rRectF2.left){
				m_rRectF2.left = m_rRectF2.right+m_rRectF2.bottom-m_rRectF2.top;
			}
	        canvas.drawOval(m_rRectF2, m_oPaint);  
		//画出标签
			canvas.save();	
			canvas.translate(x_origin, y_origin);
			
			m_oPaint.setColor(m_cLineColor);
			m_oPaint.setAntiAlias(true); // 设置画笔的锯齿效果
			m_oPaint.setTextSize(16);
			m_oPaint.setStyle(Paint.Style.FILL);
			Path path = new Path();
			path.addArc(new RectF(-x_p, -y_p, x_p, y_p), 0, 360);		
		
			canvas.rotate(135);
			canvas.rotate(-270/scale);
			
		
			for(int i=0;i<=scale;i++){						 
				canvas.rotate(270/scale); 
				
//				 if(i%2==0){ //2个刻度显示一个标签
					 float label_value = minValue+( (maxValue-minValue)/scale *(float)i);
					 DecimalFormat decimalFloat =null;
				//	 System.out.println((maxValue-minValue)%scale);
					 
					 if((maxValue-minValue)*i%scale!=0)
					 decimalFloat = new DecimalFormat("0.0"); //float小数点精度处理 
					 else
					 decimalFloat = new DecimalFormat("0"); 
					 
					 String str = decimalFloat.format(label_value); 					 
					 canvas.drawTextOnPath(str, path, -5, 30, m_oPaint);					
//				 }
				 
			}
			canvas.restore(); 
			
		//画出三角指针
			canvas.save();	
			canvas.translate(x_origin, y_origin);
			m_oPaint.setColor(m_cFillColor);
			m_oPaint.setAntiAlias(true); // 设置画笔的锯齿效果			
			m_oPaint.setStyle(Paint.Style.FILL);
			canvas.rotate(45);
			canvas.rotate(angle);
			Path path2 = new Path();
			path2.moveTo(8, 0); 
			path2.lineTo(0, y_p*(float)0.7);
			path2.lineTo(-8, 0);
			path2.lineTo(8, 0);
			canvas.drawPath(path2, m_oPaint);

			canvas.restore();
			
	      //画出指针
			m_oPaint.setColor(Color.RED);			
			m_oPaint.setStrokeWidth(3);			
			canvas.save();
			canvas.translate(x_origin, y_origin);		
			canvas.rotate(45);
			canvas.rotate(angle);
//			canvas.drawLine(0, 0, 0, y_p*(float)0.7, m_oPaint);		
			
			
			canvas.restore();
			 
				 
			//画出中心点	
			m_oPaint.setColor(m_cBorderColor); // 仅填充单色		
			m_oPaint.setStyle(Paint.Style.FILL);   
			canvas.drawCircle(x_origin, y_origin, 12,  m_oPaint);
					
			

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
		if (m_rRenderWindow.isLayoutVisible(m_rBBox)) {
			layout(nX, nY, nX+nWidth, nY+nHeight);
		}
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
	       	m_oPaint.setAlpha((int)(m_fAlpha*255));
        }
        else if ("BackgroundColor".equals(strName)) {  //表盘基础背景色
        	m_cBackgroundColor = Color.parseColor(strValue);
        }
        else if ("BorderColor".equals(strName)) {     //表盘外圆圈颜色
        	m_cBorderColor = Color.parseColor(strValue);
        }
        else if ("FillColor".equals(strName)) {       //指针颜色
        		m_cFillColor = Color.parseColor(strValue);
        }
        else if ("LineColor".equals(strName)) {      //表盘刻度 数据颜色
        	m_cLineColor = Color.parseColor(strValue);
        }
        else if ("WarmPer".equals(strName)){      
        	if("".equals(strValue)){ 
        		warnPer = 0;
        	}else{
               	warnPer = Float.parseFloat(strValue);
        	}
        } 
        else if ("WarmPerColor".equals(strName)){     //告警圆弧区颜色
        	if("".equals(strValue)){
        		
        	}else{
            	warnPerColor = Color.parseColor(strValue);
        	}
        }
        else if ("Expression".equals(strName)) 
       	 	m_strExpression = strValue;
        else if ("ColorExpression".equals(strName))
        	m_strColorExpression = strValue;
        else if ("MaxValue".equals(strName)) 
        	maxValue = Integer.parseInt(strValue);
        else if ("MinValue".equals(strName))
        	minValue = Integer.parseInt(strValue);
        else if ("scale".equals(strName))
        {
        	scale = Integer.parseInt(strValue);
          
        }
        else if ("mode".equals(strName))
        	if("".equals(strValue)){ 
        		
        	}else{
        		mode = Integer.parseInt(strValue);
        	}
        	
	}

	@Override
	public void initFinished()
	{
	}

	public String getBindingExpression() {
		return m_strExpression;
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
	
	@Override
	public void updateWidget() {
		this.invalidate();
	}
	
	@Override
	public boolean updateValue()
	{
		m_bneedupdate = false;

		SgRealTimeData oRealTimeData = m_rRenderWindow.m_oShareObject.m_mapRealTimeDatas.get(this.getUniqueID());
		if (oRealTimeData == null)
			return false;
		String strValue = oRealTimeData.strValue;
		if (strValue == null || "".equals(strValue) == true)
			return false;
		
		int nValue = 0;
		try {
			nValue = Integer.parseInt(strValue);
		}catch(Exception e) {
			
		}
		
		if(strValue.equals(oldSignalValue)){
			return false;
        }else{
        	oldSignalValue = strValue;
 //    	 	parseFontcolor(oRealTimeData.strData);   //解析数值颜色表达式 fjw add	    
     	 	if("".equals(oRealTimeData.strData)) return false;
     	 	data_value = Float.parseFloat(oRealTimeData.strData);
	    	return true;
        }

	}
	//颜色解析函数  传入参数：显示值   fjw add
	public int parseFontcolor(String strValue)  
	{
		m_cSingleFillColor = m_cStartFillColor;
		if( (m_strColorExpression == null)||("".equals(m_strColorExpression)) ) return -1;
		if( (strValue == null)||("".equals(strValue)) ) return -1;
		if("-999999".equals(strValue)) return -1;		
//		
		if( (">".equals(m_strColorExpression.substring(0,1)))!=true ) return -1;

	
		String buf[] = m_strColorExpression.split(">"); //提取表达式中的条件与颜色单元
		for(int i=1;i<buf.length;i++){
			String a[] = buf[i].split("\\[|\\]"); //处理分隔符[ ]			

			//比较数值	
			float data = Float.parseFloat(a[0]); //获得比较值
			float value = Float.parseFloat(strValue); //输入值
			if(value > data){
				m_cSingleFillColor = Color.parseColor(a[1]);
			}
		}		
		return m_cSingleFillColor;
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
	
	public View getView() {
		return this;
	}
	
	public int getZIndex()
	{
		return m_nZIndex;
	}
	
	public Rect getBBox() {
		return m_rBBox;
	}
	
// params:
	String m_strID = "";
	String m_strType = ""; 
    int m_nZIndex = 4;
	int m_nPosX = 349;
	int m_nPosY = 78;
	int m_nWidth = 200;
	int m_nHeight = 150;
	float m_fAlpha = 1.0f;
	float m_fRotateAngle = 0.0f;
	int m_cBackgroundColor = 0xFFFFFFFF;
	int m_cBorderColor = 0xFF000000;

	int m_cFillColor = 0xFFF2C0FF;
	int m_cLineColor = 0xFF000000;
	boolean m_bIsDashed = false;
	String m_strExpression = "Binding{[Value[Equip:114-Temp:173-Signal:1]]}";
	String m_strColorExpression = ">20[#FF009090]>30[#FF0000FF]>50[#FFFF0000]>60[#FFFFFF00]";
	
	int m_cSingleFillColor = 0xFF0000FF;
	int m_cStartFillColor = 0x00000000;
	float[] m_arrGradientColorPos = null;
	int[] m_arrGradientFillColor = null;
	boolean m_bIsHGradient = false; // 水平渐变
	 
	float maxValue = 100;  //表盘的最大值
	float minValue = 0;  //表盘的最小值
	int scale = 5;      //表盘的刻度
	int mode = 1;        //表盘样式
	float data_value = 10; //目前的表盘数值
	int m_nBorderWidth = 10;  //线条宽度
	int m_nfillWidth = 30;  //填充的圆环宽度
	String str_value="";
	float warnPer = (float)0.8;  //告警圆环阀值开始比例
	int warnPerColor = 0xFFFF0000;   //告警圆环颜色
	
	MainWindow m_rRenderWindow = null;
	Paint m_oPaint = null;  
	RectF m_rRectF1 = null;
	RectF m_rRectF2 = null;
	RectF m_rRectF3 = null;
	Rect m_rBBox = null;
	public boolean m_bneedupdate = true;
	String oldSignalValue = "";
}
