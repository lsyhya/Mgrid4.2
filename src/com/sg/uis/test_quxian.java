
package com.sg.uis;



import com.sg.common.SgRealTimeData;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Paint.Style;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RadioButton;

import com.mgrid.main.MainWindow;
import com.sg.common.IObject;


/**写死 展示曲线**/
//author：fjw0312
//made time：2015.12.29
//该控件采集
public class test_quxian extends View implements IObject {
	
	public test_quxian(Context context) {
		super(context);
		
		m_rBBox = new Rect(); //新建一个矩形类给予空间		
		m_oPaint = new Paint();   //赋予画笔类空间
		m_2Paint = new Paint();   //赋予画笔类空间 画点和曲线
		m_oPaint.setTextSize(m_fFontSize); //设置画笔线条大小
		m_oPaint.setColor(m_nFontColor);   //设置画笔颜色
		m_oPaint.setAntiAlias(true); // 设置画笔的锯齿效果
		m_oPaint.setStyle(Paint.Style.STROKE); //设置画笔风格
		max_value = new float[4];
		for(int i=0;i<4;i++){
			max_value[i] = 1;
		}
		
		map_Htime_vlaue = new HashMap<Integer,String>();  //保存小时的时间——数值
		map_Dtime_vlaue = new HashMap<Integer,String>();  //保存每天的时间——数值
		map_Mtime_vlaue = new HashMap<Integer,String>();  //保存每月的时间——数值
		map_Ytime_vlaue = new HashMap<Integer,String>();  //保存每年的时间——数值
		Htimelist = new ArrayList<Integer>();  //保存小时的时间
		Dtimelist = new ArrayList<Integer>();  //保存每天的时间
		Mtimelist = new ArrayList<Integer>();  //保存每月的时间
		Ytimelist = new ArrayList<Integer>();  //保存每年的时间
		//赋予曲线的数值
		for(int i=0;i<300;i+=20){
			Htimelist.add(i);
			float v = (float)0.6+ (float)0.0005*i;
			String s= String.valueOf(v);
			map_Htime_vlaue.put(i, s);
		}
		for(int i=300;i<900;i+=20){
			Htimelist.add(i);
			float v = (float)0.75 - (float)0.0005*(i-300);
			String s= String.valueOf(v);
			map_Htime_vlaue.put(i, s);
		}
		for(int i=900;i<2000;i+=20){
			Htimelist.add(i);
			float v = (float)0.45 + (float)0.0004*(i-900);
			String s= String.valueOf(v);
			map_Htime_vlaue.put(i, s);
		}
		for(int i=2000;i<3600;i+=20){
			Htimelist.add(i);
			float v = (float)0.89 - (float)0.0003*(i-2000);
			String s= String.valueOf(v);
			map_Htime_vlaue.put(i, s);
		}
		
		for(int i=0;i<200;i+=10){
			Dtimelist.add(i);
			float v = (float)0.6+ (float)0.0005*i;
			String s= String.valueOf(v);
			map_Dtime_vlaue.put(i, s);
		}
		for(int i=200;i<250;i+=10){
			Dtimelist.add(i);
			float v = (float)0.7+ (float)0.004*(i-200);
			String s= String.valueOf(v);
			map_Dtime_vlaue.put(i, s);
		}
		for(int i=250;i<280;i+=10){
			Dtimelist.add(i);
			float v = (float)0.9- (float)0.01*(i-250);
			String s= String.valueOf(v);
			map_Dtime_vlaue.put(i, s);
		}
		for(int i=280;i<1000;i+=10){
			Dtimelist.add(i);
			float v = (float)0.6- (float)0.0001*(i-280);
			String s= String.valueOf(v);
			map_Dtime_vlaue.put(i, s);
		}
		for(int i=1000;i<1010;i+=10){
			Dtimelist.add(i);
			float v = (float)0.528- (float)0.03*(i-1000);
			String s= String.valueOf(v);
			map_Dtime_vlaue.put(i, s);
		}
		for(int i=1010;i<1014;i+=10){
			Dtimelist.add(i);
			float v = (float)0.228+ (float)0.08*(i-1010);
			String s= String.valueOf(v);
			map_Dtime_vlaue.put(i, s);
		}
		for(int i=1014;i<1440;i+=10){
			Dtimelist.add(i);
			float v = (float)0.548+ (float)0.0002*(i-1014);
			String s= String.valueOf(v);
			map_Dtime_vlaue.put(i, s);
		}
		
		for(int i=0;i<31;i++){
			Mtimelist.add(i);
			float v = (float)0.6;
			if(i==6 || i==13 ||i==18||i==22) v = v+(float)0.18;
			if(i==2||i==16||i==21) v = v-(float)0.26;
			if(i==28) v = v+(float)0.09;
			String s= String.valueOf(v);
			map_Mtime_vlaue.put(i, s);
		}
		for(int i=0;i<12;i++){
			Ytimelist.add(i);
			float v = (float)0.6;
			if(i==2) v = v-(float)0.06;
			if(i==6) v = v+(float)0.10;
			if(i==7) v = v+(float)0.12;
			if(i==8) v = v+(float)0.11;
			if(i==11) v = v-(float)0.09;
			if(i==11) v = v-(float)0.1;
			String s= String.valueOf(v);
			map_Ytime_vlaue.put(i, s);
		}

		
		//定义选择按纽组 按钮数4个
		ridobuttons = new RadioButton[4];
		ridobuttons[0] = new RadioButton(context);
		ridobuttons[0].setText("1 h");
		ridobuttons[0].setChecked(true);
		ridobuttons[1] = new RadioButton(context);
		ridobuttons[1].setText("24 h");
		ridobuttons[2] = new RadioButton(context);
		ridobuttons[2].setText("1 mon");
		ridobuttons[3] = new RadioButton(context);
		ridobuttons[3].setText("1 year");
		for(int i=0;i<4;i++){
			ridobuttons[i].setTextColor(Color.BLACK);
			ridobuttons[i].setOnClickListener(l);				
/*				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					String strText = (String) ((RadioButton) arg0).getText();
					if("1 h".equals(strText)){
						mode = 0;
						
					}else if("24 h".equals(strText)){
						mode = 1;
					}else if("1 mon".equals(strText)){
						mode = 2;
					}else if("1 year".equals(strText)){
						mode = 3;
					}
					ridobuttons[mode].setChecked(true);
					
				}
			});
*/		}			
	}
	private OnClickListener l = new OnClickListener() {				
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			String strText = (String) ((RadioButton) arg0).getText();
			for(int i=0;i<4;i++)
				ridobuttons[i].setChecked(false);
			if("1 h".equals(strText)){
				mode = 0;				
			}else if("24 h".equals(strText)){
				mode = 1;
			}else if("1 mon".equals(strText)){
				mode = 2;
			}else if("1 year".equals(strText)){
				mode = 3;
			}
			ridobuttons[mode].setChecked(true);
			updateWidget(); //该view 请求重新draw()，只会draw该view
		}
	};

	@Override //画布绘制
	protected void onDraw(Canvas canvas) {
		if (m_rRenderWindow == null)
			return;
		if (m_rRenderWindow.isLayoutVisible(getBBox()) == false)
			return;
		
		Log.w("SignalCurve","into onDraw!");
		//画出标题
//		m_oPaint.setColor(Color.WHITE);   //设置画笔颜色
//		m_oPaint.setTextSize(25); //设置画笔线条大小
//		canvas.drawText("动态历史曲线",m_nHeight/2, 25, m_oPaint); // 画y轴刻度
		
		m_oPaint.setColor(m_nLineColor);   //设置画笔颜色
		m_oPaint.setTextSize(20); //设置画笔线条大小
		int jg = 40;  //坐标轴与边沿的距离
		int kel = 5;  //刻度长度
		//画出坐标轴
		canvas.drawLine(jg, m_nHeight-jg, m_nWidth-jg, m_nHeight-jg, m_oPaint); // 画线x轴
		canvas.drawLine(jg, m_nHeight-jg, jg, jg, m_oPaint);    // 画线y轴
		//画出刻度点
		int num = 12+1;
		if(mode==1){
			num = 24+1;
		}else if(mode==2){
			num = 31+1;
		}else if(mode==3){
			num = 12+1;
		}
		int x_unit = (m_nWidth-2*jg-jg/2)/(num-1);  //x坐标轴的有效长度m_nWidth-2*jg-jg/2
		int y_unit = (m_nHeight-2*jg-jg/2)/10; //y坐标轴的有效长度m_nHeight-2*jg-jg/2
		for(int i=0;i<11;i++){
			canvas.drawLine(jg, m_nHeight-jg-y_unit*i, jg+kel, m_nHeight-jg-y_unit*i, m_oPaint); // 画y轴刻度线
		}
		for(int i=0;i<num;i++){
			canvas.drawLine(x_unit*i+jg, m_nHeight-jg, x_unit*i+jg, m_nHeight-jg-kel, m_oPaint); // 画x轴刻度线
		}
		
		//y轴的数值最大值
		float y_max = max_value[mode]+max_value[mode]/10*2; //为最大值的120%
		float y_p = y_max/10;
		
		//画刻度标签
		m_oPaint.setTextSize(12); // 设置字体大小
		for(int i=0;i<11;i++){   //画出Y轴
			DecimalFormat decimalFloat = new DecimalFormat("0.00"); //float小数点精度处理
			 String str_yp = decimalFloat.format(y_p*i);
			canvas.drawText(str_yp, jg/8, m_nHeight-jg-y_unit*i, m_oPaint); // 画y轴标签
			decimalFloat = null;
		}
		for(int i=0;i<num;i++){  //画出x轴			
//			canvas.drawText(x_markLine[mode][i], x_unit*i+20, m_nHeight-10, m_oPaint); // 画x轴标签
			canvas.drawText(x_markLine[mode][i], x_unit*i+20, m_nHeight-jg/2, m_oPaint); // 画x轴标签
		}
		
		//画出数值点
		
		m_2Paint.setColor(m_nFontColor);   //设置画笔颜色      
		m_2Paint.setStyle(Style.FILL); 	//设置画笔为实心
		m_2Paint.setStrokeWidth((float)2.0);    //设置线条宽度
		HashMap<Integer,String> map = new HashMap<Integer,String>();
		List<Integer> time_list = new ArrayList<Integer>();
		float xv_unit = 0;
		float yv_unit = (m_nHeight-2*jg-jg/2)/y_max;
		if(mode==0){
			map = map_Htime_vlaue;
			time_list = Htimelist;
			xv_unit = (float)(m_nWidth-2*jg-jg/2)/3600;			
		}
		else if(mode==1){
			map = map_Dtime_vlaue;
			time_list = Dtimelist;
			xv_unit = (float)(m_nWidth-2*jg-jg/2)/60/24;
//			Log.e("AutoSigList-onDraw-模式1","map链长度："
//			+String.valueOf(map.size())+"list链长度："+String.valueOf(time_list.size()));
		}
		else if(mode==2){
			map = map_Mtime_vlaue;
			time_list = Mtimelist;
			xv_unit = (float)(m_nWidth-2*jg-jg/2)/31;
//			Log.e("AutoSigList-onDraw-模式2","map链长度："
//					+String.valueOf(map.size())+"list链长度："+String.valueOf(time_list.size()));
		}
		else if(mode==3){
			map = map_Ytime_vlaue;
			time_list = Ytimelist;
			xv_unit = (float)(m_nWidth-2*jg-jg/2)/12;
//			Log.e("AutoSigList-onDraw-模式3","map链长度："
//					+String.valueOf(map.size())+"list链长度："+String.valueOf(time_list.size()));
		}
		float pre_x = 0;
		float pre_y = 0;
		//遍历链表
		Iterator<Integer> keylist = time_list.iterator();
		while(keylist.hasNext()){
			int ii_time = keylist.next();		 //获得时间
			String s_value = map.get( ii_time );  //获得数值			
			float i_value = Float.parseFloat(s_value);
			
			float now_x = jg+ii_time*xv_unit;
			float now_y = m_nHeight-jg-i_value*yv_unit;
			
	//		canvas.drawPoint(now_x, now_y, m_oPaint);
			canvas.drawCircle(now_x, now_y,1, m_oPaint); // 画出数值点	
			
			if(pre_x!=0 || pre_y!=0)
				canvas.drawLine(pre_x,pre_y,now_x,now_y,m_2Paint);  //将点连接成曲线
			pre_x = now_x;
			pre_y = now_y;
			Log.e("AutoSigList-onDraw","map链长度："
					+String.valueOf(map.size())+"list链长度："+String.valueOf(time_list.size()));
			
		}
		map = null;
		time_list = null;  
		
		super.onDraw(canvas);
	}

	
	@Override //绘制底板
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
			//绘制RadioButton的底板空间
			for (int i = 0; i < 4; i++)
				ridobuttons[i].layout(nX + i * nWidth / 4, nY, nX + i * nWidth / 4 + nWidth / 4, nY+20);
		}
	}
	
	@Override//将该控件添加进入主页面
	public void addToRenderWindow(MainWindow rWin) {
		m_rRenderWindow = rWin;
		rWin.addView(this);
		for (int i = 0; i < ridobuttons.length; i++)
			rWin.addView(ridobuttons[i]);
//			rWin.addView(ridobuttons[1]);
//			rWin.addView(ridobuttons[2]);
//			rWin.addView(ridobuttons[4]);
	}
	
	@Override//将该控件从主页面移除
	public void removeFromRenderWindow(MainWindow rWin) {  
		m_rRenderWindow = null;
		for (int i = 0; i < ridobuttons.length; ++i)
			rWin.removeView(ridobuttons[i]);
		rWin.removeView(this);
	} 
	
	//解析控件参数
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
        else if ("Expression".equals(strName)) {
        	m_strExpression = strValue;
        }
        else if ("FontSize".equals(strName)) {
        	m_fFontSize= Float.parseFloat(strValue);
        	this.setFontSize(m_fFontSize);
        }
        else if ("FontColor".equals(strName)) {
        	m_nFontColor= Color.parseColor(strValue);
        	this.setFontColor(m_nFontColor);
        }
        else if ("LineColor".equals(strName)) {
        	m_nLineColor= Color.parseColor(strValue);
        	this.setLineColor(m_nLineColor);
        }
        else if ("BackgroundColor".equals(strName)) {
        	BackgroundColor = Color.parseColor(strValue);
        	this.setBackgroundColor(BackgroundColor);
        }
        else if ("mode".equals(strName)) {
        	if("".equals(strValue)) mode = 0;
        	else mode = Integer.parseInt(strValue);
        }
	}

	//获得该控件
	public View getView() {
		return this;
	}
	//获得底板方框
	public Rect getBBox() {
		return m_rBBox;
	}	
	@Override //完成初始化后处理函数
	public void initFinished()
	{
	}
	
	//设置ui 控件id
	public void setUniqueID(String strID) {
		m_strID = strID;
	}
	//获取ui 控件id
	public String getUniqueID() {
		return m_strID;
	}
	//设置ui 控件类型
	public void setType(String strType) {
		m_strType = strType;
	}
	//获取ui 控件类型
	public String getType() {
		return m_strType;
	}
	//获取控件所在图层
	public int getZIndex(){
		return m_nZIndex;
	}
	//获取控件表达式
	public String getBindingExpression() {
		return m_strExpression;
	}	
	//更新ui控件属性（一些参数）变化
	public void updateWidget() {
		this.invalidate(); //该view 请求重新draw()，只会draw该view
	}
	@Override //是否需要界面刷新标志函数
    public boolean needupdate()
    {
	    return m_bneedupdate;
    }	
	@Override //是否需要界面刷新标志函数
    public void needupdate(boolean bNeedUpdate)
    {
	    m_bneedupdate = bNeedUpdate;
    }	
	@Override   //数据参数更新函数 ui属性变化函数 
	public boolean updateValue()
	{
		m_bneedupdate = false;
		
		
		Log.w("SignalCurve","into updateValue!");     
        return true;  //有控件参数变化并要变化界面view，不管是text函数图形 都要返回true;
	}
