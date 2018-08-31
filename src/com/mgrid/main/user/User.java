package com.mgrid.main.user;


/*
 * ”√ªß¿‡
 */

public class User {

	private String uid;
	private String pw;
	
	private String index="";
	
	public User(String UserID,String passWord,String index)
	{
		this.uid=UserID;
		this.pw=passWord;	
		this.index=index;
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
