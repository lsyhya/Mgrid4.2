package comm_service;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import com.mgrid.data.DataGetter;

import android.util.Log;
import data_model.apk_active_event;
import data_model.apk_active_signal;
import data_model.ipc_active_event;
import data_model.ipc_cfg_control;
import data_model.ipc_cfg_ctrl_parameaning;
import data_model.ipc_cfg_event;
import data_model.ipc_cfg_signal;
import data_model.ipc_cfg_signal_name;
import data_model.ipc_cfg_trigger_value;
import data_model.ipc_control;
import data_model.ipc_control_value_data;
import data_model.ipc_data_signal;
import data_model.ipc_equipment;
import data_model.ipc_history_signal;



public class service {
	
	public static String IP = "127.0.0.1";
	public static int PORT = 9630;	

	 
	public static byte[] send_and_receive(byte[] send_buf, String ip_addr, int port) {
		long l=System.currentTimeMillis();
		byte[] receivetemp = new byte[1024*1024];
		byte[] recv = null;
		// TODO: 容错返回 NULL 情况
		
			Socket socket;		
			socket = new Socket(); // first

			OutputStream out = null;
			InputStream in = null;

			try {
			socket.connect(new InetSocketAddress(ip_addr, port), 1000);  //
			//System.out.println("通讯时间AAAAAAAAAAAAAAA：QQQ"+(System.currentTimeMillis()-l));
			socket.setSoTimeout(5000);  // 
			
			out = socket.getOutputStream();
			in = socket.getInputStream();
			out.write(send_buf, 0, send_buf.length);
			//long l1=System.currentTimeMillis();
			//System.out.println("通讯时间BBBBBBBBBBBBBBBBbb：QQQ"+(l1-l));
			socket.setSoTimeout(5000);  // 
			out.flush();
			int index = 0;
			byte[] soh = new byte[1];
			int try_times = 0;
			while (true) {
				//long l10=System.currentTimeMillis();
				
				if (in.read(soh, index, 1) == 1) {
				//	System.out.println("通讯时间GGGGGGGGGGGGGGGG：QQQ"+(System.currentTimeMillis()-l10));
					break;					
				}
				
				/*try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					;
				}*/
				
				try_times ++;
				if (try_times > 100) {
					socket.close();
					receivetemp = null; //fjw add 释放内存
					return null;
				}
				
				
//				try_times ++;
//				try {
//					Thread.sleep(10);
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				if (try_times > 10) {
//					System.out.println("Socket断开了 QQQ");
//					socket.close();
//					receivetemp = null; //fjw add 释放内存
//					return null;
//				}				
				
				
				
				
				
				
			}
			//long l2=System.currentTimeMillis();
			//System.out.println("通讯时间CCCCCCCCCCCCCCCc：QQQ"+(l2-l1));
			
			receivetemp[0] = soh[0];
			if (receivetemp[0] != 0x01) {
				//.out.println("Socket soh not rightQQQ");
				socket.close();
				receivetemp = null; //fjw add 释放内存
				return null;
			}
			
			index ++;

			if (in.read(receivetemp, index, protocol.MSG_HEAD_LEN-1) 
  	    	    	!= protocol.MSG_HEAD_LEN-1) {
				//System.out.println("Socket length error QQQ");
				socket.close();
				receivetemp = null; //fjw add 释放内存
				return null;
			}
			index += protocol.MSG_HEAD_LEN-1;
			msg_head head = protocol.parse_msg_head(receivetemp);
			
			if (head.length == 1) {
				head.length = 0;	// must response error
			}
			
			int body_recved = 0;
			int body_left = head.length;
			try_times = 0;
			long l3=System.currentTimeMillis();
			//System.out.println("通讯时间DDDDDDDDDDDDDDD：QQQ"+(l3-l2));
			if (head.length > receivetemp.length) {
				socket.close();  // need to close socket
				receivetemp = null; //fjw add 释放内存
				return null;
			}
			
			recv = new byte[protocol.MSG_HEAD_LEN+head.length];
			System.arraycopy(receivetemp, 0, recv, 0, protocol.MSG_HEAD_LEN);
			long l4=System.currentTimeMillis();
			//System.out.println("通讯时EEEEEEEEEEEEEEE：QQQ"+(l4-l3));
			
			long l5=System.currentTimeMillis();
			
			while (body_left > 0) {
				body_recved = in.read(receivetemp, 0, body_left > 1024*1024 ? 1024 * 1024 : body_left);
				
				// 对意外读取到末尾容错
				if (-1 == body_recved)
				{
					socket.close();
					receivetemp = null; //fjw add 释放内存
					recv = null;  //fjw add 释放内存
					return null;
				}
				
				System.arraycopy(receivetemp, 0, recv, protocol.MSG_HEAD_LEN + head.length - body_left, body_recved);
				
				body_left -= body_recved;
				if (0 == body_left) break;
				
				if (try_times > 5) {
				//	System.out.println("try times >5 QQQ");
					socket.close();
					receivetemp = null; //fjw add 释放内存
					recv = null;  //fjw add 释放内存
					return null;
				}
				
	            try_times ++; 
			}
			//System.out.println("通讯时间FFFFFFFFFFFFFFF：QQQ"+(System.currentTimeMillis()-l5));
			
			receivetemp = null; //fjw add 释放内存
			
			
			return recv;
		} catch (IOException e)
		{
		
			e.printStackTrace();
			receivetemp = null; //fjw add 释放内存
		}finally
		{
			try
			{
				in.close();
			} catch (Exception e)
			{
				e.printStackTrace();
			}
			
			try
			{
				out.close();
			} catch (Throwable e)
			{
				e.printStackTrace();
			}
			
			try
			{
				socket.close();
			} catch (Throwable e)
			{
				String ee = e.getMessage().toString();
			
				receivetemp = null; //fjw add 释放内存
			}
			//long i=System.currentTimeMillis()-l;
//			System.out.println("网络请求时间："+i);
		}
		receivetemp = null; //fjw add 释放内存
		recv = null;  //fjw add 释放内存
		
		return null;
	}
	
	
	

	public static List<ipc_equipment> get_equipment_list(String ip_addr, int port) {
		byte[] send_buf = protocol.build_query_equip_list();
		byte[] recv_buf = send_and_receive(send_buf, ip_addr, port);
		
		return protocol.parse_query_equip_list_ack(recv_buf); 
	}
	

	public static List<ipc_cfg_signal> get_signal_cfg_list(String ip_addr, int port, int equipid) {
		byte[] send_buf = protocol.build_query_signal_list(equipid);
		byte[] recv_buf = send_and_receive(send_buf, ip_addr, port);
		
		if(equipid==188)
		{
			for (int i = 0; i < recv_buf.length; i++) {
				System.out.print(recv_buf[i]+",");
			} 
		}
		
		return protocol.parse_query_signal_list_ack(recv_buf);
	}     
	

	public static List<ipc_data_signal> get_signal_data_list(String ip_addr, int port, int equipid) {
		byte[] send_buf = protocol.build_query_signal_rt_list(equipid);
		byte[] recv_buf = send_and_receive(send_buf, ip_addr, port);
		
		return protocol.parse_query_signal_list_rt_ack(recv_buf);
	}
	

	// WARN: 低效慎用！！！  -- CharlesChen
	public static ipc_data_signal get_signal_data(String ip_addr, int port, int equipid, int signalid) {
		byte[] send_buf = protocol.build_query_signal_rt_list(equipid);
		byte[] recv_buf = send_and_receive(send_buf, ip_addr, port);
		
		List<ipc_data_signal> ipc_data_sigs =  protocol.parse_query_signal_list_rt_ack(recv_buf);
		ipc_data_signal ipc_data_sig = new ipc_data_signal();
		
		int i = 0;
		for (i = 0; i < ipc_data_sigs.size(); ++i) {
			if (signalid == ((ipc_data_signal)ipc_data_sigs.toArray()[i]).sigid) {
				ipc_data_sig = (ipc_data_signal)ipc_data_sigs.toArray()[i];
				break;
			}
		}
		
		return ipc_data_sig;
	}
	

	// WARN: 低效慎用！！！  -- CharlesChen
	public static int get_event_level(String ip_addr, int port, int equipid, int signalid)
	{
		byte[] send_buf = protocol.build_query_signal_rt_list(equipid);
		byte[] recv_buf = send_and_receive(send_buf, ip_addr, port);
		
		List<ipc_data_signal> ipc_data_sigs =  protocol.parse_query_signal_list_rt_ack(recv_buf);
		ipc_data_signal ipc_data_sig = new ipc_data_signal();
		
		int i = 0;
		for (i = 0; i < ipc_data_sigs.size(); ++i) {
			if (signalid == ((ipc_data_signal)ipc_data_sigs.toArray()[i]).sigid) {
				ipc_data_sig = (ipc_data_signal)ipc_data_sigs.toArray()[i];
				break;
			}
		}
		
		return ipc_data_sig.severity;
	}
	
	
	

	public static List<ipc_cfg_control> get_control_cfg_list(String ip_addr, int port, int equipid) {
		byte[] send_buf = protocol.build_query_control_list(equipid);
		byte[] recv_buf = send_and_receive(send_buf, ip_addr, port);
		
		return protocol.parse_query_control_list_ack(recv_buf);
	}
	

	public static List<ipc_cfg_ctrl_parameaning> get_control_parameaning_cfg_list(String ip_addr, int port, int equipid) {
		byte[] send_buf = protocol.build_query_control_parameaning_list(equipid);
		byte[] recv_buf = send_and_receive(send_buf, ip_addr, port);
		
		return protocol.parse_query_control_parameaning_list_ack(recv_buf);
	}
	

	public static List<ipc_control_value_data> 
	get_control_value_data_list(String ip_addr, int port, int equipid) {
		byte[] send_buf = protocol.build_query_control_value_data(equipid);
		byte[] recv_buf = send_and_receive(send_buf, ip_addr, port);
		
		return protocol.build_query_control_value_data_ack(recv_buf);
	}
	

	public static List<ipc_cfg_event> get_event_cfg_list(String ip_addr, int port, int equipid) {
		byte[] send_buf = protocol.build_query_event_list(equipid);
		byte[] recv_buf = send_and_receive(send_buf, ip_addr, port);
		
		return protocol.parse_query_event_list_ack(recv_buf);
	}
	

	public static List<ipc_active_event> get_all_active_alarm_list(String ip_addr, int port) {
		byte[] send_buf = protocol.build_query_all_active_alarm_list();
		byte[] recv_buf = send_and_receive(send_buf, ip_addr, port);
		
		return protocol.parse_query_all_active_alarm_list_ack(recv_buf);
	}
	

	public static List<ipc_active_event> get_equip_active_alarm_list(String ip_addr, int port, int equipid) {
		byte[] send_buf = protocol.build_query_equip_active_alarm_list(equipid);
		byte[] recv_buf = send_and_receive(send_buf, ip_addr, port);
		
		return protocol.parse_query_equip_active_alarm_list_ack(recv_buf);
	}
	

	public static int send_control_cmd(String ip_addr, int port, List<ipc_control> control_cmds) {
		byte[] send_buf = protocol.build_control_cmd(control_cmds);
		byte[] recv_buf = send_and_receive(send_buf, ip_addr, port);
		
		return protocol.parse_control_cmd_ack(recv_buf);
	}
	

	public static List<ipc_history_signal> get_history_signal_list(String ip_addr, int port, int equipid) {
		byte[] send_buf = protocol.build_query_history_signal_list(equipid);
		byte[] recv_buf = send_and_receive(send_buf, ip_addr, port);

		List<ipc_history_signal> history = protocol.parse_query_history_signal_ack(recv_buf);

		return history;
	}


	public static List<ipc_history_signal> get_his_sig_list(String ip_addr, int port, int equipid, int signalid,
			long startime, long span, long count, boolean order)
	{
		
		byte[] send_buf = protocol.build_query_his_sig(equipid, signalid, startime, span, count, order);
		byte[] recv_buf = send_and_receive(send_buf, ip_addr, port);
	
		List<ipc_history_signal> sih  = protocol.parse_query_his_sig_ack(recv_buf);
		

		return sih;

	}
	
	

	public static int get_MU_State()
	{
		List<ipc_active_event> alarmList = get_all_active_alarm_list(IP,PORT);
		int state = 0;
		
		if(alarmList.isEmpty())
		{
			return 0;
		}
		for (int i = 0; i < alarmList.size(); ++i) 
		{
			if (10001 == ((ipc_active_event)alarmList.toArray()[i]).eventid) 
			{
				state = 1;
			}
		}
	   if(state==1 && alarmList.size()>0)
		{
			return 1;
		}
		else
		{
			return 2;
		}
	}
	

	public static List<apk_active_event> get_Active_Event(int equipID)
	{

	
		List<ipc_active_event>  activeEventList = get_all_active_alarm_list(IP, PORT);
		
		List<apk_active_event> eventList = new ArrayList<apk_active_event>();

		for(int i = 0; i < activeEventList.size(); ++i)
		{
			apk_active_event event = new apk_active_event();
			event.eventid = ((ipc_active_event)activeEventList.toArray()[i]).eventid;
			event.starttime = ((ipc_active_event)activeEventList.toArray()[i]).starttime;
			event.endtime = ((ipc_active_event)activeEventList.toArray()[i]).endtime;
			event.grade = ((ipc_active_event)activeEventList.toArray()[i]).grade;
			event.equipid = ((ipc_active_event)activeEventList.toArray()[i]).equipid;
			event.meaning = ((ipc_active_event)activeEventList.toArray()[i]).meaning;
			event.is_active = ((ipc_active_event)activeEventList.toArray()[i]).is_active;
			// just for test {{{
			//event.name = get_event_name(event.equipid, event.eventid);
			event.name = DataGetter.getEventName(String.valueOf(event.equipid), String.valueOf(event.eventid));
			event.equipName = DataGetter.getEquipmentName(String.valueOf(event.equipid));
			// }}}
			eventList.add(event);
		}
		return eventList;
	}
	

	public static List<apk_active_signal> get_Active_Signal(int equipID)
	{
	
		List<ipc_data_signal>  activeSignalList = get_signal_data_list(IP,PORT,equipID);
		List<apk_active_signal> signalList = new ArrayList<apk_active_signal>();
		for(int i = 0; i < activeSignalList.size(); ++i)
		{
			apk_active_signal signal = new apk_active_signal();
			
			signal.sigid = ((ipc_data_signal)activeSignalList.toArray()[i]).sigid;
			signal.freshtime = ((ipc_data_signal)activeSignalList.toArray()[i]).freshtime;
			signal.severity = ((ipc_data_signal)activeSignalList.toArray()[i]).severity;
			signal.value = ((ipc_data_signal)activeSignalList.toArray()[i]).value;
			signal.value_type = ((ipc_data_signal)activeSignalList.toArray()[i]).value_type;
			signal.equipid = ((ipc_data_signal)activeSignalList.toArray()[i]).equipid;
			
			ipc_cfg_signal signalcfg = get_cfg_signal(equipID,signal.sigid);
			
			signal.name = (null == signalcfg ? "" : signalcfg.name);
			signal.unit = (null == signalcfg ? "" : signalcfg.unit);
			
			signalList.add(signal);
		}
		return signalList;
	}
	
	public static String get_event_name(int equID, int eventID)
	{
		List<ipc_cfg_event>  cfgEventList =	 get_event_cfg_list(IP,PORT,equID);
		String name = "" ;
		for(int i = 0; i < cfgEventList.size(); ++i)
		{
			if(((ipc_cfg_event)cfgEventList.toArray()[i]).id == eventID)
				name =  ((ipc_cfg_event)cfgEventList.toArray()[i]).name;
		}
		return name;
	}
	
	public static ipc_cfg_signal get_cfg_signal(int equID, int signalID)
	{
		List<ipc_cfg_signal>  cfgSignalList =	 get_signal_cfg_list(IP,PORT,equID);
		ipc_cfg_signal signal = null ;
		for(int i = 0; i < cfgSignalList.size(); ++i)
		{
			if(((ipc_cfg_signal)cfgSignalList.toArray()[i]).id == signalID)
				signal =  ((ipc_cfg_signal)cfgSignalList.toArray()[i]);
		}
		return signal;
	}
	

	public static int get_Equipment_State(int equipID)
	{
		List<ipc_active_event> alarmList = get_equip_active_alarm_list(IP,PORT,equipID);
		int state = 0;
		
		if(alarmList.isEmpty())
		{
			return 0;
		}
		for (int i = 0; i < alarmList.size(); ++i) 
		{
			if (10001 == ((ipc_active_event)alarmList.toArray()[i]).eventid) 
			{
				state = 1;
			}
		}
	   if(state==1 && alarmList.size()>0)
		{
			return 1;
		}
		else
		{
			return 2;
		}
	}
	
	public static List<ipc_cfg_trigger_value> get_cfg_trigger_value(String ip_addr, int port, int equipid)
	{
		byte[] send_buf = protocol.build_query_event_trigger_value_list(equipid);
		byte[] recv_buf = send_and_receive(send_buf, ip_addr, port);
		
		return protocol.parse_query_event_trigger_value_ack(recv_buf);
	}
	
	public static int set_cfg_trigger_value(String ip_addr, int port, List<ipc_cfg_trigger_value> trig_values) {
		byte[] send_buf = protocol.build_set_event_trigger_value(trig_values);
		byte[] recv_buf = send_and_receive(send_buf, ip_addr, port);
		
		return protocol.parse_set_event_trigger_value_ack(recv_buf);
	}

	public static int set_cfg_signal_name(String ip_addr, int port, List<ipc_cfg_signal_name> signal_name) {
		byte[] send_buf = protocol.build_set_signal_name(signal_name);
		byte[] recv_buf = send_and_receive(send_buf, ip_addr, port);
		
		return protocol.parse_set_signal_name_ack(recv_buf);
	}
	
}
