package com.mgrid.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface.OnClickListener;

public class DialogUtils {

	private DialogUtils() {

	}

	private static DialogUtils dialogUtils = new DialogUtils();

	public static DialogUtils getDialog() {
		return dialogUtils;
	}

	public void showDialog(Context context, String title, String content) {
		new AlertDialog.Builder(context).setTitle(title).setMessage(content).show();
	}

	public void showOnClickDialog(Context context, String title, String content,String postText,OnClickListener postlis,String negaText,OnClickListener negalis)
	{
		new AlertDialog.Builder(context).setTitle(title).setMessage(content).setPositiveButton(postText,postlis).setNegativeButton(negaText, negalis).create().show();
	}
	
	
}
