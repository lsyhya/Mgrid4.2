package com.sg.common.lsyBase;

public class MyDoorUser {

	private String name,cardid,uid,pw,time; 
	
	public MyDoorUser(String Name,String CardID,String UID,String PW,String Time) {
		this.name=Name;
		this.cardid=CardID;
		this.uid=UID;
		this.pw=PW;
		this.time=Time;
		
	}
	
	public MyDoorUser()
	{
		
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCardid() {
		return cardid;
	}

	public void setCardid(String cardid) {
		this.cardid = cardid;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getPw() {
		return pw;
	}

	public void setPw(String pw) {
		this.pw = pw;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	
}
