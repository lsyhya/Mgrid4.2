package com.mgrid.data;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;

import com.mgrid.data.EquipmentDataModel.CommandCfg;
import com.mgrid.data.EquipmentDataModel.CommandCfg.CmdParameaningCfg;
import com.mgrid.data.EquipmentDataModel.Equipment;
import com.mgrid.data.EquipmentDataModel.Event;
import com.mgrid.data.EquipmentDataModel.EventCfg;
import com.mgrid.data.EquipmentDataModel.EventConditionCfg;
import com.mgrid.data.EquipmentDataModel.Signal;
import com.mgrid.main.MGridActivity;
import com.sg.common.IObject;
import com.sg.uis.SaveEquipt;
import com.sg.uis.SaveSignal;
import comm_service.local_file;
import comm_service.local_rc;
import comm_service.msg_head;
import comm_service.msg_type;
import comm_service.protocol;
import comm_service.service;

import data_model.ipc_active_event;
import data_model.ipc_cfg_control;
import data_model.ipc_cfg_ctrl_parameaning;
import data_model.ipc_cfg_event;
import data_model.ipc_cfg_signal;
import data_model.ipc_cfg_trigger_value;
import data_model.ipc_equipment;
import data_model.local_his_signal;
import data_model.save_curve_signal;
import data_model.save_multipoint_signal;

@SuppressWarnings("unused")
@SuppressLint("SimpleDateFormat")
public class DataGetter extends Thread {
	public DataGetter() {
		// fjw add
		// flag1_map = new HashMap<String,Long>();
		// flag2_map = new HashMap<String,Long>();
		flag3_map = new HashMap<String, Long>();
		flag4_map = new HashMap<String, Long>();
		trigger_list = new HashMap<String, List<ipc_cfg_trigger_value>>();

		if (equipment.myThread.isAlive()) {

		} else {
			equipment.myThread.start();
		}
	}

