package com.leftstache.spring.rest.store;

import com.leftstache.spring.rest.core.*;
import org.springframework.stereotype.*;

import java.io.*;
import java.util.*;
import java.util.concurrent.*;

/**
 * @author Joel Johnson
 */
@Component
public class ServiceStore {
	private final Map<String, Class> entityTypeMap = new ConcurrentHashMap<>();
	private final Map<String, Class> idTypeMap = new ConcurrentHashMap<>();

	private final Map<String, CreateLogic> createLogicMap = new ConcurrentHashMap<>();
	private final Map<String, GetLogic> getLogicMap = new ConcurrentHashMap<>();
	private final Map<String, DeleteLogic> deleteLogicMap = new ConcurrentHashMap<>();

	public void registerCreateLogic(CreateLogic object) {
		if(object == null) {
			throw new NullPointerException("object");
		}

		String restfulName = object.getRestfulName();
		entityTypeMap.put(restfulName, object.getEntityType());
		idTypeMap.put(restfulName, object.getIdType());
		createLogicMap.put(restfulName, object);
	}

	public void registerGetLogic(GetLogic object) {
		if(object == null) {
			throw new NullPointerException("object");
		}

		String restfulName = object.getRestfulName();
		entityTypeMap.put(restfulName, object.getEntityType());
		idTypeMap.put(restfulName, object.getIdType());
		getLogicMap.put(restfulName, object);
	}

	public void registerDeleteLogic(DeleteLogic object) {
		if(object == null) {
			throw new NullPointerException("object");
		}

		String restfulName = object.getRestfulName();
		entityTypeMap.put(restfulName, object.getEntityType());
		idTypeMap.put(restfulName, object.getIdType());
		deleteLogicMap.put(restfulName, object);
	}

	public Class getEntityType(String name) {
		return entityTypeMap.get(name);
	}

	public Class getIdType(String name) {
		return idTypeMap.get(name);
	}

	public boolean supports(String name) {
		return idTypeMap.containsKey(name);
	}

	public <E, I extends Serializable> CreateLogic<E, I> getCreateLogic(String name) {
		return createLogicMap.get(name);
	}

	public <E, I extends Serializable> GetLogic<E, I> getGetLogic(String name) {
		return getLogicMap.get(name);
	}

	public <E, I extends Serializable> DeleteLogic<E, I> getDeleteLogic(String name) {
		return deleteLogicMap.get(name);
	}
}
