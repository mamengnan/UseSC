package sc.ustc.controller;

import com.sun.deploy.net.HttpRequest;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

/**
 * Created by mmn on 2017/12/14.
 */
public class Proxy {
    public String  useProxy(Action action, List<Interceptor> interceptors, HttpServletRequest request){
        String re1="";
        Reflection reflect=new Reflection();
       // 1. interceptor stack    2. jsp paramap
        try{
            Class cla=Class.forName(action.getClassname());
            Enhancer enhancer = new Enhancer();
            enhancer.setSuperclass(cla);
            enhancer.setCallback(new MethodInterceptor() {
                @Override
                public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
                    // reflect2.reflect(n1, m1,new Object[]{ac});
                    Stack<Interceptor> interceptorStack = new Stack<Interceptor>();
                    for (Interceptor interceptor : interceptors) {
                        interceptorStack.push(interceptor);
                        reflect.reflect(interceptor.getClassname(), interceptor.getProdo(), new Object[]{action.getName()});
                    }
                    System.out.println("before method run...");
                    Object result = proxy.invokeSuper(obj, args);
                    System.out.println("result:" + result);
                    //    reflect2.reflect(n1,m2,new Object[]{result.toString()});
                    while (!interceptorStack.isEmpty()) {
                        Interceptor afterint = interceptorStack.pop();
                        reflect.reflect(afterint.getClassname(), afterint.getAfterdo(), new Object[]{result});
                    }
                    System.out.println("after method run...");
                    return result;
                }
            });
            Object obj=enhancer.create();
            Object[] oo=new Object[]{request.getParameter("username"),request.getParameter("userpass")};
            re1=(String)Reflection.invokeMethod(obj,action.getMethodname(), oo);
            for(Result result:action.getResults()){
                if(result.getName().equals(re1)){
                    return  result.getType()+"|"+result.getValue();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return "no_result";//如果没有匹配的<result>，响应客户端为信息为：没有请求的资源。
    }
    public  String  useproxy(String ac,String n1,String m1,String m2,String n2,String m3) {
        String re="";
        Reflection reflect2=new Reflection();
       // Class cla=sc.ustc.controller.Reflection.class;
        Class cla = null;
        try {
            cla = Class.forName(n2);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(cla);
        enhancer.setCallback((MethodInterceptor) (obj, method, args, proxy) -> {
            reflect2.reflect(n1, m1,new Object[]{ac});
            System.out.println("before method run...");
            Object result = proxy.invokeSuper(obj, args);
            System.out.println("result:"+result);
           reflect2.reflect(n1,m2,new Object[]{result.toString()});
            System.out.println("after method run...");
            return result;
        });
       // enhancer.create();
       // Reflection reflect1=(Reflection)enhancer.create();
       // re=(String) reflect1.reflect(n2,m3,new Object[]{});
        Object obj=enhancer.create();
      //  Object obj = cla.newInstance();
        Object aa = null;
        try {
            aa = Reflection.invokeMethod(obj,m3, new Object[]{});
        } catch (Exception e) {
            e.printStackTrace();
        }
        re = (String) aa;

       return re;

    }

}
