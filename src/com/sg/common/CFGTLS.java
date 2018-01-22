package com.sg.common;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Hashtable;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.util.Log;

public class CFGTLS
{
	public static double getPadHeight(double objheight, double formheight, float fontsize)
	{
		double scheight = 768;
		double rate = scheight/formheight;
		return objheight*rate-CFGTLS.getFontHeight(fontsize);
	}
	
	public static double getFontHeight(float fontSize)
	{
		PAINT.setTextSize(fontSize);
	    FontMetrics fm = PAINT.getFontMetrics();
	    
	    //return (int) Math.ceil(fm.descent - fm.ascent);
	    return Math.ceil(fm.descent - fm.top) + 2;
	}
	
	public static int parseColor(String strValue)
	{
/*
	 // 解析文字描述颜色
   	 if ("BLACK".equalsIgnoreCase(strValue))
   	 {
   		return Color.BLACK;
   	 }
   	 else if ("BLUE".equalsIgnoreCase(strValue))
   	 {
   		return Color.BLUE;
   	 }
   	 else if ("CYAN".equalsIgnoreCase(strValue))
   	 {
   		return Color.CYAN;
   	 }
   	 else if ("DKGRAY".equalsIgnoreCase(strValue))
   	 {
   		return Color.DKGRAY;
   	 }
   	 else if ("GRAY".equalsIgnoreCase(strValue))
   	 {
   		return Color.GRAY;
   	 }
   	 else if ("GREEN".equalsIgnoreCase(strValue))
   	 {
   		return Color.GREEN;
   	 }
   	 else if ("LTGRAY".equalsIgnoreCase(strValue))
   	 {
   		return Color.LTGRAY;
   	 }
   	 else if ("MAGENTA".equalsIgnoreCase(strValue))
   	 {
   		return Color.MAGENTA;
   	 }
   	 else if ("RED".equalsIgnoreCase(strValue))
   	 {
   		return Color.RED;
   	 }
   	 else if ("TRANSPARENT".equalsIgnoreCase(strValue))
   	 {
   		return Color.TRANSPARENT;
   	 }
   	 else if ("WHITE".equalsIgnoreCase(strValue))
   	 {
   		return Color.WHITE;
   	 }
   	 else if ("YELLOW".equalsIgnoreCase(strValue))
   	 {
   		 return  Color.YELLOW;
   	 }
*/
   	 if("Highlight".equalsIgnoreCase(strValue)||"MenuHighlight".equalsIgnoreCase(strValue))
   	 {
   		strValue="#FF48B0FF";
   	 }
		
 	
	 if(strValue.equals("LimeGreen"))
	 {
		 return Color.rgb(96, 208, 133);
	 }else if(strValue.equals("Silver"))
	 {
		 return Color.rgb(209, 209, 209);
	 }
		
   	    // 解析 AA,RR,GG,BB 格式
		if (strValue.contains(","))
		{
		String str[] = strValue.split(",");
		int array[] = new int[str.length];

		for (int i = 0; i < str.length; i++)
			array[i] = Integer.parseInt(str[i].trim());

		// 判断是否有透明度
		if (3 == str.length)
			return Color.rgb(array[0], array[1], array[2]);
		else if (3 < str.length)
			return Color.argb(array[0], array[1], array[2], array[3]);
		}

		/*
   	  // 解析 FF000000 格式
	  if (strValue.length() == 8) return Color.parseColor("#"+strValue);
	  // 解析 ##FF000000 格式
	  else if(strValue.length() == 10)
	  {
		  String str1 = strValue.substring(1);
		  return Color.parseColor(str1);
	  }
	  */
		
	  // 尝试直接解析。
	  return Color.parseColor(strValue);
	}
	
	/**
	 * 获取图片对象， 传入MD5码。
	 * 
	 * @param md5 MD5码字符串
	 * @return 成功返回BitMap，否则返回null。
	 * @throws 
	 */
	public static Bitmap getBitmap(String md5)
	{
		if (null == md5 || md5.isEmpty()) return null;
		
		return ht_images.get(md5);
	}
	
	/**
	 * 获取图片对象， 传入文件路径。当资源不存在时将创建该资源。
	 * 
	 * @param filename 文件名，带路径。
	 * @return 成功返回BitMap，否则返回null。
	 * @throws FileNotFoundException
	 */
	public static Bitmap getBitmapByPath(String filename) throws FileNotFoundException
	{
		String md5 = MD5Util.getMD5ByFilepath(filename);
		if (null == md5 || md5.isEmpty()) return null;
		
		Bitmap bitmap = ht_images.get(md5);
		
		if (null != bitmap) return bitmap;
		
		// 该图片文件未被加载过，创建其图片对象。
		BitmapFactory.Options o = null;
		FileInputStream stream = null;

		if (ZOOMOUT || ZOOMIN)
		{
			stream = new FileInputStream(filename);

			// 解码图片尺寸，仅解码METADATA元素区。
			o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;
			
			BitmapFactory.decodeStream(stream, null, o);
			
			try
			{
				stream.close();
			} catch (IOException e)
			{
				e.printStackTrace();
			}

			// 获取原始图片尺寸
			int inWidth = o.outWidth;
			int inHeight = o.outHeight;
			Log.v("Warnning", String.format("Original bitmap size: (%dx%d).", inWidth, inHeight));

			// 获取预缩放图片尺寸
			int targetWidth = 1024;
			int targetHeight = 768;
			o = new BitmapFactory.Options();
			o.inSampleSize = Math.max(inWidth / targetWidth, inHeight / targetHeight);
			
			// 系统内存不足时可回收pixels
			o.inPurgeable = true;
			
			// 设置为非深拷贝，重要(但此处关闭)！
			o.inInputShareable = true;
			
			if (!BITMAP_HIGHQUALITY)
			{
				// 表示16位位图 565代表对应三原色占的位数
				o.inPreferredConfig = Bitmap.Config.RGB_565;
			}
			
			// TODO: 提供多种显示质量，INI可配。
		}

		// 解码预缩放图片
		stream = new FileInputStream(filename);
		bitmap = BitmapFactory.decodeStream(stream, null, o);
		
		try
		{
			stream.close();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		finally
		{
			stream = null;
		}
		
		ht_images.put(md5, bitmap);
		
		Log.v("Warnning", String.format("Pre-sized bitmap size: (%dx%d).", bitmap.getWidth(), bitmap.getHeight()));
		return bitmap;
	} // end of getBitmap
	
    private static Paint PAINT = new Paint();
	private static Hashtable<String, Bitmap> ht_images = new Hashtable<String, Bitmap>();
	
	private static boolean ZOOMOUT = true;
	private static boolean ZOOMIN = false;
	
	public static boolean BITMAP_HIGHQUALITY = false;
}
