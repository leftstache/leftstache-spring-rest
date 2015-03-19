package com.leftstache.spring.rest.store;

import com.leftstache.spring.rest.controller.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.context.*;
import org.springframework.stereotype.*;

import java.util.*;
import java.util.concurrent.*;

/**
 * @author Joel Johnson
 */
@Component
public class ServiceStore {
	private Map<String, ServiceWrapper> serviceWrapperMap = new ConcurrentHashMap<>();
	private final Converter converter;
	private final ApplicationContext applicationContext;

	@Autowired
	public ServiceStore(Converter converter, ApplicationContext applicationContext) {
		this.converter = converter;
		this.applicationContext = applicationContext;
	}

	public void registerService(Object object) {
		if(object == null) {
			throw new NullPointerException("object");
		}

		ServiceWrapper serviceWrapper = ServiceWrapper.forObject(converter, applicationContext, object);
		serviceWrapperMap.put(serviceWrapper.getName(), serviceWrapper);
	}

	public ServiceWrapper getService(String name) {
		return serviceWrapperMap.get(name);
	}
}
