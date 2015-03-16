package com.leftstache.spring.rest.controller;

import com.fasterxml.jackson.databind.*;
import com.leftstache.spring.rest.core.*;
import com.leftstache.spring.rest.store.*;
import com.leftstache.spring.rest.stub.*;
import org.junit.*;
import org.springframework.data.repository.*;


import static org.mockito.Mockito.*;

/**
 * @author Joel Johnson
 */
public class AutoRestControllerTest {
	private static final String ENTITY_NAME = "example";
	private ObjectMapper objectMapper;

	private RepositoryStub<Example, Long> repository;

	private RepositoryStore repositoryStore;
	private ServiceStore serviceStore;

	@Before
	public void setUp() {
		objectMapper = new ObjectMapper();
		repository = new RepositoryStub<>();

		repositoryStore = mock(RepositoryStore.class);
		serviceStore = mock(ServiceStore.class);
	}

	@Test
	public void testGet_repository() {
		when(repositoryStore.getRepository(ENTITY_NAME)).thenReturn((PagingAndSortingRepository)repository);
		when(repositoryStore.getIdType(ENTITY_NAME)).thenReturn(Long.class);
		when(serviceStore.getGetLogic(any())).thenReturn(null);

		Example entity = new Example();
		repository.add(1L, entity);

		AutoRestController autoRestController = new AutoRestController(repositoryStore, serviceStore, objectMapper);
		Object byId = autoRestController.getById(ENTITY_NAME, "1");
		Assert.assertTrue(entity == byId);
	}

	@Test
	public void testGet_service() {
		when(repositoryStore.getRepository(ENTITY_NAME)).thenReturn((PagingAndSortingRepository)repository);
		when(serviceStore.getIdType(ENTITY_NAME)).thenReturn(Long.class);

		Example serviceEntity = new Example();
		when(serviceStore.getGetLogic(any())).thenReturn(aLong -> serviceEntity);

		Example repoEntity = new Example();
		repository.add(1L, repoEntity);

		AutoRestController autoRestController = new AutoRestController(repositoryStore, serviceStore, objectMapper);
		Object byId = autoRestController.getById(ENTITY_NAME, "1");
		Assert.assertTrue(serviceEntity == byId);
	}

	@Test
	public void testGet_none() {
		when(repositoryStore.getRepository(ENTITY_NAME)).thenReturn(null);
		when(serviceStore.getGetLogic(any())).thenReturn(null);
		AutoRestController autoRestController = new AutoRestController(repositoryStore, serviceStore, objectMapper);

		try {
			autoRestController.getById(ENTITY_NAME, "1");
		} catch (RuntimeException e) {
			Assert.assertEquals("Unsupported endpoint: example", e.getMessage());
		}
	}

	@Test
	public void testDelete_repository() {
		when(repositoryStore.getRepository(ENTITY_NAME)).thenReturn((PagingAndSortingRepository)repository);
		when(repositoryStore.getIdType(ENTITY_NAME)).thenReturn(Long.class);
		when(serviceStore.getDeleteLogic(any())).thenReturn(null);

		Example entity = new Example();
		repository.add(1L, entity);

		AutoRestController autoRestController = new AutoRestController(repositoryStore, serviceStore, objectMapper);
		autoRestController.deleteById(ENTITY_NAME, "1");
		Assert.assertFalse(repository.exists(1L));
	}


	@Test
	public void testDelete_service() {
		when(repositoryStore.getRepository(ENTITY_NAME)).thenReturn((PagingAndSortingRepository)repository);
		when(serviceStore.getIdType(ENTITY_NAME)).thenReturn(Long.class);

		Long[] deleted = new Long[1];
		when(serviceStore.getDeleteLogic(any())).thenReturn(aLong -> deleted[0] = (Long)aLong);

		Example repoEntity = new Example();
		repository.add(1L, repoEntity);

		AutoRestController autoRestController = new AutoRestController(repositoryStore, serviceStore, objectMapper);
		autoRestController.deleteById(ENTITY_NAME, "1");
		Assert.assertEquals((Object) 1L, deleted[0]);
	}



	@Test
	public void testDelete_none() {
		when(repositoryStore.getRepository(ENTITY_NAME)).thenReturn(null);
		when(serviceStore.getDeleteLogic(any())).thenReturn(null);
		AutoRestController autoRestController = new AutoRestController(repositoryStore, serviceStore, objectMapper);

		try {
			autoRestController.deleteById(ENTITY_NAME, "1");
		} catch (RuntimeException e) {
			Assert.assertEquals("Unsupported endpoint: example", e.getMessage());
		}
	}

	private class Example {

	}
}
