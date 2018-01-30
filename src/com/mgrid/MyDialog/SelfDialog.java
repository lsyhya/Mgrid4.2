package com.mgrid.MyDialog;

import com.mgrid.main.R;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * 创建自定义的dialog
 */
public class SelfDialog extends Dialog {


	private TextView textView;
	
	public SelfDialog(Context context) {
		super(context, R.style.MyTest);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.progress1);
		// 按空白处不能取消动画
		setCanceledOnTouchOutside(false);
        initView();
	}

	private void initView()
	{
		textView=(TextView) findViewById(R.id.tv);
	}
	
	public void settext(String text)
	{
		textView.setText(text);
	}

}
