package com.leftstache.spring.rest.core;

import com.leftstache.spring.rest.util.*;

import java.beans.*;

/**
 * @author Joel Johnson
 */
public interface RestfulEntity<T> {
	default String getRestfulName() {
		return Introspector.decapitalize(getEntityType().getSimpleName());
	}

	default Class<T> getEntityType() {
		return ReflectionUtil.findEntityType(this.getClass(), CreateLogic.class);
	}
}
