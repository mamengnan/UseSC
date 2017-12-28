package water.ustc.interceptor;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.text.SimpleDateFormat;
/**
 * Created by mmn on 2017/12/13.
 */
public class LogInterceptor {
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//可以方便地修改日期格式
    private static  String name="";
    private static String result="";
    private static String stime,etime;

    //记录每次客户端请求的 action 名称<name>、访问开始时间<s-time>
    public void preAction(String name){
        Date now = new Date();
       // SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");//可以方便地修改日期格式
        this.stime=dateFormat.format( now );
        this.name=name;
        System.out.println("s-time:"+this.stime);
    }

    //访问结束时间<e-time>、请求返回结果<result>，并将信息追加至日志文件 log.xml，保存在 PC 磁盘上
    public void afterAction(String result){
        Date now = new Date();
      //  SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");//可以方便地修改日期格式
        this.etime = dateFormat.format( now );
        this.result=result;
        System.out.println("xml: s-time:"+this.stime+"|e-time:"+this.etime+"|name:"+this.name+"|result"+this.result);
        //write xml
        writexml();
    }
    public void writexml(){
        boolean first=false;
        File file=new File("D:/log.xml");
        if (!file.exists()) {   first=true;}

        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            //通过文档构建器工厂获取一个文档构建器
            DocumentBuilder db = dbf.newDocumentBuilder();
            //通过文档通过文档构建器构建一个文档实例
            if(first){
                Document doc = db.newDocument();
                Element root=doc.createElement("log");
                doc.appendChild(root);

                TransformerFactory tff = TransformerFactory.newInstance();
                // 创建Transformer对象
                Transformer tf = tff.newTransformer();
                // 设置输出数据时换行
                tf.setOutputProperty(OutputKeys.INDENT, "yes");
                // 使用Transformer的transform()方法将DOM树转换成XML
                tf.transform(new DOMSource(doc), new StreamResult(file));


            }else {
                Document doc = db.parse(file);

                Element actionElement = doc.createElement("action");

                Element nameElement = doc.createElement("name");
                Element stimeElement = doc.createElement("s-time");
                Element etimeElement = doc.createElement("e-time");
                Element resultElement = doc.createElement("result");

                nameElement.setTextContent(name);
                stimeElement.setTextContent(stime);
                etimeElement.setTextContent(etime);
                resultElement.setTextContent(result);
              //  Text n = doc.createTextNode(name);
              //  Text et = doc.createTextNode(etime);
              //  Text st = doc.createTextNode(stime);
             //   Text re = doc.createTextNode(result);

               // nameElement.appendChild(n);
            //    stimeElement.appendChild(st);
             //   etimeElement.appendChild(et);
             //   resultElement.appendChild(re);

                actionElement.appendChild(nameElement);
                actionElement.appendChild(stimeElement);
                actionElement.appendChild(etimeElement);
                actionElement.appendChild(resultElement);

                doc.getElementsByTagName("log").item(0).appendChild(actionElement);

                // 创建TransformerFactory对象
                TransformerFactory tff = TransformerFactory.newInstance();
                // 创建Transformer对象
                Transformer tf = tff.newTransformer();
                // 设置输出数据时换行
                tf.setOutputProperty(OutputKeys.INDENT, "yes");
                // 使用Transformer的transform()方法将DOM树转换成XML
                tf.transform(new DOMSource(doc), new StreamResult(file));
            }

        }catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        LogInterceptor l=new LogInterceptor();
        l.writexml();
    }
}
