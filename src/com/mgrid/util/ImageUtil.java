package com.mgrid.util;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;

public class ImageUtil {

	
	private Context context;
	
	public ImageUtil(Context context)
	{
		this.context=context;
	}
	
	public String getPath(Uri uri) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			if (DocumentsContract.isDocumentUri(context, uri)) {
				// ExternalStorageProvider
				if (isExternalStorageDocument(uri)) {
					final String docId = DocumentsContract.getDocumentId(uri);
					final String[] split = docId.split(":");
					final String type = split[0];

					if ("primary".equalsIgnoreCase(type)) {
						return Environment.getExternalStorageDirectory() + "/" + split[1];
					}

					// TODO handle non-primary volumes
				} else if (isDownloadsDocument(uri)) {

					final String id = DocumentsContract.getDocumentId(uri);
					final Uri contentUri = ContentUris.withAppendedId(
							Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

					return getDataColumn(context, contentUri, null, null);
				} else if (isMediaDocument(uri)) {
					final String docId = DocumentsContract.getDocumentId(uri);
					final String[] split = docId.split(":");
					final String type = split[0];

					Uri contentUri = null;
					if ("image".equals(type)) {
						contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
					} else if ("video".equals(type)) {
						contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
					} else if ("audio".equals(type)) {
						contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
					}

					final String selection = "_id=?";
					final String[] selectionArgs = new String[] {
							split[1]
					};

					return getDataColumn(context, contentUri, selection, selectionArgs);
				}
			}
		}
		String[] proj = { MediaStore.Images.Media.DATA };
		Cursor actualimagecursor = context.getContentResolver().query(uri, proj, null, null, null);
		int actual_image_column_index = actualimagecursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		actualimagecursor.moveToFirst();
		String img_path = actualimagecursor.getString(actual_image_column_index);
		String end = img_path.substring(img_path.length() - 4);
		if (0 != end.compareToIgnoreCase(".jpg") && 0 != end.compareToIgnoreCase(".png")) {
			return null;
		}
		return img_path;
	}
	
	
	/**
	 * @param uri The Uri to check.
	 * @return Whether the Uri authority is ExternalStorageProvider.
	 */
	public static boolean isExternalStorageDocument(Uri uri) {
		return "com.android.externalstorage.documents".equals(uri.getAuthority());
	}

	/**
	 * @param uri The Uri to check.
	 * @return Whether the Uri authority is DownloadsProvider.
	 */
	public static boolean isDownloadsDocument(Uri uri) {
		return "com.android.providers.downloads.documents".equals(uri.getAuthority());
	}

	/**
	 * @param uri The Uri to check.
	 * @return Whether the Uri authority is MediaProvider.
	 */
	public static boolean isMediaDocument(Uri uri) {
		return "com.android.providers.media.documents".equals(uri.getAuthority());
	}

	/**
	 * Get the value of the data column for this Uri. This is useful for
	 * MediaStore Uris, and other file-based ContentProviders.
	 *
	 * @param context The context.
	 * @param uri The Uri to query.
	 * @param selection (Optional) Filter used in the query.
	 * @param selectionArgs (Optional) Selection arguments used in the query.
	 * @return The value of the _data column, which is typically a file path.
	 */
	public static String getDataColumn(Context context, Uri uri, String selection,
									   String[] selectionArgs) {

		Cursor cursor = null;
		final String column = "_data";
		final String[] projection = {
				column
		};

		try {
			cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
					null);
			if (cursor != null && cursor.moveToFirst()) {
				final int index = cursor.getColumnIndexOrThrow(column);
				return cursor.getString(index);
			}
		} finally {
			if (cursor != null)
				cursor.close();
		}
		return null;
	}
	
	
	 public byte[] getNV21(int inputWidth, int inputHeight, Bitmap scaled) {

	       int widht = inputWidth%2 == 0 ? inputWidth : inputWidth-1;
	       int height = inputHeight%2 == 0 ? inputHeight : inputHeight-1;



	        int[] argb = new int[widht * height];

	        scaled.getPixels(argb, 0, widht, 0, 0, widht, height);

	        byte[] yuv = new byte[widht * height * 3 / 2];
	        encodeYUV420SP(yuv, argb, widht, height);

	        //scaled.recycle();

	        return yuv;
	    }

	    private void encodeYUV420SP(byte[] yuv420sp, int[] argb, int width, int height) {
	        final int frameSize = width * height;
	        int yIndex = 0;
	        int uvIndex = frameSize;
	        int a, R, G, B, Y, U, V;
	        int index = 0;
	        for (int j = 0; j < height; j++) {
	            for (int i = 0; i < width; i++) {
	                a = (argb[index] & 0xff000000) >> 24; // a is not used obviously
	                R = (argb[index] & 0xff0000) >> 16;
	                G = (argb[index] & 0xff00) >> 8;
	                B = (argb[index] & 0xff) >> 0;
	                // well known RGB to YUV algorithm
	                Y = ((66 * R + 129 * G + 25 * B + 128) >> 8) + 16;
	                U = ((-38 * R - 74 * G + 112 * B + 128) >> 8) + 128;
	                V = ((112 * R - 94 * G - 18 * B + 128) >> 8) + 128;

	                // NV21 has a plane of Y and interleaved planes of VU each sampled by a factor of 2
	                //    meaning for every 4 Y pixels there are 1 V and 1 U.  Note the sampling is every other
	                //    pixel AND every other scanline.
	                yuv420sp[yIndex++] = (byte) ((Y < 0) ? 0 : ((Y > 255) ? 255 : Y));
	                if (j % 2 == 0 && index % 2 == 0) {
	                    yuv420sp[uvIndex++] = (byte) ((V < 0) ? 0 : ((V > 255) ? 255 : V));
	                    yuv420sp[uvIndex++] = (byte) ((U < 0) ? 0 : ((U > 255) ? 255 : U));
	                }
	                index++;
	            }
	        }
	    }

}
