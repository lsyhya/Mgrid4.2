package com.sg.uis.LsyNewView;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.net.Uri;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mgrid.data.DataGetter;
import com.mgrid.main.MGridActivity;
import com.mgrid.main.MainWindow;
import com.mgrid.main.R;
import com.mgrid.main.SoundService;
import com.sg.common.CFGTLS;
import com.sg.common.IObject;
import com.sg.common.UtExpressionParser;
import com.sg.common.UtExpressionParser.stBindingExpression;
import com.sg.common.UtExpressionParser.stExpression;
import comm_service.service;

import data_model.ipc_control;

/** 按钮 */
public class CoolButton extends TextView implements IObject {

	// private FancyButton button=null;

	private String Prompt = "";
	private String problem = "";
	private String OK = "";
	private String ON = "";

	private int mRadius = 6;
	private int mDefaultBackgroundColor =  Color.parseColor("#383F47");
	private int mFocusBackgroundColor = Color.parseColor("#aa383F47");
	private int mDisabledBackgroundColor = Color.parseColor("#f6f7f9");


	private int mBorderColor = Color.parseColor("#A3A6AA");
	private int mBorderWidth = 1;

	public CoolButton(Context context) {
		super(context);

		//

		setTextSize(15);
		setTextColor(Color.parseColor("#ffffff"));
		// setBackgroundResource(R.drawable.bg_shadow);
		// setPadding(0, 2, 0, 0);
		// setTypeface(Typeface.MONOSPACE);

		setupBackground();

		m_oPaint = new Paint();
		m_rBBox = new Rect();

		if (MGridActivity.whatLanguage) {

			Prompt = "提示";
			problem = "是否确定?";
			OK = "确认:";
			ON = "取消";

		} else {

			Prompt = "Hint";
			problem = "Are you sure? ";
			OK = "yes";
			ON = "cancel";

		}

		setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				Toast.makeText(getContext(), "123", 200).show();
			}
		});

	}

	private void applyRadius(GradientDrawable drawable) {
		drawable.setCornerRadius(mRadius);
	}

	private void setupBackground() {
		// 其他状态背景
		GradientDrawable defaultDrawable = new GradientDrawable();
		applyRadius(defaultDrawable);
		defaultDrawable.setColor(mDefaultBackgroundColor);
		if (mBorderColor != 0) {
			defaultDrawable.setStroke(mBorderWidth, mBorderColor);
		}

		//获取焦距背景
		GradientDrawable focusDrawable = new GradientDrawable();
		applyRadius(focusDrawable);
		focusDrawable.setColor(mFocusBackgroundColor);
		if (mBorderColor != 0) {
			focusDrawable.setStroke(mBorderWidth, mBorderColor);
		}

		// 没点击背景
		GradientDrawable disabledDrawable = new GradientDrawable();
		applyRadius(disabledDrawable);
		disabledDrawable.setColor(mDisabledBackgroundColor);
		if (mBorderColor != 0) {
			disabledDrawable.setStroke(mBorderWidth, mBorderColor);
		}

		// 点击时背景
		GradientDrawable drawable2 = new GradientDrawable();
		applyRadius(drawable2);
		drawable2.setColor(mFocusBackgroundColor);
		if (mBorderColor != 0) {

			drawable2.setStroke(mBorderWidth, mBorderColor);

		}

		StateListDrawable states = new StateListDrawable();
		if (mFocusBackgroundColor != 0) {
			states.addState(new int[] { android.R.attr.state_pressed },
					drawable2);
			states.addState(new int[] { android.R.attr.state_focused },
					focusDrawable);
			states.addState(new int[] { -android.R.attr.state_enabled },
					disabledDrawable);
		}

		states.addState(new int[] {}, defaultDrawable);

		if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN) {
			this.setBackgroundDrawable(states);
		} else {
			this.setBackground(states);
		}

	}

	@SuppressLint("DrawAllocation")
	protected void onDraw(Canvas canvas) {
		if (m_rRenderWindow == null)
			return;
		if (m_rRenderWindow.isLayoutVisible(getBBox()) == false)
			return;

		super.onDraw(canvas);
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

		m_rBBox.left = nX;
		m_rBBox.top = nY;
		m_rBBox.right = nX + nWidth;
		m_rBBox.bottom = nY + nHeight;
		if (m_rRenderWindow.isLayoutVisible(m_rBBox)) {
			this.layout(nX + 1, nY + 1, nX + nWidth, nY + nHeight - 2);
		}
	}

	@Override
	public void addToRenderWindow(MainWindow rWin) {
		m_rRenderWindow = rWin;
		rWin.addView(this);
	}

	@Override
	public void removeFromRenderWindow(MainWindow rWin) {
		rWin.removeView(this);
	}

	public void parseProperties(String strName, String strValue,
			String strResFolder) {
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
		} else if ("BackgroundColor".equals(strName)) {
			if (strValue.isEmpty())
				return;
			m_cBackgroundColor = Color.parseColor(strValue);
			// this.setBackgroundColor(m_cBackgroundColor);
		} else if ("Content".equals(strName)) {
			m_strContent = strValue;
			this.setText(m_strContent);
		} else if ("FontFamily".equals(strName))
			m_strFontFamily = strValue;
		else if ("FontSize".equals(strName)) {
			float fWinScale = (float) MainWindow.SCREEN_WIDTH
					/ (float) MainWindow.FORM_WIDTH;
			m_fFontSize = Float.parseFloat(strValue) * fWinScale;
			this.setTextSize(Float.parseFloat(strValue));
		} else if ("IsBold".equals(strName))
			m_bIsBold = Boolean.parseBoolean(strValue);
		else if ("FontColor".equals(strName)) {
			m_cFontColor = Color.parseColor(strValue);

		} else if ("ClickEvent".equals(strName))
			m_strClickEvent = strValue;
		else if ("Url".equals(strName))
			m_strUrl = strValue;
		else if ("CmdExpression".equals(strName))
			m_strCmdExpression = strValue;

		else if ("HorizontalContentAlignment".equals(strName))
			m_strHorizontalContentAlignment = strValue;
		else if ("VerticalContentAlignment".equals(strName))
			m_strVerticalContentAlignment = strValue;
		else if ("Expression".equals(strName)) {

		}
	}

	@Override
	public void initFinished() {
		int nFlag = Gravity.NO_GRAVITY;
		if ("Left".equals(m_strHorizontalContentAlignment))
			nFlag |= Gravity.LEFT;
		else if ("Right".equals(m_strHorizontalContentAlignment))
			nFlag |= Gravity.RIGHT;
		else if ("Center".equals(m_strHorizontalContentAlignment))
			nFlag |= Gravity.CENTER_HORIZONTAL;

		if ("Top".equals(m_strVerticalContentAlignment))
			nFlag |= Gravity.TOP;
		else if ("Bottom".equals(m_strVerticalContentAlignment)) {
			nFlag |= Gravity.BOTTOM;
			double padSize = CFGTLS.getPadHeight(m_nHeight,
					MainWindow.FORM_HEIGHT, getTextSize());
			setPadding(0, (int) padSize, 0, 0);
		} else if ("Center".equals(m_strVerticalContentAlignment)) {
			nFlag |= Gravity.CENTER_VERTICAL;
			double padSize = CFGTLS.getPadHeight(m_nHeight,
					MainWindow.FORM_HEIGHT, getTextSize()) / 2f;
			setPadding(0, (int) padSize, 0, (int) padSize);
		}

		setGravity(nFlag);
	}

	public String getBindingExpression() {
		return m_strCmdExpression;
	}

	public void setUniqueID(String strID) {
		m_strID = strID;
	}

	public void setType(String strType) {
		m_strType = strType;
	}

	public String getUniqueID() {
		return m_strID;
	}

	public String getType() {
		return m_strType;
	}

	private void onClicked() {
		// 点击事件
		if ("".equals(m_strClickEvent) == false) {
			if ("显示桌面".equals(m_strClickEvent)) {
				// 发起Home指令
				if (m_oHomeIntent == null) {
					m_oHomeIntent = new Intent();
					m_oHomeIntent.setAction("android.intent.action.MAIN");
					m_oHomeIntent.addCategory("android.intent.category.HOME");
				}
				this.getContext().startActivity(m_oHomeIntent);

				if (m_rRenderWindow != null)
					m_rRenderWindow.showTaskUI(true);
			} else if ("显示IP".equals(m_strClickEvent)) {
				Toast.makeText(m_rRenderWindow.getContext(),
						MGridActivity.getLocalIP(), Toast.LENGTH_SHORT).show();
			} else if ("删除历史".equals(m_strClickEvent)) {
				AlertDialog.Builder builder = new AlertDialog.Builder(
						getContext());
				builder.setTitle(Prompt);
				builder.setMessage(problem);
				builder.setPositiveButton(OK,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface arg0, int arg1) {

								deleteDir(new File("/mgrid/log"));

								showHint();
							}
						});
				builder.setNegativeButton(ON,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface arg0, int arg1) {

							}
						});
				builder.create().show();

			} else if ("跳转应用".equals(m_strClickEvent)) {
				// 包名 类名都知道
				ComponentName componetName = new ComponentName(
				// 这个是另外一个应用程序的包名
						"com.mcu.iVMSHD",
						// 这个参数是要启动的Activity
						"com.mcu.iVMSHD.activity.LoadingActivity");
				Intent intent = new Intent();
				// 添加一个参数表示从apk1传过去的

				intent.setComponent(componetName);
				getContext().startActivity(intent);
				if (m_rRenderWindow != null)
					m_rRenderWindow.showTaskUI(true);

			} else if (m_strClickEvent.equals("关闭告警")) {

				Intent intent = new Intent(m_rRenderWindow.m_oMgridActivity,
						SoundService.class);
				intent.putExtra("playing", false);
				m_rRenderWindow.m_oMgridActivity.startService(intent);

			} else if (m_strClickEvent.equals("开关")) {

				isSwitch = true;

			} else {
				String[] arrStr = m_strClickEvent.split("\\(");
				boolean isMask = true;// 用来判断是否为权限页面
				boolean isNeedPW = true; // 用来判断权限页面是否需要密码
				if (m_rRenderWindow != null && arrStr[0].equals("Show")) {
					int count = -1;
					String[] str = arrStr[1].split("\\)");
					// 此次循环的作用是找出当前权限页面所在的总权限页面
					if (MGridActivity.m_MaskPage != null) {
						for (int i = 0; i < MGridActivity.m_MaskPage.length; i++) {

							for (String s : MGridActivity.m_MaskPage[i]) {
								if (s.equals(DataGetter.currentPage)) {
									count = i;
									break;
								}
							}
							if (count != -1)
								break;
						}

						if (count != -1) {
							// 此次循环是判断需要跳转的页面和当前页面是不是在同一个总权限页面
							for (String s : MGridActivity.m_MaskPage[count]) {
								if (s.equals(str[0] + ".xml")) // 如果当前页面为权限页面（只支持在一个总权限页面）
								{
									isNeedPW = false;

								}
							}
						}

						if (isNeedPW) {
							// 此次循环是判断需要跳转页面是否为权限页面。
							for (int i = 0; i < MGridActivity.m_MaskPage.length; i++) {
								for (String s : MGridActivity.m_MaskPage[i]) {
									if (!s.equals("1")) {
										if ((s.substring(0, s.length() - 4))
												.equals(str[0])) {
											MaskCount = i;
											showPassDialog();
											isMask = false;
											break;
										}
									}
								}
								if (!isMask)
									break;
							}

						}
					}

					if (isMask) {
						m_rRenderWindow.changePage(str[0]);
					}

				}
			}
		}

		// 打开网页
		if ("".equals(m_strUrl) == false) {
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setData(Uri.parse(m_strUrl));
			this.getContext().startActivity(intent);
		}

		// 发送控制命令 目前多绑定只支持，遥控值都一样的多绑定！
		if ("".equals(m_strCmdExpression) == false) {

			if (isSwitch) {
				parse_cmd();
				AlertDialog.Builder builder = new AlertDialog.Builder(
						getContext());
				builder.setTitle("提示");
				builder.setMessage("请选择开关");
				builder.setPositiveButton("关",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface arg0, int arg1) {

								if (c_control.size() == 0) {
									ipc_control ip = new ipc_control();
									ip.equipid = c_equipid;
									ip.ctrlid = c_ctrlid;
									ip.valuetype = c_parameterid;
									ip.value = c_value;
									c_control.add(ip);
									System.out.println(c_equipid + ""
											+ c_ctrlid + "" + c_parameterid
											+ "" + c_value);
								}

								new Thread(new Runnable() {

									@Override
									public void run() {
										if (0 != service.send_control_cmd(
												service.IP, service.PORT,
												c_control)) {
											String str = new String("控制失败！");
											Message msg = new Message();
											msg.what = 2;
											msg.obj = str;
											m_rRenderWindow.m_oInvalidateHandler
													.sendMessage(msg);
										} else {
											String str = new String("控制成功.");
											Message msg = new Message();
											msg.what = 1;
											msg.obj = str;
											m_rRenderWindow.m_oInvalidateHandler
													.sendMessage(msg);
										}

									}
								}).start();

							}
						});
				builder.setNegativeButton("开",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface arg0, int arg1) {

								if (o_control.size() == 0) {
									ipc_control ipcC = new ipc_control();
									ipcC.equipid = o_equipid;
									ipcC.ctrlid = o_ctrlid;
									ipcC.valuetype = o_parameterid;
									ipcC.value = o_value;
									o_control.add(ipcC);
									System.out.println(o_equipid + ""
											+ o_ctrlid + "" + o_parameterid
											+ "" + o_value);
								}
								new Thread(new Runnable() {

									@Override
									public void run() {
										if (0 != service.send_control_cmd(
												service.IP, service.PORT,
												o_control)) {
											String str = new String("控制失败！");
											Message msg = new Message();
											msg.what = 2;
											msg.obj = str;
											m_rRenderWindow.m_oInvalidateHandler
													.sendMessage(msg);
										} else {
											String str = new String("控制成功.");
											Message msg = new Message();
											msg.what = 1;
											msg.obj = str;
											m_rRenderWindow.m_oInvalidateHandler
													.sendMessage(msg);
										}

									}
								}).start();

							}
						});
				builder.create().show();

			} else {
				AlertDialog.Builder builder = new AlertDialog.Builder(
						getContext());
				builder.setTitle(Prompt);
				builder.setMessage(problem);
				builder.setPositiveButton(OK,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								synchronized (m_rRenderWindow.m_oShareObject) {
									deal_cmd();
									if ("".equals(cmd_value) == false) {
										// Log.e("button_onclick->cmd_value:",
										// cmd_value);
										// 添加广播信息
										// 将控制命令的内容添加到控制命令链表上 fjw add
										m_rRenderWindow.m_oShareObject.m_mapCmdCommand
												.put(getUniqueID(), cmd_value);

									}
								}

							}
						});
				builder.setNegativeButton(ON,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface arg0, int arg1) {

								// Toast.makeText(getContext(),
								// "You have cancelled", 1000).show();
							}
						});
				builder.create().show();
			}

		}

	}

	// 显示用户权限进入对话框
	public void showPassDialog() {
		// LayoutInflater是用来找layout文件夹下的xml布局文件，并且实例化
		LayoutInflater factory = LayoutInflater.from(m_rRenderWindow
				.getContext());
		// 把activity_login中的控件定义在View中
		final View textEntryView = factory.inflate(R.layout.page_xml, null);

		// 将LoginActivity中的控件显示在对话框中
		new AlertDialog.Builder(m_rRenderWindow.getContext())
		// 对话框的标题
				.setTitle("用户权限登录")
				// 设定显示的View
				.setView(textEntryView)
				// 对话框中的“登陆”按钮的点击事件
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int whichButton) {

						// 获取用户输入的“用户名”，“密码”
						// 注意：textEntryView.findViewById很重要，因为上面factory.inflate(R.layout.activity_login,
						// null)将页面布局赋值给了textEntryView了
						// final EditText etUserName =
						// (EditText)textEntryView.findViewById(R.id.etuserName);
						final EditText etPassword = (EditText) textEntryView
								.findViewById(R.id.pageet);

						// 将页面输入框中获得的“用户名”，“密码”转为字符串
						// String userName =
						// etUserName.getText().toString().trim();
						String password = etPassword.getText().toString()
								.trim();
						if (password
								.equals(MGridActivity.m_pagePassWord[MaskCount])) {
							String[] arrStr = m_strClickEvent.split("\\(");
							if (m_rRenderWindow != null
									&& "Show".equals(arrStr[0])) {
								String[] arrSplit = arrStr[1].split("\\)");
								m_rRenderWindow.changePage(arrSplit[0]);
							}
						} else {
							Toast.makeText(m_rRenderWindow.getContext(),
									"密码错误", Toast.LENGTH_SHORT).show();
							// Toast.makeText(m_rRenderWindow.getContext(),
							// "Incorrect username or password!",
							// Toast.LENGTH_SHORT).show();
						}
					}

				})
				// 对话框的“退出”单击事件
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						// LoginActivity.this.finish();
					}
				})

				// 对话框的创建、显示
				.create().show();
	}

	// 删除历史文件
	private void deleteDir(File dir) {
		if (dir.isDirectory()) {
			File[] files = dir.listFiles();

			for (File f : files) {
				deleteDir(f);
			}
		}
		boolean is = dir.delete();
		isDelete = "成功";

	}

	//
	private void showHint() {
		if (MGridActivity.whatLanguage)
			new AlertDialog.Builder(getContext())
					.setTitle("删除" + isDelete + ",请自行重启机器")
					.setPositiveButton("确认",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface arg0,
										int arg1) {
									// TODO Auto-generated method stub

								}
							}).create().show();
		else
			new AlertDialog.Builder(getContext())
					.setTitle("Delete Success, Please Restart")
					.setPositiveButton("OK",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface arg0,
										int arg1) {
									// TODO Auto-generated method stub

								}
							}).create().show();
	}

	// fjw add 按钮控制命令功能的控制命令的绑定表达式解析
	// 解析出控件表达式，返回控件表达式类
	public boolean deal_cmd() {
		// if("".equals(m_strCmdExpression)) return false;
		// if(m_strCmdExpression==null) return false;
		stExpression oMathExpress = UtExpressionParser.getInstance()
				.parseExpression(m_strCmdExpression);
		if (oMathExpress != null) {
			// 遍历控件表达式各个数据单元表达式类
			Iterator<HashMap.Entry<String, stBindingExpression>> it = oMathExpress.mapObjectExpress
					.entrySet().iterator();
			while (it.hasNext()) {
				stBindingExpression oBindingExpression = it.next().getValue();
				cmd_value = oBindingExpression.strValue;
			}
		}
		return true;
	}

	// Binding{[Cmd[Equip:139-Temp:178-Command:1-Parameter:1-Value:-256]]|[Cmd[Equip:140-Temp:178-Command:1-Parameter:1-Value:-256]]}
	public boolean parse_cmd() {
		if (c_equipid == -100 && c_ctrlid == -100 && c_parameterid == -100
				&& c_value.equals("") && o_equipid == -100 && o_ctrlid == -100
				&& o_parameterid == -100 && o_value.equals("")) {
			String cmd = UtExpressionParser
					.removeBindingString(m_strCmdExpression);

			String[] args = cmd.split("\\|");

			// 开
			String[] args1 = args[0].split("-");
			o_equipid = Integer.parseInt(args1[0].split(":")[1]);
			o_ctrlid = Integer.parseInt(args1[2].split(":")[1]);
			o_parameterid = Integer.parseInt(args1[3].split(":")[1]);
			System.out.println(args1[4]);
			o_value = args[0].split("Value:")[1].replace("]", "");
			// 关
			String[] args2 = args[1].split("-");
			c_equipid = Integer.parseInt(args2[0].split(":")[1]);
			c_ctrlid = Integer.parseInt(args2[2].split(":")[1]);
			c_parameterid = Integer.parseInt(args2[3].split(":")[1]);
			c_value = args[1].split("Value:")[1].replace("]", "");

		}
		return true;
	}

	@Override
	public void updateWidget() {
	}

	@Override
	public boolean updateValue() {
		return false;
	}

	@Override
	public boolean needupdate() {
		return false;
	}

	@Override
	public void needupdate(boolean bNeedUpdate) {
	}

	public View getView() {
		return this;
	}

	public int getZIndex() {
		return m_nZIndex;
	}

	public Rect getBBox() {
		return m_rBBox;
	}

	// params:
	String m_strID = "";
	String m_strType = "";
	int m_nZIndex = 7;
	int m_nPosX = 152;
	int m_nPosY = 287;
	int m_nWidth = 75;
	int m_nHeight = 23;
	float m_fAlpha = 1.0f;
	int m_cBackgroundColor = 0xF00CF00C;
	String m_strContent = "按钮";
	String m_strFontFamily = "微软雅黑";
	float m_fFontSize = 12.0f;
	boolean m_bIsBold = false;
	int m_cFontColor = 0xFF008000;
	String m_strClickEvent = "科士达-IDU系统设定UPS.xml";
	String m_strUrl = "www.baidu.com";
	String m_strCmdExpression = "Binding{[Cmd[Equip:1-Temp:173-Command:1-Parameter:1-Value:1]]}";
	String m_strHorizontalContentAlignment = "Center";
	String m_strVerticalContentAlignment = "Center";
	boolean m_bPressed = false;
	MainWindow m_rRenderWindow = null;
	String cmd_value = "";

	Paint m_oPaint = null;
	Rect m_rBBox = null;

	// 记录触摸坐标，过滤滑动操作。解决滑动误操作点击问题。
	public float m_xscal = 0;
	public float m_yscal = 0;

	private int MaskCount = -1;

	Intent m_oHomeIntent = null;

	String isDelete = "失败";
	private boolean isSwitch = false;
	private int c_equipid = -100, c_ctrlid = -100, c_parameterid = -100,
			o_equipid = -100, o_ctrlid = -100, o_parameterid = -100;
	private String c_value = "", o_value = "";
	List<ipc_control> c_control = new ArrayList<ipc_control>();
	List<ipc_control> o_control = new ArrayList<ipc_control>();
}
