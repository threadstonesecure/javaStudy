/**
 * 
 */
package dlt.web.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import dlt.application.AppDemo;
import dlt.domain.model.Demo;


@Controller
@RequestMapping(value = "/demo")
public class DemoAction {

	@Autowired
	private AppDemo appDemo;

	@RequestMapping(value = "listByDept", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> listByDept(String deptid,int start, int limit) {
		
		return appDemo.loadByDept(deptid, start, limit);
	}

	@RequestMapping(value = "save", method = RequestMethod.POST)
	@ResponseBody
	public String save(Demo demo) {
		String id = appDemo.save(demo);
		return "{'success':true, 'id':'" + id + "'}";
	}


	@RequestMapping(value = "delete", method = RequestMethod.POST)
	@ResponseBody
	public void delete(String[] ids) {
		appDemo.delete(ids);
	}
	
	@RequestMapping(value = "saveOrUpdate", method = RequestMethod.POST)
	@ResponseBody
	public String saveOrUpdate(Demo demo) {
		String id = null;
		if (demo.getId() == null){
			id = appDemo.save(demo);
		}else{
		    id = appDemo.saveOrUpdate(demo);
		}
		return "{'success':true, 'id':'" + id + "'}";
	}


}
