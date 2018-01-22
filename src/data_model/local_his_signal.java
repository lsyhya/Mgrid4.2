package data_model;  

//made author:fjw0312
//date:2016
//notice:
public class local_his_signal {
	
	public String equipid;    
	public String equip_name; //设备名
	public String sigid;      //信号id
	public String name;        //信号名
	public String value;       //信号值
	public String unit;        //单位
	public String value_type;   //信号类型
	public String is_invalid;   //0 --> valid, 1 --> invalid
	public String severity;  
	public String freshtime;  
	


	public String to_string(){
		
		String buf = null;  
		String a1 = equipid + "`";
		String a2 = equip_name + "`";
		String a3 = sigid + "`";
		String a4 = name + "`";
		String a5 = value + "`";
		String a6 = unit + "`";
		String a7 = value_type + "`";
		String a8 = is_invalid + "`";
		String a9 = severity + "`";
		String a10 = freshtime;
		
		buf = a1+a2+a3+a4+a5+a6+a7+a8+a9+a10;
		
		return buf;
	}

	public boolean read_string(String buf){
		
		String[] a  = new String[100];
		
		a = buf.split("`");

		if(a.length != 10){
			return false;
		}
		
		equipid = a[0];    
		equip_name = a[1];
		sigid = a[2];   
		name = a[3];       
		value = a[4];       
		unit = a[5];        
		value_type = a[6];   
		is_invalid = a[7];  
		severity = a[8];  
		freshtime = a[9];  
	
		
		return true;
	}
		
}
