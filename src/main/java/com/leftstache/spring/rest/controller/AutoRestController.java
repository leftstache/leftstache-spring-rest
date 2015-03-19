package com.leftstache.spring.rest.controller;

import com.fasterxml.jackson.core.type.*;
import com.fasterxml.jackson.databind.*;
import com.leftstache.spring.rest.core.*;
import com.leftstache.spring.rest.store.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.data.domain.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.*;
import java.io.*;
import java.util.*;

/**
 * @author Joel Johnson
 */
@RestController
public class AutoRestController {
	private final ServiceStore serviceStore;
	private final ObjectMapper objectMapper;

	@Autowired
	public AutoRestController(ServiceStore serviceStore, ObjectMapper objectMapper) {
		this.serviceStore = serviceStore;
		this.objectMapper = objectMapper;
	}

	@RequestMapping(value = "/{name}", method = RequestMethod.GET)
	public ResponseEntity<Page<Object>> search(
		@PathVariable("name") String name,
		@RequestParam(required = false) Map<String, String> queryString,
		@RequestBody(required = false) Map<String, Object> queryMap
	) throws IOException {
		if(queryMap == null && queryString.containsKey("query")) {
			TypeReference<Map<String, Object>> typeReference = new TypeReference<Map<String, Object>>() {};
			queryMap = objectMapper.readValue(queryString.get("query"), typeReference);
		}

		ServiceWrapper service = serviceStore.getService(name);
		if(service != null) {
			if (service.supports(RestEndpoint.Type.SEARCH)) {
				return service.search(queryString, queryMap);
			}

			return notSupported();
		}

		return notFound();
	}

	@RequestMapping(value = "/{name}/{id}", method = RequestMethod.GET)
	public ResponseEntity<Object> getById(@PathVariable("name") String name, @PathVariable("id") String idRaw) {
		ServiceWrapper service = serviceStore.getService(name);
		if(service != null) {
			if (service.supports(RestEndpoint.Type.GET)) {
				return service.get(idRaw);
			}

			return notSupported();
		}

		return notFound();
	}

	@RequestMapping(value = "/{name}", method = RequestMethod.POST)
	public ResponseEntity<Void> create(@PathVariable("name") String name, @RequestBody(required = false) Map<String, Object> requestBody, HttpServletRequest request) {
		ServiceWrapper service = serviceStore.getService(name);
		if(service != null) {
			if (requestBody != null && service.supports(RestEndpoint.Type.CREATE)) {
				return service.create(request.getRequestURL().toString(), requestBody);
			}

			return notSupported();
		}

		return notFound();
	}

	@RequestMapping(value = "/{name}/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Void> deleteById(@PathVariable("name") String name, @PathVariable("id") String idRaw) {
		ServiceWrapper service = serviceStore.getService(name);
		if(service != null) {
			if (service.supports(RestEndpoint.Type.DELETE)) {
				return service.delete(idRaw);
			}

			return notSupported();
		}

		return notFound();
	}

	@RequestMapping(value ="/{name}/{id}", method = RequestMethod.PATCH)
	public ResponseEntity<Void> patch( @PathVariable("name") String name, @PathVariable("id") String idRaw, @RequestBody(required = false) Map<String, Object> changes) {
		ServiceWrapper service = serviceStore.getService(name);
		if(service != null) {
			if (changes != null && service.supports(RestEndpoint.Type.EDIT)) {
				return service.edit(idRaw, changes);
			}

			return notSupported();
		}

		return notFound();
	}

	@RequestMapping(value ="/{name}/{id}", method = RequestMethod.PUT)
	public ResponseEntity<Void> replace( @PathVariable("name") String name, @PathVariable("id") String idRaw, @RequestBody(required = false) Map<String, Object> requestBody) {
		ServiceWrapper service = serviceStore.getService(name);
		if(service != null) {
			if (requestBody != null && service.supports(RestEndpoint.Type.REPLACE)) {
				return service.replace(idRaw, requestBody);
			}

			return notSupported();
		}

		return notFound();
	}

	private <T> ResponseEntity<T> notFound() {
		return new ResponseEntity<T>(HttpStatus.NOT_FOUND);
	}

	private <T> ResponseEntity<T> notSupported() {
		return new ResponseEntity<T>(HttpStatus.METHOD_NOT_ALLOWED);
	}
}
