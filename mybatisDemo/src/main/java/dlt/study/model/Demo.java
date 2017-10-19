/**
 * 
 */
package dlt.study.model;

import java.io.Serializable;
import java.util.Date;

public class Demo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String id;

	private String sn;
	private String app;
	private String appDept;

	private Date appDate;
	private String name;
	private String code;
	private String dept;
	private String stage;
	private String director;
	private String costs;
	private String memo;

	private Demo parent;
	public Demo getParent() {
		return parent;
	}

	public void setParent(Demo parent) {
		this.parent = parent;
	}

	/**
	 * @return the id
	 */

	public String getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.gpdi.emoss.domain.model.process.ProcessModel#getTitle()
	 */

	public String getTitle() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
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
	 * @param sn
	 *            the sn to set
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
	 * @param app
	 *            the app to set
	 */
	public void setApp(String app) {
		this.app = app;
	}



	public Date getAppDate() {
		return appDate;
	}

	public void setAppDate(Date appDate) {
		this.appDate = appDate;
	}

	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @param code
	 *            the code to set
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
	 * @param dept
	 *            the dept to set
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
	 * @param stage
	 *            the stage to set
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
	 * @param director
	 *            the director to set
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
	 * @param costs
	 *            the costs to set
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
	 * @param memo
	 *            the memo to set
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
	 * @param appDept
	 *            the appDept to set
	 */
	public void setAppDept(String appDept) {
		this.appDept = appDept;
	}

	@Override
	public String toString() {
		return "Demo [id=" + id + ", sn=" + sn + ", app=" + app + ", appDept="
				+ appDept + ", appDate=" + appDate + ", name=" + name
				+ ", code=" + code + ", dept=" + dept + ", stage=" + stage
				+ ", director=" + director + ", costs=" + costs + ", memo="
				+ memo + ", parent=" + parent + "]";
	}
	



}
