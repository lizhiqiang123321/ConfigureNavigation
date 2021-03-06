package com.sample.http;

import util.XmlToHtmlJs;

import java.awt.*;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;

public class ServerTest {
	public static void main(String[] args) {
		//声明变量
                ServerSocket ss=null;
                Socket s=null;
                boolean flag=true;

                try {
                	int port=7893;
                	System.out.println("Server Port:"+port);
					ss=new ServerSocket(port, 1024);
                    initWebapp();
                    openDefaultBrowser();
					while(flag)
					{
						//接受客户端发送过来的Socket
						s=ss.accept();
						HttpCreatorImpl hci=new HttpCreatorImpl(s);
						HttpRequest request=hci.getHttpRequest();
						HttpResponse response=hci.getHttpResponse();
						HttpAccessProcessor hapi=hci.getHttpAccessProcessor();

						//	用于测试收到的信息
						if(request.isStaticResource())//处理静态信息
						{
							System.out.println("静态");
							hapi.processStaticResource(request, response);
							System.out.println("静态加载完成");
						}
 						else if(request.isDynamicResource())//处理动态请求
						{
							System.out.println("动态");
							hapi.processDynamicResource(request, response);
						} else {
							System.out.println("无法处理");
							hapi.sendError(404, request, response);
							//new XmlToHtmlJs().analysisXml();
							//System.out.println("生成html文件");
						}
						s.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}

	}

    private static void initWebapp() {
        XmlToHtmlJs xmlToHtmlJs = new XmlToHtmlJs();
        xmlToHtmlJs.analysisXml();
    }

    private static void extractHtml() {

    }

    //打开默认浏览器
    public static void openDefaultBrowser(){
        try {
            String os = System.getProperty("os.name");
            if(os.toLowerCase().startsWith("win")){
                URI uri = new URI("http://120.27.19.38:7893/welcome.html");
                Desktop.getDesktop().browse(uri);
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
