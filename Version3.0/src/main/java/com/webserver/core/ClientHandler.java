package com.webserver.core;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

/**
 * 负责与指定客户端进行HTTP交互
 * HTTP协议要求与客户端的交互规则采取一问一答
 * 1解析请求（一问）
 * 2处理请求
 * 3发送响应(一答）
 *
 */
public class ClientHandler implements Runnable{
    private Socket socket;
    public  ClientHandler(Socket socket){
        this.socket=socket;
    }

    @Override

    public void run() {
       try {
           //1解析请求
           InputStream in = socket.getInputStream();
           //测试读取客户端发送过来的请求内容
           int d;
           char cur = ' ';//表示本次读取到的字符
           char pre = ' ';//表示上次读取到的字符
           StringBuilder builder = new StringBuilder();//保存读取到的所有字符
           while ((d=in.read())!=-1){
               cur = (char)d;//本次读取到的字符
               //如果上次读取的 是回车符号，本次读取的是换行符号则停止读取
               if (pre == 13&&cur==10){
                   break;
               }
               builder.append(cur);
               pre = cur;
           }
           String line = builder.toString().trim();
           System.out.println("请求行："+line);
           //请求行：GET / HTTP/1.1请求行拆分
           String method;//请求方式
           String uri;//抽象路径
           String protocol;//协议版本

           String [] data = line.split("\\s");
           method = data[0];
           uri = data[1];
           protocol = data[2];

           System.out.println("method:"+method);
           System.out.println("uri:"+uri);
           System.out.println("protocol:"+protocol);

           //2处理请求

           //3发送响应

       }catch (Exception e){
           e.printStackTrace();
       }finally {
           try {
               socket.close();
           } catch (IOException e) {
               e.printStackTrace();
           }
       }
    }
}
