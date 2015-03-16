package com.leftstache.spring.rest.controller;

import com.fasterxml.jackson.databind.*;
import com.leftstache.spring.rest.core.*;
import com.leftstache.spring.rest.store.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.data.domain.*;
import org.springframework.data.repository.*;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.util.*;

/**
 * @author Joel Johnson
 */
@RestController
public class AutoRestController {
	@Autowired
	private RepositoryStore repositoryStore;

	@Autowired
	private ServiceStore serviceStore;

	@Autowired
	private ObjectMapper objectMapper;

	@RequestMapping(value = "/{name}", method = RequestMethod.GET)
	public Page<?> search(@PathVariable("name") String name) {
		PagingAndSortingRepository<?, ?> repository = repositoryStore.getRepository(name);
		if(repository != null) {
			return repository.findAll(new PageRequest(0, 10));
		}

		throw unsupportedException(name);
	}

	@RequestMapping(value = "/{name}/{id}", method = RequestMethod.GET)
	public Object getById(@PathVariable("name") String name, @PathVariable("id") String idRaw) {
		GetLogic<Object, Serializable> getLogic = serviceStore.getGetLogic(name);
		if(getLogic != null) {
			Class<? extends Serializable> idType = serviceStore.getIdType(name);
			Serializable id = objectMapper.convertValue(idRaw, idType);

			return getLogic.get(id);
		} else {
			PagingAndSortingRepository<Object, Serializable> repository = repositoryStore.getRepository(name);
			if (repository != null) {
				Class<? extends Serializable> idType = repositoryStore.getIdType(name);
				Serializable id = objectMapper.convertValue(idRaw, idType);

				return repository.findOne(id);
			}
		}

		throw unsupportedException(name);
	}

	@RequestMapping(value = "/{name}", method = RequestMethod.POST)
	public Object create(@PathVariable("name") String name, @RequestBody Map<String, Object> requestBody) {
		CreateLogic<Object, Serializable> createLogic = serviceStore.getCreateLogic(name);
		if(createLogic != null) {
			Class entityType = serviceStore.getEntityType(name);
			Object object = objectMapper.convertValue(requestBody, entityType);
			return createLogic.create(object);
		} else {
			PagingAndSortingRepository<Object, Serializable> repository = repositoryStore.getRepository(name);
			if(repository != null) {
				Class entityType = repositoryStore.getEntityType(name);
				Object object = objectMapper.convertValue(requestBody, entityType);
				return repository.save(object);
			}
		}

		throw unsupportedException(name);
	}

	private RuntimeException unsupportedException(String name) {
		return new RuntimeException("Unsupported endpoint: " + name); // TODO
	}
}
