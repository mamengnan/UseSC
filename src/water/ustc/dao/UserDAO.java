package water.ustc.dao;

import sc.ustc.config.ConfigMessage;
import sc.ustc.dao.Conversation;
import water.ustc.bean.UserBean;

import java.sql.*;

/**
 * Created by mmn on 2017/12/25.
 */
public class UserDAO extends sc.ustc.dao.BaseDAO {

    private UserDAO() {
      ConfigMessage configMessage=ConfigMessage.getInstance();
      userName=configMessage.getJdbc().getDb_username();
      userPassword=configMessage.getJdbc().getDb_password();
      driver=configMessage.getJdbc().getDriver_class();
      url=configMessage.getJdbc().getUrl_path();
       //mysql
    /*
      userPassword="password";
      userName="root";
      driver="com.mysql.jdbc.Driver";
      url="jdbc:mysql://localhost:3306/userlist";
    */
        //sqlite
       /*
        driver="org.sqlite.JDBC";
        url="jdbc:sqlite:D:\\SQLite\\userlist.db3";
        userName="";
        userPassword="";
     */
    }
    private static UserDAO userDAO=new UserDAO();
    public  static UserDAO getInstance(){
        return userDAO;
    }


    @Override
    public Object query(String uname) throws SQLException { // 负责执行 sql 语句，并返回结果对象
       openDBConnection();
       UserBean userBean=new UserBean();
       userBean.setUserName(uname);
       Object object= Conversation.getObject(userBean);
      // closeDBConnection();
       return object;
    }

    @Override
    public boolean insert(Object object) {//负责执行 sql 语句，并返回执行结果
        openDBConnection();
        boolean result=Conversation.insertObject(object);
        try {
            closeDBConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public boolean update(Object object) { //负责执行 sql 语句，并返回执行结果
        openDBConnection();
        boolean result=Conversation.updateObject(object);
        try {
            closeDBConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public boolean delete(Object object) { //负责执行 sql 语句，并返回执行结果
        openDBConnection();
        boolean result=Conversation.deleteObject(object);
        try {
            closeDBConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
    public static void main(String[] argv){
        UserDAO userDAO=UserDAO.getInstance();


    }
}