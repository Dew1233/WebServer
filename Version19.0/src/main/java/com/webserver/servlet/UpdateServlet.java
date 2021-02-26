package com.webserver.servlet;

import com.webserver.http.HttpRequest;
import com.webserver.http.HttpResponse;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;

/**
 * servlet是JAVAEE标准中的一个接口，意思是运行在服务端的小程序
 * 我们用他处理某个具体的请求
 *
 * 当前Servlet用于处理用户注册业务
 */
public class UpdateServlet {
    public void service(HttpRequest request,HttpResponse response) {
        System.out.println("UpdateServlet:开始处理用户信息修改……");
        /*
        1:通过request获取用户在注册页面上输入的注册信息(表单上的信息)
        2：将用户的注册信息写入user.dat中
        3：设置response给客户端响应注册结果页面
         */
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String nickname = request.getParameter("nickname");
        String ageStr = request.getParameter("age");
        /*
        必要的验证工作，如果上面的四项有空的，年龄不是一个数字时候，直接响应给
        客户端一个提示页面：reg_info_error.html,里面居中显示一行字：注册信息有误，请重新注册
        注：该页面也放在webapps/myweb这个网络应用中。
         */


        if (username == null || password == null || nickname == null || ageStr==null||!(ageStr.matches("[0-9]{1,3}"))) {
            File file1 = new File("./webapps/myweb/reg_info_error.html");
            response.setEntity(file1);
            return;
        }
        int age = Integer.parseInt(ageStr);//年龄转换为int值
        System.out.println(username + "," + password + "," + nickname + "," + age);
        /*
        2每条用户信息占用100字节，其中用户名，密码，昵称为字符串各占32字节，年龄为int值占4字节

         */
        try (
                RandomAccessFile raf = new RandomAccessFile("user.dat", "rw");
                /*
                验证是否为重复用户
                先读取user.dat文件中现有的所有用户的名字，并且与本次注册的用户名对比，如果
                存在则响应页面：have_user.html居中显示一行字:该用户已经存在，请重新注册
                否则才进行注册操作
                 */


        ) {

            for (int i =0;i<raf.length()/100;i++){
                raf.seek(i*100);
                byte[] data = new byte[32];
                raf.read(data);
                String name = new String(data,"utf-8").trim();
                if(name.equals(username)){
                    raf.seek(i*100);
                    //用户名
                    data = username.getBytes("utf-8");
                    data = Arrays.copyOf(data, 32);
                    raf.write(data);
                    //写密码
                    data = password.getBytes("utf-8");
                    data = Arrays.copyOf(data, 32);
                    raf.write(data);
                    //写昵称
                    data = nickname.getBytes("utf-8");
                    data = Arrays.copyOf(data, 32);
                    raf.write(data);
                    //写年龄
                    raf.writeInt(age);
                    System.out.println("修改完毕");
                    //3
                    File file = new File("./webapps/myweb/update_success.html");
                    response.setEntity(file);
                }
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
         System.out.println("RegServlet：信息修改 处理完毕");
    }
}
