package com.sg.common.lsyBase;

import java.util.List;

import com.mgrid.util.ByteUtil;

import android.util.Log;

public class DoorConfig {

	public static String get(int REMARK, int STATUS, String CargID, List<MyDoorUser> user) {

		String s=HexStr2TenStrBW(CargID,10);

		Log.e("msg", s);

		switch (REMARK) {
		case 0:

			for (MyDoorUser my : user) {
				if (s.equals(my.getCardid())) {
					return my.getName() + "刷卡成功";
				}
			}

			return "刷卡成功";//"开门成功";
		case 1:

			return "键入用户ID及个人密码开门的记录（取消)";
		case 2:

			return "远程(由SU)开门记录";
		case 3:

			return "内部旋钮开门记录";
		case 4:

			return "联动开门记录（取消）";
		case 5:

			switch (CargID) {

			case "0000000000":

				return "红外报警开始";

			case "0000000001":

				return "红外停止报警";

			case "0000000002":

				return "非正常开门";

			case "0000000003":

				return "关门";//"门被关闭（非正常状态的关门）";

			case "0000000004":

				return "联动(I2)有效";
			case "0000000005":

				return "联动(I2)无效";
			case "0000000006":

				return "SM内部存储器发生错误, SM自动进行了初始化操作";
			case "0000000007":

				return "红外监测被关闭";

			case "0000000008":

				return "红外监测开启";

			case "0000000009":

				return "门碰开关监测被关闭";

			case "0000000010":

				return "门碰开关监测开启";

			case "0000000081":

				return "开门";//"在要求的延时内，正常开门";
			case "0000000082":

				return "超时未开门(在要求的延时内，未开门,但锁是开的)";
			case "0000000083":

				return "关门";//"关门(在要求的延时内关门)";
			case "0000000084":

				return "";//"超时未关门";

			default:
				return "无效的告警记录";
			}

		case 6:

			return "SM掉电记录";
		case 7:

			switch (s) {

			case "0000000000":

				return "修改了SM的密码";

			case "0000000002":

				return "修改了门的特性控制参数";

			case "0000000004":

				return "增加了新用户";

			case "0000000008":

				return "删除了用户资料";

			case "0000000010":

				return "修改了实时钟";

			case "0000000020":

				return "修改了控制准进的时段设置";
			case "0000000040":

				return "修改了节假日列表";
			case "0000000080":

				return "修改了红外开启（关闭）的设置控制字";

			default:
				return "内部控制参数被修改";
			}

		case 8:

			return "无效的用户卡刷卡记录。";
		case 9:

			for (MyDoorUser my : user) {
				if (s.equals(my.getCardid())) {
					return my.getName() + "用户卡的有效期已过 ";
				}
			}

			return "用户卡的有效期已过";
		case 10:

			return "当前时间该用户卡无进入权限";

		case 48:

			return "开门";//"外面钥匙开门记录（进门）";

		default:
			return "";
		}
	}

	public static MyDoorUser getDoorUser(String CargID, String UID, String PW, String TIME, String VIP) {
		
		String CIDNUM=HexStr2TenStrBW(CargID,10);
		String UIDNUM=HexStr2TenStrBW(UID,8);
		
		MyDoorUser my=new MyDoorUser("", CIDNUM, UIDNUM, PW, TIME);
		
		return my;
	}
	
	private static String HexStr2TenStrBW(String str,int num)
	{
		String s = ByteUtil.hexStr2decimal(str) + "";

		int i = num - s.length();

		while (i > 0) {
			s = "0" + s;
			i--;
		}
		
		return s;
	}

}
