package sc.ustc.controller;

import com.sun.deploy.net.HttpRequest;

import javax.servlet.ServletResponse;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by mmn on 2017/12/6.
 */
public class Processor {
    private Processor(){

    }
    private Config config=Config.getinstance();
    private static Processor pro=new Processor();
    public static Processor getinstance(){
        return pro;
    }

    public  String parseUrl(String url){
        String ActionName=getActionName(url);
        DomXml dom=new DomXml();
        String fp;
        fp= this.getClass().getClassLoader().getResource("controller.xml").getPath();
       // fp=("controller.xml");
        String res=(String) dom.parseAction(fp,ActionName);
        return res;
    }

    public String parseUrl(String url, HttpServletRequest request){
        String ActionName=getActionName(url);
        List<Interceptor> totalint= config.getInterceptors();
        List<Interceptor> refint=new ArrayList<>();
        for(Action action:config.getactions()){
            if(action.getName().equals(ActionName)){
                //ref interceptor
                for(String refname:action.getInternames()){
                    for(Interceptor interceptor:totalint){
                        if(interceptor.getName().equals(refname)){
                            refint.add(interceptor);
                        }
                    }
                }
                //proxy
                Proxy newproxy=new Proxy();

                String res=newproxy.useProxy(action,refint,request);
                // get res   return res;
                return res;
               // return "no_result";//如果没有匹配的<result>，响应客户端为信息为：没有请求的资源。
             }
        }
        return "no_action";//如果没有找到，响应客户端信息为：不可识别的 action 请求。
    }

    public String getActionName(String url){
        String name="";
        // http://localhost:8080/xxx/login.sc
        String spl[]=url.split("/");
        for (int i = 0 ; i <spl.length ; i++ ){
            if(spl[i].indexOf(".sc")!=-1){
                //System.out.println("包含");
                name=spl[i].substring(0,spl[i].length()-3);
            }
        }
        return name;
    }
    public void dispatch(String res,HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        /**
         * 客户端跳转：效率低
         * session范围属性，url中的参数会传递下去，request范围属性不传递
         */
        //response.sendRedirect(url);

        /**
         * 服务器端跳转：常用，效率高
         * request范围属性，session范围属性，url中的参数会传递
         */
        resp.setContentType("text/html;charset=utf-8");
        PrintWriter pw = resp.getWriter();
        switch (res) {
            case "no_action":pw.write("不可识别的 action 请求");break;
            case "no_result":pw.write("没有请求的资源");break;
            default:
                String spl[] = res.split("\\|");
                switch (spl[0]) {
                    case "forward":
                        System.out.println(spl[1]);
                        req.getRequestDispatcher(spl[1]).forward(req, resp);
                        break;
                    case "redirect":
                        System.out.println(spl[1]);
                        resp.sendRedirect(spl[1]);
                        break;
                }

        }
    }
}
