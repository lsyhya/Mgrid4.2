package comm_service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import data_model.local_his_signal;
import data_model.rc_value;


//made author:fjw0312
//date:2015
//notice:
public class local_rc {
	
	float f_value = 0;
	String old_date = "";
	float old_value = -1;
	float D_value = 0;
	float D_mon = 0;
	float D_year = 0;
	public static float buf_3[]= new float[3];
	public List<rc_value> Dlist = new ArrayList<rc_value>();
	
	
	private boolean is_has(String filename,String date_buf){ 
	
		local_file l_file = new local_file();
		l_file.has_file(filename, 4);
		String str_line = l_file.read_str_line(date_buf);
		if(str_line.equals(""))  return false;
		return true;
	}

	private float get_value(String filename, String date_buf){
		if(filename.equals("")||date_buf.equals("")) return 0;
		f_value = 0;
		try{

		local_file l_file = new local_file();
		l_file.has_file(filename, 4);
		String str_line = l_file.read_str_line(date_buf);
		if(str_line.equals(""))  return 0;
	
		local_his_signal line_sig = new local_his_signal(); 
		line_sig.read_string(str_line);		
		f_value = Float.parseFloat(line_sig.value);
		}catch(Exception e){
		
			return 0;
		}
		return f_value;
	}
	
	public List<rc_value> getD_Vlaue(int equipID,int signalID,int year,int mon,int day,int mode){
		String filename="";//文件名
		Dlist.clear();
		try{
			if(mode==0){    //mon=0；
				filename = String.valueOf(equipID)+"-"+String.valueOf(signalID)+"#C_Year#"+String.valueOf(year);
		
				local_file l_file = new local_file(); 
				l_file.has_file(filename, 4);				
				l_file.read_all_line();
				List<String> strlst = new ArrayList<String>();
				strlst = l_file.buflist1;
			
				if(strlst==null) return null;
				for(int i=0;i<strlst.size();i++){
					rc_value rc_class = new rc_value();
					rc_class.read_string( strlst.get(i) );

					Dlist.add(rc_class);   
				}

			}else if(mode==1)
			{
				filename = String.valueOf(equipID)+"-"+String.valueOf(signalID)+"#C_Mon#"+String.valueOf(year);
				
				local_file l_file = new local_file(); 
				l_file.has_file(filename, 4);				
				l_file.read_all_line();
				List<String> strlst = new ArrayList<String>();
				strlst = l_file.buflist1;
			 
				if(strlst==null) return null;
				for(int i=0;i<strlst.size();i++){ 
					rc_value rc_class = new rc_value();
					rc_class.read_string( strlst.get(i) );

					Dlist.add(rc_class);   
				}
			}
			else{
				filename = String.valueOf(equipID)+"-"+String.valueOf(signalID)+"#C_Day#"+String.valueOf(year);
	
				if(day==0){ 
			
					local_file l_file = new local_file(); 
					l_file.has_file(filename, 4);
					String str_mon = String.valueOf(mon);
					if(mon<10) str_mon = "0"+String.valueOf(mon);
					l_file.read_str_all_line(String.valueOf(year)+"-"+str_mon);
					List<String> strlst = new ArrayList<String>();
					strlst = l_file.buflist2;			 
					
					if(strlst==null||strlst.size()==0){ 
						return null;}
				
					for(int i=0;i<strlst.size();i++){
						rc_value rc_class = new rc_value();
						rc_class.read_string( strlst.get(i) );
		
						Dlist.add(rc_class);  
					}
				}
				else{      
				
					local_file l_file = new local_file();
					l_file.has_file(filename, 4);		
					String str_mon = String.valueOf(mon);
					if(mon<10) str_mon = "0"+String.valueOf(mon);
					String str_day = String.valueOf(day);
					if(day<10) str_day = "0"+String.valueOf(day);
					String line = l_file.read_str_line(String.valueOf(year)+"-"+str_mon+"-"+str_day);
					if("".equals(line)) return null;
					rc_value rc_class = new rc_value();
					rc_class.read_string( line );
			 
					Dlist.add(rc_class);  
				}
			}
		}catch(Exception e){
		
		}
	
		return Dlist; 
	}

