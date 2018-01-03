package sc.ustc.config;

/**
 * Created by mmn on 2017/12/29.
 */
public class JDBC {
    private String driver_class;
    private String url_path;
    private String db_username;
    private String db_password;

    public JDBC(){}

    public JDBC(String driver_class, String url_path, String db_username, String db_password) {
        this.driver_class = driver_class;
        this.url_path = url_path;
        this.db_username = db_username;
        this.db_password = db_password;
    }

    public String getDb_username() {
        return db_username;
    }

    public void setDb_username(String db_username) {
        this.db_username = db_username;
    }

    public String getDb_password() {
        return db_password;
    }

    public void setDb_password(String db_password) {
        this.db_password = db_password;
    }

    public String getDriver_class() {
        return driver_class;
    }

    public void setDriver_class(String driver_class) {
        this.driver_class = driver_class;
    }

    public String getUrl_path() {
        return url_path;
    }

    public void setUrl_path(String url_path) {
        this.url_path = url_path;
    }
}
