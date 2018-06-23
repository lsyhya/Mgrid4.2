package data_model;

public class locat_his_DoorEvent {

	public String UserID;
	public String Event;
	public String Time;
	public String Result;
	
	public boolean read_string(String buf){
		
		String[] arg1=buf.split(",");
		if(arg1.length!=4)
		return false;		
		UserID=arg1[0];		
		Event=arg1[2];
		Time=arg1[1];
		Result=arg1[3];
		return true;
	}
	
}
