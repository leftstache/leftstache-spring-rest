package com.leftstache.spring.rest.core;

import java.io.*;

/**
 * @author Joel Johnson
 */
public interface GetLogic<ENTITY, ID extends Serializable> extends Restful<ENTITY, ID> {
	ENTITY get(ID id);
}
