package com.leftstache.spring.rest;

import com.fasterxml.jackson.databind.*;
import org.springframework.boot.autoconfigure.condition.*;
import org.springframework.context.annotation.*;

/**
 * @author Joel Johnson
 */
@Configuration
@ComponentScan("com.leftstache.spring.rest")
public class SpringRestAutoConfigure {
	@Bean
	@ConditionalOnMissingBean
	public ObjectMapper objectMapper() {
		return new ObjectMapper();
	}
}
