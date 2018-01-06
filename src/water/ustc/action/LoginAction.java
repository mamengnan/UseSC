package water.ustc.action;
import org.dom4j.*;
import sc.ustc.controller.DI;
import water.ustc.bean.UserBean;
/**
 * Created by mmn on 2017/12/5.
 */
public class LoginAction {
    private UserBean userBean;//=new UserBean();
    public String handleLogin(String username,String userpass){
        System.out.println("name:"+username);
        System.out.println("pass:"+userpass);
     /*   if(userpass.equals("2017")){return "success";}
        else{return  "failure";}*/
      //  UserBean userBean=new UserBean();

        //没有new语句就会抛异常，所以靠注入初始化
        userBean.setUserName(username);
        userBean.setUserPass(userpass);
        boolean b=userBean.signIn();
        if(b){
            return "success";
        }else {
            return "failure";
        }
    }

    public UserBean getUserBean() {
        return userBean;
    }

    public void setUserBean(UserBean userBean) {
        this.userBean = userBean;
    }
}
