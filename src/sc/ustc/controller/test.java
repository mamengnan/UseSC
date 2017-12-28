package sc.ustc.controller;

import javax.servlet.ServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.sql.*;

/**
 * Created by mmn on 2017/12/16.
 */
public class test {
    public static void main(String [] args){
           //sqlite test;
        String driver="org.sqlite.JDBC";
        String url="jdbc:sqlite:D:\\SQLite\\userlist.db3";
        //String url="jdbc:sqlite:mmntest.db"
        String sql="SELECT * FROM user where username=\'mamengnan\'";
        System.out.println(sql);
        Connection connection=null;
        try{
            Class.forName(driver);
            connection=DriverManager.getConnection(url);
        }catch (Exception e){
            System.err.println(e.getClass().getName()+":"+e.getMessage());
            System.exit(0);
        }
        System.out.println("open database succefully");
        try {
            Statement statement=connection.createStatement();
            ResultSet resultSet=statement.executeQuery(sql);
            while(resultSet.next()){
               String ps=resultSet.getString("userpassword");
               String na=resultSet.getString("username");
               String id=resultSet.getString("userid");
                System.out.println(ps+"  "+na+" "+id);
            }
            resultSet.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

