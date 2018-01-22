package com.mgrid.fuction;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.FloatMath;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.ImageView;

 
//made author:fjw0312
//date:2016
//notice:
public class PhotoImage extends ImageView{

	//Fileds
	Bitmap bitmp;
	public String name= "";
	public ArrayList<String> lstName = new ArrayList<String>();
	public ArrayList<Bitmap> lstBitmap = new ArrayList<Bitmap>();
	
	float start_x=0,start_y=0;
	float end_x=0,end_y=0;
	int touchPonitNum = 0; 
	float oldDistance = 0; 
	float scalePer = 1; 
	float oldTranlate_x =0; 
	float oldTranlate_y =0; 
	
	public PhotoImage(Context context) {
		// TODO Auto-generated constructor stub
		super(context);		
	}
	

	public boolean addParam(ArrayList<String> a_lstName,ArrayList<Bitmap> a_lstBitmap,String a_name){
		lstName = a_lstName;
		lstBitmap =  a_lstBitmap;
		name = a_name;
		return true;
	}
	
	public boolean setPhoto(String name){
		Bitmap bitmap = BitmapFactory.decodeFile(name); 
		this.setImageBitmap(bitmap);
		return true;
	}
	
	
	public boolean onTouchEvent(MotionEvent event){
		super.onTouchEvent(event);
		switch(event.getActionMasked()){ 
			case MotionEvent.ACTION_DOWN : 
				touchPonitNum = 1;
				start_x = event.getX(0);
				start_y = event.getY(0);
				break;
			case MotionEvent.ACTION_UP :
				touchPonitNum = 0;
				break;
			case MotionEvent.ACTION_POINTER_DOWN:  
				touchPonitNum++;
				oldDistance = distance(event);
				break;
			case MotionEvent.ACTION_POINTER_UP:
				touchPonitNum--;
				break;
			case MotionEvent.ACTION_MOVE : 	
				if(touchPonitNum>=2){ 
					float newDistance = distance(event); 
					float per = (newDistance-oldDistance)/100;
					scalePer = per + scalePer;
					if(scalePer<1) scalePer = 1;   
					if(scalePer>5) scalePer = 5;
					if(newDistance-oldDistance>5){ 						
						this.setScaleX(scalePer); 
						this.setScaleY(scalePer);						
					}else if(oldDistance-newDistance>5){ 						
						this.setScaleX(scalePer); 
						this.setScaleY(scalePer);					
				    }
				}else{ 
					if(scalePer>1){     
						end_x = event.getX(0);
						end_y = event.getY(0);
						float tranlate_x = end_x-start_x;
						float tranlate_y = end_y-start_y;
						oldTranlate_x = oldTranlate_x+tranlate_x;
						oldTranlate_y = oldTranlate_y+tranlate_y;
						if(oldTranlate_x > 2100)  oldTranlate_x = 2100; 
						if(oldTranlate_x < -2100)  oldTranlate_x = -2100;
						if(oldTranlate_y > 1500)  oldTranlate_y = 1500;
						if(oldTranlate_y < -1500)  oldTranlate_y = -1500;
						this.setTranslationX(oldTranlate_x);
						this.setTranslationY(oldTranlate_y);	

						
					}else{
						this.setTranslationX(0);
						this.setTranslationY(0);
						oldTranlate_x = 0;
						oldTranlate_y = 0;
					}
				}
				break;
		}
		return true;
		
	}
	
	private float distance(MotionEvent event){
		float eX = event.getX(1) - event.getX(0);
		float eY = event.getY(1) - event.getY(0);
		return FloatMath.sqrt(eX * eX + eY * eY);
	}

}
