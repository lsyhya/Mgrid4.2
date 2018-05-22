package com.sg.common;

import java.util.Locale;

import android.content.Context;

public class LanguageStr {

	// 语言选择

	// MgridActivity
	public static String Load = "加载完";
	public static String PSS = "照片保存成功";
	public static String PSF = "照片保存失败";

	// DataGetter
	public static String meaning = "通信中断";

	// GridviewActivity+ImageviewActivity
	public static String back = "返回";

	// MainWindow+LsyChangExpression
	public static String Success = "设置成功";
	public static String Fail = "错误";

	// LoginUtil+SgImage
	public static String yes = "进入";
	public static String no = "退出";
	public static String systemExit = "登陆";
	public static String Text1 = "请输入正确";
	public static String Text2 = "用户名或密码错误";

	// CoolButton+SgButton
	public static String Prompt = "提示";
	public static String problem = "是否确定?";
	public static String OK = "确认:";
	public static String ON = "取消";
	public static String text = "删除成功,请自行重启机器";

	// HisEvent+SaveEquipt+sgEventList+SgSignalList
	public static String DeviceName = "设备名称";
	public static String AlarmName = "告警名称";
	public static String AlarmMeaning = "告警含义";
	public static String Numericalsignal = "信号数值";
	public static String AlarmSeverity = "告警等级";
	public static String StartTime = "开始时间";
	public static String EndTime = "结束时间";
	public static String DeviceList = "  设备↓   ";
	public static String SetTime = "设置日期";
	public static String PreveDay = "后一天";
	public static String NextDay = "前一天";
	public static String Receive = "  获取   ";
	public static String AllDevice = "全部设备";
	public static String one = "通知", two = "一般告警", three = "严重告警", four = "致命告警", set = "设置",level="等级";
	public static String SignalName = "信号名称";
	public static String Value = "数值";
	public static String Unit = "单位";
	public static String ValueType = "数值类型";
	public static String AcquisitionTime = "采集时间";

	// SgChangePassWord+SgChangXmlPW
	public static String oldPw = "旧密码:";
	public static String newPw = "新密码:";
	public static String text1 = "密码修改成功";
	public static String text2 = "旧密码输入错误，请重新输入";
	public static String text3 = "密码输入不完整";
	public static String confirm = "确    认:";
	public static String text11 = "你的配置出现错误", text12 = "你根本没有权限页面", text13 = "两次密码输入不一样",
			text14 = "孩子， 你组态配置的标签大于权限页面的个数", text15 = "密码修改成功", text16 = "旧密码输入错误，请重新输入", text17 = "密码输入不完整";

	// SgChangNamePhoneTypeState
	public static String Name = "姓名";
	public static String Phone = "号码";
	public static String Level = "等级";
	public static String Show = "显示";
	public static String Add = "添加";
	public static String Alter = "修改";
	public static String State = "状态";
	public static String delete = "删除";
	public static String NO = "取消";
	public static String text4 = "读取文件出错，可能没有这个文件", text5 = "该列暂未添加号码", text6 = "修改成功", text7 = "号码位数不对",
			text8 = "请输入完整", text9 = "添加成功", text10 = "删除成功";

	// SgImage
	public static String denglu = "用户权限登录";
	public static String pwText = "密码";
	public static String text18 = "请等待加载完成！！";
	public static String text19 = "用户名或密码错误";
	public static String userName = "用户名：";
	public static String PWD = "密    码：";

	// SgIsolationEventSetter
	public static String off = "告警屏蔽", on = "告警开启";

	// SgSignalList
	public static String Names = "名称";
	public static String RefreshTime = "刷新时间";
	public static String Implication = "含义";

	// SgYKParameter
	public static String Chooose = "请选择 ↓";
	
	
	//LanguageStr
	//public static String text_Su = "切换完成,等待30s重启";
	public static String title = "提示";
	public static String content = "切换完成,等待30s重启";
	
	//SgSplineChart
	//public static String text_Su = "切换完成,等待30s重启";
	public static String h = "时";
	public static String d = "日";
	public static String m = "月";
	public static String y = "年";
	
	
	public static String text20 = "文件不完整";
	
	

