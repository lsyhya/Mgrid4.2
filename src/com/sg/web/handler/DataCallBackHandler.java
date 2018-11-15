package com.sg.web.handler;

import java.io.IOException;
import java.util.Map;

import org.apache.httpcore.HttpException;
import org.apache.httpcore.HttpRequest;
import org.apache.httpcore.HttpResponse;
import org.apache.httpcore.protocol.HttpContext;

import com.mgrid.main.MGridActivity;
import com.sg.web.RC_LabelObject;
import com.sg.web.base.ViewObjectSetCallBack;
import com.sg.web.object.HisEventOb;
import com.sg.web.object.RC_LabelOb;
import com.sg.web.object.SaveEquiptOb;
import com.yanzhenjie.andserver.RequestHandler;
import com.yanzhenjie.andserver.RequestMethod;
import com.yanzhenjie.andserver.annotation.RequestMapping;
import com.yanzhenjie.andserver.util.HttpRequestParser;

import android.util.Log;

public class DataCallBackHandler implements RequestHandler  {

	@RequestMapping(method = { RequestMethod.POST })
	@Override
	public void handle(HttpRequest request, HttpResponse response, HttpContext context)
			throws HttpException, IOException {
		
		Log.e("TAG", "DataCallBackHandler");
		Map<String, String> params=HttpRequestParser.parseParams(request);
		String typeId=params.get("typeId");
		String titleName = params.get("titleName");			
		Map<String, ViewObjectSetCallBack> map = MGridActivity.ViewSetBackObject.get(titleName);
		ViewObjectSetCallBack back = map.get(typeId);
	
		if(typeId.contains("SaveEquipt"))
		{
		
								
			SaveEquiptOb ob=new SaveEquiptOb();
			ob.setEquipName(params.get("equip"));
	        ob.setTime(params.get("time"));
			ob.setResponse(response);
			
			back.onControl(ob);
		}else if(typeId.contains("HisEvent"))
		{
			
			HisEventOb ob=new HisEventOb();
			ob.setEquipName(params.get("equip"));
	        ob.setStartTime(params.get("startTime"));
	        ob.setEndTime(params.get("endTime"));
			ob.setResponse(response);
			
			back.onControl(ob);
			
		}else if(typeId.contains("RC_Label"))
		{
			
			RC_LabelOb ob=new RC_LabelOb();
			
	
			ob.setResponse(response);
			ob.setValue(params.get("value"));
			
			back.onControl(ob);
			
		}
		
		
	}

}
