package com.webserver.core;

import com.webserver.http.HttpRequest;
import com.webserver.http.HttpResponse;

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
          HttpResponse response = new HttpResponse(socket);
          //2处理请求
          //首先通过request获取抽象路径
          String path = request.getUri();
          File file = new File("./webapps"+path);
          if (file.exists()&&file.isFile()){
                  //3发送响应
                  //先发送一个固定页面，测试浏览器是否可以正常接收
//          File file = new File("./webapps/myweb/index.html");

              System.out.println("资源存在");
              OutputStream out = socket.getOutputStream();


              response.setEntity(file);

              //1：发送状态行
              //2：发送响应头

          }else {
              System.out.println("资源不存在");
              File notFoundPage = new File("./webapps/root/404.html");
              response.setStatusCode(404);
              response.setStatusReason("NotFound");
              response.setEntity(notFoundPage);
          }
          //3:发送响应正文
          response.flush();
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
