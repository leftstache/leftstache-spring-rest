package com.leftstache.spring.rest.util;

import java.lang.reflect.*;
import java.util.*;

/**
 * @author Joel Johnson
 */
public final class ReflectionUtil {

	public static Class findEntityType(Class startClass, Class targetInterface) {
		Class currentClass = startClass;
		while(currentClass != null && currentClass != Object.class) {
			Type[] genericInterfaces = currentClass.getGenericInterfaces();
			for (Type genericInterface : genericInterfaces) {
				if (genericInterface.getTypeName().startsWith(targetInterface.getName() + "<")) {
					if (genericInterface instanceof ParameterizedType) {
						return (Class) ((ParameterizedType) genericInterface).getActualTypeArguments()[0];
					}
					break;
				} else if(genericInterface instanceof Class) {
					Class entityType = findEntityType((Class) genericInterface, targetInterface);
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
