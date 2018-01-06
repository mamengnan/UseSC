package sc.ustc.controller;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.File;
import java.util.List;
import java.util.Objects;

/**
 * Created by mmn on 2018/1/4.
 */
public class DI {
    public  static Object DependenceInjection(Object obj){
        String fp=DI.class.getClassLoader().getResource("di.xml").getPath();
        File diXml = new File(fp);
        Document diDocument = null;
        try {
            diDocument = (new SAXReader()).read(diXml);
            System.out.println(fp);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        Element diRootElement = diDocument.getRootElement();
        List<Element> diBeans = diRootElement.elements("bean");
        for(Element bean:diBeans){
            List<Element> fields=bean.elements();
            if(fields.size()>0){
                System.out.println("bean has field:"+bean.attributeValue("id"));
                for(Element field:fields){
                    for(Element bn:diBeans){
                        if(bn.attributeValue("id").equals(field.attributeValue("bean-ref"))){
                            System.out.println("bean ref id:"+bn.attributeValue("id"));
                            Object refbean = null;
                            BeanInfo actionBI=null;
                            try {
                                refbean= (Class.forName(bn.attributeValue("class"))).newInstance();
                                actionBI =Introspector.getBeanInfo(obj.getClass(), Object.class);

                                PropertyDescriptor[] actionProps = actionBI.getPropertyDescriptors();
                                for (PropertyDescriptor actionProp : actionProps) {
                                    if (Objects.equals(actionProp.getName(), field.attributeValue("name"))) {
                                        //action
                                        actionProp.getWriteMethod().invoke(obj, refbean);
                                        System.out.println("field name:"+field.attributeValue("name"));
                                    }
                                 }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    }
                }
            }
        }


        return obj;
    }

}
