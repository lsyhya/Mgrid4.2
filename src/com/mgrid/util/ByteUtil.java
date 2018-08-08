package com.mgrid.util;

/**
 * Author by Winds on 2016/10/18. Email heardown@163.com.
 */
public class ByteUtil {

	// public static void main(String[] args) {
	// byte[] bytes = {
	// (byte) 0xab, 0x01, 0x11
	// };
	// String hexStr = bytes2HexStr(bytes);
	// System.out.println(hexStr);
	// System.out.println(hexStr2decimal(hexStr));
	// System.out.println(decimal2fitHex(570));
	// String adc = "abc";
	// System.out.println(str2HexString(adc));
	// System.out.println(bytes2HexStr(adc.getBytes()));
	// }

	/**
	 * 字节数组转换成对应的16进制表示的字符串
	 *
	 * @param src
	 * @return
	 */
	public static String bytes2HexStr(byte[] src) {
		StringBuilder builder = new StringBuilder();
		if (src == null || src.length <= 0) {
			return "";
		}
		char[] buffer = new char[2];
		for (int i = 0; i < src.length; i++) {
			buffer[0] = Character.forDigit((src[i] >>> 4) & 0x0F, 16);
			buffer[1] = Character.forDigit(src[i] & 0x0F, 16);
			builder.append(buffer);
		}
		return builder.toString().toUpperCase();
	}
	
	/**
	 * byte[] 转成字符�?
	 */
	
	public static String bytestoChatr(byte[] src, int dec, int length) {
	
		byte[] temp = new byte[length];
		System.arraycopy(src, dec, temp, 0, length);
		StringBuffer sb=new StringBuffer();
		sb.append("7E");
		for (int i = 0; i < temp.length; i++) {
			if(temp[i]!=0x7E&&temp[i]!=0x0D)
			{
				sb.append((char)temp[i]);
			}
		}
		sb.append("0D");
		
		return sb.toString();
	}

	/**
	 * 十六进制字节数组转字符串
	 *
	 * @param src
	 *            目标数组
	 * @param dec
	 *            起始位置
	 * @param length
	 *            长度
	 * @return
	 */
	public static String bytes2HexStr(byte[] src, int dec, int length) {
		byte[] temp = new byte[length];
		System.arraycopy(src, dec, temp, 0, length);
		return bytes2HexStr(temp);
	}

	/**
	 * 16进制字符串转10进制数字
	 *
	 * @param hex
	 * @return
	 */
	public static long hexStr2decimal(String hex) {
		return Long.parseLong(hex, 16);
	}

	/**
	 * 把十进制数字转换成足位的十六进制字符�?,并补全空�?
	 *
	 * @param num
	 * @return
	 */
	public static String decimal2fitHex(long num) {
		String hex = Long.toHexString(num).toUpperCase();
		if (hex.length() % 2 != 0) {
			return "0" + hex;
		}
		return hex.toUpperCase();
	}

	/**
	 * 得到L.TH
	 *
	 * @param num
	 * @return
	 */
	public static String getLTH(long num) {

		String hex = Long.toHexString(num).toUpperCase();

		String LTH = hex;

		for (int i = 0; i < 4 - hex.length(); i++) {
			LTH = "0" + LTH;
		}

		char[] chars = LTH.toCharArray();

		int LCHKSUM = 0;

		for (int i = 0; i < chars.length; i++) {
			LCHKSUM += hexChar2byte(chars[i]);
		}

		String LCHKSUM_str = byte2hexChar((~LCHKSUM & 0x0F) + 1); // 取补�?1 返回16进制

		StringBuilder sb = new StringBuilder(LTH);

		return sb.replace(0, 1, LCHKSUM_str).toString();
	}

	/**
	 * 得到CHK-SUM
	 *
	 * @param num
	 * @return
	 */
	public static String getSHK(String str) {
		byte[] bytes = str.getBytes();
		long l = getLong(bytes);
        StringBuffer sb=new StringBuffer();
        sb.append(byte2hexChar((int)(l>>>12)));
        sb.append(byte2hexChar((int)(l>>>8&0x0F)));
        sb.append(byte2hexChar((int)(l>>>4&0x00F)));
        sb.append(byte2hexChar((int)(l&0x000F)));
		
		return sb.toString();
	}

