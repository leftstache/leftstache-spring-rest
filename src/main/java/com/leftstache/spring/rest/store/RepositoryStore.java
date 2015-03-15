package com.leftstache.spring.rest.store;

import com.leftstache.spring.rest.core.*;
import org.springframework.data.repository.*;
import org.springframework.stereotype.*;

import java.io.*;
import java.util.*;
import java.util.concurrent.*;

/**
 * @author Joel Johnson
 */
@Component
public class RepositoryStore {
	private final Map<String, Class> entityTypeMap = new ConcurrentHashMap<>();
	private final Map<String, Class> idTypeMap = new ConcurrentHashMap<>();
	private final Map<String, PagingAndSortingRepository> repositoryMap = new ConcurrentHashMap<>();

	public void registerRepository(Restful repository) {
		if(!(repository instanceof PagingAndSortingRepository)) {
			throw new IllegalArgumentException("repository must implement Restful and PagingAndSortingRepository");
		}

		entityTypeMap.put(repository.getRestfulName(), repository.getEntityType());
		idTypeMap.put(repository.getRestfulName(), repository.getIdType());
		repositoryMap.put(repository.getRestfulName(), (PagingAndSortingRepository) repository);
	}

	public Class getEntityType(String name) {
		return entityTypeMap.get(name);
	}

	public Class getIdType(String name) {
		return idTypeMap.get(name);
	}

	public <ENTITY, ID extends Serializable> PagingAndSortingRepository<ENTITY, ID> getRepository(String name) {
		return repositoryMap.get(name);
	}
}
