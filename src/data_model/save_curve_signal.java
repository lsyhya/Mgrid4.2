package data_model;

import android.util.Log;

//made by fjw  用于动态曲线
//made author:fjw0312
//date:2016
//notice:
public class save_curve_signal {
	
	public float new_value;
	public String new_time;
	public float curve_value_buf[] = {0,0,0,0,0,0,0,0,0,0}; 
	public String curve_time_buf[] = {"0","0","0","0","0","0","0","0","0","0"}; 
	public float curve_mark_yline[] = new float[10]; 
	public String curve_mark_xline[] = new String[10]; 
	public float max_markLineValue;
	public int  num = 10; 
	public long samperTime = 0;  
	
	

    boolean deal_x(){
		for(int i=1;i<num;i++){  
			curve_mark_xline[i] = curve_time_buf[i];
		}
		return true;
	}

    boolean deal_y(){
		
	
    	float max_y = curve_value_buf[0];
		for(int i=0;i<num;i++){
			if(max_y < curve_value_buf[i]){
				max_y = curve_value_buf[i];
			}
		}
		max_markLineValue = max_y + max_y/5; 
		float line_unit = max_markLineValue/10;
		for(int i=1;i<10;i++){
			curve_mark_yline[i] = line_unit*i; 
		}
		
		return true;
	}
	
	public boolean add_point(String value,String time){
		try{
			if(value == null) new_value = 0;
			else new_value = Float.valueOf( value );
			if(time == null) new_time = "0";
			else new_time =  time ;

		
			float buf_v[] = new float[num]; 
			String buf_t[] = new String[num]; 
			for(int i=0;i<num;i++){
				buf_v[i] = curve_value_buf[i];
				buf_t[i] = curve_time_buf[i];
			}
		
			for(int i= 0;i<num-1;i++){
				curve_value_buf[i] = buf_v[i+1];
				curve_time_buf[i] = buf_t[i+1];
			}
			curve_value_buf[num-1] = new_value;
			curve_time_buf[num-1] = new_time;
		
			deal_x(); 
			deal_y();  
		}catch(Exception e){
			
		}
		return true;
	}

}
