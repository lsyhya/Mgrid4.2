package comm_service;

import android.annotation.SuppressLint;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//author: fjw0312
//date: 2015 08 10

@SuppressLint("SimpleDateFormat")
public class local_file {

	String his_equip_path = "/mgrid/log/his_equip_sig/";
	String his_signal_path = "/mgrid/log/his_signal/";
	String his_event_path = "/mgrid/log/event/";
	String RC_signal = "/mgrid/log/RC_signal/";
	String his_curve = "/mgrid/log/his_curve/";
	String name = "test.dat";
	File file = null;
	Writer fws = null;
	Reader frs = null;
	InputStream fis = null;
	OutputStream fos = null;
	BufferedReader bufread = null;
	PrintWriter priwrite = null;
	public static int num_signal = 0;
	public static int w_line_num = 0;
	public static int r_line_num = 0;

	public boolean has_file(String filename, int who) {
		if (who == 1)
			file = new File(his_equip_path + filename + ".dat");
		if (who == 2)
			file = new File(his_signal_path + filename + ".dat");
		if (who == 3)
			file = new File(his_event_path + filename + ".dat");
		if (who == 4)
			file = new File(RC_signal + filename + ".dat");
		if (who == 5)
			file = new File(his_curve + filename + ".dat");
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				return false;
			}
		}

		if (!file.canRead()) {

			return false;
		}

		if (!file.canWrite()) {

			return false;
		}

		return true;
	}

	public boolean write_buf(String buf) {

		try {
			fws = new FileWriter(file);
			fws.write(buf);
			fws.close();
		} catch (Exception e) {

		}

		return true;
	}

	@SuppressWarnings("unused")
	public String read_buf(String buf) {

		try {
			char c[] = new char[100];
			frs = new FileReader(file);
			for (int i = 0; (frs.read(c)) != -1; i++)
				;
			frs.close();
			buf = c.toString();
		} catch (Exception e) {

		}

		return buf;
	}

	public boolean write_byte(int buf) {

		try {
			fos = new FileOutputStream(file);
			fos.write(buf);
			fos.close();
		} catch (Exception e) {

		}

		return true;
	}

	public int[] read_byte(int[] buf) {

		try {
			fis = new FileInputStream(file);
			for (int i = 0; (buf[i] = fis.read()) != -1; i++)
				;
			fis.close();
		} catch (Exception e) {

		}

		return buf;
	}

	public boolean write_line(String buf) {
		w_line_num++;
		if (w_line_num > 20000000)
			w_line_num = 0;

		try {
			priwrite = new PrintWriter(new FileWriter(file, true), true);
			priwrite.println(buf);
			priwrite.close();
			
		} catch (Exception e) {

		}

		return true;
	}

	public String read_line() {
		r_line_num++;
		if (r_line_num > 20000000)
			r_line_num = 0;

		try {
			bufread = new BufferedReader(new FileReader(file));
			if ((buf_line = bufread.readLine()) == null) {
				bufread.close();
				return null;
			}
			bufread.close();
		} catch (Exception e) {

			return null;
		}

		return buf_line;
	}

	public String read_later_line() {

		try {
			bufread = new BufferedReader(new FileReader(file));
			while ((buf_later_line = bufread.readLine()) != null) {
			}

			bufread.close();
		} catch (Exception e) {

			return null;
		}

		return buf_later_line;
	}

	public boolean read_all_line() {

		r_line_num = 0;
		String buf = new String();

		buflist1.clear();

		try {
			bufread = new BufferedReader(new FileReader(file));
			//bufread = new BufferedReader(new InputStreamReader(new FileInputStream(file),"GB2312"));
			while ((buf = bufread.readLine()) != null) {
				buflist1.add(buf);
				r_line_num++;
			}
			bufread.close();
		} catch (Exception e) {

			return false; 
		}
        
		return true;
	}
	
	@SuppressWarnings("unused")
	public boolean read_time_line(long starttime ,long endtime )
	{
		r_line_num = 0;
		String buff = new String();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date dt = null;

		try {
			bufread = new BufferedReader(new FileReader(file));
			while ((buff = bufread.readLine()) != null) {
				String[] list=buff.split(",");
				dt = sdf.parse(list[0]);
				long lTime = dt.getTime();
				if(starttime<lTime&&lTime<endtime) r_line_num++;
			}
			bufread.close();
		} catch (Exception e) {

			return false;
		}

		return true;
	}

	public String read_str_line(String buf) {
		buf_str_line = "";
		String buff = "";
		try {
			bufread = new BufferedReader(new FileReader(file));
			while ((buff = bufread.readLine()) != null) {

				if (buff.contains(buf)) {
					buf_str_line = buff;
					break;
				}
			}
			bufread.close();
		} catch (Exception e) {

			return buf_str_line;
		}
		return buf_str_line;
	}
	
	public List<String> read_str_all_line(String buf) {
		String buff = "";
		buflist2.clear();
		try {
			bufread = new BufferedReader(new FileReader(file));
			while ((buff = bufread.readLine()) != null) {

				if (buff.contains(buf)) {
					buflist2.add(buff);
				}
			}
			bufread.close();
		} catch (Exception e) {

			return buflist2;
		}
		return buflist2;
	}
	

	public List<String> buflist1 = new ArrayList<String>();
	public List<String> buflist2 = new ArrayList<String>();
	String buf_line = new String();
	String buf_later_line = new String();
	String buf_str_line = new String();

}
