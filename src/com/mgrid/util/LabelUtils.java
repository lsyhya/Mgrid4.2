package com.mgrid.util;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import android.content.Context;
import android.widget.Toast;

import com.mgrid.main.MGridActivity;
import com.mgrid.main.MainWindow;
import com.sg.common.IObject;
import com.sg.common.UtExpressionParser.stBindingExpression;
import com.sg.uis.SgIsolationEventSetter;
import com.sg.uis.SgLabel;

public class LabelUtils {

	NodeList list1=null;
	Context context=null;
	
	public LabelUtils(Context context)
	{
		this.context=context;
	}
	
	public void setDoubleButton(MainWindow m_oSgSgRenderManager)
	{
		Iterator<String> iter_Double =m_oSgSgRenderManager.m_mapUIs.keySet().iterator();		
		while (iter_Double.hasNext()) {
			String strKey = iter_Double.next();
			IObject obj = m_oSgSgRenderManager.m_mapUIs.get(strKey);
            if(obj.getType().equals("DoubleImageButton"))
            {
            	SgIsolationEventSetter sgIE=(SgIsolationEventSetter)obj;
            	if(sgIE.isIn)
            		sgIE.setEnabled();
            	
            }	
           
		}
	}
	
	public  ArrayList<String>  getButtonId()
	{

		ArrayList<String> list =new ArrayList<String>();

				    if(list1==null)	
				    	list1=getList();
				    if(list1==null)
				    {
				    	return null;
				    }
				    
			        for (int i = 0; i < list1.getLength(); i++) {   				
					Element element1 = (Element) list1.item(i);
					String EquipId=element1.getAttribute("EquipId");

					String EventLocked=element1.getAttribute("EventLocked");
					if(EventLocked.equals("true"))
					{
						list.add(EquipId+"");
						
					}
			      }	

		return  list;
	}
	
	
	public NodeList getList()
	{
		    DocumentBuilderFactory	dbf=null;
			DocumentBuilder db=null;
			Document doc=null;
			try {
			 dbf = DocumentBuilderFactory.newInstance();
			 db = dbf.newDocumentBuilder();
			 File f=new File("/data/mgrid/sampler/XmlCfg/MonitorUnitVTU.xml");
			 if(f.exists()){
			 doc = db.parse(f);			
			 }
			 else{			 
			 return null; 
			 }
		} catch (Exception e) {
			
			e.printStackTrace();
		}        				   			  
	    NodeList list = doc.getElementsByTagName("CfgEquipment");	
		return   list;
	}
	



	
}
