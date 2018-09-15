package com.sg.uis;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.mgrid.main.MGridActivity;
import com.mgrid.main.MainWindow;
import com.sg.common.CFGTLS;
import com.sg.common.IObject;
import com.sg.common.SgRealTimeData;
import com.sg.common.UtExpressionParser;
import com.sg.common.UtExpressionParser.stBindingExpression;
import com.sg.common.UtExpressionParser.stExpression;
import com.sg.web.LableObject;
import com.sg.web.base.ViewObjectBase;
import com.sg.web.base.ViewObjectSetCallBack;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import comm_service.service;
import data_model.ipc_control;

/** 标签 */
public class SgLabel extends TextView implements IObject ,ViewObjectSetCallBack{

	// ShimmerTextView shimmerTv;
	// Shimmer sr;
	
	public ViewObjectBase base=new LableObject();
	

	public SgLabel(Context context) {
		super(context);

		init(this);

	}

	private void init(View view) {
		view.setClickable(true);
		view.setBackgroundColor(0x00000000);
		m_rBBox = new Rect();
		

		view.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (m_cmdExpression != null && !m_cmdExpression.equals("")) {

					parse_cmd();
					AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
					builder.setTitle("提示");
					builder.setMessage("请选择开关");
					builder.setPositiveButton("关", new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface arg0, int arg1) {

							if (c_control.size() == 0) {
								ipc_control ip = new ipc_control();
								ip.equipid = c_equipid;
								ip.ctrlid = c_ctrlid;
								ip.valuetype = c_parameterid;
								ip.value = c_value;
								c_control.add(ip);
								System.out.println(c_equipid + "" + c_ctrlid + "" + c_parameterid + "" + c_value);
							}

							new Thread(new Runnable() {

								@Override
								public void run() {
									if (0 != service.send_control_cmd(service.IP, service.PORT, c_control)) {
										String str = new String("控制失败！");
										Message msg = new Message();
										msg.what = 2;
										msg.obj = str;
										m_rRenderWindow.m_oInvalidateHandler.sendMessage(msg);
									} else {
										String str = new String("控制成功.");
										Message msg = new Message();
										msg.what = 1;
										msg.obj = str;
										m_rRenderWindow.m_oInvalidateHandler.sendMessage(msg);
									}

								}
							}).start();

						}
					});
					builder.setNegativeButton("开", new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface arg0, int arg1) {

							if (o_control.size() == 0) {
								ipc_control ipcC = new ipc_control();
								ipcC.equipid = o_equipid;
								ipcC.ctrlid = o_ctrlid;
								ipcC.valuetype = o_parameterid;
								ipcC.value = o_value;
								o_control.add(ipcC);
								System.out.println(o_equipid + "" + o_ctrlid + "" + o_parameterid + "" + o_value);
							}
							new Thread(new Runnable() {

								@Override
								public void run() {
									if (0 != service.send_control_cmd(service.IP, service.PORT, o_control)) {
										String str = new String("控制失败！");
										Message msg = new Message();
										msg.what = 2;
										msg.obj = str;
										m_rRenderWindow.m_oInvalidateHandler.sendMessage(msg);
									} else {
										String str = new String("控制成功.");
										Message msg = new Message();
										msg.what = 1;
										msg.obj = str;
										m_rRenderWindow.m_oInvalidateHandler.sendMessage(msg);
									}

								}
							}).start();

						}
					});
					builder.create().show();
				}

			}
		});
	}

	@Override
	public void doLayout(boolean bool, int l, int t, int r, int b) {
		if (m_rRenderWindow == null)
			return;
		int nX = l + (int) (((float) m_nPosX / (float) MainWindow.FORM_WIDTH) * (r - l));
		int nY = t + (int) (((float) m_nPosY / (float) MainWindow.FORM_HEIGHT) * (b - t));
		int nWidth = (int) (((float) m_nWidth / (float) MainWindow.FORM_WIDTH) * (r - l));
		int nHeight = (int) (((float) m_nHeight / (float) MainWindow.FORM_HEIGHT) * (b - t));

		m_rBBox.left = nX;
		m_rBBox.top = nY;
		m_rBBox.right = nX + nWidth;
		m_rBBox.bottom = nY + nHeight;
		if (m_rRenderWindow.isLayoutVisible(m_rBBox)) {
			layout(nX, nY, nX + nWidth, nY + nHeight);
			// shimmerTv.layout(nX, nY, nX + nWidth, nY + nHeight);
			// shimmerTv.setPadding(0, nHeight/8, 0, 0);
		}
	}

	public void onDraw(Canvas canvas) {

		if (m_rRenderWindow == null)
			return;
		if (m_rRenderWindow.isLayoutVisible(getBBox()) == false)
			return;

		super.onDraw(canvas);
	}

	public View getView() {
		return this;
	}

	public int getZIndex() {
		return m_nZIndex;
	}

	@Override
	public void addToRenderWindow(MainWindow rWin) {
		m_rRenderWindow = rWin;
		rWin.addView(this);
		// rWin.addView(shimmerTv);
	}

	@Override
	public void removeFromRenderWindow(MainWindow rWin) {
		rWin.removeView(this);
	}

	public void parseProperties(String strName, String strValue, String strResFolder) {

		if ("ZIndex".equals(strName)) {
			m_nZIndex = Integer.parseInt(strValue);
				
//			base.setZIndex(m_nZIndex);
//			base.setFromWight(MainWindow.FORM_WIDTH);
//			base.setFromHeight(MainWindow.FORM_HEIGHT);
			
			if (MainWindow.MAXZINDEX < m_nZIndex)
				MainWindow.MAXZINDEX = m_nZIndex;
		} else if ("Location".equals(strName)) {
			String[] arrStr = strValue.split(",");
			m_nPosX = Integer.parseInt(arrStr[0]);
			m_nPosY = Integer.parseInt(arrStr[1]);
			
			//base.setLeft(m_nPosX);
			//base.setTop(m_nPosY);
			
			
		} else if ("Size".equals(strName)) {
			String[] arrSize = strValue.split(",");
			m_nWidth = Integer.parseInt(arrSize[0]);
			m_nHeight = Integer.parseInt(arrSize[1]);
			
			//base.setWight(m_nWidth);
			//base.setHeght(m_nHeight);
			
		} else if ("Alpha".equals(strName)) {
			m_fAlpha = Float.parseFloat(strValue);
		} else if ("RotateAngle".equals(strName)) {
			m_fRotateAngle = Float.parseFloat(strValue);
		} else if ("Content".equals(strName)) {
			m_strContent = strValue;
			c_Content = strValue;
			this.setText(m_strContent);
			//((LableObject)base).setText(c_Content);
			// shimmerTv.setText(m_strContent);
		} else if ("FontFamily".equals(strName)) {
			m_strFontFamily = strValue;
			// this.setTypeface(MyApplication.typeface);
		} else if ("FontSize".equals(strName)) {
			float fWinScale = (float) MainWindow.SCREEN_WIDTH / (float) MainWindow.FORM_WIDTH;
			m_fFontSize = Float.parseFloat(strValue) * fWinScale;
			//((LableObject)base).setTextSize(m_fFontSize);
			this.setTextSize(m_fFontSize);
			// shimmerTv.setTextSize(m_fFontSize);
		} else if ("IsBold".equals(strName))
			m_bIsBold = Boolean.parseBoolean(strValue);
		else if ("FontColor".equals(strName)) {
			currColor=strValue;
			m_cFontColor = Color.parseColor(strValue);
			m_cStartFillColor = m_cFontColor;
			//((LableObject)base).setTextColor("#"+strValue.substring(3, strValue.length()));
			this.setTextColor(m_cFontColor);
			// shimmerTv.setTextColor(m_cFontColor);
		} else if ("HorizontalContentAlignment".equals(strName))
			m_strHorizontalContentAlignment = strValue;
		else if ("VerticalContentAlignment".equals(strName))
			m_strVerticalContentAlignment = strValue;
		else if ("Expression".equals(strName))
			m_strExpression = strValue;
		else if ("ColorExpression".equals(strName))
			m_strColorExpression = strValue; // 字体颜色变化表达式
		else if ("CmdExpression".equals(strName))
			m_cmdExpression = strValue;
		else if ("MaxLevelExpression".equals(strName))
			try {
				if (strValue != null && !strValue.equals("")) {
					String[] str = strValue.split(" ");
					max = Float.parseFloat(str[1]);
					mark = str[0];
				}

			} catch (Exception e) {
				System.out.println(e);
				Toast.makeText(getContext(), MGridActivity.XmlFile + ":" + getUniqueID() + "出错", Toast.LENGTH_LONG)
						.show();
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
			double padSize = CFGTLS.getPadHeight(m_nHeight, MainWindow.FORM_HEIGHT, getTextSize());
			setPadding(0, (int) padSize, 0, 0);
		} else if ("Center".equals(m_strVerticalContentAlignment)) {
			nFlag |= Gravity.CENTER_VERTICAL;
			double padSize = CFGTLS.getPadHeight(m_nHeight, MainWindow.FORM_HEIGHT, getTextSize()) / 2;
			setPadding(0, (int) padSize, 0, (int) padSize);
		}

		setGravity(nFlag);
		// shimmerTv.setGravity(nFlag);
	}

	public String getBindingExpression() {
		return m_strExpression;
	}

	public void updateWidget() {

		this.setTextColor(m_cFontColor);
		this.setText(m_strContent);
		this.invalidate();

		// shimmerTv.setTextColor(m_cFontColor);
		// shimmerTv.setText(m_strContent);
		// shimmerTv.invalidate();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public boolean updateValue() {

		m_bneedupdate = false;

		SgRealTimeData oRealTimeData = m_rRenderWindow.m_oShareObject.m_mapRealTimeDatas.get(this.getUniqueID());

		stExpression oMathExpress = m_rRenderWindow.m_oCaculateThread.m_mapExpression.get(m_strID);

		stBindingExpression oBindingExpression = null;
		if (oMathExpress.mapObjectExpress.size() <= 1) {
			oBindingExpression = m_rRenderWindow.Label_data.get(m_strID);
			if (oBindingExpression == null)
				return false;
		}

		if (oRealTimeData == null)
			return false;

		String strValue = oRealTimeData.strValue;

		if (strValue == null || "".equals(strValue) == true)
			return false;

		if (MGridActivity.LabelList.size() != 0&&oBindingExpression!=null) {

			if (MGridActivity.LabelList.contains(oBindingExpression.nEquipId + "")) {

				m_strContent = "--";
				m_cFontColor = Color.GRAY;
				m_bneedupdate = false;
				return true;
			}
		}

		if (MGridActivity.EventClose.size() > 0&&oBindingExpression!=null) {
			Iterator iter = MGridActivity.EventClose.entrySet().iterator();
			while (iter.hasNext()) {
				Map.Entry entry = (Map.Entry) iter.next();
				Map<String, String> map = (Map<String, String>) entry.getValue();
				Iterator iter1 = map.entrySet().iterator();
				while (iter1.hasNext()) {
					Map.Entry entry1 = (Map.Entry) iter1.next();
					String eid = (String) entry1.getKey();
					String sid = (String) entry1.getValue();
					if (eid.equals(oBindingExpression.nEquipId + "") && sid.equals(oBindingExpression.nSignalId + "")) {
						m_strContent = "--";
						m_cFontColor = Color.GRAY;
						m_bneedupdate = false;
						return true;
					}

				}
			}

		}

		// 内容变化才刷新页面
		if (m_strSignalValue.equals(strValue) == false) {

			m_strSignalValue = strValue; // 保存数值留作下次比较

			m_strContent = strValue; // 界面数值赋予

			parseFontcolor(oRealTimeData.strData); // 解析数值颜色表达式 fjw add
			try {
				float ff = Float.parseFloat(strValue);
				// if (max != -999 && ff >= max) {
				// m_strContent = max + "";
				// }
				if (max != -999) {
					switch (mark) {
					case ">":
						if (ff > max) {
							m_strContent = c_Content;
						}
						break;
					case "<":
						if (ff < max) {
							m_strContent = c_Content;
						}
						break;
					case ">=":
						if (ff >= max) {
							m_strContent = c_Content;
						}
						break;
					case "<=":
						if (ff <= max) {
							m_strContent = c_Content;
						}
						break;
					case "==":
						if (ff == max) {
							m_strContent = c_Content;
						}
						break;
					}
				}

			} catch (Exception e) {

			}

			return true;

		}
		return false;
	}

	// 颜色解析函数 传入参数：显示值 fjw add
	public int parseFontcolor(String strValue) {
		m_cFontColor = m_cStartFillColor;
		if ((m_strColorExpression == null) || ("".equals(m_strColorExpression)))
			return -1;
		if ((strValue == null) || ("".equals(strValue)))
			return -1;
		if ("-999999".equals(strValue))
			return -1;

		// Log.e("Label-updataValue",
		// "into!"+"--"+m_strColorExpression.substring(0,1));
		if ((">".equals(m_strColorExpression.substring(0, 1))) != true)
			return -1;

		String buf[] = m_strColorExpression.split(">"); // 提取表达式中的条件与颜色单元
		for (int i = 1; i < buf.length; i++) {
			String a[] = buf[i].split("\\[|\\]"); // 处理分隔符[ ]
			// Log.e("Label-updataValue", "比较值"+a[0]+"+颜色数值："+a[1]);
			// 比较数值
			float data = Float.parseFloat(a[0]); // 获得比较值
			float value = Float.parseFloat(strValue); // 输入值
			if (value > data) {
				m_cFontColor = Color.parseColor(a[1]);
			}
		}
		return m_cFontColor;
	}

	public boolean parse_cmd() {
		if (c_equipid == -100 && c_ctrlid == -100 && c_parameterid == -100 && c_value.equals("") && o_equipid == -100
				&& o_ctrlid == -100 && o_parameterid == -100 && o_value.equals("")) {
			String cmd = UtExpressionParser.removeBindingString(m_cmdExpression);

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
	public boolean needupdate() {
		return m_bneedupdate;
	}

	@Override
	public void needupdate(boolean bNeedUpdate) {
		m_bneedupdate = bNeedUpdate;
	}

	public void setUniqueID(String strID) {
		m_strID = strID;
		//base.setTypeId(m_strID);
	}

	public void setType(String strType) {
		m_strType = strType;
		//base.setType(m_strType);
	}

	public String getUniqueID() {
		return m_strID;
	}

	public String getType() {
		return m_strType;
	}

	public Rect getBBox() {
		return m_rBBox;
	}

	// params:
	String m_strID = "";
	String m_strType = "Label";
	int m_nZIndex = 1;
	int m_nPosX = 49;
	int m_nPosY = 306;
	int m_nWidth = 60;
	int m_nHeight = 30;
	float m_fAlpha = 1.0f;
	float m_fRotateAngle = 0.0f;
	String m_strContent = "设置内容";
	String m_strFontFamily = "微软雅黑";
	String c_Content = "";
	float m_fFontSize = 12.0f;
	boolean m_bIsBold = false;
	int m_cFontColor = 0xFF008000;
	int m_cStartFillColor = 0x00000000;
	String currColor;
	String m_strHorizontalContentAlignment = "Center";
	String m_strVerticalContentAlignment = "Center";
	String m_strExpression = "Binding{[Value[Equip:114-Temp:173-Signal:1]]}";
	String m_strColorExpression = ">20[#FF009090]>30[#FF0000FF]>50[#FFFF0000]>60[#FFFFFF00]";
	String m_cmdExpression = "";
	float max = -999;
	String mark = "";
	MainWindow m_rRenderWindow = null;
	String m_strSignalValue = "";

	Rect m_rBBox = null;

	public boolean m_bneedupdate = true;
	public boolean m_bValueupdate = true;
	public boolean First = true;

	private int c_equipid = -100, c_ctrlid = -100, c_parameterid = -100, o_equipid = -100, o_ctrlid = -100,
			o_parameterid = -100;
	private String c_value = "", o_value = "";
	List<ipc_control> c_control = new ArrayList<ipc_control>();
	List<ipc_control> o_control = new ArrayList<ipc_control>();


	@Override
	public void onCall() {
		
		base.setZIndex(m_nZIndex);
		base.setFromHeight(MainWindow.FORM_HEIGHT);
		base.setFromWight(MainWindow.FORM_WIDTH);
		
		base.setWight(m_nWidth);
		base.setHeght(m_nHeight);
		
		base.setLeft(m_nPosX);
		base.setTop(m_nPosY);
		
		base.setTypeId(m_strID);
		base.setType(m_strType);
		
		((LableObject)base).setText(c_Content);
		((LableObject)base).setTextSize(m_fFontSize);
		((LableObject)base).setTextColor("#"+currColor.substring(3, currColor.length()));
		
	}
	

}
