package com.sg.common.lsyBase;

public class MyDoorEvent {

	
	public MyDoorEvent(String ci,String ti,String ev) {
		
		this.CardID=ci;
		this.Time=ti;
		this.Event=ev;
	}
	
	public String getCardID() {
		return CardID;
	}

	public void setCardID(String cardID) {
		CardID = cardID;
	}

	public String getTime() {
		return Time;
	}

	public void setTime(String time) {
		Time = time;
	}

	public String getEvent() {
		return Event;
	}

	public void setEvent(String event) {
		Event = event;
	}

	private String CardID, Time, Event;

}
