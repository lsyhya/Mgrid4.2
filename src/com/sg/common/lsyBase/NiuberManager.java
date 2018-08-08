package com.sg.common.lsyBase;

import com.mgrid.util.ByteUtil;

public class NiuberManager {

	/*
	 * 授权
	 */

	public static String setVip() {

		StringBuffer SB = new StringBuffer();
		// SB.append("7E");
		SB.append("10");
		SB.append("01");
		SB.append("80");
		SB.append("48");
		SB.append("TH");
		SB.append("INFO");
		// SB.append("SUM");
		// SB.append("0D");
		String info = "F0E0" + "0000000000";
		String th = ByteUtil.getLTH(info.length());
		String data_NOSUM = SB.toString().replace("INFO", info).replace("TH", th);
		String SUM = ByteUtil.getSHK(data_NOSUM);
		String sendData = "7E" + ByteUtil.bytes2HexStr((data_NOSUM + SUM).getBytes()) + "0D";

		return sendData;

	}

	/*
	 * 设置时间 SOI: 7E VER: 10 ADR: 01 CID1: 80 CID2/RTN:49 L.TH :TH INFO :INFO
	 * CHK-SUM:SUM EOI:0D
	 */
	public static String setTime(String year, String month, String day, String week, String hour, String min,
			String sec) {

		String info = "F1E0" + year + month + day + week + hour + min + sec;

		return getSendData(info, true);

	}

	/*
	 * �?�? �?要授�? user="" 不带授权�?�?
	 */

	public static String openDoor(String user) {

		String info = "F1ED01" + user;
		return getSendData(info, true);
	}

	/*
	 * 增加�?个用�? Vip="C0" 默认特权用户
	 */
	public static String addUser(String CardId, String UserId, String UserPassWord, String Time) {

		
		CardId=ByteUtil.decimal2fitHex(Long.parseLong(CardId),10);
		UserId=ByteUtil.decimal2fitHex(Long.parseLong(UserId),8);
	
		
		String info = "F1E3" + CardId + UserId + UserPassWord + Time + "C0";
		return getSendData(info, true);

	}

	/*
	 * 删除�?个用�? 以卡�?
	 * 
	 */
	public static String deleteUserCardId(String CardId) {

		
		CardId=ByteUtil.decimal2fitHex(Long.parseLong(CardId),10);
		String info = "F1E400" + CardId;
		return getSendData(info, true);

	}

	/*
	 * 删除�?个用�? 以用户ID
	 * 
	 */
	public static void deleteUserUserID(String UserId) {

		String info = "F1E40100" + UserId;
		getSendData(info, true);

	}

	/*
	 * 删除�?有用�?
	 * 
	 */
	public static String deleteAllUser() {

		String info = "F1E402" + "0000000000";
		return getSendData(info, true);

	}

	/*
	 * 读取用户数目
	 * 
	 */
	public static void getUserCount() {

		String info = "F2E500";
		getSendData(info, false);

	}

	/*
	 * 读取指定位置用户信息
	 * 
	 */
	public static String getUserInfo(int index) {

		String sIndex = ByteUtil.decimal2fitHex(index);
		String info = "F2E6" + sIndex + "00";

		return getSendData(info, false);

	}

	/*
	 * 查询指定用户编号（ID）的用户是否存在
	 * 
	 */
	public static void getUserInfoUserId(String userId) {

		String info = "F2E6" + userId;

		getSendData(info, false);

	}

	/*
	 * 查询指定卡号的用户卡是否存在
	 * 
	 */
	public static void getUserInfoCardId(String CardId) {

		String info = "F2E6" + CardId;

		getSendData(info, false);

	}

	/*
	 * 读取历史记录柜桶参数
	 * 
	 */
	public static void getHisIndex() {

		String info = "F2E100";

		getSendData(info, false);

	}

	/*
	 * SM将LOADP位置的历史记�?, 连同LOADP本身�?并返�?
	 * 
	 */
	public static String getHisInfo() {

		String info = "F2EE01";

		return getSendData(info, false);

	}

	/*
	 * 读取SM的实时钟
	 */

	public static String getSMTime() {

		String info = "F2E000";
		return getSendData(info, false);
	}
	
	

	private static String getSendData(String info, boolean set) {

		StringBuffer SB = new StringBuffer();
		// SB.append("7E");
		SB.append("10");
		SB.append("01");
		SB.append("80");
		if (set) {
			SB.append("49");
		} else {
			SB.append("4A");
		}
		SB.append("TH");
		SB.append("INFO");
		// SB.append("SUM");
		// SB.append("0D");
		String th = ByteUtil.getLTH(info.length());
		String data_NOSUM = SB.toString().replace("INFO", info).replace("TH", th);
		String SUM = ByteUtil.getSHK(data_NOSUM);
		
		//Log.e("data_NOSUM + SUM", data_NOSUM + SUM);
		
		String sendData = "7E" + ByteUtil.bytes2HexStr((data_NOSUM + SUM).getBytes()) + "0D";

		return sendData;
	}

}
