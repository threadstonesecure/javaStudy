package dlt.domain.model;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

public class User  implements  Comparable<User>,Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = -8248047768066904738L;
	
	private Integer userId;
    private String userName;
    private String password;
    private boolean married;
    private int    age;
    private double income;
    private User   wife; 
    
    private List<User> childs;
    private String[] interests;
    
    public User(){}

    public User(String userName){
    	this.userName = userName;
    }

    public User(String userName , int age){
    	this.userName = userName;
    	this.age = age;
	}

	public List<User> getChilds() {
		return childs;
	}
	public void setChilds(List<User> childs) {
		this.childs = childs;
	}
	public User getWife() {
		return wife;
	}
	public void setWife(User wife) {
		this.wife = wife;
	}
	public double getIncome() {
		return income;
	}
	public void setIncome(double income) {
		this.income = income;
	}

	

    
    public String[] getInterests() {
		return interests;
	}
	public void setInterests(String[] interests) {
		this.interests = interests;
	}

	public boolean isMarried() {
		return married;
	}
	public void setMarried(boolean isMarried) {
		this.married = isMarried;
	}
	public Integer getUserId() {
        return userId;
    }
    public void setUserId(Integer userId) {
        this.userId = userId;
    }
    
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	@Override
	public String toString() {
		return "User [userId=" + userId + ", userName=" + userName
				+ ", password=" + password + ", married=" + married + ", age="
				+ age + ", income=" + income + ", wife=" + wife + ", childs="
				+ childs + ", interests=" + Arrays.toString(interests) + "]";
	}


	@Override
	public int compareTo(User o) {
		return this.getUserName().compareTo(o.userName);
	}
}
