package com.leftstache.spring.rest.util;

import sun.reflect.generics.reflectiveObjects.*;

import java.lang.reflect.*;

/**
 * @author Joel Johnson
 */
public final class ReflectionUtil {

	public static Class findGenericType(Class startClass, Class targetInterface, int index) {
		Class currentClass = startClass;
		while(currentClass != null && currentClass != Object.class) {
			Type[] genericInterfaces = currentClass.getGenericInterfaces();
			for (Type genericInterface : genericInterfaces) {
				if(genericInterface instanceof ParameterizedTypeImpl) {
					ParameterizedTypeImpl parameterizedType = (ParameterizedTypeImpl) genericInterface;
					Class<?> rawType = parameterizedType.getRawType();
					if(targetInterface.isAssignableFrom(rawType)) {
						return (Class) parameterizedType.getActualTypeArguments()[index];
					}
				} else if(genericInterface instanceof Class) {
					Class entityType = findGenericType((Class) genericInterface, targetInterface, index);
					if(entityType != null) {
						return entityType;
					}
				}
			}

			currentClass = currentClass.getSuperclass();
		}

		return null;
	}

	private ReflectionUtil() {}
}
