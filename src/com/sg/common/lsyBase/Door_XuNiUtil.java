package com.sg.common.lsyBase;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Pattern;

import com.mgrid.main.MGridActivity;
import com.mgrid.main.user.User;
import com.mgrid.main.user.UserEvent;
import com.mgrid.mysqlbase.SqliteUtil;
import com.mgrid.util.MathUtil;
import com.mgrid.util.TimeUtils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public class Door_XuNiUtil {

	private static MGridActivity mActivity = null;
	private Map<Integer, Door_XuNiCallBack> backMap = new HashMap<>();
	private static Door_XuNiUtil niUtil = new Door_XuNiUtil();
	private String nowUser = "";
	private SqliteUtil sql;

	private Door_XuNiUtil() {

	}

	public static Door_XuNiUtil getIntance() {
		return niUtil;
	}

	public void setCallBack(int index, Door_XuNiCallBack back) {

		backMap.put(index, back);

	}

	public void setSql(SqliteUtil sql) {
		this.sql = sql;
	}

	public void setContext(Context context) {
		if (mActivity == null) {

			mActivity = (MGridActivity) context;
		}
	}
	
	public  boolean isNumeric(String str){
	    Pattern pattern = Pattern.compile("[0-9]*");
	    return pattern.matcher(str).matches();   
	}

	/**
	 * 添加用户
	 */

	public void add(final String id, final String pw, final int index,final String time) {

		MGridActivity.ecOneService.execute(new Runnable() {

			@Override
			public void run() {

				Door_XuNiCallBack back = backMap.get(index);

				//Log.e("tag", id+"::"+pw+"::"+index);
				if (id.equals("") || pw.equals("")|| time.equals("")) {

					
					if (back != null) {
						
						back.onFail(3);
						
						
					}
					
					backMap.get(-1).onFail(2);

				} else {

				   

					if (back != null)
					{
						
						if(isNumeric(pw)&&MathUtil.getMathUtil().isValidDate(time))
						{
							Map<String, String> map = new HashMap<String, String>();
							map.put("User" + index, id);
							map.put("PassWord" + index, pw);
							map.put("Time" + index, time);

							User user;
							user = new User(id, pw, index + "",time);
							MGridActivity.userManager.addUser(index, user);

							saveData("SysConf", map, false);
							
							backMap.get(-1).onSuccess(2,id,pw,time);
							back.onSuccess(2,id,pw,time);	
						}else
						{
							backMap.get(-1).onFail(2);
						}
			
					}else
					{
						backMap.get(-1).onFail(2);
					}

				}

			}

		});

	}

	/**
	 * 删除用户
	 */

	public void delete(final String id, final String pw, final int index) {

		MGridActivity.ecOneService.execute(new Runnable() {

			@Override
			public void run() {
				
			
				
				
				
//				if(id.equals("") || pw.equals(""))
//				{
//					
//					
//					if (back != null) {
//						
//						back.onFail(3);						
//						
//					}
//					
//					backMap.get(-1).onFail(3);
//					
//					return ;
//					
//				}
//				

				Door_XuNiCallBack back = backMap.get(index);
				

				if (back != null)
				{
					Map<String, String> map = new HashMap<String, String>();
					map.put("User" + index, id);
					map.put("PassWord" + index, pw);
					map.put("Time" + index, "");
					saveData("SysConf", map, true);
					MGridActivity.userManager.deleteUser(index);					
					back.onSuccess(0,id,pw,"");
					backMap.get(-1).onSuccess(3,id,pw,"");
					
				}else
				{
					backMap.get(-1).onFail(3);
				}
			}
		});

	}

	/**
	 * 修改用户
	 */

	public void alter(final String id, final String pw, final int index,final String time) {
		MGridActivity.ecOneService.execute(new Runnable() {

			@Override
			public void run() {

				Door_XuNiCallBack back = backMap.get(index);
				if (id.equals("") || pw.equals("")) {

					if (back != null)
						back.onFail(3);

				} else {
					Map<String, String> map = new HashMap<String, String>();

					map.put("User" + index, id);
					map.put("PassWord" + index, pw);
					map.put("Time" + index, time);

					saveData("SysConf", map, false);

					MGridActivity.userManager.setUser(index, id, pw,time);

					if (back != null)
						back.onSuccess(1,id,pw,"");

				}

			}
		});
	}

	public void openDoor(String uid, String pw, int index) {
		
		
		if (uid != null) {

			User user = MGridActivity.userManager.getUserManaget().get(index);
			nowUser = uid;
			if (user.getUid().equals(uid) && user.getPw().equals(pw)&&Integer.parseInt(user.getTime())>Integer.parseInt(TimeUtils.getNowFormatTime("yyyyMMdd"))) {
				saveResult(1);

			} else {
				saveResult(0);
			}

		} else {

			if (isSure(pw)) {

			} else {
				saveResult(0);
			}

		}

	}

	// 密码是否正确
	private boolean isSure(String passWord) {

		getNowUser(passWord);

		if (MGridActivity.m_ControlAway == 1) {

			if (MGridActivity.userManager.getNowUser() != null) {

				if (MGridActivity.userManager.getNowUser().getPw().equals(passWord)&&Integer.parseInt(MGridActivity.userManager.getNowUser().getTime())>Integer.parseInt(TimeUtils.getNowFormatTime("yyyyMMdd"))) {

					saveResult(1);
					return true;

				}

			}

		} else if (MGridActivity.m_ControlAway == 0) {

			if (MGridActivity.userManager.getPassWordList().contains(passWord)) {

				saveResult(1);
				return true;

			}

		}

		return false;
	}

	// 保存结果
	private void saveResult(final int i) {

		MGridActivity.ecOneService.execute(new Runnable() {

			@Override
			public void run() {

				String nowTime = TimeUtils.getNowTime();
				String event = "开门";

				switch (i) {
				case 0:

					UserEvent ue1 = new UserEvent(nowUser, nowTime, event, "失败");
					sql.addXuNiEventValue(ue1, 0);
					backMap.get(-1).onFail(0);
					

					break;

				case 1:

					UserEvent ue2 = new UserEvent(nowUser, nowTime, event, "成功");
					sql.addXuNiEventValue(ue2, 0);
					backMap.get(-1).onSuccess(1,"","","");

					break;
				}

			}
		});

	}

	/**
	 * 得到当前操作用户
	 * 
	 * @param string
	 */
	private void getNowUser(String passWord) {

		if (MGridActivity.m_ControlAway == 1) {
			if (MGridActivity.userManager.getNowUser() != null) {
				nowUser = MGridActivity.userManager.getNowUser().getUid();
			}
		} else if (MGridActivity.m_ControlAway == 0) {

			Map<Integer, User> userManaget = MGridActivity.userManager.getUserManaget();
			Iterator<Map.Entry<Integer, User>> it = userManaget.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry<Integer, User> entry = it.next();
				if (entry.getValue().getPw().equals(passWord)) {
					nowUser = entry.getValue().getUid();
					return;
				}

				nowUser = passWord;
			}

		}
	}

	/**
	 * 保存用户到INI
	 * 
	 * @param type
	 * @param map
	 * @param isDelete
	 */
	private void saveData(String type, Map<String, String> map, boolean isDelete) {

		try {

			if (mActivity.iniReader != null) {

				synchronized (MGridActivity.mgridIniPath) {

					BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
							new FileOutputStream(new File(MGridActivity.mgridIniPath)), "gb2312"));

					Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();
					while (it.hasNext()) {
						Map.Entry<String, String> entry = it.next();
						String key = entry.getKey();
						String value = entry.getValue();
						if (isDelete) {

							mActivity.iniReader.removeStr(type, key, value);

						} else {

							mActivity.iniReader.setStr(type, key, value);

						}

					}

					mActivity.iniReader.writeStr(bw);

					bw.flush();
					bw.close();

				}

			}

		} catch (Exception e) {

		}

	}

}
