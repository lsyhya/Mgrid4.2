
package com.sg.uis;

import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import com.sg.common.UtExpressionParser.stExpression;

/** 仪表盘 */
public class SgAmmeter extends View implements IObject {
	
	private RectF m_rRectF1,m_rRectF2,m_rRectF3;
	int m_nBorderWidth = 10;
	int m_nfillWidth = 30;  //填充的圆环宽度
	
	public SgAmmeter(Context context) {
		super(context);
		this.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				return true;
			}
        });
		m_rBBox = new Rect();
		try {
			AssetManager assetManager = this.getContext().getResources().getAssets();
			InputStream is = null;

			if (null == m_oBackImage)
			{
				is = assetManager.open("ui/round.png");
				m_oBackImage = BitmapFactory.decodeStream(is);
				is.close();
			}

			if (null == m_oFrontImage)
			{
				is = assetManager.open("ui/Ammeter_Front.png");
				m_oFrontImage = BitmapFactory.decodeStream(is);
				is.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		m_oPaint = new Paint();
		m_oPaint.setTextSize(m_fFontSize);
		m_oPaint.setColor(Color.BLACK);
		m_oPaint.setAntiAlias(true); // 设置画笔的锯齿效果
		m_oPaint.setStyle(Paint.Style.STROKE); 
        m_rRectF1 = new RectF();
        m_rRectF2 = new RectF();
		m_rRectF3 = new RectF(); 
	}

	@Override
	protected void onDraw(Canvas canvas) {
		if (m_rRenderWindow == null)
			return;
		if (m_rRenderWindow.isLayoutVisible(getBBox()) == false)
			return;
		
		int nWidth = (int) (((float)(m_nWidth) / (float)MainWindow.FORM_WIDTH) * (m_rRenderWindow.VIEW_RIGHT - m_rRenderWindow.VIEW_LEFT));
		int nHeight = (int) (((float)(m_nHeight) / (float)MainWindow.FORM_HEIGHT) * (m_rRenderWindow.VIEW_BOTTOM - m_rRenderWindow.VIEW_TOP));


		
		float angle = 270/(maxValue-minValue) * (data_value-minValue);
		int pad = m_nBorderWidth/2+4;  //外圆边距
		m_rRectF1.left = pad+m_nfillWidth/2;
		m_rRectF1.top = pad+m_nfillWidth/2;
		if(nWidth<nHeight){    //用小的一边长度   保证为圆不变形
			m_rRectF1.right = m_oBackImage.getWidth()-pad-m_nfillWidth/2;;
			m_rRectF1.bottom = m_oBackImage.getWidth()-pad-m_nfillWidth/2;
		}else{
			m_rRectF1.right = m_oBackImage.getHeight()-pad-m_nfillWidth/2;
			m_rRectF1.bottom = m_oBackImage.getHeight()-pad-m_nfillWidth/2;
		}
	
	
		
 
		if(scale>maxValue) scale=(int) maxValue;
		
			//画出外圆线
			m_rRectF2.left = m_rRectF1.left-m_nfillWidth/2;
			m_rRectF2.top = m_rRectF1.top-m_nfillWidth/2;
			m_rRectF2.right = m_rRectF1.right+m_nfillWidth/2;
			m_rRectF2.bottom = m_rRectF1.bottom+m_nfillWidth/2;
			
			float fScaleX = (float)nWidth / (float)m_oBackImage.getWidth();
			float fScaleY = (float)nHeight / (float)m_oBackImage.getHeight();
			canvas.scale(fScaleX, fScaleY);
			// draw back image
			canvas.drawBitmap(m_oBackImage, 0.0f,0.0f, m_oPaint);
			
			
			m_oPaint.setColor(m_cBorderColor);
			m_oPaint.setAntiAlias(true); // 设置画笔的锯齿效果
			m_oPaint.setStrokeWidth(m_nBorderWidth);
			m_oPaint.setStyle(Paint.Style.STROKE);
	  //      canvas.drawOval(m_rRectF2, m_oPaint);  
			 canvas.drawArc(m_rRectF2, //弧线所使用的矩形区域大小   
					 	135,  //开始角度   
					 	270, //扫过的角度   
			            false, //是否使用中心     
			            m_oPaint); 	
	        
	        
	        
	        
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
//			m_oPaint.setStrokeWidth(2);				 
//			canvas.save();
//			canvas.translate(x_origin, y_origin);
//			canvas.rotate(45);
//			float count2 = scale*10;
//			for(int i=0;i<=count2;i++){						 				 
//				 canvas.drawLine(0, y_p, 0, y_p-10, m_oPaint);	
//				 canvas.rotate((float)270/(float)count2);
//			}
//			canvas.restore();
		
		//再覆盖一遍外圆	
			m_oPaint.setColor(m_cBorderColor); // 仅填充单色	
			m_oPaint.setStrokeWidth(m_nBorderWidth);
			m_oPaint.setStyle(Paint.Style.STROKE);
			m_oPaint.setAntiAlias(true); // 设置画笔的锯齿效果   
			if(m_rRectF2.bottom-m_rRectF2.top!=m_rRectF2.right-m_rRectF2.left){
				m_rRectF2.left = m_rRectF2.right+m_rRectF2.bottom-m_rRectF2.top;
			}
			 canvas.drawArc(m_rRectF2, //弧线所使用的矩形区域大小   
					 	135,  //开始角度   
					 	270, //扫过的角度   
			            false, //是否使用中心     
			            m_oPaint); 	  
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


	
	
		
//		Paint p=new Paint();
//		if(warnPer!=0){
//			 //画出告警圆弧
//			int hh = 10;  //圆弧线条宽度 
//			p.setColor(Color.GREEN);
//			p.setStrokeWidth(hh);
//			p.setStyle(Paint.Style.STROKE);   
//			p.setAntiAlias(true);
//			float angle_x = 270*warnPer;
//			canvas.drawArc(m_rRectF1, //弧线所使用的矩形区域大小   
//				 	135,  //开始角度   
//				 	angle_x, //扫过的角度   
//		            false, //是否使用中心     
//		            p); 			
//			p.setColor(Color.RED); // 仅填充单色			
//			canvas.drawArc(m_rRectF1, //弧线所使用的矩形区域大小   
//					 	angle_x+135,  //开始角度   
//					 	270-angle_x, //扫过的角度   
//			            false, //是否使用中心     
//			            p); 				
//	    }
//
//	
//		
//		// 指针移到中心位置(减m_oFrontImage.getHeight()/2是为了让指针刚好在中心位置)
//		canvas.translate(m_oBackImage.getWidth()/2.0f, m_oBackImage.getHeight()/2.0f-m_oFrontImage.getHeight()/2.0f);
//		
//
//		// draw Gradations
//		drawGradations(canvas, -m_oBackImage.getWidth()/2.0f*0.56f, 0.0f, m_oFrontImage.getHeight()/2.0f);
//		// draw pointer
//		canvas.rotate(-25.0f, 0.0f, m_oFrontImage.getHeight()/2.0f);
//		float fAngle = m_fCurrentValue / m_fMeasure * m_nTotalAngle;
//		canvas.rotate(fAngle, 0.0f, m_oFrontImage.getHeight()/2.0f);
//		canvas.drawBitmap(m_oFrontImage, 0.0f,0.0f, m_oPaint);
		
		 
		
	}

//	// 绘制刻度
//	private void drawGradations(Canvas canvas, float fY, float CenterXOffset, float CenterYOffset) {
//		DecimalFormat decimalFormat = new DecimalFormat("###.#");
//		canvas.rotate(-2*66, CenterXOffset, CenterYOffset);
//		//Log.v("Warnning", "仪表盘 = "+m_fMeasure);
//		for (int i = 0; i < 9; ++i) {
//			if (i % 2 == 0) {
//				String valueString = String.valueOf(decimalFormat.format((float)i * m_fMeasure / 8.0f));
//				canvas.drawText(valueString, -3.0f*valueString.length(), fY, m_oPaint);
//				//canvas.drawLine(0, -m_oBackImage.getWidth(), 0, (float) (-m_oBackImage.getWidth()*0.2), m_oPaint);
//			}
//			canvas.rotate(33.0f, CenterXOffset, CenterYOffset);
//		}		
//	}
	
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
		if (m_oBackImage != null && m_oBackImage.isRecycled() == false)
			m_oBackImage.recycle();
		if (m_oFrontImage != null && m_oFrontImage.isRecycled() == false)
			m_oFrontImage.recycle();
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
        else if ("CurrentValue".equals(strName))
        	data_value = Float.parseFloat(strValue);
        else if ("Measure".equals(strName))
        	maxValue = Float.parseFloat(strValue);
        else if ("CurrentUnit".equals(strName))
        	m_strCurrentUnit = strValue;
        else if ("Expression".equals(strName)) {
        	m_strExpression = strValue;
        }
        else if ("WarmPer".equals(strName)) {
        	if(strValue!=null&&!strValue.equals(""))
        		warnPer = Float.parseFloat(strValue);      	
        }
        else if ("BorderColor".equals(strName)) {
        	if(strValue!=null&&!strValue.equals(""))
        		m_cBorderColor = Color.parseColor(strValue);      	
        }
        else if ("FillColor".equals(strName)) {
        	if(strValue!=null&&!strValue.equals(""))
        		m_cFillColor =Color.parseColor(strValue);
        }
        else if ("LineColor".equals(strName)) {
        	if(strValue!=null&&!strValue.equals(""))
        		m_cLineColor = Color.parseColor(strValue);
       }
        else if ("scale".equals(strName)) {
        	if(strValue!=null&&!strValue.equals(""))
        		scale = Integer.parseInt(strValue);
       }
       else if ("WarmPerColor".equals(strName)) {
        	if(strValue!=null&&!strValue.equals(""))
        		warnPerColor = Color.parseColor(strValue);
       }
	}

	@Override
	public void initFinished()
	{
	}

	public String getBindingExpression() {
		return m_strExpression;
	}
	
	// 设备更新
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
		
		float fValue = -1.0f;
		try {
			fValue = Float.parseFloat(strValue);
		}catch(Exception e) {
			
		}
       
        // 数值变化时才刷新页面
        if (fValue != data_value) {
        	setCurrentValue(fValue);
        	return true;
        }
        
        return false;
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
	
	public void setCurrentValue(float fValue) {
		data_value = fValue;
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
    int m_nZIndex = 13;
	int m_nPosX = 300;
	int m_nPosY = 397;
	int m_nWidth = 150;
	int m_nHeight = 137;
	float m_fAlpha = 1.0f;
	
	float minValue=0.0f;
	float data_value = 0.0f;
	float maxValue = 1500.0f;
	String m_strCurrentUnit = "KV";
	String m_strExpression = "Binding{[Value[Equip:119-Temp:167-Signal:2]]}";
	float warnPer=0.6f;
	MainWindow m_rRenderWindow = null;
	
	stExpression m_oMathExpression = null;
	
	private static Bitmap m_oBackImage = null;
	private static Bitmap m_oFrontImage = null;
	
	Rect m_rBBox = null;
	
	Paint m_oPaint = null;
	float m_fFontSize = 18.0f;
	float m_nTotalAngle = 40f*8;
	
	int m_cBorderColor=Color.parseColor("#FF6066D5");
	int m_cBackgroundColor=Color.parseColor("#00000000");
	int warnPerColor=Color.RED;
	int m_cLineColor=Color.parseColor("#FF6066D5");
	int m_cFillColor=Color.parseColor("#FF6066D5");
//    <Property Name="BorderColor" Value="#FFFF80C0" />
//    <Property Name="FillColor" Value="#FFFFFF00" />
//    <Property Name="LineColor" Value="#FF00FFFF" />
	
	public boolean m_bneedupdate = true;
	int scale=10;
}
