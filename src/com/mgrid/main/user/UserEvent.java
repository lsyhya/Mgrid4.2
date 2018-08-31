package com.mgrid.main.user;

public class UserEvent {
	
	private String uid, time, event,eventresult;

	
	public UserEvent(String uid,String time,String event,String eventresult) {
		
		this.uid=uid;
		this.time=time;
		this.event=event;
		this.eventresult=eventresult;
	}
	
	
	public UserEvent() {
		
	}
	
	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
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

	public String getEventresult() {
		return eventresult;
	}

	public void setEventresult(String eventresult) {
		this.eventresult = eventresult;
	}
	

}
