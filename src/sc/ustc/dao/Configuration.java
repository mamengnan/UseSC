package sc.ustc.dao;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import sc.ustc.config.*;

/**
 * Created by mmn on 2017/12/28.
 */
public class Configuration {
    /*
    负责解析 UseSC 工程的配置 or_mapping.xml
     */
    private static Configuration configuration=new Configuration();
    private Configuration(){}
    public static Configuration getInstance(){return  configuration;}

    private String fp=this.getClass().getClassLoader().getResource("or_mapping.xml").getPath();

    public JDBC parseJDBC(){
        System.out.println("begin parse jdbc");
        JDBC jdbc=new JDBC();
        try{
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            //通过文档构建器工厂获取一个文档构建器
            DocumentBuilder db = dbf.newDocumentBuilder();
            //通过文档通过文档构建器构建一个文档实例
            Document doc = db.parse(new File(fp));
            //获取所有名字为 “class” 的节点
            NodeList jd = doc.getElementsByTagName("jdbc");
            NodeList pros=jd.item(0).getChildNodes();
            for(int i=0;i<pros.getLength();i++){
                if (pros.item(i).getNodeType() == Node.ELEMENT_NODE) {
                    if(pros.item(i).getNodeName().toString().equals("property")) {
                        NodeList rp=pros.item(i).getChildNodes();
                            String tname=rp.item(1).getTextContent();
                            String tvalue=rp.item(3).getTextContent();
                            System.out.println(tname+": "+tvalue);
                            switch (tname){
                                case "driver_class":jdbc.setDriver_class(tvalue);break;
                                case "url_path":jdbc.setUrl_path(tvalue);break;
                                case "db_username":jdbc.setDb_username(tvalue);break;
                                case "db_password":jdbc.setDb_password(tvalue);break;
                            }
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        System.out.println("parse jdbc over");
        return jdbc;
    }
    public List<ORM_class> parseClass(){
        System.out.println("begin parse class");
        List<ORM_class> classes=new ArrayList<>();
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            //通过文档构建器工厂获取一个文档构建器
            DocumentBuilder db = dbf.newDocumentBuilder();
            //通过文档通过文档构建器构建一个文档实例
            Document doc = db.parse(new File(fp));
            //获取所有名字为 “class” 的节点
            NodeList ces = doc.getElementsByTagName("class");
            int sizec=ces.getLength();
            for (int i = 0; i< sizec; i++) {
                List<Property>properties=new ArrayList<>();
                String classname="",table="",id="";
                Node c=ces.item(i);
                NodeList childlist = c.getChildNodes();
                for(int t = 0; t<childlist.getLength(); t++) {
                    if (childlist.item(t).getNodeType() == Node.ELEMENT_NODE) {
                        if (childlist.item(t).getNodeName().toString().equals("property")) {//property
                            NodeList p4=childlist.item(t).getChildNodes();
                            String name=p4.item(1).getTextContent();
                            String column=p4.item(3).getTextContent();
                            String type=p4.item(5).getTextContent();
                            String lazy=p4.item(7).getTextContent();
                            System.out.println("p4: "+name+" "+column+" "+type+" "+lazy);
                            Property property=new Property(name,column,type,lazy);
                            properties.add(property);
                        }else if(childlist.item(t).getNodeName().toString().equals("name")){
                            classname=childlist.item(t).getTextContent();
                            System.out.println("class name: "+classname);
                        }else if(childlist.item(t).getNodeName().toString().equals("table")){
                            table=childlist.item(t).getTextContent();
                            System.out.println("table name: "+table);
                        }else if(childlist.item(t).getNodeName().toString().equals("id")){
                            id=childlist.item(t).getTextContent();
                            System.out.println("id: "+id);
                        }
                    }
                }
                //class
                ORM_class newclass=new ORM_class(classname,table,id,properties);
                classes.add(newclass);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        System.out.println("parse class over");
        return classes;
    }
}
