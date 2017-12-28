package water.ustc.dao;

import water.ustc.bean.UserBean;

import java.sql.*;

/**
 * Created by mmn on 2017/12/25.
 */
public class UserDAO extends sc.ustc.dao.BaseDAO {

    private UserDAO() {

       //mysql
        /*
      userPassword="password";
      userName="root";
      driver="com.mysql.jdbc.Driver";
      url="jdbc:mysql://localhost:3306/userlist";
      */
        //sqlite

        driver="org.sqlite.JDBC";
        url="jdbc:sqlite:D:\\SQLite\\userlist.db3";
        userName="";
        userPassword="";

    }
    private static UserDAO userDAO=new UserDAO();
    public  static UserDAO getInstance(){
        return userDAO;
    }
    public Object query(String sql) { // 负责执行 sql 语句，并返回结果对象
        System.out.println("sql: "+sql);
        String ps="";
        String na="";
        String id="";
        super.openDBConnection();
        try {
            Statement statement=connection.createStatement();
            ResultSet resultSet=statement.executeQuery(sql);
            while(resultSet.next()){
                ps=resultSet.getString("userpassword");
                na=resultSet.getString("username");
                id=resultSet.getString("userid");
            }
            resultSet.close();
            super.closeDBConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //如果password存在，返回UserBean

        if(!ps.equals("")) {
            UserBean userBean= new UserBean(id,na,ps);
            return userBean;
        }
        return null;
    }

    public boolean insert(String sql) {//负责执行 sql 语句，并返回执行结果
        try {
            super.openDBConnection();
            connection.setAutoCommit(false);
            Statement statement=connection.createStatement();
            statement.executeUpdate(sql);//insert into tablename()values()
            statement.close();
            connection.commit();
            super.closeDBConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    public boolean update(String sql) { //负责执行 sql 语句，并返回执行结果
        //update tablename set userpassword=44 where userid=1;
        try {
            super.openDBConnection();
            connection.setAutoCommit(false);
            Statement statement=connection.createStatement();
            statement.executeUpdate(sql);
            statement.close();
            connection.commit();
            super.closeDBConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    public boolean delete(String sql) { //负责执行 sql 语句，并返回执行结果
        try {
            super.openDBConnection();
            connection.setAutoCommit(false);
            Statement statement=connection.createStatement();
            statement.executeUpdate(sql);//delete from tablename where id=2;
            statement.close();
            connection.commit();
            super.closeDBConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return  true;
    }
}