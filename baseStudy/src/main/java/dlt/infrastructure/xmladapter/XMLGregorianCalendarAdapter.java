package dlt.infrastructure.xmladapter;

import java.util.Date;
import java.util.GregorianCalendar;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

public class XMLGregorianCalendarAdapter extends
		XmlAdapter<String, XMLGregorianCalendar> {

	@Override
	public String marshal(XMLGregorianCalendar v) throws Exception {

		return v.toXMLFormat();
	}

	@Override
	public XMLGregorianCalendar unmarshal(String v) throws Exception {
		return DatatypeFactory.newInstance().newXMLGregorianCalendar(v);
	}

	public static XMLGregorianCalendar convertToXMLGregorianCalendar(Date date) {

		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(date);
		XMLGregorianCalendar gc = null;
		try {
			gc = DatatypeFactory.newInstance().newXMLGregorianCalendar(cal);
		} catch (Exception e) {

			e.printStackTrace();
		}
		return gc;
	}

	public static Date convertToDate(XMLGregorianCalendar cal) throws Exception {
		GregorianCalendar ca = cal.toGregorianCalendar();
		return ca.getTime();
	}
}
