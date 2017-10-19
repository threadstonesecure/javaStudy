package dlt.study;

import com.atomikos.icatch.jta.UserTransactionManager;
import com.atomikos.jdbc.AtomikosDataSourceBean;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.sql.DataSource;
import javax.sql.XAConnection;
import javax.transaction.TransactionManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Properties;

/**
 * Created by denglt on 2017/6/5.
 */
public class AtomikosExample {

    private static UserTransactionManager utm;
    private static AtomikosDataSourceBean ds;
    private static AtomikosDataSourceBean ds2;

    @BeforeClass
    public static void init() throws Exception {
        utm = new UserTransactionManager();
        utm.init();
        ds = new AtomikosDataSourceBean();
        ds.setUniqueResourceName("mysql");
        ds.setXaDataSourceClassName("com.mysql.jdbc.jdbc2.optional.MysqlXADataSource");
        Properties p = new Properties();
        p.setProperty("user", "root");
        p.setProperty("password", "denglt");
        p.setProperty("URL", "jdbc:mysql://127.0.0.1:3306/test?zeroDateTimeBehavior=convertToNull");
        ds.setXaProperties(p);
        ds.setPoolSize(5);

        ds2 = new AtomikosDataSourceBean();
        ds2.setUniqueResourceName("mysql2");
        ds2.setXaDataSourceClassName("com.mysql.jdbc.jdbc2.optional.MysqlXADataSource");
        Properties p2 = new Properties();
        p2.setProperty("user", "med");
        p2.setProperty("password", "medicalcore01");
        p2.setProperty("URL", "jdbc:mysql://112.124.33.7:3306/med2?zeroDateTimeBehavior=convertToNull");
        ds2.setXaProperties(p2);
        ds2.setPoolSize(5);
    }

    @AfterClass
    public static void shutdown() {
        ds.close();
        utm.close();
    }

    @Test
    public void  nonXA() throws Exception {
        Connection c = ds.getConnection();
        c.setAutoCommit(false);
        System.out.println(c);
        PreparedStatement ps = c.prepareStatement("delete from test where id = 2");
        ps.execute();
        c.commit();
        c.close();

    }

    @Test
    public void XAOnOneDS() throws Exception {
        utm.begin();
        // use connection to execute sql
        Connection c = ds.getConnection();
        Connection c2 = ds.getConnection();
        System.out.println(c);
        System.out.println(c2);
        PreparedStatement ps = c.prepareStatement("delete from test where id = 1000");
        ps.execute();
        PreparedStatement ps2 = c2.prepareStatement("insert into test values(1000,'dddd',null,null,null)");
        ps2.execute();
        utm.commit();
        c.close();
        c2.close();
    }

    @Test
    public void XAOnMutilDS() throws Exception{
        utm.begin();
        // use connection to execute sql
        Connection c = ds.getConnection();
        Connection c2 = ds2.getConnection();
        System.out.println(c);
        System.out.println(c2);
        PreparedStatement ps = c.prepareStatement("delete from test where id = 1000");
        ps.execute();
        PreparedStatement ps2 = c2.prepareStatement("delete from no_exists_table where id = 1000");
        ps2.execute();
        utm.commit();
        c.close();
        c2.close();
    }
}
