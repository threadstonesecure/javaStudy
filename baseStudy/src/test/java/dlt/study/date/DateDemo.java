package dlt.study.date;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.junit.Test;

public class DateDemo {

	@Test
	public void dateDemo() throws Exception {
		GregorianCalendar cal = new GregorianCalendar();
		Date d = new Date();
		System.out.println(d.getTime());// 该函数返回自1970年1月1日 00:00:00
										// GMT以来此对象表示的毫秒数
		cal.setTime(d);
		System.out.println(cal);
		System.out.println(cal.getTimeZone());
		int offset = TimeZone.getDefault().getRawOffset(); // TimeZone表示时区偏移量，本质上以毫秒数保存与GMT的差值
		System.out.println(offset);
		DateFormat df = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
		Date birthday = df.parse("1976-04-16 10:20:30");
		cal.setTime(birthday);
		System.out.println(cal);
		System.out.println(cal.getTimeZone());
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

	@Test
	public void date2XML() {
		XMLGregorianCalendar xmlGC = convertToXMLGregorianCalendar(new Date());
		System.out.println(xmlGC.toXMLFormat()); // 2015-05-20T16:54:05.790+08:00
		System.out.println(xmlGC);
	}

	@Test
	public void data2XML2() throws Exception {
		DatatypeFactory df = DatatypeFactory.newInstance();
		XMLGregorianCalendar xmlGC = df.newXMLGregorianCalendar(2015, 5, 20,
				16, 50, 10, 0, TimeZone.getDefault().getRawOffset() / 60000); // TimeZone.getDefault().getRawOffset()/60000=
																				// 8*60

		System.out.println(xmlGC.toXMLFormat());// 2015-05-20T16:50:10.000+08:00
	}

	@Test
	public void parseXMLGC() throws Exception {
		XMLGregorianCalendar xmlGC = DatatypeFactory.newInstance()
				.newXMLGregorianCalendar("2015-05-20T16:50:10.000+08:00");
		System.out.println(xmlGC.toXMLFormat());
	}
}
