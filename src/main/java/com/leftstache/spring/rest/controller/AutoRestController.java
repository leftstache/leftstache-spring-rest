package com.leftstache.spring.rest.controller;

import com.fasterxml.jackson.databind.*;
import com.leftstache.spring.rest.core.*;
import com.leftstache.spring.rest.store.*;
import com.leftstache.spring.rest.util.*;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.data.domain.*;
import org.springframework.data.repository.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.util.*;

/**
 * @author Joel Johnson
 */
@RestController
public class AutoRestController {
	private Logger logger = LoggerFactory.getLogger(AutoRestController.class);

	private final RepositoryStore repositoryStore;
	private final ServiceStore serviceStore;
	private final ObjectMapper objectMapper;
	private final BeanPatcher beanPatcher;

	@Autowired
	public AutoRestController(RepositoryStore repositoryStore, ServiceStore serviceStore, ObjectMapper objectMapper, BeanPatcher beanPatcher) {
		this.repositoryStore = repositoryStore;
		this.serviceStore = serviceStore;
		this.objectMapper = objectMapper;
		this.beanPatcher = beanPatcher;
	}

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
		if(serviceStore.supports(name)) {
			GetLogic<Object, Serializable> getLogic = serviceStore.getGetLogic(name);
			Class<? extends Serializable> idType = serviceStore.getIdType(name);
			Serializable id = objectMapper.convertValue(idRaw, idType);

			return getLogic.get(id);
		} else if(repositoryStore.supports(name)) {
			PagingAndSortingRepository<Object, Serializable> repository = repositoryStore.getRepository(name);
			Class<? extends Serializable> idType = repositoryStore.getIdType(name);
			Serializable id = objectMapper.convertValue(idRaw, idType);

			return repository.findOne(id);
		}

		throw unsupportedException(name);
	}

	@RequestMapping(value = "/{name}", method = RequestMethod.POST)
	public ResponseEntity<Object> create(@PathVariable("name") String name, @RequestBody Map<String, Object> requestBody) {
		if(serviceStore.supports(name)) {
			CreateLogic<Object, Serializable> createLogic = serviceStore.getCreateLogic(name);
			Class entityType = serviceStore.getEntityType(name);
			Object object = objectMapper.convertValue(requestBody, entityType);

			Object created = createLogic.create(object);
			return new ResponseEntity<Object>(created, HttpStatus.CREATED);
		} else if(repositoryStore.supports(name)) {
			PagingAndSortingRepository<Object, Serializable> repository = repositoryStore.getRepository(name);
			Class entityType = repositoryStore.getEntityType(name);
			Object object = objectMapper.convertValue(requestBody, entityType);

			Object saved = repository.save(object);
			return new ResponseEntity<Object>(saved, HttpStatus.CREATED);
		}

		throw unsupportedException(name);
	}

	@RequestMapping(value = "/{name}/{id}", method = RequestMethod.DELETE)
	public void deleteById(@PathVariable("name") String name, @PathVariable("id") String idRaw) {
		if(serviceStore.supports(name)) {
			DeleteLogic<Object, Serializable> deleteLogic = serviceStore.getDeleteLogic(name);
			Class<? extends Serializable> idType = serviceStore.getIdType(name);
			Serializable id = objectMapper.convertValue(idRaw, idType);

			deleteLogic.delete(id);
			return;
		} else if(repositoryStore.supports(name)) {
			PagingAndSortingRepository<Object, Serializable> repository = repositoryStore.getRepository(name);
			Class<? extends Serializable> idType = repositoryStore.getIdType(name);
			Serializable id = objectMapper.convertValue(idRaw, idType);

			repository.delete(id);
			return;
		}

		throw unsupportedException(name);
	}

	@RequestMapping(value ="/{name}/{id}", method = RequestMethod.PATCH)
	public ResponseEntity<Object> patch( @PathVariable("name") String name, @PathVariable("id") String idRaw, @RequestBody Map<String, Object> changes) {
		if(serviceStore.supports(name)) {
			PatchLogic<Object, Serializable> patchLogic = serviceStore.getPatchLogic(name);

			Class<? extends Serializable> idType = serviceStore.getIdType(name);
			Serializable id = objectMapper.convertValue(idRaw, idType);

			return new ResponseEntity<Object>(patchLogic.patch(id, changes), HttpStatus.ACCEPTED);
		} else if(repositoryStore.supports(name)) {
			PagingAndSortingRepository<Object, Serializable> repository = repositoryStore.getRepository(name);

			Class<? extends Serializable> idType = repositoryStore.getIdType(name);
			Serializable id = objectMapper.convertValue(idRaw, idType);

			Object entity = repository.findOne(id);
			try {
				beanPatcher.patch(entity, changes);
			} catch (BeanPatcher.PropertyException e) {
				logger.error("Internal Server Error", e);
				return new ResponseEntity<Object>(HttpStatus.INTERNAL_SERVER_ERROR);
			} catch (BeanPatcher.InputException e) {
				logger.info("Unprocessable Entity", e);
				return new ResponseEntity<Object>(HttpStatus.UNPROCESSABLE_ENTITY);
			}

			Object savedEntity = repository.save(entity);
			return new ResponseEntity<Object>(savedEntity, HttpStatus.OK);
		}

		throw unsupportedException(name);
	}

	private RuntimeException unsupportedException(String name) {
		return new RuntimeException("Unsupported endpoint: " + name); // TODO
	}
}
