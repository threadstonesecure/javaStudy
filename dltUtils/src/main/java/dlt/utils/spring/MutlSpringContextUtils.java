package dlt.utils.spring;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;


@Service
public final class MutlSpringContextUtils implements ApplicationContextAware {
	
	private static List<ApplicationContext> contexts = new ArrayList<ApplicationContext>() ;

	public static List<ApplicationContext> getApplicationContext() {
		return contexts;
	}

	public  void setApplicationContext(ApplicationContext applicationContext) {
		MutlSpringContextUtils.contexts.add(applicationContext);
	}
		
	
	@SuppressWarnings("unchecked")
	public static <T> T getBean(String beanName){
		for (ApplicationContext ac : contexts){
			Object o = ac.getBean(beanName);
			if ( o != null){
				return (T) o;
			}
			
		}
		return null;
	}

	
	public static <T> T getBean(Class<T> requiredType){
		for (ApplicationContext ac : contexts){
			T o = ac.getBean(requiredType);
			if ( o != null){
				return o;
			}
			
		}
		return null;
	}	
	
	public static void printInfo(){
		for (ApplicationContext ac : contexts){
			
			System.out.println(ac);
			System.out.println(ac.getParent());
			
		}
	}
}
