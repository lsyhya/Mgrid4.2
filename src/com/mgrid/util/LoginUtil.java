package com.mgrid.util;

import java.util.ArrayList;
import java.util.List;

import com.mgrid.MyDialog.SelfDialog;
import com.mgrid.main.FaceActivity;
import com.mgrid.main.MGridActivity;
import com.mgrid.main.R;
import com.mgrid.main.face.FaceBase;
import com.mgrid.main.face.FaceDB;
import com.mgrid.main.face.facelistview.FaceBean;
import com.mgrid.main.face.facelistview.MyAdapter;
import com.mgrid.uncaughtexceptionhandler.MyApplication;
import com.sg.common.LanguageStr;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.nfc.tech.IsoDep;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView.FindListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class LoginUtil {

	private String yes = LanguageStr.yes;
	private String no = LanguageStr.no;
	private String systemExit = LanguageStr.systemExit;

	private String Text1 = LanguageStr.Text1;
	private String Text2 = LanguageStr.Text2;
	
	public static boolean isPlay=false;

	Context context;
	String filePath = Environment.getExternalStorageDirectory().getPath();

	public LoginUtil(Context context) {
		this.context = context;
		
	}

	public void showListDialog()
	{
		
		isPlay=true;
		final String[] item= {"密码登陆","人脸识别"};
		AlertDialog.Builder   dialog=new  AlertDialog.Builder(context);
		dialog.setTitle("登陆方式");
		dialog.setItems(item, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
				switch (which) {
				case 0:
					
					
					showWaiterAuthorizationDialog();
					
					break;
	            case 1:
					
	            	if(MyApplication.mFaceDB.mRegister.size()>0)
	            	{
	            		//selfDialog.show();
	            		Intent  intent =new Intent(context,FaceActivity.class);		            	
		            	((MGridActivity)context).startActivityForResult(intent, 222);
		            	//selfDialog.dismiss();
		            	
	            	}else
	            	{
	            		Toast.makeText(context, "未注册人脸,请选择密码登陆", Toast.LENGTH_SHORT).show();
	            		showListDialog();
	            	}
	            	
	            
	            	
					break;
				}
				
				
			}
		});
		dialog.setCancelable(false);
		dialog.show();
		
	}

	public void showFaceDialog()
	{
		LayoutInflater inflater=LayoutInflater.from(context);
		
		
		final View view=inflater.inflate(R.layout.facelist, null);
		ListView listView=(ListView) view.findViewById(R.id.face_list);
		
		
		List<FaceBean> list=new ArrayList<>();		
		for (FaceDB.FaceRegist faceRe:MyApplication.mFaceDB.mRegister) {
				
			String name=faceRe.mName;
			String keyPath = faceRe.mFaceList.keySet().iterator().next();
		    Bitmap bitmap=	BitmapFactory.decodeFile(keyPath);
		    FaceBean bean=new FaceBean(bitmap, name);
		    list.add(bean);	
	    }
		
				
		MyAdapter adapter=new MyAdapter(context,list);
		listView.setAdapter(adapter);
		
		AlertDialog alertDialog = new AlertDialog.Builder(context)
				.setTitle("人脸列表")

				.setView(view)

				.setPositiveButton("注册",new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						
						
						registerFace();
						
					}
				})

				.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {

						
					}
				}).create();

		alertDialog.show();		
		
	}
	
	public void showAddFaceName(final FaceBase face)
	{
		LayoutInflater inflater=LayoutInflater.from(context);
		
		
		final View view=inflater.inflate(R.layout.facename, null);
		
		
		AlertDialog alertDialog = new AlertDialog.Builder(context)
				.setTitle("绑定姓名")

				.setView(view)

				.setPositiveButton("确定",new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						
						
						EditText et=(EditText) view.findViewById(R.id.facenameet);
						String name=et.getText().toString();
						if(!name.isEmpty())
						{
						    MyApplication.mFaceDB.addFace(name, face.getFace(), face.getBitmap());																
							MGridActivity.faceList.add(face.getFace());
							Toast.makeText(context, "添加成功", Toast.LENGTH_SHORT).show();
							
						}else
						{
							Toast.makeText(context, "名字不能为null", Toast.LENGTH_SHORT).show();
						}			
					}
				})

				.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {

						
					}
				}).create();

		alertDialog.show();		
		
	}
	

	
	//注册人脸
		private  void registerFace()
		{
			Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");				
			ContentValues values = new ContentValues(1);
			values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
			Uri uri = context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
			MyApplication.setUri(uri);
			intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
			MGridActivity.ISFACEACTIVITY=true;
			((MGridActivity)context).startActivityForResult(intent, 111);			
		}
	

	public void showWaiterAuthorizationDialog() {

		LayoutInflater factory = LayoutInflater.from(context);

		final View textEntryView = factory.inflate(R.layout.page_xml, null);

		final AlertDialog alertDialog = new AlertDialog.Builder(context)
				.setTitle(systemExit)

				.setView(textEntryView)

				.setPositiveButton(yes, null)

				.setNegativeButton(no, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {

						MGridActivity activity = (MGridActivity) context;
						activity.finish();
						isPlay=false;
					}
				}).create();

		alertDialog.setCancelable(false);

		alertDialog.show();

		alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View arg0) {

						final EditText etPassword = (EditText) textEntryView
								.findViewById(R.id.pageet);

						String password = etPassword.getText().toString()
								.trim();

						if (password.equals(MGridActivity.loginPassWord)) {

							alertDialog.dismiss();
							isPlay=false;							

						} else {
							Toast.makeText(context, Text2, Toast.LENGTH_SHORT)
									.show();

						}
						
					}
				});
	}
}