	public boolean save_rc(String filename,String line_buf){
		try{
	
		local_file l_file = new local_file();
		l_file.has_file(filename, 4);
		l_file.write_line(line_buf);
		}catch(Exception e){
		
			return false;
		}
		return true;
	}
	
	public boolean add_line_deal(String name_head,String str_sig,long time){
		if("".equals(str_sig)||"".equals(name_head)) return false;	
		local_his_signal line_sig = new local_his_signal();
		line_sig.read_string(str_sig);
	
		String r_date = line_sig.freshtime.substring(0,10);  	
		String rc_filename = name_head+"#RC#"+r_date.substring(0,4); 
	
		if(is_has(rc_filename,r_date)) return false;			
		String mon = r_date.substring(5,7);
		String day = r_date.substring(8,10);		

		float value = Float.parseFloat(line_sig.value); //提取数值

	
		long pre_time = time-86400;
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date(pre_time*1000);
		String sampletime = formatter.format(date);
		String dateTime = sampletime.substring(0,10);
		String filename = name_head+"#RC#"+dateTime.substring(0,4);
	
		D_value = value - get_value(filename,dateTime);
		
		
		if(!save_rc(rc_filename, str_sig)){
		
		}

	
		String c_filename = name_head+"#C_Day#"+r_date.substring(0,4);
		rc_value rc_class = new rc_value();
		rc_class.equipid = line_sig.equipid;
		rc_class.equip_name = line_sig.equip_name;
		rc_class.sigid = line_sig.sigid;
		rc_class.name = line_sig.name;
		rc_class.value = String.valueOf(D_value);
		rc_class.unit = line_sig.unit;
		rc_class.datetime = dateTime;
		String buf_line = rc_class.to_string();
		if(!save_rc(c_filename, buf_line)){		
			
		}
	
	
		if("01".equals(day)){			
			int year_p = Integer.parseInt( r_date.substring(0,4) );
			int prev_mon = Integer.parseInt(mon);
		
			if(prev_mon==1){
				prev_mon = 12;
				year_p--; 
			
				String date_y = String.valueOf(year_p)+"-01-01";
				String filename_y = name_head+"#RC#"+String.valueOf(year_p);
		
				float y_int = get_value(filename_y,date_y);
				D_year = value-y_int;
				
						
				rc_value rcYEAR_class = new rc_value();
				rcYEAR_class.equipid = line_sig.equipid;
				rcYEAR_class.equip_name = line_sig.equip_name;
				rcYEAR_class.sigid = line_sig.sigid;
				rcYEAR_class.name = line_sig.name;
				rcYEAR_class.value = String.valueOf(D_year);
				rcYEAR_class.unit = line_sig.unit;
				rcYEAR_class.datetime = String.valueOf(year_p);
				
				String cYEAR_filename = name_head+"#C_Year#"+String.valueOf(year_p);
				String bufYEAR_line = rcYEAR_class.to_string();
				if(!save_rc(cYEAR_filename, bufYEAR_line)){		
					
				}
			
			}else{
				prev_mon--;
			}
			String str_pre_mon = String.valueOf(prev_mon);
			if(prev_mon<10) str_pre_mon ="0"+str_pre_mon;
			String date_h = String.valueOf(year_p)+"-"+str_pre_mon+"-01";
			String filename_h = name_head+"#RC#"+String.valueOf(year_p);

			float h_int = get_value(filename_h,date_h);
			D_mon = value-h_int;
			
	
			String cMON_filename = name_head+"#C_Mon#"+String.valueOf(year_p);
			rc_value rcMON_class = new rc_value();
			rcMON_class.equipid = line_sig.equipid;
			rcMON_class.equip_name = line_sig.equip_name;
			rcMON_class.sigid = line_sig.sigid;
			rcMON_class.name = line_sig.name;
			rcMON_class.value = String.valueOf(D_mon);
			rcMON_class.unit = line_sig.unit;
			rcMON_class.datetime = String.valueOf(year_p)+"-"+str_pre_mon;
			String bufMON_line = rcMON_class.to_string();
			if(!save_rc(cMON_filename, bufMON_line)){		
				
			}
	
		}else{
//			D_mon =    //待完善！
//			D_year =
		}
		buf_3[0] = D_value;		
		buf_3[1] = D_mon;	
		buf_3[2] = D_year;	
		
		return true;
	}
	

}
