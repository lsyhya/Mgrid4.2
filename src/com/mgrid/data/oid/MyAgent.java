package com.mgrid.data.oid;

import java.io.IOException;
import java.net.UnknownHostException;

import org.snmp4j.CommandResponder;
import org.snmp4j.CommandResponderEvent;
import org.snmp4j.MessageException;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.TransportMapping;
import org.snmp4j.mp.StateReference;
import org.snmp4j.mp.StatusInformation;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.VariableBinding;

import com.mgrid.mysqlbase.SqliteUtil;

import android.util.Log;

public class MyAgent {

	private SqliteUtil sqliteUtil;

	public MyAgent(SqliteUtil sqliteUtil) {
		this.sqliteUtil = sqliteUtil;
	}

	public void openAgent() {

		Handler handler = new Handler("192.168.1.152", 4701);
		handler.start();
		Log.e("MyAgent", "----------loop-------------");

	}

	class Handler implements CommandResponder {

		protected String mAddress = null;
		protected int mPort = 0;
		protected TransportMapping mServerSocket = null;
		protected Snmp mSNMP = null;

		public Handler(String mAddress, int mPort) {

			this.mAddress = mAddress;
			this.mPort = mPort;

		}

		public void start() {
			try {
				mServerSocket = new org.snmp4j.transport.DefaultUdpTransportMapping(
						new org.snmp4j.smi.UdpAddress(java.net.InetAddress.getByName(mAddress), mPort));
				mSNMP = new org.snmp4j.Snmp(mServerSocket);
				mSNMP.addCommandResponder(this);
				mServerSocket.listen();
			} catch (UnknownHostException e) {

				e.printStackTrace();

			} catch (IOException e) {

				e.printStackTrace();
			}
		}

		@Override
		public void processPdu(CommandResponderEvent aEvent) {

			PDU vPDU = aEvent.getPDU();

			if (vPDU == null) {
				return;
			}

			StatusInformation statusInformation = new StatusInformation();
			StateReference ref = aEvent.getStateReference();
			OID oid = vPDU.get(0).getOid();
			
			
			Log.e("oid", oid.toString());

			switch (vPDU.getType()) {
			
			case PDU.GET:

				
				
				
				try {

					vPDU.setType(PDU.RESPONSE);


					String value = sqliteUtil.getOIDValue("."+oid.toString());
					
					vPDU.set(0, new VariableBinding(oid, new OctetString(value)));

					aEvent.getMessageDispatcher().returnResponsePdu(aEvent.getMessageProcessingModel(),

							aEvent.getSecurityModel(), aEvent.getSecurityName(),

							aEvent.getSecurityLevel(), vPDU, aEvent.getMaxSizeResponsePDU(), ref,

							statusInformation);

				} catch (org.snmp4j.MessageException e) {
					e.printStackTrace();
				}

				break;

			case PDU.GETNEXT:

				break;
			case PDU.SET:

				break;

			case PDU.GETBULK:

				break;

			}

		}
	}

}
