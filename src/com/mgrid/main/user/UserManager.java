package com.mgrid.main.user;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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
			
		userManaget.put(index, user);
		ResetList();
	}
	
	//删除User
	public void deleteUser(int index)
	{
		
		userManaget.remove(index);
		ResetList();
	}
	
	private void ResetList()
	{
		userList.clear();
		passWordList.clear();
		Iterator<Entry<Integer,User>> it=userManaget.entrySet().iterator();
		while(it.hasNext())
		{
			Entry<Integer,User> entry=it.next();
			User u=entry.getValue();
	
			userList.add(u.getUid());
			passWordList.add(u.getPw());
		}
		
	}
	
	
	
	public void setUser(int index,String UserID,String PassWord)
	{
		

		userManaget.get(index).setUid(UserID);
		userManaget.get(index).setPw(PassWord);
		ResetList();
		
		
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
