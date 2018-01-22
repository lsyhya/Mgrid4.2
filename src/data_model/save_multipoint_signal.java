package data_model;
  
import android.util.Log;   
 
//made by fjw  用于多点可选择周期动态曲线  
//made author:fjw0312
//date:2016
//notice:
public class save_multipoint_signal {      
	
	public float new_value; 
	public String new_time; 

	public int  m1_num = 30;  
	public int  m2_num = 24;  
	public int  m3_num = 30;  
	public float m1_curve_value_buf[] = new float[30]; 
	public String m1_curve_time_buf[] = new String[30]; 
	public float m2_curve_value_buf[] = new float[24]; 
	public String m2_curve_time_buf[] = new String[24]; 
	public float m3_curve_value_buf[] = new float[30];
	public String m3_curve_time_buf[] = new String[30]; 
	public String m2_oldTime=""; 
	public String m3_oldTime=""; 
	public int add_opintTimes = 0; 
	public int add_m2opintTimes = 0; 
	public  long samperTime = 0; 
	

    boolean deal_x(int mode_num,String time_buf[],String x_mark[]){
    	if(time_buf==null) time_buf = new String[mode_num];
    	
		for(int i=1;i<mode_num;i++){   
			if(time_buf[i]==null)  time_buf[i] = " "; 
			x_mark[i] = time_buf[i];
		}
		return true;
	}

    float deal_y(int mode_num,float value_buf[],float y_mark[]){
		

    	float max_y = value_buf[0];
		for(int i=0;i<mode_num;i++){
			if(max_y < value_buf[i]){
				max_y = value_buf[i];
			}
		}

		float max_markLineValue = max_y; //
		float line_unit = max_markLineValue/10; 
		for(int i=1;i<10;i++){
			y_mark[i] = line_unit*i; //
		}
		
		return max_markLineValue;
	}
    
 
   public float get_curve(int mode,String x_mark[],float y_mark[],float value[]){
	   float max_markLineValue = 100;
	
	   switch(mode){
	   		case 1:
	   			deal_x(m1_num,m1_curve_time_buf,x_mark);
	   			max_markLineValue = deal_y(m1_num,m1_curve_value_buf,y_mark);
	   			for(int i=0;i<m1_num;i++){
	   				value[i] = m1_curve_value_buf[i];
	   			}
	   			return max_markLineValue;
	   		case 2:
	   			deal_x(m2_num,m2_curve_time_buf,x_mark);
	   			max_markLineValue = deal_y(m2_num,m2_curve_value_buf,y_mark);
	   			for(int i=0;i<m2_num;i++){
	   				value[i] = m2_curve_value_buf[i];
	   			}
	   			return max_markLineValue;
	   		case 3:
	   			deal_x(m3_num,m3_curve_time_buf,x_mark);
	   			max_markLineValue = deal_y(m3_num,m3_curve_value_buf,y_mark);
	   			for(int i=0;i<m3_num;i++){
	   				value[i] = m3_curve_value_buf[i];
	   			}
	   			return max_markLineValue;
	   		default:
	   	//		return 0;
	   			
	   }
	  	   
	   return max_markLineValue;
   }
    

   float one_hour_value(){
	   float sum = 0;
	   for(int i=0;i<m1_num;i++){
		   sum += m1_curve_value_buf[i];
	   }
	   return sum/(add_opintTimes);
   }

   float one_day_value(){
	   float sum = 0;
	   for(int i=0;i<m2_num;i++){
		   sum += m2_curve_value_buf[i];
	   }
	   return sum/(add_m2opintTimes);
   }
 

   boolean transfer(int mode_num, float value, String time,float valueBuf[],String timeBuf[]){

	   if(mode_num == 0) mode_num = 30;
	   if(time == null)  time = "0";
	   if(valueBuf == null) valueBuf = new float[mode_num];		
	   if(timeBuf == null) timeBuf = new String[mode_num];
		 

		float buf_v[] = new float[mode_num]; 
		String buf_t[] = new String[mode_num]; 
		for(int i=0;i<mode_num;i++){
			if(timeBuf[i]==null) timeBuf[i] = " ";
			buf_v[i] = valueBuf[i];
			buf_t[i] = timeBuf[i];
		}

		for(int i= 0;i<mode_num-1;i++){
			valueBuf[i] = buf_v[i+1];
			timeBuf[i] = buf_t[i+1];
		}
		valueBuf[mode_num-1] = value;
		timeBuf[mode_num-1] = time;
		

	
	   return true;
   }
   

	public boolean add_point(String value,String time){
		try{
			if(value == null) new_value = 0;
			else new_value = Float.valueOf( value );
			if(time == null) new_time = " ";
			else new_time =  time ;
			
			if(new_time.length()<15) return true;
	
			String minsec_tmp = new_time.substring(14);
			if(minsec_tmp == null) minsec_tmp = " ";
			transfer(m1_num,new_value,minsec_tmp,m1_curve_value_buf,m1_curve_time_buf);
			add_opintTimes++;
			if(add_opintTimes>30) add_opintTimes=30;
			
		
			String day_tmp = new_time.substring(8, 10);
			if(day_tmp == null) day_tmp = " ";
			String hour_tmp = new_time.substring(11, 13);
			if(hour_tmp == null) hour_tmp = " ";
			String min_tmp = new_time.substring(14, 16);
			if(min_tmp == null) min_tmp = " ";

			if(  "".equals(m2_oldTime)|| !hour_tmp.equals(m2_oldTime) ){				
				m2_oldTime = hour_tmp;
				float hour_value = one_hour_value();
		
				transfer(m2_num,hour_value,hour_tmp,m2_curve_value_buf,m2_curve_time_buf);
				add_m2opintTimes++;
				if(add_m2opintTimes>24) add_m2opintTimes=24;
			}
		
			if( "".equals(m3_oldTime)|| !day_tmp.equals(m3_oldTime)  ){
				m3_oldTime = day_tmp;
				float day_value = one_day_value();
			
				transfer(m3_num,day_value,day_tmp,m3_curve_value_buf,m3_curve_time_buf);
				
			}

		}catch(Exception e){

		}
		return true;
	}

}
