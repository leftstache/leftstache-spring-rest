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

	private final ServiceStore serviceStore;
	private final ObjectMapper objectMapper;
	private final BeanPatcher beanPatcher;

	@Autowired
	public AutoRestController(ServiceStore serviceStore, ObjectMapper objectMapper, BeanPatcher beanPatcher) {
		this.serviceStore = serviceStore;
		this.objectMapper = objectMapper;
		this.beanPatcher = beanPatcher;
	}

	@RequestMapping(value = "/{name}", method = RequestMethod.GET)
	public Page<?> search(
		@PathVariable("name") String name,
		@RequestParam(value = "page", defaultValue = "0") int page,
		@RequestParam(value = "pageSize", defaultValue = "100") int pageSize,
		@RequestParam(value = "sortOrder", defaultValue = "ASC") String sortOrderStr,
		@RequestParam(value = "sortBy", required = false) String[] sortBy
	) {
		throw unsupportedException(name);
	}

	@RequestMapping(value = "/{name}/{id}", method = RequestMethod.GET)
	public Object getById(@PathVariable("name") String name, @PathVariable("id") String idRaw) {
		throw unsupportedException(name);
	}

	@RequestMapping(value = "/{name}", method = RequestMethod.POST)
	public ResponseEntity<Object> create(@PathVariable("name") String name, @RequestBody Map<String, Object> requestBody) {
		throw unsupportedException(name);
	}

	@RequestMapping(value = "/{name}/{id}", method = RequestMethod.DELETE)
	public void deleteById(@PathVariable("name") String name, @PathVariable("id") String idRaw) {
		throw unsupportedException(name);
	}

	@RequestMapping(value ="/{name}/{id}", method = RequestMethod.PATCH)
	public ResponseEntity<Object> patch( @PathVariable("name") String name, @PathVariable("id") String idRaw, @RequestBody Map<String, Object> changes) {
		throw unsupportedException(name);
	}

	private RuntimeException unsupportedException(String name) {
		return new RuntimeException("Unsupported endpoint: " + name); // TODO
	}
}
