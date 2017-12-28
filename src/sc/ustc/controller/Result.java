package sc.ustc.controller;

/**
 * Created by mmn on 2017/12/23.
 */
public class Result {
    private String name;
    private String type;
    private String value;
    public Result(){}
    public Result(String name,String type,String value){
        this.name=name;
        this.type=type;
        this.value=value;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public String getType() {
        return type;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
