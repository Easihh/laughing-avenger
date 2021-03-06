package com.thanatos.server.utility;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class AppUtils implements ApplicationContextAware{
	
	private static ApplicationContext context;
	
	@Override
	public void setApplicationContext(ApplicationContext appContext)
			throws BeansException {
		context=appContext;
	}
	
	public static ApplicationContext getAppContext(){
		return context;
	}

}
