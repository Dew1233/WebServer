package com.webserver.http;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

/**
 * 响应对象，当前类的每一个实例用于表示给客户端发送一个Http响应
 * 每个响应应由三部分构成：
 * 状态行，响应头，响应正文(正文部分可以没有)
 */
public class HttpResponse {

    //状态行相关信息
    private int statusCode = 200;//状态代码默认值200，因为绝大多数请求实际应用中都能正确处理
    private String statusReason = "OK";

    //响应头相关信息

    //响应正文相关信息
    private File entity;//响应正文对应的实体文件

    private Socket socket;
    public  HttpResponse(Socket socket){
        this.socket = socket;
    }

    public void flush() throws IOException {
        /*
        发送一个响应时候，按顺序发送状态行，响应头，响应正文
         */
        sendStatusLine();
        sendHeaders();
        sendContent();
    }
    //发送一个响应的三个步骤
   //1:发送状态行
   private void sendStatusLine(){
       System.out.println("HttpResponse:开始发送状态行……");
       try {
           OutputStream out = socket.getOutputStream();
           //1：发送状态行
           String line = "HTTP/1.1"+" "+statusCode+" "+statusReason;
           byte[] data = line.getBytes("ISO8859-1");
           out.write(data);
           out.write(13);
           out.write(10);
       }catch (IOException e){
           e.printStackTrace();
       }
       System.out.println("HttpResponse:状态行发送完毕!");
   }
   //2：发送响应头
   private void sendHeaders(){
       System.out.println("HttpResponse:开始发送响应头……");
       try {
           //2:发送响应头
           OutputStream out = socket.getOutputStream();
           String line = "Content-Type:text/html";
           byte[] data = line.getBytes("ISO8859-1");
           out.write(data);
           out.write(13);
           out.write(10);

           line = "Content-Length:"+entity.length();
           data = line.getBytes("ISO8859-1");
           out.write(data);
           out.write(13);
           out.write(10);

           //不加上会出现空白问题
           out.write(13);
           out.write(10);
       }catch (IOException e){
           e.printStackTrace();
       }
       System.out.println("HttpResponse:响应头发送完毕!");
   }
   //3:发送响应正文
   private void sendContent(){
       System.out.println("HttpResponse:开始发送响应正文……");
       try( //创建文件输入流读取要发送的文件数据
             FileInputStream fis = new FileInputStream(entity);) {
           //3:发送响应正文(文件内容)
           OutputStream out = socket.getOutputStream();


           int len;
           byte[] buf = new byte[1024*10];
           while ((len = fis.read(buf))!=-1){
               out.write(buf,0,len);
           }
           System.out.println("响应发送完毕");


       }catch (IOException e){
           e.printStackTrace();
       }
       System.out.println("HttpResponse:响应正文发送完毕!");
   }

   public File getEntity(){
        return entity;
   }
   public void setEntity(File entity){
        this.entity = entity;
   }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatusReason() {
        return statusReason;
    }

    public void setStatusReason(String statusReason) {
        this.statusReason = statusReason;
    }
}
