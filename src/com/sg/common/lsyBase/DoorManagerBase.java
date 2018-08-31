package com.sg.common.lsyBase;

public interface DoorManagerBase {
	
	
	/**
	 * 添加用户
	 */
	public void addUsr(String str);
	
	/**
	 * 删除用户
	 */
	public void deleteUsr(String str);
	
	
	/**
	 * 设置时间
	 */
	public void setTime(String str);
	
	
	/**
	 * 开门
	 */
	public void openDoor(String str);
	
	/**
	 * 获取所有用户
	 */
	public void getUsers();
	
	/**
	 * 获取所有记录
	 */
	public void getAllEvent();
	
	
	/**
	 * 获取最新记录
	 */
	public void getNewEvent();
	
	
	/**
	 * 处理信息
	 */
	
	public void getSendData(String recive);
	
	/**
	 * 注册ClientManager
	 */
	public void setManager(ClientManager manager);

}
