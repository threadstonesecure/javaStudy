package dlt.study.app;

import dlt.study.model.Demo;

public interface DemoService {

	public Demo get(String id);
	
	public Demo insert(Demo demo);
}
