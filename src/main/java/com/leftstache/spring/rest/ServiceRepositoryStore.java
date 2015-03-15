package com.leftstache.spring.rest;

import org.springframework.data.repository.*;
import org.springframework.stereotype.*;

import java.io.*;
import java.util.*;

/**
 * @author Joel Johnson
 */
@Component
public class ServiceRepositoryStore {
	private Map<Class<?>, PagingAndSortingRepository<?, ?>> typeToRepository = new HashMap<>();
	private Map<Class<?>, ServiceWrapper> typeToService = new HashMap<>();
	private Map<Class<?>, String> typeToName = new HashMap<>();
	private Map<String, Class<?>> nameToType = new HashMap<>();
	private Map<String, Class<? extends Serializable>> nameToIdType = new HashMap<>();

	public void registerRepository(Class<?> entityType, Class<? extends Serializable> idType, String name, PagingAndSortingRepository<?, ?> repository) {
		addNameTypeRelationship(entityType, idType, name);

		typeToRepository.put(entityType, repository);
	}

	public void registerService(Class<?> entityType, String name, Object service) {
		ServiceWrapper serviceWrapper = new ServiceWrapper(this, name, service);
		typeToService.put(entityType, serviceWrapper);
	}

	private void addNameTypeRelationship(Class<?> entityType, Class<? extends Serializable> idType, String name) {
		if(typeToName.containsKey(entityType)) {
			throw new IllegalArgumentException("Repository with entity type " + entityType + " already registered");
		}

		if(nameToType.containsKey(name)) {
			throw new IllegalArgumentException("Repository with name " + name + " already registered to " + nameToType.get(name));
		}

		if(nameToIdType.containsKey(name)) {
			throw new IllegalArgumentException("Repository with name " + name + " already registered to " + nameToIdType.get(name));
		}

		typeToName.put(entityType, name);
		nameToType.put(name, entityType);
		nameToIdType.put(name, idType);
	}

	public PagingAndSortingRepository<?, ?> findRepositoryForName(String name) {
		Class<?> type = nameToType.get(name);
		if(type == null) {
			return null;
		}

		PagingAndSortingRepository<?, ?> repository = typeToRepository.get(type);
		if(repository == null) {
			return null;
		}

		return repository;
	}

	public Class<?> classForName(String name) {
		return nameToType.get(name);
	}

	public Class<? extends Serializable> idTypeForName(String name) {
		return nameToIdType.get(name);
	}
}
