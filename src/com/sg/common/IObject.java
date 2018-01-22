package com.sg.common;

import com.mgrid.main.MainWindow;

import android.view.View;
/** IObject */
public interface IObject {
	
	/** 用于布局UI在屏幕的显示位置l,t,r,b分别对应矩形的左、上、右、上 */
	public void doLayout(boolean bool, int l, int t, int r, int b);
	
	/** UI的唯一ID标识，该ID解析XML的时候读取得到 */
	public void setUniqueID(String strID);
	
	/** 获取UI的唯一ID */
	public String getUniqueID();
	
	/** UI的类型(Label、Image or Button) */
	public void setType(String strType);
	
	/** 获取UI的类型 */
	public String getType();
	
	/** 用于解析xml，如<Element ID="label0" Type="Form">
	   strName对应Element,strValue对应label0，strResFolder资源路径  */
	public void parseProperties(String strName, String strValue, String strResFolder) throws Exception;
	
	/** 完成初始化后处理函数 */
	public void initFinished();
	
	/** 用于将UI添加到窗口 */
	public void addToRenderWindow(MainWindow rWin);
	
	/** 从窗口中移除UI */
	public void removeFromRenderWindow(MainWindow rWin);
	
	/** 更新UI属性的变化 */
	public void updateWidget();
	
	/** 更新控件数据， 返回值表示是否需要刷新界面 */
	public boolean updateValue();
	
	/** 是否需要更新 */
	public boolean needupdate();
	public void needupdate(boolean bNeedUpdate);
	
	/** 获取绑定表达式 */
	public String getBindingExpression();
	
	/** 获取View */
	public View getView();
	
	/** 获取控件所在层 */
	public int getZIndex();
	
}
