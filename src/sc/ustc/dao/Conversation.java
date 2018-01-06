package sc.ustc.dao;

import org.apache.commons.lang3.StringUtils;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import sc.ustc.config.ConfigMessage;
import sc.ustc.config.ORM_class;
import sc.ustc.config.Property;
import water.ustc.bean.BaseBean;
import water.ustc.dao.UserDAO;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sc.ustc.controller.Proxy;

/**
 * Created by mmn on 2017/12/28.
 */
public class Conversation {
    /*
    Conversation 负责完成将对象操作映射为数据表操作，即在 Conversation 中定义数据操作 CRUD 方法，每个方法将对象操
     作解释成目标数据库的 DML 或 DDL，通过 JDBC 完成数据持久化。
     */
/*
    public static Object getObject(Object object){
        try {
            //生成PreparedStatement，不能替换字段名，表名，只能替换字段值。所以用拼接了！
            //分析輸入，輸出class
            Class objectClass=object.getClass();
            String classname=objectClass.getSimpleName();
            Object returnObject=objectClass.newInstance();
            BeanInfo beanInfo= Introspector.getBeanInfo(objectClass,Object.class);
            PropertyDescriptor[] propertyDescriptors=beanInfo.getPropertyDescriptors();
            //配置數據
            List<ORM_class> clsCon=ConfigMessage.getInstance().getClasses();
            String table="",id="";
            List<Property> propertys=new ArrayList<>();
            for(ORM_class acls:clsCon){
                if(acls.getName().equals(classname)){ //find class
                    table=acls.getTable();//table
                    id=acls.getId(); //primary key（bean）
                    propertys=acls.getProperties();
                    System.out.println("find class to table");
                    break;
                }
            }
            Map<String,String> nameCol=new HashMap<String,String>();
            for(Property apro:propertys){
                nameCol.put(apro.getName(),apro.getColumn());
            }
            String priKey=nameCol.get(id);
            //introspector从bean取数据
            Map<String, PropertyDescriptor> objProps = new HashMap<String, PropertyDescriptor>();
            for (PropertyDescriptor objProp :propertyDescriptors) {
                objProps.put(objProp.getName(), objProp);
            }
            String priValue=(String) objProps.get(id).getReadMethod().invoke(object);
            System.out.println("table:"+table+"  prikey:"+priKey+"  privalue:"+priValue);
            Statement statement= UserDAO.getInstance().getConnection().createStatement();
            String sql="select * from "+table+" where "+priKey+"=\'"+priValue+"\'";
            System.out.println(sql);
            ResultSet rs=statement.executeQuery(sql);
            Map<String, Object> lazyLoads = new HashMap<String, Object>();   //lazyload
            if (rs.next()){
                for(Property apro:propertys) {
                    String value=rs.getString(apro.getColumn());
                    if(apro.getLazy().equals("true")){  //lazy =true
                        lazyLoads.put(apro.getName(),value);
                        System.out.println("lazy bean: "+apro.getName()+" "+value);
                       // objProps.get(apro.getName()).getWriteMethod().invoke(returnObject,value);
                    }else{
                        objProps.get(apro.getName()).getWriteMethod().invoke(returnObject,value);
                        System.out.println("writemedname:"+objProps.get(apro.getName()).getWriteMethod().getName());
                        System.out.println("create new bean: "+apro.getName()+" "+value);
                    }

                }
                Method lazyLoad = objectClass.getMethod("lazyLoad", Map.class);
                lazyLoad.invoke(returnObject, lazyLoads);
                System.out.println("load lier");
                return returnObject;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
 */

