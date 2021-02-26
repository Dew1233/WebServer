package com.webserver.servlet;

import com.webserver.http.HttpRequest;
import com.webserver.http.HttpResponse;
import com.webserver.vo.User;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.FileTemplateResolver;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.util.Arrays;

public class ToUpdateServlet extends HttpServlet {
    public void doGet(HttpRequest request, HttpResponse response){
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

    @Override
    public void doPost(HttpRequest request, HttpResponse response) {
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
