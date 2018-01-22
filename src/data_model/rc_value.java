package data_model;

//made author:fjw0312
//date:2016
//notice:
public class rc_value {
	public String equipid;    //设备id
	public String equip_name; //设备名
	public String sigid;      //信号id
	public String name;        //信号名
	public String value;       //数值
	public String unit;        //单位
	//public String value_type;   //信号类型
	//public String is_invalid;   //0 --> valid, 1 --> invalid
	//public String severity;   //关联告警等级
	public String datetime;   
	


	public String to_string(){
		
		String buf = null;  
		String a1 = equipid + "`";
		String a2 = equip_name + "`";
		String a3 = sigid + "`";
		String a4 = name + "`";
		String a5 = value + "`";
		String a6 = unit + "`";
		//String a7 = value_type + "`";
		//String a8 = is_invalid + "`";
		//String a9 = severity + "`";
		String a10 = datetime;
		
		buf = a1+a2+a3+a4+a5+a6+a10;
		
		return buf;
	}

	public boolean read_string(String buf){
		
		String[] a  = new String[100];
		
		a = buf.split("`");

		if(a.length != 7){
			return false;
		}
		
		equipid = a[0];    //设备id
		equip_name = a[1]; //设备名
		sigid = a[2];      //信号id
		name = a[3];        //信号名
		value = a[4];       //信号值
		unit = a[5];        //单位

		datetime = a[6];   

		
		
		return true;
	}
	
	
}
