package dlt.study.jaxb;

public class Data {
	public static Customer CUSTOMER;
	 
    static {
        CUSTOMER = new Customer();
        CUSTOMER.setId(123);
        CUSTOMER.setName("Jane Doe 邓隆通");
 
        Address address = new Address();
        address.setStreet("1 A Street");
        address.setCity("Any Town");
        CUSTOMER.setAddress(address);
 
        PhoneNumber workPhoneNumber = new PhoneNumber();
        workPhoneNumber.setType("work");
        workPhoneNumber.setNumber("555-WORK");
        CUSTOMER.getPhoneNumbers().add(workPhoneNumber);
 
        PhoneNumber cellPhoneNumber = new PhoneNumber();
        cellPhoneNumber.setType("cell");
        cellPhoneNumber.setNumber("555-CELL");
        CUSTOMER.getPhoneNumbers().add(cellPhoneNumber);
    }
}
