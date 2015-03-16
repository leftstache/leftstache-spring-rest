package com.leftstache.spring.rest.core;

import java.io.*;

/**
 * @author Joel Johnson
 */
public interface GetLogic<ENTITY, ID extends Serializable> extends Restful<ENTITY, ID> {
	/**
	 * Defines in a service the method used to get an {@link ENTITY} with the given id.
	 * Implementing this method will result in this method being called instead of the repository.
	 *
	 * @param id The id of the entity to fetch.
	 * @return The entity as it exists in the database.
	 */
	ENTITY get(ID id);
}
