package app;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Created by denglt on 2017/7/6.
 */
public class StartServer {
    private static Log log = LogFactory.getLog(StartServer.class);
    public static void main(String[] args) throws Exception {
        try {
            JettyServer jettyServer = new JettyServer(Integer.parseInt(args[0]));
            jettyServer.start();
        }catch(Exception ex){
            log.error("启动Server发生意外，程序退出：", ex);
        }
    }
}
