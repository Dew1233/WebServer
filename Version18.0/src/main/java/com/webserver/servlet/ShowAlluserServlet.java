package com.webserver.servlet;


import com.webserver.http.HttpRequest;
import com.webserver.http.HttpResponse;
import com.webserver.vo.User;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.FileTemplateResolver;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ShowAlluserServlet {
    public void service(HttpRequest request, HttpResponse response){
        System.out.println("ShowAllUserServlet：开始处理用户列表页面……");
        //先将user.dat文件中所有的用户信息读取出来
        List<User> list = new ArrayList<>();//保存所有用户记录的集合
        try(
                RandomAccessFile raf = new RandomAccessFile("user.dat","r");
                ){
            for (int i=0;i<raf.length()/100;i++){
                raf.seek(i*100);
                byte [] data = new byte[32];
                raf.read(data);
                String username = new String(data,"utf-8").trim();
                raf.read(data);
                String password = new String(data,"utf-8").trim();
                raf.read(data);
                String nickname = new String(data,"utf-8").trim();
                int age = raf.readInt();
                User user = new User(username,password,nickname,age);
                list.add(user);
                System.out.println(user);
            }

        }catch (IOException e){
            e.printStackTrace();
        }
        //2：使用thymeleaf将数据与静态页面userList.html结合生成动态页面
       //2.1创建Context实例，thymeleaf提供的，用于保存所有在页面上要显示的数据
        Context context = new Context();//类似于Map
        //将存放所有用户信息的List集合存在Context
        context.setVariable("list",list);//类似Map的key,value
       //2.2初始化thymeleaf模板引擎
        //模板解释器，用来告知模板引擎模板的相关情况（模板是要结合的静态页面）
        FileTemplateResolver resolver = new FileTemplateResolver();
        resolver.setTemplateMode("html");//模板是html
        resolver.setCharacterEncoding("UTF-8");
        //实例化模板引擎
        TemplateEngine te = new TemplateEngine();
        //将模板解释器设置给引擎，这样就能了解模板的相关信息
        te.setTemplateResolver(resolver);
        //2.3利用模板引擎将数据与静态页面结合生成动态页面
        /**
         * process方法用于生成动态页面
         * 参数1：模板位置(静态页面的位置)
         * 参数2：要在页面上显示的动态数据
         * 返回值：生成好的动态页面源代码
         */
        String html = te.process("./webapps/myweb/userList.html",context);
        System.out.println(html);
        PrintWriter pw = response.getWriter();
        pw.println(html);

        System.out.println("页面生成完毕");


         //设置 正文类型，告知浏览器它是一个页面
         response.setContentType("text/html");

        System.out.println("ShowAlluserServlet:用户列表页面处理完毕！");

    }
}
