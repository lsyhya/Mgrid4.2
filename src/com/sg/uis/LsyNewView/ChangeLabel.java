package com.sg.uis.LsyNewView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.mgrid.main.MGridActivity;
import com.mgrid.main.MainWindow;
import com.sg.common.CFGTLS;
import com.sg.common.IObject;
import com.sg.common.TotalVariable;

/** 按钮 */
@SuppressLint({ "ShowToast", "InflateParams", "RtlHardcoded",
		"ClickableViewAccessibility" })
public class ChangeLabel extends TextView implements IObject {


	public ChangeLabel(Context context) {
		super(context);

		this.setBackgroundColor(0x00000000);
		
		m_oPaint = new Paint();
		m_rBBox = new Rect();
		
	}

	

	@SuppressLint("DrawAllocation")
	protected void onDraw(Canvas canvas) {
		if (m_rRenderWindow == null)
			return;
		if (m_rRenderWindow.isLayoutVisible(getBBox()) == false)
			return;
		super.onDraw(canvas);
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
			this.layout(nX, nY, nX + nWidth, nY + nHeight);		
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
		} else if ("Content".equals(strName)) {
			m_strContent = strValue;
			this.setText(m_strContent);
		} else if ("FontFamily".equals(strName))
			m_strFontFamily = strValue;
		else if ("FontSize".equals(strName)) {
			float fWinScale = (float) MainWindow.SCREEN_WIDTH
					/ (float) MainWindow.FORM_WIDTH;
			m_fFontSize = Float.parseFloat(strValue) * fWinScale;
			this.setTextSize(m_fFontSize);
		} else if ("IsBold".equals(strName))
			m_bIsBold = Boolean.parseBoolean(strValue);
		else if ("FontColor".equals(strName)) {
			m_cFontColor = Color.parseColor(strValue);
			this.setTextColor(m_cFontColor);
		} else if ("HorizontalContentAlignment".equals(strName))
			m_strHorizontalContentAlignment = strValue;
		else if ("VerticalContentAlignment".equals(strName))
			m_strVerticalContentAlignment = strValue;
		else if ("index".equals(strName))
			index = strValue;
		    TotalVariable.ORDERLISTS.put(index, this);
		    MGridActivity.xianChengChi.execute(new Runnable() {
				
				@Override
				public void run() {
					
					String str=readText();
					if(str!=null)
					{
						Message mes=new Message();
						mes.what=0;
						mes.obj=str;
						handler.sendMessage(mes);
								
					}
				}
			});
	}
	
	
	
	Handler handler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				
				setText((String)msg.obj);
				break;

			case 1:
				
				break;
			}
			
		};
	};


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

	@Override
	public void updateWidget() {

		
		
	}
	
	public void updateText(final String text)
	{
		setText(text);
		System.out.println(text);
		invalidate();
		//Toast.makeText(getContext(), "修改成功", 200).show();
		MGridActivity.xianChengChi.execute(new Runnable() {
			
			@Override
			public void run() {
				  
				saveText(text); 
			}
		});
	}
	
	private void saveText(String text)
	{
		File f=new File(filePath+"/"+index+".index");
		if(!f.exists())
		{
			try {
				f.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			BufferedWriter writer=new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f), "gb2312"));
			writer.write(text);
			writer.flush();
			writer.close();
		} catch (Exception e) {			
			e.printStackTrace();
		} 
		
	}
	
	private String readText()
	{
		File f=new File(filePath+"/"+index+".index");
		if(!f.exists())
		{
			return null;
		}
		try {
			BufferedReader reader=new BufferedReader(new InputStreamReader(new FileInputStream(f), "gb2312"));
			String str=reader.readLine();
			reader.close();
			return str;
		} catch (Exception e) {			
			e.printStackTrace();
		}
		return null; 
		
	}
	

	@Override 
	public boolean updateValue() {

		return false;
	}

	@Override
	public boolean needupdate() {
		return true;
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

	MainWindow m_rRenderWindow = null;
	Paint m_oPaint = null;
	Rect m_rBBox = null;

    String index="";
	public boolean m_bneedupdate = true;
	private String mExpression = "";
	
	String filePath=Environment.getExternalStorageDirectory().getPath();
}


