package com.leftstache.spring.rest.core;

import java.io.*;
import java.lang.annotation.*;

/**
 * @author Joel Johnson
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface Restful {
	/**
	 * If on a Repository, the name of the base path that should be exposed via rest. Typically the name of the entity being served.
	 * If on a @Service, the name used on the corresponding Repository.
	 */
	String name() default "";

	/**
	 * The type of the entity
	 */
	Class<?> entity();

	/**
	 * The type of the ID of the entity. Required for Services or Repositories with Get functionality.
	 */
	Class<? extends Serializable> id() default Long.class;
}
