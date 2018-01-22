package com.mgrid.main;

import java.io.File;

import com.mgrid.fuction.PhotoGridview;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;


//fjw made 2016 4 7 
//made author:fjw0312
//date:2016
//notice:
public class GridviewActivity extends Activity {

	//Fileds
	RelativeLayout layout;
	PhotoGridview photoview;
	Button button;
	boolean flagEnd = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.page);
		layout = (RelativeLayout)findViewById(R.id.layout_pageID);
	
		setPage2();
	}

	private void setPage1(){
		photoview = new PhotoGridview(this);
		setContentView(photoview);
		mythread.start();
	}
	
	private void setPage2(){
		photoview = new PhotoGridview(this);
		RelativeLayout.LayoutParams photoParams = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
		
		button= new Button(this);
		button.setText("·µ»Ø");
		RelativeLayout.LayoutParams buttonParams = new LayoutParams(100,80); 
		buttonParams.setMargins(900, 580, 1000, 650); 
		button.setOnClickListener(l);
		
		layout.addView(photoview, photoParams);		
		layout.addView(button,buttonParams);
		mythread.start();
	}
	
	private OnClickListener l = new OnClickListener() {
		
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub

			flagEnd = true;
			GridviewActivity.this.finish();
		}
	};

	Thread mythread = new Thread(new Runnable() {
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			while(true){
				if(photoview.flag){
				
					photoview.flag = false; 
				
					ImageviewActivity.name = photoview.select_name;
					Intent intent2 = new Intent(GridviewActivity.this, ImageviewActivity.class);
					startActivity(intent2);
				}else{
					try{
						Thread.sleep(500);					
					}catch(Exception e){
						
					}
				}
			
				if(flagEnd)	break;		 
			}
		}
	});

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
