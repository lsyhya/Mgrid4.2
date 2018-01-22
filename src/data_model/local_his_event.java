package data_model;  

import android.util.Log;


//made author:fjw0312
//date:2016
//notice:
public class local_his_event {
	
	public String start_time="";   //告警时间        0
	public String finish_time="";   //结束时间     1	
	public String equip_name=""; //设备名              2
	public String equipid="";    //设备id	  3
	public String event_name="";  //告警名	  4
	public String sig_name="";    //信号名           
	public String sigid="";      //信号id     
	public String event_id="";    //告警id    5
	public String severity="";   //关联告警等级  8
	public String value="";       //信号值         11	
	public String event_mean="";  //告警含义     12
	
	public String timelaterStr = ""; 
	

	public boolean put_equiptName(String name){
		equip_name = name;
		return true;
	}

	public boolean put_signalName(String name){
		sig_name = name;
		return true;
	}
	
	public boolean put_eventName(String name){
		event_name = name;
		return true;
	}
	
	public String get_signalId(){
		return sigid;
	}
	
	public String get_eventId(){
		return event_id;
	}
	
		

	public boolean read_string(String buf){
		
		String[] a  = new String[100];
		
		a = buf.split(",");
		timelaterStr = buf.substring(40);

		if(a.length != 13){
			return false;
		}
		
		start_time = a[0];   
		finish_time = a[1];  
		equip_name = a[2];
		equipid = a[3];    
		event_name = a[4]; 
		event_id = a[5];   
		severity = a[8];  
		value = a[11];   
		event_mean = a[12]; 
		sig_name = "";    

		
		
		return true;
	}
		
}
