package sc.ustc.controller;

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
/**
 * Created by mmn on 2017/12/23.
 */
public class ParseConfig {
    private static ParseConfig p=new ParseConfig();
    private ParseConfig(){}
    public static ParseConfig getinstance(){
        return p;
    }
    private String fp=this.getClass().getClassLoader().getResource("controller.xml").getPath();
    public List<Action> parseAction(){
        List<Action> actions=new ArrayList<>();
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            //通过文档构建器工厂获取一个文档构建器
            DocumentBuilder db = dbf.newDocumentBuilder();
            //通过文档通过文档构建器构建一个文档实例
            Document doc = db.parse(new File(fp));
            //获取所有名字为 “action” 的节点
            NodeList acs = doc.getElementsByTagName("action");
            int size1 = acs.getLength();
            for (int i = 0; i < size1; i++){
                List<Result> results=new ArrayList<>();
                List<String> internames=new ArrayList<>();
                Node ac = acs.item(i);
                NamedNodeMap acmap = ac.getAttributes();
                String name,med,cls,re="";
                name=acmap.getNamedItem("name").getNodeValue().toString();
                med=acmap.getNamedItem("method").getNodeValue().toString();
                cls=acmap.getNamedItem("class").getNodeValue().toString();
                NodeList childlist = ac.getChildNodes();
                for(int t = 0; t<childlist.getLength(); t++) {
                    if (childlist.item(t).getNodeType() == Node.ELEMENT_NODE) {
                        if (childlist.item(t).getNodeName().toString().equals("result")) {//result
                            NamedNodeMap remap = childlist.item(t).getAttributes();
                            String rename, retype, revalue;
                            rename = remap.getNamedItem("name").getNodeValue().toString();
                            retype = remap.getNamedItem("type").getNodeValue().toString();
                            revalue = remap.getNamedItem("value").getNodeValue().toString();
                            Result result=new Result(rename,retype,revalue);
                            results.add(result);
                        }else if(childlist.item(t).getNodeName().toString().equals("interceptor-ref")){
                            NamedNodeMap intermap = childlist.item(t).getAttributes();
                            String intername = intermap.getNamedItem("name").getNodeValue().toString();
                            internames.add(intername);
                        }
                    }
                }
                 Action newac=new Action(name,cls,med,results,internames);
                 actions.add(newac);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return actions;
    }

    public List<Interceptor> parseInterceptor(){
        List<Interceptor> interceptors=new ArrayList<>();
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            //通过文档构建器工厂获取一个文档构建器
            DocumentBuilder db = dbf.newDocumentBuilder();
            //通过文档通过文档构建器构建一个文档实例
            Document doc = db.parse(new File(fp));
            //获取所有名字为 “action” 的节点
            NodeList inters = doc.getElementsByTagName("interceptor");
            int sizei=inters.getLength();
            for (int j = 0; j < sizei; j++) {
                NamedNodeMap imap = inters.item(j).getAttributes();
                String iname, icls, ipre, iafter;
                iname = imap.getNamedItem("name").getNodeValue().toString();
                icls = imap.getNamedItem("class").getNodeValue().toString();
                ipre = imap.getNamedItem("predo").getNodeValue().toString();
                iafter = imap.getNamedItem("afterdo").getNodeValue().toString();
                Interceptor newint=new Interceptor(iname,icls,ipre,iafter);
                interceptors.add(newint);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return interceptors;
    }
}
