package com.sg.common;

import java.util.HashMap;
import java.util.Map;


/**
 * 
 * @author lsy
 *全局变量
 */
public class TotalVariable {

	public static  Map<String,IObject> ORDERLISTS=new HashMap<String, IObject>();   //序列号 容器 index+ChangLabel对象
	public static  Map<String,IObject> ALARMPIEMAP=new HashMap<String, IObject>();  //控件id  控件对象
}
