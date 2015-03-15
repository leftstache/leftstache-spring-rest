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
		return repository.findAll(new PageRequest(0, 10));
	}

	@RequestMapping(value = "/{name}/{id}", method = RequestMethod.GET)
	public Object getById(@PathVariable("name") String name, @PathVariable("id") String idRaw) {
		PagingAndSortingRepository<Object, Serializable> repository = (PagingAndSortingRepository<Object, Serializable>) repositoryStore.getRepository(name);

		Class<? extends Serializable> idType = repositoryStore.getIdType(name);
		Serializable id = objectMapper.convertValue(idRaw, idType);

		return repository.findOne(id);
	}

	@RequestMapping(value = "/{name}", method = RequestMethod.POST)
	public Object create(@PathVariable("name") String name, @RequestBody Map<String, Object> requestBody) {
		Class<?> repoType = repositoryStore.getEntityType(name);
		Object object = objectMapper.convertValue(requestBody, repoType);

		CreateLogic<Object, Serializable> createLogic = serviceStore.getCreateLogic(name);
		if(createLogic != null) {
			return createLogic.createLogic(object);
		} else {
			PagingAndSortingRepository<Object, ?> repository = (PagingAndSortingRepository<Object, ?>) repositoryStore.getRepository(name);
			if(repository != null) {
				return repository.save(object);
			}
		}

		throw new NullPointerException(name); //TODO
	}
}
