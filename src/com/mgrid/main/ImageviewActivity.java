package com.mgrid.main;

import com.mgrid.fuction.PhotoImage;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;


//fjw made 2016 4 6 
//made author:fjw0312
//date:2016
//notice:
public class ImageviewActivity extends Activity {

	//Fileds
	RelativeLayout layout;
	public static String name="/mnt/sdcard/fjw_photo/1.jpg";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.page);
		layout = (RelativeLayout)findViewById(R.id.layout_pageID);

		setPage2();
	}
	
	private void setPage1(){
		PhotoImage photo = new PhotoImage(this);
		photo.setPhoto(name);
		setContentView(photo);
	}
	
	private void setPage2(){
		PhotoImage photo = new PhotoImage(this);
		photo.setPhoto(name);                      
		RelativeLayout.LayoutParams photoParams = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
		Button button = new Button(this);
		button.setText("·µ»Ø");
		RelativeLayout.LayoutParams buttonParams = new LayoutParams(100,80); 
		buttonParams.setMargins(900, 580, 1000, 650); 
		button.setOnClickListener(l); 
		
		layout.addView(photo, photoParams);		
		layout.addView(button,buttonParams);

	}
	
	private OnClickListener l = new OnClickListener() {
		
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
		
			ImageviewActivity.this.finish();
		}
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
