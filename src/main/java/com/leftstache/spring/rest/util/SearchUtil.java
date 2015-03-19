package com.leftstache.spring.rest.util;

import org.springframework.data.jpa.domain.*;
import org.springframework.stereotype.*;

import javax.persistence.criteria.*;
import java.util.*;

/**
 * @author Joel Johnson
 */
public final class SearchUtil {
	public static Specification generateSearchQuery(final Map<String, Object> queryMap) {
		Specification specification = new DynamicSearchSpecification(queryMap);
		return specification;
	}

	private static class DynamicSearchSpecification implements Specification {
		private final Map<String, Object> queryMap;

		public DynamicSearchSpecification(Map<String, Object> queryMap) {
			this.queryMap = queryMap;
		}

		@Override
		public Predicate toPredicate(Root root, CriteriaQuery query, CriteriaBuilder cb) {
			Predicate lastPredicate = null;
			for (Map.Entry<String, Object> entry : queryMap.entrySet()) {
				String key = entry.getKey();
				Object value = entry.getValue();

				Predicate currentPredicate;
				Path variable = root.get(key);
				if(value == null) {
					currentPredicate = cb.isNotNull(variable);
				} else {
					currentPredicate = cb.equal(variable, value);
				}

				if(lastPredicate == null) {
					lastPredicate = currentPredicate;
				} else if(currentPredicate != null) {
					lastPredicate = cb.and(lastPredicate, currentPredicate);
				}
			}
			return lastPredicate;
		}
	}

	private SearchUtil() {}
}
