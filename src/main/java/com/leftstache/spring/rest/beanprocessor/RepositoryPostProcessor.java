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

		if(bean instanceof PagingAndSortingRepository && bean instanceof Restful) {
			repositoryStore.registerRepository((Restful)bean);
		}
		
		return bean;
	}

	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		return bean;
	}
}
