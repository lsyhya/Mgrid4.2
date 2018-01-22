package data_model;

public class local_his_Alarm {

	public String equip_name="";
	public String start_time="";  
	public String end_time="";   	
	public String result="";   
	public String control="";  
	public String alarm="";  
    public String yichang="";  
	public boolean read_string(String buf){
		
		String[] arg1=buf.split(",");
		if(arg1.length!=4)
		return false;		
		equip_name=arg1[0];
		String[] arg2=arg1[1].split("&");
		start_time=arg2[0];
		control=arg2[2];
		alarm=arg2[3];
		String[] arg3=arg1[3].split("&");
		end_time=arg3[0];  
		result=arg3[1];
		yichang=arg3[2];
		return true;
	}
}
