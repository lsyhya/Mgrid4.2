package com.sg.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class MD5Util
{
	/**
	 * 获取文件MD5码，传入文件输入流。
	 * 
	 * @param in FileInputStream流。
	 * @return MD5码字符串
	 * @throws 
	 */
	public static String getMd5ByFileInputStream(FileInputStream in)
	{
		String value = null;
		
		try
		{
			MappedByteBuffer byteBuffer = in.getChannel().map(FileChannel.MapMode.READ_ONLY, 0, in.available());
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			md5.update(byteBuffer);
			BigInteger bi = new BigInteger(1, md5.digest());
			value = bi.toString(16);
		} catch (Exception e)
		{
			e.printStackTrace();
		} finally
		{
		}

		return value;
	}

	/**
	 * 获取文件MD5码，传入文件对象。
	 * 
	 * @param file File对象。
	 * @return MD5码字符串
	 * @throws FileNotFoundException
	 */
	public static String getMd5ByFile(File file) throws FileNotFoundException
	{
		String value = null;
		FileInputStream in = new FileInputStream(file);

		try
		{
			MappedByteBuffer byteBuffer = in.getChannel().map(FileChannel.MapMode.READ_ONLY, 0, file.length());
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			md5.update(byteBuffer);
			BigInteger bi = new BigInteger(1, md5.digest());
			value = bi.toString(16);
		} catch (Exception e)
		{
			e.printStackTrace();
		} finally
		{
			if (null != in)
			{
				try
				{
					in.close();
				} catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		}

		return value;
	}

	/**
	 * 获取文件MD5码，传入文件路径。
	 * 
	 * @param filepath 文件名，带路径。
	 * @return MD5码字符串
	 * @throws FileNotFoundException
	 */
	public static String getMD5ByFilepath(String filepath) throws FileNotFoundException
	{
		String rtnvel = null;

		if (filepath == null || filepath.isEmpty())
			return rtnvel;

		File file = new File(filepath);

		rtnvel = getMd5ByFile(file);

		return rtnvel;
	}

	/*
	// 获取文件MD5码，传入文件路径。 
	// use Apache Commons Codec.
	public static String getMD5DG(String filepath)
	{
		FileInputStream fis = new FileInputStream(filepath);
		String md5 = DigestUtils.md5Hex(IOUtils.toByteArray(fis));
		IOUtils.closeQuietly(fis);

		return md5;
	}
	*/

	/*
	// test
	public static void main(String[] args) throws IOException
	{
		long begin = System.currentTimeMillis();
		File big = new File("E:\\bigfile.rar");
		String md5 = getFileMD5String(big);
		// String md5 = getFileMD5String_deprecated(big);
		long end = System.currentTimeMillis();
		System.out.println("md5:" + md5 + " time:" + ((end - begin) / 1000) + "s");
	}
	*/

	/**
	 * 适用于上G大的文件
	 * 
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public static String getFileMD5String(File file) throws IOException
	{
		FileInputStream in = new FileInputStream(file);
		byte[] buffer = new byte[1024 * 1024 * 10];
		
		int len = 0;
		while ((len = in.read(buffer)) > 0)
		{
			messagedigest.update(buffer, 0, len);
		}
		
		in.close();
		return bufferToHex(messagedigest.digest());
	}

	/**
	 * 太大的文件会导致内存溢出
	 * 
	 * @deprecated
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public static String getFileMD5String_deprecated(File file) throws IOException
	{
		FileInputStream in = new FileInputStream(file);
		FileChannel ch = in.getChannel();
		MappedByteBuffer byteBuffer = ch.map(FileChannel.MapMode.READ_ONLY, 0, file.length());
		messagedigest.update(byteBuffer);
		
		in.close();
		return bufferToHex(messagedigest.digest());
	}

	public static String getMD5String(String s)
	{
		return getMD5String(s.getBytes());
	}

	public static String getMD5String(byte[] bytes)
	{
		messagedigest.update(bytes);
		return bufferToHex(messagedigest.digest());
	}

	private static String bufferToHex(byte bytes[])
	{
		return bufferToHex(bytes, 0, bytes.length);
	}

	private static String bufferToHex(byte bytes[], int m, int n)
	{
		StringBuffer stringbuffer = new StringBuffer(2 * n);
		int k = m + n;
		for (int l = m; l < k; l++)
		{
			appendHexPair(bytes[l], stringbuffer);
		}
		return stringbuffer.toString();
	}

	private static void appendHexPair(byte bt, StringBuffer stringbuffer)
	{
		char c0 = hexDigits[(bt & 0xf0) >> 4];
		char c1 = hexDigits[bt & 0xf];
		stringbuffer.append(c0);
		stringbuffer.append(c1);
	}

	public static boolean checkPassword(String password, String md5PwdStr)
	{
		String s = getMD5String(password);
		return s.equals(md5PwdStr);
	}

	/**
	 * 默认的密码字符串组合，apache校验下载的文件的正确性用的就是默认的这个组合
	 */
	protected static char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
	protected static MessageDigest messagedigest = null;
	static
	{
		try
		{
			messagedigest = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException nsaex)
		{
			System.err.println(MD5Util.class.getName() + "初始化失败，MessageDigest不支持MD5Util。");
			nsaex.printStackTrace();
		}
	}

}
