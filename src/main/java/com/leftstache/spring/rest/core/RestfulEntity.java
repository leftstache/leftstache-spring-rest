package com.leftstache.spring.rest.core;

import com.leftstache.spring.rest.util.*;

import java.beans.*;
import java.io.*;

/**
 * @author Joel Johnson
 */
public interface RestfulEntity<ENTITY, ID extends Serializable> {
	default String getRestfulName() {
		return Introspector.decapitalize(getEntityType().getSimpleName());
	}

	default Class<ENTITY> getEntityType() {
		return ReflectionUtil.findGenericType(this.getClass(), CreateLogic.class, 0);
	}

	default Class<ID> getIdType() {
		return ReflectionUtil.findGenericType(this.getClass(), CreateLogic.class, 1);
	}
}
