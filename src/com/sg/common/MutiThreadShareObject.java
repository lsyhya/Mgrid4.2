package com.sg.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.mgrid.data.DataGetter;
import com.mgrid.data.EquipmentDataModel.Event;
import com.mgrid.data.EquipmentDataModel.Signal;
import com.sg.common.UtExpressionParser.stBindingExpression;
import com.sg.common.UtExpressionParser.stExpression;
import comm_service.service;

import data_model.ipc_cfg_signal_name;
import data_model.ipc_cfg_trigger_value;
import data_model.ipc_control;
import data_model.ipc_history_signal;
import data_model.local_his_event;
import data_model.local_his_signal;
import data_model.save_curve_signal;
import data_model.save_multipoint_signal;

public class MutiThreadShareObject {
	
	public MutiThreadShareObject() {
		m_listUpdateFromTcpValues = new ArrayList<IObject>();
		m_mapCmdCommand = new HashMap<String, String>();
		m_mapTriggerCommand =  new HashMap<String, String>();
		m_mapNamingCommand =  new HashMap<String, String>();
		m_mapEventListDatas = new HashMap<String, Hashtable<String, Hashtable<String, Event>> >();
		m_mapSignalListDatas = new HashMap<String, Hashtable<String, Signal> >();
		m_mapMutiChartDatas = new HashMap<String, List<String>>();
		m_mapRealTimeDatas = new HashMap<String, SgRealTimeData>();
		m_mapHistorySignals = new HashMap<String, List<List<ipc_history_signal>>>();
		m_mapHistorySignal = new HashMap<String, List<ipc_history_signal>>();
		m_mapLocalSignal = new HashMap<String, List<local_his_signal>>();
		m_mapSaveEquipt = new HashMap<String,List<local_his_signal>>();
		m_mapSaveSignal = new HashMap<String,List<local_his_signal>>();
		m_mapHisPoint = new HashMap<String,save_curve_signal>();
		m_mapMultiPoint = new HashMap<String,save_multipoint_signal>();
		m_mapLocalEvent = new HashMap<String,List<local_his_event>>();
		m_SgIsolationEventSetter=new HashMap<String, stBindingExpression>();
	}

	//列表清0
	public void clearFromTcpValue() {
		m_listUpdateFromTcpValues.clear();
		m_mapMutiChartDatas.clear();
		m_mapEventListDatas.clear();
		m_mapSignalListDatas.clear();
		m_mapRealTimeDatas.clear();
		m_mapHistorySignals.clear();
		m_mapHistorySignal.clear();
		m_mapLocalSignal.clear();
		m_mapSaveEquipt.clear();
		m_mapSaveSignal.clear();
		m_mapHisPoint.clear();
		m_mapMultiPoint.clear();
		m_mapLocalEvent.clear();
	}
	
	public void clearCmdCommand() {
		m_mapCmdCommand.clear();
	}
	
	public void clearTriggerCommand() {
		m_mapTriggerCommand.clear();
	}
	
	public void clearNamingCommand() {
		m_mapNamingCommand.clear();
	}
	
	public List<String> getMutiChartData(String strUniqueID) {
		return m_mapMutiChartDatas.get(strUniqueID);
	}
	
