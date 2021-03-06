package water.ustc.bean;

import water.ustc.dao.UserDAO;

import java.sql.SQLException;

/**
 * Created by mmn on 2017/12/25.
 */
public class UserBean { //basebean 的load接口可以放弃了
    private String  userId;
    private String userName;
    private String userPass;
    public UserBean(){}
    public UserBean(String userId, String userName, String userPass) {
        this.userId = userId;
        this.userName = userName;
        this.userPass = userPass;
    }
    public boolean signIn(){ //处理登录业务
       //userName构造sql
     //   String sql="";
       // sql="select * from user where username='"+userName+"'";
        UserDAO dao=UserDAO.getInstance();
        /*
        dao.setUrl("jdbc:mysql://localhost:3306/userlist");
        dao.setDriver("com.mysql.jdbc.Driver");
        dao.setUserName("root");
        dao.setUserPassword("password");*/
        UserBean obj= null;
        try {
            obj = (UserBean)dao.query(userName);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if(obj==null){
            return false;
        }else{
            String pwd=obj.getUserPass();
            System.out.println("test lazy load:"+obj.getUserPass());
            if(userPass.equals(pwd)){
                return true;
            }else{
                return false;
            }
        }
    }
    public String getUserId() {
        return userId;
    }

    public String getUserPass() {
        return userPass;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserPass(String userPass) {
        this.userPass = userPass;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
