package com.leftstache.spring.rest.core;

import com.leftstache.spring.rest.exception.*;
import com.leftstache.spring.rest.util.*;

import java.beans.*;
import java.io.*;

/**
 * @author Joel Johnson
 */
public interface Restful<ENTITY, ID extends Serializable> {
	default String getRestfulName() {
		return Introspector.decapitalize(getEntityType().getSimpleName());
	}

	default Class<ENTITY> getEntityType() {
		Class<ENTITY> genericType = ReflectionUtil.findGenericType(this.getClass(), Restful.class, 0);
		if(genericType == null) {
			throw new RestfulConfigurationException("unable to infer entity type for " + this);
		}
		return genericType;
	}

	default Class<ID> getIdType() {
		Class<ID> genericType = ReflectionUtil.findGenericType(this.getClass(), Restful.class, 1);
		if(genericType == null) {
			throw new RestfulConfigurationException("unable to infer id type for " + this);
		}
		return genericType;
	}
}
