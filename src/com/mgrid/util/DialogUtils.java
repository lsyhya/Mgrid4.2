package com.mgrid.util;

import android.app.AlertDialog;
import android.content.Context;

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

}
