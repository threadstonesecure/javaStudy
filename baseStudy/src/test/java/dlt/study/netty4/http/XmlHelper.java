package dlt.study.netty4.http;

/**
 * 仅仅是个demo
 * Created by denglt on 2016/4/8.
 */
public class XmlHelper {

    public static  String  toXml(Object o){
        if (o instanceof IXml){
            return ((IXml) o).toXml();
        }

        return "未知对象";
    }

    public static Object parseXml(String xml){
        return xml;
    }
}
