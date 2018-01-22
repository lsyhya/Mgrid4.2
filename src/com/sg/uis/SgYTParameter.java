package com.sg.uis;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.InputFilter;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.mgrid.main.MainWindow;
import com.mgrid.main.R;
import com.sg.common.CFGTLS;
import com.sg.common.IObject;

public class SgYTParameter extends TextView implements IObject {
	public SgYTParameter(Context context) {
		super(context); 
		this.setClickable(true);
		this.setGravity(Gravity.CENTER);
		this.setBackgroundColor(Color.GRAY);
		this.setFocusableInTouchMode(true);
		m_fFontSize = this.getTextSize();
		
		// 监听 按钮 触控事件
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
    //    setBackgroundResource(R.drawable.bg_shadow);
        setPadding(0, 0, 0, 0);
        
        m_oPaint = new Paint();
        m_rBBox = new Rect();
        m_oEditText = new EditText(context);
        m_oEditText.setBackgroundResource(android.R.drawable.edit_text);
      //  m_oEditText.setBackgroundResource(R.drawable.et_select);
        
        m_oEditText.setPadding(0, 0, 0, 0);
  
        m_oEditText.setFilters(new  InputFilter[]{ new  InputFilter.LengthFilter(12)});
        m_oEditText.setSingleLine();
        m_oEditText.setGravity(Gravity.CENTER);

        // 监听 文本框 触控事件
        m_oEditText.setOnTouchListener(new OnTouchListener(){
        	public boolean onTouch(View v, MotionEvent event){
        	//m_oEditText.setInputType(InputType.TYPE_NULL); //关闭软键盘
	            switch (event.getAction())
	            {
		            case MotionEvent.ACTION_DOWN:
		            	break;
		            
		            case MotionEvent.ACTION_UP:
		            	break;
		            	
		            case MotionEvent.ACTION_CANCEL:
		            	/*
		            	//imm.showSoftInput(m_oEditText, 0);
		            	m_oEditText.setFocusable(true);
		            	m_oEditText.requestFocus();
		            	m_oEditText.setInputType(EditorInfo.TYPE_CLASS_PHONE);
		            	imm.showSoftInput(m_oEditText, InputMethodManager.SHOW_FORCED);
		            	*/
		            	/*
		        		boolean isOpen = imm.isActive();
		    			if (!isOpen)
		    			{
		    				imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);  // 隐藏任何可能出现的软键盘
		    			}
		    			*/
						//m_oEditText.setSelection(m_oEditText.getText().length());  //光标移动最后
		            	//return true;
		            	//break;
		            	
		            default: 
		            	break;
	            }
	            
				return false;
        	}});
        /*
        m_oEditText.setOnLongClickListener(new View.OnLongClickListener() {
            
			@Override
			public boolean onLongClick(View v) {
				imm.showSoftInput(m_oEditText, InputMethodManager.SHOW_FORCED);
				m_oEditText.setSelection(m_oEditText.getText().length());  //光标移动最后
				
				bEditable = true;
				SgYTParameter.this.setEnabled(bEditable); 
				return true;
			}
		});*/
        
        m_oEditText.setOnClickListener(new View.OnClickListener()
		{
			
			@Override
			public void onClick(View arg0) {
				imm.showSoftInput(m_oEditText, InputMethodManager.SHOW_FORCED);
				m_oEditText.setFocusableInTouchMode(true);
				
			}
		});
        
        m_oEditText.setTextColor(Color.BLACK);
		m_oEditText.setCursorVisible(true);
        m_oEditText.setInputType(EditorInfo.TYPE_CLASS_PHONE); 
        imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
        //imm.showSoftInput(m_oEditText, 0);
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
			this.layout(nX+(int)(nWidth*0.71f), nY, nX+nWidth, nY+nHeight);
			
			m_oEditText.layout(nX, nY+nHeight/10, nX+(int)(nWidth*0.7f), nY+(int)(nHeight*1.0f)-nHeight/10);
		}
	}

	@Override
	public void addToRenderWindow(MainWindow rWin) {
		m_rRenderWindow = rWin;
		
		rWin.addView(m_oEditText);
		rWin.addView(this);
	}

	@Override
	public void removeFromRenderWindow(MainWindow rWin) {

		rWin.removeView(m_oEditText);
		rWin.removeView(this);
	}

	public void parseProperties(String strName, String strValue, String strResFolder) throws Exception {

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
        	  if("#FF000000".equals(strValue))
			    {
        		    setBackgroundResource(R.drawable.bg_shadow);        	
        	        m_oEditText.setBackgroundResource(R.drawable.et_select);
			    }else
			    {
			    	m_cBackgroundColor = Color.parseColor(strValue);
			    }
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
	public void initFinished()
	{
		setGravity(Gravity.CENTER);		
		double padSize = CFGTLS.getPadHeight(m_nHeight, MainWindow.FORM_HEIGHT, getTextSize())/2;
		setPadding(0, (int) padSize, 0, (int) padSize);
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
	
	private void onClicked() {
		String editvalue = m_oEditText.getText().toString().trim();
		
		if (editvalue.isEmpty()) return;
		
		try {
			Double.parseDouble(editvalue);
			} catch (NumberFormatException e) {
				//new AlertDialog.Builder(this.getContext()).setTitle("错误") .setMessage("请输入合法数值！") .show();
				m_oEditText.setText("");		
				Drawable d = m_oEditText.getResources().getDrawable(android.R.drawable.stat_notify_error);
				d.setBounds(0, 0, 30, 30);
				m_oEditText.setError("格式错误！", d);				
				m_oEditText.requestFocus();
			return;
			} 
		
		// 发送控制命令
		if ("".equals(m_strCmdExpression) == false)
		{
			synchronized(m_rRenderWindow.m_oShareObject)
			{
			//	System.out.println("我进来了，值为："+m_oEditText.getText().toString().trim());
				m_rRenderWindow.m_oShareObject.m_mapCmdCommand.put(getUniqueID(), m_oEditText.getText().toString().trim());
			}
		}
	}
	
	public String getBindingExpression() {
		return m_strCmdExpression;
	}
	
	@Override
	public void updateWidget() {		
	}

	@Override
	public boolean updateValue()
	{
        return false;
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
	

    /** 
         * 将文件生成位图 
         * @param path 
         * @return 
         * @throws IOException 
         *\/  
        private static final int BUFFER_SIZE = 1024 * 16;  // 最大 16 K
        public BitmapDrawable getImageDrawable(String path)  
            throws IOException  
        {
            //打开文件
            File file = new File(path);
            if(!file.exists())
            {
                return null;
            }
              
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            byte[] bt = new byte[BUFFER_SIZE];
              
            //得到文件的输入流
            InputStream in = new FileInputStream(file);
              
            //将文件读出到输出流中   
            int readLength = in.read(bt);  
            while (readLength != -1) {  
                outStream.write(bt, 0, readLength);  
                readLength = in.read(bt);  
            }
            
            in.close();
              
            //转换成byte 后 再格式化成位图   
            byte[] data = outStream.toByteArray();  
            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);// 生成位图   
            BitmapDrawable bd = new BitmapDrawable(bitmap);  
              
            return bd;  
        } 
        */

// params:
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
	String m_strCmdExpression = "Binding{[Cmd[Equip:113-Temp:168-Command:1]]}";
	boolean m_bIsValueRelateSignal = false;
	float m_fButtonWidthRate = 0.3f;
	MainWindow m_rRenderWindow = null;
	float m_fFontSize = 6.0f;
	boolean m_bPressed = false;
	Paint m_oPaint = null;
	Rect m_rBBox = null;
	InputMethodManager imm = null;
	EditText m_oEditText = null;
	
	// 记录触摸坐标，过滤滑动操作。解决滑动误操作点击问题。
	public float m_xscal = 0;
	public float m_yscal = 0;
}
