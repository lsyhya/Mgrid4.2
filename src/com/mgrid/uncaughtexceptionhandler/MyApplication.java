package com.mgrid.uncaughtexceptionhandler;

import com.mgrid.main.face.FaceDB;

import android.app.Application;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.media.ExifInterface;
import android.net.Uri;
import android.util.Log;

public class MyApplication extends Application {

	public static Typeface typeface;
	private static Uri image;
	private static final String  dbPath="SQL";
	public  static FaceDB mFaceDB;

	@Override
	public void onCreate() {
		super.onCreate();

		typeface = Typeface.createFromAsset(getAssets(), "fonts/kt.ttf");
		CrashHandler crashHandler = CrashHandler.getInstance();
		crashHandler.init(getApplicationContext());
		
		mFaceDB=new FaceDB(this.getExternalCacheDir().getPath());
		image=null;
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
			
				mFaceDB.loadFaces();
				
			}
		}).start();
	}
	
	public static void  setUri(Uri images)
	{
		image=images;
	}
	
	public static Uri getUri()
	{
		return image;
	}
	
	
	public static Bitmap decodeImage(String path) {
		Bitmap res;
		try {
			ExifInterface exif = new ExifInterface(path);
			int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

			BitmapFactory.Options op = new BitmapFactory.Options();
			op.inSampleSize = 1;
			op.inJustDecodeBounds = false;
			//op.inMutable = true;
			res = BitmapFactory.decodeFile(path, op);
			//rotate and scale.
			Matrix matrix = new Matrix();

			if (orientation == ExifInterface.ORIENTATION_ROTATE_90) {
				matrix.postRotate(90);
			} else if (orientation == ExifInterface.ORIENTATION_ROTATE_180) {
				matrix.postRotate(180);
			} else if (orientation == ExifInterface.ORIENTATION_ROTATE_270) {
				matrix.postRotate(270);
			}

			if(res!=null)
			{
				Bitmap temp = Bitmap.createBitmap(res, 0, 0, res.getWidth(), res.getHeight(), matrix, true);
				Log.d("com.arcsoft", "check target Image:" + temp.getWidth() + "X" + temp.getHeight());

				if (!temp.equals(res)) {
					res.recycle();
				}
				return temp;
			}
			

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	
//	@Override
//	public SQLiteDatabase openOrCreateDatabase(String name, int mode, CursorFactory factory,
//			DatabaseErrorHandler errorHandler) {
//
//		SQLiteDatabase sqLiteDatabase = SQLiteDatabase.openOrCreateDatabase(getDatabasePath(name), null);
//
//		return sqLiteDatabase;
//	}
//
//	@Override
//	public SQLiteDatabase openOrCreateDatabase(String name, int mode, CursorFactory factory) {
//
//		SQLiteDatabase result = SQLiteDatabase.openOrCreateDatabase(getDatabasePath(name), null);
//		return result;
//
//	}
//
//	@Override
//	public File getDatabasePath(String name) {
//		File parentFile = new File(
//				Environment.getExternalStorageDirectory() + File.separator + dbPath + File.separator);
//		if (!parentFile.exists()) {
//			
//			boolean mkParentRes = parentFile.mkdirs();
//		
//		}
//
//		File realDBFile = new File(parentFile, name);
//		if (!realDBFile.exists()) {
//			try {
//				realDBFile.createNewFile();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
//		return realDBFile;
//
//	}

}
