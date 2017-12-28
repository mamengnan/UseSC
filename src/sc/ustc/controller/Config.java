package sc.ustc.controller;

import java.util.*;

/**
 * Created by mmn on 2017/12/23.
 */
public class Config {
    private Config(){
        ParseConfig p=ParseConfig.getinstance();
        actions=p.parseAction();
        interceptors=p.parseInterceptor();
    }
    private static Config c=new Config();
    private List<Action> actions=new ArrayList<>();
    private List<Interceptor> interceptors=new ArrayList<>();
    //singleton
    public static Config getinstance(){
        return c;
    }
    public List<Action> getactions(){
        return actions;
    }
    public List<Interceptor> getInterceptors(){
        return interceptors;
    }
    public void setInterceptors( List<Interceptor> interceptors){
        this.interceptors=interceptors;
    }
    public  void setActions( List<Action> actions){
        this.actions=actions;
    }

    public List<Action> getActions() {
        return actions;
    }
}
