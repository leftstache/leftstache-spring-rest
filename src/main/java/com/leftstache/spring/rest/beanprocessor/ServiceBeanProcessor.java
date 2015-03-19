package com.leftstache.spring.rest.beanprocessor;

import com.leftstache.spring.rest.core.*;
import com.leftstache.spring.rest.store.*;
import org.springframework.beans.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.beans.factory.config.*;
import org.springframework.stereotype.*;

/**
 * @author Joel Johnson
 */
@Component
public class ServiceBeanProcessor implements BeanPostProcessor {
	@Autowired
	private ServiceStore serviceStore;

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		if(bean != null && bean instanceof RestfulService) {
			serviceStore.registerService(bean);
		}
		return bean;
	}

	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException { return bean; }
}
