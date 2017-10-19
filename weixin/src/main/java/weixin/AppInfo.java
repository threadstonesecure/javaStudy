package weixin;

import dlt.utils.spring.PropertyConfigurer;

/**
 *
 * Created by denglt on 2015/11/10.
 */
public class AppInfo {
    private static final String APP_ID = "wx.app.id";
    private static final String APP_SECRET="wx.app.secret";

    public static String getAppId(){
        return PropertyConfigurer.getProperty(APP_ID);
    }

    public static String getAppSecret(){
        return PropertyConfigurer.getProperty(APP_SECRET);
    }
}

