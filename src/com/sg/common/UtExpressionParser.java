package com.sg.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.mgrid.main.MGridActivity;

import android.os.Message;
import android.widget.Toast;

//表达式解析类
public class UtExpressionParser {
	public UtExpressionParser() {}
	
	private volatile static UtExpressionParser _instance = null;
	//实例化本类变量
	public synchronized static UtExpressionParser getInstance() {
		if (_instance == null) {
			_instance = new UtExpressionParser();
		}
		
		return _instance;
	}
	
	public class stBindingExpression {
		public String strBindType = "";
		public int nEquipId = -1;
		public int nTemplateId = -1;
		public int nSignalId = -1;
		// for cmd
		public int nCommandId = -1;
		public int nValueType = 0;   
		public String strValue = "";
		
		// for trigger
		public int nEventId = -1;
		public int nConditionId = -1;
	}
	
	public class stIfElseExpression {
		public boolean isDigist = false;
		public String strRet = "0";
		public String strTrueSelect = "";
		public String strFalseSelect = "";
	}
	
	public class stIntervalExpression {
		public List<String> listInterval;
		public List<String> listValue;
		public stIntervalExpression() {
			listInterval = new ArrayList<String>();
			listValue = new ArrayList<String>();
		}
	}
	
	//控件表达式 类
	public class stExpression {
		// TODO: 各属性含义？  -- CharlesChen
		public String strUiType = "";
		public String strBindType = "";
		public int iMask = 0;        
		public HashMap<String, stBindingExpression> mapObjectExpress = null;
		public List<String> listMathExpress = null; 
		public stExpression() {
			mapObjectExpress = new HashMap<String, stBindingExpression>();
			listMathExpress = new ArrayList<String>();
		}
	}
	

	/**
	 * 
	 * 去掉两边的大括号
	 *
	 * */
	public static String removeBindingString(String strExpression) {
		if ("".equals(strExpression) == true)
			return "";
		
		String[] arrStr = strExpression.split("\\{");
	
		if (arrStr.length <= 1)
			return "";
		String newStr = arrStr[1];
		arrStr = newStr.split("\\}");
	
		if (arrStr.length < 1)
			return "";
		newStr = arrStr[0];
		return newStr;
	}

	/**
	 * 
	 * 
	 * 
	 * */
	public static String getMathExpression(String strBindingExpression) {
		if ("".equals(strBindingExpression) == true)
			return "";
		
		String strExpression = removeBindingString(strBindingExpression);
		if ("".equals(strExpression) == true)
			return "";

		
		int nLastIndex = strExpression.length()-1;
		for (int i = strExpression.length() - 1; i > 0; --i) {
			char cCur = strExpression.charAt(i);
			if (']' == cCur || ')' == cCur) {
				nLastIndex = i;
				break;
			}
		}
		
		return strExpression.substring(0, nLastIndex+1);
	}
	

	public stExpression parseExpression(String strBindingExpression) {
		if ("".equals(strBindingExpression) == true)
			return null;

		String newStr = getMathExpression(strBindingExpression);

		
		stExpression oExpression = new stExpression();
		HashMap<String, String> mapStrExpress = new HashMap<String, String>();
		
		boolean bFirst = false; 
		boolean bSecond = false;
		int nStartIndex = 0; 
		int nEndIndex = 0;    
		boolean bExpressFlag = false;
		//获取表达式里面的内容每个内容 
		//例如：[Equip[Equip:1]]|[Equip[Equip:2]]|[Equip[Equip:3]]|[Equip[Equip:4]]|[Equip[Equip:6]]
		//分成    [Equip[Equip:1]] ....   [Equip[Equip:6]] 这样
		for (int i = 0; i < newStr.length(); ++i) {
			char c = newStr.charAt(i);
			if (c == '[' && bFirst == false) {
				bFirst = true;
				nStartIndex = i;
				bExpressFlag = true;
				continue;
			}
			if (c == '[' && bSecond == false) {
				bSecond = true;
				continue;
			}
			
			if (c == ']' && bSecond == true) {
				bSecond = false;
				continue;
			}
		
			if (c == ']' && bFirst == true) {
				bFirst = false;
				nEndIndex = i;
				
			
				String strKey = newStr.substring(nStartIndex, nEndIndex+1);
				mapStrExpress.put(strKey, strKey); 
				
				
				oExpression.listMathExpress.add(strKey); 
				bExpressFlag = false;
				continue;
			}
			
	
			if (bExpressFlag == false) {
				oExpression.listMathExpress.add(c+"");
			}
		}
	

		Iterator<String> iter = mapStrExpress.keySet().iterator();
        while (iter.hasNext()) {
        	String strValue = mapStrExpress.get(iter.next());
        	if ("".equals(strValue))
        		continue;
        	stBindingExpression st = new stBindingExpression(); 
        	try {
        	String[] arrExpress = strValue.split("\\[");
        	st.strBindType = arrExpress[1];//表达式类型
        	oExpression.strBindType = arrExpress[1];    
      //  	System.out.println(":::"+strBindingExpression);
      //  	System.out.println(",,,"+strValue);
        	String strNew = arrExpress[2];              
        	String[] arrTemp = strNew.split("\\]"); 
        	strNew = arrTemp[0];                        
        	String[] arrId = strNew.split("-");   
        	//System.out.println(strNew);
        	for (int i = 0; i < arrId.length; ++i) {
        		String[] arrValue = arrId[i].split(":");
        		if (arrValue.length < 2)
        			continue;
        		if ("Equip".equals(arrValue[0])) 
        			st.nEquipId = Integer.parseInt(arrValue[1]);
        		else if ("Temp".equals(arrValue[0])) 
        			st.nTemplateId = Integer.parseInt(arrValue[1]);
        		else if ("Signal".equals(arrValue[0])) 
        			st.nSignalId = Integer.parseInt(arrValue[1]);
        		else if ("Command".equals(arrValue[0])) {
        			st.nCommandId = Integer.parseInt(arrValue[1]);
        		}
        		else if ("Parameter".equals(arrValue[0])) {
        			st.nValueType = Integer.parseInt(arrValue[1]);
        		}
        		else if ("Value".equals(arrValue[0])) {
        			st.strValue = arrValue[1];
        		}
        		else if ("Event".equals(arrValue[0])) {
        			st.nEventId = Integer.parseInt(arrValue[1]);
        		}
        		else if ("Condition".equals(arrValue[0])) {
        			st.nConditionId = Integer.parseInt(arrValue[1]); 
        		}
        	}
        	 
        	oExpression.mapObjectExpress.put(strValue, st); 	//添加表达式内容  和 表达式解析（包括表达式类型和类型中数据类型）
            } catch (Exception e) {
				
            	Message msg=new Message();
            	msg.what=2;
            	msg.obj=strBindingExpression+"";
            	MGridActivity.handler.sendMessage(msg);
			}
        }
         
		return oExpression;   
	}
	
