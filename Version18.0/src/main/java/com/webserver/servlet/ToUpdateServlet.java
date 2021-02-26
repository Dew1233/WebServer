package com.webserver.servlet;

import com.webserver.http.HttpRequest;
import com.webserver.http.HttpResponse;
import com.webserver.vo.User;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.FileTemplateResolver;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.RandomAccessFile;

public class ToUpdateServlet {
    public void service(HttpRequest request, HttpResponse response){
        //获取超链接传递过来的用户名
        String username = request.getParameter("username");

        //找到该用户信息
        try(
                RandomAccessFile raf = new RandomAccessFile("user.dat","r");
        ){
            for(int i=0;i<raf.length()/100;i++){
                raf.seek(i*100);
                byte[] data = new byte[32];
                raf.read(data);
                String name = new String(data,"UTF-8").trim();
                if(name.equals(username)){
                    //找到该用户，读取该用户其他信息
                    raf.read(data);
                    String pwd = new String(data,"UTF-8").trim();
                    raf.read(data);
                    String nick = new String(data,"UTF-8").trim();
                    int age = raf.readInt();
                    response.setContentType("text/html");
                    //拼接页面
//                    createHtml(response,name,pwd,nick,age);
                    //使用thymeleaf
                    User user = new User(name,pwd,nick,age);
                    Context context = new Context();
                    context.setVariable("user",user);

                    FileTemplateResolver resolver = new FileTemplateResolver();
                    resolver.setTemplateMode("html");//模板是html
                    resolver.setCharacterEncoding("UTF-8");//模板使用的字符集
                    TemplateEngine te = new TemplateEngine();
                    te.setTemplateResolver(resolver);

                    String html = te.process("./webapps/myweb/update.html",context);
                    //将生成好的html代码交给response
                    PrintWriter pw = response.getWriter();
                    pw.println(html);

                    break;
                }
            }


        }catch(IOException e){
            e.printStackTrace();
        }

    }

    private void createHtml(HttpResponse response,String username,String password,String nickname,int age){
        PrintWriter pw = response.getWriter();
        pw.println("<!DOCTYPE html>");
        pw.println("<html lang=\"en\">");
        pw.println("<head>");
        pw.println("    <meta charset=\"UTF-8\">");
        pw.println("    <title>修改信息</title>");
        pw.println("</head>");
        pw.println("<body>");
        pw.println("    <center>");
        pw.println("        <h1>修改信息</h1>");
        pw.println("        <form action=\"./updateUser\" method=\"post\">");
        pw.println("            <table border=\"1\">");
        pw.println("                 <tr>");
        pw.println("                        <td>用户名</td>");
        pw.println("                        <td><input name=\"username\" value='"+username+"' type=\"text\"></td>");
        pw.println("                </tr>");
        pw.println("                 <tr>");
        pw.println("                        <td>密码</td>");
        pw.println("                        <td><input name=\"password\" value='"+password+"' type=\"text\"></td>");
        pw.println("                </tr>");
        pw.println("                 <tr>");
        pw.println("                        <td>昵称</td>");
        pw.println("                        <td><input name=\"nickname\" value='"+nickname+"' type=\"text\"></td>");
        pw.println("                </tr>");
        pw.println("                 <tr>");
        pw.println("                        <td>年龄</td>");
        pw.println("                        <td><input name=\"age\" value='"+age+"' type=\"text\"></td>");
        pw.println("                </tr>");
        pw.println("                 <tr>");
        pw.println("                        <td align=\"center\" colspan=\"2\"><input type=\"submit\" value=\"修改\"></td>");
        pw.println("                </tr>");
        pw.println("            </table>");
        pw.println("         </form>");
        pw.println("    </center>");
        pw.println("</body>");
        pw.println("</html>");

    }


}
