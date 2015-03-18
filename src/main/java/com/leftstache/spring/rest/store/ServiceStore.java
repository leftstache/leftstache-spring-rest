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

	public void registerService(Object object) {
		if(object == null) {
			throw new NullPointerException("object");
		}


	}

	public Class getEntityType(String name) {
		return entityTypeMap.get(name);
	}

	public Class getIdType(String name) {
		return idTypeMap.get(name);
	}

	public boolean supports(String name) {
		return idTypeMap.containsKey(name);
	}
}
