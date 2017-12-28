package sc.ustc.controller;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mmn on 2017/12/23.
 */
public class Action {
    private String name=new String();
    private String classname=new String();
    private String methodname=new String();
    private List<Result> results=new ArrayList<>();
    private List<String> internames=new ArrayList<>();
    public Action(){}
    public Action(String name, String classname, String methodname, List<Result> results, List<String> internames) {
        this.name = name;
        this.classname = classname;
        this.methodname = methodname;
        this.results = results;
        this.internames = internames;
    }
    public String getName(){
        return  name;
    }
    public String getClassname(){
        return classname;
    }
    public String getMethodname(){
        return  methodname;
    }
    public List<Result> getResults(){
        return results;
    }
    public List<String> getInternames(){
        return internames;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setClassname(String classname) {
        this.classname = classname;
    }

    public void setInternames(List<String> internames) {
        this.internames = internames;
    }

    public void setMethodname(String methodname) {
        this.methodname = methodname;
    }

    public void setResults(List<Result> results) {
        this.results = results;
    }
}
