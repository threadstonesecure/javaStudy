/**
 * 
 */
package dlt.domain.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Proxy;


/**
 * SQL> desc demo
Name     Type           Nullable Default Comments 
-------- -------------- -------- ------- -------- 
ID       VARCHAR2(32)                             
SN       VARCHAR2(32)   Y                         
APP      VARCHAR2(100)  Y                         
APP_DATE DATE           Y                         
NAME     VARCHAR2(1000) Y                         
CODE     VARCHAR2(100)  Y                         
DEPT     VARCHAR2(100)  Y                         
STAGE    VARCHAR2(100)  Y                         
DIRECTOR VARCHAR2(100)  Y                         
COSTS    VARCHAR2(100)  Y                         
MEMO     VARCHAR2(2000) Y                         
APP_DEPT VARCHAR2(32)   Y 
 * @author dlt
 *
 */

@Entity
@Proxy(lazy = false)
public class Demo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */

	@Id
	@GenericGenerator(name = "uuid", strategy = "uuid32b")
	@GeneratedValue(generator = "uuid")
	private String id;

	private String sn;
	private String app;
	private String appDept;


	@Column(name = "app_date")
	private Date date;
	private String name;
	private String code;
	private String dept;
	private String stage;
	private String director;
	private String costs;
	private String memo;

	/**
	 * @return the id
	 */
	
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/* (non-Javadoc)
	 * @see com.gpdi.emoss.domain.model.process.ProcessModel#getTitle()
	 */

	public String getTitle() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the sn
	 */
	public String getSn() {
		return sn;
	}

	/**
	 * @param sn the sn to set
	 */
	public void setSn(String sn) {
		this.sn = sn;
	}

	/**
	 * @return the app
	 */
	public String getApp() {
		return app;
	}

	/**
	 * @param app the app to set
	 */
	public void setApp(String app) {
		this.app = app;
	}

	/**
	 * @return the date
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * @param date the date to set
	 */
	public void setDate(Date date) {
		this.date = date;
	}

	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @param code the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * @return the dept
	 */
	public String getDept() {
		return dept;
	}

	/**
	 * @param dept the dept to set
	 */
	public void setDept(String dept) {
		this.dept = dept;
	}

	/**
	 * @return the stage
	 */
	public String getStage() {
		return stage;
	}

	/**
	 * @param stage the stage to set
	 */
	public void setStage(String stage) {
		this.stage = stage;
	}

	/**
	 * @return the director
	 */
	public String getDirector() {
		return director;
	}

	/**
	 * @param director the director to set
	 */
	public void setDirector(String director) {
		this.director = director;
	}

	/**
	 * @return the costs
	 */
	public String getCosts() {
		return costs;
	}

	/**
	 * @param costs the costs to set
	 */
	public void setCosts(String costs) {
		this.costs = costs;
	}

	/**
	 * @return the memo
	 */
	public String getMemo() {
		return memo;
	}

	/**
	 * @param memo the memo to set
	 */
	public void setMemo(String memo) {
		this.memo = memo;
	}

	/**
	 * @return the appDept
	 */
	public String getAppDept() {
		return appDept;
	}

	/**
	 * @param appDept the appDept to set
	 */
	public void setAppDept(String appDept) {
		this.appDept = appDept;
	}

}
