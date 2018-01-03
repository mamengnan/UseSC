package sc.ustc.dao;

import com.sun.deploy.util.StringUtils;
import sc.ustc.config.ConfigMessage;
import sc.ustc.config.ORM_class;
import sc.ustc.config.Property;

import water.ustc.dao.UserDAO;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;

import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;


/**
 * Created by mmn on 2017/12/28.
 */
public class Conversation {
    /*
    Conversation 负责完成将对象操作映射为数据表操作，即在 Conversation 中定义数据操作 CRUD 方法，每个方法将对象操
     作解释成目标数据库的 DML 或 DDL，通过 JDBC 完成数据持久化。
     */
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
            String sql="INSERT INTO "+table+"("+ StringUtils.join(columns,",")
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
            String sql="UPDATE "+table+" set "+StringUtils.join(updateKeyValues, ",")
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
