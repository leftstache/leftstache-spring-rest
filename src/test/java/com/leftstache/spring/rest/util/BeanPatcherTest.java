package com.leftstache.spring.rest.util;

import com.fasterxml.jackson.databind.*;
import org.junit.*;

import java.util.*;

import static org.junit.Assert.assertEquals;

/**
 * @author Joel Johnson
 */
public class BeanPatcherTest {

	private BeanPatcher beanPatcher;

	@Before
	public void setUp() {
		ObjectMapper objectMapper = new ObjectMapper();
		beanPatcher = new BeanPatcher(objectMapper);
	}

	@Test
	public void testSimplePatch() throws BeanPatcher.InputException, BeanPatcher.PropertyException {
		SimpleExample simpleExample = new SimpleExample("name initial", 10, "readonly initial");

		Map<String, Object> patch = new HashMap<>();
		patch.put("name", "new name");
		patch.put("age", 20);
		patch.put("readonly", "shouldn't change");

		beanPatcher.patch(simpleExample, patch);

		assertEquals("new name", simpleExample.getName());
		assertEquals(20, simpleExample.getAge());
		assertEquals("readonly initial", simpleExample.getReadonly());
	}

	@Test
	public void testNestedPatch() throws BeanPatcher.InputException, BeanPatcher.PropertyException {
		SimpleExample simpleExample = new SimpleExample("name initial", 10, "readonly initial");
		NestedExample nestedExample = new NestedExample("initial", simpleExample);

		Map<String, Object> simplePatch = new HashMap<>();
		simplePatch.put("name", "new name");
		simplePatch.put("age", 20);
		simplePatch.put("readonly", "shouldn't change");

		Map<String, Object> patch = new HashMap<>();
		patch.put("value", "new value");
		patch.put("simpleExample", simplePatch);

		beanPatcher.patch(nestedExample, patch);

		assertEquals("new value", nestedExample.getValue());

		simpleExample = nestedExample.getSimpleExample();
		assertEquals("new name", simpleExample.getName());
		assertEquals(20, simpleExample.getAge());
		assertEquals("readonly initial", simpleExample.getReadonly());
	}

	private static class SimpleExample {
		private String name;
		private int age;
		private String readonly;

		public SimpleExample() {

		}

		public SimpleExample(String name, int age, String readonly) {
			this.name = name;
			this.age = age;
			this.readonly = readonly;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public int getAge() {
			return age;
		}

		public void setAge(int age) {
			this.age = age;
		}

		public String getReadonly() {
			return readonly;
		}
	}

	private static class NestedExample {
		private String value;
		private SimpleExample simpleExample;

		public NestedExample(String value, SimpleExample simpleExample) {
			this.value = value;
			this.simpleExample = simpleExample;
		}

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}

		public SimpleExample getSimpleExample() {
			return simpleExample;
		}

		public void setSimpleExample(SimpleExample simpleExample) {
			this.simpleExample = simpleExample;
		}
	}
}
