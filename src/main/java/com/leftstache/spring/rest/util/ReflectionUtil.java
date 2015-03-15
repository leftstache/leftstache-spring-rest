package com.leftstache.spring.rest.util;

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
				if (genericInterface.getTypeName().startsWith(targetInterface.getName() + "<")) {
					if (genericInterface instanceof ParameterizedType) {
						return (Class) ((ParameterizedType) genericInterface).getActualTypeArguments()[index];
					}
					break;
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
