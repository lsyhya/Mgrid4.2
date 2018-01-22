package com.sg.uis;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.SweepGradient;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mgrid.data.DataGetter;
import com.mgrid.data.EquipmentDataModel.Equipment;
import com.mgrid.data.EquipmentDataModel.Signal;
import com.mgrid.main.MGridActivity;
import com.mgrid.main.MainWindow;
import com.mgrid.util.XmlUtils;
import com.sg.common.CFGTLS;
import com.sg.common.IObject;
import com.sg.common.UtIniReader;
import comm_service.service;

import data_model.ipc_control;

/**
 * 
 * @author lsy 告警联动
 */
@SuppressLint({ "ShowToast", "ClickableViewAccessibility", "HandlerLeak",
		"SimpleDateFormat", "DrawAllocation" })
@SuppressWarnings("unused")
public class SgAlarmAction extends TextView implements IObject {

	// 设置告警判断值
	private TextView Et_inputValue = null;
	// private TextView Et_inputValue_t = null;
	// 设置 命令意义
	private TextView Tv_outputCommandMeaning = null;
	// 设置告警延时
	public EditText Et_timeLapse = null;
	// 保存 删除按钮
	private Button Btn_delect = null;
	private Button Btn_ensure = null;

	// private Button Btn_stop = null;
	// 设备 信号 单选框 (输入)1
	private TextView Tv_Input_equipList = null;
	private TextView Tv_Input_SignalList = null;
	// 设备 信号 单选框 (输入)2
	// private TextView Tv_Input_equipList_t = null;
	// private TextView Tv_Input_SignalList_t = null;

	// 设备 命令名字 单选框 (输出)
	private TextView Tv_Output_equipList = null;
	private TextView Tv_Output_CommandNameList = null;
	// 检测结果
	private TextView Tv_Detection_equipList = null;
	private TextView Tv_Detection_SignalList = null;
	private TextView Et_Detection_result = null;

	// 设备 信号列表
	private String[] equaipList = null;
	private String[] signalList = null;
	private String[] signalList_De = null;
	private String[] CommandNameList = null;
	private String[] CommandMeaning = null;
	private String[] SignalMeaning = null;
	// private String[] SignalMeaning_t = null;
	private String[] SignalMeaning_D = null;
	private String[] signalList_t = null;

	//
	private static String equipName = "设备";
	private static String signalName = "信号";
	private static String commandName = "命令";
	private static String stateName = "状态";

	private String path = "/data/mgrid/sampler/XmlCfg";
	private String Vtu_path = "/data/mgrid/sampler/XmlCfg/MonitorUnitVTU.xml";

	private String event_path = Environment.getExternalStorageDirectory()
			.getPath() + "/Command";
	private String event_data = "/data/mgrid/sampler/SO";
	private String dialog_path = "/mgrid/data/Command";

