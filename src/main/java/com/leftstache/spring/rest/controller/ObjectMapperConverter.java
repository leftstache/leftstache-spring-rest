package com.leftstache.spring.rest.controller;

import com.fasterxml.jackson.databind.*;
import org.springframework.beans.factory.annotation.*;

/**
 * @author Joel Johnson
 */
public class ObjectMapperConverter implements Converter {
	private final ObjectMapper objectMapper;

	@Autowired
	public ObjectMapperConverter(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}


	@Override
	public <R, P> R convertTo(Class<? extends R> to, P from) {
		return objectMapper.convertValue(from, to);
	}
}
