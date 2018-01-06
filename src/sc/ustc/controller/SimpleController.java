package sc.ustc.controller;

import javax.servlet.ServletException;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by mmn on 2017/12/2.
 */

/*
this.getClass().getClassLoader().getResource("controller.xml").getPath()
 */
public class SimpleController extends HttpServlet {
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
        System.out.println("contextpath:"+req.getContextPath());
        System.out.println("servletpath"+req.getServletPath());
       // Map<String,String[]> paramap=(Map<String, String[]>) req.getParameterMap();
        Processor pro=Processor.getinstance();
        String result;
       // result=pro.parseUrl(req.getRequestURL().toString());
        result=pro.parseUrl(req.getRequestURL().toString(),req);
       // System.out.println("2"+res);
        pro.dispatch(result,req,resp);
/*
        resp.setContentType("text/html;charset=utf-8");
        PrintWriter pw = resp.getWriter();
        pw.print("<html><head><title>SimpleController</title></head><body>欢迎使用SimpleController!</body></html>");
*/
    }
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
        this.doPost(req,resp);
    }
}