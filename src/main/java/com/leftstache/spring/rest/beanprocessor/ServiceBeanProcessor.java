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
		if(bean instanceof CreateLogic) {
			serviceStore.registerCreateLogic((CreateLogic) bean);
		}

		if(bean instanceof GetLogic) {
			serviceStore.registerGetLogic((GetLogic)bean);
		}

		if(bean instanceof DeleteLogic) {
			serviceStore.registerDeleteLogic((DeleteLogic)bean);
		}

		return bean;
	}

	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException { return bean; }
}