//计算链表的最大值
	public float get_max_vlaue(HashMap<Integer,String> map){
		float max_value = 0;
		//遍历链表
		Iterator<Integer> keylist = map.keySet().iterator();
		while(keylist.hasNext()){
			String g_value = map.get( keylist.next()  );
			float f_value = Float.parseFloat(g_value);
			if(f_value>max_value)
				max_value = f_value;
		}
		return max_value;
	}
//计算链表的平均值
	public float get_aver(HashMap<Integer,String> map){
		float aver_value = 0;
		float num = 0;
		int i = 0;
		//遍历链表
		Iterator<Integer> keylist = map.keySet().iterator();
		while(keylist.hasNext()){
			String g_value = map.get( keylist.next() );
			float f_value = Float.parseFloat(g_value);
			if(f_value==0) continue;
			num+=f_value;
			i++;
		}
		aver_value = num/i;
		
		return aver_value;
	}

//可能会调用的变化控件参数
	public void setFontSize(float FontSize){
		m_fFontSize = FontSize;
	}
	public void setFontColor(int FontColor){
		m_nFontColor = FontColor;
	}
	public void setLineColor(int LineColor){
		m_nLineColor = LineColor;
	}
	
	
// params:
//xml 控件的参数
	String m_strID = "";     //控件id
	String m_strType = "";    //控件类型
    int m_nZIndex = 20;     //控件id标号
	int m_nPosX = 300;     //控件位置location x y坐标值
	int m_nPosY = 397;
	int m_nWidth = 150;    //控件大小 长和宽 w h 
	int m_nHeight = 137;
	float m_fAlpha = 1.0f;   //控件色相深度
	String m_strExpression = null;//控件表达式
	float m_fFontSize = 20.0f;	   //控件画笔 字体大小
	int m_nFontColor = 0xFFFF0000;  //控件画笔 (曲线点颜色) 字体颜色 0xFFFF0000为红色
	int m_nLineColor = 0xFFFF0000;  //控件画笔  (坐标轴颜色)字体颜色 0xFFFF0000为红色
	int BackgroundColor = 0xFF000000; //控件底板颜色    0xFF000000为黑色	
