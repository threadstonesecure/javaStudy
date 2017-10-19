import oracle.jdbc.driver.OracleDriver;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Created by denglt on 2017/7/5.
 */
public class ConnectDB {
    public String ip;
    public String port = "1521";
    public String sid;
    public String username;
    public String password;
    public boolean isDefaultConnect = false;
    public Connection connection;

    public Connection getConnection() throws SQLException {
        String url = "jdbc:oracle:thin:@" + ip + ":" + 1521 + "/" + sid;
        System.out.println(url);
        Properties props = new Properties();
        props.put("user", username);
        props.put("password", password);
        props.put("SetBigStringTryClob", "true");
        if (username.equals("sys") || username.equals("SYS")) {
            props.put("internal_logon", "sysdba");
        }
        DriverManager.registerDriver(new OracleDriver());

        connection = DriverManager.getConnection(url, props);
        return connection;
    }

    public static void main(String[] args) throws Exception {
        ConnectDB connectDB = new ConnectDB();
        connectDB.ip = "10.10.10.129";
        connectDB.sid = "pdboradlt";
        connectDB.username = "dba_dlt";
        connectDB.password = "dlt760416";
        connectDB.getConnection();
    }
}
