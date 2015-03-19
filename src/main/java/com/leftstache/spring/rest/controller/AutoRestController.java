package com.leftstache.spring.rest.controller;

import com.leftstache.spring.rest.core.*;
import com.leftstache.spring.rest.store.*;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.data.domain.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.*;
import java.util.*;

/**
 * @author Joel Johnson
 */
@RestController
public class AutoRestController {
	private final ServiceStore serviceStore;

	@Autowired
	public AutoRestController(ServiceStore serviceStore) {
		this.serviceStore = serviceStore;
	}

	@RequestMapping(value = "/{name}", method = RequestMethod.GET)
	public ResponseEntity<Page<Object>> search(
		@PathVariable("name") String name,
		@RequestParam Map<String, String[]> queryString
	) {
		ServiceWrapper service = serviceStore.getService(name);
		if(service.supports(RestEndpoint.Type.SEARCH)) {
			return service.search(queryString);
		}

		throw new RuntimeException("Unsupported endpoint: " + name);
	}

	@RequestMapping(value = "/{name}/{id}", method = RequestMethod.GET)
	public ResponseEntity<Object> getById(@PathVariable("name") String name, @PathVariable("id") String idRaw) {
		ServiceWrapper service = serviceStore.getService(name);
		if(service.supports(RestEndpoint.Type.GET)) {
			return service.get(idRaw);
		}

		throw new RuntimeException("Unsupported endpoint: " + name);
	}

	@RequestMapping(value = "/{name}", method = RequestMethod.POST)
	public ResponseEntity<Void> create(@PathVariable("name") String name, @RequestBody Map<String, Object> requestBody, HttpServletRequest request) {
		ServiceWrapper service = serviceStore.getService(name);
		if(service.supports(RestEndpoint.Type.CREATE)) {
			return service.create(request.getRequestURI(), requestBody);
		}

		throw new RuntimeException("Unsupported endpoint: " + name);
	}

	@RequestMapping(value = "/{name}/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Void> deleteById(@PathVariable("name") String name, @PathVariable("id") String idRaw) {
		ServiceWrapper service = serviceStore.getService(name);
		if(service.supports(RestEndpoint.Type.DELETE)) {
			return service.delete(idRaw);
		}

		throw new RuntimeException("Unsupported endpoint: " + name);
	}

	@RequestMapping(value ="/{name}/{id}", method = RequestMethod.PATCH)
	public ResponseEntity<Object> patch( @PathVariable("name") String name, @PathVariable("id") String idRaw, @RequestBody Map<String, Object> changes) {
		ServiceWrapper service = serviceStore.getService(name);
		if(service.supports(RestEndpoint.Type.EDIT)) {
			return service.edit(idRaw, changes);
		}

		throw new RuntimeException("Unsupported endpoint: " + name);
	}

}