	public List<List<ipc_history_signal>>  getHistorySignal(String strUniqueID) {
		return m_mapHistorySignals.get(strUniqueID);
	}

	
	public void processCmdCommands(HashMap<String, stExpression> mapCmds) {

		
		Iterator<String> iter = m_mapCmdCommand.keySet().iterator();
        while (iter.hasNext()) {
        	String strKey = iter.next();  
 
        	stExpression oExpress = mapCmds.get(strKey); 
    
        	if (oExpress == null){
        		Log.e("threadShare-processCmdCommands","oExpress == null");
        		continue;
        	}
        	Iterator<String> iterValue = oExpress.mapObjectExpress.keySet().iterator();
        	while(iterValue.hasNext()) {  //fjw "if" change "while" 实现命令控制可以多绑定多绑定      	
	        	String strTempKey = iterValue.next(); 

	        	stBindingExpression bindingExpression = oExpress.mapObjectExpress.get(strTempKey); //一个数据单元绑定表达式
	        	if(bindingExpression == null)  break;
	        	List<ipc_control> lstCtrl = new ArrayList<ipc_control>();
				ipc_control ipcC = new ipc_control();
				ipcC.equipid = bindingExpression.nEquipId;
				ipcC.ctrlid = bindingExpression.nCommandId;
				ipcC.valuetype = bindingExpression.nValueType;
				ipcC.value = m_mapCmdCommand.get(strKey);//bindingExpression.strValue;
			
				lstCtrl.add(ipcC);
		
				if (0 != service.send_control_cmd(service.IP, service.PORT, lstCtrl))
				{
					String str = new String("控制失败！");
					Message msg = new Message();
					msg.what = 2;
					msg.obj = str;
					m_oInvalidateHandler.sendMessage(msg);
				}
				else
				{
					String str = new String("控制成功.");
					Message msg = new Message();
					msg.what = 1;
					msg.obj = str;
					m_oInvalidateHandler.sendMessage(msg);
				}

				ipcC = null;
				lstCtrl.clear();
        	}
		}
		clearCmdCommand();
	}
	
	public void processTriggerCommands(HashMap<String, stExpression> mapTriggers) {
		
		Iterator<String> iter = m_mapTriggerCommand.keySet().iterator();
		while (iter.hasNext())
		{
			String strKey = iter.next();
			stExpression oExpress = mapTriggers.get(strKey);
			if (oExpress == null)
				continue;

			List<ipc_cfg_trigger_value> lstCtrl = new ArrayList<ipc_cfg_trigger_value>();
			Iterator<String> iterValue = oExpress.mapObjectExpress.keySet().iterator();
			if ("Trigger".equals(oExpress.strBindType))
			{
				
				float value = Float.valueOf(m_mapTriggerCommand.get(strKey));// bindingExpression.strValue;
				while (iterValue.hasNext())
				{
					String strTempKey = iterValue.next();
					stBindingExpression bindingExpression = oExpress.mapObjectExpress.get(strTempKey);

					ipc_cfg_trigger_value ipcC = new ipc_cfg_trigger_value();
					ipcC.equipid = bindingExpression.nEquipId;
					ipcC.eventid = bindingExpression.nEventId;
					ipcC.conditionid = bindingExpression.nConditionId;
					ipcC.startvalue = value;
					ipcC.mark = 1;
					lstCtrl.add(ipcC);
					ipcC = null;
				}
			} else if ("Mask".equals(oExpress.strBindType))
			{
				
				int mask = Integer.valueOf(m_mapTriggerCommand.get(strKey));// bindingExpression.strValue;
				while (iterValue.hasNext())
				{
					String strTempKey = iterValue.next();
					stBindingExpression bindingExpression = oExpress.mapObjectExpress.get(strTempKey);

					ipc_cfg_trigger_value ipcC = new ipc_cfg_trigger_value();
					ipcC.equipid = bindingExpression.nEquipId;
					ipcC.eventid = bindingExpression.nEventId;
					ipcC.conditionid = 1;
					ipcC.enabled = mask;
					ipcC.mark = 3;
					lstCtrl.add(ipcC);
					ipcC = null;
				}
			}


			if (0 != service.set_cfg_trigger_value(service.IP, service.PORT, lstCtrl))
			{
				String str = new String("设置失败！");
				Message msg = new Message();
				msg.what = 2;
				msg.obj = str;
				m_oInvalidateHandler.sendMessage(msg);
			} else
			{
				String str = new String(strKey+"设置成功.");
				Message msg = new Message();
				msg.what = 1;
				msg.obj = str;
				m_oInvalidateHandler.sendMessage(msg);
			}

			lstCtrl.clear();
		}
        clearTriggerCommand();
	}
	
