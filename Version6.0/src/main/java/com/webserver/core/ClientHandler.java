package com.webserver.core;

import com.webserver.http.HttpRequest;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class ClientHandler implements Runnable{
    private Socket socket;
    public ClientHandler(Socket socket){
        this.socket = socket;
    }
  public void run(){
      try {
          //1解析请求
          HttpRequest request = new HttpRequest(socket);
          //2处理请求

          //3发送响应
          //先发送一个固定页面，测试浏览器是否可以正常接收
          File file = new File("./webapps/myweb/index.html");

          OutputStream out = socket.getOutputStream();
          //1：发送状态行
         String line = "HTTP/1.1 200 OK";
         byte[] data = line.getBytes("ISO8859-1");
         out.write(data);
         out.write(13);
         out.write(10);

         //2:发送响应头
          line = "Content-Type:text/html";
          data = line.getBytes("ISO8859-1");
          out.write(data);
          out.write(13);
          out.write(10);

          line = "Content-Length:"+file.length();
          data = line.getBytes("ISO8859-1");
          out.write(data);
          out.write(13);
          out.write(10);

          //3:发送响应正文(文件内容)
          //创建文件输入流读取要发送的文件数据
          FileInputStream fis = new FileInputStream(file);
          int len;
          byte[] buf = new byte[1024*10];
          while ((len = fis.read(buf))!=-1){
              out.write(buf,0,len);
          }
          System.out.println("响应发送完毕");

      } catch (Exception e) {
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
