package com.mgrid.main.user;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 * 用户管理类
 */
public class UserManager {

	private Map<Integer,User> userManaget=new HashMap<Integer, User>();//所有的用户合集
	private List<String> userList=new ArrayList<String>();//所有的用户名合集    
	private List<String> passWordList=new ArrayList<String>();//所有的密码合集
	private User nowUser=null;//当前登录的用户，  为null表示为未登录

	public Map<Integer,User> getUserManaget() {
		return userManaget;
	}

	public User getNowUser() {
		return nowUser;
	}

	public void setNowUser(User nowUser) {
		this.nowUser = nowUser;
	}

	public void setUserManaget(Map<Integer,User> userManaget) {
		this.userManaget = userManaget;
	}
		
	//添加User
	public void addUser(int index,User user)
	{
		userList.add(user.getUserID());
		passWordList.add(user.getPassWord());
		
		userManaget.put(index, user);
	}
	
	//删除User
	public void deleteUser(int index)
	{
		userList.remove(userManaget.get(index).getUserID());
		passWordList.remove(userManaget.get(index).getPassWord());
		userManaget.remove(index);
	}
	
	public void setUser(int index,String UserID,String PassWord,String OUserID,String OPassWord)
	{
		
		userList.remove(OUserID);
		passWordList.remove(OPassWord);
		
		userList.add(UserID);
		passWordList.add(PassWord);
		
		userManaget.get(index).setUserID(UserID);
		userManaget.get(index).setPassWord(PassWord);
		
		
	}
	
	public List<String> getUserList() {
		return userList;
	}

	public void setUserList(List<String> userList) {
		this.userList = userList;
	}

	public List<String> getPassWordList() {
		return passWordList;
	}

	public void setPassWordList(List<String> passWordList) {
		this.passWordList = passWordList;
	}

	//获得User	
	public User getUser(int index)
	{
		
		return userManaget.get(index);
	}
	
	
	
}
