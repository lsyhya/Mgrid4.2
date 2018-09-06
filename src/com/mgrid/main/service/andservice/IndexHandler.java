package com.mgrid.main.service.andservice;

import java.io.IOException;

import org.apache.httpcore.HttpException;
import org.apache.httpcore.HttpRequest;
import org.apache.httpcore.HttpResponse;
import org.apache.httpcore.protocol.HttpContext;

import com.yanzhenjie.andserver.RequestHandler;
import com.yanzhenjie.andserver.RequestMethod;
import com.yanzhenjie.andserver.annotation.RequestMapping;

import android.util.Log;

public class IndexHandler implements RequestHandler{

	@RequestMapping(method = {RequestMethod.POST})
	@Override
	public void handle(HttpRequest arg0, HttpResponse arg1, HttpContext arg2) throws HttpException, IOException {
		
		
		  Log.e("HEHE","我进来了");
		
		
	}
}
