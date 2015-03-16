package com.leftstache.spring.rest.core;

import java.io.*;
import java.util.*;

/**
 * @author Joel Johnson
 */
public interface PatchLogic<ENTITY, ID extends Serializable> extends Restful<ENTITY, ID> {
	/**
	 * When a class with the {@link org.springframework.stereotype.Service} annotation implements this method
	 * it will be called instead of the repostory save message when the patch logic is invoked in the controller.
	 *
	 * Implement this method to manually apply the patch or run other biz logic at the time of the patch.
	 *
	 * To easily apply the patch, call {@link com.leftstache.spring.rest.util.BeanPatcher#patch(Object, java.util.Map)}.
	 * This will apply the patch without saving the results.
	 *
	 * @param id The id of the object that should be updated.
	 * @param changes A map of changes to be applied to the entity.
	 * @return The final state of the persisted object. This will be returned to the API after it is called.
	 */
	ENTITY patch(ID id, Map<String, Object> changes);
}
