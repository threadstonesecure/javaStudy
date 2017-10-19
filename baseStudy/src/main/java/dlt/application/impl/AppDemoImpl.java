/**
 * 
 */
package dlt.application.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import dlt.application.AppDemo;
import dlt.domain.model.Demo;
import dlt.utils.hibernate.dao.HibernateService;

@Service
public class AppDemoImpl implements AppDemo {

	@Autowired
	private HibernateService hibService;
	
	@Autowired
	private JdbcTemplate jdbcTemplate;

	/**
	 * @param start
	 * @param limit
	 * @param result
	 */
	@Override
	public Map<String, Object> loadByDept(String deptid, int start, int limit) {
		Map<String, Object> result = new HashMap<String, Object>(3);
		result.put("success", Boolean.TRUE);
		String[] param = { "dept" };
		Object[] value = { deptid };
		Long total = hibService.uniqueQuery("select count(1) from Demo d where d.appDept=:dept order by d.date desc", param, value);
		result.put("total", total);
		if (total > 0) {
			List<Demo> rows = hibService.queryHQL("from Demo d where d.appDept=:dept order by d.date desc", param, value, start, limit);
			result.put("rows", rows);
		}
		return result;
	}


	@Override
	public String save(Demo demo) {
			hibService.save(demo);
			return demo.getId();

	}

	private  String saveJDBC(Demo demo) {
		// jdbcTemplate.execute(action)
		return demo.getId();

}
	@Override
	public String saveOrUpdate(Demo demo) {
			hibService.saveOrUpdate(demo);
			return demo.getId();
	}

	@Override
	public void delete(String[] ids) {
		for (String id : ids) {
			hibService.delete(Demo.class, id);
		}
	}

}
