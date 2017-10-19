/**
 * 
 */
package dlt.application;

import java.util.Map;

import dlt.domain.model.Demo;


public interface AppDemo {

	/**
	 * @param start
	 * @param limit
	 * @return
	 */
	Map<String, Object> loadByDept(String deptid, int start, int limit);

	/**
	 * @param demo
	 * @return
	 */
	String save(Demo demo);
	
	String saveOrUpdate(Demo demo);

	/**
	 * @param ids
	 */
	void delete(String[] ids);

}
