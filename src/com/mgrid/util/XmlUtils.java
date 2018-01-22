package com.mgrid.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class XmlUtils {

	private static XmlUtils xml = new XmlUtils();

	private XmlUtils() {

		File file = new File(path);
		if (file.isDirectory()) {
			String[] filelist = file.list();
			for (int i = 0; i < filelist.length; i++) {
				if (filelist[i].endsWith(".xml")
						&& !filelist[i].contains("MonitorUnitVTU")) {
					xmlFileListName.add(filelist[i]);
				}else if(filelist[i].contains("MonitorUnitVTU"))
				{
					Mainpath=filelist[i];
				}
			}

		} else {

		}
		try {
			dbf = DocumentBuilderFactory.newInstance();
			db = dbf.newDocumentBuilder();

		} catch (Exception e) {

			e.printStackTrace();
		}
		parseVTUIOXml();

	}

	public static XmlUtils getXml() {
		return xml;
	}

	public List<String> getFileListName() {
		return xmlFileListName;
	}
	


	public NodeList getNodeList(String name, String EquipTemplateId) {
		for (String s : xmlFileListName) {
			DangQianFile = new File(path + "/" + s);
			if (DangQianFile.exists()) {
				try {
					doc = db.parse(DangQianFile);
					doc.normalize();
				} catch (Exception e) {

					e.printStackTrace();
				}
				NodeList ET_list = doc.getElementsByTagName("EquipTemplate");
				Element element1 = (Element) ET_list.item(0);
				String ET_Id = element1.getAttribute("EquipTemplateId");
				if (!ET_Id.equals(EquipTemplateId)) {
					continue;
				}
				nodelist = doc.getElementsByTagName(name);
				break;
			}
		}
		return nodelist;
	}
	
	public String getTemplateId(String equipId)
	{
		
	    File  Vtu=new File(path+"/"+Mainpath);
	    if(!Vtu.exists())
	    {
	    	System.out.println("不存在");
	    	return "";
	    }
	    String EquipTemplateId="";
	    try {
			doc=db.parse(Vtu);
			
		} catch (Exception e) {
			
			e.printStackTrace();
		} 
		NodeList list = doc
				.getElementsByTagName("CfgEquipment");
		System.out.println("屏蔽大小："+list.getLength());
		if (list == null)
			return "";
		for (int i = 0; i < list.getLength(); i++) {
			Element element1 = (Element) list.item(i);
			String EquipId = element1.getAttribute("EquipId");
			if (EquipId.equals(equipId)) {
			EquipTemplateId = element1
						.getAttribute("EquipTemplateId");
			//System.out.println(EquipTemplateId);
			break;
			}
		}
        return EquipTemplateId;
	}
	
	
	private void parseVTUIOXml()
	{
		for (String s : xmlFileListName) {
		if (s.equals("EquipmentTemplateVTUIO.xml")||s.contains("EquipmentTemplateVTUIO")) {
			nowFile = new File(path + "/" + s);
			if (nowFile.exists()) {
				try {
					doc = db.parse(nowFile);
					doc.normalize();
				} catch (Exception e) {

					e.printStackTrace();
				}
				Commandlist = doc
						.getElementsByTagName("EquipCommand");

			}
		}
		}
	}
	
	public NodeList getCommandlist() {
	   
		  return Commandlist;
	   
	}

	public void saveXmlData() {
		// 保存数据
		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer tran;
		try {
			tran = tf.newTransformer();
			DOMSource dom = new DOMSource(doc);
			// 设置编码类
			tran.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
			// 设置根结点换行问题
			tran.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, "");
			tran.setOutputProperty("{http://xml.apache.org/xslt}indent-amount",
					"4");
			tran.setOutputProperty(OutputKeys.INDENT, "yes");
			// tran.setOutputProperty(OutputKeys.INDENT, "yes");
			StreamResult result = new StreamResult(new FileOutputStream(
					DangQianFile));
			tran.transform(dom, result);
		} catch (Exception e) {

		}
	}

	public NodeList nodelist = null;
	public NodeList Commandlist=null;
	public List<String> xmlFileListName = new ArrayList<String>();
	private DocumentBuilderFactory dbf = null;
	private DocumentBuilder db = null;
	private Document doc = null;
	private String path = "/data/mgrid/sampler/XmlCfg";
	public  String Mainpath="/data/mgrid/sampler/XmlCfg/MonitorUnitVTU.xml";
	                                                  
	private  File DangQianFile = null;
	private  File nowFile = null;

}
