package com.leftstache.spring.rest;

/**
 * @author Joel Johnson
 */
public class ServiceWrapper {
	private final ServiceRepositoryStore serviceRepositoryStore;
	private final String name;
	private final Object service;

	public ServiceWrapper(ServiceRepositoryStore serviceRepositoryStore, String name, Object service) {
		this.serviceRepositoryStore = serviceRepositoryStore;
		this.name = name;
		this.service = service;
	}

	public <T> T invokeCreate(T object) {
		return object;
	}

	public <T> T invokeGet(Object id) {
		return null;
	}

	public <T> T invokeSearch() {
		return null;
	}
}
