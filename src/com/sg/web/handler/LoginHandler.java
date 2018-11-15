package com.sg.web.handler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.httpcore.HttpException;
import org.apache.httpcore.HttpRequest;
import org.apache.httpcore.HttpResponse;
import org.apache.httpcore.entity.StringEntity;
import org.apache.httpcore.protocol.HttpContext;

import com.alibaba.fastjson.JSON;
import com.mgrid.main.MGridActivity;
import com.mgrid.util.PageChangeUtil;
import com.yanzhenjie.andserver.RequestHandler;
import com.yanzhenjie.andserver.RequestMethod;
import com.yanzhenjie.andserver.annotation.RequestMapping;
import com.yanzhenjie.andserver.util.HttpRequestParser;

import android.util.Log;

public class LoginHandler implements RequestHandler {

	
	
	@RequestMapping(method = { RequestMethod.POST })
	@Override
	public void handle(HttpRequest arg0, HttpResponse arg1, HttpContext arg2) throws HttpException, IOException {

		Log.e("LoginHandler", "我进来了");

		Map<String, String> params = HttpRequestParser.parseParams(arg0);

		String user=params.get("user");
		String password=params.get("password");
		
		Log.e("TAG", user+":"+password);
		
		
		
		PageChangeUtil page=new PageChangeUtil();
		boolean index=page.judgeUserPW(user, password);
		
		if(MGridActivity.userManager.getUserManaget().size()>0&&index)
		{
			Map<String,String> map=new HashMap<String,String>();
			map.put("user", user);
			map.put("password", password);
			map.put("html", MGridActivity.m_sMainPage.replace(".xml", ".html"));
			String json = JSON.toJSON(map).toString();
			StringEntity stringEntity = new StringEntity(json, "utf-8");
			arg1.setStatusCode(200);
			arg1.setEntity(stringEntity);
		}else if(MGridActivity.userManager.getUserManaget().size()==0)
		{
			
			if(user.equals("admin")&&password.equals("12348765"))
			{
				Map<String,String> map=new HashMap<String,String>();
				map.put("user", user);
				map.put("password", password);
				map.put("html", MGridActivity.m_sMainPage.replace(".xml", ".html"));
				String json = JSON.toJSON(map).toString();
				StringEntity stringEntity = new StringEntity(json, "utf-8");
				arg1.setStatusCode(200);
				arg1.setEntity(stringEntity);
			}else
			{
				StringEntity stringEntity = new StringEntity("Fail", "utf-8");
				arg1.setStatusCode(300);
				arg1.setEntity(stringEntity);
			}
			
		}
		else
		{
			StringEntity stringEntity = new StringEntity("Fail", "utf-8");
			arg1.setStatusCode(300);
			arg1.setEntity(stringEntity);
		}
		

		

	}

}