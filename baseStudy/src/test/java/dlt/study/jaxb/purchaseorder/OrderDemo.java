package dlt.study.jaxb.purchaseorder;

import java.util.Date;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;
import javax.xml.datatype.XMLGregorianCalendar;

import dlt.infrastructure.xmladapter.XMLGregorianCalendarAdapter;

public class OrderDemo {
	public static void main(String[] args) throws Exception {
		JAXBContext jc = JAXBContext.newInstance(PurchaseOrderType.class);
		Marshaller marshaller = jc.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");

		ObjectFactory objectFactory = new ObjectFactory();
		PurchaseOrderType purchaseOrderType = newOrder();

		JAXBElement<PurchaseOrderType> jaxbElement = objectFactory
				.createPurchaseOrder(purchaseOrderType);
		marshaller.marshal(jaxbElement, System.out);
	}

	public static PurchaseOrderType newOrder() throws Exception {
		PurchaseOrderType purchaseOrderType = new PurchaseOrderType();

		XMLGregorianCalendar orderDate = XMLGregorianCalendarAdapter
				.convertToXMLGregorianCalendar(new Date());
		purchaseOrderType.setOrderDate(orderDate);
        USAddress shipTo = new USAddress();
        shipTo.setCountry("US");
        shipTo.setName("Alice Smith");
        shipTo.setStreet("123 Maple Street");
        purchaseOrderType.setShipTo(shipTo);
        
        USAddress billTo = new USAddress();
        billTo.setCountry("US");
        billTo.setName("Robert Smith");
        billTo.setStreet("8 Oak Avenue");
        purchaseOrderType.setBillTo(billTo); 
       
		return purchaseOrderType;
	}

}