	// 系统语言和配置语言 true为中文 false为英
	public static boolean systemLanguage = true;
	public static String iniLanguage = "";

	// 获取系统语言
	public static void whatLanguageSystem(Context context) {
		Locale locale = context.getResources().getConfiguration().locale;
		String language = locale.getLanguage();
		if (language.endsWith("zh"))
			systemLanguage = true;
		else
			systemLanguage = false;
	}

	// 设置语言
	public static void setLanguage() {
		if (iniLanguage == null || iniLanguage.equals("")) {
			if (systemLanguage == true) {

			} else {
				setEnglish();
			}
		} else {
			if (iniLanguage.equals("Chinese")) {

			} else if (iniLanguage.equals("English")) {

				setEnglish();
			}
		}
	}

	// 设置英文语言
	public static void setEnglish() {

		// MgridActivity
		Load = "Loaded";
		PSS="Save success";
		PSF="Save failure";

		// DataGetter
		meaning = "Lost";

		// GridviewActivity+ImageviewActivity
		back = "Back";

		// MainWindow+LsyChangExpression
		Success = "Success";
		Fail = "Fail";

		// LoginUtil+SgImage
		yes = "ok";
		no = "cancel";
		systemExit = "LOGIN";
		Text1 = "Please enter sure！！";
		Text2 = "Password or user name error";

		// CoolButton+SgButton
		Prompt = "Hint";
		problem = "Are you sure? ";
		OK = "yes";
		ON = "cancel";
		text = "Delete Success, Please Restart";

		// HisEvent+SaveEquipt+sgEventList+SgSignalList
		DeviceName = "Device Name";
		AlarmName = "Alarm Name";
		AlarmMeaning = "Alarm Meaning";
		Numericalsignal = "Numerical Signal";
		AlarmSeverity = "Alarm Severity";
		StartTime = "Start Time";
		EndTime = "End Time";
		DeviceList = "  Device↓   ";
		SetTime = "Set Time";
		PreveDay = "Previous Day";
		NextDay = "Next Day";
		Receive = "  Receipt   ";
		AllDevice = "All Device";
		one = "Notice";
		two = "GeneralAlarm";
		three = "CriticalAlarm";
		four = "FatalAlarm";
		set = "Set";
		level="Level";

		SignalName = "Signal Name";
		Value = "Value";
		Unit = "Unit";
		ValueType = "Value Type";
		AcquisitionTime = "Acquisition Time";

		// SgChangePassWord+SgChangXmlPW
		oldPw = "Old Password";
		newPw = "New Password";
		text1 = "Password changes succeeded";
		text2 = "Old password entered error, please re-enter";
		text3 = "The password input is incomplete";
		confirm = "Confirm";
		text11 = "There is an error in your configuration";
		text12 = "You have no permissions page at all";
		text13 = "The two password input is not the same";
		text14 = "The label of your configuration configuration is greater than the number of permissions pages ";
		text15 = "Password changes succeeded";
		text16 = "Old password entered error, please re-enter";
		text17 = "The password input is incomplete";

		// SgChangNamePhoneTypeState
		Name = "Name";
		Phone = "Phone";
		Level = "Level";
		Show = "Show";
		Add = "Add";
		Alter = "Alter";
		State = "State";
		delete = "Delete";
		NO = "NO";
		text4 = "NO File";
		text5 = "NO Phone";
		text6 = "Success";
		text7 = "phone num error";
		text8 = "Please input full";
		text9 = "Success";
		text10 = "Success";

		// SgImage
		denglu = "Login";
		pwText = "PassWord";
		text18 = "Please wait for loading to complete！！";
		text19 = "Password or user name error";
		userName = "User  ID:";
		PWD = "PassWord:";

		// SgIsolationEventSetter
		off = "OFF";
		on = "ON";

		// SgSignalList

		Names = "Name";
		RefreshTime = "RefreshTime";
		Implication = "Meaning";

		// SgYKParameter
		Chooose = "Choose↓";
		
		//LanguageStr
		title="Title";
		content = "Please wait for the reboot of the 30s";
		
		//SgSplineChart
		h="hr";
		d="day";
		m="mon";
		y="year";
		
		
		text20="fail";
		
		
	}

}
