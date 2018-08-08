package com.sg.common.lsyBase;

public class MyDoorUser {

	private String name,CardID,UID,PW,Time; 
	
	public MyDoorUser(String name,String CardID,String UID,String PW,String Time) {
		this.name=name;
		this.CardID=CardID;
		this.UID=UID;
		this.PW=PW;
		this.Time=Time;
		
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCardID() {
		return CardID;
	}

	public void setCardID(String cardID) {
		CardID = cardID;
	}

	public String getUID() {
		return UID;
	}

	public void setUID(String uID) {
		UID = uID;
	}

	public String getPW() {
		return PW;
	}

	public void setPW(String pW) {
		PW = pW;
	}

	public String getTime() {
		return Time;
	}

	public void setTime(String time) {
		Time = time;
	}
	
}
