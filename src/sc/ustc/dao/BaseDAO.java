package sc.ustc.dao;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import com.mysql.jdbc.*;
import sc.ustc.config.ConfigMessage;

/**
 * Created by mmn on 2017/12/25.
 */
public abstract class BaseDAO {
    protected String driver;//（数据库驱 动类）
    protected String url;   //（数据库访问路径）
    protected String userName;//（数据库用户名）
    protected String userPassword;//（数据库用户密码）
    protected Connection connection;

    public String getDriver() {
        return driver;
    }
    public String getUserName() {
        return userName;
    }
    public Connection getConnection() {
        return connection;
    }
    public String getUrl() {
        return url;
    }
    public String getUserPassword() {
        return userPassword;
    }
    public void setConnection(Connection connection) {
        this.connection = connection;
    }
    public void setDriver(String driver){
        this.driver=driver;
    }
    public void setUrl(String url){
        this.url=url;
    }
    public void setUserName(String userName){
        this.userName=userName;
    }
    public void setUserPassword(String userPassword){
        this.userPassword=userPassword;
    }
    //实现方法
    public void openDBConnection()  {  //负责打开数据库连接

        try {
            System.out.println("driver:"+driver);
            Class.forName(driver);  //加载驱动
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try {
            System.out.println("url: "+url);
            System.out.println("username: "+userName);
            System.out.println("userpass: "+userPassword);
            connection = DriverManager.getConnection(url,userName,userPassword);

            if(!connection.isClosed()){
               System.out.println("succeeded connecting to the database!");
           }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean closeDBConnection() throws SQLException { //负责关闭数据库连接
        try {
            connection.close();
            System.out.println("connection closed!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }
    //抽象方法
    public abstract Object query(String uname) throws SQLException; // 负责执行 sql 语句，并返回结果对象

    public abstract boolean insert(Object object);//负责执行 sql 语句，并返回执行结果

    public abstract boolean update(Object object); //负责执行 sql 语句，并返回执行结果

    public abstract boolean delete(Object object); //负责执行 sql 语句，并返回执行结果
}