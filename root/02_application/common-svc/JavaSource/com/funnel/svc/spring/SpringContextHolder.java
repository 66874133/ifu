package com.funnel.svc.spring;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

@SuppressWarnings("unchecked")
public class SpringContextHolder implements ApplicationContextAware,
		DisposableBean {
	private static ApplicationContext applicationContext = null;

	public void setApplicationContext(ApplicationContext applicationContext) {
		SpringContextHolder.applicationContext = applicationContext;
	}

	public void destroy() throws Exception {
		clear();
	}

	public static ApplicationContext getApplicationContext() {
		assertContextInjected();
		return applicationContext;
	}

	public static <T> T getBean(String name) {
		assertContextInjected();
		return (T) applicationContext.getBean(name);
	}

	public static void clear() {
		applicationContext = null;
	}

	private static void assertContextInjected() {
		if (applicationContext == null)
			throw new IllegalStateException(
					"applicationContext为空，请检查SpringContextHolder配置");
	}
}