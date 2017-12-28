package sc.ustc.controller;

/**
 * Created by mmn on 2017/12/23.
 */
public class Interceptor {
    private String name= "";
    private String classname="";
    private String prodo="";
    private String afterdo="";
    public Interceptor(){}
    public Interceptor(String name, String classname, String prodo, String afterdo) {
        this.name = name;
        this.classname = classname;
        this.prodo = prodo;
        this.afterdo = afterdo;
    }

    public String getName() {
        return name;
    }

    public String getAfterdo() {
        return afterdo;
    }

    public String getClassname() {
        return classname;
    }

    public String getProdo() {
        return prodo;
    }

    public void setAfterdo(String afterdo) {
        this.afterdo = afterdo;
    }

    public void setClassname(String classname) {
        this.classname = classname;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setProdo(String prodo) {
        this.prodo = prodo;
    }

}
