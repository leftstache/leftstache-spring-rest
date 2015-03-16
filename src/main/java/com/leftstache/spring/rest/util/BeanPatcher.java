package com.leftstache.spring.rest.util;

import com.fasterxml.jackson.databind.*;
import org.springframework.beans.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.data.repository.*;
import org.springframework.stereotype.*;

import java.beans.*;
import java.io.*;
import java.lang.reflect.*;
import java.util.*;

/**
 * @author Joel Johnson
 */
@Component
public class BeanPatcher {
	private final ObjectMapper objectMapper;

	@Autowired
	public BeanPatcher(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}

	/**
	 * Updates the entity for the given ID and applies all fields mapped in the changes map.
	 * @param entity 	The entity object to have the changes applied to.
	 * @param changes 	The changes to be applied. All fields will be converted to the proper type using the autowired ObjectMapper.
	 * @param <ENTITY>  The type of object being patched.
	 * @throws PropertyException 	Thrown if there's a problem with the entity. For example, the setter throws an exception.
	 * @throws InputException		Thrown if there's a problem with a property. For example, the setter for the field take one type, and the value is incompatible with that type.
	 */
	public <ENTITY> void patch(ENTITY entity, Map<String, Object> changes) throws PropertyException, InputException {
		applyPatch(entity, changes);
	}

	private <ENTITY> void applyPatch(ENTITY entity, Map<String, Object> patch) throws PropertyException, InputException {
		for (Map.Entry<String, Object> entry : patch.entrySet()) {
			String fieldName = entry.getKey();
			Object rawValue = entry.getValue();

			PropertyDescriptor property = BeanUtils.getPropertyDescriptor(entity.getClass(), fieldName);
			Method setter = property.getWriteMethod();
			if(setter == null) {
				continue;
			}

			Class<?> propertyType = property.getPropertyType();
			Object propertyValue;

			try {
				propertyValue = objectMapper.convertValue(rawValue, propertyType);
			} catch (IllegalArgumentException e) {
				throw new InputException("Unable to convert field " + fieldName + " to " + propertyType, e);
			}

			try {
				setter.setAccessible(true);
				setter.invoke(propertyValue);
			} catch (IllegalAccessException | InvocationTargetException e) {
				throw new PropertyException("Unable to set field " + fieldName, e);
			}
		}
	}

	public static class InputException extends java.lang.Exception {
		public InputException(String message, Throwable cause) {
			super(message, cause);
		}
	}

	public static class PropertyException extends java.lang.Exception {
		public PropertyException(String message, Throwable cause) {
			super(message, cause);
		}
	}
}
