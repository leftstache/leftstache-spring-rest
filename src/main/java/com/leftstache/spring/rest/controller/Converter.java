package com.leftstache.spring.rest.controller;

import java.io.*;

/**
 * @author Joel Johnson
 */
public interface Converter {
	public <R, P> R convertTo(Class<? extends R> to, P from);
}