//控件必要变量
	Rect m_rBBox = null;   //新建底板方框 类变量
	Paint m_oPaint = null; //新建画笔 类变量
	Paint m_2Paint = null; //新建画笔 类变量
	MainWindow m_rRenderWindow = null;  //新建主页 类变量
	public boolean m_bneedupdate = true; //新建需要控件数据更新标志的变量
	RadioButton[] ridobuttons;
 
	
//X坐标轴的数组标签
	String x_markLine[][] = {{"0","5","10","15","20","25","30","35","40","45","50","55","60"},
	{"0","1:00","2:00","3:00","4:00","5:00","6:00","7:00","8:00","9:00","10:00","11:00",
		    "12:00","13:00","14:00","15:00","16:00","17:00","18:00","19:00","20:00",
		    "21:00","22:00","23:00","24:00"},
	{"0","1","2","3","4","5","6","7","8","9","10","11","12","13","14","15",
			"16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31"},
	{"0","1","2","3","4","5","6","7","8","9","10","11","12"}};
	int mode = 0; //设置曲线模式 0：1h  1:1天  2：一个月   3：一年
	HashMap<Integer,String> map_Htime_vlaue = null;  //保存小时的时间——数值
	HashMap<Integer,String> map_Dtime_vlaue = null;  //保存每天的时间——数值
	HashMap<Integer,String> map_Mtime_vlaue = null;  //保存每月的时间——数值
	HashMap<Integer,String> map_Ytime_vlaue = null;  //保存每年的时间——数值
	List<Integer> Htimelist = null;  //保存小时的时间
	List<Integer> Dtimelist = null;  //保存每天的时间
	List<Integer> Mtimelist = null;  //保存每月的时间
	List<Integer> Ytimelist = null;  //保存每年的时间
		
		
	float max_value[] = null;
	public  static int old_min = 0;
		
}
