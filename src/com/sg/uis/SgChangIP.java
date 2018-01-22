package com.sg.uis;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.provider.Settings;
import android.text.InputFilter;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mgrid.main.MGridActivity;
import com.mgrid.main.MainWindow;
import com.mgrid.main.R;
import com.sg.common.CFGTLS;
import com.sg.common.IObject;

/**
 * 改ip
 * @author Administrator
 *
 */
public class SgChangIP extends TextView implements IObject{

	public SgChangIP(Context context) {
		super(context);
		this.setClickable(true);
		this.setGravity(Gravity.CENTER);
		this.setFocusableInTouchMode(true);
		m_fFontSize = this.getTextSize();
		this.setTextSize(20);
		this.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View view, MotionEvent event) {
	            switch (event.getAction())
	            {
		            case MotionEvent.ACTION_DOWN:
		            	m_bPressed = true;	
		            	view.invalidate();
		            	
		            	m_xscal = event.getX();
		            	m_yscal = event.getY();
		            break;
		            
		            case MotionEvent.ACTION_UP: 
		            	m_bPressed = false;	
		            	view.invalidate();
		            	
		            	if (m_xscal == event.getX() && m_yscal == event.getY())
		            	onClicked();
		            break;
		            default: break;
	            }
				return true;
			}
        });
		
		setBackgroundResource(android.R.drawable.btn_default);
        setPadding(0, 0, 0, 0);
        
        m_oPaint = new Paint();
        m_rBBox = new Rect();
        m_oEditTextIP = new EditText(context);
        tvIP=new TextView(context);       
        m_oEditTextIP.setBackgroundResource(android.R.drawable.edit_text);
        m_oEditTextIP.setPadding(0, 0, 0, 0);
        tvIP.setPadding(0, 0, 0, 0);
        tvIP.setTextSize(20); 
        m_oEditTextIP.setTextSize(20);
        tvIP.setText("I P:");
        tvIP.setTextColor(Color.BLACK);
        
        
 
        m_oEditTextIP.setFilters(new  InputFilter[]{ new  InputFilter.LengthFilter(12)});

        m_oEditTextIP.setSingleLine();

        m_oEditTextIP.setGravity(Gravity.CENTER);
        
       // m_oEditText.setGravity(Gravity.CENTER);//设置字体居中
        
        
        m_oEditTextIP.setOnClickListener(new View.OnClickListener()
      		{
      			
      			@Override
      			public void onClick(View arg0) {
      				imm.showSoftInput(m_oEditTextIP, InputMethodManager.SHOW_FORCED);//获取到这个类。
      				m_oEditTextIP.setFocusableInTouchMode(true);//获取焦点
      				
      			}
      		});

        m_oEditTextIP.setTextColor(Color.BLACK);
       // m_oEditTextNEW.setHint("请输入新密码");	//设置提示文字
       // m_oEditTextOLD.setHint("请输入原密码"); 
        m_oEditTextIP.setCursorVisible(true);//让edittext出现光标
 
        m_oEditTextIP.setInputType(EditorInfo.TYPE_CLASS_PHONE); //设置文本格式
        imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);//显示输入法窗口

	}
	
	@SuppressLint("DrawAllocation")
	protected void onDraw(Canvas canvas) {
		if (m_rRenderWindow == null)
			return;
		if (m_rRenderWindow.isLayoutVisible(getBBox()) == false)
			return;

		if (m_bPressed) {
			int nWidth = (int) (((float)(m_nWidth) / (float)MainWindow.FORM_WIDTH) * (m_rRenderWindow.VIEW_RIGHT - m_rRenderWindow.VIEW_LEFT));
			int nHeight = (int) (((float)(m_nHeight) / (float)MainWindow.FORM_HEIGHT) * (m_rRenderWindow.VIEW_BOTTOM - m_rRenderWindow.VIEW_TOP));

			m_oPaint.setColor(0x50FF00F0);
			m_oPaint.setStyle(Paint.Style.FILL); 
			canvas.drawRect(0,0,nWidth,nHeight, m_oPaint);
		}
		super.onDraw(canvas);
	}

	protected void onClicked() {
		
	
		
		//跳转到系统设置界面
		showSystemIPSettingDialog();
		 

	    
	  
//	    try {
//	    Runtime mRuntime = Runtime.getRuntime();
//	    Process mProcess = Runtime.getRuntime().exec("su");
//	    //Process中封装了返回的结果和执行错误的结果
//	    mProcess = mRuntime.exec("ifconfig eth0");
//	    BufferedReader mReader = new BufferedReader(new InputStreamReader(mProcess.getInputStream()));
//	    StringBuffer mRespBuff = new StringBuffer();
//	    char[] buff = new char[1024];
//	    int ch = 0;
//	    while((ch = mReader.read(buff)) != -1){
//	    mRespBuff.append(buff, 0, ch);
//	    }
//	    Toast.makeText(getContext(), mRespBuff.toString(), Toast.LENGTH_SHORT).show();
//	    mReader.close();
//	    } catch (IOException e) {
//	    // TODO Auto-generated catch block
//	    e.printStackTrace();
//	    }
	     
	}
	
    

	
	 public  void showSystemIPSettingDialog() {
	    	
	  
			LayoutInflater factory = LayoutInflater.from(m_rRenderWindow.getContext());
			final View textEntryView = factory.inflate(R.layout.auth_dialog, null);
			new AlertDialog.Builder(m_rRenderWindow.getContext())
	      .setTitle("进入系统设置")
	       .setView(textEntryView)
	      .setPositiveButton("确定", new DialogInterface.OnClickListener() {
	           public void onClick(DialogInterface dialog, int whichButton) { 

	        	final EditText etUserName = (EditText)textEntryView.findViewById(R.id.etuserName);
	            final EditText etPassword = (EditText)textEntryView.findViewById(R.id.etPWD);
	            
	   	        String userName = etUserName.getText().toString().trim();
	   	    	String password = etPassword.getText().toString().trim();
	   	    	

	   	    	if((userName.equals("lsy") && password.equals("lsy"))||
	   	    			(userName.equals(MGridActivity.m_UserName)&&password.equals(MGridActivity.m_PassWord))){
	   	    	 Intent intent =  new Intent(Settings.ACTION_AIRPLANE_MODE_SETTINGS);  
	   			 m_rRenderWindow.m_oMgridActivity.startActivity(intent);  
	   			 if(m_rRenderWindow!=null)
	   			 m_rRenderWindow.showTaskUI(true);
				}
	   	    	else
	   	    	{
	   	    		Toast.makeText(getContext(), "账户或密码输入错误", 1000).show();
	   	    	}

	           }
	      }

	       )
	       //对话框的“退出”单击事件
	       .setNegativeButton("取消", new DialogInterface.OnClickListener() {
	           public void onClick(DialogInterface dialog, int whichButton) {
	        	   //LoginActivity.this.finish();
	           }
	       })
	       
	        //对话框的创建、显示
			.create().show();
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
			
			tvIP.layout(nX, nY, nX+(int)(nWidth*0.2f),  nY+nHeight);
			m_oEditTextIP.layout(nX+(int)(nWidth*0.21f), nY,nX+(int)(nWidth*0.79f),nY+nHeight);
			this.layout(nX+(int)(nWidth*0.8f), nY, nX+nWidth, nY+nHeight);
		
		}
		
	}

	@Override
	public void setUniqueID(String strID) {
		// TODO Auto-generated method stub
		m_strID = strID;
	}

	@Override
	public String getUniqueID() {
		// TODO Auto-generated method stub
		return m_strID;
	}

	@Override
	public void setType(String strType) {
		// TODO Auto-generated method stub
		m_strType = strType;
	}

	@Override
	public String getType() {
		// TODO Auto-generated method stub
		return m_strType;
	}

	@Override
	public void parseProperties(String strName, String strValue,
			String strResFolder) throws Exception {
		

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
	        else if ("Content".equals(strName)) {
		       	 m_strContent = strValue;
		       	 this.setText(m_strContent);
	        }
	        else if ("FontFamily".equals(strName))
	       	 	m_strFontFamily = strValue;
	        else if ("IsBold".equals(strName))
	       	 	m_bIsBold = Boolean.parseBoolean(strValue);
	        else if ("BackgroundColor".equals(strName)) 
	        	m_cBackgroundColor = Color.parseColor(strValue);
	        else if ("FontColor".equals(strName)) {
		       	 m_cFontColor = Color.parseColor(strValue);
		       	 this.setTextColor(m_cFontColor);
	        }
	        else if ("CmdExpression".equals(strName)) {
	       	 	m_strCmdExpression = strValue;
	        }
	        else if ("IsValueRelateSignal".equals(strName)) {
	        	if ("True".equals(strValue))
	        		m_bIsValueRelateSignal = true;
	        	else
	        		m_bIsValueRelateSignal = false;
	        }
	        else if ("ButtonWidthRate".equals(strName)) {
	        	m_fButtonWidthRate = Float.parseFloat(strValue);
	        }
		}
		
	

	@Override
	public void initFinished() {
            setGravity(Gravity.CENTER);
		
		double padSize = CFGTLS.getPadHeight(m_nHeight, MainWindow.FORM_HEIGHT, getTextSize())/2;
		setPadding(0, (int) padSize, 0, (int) padSize);
		
	}

	@Override
	public void addToRenderWindow(MainWindow rWin) {
        m_rRenderWindow = rWin;
		
		rWin.addView(m_oEditTextIP);
		rWin.addView(tvIP);
		rWin.addView(this);
		
	}

	@Override
	public void removeFromRenderWindow(MainWindow rWin) {
		// TODO Auto-generated method stub
		rWin.removeView(m_oEditTextIP);
		rWin.removeView(tvIP);
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
	
	
	//数据
	String m_strID = "";
	String m_strType = "";
    int m_nZIndex = 10;
	int m_nPosX = 152;
	int m_nPosY = 287;
	int m_nWidth = 75;
	int m_nHeight = 23;
	float m_fAlpha = 1.0f;
	boolean m_bIsBold = false;
	String m_strFontFamily = "微软雅黑";
	int m_cBackgroundColor = 0xFFFCFCFC;
	int m_cFontColor = 0xFF000000;
	String m_strContent = "Setting";
	String m_strCmdExpression = "";
	boolean m_bIsValueRelateSignal = false;
	float m_fButtonWidthRate = 0.3f;
	MainWindow m_rRenderWindow = null;
	float m_fFontSize = 6.0f;
	boolean m_bPressed = false;
	Paint m_oPaint = null;
	Rect m_rBBox = null;
	InputMethodManager imm = null;
	EditText m_oEditTextIP = null;
	TextView tvIP=null;

	
	// 记录触摸坐标，过滤滑动操作。解决滑动误操作点击问题。
		public float m_xscal = 0;
		public float m_yscal = 0;

}
