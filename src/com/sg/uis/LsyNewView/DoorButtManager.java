package com.sg.uis.LsyNewView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mgrid.main.MGridActivity;
import com.mgrid.main.NiuberDoorService;
import com.mgrid.mysqlbase.SqliteUtil;
import com.mgrid.util.TimeUtils;
import com.sg.common.lsyBase.ClientManager;
import com.sg.common.lsyBase.DoorCallBack;
import com.sg.common.lsyBase.MyDoorEvent;
import com.sg.common.lsyBase.MyDoorTime;
import com.sg.common.lsyBase.MyDoorUser;

import android.util.Log;

public class DoorButtManager {

	private ClientManager managers;
	private NBerDoorView nDoorView;

	private String sendData = "";

	public boolean isAdd, isDelete, isOpen, isVip, isSetTime;
	public boolean isAddResult;

	public DoorButtManager(final ClientManager manager, NBerDoorView nDoorView) {

		this.managers = manager;
		this.nDoorView = nDoorView;

		nDoorView.setCallBack(new DoorCallBack() {

			@Override
			public void onSetSuc(Object my) {

				if (isAdd) {

					sendData = "ADD0";
					managers.sendMessage(sendData);
					isAdd = false;

				}

				if (isDelete) {

					sendData = "DELETE0";
					managers.sendMessage(sendData);
					isDelete = false;

				}

				if (isOpen) {
					sendData = "OPEN0";
					managers.sendMessage(sendData);
					isOpen = false;
				}

				if (isVip) {
					sendData = "PROMISE0";
					managers.sendMessage(sendData);
					isVip = false;
				}

				if (isSetTime) {
					sendData = "SETTIME0";
					managers.sendMessage(sendData);
					isSetTime = false;
				}

			}

			@Override
			public void onSetFail() {

				if (isAdd) {

					sendData = "ADD1";
					managers.sendMessage(sendData);
					isAdd = false;

				}

				if (isDelete) {

					sendData = "DELETE1";
					managers.sendMessage(sendData);
					isDelete = false;

				}

				if (isOpen) {
					sendData = "OPEN1";
					managers.sendMessage(sendData);
					isOpen = false;
				}

				if (isVip) {
					sendData = "PROMISE1";
					managers.sendMessage(sendData);
					isVip = false;
				}

				if (isSetTime) {
					sendData = "SETTIME1";
					managers.sendMessage(sendData);
					isSetTime = false;
				}

			}
		});

	}

	public String getSendData(String recive) {

		Log.e("REC", recive);

		if (recive.equals("test")) {

		} else if (recive.startsWith("ADD")) {

			String bean = recive.replace("ADD", "");

			if (!bean.equals("")) {
				addUsr(bean);
			} else {
				nDoorView.callBackResult(false, "");
			}

		} else if (recive.startsWith("DELETE")) {
			String bean = recive.replace("DELETE", "");
			if (!bean.equals("")) {
				deleteUsr(bean);
			} else {
				nDoorView.callBackResult(false, "");
			}

		} else if (recive.startsWith("OPEN")) {

			openDoor();

		} else if (recive.startsWith("PROMISE")) {

			setVip();

		} else if (recive.startsWith("GETUSER")) {

			getUsers();

		} else if (recive.startsWith("GETEVENT")) {

			getEvent();

		} else if (recive.startsWith("SETTIME")) {

			String time = recive.replace("SETTIME", "");
			
			if (!time.equals("")) {
				
				setTime(time);
			
			} else {

				nDoorView.callBackResult(false, "");
			}
		}

		return "";
	}

	private void addUsr(String str) {

		MyDoorUser my = JSON.parseObject(str, MyDoorUser.class);

		if (my != null) {

			isAdd = true;
			nDoorView.add(my.getName(), my.getCardid(), NBerDoorView.UID, NBerDoorView.PW, my.getTime());

		}

	}

	private void deleteUsr(String str) {

		final MyDoorUser my = JSON.parseObject(str, MyDoorUser.class);

		if (my != null) {

			isDelete = true;

			if (my.getCardid().equals("0000000000")) {

				nDoorView.deleteAllUser();

			} else {

				nDoorView.deleteUser(my.getCardid());

			}

		}

	}

	private void openDoor() {

		isOpen = true;
		nDoorView.openDoor();

	}

	private void setVip() {

		isVip = true;
		nDoorView.setVip();

	}

	private void getUsers() {

		nDoorView.getUserData();

		ArrayList<MyDoorUser> list = (ArrayList<MyDoorUser>) nDoorView.getSqliteUtil().getUser();

		if (list != null) {

			String str = JSON.toJSON(list).toString();

			sendData = "GETUSER" + str;

			managers.sendMessage(sendData);

		} else {
			sendData = "GETUSER";
			managers.sendMessage(sendData);
		}
	}

	private void getEvent() {

		SqliteUtil sql = nDoorView.getSqliteUtil();
		if (sql != null) {
			List<MyDoorEvent> list = sql.getListValues();
			if (list != null) {

				String str = JSON.toJSON(list).toString();
				sendData = "GETEVENT" + str;
				managers.sendMessage(sendData);
			} else {
				sendData = "GETEVENT";
				managers.sendMessage(sendData);
			}
		} else {

			sendData = "GETEVENT";
			managers.sendMessage(sendData);

		}

	}

	private void setTime(String str) {

		final MyDoorTime time = JSON.parseObject(str, MyDoorTime.class);

		if (time != null) {
			MGridActivity.ecOneService.execute(new Runnable() {

				@Override
				public void run() {

					isSetTime = true;

					Log.e("TIME", time.getYear() + time.getMonth() + time.getDay() + time.getWeek() + time.getHour() +

							time.getMin() + time.getSec());

					nDoorView.setTime(time.getYear(), time.getMonth(), time.getDay(),
							"0" + TimeUtils.getWeekOfDate(new Date()), time.getHour(), time.getMin(), time.getSec());

				}
			});

		}

	}

}