	// [Value[Equip:61-Temp:6-Signal:2]]==0?"图片1.png":"图片2.png"
	public stIfElseExpression parseIfElseExpression(String strBindingExpression) {
		
		if ("".equals(strBindingExpression) == true)
			return null;
		
		String strExpression = removeBindingString(strBindingExpression);
		if ("".equals(strExpression) == true)
			return null;

		int nStartIndex = strExpression.length()-1;
		for (int i = strExpression.length() - 1; i > 0; --i) {
			char cCur = strExpression.charAt(i);
			if (']' == cCur || ')' == cCur) {
				nStartIndex = i;
				break;
			}
		}

		String strNewStr = strExpression.substring(nStartIndex+1, strExpression.length());
		if (strNewStr == null || "".equals(strNewStr))
			return null;
		
		stIfElseExpression st = new stIfElseExpression();

		String[] arrStr = strNewStr.split("\\=");
		// ""、""和 0?"图片1.png":"图片2.png"
		if (arrStr.length < 1)
			return null;
		
		// 0?"图片1.png":"图片2.png"
		strNewStr = arrStr[arrStr.length-1];
		arrStr = strNewStr.split("\\?");
		if (arrStr.length < 2)
			return null;
		st.strRet = arrStr[0];
		
		try {
			Double.parseDouble(st.strRet);
			st.isDigist = true;
		}catch(Exception e) {
			st.isDigist = false;
		}
		// "图片1.png":"图片2.png"
		strNewStr = arrStr[1];
		arrStr = strNewStr.split(":");
		if (arrStr.length < 2)
			return null;
		for (int i = 0; i < arrStr.length; ++i) {
			String[] arrValue = arrStr[i].split("\"");
			if (arrValue.length < 2)
				continue;
			if (i == 0)
				st.strTrueSelect = arrValue[1];
			else if (i == 1)
				st.strFalseSelect = arrValue[1];
		}
		return st;
	}
	
	//  [Value[Equip:61-Temp:6-Signal:2]]|10&20|#E2D80000&#FF9C0092
	public stIntervalExpression parseColorIntervalExpression(String strBindingExpression) {
		if ("".equals(strBindingExpression) == true)
			return null;
		String strExpression = removeBindingString(strBindingExpression);
		if ("".equals(strExpression) == true)
			return null;
		
		int nStartIndex = strExpression.length()-1;
		for (int i = strExpression.length() - 1; i > 0; --i) {
			char cCur = strExpression.charAt(i);
			if (']' == cCur || ')' == cCur) {
				nStartIndex = i; 
				break;
			}
		}

		String strNewStr = strExpression.substring(nStartIndex+1, strExpression.length());
		if (strNewStr == null || "".equals(strNewStr))
			return null;
		
		stIntervalExpression st = new stIntervalExpression();
		// |10&20|#E2D80000&#FF9C0092
		String[] arrStr = strNewStr.split("\\|");
		if (arrStr.length < 3)
			return null;
		strNewStr = arrStr[1]; // 10&20
		String strNewStr1 = arrStr[2]; //#E2D80000&#FF9C0092
		String[] arrIntervals = strNewStr.split("&");
		for (int i = 0; i < arrIntervals.length; ++i) {
			st.listInterval.add(arrIntervals[i]);
		}
		String[] arrColors = strNewStr1.split("&");
		for (int i = 0; i < arrColors.length; ++i) {
			st.listValue.add(arrColors[i]);
		}
		return st;
	}
}
