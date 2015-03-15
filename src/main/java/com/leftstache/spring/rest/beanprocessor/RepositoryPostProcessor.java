package com.leftstache.spring.rest.beanprocessor;

import com.leftstache.spring.rest.core.*;
import com.leftstache.spring.rest.store.*;
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
public class RepositoryPostProcessor implements BeanPostProcessor {
	private final RepositoryStore repositoryStore;

	@Autowired
	public RepositoryPostProcessor(RepositoryStore repositoryStore) {
		this.repositoryStore = repositoryStore;
	}

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		if(bean == null) {
			return null;
		}

		if(bean instanceof PagingAndSortingRepository) {
			Class<?> beanType = bean.getClass();
			Restful restfulAnnotation = getRestfulAnnotation(beanType);
			if (restfulAnnotation != null) {
				Class<?> type = restfulAnnotation.entity();
				Class<? extends Serializable> idType = restfulAnnotation.id();
				String name = restfulAnnotation.name();
				if (name.isEmpty()) {
					name = type.getClass().getSimpleName();
				}

				repositoryStore.registerRepository(type, idType, name, (PagingAndSortingRepository<?, ?>) bean);
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

	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		return bean;
	}
}
