package com.leftstache.spring.rest.core;

import java.io.*;

/**
 * @author Joel Johnson
 */
public interface CreateLogic<ENTITY, ID extends Serializable> extends Restful<ENTITY, ID> {
	/**
	 * When a class with the {@link org.springframework.stereotype.Service} annotation implements this method
	 * it will be called instead of the save method on the repository. Allowing for additional bizLogic to
	 * take place.
	 *
	 * If this method returns without an exception, it assumes an instance was created and a CREATED status code will be returned.
	 *
	 * @param object The object to be persisted. It is expected that the implementation of this method saves this object.
	 * @return The final state of the persisted object. This will be returned to the API after it is called.
	 */
	ENTITY create(ENTITY object);
}
