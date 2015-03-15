package com.leftstache.spring.rest.store;

import com.leftstache.spring.rest.core.*;
import org.springframework.stereotype.*;

import java.util.*;
import java.util.concurrent.*;

/**
 * @author Joel Johnson
 */
@Component
public class ServiceStore {
	private final Map<String, CreateLogic> createLogicMap = new ConcurrentHashMap<>();

	public void registerCreateLogic(CreateLogic createLogic) {
		if(createLogic == null) {
			throw new NullPointerException("createLogic");
		}

		String restfulName = createLogic.restfulName();
		createLogicMap.put(restfulName, createLogic);
	}

	public CreateLogic getCreateLogic(String name) {
		return createLogicMap.get(name);
	}
}
