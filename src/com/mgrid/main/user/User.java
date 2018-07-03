package com.mgrid.main.user;


/*
 * 用户类
 */

public class User {

	private String UserID;
	private String passWord;
	private int flag=-1;//0代表管理员，其它代表普通用户
	
	public User(String UserID,String passWord,int flag)
	{
		this.UserID=UserID;
		this.passWord=passWord;
		this.flag=flag;
	}

	public String getUserID() {
		return UserID;
	}

	public void setUserID(String userID) {
		UserID = userID;
	}

	public String getPassWord() {
		return passWord;
	}

	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}
		
}
