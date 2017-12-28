package water.ustc.action;
        import com.sun.deploy.net.HttpRequest;
        import water.ustc.bean.UserBean;

        import javax.servlet.http.HttpServlet;
        import javax.servlet.http.HttpServletRequest;
        import java.util.HashMap;
        import java.util.Map;
        import java.util.Random;
/**
 * Created by mmn on 2017/12/5.
 */
public class LoginAction {
    public String handleLogin(String username,String userpass){
        System.out.println("name:"+username);
        System.out.println("pass:"+userpass);
     /*   if(userpass.equals("2017")){return "success";}
        else{return  "failure";}*/
        UserBean userBean=new UserBean();
        userBean.setUserName(username);
        userBean.setUserPass(userpass);
        boolean b=userBean.signIn();
        if(b){
            return "success";
        }else{
            return "failure";
        }

    }
}
