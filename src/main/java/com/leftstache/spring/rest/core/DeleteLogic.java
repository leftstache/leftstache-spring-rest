package com.leftstache.spring.rest.core;

import java.io.*;

/**
 * @author Joel Johnson
 */
public interface DeleteLogic<ENTITY, ID extends Serializable> extends Restful<ENTITY, ID> {
	/**
	 * Defines logic of how an entity of type {@link ENTITY} should be deleted.
	 *
	 * Common usecases is to mark the item as deleted as opposed to actually removing the item from the DB.
	 *
	 * @param id The id of the entity to be deleted.
	 */
	void delete(ID id);
}
