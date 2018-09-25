package com.sg.web.HTML;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;

public class BuildHtml {
	
	public static void buildHtml(String path,String dataName)
	{
		
		StringBuilder sbHtml=new StringBuilder();
		
		try {
			
			File file=new File(path);
			if(!file.exists())
			{
				file.createNewFile();
			}
			
			PrintStream ps=new PrintStream(new FileOutputStream(file));
			sbHtml.append("<html>\r\n" + 
					"<head>\r\n" + 
					"\r\n" + 
					"    <title>"+dataName+"</title>\r\n" + 
					"\r\n" + 
					"    <script src=\"js/myData.js\"></script>\r\n" + 
					"    <script src=\"js/echarts.js\"></script>\r\n" + 
					"    <script src=\"js/BuildHtml.js\"></script>\r\n" + 
					"    <script src=\"js/jquery.min.js\"></script>\r\n" + 
					"    <script src=\"js/jquery.form.js\"></script>\r\n" + 
					"\r\n" + 
					"  <script type=\"text/javascript\">\r\n" + 
					"\r\n" + 
					"\r\n" + 
					"\r\n" + 
					"      function refreshOnTime(){\r\n" + 
					"\r\n" + 
					"          var title=$(document).attr('title');\r\n" + 
					"\r\n" + 
					"          $.ajax({\r\n" + 
					"              url :  \"getdata\",	//请求url\r\n" + 
					"              type : \"POST\",	//请求类型  post|get\r\n" + 
					"              data : \"titleName=\"+title,	//后台用 request.getParameter(\"key\");\r\n" + 
					"              dataType : \"json\",  //返回数据的 类型 text|json|html--\r\n" + 
					"              success : function(users){	//回调函数 和 后台返回的 数据\r\n" + 
					"\r\n" + 
					"                  if(users!='fail')\r\n" + 
					"                  {\r\n" + 
					"\r\n" + 
					"\r\n" + 
					"\r\n" + 
					"                      for(var i=0; i<users.length; i++){\r\n" + 
					"\r\n" + 
					"                          upadate(users[i],users[i].type,users[i].typeId,users[i].value);\r\n" + 
					"                      }\r\n" + 
					"\r\n" + 
					"\r\n" + 
					"                  }else\r\n" + 
					"                  {\r\n" + 
					"                      alert(users);\r\n" + 
					"                  }\r\n" + 
					"\r\n" + 
					"\r\n" + 
					"              }\r\n" + 
					"          });\r\n" + 
					"      }\r\n" + 
					"\r\n" + 
					"\r\n" + 
					"  $(function(){\r\n" + 
					"\r\n" + 
					"\r\n" + 
					"\r\n" + 
					"		$.ajax({\r\n" + 
					"			url :  \"test\",	//请求url\r\n" + 
					"			type : \"POST\",	//请求类型  post|get\r\n" + 
					"            data : \"titleName="+dataName+"\",	//后台用 request.getParameter(\"key\");\r\n" + 
					"			dataType : \"json\",  //返回数据的 类型 text|json|html--\r\n" + 
					"			success : function(users){	//回调函数 和 后台返回的 数据\r\n" + 
					"\r\n" + 
					"                if(users!='fail')\r\n" + 
					"                {\r\n" + 
					"\r\n" + 
					"                   // var wh=window.screen.availWidth/1024;\r\n" + 
					"                    $(\"div.Main\").css({\"position\":\"absolute\",\"left\":\"0px\",\"top\":\"0px\",\"background-color\":\"white\",\"height\":window.screen.availHeight,\"width\":window.screen.availWidth});\r\n" + 
					"\r\n" + 
					"                    for(var i=0; i<users.length; i++){\r\n" + 
					"\r\n" + 
					"                       var l=users[i].left/users[i].fromWight*window.screen.availWidth;\r\n" + 
					"                       var t=users[i].top/users[i].fromHeight*window.screen.availHeight;\r\n" + 
					"                       var h=users[i].heght/users[i].fromHeight*window.screen.availHeight;\r\n" + 
					"                       var w=users[i].wight/users[i].fromWight*window.screen.availWidth;\r\n" + 
					"\r\n" + 
					"\r\n" + 
					"                       init(users[i],users[i].type,users[i].typeId,'absolute',users[i].ZIndex,l,t,h,w);\r\n" + 
					"                    }\r\n" + 
					"                    //refreshOnTime($(document).attr('title'));\r\n" + 
					"                    setInterval(refreshOnTime, 7000);\r\n" + 
					"\r\n" + 
					"                }else\r\n" + 
					"                {\r\n" + 
					"                    alert(users);\r\n" + 
					"                }\r\n" + 
					"\r\n" + 
					"			}\r\n" + 
					"		});\r\n" + 
					"	});\r\n" + 
					"\r\n" + 
					"</script>\r\n" + 
					"\r\n" + 
					"</head>\r\n" + 
					"\r\n" + 
					"<body>\r\n" + 
					"<div class=\"Main\">\r\n" + 
					"\r\n" + 
					"</div>\r\n" + 
					"\r\n" + 
					"</body>\r\n" + 
					"</html>\r\n" + 
					"");
	
			
			  ps.println(sbHtml.toString());
			  ps.flush();
			  ps.close();
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		
	}

}
