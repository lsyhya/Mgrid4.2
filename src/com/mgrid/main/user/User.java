package com.mgrid.main.user;


/*
 * ”√ªß¿‡
 */

public class User {

	private String uid;
	private String pw;
	
	private String index="";
	private String time="";
	
	public User(String UserID,String passWord,String index,String time)
	{
		this.uid=UserID;
		this.pw=passWord;	
		this.index=index;
		this.time=time;
	}
	
	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public User()
	{
		
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

	public String getIndex() {
		return index;
	}

	public void setIndex(String index) {
		this.index = index;
	}

	

	
		
}
