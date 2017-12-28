package sc.ustc.controller;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
/**
 * Created by mmn on 2017/12/6.
 */
public class DomXml {
   public  String parseAction(String fp,String ActionName){

       //实例化一个文档构建器工厂
       DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
       try {
           //通过文档构建器工厂获取一个文档构建器
           DocumentBuilder db = dbf.newDocumentBuilder();
           //通过文档通过文档构建器构建一个文档实例
           Document doc = db.parse(new File(fp));
           //获取所有名字为 “action” 的节点
           NodeList acs = doc.getElementsByTagName("action");
           NodeList inters = doc.getElementsByTagName("interceptor");
           int size1 = acs.getLength();
           int sizei=inters.getLength();
          // System.out.println("action标签的个数为：" + size1);
           for (int i = 0; i < size1; i++) {
               Node ac = acs.item(i);
               //通过Node对象的getAttributes()方法获取全的属性值
               NamedNodeMap acmap = ac.getAttributes();
               String name,med,cls,re="";
               name=acmap.getNamedItem("name").getNodeValue().toString();
               med=acmap.getNamedItem("method").getNodeValue().toString();
               cls=acmap.getNamedItem("class").getNodeValue().toString();
               if(name.equals(ActionName)){
                   NodeList childlist = ac.getChildNodes();
                   boolean isinter=false;
                   for(int t = 0; t<childlist.getLength(); t++) {
                       if (childlist.item(t).getNodeType() == Node.ELEMENT_NODE) {
                           if (childlist.item(t).getNodeName().toString().equals("interceptor-ref")) {
                               NamedNodeMap intermap = childlist.item(t).getAttributes();
                               String intername = intermap.getNamedItem("name").getNodeValue().toString();
                               System.out.println("intername is:" + intername);
                               for (int j = 0; j < sizei; j++) {
                                   NamedNodeMap imap = inters.item(j).getAttributes();
                                   String iname, icls, ipre, iafter;
                                   iname = imap.getNamedItem("name").getNodeValue().toString();
                                   icls = imap.getNamedItem("class").getNodeValue().toString();
                                   ipre = imap.getNamedItem("predo").getNodeValue().toString();
                                   iafter = imap.getNamedItem("afterdo").getNodeValue().toString();
                                   if (iname.equals(intername)) {
                                     //  System.out.println("find!! iname:" + iname + "|icls:" + icls + "|ipre:" + ipre + "|iafter:" + iafter);
                                 //      System.out.println("action  name:"+name+"|method:"+med+"|class"+cls);
                                       isinter = true;
                                       Proxy p=new Proxy();
                                       re=p.useproxy(name,icls,ipre,iafter,cls,med);
                                      // re="success";
                                   }
                               }
                           }
                       }
                   }
                   if(!isinter){
                       System.out.println("no interceptor!");
                       Reflection reflection=new Reflection();
                       re=(String) reflection.reflect(cls,med,new Object[]{});
                   }
                   for(int t = 0; t<childlist.getLength(); t++) {
                       if (childlist.item(t).getNodeType() == Node.ELEMENT_NODE) {
                           if (childlist.item(t).getNodeName().toString().equals("result")) {//result
                               NamedNodeMap remap = childlist.item(t).getAttributes();
                               String rename, retype, revalue;
                               rename = remap.getNamedItem("name").getNodeValue().toString();
                               retype = remap.getNamedItem("type").getNodeValue().toString();
                               revalue = remap.getNamedItem("value").getNodeValue().toString();
                               if (rename.equals(re)) {
                                   System.out.println(retype + "|" + revalue);
                                   return retype + "|" + revalue;
                               }
                           }
                       }
                   }
                   return "no_result";//如果没有匹配的<result>，响应客户端为信息为：没有请求的资源。
               }
           }

       } catch (ParserConfigurationException ex) {
           ex.printStackTrace();
       } catch (IOException ex) {
           ex.printStackTrace();
       } catch (SAXException ex) {
           ex.printStackTrace();
       } catch (ClassNotFoundException e) {
           e.printStackTrace();
       }
       return "no_action";//如果没有找到，响应客户端信息为：不可识别的 action 请求。
   }

   public static void main(String[] args) {
        DomXml domxml = new DomXml();
        String actionname;
       // actionname="register";
       actionname="login";
        String re=domxml.parseAction("C:\\Users\\mmn\\IdeaProjects\\UseSC\\src\\controller.xml",actionname);
        System.out.println("we get result:"+re);
    }
}
