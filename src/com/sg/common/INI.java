package com.sg.common;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public final class INI {
	private final static Map<String, Map<String, Object>> iniFile = new HashMap<String, Map<String, Object>>();

	private INI() {

	}

	final public static synchronized void setValue(String section, String key,
			Object value) {
		Map<String, Object> sectionMap = iniFile.get(section);

		if (sectionMap == null) {
			sectionMap = new HashMap<String, Object>();
			iniFile.put(section, sectionMap);
		}
		sectionMap.put(key, value);
	}

	final public static synchronized Object getValue(String section, String key) {
		Object obj = null;
		Map<String, Object> item = iniFile.get(section);
		if (item != null) {
			obj = item.get(key);
		}
		return obj;
	}

	final public static synchronized void load(String path) throws IOException {
		iniFile.clear();  // 每次加载前清空
		
		DataInputStream ds = new DataInputStream(new FileInputStream(path));
		String str = ds.readLine();
		
		String section = null;
		while (str != null) {
			str = str.trim();  // 去除前后空格
			 if (str.isEmpty())
			 {
				 str = ds.readLine();
				 continue;
			 }
			
			// System.out.println(str);
			if (str.startsWith("//"))
			{
				str = ds.readLine();
				continue;  // 跳过注释
			}
			
			if (str.startsWith("[")) {
				Map<String, Object> itemMap = new HashMap<String, Object>();
				section = str.substring(1, str.length() - 1);
				section = section.trim();  // 去除前后空格
				// System.out.println(section);
				iniFile.put(section, itemMap);
			} else {
				Map<String, Object> itemMap = iniFile.get(section);
				
				String key = str.substring(0, str.indexOf("="));
				
				if (key == null)
				{
					str = ds.readLine();
					continue;
				}
				key = key.trim();
				if (key.isEmpty())
				{
					str = ds.readLine();
					continue;
				}
				
				String value = str.substring(str.indexOf("=") + 1);
				
				if (value == null) value = "";
				value = value.trim();
				
				itemMap.put(key, value);
			}

			str = ds.readLine();
		}
		
		ds.close();
	}

	final public static synchronized void write(String name) throws IOException {
		StringBuilder sb = new StringBuilder("");
		for (String section : iniFile.keySet()) {
			sb.append("[").append(section).append("]").append("\n");
			Map<String, Object> map = iniFile.get(section);
			Set<String> keySet = map.keySet();
			for (String key : keySet) {
				sb.append(key).append("=").append(map.get(key)).append("\n");
			}

		}

		File file = new File(name);
		if (!file.exists()) {
			file.createNewFile();

		}
		try {

			OutputStream os = new FileOutputStream(file);
			os.write(sb.toString().getBytes());
			os.flush();
			os.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
