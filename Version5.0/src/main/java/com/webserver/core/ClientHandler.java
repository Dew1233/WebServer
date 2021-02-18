package com.webserver.core;

import com.webserver.http.HttpRequest;

import java.io.IOException;
import java.io.InputStream;
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
          HttpRequest request = new HttpRequest(socket);
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
