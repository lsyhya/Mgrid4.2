package com.mgrid.util;

import java.util.ArrayList;
import java.util.List;

import com.sg.common.UtExpressionParser;

public class ExpressionUtils {

	private static ExpressionUtils expressionUtils = new ExpressionUtils();

	public static ExpressionUtils getExpressionUtils() {
		return expressionUtils;
	}

	// Binding{[Value[Equip:118-Temp:177-Signal:78]]|[Value[Equip:118-Temp:177-Signal:78]]}
	public List<String> parse(String cmd) {
		String removeBind = removeBindingString(cmd); // [Value[Equip:118-Temp:177-Signal:78]]|[Value[Equip:118-Temp:177-Signal:78]]
		String[] eachCmd = removeBind.split("\\|"); // [Value[Equip:118-Temp:177-Signal:78]]
		List<String> cmdList = new ArrayList<String>();
		for (int i = 0; i < eachCmd.length; i++) {
			String[] eachPart = eachCmd[i].split("-");
			if(eachPart.length>1)
			{
			String equipId = eachPart[0].split(":")[1];
			String tempId = eachPart[1].split(":")[1];
			String signalId = eachPart[2].replace("]", "").split(":")[1];
			cmdList.add(equipId + "-" + tempId + "-" + signalId);
			}else
			{
				String equipId = eachPart[0].replace("]", "").split(":")[1];
				cmdList.add(equipId);
			}
		}
		return cmdList;
	}
	
	//Binding{[Cmd[Equip:1-Temp:177-Command:2-Parameter:1-Value:0]]}	
	public List<String> parseYKP(String cmd) {
		String removeBind = removeBindingString(cmd); 
		String[] eachCmd = removeBind.split("\\|"); 
		List<String> cmdList = new ArrayList<String>();
		for (int i = 0; i < eachCmd.length; i++) {
			String[] eachPart = eachCmd[i].split("-");
			if(eachPart.length==5)
			{
			String equipId = eachPart[0].split(":")[1];
			String tempId = eachPart[1].split(":")[1];
			String Command = eachPart[2].split(":")[1];
			String Parameter = eachPart[3].split(":")[1];
			String Value = eachPart[4].replace("]", "").split(":")[1];
			cmdList.add(equipId + "-" + tempId + "-" + Command+"-"+Parameter+"-"+Value);
			}else
			{
				String equipId = eachPart[0].replace("]", "").split(":")[1];
				cmdList.add(equipId);
			}
		}
		return cmdList;
	}

	public List<String> parseOnlyEq(String cmd) {
		String removeBind = UtExpressionParser // [Value[Equip:118]]|[Value[Equip:119]]
				.removeBindingString(cmd);
		String[] eachCmd = removeBind.split("\\|"); // [Value[Equip:118]]
		List<String> cmdList = new ArrayList<String>();
		for (int i = 0; i < eachCmd.length; i++) {
			String[] eachPart = eachCmd[i].split(":"); // [Value[Equip , 118]]
			String equipId = eachPart[1].replace("]", "");
			cmdList.add(equipId);
		}
		return cmdList;
	}

	public int getSize(String cmd)
	{
		String removeBind = UtExpressionParser // [Value[Equip:118]]|[Value[Equip:119]]
				.removeBindingString(cmd);
		String[] eachCmd = removeBind.split("\\|"); // [Value[Equip:118]]
		return eachCmd.length;
	}
	

	public String removeBindingString(String cmd) {
		String s = cmd.replace("}", "");
		s = s.replace("Binding{", "");
		return s;
	}

}
