package com.sg.web.handler;

import java.io.IOException;
import java.util.Map;

import org.apache.httpcore.HttpException;
import org.apache.httpcore.HttpRequest;
import org.apache.httpcore.HttpResponse;
import org.apache.httpcore.entity.StringEntity;
import org.apache.httpcore.protocol.HttpContext;

import com.alibaba.fastjson.JSON;
import com.mgrid.main.MGridActivity;
import com.yanzhenjie.andserver.RequestHandler;
import com.yanzhenjie.andserver.RequestMethod;
import com.yanzhenjie.andserver.annotation.RequestMapping;
import com.yanzhenjie.andserver.util.HttpRequestParser;

import android.util.Log;

public class SendDataHandler implements RequestHandler{

	@RequestMapping(method = {RequestMethod.POST})
	@Override
	public void handle(HttpRequest request, HttpResponse response, HttpContext context)
			throws HttpException, IOException {
		
		
		
		   Log.e("SendDataHandler","我进来了");

		   Map<String, String> params = HttpRequestParser.parseParams(request);	       
	 
	
		   
		  
		   
		  if(params.containsKey("titleName"))
		  {
			  
			  String json=JSON.toJSON(MGridActivity.ViewJosnObject.get(params.get("titleName"))).toString();	
			  Log.e("json", MGridActivity.ViewJosnObject.get(params.get("titleName")).size()+"");
			  Log.e("json", json);
			  StringEntity stringEntity = new StringEntity(json, "utf-8");
			  response.setStatusCode(200);
			  response.setEntity(stringEntity);
			  
		  }else
		  {
			  
			  Log.e("", "失败了");
			  StringEntity stringEntity = new StringEntity("fail", "utf-8");
			  response.setStatusCode(200);
			  response.setEntity(stringEntity); 
			  
		  }
		
	}

}
