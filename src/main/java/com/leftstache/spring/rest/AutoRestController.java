package com.leftstache.spring.rest;

import com.fasterxml.jackson.databind.*;
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
	private ServiceRepositoryStore serviceRepositoryStore;

	@Autowired
	private ObjectMapper objectMapper;

	@RequestMapping(value = "/{name}", method = RequestMethod.GET)
	public Page<?> get(@PathVariable("name") String name) {
		PagingAndSortingRepository<?, ?> repository = serviceRepositoryStore.findRepositoryForName(name);
		return repository.findAll(new PageRequest(0, 10));
	}

	@RequestMapping(value = "/{name}", method = RequestMethod.POST)
	public Object get(@PathVariable("name") String name, @RequestBody Map<String, Object> requestBody) {
		Class<?> repoType = serviceRepositoryStore.classForName(name);
		Object object = objectMapper.convertValue(requestBody, repoType);

		PagingAndSortingRepository<Object, ?> repository = (PagingAndSortingRepository<Object, ?>) serviceRepositoryStore.findRepositoryForName(name);
		return repository.save(object);
	}

	@RequestMapping(value = "/{name}/{id}", method = RequestMethod.GET)
	public Object get(@PathVariable("name") String name, @PathVariable("id") String idRaw) {
		PagingAndSortingRepository<Object, Serializable> repository = (PagingAndSortingRepository<Object, Serializable>) serviceRepositoryStore.findRepositoryForName(name);

		Class<? extends Serializable> idType = serviceRepositoryStore.idTypeForName(name);
		Serializable id = objectMapper.convertValue(idRaw, idType);

		return repository.findOne(id);
	}
}
