package com.sg.common.lsyBase;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.mgrid.main.MGridActivity;
import com.mgrid.main.user.User;
import com.mgrid.main.user.UserEvent;
import com.sg.common.IObject;
import com.sg.uis.newView.DoorInvented;

import android.util.Log;

public class DoorXuNiManager implements DoorManagerBase,DoorCallBack{
	
	private ClientManager managers;
	private DoorInvented  nDoorView;


	private String  sendData = "";
	private boolean isSend = false;

	
	public DoorXuNiManager(IObject nDoorView) {
		
		this.nDoorView=(DoorInvented) nDoorView;
		this.nDoorView.setCallBack(this);
		
	}
	
	

	@Override
	public void addUsr(String str) {
		
		
		User my = JSON.parseObject(str, User.class);

		if (my != null) {

			nDoorView.Add(my.getUid(),my.getPw(),Integer.parseInt(my.getIndex()),my.getTime());

		}
		
		
	}

	@Override
	public void deleteUsr(String str) {
		
		User my = JSON.parseObject(str, User.class);

		if (my != null) {

			nDoorView.Delete(my.getUid(),my.getPw(),Integer.parseInt(my.getIndex()));

		}

	}

	@Override
	public void setTime(String str) {
		// TODO Auto-generated method stub
		
		
		
	}

	@Override
	public void openDoor(String str) {
	
		User my = JSON.parseObject(str, User.class);
		
		if (my != null) {

			nDoorView.Open(my.getUid(),my.getPw(),Integer.parseInt(my.getIndex()));

		}
		
		//nDoorView.Open(id, pw, index);
		
	}

	@Override
	public void getUsers() {
		
		
		isSend = false;
				
		Map<Integer,User> map=MGridActivity.userManager.getUserManaget();		
		ArrayList<User> list = new ArrayList<>();
		Iterator<Map.Entry<Integer, User>> it=map.entrySet().iterator();
		while(it.hasNext())
		{
			list.add(it.next().getValue());
		}
		
		

		if (list != null) {

			String str = JSON.toJSON(list).toString();

			sendData = "GETUSER" + str;

			managers.sendMessage(sendData);

		} 

		
	}

	@Override
	public void getAllEvent() {
	
		
		isSend = false;
		List<UserEvent> list=nDoorView.getSqliteUtil().getXuNiListValues();
		
		if(list!=null)
		{
			String str=JSON.toJSON(list).toString();
			sendData = "GETEVENTALL" + str;
			managers.sendMessage(sendData);
		}
		
		
	}

	@Override
	public void getNewEvent() {
		
		isSend = false;
		List<UserEvent> list=nDoorView.getSqliteUtil().getXuNiNowListValues();
		
		if(list!=null)
		{
			String str=JSON.toJSON(list).toString();
			sendData = "GETEVENTNEW" + str;
			managers.sendMessage(sendData);
		}
		
	}

	@Override
	public void getSendData(String recive) {
		
        Log.e("REC", recive);
		
		if(managers==null)
		 return;

		isSend = true;

		try {

			if (recive.equals("test")) {

			} else if (recive.startsWith("ADD")) {

				String bean = recive.replace("ADD", "");

				if (!bean.equals("")) {

					addUsr(bean);

				} else {

					nDoorView.callBackResult(false, "Add");
				}

			} else if (recive.startsWith("DELETE")) {

				String bean = recive.replace("DELETE", "");

				if (!bean.equals("")) {

					deleteUsr(bean);

				} else {

					nDoorView.callBackResult(false, "Delete");
				}

			} else if (recive.startsWith("OPEN")) {

				String bean = recive.replace("OPEN", "");
				if (!bean.equals("")) {

					openDoor(bean);

				} else {

					nDoorView.callBackResult(false, "Open");
				}
                

			} else if (recive.startsWith("PROMISE")) {

				

			} else if (recive.startsWith("GETUSER")) {

				getUsers();

			} else if (recive.startsWith("GETEVENTALL")) {

				getAllEvent();

			} else if (recive.startsWith("GETEVENTNEW")) {

				getNewEvent();

			} else if (recive.startsWith("SETTIME")) {

				

			} else {

				nDoorView.callBackResult();

			}

		} catch (Exception e) {

			nDoorView.callBackResult();
		}
		
	}

	@Override
	public void setManager(ClientManager manager) {
		
		this.managers = manager;
	}



	@Override
	public void onSetSuc(String my) {
		
		
		
		
		if (!isSend||managers==null) {
			return;
		}

		isSend = false;

		switch (my) {
		case "Add":

			sendData = "ADD0";
			managers.sendMessage(sendData);

			break;

		case "Delete":

			sendData = "DELETE0";
			managers.sendMessage(sendData);

			break;

		case "Open":

			sendData = "OPEN0";
			managers.sendMessage(sendData);

			break;

		

		default:

			managers.sendMessage("Fail");

			break;
		}
		
		
	}



	@Override
	public void onSetFail(String my) {
		
		
		
		if (!isSend||managers==null) {
			return;
		}

		isSend = false;

		switch (my) {
		case "Add":

			sendData = "ADD1";
			managers.sendMessage(sendData);

			break;

		case "Delete":

			sendData = "DELETE1";
			managers.sendMessage(sendData);

			break;

		case "Open":

			sendData = "OPEN1";
			managers.sendMessage(sendData);

			break;


		default:

			managers.sendMessage("Fail");

			break;
		}
		
		
	}



	@Override
	public void onSetErr() {
		
	
		if (!isSend||managers==null) {
			return;
		}

		isSend = false;

		sendData = "Fail";
		managers.sendMessage(sendData);

		
		
	}

}
