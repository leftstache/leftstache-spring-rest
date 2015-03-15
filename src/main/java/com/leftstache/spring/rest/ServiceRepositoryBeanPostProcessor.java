package com.leftstache.spring.rest;

import com.leftstache.spring.rest.core.*;
import org.springframework.beans.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.beans.factory.config.*;

import org.springframework.data.repository.*;
import org.springframework.stereotype.*;

import java.io.*;

/**
 * @author Joel Johnson
 */
@Component
public class ServiceRepositoryBeanPostProcessor implements BeanPostProcessor {
	private final ServiceRepositoryStore serviceRepositoryStore;

	@Autowired
	public ServiceRepositoryBeanPostProcessor(ServiceRepositoryStore serviceRepositoryStore) {
		this.serviceRepositoryStore = serviceRepositoryStore;
	}

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		if(bean == null) {
			return null;
		}

		Class<?> beanType = bean.getClass();
		Restful restfulAnnotation = getRestfulAnnotation(beanType);
		if(restfulAnnotation != null) {
			Class<?> type = restfulAnnotation.entity();
			Class<? extends Serializable> idType = restfulAnnotation.id();
			String name = restfulAnnotation.name();
			if(name.isEmpty()) {
				name = type.getClass().getSimpleName();
			}

			if(bean instanceof PagingAndSortingRepository) {
				registerRepository(name, type, idType, (PagingAndSortingRepository<?, ?>) bean);
			} else {
				Service serviceAnnotation = beanType.getAnnotation(Service.class);
				if(serviceAnnotation != null) {
					registerService(type, idType, bean);
				}
			}
		}

		return bean;
	}

	private Restful getRestfulAnnotation(Class<?> beanType) {
		Restful annotation = beanType.getAnnotation(Restful.class);
		if(annotation == null) {
			Class<?>[] interfaces = beanType.getInterfaces();
			for (Class<?> anInterface : interfaces) {
				annotation = anInterface.getAnnotation(Restful.class);
				if(annotation != null) {
					break;
				}
			}
		}
		return annotation;
	}

	private void registerRepository(String name, Class<?> entityType, Class<? extends Serializable> idType, PagingAndSortingRepository<?, ?> bean) {
		serviceRepositoryStore.registerRepository(entityType, idType, name, bean);
	}

	private void registerService(Class<?> entityType, Class<? extends Serializable> idType, Object bean) {
		serviceRepositoryStore.registerService(entityType, bean);
	}

	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		return bean;
	}
}
