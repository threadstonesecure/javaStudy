package dlt.study.jaxb;

import com.thoughtworks.xstream.XStream;

public class XStreamDemo {
	public static void main(String[] args) {
        XStream xstream = new XStream();
        xstream.autodetectAnnotations(true);
        xstream.toXML(Data.CUSTOMER, System.out);
    }
}
