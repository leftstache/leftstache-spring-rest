package com.leftstache.spring.rest.core;

import org.springframework.http.*;

import java.lang.annotation.*;

/**
 * @author Joel Johnson
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RestEndpoint {
	Type value();

	public static enum Type {
		GET,
		SEARCH,
		EDIT,
		REPLACE,
		CREATE,
		DELETE
	}
}
