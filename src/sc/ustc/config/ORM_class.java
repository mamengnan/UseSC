package sc.ustc.config;

import java.util.List;

/**
 * Created by mmn on 2017/12/29.
 */
public class ORM_class {
    private String name;
    private String table;
    private String id;
    private List<Property> properties;

    public ORM_class(){}

    public ORM_class(String name, String table, String id, List<Property> properties) {
        this.name = name;
        this.table = table;
        this.id = id;
        this.properties = properties;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Property> getProperties() {
        return properties;
    }

    public void setProperties(List<Property> properties) {
        this.properties = properties;
    }
}