    public static Object getObject(Object object){

        Class objcls=object.getClass();
        String classname=objcls.getSimpleName();
        List<ORM_class> clsCon=ConfigMessage.getInstance().getClasses();
        BeanInfo beanInfo= null;
        try {
            beanInfo = Introspector.getBeanInfo(objcls,Object.class);
        } catch (IntrospectionException e) {
            e.printStackTrace();
        }
        PropertyDescriptor[] propertyDescriptors=beanInfo.getPropertyDescriptors();

        List<Property> propertys=new ArrayList<>();
        //配置數據
        String table2="",id2="";
        for(ORM_class acls:clsCon){
            if(acls.getName().equals(classname)){ //find class
                table2=acls.getTable();//table
                id2=acls.getId(); //primary key（bean）
                propertys=acls.getProperties();
                System.out.println("find class to table");
                break;
            }
        }
        String table=table2,id=id2;
        //....把非延迟的查出来，延迟的不管了 no select
        Map<String,String> nameCol=new HashMap<String,String>();
        List<String> lazyname=new ArrayList<>();
        List<String> hotname=new ArrayList<>();
        List<String> lazycol=new ArrayList<>();
        List<String> hotcol=new ArrayList<>();
        for(Property apro:propertys){ //遍历表属性
            nameCol.put(apro.getName(),apro.getColumn());
            if(apro.getLazy().toString().equals("true")){
                lazyname.add(apro.getName());
                lazycol.add(apro.getColumn());
                System.out.println("lazy:"+apro.getName());
            }else{
                hotname.add(apro.getName());
                hotcol.add(apro.getColumn());
                System.out.println("hot:"+apro.getName());
            }
        }
        String priKey=nameCol.get(id);
        //introspector从bean取数据
        Map<String, PropertyDescriptor> objProps = new HashMap<String, PropertyDescriptor>();
        for (PropertyDescriptor objProp :propertyDescriptors) {
            objProps.put(objProp.getName(), objProp);
        }
        String priValue2=null;
        Statement statement2=null;
        String sql="";
        try {
            priValue2 = (String) objProps.get(id).getReadMethod().invoke(object);
            System.out.println("table:" + table + "  prikey:" + priKey + "  privalue:" + priValue2);
            statement2 = UserDAO.getInstance().getConnection().createStatement();
           // String sql = "select * from " + table + " where " + priKey + "=\'" + priValue + "\'";
        }catch (Exception e){
            e.printStackTrace();
        }
        String priValue=priValue2;
        Statement statement=statement2;
        //记录延迟加载是否完成
        Map<String,Boolean> firstload=new HashMap<String,Boolean>();
        for(String lazy:lazyname){
            firstload.put(lazy,false);
        }
        Object intercept=new MethodInterceptor() {
            @Override
            public Object intercept(Object obj, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
                //拦截getter方法
                for(PropertyDescriptor des:propertyDescriptors){
                    if(method.getName().equals(des.getReadMethod().getName())
                            &&lazyname.contains(des.getName())){  //lazyload
                    //sql lazy property
                        if(!firstload.get(des.getName())){
                            //sql writemed
                            String sql="select "+nameCol.get(des.getName())+" from "
                                    + table + " where " + priKey + "=\'" + priValue + "\'";
                            System.out.println("lazy sql:"+sql);
                            try {
                                ResultSet rs=statement.executeQuery(sql);
                                if(rs.next()){
                                        String value=rs.getString(nameCol.get(des.getName()));
                                        objProps.get(des.getName()).getWriteMethod().invoke(obj,value);
                                        System.out.println("lazy pro:"+des.getName()+" value:"+value);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            firstload.replace(des.getName(),true);
                        }
                    }
                }
                Object result = methodProxy.invokeSuper(obj, args);
                return result;
            }
        };
        Object proxyobj=new Proxy().loadproxy(object,intercept);
        //内省注入非延迟属性
        sql = "select "+StringUtils.join(hotcol.toArray(),",")+" from "
                + table + " where " + priKey + "=\'" + priValue + "\'";
        System.out.println("hot sql:"+sql);
        try {
            ResultSet rs=statement.executeQuery(sql);
            if(rs.next()){
                for(String name:hotname) {
                    String value=rs.getString(nameCol.get(name));
                    objProps.get(name).getWriteMethod().invoke(proxyobj,value);
                    System.out.println("hot pro:"+name+" value:"+value);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return proxyobj;
    }

    public static boolean insertObject(Object object){
        try{
            //分析輸入，輸出class
            Class objectClass=object.getClass();
            String classname=objectClass.getSimpleName();
            BeanInfo beanInfo= Introspector.getBeanInfo(objectClass,Object.class);
            PropertyDescriptor[] propertyDescriptors=beanInfo.getPropertyDescriptors();
            //配置數據
            List<ORM_class> clsCon=ConfigMessage.getInstance().getClasses();
            String table="",id="";
            List<Property> propertys=new ArrayList<>();
            for(ORM_class acls:clsCon){
                if(acls.getName().equals(classname)){ //find class
                    table=acls.getTable();//table
                    id=acls.getId(); //primary key（bean）没啥用
                    propertys=acls.getProperties();
                    System.out.println("find class to table");
                    break;
                }
            }
            Map<String,String> nameCol=new HashMap<String,String>();
            for(Property apro:propertys){                              //beanPro-column map
                nameCol.put(apro.getName(),apro.getColumn());
            }

            //introspector从bean取数据
            Map<String, PropertyDescriptor> objProps = new HashMap<String, PropertyDescriptor>();
            for (PropertyDescriptor objProp :propertyDescriptors) {
                objProps.put(objProp.getName(), objProp);
            }
           // String priValue=(String) objProps.get(id).getReadMethod().invoke(object);
            List<String> columns=new ArrayList<String>();
            List<String> values=new ArrayList<String>();
            for(Map.Entry<String,String> entity:nameCol.entrySet()){
                columns.add(entity.getValue());
                String value=(String) objProps.get(entity.getKey()).getReadMethod().invoke(object);
                values.add("'"+value+"'");
            }
            Statement statement= UserDAO.getInstance().getConnection().createStatement();
            String sql="INSERT INTO "+table+"("+ StringUtils.join(columns.toArray(),",")
                    +")"+" values("+StringUtils.join(values,",")+")";
            System.out.println(sql);
            int result=statement.executeUpdate(sql);
            return (result!=0);
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }
    public static boolean deleteObject(Object object){
        //得到主键就行了
        try{
            //分析輸入，輸出class
            Class objectClass=object.getClass();
            String classname=objectClass.getSimpleName();
            BeanInfo beanInfo= Introspector.getBeanInfo(objectClass,Object.class);
            PropertyDescriptor[] propertyDescriptors=beanInfo.getPropertyDescriptors();
            //配置數據
            List<ORM_class> clsCon=ConfigMessage.getInstance().getClasses();
            String table="",id="";
            List<Property> propertys=new ArrayList<>();
            for(ORM_class acls:clsCon){
                if(acls.getName().equals(classname)){ //find class
                    table=acls.getTable();//table
                    id=acls.getId(); //primary key（bean）没啥用
                    propertys=acls.getProperties();
                    System.out.println("find class to table");
                    break;
                }
            }
            Map<String,String> nameCol=new HashMap<String,String>();
            for(Property apro:propertys){                              //beanPro-column map
                nameCol.put(apro.getName(),apro.getColumn());
            }

            //introspector从bean取数据
            Map<String, PropertyDescriptor> objProps = new HashMap<String, PropertyDescriptor>();
            for (PropertyDescriptor objProp :propertyDescriptors) {
                objProps.put(objProp.getName(), objProp);
            }
            //prikey value
            String priValue=(String) objProps.get(id).getReadMethod().invoke(object);
            Statement statement= UserDAO.getInstance().getConnection().createStatement();
            String sql="delete from "+table+"where "+nameCol.get(id)+"='"+priValue+"'";
            System.out.println("sql:"+sql);
            int result=statement.executeUpdate(sql);
            return  result!=0;
        }catch (Exception e){
            e.printStackTrace();
        }
        return  false;
    }
    public static boolean updateObject(Object object){
        //主键和所有
        try{
            //分析輸入，輸出class
            Class objectClass=object.getClass();
            String classname=objectClass.getSimpleName();
            BeanInfo beanInfo= Introspector.getBeanInfo(objectClass,Object.class);
            PropertyDescriptor[] propertyDescriptors=beanInfo.getPropertyDescriptors();
            //配置數據
            List<ORM_class> clsCon=ConfigMessage.getInstance().getClasses();
            String table="",id="";
            List<Property> propertys=new ArrayList<>();
            for(ORM_class acls:clsCon){
                if(acls.getName().equals(classname)){ //find class
                    table=acls.getTable();//table
                    id=acls.getId(); //primary key（bean）
                    propertys=acls.getProperties();
                    System.out.println("find class to table");
                    break;
                }
            }
            Map<String,String> nameCol=new HashMap<String,String>();
            for(Property apro:propertys){                              //beanPro-column map
                nameCol.put(apro.getName(),apro.getColumn());
            }

            //introspector从bean取数据
            Map<String, PropertyDescriptor> objProps = new HashMap<String, PropertyDescriptor>();
            for (PropertyDescriptor objProp :propertyDescriptors) {
                objProps.put(objProp.getName(), objProp);
            }
            //prikey value
            String priValue=(String) objProps.get(id).getReadMethod().invoke(object);
            List<String> updateKeyValues = new ArrayList<String>();
            for(Map.Entry<String,String> entity:nameCol.entrySet()){
                String col=entity.getValue();
                String va=(String)objProps.get(entity.getKey()).getReadMethod().invoke(object);
                updateKeyValues.add(col+"='"+va+"'");
            }
            Statement statement= UserDAO.getInstance().getConnection().createStatement();
            String sql="UPDATE "+table+" set "+StringUtils.join(updateKeyValues.toArray(), ",")
                    +"where "+nameCol.get(id)+"='"+priValue+"'";
            System.out.println("sql:"+sql);
            int result=statement.executeUpdate(sql);
            return  result!=0;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }
}
