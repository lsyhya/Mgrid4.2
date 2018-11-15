package com.sg.web.handler;

import java.io.IOException;
import java.util.Map;

import org.apache.httpcore.HttpException;
import org.apache.httpcore.HttpRequest;
import org.apache.httpcore.HttpResponse;
import org.apache.httpcore.protocol.HttpContext;

import com.mgrid.main.MGridActivity;
import com.sg.web.base.ViewObjectSetCallBack;
import com.yanzhenjie.andserver.RequestHandler;
import com.yanzhenjie.andserver.RequestMethod;
import com.yanzhenjie.andserver.annotation.RequestMapping;
import com.yanzhenjie.andserver.util.HttpRequestParser;

import android.util.Log;

public class OnClickHandler implements RequestHandler {

	@RequestMapping(method = { RequestMethod.POST })
	@Override
	public void handle(HttpRequest request, HttpResponse response, HttpContext context)
			throws HttpException, IOException {

		
		
		Log.e("Tag", "OnClickHandler");
		Map<String, String> params = HttpRequestParser.parseParams(request);
		if (params.containsKey("titleName") && params.containsKey("typeId")) {

			String titleName = params.get("titleName");
			String typeId = params.get("typeId");

			Map<String, ViewObjectSetCallBack> map = MGridActivity.ViewSetBackObject.get(titleName);
			ViewObjectSetCallBack back = map.get(typeId);

			if (!params.containsKey("value")) {
				
				back.onControl("");
			} else {
				
				String value = params.get("value");
				back.onControl(value);
			}

		}

	}

}
