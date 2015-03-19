package com.leftstache.spring.rest.store;

import com.google.common.collect.*;
import com.leftstache.spring.rest.controller.*;
import com.leftstache.spring.rest.core.*;
import com.leftstache.spring.rest.util.*;
import org.slf4j.*;
import org.springframework.beans.factory.*;
import org.springframework.data.domain.*;
import org.springframework.data.repository.*;
import org.springframework.http.*;
import org.springframework.util.*;

import java.beans.*;
import java.io.*;
import java.lang.reflect.*;
import java.util.*;

/**
 * @author Joel Johnson
 */
public class ServiceWrapper {
	private static final Logger logger = LoggerFactory.getLogger(ServiceWrapper.class);

	private final Converter converter;
	private final BeanFactory beanFactory;
	private final String name;
	private final Object instance;
	private final Class repositoryType;
	private final Class<?> entityType;
	private final Class<? extends Serializable> idType;
	private final Map<RestEndpoint.Type, Method> exposedMethods;
	private final PagingAndSortingRepository repository;

	public static ServiceWrapper forObject(Converter converter, BeanFactory beanFactory, Object object) {
		Class<?> serviceType = object.getClass();
		Class<? extends PagingAndSortingRepository> repositoryType = ReflectionUtil.findGenericType(serviceType, RestfulService.class, 0);
		Class<?> entityType = ReflectionUtil.findGenericType(repositoryType, PagingAndSortingRepository.class, 0);
		Class<? extends Serializable> idType = ReflectionUtil.findGenericType(repositoryType, PagingAndSortingRepository.class, 1);
		String name = Introspector.decapitalize(entityType.getSimpleName());

		ImmutableMap.Builder<RestEndpoint.Type, Method> exposedMethods = ImmutableMap.<RestEndpoint.Type, Method>builder();

		Method[] methods = serviceType.getMethods();
		for (Method method : methods) {
			RestEndpoint annotation = method.getAnnotation(RestEndpoint.class);
			if(annotation != null) {
				logger.info("registering {} endpoint for name '{}'", annotation.value(), name);
				method.setAccessible(true);
				exposedMethods.put(annotation.value(), method);
			}
		}

		return new ServiceWrapper(converter, beanFactory, name, object, repositoryType, entityType, idType, exposedMethods.build());
	}

	private ServiceWrapper(Converter converter, BeanFactory beanFactory, String name, Object instance, Class<? extends PagingAndSortingRepository> repositoryType, Class<?> entityType, Class<? extends Serializable> idType, Map<RestEndpoint.Type, Method> exposedMethods) {
		this.converter = converter;
		this.beanFactory = beanFactory;
		this.name = name;
		this.instance = instance;
		this.repositoryType = repositoryType;
		this.repository = beanFactory.getBean(repositoryType);
		this.entityType = entityType;
		this.idType = idType;
		this.exposedMethods = exposedMethods;
	}

	public String getName() {
		return name;
	}

	public boolean supports(RestEndpoint.Type method) {
		return method == RestEndpoint.Type.GET || method == RestEndpoint.Type.SEARCH || exposedMethods.containsKey(method);
	}

	public ResponseEntity<Object> get(String idStr) {
		Serializable id = converter.convertTo(idType, idStr);
		Object fetchedObject;
		if(exposedMethods.get(RestEndpoint.Type.GET) != null) {
			try {
				fetchedObject = invoke(RestEndpoint.Type.GET, new Object[]{id});
			} catch (InvocationTargetException | IllegalAccessException e) {
				logger.error("Unable to invoke service method", e);
				return new ResponseEntity<Object>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} else {
			fetchedObject = repository.findOne(id);
		}
		ResponseEntity<Object> result = new ResponseEntity<Object>(fetchedObject, HttpStatus.OK);
		return result;
	}

	public ResponseEntity<Page<Object>> search(Map<String, String[]> query) {
		return null;
	}

	public ResponseEntity<Object> edit(String id, Map<String, Object> patch) {
		return null;
	}

	public ResponseEntity<Void> create(String requestUri, Map<String, Object> body) {
		Object toCreate = converter.convertTo(entityType, body);
		Serializable id;
		try {
			id = (Serializable) invoke(RestEndpoint.Type.CREATE, new Object[]{toCreate});
		} catch (InvocationTargetException | IllegalAccessException e) {
			logger.error("Unable to invoke service method", e);
			return new ResponseEntity<Void>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		String idStr = converter.convertTo(String.class, id);
		String location;
		if(requestUri.endsWith("/")) {
			location = requestUri + idStr;
		} else {
			location = requestUri + "/" + idStr;
		}

		MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
		headers.add("location", location);
		return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
	}

	public ResponseEntity<Void> delete(String id) {
		try {
			invoke(RestEndpoint.Type.DELETE, new Object[]{id});
		} catch (InvocationTargetException | IllegalAccessException e) {
			logger.error("Unable to invoke service method", e);
			return new ResponseEntity<Void>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
	}

	private Object invoke(RestEndpoint.Type type, Object[] parameters) throws InvocationTargetException, IllegalAccessException {
		Method method = exposedMethods.get(type);
		return method.invoke(instance, parameters);
	}

	@Override
	public String toString() {
		return name + " -> " + instance.getClass().getName();
	}
}
