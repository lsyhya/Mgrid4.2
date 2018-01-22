package comm_service;

public class msg_head {
	public byte soh;
	public byte version;
	public short sn;
	public short cmd;
	public int length;
	public byte ret_code;
	public int para;
}
