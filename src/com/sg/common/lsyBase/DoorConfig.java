package com.sg.common.lsyBase;

import java.util.List;

import com.mgrid.util.ByteUtil;

import android.util.Log;

public class DoorConfig {

	public static String get(int REMARK, int STATUS,String CargID,List<MyDoorUser> user) {
		switch (REMARK) {
		case 0:

			
			
			String s=ByteUtil.hexStr2decimal(CargID)+"";
			
			int i=10-s.length();
			while(i>0)
			{
				s="0"+s;
				i--;
			}
			
			
			Log.e("msg", s);
			
			for(MyDoorUser my:user)
			{
				if(s.equals(my.getCardID()))
				{
					return my.getName()+"开门成功";
				}
			}
			
			
			return "开门成功";
		case 1:

			return "键入用户ID及个人密码开门的记录（取消)";
		case 2:

			return "远程(由SU)开门记录";
		case 3:

			return "手动开门记录";
		case 4:

			return "联动开门记录（取消）";
		case 5:

			return "报警 (或报警取消) 记录";
		case 6:

			return "SM掉电记录";
		case 7:

			return "内部控制参数被修改";
		case 8:

			return "无效的用户卡刷卡记录。";
		case 9:

			return "用户卡的有效期已过";
		case 10:

			return "当前时间该用户卡无进入权限";

		default:
			return "错误信息";
		}
	}

}
