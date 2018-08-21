package com.sg.common.lsyBase;

public class MyDoorEvent {

	
	public MyDoorEvent(String ci,String ti,String ev) {
		
		this.cardid=ci;
		this.time=ti;
		this.event=ev;
	}
	
	public MyDoorEvent() {
		
		
	}

	public String getCardid() {
		return cardid;
	}



	public void setCardid(String cardid) {
		this.cardid = cardid;
	}



	public String getTime() {
		return time;
	}



	public void setTime(String time) {
		this.time = time;
	}



	public String getEvent() {
		return event;
	}



	public void setEvent(String event) {
		this.event = event;
	}



	private String cardid, time, event;

}
