package water.ustc.action;

import java.util.Random;

/**
 * Created by mmn on 2017/12/5.
 */
public class RegisterAction {
    public String handleRegister(String username,String userpass){
        int max=2;
        int min=1;
        Random random = new Random();
        int s = random.nextInt(max)%(max-min+1) + min;
        switch (s){
            case 1:return "success";
            case 2:return "failure";
            default:return "";
        }
    }
}
