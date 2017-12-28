package sc.ustc.controller;
import java.lang.reflect.*;
/**
 * Created by mmn on 2017/12/6.
 */
public class Reflection {
    public static Object invokeMethod(Object newObj, String methodName, Object[] args) throws Exception {
        Class ownerClass = newObj.getClass();
        Class[] argsClass = new Class[args.length];
        for (int i = 0, j = args.length; i < j; i++) {
            argsClass[i] = args[i].getClass();
        }
        Method method = ownerClass.getMethod(methodName, argsClass);
        return method.invoke(newObj, args);
    }

    public Object reflect(String classname, String methodname, Object[] args) throws ClassNotFoundException {
        Object re=new Object();
        try {
            Class cla = Class.forName(classname);
            Object obj = cla.newInstance();
            // Object[] argspara=new Object[]{};
            Object aa = Reflection.invokeMethod(obj, methodname, args);
            re = aa;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return re;
    }
}
    /*
    public String reflect(String classname,String methodname,String a) throws ClassNotFoundException {
        String re="";
        try {
            // String cla="C:\\Users\\mmn\\IdeaProjects\\UseSC\\src\\water\\ustc\\action\\LoginAction";
            //classname = "water.ustc.action.LoginAction";
            //  methodname = "handleLogin";
            Class cla = Class.forName(classname);
            Object obj = cla.newInstance();
            Method med = cla.getDeclaredMethod(methodname,class java.lang.String);
            String rtype=med.getReturnType().toString();
            System.out.println(rtype);
            //String re;  class java.lang.String    void  int
            switch (rtype){
                case "class java.lang.String": re = (String) med.invoke(obj);break;
                case "void":med.invoke(obj,a);break;
                default:
            }

            //   System.out.println(re);
            // return re;
        }
        catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        System.out.println(re);
        return re;
    }*/

