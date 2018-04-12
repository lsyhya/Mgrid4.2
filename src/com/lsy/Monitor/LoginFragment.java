//package com.lsy.Monitor;
//
//import com.mgrid.main.MonitorActivity;
//import com.mgrid.main.R;
//
//import android.app.Activity;
//import android.app.Fragment;
//import android.content.Context;
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.inputmethod.InputMethodManager;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.LinearLayout;
//import android.widget.Toast;
//
//public class LoginFragment extends Fragment implements OnClickListener {
//
//	private EditText etIP, etPort, etUser, etPsd;
//	private String setIP, setPort, setUser, setPsd;
//	private Button btnLogin, btnBack;
//	private MonitorActivity act;
//	private InputMethodManager im;
//	private LinearLayout lin;
//
//	@Override
//	public void onAttach(Activity activity) {
//		super.onAttach(activity);
//		act = (MonitorActivity) activity;
//	}
//
//	@Override
//	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//
//		View view = inflater.inflate(R.layout.login_monitor, container, false);
//
//		init(view);
//
//		return view;
//	}
//
//	private void init(View view) {
//		im = (InputMethodManager) act.getSystemService(Context.INPUT_METHOD_SERVICE);
//		etIP = (EditText) view.findViewById(R.id.et_ip);
//		etPort = (EditText) view.findViewById(R.id.et_Port);
//		etUser = (EditText) view.findViewById(R.id.et_User);
//		etPsd = (EditText) view.findViewById(R.id.et_Psd);
//		btnLogin = (Button) view.findViewById(R.id.btn_login);
//		btnBack = (Button) view.findViewById(R.id.btn_back);
//
//		lin = (LinearLayout) view.findViewById(R.id.lin_login);
//		btnLogin.setOnClickListener(this);
//		btnBack.setOnClickListener(this);
//		lin.setOnClickListener(this);
//
//	}
//
//	protected void hideSoftInput(View v) {
//		InputMethodManager imm = (InputMethodManager) act.getSystemService(Context.INPUT_METHOD_SERVICE);
//		imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
//	}
//
//	private boolean getStr() {
//
//		setIP = etIP.getText().toString();
//		setPort = etPort.getText().toString();
//		setUser = etUser.getText().toString();
//		setPsd = etPsd.getText().toString();
//
//		if (setIP.equals("") || setPort.equals("") || setUser.equals("") || setPsd.equals("")) {
//			return false;
//		}
//
//		return true;
//	}
//	
//	
//	private void setStr() {
//
//		setIP = etIP.getText().toString();
//		setPort = etPort.getText().toString();
//		setUser = etUser.getText().toString();
//		setPsd = etPsd.getText().toString();
//		
//		act.setEtIP(setIP);
//		act.setEtPort(setPort);
//		act.setEtUser(setUser);
//		act.setEtPsd(setPsd);
//		
//	}
//
//	@Override
//	public void onClick(View v) {
//
//		if (v == btnLogin) {
//			
//			hideSoftInput(v);
//			if (!getStr()) {
//				Toast.makeText(act, "fail", Toast.LENGTH_SHORT).show();
//			} else {
//				
//				setStr();				
//				act.startMonitorAct();
//				
//			}
//
//		} else if (v == btnBack) {
//
//			act.finish();
//
//		} else if (v == lin) {
//			hideSoftInput(v);
//		}
//
//	}
//
//}