	public void processNamingCommands(HashMap<String, stExpression> mapCmds) {
		
		Iterator<String> iter = m_mapNamingCommand.keySet().iterator();
		while (iter.hasNext())
		{
			String strKey = iter.next();
			stExpression oExpress = mapCmds.get(strKey);
			if (oExpress == null)
				continue;

			List<ipc_cfg_signal_name> lstName = new ArrayList<ipc_cfg_signal_name>();
			Iterator<String> iterValue = oExpress.mapObjectExpress.keySet().iterator();
			if ("Naming".equals(oExpress.strBindType))
			{
				// 设定信号名
				String value = m_mapNamingCommand.get(strKey);// bindingExpression.strValue;
				while (iterValue.hasNext())
				{
					String strTempKey = iterValue.next();
					stBindingExpression bindingExpression = oExpress.mapObjectExpress.get(strTempKey);

					ipc_cfg_signal_name ipcC = new ipc_cfg_signal_name();
					ipcC.equipid = bindingExpression.nEquipId;
					ipcC.signalid = bindingExpression.nSignalId;
					ipcC.eventid = bindingExpression.nEventId;
					
					ipcC.name = value + (1>ipcC.signalid?"":DataGetter.getSignalDescription(ipcC.equipid, ipcC.signalid));
					if (0<ipcC.signalid && 1>ipcC.eventid)
					{
						DataGetter.setSignalName(ipcC.equipid, ipcC.signalid, ipcC.name);
					}
					else if (1>ipcC.signalid && 0<ipcC.eventid)
					{
						DataGetter.setEventName(ipcC.equipid, ipcC.eventid, ipcC.name);
					}
					else
					{
						break;
					}
					
					lstName.add(ipcC);
					ipcC = null;
				}
			} else
			{
			}
			
			if (lstName.isEmpty())
			{
				String str = new String("内部错误，设置失败！");
				Message msg = new Message();
				msg.what = 2;
				msg.obj = str;
				m_oInvalidateHandler.sendMessage(msg);
				continue;
			}
			
			if (0 != service.set_cfg_signal_name(service.IP, service.PORT, lstName) 
					|| lstName.size() != oExpress.mapObjectExpress.size())
			{
				String str = new String("设置失败！");
				Message msg = new Message();
				msg.what = 2;
				msg.obj = str;
				m_oInvalidateHandler.sendMessage(msg);
			} else
			{
				String str = new String("设置成功.");
				Message msg = new Message();
				msg.what = 1;
				msg.obj = str;
				m_oInvalidateHandler.sendMessage(msg);
			}

			lstName.clear();
		}
		
		clearNamingCommand();
	}
	
	boolean m_bIsReaded = true;
	public  HashMap<String,stBindingExpression> m_SgIsolationEventSetter=null;  //存取ui id 和 双图片控件表达式
	public HashMap<String, String> m_mapCmdCommand = null;
	public HashMap<String, String> m_mapTriggerCommand = null;
	public HashMap<String, String> m_mapNamingCommand = null;
	public List<IObject> m_listUpdateFromTcpValues = null;
	public HashMap<String, SgRealTimeData> m_mapRealTimeDatas = null;
	public HashMap<String, List<String>> m_mapMutiChartDatas = null;  // <IObject_UID, List<Value>>
	public HashMap<String, Hashtable<String, Hashtable<String, Event>> > m_mapEventListDatas = null; // 按设备ID读取告警列表
	public HashMap<String, Hashtable<String, Signal>> m_mapSignalListDatas = null; // 按设备ID获取实时信号列表
	public HashMap<String, List<List<ipc_history_signal>>> m_mapHistorySignals = null; // 历史曲线
	public Handler m_oInvalidateHandler = null;
	public HashMap<String, List<ipc_history_signal>> m_mapHistorySignal = null;
	public HashMap<String, List<local_his_signal>> m_mapLocalSignal = null; //本地保存的历史数据
	public HashMap<String, List<local_his_signal>> m_mapSaveSignal = null; //本地保存的历史数据 = null; //本地保存的信号历史数据
	public HashMap<String, List<local_his_signal>> m_mapSaveEquipt = null; //本地保存的历史数据 = null; //本地保存的设备历史数据
	public HashMap<String,save_curve_signal> m_mapHisPoint = null;
	public HashMap<String,save_multipoint_signal> m_mapMultiPoint = null;
	public HashMap<String,List<local_his_event>> m_mapLocalEvent = null; //本地保存的告警数据
}
