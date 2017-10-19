package dlt.study.jaxb;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.StringReader;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.namespace.QName;

import org.junit.Test;

public class JAXBDemo {
	@Test
	public void objectToXML() throws Exception {
		JAXBContext jc = JAXBContext.newInstance(Customer.class);
		Marshaller marshaller = jc.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
		Data.CUSTOMER.getAddress().setCity(null); //故意設置為null的，注意xml中city的輸出

		JAXBElement<Customer> jaxbElement = new JAXBElement<Customer>(
				new QName("customer"), Customer.class, Data.CUSTOMER);

		marshaller.marshal(jaxbElement, System.out);
	}

	@Test
	public void xmlToObject() throws Exception {
		System.out.println("============xmlToObject=============");
		JAXBContext jc = JAXBContext.newInstance(Customer.class);
		Marshaller marshaller = jc.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
		Data.CUSTOMER.getAddress().setCity(null);
		JAXBElement<Customer> jaxbElement = new JAXBElement<Customer>(
				new QName("customer"), Customer.class, Data.CUSTOMER);
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		marshaller.marshal(jaxbElement, bos);

		ByteArrayInputStream bin = new ByteArrayInputStream(bos.toByteArray());

		Unmarshaller unmarshaller = jc.createUnmarshaller();

		Customer customer = (Customer) unmarshaller.unmarshal(bin);
		System.out.println(customer);
		if (customer.getAddress().getCity() == null) {
			System.out
					.println("customer.getAddress().getCity() is null");
		} else {
			System.out.println("customer.getAddress().getCity()='" + customer.getAddress().getCity()+"'");
		}

	}

	/*
	 * 验证<city />或 <city></city>格式  。
	 * 注意null和 空串是不通的
	 */
	@Test
	public void xmlToObject2() throws Exception {
		System.out.println("============xmlToObject2=============");		
		JAXBContext jc = JAXBContext.newInstance(Customer.class);
		String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
				+ "	<customer id=\"123\"> "
				+ "	    <name>Jane Doe 邓隆通</name> "
				+ "	    <address>           "
				+ "	        <street>1 A Street</street>  "
				+ "	        <city />  "
				+ "	    </address>    "
				+ "	    <phone-number type=\"work\">555-WORK</phone-number>  "
				+ "	    <phone-number type=\"cell\">555-CELL</phone-number>  "
				+ "	</customer>";

		Unmarshaller unmarshaller = jc.createUnmarshaller();

		StringReader sr = new StringReader(xml);
		Customer customer = (Customer) unmarshaller.unmarshal(sr);
		System.out.println(customer);
		
		if (customer.getAddress().getCity() == null) {
			System.out
					.println("customer.getAddress().getCity() is null");
		} else {
			System.out.println("customer.getAddress().getCity()='" + customer.getAddress().getCity()+"'");
		}
	}
}
