package com.leftstache.spring.rest.store;

import com.leftstache.spring.rest.core.*;
import org.junit.*;

/**
 * @author Joel Johnson
 */
public class ServiceStoreTest {
	@Test
	public void testAutoDiscoverEntityType() {
		ExampleCreate exampleCreate = new ExampleCreate();
		Assert.assertEquals(Example.class, exampleCreate.getEntityType());
		Assert.assertEquals(Long.class, exampleCreate.getIdType());
		Assert.assertEquals("example", exampleCreate.getRestfulName());
	}

	@Test
	public void testAutoDiscoverEntityType_doubleInherit_class() {
		DoubleInherit exampleCreate = new DoubleInherit();
		Assert.assertEquals(Example.class, exampleCreate.getEntityType());
		Assert.assertEquals(Long.class, exampleCreate.getIdType());
		Assert.assertEquals("example", exampleCreate.getRestfulName());
	}

	@Test
	public void testAutoDiscoverEntityType_doubleInherit_interface() {
		DoubleInheritInterfaceImpl exampleCreate = new DoubleInheritInterfaceImpl();
		Assert.assertEquals(Example.class, exampleCreate.getEntityType());
		Assert.assertEquals(Long.class, exampleCreate.getIdType());
		Assert.assertEquals("example", exampleCreate.getRestfulName());
	}

	private static interface DoubleInheritInterface extends CreateLogic<Example, Long> {

	}

	private static class DoubleInheritInterfaceImpl implements DoubleInheritInterface {
		@Override
		public Example create(Example object) {
			return null;
		}
	}

	private static class DoubleInherit extends ExampleCreate {

	}

	private static class ExampleCreate implements CreateLogic<Example, Long> {

		@Override
		public Example create(Example object) {
			return null;
		}
	}

	private static class Example {}
}
