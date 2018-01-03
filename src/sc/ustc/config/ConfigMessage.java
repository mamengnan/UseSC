package sc.ustc.config;

import sc.ustc.dao.Configuration;

import java.util.List;

/**
 * Created by mmn on 2017/12/29.
 */
public class ConfigMessage {
    private JDBC jdbc;
    private List<ORM_class> classes;

    private ConfigMessage(){
        //congiguration
        Configuration configuration=Configuration.getInstance();
        jdbc=configuration.parseJDBC();
        classes=configuration.parseClass();
    }
    private static ConfigMessage configMessage=new ConfigMessage();
    public static ConfigMessage getInstance(){return configMessage;}

    public JDBC getJdbc() {
        return jdbc;
    }

    public void setJdbc(JDBC jdbc) {
        this.jdbc = jdbc;
    }

    public List<ORM_class> getClasses() {
        return classes;
    }

    public void setClasses(List<ORM_class> classes) {
        this.classes = classes;
    }
}
