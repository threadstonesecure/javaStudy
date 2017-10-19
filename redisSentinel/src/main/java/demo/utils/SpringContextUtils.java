package demo.utils;

import java.util.Locale;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;

@Service
public final class SpringContextUtils implements ApplicationContextAware {

	private static ApplicationContext applicationContext;

	

	public static ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	public void setApplicationContext(ApplicationContext applicationContext) {
		SpringContextUtils.applicationContext = applicationContext;
	}

	public static void init(String[] xmls) {
		applicationContext = new ClassPathXmlApplicationContext(xmls);

		if (applicationContext instanceof AbstractApplicationContext) {
			((AbstractApplicationContext) applicationContext)
					.registerShutdownHook();// 调用singleton
											// bean上的相应析构回调方法，需要在JVM里注册一个“关闭钩子”
		}

	}

	@SuppressWarnings("unchecked")
	public static <T> T getBean(String beanName) {

		return (T) applicationContext.getBean(beanName);

	}

	public static <T> T getBean(Class<T> requiredType) {
		// return (T) applicationContext.getBean(requiredType);
		return applicationContext.<T> getBean(requiredType);
	}

	/*
	 * 国际化信息 通过
	 * org.springframework.context.support.ResourceBundleMessageSource配置
	 */
	public static String getMessage(String code, Object[] args,
			String defaultMessage, Locale locale) {
		return applicationContext
				.getMessage(code, args, defaultMessage, locale);
	}

	public static String getMessage(String code, Object[] args, Locale locale)
			throws NoSuchMessageException {

		return applicationContext.getMessage(code, args, locale);
	}

	public static String getMessage(MessageSourceResolvable resolvable,
			Locale locale) throws NoSuchMessageException {

		return applicationContext.getMessage(resolvable, locale);
	}

}
