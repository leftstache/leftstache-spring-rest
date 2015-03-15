package com.leftstache.spring.rest.core;

/**
 * @author Joel Johnson
 */
public interface CreateLogic<T> extends RestfulEntity<T> {
	/**
	 * When a class with the {@link org.springframework.stereotype.Service} annotation implements this method
	 * it will be called instead of the save method on the repository. Allowing for additional bizLogic to
	 * take place.
	 *
	 * @param object The object to be persisted. It is expected that the implementation of this method saves this object.
	 * @return The final state of the persisted object. This will be returned to the API after it is called.
	 */
	T createLogic(T object);
}
