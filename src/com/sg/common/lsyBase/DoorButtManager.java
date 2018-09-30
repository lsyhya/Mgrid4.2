package com.sg.common.lsyBase;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mgrid.main.MGridActivity;
import com.mgrid.main.NiuberDoorService;
import com.mgrid.main.user.User;
import com.mgrid.mysqlbase.SqliteUtil;
import com.mgrid.util.TimeUtils;
import com.sg.common.IObject;
import com.sg.uis.newView.NBerDoorView;

import android.util.Log;
import android.widget.Switch;

public class DoorButtManager implements DoorCallBack, DoorManagerBase {

	private ClientManager managers;
	private NBerDoorView nDoorView;


	private String sendData = "";
	private boolean isSend = false;

	public DoorButtManager(IObject nDoorView) {


			this.nDoorView = (NBerDoorView) nDoorView;
			this.nDoorView.setCallBack(this);

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

		case "SetTime":

			sendData = "SETTIME0";
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

		case "SetTime":

			sendData = "SETTIME1";
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

	@Override
	public void addUsr(String str) {

		MyDoorUser my = JSON.parseObject(str, MyDoorUser.class);

		if (my != null) {

			nDoorView.add(my.getName(), my.getCardid(), NBerDoorView.UID, NBerDoorView.PW, my.getTime());

		}

	}

	@Override
	public void deleteUsr(String str) {

		final MyDoorUser my = JSON.parseObject(str, MyDoorUser.class);

		if (my != null) {

			if (my.getCardid().equals("0000000000")) {

				nDoorView.deleteAllUser();

			} else {

				nDoorView.deleteUser(my.getCardid());

			}

		}

	}

	@Override
	public void setTime(String str) {

		final MyDoorTime time = JSON.parseObject(str, MyDoorTime.class);

		if (time != null) {
			MGridActivity.ecOneService.execute(new Runnable() {

				@Override
				public void run() {

					//Log.e("TIME", time.getYear() + time.getMonth() + time.getDay() + time.getWeek() + time.getHour() +

					//		time.getMin() + time.getSec());

					nDoorView.setTime(time.getYear(), time.getMonth(), time.getDay(),
							"0" + TimeUtils.getWeekOfDate(new Date()), time.getHour(), time.getMin(), time.getSec());

				}
			});

		}

	}

	@Override
	public void openDoor(String str) {

		nDoorView.openDoor();

	}

	@Override
	public void getUsers() {

		isSend = false;
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

	@Override
	public void getAllEvent() {

		isSend = false;
		SqliteUtil sql = nDoorView.getSqliteUtil();
		if (sql != null) {
			List<MyDoorEvent> list = sql.getListValues();
			if (list != null) {

				String str = JSON.toJSON(list).toString();
				sendData = "GETEVENTALL" + str;
				managers.sendMessage(sendData);
			} else {
				sendData = "GETEVENTALL";
				managers.sendMessage(sendData);
			}
		} else {

			sendData = "GETEVENTALL";
			managers.sendMessage(sendData);

		}

	}

	@Override
	public void getNewEvent() {

		isSend = false;
		SqliteUtil sql = nDoorView.getSqliteUtil();
		if (sql != null) {

			List<MyDoorEvent> list = sql.getNowListValues();
			if (list != null) {

				String str = JSON.toJSON(list).toString();
				sendData = "GETEVENTNEW" + str;
				managers.sendMessage(sendData);
			} else {

				sendData = "GETEVENTNEW";
				managers.sendMessage(sendData);

			}

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

				openDoor("");

			} else if (recive.startsWith("PROMISE")) {

				

			} else if (recive.startsWith("GETUSER")) {

				getUsers();

			} else if (recive.startsWith("GETEVENTALL")) {

				getAllEvent();

			} else if (recive.startsWith("GETEVENTNEW")) {

				getNewEvent();

			} else if (recive.startsWith("SETTIME")) {

				String time = recive.replace("SETTIME", "");

				if (!time.equals("")) {

					setTime(time);

				} else {

					nDoorView.callBackResult(false, "SetTime");
				}

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

}
