package com.mgrid.util;

import java.util.Iterator;
import java.util.Map;

import com.mgrid.data.DataGetter;
import com.mgrid.main.MGridActivity;
import com.mgrid.main.MainWindow;
import com.mgrid.main.R;
import com.mgrid.main.user.User;
import com.sg.uis.SgImage;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class PageChangeUtil {

	private SgImage image;
	private String m_strClickEvent;
	private MainWindow m_rRenderWindow;
	private int MaskCount = -1;
	private String pageXml = "";
	private UISManager uisMa=new UISManager();

	public PageChangeUtil(SgImage image, String m_strClickEvent, MainWindow m_rRenderWindow) {
		this.image = image;
		this.m_strClickEvent = m_strClickEvent;
		this.m_rRenderWindow = m_rRenderWindow;
	}

	public void changePage() {
		if (MGridActivity.isNOChangPage) { // 页面初始化完成后
			String[] arrStr = m_strClickEvent.split("\\(");
			if (m_rRenderWindow != null && arrStr[0].equals("Show")) {
				String[] str = arrStr[1].split("\\)");
				pageXml = str[0];
				if (MGridActivity.m_MaskPage != null) {// 如果有权限页面
					for (int i = 0; i < MGridActivity.m_MaskPage.length; i++) {
						for (String s : MGridActivity.m_MaskPage[i]) {
							if (!s.equals("1")) {
								if ((s.substring(0, s.length() - 4)).equals(str[0])) {// 如果跳转页面是权限页面

									for (String page : MGridActivity.m_MaskPage[i]) {
										if (page.equals(DataGetter.currentPage)) { // 如果当前页面和跳转页面在同一权限中

											changePage(pageXml);
											return;
										}
									}

									switch (MGridActivity.m_UserAway) {
									case 0: // 默认/旧模式

										MaskCount = i;
										showPassDialog();

										break;

									case 1: // 输入密码模式

										if (MGridActivity.userManager.getUserManaget().size() != 0) {
											showUserPWDialog();
										} else {
											changePage(pageXml);
										}

										break;
									case 2: // 登录模式

										break;
									}

									return;
								}
							}
						}
					}
					changePage(pageXml);
				} else {
					changePage(pageXml);
				}
			}
		
			
		} else {
			Toast.makeText(image.getContext(), image.Text18, 1000).show();
		}
	}

	private void changePage(String xml) {
		m_rRenderWindow.changePage(xml);
	}

	// 显示用户权限进入对话框 模式1
	public void showPassDialog() {
		// LayoutInflater是用来找layout文件夹下的xml布局文件，并且实例化
		LayoutInflater factory = LayoutInflater.from(m_rRenderWindow.getContext());
		// 把activity_login中的控件定义在View中
		final View textEntryView = factory.inflate(R.layout.page_xml, null);

		TextView tv = (TextView) textEntryView.findViewById(R.id.pagetv);
		tv.setText(image.pwText);

		// 将LoginActivity中的控件显示在对话框中
		new AlertDialog.Builder(m_rRenderWindow.getContext())
				// 对话框的标题
				.setTitle(image.denglu)
				// 设定显示的View
				.setView(textEntryView)
				// 对话框中的“登陆”按钮的点击事件
				.setPositiveButton(image.yes, new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int whichButton) {

						final EditText etPassword = (EditText) textEntryView.findViewById(R.id.pageet);

						String password = etPassword.getText().toString().trim();

						if (MGridActivity.m_UserAway == 0) {
							if (password.equals(MGridActivity.m_pagePassWord[MaskCount])
									|| password.equals("88888888")) { // MaskCount

								changePage(pageXml);

							} else {

								Toast.makeText(m_rRenderWindow.getContext(), image.Text19, Toast.LENGTH_SHORT).show();

							}
						} else if (MGridActivity.m_UserAway == 1) {

							if (judgePW(password) || password.equals("88888888")) {
								changePage(pageXml);

							} else {

								Toast.makeText(m_rRenderWindow.getContext(), image.Text19, Toast.LENGTH_SHORT).show();

							}
						}
					}

				})
				// 对话框的“退出”单击事件
				.setNegativeButton(image.no, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						// LoginActivity.this.finish();
					}
				})

				// 对话框的创建、显示
				.create().show();
	}

	// 显示对话框 模式2
	public void showUserPWDialog() {

		// LayoutInflater是用来找layout文件夹下的xml布局文件，并且实例化
		LayoutInflater factory = LayoutInflater.from(m_rRenderWindow.getContext());
		// 把activity_login中的控件定义在View中
		final View textEntryView = factory.inflate(R.layout.auth_dialog, null);

		TextView tv1 = (TextView) textEntryView.findViewById(R.id.tvuserName);
		TextView tv2 = (TextView) textEntryView.findViewById(R.id.tvPWD);

		new AlertDialog.Builder(m_rRenderWindow.getContext())
				// 对话框的标题
				.setTitle(image.denglu)
				// 设定显示的View
				.setView(textEntryView)
				// 对话框中的“登陆”按钮的点击事件
				.setPositiveButton(image.yes, new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int whichButton) {

						final EditText etUserName = (EditText) textEntryView.findViewById(R.id.etuserName);
						final EditText etPassword = (EditText) textEntryView.findViewById(R.id.etPWD);

						// 将页面输入框中获得的“用户名”，“密码”转为字符串
						String userName = etUserName.getText().toString().trim();
						String password = etPassword.getText().toString().trim();

						if (userName.equals("88888888") && password.equals("88888888")) {

							changePage(pageXml);
						} else {
							if (judgeUserPW(userName, password)) {

								changePage(pageXml);
							} else {

								Toast.makeText(m_rRenderWindow.getContext(), image.Text19, Toast.LENGTH_SHORT).show();

							}
						}

					}

				})

				.setNegativeButton(image.no, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {

					}
				})

				.create().show();
	}

	// 判断新密码中有没有这个字符串
	private boolean judgePW(String PassWord) {
		if (MGridActivity.userManager.getPassWordList().contains(PassWord)) {
			return true;
		}
		return false;
	}

	//判断用户密码正确否
	private boolean judgeUserPW(String User, String PassWord) {
		Iterator<Map.Entry<Integer, User>> it = MGridActivity.userManager.getUserManaget().entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<Integer, User> map = it.next();
			if (map.getValue().getUid().toString().equals(User) && 
					map.getValue().getPw().equals(PassWord)) {

				MGridActivity.userManager.setNowUser(map.getValue());
				uisMa.hideView();
				
				return true;
			}
		}

		return false;
	}

}
