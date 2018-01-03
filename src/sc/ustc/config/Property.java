package sc.ustc.config;

/**
 * Created by mmn on 2017/12/29.
 */
public class Property {
    private String name;
    private String column;
    private String type;
    private String lazy;

    public Property(){}

    public Property(String name, String column, String type, String lazy) {
        this.name = name;
        this.column = column;
        this.type = type;
        this.lazy = lazy;
    }

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLazy() {
        return lazy;
    }

    public void setLazy(String lazy) {
        this.lazy = lazy;
    }
}
