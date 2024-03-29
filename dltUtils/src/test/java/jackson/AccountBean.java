package jackson;

import java.util.Date;

public class AccountBean {

	private Long id;
	private String name;
	private String email;
	private String address;
	private Date   birthday;
	
	
	@Override
	public String toString() {
		return "AcountBean [id=" + id + ", name=" + name + ", email=" + email
				+ ", address=" + address + ", birthday=" + birthday + "]";
	}


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public String getAddress() {
		return address;
	}


	public void setAddress(String address) {
		this.address = address;
	}


	public Date getBirthday() {
		return birthday;
	}


	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}
	
	
	
}