	/**
	 * 字节数组转换成对应的16进制表示的字符串
	 *
	 * @param src
	 * @return
	 */
	public static long getLong(byte[] src) {
		StringBuilder builder = new StringBuilder();

		if (src == null || src.length <= 0) {
			return 0;
		}

		long hex = 0;

		for (int i = 0; i < src.length; i++) {

			hex += Long.parseLong(Integer.toHexString(src[i]), 16);
			builder.append(Integer.toHexString(src[i]));
		}

		hex = (~hex & 0xFFFF) + 1;

		return hex;
	}

	/**
	 * 把十进制数字转换成足位的十六进制字符�?,并补全空�?
	 *
	 * @param num
	 * @param strLength
	 *            字符串的长度
	 * @return
	 */
	public static String decimal2fitHex(long num, int strLength) {
		String hexStr = decimal2fitHex(num);
		StringBuilder stringBuilder = new StringBuilder(hexStr);
		while (stringBuilder.length() < strLength) {
			stringBuilder.insert(0, '0');
		}
		return stringBuilder.toString();
	}

	public static String fitDecimalStr(int dicimal, int strLength) {
		StringBuilder builder = new StringBuilder(String.valueOf(dicimal));
		while (builder.length() < strLength) {
			builder.insert(0, "0");
		}
		return builder.toString();
	}

	/**
	 * 字符串转十六进制字符�?
	 *
	 * @param str
	 * @return
	 */
	public static String str2HexString(String str) {
		char[] chars = "0123456789ABCDEF".toCharArray();
		StringBuilder sb = new StringBuilder();
		byte[] bs = null;
		try {

			bs = str.getBytes("utf8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		int bit;
		for (int i = 0; i < bs.length; i++) {
			bit = (bs[i] & 0x0f0) >> 4;
			sb.append(chars[bit]);
			bit = bs[i] & 0x0f;
			sb.append(chars[bit]);
		}
		return sb.toString();
	}

	/**
	 * 把十六进制表示的字节数组字符串，转换成十六进制字节数�?
	 *
	 * @param
	 * @return byte[]
	 */
	public static byte[] hexStr2bytes(String hex) {
		int len = (hex.length() / 2);
		byte[] result = new byte[len];
		char[] achar = hex.toUpperCase().toCharArray();
		for (int i = 0; i < len; i++) {
			int pos = i * 2;
			result[i] = (byte) (hexChar2byte(achar[pos]) << 4 | hexChar2byte(achar[pos + 1]));
		}
		return result;
	}

	/**
	 * �?16进制字符[0123456789abcde]（含大小写）转成字节
	 *
	 * @param c
	 * @return
	 */
	private static int hexChar2byte(char c) {
		switch (c) {
		case '0':
			return 0;
		case '1':
			return 1;
		case '2':
			return 2;
		case '3':
			return 3;
		case '4':
			return 4;
		case '5':
			return 5;
		case '6':
			return 6;
		case '7':
			return 7;
		case '8':
			return 8;
		case '9':
			return 9;
		case 'a':
		case 'A':
			return 10;
		case 'b':
		case 'B':
			return 11;
		case 'c':
		case 'C':
			return 12;
		case 'd':
		case 'D':
			return 13;
		case 'e':
		case 'E':
			return 14;
		case 'f':
		case 'F':
			return 15;
		default:
			return -1;
		}
	}

	/**
	 * �?16进制字符[0123456789abcde]（含大小写）转成字节
	 *
	 * @param c
	 * @return
	 */
	private static String byte2hexChar(int i) {

		if (i >= 0 && i < 10) {
			return i + "";
		} else if (i == 10) {
			return "A";
		} else if (i == 11) {
			return "B";
		} else if (i == 12) {
			return "C";
		} else if (i == 13) {
			return "D";
		} else if (i == 14) {
			return "E";
		} else if (i == 15) {
			return "F";
		} else {
			return "0";
		}

	}
}
