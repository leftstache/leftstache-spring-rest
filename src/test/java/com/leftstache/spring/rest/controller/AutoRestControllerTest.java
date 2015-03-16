package com.leftstache.spring.rest.controller;

import com.fasterxml.jackson.databind.*;
import com.leftstache.spring.rest.store.*;
import com.leftstache.spring.rest.stub.*;
import com.leftstache.spring.rest.util.*;
import org.junit.*;
import org.springframework.data.repository.*;


import java.util.*;

import static org.mockito.Mockito.*;

/**
 * @author Joel Johnson
 */
public class AutoRestControllerTest {
	private static final String ENTITY_NAME = "example";
	private ObjectMapper objectMapper;
	private BeanPatcher beanPatcher;

	private RepositoryStub<Example, Long> repository;

	private RepositoryStore repositoryStore;
	private ServiceStore serviceStore;

	@Before
	public void setUp() {
		objectMapper = new ObjectMapper();
		beanPatcher = new BeanPatcher(objectMapper);
		repository = new RepositoryStub<>();

		repositoryStore = mock(RepositoryStore.class);
		serviceStore = mock(ServiceStore.class);
	}

	@Test
	public void testGet_repository() {
		when(repositoryStore.getRepository(ENTITY_NAME)).thenReturn((PagingAndSortingRepository)repository);
		when(repositoryStore.getIdType(ENTITY_NAME)).thenReturn(Long.class);
		when(repositoryStore.supports(ENTITY_NAME)).thenReturn(true);
		when(serviceStore.supports(ENTITY_NAME)).thenReturn(false);
		when(serviceStore.getGetLogic(any())).thenReturn(null);

		Example entity = new Example();
		repository.add(1L, entity);

		AutoRestController autoRestController = new AutoRestController(repositoryStore, serviceStore, objectMapper, beanPatcher);
		Object byId = autoRestController.getById(ENTITY_NAME, "1");
		Assert.assertTrue(entity == byId);
	}

	@Test
	public void testGet_service() {
		when(repositoryStore.getRepository(ENTITY_NAME)).thenReturn((PagingAndSortingRepository)repository);
		when(repositoryStore.supports(ENTITY_NAME)).thenReturn(true);
		when(serviceStore.supports(ENTITY_NAME)).thenReturn(true);
		when(serviceStore.getIdType(ENTITY_NAME)).thenReturn(Long.class);

		Example serviceEntity = new Example();
		when(serviceStore.getGetLogic(any())).thenReturn(aLong -> serviceEntity);

		Example repoEntity = new Example();
		repository.add(1L, repoEntity);

		AutoRestController autoRestController = new AutoRestController(repositoryStore, serviceStore, objectMapper, beanPatcher);
		Object byId = autoRestController.getById(ENTITY_NAME, "1");
		Assert.assertTrue(serviceEntity == byId);
	}

	@Test
	public void testGet_none() {
		when(repositoryStore.getRepository(ENTITY_NAME)).thenReturn(null);
		when(serviceStore.getGetLogic(any())).thenReturn(null);
		when(repositoryStore.supports(ENTITY_NAME)).thenReturn(false);
		when(serviceStore.supports(ENTITY_NAME)).thenReturn(false);
		AutoRestController autoRestController = new AutoRestController(repositoryStore, serviceStore, objectMapper, beanPatcher);

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
		when(repositoryStore.supports(ENTITY_NAME)).thenReturn(true);
		when(serviceStore.supports(ENTITY_NAME)).thenReturn(false);
		when(serviceStore.getDeleteLogic(any())).thenReturn(null);

		Example entity = new Example();
		repository.add(1L, entity);

		AutoRestController autoRestController = new AutoRestController(repositoryStore, serviceStore, objectMapper, beanPatcher);
		autoRestController.deleteById(ENTITY_NAME, "1");
		Assert.assertFalse(repository.exists(1L));
	}


