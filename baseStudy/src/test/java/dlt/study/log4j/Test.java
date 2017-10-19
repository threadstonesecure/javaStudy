package dlt.study.log4j;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Test {

	public static void main(String[] args) {

		Log log = LogFactory.getLog(Test.class.getName());
		log.debug(" debug ");
		log.error(" error ");
		System.out.println(log);
	}
}
