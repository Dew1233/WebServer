package com.webserver.servlet;

import com.webserver.http.HttpRequest;
import com.webserver.http.HttpResponse;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;

public class LoginServlet {
    public void service(HttpRequest request, HttpResponse response){
        System.out.println("LoginServlet:开始处理用户登录……");
        //获取当前页面输入的用户名和密码

        String username = request.getParameter("username");
        String password = request.getParameter("password");
       //验证
        if (username==null||password==null){
            File file = new File("./webapps/myweb/login_fail.html");
            response.setEntity(file);
            System.out.println("用户名或者密码为空");
        }

        try(
                RandomAccessFile raf = new RandomAccessFile("user.dat","r");
                ){
            boolean login = false;
            for (int i =0;i<raf.length()/100;i++){
                raf.seek(i*100);
                byte[] data = new byte[32];
                raf.read(data);
                String name = new String(data,"utf-8").trim();

                if (name.equals(username)){
                    raf.read(data);
                    String passwo = new String(data,"utf-8").trim();
                    if(passwo.equals(password)){
                        login = true;
                    }
                    break;
                }
            }

            if (login){
                File file = new File("./webapps/myweb/login_success.html");
                response.setEntity(file);
                System.out.println("登录成功");
            }else {
                File file = new File("./webapps/myweb/login_fail.html");
                response.setEntity(file);
                System.out.println("用户密码或者用户名错误");
                return;
            }

        }catch (IOException e){
            e.printStackTrace();
        }
        System.out.println("LoginServlet:用户登录处理结束……");
    }
}
