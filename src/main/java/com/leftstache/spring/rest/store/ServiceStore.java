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

	public void registerCreateLogic(CreateLogic createLogic) {
		if(createLogic == null) {
			throw new NullPointerException("createLogic");
		}

		entityTypeMap.put(createLogic.getRestfulName(), createLogic.getEntityType());
		idTypeMap.put(createLogic.getRestfulName(), createLogic.getIdType());
		createLogicMap.put(createLogic.getRestfulName(), createLogic);
	}

	public Class getEntityType(String name) {
		return entityTypeMap.get(name);
	}

	public Class getIdType(String name) {
		return idTypeMap.get(name);
	}

	public <E, I extends Serializable> CreateLogic<E, I> getCreateLogic(String name) {
		return createLogicMap.get(name);
	}


}