	@Test
	public void testDelete_service() {
		when(repositoryStore.getRepository(ENTITY_NAME)).thenReturn((PagingAndSortingRepository)repository);
		when(serviceStore.getIdType(ENTITY_NAME)).thenReturn(Long.class);
		when(repositoryStore.supports(ENTITY_NAME)).thenReturn(true);
		when(serviceStore.supports(ENTITY_NAME)).thenReturn(true);

		Long[] deleted = new Long[1];
		when(serviceStore.getDeleteLogic(any())).thenReturn(aLong -> deleted[0] = (Long)aLong);

		Example repoEntity = new Example();
		repository.add(1L, repoEntity);

		AutoRestController autoRestController = new AutoRestController(repositoryStore, serviceStore, objectMapper, beanPatcher);
		autoRestController.deleteById(ENTITY_NAME, "1");
		Assert.assertEquals((Object) 1L, deleted[0]);
	}



	@Test
	public void testDelete_none() {
		when(repositoryStore.getRepository(ENTITY_NAME)).thenReturn(null);
		when(serviceStore.getDeleteLogic(any())).thenReturn(null);
		when(repositoryStore.supports(ENTITY_NAME)).thenReturn(false);
		when(serviceStore.supports(ENTITY_NAME)).thenReturn(false);
		AutoRestController autoRestController = new AutoRestController(repositoryStore, serviceStore, objectMapper, beanPatcher);

		try {
			autoRestController.deleteById(ENTITY_NAME, "1");
		} catch (RuntimeException e) {
			Assert.assertEquals("Unsupported endpoint: example", e.getMessage());
		}
	}


	@Test
	public void testCreate_repository() {
		when(repositoryStore.getRepository(ENTITY_NAME)).thenReturn((PagingAndSortingRepository)repository);
		when(repositoryStore.getEntityType(ENTITY_NAME)).thenReturn(Example.class);
		when(serviceStore.getGetLogic(any())).thenReturn(null);
		when(repositoryStore.supports(ENTITY_NAME)).thenReturn(true);
		when(serviceStore.supports(ENTITY_NAME)).thenReturn(false);

		AutoRestController autoRestController = new AutoRestController(repositoryStore, serviceStore, objectMapper, beanPatcher);

		Map<String, Object> rawObject = new HashMap<>();
		rawObject.put("name", "test value");
		Example byId = (Example) autoRestController.create(ENTITY_NAME, rawObject);
		Assert.assertEquals("test value", byId.getName());

		List<Example> saved = repository.getSaved();
		Assert.assertEquals(1, saved.size());
		Assert.assertEquals(byId, saved.get(0));
	}

	@Test
	public void testCreate_service() {
		when(repositoryStore.getRepository(ENTITY_NAME)).thenReturn((PagingAndSortingRepository)repository);
		when(serviceStore.getEntityType(ENTITY_NAME)).thenReturn(Example.class);
		when(repositoryStore.supports(ENTITY_NAME)).thenReturn(true);
		when(serviceStore.supports(ENTITY_NAME)).thenReturn(true);

		Example serviceEntity = new Example();
		when(serviceStore.getCreateLogic(any())).thenReturn(newEntity -> serviceEntity);

		Example repoEntity = new Example();
		repository.add(1L, repoEntity);

		AutoRestController autoRestController = new AutoRestController(repositoryStore, serviceStore, objectMapper, beanPatcher);

		Map<String, Object> rawObject = new HashMap<>();
		rawObject.put("name", "test value");
		Example byId = (Example) autoRestController.create(ENTITY_NAME, rawObject);

		Assert.assertEquals(serviceEntity, byId);

		List<Example> saved = repository.getSaved();
		Assert.assertEquals(0, saved.size());
	}

	@Test
	public void testCreate_none() {
		when(repositoryStore.getRepository(ENTITY_NAME)).thenReturn(null);
		when(serviceStore.getCreateLogic(any())).thenReturn(null);
		when(repositoryStore.supports(ENTITY_NAME)).thenReturn(false);
		when(serviceStore.supports(ENTITY_NAME)).thenReturn(false);
		AutoRestController autoRestController = new AutoRestController(repositoryStore, serviceStore, objectMapper, beanPatcher);

		try {
			autoRestController.create(ENTITY_NAME, new HashMap<>());
		} catch (RuntimeException e) {
			Assert.assertEquals("Unsupported endpoint: example", e.getMessage());
		}
	}

	public static class Example {
		private String name;

		public Example() {
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
	}
}
