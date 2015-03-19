package com.leftstache.spring.rest.util;

import org.springframework.data.jpa.domain.*;

import javax.persistence.criteria.*;
import java.util.*;

/**
 * @author Joel Johnson
 */
public final class SearchUtil {

	public static final String OR_KEY = "$or";

	public static final String EQUAL_KEY = "$eq";
	public static final String NOT_EQUAL_KEY = "$ne";
	public static final String GREATER_THAN_KEY = "$gt";
	public static final String LESS_THAN_KEY = "$lt";
	public static final String GREATER_THAN_EQUAL_KEY = "$gte";
	public static final String LESS_THAN_EQUAL_KEY = "$lte";

	public static Specification generateSearchQuery(final Map<String, Object> queryMap) {
		Specification specification = new DynamicSearchSpecification(queryMap);
		return specification;
	}

	private static Predicate toPredicate(Root root, CriteriaQuery query, CriteriaBuilder cb, Map<String, Object> queryMap) {
		Predicate lastPredicate = null;
		for (Map.Entry<String, Object> entry : queryMap.entrySet()) {
			String key = entry.getKey();
			Object value = entry.getValue();

			if(OR_KEY.equals(key)) {
				if(value instanceof List) {
					Predicate orPredicate = null;
					for (Object v : (List)value) {
						if(!(v instanceof Map)) {
							throw new UnsupportedOperationException("Expected object in " + OR_KEY + " list, but was " + v);
						}
						Predicate currentPredicate = toPredicate(root, query, cb, (Map<String, Object>) v);
						if(orPredicate != null) {
							orPredicate = cb.or(orPredicate, currentPredicate);
						} else {
							orPredicate = currentPredicate;
						}
					}
					if(lastPredicate != null) {
						lastPredicate = cb.and(lastPredicate, orPredicate);
					} else {
						lastPredicate = orPredicate;
					}
				} else {
					throw new QueryException(OR_KEY + " should reference a list");
				}
			} else if(value instanceof List) {
				throw new QueryException("List comparisons are not currently supported");
			} else {
				Predicate currentPredicate = null;
				if(value instanceof Map) {
					Map<String, Object> nestedMap = (Map) value;
					for (Map.Entry<String, Object> nestedEntry : nestedMap.entrySet()) {
						String nestedKey = nestedEntry.getKey();
						Object nestedValue = nestedEntry.getValue();

						Predicate comparePredicate;
						if(EQUAL_KEY.equals(nestedKey)) {
							if(nestedValue == null) {
								comparePredicate = cb.isNull(root.get(key));
							} else {
								comparePredicate = cb.equal(root.get(key), nestedValue);
							}
						} else if(NOT_EQUAL_KEY.equals(nestedKey)) {
							if(nestedValue == null) {
								comparePredicate = cb.isNotNull(root.get(key));
							} else {
								comparePredicate = cb.notEqual(root.get(key), nestedValue);
							}
						} else if(LESS_THAN_KEY.equals(nestedKey)) {
							Expression<Comparable> path = root.get(key);
							comparePredicate = cb.lessThan(path, (Comparable) nestedValue);
						} else if(GREATER_THAN_KEY.equals(nestedKey)) {
							Expression<Comparable> path = root.get(key);
							comparePredicate = cb.greaterThan(path, (Comparable) nestedValue);
						} else if(LESS_THAN_EQUAL_KEY.equals(nestedKey)) {
							Expression<Comparable> path = root.get(key);
							comparePredicate = cb.lessThanOrEqualTo(path, (Comparable) nestedValue);
						} else if(GREATER_THAN_EQUAL_KEY.equals(nestedKey)){
							Expression<Comparable> path = root.get(key);
							comparePredicate = cb.greaterThanOrEqualTo(path, (Comparable)nestedValue);
						} else {
							throw new QueryException("Unsupported: " + nestedKey);
						}

						if(currentPredicate == null) {
							currentPredicate = comparePredicate;
						} else {
							currentPredicate = cb.and(currentPredicate, comparePredicate);
						}
					}
				} else {
					if(value == null) {
						currentPredicate = cb.isNull(root.get(key));
					} else {
						currentPredicate = cb.equal(root.get(key), value);
					}
				}

				if (lastPredicate != null) {
					lastPredicate = cb.and(lastPredicate, currentPredicate);
				} else {
					lastPredicate = currentPredicate;
				}
			}
		}
		return lastPredicate;
	}

	private static class DynamicSearchSpecification implements Specification {
		private final Map<String, Object> queryMap;

		public DynamicSearchSpecification(Map<String, Object> queryMap) {
			this.queryMap = queryMap;
		}

		@Override
		public Predicate toPredicate(Root root, CriteriaQuery query, CriteriaBuilder cb) {
			return SearchUtil.toPredicate(root, query, cb, queryMap);
		}
	}

	public static class QueryException extends RuntimeException {
		public QueryException(String message) {
			super(message);
		}
	}

	private SearchUtil() {}
}
