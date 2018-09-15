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
					"");
			sbHtml.append("  <script src=\"https://ajax.aspnetcdn.com/ajax/jQuery/jquery-3.2.1.js\"></script>\r\n" + 
					"\r\n" + 
					"    <script type=\"text/javascript\" src=\"http://malsup.github.io/jquery.form.js\"></script>\r\n" + 
					"");
			sbHtml.append("<script type=\"text/javascript\">\r\n");
			sbHtml.append("$(function(){\r\n" + 
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
					"\r\n" + 
					"                    }\r\n" + 
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
					"");
			
			sbHtml.append("function init(user,type,typeId, pos,index, x, y, h, w){\r\n" + 
					"\r\n" + 
					"	switch(type)\r\n" + 
					"	{\r\n" + 
					"\r\n" + 
					"	    case \"Label\":\r\n" + 
					"\r\n" + 
					"\r\n" + 
					"	        $(\"div.Main\").append(\"  <font  class='Label'></font>  \");\r\n" + 
					"            $('font.Label').removeClass(\"Label\").addClass(typeId);\r\n" + 
					"            $('font.'+typeId).text(user.text);\r\n" + 
					"	        $('font.'+typeId).css({\"position\":pos,'z-index':index,\"left\":x,\"top\":y,\"font-size\":user.textSize,\"height\":h,\"width\":w,'text-align':'center','color':user.textColor,'line-height':h+'px'});\r\n" + 
					"\r\n" + 
					"            break;\r\n" + 
					"	    case \"Rectangle\":\r\n" + 
					"\r\n" + 
					"\r\n" + 
					"            $(\"div.Main\").append(\"  <div  class='Label'></div>  \");\r\n" + 
					"            $('div.Label').removeClass(\"Label\").addClass(typeId);\r\n" + 
					"\r\n" + 
					"             var bColor;\r\n" + 
					"             if(user.borderColor==\"#FFFFFFFF\")\r\n" + 
					"            {\r\n" + 
					"                bColor='transparent';\r\n" + 
					"            }else\r\n" + 
					"             {\r\n" + 
					"                 bColor=user.borderColor;\r\n" + 
					"             }\r\n" + 
					"\r\n" + 
					"            if(user.bgColor==\"#FFFFFFFF\")\r\n" + 
					"            {\r\n" + 
					"                $('div.'+typeId).css({\"position\":pos,'z-index':index,\"left\":x,\"top\":y,\"background-color\":\"transparent\",\"height\":h,\"width\":w,'border':'solid '+user.borderWidth+' '+bColor});\r\n" + 
					"            }else{\r\n" + 
					"                $('div.'+typeId).css({\"position\":pos,'z-index':index,\"left\":x,\"top\":y,\"background-color\":user.bgColor,\"height\":h,\"width\":w,'border':'solid '+user.borderWidth+' '+bColor});\r\n" + 
					"            }\r\n" + 
					"\r\n" + 
					"	            break;\r\n" + 
					"\r\n" + 
					"        case \"Image\":\r\n" + 
					"\r\n" + 
					"\r\n" + 
					"\r\n" + 
					"\r\n" + 
					"            $(\"div.Main\").append(\"  <a class='Label'><img  class='Label' alt='fail'/></a>  \");\r\n" + 
					"            $('img.Label').removeClass(\"Label\").addClass(typeId);\r\n" + 
					"            $('a.Label').removeClass(\"Label\").addClass(typeId);\r\n" + 
					"            $('img.'+typeId).css({\"position\":pos,'z-index':index,\"left\":x,\"top\":y,\"alt\":'fali',\"height\":h,\"width\":w});\r\n" + 
					"            $('img.'+typeId).attr(\"src\",user.imagePath);\r\n" + 
					"\r\n" + 
					"            if(user.hrefUrl!=undefined)\r\n" + 
					"            {\r\n" + 
					"                $('a.'+typeId).attr(\"href\",user.hrefUrl+\".html\");\r\n" + 
					"            }\r\n" + 
					"\r\n" + 
					"\r\n" + 
					"\r\n" + 
					"\r\n" + 
					"            break;\r\n" + 
					"");
			
			
			sbHtml.append("  case \"Table\":\r\n" + 
					"\r\n" + 
					"\r\n" + 
					"\r\n" + 
					"            $(\"div.Main\").append(\" <table class='Table' > </table> \");\r\n" + 
					"            $(\"table.Table\").removeClass(\"Table\").addClass(typeId);\r\n" + 
					"            $(\"table.\"+typeId).css({\"position\":pos,'z-index':index,\"left\":x,\"top\":y,\"height\":h,\"width\":w});\r\n" + 
					"            $(\"table.\"+typeId).attr(\"borderColor\",user.lineColor);\r\n" + 
					"            $(\"table.\"+typeId).attr(\"border\",\"1px\");\r\n" + 
					"            $(\"table.\"+typeId).attr(\"cellspacing\",\"0\");\r\n" + 
					"\r\n" + 
					"            for(var i=0;i<user.rowNUm;i++)\r\n" + 
					"            {\r\n" + 
					"                $(\"table.\"+typeId).append(\"<tr class='Tr'></tr>\");\r\n" + 
					"                $(\"tr.Tr\").removeClass(\"Tr\").addClass(typeId+'Tr'+i);\r\n" + 
					"\r\n" + 
					"                for(var j=0;j<user.colNum;j++)\r\n" + 
					"                {\r\n" + 
					"                    $(\"tr.\"+typeId+'Tr'+i).append(\"<td class='Td'></td>\");\r\n" + 
					"\r\n" + 
					"                    var classNames=typeId+'Td'+j+''+i;\r\n" + 
					"                    $(\"td.Td\").removeClass(\"Td\").addClass(classNames);\r\n" + 
					"                    $(\"td.\"+classNames).attr(\"bordercolor\",user.lineColor);\r\n" + 
					"                    if(i==0) {\r\n" + 
					"\r\n" + 
					"                        $(\"td.\"+classNames).css({\"height\":user.firstRow*h});\r\n" + 
					"\r\n" + 
					"                    }else\r\n" + 
					"                    {\r\n" + 
					"                        $(\"td.\"+classNames).css({\"height\":(h-user.firstRow*h)/(user.rowNUm-1)});\r\n" + 
					"                    }\r\n" + 
					"\r\n" + 
					"\r\n" + 
					"                    if(j==0)\r\n" + 
					"                    {\r\n" + 
					"\r\n" + 
					"                        $(\"td.\"+classNames).css({\"width\":user.firstCol*w});\r\n" + 
					"                    }else {\r\n" + 
					"                        $(\"td.\"+classNames).css({\"width\":(w-user.firstCol*w)/(user.colNum-1)});\r\n" + 
					"                    }\r\n" + 
					"\r\n" + 
					"                }\r\n" + 
					"            }\r\n" + 
					"\r\n" + 
					"\r\n" + 
					"\r\n" + 
					"\r\n" + 
					"            break;");
			
			
			sbHtml.append("\r\n" + 
					"        case \"Button\":\r\n" + 
					"\r\n" + 
					"           \r\n" + 
					"\r\n" + 
					"\r\n" + 
					"            $(\"div.Main\").append(\" <a class='Button'><input type='button' class='Button'/> </a>\");\r\n" + 
					"\r\n" + 
					"            $(\"a.Button\").removeClass(\"Button\").addClass(typeId);\r\n" + 
					"            $(\"input.Button\").removeClass(\"Button\").addClass(typeId);\r\n" + 
					"            $('input.'+typeId).css({\"position\":pos,'z-index':index,\"left\":x,\"top\":y,\"height\":h,\"width\":w});\r\n" + 
					"            $(\"input.\"+typeId).css({\"color\":user.textColor,\"font-size\":user.textSize});\r\n" + 
					"            $(\"input.\"+typeId).attr('value',user.text);\r\n" + 
					"\r\n" + 
					"            if(user.hrefUrl!=undefined)\r\n" + 
					"            {\r\n" + 
					"                $('a.'+typeId).attr(\"href\",user.hrefUrl+\".html\");\r\n" + 
					"            }\r\n" + 
					"\r\n" + 
					"\r\n" + 
					"\r\n" + 
					"            break;\r\n" + 
					"");
			
			sbHtml.append("	}\r\n" + 
					"\r\n" + 
					"\r\n" + 
					"}\r\n" + 
					"");
			
			  sbHtml.append("</script>\r\n" + 
			  		"\r\n" + 
			  		"</head>\r\n" + 
			  		"\r\n" + 
			  		"<body >\r\n" + 
			  		"<div class=\"Main\">\r\n" + 
			  		"\r\n" + 
			  		"\r\n" + 
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
