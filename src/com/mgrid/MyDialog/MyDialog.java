package com.mgrid.MyDialog;

import com.mgrid.main.R;
import com.sg.uis.LsyNewView.FlikerProgressBar;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

/**
 * 
 * @author lsy
 * 自定义进度条dialog
 */

public class MyDialog extends Dialog{

private FlikerProgressBar bar;
	
	
	public MyDialog(Context context) {
		super(context,R.style.MyDialog);
		
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		 setContentView(R.layout.mydialog);
	     //按空白处不能取消动画
	     setCanceledOnTouchOutside(false);
	     //初始化界面控件
	     initView();
	
	}
	

    /**
     * 初始化界面控件
     */
    private void initView() {
    	bar=(FlikerProgressBar) findViewById(R.id.flikerbar);
    }
    
    public FlikerProgressBar getBar()
    {
    	return bar;
    }
}
