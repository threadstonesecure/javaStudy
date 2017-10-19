package dlt.study.app.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import dlt.study.app.DemoService;
import dlt.study.dao.DemoMapper;
import dlt.study.model.Demo;


@Service
public class DemoServiceImpl implements DemoService {
 
	@Autowired
	private DemoMapper demoMapper;
	
	@Override

	public Demo get(String id) {
		return demoMapper.selectDemo(id);
	}
	
	@Override
	public Demo insert(Demo demo) {
		demoMapper.insertDemoAutoID(demo);
		//demoMapper.insertDemo(demo);
		return demo;
	}
}
