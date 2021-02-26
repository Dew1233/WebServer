package com.webserver.servlet;

import com.webserver.http.HttpRequest;
import com.webserver.http.HttpResponse;

/**
 * 这个类是所有Servlet的超类
 */
public abstract class HttpServlet {
    public void service(HttpRequest request, HttpResponse response){
        String method = request.getMethod();//获取本次请求的请求方法
        if("GET".equalsIgnoreCase(method)){
            doGet(request,response);
        }else if("POST".equalsIgnoreCase(method)){
            doPost(request,response);
        }
    }

    /**
     * 用于处理Get请求
     * @param request
     * @param response
     */
    public abstract void doGet(HttpRequest request,HttpResponse response);

    /**
     * 用于处理Post请求
     * @param request
     * @param response
     */
    public abstract void doPost(HttpRequest request,HttpResponse response);
}