	public void run() {
		// System.out.println("DataGetterXXX:Run开始运行"+Thread.currentThread().getName());
		if (null == equipment.rtalarm_req) {
			equipment.rtalarm_req = protocol
					.build_query_all_active_alarm_list();
		}
		// TODO: 初始化告警配置信息，IPC现无法获取完整告警配置信息。

		while (true) {

			List<ipc_equipment> equip_list = service.get_equipment_list(
					service.IP, service.PORT);

			if (equip_list.isEmpty()) {
				try {
					sleep(2000);// sleep(2000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				continue;
			}

			Iterator<ipc_equipment> it = equip_list.iterator();
			while (it.hasNext()) {
				ipc_equipment equip = it.next();
				equipment.htEquipments
						.put(String.valueOf(equip.id), equip.name);

				Equipment equipobj = equipment.htEquipmentData.get(String
						.valueOf(equip.id));
				if (null == equipobj) {
					equipobj = equipment.new Equipment();
					equipment.htEquipmentData.put(String.valueOf(equip.id),
							equipobj);
				}

				equipobj.m_equipid = String.valueOf(equip.id);
				equipobj.m_tempid = String.valueOf(equip.templateId);
				equipobj.m_category = String.valueOf(equip.category);
				equipobj.m_name = equip.name;
				equipobj.m_xmlfile = equip.xmlname;
				equipobj.signal_req = protocol
						.build_query_signal_rt_list(equip.id);

				Hashtable<String, EventCfg> equip_event_cfg = new Hashtable<String, EventCfg>();

				List<ipc_cfg_event> event_cfg_list = service
						.get_event_cfg_list(service.IP, service.PORT, equip.id);
				Iterator<ipc_cfg_event> event_cfg_it = event_cfg_list
						.iterator();
				while (event_cfg_it.hasNext()) {
					EventCfg eventcfg = equipment.new EventCfg();
					ipc_cfg_event ipc_evtcfg = event_cfg_it.next();

					eventcfg.eventid = ipc_evtcfg.id;
					eventcfg.eventname = ipc_evtcfg.name;

					equip_event_cfg
							.put(String.valueOf(ipc_evtcfg.id), eventcfg);
					equipobj.htEventCfg.put(String.valueOf(ipc_evtcfg.id),
							eventcfg);
				}

				equipment.htEventCfg.put(String.valueOf(equip.id),
						equip_event_cfg);

				List<ipc_cfg_trigger_value> trigger_value_list = service
						.get_cfg_trigger_value(service.IP, service.PORT,
								equip.id);
				trigger_list.put(equip.id + "", trigger_value_list);
				// System.out.println("消息已传递，设备id是：" + equip.id);
				Iterator<ipc_cfg_trigger_value> trigger_value_it = trigger_value_list
						.iterator();
				EventCfg evtcfg = null;
				while (trigger_value_it.hasNext()) {
					ipc_cfg_trigger_value ipc_trigger = trigger_value_it.next();

					evtcfg = equip_event_cfg.get(String
							.valueOf(ipc_trigger.eventid));
					if (null == evtcfg)
						continue;
					evtcfg.enabled = ipc_trigger.enabled;

					EventConditionCfg condcfg = evtcfg.htConditions.get(String
							.valueOf(ipc_trigger.conditionid));
					if (null == condcfg) {
						condcfg = equipment.new EventConditionCfg();
						evtcfg.htConditions.put(
								String.valueOf(ipc_trigger.conditionid),
								condcfg);

						condcfg.conditionid = ipc_trigger.conditionid;
					}

					condcfg.severity = ipc_trigger.eventseverity;
					condcfg.startcompare = String
							.valueOf(ipc_trigger.startvalue);
					condcfg.endcompare = String.valueOf(ipc_trigger.stopvalue);
					condcfg.severity = ipc_trigger.eventseverity;

					EventCfg equipEvtCfg = equipobj.htEventCfg.get(String
							.valueOf(ipc_trigger.eventid));
					if (null == equipEvtCfg)
						continue;
					equipEvtCfg.htConditions.put(
							String.valueOf(ipc_trigger.conditionid), condcfg);
				}

				List<ipc_cfg_signal> signal_cfg = service.get_signal_cfg_list(
						service.IP, service.PORT, equip.id);

				Iterator<ipc_cfg_signal> signal_cfg_it = signal_cfg.iterator();
				while (signal_cfg_it.hasNext()) {
					ipc_cfg_signal ipc_signalcfg = signal_cfg_it.next();
					Signal signal = equipobj.htSignalData.get(String
							.valueOf(ipc_signalcfg.id));

					if (null == signal) {
						signal = equipment.new Signal();
						equipobj.htSignalData.put(
								String.valueOf(ipc_signalcfg.id), signal);

					}

					signal.id = String.valueOf(ipc_signalcfg.id);
					signal.name = ipc_signalcfg.name;
					signal.unit = ipc_signalcfg.unit;
					signal.precision = ipc_signalcfg.precision;
					signal.description = ipc_signalcfg.description;
				}

				List<ipc_cfg_control> command_cfg = service
						.get_control_cfg_list(service.IP, service.PORT,
								equip.id);
				Iterator<ipc_cfg_control> command_cfg_it = command_cfg
						.iterator();
				while (command_cfg_it.hasNext()) {
					ipc_cfg_control ipc_commandcfg = command_cfg_it.next();
					CommandCfg command = equipobj.htCmdCfg.get(String
							.valueOf(ipc_commandcfg.id));
					if (null == command) {
						command = equipment.new CommandCfg();
						equipobj.htCmdCfg.put(
								String.valueOf(ipc_commandcfg.id), command);
					}

					command.cmdid = ipc_commandcfg.id;
					command.name = ipc_commandcfg.name;
					command.unit = ipc_commandcfg.unit;
					command.fMaxValue = ipc_commandcfg.fMaxValue;
					command.fMinValue = ipc_commandcfg.fMinValue;
				}

				List<ipc_cfg_ctrl_parameaning> cmd_parameaning_cfg = service
						.get_control_parameaning_cfg_list(service.IP,
								service.PORT, equip.id);
				Iterator<ipc_cfg_ctrl_parameaning> cmd_parameaning_it = cmd_parameaning_cfg
						.iterator();
				while (cmd_parameaning_it.hasNext()) {
					ipc_cfg_ctrl_parameaning ipc_cmdparamcfg = cmd_parameaning_it
							.next();
					CommandCfg command = equipobj.htCmdCfg.get(String
							.valueOf(ipc_cmdparamcfg.ctrlid));
					if (null == command) {
						command = equipment.new CommandCfg();
						equipobj.htCmdCfg
								.put(String.valueOf(ipc_cmdparamcfg.ctrlid),
										command);
					}

					CmdParameaningCfg cmdparam = command.new CmdParameaningCfg();
					cmdparam.value = ipc_cmdparamcfg.paramvalue;
					cmdparam.meaning = ipc_cmdparamcfg.parameaning;

					command.meanings.add(cmdparam);
				}

			} // end of while equipment.hasnext

			equipment.bEquipments = true;
			equipment.bEventCfg = true;
			isLoading = false;
			break;
		}

		Equipment currEquipObj = null;

		Iterator<String> equipNameIt = equipment.hsEquipSet.iterator();
		while (equipNameIt.hasNext()) {
			if (equipment.htEquipmentData == null)
				continue;

			try {
				String key = equipNameIt.next();
				if ("".equals(key) && key == null)
					continue;
				currEquipObj = equipment.htEquipmentData.get(key);
			} catch (Exception e) {

				break;
			}

			if (null == currEquipObj)
				continue;

			proc_rtsignal(currEquipObj);
		}

		new Thread(new Runnable() {

			@Override
			public void run() {

				Iterator<String> equipNamePage = null;// 当前页面的设备id
				Equipment pageEquipObj = null;// 当前页面设备

				while (true) {
					HashSet<String> equipSet = equipment.htPageEquipSet
							.get(currentPage);
					if (null == equipSet)
						continue;
					// fix java.util.ConcurrentModificationException
					if (bIsLoading)
						equipNamePage = new CopiedIterator<String>(equipSet
								.iterator());
					else
						equipNamePage = equipSet.iterator();

					// System.out.println("循环1的次数"+equipSet.size());
					while (equipNamePage.hasNext()) {

						pageEquipObj = equipment.htEquipmentData
								.get(equipNamePage.next());
						if (null == pageEquipObj)
							continue;
						// System.out.println("我是XXX线程1 "+pageEquipObj.m_equipid);
						proc_rtsignal(pageEquipObj);
						try {
							sleep(500);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						pageEquipObj.lUpdateTime = java.lang.System
								.currentTimeMillis();

					}
					try {
						sleep(500);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

					equipSet = null;
					equipNamePage = null;
					pageEquipObj = null;
				}
			}
		}).start();

		// int flag_times = 0;
		while (true) {

			// 1注释：
			// System.out.println("我是DataGetter线程");
			try {
				sleep(5); // 保护sleep
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}

			proc_allrtalarm();
			try {
				sleep(100); // sleep(100);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}

			try {

				// System.out.println("我是XXX线程1");
				// 摔性能一些数据空间 比如说sigleList...
				Set<String> autoEquipList = equipment.autoAnyPapeRefreshEquip;
				if (autoEquipList == null)
					;
				else {
					Iterator<String> iter = autoEquipList.iterator();
					// System.out.println("次数：" + autoEquipList.size());
					while (iter.hasNext()) {

						String str_equip = iter.next();
						if ("".equals(str_equip))
							break;
						currEquipObj = equipment.htEquipmentData.get(str_equip);
						if (currEquipObj == null)
							break;

						// System.out.println("名字："+currEquipObj.m_name+"  id:"+str_equip);
						proc_autosignal(currEquipObj);
						sleep(500);// sleep(500);
					}

				}

				autoEquipList = null;

			} catch (Exception e) {

			}

			try {
				sleep(500);
			} catch (Exception e) {
				// TODO: handle exception
			}
			// fjw add end

			// System.out.println("我是XXX线程3");
			// fix java.util.ConcurrentModificationException

			// 一些表达式为value的设备 比如Sglabel控件
			if (bIsLoading)
				equipNameIt = new CopiedIterator<String>(
						equipment.hsEquipSet.iterator());
			else
				equipNameIt = equipment.hsEquipSet.iterator();
			while (equipNameIt.hasNext()) {
				// try
				// {

				currEquipObj = equipment.htEquipmentData

				.get(equipNameIt.next());
				if (null == currEquipObj)
					continue;
				long l = java.lang.System.currentTimeMillis()
						- currEquipObj.lUpdateTime;
				if (l < 30000)
					continue;
				proc_rtsignal(currEquipObj);
				try {
					sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				currEquipObj.lUpdateTime = java.lang.System.currentTimeMillis();
			}

			equipNameIt = null;

			try {
				sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// 垃圾回收
			// System.gc();
		} // end of master while(true)
	} // end of run()

	/**
	 * 获取设备信号集，线程安全。
	 * 
	 * @param String
	 *            设备实例ID
	 * @return 成功返回实例信号集Hashtable，否则返回null。
	 * @throws
	 */
	public static Hashtable<String, Signal> getEquipSignalList(
			String equipmentid) {
		EquipmentDataModel.Equipment equip = equipment.htEquipmentData
				.get(equipmentid);

		if (null == equip)
			return null;

		return equip.htSignalData;
	}

	/**
	 * Adapter getEquipSignalList
	 * 
	 * @param int 设备实例ID
	 * @return 成功返回实例信号集Hashtable，否则返回null。
	 * @throws
	 */
	public static Hashtable<String, Signal> getEquipSignalList(int equipmentid) {
		return getEquipSignalList(String.valueOf(equipmentid));
	}

	/**
	 * 获取 全局实时告警列表，线程安全。
	 * 
	 * @param
	 * @return 全局告警信息Hashtable。
	 * @throws
	 */
	public static Hashtable<String, Hashtable<String, Event>> getRTEventList() {
		return equipment.htEventData;
	}

	/**
	 * 获取 设备实时告警列表，线程安全。
	 * 
	 * @param
	 * @return 设备设备告警信息Hashtable。
	 * @throws
	 */
	public static Hashtable<String, Hashtable<String, Event>> getEquipRTEventList(
			String equipid) {
		// TODO: 尚未完成
		Hashtable<String, Hashtable<String, Event>> hashtable = new Hashtable<String, Hashtable<String, Event>>();

		hashtable.put(equipid, equipment.htEventData.get(equipid));
		return hashtable;
	}

	/**
	 * Adapter of 获取 设备实时告警列表，线程安全。
	 * 
	 * @param
	 * @return 设备告警信息Hashtable。
	 * @throws
	 */
	public static Hashtable<String, Hashtable<String, Event>> getEquipRTEventList(
			int nEquipId) {
		return getEquipRTEventList(String.valueOf(nEquipId));
	}

	/**
	 * 获取信号名， 线程安全。
	 * 
	 * @param String
	 *            设备实例ID
	 * @param String
	 *            信号ID
	 * @return String 信号名
	 * @throws
	 */
	public static String getSignalName(String equipmentid, String signalid) {
		EquipmentDataModel.Equipment equip = equipment.htEquipmentData
				.get(equipmentid);

		if (null == equip)
			return "";

		Signal signal = equip.htSignalData.get(signalid);

		return (null == signal ? "" : signal.name);
	}

	/**
	 * Adapter getSignalName
	 * 
	 * @param int 设备实例ID
	 * @param int 信号ID
	 * @return String 信号名
	 * @throws
	 */
	public static String getSignalName(int equipmentid, int signalid) {
		return getSignalName(String.valueOf(equipmentid),
				String.valueOf(signalid));
	}

	/**
	 * 设置信号名， 线程安全。
	 * 
	 * @param String
	 *            设备实例ID
	 * @param String
	 *            信号ID
	 * @param String
	 *            信号名
	 * @return boolean true为成功
	 * @throws
	 */
	public static boolean setSignalName(String equipmentid, String signalid,
			String signalname) {
		EquipmentDataModel.Equipment equip = equipment.htEquipmentData
				.get(equipmentid);

		if (null == equip)
			return false;

		Signal signal = equip.htSignalData.get(signalid);

		if (null == signal)
			return false;
		else
			signal.name = new String(signalname);

		// 通知名称有更新
		Iterator<IObject> regobj_it = signal.registedNameObj.iterator();
		while (regobj_it.hasNext()) {
			regobj_it.next().needupdate(true); // 数据有更新通知控件更新
		}

		return true;
	}

	/**
	 * Adapter setSignalName
	 * 
	 * @param int 设备实例ID
	 * @param int 信号ID
	 * @param String
	 *            信号名
	 * @return boolean true为成功
	 * @throws
	 */
	public static boolean setSignalName(int equipmentid, int signalid,
			String signalname) {
		return setSignalName(String.valueOf(equipmentid),
				String.valueOf(signalid), signalname);
	}

	/**
	 * 获取信号值， 线程安全。
	 * 
	 * @param String
	 *            设备实例ID
	 * @param String
	 *            信号ID
	 * @return String 信号值
	 * @throws
	 */
	public static String getSignalValue(String equipmentid, String signalid) {
		EquipmentDataModel.Equipment equip = equipment.htEquipmentData
				.get(equipmentid);

		if (null == equip)
			return "";

		Signal signal = equip.htSignalData.get(signalid);

		return (null == signal ? "" : signal.value);
	}

	/**
	 * Adapter getSignalValue
	 * 
	 * @param int 设备实例ID
	 * @param int 信号ID
	 * @return String 信号值
	 * @throws
	 */
	public static String getSignalValue(int equipmentid, int signalid) {
		return getSignalValue(String.valueOf(equipmentid),
				String.valueOf(signalid));
	}

	/**
	 * 获取信号含义， 线程安全。
	 * 
	 * @param String
	 *            设备实例ID
	 * @param String
	 *            信号ID
	 * @return String 信号含义
	 * @throws
	 */
	public static String getSignalMeaning(String equipmentid, String signalid) {
		EquipmentDataModel.Equipment equip = equipment.htEquipmentData
				.get(equipmentid);

		if (null == equip)
			return "";

		Signal signal = equip.htSignalData.get(signalid);

		return (null == signal ? "" : signal.meaning);
	}

	/**
	 * Adapter getSignalMeaning
	 * 
	 * @param int 设备实例ID
	 * @param int 信号ID
	 * @return String 信号含义
	 * @throws
	 */
	public static String getSignalMeaning(int equipmentid, int signalid) {
		return getSignalMeaning(String.valueOf(equipmentid),
				String.valueOf(signalid));
	}

	// fjw add
	/**
	 * 获取信号值， 线程安全。
	 * 
	 * @param String
	 *            设备实例ID
	 * @param String
	 *            信号ID
	 * @return String 信号值
	 * @throws
	 */
	public static String getSignalValueAndTime(String equipmentid,
			String signalid) {
		EquipmentDataModel.Equipment equip = equipment.htEquipmentData
				.get(equipmentid);

		if (null == equip)
			return "";

		Signal signal = equip.htSignalData.get(signalid);

		String valueandtime = signal.value + "`" + signal.freshtime;

		return (null == signal ? "" : valueandtime);
	}

	/**
	 * Adapter getSignalValueAndTime
	 * 
	 * @param int 设备实例ID
	 * @param int 信号ID
	 * @return String 信号值
	 * @throws
	 */
	public static String getSignalValueAndTime(int equipmentid, int signalid) {
		return getSignalValue(String.valueOf(equipmentid),
				String.valueOf(signalid));
	}

	// fjw add end

	/**
	 * 获取信号描述， 线程安全。
	 * 
	 * @param String
	 *            设备实例ID
	 * @param String
	 *            信号ID
	 * @return String 信号描述
	 * @throws
	 */
	public static String getSignalDescription(String equipmentid,
			String signalid) {
		EquipmentDataModel.Equipment equip = equipment.htEquipmentData
				.get(equipmentid);

		if (null == equip)
			return "";

		Signal signal = equip.htSignalData.get(signalid);

		return (null == signal ? "" : signal.description);
	}

	/**
	 * Adapter getSignalDescription
	 * 
	 * @param int 设备实例ID
	 * @param int 信号ID
	 * @return String 信号描述
	 * @throws
	 */
	public static String getSignalDescription(int equipmentid, int signalid) {
		return getSignalDescription(String.valueOf(equipmentid),
				String.valueOf(signalid));
	}

	/**
	 * 获取信号告警级别， 线程安全。
	 * 
	 * @param String
	 *            设备实例ID
	 * @param String
	 *            信号ID
	 * @return int 告警级别
	 * @throws
	 */
	public static int getSignalSeverity(String equipmentid, String signalid) {
		EquipmentDataModel.Equipment equip = equipment.htEquipmentData
				.get(equipmentid);

		if (null == equip)
			return 0;

		Signal signal = equip.htSignalData.get(signalid);

		return (null == signal ? 0 : signal.severity);
	}

	/**
	 * Adapter getSignalSeverity
	 * 
	 * @param int 设备实例ID
	 * @param int 信号ID
	 * @return int 告警级别
	 * @throws
	 */
	public static int getSignalSeverity(int equipmentid, int signalid) {
		return getSignalSeverity(String.valueOf(equipmentid),
				String.valueOf(signalid));
	}

	/**
	 * 获取控制参数含义集合。
	 * 
	 * @param String
	 *            设备实例ID
	 * @param String
	 *            告警ID
	 * @return List<CmdParameaningCfg> 告警参数配置信息集
	 * @throws
	 */
	public static List<CmdParameaningCfg> getCtrlParameaning(
			String equipmentid, String commandid) {
		EquipmentDataModel.Equipment equip = equipment.htEquipmentData
				.get(equipmentid);

		if (null == equip)
			return null;

		CommandCfg commandcfg = equip.htCmdCfg.get(commandid);

		return (null == commandcfg ? null : commandcfg.meanings);
	}

	/**
	 * Adapter getSignalSeverity
	 * 
	 * @param int 设备实例ID
	 * @param int 告警ID
	 * @return List<CmdParameaningCfg> 告警参数配置信息集
	 * @throws
	 */
	public static List<CmdParameaningCfg> getCtrlParameaning(int equipmentid,
			int commandid) {
		return getCtrlParameaning(String.valueOf(equipmentid),
				String.valueOf(commandid));
	}

	/**
	 * 获取设备id列表
	 * 
	 */
	public static HashSet<String> getEquipmentIdList() {
		return equipment.hsEquipSet;
	}

	/**
	 * 获取设备名称，若不存在则返回空字符串。
	 * 
	 * @param String
	 *            设备实例ID
	 * @return String 设备名称
	 * @throws
	 */
	public static String getEquipmentName(String equipmentid) {
		String equipname = equipment.htEquipments.get(equipmentid);
		return (null == equipname ? "" : equipname);
	}

	/**
	 * Adapter getEquipmentName
	 * 
	 * @param int 设备实例ID
	 * @return String 设备名称
	 * @throws
	 */
	public static String getEquipmentName(int equipmentid) {
		return getEquipmentName(String.valueOf(equipmentid));
	}

	/**
	 * 获取告警名称
	 * 
	 * @param String
	 *            设备实例ID
	 * @param String
	 *            告警ID
	 * @return String 告警名称
	 * @throws
	 */
	public static String getEventName(String equipmentid, String eventid) {
		Hashtable<String, EventCfg> equip_cfg = equipment.htEventCfg
				.get(equipmentid);
		if (null == equip_cfg)
			return "";

		EventCfg eventcfg = equip_cfg.get(eventid);
		if (null == eventcfg)
			return "";

		return eventcfg.eventname;
	}

	/**
	 * Adapter getEventName
	 * 
	 * @param int 设备实例ID
	 * @param int 告警ID
	 * @return String 告警名称
	 * @throws
	 */
	public static String getEventName(int equipmentid, int eventid) {
		return getEventName(String.valueOf(equipmentid),
				String.valueOf(eventid));
	}

	/**
	 * 获取设备通信状态 告警值
	 * 
	 * @param String
	 *            设备实例ID
	 * @param String
	 *            告警ID
	 * @return String 告警值
	 * @throws
	 */
	public static String getEquipState(String equipmentid, String eventid) {
		if (equipmentid == null)
			return null;
		Hashtable<String, Event> equip_date = equipment.htEventData
				.get(equipmentid);
		if (null == equip_date)
			return "0"; // 0 打勾通信正常状态

		Event eventdate = equip_date.get(eventid);
		if (null == eventdate)
			return "0"; // 0 打勾通信正常状态

		return eventdate.state;
	}

	/**
	 * Adapter getEventName
	 * 
	 * @param int 设备实例ID
	 * @param int 告警ID
	 * @return String 告警值
	 * @throws
	 */
	public static String getEquipState(int equipmentid, int eventid) {
		return getEquipState(String.valueOf(equipmentid),
				String.valueOf(eventid));
	}

	/**
	 * 设置告警名称
	 * 
	 * @param String
	 *            设备实例ID
	 * @param String
	 *            告警ID
	 * @param String
	 *            告警名称
	 * @return boolean true为成功
	 * @throws
	 */
	public static boolean setEventName(String equipmentid, String eventid,
			String eventname) {
		Hashtable<String, EventCfg> equip_cfg = equipment.htEventCfg
				.get(equipmentid);
		if (null == equip_cfg)
			return false;

		EventCfg eventcfg = equip_cfg.get(eventid);
		if (null == eventcfg)
			return false;

		eventcfg.eventname = new String(eventname);

		// 通知名称有更新
		Iterator<IObject> regobj_it = eventcfg.registedNameObj.iterator();
		while (regobj_it.hasNext()) {
			regobj_it.next().needupdate(true);
		}

		return true;
	}

	/**
	 * 获取告警条件开始比较阈值
	 * 
	 * @param String
	 *            设备实例ID
	 * @param String
	 *            告警ID
	 * @param String
	 *            条件ID
	 * @return String 起始阈值
	 * @throws
	 */
	public static String getStartCmpValue(String equipmentid, String eventid,
			String conditionId) {
		Hashtable<String, EventCfg> equip_cfg = equipment.htEventCfg
				.get(equipmentid);
		if (null == equip_cfg)
			return "";

		EventCfg eventcfg = equip_cfg.get(eventid);
		if (null == eventcfg)
			return "";

		EventConditionCfg condcfg = eventcfg.htConditions.get(conditionId);
		if (null == condcfg)
			return "";

		return condcfg.startcompare;
	}

	/**
	 * Adapter setEventName
	 * 
	 * @param int 设备实例ID
	 * @param int 告警ID
	 * @param String
	 *            告警名称
	 * @return boolean true为成功
	 * @throws
	 */
	public static boolean setEventName(int equipmentid, int eventid,
			String eventname) {
		return setEventName(String.valueOf(equipmentid),
				String.valueOf(eventid), eventname);
	}

	/**
	 * Adapter getStartCmpValue
	 * 
	 * @param int 设备实例ID
	 * @param int 告警ID
	 * @param int 条件ID
	 * @return String 起始阈值
	 * @throws
	 */
	public static String getStartCmpValue(int equipmentid, int eventid,
			int conditionId) {
		return getStartCmpValue(String.valueOf(equipmentid),
				String.valueOf(eventid), String.valueOf(conditionId));
	}

	/**
	 * 获取告警使能状态
	 * 
	 * @param String
	 *            设备实例ID
	 * @param String
	 *            告警ID
	 * @return String 使能状态, 未找到告警配置项则返回空字符串。
	 * @throws
	 */
	public static String getEventState(String equipmentid, String eventid) {
		Hashtable<String, EventCfg> equip_cfg = equipment.htEventCfg
				.get(equipmentid);
		if (null == equip_cfg)
			return "";

		EventCfg eventcfg = equip_cfg.get(eventid);
		if (null == eventcfg)
			return "";

		return String.valueOf(eventcfg.enabled);
	}

	/**
	 * Adapter getEventState
	 * 
	 * @param int 设备实例ID
	 * @param int 告警ID
	 * @return String 使能状态, 未找到告警配置项则返回空字符串。
	 * @throws
	 */
	public static String getEventState(int equipmentid, int eventid) {
		return getEventState(String.valueOf(equipmentid),
				String.valueOf(eventid));
	}

	/**
	 * 设定告警使能状态
	 * 
	 * @param String
	 *            设备实例ID
	 * @param String
	 *            告警ID
	 * @param String
	 *            使能状态， 1 告警， 0 不告警。
	 * @return boolean 设定成功返回 true, 未找到相关告警配置返回 false.
	 * @throws
	 */
	public static boolean setEventState(String equipmentid, String eventid,
			String enable) {
		Hashtable<String, EventCfg> equip_cfg = equipment.htEventCfg
				.get(equipmentid);
		if (null == equip_cfg)
			return false;

		EventCfg eventcfg = equip_cfg.get(eventid);
		if (null == eventcfg)
			return false;

		eventcfg.enabled = "1".equals(enable) ? 1 : 0;

		return true;
	}

	/**
	 * Adapter setEventState
	 * 
	 * @param int 设备实例ID
	 * @param int 告警ID
	 * @param int 使能状态， 1 告警， 0 不告警。
	 * @return boolean 设定成功返回 true, 未找到相关告警配置返回 false.
	 * @throws
	 */
	public static boolean setEventState(int equipmentid, int eventid, int enable) {
		return setEventState(String.valueOf(equipmentid),
				String.valueOf(eventid), String.valueOf(enable));
	}

	// fjw add
	public static save_curve_signal getCurveSignal(String equipmentid,
			String signalid, String objId) {
		if (equipment.htEquipmentData == null)
			return null;
		if (equipment.htEquipmentData.get(equipmentid).h_ESigId_curveList == null)
			return null;
		if (equipment.htEquipmentData.get(equipmentid).h_ESigId_curveList
				.get(objId) == null)
			return null;

		return equipment.htEquipmentData.get(equipmentid).h_ESigId_curveList
				.get(objId).get(signalid);
	}

	public static save_curve_signal getCurveSignal(int equipmentid,
			int signalid, String objId) {
		return getCurveSignal(String.valueOf(equipmentid),
				String.valueOf(signalid), objId);
	}

	// fjw add
	public static save_multipoint_signal getCurvesSignal(String equipmentid,
			String signalid, String objId) {
		if (equipment.htEquipmentData == null)
			return null;
		if (equipment.htEquipmentData.get(equipmentid).h_ESigId_curvesList == null)
			return null;
		if (equipment.htEquipmentData.get(equipmentid).h_ESigId_curvesList
				.get(objId) == null)
			return null;

		return equipment.htEquipmentData.get(equipmentid).h_ESigId_curvesList
				.get(objId).get(signalid);
	}

	public static save_multipoint_signal getCurvesSignal(int equipmentid,
			int signalid, String objId) {
		return getCurvesSignal(String.valueOf(equipmentid),
				String.valueOf(signalid), objId);
	}

	/**
	 * 登记到信号名称， Attention： 非线程安全
	 * 
	 * @param String
	 *            设备实例ID
	 * @param String
	 *            信号ID
	 * @param String
	 *            页面名称
	 * @param IObject
	 * @return boolean true成功，false失败。
	 * @throws
	 */
	public static boolean regSignalName(String equipmentid, String signalid,
			String pagename, IObject object) {
		if (null == equipmentid || null == signalid || null == pagename)
			return false;

		equipment.hsEquipSet.add(equipmentid); // SET 会解决重复问题

		HashSet<String> equipset = equipment.htPageEquipSet.get(pagename);

		if (null == equipset) {
			equipset = new HashSet<String>();
			equipment.htPageEquipSet.put(pagename, equipset);
		}

		equipset.add(equipmentid);

		EquipmentDataModel.Equipment equip = equipment.htEquipmentData
				.get(equipmentid);

		if (null == equip) {
			equip = equipment.new Equipment();
			equip.m_equipid = equipmentid;

			equip.signal_req = protocol.build_query_signal_list(Integer
					.parseInt(equipmentid));

			equipment.htEquipmentData.put(equipmentid, equip);
		}

		boolean bPutSignal = false;
		Signal signel = equip.htSignalData.get(signalid);

		if (null == signel) {
			bPutSignal = true;
			signel = equipment.new Signal();
		}

		if (null != object) {
			// TODO: 处理可能出现的特殊情况下的重复添加。
			signel.registedNameObj.add(object);
		}

		if (bPutSignal)
			equip.htSignalData.put(signalid, signel);

		return true;
	} /* end of regSignalName */

	/**
	 * Adapter登记到信号名称， Attention： 非线程安全
	 * 
	 * @param int 设备实例ID
	 * @param int 信号ID
	 * @param String
	 *            页面名称
	 * @param IObject
	 * @return boolean true成功，false失败。
	 * @throws
	 */
	public static boolean regSignalName(int nEquipId, int nSignalId,
			String pagename, IObject object) {
		return regSignalName(String.valueOf(nEquipId),
				String.valueOf(nSignalId), pagename, object);

	}

	/**
	 * 登记信号， Attention： 非线程安全
	 * 
	 * @param String
	 *            设备实例ID
	 * @param String
	 *            信号ID
	 * @param String
	 *            页面名称
	 * @param IObject
	 * @return boolean true成功，false失败。
	 * @throws
	 */
	public static boolean setSignal(String equipmentid, String signalid,
			String pagename, IObject object) {
		if (null == equipmentid || null == signalid || null == pagename)
			return false;

		equipment.hsEquipSet.add(equipmentid); // SET 会解决重复问题

		HashSet<String> equipset = equipment.htPageEquipSet.get(pagename);

		if (null == equipset) {
			equipset = new HashSet<String>();
			equipment.htPageEquipSet.put(pagename, equipset);
		}

		equipset.add(equipmentid);

		EquipmentDataModel.Equipment equip = equipment.htEquipmentData
				.get(equipmentid);

		if (null == equip) {
			equip = equipment.new Equipment();
			equip.m_equipid = equipmentid;

			equip.signal_req = protocol.build_query_signal_list(Integer
					.parseInt(equipmentid));

			equipment.htEquipmentData.put(equipmentid, equip);
		}

		boolean bPutSignal = false;
		Signal signel = equip.htSignalData.get(signalid);

		if (null == signel) {
			bPutSignal = true;
			signel = equipment.new Signal();
		}

		if (null != object) {

			// fjw add

			if ("SignalCurve".equals(object.getType())) {
				equipment.autoAnyPapeRefreshEquip.add(equipmentid);
				equipment.htEquipmentData.get(equipmentid).registedBackgroundListObj
						.put(object.getUniqueID(), object);
				equipment.htEquipmentData.get(equipmentid).registedObjID_SignalID
						.put(object.getUniqueID(), signalid);
				//
			}
			if ("SignalCurves".equals(object.getType())) {
				equipment.autoAnyPapeRefreshEquip.add(equipmentid);
				equipment.htEquipmentData.get(equipmentid).registedBackgroundListObj
						.put(object.getUniqueID(), object);
				equipment.htEquipmentData.get(equipmentid).registedObjID_SignalID
						.put(object.getUniqueID(), signalid);

			}
			if ("SaveSignal".equals(object.getType())) {
				equipment.autoAnyPapeRefreshEquip.add(equipmentid);
				equipment.htEquipmentData.get(equipmentid).registedBackgroundListObj
						.put(object.getUniqueID(), object);
				equipment.htEquipmentData.get(equipmentid).registedSignalID
						.add(signalid);

			}
			if ("AutoSig".equals(object.getType())
					|| "AutoSigList".equals(object.getType())) {
				equipment.autoAnyPapeRefreshEquip.add(equipmentid);
				equipment.htEquipmentData.get(equipmentid).registedBackgroundListObj
						.put(object.getUniqueID(), object);

			}
			if ("RC_Label".equals(object.getType())) {
				equipment.autoAnyPapeRefreshEquip.add(equipmentid);
				equipment.htEquipmentData.get(equipmentid).registedBackgroundListObj
						.put(object.getUniqueID(), object);
				equipment.htEquipmentData.get(equipmentid).registedRCSignalID
						.add(signalid);
				// Log.e("RC_Label","RC_Label注册done!");
			} else {
				// TODO: 处理可能出现的特殊情况下的重复添加。
				signel.registedObj.add(object);
			}
			// fjw add end
		}

		if (bPutSignal)
			equip.htSignalData.put(signalid, signel);

		return true;
	} /* end of setSignal */

	/**
	 * Adapter登记信号， Attention： 非线程安全
	 * 
	 * @param int 设备实例ID
	 * @param int 信号ID
	 * @param String
	 *            页面名称
	 * @param IObject
	 * @return boolean true成功，false失败。
	 * @throws
	 */
	public static boolean setSignal(int nEquipId, int nSignalId,
			String pagename, IObject object) {
		return setSignal(String.valueOf(nEquipId), String.valueOf(nSignalId),
				pagename, object);

	}

	/**
	 * 登记到告警名， Attention： 非线程安全
	 * 
	 * @param String
	 *            设备实例ID
	 * @param String
	 *            告警ID
	 * @param String
	 *            页面名称
	 * @param IObject
	 * @return boolean true成功，false失败。
	 * @throws
	 */
	public static boolean regEventName(String equipmentid, String eventid,
			String pagename, IObject object) {
		// TODO: 尚未实现。 需实现告警名称改变时通知相关控件更新。
		return false;
	}

	/**
	 * Adapter 登记到告警名
	 * 
	 * @param int 设备实例ID
	 * @param int 告警ID
	 * @param String
	 *            页面名称
	 * @param IObject
	 * @return boolean true成功，false失败。
	 * @throws
	 */
	public static boolean regEventName(int nEquipId, int nEventId,
			String pagename, IObject object) {
		return regEventName(String.valueOf(nEquipId), String.valueOf(nEventId),
				pagename, object);
	}

	/**
	 * 登记告警控件， Attention： 非线程安全
	 * 
	 * @param String
	 *            设备实例ID
	 * @param String
	 *            信号ID
	 * @param String
	 *            页面名称
	 * @param IObject
	 * @return boolean true成功，false失败。
	 * @throws
	 */
	public static boolean setAlarmSignal(String equipmentid, String signalid,
			String pagename, IObject object) {
		if (null == object)
			return false;

		if (!setSignal(equipmentid, signalid, pagename, null))
			return false;

		equipment.htEquipmentData.get(equipmentid).htSignalData.get(signalid).registedAlarmObj
				.add(object);
		return true;
	}

	/**
	 * Adapter 登记告警控件
	 * 
	 * @param int 设备实例ID
	 * @param int 信号ID
	 * @param String
	 *            页面名称
	 * @param IObject
	 * @return boolean true成功，false失败。
	 * @throws
	 */
	public static boolean setAlarmSignal(int nEquipId, int nSignalId,
			String pagename, IObject object) {
		return setAlarmSignal(String.valueOf(nEquipId),
				String.valueOf(nSignalId), pagename, object);
	}

	// fjw add
	/**
	 * 登记本地保存数据控件， Attention： 非线程安全
	 * 
	 * @param String
	 *            设备实例ID
	 * @param String
	 *            信号ID
	 * @param String
	 *            页面名称
	 * @param IObject
	 * @return boolean true成功，false失败。
	 * @throws
	 */
	public static boolean setLocalSignal(final String equipId, String pagename,
			final IObject object) {
		//
		if (null == object)
			return false;

		// long startTime = System.currentTimeMillis();
		// while (true) {
		// if (!equipment.htEquipmentData.containsKey(equipId)) {
		// long endTime = System.currentTimeMillis();
		// if (startTime - endTime >= 3000) {
		// return false;
		// }
		// continue;
		// }
		// break;
		// }
		// equipment.htEquipmentData.get(equipId).registedBackgroundListObj.put(
		// object.getUniqueID(), object);
		// equipment.autoAnyPapeRefreshEquip.add(equipId);
		MGridActivity.xianChengChi.execute(new Runnable() {

			@Override
			public void run() {

				while (true) {
				
					if (!isLoading) {
						if (equipment.htEquipmentData.containsKey(equipId)) {
							equipment.htEquipmentData.get(equipId).registedBackgroundListObj
									.put(object.getUniqueID(), object);
							equipment.autoAnyPapeRefreshEquip.add(equipId);
						}
						break;
					}
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}

			}
		});

		return true;

	}

	/**
	 * Adapter 登记本地保存数据控件
	 * 
	 * @param int 设备实例ID
	 * @param int 信号ID
	 * @param String
	 *            页面名称
	 * @param IObject
	 * @return boolean true成功，false失败。
	 * @throws
	 */
	public static boolean setLocalSignal(int nEquipId, String pagename,
			IObject object) {
		return setLocalSignal(String.valueOf(nEquipId), pagename, object);
	}

	// fjw add end

	/**
	 * 注册信号列表控件， Attention： 非线程安全
	 * 
	 * @param String
	 *            equipId 设备实例ID
	 * @param String
	 *            pagename 页面名称
	 * @param IObject
	 * @return boolean true成功，false失败。
	 * @throws
	 */
	public static boolean setSignalList(String equipId, String pagename,
			IObject object) {
		if (null == equipId || null == object)
			return false;

		EquipmentDataModel.Equipment equip = equipment.htEquipmentData
				.get(equipId);

		if (null == equip) {
			equip = equipment.new Equipment();
			equip.m_equipid = equipId;

			equip.signal_req = protocol.build_query_signal_list(Integer
					.parseInt(equipId));

			equipment.htEquipmentData.put(equipId, equip);
		}

		HashSet<String> equipset = equipment.htPageEquipSet.get(pagename);

		if (null == equipset) {
			equipset = new HashSet<String>();
			equipment.htPageEquipSet.put(pagename, equipset);
		}

		equipset.add(equipId);

		return equip.registedLstObj.add(object);
	}

	/**
	 * setSignalList Adapter
	 * 
	 * @param int nEquipId 设备实例ID
	 * @param String
	 *            pagename 页面名称
	 * @param IObject
	 * @return boolean true成功，false失败。
	 * @throws
	 */
	public static boolean setSignalList(int nEquipId, String pagename,
			IObject object) {
		return setSignalList(String.valueOf(nEquipId), pagename, object);
	}

	/**
	 * 注册全局告警列表控件
	 * 
	 * @param IObject
	 * @return boolean true成功，false失败。
	 * @throws
	 */
	public static boolean setMainAlarmList(IObject object) {
		if (null == object)
			return false;
		return equipment.lstRegistedMainAlarmList.add(object);
	}

	/**
	 * 注册全局告警列表控件
	 * 
	 * @param IObject
	 * @return boolean true成功，false失败。
	 * @throws
	 */
	public static boolean setEquipState(String equipId, IObject object) {
		if (null == object)
			return false;
		if (null == equipId)
			return false;
		equipment.registedStateObj.add(object); // 注册state控件更新的链表

		return true;
	}

	public static boolean setEquipState(int nEquipId, IObject object) {
		return setEquipState(String.valueOf(nEquipId), object);
	}

	// lsy add
	public static HashMap<String, List<ipc_cfg_trigger_value>> getTrigger_list() {
		return trigger_list;
	}

	// ---------------------------------------------------------------
	// fjw add
	private static boolean deal_clearEquip(Equipment equipObj, int sig_no,
			Signal signal, String[] blocks) {

		for (int i = 0; i < sig_no; i++) {
			try {
				String[] f_items = blocks[i].split("`");
				signal = equipObj.htSignalData.get(f_items[1]);
				if (null == signal)
					break;
				signal.value = "0";

				signal.meaning = "0.0";
				if (i == sig_no - 1) {
					if (MGridActivity.whatLanguage) {
						signal.meaning = "通信中断";
						signal.value = "0.00000";
					} else {
						signal.meaning = "Lost";
						signal.value = "0.00000";
					}

				}

				if (signal.severity != 4) {
					signal.severity = 4;

					Iterator<IObject> regobj_it = signal.registedObj.iterator();
					while (regobj_it.hasNext()) {
						regobj_it.next().needupdate(true);
						sleep(200);
					}

					Iterator<IObject> regalarmobj_it = signal.registedAlarmObj
							.iterator();
					while (regalarmobj_it.hasNext()) {
						regalarmobj_it.next().needupdate(true);

						sleep(200);

					}

					Iterator<IObject> reglstobj_it = equipObj.registedLstObj
							.iterator();
					while (reglstobj_it.hasNext()) {
						IObject object = reglstobj_it.next();
						// if("SignalList".equals(object.getType()) ) break;
						object.needupdate(true);
						sleep(200);
					}

				}
			} catch (Exception e) {

			}
		}

		return true;
	}

	// 请求、处理实时信号
	private static boolean proc_rtsignal(Equipment equipObj) {
		if (null == equipObj)
			return false;
		if (null == equipObj.signal_req)
			return false;

		byte[] recv_buf = service.send_and_receive(equipObj.signal_req,
				service.IP, service.PORT);

		if (null == recv_buf)
			return false;
		msg_head head = protocol.parse_msg_head(recv_buf);
		if (head.cmd != msg_type.MSG_QUERY_SIG_LIST_RT_ACK)
			return false;

		int sig_no = head.para;
		int i = 0;

		// TODO: 寻求不拷贝方案
		byte[] body_buf = new byte[head.length];
		if (head.length > 1) {
			try {
				System.arraycopy(recv_buf, protocol.MSG_HEAD_LEN, body_buf, 0,
						head.length);
				recv_buf = null; // 清空数组内存
			} catch (Exception e) {
				return false;
			}
		} else {
			return false;
		}

		String body = new String(body_buf);

		Signal signal = null;
		String[] blocks = body.split("\\|");

		body_buf = null;
		body = null;
		recv_buf = null;

		if (sig_no > 0) {
			String[] items = blocks[0].split("`");

			if (equipObj.strSampleUpdateTime.equals(items[2])) {

				String[] f_items = blocks[sig_no - 1].split("`");
				signal = equipObj.htSignalData.get(f_items[1]);
				// String str_signal = signal.value;
				equipObj.oldSameUpdateTime = Integer
						.parseInt(equipObj.strSampleUpdateTime);
				// String str_lastSignalValue = str_signal.substring(0,6);

				if (equipObj.lUpdateTime / 1000 - equipObj.oldSameUpdateTime > 180) {

					// System.out.println("我是线程清零");
					deal_clearEquip(equipObj, sig_no, signal, blocks);

					return false;
				}
				f_items = null;
				// System.out.println("XXXXXXXXXXXX：4");
				return true;
			} else {
				equipObj.strSampleUpdateTime = new String(items[2]);

			}
		} else {

			return false;

		}

		// System.out.println("我是线程3");
		boolean equipupdated = false;
		boolean haseveritychanged = false;
		for (i = 0; i < sig_no; ++i) {
			String[] items = blocks[i].split("`");

			signal = equipObj.htSignalData.get(items[1]);

			if (null == signal)
				continue;

			try {
				int severity;
				String meaning = "";

				signal.freshtime = Integer.parseInt(items[2]);
				signal.is_invalid = Integer.parseInt(items[3]);
				signal.value_type = Integer.parseInt(items[4]);

				if (!items[5].equals(signal.value)) {
					equipupdated = true;
					// float f=Float.parseFloat(items[5]);
					// int ii=(int) (f*100);
					// f=(float) (ii/100.0);
					// signal.value = f+"";
					signal.value = new String(items[5]);

				}

				severity = Integer.parseInt(items[6]);

				if (signal.severity != severity) {
					haseveritychanged = true;
					signal.severity = severity;

					Iterator<IObject> regalarmobj_it = signal.registedAlarmObj
							.iterator();
					while (regalarmobj_it.hasNext()) {
						// System.out.println("123severity不一样：我更新了");
						regalarmobj_it.next().needupdate(true);

					}
				}

				if (7 < items.length)
					meaning = items[7].trim();

				if (meaning.isEmpty()) {
					BigDecimal bd = new BigDecimal(signal.value);
					bd = bd.setScale(signal.precision, BigDecimal.ROUND_HALF_UP);
					meaning = bd.toString();
				}

				if (!meaning.equals(signal.meaning)) {
					signal.meaning = new String(meaning);

					Iterator<IObject> regobj_it = signal.registedObj.iterator();
					while (regobj_it.hasNext()) {
						IObject reg_ui = regobj_it.next();

						if ("SaveSignal".equals(reg_ui.getType())) {
							;
						} else if ("SignalEquipt".equals(reg_ui.getType())) {
							;
						} else if ("SignalCurve".equals(reg_ui.getType())) {
							;
						} else if ("SignalCurves".equals(reg_ui.getType())) {
							;
						} else {
							// System.out.println("123meaning不一样：我更新了");
							reg_ui.needupdate(true);
						}

					}
				}

			} catch (Exception e) {

			} finally {
				// System.out.println("id123："+signal.id);
				// System.out.println("value123："+signal.value);
				// System.out.println("meaning123: "+signal.meaning);
			}
		} // end of for(sig_no)
			// fjw add

		blocks = null;

		// fjw add end
		if (equipupdated) {

			Iterator<IObject> reglstobj_it = equipObj.registedLstObj.iterator();
			while (reglstobj_it.hasNext()) {
				IObject reg_ui = reglstobj_it.next();

				if ("SaveSignal".equals(reg_ui.getType())) {
					;
				} else if ("SignalEquipt".equals(reg_ui.getType())) {
					;
				} else if ("LocalList".equals(reg_ui.getType())) {
					;
				} else if ("SignalCurve".equals(reg_ui.getType())) {
					;
				} else if ("SignalCurves".equals(reg_ui.getType())) {
					;
				} else {
					// System.out.println("123value不一样：我更新了");
					reg_ui.needupdate(true);

				}

			}
		}
		// System.out.println("实时处理XXX：6");
		return true;
	} // end of proc_rtsignal

	public static String proc_rtsignal(Equipment equipObj, String Sid) {
		String s = "-1";
		if (null == equipObj)
			return s;
		if (null == equipObj.signal_req)
			return s;

		byte[] recv_buf = service.send_and_receive(equipObj.signal_req,
				service.IP, service.PORT);

		if (null == recv_buf)
			return s;
		msg_head head = protocol.parse_msg_head(recv_buf);
		if (head.cmd != msg_type.MSG_QUERY_SIG_LIST_RT_ACK)
			return s;

		int sig_no = head.para;
		int i = 0;

		// TODO: 寻求不拷贝方案
		byte[] body_buf = new byte[head.length];
		if (head.length > 1) {
			try {
				System.arraycopy(recv_buf, protocol.MSG_HEAD_LEN, body_buf, 0,
						head.length);
				recv_buf = null; // 清空数组内存
			} catch (Exception e) {
				return s;
			}
		} else {
			return s;
		}
		// System.out.println("我是线程2");
		String body = new String(body_buf);

		Signal signal = null;
		String[] blocks = body.split("\\|");

		body_buf = null;
		body = null;
		recv_buf = null;
		// System.out.println("我是线程3");
		boolean equipupdated = false;
		boolean haseveritychanged = false;

		for (i = 0; i < sig_no; ++i) {
			String[] items = blocks[i].split("`");

			if (!items[1].equals(Sid))
				continue;
			signal = equipObj.htSignalData.get(items[1]);

			if (null == signal)
				continue;
			s = new String(items[5]);
			break;
		}
		return s;
	}

	private static boolean proc_allrtalarm() {

		if (equipment.rtalarm_req == null)
			return false;
		byte[] recv_buf = service.send_and_receive(equipment.rtalarm_req,
				service.IP, service.PORT);
		if (recv_buf == null) {
			return false;
		}

		msg_head head = protocol.parse_msg_head(recv_buf);

		if (head.cmd != msg_type.MSG_ID_QUERY_ALL_ACTIVE_ALARM_ACK) {
			return false;
		}

		int block_no = head.para;
		int i = 0;

		// TODO: 寻求不拷贝方案
		byte[] body_buf = new byte[head.length];
		if (head.length > 1) {
			try {
				System.arraycopy(recv_buf, protocol.MSG_HEAD_LEN, body_buf, 0,
						head.length);
				recv_buf = null; // 释放数组内存
			} catch (Exception e) {

			}
		} else {
			// fjw add
			if (alarm_flag) {

				equipment.htEventData = null;

				Iterator<IObject> reglstobj_it = equipment.lstRegistedMainAlarmList
						.iterator();
				while (reglstobj_it.hasNext()) {
					//System.out.println("我更新了："+reglstobj_it.next().getType());
					reglstobj_it.next().needupdate(true);
					try {

						sleep(50);
					} catch (Exception e) {
						;
					}

				}
			}
			alarm_flag = false;
			// fjw add end
			return false; // 没有告警内容返回false
		}

		String body = new String(body_buf);
		if (equipment.rtalarm_rspbody.equals(body))
			return true;
		equipment.rtalarm_rspbody = body;
		body = null; //
		body_buf = null; //
		recv_buf = null; //

		// TODO: 处理更新
		int flag_state = 0;
		Event event = null;
		Hashtable<String, Event> equipevt = null;
		Hashtable<String, Hashtable<String, Event>> eventData = new Hashtable<String, Hashtable<String, Event>>();
		String[] blocks = equipment.rtalarm_rspbody.split("\\|");
		for (i = 0; i < block_no; ++i) {
			String[] items = blocks[i].split("`");
			ipc_active_event alarm = new ipc_active_event();

			// System.out.println(items.toString());
			try {
				alarm.equipid = Integer.parseInt(items[0]);
				alarm.eventid = Integer.parseInt(items[1]);

				// equipevt = equipment.htEventData.get(items[0]);
				equipevt = eventData.get(items[0]);
				if (null == equipevt) {
					equipevt = new Hashtable<String, Event>();
					eventData.put(new String(items[0]), equipevt);
					// equipment.htEventData.put(items[0], equipevt);
				}

				event = equipevt.get(items[1]);
				if (null == event) {
					event = equipment.new Event();
					equipevt.put(new String(items[1]), event);
				}
				// event.eventid = items[1]; //fjw add 添加告警id
				event.name = getEventName(items[0], items[1]);
				// TODO: 实现getEventSignalId函数
				// event.value = getSignalValue(items[0],
				// getEventSignalId(items[0], items[1]));
				event.starttime = Integer.parseInt(items[2]);
				event.stoptime = Integer.parseInt(items[3]);
				// alarm.is_active = Integer.parseInt(items[4]);
				event.grade = Integer.parseInt(items[5]);
				event.meaning = (null == items[6] ? "" : new String(items[6]));

				if ((items[0] != null) && ("10001".equals(items[1]))) {
					int state = service.get_Equipment_State(Integer
							.valueOf(items[0]));
					if (state == 1) {
						flag_state = 1;
						event.state = "1";

					} else {
						event.state = "0";
					}
				}
				// fjw add end
			} catch (Exception e) {

			}
		} // for end

		equipment.htEventData = eventData;
		alarm_flag = true;

		Iterator<IObject> reglstobj_it = equipment.lstRegistedMainAlarmList
				.iterator();
		while (reglstobj_it.hasNext()) {
			reglstobj_it.next().needupdate(true);
			try {

				sleep(50);
			} catch (Exception e) {
				;
			}

		}

		// fjw add
		if (flag_state == 1) {
			flag_state = 0;
			Iterator<IObject> reglstobj_it2 = equipment.registedStateObj
					.iterator();
			while (reglstobj_it2.hasNext()) {
				reglstobj_it2.next().needupdate(true);
				try {
					sleep(50);
				} catch (Exception e) {
					;
				}
			}
		}

		return true;
	} // end of proc_allrtalarm

	// fjw add proc_autosignal() this function
	// made time: 2015 8 14

	private static boolean proc_autosignal(Equipment equipObj) {
		if (null == equipObj)
			return false;

		if (equipObj.registedBackgroundListObj == null)
			return false;

		if (null == equipObj.signal_req)
			return false;
		byte[] recv_buf = service.send_and_receive(equipObj.signal_req,
				service.IP, service.PORT);

		if (null == recv_buf)
			return false;

		msg_head head = protocol.parse_msg_head(recv_buf);
		if (head.cmd != msg_type.MSG_QUERY_SIG_LIST_RT_ACK)
			return false;

		int sig_no = head.para;
		int i = 0;

		// TODO:
		byte[] body_buf = new byte[head.length];
		if (head.length > 1) {
			try {
				System.arraycopy(recv_buf, protocol.MSG_HEAD_LEN, body_buf, 0,
						head.length);
				recv_buf = null; //
			} catch (Exception e) {
				return false;
			}
		} else {
			return false;
		}

		String body = new String(body_buf);

		Signal signal = null;
		String[] blocks = body.split("\\|");
		body = null; //
		body_buf = null;
		recv_buf = null; //

		if (sig_no > 0) {
			String[] items = blocks[0].split("`");

			if (equipObj.proAutoUpdateTime.equals(items[2])) {

				return false;
			} else
				equipObj.proAutoUpdateTime = new String(items[2]);
		} else {
			return false;
		}

		// fjw add
		String[] str_equip_signal = new String[sig_no];
		long time = 0;
		String dateTime = "";
		String sampletime = "";

		for (i = 0; i < sig_no; ++i) {
			String[] items = blocks[i].split("`");

			signal = equipObj.htSignalData.get(items[1]);
			if (null == signal)
				continue;

			try {
				time = Integer.parseInt(items[2]);

				SimpleDateFormat formatter = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss");
				Date date = new Date(time * 1000);
				sampletime = formatter.format(date);
				dateTime = sampletime.substring(0, 10);

				local_his_signal his_sig = new local_his_signal();
				his_sig.equipid = equipObj.m_equipid;
				his_sig.equip_name = equipObj.m_name;
				his_sig.sigid = items[1];
				his_sig.freshtime = sampletime;
				his_sig.is_invalid = items[3];
				his_sig.value_type = items[4];
				his_sig.value = items[5];
				his_sig.severity = items[6];
				his_sig.name = signal.name;
				his_sig.unit = signal.unit;
				str_equip_signal[i] = his_sig.to_string();
				if ((his_sig.value == null) || ("".equals(his_sig.value)))
					return false;
				his_sig = null;

			} catch (Exception e) {

			}

		} // end of for(sig_no)
		blocks = null;

		String str_lastSignalValue = signal.value.substring(0, 6);
		if (("10001".equals(signal.id))
				&& ("0.0000".equals(str_lastSignalValue))) {

			return false;
		}

		if (equipObj.registedBackgroundListObj == null)
			return false;
		Iterator<String> obj_idList = equipObj.registedBackgroundListObj
				.keySet().iterator();// 遍历出该设备所关联的控件id

		while (obj_idList.hasNext()) {
			String obj_id = obj_idList.next();
			IObject iobject = equipObj.registedBackgroundListObj.get(obj_id);

			if ("SignalCurve".equals(iobject.getType())) {

				saveSignalCurveData(time, iobject, equipObj, str_equip_signal);
			}
			if ("SignalCurves".equals(iobject.getType())) {

				saveSignalCurvesData(time, iobject, equipObj, str_equip_signal);
			}
			if ("SaveSignal".equals(iobject.getType())) {

				loadSignalData(time, iobject, equipObj, str_equip_signal,
						dateTime);
			}
			if ("SaveEquipt".equals(iobject.getType())
					|| "HistorySignalList".equals(iobject.getType())) {

				loadEquipSignalData(time, iobject, equipObj, str_equip_signal,
						dateTime);
			}
			if ("RC_Label".equals(iobject.getType())) {

				loadRCData(time, iobject, equipObj, str_equip_signal,
						sampletime);
			}
			if ("AutoSig".equals(iobject.getType())
					|| "AutoSigList".equals(iobject.getType())) {

				iobject.needupdate(true);

			}
		}

		str_equip_signal = null;
		proc_auto_time = time;
		return true;
	} // end of proc_autosignal

	// ---------------------fjw add-----------------------

	private static boolean saveSignalCurveData(long time, IObject ui_object,
			Equipment equipObj, String[] str_equip_signal) {

		String obj_Id = ui_object.getUniqueID();
		String obj_sigId = equipObj.registedObjID_SignalID.get(obj_Id);
		if ("".equals(obj_sigId))
			return false;

		try {
			int no = Integer.valueOf(obj_sigId);
			String sigstr = str_equip_signal[no - 1];
			local_his_signal sig_class = new local_his_signal();

			if (sig_class.read_string(sigstr)) {

				Hashtable<String, save_curve_signal> sig_curvelist = new Hashtable<String, save_curve_signal>();

				save_curve_signal curve = new save_curve_signal();

				if (equipObj.h_ESigId_curveList.containsKey(obj_Id)) {
					sig_curvelist = equipObj.h_ESigId_curveList.get(obj_Id);

					if (sig_curvelist.containsKey(obj_sigId)) {
						curve = sig_curvelist.get(obj_sigId);
					}
				}

				if (time - curve.samperTime < curve_getTime)
					return false;

				curve.samperTime = time;

				if (!curve.add_point(sig_class.value, sig_class.freshtime)) {

					return false;
				}
				sig_curvelist.put(obj_sigId, curve);
				curve = null;

				equipObj.h_ESigId_curveList.put(obj_Id, sig_curvelist);
				sig_curvelist = null;

				if (equipObj.h_ESigId_curveList.get(obj_Id).size() > 10)
					equipObj.h_ESigId_curveList.get(obj_Id).clear();

				if (equipObj.h_ESigId_curveList.size() > 20)
					equipObj.h_ESigId_curveList.clear();

			}
			sig_class = null; // 清空内存

		} catch (Exception e) {

		}

		try {
			ui_object.needupdate(true);
			sleep(200);// sleep(500);
		} catch (Exception e) {

			return false;
		}
		return true;
	}

	private static boolean saveSignalCurvesData(long time, IObject ui_object,
			Equipment equipObj, String[] str_equip_signal) {

		String obj_Id = ui_object.getUniqueID();
		String obj_sigId = equipObj.registedObjID_SignalID.get(obj_Id);
		if ("".equals(obj_sigId))
			return false;

		try {
			int no = Integer.valueOf(obj_sigId);
			String sigstr = str_equip_signal[no - 1];
			local_his_signal sig_class = new local_his_signal();

			if (sig_class.read_string(sigstr)) {

				Hashtable<String, save_multipoint_signal> sig_curveslist = new Hashtable<String, save_multipoint_signal>();

				save_multipoint_signal curves = new save_multipoint_signal();

				if (equipObj.h_ESigId_curvesList.containsKey(obj_Id)) {
					sig_curveslist = equipObj.h_ESigId_curvesList.get(obj_Id);

					if (sig_curveslist.containsKey(obj_sigId)) {
						curves = sig_curveslist.get(obj_sigId);
					}
				}

				if (time - curves.samperTime < curves_getTime)
					return false;

				curves.samperTime = time;

				if (!curves.add_point(sig_class.value, sig_class.freshtime)) {

					return false;
				}
				sig_curveslist.put(obj_sigId, curves);
				curves = null;

				equipObj.h_ESigId_curvesList.put(obj_Id, sig_curveslist);
				sig_curveslist = null;

				if (equipObj.h_ESigId_curvesList.get(obj_Id).size() > 10)
					equipObj.h_ESigId_curvesList.get(obj_Id).clear();

				if (equipObj.h_ESigId_curvesList.size() > 20)
					equipObj.h_ESigId_curvesList.clear();

			}
			sig_class = null; // 清空内存

		} catch (Exception e) {

		}

		try {
			ui_object.needupdate(true);
			sleep(200);// sleep(500);
		} catch (Exception e) {

			return false;
		}
		return true;
	}

	private static boolean loadSignalData(long time, IObject ui_object,
			Equipment equipObj, String[] str_equip_signal, String dateTime) {

		if (flag3_map.isEmpty()
				|| !flag3_map.containsKey(equipObj.m_equipid)
				|| (time - flag3_map.get(equipObj.m_equipid) >= SaveSignal.save_time)) {

			flag_equipid = equipObj.m_equipid;
			flag_time = time;
			if ((flag_equipid == "") || (flag_equipid == null)
					|| flag_time == 0) {
				return true;
			}
			flag3_map.put(flag_equipid, flag_time);

			Iterator<String> itrsigid = equipObj.registedSignalID.iterator();
			if (itrsigid == null)
				return false;
			while (itrsigid.hasNext()) {
				try {
					String str_no = itrsigid.next();
					if (str_no == null)
						break;
					int no = Integer.valueOf(str_no);
					String sigstr = str_equip_signal[no - 1];

					local_file l_file = new local_file();
					l_file.has_file(flag_equipid + "-" + str_no + "#"
							+ dateTime, 2);

					l_file.write_line(sigstr);

					l_file = null;
				} catch (Exception e) {

				}
			}

			try {

			} catch (Exception e) {

				return false;
			}
		} // if(保存时间) end
		return true;
	}

	private static boolean loadEquipSignalData(long time, IObject ui_object,
			Equipment equipObj, String[] equip_his_signal, String dateTime) {

		if (flag4_map.isEmpty()
				|| !flag4_map.containsKey(equipObj.m_equipid)
				|| (time - flag4_map.get(equipObj.m_equipid) >= SaveEquipt.save_time)) {

			flag_equipid = equipObj.m_equipid;
			flag_time = time;
			if ((flag_equipid == "") || (flag_equipid == null)
					|| flag_time == 0) {
				return true;
			}
			try {
				flag4_map.put(flag_equipid, flag_time);

				local_file l_file = new local_file(); //
				l_file.has_file(flag_equipid + "#" + dateTime, 1);

				for (int j = 0; j < equip_his_signal.length; j++) {
					l_file.write_line(equip_his_signal[j]);

				}
				local_file.num_signal = equip_his_signal.length;
				l_file = null;

			} catch (Exception e) {

			}

			try {

			} catch (Exception e) {

				return false;
			}

		} // if(保存时间) end
		return true;
	}

	private static boolean loadRCData(long time, IObject ui_object,
			Equipment equipObj, String[] str_equip_signal, String sampletime) {

		int now_hour = Integer.parseInt(sampletime.substring(11, 13));
		int now_min = Integer.parseInt(sampletime.substring(14, 16));
		if ((now_hour == 0) && (now_min > 5) && (now_min < 10)) {

			Iterator<String> itrsigid = equipObj.registedRCSignalID.iterator();
			if (itrsigid == null)
				return false;
			while (itrsigid.hasNext()) {
				try {
					String str_no = itrsigid.next();
					if (str_no == null)
						break;
					int no = Integer.valueOf(str_no);//
					String sigstr = str_equip_signal[no - 1]; //

					local_rc l_rc = new local_rc();
					String name_head = equipObj.m_equipid + "-" + str_no;

					l_rc.add_line_deal(name_head, sigstr, time);
					l_rc = null;
				} catch (Exception e) {

				}
			}

			try {
				ui_object.needupdate(true);
				sleep(200);// sleep(500);
			} catch (Exception e) {

				return false;
			}
		} // if(保存时间) end
		return true;
	}

	// --------------------内部调用函数end--------------------

	// 内部结构 ===========================================================
	// 解决读取迭代器时，其他线程增加项目导致的不可捕获型异常 ConcurrentModificationException 。
	public class CopiedIterator<T> implements Iterator<T> {
		private Iterator<T> iterator = null;

		public CopiedIterator(Iterator<T> itr) {
			LinkedList<T> list = new LinkedList<T>();
			while (itr.hasNext()) {
				list.add(itr.next());
			}
			this.iterator = list.iterator();
		}

		public boolean hasNext() {
			return this.iterator.hasNext();
		}

		public void remove() {
			throw new UnsupportedOperationException(
					"This is a read-only iterator.");
		}

		public T next() {
			return this.iterator.next();
		}
	}

	// 成员数据 ===========================================================
	public static EquipmentDataModel equipment = new EquipmentDataModel();
	public static String currentPage = "";
	public static boolean bIsLoading = false;
	public static long flag_time = 0;
	public static String flag_equipid = null;
	public static HashMap<String, Long> flag3_map = null;
	public static HashMap<String, Long> flag4_map = null;
	public static save_curve_signal curve_class = new save_curve_signal();
	public static save_multipoint_signal curves_class = new save_multipoint_signal();
	public static int curve_getTime = 30;
	public static int curves_getTime = 60 * 2;

	public static long proc_auto_time = 0;
	public static boolean alarm_flag = false;
	public static boolean isPut = true;
	// lsy add
	public static HashMap<String, List<ipc_cfg_trigger_value>> trigger_list = null;
	public static ArrayList<String> LabelList = null;
	public static boolean isLoading = true;
}