	public SgAlarmAction(final Context context) {
		super(context);

		m_oPaint = new Paint();
		m_rBBox = new Rect();

		Et_inputValue = new TextView(context);

		Et_Detection_result = new TextView(context);
		Et_timeLapse = new EditText(context);

		Btn_delect = new Button(context);
		Btn_ensure = new Button(context);
		// Btn_stop = new Button(context);
		Tv_Input_equipList = new TextView(context);
		Tv_Input_SignalList = new TextView(context);
		Tv_Output_equipList = new TextView(context);
		Tv_Output_CommandNameList = new TextView(context);
		Tv_outputCommandMeaning = new TextView(context);
		Tv_Detection_equipList = new TextView(context);
		Tv_Detection_SignalList = new TextView(context);

		Et_timeLapse.setBackgroundResource(android.R.drawable.edit_text);

		Btn_delect.setBackgroundResource(android.R.drawable.btn_default_small);
		Btn_ensure.setBackgroundResource(android.R.drawable.btn_default_small);

		Et_inputValue.setBackgroundColor(Color.argb(100, 100, 100, 100));

		Et_Detection_result.setBackgroundColor(Color.argb(100, 100, 100, 100));
		Tv_Input_equipList.setBackgroundColor(Color.argb(100, 100, 100, 100));
		Tv_Input_SignalList.setBackgroundColor(Color.argb(100, 100, 100, 100));
		Tv_Output_equipList.setBackgroundColor(Color.argb(100, 100, 100, 100));

		Tv_Output_CommandNameList.setBackgroundColor(Color.argb(100, 100, 100,
				100));
		Tv_outputCommandMeaning.setBackgroundColor(Color.argb(100, 100, 100,
				100));
		Tv_Detection_equipList.setBackgroundColor(Color
				.argb(100, 100, 100, 100));
		Tv_Detection_SignalList.setBackgroundColor(Color.argb(100, 100, 100,
				100));

		// Et_inputValue.setPadding(0, 0, 0, 0);
		// Et_inputValue_t.setPadding(0, 0, 0, 0);
		// Et_Detection_result.setPadding(0, 0, 0, 0);
		Et_timeLapse.setPadding(0, 0, 0, 0);

		Btn_delect.setPadding(0, 0, 0, 0);
		Btn_ensure.setPadding(0, 0, 0, 0);
		// Btn_stop.setPadding(0, 0, 0, 0);

		Et_inputValue.setTextSize(16);
		// Et_inputValue_t.setTextSize(16);
		Et_Detection_result.setTextSize(16);
		Et_timeLapse.setTextSize(20);

		Btn_delect.setTextSize(20);
		Btn_ensure.setTextSize(20);
		// Btn_stop.setTextSize(20);
		Tv_Input_equipList.setTextSize(16);
		Tv_Input_SignalList.setTextSize(16);
		// Tv_Input_equipList_t.setTextSize(16);
		// Tv_Input_SignalList_t.setTextSize(16);

		Tv_Output_equipList.setTextSize(16);
		Tv_Output_CommandNameList.setTextSize(16);
		Tv_outputCommandMeaning.setTextSize(16);
		Tv_Detection_equipList.setTextSize(16);
		Tv_Detection_SignalList.setTextSize(16);

		Et_inputValue.setTextColor(Color.BLACK);
		// Et_inputValue_t.setTextColor(Color.BLACK);
		Et_Detection_result.setTextColor(Color.BLACK);
		Et_timeLapse.setTextColor(Color.BLACK);
		Btn_delect.setTextColor(Color.BLACK);
		Btn_ensure.setTextColor(Color.BLACK);

		Tv_Input_equipList.setTextColor(Color.BLACK);
		Tv_Input_SignalList.setTextColor(Color.BLACK);

		Tv_Output_equipList.setTextColor(Color.BLACK);
		Tv_Output_CommandNameList.setTextColor(Color.BLACK);
		Tv_outputCommandMeaning.setTextColor(Color.BLACK);
		Tv_Detection_equipList.setTextColor(Color.BLACK);
		Tv_Detection_SignalList.setTextColor(Color.BLACK);

		Btn_delect.setText("删除");
		Btn_ensure.setText("确认");

		Tv_Input_equipList.setText(equipName);
		Tv_Input_SignalList.setText(signalName);

		Tv_Output_equipList.setText(equipName);
		Tv_Output_CommandNameList.setText(commandName);
		Tv_outputCommandMeaning.setText(stateName);
		Tv_Detection_equipList.setText(equipName);
		Tv_Detection_SignalList.setText(signalName);
		Et_inputValue.setText("状态");
		Et_Detection_result.setText("状态");

		Et_timeLapse.setSingleLine();

		Et_inputValue.setGravity(Gravity.CENTER);

		Et_Detection_result.setGravity(Gravity.CENTER);
		Et_timeLapse.setGravity(Gravity.CENTER);
		Btn_delect.setGravity(Gravity.CENTER);
		Btn_ensure.setGravity(Gravity.CENTER);

		Tv_Input_equipList.setGravity(Gravity.CENTER);
		Tv_Input_SignalList.setGravity(Gravity.CENTER);

		Tv_Output_equipList.setGravity(Gravity.CENTER);
		Tv_Output_CommandNameList.setGravity(Gravity.CENTER);
		Tv_outputCommandMeaning.setGravity(Gravity.CENTER);
		Tv_Detection_equipList.setGravity(Gravity.CENTER);
		Tv_Detection_SignalList.setGravity(Gravity.CENTER);

		imm = (InputMethodManager) context
				.getSystemService(Context.INPUT_METHOD_SERVICE);

		Et_inputValue.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// imm.showSoftInput(Et_inputValue,
				// InputMethodManager.SHOW_FORCED);// 获取到这个类。
				// Et_inputValue.setFocusableInTouchMode(true);// 获取焦点
				showSignalMeaning(context);

			}
		});

		// Et_inputValue_t.setOnClickListener(new View.OnClickListener() {
		//
		// @Override
		// public void onClick(View arg0) {
		// // imm.showSoftInput(Et_inputValue_t,
		// // InputMethodManager.SHOW_FORCED);// 获取到这个类。
		// // Et_inputValue_t.setFocusableInTouchMode(true);// 获取焦点
		// showSignalMeaning_t(context);
		// }
		// });

		Et_timeLapse.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				imm.showSoftInput(Et_timeLapse, InputMethodManager.SHOW_FORCED);// 获取到这个类。
				Et_timeLapse.setFocusableInTouchMode(true);// 获取焦点

			}
		});

		Et_Detection_result.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// imm.showSoftInput(Et_Detection_result,
				// InputMethodManager.SHOW_FORCED);// 获取到这个类。
				// Et_Detection_result.setFocusableInTouchMode(true);// 获取焦点
				showSignalMeaning_D(context);

			}
		});

		// Et_inputValue.setCursorVisible(true);
		// Et_inputValue_t.setCursorVisible(true);
		// Et_Detection_result.setCursorVisible(true);
		Et_timeLapse.setCursorVisible(true);

		// Et_inputValue.setInputType(EditorInfo.TYPE_CLASS_PHONE);
		// Et_inputValue_t.setInputType(EditorInfo.TYPE_CLASS_PHONE);
		// Et_Detection_result.setInputType(EditorInfo.TYPE_CLASS_PHONE);
		Et_timeLapse.setInputType(EditorInfo.TYPE_CLASS_PHONE);

		Tv_Input_equipList.setClickable(true);
		Tv_Input_SignalList.setClickable(true);
		// Tv_Input_equipList_t.setClickable(true);
		// Tv_Input_SignalList_t.setClickable(true);
		Tv_Output_equipList.setClickable(true);
		Tv_Output_CommandNameList.setClickable(true);
		Tv_Detection_equipList.setClickable(true);
		Tv_Detection_SignalList.setClickable(true);

		View.OnTouchListener o = new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {

				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:

					v.setBackgroundColor(Color.GREEN);

					break;

				case MotionEvent.ACTION_UP:

					v.setBackgroundColor(Color.argb(100, 100, 100, 100));

					break;

				}
				return false;
			}
		};

		Tv_outputCommandMeaning.setOnTouchListener(o);
		Tv_Input_equipList.setOnTouchListener(o);
		Tv_Input_SignalList.setOnTouchListener(o);
		Tv_Output_equipList.setOnTouchListener(o);
		Tv_Output_CommandNameList.setOnTouchListener(o);
		Tv_Detection_equipList.setOnTouchListener(o);
		Tv_Detection_SignalList.setOnTouchListener(o);

		Tv_Input_equipList.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (neepUpdateTime) {
					get_equiptList();
					showEquaip(context, 0);
				} else {
					Toast.makeText(getContext(), "当前有告警联动，请等待系统处理完毕", 1000)
							.show();
				}

			}
		});


		Tv_Input_SignalList.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (neepUpdateTime) {
					
					get_SignalList(0);
					showSignal(context, 0);
				} else {
					Toast.makeText(getContext(), "当前有告警联动，请等待系统处理完毕", 1000)
							.show();
				}
			}
		});



		Tv_Output_equipList.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (neepUpdateTime) {
					if (isEquaipRun_output) {
						isEquaipRun_output = false;
						get_equiptList();
					}
					showEquaip(context, 1);
				} else {
					Toast.makeText(getContext(), "当前有告警联动，请等待系统处理完毕", 1000)
							.show();
				}

			}
		});

		Tv_Output_CommandNameList.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (neepUpdateTime) {
					showCommName(context);
				} else {
					Toast.makeText(getContext(), "当前有告警联动，请等待系统处理完毕", 1000)
							.show();
				}

			}
		});

		Tv_outputCommandMeaning.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {

				if (neepUpdateTime) {
					showCommandMeaning(context);
				} else {
					Toast.makeText(getContext(), "当前有告警联动，请等待系统处理完毕", 1000)
							.show();
				}

			}
		});

		Tv_Detection_equipList.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (neepUpdateTime) {
					if (isEquaipRun_Detection) {
						isEquaipRun_Detection = false;
						get_equiptList();
					}
					showEquaip(context, 2);
				} else {
					Toast.makeText(getContext(), "当前有告警联动，请等待系统处理完毕", 1000)
							.show();
				}

			}
		});

		Tv_Detection_SignalList.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (neepUpdateTime) {
					
					getDeSignal();
					showSignal(context, 1);
				} else {
					Toast.makeText(getContext(), "当前有告警联动，请等待系统处理完毕", 1000)
							.show();
				}

			}
		});

		Btn_delect.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Btn_delect.setEnabled(false);
				handler.postDelayed(runable, 2000);
				if (neepUpdateTime) {
					delectFile();
				} else {

					Toast.makeText(getContext(), "当前有告警联动，请等待系统处理完毕", 1000)
							.show();
				}
			}
		});

		Btn_ensure.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Btn_ensure.setEnabled(false);
				handler.postDelayed(runable, 2000);
				
				if (Tv_Input_equipList.getText().toString().equals("设备")
						|| Tv_Output_equipList.getText().toString()
								.equals("设备")
						|| Et_inputValue.getText().toString().equals("状态")
						|| Tv_outputCommandMeaning.getText().toString()
								.equals("状态")
						|| Et_timeLapse.getText().toString().equals("")
						|| Tv_Input_SignalList.getText().toString()
								.equals("信号")
						|| Tv_Output_CommandNameList.getText().toString()
								.equals("命令")
						|| Tv_Detection_equipList.getText().toString()
								.equals("设备")
						|| Tv_Detection_SignalList.getText().toString()
								.equals("信号")
						|| Et_Detection_result.getText().toString()
								.equals("状态")) {
					Toast.makeText(context, "请输入完整", 500).show();

				} else {

					ClickSure();
				}

			}
		});

		// 获取xml模板文件
		new Thread(new Runnable() {

			@Override
			public void run() {
				getFileList();
			}
		}).start();

	}

	public void ClickSure() {

		if (neepUpdateTime) {
			if (true) {
				ipc_control ipc = new ipc_control();

				AlarmValue_name = Et_inputValue.getText().toString();
				// AlarmValue_name_t = Et_inputValue_t.getText()
				// .toString();
				if (Str_SignalId.size() > 0)
					AlarmValue = Str_SignalId.get(AlarmValue_name);
				// if (Str_SignalId_t.size() > 0)
				// AlarmValue_t = Str_SignalId_t
				// .get(AlarmValue_name_t);

				CommandName = Tv_Output_CommandNameList.getText().toString();
				if (Str_CommandId.size() > 0) {
					CommandId = Str_CommandId.get(Tv_Output_CommandNameList
							.getText().toString());
					// System.out.println("CommandId:"+CommandId);

				}
				if (Str_ipc_Ctrl.size() > 0) {
					ParameterValue = Str_ipc_Ctrl.get(Tv_outputCommandMeaning
							.getText().toString());
				}
				ParameterId = "1";

				Meaning = Tv_outputCommandMeaning.getText().toString();
				if (Meaning.equals("闭合"))
					Meaning = "close";
				else
					Meaning = "open";
				TimeLapse = Et_timeLapse.getText().toString();

				DeteResult_name = Et_Detection_result.getText().toString();
				if (Str_SignalId_d.size() > 0)
					DeteResult = Str_SignalId_d.get(DeteResult_name);

				if (CommandId.equals("") || ParameterValue.equals("")) {
					Toast.makeText(getContext(), "配置出现错误，请重新配置", 500).show();
					return;
				}
				ipc.equipid = Integer.parseInt(output_SelectEquaipId);
				ipc.ctrlid = Integer.parseInt(CommandId);
				ipc.valuetype = 1;
				ipc.value = ParameterValue;
				lstCtrl.clear();
				lstCtrl.add(ipc);
				saveData();
				OldValue = "";
				// OldValue_t = "";
				neepUpdate = true;
				sb_all.setLength(0);

				File f_ini = new File(event_path + "/" + label + ".log");
				if (f_ini.exists()) {
					f_ini.delete();
				}
				MGridActivity.AlarmShow.put(label, AlarmShow);
				MGridActivity.AlarmAll.put(label, SgAlarmAction.this);

			} else {

				Toast.makeText(getContext(), "告警请输入0或者1", 500).show();

			}
		} else {
			Toast.makeText(getContext(), "当前有联动，请稍后修改", 1000).show();
		}
	}

	protected void delectFile() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				neepUpdate = false;
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {

					e.printStackTrace();
				}
				File f = new File(event_data + "/" + label + ".data");
				if (f.exists() && f.isFile()) {
					f.delete();
					input_SelectEquaipId = "";
					input_SelectSignalId = "";
					// input_SelectEquaipId_t = "";
					// input_SelectSignalId_t = "";

					output_SelectEquaipId = "";
					AlarmValue = "";
					AlarmValue_name = "";
					// AlarmValue_t = "";
					// AlarmValue_name_t = "";
					CommandName = "";
					CommandId = "";
					ParameterId = "";
					ParameterValue = "";
					Meaning = "";
					TimeLapse = "";
					detection_SelectEquipId = "";
					detection_SelectSignalId = "";
					DeteResult = "";
					DeteResult_name = "";
					lstCtrl.clear();
					equaipList = null;
					signalList = null;
					signalList_t = null;
					signalList_De = null;
					CommandNameList = null;
					CommandMeaning = null;
					SignalMeaning = null;
					// SignalMeaning_t = null;
					SignalMeaning_D = null;

					MGridActivity.AlarmShow.remove(label);
					handler.sendEmptyMessage(1);
					// neepUpdate=true;
					// Toast.makeText(getContext(), "删除成功", 500).show();

				} else {
					// Toast.makeText(getContext(), "删除失败", 500).show();
				}

			}
		}).start();

	}

	
	//为了防止连续点击
	Runnable runable=new Runnable() {
	
		@Override
		public void run() {
			
			Btn_ensure.setEnabled(true);
			Btn_delect.setEnabled(true);
		}
	};
	
	
	// 将命令保存 方便重启也能使用
	protected void saveData() {

		File d = new File(event_data);
		File f = new File(event_data + "/" + label + ".data");
		File m = new File(event_path);

		// File f = new File(Environment
		// .getExternalStorageDirectory().getPath()+"/" + label + ".data");

		try {
			if (!d.exists()) {
				d.mkdir();

			}
			if (!m.exists()) {
				m.mkdir();
			}
			if (!f.exists()) {

				f.createNewFile();
			}
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(f), "GB2312"));
			String allS = "[Command]&&&IEquipId=" + input_SelectEquaipId
					+ "&&&SignalId=" + input_SelectSignalId + "&&&AlarmValue="
					+ AlarmValue + "&&&AlarmValueName=" + AlarmValue_name
					+ "&&&OEquipId=" + output_SelectEquaipId
					+ "&&&CommandName=" + CommandName + "&&&CommandId="
					+ CommandId + "&&&ParameterId=" + ParameterId
					+ "&&&ParameterValue=" + ParameterValue + "&&&Meaning="
					+ Meaning + "&&&TimeLapse=" + TimeLapse
					+ "&&&DetectionEID=" + detection_SelectEquipId
					+ "&&&DetectionSID=" + detection_SelectSignalId
					+ "&&&DeteResult=" + DeteResult + "&&&DeteResultName="
					+ DeteResult_name;
			String[] buffer = allS.split("&&&");
			for (int i = 0; i < buffer.length; i++) {

				bw.write(buffer[i]);
				bw.newLine();
			}
			bw.flush();
			bw.close();
			Toast.makeText(getContext(), "保存成功", 500).show();
		} catch (Exception e) {
			// System.out.println("我出错了");

			e.printStackTrace();
			Toast.makeText(getContext(), "保存失败", 500).show();
		}

	}

	protected void showCommandMeaning(Context context) {

		if (neepUpdate) {
			Toast.makeText(getContext(), "请删除后再修改", 1000).show();
			return;
		}
		if (CommandMeaning != null) {
			if (CommandMeaning.length == 0) {
				Toast.makeText(getContext(), "请稍后", 1000).show();
				return;
			}
		}
		AlertDialog.Builder builder = new Builder(context);
		builder.setTitle("状态");
		builder.setSingleChoiceItems(CommandMeaning, 0,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

						String s = CommandMeaning[which];
						Tv_outputCommandMeaning.setText(s);

					}
				});
		builder.show();

	}

	protected void showSignalMeaning(Context context) {

		if (neepUpdate) {
			Toast.makeText(getContext(), "请删除后再修改", 1000).show();
			return;
		}
		if (SignalMeaning != null) {
			if (SignalMeaning.length == 0) {
				Toast.makeText(getContext(), "请稍后", 1000).show();
				return;
			}
		}
		AlertDialog.Builder builder = new Builder(context);
		builder.setTitle("状态");
		builder.setSingleChoiceItems(SignalMeaning, 0,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

						String s = SignalMeaning[which];
						Et_inputValue.setText(s);

					}
				});
		builder.show();

	}

	protected void showSignalMeaning_D(Context context) {

		if (neepUpdate) {
			Toast.makeText(getContext(), "请删除后再修改", 1000).show();
			return;
		}
		if (SignalMeaning_D != null) {
			if (SignalMeaning_D.length == 0) {
				Toast.makeText(getContext(), "请稍后", 1000).show();
				return;
			}
		}
		AlertDialog.Builder builder = new Builder(context);
		builder.setTitle("状态");
		builder.setSingleChoiceItems(SignalMeaning_D, 0,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

						String s = SignalMeaning_D[which];
						Et_Detection_result.setText(s);

					}
				});
		builder.show();

	}

	// protected void showSignalMeaning_t(Context context) {
	//
	// if (neepUpdate) {
	// Toast.makeText(getContext(), "请删除后再修改", 1000).show();
	// return;
	// }
	// if (SignalMeaning_t != null) {
	// if (SignalMeaning_t.length == 0) {
	// Toast.makeText(getContext(), "请稍后", 1000).show();
	// return;
	// }
	// }
	// AlertDialog.Builder builder = new Builder(context);
	// builder.setTitle("状态");
	//
	// builder.setSingleChoiceItems(SignalMeaning_t, 0,
	// new DialogInterface.OnClickListener() {
	//
	// @Override
	// public void onClick(DialogInterface dialog, int which) {
	//
	// String s = SignalMeaning_t[which];
	// // Et_inputValue_t.setText(s);
	//
	// }
	// });
	// builder.show();
	//
	// }

	// 得到EquipTemplateId值 好知道对应的模板文件
	protected void getEquipTemplateId() {
		final File f = new File(Vtu_path);
		if (f.exists()) {
			new Thread(new Runnable() {

				@Override
				public void run() {
					try {
						doc = db.parse(f);
						NodeList list = doc
								.getElementsByTagName("CfgEquipment");
						if (list == null)
							return;
						for (int i = 0; i < list.getLength(); i++) {
							Element element1 = (Element) list.item(i);
							String EquipId = element1.getAttribute("EquipId");
							if (EquipId.equals(output_SelectEquaipId)) {
								EquipTemplateId = element1
										.getAttribute("EquipTemplateId");
								// System.out.println(EquipTemplateId);
								break;
							}
						}

						for (String s : xmlFileListName) {
							File f = new File(path + "/" + s);
							if (f.exists()) {
								doc = db.parse(f);
								NodeList ET_list = doc
										.getElementsByTagName("EquipTemplate");
								Element element1 = (Element) ET_list.item(0);
								String ET_Id = element1
										.getAttribute("EquipTemplateId");
								if (!ET_Id.equals(EquipTemplateId)) {
									continue;
								}

								// System.out.println(s);
								NodeList EC_list = doc
										.getElementsByTagName("EquipCommand");
								if (EC_list == null)
									return;

								CommandNameList = new String[EC_list
										.getLength()];
								ipc_control ipcC = new ipc_control();
								for (int i = 0; i < EC_list.getLength(); i++) {
									Element element_ec = (Element) EC_list
											.item(i);
									String id = element_ec
											.getAttribute("CommandId");

									CommandNameList[i] = element_ec
											.getAttribute("CommandName");
									// System.out.println(CommandNameList[i]+" 名字：ID "+id);
									Str_CommandId.put(CommandNameList[i], id);

								}
								break;
							}
						}

					} catch (Exception e) {

						e.printStackTrace();
					}
				}
			}).start();

		} else {
			Toast.makeText(getContext(), "读取文件出错", 500).show();
		}

	}

	protected void getFileList() {
		File file = new File(path);
		if (file.isDirectory()) {
			String[] filelist = file.list();
			for (int i = 0; i < filelist.length; i++) {
				if (filelist[i].endsWith(".xml")
						&& !filelist[i].equals("MonitorUnitVTU.xml")) {
					xmlFileListName.add(filelist[i]);
				}
			}

		} else {

		}

		try {
			dbf = DocumentBuilderFactory.newInstance();
			db = dbf.newDocumentBuilder();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// 显示设备列表单选框
	protected void showEquaip(Context context, final int lable) {
		if (neepUpdate) {
			Toast.makeText(getContext(), "请删除后再修改", 1000).show();
			return;
		}
		if (equaipList != null) {
			if (equaipList.length == 0) {
				Toast.makeText(getContext(), "请稍后", 1000).show();
				return;
			}
		}
		AlertDialog.Builder builder = new Builder(context);
		builder.setTitle("设备");
		builder.setSingleChoiceItems(equaipList, lable == 0 ? EI_selectId
				: EO_selectId, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

				String s = equaipList[which];
				if (lable == 0) {
					input_SelectEquaipId = equaipIdName.get(equaipList[which]);
					EI_selectId = which;
					// if (s.length() > 4) {
					// Tv_Input_equipList.setSingleLine(true);
					// Tv_Input_equipList.setGravity(Gravity.CENTER_VERTICAL);
					// Tv_Input_equipList.setEllipsize(TruncateAt.END);
					//
					// } else {
					// Tv_Input_equipList.setSingleLine(false);
					// Tv_Input_equipList.setGravity(Gravity.CENTER);
					// }
					Tv_Input_equipList.setText(s);
					Tv_Input_SignalList.setText(signalName);
					Et_inputValue.setText("状态");
				} else if (lable == 1) {
					output_SelectEquaipId = equaipIdName.get(equaipList[which]);
					getEquipTemplateId();
					EO_selectId = which;

					Tv_Output_equipList.setText(s);
					Tv_Output_CommandNameList.setText(commandName);
					Tv_outputCommandMeaning.setText(stateName);
				} else if (lable == 2) {
					detection_SelectEquipId = equaipIdName
							.get(equaipList[which]);

					Tv_Detection_equipList.setText(s);
					Tv_Detection_SignalList.setText(signalName);
					Et_Detection_result.setText("状态");
				} else if (lable == 3) {
					// input_SelectEquaipId_t = equaipIdName
					// .get(equaipList[which]);

					// Tv_Input_equipList_t.setText(s);
					// Tv_Input_SignalList_t.setText(signalName);
					// Et_inputValue_t.setText("状态");
				}

			}
		});
		builder.show();

	}

	// 显示信号列表单选框
	protected void showSignal(Context context, final int label) {
		 if (neepUpdate) {
	//	 Toast.makeText(getContext(), "请删除后再修改", 1000).show();
		 return;
		 }
		switch (label) {
		case 0:

			if (signalList != null) {
				if (signalList.length == 0) {
					Toast.makeText(getContext(), "请稍后", 1000).show();
					return;
				}
			}
			break;
		case 1:

			if (signalList_De != null) {
				if (signalList_De.length == 0) {
					Toast.makeText(getContext(), "请稍后", 1000).show();
					return;
				}
			}
			break;
		case 2:

			if (signalList_t != null) {
				if (signalList_t.length == 0) {
					Toast.makeText(getContext(), "请稍后", 1000).show();
					return;
				}
			}
			break;

		}

		AlertDialog.Builder builder = new Builder(context);
		builder.setTitle("信号");
		builder.setSingleChoiceItems(label == 0 ? signalList
				: label == 1 ? signalList_De : signalList_t,
				label == 0 ? SI_selectId : De_selectId,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

						if (label == 0) {
							String s = signalList[which];
							SI_selectId = which;

							Tv_Input_SignalList.setText(s);
							input_SelectSignalId = signalIdName.get(s);
							Et_inputValue.setText("状态");
							getSignalMeaning(0);

						} else if (label == 1) {
							String s = signalList_De[which];
							De_selectId = which;

							Tv_Detection_SignalList.setText(s);
							detection_SelectSignalId = signalIdName.get(s);
							Et_Detection_result.setText("状态");
							getSignalMeaning(2);

						} else if (label == 2) {
							String s = signalList_t[which];

							getSignalMeaning(1);
						}
					}
				});
		builder.show();
	}

	protected void showCommName(Context context) {
		if (neepUpdate) {
			Toast.makeText(getContext(), "请删除后再修改", 1000).show();
			return;
		}
		if (CommandNameList != null) {
			if (CommandNameList.length == 0) {
				Toast.makeText(getContext(), "请稍后", 1000).show();
				return;
			}
		}
		AlertDialog.Builder builder = new Builder(context);
		builder.setTitle("命令名");
		builder.setSingleChoiceItems(CommandNameList, 0,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

						Tv_Output_CommandNameList
								.setText(CommandNameList[which]);
						Tv_outputCommandMeaning.setText(stateName);
						getCommandMeaning(CommandNameList[which]);

					}
				});
		builder.show();
	}

	protected void getCommandMeaning(final String string) {

		CommandMeaning = new String[2];
		new Thread(new Runnable() {

			@Override
			public void run() {
				NodeList EC_list = doc.getElementsByTagName("EquipCommand");
				if (EC_list == null)
					return;
				ipc_control ipcC = new ipc_control();
				for (int i = 0; i < EC_list.getLength(); i++) {
					Element element_ec = (Element) EC_list.item(i);
					// System.out.println(element_ec.getAttribute("CommandName")
					// + ":" + string);
					if (!element_ec.getAttribute("CommandName").equals(string))
						continue;

					NodeList CommandParameter_no = element_ec.getChildNodes();
					for (int j = 0; j < CommandParameter_no.getLength(); j++) {
						NodeList CommandMeaning_mo = CommandParameter_no
								.item(j).getChildNodes();
						int m = 0;
						for (int n = 0; n < CommandMeaning_mo.getLength(); n++) {

							if (CommandMeaning_mo.item(n).getNodeName()
									.equals("CommandMeaning")) {
								Element element_CM = (Element) CommandMeaning_mo
										.item(n);
								String ParameterValue = element_CM
										.getAttribute("ParameterValue");
								String Meaning = element_CM
										.getAttribute("Meaning");

								CommandMeaning[m] = Meaning;
								Str_ipc_Ctrl.put(Meaning, ParameterValue);
								m++;
							}
						}
					}

				}

			}
		}).start();

	}

	protected void getSignalMeaning(int id) {

		switch (id) {
		case 0:
			SignalMeaning = new String[2];
			new Thread(new Runnable() {

				@Override
				public void run() {
					String TemplateId = xml.getTemplateId(input_SelectEquaipId);
					NodeList nodeList = xml.getNodeList("EquipSignal",
							TemplateId);
					for (int i = 0; i < nodeList.getLength(); i++) {
						Element element1 = (Element) nodeList.item(i);
						String sId = element1.getAttribute("SignalId");
						if (sId.equals(input_SelectSignalId)) {
							NodeList SignalMeaning_no = element1
									.getChildNodes();
							for (int j = 0; j < SignalMeaning_no.getLength(); j++) {
								NodeList SignalMeanList = SignalMeaning_no
										.item(j).getChildNodes();
								int m = 0;
								for (int n = 0; n < SignalMeanList.getLength(); n++) {

									if (SignalMeanList.item(n).getNodeName()
											.equals("SignalMeaning")) {
										Element element_CM = (Element) SignalMeanList
												.item(n);
										String SignalValue = element_CM
												.getAttribute("StateValue");
										String Meaning = element_CM
												.getAttribute("Meaning");

										if (!SignalValue.equals("")
												&& !Meaning.equals("")) {
											SignalMeaning[m] = Meaning;
											Str_SignalId.put(Meaning,
													SignalValue);
											m++;
										}

									}
								}
							}
						}
					}
				}
			}).start();

			break;

		case 1:
			// SignalMeaning_t = new String[2];
			// new Thread(new Runnable() {
			//
			// @Override
			// public void run() {
			// String TemplateId = xml
			// .getTemplateId(input_SelectEquaipId_t);
			// NodeList nodeList = xml.getNodeList("EquipSignal",
			// TemplateId);
			// for (int i = 0; i < nodeList.getLength(); i++) {
			// Element element1 = (Element) nodeList.item(i);
			// String sId = element1.getAttribute("SignalId");
			// if (sId.equals(input_SelectSignalId_t)) {
			// NodeList SignalMeaning_no = element1
			// .getChildNodes();
			// for (int j = 0; j < SignalMeaning_no.getLength(); j++) {
			// NodeList SignalMeanList = SignalMeaning_no
			// .item(j).getChildNodes();
			// int m = 0;
			// for (int n = 0; n < SignalMeanList.getLength(); n++) {
			//
			// if (SignalMeanList.item(n).getNodeName()
			// .equals("SignalMeaning")) {
			// Element element_CM = (Element) SignalMeanList
			// .item(n);
			// String SignalValue = element_CM
			// .getAttribute("StateValue");
			// String Meaning = element_CM
			// .getAttribute("Meaning");
			// if (!SignalValue.equals("")
			// && !Meaning.equals("")) {
			// SignalMeaning_t[m] = Meaning;
			// Str_SignalId_t.put(Meaning,
			// SignalValue);
			// m++;
			// }
			//
			// }
			// }
			// }
			// }
			// }
			// }
			// }).start();

			break;
		case 2:
			SignalMeaning_D = new String[2];
			new Thread(new Runnable() {

				@Override
				public void run() {
					String TemplateId = xml
							.getTemplateId(detection_SelectEquipId);
					NodeList nodeList = xml.getNodeList("EquipSignal",
							TemplateId);
					for (int i = 0; i < nodeList.getLength(); i++) {
						Element element1 = (Element) nodeList.item(i);
						String sId = element1.getAttribute("SignalId");
						if (sId.equals(detection_SelectSignalId)) {
							NodeList SignalMeaning_no = element1
									.getChildNodes();
							for (int j = 0; j < SignalMeaning_no.getLength(); j++) {
								NodeList SignalMeanList = SignalMeaning_no
										.item(j).getChildNodes();
								int m = 0;
								for (int n = 0; n < SignalMeanList.getLength(); n++) {

									if (SignalMeanList.item(n).getNodeName()
											.equals("SignalMeaning")) {
										Element element_CM = (Element) SignalMeanList
												.item(n);
										String SignalValue = element_CM
												.getAttribute("StateValue");
										String Meaning = element_CM
												.getAttribute("Meaning");
										if (!SignalValue.equals("")
												&& !Meaning.equals("")) {
											SignalMeaning_D[m] = Meaning;
											Str_SignalId_d.put(Meaning,
													SignalValue);
											m++;
										}

									}
								}
							}
						}
					}
				}
			}).start();
			break;

		}

	}

	// 得到所有设备对应的信号列表
	protected void get_SignalList(int id) {

		if (neepUpdate) {
			Toast.makeText(getContext(), "请删除后再修改", 1000).show();
			return;
		}
		
		if (id == 0) {
			if (input_SelectEquaipId.equals(""))
				return;
			Hashtable<String, Signal> ht_singal = DataGetter
					.getEquipSignalList(input_SelectEquaipId);
			signalList = new String[ht_singal.size()];
			Iterator<Map.Entry<String, Signal>> it = ht_singal.entrySet()
					.iterator();
			int i = 0;
			while (it.hasNext()) {
				Map.Entry<String, Signal> entry = it.next();
				input_SelectSignalId = entry.getKey();
				String signalName = DataGetter.getSignalName(
						input_SelectEquaipId, input_SelectSignalId);
				signalIdName.put(signalName, input_SelectSignalId);
				signalList[i] = signalName;
				i++;
			}
		} else if (id == 1) {

		}

	}

	protected void getDeSignal() {
		
		if (neepUpdate) {
			Toast.makeText(getContext(), "请删除后再修改", 1000).show();
			return;
		}

		if (detection_SelectEquipId.equals(""))
			return;
		Hashtable<String, Signal> ht_singal = DataGetter
				.getEquipSignalList(detection_SelectEquipId);
		signalList_De = new String[ht_singal.size()];
		Iterator<Map.Entry<String, Signal>> it = ht_singal.entrySet()
				.iterator();
		int i = 0;
		while (it.hasNext()) {
			Map.Entry<String, Signal> entry = it.next();
			detection_SelectSignalId = entry.getKey();
			String signalName = DataGetter.getSignalName(
					detection_SelectEquipId, detection_SelectSignalId);
			signalIdName.put(signalName, detection_SelectSignalId);
			signalList_De[i] = signalName;
			i++;
		}

	}

	// 得到所有设备列表
	protected void get_equiptList() {
		
		if (neepUpdate) {
			Toast.makeText(getContext(), "请删除后再修改", 1000).show();
			return;
		}
		HashSet<String> ht_equiptID = DataGetter.getEquipmentIdList();
		Iterator<String> it = ht_equiptID.iterator();
		equaipList = new String[ht_equiptID.size()];
	//	System.out.println("大小："+ht_equiptID.size());
		
		int i = 0;
		while (it.hasNext()) {
			String equipId = it.next();
			String equipName = DataGetter.getEquipmentName(equipId);
		//	System.out.println("名字："+equipName);
		//	System.out.println("ID："+equipId);
			equaipIdName.put(equipName, equipId);
			equaipList[i] = equipName;
			i++;
		}

	}

	@Override
	public void doLayout(boolean bool, int l, int t, int r, int b) {
		if (m_rRenderWindow == null)
			return;
		int nX = l
				+ (int) (((float) m_nPosX / (float) MainWindow.FORM_WIDTH) * (r - l));
		int nY = t
				+ (int) (((float) m_nPosY / (float) MainWindow.FORM_HEIGHT) * (b - t));
		int nWidth = (int) (((float) (m_nWidth) / (float) MainWindow.FORM_WIDTH) * (r - l));
		int nHeight = (int) (((float) (m_nHeight) / (float) MainWindow.FORM_HEIGHT) * (b - t));
		X = nX;
		Y = nY;
		W = nWidth;
		H = nHeight;
		m_rBBox.left = nX;
		m_rBBox.top = nY;
		m_rBBox.right = nX + nWidth;
		m_rBBox.bottom = nY + nHeight;

		if (m_rRenderWindow.isLayoutVisible(m_rBBox)) {

			Tv_Input_equipList.layout((int) (nX + 0.025 * nWidth),
					(int) (nY + 0.11 * nHeight), nX + (int) (nWidth * 0.355f),
					(int) (nY + 0.19 * nHeight));
			Tv_Input_SignalList.layout((int) (nX + 0.375 * nWidth),
					(int) (nY + 0.11 * nHeight), nX + (int) (nWidth * 0.705f),
					(int) (nY + 0.19 * nHeight));
			Et_inputValue.layout(nX + (int) (nWidth * 0.73f),
					(int) (nY + 0.11 * nHeight), nX + (int) (nWidth * 1f),
					(int) (nY + 0.19 * nHeight));

			// Tv_Input_equipList_t.layout((int) (nX + 0.025 * nWidth),
			// (int) (nY + 0.21 * nHeight), nX + (int) (nWidth * 0.355f),
			// (int) (nY + 0.29 * nHeight));
			// Tv_Input_SignalList_t.layout((int) (nX + 0.375 * nWidth),
			// (int) (nY + 0.21 * nHeight), nX + (int) (nWidth * 0.705f),
			// (int) (nY + 0.29 * nHeight));
			// Et_inputValue_t.layout(nX + (int) (nWidth * 0.73f),
			// (int) (nY + 0.21 * nHeight), nX + (int) (nWidth * 1f),
			// (int) (nY + 0.29 * nHeight));

			Tv_Output_equipList.layout((int) (nX + 0.025 * nWidth),
					(int) (nY + 0.46 * nHeight), nX + (int) (nWidth * 0.355f),
					(int) (nY + 0.54 * nHeight));
			Tv_Output_CommandNameList.layout((int) (nX + 0.375 * nWidth),
					(int) (nY + 0.46 * nHeight), nX + (int) (nWidth * 0.705f),
					(int) (nY + 0.54 * nHeight));
			Tv_outputCommandMeaning.layout(nX + (int) (nWidth * 0.73f),
					(int) (nY + 0.46 * nHeight), nX + (int) (nWidth * 1f),
					(int) (nY + 0.54 * nHeight));
			Et_timeLapse.layout((int) (nX + 0.025 * nWidth),
					(int) (nY + 0.56 * nHeight), nX + (int) (nWidth * 0.355f),
					(int) (nY + 0.64 * nHeight));

			Tv_Detection_equipList.layout((int) (nX + 0.025 * nWidth),
					(int) (nY + 0.81 * nHeight), nX + (int) (nWidth * 0.355f),
					(int) (nY + 0.89 * nHeight));
			Tv_Detection_SignalList.layout((int) (nX + 0.375 * nWidth),
					(int) (nY + 0.81 * nHeight), nX + (int) (nWidth * 0.705f),
					(int) (nY + 0.89 * nHeight));
			Et_Detection_result.layout(nX + (int) (nWidth * 0.73f),
					(int) (nY + 0.81 * nHeight), nX + (int) (nWidth * 1f),
					(int) (nY + 0.89 * nHeight));

			Btn_ensure.layout(nX + (int) (nWidth * 0.1f),
					(int) (nY + 0.95 * nHeight), nX + (int) (nWidth * 0.4f),
					(int) (nY + 1.05 * nHeight));
			Btn_delect.layout(nX + (int) (nWidth * 0.6f),
					(int) (nY + 0.95 * nHeight), nX + (int) (nWidth * 0.9f),
					(int) (nY + 1.05 * nHeight));

			this.layout(nX, nY, nX + nWidth, nY + nHeight);

		}

	}

	protected void onDraw(Canvas canvas) {

		// System.out.println("1234567890");
		Paint p = new Paint();
		// p.setTextSize(1);
		p.setColor(Color.BLACK);
		p.setAntiAlias(true);
		p.setStyle(Paint.Style.STROKE);
		p.setStrokeWidth(1);

		canvas.drawRect(0, 0, W, (int) (0 + 0.3 * H), p);
		canvas.drawRect(0, (int) (0 + 0.1 * H), W, (int) (0 + 0.3 * H), p);
		canvas.drawRect(0, (int) (0 + 0.35 * H), W, (int) (0 + 0.65 * H), p);
		canvas.drawRect(0, (int) (0 + 0.45 * H), W, (int) (0 + 0.65 * H), p);
		canvas.drawRect(0, (int) (0 + 0.7 * H), W, (int) (0 + 0.9 * H), p);
		canvas.drawRect(0, (int) (0 + 0.8 * H), W, (int) (0 + 0.9 * H), p);

		p.setTextSize(20);
		p.setStyle(Paint.Style.FILL);
		canvas.drawText("输入信号", (int) (W * 0.33), (int) (0.066 * H), p);
		canvas.drawText("输出控制", (int) (W * 0.33), (int) (0.416 * H), p);
		canvas.drawText("检测信号", (int) (W * 0.33), (int) (0.766 * H), p);
		canvas.drawText("分钟", (int) (W * 0.375), (int) (0.615 * H), p);
		super.onDraw(canvas);
	}

	@Override
	public void setUniqueID(String strID) {
		m_strID = strID;

	}

	@Override
	public String getUniqueID() {

		return m_strID;
	}

	@Override
	public void setType(String strType) {
		m_strType = strType;
	}

	@Override
	public String getType() {

		return m_strType;
	}

	@Override
	public void parseProperties(String strName, String strValue,
			String strResFolder) throws Exception {

		if ("ZIndex".equals(strName)) {
			m_nZIndex = Integer.parseInt(strValue);
			if (MainWindow.MAXZINDEX < m_nZIndex)
				MainWindow.MAXZINDEX = m_nZIndex;
		} else if ("Location".equals(strName)) {
			String[] arrStr = strValue.split(",");
			m_nPosX = Integer.parseInt(arrStr[0]);
			m_nPosY = Integer.parseInt(arrStr[1]);
		} else if ("Size".equals(strName)) {
			String[] arrSize = strValue.split(",");
			m_nWidth = Integer.parseInt(arrSize[0]);
			m_nHeight = Integer.parseInt(arrSize[1]);
		} else if ("Alpha".equals(strName)) {
			m_fAlpha = Float.parseFloat(strValue);
		} else if ("Content".equals(strName)) {
			m_strContent = strValue;
			// this.setText(m_strContent);
		} else if ("FontFamily".equals(strName))
			m_strFontFamily = strValue;
		else if ("IsBold".equals(strName))
			m_bIsBold = Boolean.parseBoolean(strValue);
		else if ("BackgroundColor".equals(strName))
			m_cBackgroundColor = Color.parseColor(strValue);
		else if ("FontColor".equals(strName)) {
			m_cFontColor = Color.parseColor(strValue);
		} else if ("CmdExpression".equals(strName)) {
			m_strCmdExpression = strValue;
		} else if ("IsValueRelateSignal".equals(strName)) {
			if ("True".equals(strValue))
				m_bIsValueRelateSignal = true;
			else
				m_bIsValueRelateSignal = false;
		} else if ("ButtonWidthRate".equals(strName)) {
			m_fButtonWidthRate = Float.parseFloat(strValue);
		} else if ("Labelorder".equals(strName)) {
			label = strValue;

			// 解析label.data文件
			new Thread(new Runnable() {

				@Override
				public void run() {
					UtIniReader iniReader = null;
					File f_data = new File(event_data + "/" + label + ".data");
					File f_ini = new File(event_path + "/" + label + ".log");
					// File f_num=new File(event_path + "/" + label + ".txt");
					try {
						if (!f_data.exists())
							return;
						MGridActivity.AlarmShow.put(label, AlarmShow);
						MGridActivity.AlarmAll.put(label, SgAlarmAction.this);
						// System.out.println(label +"哥哥1");
						iniReader = new UtIniReader(f_data.getAbsolutePath());
						input_SelectEquaipId = iniReader.getValue("Command",
								"IEquipId");
						input_SelectSignalId = iniReader.getValue("Command",
								"SignalId");
						AlarmValue = iniReader
								.getValue("Command", "AlarmValue");
						AlarmValue_name = iniReader.getValue("Command",
								"AlarmValueName");
						// input_SelectEquaipId_t =
						// iniReader.getValue("Command",
						// "IEquipId_t");
						// input_SelectSignalId_t =
						// iniReader.getValue("Command",
						// "SignalId_t");
						// AlarmValue_t = iniReader.getValue("Command",
						// "AlarmValue_t");
						// AlarmValue_name_t = iniReader.getValue("Command",
						// "AlarmValueName_t");
						output_SelectEquaipId = iniReader.getValue("Command",
								"OEquipId");
						CommandName = iniReader.getValue("Command",
								"CommandName");
						CommandId = iniReader.getValue("Command", "CommandId");
						ParameterId = iniReader.getValue("Command",
								"ParameterId");
						ParameterValue = iniReader.getValue("Command",
								"ParameterValue");
						if (ParameterValue.equals("null")) {
							handler.sendEmptyMessage(6);
							return;
						}
						// System.out.println(label +"哥哥2");
						Meaning = iniReader.getValue("Command", "Meaning");
						if (Meaning.equals("close"))
							Meaning = "闭合";
						else
							Meaning = "断开";
						TimeLapse = iniReader.getValue("Command", "TimeLapse");
						detection_SelectEquipId = iniReader.getValue("Command",
								"DetectionEID");
						detection_SelectSignalId = iniReader.getValue(
								"Command", "DetectionSID");
						DeteResult = iniReader
								.getValue("Command", "DeteResult");
						DeteResult_name = iniReader.getValue("Command",
								"DeteResultName");
						// neepUpdate = true;
						getEquipTemplateId();
						ipc_control ipc = new ipc_control();
						ipc.equipid = Integer.parseInt(output_SelectEquaipId);
						ipc.ctrlid = Integer.parseInt(CommandId);
						ipc.valuetype = 1;
						ipc.value = ParameterValue;
						lstCtrl.clear();
						lstCtrl.add(ipc);
						int m = 0;

						while (DataGetter
								.getEquipmentName(input_SelectEquaipId).equals(
										"")
								|| DataGetter.getSignalName(
										input_SelectEquaipId,
										input_SelectSignalId).equals("")
								|| DataGetter.getEquipmentName(
										output_SelectEquaipId).equals("")
								|| DataGetter.getEquipmentName(
										detection_SelectEquipId).equals("")
								|| DataGetter.getSignalName(
										detection_SelectEquipId,
										detection_SelectSignalId).equals("")
								|| DataGetter.getSignalValue(
										input_SelectEquaipId,
										input_SelectSignalId).equals("")) {

							Thread.sleep(500);
						}

						handler.sendEmptyMessage(0);

						if (f_ini.exists()) {

							@SuppressWarnings("resource")
							BufferedReader br = new BufferedReader(
									new FileReader(f_ini));
							List<String> list = new ArrayList<String>();
							String s = null;
							int line = 0;
							while ((s = br.readLine()) != null) {
								line++;
								list.add(s);

							}

							if (line == 3) {
								f_ini.delete();
								AlarmShow.clear();
								neepUpdate = true;
							} else if (line == 2) {

								long lTime = getStartTime(list);
								setXiaDianTime(list);
								long nTime = System.currentTimeMillis();
								if (nTime / 1000 - lTime <= 1800) {
									if (true) {
										String S_value = "";
										Equipment Obj = DataGetter.equipment.htEquipmentData
												.get(input_SelectEquaipId);
										S_value = DataGetter.proc_rtsignal(Obj,
												input_SelectSignalId);
										S_value = (int) Float
												.parseFloat(S_value) + "";
										OldValue = S_value;

										if (nTime / 1000 - lTime <= Integer
												.parseInt(TimeLapse) * 60) {

											waitJumpTwo(lTime);

										} else {

											try {
												Thread.sleep(10000);
											} catch (InterruptedException e) {

												e.printStackTrace();
											}

											judgeResult();

										}
										neepUpdate = true;

									} else {
										f_ini.delete();
										AlarmShow.clear();
										neepUpdate = true;

									}
								} else {
									f_ini.delete();
									AlarmShow.clear();
									neepUpdate = true;

								}

							} else if (line == 1) {

								long lTime = getStartTime(list);
								long nTime = System.currentTimeMillis();
								if (nTime / 1000 - lTime <= 1800) {
									if (testValue()) {
										if (nTime / 1000 - lTime <= Integer
												.parseInt(TimeLapse) * 60) {

											waitJump(lTime);

										} else {

											int i = 0;
											while (!isDetectionWin) {
												if (i > 2)
													isDetectionLose = true;
												sendCmdAndUpdateUI();
												if (isDetectionLose)
													break;
												i++;
												try {
													Thread.sleep(10000);
												} catch (InterruptedException e) {

													e.printStackTrace();
												}
											}
											judgeResult();
										}
										neepUpdate = true;

									} else {
										f_ini.delete();
										AlarmShow.clear();
										neepUpdate = true;
									}
								} else {
									f_ini.delete();
									AlarmShow.clear();
									neepUpdate = true;

								}
							} else if (line == 0) {
								f_ini.delete();
								AlarmShow.clear();
								neepUpdate = true;
							} else {
								f_ini.delete();
								AlarmShow.clear();
								neepUpdate = true;
							}

						} else {

							neepUpdate = true;
						}

					} catch (Exception e) {

						iniReader = null;
						e.printStackTrace();
						System.out.println("出现了异常");
					}

				}
			}).start();

			new Thread(new Runnable() {

				@Override
				public void run() {
					while (true) {
						updateValue();
						try {
							Thread.sleep(500);
						} catch (InterruptedException e) {

							e.printStackTrace();
						}

					}

				}
			}).start();

		} else if ("Expression".equals(strName)) {

			m_strCmdExpression = strValue;
		}

	}

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				// System.out.println("label:" + label);
				Et_inputValue.setText(AlarmValue_name);
				// Et_inputValue_t.setText(AlarmValue_name_t);
				// Et_inputValue.setHint(AlarmValue);
				// Et_inputValue_t.setHint(AlarmValue_t);
				Tv_Output_CommandNameList.setText(CommandName);
				Tv_outputCommandMeaning.setText(Meaning);

				Et_timeLapse.setText("");
				Et_Detection_result.setText(DeteResult_name);
				Tv_Input_equipList.setText(DataGetter
						.getEquipmentName(input_SelectEquaipId));
				Tv_Input_SignalList.setText(DataGetter.getSignalName(
						input_SelectEquaipId, input_SelectSignalId));
				// Tv_Input_equipList_t.setText(DataGetter
				// .getEquipmentName(input_SelectEquaipId_t));
				// Tv_Input_SignalList_t.setText(DataGetter.getSignalName(
				// input_SelectEquaipId_t, input_SelectSignalId_t));
				Tv_Output_equipList.setText(DataGetter
						.getEquipmentName(output_SelectEquaipId));
				Tv_Detection_equipList.setText(DataGetter
						.getEquipmentName(detection_SelectEquipId));
				Tv_Detection_SignalList.setText(DataGetter.getSignalName(
						detection_SelectEquipId, detection_SelectSignalId));

				break;

			case 1:
				Tv_Input_equipList.setText(equipName);
				Tv_Input_SignalList.setText(signalName);
				// Tv_Input_equipList_t.setText(equipName);
				// Tv_Input_SignalList_t.setText(signalName);
				Tv_Output_equipList.setText(equipName);
				Tv_Output_CommandNameList.setText(commandName);
				Tv_outputCommandMeaning.setText(stateName);
				// Et_inputValue.setHint("告警");
				// Et_inputValue_t.setHint("告警");
				// Et_outputValue.setHint("命令");
				Et_timeLapse.setText("");
				// Et_Detection_result.setHint("检测");
				Tv_Detection_equipList.setText(equipName);
				Tv_Detection_SignalList.setText(signalName);
				Et_Detection_result.setText("状态");
				Et_inputValue.setText("状态");
				// Et_inputValue_t.setText("状态");

				break;
			case 2:
				Toast.makeText(getContext(), (String) msg.obj,
						Toast.LENGTH_SHORT).show();

				break;
			case 3:
				Toast.makeText(getContext(), (String) msg.obj,
						Toast.LENGTH_SHORT).show();
				break;
			case 4:
				AlertDialog.Builder builder = new AlertDialog.Builder(
						getContext());
				builder.setTitle("提示");
				builder.setMessage(Tv_Detection_SignalList.getText().toString()
						+ "控制成功");
				builder.setPositiveButton("确认",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface arg0, int arg1) {

								// delDialogCount("win");
							}

						});
				builder.create();
				builder.setCancelable(false);
				builder.show();
				break;
			case 5:

				AlertDialog.Builder builder1 = new AlertDialog.Builder(
						getContext());
				builder1.setTitle("提示");
				builder1.setMessage(Tv_Detection_SignalList.getText()
						.toString() + "控制失败");
				builder1.setPositiveButton("确认",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface arg0, int arg1) {

								// delDialogCount("lose");
							}

						});
				builder1.create();
				builder1.setCancelable(false);
				builder1.show();

				break;

			case 6:

				Toast.makeText(getContext(), "配置出现了问题，请重新配置",
						Toast.LENGTH_SHORT).show();

				break;
			case 7:

				Toast.makeText(getContext(), "重置Do发生异常", Toast.LENGTH_SHORT)
						.show();

				break;
			case 8:

				Et_timeLapse.setText(TimeLapse);

				break;

			}

			super.handleMessage(msg);
		}
	};

	@Override
	public void initFinished() {
		setGravity(Gravity.CENTER);

		double padSize = CFGTLS.getPadHeight(m_nHeight, MainWindow.FORM_HEIGHT,
				getTextSize()) / 2;
		setPadding(0, (int) padSize, 0, (int) padSize);

	}

	private void waitJump(long time) {

		while (testValue()) {
			long newTime = System.currentTimeMillis();
			if (newTime / 1000 - time >= Integer.parseInt(TimeLapse) * 60) {
				int i = 0;
				while (!isDetectionWin) {
					if (i > 2)
						isDetectionLose = true;
					sendCmdAndUpdateUI();
					if (isDetectionLose)
						break;
					i++;
					try {
						Thread.sleep(10000);
					} catch (InterruptedException e) {

						e.printStackTrace();
					}
				}
				judgeResult();
				break;
			}

		}
	}

	private void waitJumpTwo(long time) {

		while (testValue()) {
			long newTime = System.currentTimeMillis();
			if (newTime / 1000 - time >= Integer.parseInt(TimeLapse) * 60) {
				try {
					Thread.sleep(10000);
				} catch (InterruptedException e) {

					e.printStackTrace();
				}
				judgeResult();
				break;
			}
		}
	}

	private boolean testValue() {
		String S_value = "";

		Equipment Obj = DataGetter.equipment.htEquipmentData
				.get(input_SelectEquaipId);
		S_value = DataGetter.proc_rtsignal(Obj, input_SelectSignalId);

		S_value = (int) Float.parseFloat(S_value) + "";
		OldValue = S_value;
		if (S_value.equals(AlarmValue))
			return true;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date dt = new Date(System.currentTimeMillis());
		String sDate = sdf.format(dt);
		sb_all.append(",无," + sDate + "&市电恢复正常&" + (isYiChang ? "异常" : "正常"));
		writeAllEvent();
		return false;
	}

	protected long getStartTime(List<String> list) {
		isYiChang = true;
		String startTime = list.get(0).split("&")[0];
		sb_all.append(label
				+ ","
				+ startTime
				+ "&检测到UPS市电异常 等待"
				+ TimeLapse
				+ "分钟后控制下电 期间发生了异常&"
				+ DataGetter.getSignalName(detection_SelectEquipId,
						detection_SelectSignalId) + "&市电异常");
		AlarmShow.add("&检测到UPS市电异常 等待" + TimeLapse + "分钟后控制下电");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date dt2 = null;
		try {
			dt2 = sdf.parse(startTime);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		long lTime = dt2.getTime() / 1000;
		return lTime;
	}

	protected void setXiaDianTime(List<String> list) {
		String XiaDianTime = list.get(1).split(",")[1].split("&")[0];
		sb_all.append("," + XiaDianTime + "&控制下电中");
		AlarmShow.add("控制下电中");
	}

	private boolean judgeResult() {

		if (detection_SelectEquipId == "" || detection_SelectSignalId == ""
				|| DeteResult == "")
			return false;

		Equipment Obj = DataGetter.equipment.htEquipmentData
				.get(detection_SelectEquipId);

		boolean isLose = false;

		int i = 0;
		while (true) {

			String string = DataGetter.proc_rtsignal(Obj,
					detection_SelectSignalId);
			if (Float.parseFloat(string) == Float.parseFloat(DeteResult)) {
				isLose = true;
				break;
			}
			i++;
			if (i >= 2)
				break;
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		if (event_bw == null) {
			eventFiles = new File(event_path + "/" + label + ".log");
			try {
				event_bw = new BufferedWriter(new OutputStreamWriter(
						new FileOutputStream(eventFiles, true), "GB2312"));

			} catch (Exception e1) {

				e1.printStackTrace();
			}
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date dt = new Date(System.currentTimeMillis());
		String sDate = sdf.format(dt);
		if (isLose) {

			String lose_d = DataGetter.getSignalValue(detection_SelectEquipId,
					"10001");
			lose_d = (int) Float.parseFloat(lose_d) + "";
			if (lose_d.equals("0")) {
				handler.sendEmptyMessage(5);
				DoReset();
				if (event_bw != null) {

					try {
						event_bw.write("false," + sDate + "&控制结果失败");
						sb_all.append("," + sDate + "&控制结果失败&"
								+ (isYiChang ? "异常" : "正常"));
						event_bw.newLine();
						event_bw.flush();
						event_bw.close();
						event_bw = null;
						AlarmShow.add("控制结果失败");

					} catch (Exception e) {

						e.printStackTrace();
					}

					writeAllEvent();

				}
			} else {
				handler.sendEmptyMessage(4);
				DoReset();
				if (event_bw != null) {
					try {

						event_bw.write("true," + sDate + "&控制结果成功");
						sb_all.append("," + sDate + "&控制结果成功&"
								+ (isYiChang ? "异常" : "正常"));
						event_bw.newLine();
						event_bw.flush();
						event_bw.close();
						System.out.println("写成功");
						event_bw = null;
						AlarmShow.add("控制结果成功");
					} catch (Exception e) {

						e.printStackTrace();
						System.out.println("写异常");
					}

					writeAllEvent();

				}
			}

		} else {
			handler.sendEmptyMessage(5);
			DoReset();
			if (event_bw != null) {

				try {
					event_bw.write("false," + sDate + "&控制结果失败");
					sb_all.append("," + sDate + "&控制结果失败&"
							+ (isYiChang ? "异常" : "正常"));
					event_bw.newLine();
					event_bw.flush();
					event_bw.close();
					event_bw = null;
					AlarmShow.add("控制结果失败");
				} catch (Exception e) {

					e.printStackTrace();
				}
				writeAllEvent();

			}
		}

		isDetectionLose = false;
		isChengli = true;
		isHuiFu = false;
		return true;

	}

	private void saveDialogCount(String s) {
		int i = 0;
		File f_d = new File(event_path + "/" + label + s);
		if (!f_d.exists())
			f_d.mkdirs();

		while (true) {
			File f_f = new File(event_path + "/" + label + s + "/" + i + ".h");
			if (!f_f.exists()) {
				try {
					f_f.createNewFile();
				} catch (IOException e) {

					e.printStackTrace();
				}
				break;
			}
			i++;
		}

	}

	private int getDialogCount(String s) {
		File f_d = new File(event_path + "/" + label + s);
		if (!f_d.exists())
			return 0;
		File[] files = f_d.listFiles();
		return files.length;

	}

	private void delDialogCount(final String s) {
		new Thread(new Runnable() {

			@Override
			public void run() {

				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {

					e.printStackTrace();
				}
				File f_d = new File(event_path + "/" + label + s);
				if (!f_d.exists())
					return;
				File[] files = f_d.listFiles();
				if (files.length == 0)
					return;
				files[files.length - 1].delete();

			}
		}).start();

	}

	private void sendCmdAndUpdateUI() {
		eventFiles = new File(event_path + "/" + label + ".log");
		try {
			event_bw = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(eventFiles, true), "GB2312"));
		} catch (Exception e1) {

			e1.printStackTrace();
		}
		if (0 != service.send_control_cmd(service.IP, service.PORT, lstCtrl)) {

			System.out.println(label + " 失败");
			isDetectionWin = false;
			if (isDetectionLose) {
				if (event_bw != null) {
					try {
						SimpleDateFormat sdf = new SimpleDateFormat(
								"yyyy-MM-dd HH:mm:ss");
						Date dt = new Date(System.currentTimeMillis());
						String sDate = sdf.format(dt);
						sb_all.append("," + sDate + "&控制下电l");
						event_bw.write("lose," + sDate + "&控制下电中");
						event_bw.newLine();
						event_bw.flush();
						event_bw.close();
						event_bw = null;
						AlarmShow.add("控制下电中");
					} catch (Exception e) {

						e.printStackTrace();
					}

				}
			}
		} else {

			System.out.println(label + " 成功");
			isDetectionWin = true;
			if (event_bw != null) {
				try {
					SimpleDateFormat sdf = new SimpleDateFormat(
							"yyyy-MM-dd HH:mm:ss");
					Date dt = new Date(System.currentTimeMillis());
					String sDate = sdf.format(dt);
					sb_all.append("," + sDate + "&控制下电w");
					event_bw.write("win," + sDate + "&控制下电中");
					event_bw.newLine();
					event_bw.flush();
					event_bw.close();
					event_bw = null;
					AlarmShow.add("控制下电中");

				} catch (Exception e) {

					e.printStackTrace();
				}

			}
		}

		// DoReset();
	}

	private void writeAllEvent() {

		File file = new File(dialog_path);
		if (!file.exists())
			file.mkdir();

		synchronized (MGridActivity.all_Event_file) {
			try {
				if (!MGridActivity.all_Event_file.exists())
					MGridActivity.all_Event_file.createNewFile();
				if (MGridActivity.all_Event_file.length() > 1024 * 100) {
					File f = new File("/mgrid/data/Command/0.log.bak");
					if (f.exists())
						f.delete();
					MGridActivity.all_Event_file.renameTo(new File(
							"/mgrid/data/Command/0.log.bak"));
					MGridActivity.all_Event_file = new File(
							"/mgrid/data/Command/0.log");
					if (!MGridActivity.all_Event_file.exists())
						MGridActivity.all_Event_file.createNewFile();
				}
				BufferedWriter bw = new BufferedWriter(
						new OutputStreamWriter(new FileOutputStream(
								MGridActivity.all_Event_file, true), "gb2312"));
				bw.write(sb_all.toString());
				bw.newLine();
				bw.flush();
				bw.close();
			} catch (Exception e) {

				e.printStackTrace();
			}
			System.out.println("我保存了");
		}

	}

	// 重置do1 和 do2
	private void DoReset() {

		new Thread(new Runnable() {

			@Override
			public void run() {

				List<ipc_control> do_list_Reset = new ArrayList<ipc_control>();
				ipc_control do_ipc_Reset = new ipc_control();

				do_ipc_Reset.equipid = Integer.parseInt(output_SelectEquaipId);
				do_ipc_Reset.ctrlid = Integer.parseInt(CommandId);
				do_ipc_Reset.valuetype = 1;
				if (ParameterValue.equals("1"))
					do_ipc_Reset.value = "0";
				else
					do_ipc_Reset.value = "1";
				do_list_Reset.add(do_ipc_Reset);
				int i = 0;
				while (service.send_control_cmd(service.IP, service.PORT,
						do_list_Reset) != 0) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					if (i >= 10) {
						handler.sendEmptyMessage(7);
						break;
					}
					i++;
				}

			}
		}).start();

	}

	private void clearFile(File f) {
		if (f.exists()) {
			try {
				FileWriter fw = new FileWriter(f);
				fw.write("");
				fw.close();
			} catch (IOException e) {

				e.printStackTrace();
			}

		}
	}

	@Override
	public void addToRenderWindow(MainWindow rWin) {

		m_rRenderWindow = rWin;
		m_rRenderWindow.addView(Et_inputValue);
		m_rRenderWindow.addView(Btn_ensure);
		m_rRenderWindow.addView(Btn_delect);
		m_rRenderWindow.addView(Tv_Input_equipList);
		m_rRenderWindow.addView(Tv_Input_SignalList);
		m_rRenderWindow.addView(Tv_Output_equipList);
		m_rRenderWindow.addView(Tv_Output_CommandNameList);
		m_rRenderWindow.addView(Tv_outputCommandMeaning);
		m_rRenderWindow.addView(Et_timeLapse);
		m_rRenderWindow.addView(Tv_Detection_equipList);
		m_rRenderWindow.addView(Tv_Detection_SignalList);
		m_rRenderWindow.addView(Et_Detection_result);
		// m_rRenderWindow.addView(Tv_Input_equipList_t);
		// m_rRenderWindow.addView(Tv_Input_SignalList_t);
		// m_rRenderWindow.addView(Et_inputValue_t);
		m_rRenderWindow.addView(this);

		// m_rRenderWindow.addView(Btn_stop);

	}

	@Override
	public void removeFromRenderWindow(MainWindow rWin) {

	}

	@Override
	public void updateWidget() {

	}

	private void creatPath() {
		File m = new File(event_path);
		if (!m.exists()) {
			m.mkdir();
		}
	}

	@Override
	public boolean updateValue() {

		try {
			Thread.sleep(2000);
		} catch (InterruptedException e1) {

			e1.printStackTrace();
		}
		if (!neepUpdate)
			return false;

		Equipment Obj = DataGetter.equipment.htEquipmentData
				.get(input_SelectEquaipId);
		String S_value = DataGetter.proc_rtsignal(Obj, input_SelectSignalId);
		
		
		
		String lose = DataGetter.getSignalValue(input_SelectEquaipId, "10001");

	

		// Equipment Obj_d = DataGetter.equipment.htEquipmentData
		// .get(detection_SelectEquipId);
		// String string = DataGetter.proc_rtsignal(Obj_d,
		// detection_SelectSignalId);
		if (S_value == null || S_value.equals("") || lose.equals("")
				|| lose == null)
			return false;
		if (AlarmValue.equals("") || TimeLapse.equals("") || lstCtrl == null)
			return false;
		S_value = (int) Float.parseFloat(S_value) + "";
		// string = (int) Float.parseFloat(string) + "";
		lose = (int) Float.parseFloat(lose) + "";
		if (lose.equals("0"))
			return false;

		System.out.println(S_value + ":::" + "::label" + label);
		if (!S_value.equals(AlarmValue)) {
			neepUpdateTime = true;

			if (eventFiles == null)
				eventFiles = new File(event_path + "/" + label + ".log");
			if (eventFiles.exists()) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				if (isHuiFu) {
					SimpleDateFormat sdf = new SimpleDateFormat(
							"yyyy-MM-dd HH:mm:ss");
					Date dt = new Date(System.currentTimeMillis());
					String sDate = sdf.format(dt);
					sb_all.append(",无," + sDate + "&市电恢复正常&"
							+ (isYiChang ? "异常" : "正常"));
					writeAllEvent();
				}
				eventFiles.delete();
				AlarmShow.clear();

			}
			sb_all.setLength(0);
		}

		if ((!OldValue.equals(S_value) && S_value.equals(AlarmValue) && neepUpdateTime)) {
			neepUpdateTime = false;

			oldTime = System.currentTimeMillis();

			creatPath();
			if (eventFiles == null)
				eventFiles = new File(event_path + "/" + label + ".log");
			if (!eventFiles.exists()) {
				try {
					eventFiles.createNewFile();
				} catch (Exception e) {

					e.printStackTrace();
				}
			}
			isHuiFu = true;
			try {
				BufferedWriter event = new BufferedWriter(
						new OutputStreamWriter(
								new FileOutputStream(eventFiles), "GB2312"));
				SimpleDateFormat sdf = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss");
				Date dt = new Date(oldTime);
				String sDate = sdf.format(dt);
				sb_all.append(label + "," + sDate + "&检测到UPS市电异常 等待"
						+ TimeLapse + "分钟后控制下电&"
						+ Tv_Detection_SignalList.getText() + "&市电异常");
				event.write(sDate + "&检测到UPS市电异常 等待" + TimeLapse + "分钟后控制下电");
				event.newLine();
				event.flush();
				event.close();
				AlarmShow.add("检测到市电掉电 等待" + TimeLapse + "分钟后控制下电");
			} catch (Exception e) {

				e.printStackTrace();
			}

			OldValue = S_value;
			// OldValue_d = string;

		}

		if (neepUpdateTime) {
			newTime = oldTime;
			OldValue = S_value;
			// OldValue_d = string;

		} else {
			newTime = System.currentTimeMillis();

		}

		if ((newTime - oldTime) > Integer.parseInt(TimeLapse) * 1000 * 60) {

			isDetectionWin = false;
			int i = 0;
			while (!isDetectionWin) {
				if (i > 2)
					isDetectionLose = true;
				sendCmdAndUpdateUI();
				if (isDetectionLose)
					break;
				i++;
				try {
					Thread.sleep(10000);
				} catch (InterruptedException e) {

					e.printStackTrace();
				}
			}
			judgeResult();
			neepUpdateTime = true;

		}

		return true;

	}

	@Override
	public boolean needupdate() {

		return false;
	}

	@Override
	public void needupdate(boolean bNeedUpdate) {

	}

	@Override
	public String getBindingExpression() {

		return m_strCmdExpression;
	}

	@Override
	public View getView() {

		return this;
	}

	@Override
	public int getZIndex() {

		return m_nZIndex;
	}

	public Rect getBBox() {
		return m_rBBox;
	}

	String m_strID = "";
	String m_strType = "";
	int m_nZIndex = 10;
	int m_nPosX = 152;
	int m_nPosY = 287;
	int m_nWidth = 75;
	int m_nHeight = 23;
	float m_fAlpha = 1.0f;
	boolean m_bIsBold = false;
	String m_strFontFamily = "微软雅黑";
	int m_cBackgroundColor = 0xFFFCFCFC;
	int m_cFontColor = 0xFF000000;
	String m_strContent = "Setting";
	String m_strCmdExpression = "";
	boolean m_bIsValueRelateSignal = false;
	float m_fButtonWidthRate = 0.3f;
	MainWindow m_rRenderWindow = null;
	float m_fFontSize = 6.0f;
	boolean m_bPressed = false;
	Paint m_oPaint = null;
	Rect m_rBBox = null;
	InputMethodManager imm = null;
	private boolean isEquaipRun_input = true;
	private boolean isEquaipRun_output = true;
	private boolean isEquaipRun_Detection = true;

	private String input_SelectEquaipId = "";
	private String input_SelectSignalId = "";
	private String output_SelectEquaipId = "";
	private String detection_SelectEquipId = "";
	private String detection_SelectSignalId = "";

	private boolean isChengli = false;
	// private String input_SelectEquaipId_t = "";
	// private String input_SelectSignalId_t = "";

	private String AlarmValue = "";
	private String AlarmValue_name = "";
	// private String AlarmValue_name_t = "";
	// private String AlarmValue_t = "";
	private String CommandName = "";
	private String CommandId = "";
	private String ParameterId = "";
	private String ParameterValue = "";
	private String Meaning = "";
	public String TimeLapse = "";
	private String DeteResult = "";
	private String DeteResult_name = "";

	private int EI_selectId = 0;
	private int SI_selectId = 0;
	private int EO_selectId = 0;
	private int De_selectId = 0;

	private HashMap<String, String> equaipIdName = new HashMap<String, String>();
	private HashMap<String, String> signalIdName = new HashMap<String, String>();
	private HashMap<String, String> signalIdName_t = new HashMap<String, String>();
	// private HashMap<String, String> signalIdName_De= new HashMap<String,
	// String>();

	private List<String> xmlFileListName = new ArrayList<String>();

	private DocumentBuilderFactory dbf = null;
	private DocumentBuilder db = null;
	private Document doc = null;

	private String EquipTemplateId = "";
	private List<ipc_control> lstCtrl = new ArrayList<ipc_control>();
	private HashMap<String, String> Str_ipc_Ctrl = new HashMap<String, String>();
	private HashMap<String, String> Str_CommandId = new HashMap<String, String>();
	private HashMap<String, String> Str_SignalId = new HashMap<String, String>();
	// private HashMap<String, String> Str_SignalId_t = new HashMap<String,
	// String>();
	private HashMap<String, String> Str_SignalId_d = new HashMap<String, String>();

	private String label = "";

	private boolean neepUpdateTime = true;
	private long oldTime;
	private long newTime;
	private boolean neepUpdate = false;
	private String OldValue = "";
	private String OldValue_d = "";
	// private String OldValue_t = "";
	private File eventFiles = null;
	private BufferedWriter event_bw;
	private StringBuffer sb_all = new StringBuffer();
	private int W = 0;
	private int H = 0;
	private int X = 0;
	private int Y = 0;
	private boolean isDetectionLose = false;
	private boolean isDetectionWin = false;
	private BufferedWriter bw = null;
	private XmlUtils xml = XmlUtils.getXml();
	private ArrayList<String> AlarmShow = new ArrayList<String>();
	private boolean isYiChang = false;// 是否出现断电等异常
	private boolean isHuiFu = false;// 是否恢复正常

	public void updateText() {

		handler.sendEmptyMessage(8);
	}
}
